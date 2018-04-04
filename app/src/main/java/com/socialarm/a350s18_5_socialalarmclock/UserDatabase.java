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
 */

/**
 * Database class used to push user data to the cloud
 */
public class UserDatabase {

    public static final FirebaseFirestore db = DatabaseSingleton.getInstance();

    private static final String TAG = "UserDatabase";

    public static void addNewUser(final User user){
        final DocumentReference docRef = db.collection("users").document(user.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        addUser(user);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    interface UserLambda
    {
        public void callback(User user);
    }

    /**
     *  Gets all users and only returns the one that matches someone with more exp w/ db check it out
     */
    public static void getUser(String user_id, final UserLambda userLambda)
    {
        // TODO: refactor this bad code
        db.collection("users").document(user_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        //callback
                        userLambda.callback(user);
                    }
                })
                .addOnFailureListener(e -> Log.d("User", "Error getting user"));
    }

    public static void addUser(User user){
        // Add a new document with a generated ID
        Log.d(TAG, user.getId());
        db.collection("users").document(user.getId()).set(user);
    }
}
