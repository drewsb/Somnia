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
import java.util.Random;

import com.socialarm.a350s18_5_socialalarmclock.LeaderBoardFragment.*;

import static com.facebook.FacebookSdk.getApplicationContext;

public final class Statistic {

    // TODO: This is a memory leak, please fix (@Drew Boyette)
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //private constructor
    private Statistic() {}

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
    private static void GetAllEvents(final EventLambda eventLambda) {

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
    static void getLeaderboardEventsSince(List<String> friends_list,
                                                 Duration duration,
                                                 SleepStatType type,
                                                 SortDirection direction,
                                                 final LeaderboardEntryLambda lbeLambda) {
        List<LeaderboardEntry> entryList = new ArrayList<>();

        // TODO: This is literally slower than a dead sloth, refactor at later iteration someone please
        for (String friend_id : friends_list) {
            GetAllEvents(events -> {
                GetUser(friend_id, friend -> {
                    Calendar calendar = Calendar.getInstance();

                    //lambda to compute days in milliseconds
                    DayCalculator dayCalculator = day -> day * 24 * 3600 * 1000L;

                    //events filtered by last week, month, year
                    List<Event> filteredEvents = new ArrayList<>();

                    for(Event event : events)
                    {
                        if(isWithinDuration(event, duration, calendar, dayCalculator) &&
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

    private static boolean isWithinDuration(Event event, Duration duration, Calendar cal, DayCalculator dayCalc) {
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
        Date lastDate = new Date(cal.getTime().getTime() - dayCalc.GetInMilliSeconds(difference_days));

        return date.after(lastDate);
    }

    private static boolean isCorrectType(Event event, SleepStatType statType) {
        String eventType = null;
        switch(statType) {
            case SNOOZE:
                eventType = "snooze";
                break;
            case OVERSLEEP:
                eventType = "overslept";
                break;
            default:
                eventType = "overslept";
        }

        return event.getAction().equals(eventType);
    }

    private static boolean isCorrectUser(Event event, String user_id) {
        return event.getUser_id().equals(user_id);
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

    private static long getDaysSinceEpoch() {
        return System.currentTimeMillis() / 1000 / 60 / 60 / 24;
    }

    /**
     * Get events for user_id since last week, month, year
     *
     * (to use, use lambda such as (events) -> {Log.v("...", events.toString())} and pass in to function
     */
    static void GetEventsSince(TimeDifference difference, String user_id, final EventLambda eventLambda)
    {
        // TODO: Refactor this slow code
        GetAllEvents(events ->
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
                    case YEAR: // Get days since epoch
                        difference_days = 365;
                        break;
                    default:
                        break;
                }

                //calculate now - week, month, year
                Date lastDate = new Date(calender.getTime().getTime() - dayCalculator.GetInMilliSeconds(difference_days));

                //check if event was greater than last week, month, year
                if(date.after(lastDate) && isCorrectUser(event, user_id)) {
                    filteredEvents.add(event);
                }
            }

            // call the callback
            eventLambda.callback(filteredEvents);
        });
    }

    interface FriendsLambda
    {
        void callback(List<User> friends);
    }

    /**
     *  Gets all users and checks if they are in list and populate. someone with more exp w/ db check it out
     */
    static void GetFriends(User user, final FriendsLambda friendsLambda)
    {
        // TODO: refactor this slow code
        db.collection("users").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        List<User> friends = new ArrayList<User>();
                        for(DocumentSnapshot ds : documentSnapshots)
                        {
                            User friend = ds.toObject(User.class);
                            try {
                                //check if in list
                                if (user != null && user.getFriend_ids().contains(friend.getId())) {
                                    friends.add(friend);
                                }
                            } catch (NullPointerException e) { // cleanly handle NPE as per request
                                CharSequence text = "You do not have a friends list";
                                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
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

    /**
     *  gets all users and only returns the one that matches someone with more exp w/ db check it out
     */
    static void GetUser(String user_id, final UserLambda userLambda)
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

    //USED FOR TESTING
    public static void WriteEvent(Event e)
    {
        //ugh what is this
        db.collection("events").document(new Random().nextLong() + "").set(e);
    }
}
