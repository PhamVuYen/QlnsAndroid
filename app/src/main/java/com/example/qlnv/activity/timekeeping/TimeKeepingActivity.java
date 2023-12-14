package com.example.qlnv.activity.timekeeping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qlnv.Injector;
import com.example.qlnv.Preference;
import com.example.qlnv.R;
import com.example.qlnv.activity.HomeActivity;
import com.example.qlnv.model.TimeKeeping;
import com.google.gson.JsonObject;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeKeepingActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        askPermission();
        if (CameraPermission) {
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCodeScanner.startPreview();
                }
            });
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull @NotNull Result result) {
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            if (!result.toString().isEmpty()) {
                                queryTimeKeeping();
//                                addCheckIn();
                            }
                        }
                    });

                }
            });
        }
    }

    void queryTimeKeeping() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_CHECK_CHAMCONGNGAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseALl", response);
                        JSONArray jsonArray = new JSONArray(response);
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        String timeCheckIn = jsonObject.getString("GioDen");
//                        addCheckIn();
                        if (jsonArray.length() > 0) {
                            addCheckOut();
                            Preference preferences = Preference.getInstance(TimeKeepingActivity.this);
                            preferences.saveTime();
                        } else {
                            addCheckIn();
                        }
                        Log.d("response", response);
                    } catch (Exception e) {
                        Toast.makeText(TimeKeepingActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("Ngay", Injector.getCurrentDay());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void addCheckIn() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_CHECKIN_CHAMCONG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseCheckIn", response);
                        Intent i = new Intent(TimeKeepingActivity.this, SuccessActivity.class);
                        i.putExtra("timekeeping", getResources().getString(R.string.checkin));
                        startActivity(i);
//                        getTimeKeeping();
                    } catch (Exception e) {
//                        Toast.makeText(TimeKeepingActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("Ngay", Injector.getCurrentDay());
                param.put("GioDen", Injector.getCurrentTime());
                param.put("Thang", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void addCheckOut() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Injector.URL_CHECHKOUT_CHAMCONG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Log.d("responseCheckOut", response);
                        Intent i = new Intent(TimeKeepingActivity.this, SuccessActivity.class);
                        i.putExtra("timekeeping", getResources().getString(R.string.checkout));
                        startActivity(i);
                    } catch (Exception e) {
//                        Toast.makeText(TimeKeepingActivity.this, "Some error", Toast.LENGTH_LONG).show();
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
                param.put("Ngay", Injector.getCurrentDay());
                param.put("GioVe", Injector.getCurrentTime());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TimeKeepingActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);
            } else {
                mCodeScanner.startPreview();
                CameraPermission = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == CAMERA_PERM) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mCodeScanner.startPreview();
                CameraPermission = true;
                mCodeScanner.setDecodeCallback(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull @NotNull Result result) {

                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                if (!result.toString().isEmpty()) {
//                                    byte[] bytes = result.toString().getBytes(StandardCharsets.UTF_8);
//                                    byte[] decompress = java.util.Base64.getDecoder().decode(bytes);
//                                    Intent intent = new Intent(TimeKeepingActivity.this, SuccessActivity.class);
//                                    intent.putExtra("jsonQrCode", new String(decompress));
//                                    startActivity(intent);
                                    queryTimeKeeping();
                                }
                            }
                        });

                    }
                });
            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions(TimeKeepingActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            }).create().show();

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [Settings] > [Permissions]")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    finish();

                                }
                            }).create().show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (CameraPermission) {
            mCodeScanner.releaseResources();
        }

        super.onPause();
    }
}