package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.socialarm.a350s18_5_socialalarmclock.LoginActivity;
import com.socialarm.a350s18_5_socialalarmclock.User;
import com.socialarm.a350s18_5_socialalarmclock.UserDatabase;

/**
 * Created by Alex on 3/28/2018.
 */

public class SomniaFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static String token;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ID", "Refreshed token: " + refreshedToken);
        token = refreshedToken;
        Context applicationContext = LoginActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String id = prefs.getString("id", null);
        if (id != null) {
            UserDatabase.updateFirebaseId(id, token);
        }
    }

    public static String getToken() {
        return token;
    }
}
