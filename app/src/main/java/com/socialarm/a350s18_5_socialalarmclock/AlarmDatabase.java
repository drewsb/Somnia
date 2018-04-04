package com.socialarm.a350s18_5_socialalarmclock;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * Created by drewboyette on 3/27/18.
 */

/**
 * Defines various functions that perform queries on the alarm database
 */
public class AlarmDatabase {

    private static final String TAG = "AlarmDatabase";

    interface AlarmLambda
    {
        public void callback(Alarm alarm);
    }

    private AlarmDatabase() {}
    /**
     * Add alarm to alarm collection, and add alarm id to user alarm collection
     with the option "On" set to true
     * @param alarm
     */
    public static void addAlarm(final Alarm alarm) {
        String userID = alarm.getUser_id();
        // Add a new document with a generated ID
        String alarmID = "" + System.identityHashCode(alarm);
        DatabaseSingleton.getInstance().collection("alarms").document(userID + alarmID).set(alarm);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("On", true);
        DatabaseSingleton.getInstance().collection("users").document(userID).collection("alarms").document(alarmID).set(data);
    }

    public void getAlarm(final String user_id, final int alarm_id, final AlarmLambda alarmLambda) {
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        db.collection("alarms").document(user_id + alarm_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Alarm alarm = documentSnapshot.toObject(Alarm.class);

                        //callback
                        alarmLambda.callback(alarm);
                    }
                })
                .addOnFailureListener(e -> Log.d("User", "Error getting user"));
    }

    /**
     * Delete alarm from the alarm collection, and remove alarm id from user alarm collection
     * @param alarm
     */
    public static void deleteAlarm(final Alarm alarm) {
        String userID = alarm.getUser_id();
        String alarmId = "" + System.identityHashCode(alarm);
        DatabaseSingleton.getInstance().collection("alarms").document(userID + alarmId).delete();
        DatabaseSingleton.getInstance().collection("users").document(userID).collection("alarms").document(alarmId).delete();
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
        DatabaseSingleton.getInstance().collection("users").document(userID).collection("alarms").document(alarmId).set(data);
    }
}
