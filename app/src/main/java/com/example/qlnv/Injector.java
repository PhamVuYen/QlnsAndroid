package com.example.qlnv;

import android.util.Log;
import com.example.qlnv.model.Employee;
import com.example.qlnv.model.Room;
import com.example.qlnv.model.TimeKeeping;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Injector {
    public static Employee employee;
    public static TimeKeeping timeKeeping;
    public static String IP = "192.168.1.13";
    public static String PORT = ":8080";
    public static String URL_CONNECT_SERVER = "http://" + IP + PORT + "/QLNS_V1/Connect.php";
    public static String URL_USER = "http://" + IP + PORT + "/QLNS_V1/Staffs/getStaff.php";

    public static String URL_LOGIN = "http://" + IP + PORT + "/QLNS_V1/Login/responseLogin1.php";
    public static String URL_QUERY_USER_ROOM = "http://" + IP +  PORT + "/QLNS_V1/Staffs/getStaffsRoom.php";
    public static String URL_ADD_USER = "http://" + IP + PORT +"/QLNS_V1/Staffs/addStaff.php";
    public static String URL_DEL_USER = "http://" + IP + PORT +"/QLNS_V1/Staffs/delStaff.php";
    public static String URL_EDIT_USER = "http://" + IP + PORT +"/QLNS_V1/Staffs/editStaff.php";
    public static String URL_UPDATE_PASS = "http://" + IP + PORT +"/QLNS_V1/Staffs/updatePassword.php";

    public static String URL_UPDATE_CHANGE_ROOM =  "http://" + IP + PORT +"/QLNS_V1/Staffs/changeRoom.php";

    public static String URL_ROOM = "http://" + IP + PORT +"/QLNS_V1/Departments/getPhongBan.php";
    public static String URL_ADD_ROOM = "http://" + IP + PORT +"/QLNS_V1/Departments/addPhongBan.php";
    public static String URL_DEL_ROOM = "http://" + IP + PORT +"/QLNS_V1/Departments/delPhongBan.php";
    public static String URL_EDIT_ROOM = "http://" + IP + PORT +"/QLNS_V1/Departments/editPhongBan.php";
    public static String URL_EDIT_LANHDAO = "http://" + IP + PORT +"/QLNS_V1/Staffs/setChucVu.php";
    public static String URL_INSERT_LANHDAO = "http://" + IP + PORT +"/QLNS_V1/Staffs/bonhiemChucVu.php";

    public static String URL_ASSIGN_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/addJob.php";
    public static String URL_QUERY_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/getJob.php";
    public static String URL_UPDATE_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/editJob.php";
    public static String URL_DELETE_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/delJob.php";
    public static String URL_UPDATE_STATUS_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/getStatus.php";
    public static String URL_QUERY_ALL_TASK = "http://" + IP + PORT +"/QLNS_V1/Jobs/getJobRoom.php";

    public static String URL_CHECK_CHAMCONGNGAY = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/CheckNgayChamCong.php";
    public static String URL_CHECKIN_CHAMCONG = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/addChechIn.php";
    public static String URL_CHECHKOUT_CHAMCONG = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/addChechOut.php";

    public static String URL_ADD_TIME_RECORER = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/addTimeRecorder.php";
    public static String URL_UPDATE_TIME_RECORER = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/editTimeRecorder.php";
    public static String URL_GET_TIME_RECORER = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/getTimeRecorder.php";

    public static String URL_GET_CHAM_CONG_NV = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/getChamCongNV.php";

    public static String URL_GET_CHAM_CONG_USER = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/getChamCongUser.php";
    public static String URL_GET_CHAM_CONG_NV_ALL = "http://" + IP + PORT +"/QLNS_V1/TimeRecorder/getChamCong.php";
    public static String URL_SEARCH_CHAM_CONG = "http://" + IP + PORT +"/QLNS_V1/Search/searchChamCong.php";

    public static String URL_SEARCH_CHAM_CONG_USER = "http://" + IP + PORT +"/QLNS_V1/Search/searchChamCongByUser.php";

    public static String TIME_ARRIVE = "08:00";
    public static String TIME_LEAVE = "17:00";
    public static int PUNISH = 100000;


    public static Employee getEmployee() {
        if (employee == null) {
            employee = new Employee();
        }
        return employee;
    }

    public static void clearEmployee() {
        employee = null;
    }

    public static TimeKeeping getTimeKeeping() {
        if (timeKeeping == null) {
            timeKeeping = new TimeKeeping();
        }
        return timeKeeping;
    }

    public static String dateToString(Date date) {
        String dateToStr = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return dateToStr;
    }

    public static String datetoStringTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String todayAsString = formatter.format(date);
        Log.d("todayAsString", todayAsString);
        return todayAsString;
    }

    public static int compareDateWithCurrent(String time) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateTime = sf.parse(time);
        Date now = new Date(System.currentTimeMillis()); // 2016-03-10 22:06:10
        Log.d("now", now + "");
        return dateTime.compareTo(now);
    }

    public static String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = dateFormat.format(currentTime);
        return strDate;
    }

    public static String getCurrentTime() {
        String date = getCurrentDate();
        String[] dateTime = date.split(" ");
        return dateTime[1];
    }

    public static String getCurrentDay() {
        String date = getCurrentDate();
        String[] dateTime = date.split(" ");
        return dateTime[0];
    }

    public static String getLateTimeArrive(String currentTime) throws ParseException {
        DateFormat df = new SimpleDateFormat("hh:mm");
        Date date1 = df.parse(currentTime);
        Date date2 = df.parse(TIME_ARRIVE);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        long rs = diff / (1000 * 60);
        Log.d("checkInLate", rs + "|" + currentTime);
        return String.valueOf(rs);
    }

    public static String getEarlyTimeLeave(String currentTime) throws ParseException {
        DateFormat df = new SimpleDateFormat("hh:mm");
        Date date1 = df.parse(currentTime);
        Date date2 = df.parse(TIME_LEAVE);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        long rs = diff / (1000 * 60);
        Log.d("checkOutEarly", rs + "");
        return String.valueOf(rs);
    }


    public static String getPunish(String lateMinute) {
        int dive = Integer.parseInt(lateMinute) / 60;
        int punish = dive * PUNISH;
        return String.valueOf(punish);
    }

    public static int countHolidays(int month) {
        //assuming month is 1-12
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
        return lastDay - total;//as we need workingDays, not weekends
    }

    public static LocalDate getLocalDateTask(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return  LocalDate.parse(date, formatter);
    }

    public static LocalTime getLocalTimeTask(String time) {
        return LocalTime.parse(time);

    }


}
