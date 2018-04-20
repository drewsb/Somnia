package com.socialarm.a350s18_5_socialalarmclock.Achievement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.Activity.Main.LoginActivity;

/**
 * Created by drewboyette on 4/20/18.
 */

public class AchievementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Achievement", "Received achievement!");
        Context applicationContext = LoginActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String user_id =  prefs.getString("id", null);

        AchievementInfo achievementInfo = new AchievementInfo(context);

        achievementInfo.getMostRecentAchievement(user_id, a -> {
            String message = String.format("Congratulations! You have received a %s star this week.", a.getMetal().toString());
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            achievementInfo.saveAchievementInfo(a);
        });
    }

}