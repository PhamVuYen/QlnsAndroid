package com.example.qlnv.activity.manageuser;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.Injector;
import com.example.qlnv.R;
import com.example.qlnv.model.Role;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * màn hình sẽ hiển thị danh sách nhân viên vào 2 ListView khác nhau
 * ListView 1 dùng Radio để chọn trưởng phòng
 * ListView 2 dùng Checkbox để chọn phó phòng
 *
 * @author drthanh
 */
public class SetManagerActivity extends Activity {

    ListView lvManager, lvSubManager;
    TextView tvTittle;
    ArrayList<Employee> arrNvForTP = new ArrayList<Employee>();
    ArrayList<Employee> arrNvForPP = new ArrayList<Employee>();
    ArrayAdapter<Employee> adapterForTP;
    ArrayAdapter<Employee> adapterForPP;
    TextView btnApply;
    int lastChecked = -1;
    Room pb = null;
    Employee employee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap_truong_phong);
        getFormWidgets();

    }


    public void getFormWidgets() {
        tvTittle = findViewById(R.id.textView1);
        lvManager = (ListView) findViewById(R.id.lvtruongphong);
        lvSubManager = findViewById(R.id.lvphophong);

        lvManager.setTextFilterEnabled(true);
        lvManager.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvManager.setOnItemClickListener(new OnItemClickListener() {
            boolean somethingChecked = false;

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                arrNvForTP.get(arg2).setRole("Trưởng phòng");
                if (somethingChecked) {
                    CheckedTextView cv = (CheckedTextView) arg1;
                    cv.setChecked(false);
                }
                CheckedTextView cv = (CheckedTextView) arg1;
                if (!cv.isChecked()) {
                    cv.setChecked(true);
                    arrNvForTP.get(arg2).setRole("Trưởng phòng");
                } else {
                    arrNvForTP.get(arg2).setRole("Nhân viên");
                }
                lastChecked = arg2;
                somethingChecked = true;
            }

        });

        adapterForTP = new ArrayAdapter<Employee>(this, android.R.layout.simple_list_item_single_choice, arrNvForTP);
        lvManager.setAdapter(adapterForTP);

        adapterForPP = new ArrayAdapter<Employee>(this, android.R.layout.simple_list_item_multiple_choice, arrNvForPP);
        lvSubManager.setAdapter(adapterForPP);
//        lvSubManager.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CheckedTextView cv = (CheckedTextView) view;
//                if (!cv.isChecked()) {
//                    cv.setChecked(true);
//                    arrNvForPP.get(i).setRole(Role.SubManager);
//                } else {
//                    cv.setChecked(false);
//                    arrNvForPP.get(i).setRole(Role.Employee);
//                }
//            }
//        });


        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("DATA");
        pb = (Room) bundle.getSerializable("PHONGBAN");
        if (pb.dsnv != null) {
            for (int index = 0; index < pb.dsnv.size(); index++) {
                if (pb.dsnv.get(index).getRole().equals("Trưởng phòng")) {
                    employee = pb.dsnv.get(index);
                }
            }
        }
        addNvToListTP(pb);
        addNvToListPP(pb);
        adapterForTP.notifyDataSetChanged();
        adapterForPP.notifyDataSetChanged();
        btnApply = (TextView) findViewById(R.id.imgapply);
        btnApply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                doApply();

            }
        });

        tvTittle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= tvTittle.getCompoundPaddingLeft()) {
                        finish();

                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void doApply() {
        if (employee != null) {
            resetTPToDB(employee);
        }
        if (pb.dsnv != null) {
            Employee employee = pb.dsnv.get(lastChecked);
            updateTPToDB(employee);
        }

    }

    private void resetTPToDB(Employee nv) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_EDIT_LANHDAO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseResetTPToDB", response);
                    } catch (Exception e) {
//                        Toast.makeText(SetManagerActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("MaNV", nv.getId());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateTPToDB(Employee nv) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_INSERT_LANHDAO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("updateTPToDB", response);
                        Intent i = getIntent();
                        Bundle bundle = new Bundle();
                        for (int index = 0; index < pb.dsnv.size(); index++) {
                            Employee employee = pb.dsnv.get(index);
                            if (index == lastChecked) {
                                employee.setRole("Trưởng phòng");
                            } else {
                                employee.setRole("Nhân viên");
                            }
                            pb.dsnv.set(index, employee);
                        }
                        bundle.putSerializable("PHONGBAN", pb);
                        i.putExtra("DATA", bundle);
                        setResult(ManageUserActivity.THIET_LAP_TP_PP_THANHCONG, i);
                        finish();
                    } catch (Exception e) {
//                        Toast.makeText(SetManagerActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("MaNV", nv.getId());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void addNvToListTP(Room pb) {
        arrNvForTP.clear();
        for (Employee nv : pb.getListNhanVien()) {
            arrNvForTP.add(nv);
        }
    }

    public void addNvToListPP(Room pb) {
        arrNvForPP.clear();
        for (Employee nv : pb.getListNhanVien()) {
            arrNvForPP.add(nv);
        }
    }

}
