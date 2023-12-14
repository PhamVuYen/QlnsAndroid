package com.example.qlnv;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;

public class ConnectionAsync extends AsyncTask<Void, Integer, Boolean> {


    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            URL myUrl = new URL(Injector.URL_USER);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            Log.e("errConnection",e.toString());
            // Handle your exceptions
            return false;
        }
    }
}