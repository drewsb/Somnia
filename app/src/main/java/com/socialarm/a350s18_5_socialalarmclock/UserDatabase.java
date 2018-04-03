package com.socialarm.a350s18_5_socialalarmclock;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by drewboyette on 3/13/18.
 *
 * Database class used to push user data to the cloud
 */
public class UserDatabase {

    private static final String TAG = "UserDatabase";

    public static void checkUserExists(final User user){
        final DocumentReference docRef = DatabaseSingleton.getInstance().collection("users").document(user.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        UserDatabase.addUser(user);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static void addUser(User user){
        // Add a new document with a generated ID
        Log.d(TAG, user.getId());
        DatabaseSingleton.getInstance().collection("users").document(user.getId()).set(user);
    }
}
