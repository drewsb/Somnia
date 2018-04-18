package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.AlarmEvent;
import com.socialarm.a350s18_5_socialalarmclock.R;

public class SomniaFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String type = remoteMessage.getData().get("type");
            if (type.equalsIgnoreCase("snooze")) {
                Intent response = new Intent(this, Response.class);
                response.putExtra("id", remoteMessage.getData().get("id"));
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, response, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, null)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Somnia")
                        .setContentText("A Friend just snoozed")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1,builder.build());
            } else if (type.equalsIgnoreCase("wakeup")) {
                Intent i = new Intent(this, AlarmEvent.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Alarm", 1);// id of alarm
                startActivity(i);
            }
        }
    }
}
