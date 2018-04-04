package com.socialarm.a350s18_5_socialalarmclock;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by drewboyette on 3/13/18.
 */

/**
 * Database class used to push user data to the firebase database
 */
public class UserDatabase {

    private static final String TAG = "UserDatabase";

    private UserDatabase() {
    }

    interface FriendsLambda {
        void callback(List<User> friends);
    }


    interface UserLambda {
        public void callback(User user);
    }

    /**
     * Check if the user is already in the Firebase database. If not, call addUser(user).
     *
     * @param user
     */
    public static void addNewUser(final User user) {
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        final DocumentReference docRef = db.collection("users").document(user.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        addUser(user);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * Gets all users and only returns the one that matches someone with more exp w/ db check it out
     */
    public static void getUser(String user_id, final UserLambda userLambda) {
        FirebaseFirestore db = DatabaseSingleton.getInstance();
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

    /**
     * Gets all users and checks if they are in list and populate. someone with more exp w/ db check it out
     */
    public static void getFriends(User user, final FriendsLambda friendsLambda) {
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        // TODO: refactor this slow code
        db.collection("users").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        List<User> friends = new ArrayList<User>();
                        for (DocumentSnapshot ds : documentSnapshots) {
                            User friend = ds.toObject(User.class);

                            if (user != null && user.getFriend_ids() != null && user.getFriend_ids().contains(friend.getId())) {
                                friends.add(friend);
                            } else {
                                CharSequence text = "You do not have a friends list";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            }
                        }
                        //callback
                        friendsLambda.callback(friends);
                    }
                })
                .addOnFailureListener(e -> Log.d("Friend", "Error getting friends"));
    }

    /**
     * Add given user to the database.
     *
     * @param user
     */
    public static void addUser(User user) {
        DatabaseSingleton.getInstance().collection("users").document(user.getId()).set(user);
    }

    /**
     * Retrieve the user's next alarm.
     * @param user
     * @return
     */
    public static Alarm getMostRecentAlarm(User user) {
        String user_id = user.getId();
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        CollectionReference ref = DatabaseSingleton.getInstance().collection("users").document(user_id)
                .collection("alarms");
        TreeMap<Alarm, Integer> alarmTimeMap = new TreeMap<Alarm,Integer>();
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, doc.getId() + " => " + doc.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return new Alarm(user.getId(), 41, 1, "Wednesday", 5, 0, 10);
    }

    /**
     * Update given user's firebase id
     * @param self
     * @param firebase_id
     */
    public static void updateFirebaseId(String self, String firebase_id) {
        DatabaseSingleton.getInstance().collection("users").document(self).update("firebase_id", firebase_id);
    }

    public static void updateFirebaseId(String self, String firebase_id) {
        db.collection("users").document(self).update("firebase_id", firebase_id);
    }
}
