package com.example.qlnv.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.qlnv.model.Employee;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertUser(Employee user);


    @Update
    void updateUser(Employee user);

    @Query("SELECT * FROM user")
    List<Employee> observeAllUser();



    @Delete
    void deleteUser(Employee user);



}
