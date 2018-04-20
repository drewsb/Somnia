package com.socialarm.a350s18_5_socialalarmclock.Achievement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by drewboyette on 4/19/18.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReceiver", "Setting alarm");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BootReceiver", "Setting alarm!");
            AchievementUtil.setWeeklyAlarm(context);
        }
    }
}
