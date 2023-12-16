package com.example.qlnv.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlnv.Injector;
import com.example.qlnv.R;
import com.example.qlnv.SharedPref;
import com.example.qlnv.activity.HomeActivity;
import com.example.qlnv.activity.LoginActivity;
import com.example.qlnv.model.Employee;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvChangePassword,tvLogout,tvAbsent,tvName,tvID,tvDoB,tvMail,tvMobile;
    Employee employee;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();
        employee = Injector.getEmployee();
        tvChangePassword.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvAbsent.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvID.setText(employee.getId());
        tvName.setText(employee.getName());
        tvDoB.setText(employee.getDateOfbirth()+"");
        tvMail.setText(employee.getEmail());
        tvMobile.setText(employee.getPhone());
    }

    private void initView() {
        tvChangePassword = findViewById(R.id.btnChangePassword);
        tvLogout = findViewById(R.id.btnLogOut);
        tvAbsent = findViewById(R.id.btnAbsent);
        tvName = findViewById(R.id.tvName);
        tvID = findViewById(R.id.tvID);
        tvDoB = findViewById(R.id.tvDoB);
        tvMail = findViewById(R.id.textView_show_gender);
        tvMobile = findViewById(R.id.textView_show_mobile);
        imgBack = findViewById(R.id.imgBack);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnChangePassword:
                startActivity(new Intent(AccountActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.btnLogOut:
                startActivity(new Intent(AccountActivity.this,LoginActivity.class));
                SharedPref sharedPref = SharedPref.getInstance();
                sharedPref.clearSharedPref(AccountActivity.this);
                Injector.clearEmployee();
                finish();
                break;
            case R.id.btnAbsent:
                startActivity(new Intent(AccountActivity.this,AbsentActivity.class));
                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }
}