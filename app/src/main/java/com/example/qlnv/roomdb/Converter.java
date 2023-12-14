package com.example.qlnv.roomdb;

import androidx.room.TypeConverter;

import com.example.qlnv.model.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Converter {
    //    @TypeConverter
//    public static Role fromString(String jsonString) {
//        if (jsonString == null) {
//            return null;
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<Role>() {}.getType();
//        Role role = gson.fromJson(jsonString,type);
//        return role;
//    }
//
//    @TypeConverter
//    public String fromRole(Role role) {
//        if (role == null) {
//            return null;
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<Role>() {}.getType();
//        String json = gson.toJson(role, type);
//        return json;
//    }
    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Role toRole (String value)  {
        return Role.valueOf(value);
    }


    @TypeConverter
    public static String fromRole(Role value) {
       return value.getRole();
    }
}
