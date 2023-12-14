package com.example.qlnv.activity.assigntask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.qlnv.activity.manageuser.EditEmployeeActivity;
import com.example.qlnv.activity.manageuser.ManageUserActivity;
import com.example.qlnv.adapter.AttachedFilesAdapter;
import com.example.qlnv.model.Employee;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStatusTaskActivity extends AppCompatActivity {
    private Spinner spinner;
    private Button btnUpdateTask;
    private TextView tvAttachFile;
    private RecyclerView attachedFilesRecyclerView;
    AttachedFilesAdapter attachedFilesAdapter;
    private String status = "";
    private List<Uri> attachedFiles = new ArrayList<>();
    String idTask = "";
    private static final int REQUEST_CODE_ATTACH_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status_task);
        initView();
        idTask = getIntent().getStringExtra("idTask");
        ArrayList<String> items = new ArrayList<>();
        items.add("Đang làm");
        items.add("Đã hoàn thành");
        items.add("Quá hạn");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        attachedFilesAdapter = new AttachedFilesAdapter(attachedFiles,this);
        attachedFilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attachedFilesRecyclerView.setAdapter(attachedFilesAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = (String) parent.getItemAtPosition(pos);
                status = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusTask(idTask, status);
            }
        });

        tvAttachFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if((event.getRawX() >= tvAttachFile.getRight() - tvAttachFile.getTotalPaddingRight())) {
                        showFileChooser();

                        return true;
                    }
                }
                return true;
            }
        });


    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent chooserIntent = Intent.createChooser(intent, "Select File");
        startActivityForResult(chooserIntent, REQUEST_CODE_ATTACH_FILE);
    }

    // onActivityResult method to handle the selected files
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ATTACH_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                attachedFiles.add(fileUri);
                attachedFilesAdapter.notifyDataSetChanged();
            } else if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    attachedFiles.add(fileUri);
                }
                attachedFilesAdapter.notifyDataSetChanged();
            }
        }
    }


    void updateStatusTask(String id, String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_STATUS_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(UpdateStatusTaskActivity.this, "Some error", Toast.LENGTH_LONG).show();
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


    void initView() {
        tvAttachFile = findViewById(R.id.tvAttachFile);
        attachedFilesRecyclerView = findViewById(R.id.rvFile);
        spinner = findViewById(R.id.spinner1);
        btnUpdateTask = findViewById(R.id.update_task_status);
    }
}