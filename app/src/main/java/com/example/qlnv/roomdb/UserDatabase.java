package com.example.qlnv.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.qlnv.model.Employee;


@Database(
entities = {Employee.class},
        exportSchema = false,
        version = 1
)
@TypeConverters({Converter.class})

public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

}


