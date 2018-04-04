package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.functions.FirebaseFunctions;
import com.socialarm.a350s18_5_socialalarmclock.LoginActivity;
import com.socialarm.a350s18_5_socialalarmclock.User;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Alex on 4/3/2018.
 */

public class MessageSender {
    private FirebaseFunctions functions;

    private SharedPreferences prefs;

    public MessageSender() {
        functions = FirebaseFunctions.getInstance();

        Context applicationContext = LoginActivity.getContextOfApplication();
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void notifyFriends(String type) {
        Map<String, Object> data = new HashMap<>();
        String id = prefs.getString("id", null);
        data.put("id", id);
        data.put("type", type);

        functions.getHttpsCallable("notifyFriends").call(data);
    }

    public void sendAlarm(String friendId) {
        Map<String, Object> data = new HashMap<>();
        String id = prefs.getString("id", null);
        data.put("id", id);
        data.put("other_id", friendId);

        functions.getHttpsCallable("sendAlarm").call(data);
    }
}
