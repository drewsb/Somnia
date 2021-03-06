package com.socialarm.a350s18_5_socialalarmclock.Database;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Helper.Consumer;

import java.util.HashMap;
import java.util.List;

/**
 * Created by drewboyette on 3/27/18.
 */

/**
 * Defines various functions that perform queries on the alarm database
 */
public class AlarmDatabase {

    private static final String TAG = "AlarmDatabase";

    private AlarmDatabase() {}

    /**
     * Add alarm to alarm collection, and add alarm id to user alarm collection
     with the option "On" set to true
     * @param alarm
     */
    public static void addAlarm(final Alarm alarm) {
        FirebaseFirestore db =  DatabaseSingleton.getInstance();
        String userID = alarm.getUser_id();
        // Add a new document with a generated ID
        String alarmID = "" + alarm.hashCode();
        getAlarm(alarmID, userID, alarmResult -> {
            if (alarmResult == null) {
                Log.d(TAG, "Successfully added alarm with ID " + alarmID + " to database.");
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("On", true);
                db.collection("alarms").document(userID + alarmID).set(alarm);
                db.collection("users").document(userID).collection("alarms").document(alarmID).set(data);
            }
        });
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

    /**
     * Get all alarms since beginning
     *
     * @param alarmListCallback the function to run once the call is complete
     */
    static void getAllAlarms(Consumer<List<Alarm>> alarmListCallback) {
        DatabaseSingleton.getInstance().collection("alarms").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        // Convert the whole Query Snapshot to a list
                        List<Alarm> alarms = documentSnapshots.toObjects(Alarm.class);

                        //callback
                        alarmListCallback.callback(alarms);
                    }
                })
                .addOnFailureListener(e -> Log.d("Alarm", "Error getting alarms"));
    }

    /**
     * Get alarm that matches the given alarm id
     *
     * @param alarm_id the ID of the alarm to get
     * @param alarmCallback the function to run once the call is complete
     */
    public static void getAlarm(String alarm_id, String user_id, final Consumer<Alarm> alarmCallback) {
        DatabaseSingleton.getInstance().collection("alarms").document(user_id + alarm_id).get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.exists()) {
                        // Convert the whole Query Snapshot to a list
                        Alarm alarm = documentSnapshots.toObject(Alarm.class);

                        //callback
                        alarmCallback.callback(alarm);
                    }
                    else {
                        alarmCallback.callback(null);
                    }
                })
                .addOnFailureListener( error -> {
                    Log.d(TAG, "Error retrieving alarm");
                    alarmCallback.callback(null);
                });
    }

}
