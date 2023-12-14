package com.example.qlnv.roomdb;

import android.content.Context;

import androidx.room.Room;

public class UserDatabaseClient {

    private static final String DB_NAME = "user_db";
    private static UserDatabase userDatabase;

    public static synchronized UserDatabase getInstance(Context context) {
        if (userDatabase == null) {
            userDatabase = Room.databaseBuilder(
                            context.getApplicationContext(), UserDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return userDatabase;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

}

