package com.example.qlnv.activity.assigntask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;
import com.example.qlnv.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText edtTaskName;
    private Button task_assigned_btn;
    public String task_id_unique;
    private TextView tvHourEnd, tvDateEnd;
    private Spinner spinnerName, spinnerStatus;
    private ArrayList<Employee> arrayList = new ArrayList<>();
    private ArrayAdapter<String> nameAdapter;
    private ArrayList<String> items = new ArrayList<>();
    private Task task;
    private String[] separated = new String[2];
    private Employee employee = null;
    private int position = 0;
    private String ID = "";
    private String idTask = "";
    private String status = "";
    private String deadline = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
        initView();

        employee = Injector.getEmployee();
        ID = employee.getId();


//        Intent i = getIntent();
//        Bundle b = i.getBundleExtra("DATA");
//        task = b.getSerializable()
        edtTaskName.setText(getIntent().getStringExtra("taskname"));
        position = getIntent().getIntExtra("position", 0);
        String deadline = getIntent().getStringExtra("deadline");
        idTask = getIntent().getStringExtra("idTask");



        String[] time  = deadline.split(" ",-1);
        tvDateEnd.setText(time[0]);
        tvHourEnd.setText(time[1]);


        arrayList = (ArrayList<Employee>) getIntent().getSerializableExtra("listuser");


        task_id_unique = UUID.randomUUID().toString();
        task_assigned_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask(idTask);
            }
        });

        tvHourEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(
                        UpdateTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvHourEnd.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateTaskActivity.this,
                        UpdateTaskActivity.this,
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


        nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerName.setAdapter(nameAdapter);
        spinnerName.setSelection(position);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = (String) parent.getItemAtPosition(pos);
                separated = ((String) item).split("-");
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> itemStatus = new ArrayList<>();
        itemStatus.add("Đang làm");
        itemStatus.add("Đã hoàn thành");
        itemStatus.add("Quá hạn");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemStatus);
        spinnerStatus.setAdapter(statusAdapter);
        spinnerStatus.setSelection(position);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = (String) parent.getItemAtPosition(pos);
                status = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView() {
        edtTaskName = findViewById(R.id.task_assigned_name);
        task_assigned_btn = findViewById(R.id.task_assigned_btn);
        tvHourEnd = findViewById(R.id.tvHourEnd);
        tvDateEnd = findViewById(R.id.tvDateEnd);
        spinnerName = findViewById(R.id.spinner1);
        spinnerStatus = findViewById(R.id.spinner2);
    }


    private void updateTask(String idTask) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);
                        finish();
                    } catch (Exception e) {
//                        Toast.makeText(UpdateTaskActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                String deadline = tvDateEnd.getText().toString() + " " + tvHourEnd.getText().toString();
                param.put("MaCViec", idTask);
                param.put("TenCViec", edtTaskName.getText().toString());
                param.put("Status", status);
                param.put("MaNV", separated[1]);
                param.put("DealineCV", deadline);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}