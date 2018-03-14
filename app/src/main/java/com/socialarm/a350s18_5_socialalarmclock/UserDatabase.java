package com.socialarm.a350s18_5_socialalarmclock;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by drewboyette on 3/13/18.
 */

public class UserDatabase {

    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "UserDatabase";

    public static void addUser(User user){
        // Add a new document with a generated ID
        Log.d(TAG, user.getId());
        db.collection("users").document(user.getId()).set(user);
    }
}
