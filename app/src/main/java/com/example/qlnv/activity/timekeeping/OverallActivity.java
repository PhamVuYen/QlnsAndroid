package com.example.qlnv.activity.timekeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.qlnv.Preference;
import com.example.qlnv.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OverallActivity extends AppCompatActivity {
    TextView tvWorkDay,tvLateMinute,tvFine,tvSalary;
    Button btnDetail;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);
        initView();
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OverallActivity.this,TimeKeepingDetailActivity.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getInfoByID();
    }

    public void getInfoByID() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_GET_TIME_RECORER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseGet", response);
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray == null || jsonArray.length() == 0) {
                            Toast.makeText(OverallActivity.this, "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String NgayCong = jsonObject.getString("NgayCong");
                                String PhutDiMuon = jsonObject.getString("PhutDiMuon");
                                tvWorkDay.setText(NgayCong);
                                tvLateMinute.setText(PhutDiMuon);
                                tvFine.setText(Injector.getPunish(PhutDiMuon));
//                                int dayWork =Injector.countHolidays(Calendar.getInstance().get(Calendar.MONTH) + 1);
//                                double percent = Double.parseDouble(NgayCong)/Double.parseDouble(String.valueOf(dayWork));
//                                double salary = Integer.parseInt(Injector.getEmployee().getMucluong())*percent;
//                                int realSalary = (int) salary - Integer.parseInt(Injector.getPunish(PhutDiMuon));
//                                tvSalary.setText(getSalary);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(OverallActivity.this, "Some error" +e.toString(), Toast.LENGTH_LONG).show();
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
                Log.d("Month",String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                param.put("Thang", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        tvWorkDay = findViewById(R.id.tvWorkDay);
        tvLateMinute = findViewById(R.id.tvLateMinute);
        tvFine = findViewById(R.id.tvFine);
        tvSalary = findViewById(R.id.tvSalary);
        btnDetail = findViewById(R.id.btnDetail);
    }

}