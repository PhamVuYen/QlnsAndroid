package com.example.qlnv;

import static android.content.Context.WIFI_SERVICE;

import org.junit.Test;

import static org.junit.Assert.*;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTime() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        try {
//            Date dateTime = sf.parse("24-11-2022 18:55");
////            Date now = new Date(System.currentTimeMillis()); // 2016-03-10 22:06:10
//            Date now = sf.parse("25-11-2022 18:55");
//           Log.d("compare",dateTime.compareTo(now) +"");
//            System.out.println(dateTime);
//            System.out.println(now);
////            System.out.println("");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = sdf.parse("2020-07-20");
//        Date date2 = sdf.parse("2020-06-18");
//        Log.d("compare",date1.compareTo(date2) +"");
//        Date now = new Date(System.currentTimeMillis());
//
//        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
//        String strDate = dateFormat.format(now);

        DateFormat df = new java.text.SimpleDateFormat("hh:mm");
        java.util.Date date1 = df.parse("18:40");
        java.util.Date date2 = df.parse("19:05");
        long diff = date2.getTime() - date1.getTime();
        long rs = diff / (1000 * 60);
        Log.d("now", rs + "");
    }

    @Test
    public void test123() {
        int month = 11;
        Calendar firstDayCal = Calendar.getInstance();
        Calendar lastDayCal = Calendar.getInstance();
        firstDayCal.set(Calendar.MONTH, month - 1);//here we should put 0-11;
        lastDayCal.set(Calendar.MONTH, month - 1);
        int firstDay = firstDayCal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDay = firstDayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        firstDayCal.set(Calendar.DAY_OF_MONTH, firstDay);
        lastDayCal.set(Calendar.DAY_OF_MONTH, lastDay);
        //any month have no less than 28 days, so 4 full weeks - so 8 weekends.
        int total = 8;
        switch (lastDay) {
            case 29:
                //leap-year february can have one extra holiday if it starts on sunday or ends on saturday
                if ((firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                    total++;
                }
                break;
            case 30:
                //30-day month can have one extra holiday if it starts on sunday or ends on saturday...
                if ((firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                    total++;
                    //...or two extra holiday if it starts on saturday or ends on sunday
                } else if ((firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    total = total + 2;
                }
                break;
            case 31:
                //31-day month can have one extra holiday if it starts on sunday or ends on saturday...
                if ((firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                    total++;
                    //...or two extra holiday if it starts on (friday or saturday) or ends on (sunday or monday)
                } else if (((firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) || (firstDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) ||
                        ((lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (lastDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY))) {
                    total = total + 2;
                }
                break;
            default:
                break;
        }
        Log.d("total", lastDay + "");
        Log.d("total1", total + "");

    }

    @Test
    public void test345() {
        int dayWork = Injector.countHolidays(Calendar.getInstance().get(Calendar.MONTH) + 1);
        double percent = Double.parseDouble("1") / Double.parseDouble(String.valueOf(dayWork));
        double salary = Integer.parseInt("1000000") * percent;
        Log.d("percent", percent + "");
        Log.d("salary", (int) salary + "");
    }

    @Test
    public void test456() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = "06-08-2016";

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        Log.d("localDate", localDate + "");
//        String strTime = "16:28";
//
//
//        // 2. parse time in String format using above dateTimeFormatter
//        LocalTime localDate = LocalTime.parse(strTime);
//        Log.d("localTime",localDate +"");
    }

    @Test
    public void test124124() {
        ArrayList<LocalDate> arr = new ArrayList<>();
        arr.add(LocalDate.ofEpochDay(2022 - 12 - 24));
        arr.add(LocalDate.ofEpochDay(2022 - 11 - 24));
        arr.add(LocalDate.ofEpochDay(2022 - 11 - 23));
        arr.add(LocalDate.ofEpochDay(2022 - 12 - 23));
        LocalDate localDate = LocalDate.ofEpochDay(2022 - 12 - 23);
        for (LocalDate localDate1 : arr) {
            if (localDate1.equals(localDate1)) {
                Log.d("true", "true");
            } else {
                Log.d("true", "false");
            }
        }
    }

    @Test
    public void test123213() {
        try {
            URL myUrl = new URL(Injector.URL_CONNECT_SERVER);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(5);
            connection.connect();
            Log.d("connect","connect");
        } catch (Exception e) {
            // Handle your exceptions
            Log.d("err","err");
        }

    }
}