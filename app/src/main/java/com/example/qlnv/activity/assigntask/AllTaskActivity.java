package com.example.qlnv.activity.assigntask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.OnClickListenerAllTask;
import com.example.qlnv.R;
import com.example.qlnv.adapter.AllTaskAdapter;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllTaskActivity extends AppCompatActivity {
    public static final int UPDATE_TASK = 1111;
    RecyclerView rvTask;
    AllTaskAdapter adapterAllTask;
    Button btnAssignTask;
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Employee> arrayList = new ArrayList<>();
    String id = "";
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks_details);
        initView();
        id = getIntent().getStringExtra("id");
        arrayList = (ArrayList<Employee>) getIntent().getSerializableExtra("listuser");
        adapterAllTask = new AllTaskAdapter(tasks, AllTaskActivity.this);
        adapterAllTask.setOnClickListener(new OnClickListenerAllTask() {
            @Override
            public void onItemLongClick(int id, int position, Task task) {
                switch (id) {
                    case R.id.removeTask:
                        removeTask(task.getTask_id(), position);
                        break;
                    case R.id.editTask:
                        Intent intent = new Intent(AllTaskActivity.this, UpdateTaskActivity.class);
                        intent.putExtra("listuser", arrayList);
                        intent.putExtra("idTask",task.getTask_id());
                        intent.putExtra("taskname",task.getTask_name());
                        intent.putExtra("deadline",task.getDeadline());
                        intent.putExtra("assignfor",task.getUser_id());
                        intent.putExtra("status",task.getTask_status());
                        intent.putExtra("position",position);
                        startActivity(intent);
                        break;
                }
            }
        });
        rvTask.setAdapter(adapterAllTask);
        rvTask.setLayoutManager(new LinearLayoutManager(AllTaskActivity.this));

        btnAssignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllTaskActivity.this, AssignTaskActivity.class);
                intent.putExtra("listuser", arrayList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterAllTask.tasks.clear();
        queryAllTask(id);
    }



    void removeTask(String idTask, int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_DELETE_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                adapterAllTask.tasks.remove(position);
                adapterAllTask.notifyDataSetChanged();
                Toast.makeText(AllTaskActivity.this, "Remove task successfully", Toast.LENGTH_SHORT).show();

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

    public void queryAllTask(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_QUERY_ALL_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if (response != null) {
                    try {
                        JSONObject jsonArray = new JSONObject(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
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
                                    updateStatusTask(MaCViec,Status);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            tasks.add(new Task(userid, MaCViec, TenCViec, Status, id, DeadlineCV, DeadlineCV));
                            adapterAllTask.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.e("err",e.getMessage());
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

    void updateStatusTask(String id,String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_STATUS_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response",response);
//                        finish();
                    } catch (Exception e) {
                        Toast.makeText(AllTaskActivity.this, "Some error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err",error+"");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("MaCViec", id);
                param.put("Status",status);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void initView() {
        rvTask = findViewById(R.id.rv_task);
        btnAssignTask = findViewById(R.id.assign_new_task);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //lấy kết quả sửa nhân viên thành công
        if (resultCode == AllTaskActivity.UPDATE_TASK) {
            Bundle b = data.getBundleExtra("DATA");
            Task task = (Task) b.getSerializable("task");
            int position = b.getInt("position");
            adapterAllTask.tasks.set(position, task);
            adapterAllTask.notifyDataSetChanged();
        }

    }


}
