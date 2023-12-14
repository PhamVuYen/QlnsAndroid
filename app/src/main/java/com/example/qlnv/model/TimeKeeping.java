package com.example.qlnv.model;

public class TimeKeeping {
    String idNv;
    String checkIn;
    String checkOut;
    String month;
    String day;

    public TimeKeeping() {
    }

    public TimeKeeping(String idNv, String checkIn, String checkOut, String month, String day) {
        this.idNv = idNv;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.month = month;
        this.day = day;
    }

    public String getIdNv() {
        return idNv;
    }

    public void setIdNv(String idNv) {
        this.idNv = idNv;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String day) {
        this.month = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
