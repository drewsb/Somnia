package com.socialarm.a350s18_5_socialalarmclock.Achievement;

/**
 * Created by drewboyette on 4/19/18.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AchievementUtil {


    public static void setWeeklyAlarm(Context context) {
        Log.d("AchievementUtil", "Setting Alarm'");
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, BootReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24 * 7, alarmIntent);
    }
}
