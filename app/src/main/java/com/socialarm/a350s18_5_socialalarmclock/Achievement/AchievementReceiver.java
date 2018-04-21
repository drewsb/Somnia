package com.socialarm.a350s18_5_socialalarmclock.Achievement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Main.LoginActivity;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Main.MainActivity;
import com.socialarm.a350s18_5_socialalarmclock.Database.DatabaseSingleton;

import java.util.Calendar;

/**
 * Created by drewboyette on 4/20/18.
 */

public class AchievementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Achievement", "Received achievement!");
        String user_id =  (String) intent.getExtras().get("user_id");

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseApp.initializeApp(context);
        DatabaseSingleton.getInstance().setFirestoreSettings(settings);

        AchievementInfo achievementInfo = new AchievementInfo(context);

        achievementInfo.getMostRecentAchievement(user_id, a -> {
            String message = String.format("Congratulations! You have received a %s star this week.", a.getMetal().toString().toLowerCase());
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            achievementInfo.saveAchievementInfo(a);
        });
    }

    public static void setAlarm(Context context) {
        Log.d("AchievementReceiver", "Setting Alarm");
        Context applicationContext = LoginActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String user_id =  prefs.getString("id", null);

        Intent i = new Intent(context, AchievementReceiver.class);
        i.putExtra("user_id", user_id);

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        // Set the alarm to start at approximately 12:00 p.m on Monday.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);


        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, i,  PendingIntent.FLAG_UPDATE_CURRENT);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
    }

}