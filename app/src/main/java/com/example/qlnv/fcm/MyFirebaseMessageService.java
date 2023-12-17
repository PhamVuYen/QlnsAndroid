package com.example.qlnv.fcm;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.qlnv.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if(message.getData().size() > 0){
            Map<String, String> data = message.getData();
            handleData(data);
        }else {
            handleNotification(message.getNotification());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void handleData(Map<String, String> data){
        // truyen data
        String title = data.get("title");
        String message = data.get("message");

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, intent);
    }

    private void handleNotification(RemoteMessage.Notification notification){

        String title = notification.getTitle();
        String message = notification.getBody();

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, intent);

    }
}
