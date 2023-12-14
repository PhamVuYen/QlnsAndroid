package com.example.qlnv.model;

public class Summary {
    String idNv;
    int workingDay;
    int lateDay;
    int month;

    public Summary(String idNv, int workingDay, int lateDay, int month) {
        this.idNv = idNv;
        this.workingDay = workingDay;
        this.lateDay = lateDay;
        this.month = month;
    }

    public String getIdNv() {
        return idNv;
    }

    public void setIdNv(String idNv) {
        this.idNv = idNv;
    }

    public int getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(int workingDay) {
        this.workingDay = workingDay;
    }

    public int getLateDay() {
        return lateDay;
    }

    public void setLateDay(int lateDay) {
        this.lateDay = lateDay;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
