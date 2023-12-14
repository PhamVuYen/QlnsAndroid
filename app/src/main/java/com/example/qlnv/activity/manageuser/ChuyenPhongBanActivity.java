package com.example.qlnv.activity.manageuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.qlnv.activity.account.ChangePasswordActivity;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;


public class ChuyenPhongBanActivity extends Activity {

	ListView lvPb;
	private static ArrayList<Room> arrRoom =null;
	ArrayAdapter<Room> adapter;
	ImageButton btnApply;
	Employee nv=null;
	Room room = new Room();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chuyen_phong_ban);
		getFormWidgets();
		//lấy nhân viên từ màn hình xem danh sách nhân viên
		Intent i=getIntent();
		Bundle b= i.getBundleExtra("DATA");
		nv=(Employee) b.getSerializable("NHANVIEN");
	}
	/**
	 * hàm lấy control theo id
	 * đồng thời load toàn bộ danh sách phòng ban ở MainActivity
	 * lên ListView để sử dụng
	 * android.R.layout.simple_list_item_single_choice ->dùng Radio
	 * Bắt buộc phải xử lý hàm: lvPb.setOnItemClickListener
	 * để gán checked cho Radio
	 */
	public void getFormWidgets()
	{
		lvPb=(ListView) findViewById(R.id.lvphongban);
		btnApply=(ImageButton) findViewById(R.id.imgapply);
		
		arrRoom = ManageUserActivity.getListPhongBan();
		adapter=new ArrayAdapter<Room>
		(this, android.R.layout.simple_list_item_single_choice,
				arrRoom);
		lvPb.setAdapter(adapter);

		lvPb.setOnItemClickListener(new OnItemClickListener() {

			boolean somethingChecked = false;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//hiển nhiên View arg1 là CheckedTextView
				if(somethingChecked){
					CheckedTextView cv = (CheckedTextView) arg1;
					cv.setChecked(false);

				}
				CheckedTextView cv = (CheckedTextView) arg1;
				if(!cv.isChecked())
				{
					cv.setChecked(true);
					arrRoom.get(arg2).themNv(nv);
					room = arrRoom.get(arg2);
				}
				somethingChecked=true;
			}
		});
		//khi chọn nút Apply thì tiến hành đóng màn hình này
		//và truyền lệnh về cho DanhSachNhanVienACtivity
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doApply();

			}
		});
	}
	public void doApply()
	{
		changeRoom(room);
		setResult(ManageUserActivity.CHUYENPHONG_THANHCONG);
		finish();
	}


	private void changeRoom(Room room) {
		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
		StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_UPDATE_CHANGE_ROOM, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					try {
						Log.d("response", response);
						Toast.makeText(ChuyenPhongBanActivity.this, "Updating password is successful", Toast.LENGTH_LONG).show();
						finish();
					} catch (Exception e) {
                        Toast.makeText(ChuyenPhongBanActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
				param.put("MaPB", room.getId());
				Log.d("PARAM",room.getId() + nv.getRoom());
				return param;
			}
		};
		requestQueue.add(stringRequest);
	}
}
