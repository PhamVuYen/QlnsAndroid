package com.example.qlnv.activity.manageuser;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.example.qlnv.activity.assigntask.AssignTaskActivity;
import com.example.qlnv.model.Role;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddEmployeeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView btnXoaTrang, btnLuuNhanVien,tvDate;
    private EditText editManv, editTenNv,edtAddress,edtPhone,edtEmail,edtCMND,edtSTK,edtLuong;
    private RadioButton radNam;
    private MaterialToolbar toolbar;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private String idRoom = "";
    private ImageView imgBack;
    private Employee nv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        Intent i = getIntent();
        idRoom = i.getStringExtra("idRoom");
        getFormWidgets();
        addEvents();
    }

    public void getFormWidgets() {
        imgBack = findViewById(R.id.imgBack);
        btnXoaTrang = findViewById(R.id.btnxoatrang);
        btnLuuNhanVien = findViewById(R.id.btnluunv);
        tvDate = findViewById(R.id.tvDate);
        editManv = findViewById(R.id.editMaNV);
        editTenNv = findViewById(R.id.editTenNV);
        edtAddress = findViewById(R.id.editDiachi);
        edtPhone = findViewById(R.id.editSDT);
        edtEmail = findViewById(R.id.editEmail);
        edtCMND = findViewById(R.id.editCMND);
        edtLuong = findViewById(R.id.editLuong);
        edtSTK = findViewById(R.id.editSTK);
        radNam = findViewById(R.id.radNam);

    }

    public void addEvents() {
        btnXoaTrang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                doXoaTrang();
            }
        });
        btnLuuNhanVien.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doLuuNhanVien();
            }
        });
        tvDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AddEmployeeActivity.this,
                        AddEmployeeActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void doXoaTrang() {
        editManv.setText("");
        editTenNv.setText("");
        editManv.requestFocus();
    }

    public void doLuuNhanVien() {
        nv = new Employee();
        nv.setId(editManv.getText() + "");
        nv.setName(editTenNv.getText() + "");
        nv.setRole("Nhân viên");
        nv.setSex(!radNam.isChecked());
        nv.setAddress(edtAddress.getText() + "");
        nv.setEmail(edtEmail.getText() +"");
        nv.setIdentified(edtCMND.getText()+"");
        nv.setRoom(idRoom);
        nv.setPhone(edtPhone.getText()+"");
        nv.setMucluong(edtLuong.getText()+"");
        nv.setStk(edtSTK.getText() +"");
        nv.setDateOfbirth(new Date());
        nv.setPassword(edtCMND.getText());
        Log.d("NHANVIEN1",nv.toString());
        addUserToDB(nv);
    }

    private void addUserToDB(Employee nv) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_ADD_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response",response);
                        Intent i = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("NHANVIEN", nv);
                        i.putExtra("DATA", bundle);
                        setResult(ManageUserActivity.THEM_NHAN_VIEN_THANHCONG, i);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(AddEmployeeActivity.this, "Fail to connect server employee in room", Toast.LENGTH_LONG).show();
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
                param.put("MaNV", nv.getId());
                param.put("TenNV", nv.getName());
                param.put("NgaySinh", Injector.dateToString(nv.getDateOfbirth()));
                param.put("DiaChi", nv.getAddress());
                param.put("GioiTinh", nv.isSex() ? "Nam" : "Nữ");
                param.put("Phone",nv.getPhone());
                param.put("Email", nv.getEmail());
                param.put("SoCMND", nv.getIdentified());
                param.put("SoTk", nv.getStk());
                param.put("MaPB", nv.getIdRoom());
                param.put("MucLuong", nv.getMucluong());
                param.put("ChucVu","Nhân Viên");
                param.put("Password",nv.getPassword());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date= dayOfMonth + "/" + month+"/"+ year;
        Log.d("date",date+"");
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d=dateFormat.parse(date);
            tvDate.setText(Injector.dateToString(new Date()));
//            nv.setDateOfbirth(d);
        }
        catch(Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep"+e);
        }

    }
}
