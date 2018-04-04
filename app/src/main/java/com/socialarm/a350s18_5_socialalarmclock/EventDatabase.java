package com.socialarm.a350s18_5_socialalarmclock;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.socialarm.a350s18_5_socialalarmclock.LeaderBoardFragment.*;

import static com.facebook.FacebookSdk.getApplicationContext;

public final class EventDatabase {

    private static final FirebaseFirestore db = DatabaseSingleton.getInstance();

    //private constructor
    public EventDatabase() {}

    interface EventLambda
    {
        void callback(List<Event> events);
    }

    interface LeaderboardEntryLambda
    {
        void callback(List<LeaderboardEntry> events);
    }
    /**
     * Get all events since beginning
     *
     * (to use, use lambda since as (events) -> {Log.v("...", events.toString())} and pass in to function
     */
    private static void getAllEvents(final EventLambda eventLambda) {

            db.collection("events").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        // Convert the whole Query Snapshot to a list
                        List<Event> events = documentSnapshots.toObjects(Event.class);

                        //callback
                        eventLambda.callback(events);
                    }
                })
                .addOnFailureListener(e -> Log.d("Event", "Error getting events"));
    }

    // TODO: Combine this with other get events since?
    public static void getLeaderboardEventsSince(List<String> friends_list,
                                                 Duration duration,
                                                 SleepStatType type,
                                                 SortDirection direction,
                                                 final LeaderboardEntryLambda lbeLambda) {
        List<LeaderboardEntry> entryList = new ArrayList<>();

        // TODO: This is literally slower than a dead sloth, we should query all events and sort them in single pass, will refactor
        for (String friend_id : friends_list) {
            getAllEvents(events -> {
                UserDatabase.getUser(friend_id, friend -> {
                    Calendar calendar = Calendar.getInstance();

                    //events filtered by last week, month, year
                    List<Event> filteredEvents = new ArrayList<>();

                    for(Event event : events)
                    {
                        if(isWithinDuration(event, duration, calendar) &&
                                isCorrectType(event, type) &&
                                isCorrectUser(event, friend_id)) {
                            filteredEvents.add(event);
                        }
                    }

                    String friendName = friend.getFirst_name() + " " + friend.getLast_name();
                    entryList.add(new LeaderboardEntry(friendName, filteredEvents.size(), direction));
                    Collections.sort(entryList);

                    if (entryList.size() == friends_list.size()) {
                        lbeLambda.callback(entryList);
                    }
                });
            });
        }
    }

    private static long getInMilliSeconds(long day)
    {
        return day * 24 * 60 * 60 * 1000L;
    }

    private static boolean isWithinDuration(Event event, Duration duration, Calendar cal) {
        //convert to a date
        Date date = new Date(event.getTimestamp());

        //subtract to find if in week, month or year
        long difference_days = 7;

        switch (duration)
        {
            case THIS_WEEK:
                difference_days = 7;
                break;
            case THIS_MONTH:
                difference_days = 30;
                break;
            case ALL_TIME:
                difference_days = getDaysSinceEpoch();
                break;
            default:
                break;
        }

        //calculate now - week, month, year
        Date lastDate = new Date(cal.getTime().getTime() - getInMilliSeconds(difference_days));

        return date.after(lastDate);
    }

    private static boolean isCorrectType(Event event, SleepStatType statType) {
        switch(statType) {
            case SNOOZE:
                return event.getAction().equals("snooze");
            case OVERSLEEP:
                return event.getAction().equals("overslept");
            default:
                return event.getAction().equals("overslept");
        }
    }

    private static boolean isCorrectUser(Event event, String user_id) {
        return event.getUser_id().equals(user_id);
    }

    public enum TimeDifference {
        WEEK,
        MONTH,
        YEAR
    }

    private static long getDaysSinceEpoch() {
        return System.currentTimeMillis() / 86400000;
    }

    /**
     * Get events for user_id since last week, month, year
     *
     * (to use, use lambda such as (events) -> {Log.v("...", events.toString())} and pass in to function
     */
    public static void getEventsSince(TimeDifference difference, String user_id, final EventLambda eventLambda) {
        // TODO: Refactor this slow code
        getAllEvents(events ->
        {
            Calendar calender = Calendar.getInstance();

            //events filtered by last week, month, year
            List<Event> filteredEvents = new ArrayList<Event>();

            for (Event event : events) {
                //convert to a date
                Date date = new Date(event.getTimestamp());

                //subtract to find if in week, month or year
                long difference_days = 7;

                switch (difference) {
                    case WEEK:
                        difference_days = 7;
                        break;
                    case MONTH:
                        difference_days = 30;
                        break;
                    case YEAR: // Get days since epoch
                        difference_days = 365;
                        break;
                    default:
                        break;
                }

                //calculate now - week, month, year
                Date lastDate = new Date(calender.getTime().getTime() - getInMilliSeconds(difference_days));

                //check if event was greater than last week, month, year
                if (date.after(lastDate) && isCorrectUser(event, user_id)) {
                    filteredEvents.add(event);
                }
            }

            // call the callback
            eventLambda.callback(filteredEvents);
        });
    }


    /**
     * Add event to event collection
     * ID of event is "user_id + timestamp" of the event
     * @param event
     */
    public void addEvent(final Event event) {
        String userID = event.getUser_id();
        db.collection("events").document(userID + event.getTimestamp()).set(event);
    }
}
