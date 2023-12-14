package com.example.qlnv.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.accounts.Account;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.Preference;
import com.example.qlnv.R;
import com.example.qlnv.activity.account.AccountActivity;
import com.example.qlnv.activity.assigntask.AllTaskActivity;
import com.example.qlnv.activity.assigntask.AssignTaskActivity;
import com.example.qlnv.activity.assigntask.CalendarActivity;
import com.example.qlnv.activity.assigntask.TaskActivity;
import com.example.qlnv.activity.manageuser.EmployeeListActivity;
import com.example.qlnv.activity.manageuser.ManageUserActivity;
import com.example.qlnv.activity.summary.SummaryActivity;
import com.example.qlnv.activity.timekeeping.OverallActivity;
import com.example.qlnv.activity.timekeeping.TimeKeepingActivity;
import com.example.qlnv.activity.timekeeping.TimeKeepingDetailActivity;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Role;
import com.example.qlnv.model.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EMPLOYEE = "EMPLOYEE";
    ConstraintLayout timeKeeping, manageUser, assignTask, myTask, account, viewAllTask, summary;
    TextView tvUserName, tvRole;
    Employee employee;
    ArrayList<Employee> arrayList = new ArrayList<>();
    ArrayList<Room> arrRoom = new ArrayList<>();
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        timeKeeping.setOnClickListener(this);
        manageUser.setOnClickListener(this);
        assignTask.setOnClickListener(this);
        myTask.setOnClickListener(this);
        account.setOnClickListener(this);
        viewAllTask.setOnClickListener(this);
        summary.setOnClickListener(this);
        employee = Injector.getEmployee();
        tvUserName.setText(employee.getName() + "-" + employee.getId());
        tvRole.setText(employee.getRole());
        sharedPref = HomeActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("mTime", System.currentTimeMillis()).apply();
        getDataRoom();
        getUserInRoom(employee.getIdRoom());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void initView() {
        timeKeeping = findViewById(R.id.timekeeping);
        manageUser = findViewById(R.id.manageuser);
        assignTask = findViewById(R.id.assigntask);
        myTask = findViewById(R.id.mytask);
        account = findViewById(R.id.account);
        summary = findViewById(R.id.summary);
        viewAllTask = findViewById(R.id.viewAllTask);
        tvUserName = findViewById(R.id.tvUsernameHome);
        tvRole = findViewById(R.id.tvRole);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.timekeeping:
                Preference preferences = Preference.getInstance(HomeActivity.this);
                if (!preferences.isTimeToClick() && preferences.getIDUser().equals(Injector.getEmployee().getId())) {
                    Toast.makeText(HomeActivity.this, "You have checked out", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(HomeActivity.this, TimeKeepingActivity.class));
                }
                break;
            case R.id.manageuser:
                if (employee.getRole().equals("ADMIN")) {
                    startActivity(new Intent(HomeActivity.this, ManageUserActivity.class));
                } else if (employee.getRole().equals("Trưởng phòng")) {
                    Room room = new Room();
                    room.setId(employee.getIdRoom());
                    for (Room r : arrRoom) {
                        if (r.getId().equals(employee.getIdRoom())) {
                            room.setName(r.getName());
                        }
                    }
                    room.dsnv = arrayList;
                    Intent i = new Intent(this, EmployeeListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PHONGBAN", room);
                    i.putExtra("DATA", bundle);
                    startActivity(i);
                } else {
                    Toast.makeText(HomeActivity.this, "You don't have permission", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.assigntask:
                if (employee.getRole().equals("Trưởng phòng")) {
                    if (arrayList.size() > 0) {
                        Intent intent = new Intent(HomeActivity.this, AssignTaskActivity.class);
                        intent.putExtra("listuser", arrayList);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "You don't have permission", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mytask:
                if (!employee.getRole().equals("ADMIN")) {
                    startActivity(new Intent(HomeActivity.this, TaskActivity.class));
                } else {
                    Toast.makeText(this,"You don't have permission",Toast.LENGTH_LONG).show();
                }
//                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
//                intent.putExtra("id", employee.getId());
//                intent.putExtra("task", "MY_TASK");
//                startActivity(intent);
                break;
            case R.id.account:
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                break;
            case R.id.viewAllTask:
//                if (employee.getRole().equals("Trưởng phòng")) {
//                    if (arrayList.size() > 0) {
//                        Intent intent = new Intent(HomeActivity.this, AllTaskActivity.class);
//                        intent.putExtra("id", employee.getId());
//                        intent.putExtra("listuser", arrayList);
//                        startActivity(intent);
//                    }
//                } else {
//                    Toast.makeText(HomeActivity.this, "You don't have permission", Toast.LENGTH_LONG).show();
//                }
                if (employee.getRole().equals("Trưởng phòng")) {
                        Intent i = new Intent(HomeActivity.this, CalendarActivity.class);
                        i.putExtra("id", employee.getId());
                        i.putExtra("task", "ALL_TASK");
                        startActivity(i);
                } else {
                    Toast.makeText(HomeActivity.this, "You don't have permission", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.summary:
                if (Injector.getEmployee().getRole().equals("ADMIN")) {
                    Intent i = new Intent(HomeActivity.this, TimeKeepingDetailActivity.class);
                    i.putExtra(EMPLOYEE,employee);
                    startActivity(new Intent(HomeActivity.this, TimeKeepingDetailActivity.class));
                } else {
                    Intent i = new Intent(HomeActivity.this, OverallActivity.class);
                    i.putExtra(EMPLOYEE,employee);
//                    startActivity(new Intent(HomeActivity.this, TimeKeepingDetailActivity.class));
                    startActivity(i);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
        System.exit(0);  // exit app
    }

    void getDataRoom() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Injector.URL_ROOM, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String idRoom = jsonObject.getString("MaPB");
                            String nameRoom = jsonObject.getString("TenPB");
                            Room room = new Room();
                            room.setId(idRoom);
                            room.setName(nameRoom);
                            //room.dsnv
                            arrRoom.add(room);
                        } catch (Exception e) {
                            Toast.makeText(HomeActivity.this, "Some error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error + "");
                Toast.makeText(HomeActivity.this, error + "Can't connect server employee", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void getUserInRoom(String idroom) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_QUERY_USER_ROOM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        for (int i = 0; i < jsonObject1.length(); i++) {
                            JSONObject jsonObject = jsonObject1.getJSONObject(String.valueOf(i));
                            String mnv = jsonObject.getString("MaNV");
                            String name = jsonObject.getString("TenNV");
                            Date date = new Date();
                            String diachi = jsonObject.getString("DiaChi");
                            String gioitinh = jsonObject.getString("GioiTinh");
                            String phone = jsonObject.getString("Phone");
                            String email = jsonObject.getString("Email");
                            String cmnd = jsonObject.getString("SoCMND");
                            String stk = jsonObject.getString("SoTk");
                            String luong = jsonObject.getString("MucLuong");
                            String chucvu = jsonObject.getString("ChucVu");
                            Boolean sex = false;
                            if (gioitinh.equals("nam")) {
                                sex = true;
                            }
                            Employee employee = new Employee(mnv, name, date, diachi, sex, phone, email, cmnd, chucvu, idroom, stk, luong);
                            arrayList.add(employee);
                        }
                        Log.d("responseUser", response);
                    } catch (Exception e) {
//                        Toast.makeText(ManageUserActivity.this, "Fail to connect server employee in room", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("MaPB", idroom);
                return param;
            }
        };

        requestQueue.add(stringRequest);
    }


}