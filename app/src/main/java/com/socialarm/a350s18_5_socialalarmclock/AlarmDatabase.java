package com.socialarm.a350s18_5_socialalarmclock;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * Created by drewboyette on 3/27/18.
 */

public class AlarmDatabase {

    public static final FirebaseFirestore db = DatabaseSingleton.getInstance();

    private static final String TAG = "AlarmDatabase";


    /**
     * Add alarm to alarm collection, and add alarm id to user alarm collection
     with the option "On" set to true
     * @param alarm
     */
    public static void addAlarm(final Alarm alarm) {
        String userID = alarm.getUser_id();
        // Add a new document with a generated ID
        String alarmID = "" + System.identityHashCode(alarm);
        db.collection("alarms").document(userID + alarmID).set(alarm);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("On", true);
        db.collection("users").document(userID).collection("alarms").document(alarmID).set(data);
    }

    /**
     * Delete alarm from the alarm collection, and remove alarm id from user alarm collection
     * @param alarm
     */
    public static void deleteAlarm(final Alarm alarm) {
        String userID = alarm.getUser_id();
        String alarmId = "" + System.identityHashCode(alarm);
        db.collection("alarms").document(userID + alarmId).delete();
        db.collection("users").document(userID).collection("alarms").document(alarmId).delete();
    }

    /**
     * Sets an alarm in the user collection either on or off depending on the OnBool Boolean
     * @param alarm
     * @param OnBool
     */
    public static void enableAlarm(final Alarm alarm, Boolean OnBool) {
        String userID = alarm.getUser_id();
        String alarmId = "" + System.identityHashCode(alarm);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("On", OnBool);
        db.collection("users").document(userID).collection("alarms").document(alarmId).set(data);
    }
}
