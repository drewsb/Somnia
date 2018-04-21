package com.socialarm.a350s18_5_socialalarmclock.Database;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Helper.Consumer;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


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

    /**
     * Simple class used to update a counter memory rather than using a primitive.
     */
    private static class IntegerCounter {
        int counter;

        public IntegerCounter() {
            this.counter = 0;
        }

        public void update(){
            this.counter++;
        }
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
    public static void getUser(String user_id, final Consumer<User> userCallback) {
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        db.collection("users").document(user_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        //callback
                        userCallback.callback(user);
                    }
                })
                .addOnFailureListener(e -> Log.d("User", "Error getting user"));
    }

    /**
     * Gets all users and checks if they are in list and populate. someone with more exp w/ db check it out
     */
    public static void getFriends(User user, final Consumer<List<User>> friendsCallback) {
        IntegerCounter friendCounter = new IntegerCounter();
        List<String> friend_ids = user.getFriend_ids();
        List<User> friendsList = new ArrayList<User>();
        for(String id : friend_ids) {
            getUser(id, userResult -> {
                friendCounter.update();
                if (userResult == null) {
                    Log.d(TAG, "Error searching for user: " + userResult);
                    return;
                }
                friendsList.add(userResult);
                if (friendCounter.counter == friend_ids.size()) {
                    friendsCallback.callback(friendsList);
                }
            });
        }
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
     * Retrieve the user's next alarm by querying all the user's alarms and
     * finding the closest relative to the current time.
     * @param user
     * @return
     */
    public static void getMostRecentAlarm(User user, final Consumer<Alarm> alarmsCallback) {
        TreeMap<Double, Alarm> alarmMap = new TreeMap<Double, Alarm>();
        IntegerCounter alarmCounter = new IntegerCounter();
        String user_id = user.getId();
        FirebaseFirestore db = DatabaseSingleton.getInstance();
        CollectionReference ref = DatabaseSingleton.getInstance().collection("users").document(user_id)
                .collection("alarms");
        Log.d("Collection: ", "is " + ref);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                AlarmDatabase.getAlarm(doc.getId(), user_id, alarmResult -> {
                                    alarmCounter.update();
                                    if (alarmResult == null) {
                                        Log.d(TAG, "Error searching for alarm: " + doc.getId());
                                    } else {
                                        alarmMap.put(alarmResult.getTimeUntilAlarm(), alarmResult);
                                    }
                                    if (alarmCounter.counter == task.getResult().size()) {
                                        if (!alarmMap.isEmpty()) {
                                            alarmsCallback.callback(alarmMap.get(alarmMap.firstKey()));
                                        } else {
                                            alarmsCallback.callback(null);
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            alarmsCallback.callback(null);
                        }
                    }
                });
        Log.d("Kek: ", "is " + ref);
    }

    /**
     * Update given user's firebase id
     * @param self
     * @param firebase_id
     */
    public static void updateFirebaseId(String self, String firebase_id) {
        DatabaseSingleton.getInstance().collection("users").document(self).update("firebase_id", firebase_id);
    }

}
