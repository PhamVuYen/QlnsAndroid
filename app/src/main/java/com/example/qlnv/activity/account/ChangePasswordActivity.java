package com.example.qlnv.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.qlnv.model.Employee;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText edtOldPass, edtNewPass, edtNewPassAgain;
    MaterialButton btnSave;
    ImageView imgBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        initView();
        btnSave.setOnClickListener(new View.OnClickListener() { // update URL change password
            @Override
            public void onClick(View view) {
                Employee employee = Injector.getEmployee();
                Log.d("password",edtOldPass.getText().toString() + "|" +employee.getPassword());
                if (edtOldPass.getText().toString().equals(employee.getPassword())) {
                    if (edtNewPass.getText().toString().equals(edtNewPassAgain.getText().toString())) {
                        employee.setPassword(edtNewPass.getText().toString());
                        updatePasswordToDB(employee);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "New password isn't equal with confirm password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        edtOldPass = findViewById(R.id.tietPassword);
        edtNewPass = findViewById(R.id.tietPasswordNewPass);
        edtNewPassAgain = findViewById(R.id.tietPasswordConfirmNewPass);
        btnSave = findViewById(R.id.btnChangePassword);
        imgBack = findViewById(R.id.imgBack);
    }

    private void updatePasswordToDB(Employee nv) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);
                        Toast.makeText(ChangePasswordActivity.this, "Updating password is successful", Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception e) {
//                        Toast.makeText(ChangePasswordActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("Password", nv.getPassword());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
