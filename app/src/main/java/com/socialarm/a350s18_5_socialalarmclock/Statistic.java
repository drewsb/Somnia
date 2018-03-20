package com.socialarm.a350s18_5_socialalarmclock;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.FutureTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public final class Statistic {

    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();


    //private constructor
    private Statistic() {

    }

    interface EventLambda
    {
        public void callback(List<Event> events);
    }

    public static void GetEvents(String user_id, final EventLambda eventLambda) {

            db.collection("events").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        return;
                    } else {
                        // Convert the whole Query Snapshot to a list
                        List<Event> events = documentSnapshots.toObjects(Event.class);

                        //callback
                        eventLambda.callback(events);
                    }
                })
                .addOnFailureListener(e -> Log.d("Event", "Error getting events"));
    }

    public enum TimeDifference {
        WEEK,
        MONTH,
        YEAR
    }

    interface DayCalculator
    {
        long GetInMilliSeconds(long day);
    }

    public static void GetEventsSince(TimeDifference difference, String user_id, final EventLambda eventLambda)
    {
        GetEvents(user_id, events ->
        {
            Calendar calender = Calendar.getInstance();

            //lambda to compute days in milliseconds
            DayCalculator dayCalculator = day -> day * 24 * 3600 * 1000L;

            //events filtered by last week, month, year
            List<Event> filteredEvents = new ArrayList<Event>();

            for(Event event : events)
            {
                //convert to a date
                Date date = new Date(event.getTimestamp());

                //subtract to find if in week, month or year
                long difference_days = 7;

                switch (difference)
                {
                    case WEEK:
                        difference_days = 7;
                        break;
                    case MONTH:
                        difference_days = 30;
                        break;
                    case YEAR:
                        difference_days = 365;
                        break;
                    default:
                        break;
                }

                //calculate now - week, month, year
                Date lastDate = new Date(calender.getTime().getTime() - dayCalculator.GetInMilliSeconds(difference_days));

                //check if event was greater than last week, month, year
                if(date.after(lastDate)) {
                    filteredEvents.add(event);
                }
            }

            //callback
            eventLambda.callback(filteredEvents);
        });
    }
}
