package com.example.qlnv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Preference {
    private static final String PREF_NAME = "PREF_NAME";
    private final SharedPreferences sharedPreferences;
    public static Preference preference;
    private Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Preference getInstance(Context context) {
        if (preference == null) {
            preference = new Preference(context);
        }
        return preference;
    }

    public boolean isTimeToClick() {
        Date oldDate = new Date(sharedPreferences.getLong("mTime", System.currentTimeMillis()));
        GregorianCalendar oldCalendar = new GregorianCalendar();
        oldCalendar.setTime(oldDate);
        Calendar newCalendar = new GregorianCalendar();
        return newCalendar.get(Calendar.DATE) != oldCalendar.get(Calendar.DATE) ||
                newCalendar.get(Calendar.MONTH) != oldCalendar.get(Calendar.MONTH) ||
                newCalendar.get(Calendar.YEAR) != oldCalendar.get(Calendar.YEAR);
    }

    public String getIDUser() {
        return  sharedPreferences.getString("idUser","");
    }

    public void saveTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("mTime", System.currentTimeMillis());
        editor.putString("idUser",Injector.getEmployee().getId());
        editor.apply();
    }
}
