package com.socialarm.a350s18_5_socialalarmclock;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.facebook.FacebookSdk.getApplicationContext;

public final class Statistic {

    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();


    //private constructor
    private Statistic() {

    }

    public static List<Event> GetEvents(String user_id) {
        Task<QuerySnapshot> querySnapshotTask = db.collection("events").get();
        QuerySnapshot documentSnapshots = null;
        try {
            documentSnapshots = Tasks.await(querySnapshotTask);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (documentSnapshots.isEmpty()) {
            Log.d("AA", "onSuccess: LIST EMPTY");
            return null;
        }
        // Convert the whole Query Snapshot to a list
        // of objects directly! No need to fetch each
        // document.
        List<Event> types = documentSnapshots.toObjects(Event.class);

        Log.d("AAA", "onSuccess: " + types.toString());
        return types;
    }
}
