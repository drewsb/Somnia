package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.AlarmEvent;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SomniaFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            String type = data.get("type");
            if (type.equalsIgnoreCase("snooze")) {
                Intent response = new Intent(this, Response.class);
                response.putExtra("user_id", data.get("id"));
                response.putExtra("hour", Integer.parseInt(data.get("hour")));
                response.putExtra("minute", Integer.parseInt(data.get("minute")));
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, response, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, null)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Somnia")
                        .setContentText("A Friend just snoozed")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1,builder.build());
            } else if (type.equalsIgnoreCase("alarm")) {
                String audio;
                if ((audio = data.get("audio")) != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference ref = storage.getReference(audio);
                    try {
                        File temp = File.createTempFile("audio", "mp3");
                        ref.getFile(temp).addOnFailureListener(exception -> {
                            retriggerDefault();
                        }).addOnSuccessListener(taskSnapshot -> {
                            ref.delete();
                            Intent i = new Intent(this, AlarmEvent.class);
                            i.putExtra("audio", temp.getAbsolutePath());
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        });
                    } catch (IOException e){
                        retriggerDefault();
                    }

                } else {
                    retriggerDefault();
                }
            }
        }
    }

    private void retriggerDefault() {
        Intent i = new Intent(this, AlarmEvent.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
