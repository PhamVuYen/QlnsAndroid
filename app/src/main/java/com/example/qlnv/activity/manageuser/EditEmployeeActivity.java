package com.example.qlnv.activity.manageuser;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Role;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EditEmployeeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
	private TextView btnXoaTrang, btnLuuNhanVien,tvDate;
	private EditText editTenNv,edtAddress,edtPhone,edtEmail,edtCMND,edtSTK,edtLuong;
	private ImageView imgBack;
	private RadioButton radNam;
	private String idRoom = "";
	private Employee nv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_nhan_vien_new);
		getFormWidgets();
		getDefaultData();
		addEvents();
	}

	public void getFormWidgets() {
		btnXoaTrang = findViewById(R.id.btnxoatrang);
		btnLuuNhanVien = findViewById(R.id.btnluunv);
		tvDate = findViewById(R.id.tvDate);
		editTenNv = findViewById(R.id.editTenNV);
		edtAddress = findViewById(R.id.editDiachi);
		edtPhone = findViewById(R.id.editSDT);
		edtEmail = findViewById(R.id.editEmail);
		edtCMND = findViewById(R.id.editCMND);
		edtLuong = findViewById(R.id.editLuong);
		edtSTK = findViewById(R.id.editSTK);
		radNam = findViewById(R.id.radNam);
		imgBack = findViewById(R.id.imgBack);
	}

	public void getDefaultData() {
		Intent i = getIntent();
		Bundle b = i.getBundleExtra("DATA");
		nv = (Employee) b.getSerializable("NHANVIEN");
		editTenNv.setText(nv.getName());
		edtAddress.setText(nv.getAddress());
		edtPhone.setText(nv.getPhone());
		edtCMND.setText(nv.getIdentified());
		edtLuong.setText(nv.getMucluong());
		edtAddress.setText(nv.getAddress());
		edtSTK.setText(nv.getStk());
		edtEmail.setText(nv.getEmail());
		tvDate.setText(Injector.dateToString(nv.getDateOfbirth()));
		radNam.setChecked(true);
		if (nv.isSex()) {
			radNam.setChecked(false);
		}
	}

	public void addEvents() {
		btnXoaTrang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				editTenNv.setText("");
				editTenNv.requestFocus();
				edtAddress.setText("");
				edtPhone.setText("");
				edtCMND.setText("");
				edtLuong.setText("");
				edtAddress.setText("");
				edtSTK.setText("");
				edtEmail.setText("");
				tvDate.setText("");
			}
		});

		btnLuuNhanVien.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nv.setName(editTenNv.getText() + "");
				nv.setRole("Nhân viên");
				nv.setSex(!radNam.isChecked());
				nv.setAddress(edtAddress.getText() + "");
				nv.setEmail(edtEmail.getText() +"");
				nv.setIdentified(edtCMND.getText()+"");
				nv.setRoom(idRoom);
				nv.setMucluong(edtLuong.getText()+"");
				nv.setStk(edtSTK.getText() +"");
				nv.setPhone(edtPhone.getText()+"");
				nv.setDateOfbirth(new Date());
				editUserToDB(nv);
			}
		});
		imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private void editUserToDB(Employee nv) {
		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_EDIT_USER, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					try {
						Log.d("response",response);
						Intent i = getIntent();
						Bundle b = new Bundle();
						b.putSerializable("NHANVIEN", nv);
						i.putExtra("DATA", b);
						setResult(ManageUserActivity.SUA_NHAN_VIEN_THANHCONG, i);
						finish();
					} catch (Exception e) {
						Toast.makeText(EditEmployeeActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
				param.put("Phone", nv.getPhone());
				param.put("Email", nv.getEmail());
				param.put("SoCMND", nv.getIdentified());
				param.put("SoTk", nv.getStk());
				param.put("MucLuong", nv.getMucluong());
				param.put("MaPB",nv.getIdRoom());
				Log.d("idroom",nv.getIdRoom()+"00");
				return param;
			}
		};
		requestQueue.add(stringRequest);
	}

	@Override
	public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
		String date= dayOfMonth + "/" + month+"/"+ year;
		Log.d("date",date+"");
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

		try {
			Date d=dateFormat.parse(date);
			tvDate.setText(Injector.dateToString(new Date()));
//            nv.setDateOfbirth(d);
		}
		catch(Exception e) {
			System.out.println("Excep"+e);
		}
	}
}
