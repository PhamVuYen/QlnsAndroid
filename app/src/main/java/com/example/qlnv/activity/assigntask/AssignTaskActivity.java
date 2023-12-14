package com.example.qlnv.activity.assigntask;

import static java.sql.Types.NULL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.R;
import com.example.qlnv.activity.HomeActivity;
import com.example.qlnv.activity.manageuser.AddEmployeeActivity;
import com.example.qlnv.activity.manageuser.ManageUserActivity;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;
import com.example.qlnv.model.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class AssignTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText edtTaskName;
    private ImageView imgBack;
    private Button task_assigned_btn;
    public String task_id_unique;
    private TextView tvHourEnd, tvDateEnd;
    private Spinner spinner;
    private String[] separated = new String[2];
    private Employee employee = null;
    private ArrayList<Employee> arrayList = new ArrayList<>();
    private String ID = "";
    ArrayAdapter<String> adapter;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        edtTaskName = findViewById(R.id.task_assigned_name);
        task_assigned_btn = findViewById(R.id.task_assigned_btn);
        tvHourEnd = findViewById(R.id.tvHourEnd);
        tvDateEnd = findViewById(R.id.tvDateEnd);
        spinner = findViewById(R.id.spinner1);
        imgBack = findViewById(R.id.imgBack);
        employee = Injector.getEmployee();
        ID = employee.getId();
        arrayList = (ArrayList<Employee>) getIntent().getSerializableExtra("listuser");
        task_id_unique = UUID.randomUUID().toString();
        task_assigned_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvHourEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(
                        AssignTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hour = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay) ;
                        String minutes = minute < 10 ? "0" + minute : String.valueOf(minute);
                        tvHourEnd.setText(hour + ":" + minutes);
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AssignTaskActivity.this,
                        AssignTaskActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        for (int i = 0; i < arrayList.size(); i++) {
            String value = "";
            if (!arrayList.get(i).getId().equals(ID)) {
                value = arrayList.get(i).getName() + "-" + arrayList.get(i).getId();
                items.add(value);
            }
        }

        items = getDataSpinner();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = (String) parent.getItemAtPosition(pos);
                separated = ((String) item).split("-");
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void updateTask() {
        if (edtTaskName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Task name can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (tvDateEnd.getText().toString().isEmpty() || tvHourEnd.getText().toString().isEmpty()) {
                Toast.makeText(this, "You must select time end task", Toast.LENGTH_SHORT).show();
            } else {
                String time = tvDateEnd.getText().toString() + " " + tvHourEnd.getText().toString();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date deadline = null;
                try {
                    deadline = df.parse(time);
                    Log.d("item", deadline + "||" + new Date());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Task task = new Task();
                task.setTask_id(task_id_unique);
                task.setTask_name(edtTaskName.getText().toString());
                task.setManage_id(Injector.getEmployee().getId());
                task.setCreateDay(Injector.dateToString(new Date()));
                task.setDeadline(time);
                task.setUser_id(separated[1]);
                task.setTask_status("Chưa xong");
                addTaskToDB(task);
            }

        }
    }

    private void addTaskToDB(Task task) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_ASSIGN_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);
                        Toast.makeText(AssignTaskActivity.this, "Task assigned successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("MaCViec", task.getTask_id());
                param.put("TenCViec", task.getTask_name());
                param.put("DealineCV", task.getDeadline());
                param.put("CreateDate", task.getCreateDay());
                param.put("MaNV", task.getUser_id());
                param.put("Status", task.getTask_status());
                param.put("CreateBy", ID);
                return param;
            }
        };
        requestQueue.add(stringRequest);

//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Injector.URL_QUERY_TASK,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response != null) {
//                            try{
//                                String TenCViec = "";
//                                String createDay = "";
//                                String DealineCV = "";
//                                JSONObject jsonObject1 = new JSONObject(response);
//                                for (int i = 0; i < jsonObject1.length(); i++) {
//                                    JSONObject jsonObject = jsonObject1.getJSONObject(String.valueOf(i));
//                                     TenCViec = jsonObject.getString("TenCViec");
//                                     createDay = jsonObject.getString("createDay");
//                                     DealineCV = jsonObject.getString("DealineCV");
//                                }
//                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//                               Notification notification = new NotificationCompat.Builder(getApplicationContext(), notificationTask.CHANNEL_ID)
//                                       .setContentTitle("Công việc: " + TenCViec)
//                                       .setContentText("Thời gian tạo: "  + createDay + "\t" + "Hạn hoàn thành: " + DealineCV)
//                                       .setLargeIcon(bitmap)
//                                       .build();
//                            }catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(stringRequest1);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int mMonth = month + 1;
        String sDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        String sMonth =  mMonth < 10 ? "0" + mMonth : String.valueOf(mMonth);
        tvDateEnd.setText(sDay + "-" + sMonth  + "-" + year);
    }

    public ArrayList<String> getDataSpinner() {
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(items);
        items.clear();
        items.addAll(hashSet);
        return items;
    }

    @Override
    public void onBackPressed() {
        items.clear();
        super.onBackPressed();
    }

}
