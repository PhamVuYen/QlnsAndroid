package com.example.qlnv.activity.assigntask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.R;
import com.example.qlnv.CalendarUtils;
import com.example.qlnv.adapter.CalendarAdapter;
import com.example.qlnv.adapter.EventAdapter;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Event;
import com.example.qlnv.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    public static final int UPDATE_TASK = 1111;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ImageView imgBack;
    EventAdapter eventAdapter;
    CalendarAdapter calendarAdapter;
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Employee> arrayList = new ArrayList<>();
    ArrayList<Event> dailyEvents = new ArrayList<>();
    HashSet<LocalDate> localDates = new HashSet<>();
    String id = "";
    String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        id = getIntent().getStringExtra("id");
        check = getIntent().getStringExtra("task");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        arrayList = (ArrayList<Employee>) getIntent().getSerializableExtra("listuser");
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        imgBack = findViewById(R.id.imgBack);

    }

    @Override
    protected void onStart() {
        super.onStart();
        dailyEvents.clear();
        if (check.equals("ALL_TASK")) {
            queryAllTask(id);
        } else {
            getTaskOfUser(id);
        }
    }


    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = CalendarUtils.daysInMonthArray();
        calendarAdapter = new CalendarAdapter(daysInMonth, this,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setDailyEvents(dailyEvents);
        calendarAdapter.setLocalDates(localDates);
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
            setMonthView();
            eventAdapter = new EventAdapter(this, dailyEvents);
            eventListView.setAdapter(eventAdapter);
            eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return false;
                }
            });
        }
    }


    private void getTaskOfUser(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_QUERY_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String TenCViec = jsonObject.getString("TenCViec");
                            String MaCViec = jsonObject.getString("MaCViec");
                            String userid = jsonObject.getString("MaNV");
                            Log.d("userid",userid + "abcde");
                            String DeadlineCV = jsonObject.getString("DealineCV");
                            String createBy = jsonObject.getString("CreateBy");
                            String Status = jsonObject.getString("Status");
                            String[] parts = DeadlineCV.split(" ");
                            tasks.add(new Task(userid, MaCViec, TenCViec, Status, createBy, DeadlineCV, DeadlineCV));
                            Event newEvent = new Event(TenCViec, Injector.getLocalDateTask(parts[0]), Injector.getLocalTimeTask(parts[1]), Status,userid);
                            Event.eventsList.add(newEvent);
                            localDates.add(Injector.getLocalDateTask(parts[0]));
                            dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
                            eventAdapter = new EventAdapter(CalendarActivity.this, dailyEvents);
                            eventListView.setAdapter(eventAdapter);
                        }
                        setMonthView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("MaNV",id);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void queryAllTask(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_QUERY_ALL_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if (response != null) {
                    try {
                        JSONObject jsonArray = new JSONObject(response);
                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(String.valueOf(i));
                            String TenCViec = jsonObject.getString("TenCViec");
                            String MaCViec = jsonObject.getString("MaCViec");
                            String userid = jsonObject.getString("MaNV");
                            String DeadlineCV = jsonObject.getString("DealineCV");
                            String Status = jsonObject.getString("Status");
                            if (!Status.equals("Đã hoàn thành")) {
                                try {
                                    int status = Injector.compareDateWithCurrent(DeadlineCV);
                                    if (status > 0) {
                                        Status = "Đang làm";
                                    } else {
                                        Status = "Quá hạn";
                                    }
                                    updateStatusTask(MaCViec, Status);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            String[] parts = DeadlineCV.split(" ");
                            tasks.add(new Task(userid, MaCViec, TenCViec, Status, id, DeadlineCV, DeadlineCV));
                            Event newEvent = new Event(TenCViec, Injector.getLocalDateTask(parts[0]), Injector.getLocalTimeTask(parts[1]), Status,userid);
                            Event.eventsList.add(newEvent);
                            localDates.add(Injector.getLocalDateTask(parts[0]));
                            dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
                            eventAdapter = new EventAdapter(CalendarActivity.this, dailyEvents);
                            eventListView.setAdapter(eventAdapter);
                        }
                        setMonthView();
                    } catch (Exception e) {
                        Log.e("err", e.getMessage());
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
                param.put("CreateBy", id);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    void removeTask(String idTask, int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_DELETE_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
//                adapterAllTask.tasks.remove(position);
//                adapterAllTask.notifyDataSetChanged();
                Toast.makeText(CalendarActivity.this, "Remove task successfully", Toast.LENGTH_SHORT).show();
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
                param.put("MaCViec", idTask);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    void updateStatusTask(String id, String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_STATUS_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);
                    } catch (Exception e) {
                        Toast.makeText(CalendarActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("MaCViec", id);
                param.put("Status", status);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Event.eventsList.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}








