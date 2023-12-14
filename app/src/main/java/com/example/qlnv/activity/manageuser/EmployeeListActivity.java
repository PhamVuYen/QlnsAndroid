package com.example.qlnv.activity.manageuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.qlnv.adapter.EmployeeAdapter;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;


public class EmployeeListActivity extends AppCompatActivity {

    TextView txtmsg;
    ImageView btnback;
    ListView lvNhanvien;
    ArrayList<Employee> arrNhanvien = new ArrayList<>();
    EmployeeAdapter adapter = null;
    Room pb = null;
    private Employee nvSelected = null;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhan_vien);
        txtmsg = findViewById(R.id.txtmsg);
        btnback = findViewById(R.id.btnback);
        lvNhanvien = findViewById(R.id.lvnhanvien);
        getDataFromMain();
        addEvents();
        if (Injector.getEmployee().getRole().equals("ADMIN")) {
            registerForContextMenu(lvNhanvien);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    public void getDataFromMain() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("DATA");
        pb = (Room) b.getSerializable("PHONGBAN");
        arrNhanvien = pb.getListNhanVien();
        Log.d("arrNhanvien",arrNhanvien.size()+"");
        adapter = new EmployeeAdapter(this,
                R.layout.layout_item_custom,
                arrNhanvien);
        lvNhanvien.setAdapter(adapter);
        txtmsg.setText("Phòng: " + pb.getName());
    }


    public void addEvents() {
        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                doUpdateToMain();
            }
        });
        lvNhanvien.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                nvSelected = arrNhanvien.get(arg2);
                position = arg2;
                return false;
            }

        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_nhanvien, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.mnusuanv:
                doSuaNhanVien();
                break;
            case R.id.mnuxoanv:
                doXoaNhanVien();
                break;
            case R.id.mnuchuyenpb:
                doChuyenPhongBan();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * hàm onActivityResult xử lý kết quả trả về
     * cho trường hợp xử dụng COntext Menu để mở các
     * Activity khác
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //lấy kết quả sửa nhân viên thành công
        if (resultCode == ManageUserActivity.SUA_NHAN_VIEN_THANHCONG) {
            Bundle b = data.getBundleExtra("DATA");
            Employee nv = (Employee) b.getSerializable("NHANVIEN");
            arrNhanvien.set(position, nv);
            adapter.notifyDataSetChanged();
        }
        //lấy kết quả chuyển phòng ban thành công
        else if (resultCode == ManageUserActivity.CHUYENPHONG_THANHCONG) {
            arrNhanvien.remove(nvSelected);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * hàm sửa nhân viên
     * đơn giản là mở Activity sửa nhân viên lên
     * rồi truyền nhân viên đang chọn qua Activity đó
     */
    public void doSuaNhanVien() {
        Intent i = new Intent(this, EditEmployeeActivity.class);
        Bundle b = new Bundle();
        nvSelected.setIdRoom(pb.getId());
        b.putSerializable("NHANVIEN", nvSelected);
        i.putExtra("DATA", b);
        startActivityForResult(i, ManageUserActivity.MO_ACTIVITY_SUA_NHAN_VIEN);
    }

    public void doChuyenPhongBan() {
        Intent i = new Intent(this, ChuyenPhongBanActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("NHANVIEN", nvSelected);
        i.putExtra("DATA", b);
        startActivityForResult(i, ManageUserActivity.MO_ACTIVITY_CHUYENPHONG);
    }

    public void doXoaNhanVien() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hỏi xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa [" + nvSelected.getName() + "]");
        builder.setIcon(android.R.drawable.ic_input_delete);
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.cancel();
            }
        });
        builder.setPositiveButton("Ừ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                deleteUserToDB(nvSelected);
                arrNhanvien.remove(nvSelected);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void deleteUserToDB(Employee nv) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_DEL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("response", response);

                    } catch (Exception e) {
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

    public void doUpdateToMain() {
        Intent i = getIntent();
        Bundle b = new Bundle();
        b.putSerializable("PHONGBAN", pb);
        i.putExtra("DATA", b);
        setResult(ManageUserActivity.CAPNHAT_DS_NHAN_VIEN_THANHCONG, i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
