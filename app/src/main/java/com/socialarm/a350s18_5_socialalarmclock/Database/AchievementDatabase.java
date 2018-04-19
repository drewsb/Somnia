package com.socialarm.a350s18_5_socialalarmclock.Database;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;

import java.util.HashMap;
import java.util.List;

/**
 * Created by drewboyette on 4/18/18.
 */

public class AchievementDatabase {

    private static final String TAG = "AlarmDatabase";


    private AchievementDatabase() {}

    /**
     * Add achievement to achievement collection, and add achievement id to user achievement collection
     * @param achievement
     */
    public static void addAchievement(final Alarm achievement) {
        FirebaseFirestore db =  DatabaseSingleton.getInstance();
    }

    /**
     * Get all alarms since beginning
     *
     * @param alarmListCallback the function to run once the call is complete
     */
    static void getAllAchivements() {

    }

    /**
     * Get achievement data from user's achievement collection
     */
    public static void getAchievement() {

    }

}
