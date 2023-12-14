package com.example.qlnv.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlnv.ConnectionAsync;
import com.example.qlnv.Injector;
import com.example.qlnv.R;
import com.example.qlnv.SharedPref;
import com.example.qlnv.model.Employee;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialButton btnLogin;
    TextInputEditText edtTextUser, edtPassword;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPref sharedPref = SharedPref.getInstance();

        if (sharedPref.getUser(this) != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogin.setOnClickListener(this);
    }

    public void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        edtTextUser = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                try {
                    if (new ConnectionAsync().execute().get()) {
                        getData();
                      //  getLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, "Check connection again ", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Log.d("Injector.URL_USER",Injector.URL_USER+"");
  //              getLogin();
        }
    }

    void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Injector.URL_USER, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String msv = jsonObject.getString("MaNV");
                            String password = jsonObject.getString("Password");
                            String idRoom = jsonObject.getString("MaPB");
                            String role = jsonObject.getString("ChucVu");
                            String name = jsonObject.getString("TenNV");
                            String luong = jsonObject.getString("MucLuong");
                            String email = jsonObject.getString("Email");
                            String phone = jsonObject.getString("Phone");
                            if (edtTextUser.getText().toString().equals(msv) && edtPassword.getText().toString().equals(password)) {
                                Employee employee = Injector.getEmployee();
                                employee.setId(msv);
                                employee.setName(name);
                                employee.setPassword(password);
                                employee.setIdRoom(idRoom);
                                employee.setRole(role);
                                employee.setMucluong(luong);
                                employee.setEmail(email);
                                employee.setPhone(phone);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error + "", Toast.LENGTH_LONG).show();
                Log.d("response1", error + "");
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    void getLogin(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               String token = response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errssssss",error+"");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("MaNV",edtTextUser.getText().toString());
                param.put("Password",edtPassword.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

}
