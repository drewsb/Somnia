package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.AlarmEvent;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Challenge.AcceptOrDeclineChallengeActivity;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.Map;

public class SomniaFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * This is called whenever a push notification comes in from firebase.
     * @param remoteMessage The message recieved.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String type = remoteMessage.getData().get("type");
            if (type.equalsIgnoreCase("snooze")) {
                // Snooze events are sent to a phone when a friend snoozes an alarm.
                // They create a notification to the response page.
                Intent response = new Intent(this, Response.class);
                Map<String, String> data = remoteMessage.getData();
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
                // Alarm notifications are sent in response to a snooze event.
                // They reopen the alarm activity.
                Intent i = new Intent(this, AlarmEvent.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else if (type.equalsIgnoreCase("challenge")) {
                // Get challenge type
                Map<String, String> data = remoteMessage.getData();
                String challengeType = data.get("challengeType");
                String challenger_id = data.get("challenger");
                String challengee_id = data.get("challengee");

                //sending request to challenge to another user (we are being challenged)
                if(challengeType.equals("send")) {
                    //fetch users
                    UserDatabase.getUser(challenger_id, challenger -> {
                        UserDatabase.getUser(challengee_id, challengee -> {
                            Intent intent = new Intent(this, AcceptOrDeclineChallengeActivity.class);
                            intent.putExtra("user", challengee);
                            intent.putExtra("friend", challenger);

                            int days = 0;
                            try {
                                days = Integer.parseInt(data.get("days"));
                            } catch (Exception e) {
                                Log.v("Somnia firebase", e.toString());
                            }

                            intent.putExtra("days", days);
                            startActivity(intent);
                        });
                    });
                }
            }
        }
    }
}
