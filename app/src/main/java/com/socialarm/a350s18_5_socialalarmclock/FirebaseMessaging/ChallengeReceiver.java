package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.socialarm.a350s18_5_socialalarmclock.Database.DatabaseSingleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by drewboyette on 4/21/18.
 */

public class ChallengeReceiver extends BroadcastReceiver {

    int days;;
    String challengerID;
    String challengeeID;

    public static ChallengeReceiver receiverInstance;

    public ChallengeReceiver() {}

    /**
     * Retrieve single instance. Create one if one doesn't already exist
     * @return
     */
    public static ChallengeReceiver getInstance() {
        if (receiverInstance == null) {
            receiverInstance = new ChallengeReceiver();
        }
        return receiverInstance;
    }

    public ChallengeReceiver(int days, String challengerID, String challengeeID) {
        this.days = days;
        this.challengerID = challengerID;
        this.challengeeID = challengeeID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Achievement", "Challenge Completed!");
        //Challenge completed
        challengeFinish(true);
    }

    /**
     * Set challenge alarm to a specific number of days in advance
     * @param context
     */
    public void setAlarm(Context context) {
        Log.d("ChallengeReceiver", "Setting Challenge");

        Intent i = new Intent(context, com.socialarm.a350s18_5_socialalarmclock.Achievement.AchievementReceiver.class);

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, this.days);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, i,  PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
    }

    public void cancelAlarm(Context context) {
        ComponentName receiver = new ComponentName(context, ChallengeReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void challengeFinish(boolean success) {
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


