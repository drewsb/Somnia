package com.socialarm.a350s18_5_socialalarmclock;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Alex on 3/28/2018.
 */

public class SomniaFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ID", "Refreshed token: " + refreshedToken);


    }
}
