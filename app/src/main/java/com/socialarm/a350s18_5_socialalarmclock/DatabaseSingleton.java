package com.socialarm.a350s18_5_socialalarmclock;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by drewboyette on 3/28/18.
 */

/**
 * Singleton class to ensure only one instance of the Firebase Database is being used
 */
public class DatabaseSingleton {

    public static FirebaseFirestore dbInstance;

    public DatabaseSingleton() {}

    /**
     * Retrieve single instance. Create one if one doesn't already exist
     * @return
     */
    public static FirebaseFirestore getInstance() {
        if (dbInstance == null) {
            dbInstance = FirebaseFirestore.getInstance();;
        }
        return dbInstance;
    }


}
