package com.example.qlnv.activity.timekeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.qlnv.R;
import com.example.qlnv.activity.HomeActivity;
import com.example.qlnv.activity.LoginActivity;
import com.example.qlnv.activity.assigntask.AllTaskActivity;
import com.example.qlnv.adapter.AllTaskAdapter;
import com.example.qlnv.adapter.TimeKeepingAdapter;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.TimeKeeping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeKeepingDetailActivity extends AppCompatActivity {
    TimeKeepingAdapter timeKeepingAdapter;
    RecyclerView rvTimeKeeping;
    EditText edtSearch;
    ImageView imgFilter,imgBack;
    ArrayList<TimeKeeping> lists = new ArrayList<>();
    int lastPosition = -1;
    Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping_detail);
        rvTimeKeeping = findViewById(R.id.rvTimeKeeping);
        edtSearch = findViewById(R.id.edtSearch);
        imgFilter = findViewById(R.id.imgFilter);
        imgBack = findViewById(R.id.imgBack);
//        employee = getIntent().getParcelableExtra(HomeActivity.EMPLOYEE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        timeKeepingAdapter = new TimeKeepingAdapter(lists, TimeKeepingDetailActivity.this);
        Log.d("Injector.getEmployee().getRole()",Injector.getEmployee().getRole());
        if (Injector.getEmployee().getRole().equals("ADMIN")) {
            queryTimeKeepingAll();
            edtSearch.setVisibility(View.VISIBLE);
        } else {
            queryTimeKeepingOfUser();
            edtSearch.setVisibility(View.GONE);
        }


        rvTimeKeeping.setAdapter(timeKeepingAdapter);
        rvTimeKeeping.setLayoutManager(new LinearLayoutManager(TimeKeepingDetailActivity.this));
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               timeKeepingAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeKeepingDetailActivity.this);

                alertDialog.setTitle("Chọn tháng");
                final String[] listItems = new String[]{
                        "All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
                };

                alertDialog.setSingleChoiceItems(listItems, lastPosition, (dialog, which) -> {
                    lastPosition = which;
                });

                // set the negative button if the user is not interested to select or change already selected item
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

                alertDialog.setPositiveButton("Ok", (dialog, which) -> {
                    Log.d("which", lastPosition + "");
                    filterMonth(listItems[lastPosition]);
                    dialog.dismiss();
                });

                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }

        });
    }


    void filterMonth(String text) {
        Log.d("text",text);
        lists.clear();
        timeKeepingAdapter.notifyDataSetChanged();
        if (text.equals("All")) {
            if (Injector.getEmployee().getRole().equals("ADMIN")) {
                queryTimeKeepingAll();
            } else {
                queryTimeKeepingOfUser();
            }
//            queryTimeKeepingAll();
        } else {
            if (Injector.getEmployee().getRole().equals("ADMIN")) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_SEARCH_CHAM_CONG, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d("responseGetMonth", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length() == 0) {
                                    Toast.makeText(TimeKeepingDetailActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String GioDen = jsonObject.getString("GioDen");
                                        String GioVe = jsonObject.getString("GioVe");
                                        String Ngay = jsonObject.getString("Ngay");
                                        String ID = jsonObject.getString("MaNV");
                                        String month = jsonObject.getString("Thang");
                                        TimeKeeping timeKeeping = new TimeKeeping();
                                        timeKeeping.setIdNv(Injector.getEmployee().getId());
                                        timeKeeping.setCheckIn(GioDen);
                                        timeKeeping.setCheckOut(GioVe);
                                        timeKeeping.setDay(Ngay);
                                        timeKeeping.setIdNv(ID);
                                        timeKeeping.setMonth(month);
                                        lists.add(timeKeeping);
                                        timeKeepingAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(TimeKeepingDetailActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                        param.put("Search", text);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            } else {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_SEARCH_CHAM_CONG_USER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d("responseGetMonth", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length() == 0) {
                                    Toast.makeText(TimeKeepingDetailActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String GioDen = jsonObject.getString("GioDen");
                                        String GioVe = jsonObject.getString("GioVe");
                                        String Ngay = jsonObject.getString("Ngay");
                                        String ID = jsonObject.getString("MaNV");
                                        String month = jsonObject.getString("Thang");
                                        TimeKeeping timeKeeping = new TimeKeeping();
                                        timeKeeping.setIdNv(Injector.getEmployee().getId());
                                        timeKeeping.setCheckIn(GioDen);
                                        timeKeeping.setCheckOut(GioVe);
                                        timeKeeping.setDay(Ngay);
                                        timeKeeping.setIdNv(ID);
                                        timeKeeping.setMonth(month);
                                        lists.add(timeKeeping);
                                        timeKeepingAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(TimeKeepingDetailActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                        param.put("Search", text);
                        param.put("MaNV", Injector.getEmployee().getId());
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }


        }
    }

    private void queryTimeKeeping() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_GET_CHAM_CONG_NV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseGet1", response);
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(TimeKeepingDetailActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String GioDen = jsonObject.getString("GioDen");
                                String GioVe = jsonObject.getString("GioVe");
                                String Ngay = jsonObject.getString("Ngay");
                                String ID = jsonObject.getString("MaNV");
                                String month = jsonObject.getString("Thang");
                                TimeKeeping timeKeeping = new TimeKeeping();
                                timeKeeping.setIdNv(Injector.getEmployee().getId());
                                timeKeeping.setCheckIn(GioDen);
                                timeKeeping.setCheckOut(GioVe);
                                timeKeeping.setDay(Ngay);
                                timeKeeping.setIdNv(ID);
                                timeKeeping.setMonth(month);
                                lists.add(timeKeeping);
                                timeKeepingAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(TimeKeepingDetailActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("MaNV", Injector.getEmployee().getId());
                param.put("Thang", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void queryTimeKeepingOfUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_GET_CHAM_CONG_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseGet1", response);
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(TimeKeepingDetailActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String GioDen = jsonObject.getString("GioDen");
                                String GioVe = jsonObject.getString("GioVe");
                                String Ngay = jsonObject.getString("Ngay");
                                String ID = jsonObject.getString("MaNV");
                                String month = jsonObject.getString("Thang");
                                TimeKeeping timeKeeping = new TimeKeeping();
                                timeKeeping.setIdNv(Injector.getEmployee().getId());
                                timeKeeping.setCheckIn(GioDen);
                                timeKeeping.setCheckOut(GioVe);
                                timeKeeping.setDay(Ngay);
                                timeKeeping.setIdNv(ID);
                                timeKeeping.setMonth(month);
                                lists.add(timeKeeping);
                                timeKeepingAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(TimeKeepingDetailActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("MaNV", Injector.getEmployee().getId());
//                param.put("Thang", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }





    void queryTimeKeepingAll() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Injector.URL_GET_CHAM_CONG_NV_ALL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.d("responseAll", response + "");
                    if (response != null) {
                        if (response.length() == 0) {
                            Toast.makeText(TimeKeepingDetailActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < response.length() ; i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String GioDen = jsonObject.getString("GioDen");
                                    String GioVe = jsonObject.getString("GioVe");
                                    String date = jsonObject.getString("Ngay");
                                    String ID = jsonObject.getString("MaNV");
                                    String month = jsonObject.getString("Thang");
                                    TimeKeeping timeKeeping = new TimeKeeping();
                                    timeKeeping.setCheckIn(GioDen);
                                    timeKeeping.setCheckOut(GioVe);
                                    timeKeeping.setIdNv(ID);
                                    timeKeeping.setDay(date);
                                    timeKeeping.setMonth(month);
                                    lists.add(timeKeeping);
//                                    Log.d("sizeList", timeKeepingAdapter.list.size() + "");
                                    timeKeepingAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Log.e("error", e.getMessage());
                                }

                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimeKeepingDetailActivity.this, error + "", Toast.LENGTH_LONG).show();
                Log.d("response1", error + "");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}