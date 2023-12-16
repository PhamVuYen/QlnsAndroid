package com.example.qlnv;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import androidx.annotation.NonNull;

import com.example.qlnv.model.Employee;
import com.google.gson.Gson;

public class SharedPref {

    private static SharedPref instance = null;

    public static final String USER = "user";
    public static final String TOKEN = "token";

    private static final String sharedPreferencesName = "kevinSharedPref";

    private SharedPref() {
    }

    public static SharedPref getInstance() {
        if (instance == null) {
            instance = new SharedPref();
        }
        return instance;
    }

    public void setToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public void setUser(Context context, Employee user){
        SharedPreferences pref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER,new Gson().toJson(user));
        editor.apply();
    }

    public String getToken(Context context){
        SharedPreferences pref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        return pref.getString(TOKEN,"");
    }


    public Employee getUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        return new Gson().fromJson(pref.getString(USER,""),Employee.class);
    }

    public void clearSharedPref(@NonNull Context context) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
