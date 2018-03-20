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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.FutureTask;
import java.util.stream.LongStream;

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

    //get events for user_id since beginning (to use, use lambda since as (events) -> {Log.v("...", events.toString())} and pass in to function
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

    //helper for GetEventsSince (lambda)
    interface DayCalculator
    {
        long GetInMilliSeconds(long day);
    }

    //get events for user_id since last week, month, year (to use, use lambda since as (events) -> {Log.v("...", events.toString())} and pass in to function
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

    interface FriendsLambda
    {
        public void callback(List<User> friends);
    }

    //SLOW, gets all users and checks if they are in list and populate. someone with more exp w/ db check it out
    public static void GetFriends(User user, final FriendsLambda friendsLambda)
    {
        db.collection("users").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        return;
                    } else {
                        List<User> friends = new ArrayList<User>();
                        for(DocumentSnapshot ds : documentSnapshots)
                        {
                            User friend = ds.toObject(User.class);
                            //check if in list
                            if(user != null && user.getFriend_ids().contains(friend.getId())) {
                                friends.add(friend);
                            }
                        }

                        //callback
                        friendsLambda.callback(friends);
                    }
                })
                .addOnFailureListener(e -> Log.d("Friend", "Error getting friends"));
    }

    interface UserLambda
    {
        public void callback(User user);
    }

    //SLOW, gets all users and checks if they are in list and populate. someone with more exp w/ db check it out
    public static void GetUser(String user_id, final UserLambda userLambda)
    {
        db.collection("users").document(user_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        return;
                    } else {
                        User user = documentSnapshot.toObject(User.class);

                        //callback
                        userLambda.callback(user);
                    }
                })
                .addOnFailureListener(e -> Log.d("User", "Error getting user"));
    }

    //USED FOR TESTING
    public static void WriteEvent(Event e)
    {
        //ugh what is this
        db.collection("events").document(new Random().nextLong() + "").set(e);
    }
}
