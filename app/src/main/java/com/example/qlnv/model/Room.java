package com.example.qlnv.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

@Entity(tableName = "room")
public class Room implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private String id;
    private String name;
    public ArrayList<Employee> dsnv = new ArrayList<Employee>();

    public Room() {

    }

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void themNv(Employee nv) {
        int i = 0;
        for (; i < dsnv.size(); i++) {
            Employee nvOld = dsnv.get(i);
            if (nvOld.getId().trim().equalsIgnoreCase(nv.getId().trim())) {
                break;
            }
        }
        if (i < dsnv.size())
            dsnv.set(i, nv);
        else
            dsnv.add(nv);
    }

    public Employee get(int index) {
        return dsnv.get(index);
    }

    public int size() {
        return dsnv.size();
    }

    public Employee getTruongPhong() {
        for (int i = 0; i < dsnv.size(); i++) {
            Employee nv = dsnv.get(i);
            if (nv.getRole().equals("Trưởng phòng"))
                return nv;
        }
        return null;
    }


    public ArrayList<Employee> getListNhanVien() {
        HashSet<Employee> set = new HashSet<Employee>(dsnv);
        dsnv = new ArrayList<Employee>(set);
        return dsnv;
    }

    @Override
    public String toString() {
        String str = "Phòng: " + getName();
        if (dsnv.size() == 0)
            str += " (Chưa có NV)";
        else
            str += " (có " + dsnv.size() + " NV)";
        return str;
    }


}