package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by drewboyette on 4/21/18.
 */

public class ChallengeReceiver extends BroadcastReceiver {


    public ChallengeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Achievement", "Challenge Completed!");
        //Challenge completed

        Bundle b = intent.getExtras();
        int days = b.getInt("days");
        String challengeeID = b.getString("challengeeID");
        String challengerID = b.getString("challengerID");

        challengeFinish(true, days, challengerID, challengeeID);
    }

    /**
     * Set challenge alarm to a specific number of days in advance
     * @param context
     */
    public void setAlarm(Context context, int days, String challengeeID, String challengerID) {
        Log.d("ChallengeReceiver", "Setting Challenge");

        Intent i = new Intent(context, com.socialarm.a350s18_5_socialalarmclock.Achievement.AchievementReceiver.class);

        i.putExtra("days", days);
        i.putExtra("challengeeID", challengeeID);
        i.putExtra("challengerID", challengerID);

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, days);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, i,  PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
    }

    public static void challengeFinish(boolean success, int days, String challengerID, String challengeeID) {
        MessageSender ms = new MessageSender();
        Map<String, Object> data = new HashMap<>();
        data.put("days", "" + days);
        data.put("challenger", challengerID);
        data.put("challengee", challengeeID);
        if(success) {
            data.put("challengeType", "success");
        } else {
            data.put("challengeType", "failure");
        }
        ms.sendDirect("" + challengerID, "success", data);
    }

}


