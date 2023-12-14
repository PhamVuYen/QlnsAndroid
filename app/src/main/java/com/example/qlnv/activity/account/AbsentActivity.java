package com.example.qlnv.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qlnv.R;
import com.example.qlnv.activity.assigntask.AssignTaskActivity;

import java.util.Calendar;

public class AbsentActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    TextView tvFromTime,tvToTime;
    EditText edtReason;
    Button btnSend;
    DatePickerDialog dialogFromTime,dialogToTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);
        initView();
    }

    private void initView() {
        tvFromTime = findViewById(R.id.tvFromTime);
        tvToTime = findViewById(R.id.tvToTime);
        edtReason = findViewById(R.id.edtReason);
        btnSend = findViewById(R.id.tvConfirm);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvFromTime:
                dialogFromTime = new DatePickerDialog(
                        AbsentActivity.this,
                        AbsentActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialogFromTime.show();
                break;
            case R.id.tvToTime:
                dialogToTime = new DatePickerDialog(
                        AbsentActivity.this,
                        AbsentActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialogToTime.show();
                break;
            case R.id.tvConfirm:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        if(datePicker.equals(dialogFromTime)) {
            tvFromTime.setText(dayOfMonth+"-"+month+"-"+year);
        } else {
            tvToTime.setText(dayOfMonth+"-"+month+"-"+year);
        }
    }
}