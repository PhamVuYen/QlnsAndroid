package com.example.qlnv.activity.assigntask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.OnClickListener;
import com.example.qlnv.R;
import com.example.qlnv.adapter.TaskAdapter;

import com.example.qlnv.adapter.TaskNewAdapter;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {
    RecyclerView rvTask;
    LinearLayout layoutEmptyList;
    ImageView imgBack;
    Employee employee;
    ArrayList<Task> tasks = new ArrayList<>();
    TaskAdapter adapterTask;
    TaskNewAdapter adapter;
    List<Object> items = new ArrayList<>();
    List<Object> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initView();
        employee = Injector.getEmployee();
//        adapterTask = new TaskAdapter(tasks, TaskActivity.this);
//        adapterTask.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onItemLongClick(String id) {
//                Intent i = new Intent(TaskActivity.this,UpdateStatusTaskActivity.class);
//                i.putExtra("idTask",id);
//                startActivity(i);
//            }
//        });
        adapter = new TaskNewAdapter(displayList,TaskActivity.this);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onItemLongClick(String id) {
                Intent i = new Intent(TaskActivity.this,UpdateStatusTaskActivity.class);
                i.putExtra("idTask",id);
                startActivity(i);
            }
        });
        rvTask.setAdapter(adapter);
        rvTask.setLayoutManager(new LinearLayoutManager(TaskActivity.this));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapterTask.tasks.clear();
        adapter.items.clear();
        getTaskOfUser(employee);
    }

    void initView() {
        rvTask = findViewById(R.id.rv_task);
        layoutEmptyList = findViewById(R.id.layout_emptyTask);
        imgBack = findViewById(R.id.imgBack);
    }

    private void getTaskOfUser(Employee employee) {
        displayList.clear();
        tasks.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_QUERY_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseuser", response);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String TenCViec = jsonObject.getString("TenCViec");
                            String MaCViec = jsonObject.getString("MaCViec");
                            String userid = jsonObject.getString("MaNV");
                            String DealineCV = jsonObject.getString("DealineCV");
                            String createBy = jsonObject.getString("CreateBy");
                            String Status = jsonObject.getString("Status");
                            tasks.add(new Task(userid,MaCViec,TenCViec,Status,createBy,DealineCV,DealineCV));
//                            adapterTask.notifyDataSetChanged();
                        }
                        Collections.sort(tasks, new Comparator<Task>() {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                            @Override
                            public int compare(Task task1, Task task2) {
                                try {
                                    Date deadline1 = dateFormat.parse(task1.getDeadline());
                                    Date deadline2 = dateFormat.parse(task2.getDeadline());
                                    return deadline2.compareTo(deadline1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        });

                        // Nhóm công việc theo ngày
                        List<List<Task>> groupedTasks = new ArrayList<>();
                        for (Task task : tasks) {
                            boolean isGrouped = false;
                            for (List<Task> group : groupedTasks) {
                                if (group.size() > 0) {
                                    Task firstTask = group.get(0);
                                    if (isSameDate(firstTask.getDeadline(), task.getDeadline())) {
                                        group.add(task);
                                        isGrouped = true;
                                        break;
                                    }
                                }
                            }
                            if (!isGrouped) {
                                List<Task> newGroup = new ArrayList<>();
                                newGroup.add(task);
                                groupedTasks.add(newGroup);
                            }
                        }

                        // Tạo danh sách hiển thị cho RecyclerView


                        for (List<Task> group : groupedTasks) {
                            if (group.size() > 0) {
                                String deadline = group.get(0).getDeadline();
                                displayList.add(deadline);
                                displayList.addAll(group);
                            }
                        }

                        // Khởi tạo Adapter và đặt Adapter cho RecyclerView
                        adapter = new TaskNewAdapter(displayList,TaskActivity.this);
                        rvTask.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
//                        adapter.update(displayList);
                    } catch (JSONException e) {
                        Log.d("errParse",e.toString());
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
                param.put("MaNV", employee.getId());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private boolean isSameDate(String date1, String date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date deadline1 = dateFormat.parse(date1.substring(0, 10)); // Lấy 10 ký tự đầu tiên (ngày tháng)
            Date deadline2 = dateFormat.parse(date2.substring(0, 10));
            return deadline1.compareTo(deadline2) == 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}