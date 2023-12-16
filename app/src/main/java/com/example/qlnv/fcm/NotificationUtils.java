package com.example.qlnv.fcm;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.qlnv.R;
import com.example.qlnv.activity.HomeActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "mychannelName";

private Map<String, Class> activityMap = new HashMap<>();
    private Context context;

    public NotificationUtils (Context context){
        this.context = context;

        activityMap.put("homeActivity", HomeActivity.class);
    }

    void displayNotification (NotificationVO notificationVO, Intent resultIntent){
        String title = notificationVO.getTitle();
        String message = notificationVO.getMessage();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        Notification notification;

        notification = builder.setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentTitle((CharSequence) resultIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .build();


    }

}
