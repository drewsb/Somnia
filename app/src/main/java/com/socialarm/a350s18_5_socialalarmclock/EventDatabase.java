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

final class EventDatabase {

    //private constructor
    private EventDatabase() {}

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
     * @param eventLambda the function to run once the call is complete
     */
     static void getAllEvents(final EventLambda eventLambda) {

        DatabaseSingleton.getInstance().collection("events").get()
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

    /**
     * getLeaderboardEventsSince gets all events from any User in the given friends_list
     * that matches a given Duration and SleepStatType
     * It then sorts the list in SortDirection before handing it off to the callback
     *
     * @param friends_list the list of users for which we will get events
     * @param duration the timeframe we want to filter for
     * @param type the type of event we want to recieve
     * @param direction the sort direction of the resulting list
     * @param lbeLambda the function to run once the call is complete
     */
    static void getLeaderboardEventsSince(List<String> friends_list,
                                                 Duration duration,
                                                 SleepStatType type,
                                                 SortDirection direction,
                                                 final LeaderboardEntryLambda lbeLambda) {
        List<LeaderboardEntry> entryList = new ArrayList<>();

        for (String friend_id : friends_list) {
            getAllEvents(events -> {
                getUser(friend_id, friend -> {
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

    /**
     * Convert a day into milliseconds
     *
     * @param day a number of days
     * @return the number in milliseconds
     */
    private static long getInMilliSeconds(long day)
    {
        return day * 24 * 60 * 60 * 1000L;
    }

    /**
     * Helper function that determines if an event is within a specified Duration
     *
     * @param event the event to check
     * @param duration the duration we are filtering for
     * @param cal the calendar so that we can get the current time
     * @return a boolean, true if within the specified Duration and false otherwise
     */
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

    /**
     * Helper function that determines if an event is a specified SleepStatType
     *
     * @param event the event to check
     * @param statType the type to check for
     * @return true if the event is the correct type and false otherwise
     */
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

    /**
     * Helper function that determines if an event is from a specified user
     *
     * @param event the event to check
     * @param user_id the user to check for
     * @return true if the event is by that user otherwise false
     */
    private static boolean isCorrectUser(Event event, String user_id) {
        return event.getUser_id().equals(user_id);
    }

    /**
     * Enum for Duration
     */
    public enum TimeDifference {
        WEEK,
        MONTH,
        YEAR
    }

    /**
     * Helper function to get number of days since the epoch
     *
     * @return the number of days
     */
    private static long getDaysSinceEpoch() {
        return System.currentTimeMillis() / 1000 / 60 / 60 / 24;
    }

    /**
     * Get events filtered for a specific user_id and TimeDifference and then pass that into the callback
     *
     * @param difference time period to filter for
     * @param user_id user to filter for
     * @param eventLambda callback function
     */
    static void getEventsSince(TimeDifference difference, String user_id, final EventLambda eventLambda)
    {
        getAllEvents(events ->
        {
            Calendar calender = Calendar.getInstance();

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
                Date lastDate = new Date(calender.getTime().getTime() - getInMilliSeconds(difference_days));

                //check if event was greater than last week, month, year
                if(date.after(lastDate) && isCorrectUser(event, user_id)) {
                    filteredEvents.add(event);
                }
            }

            // call the callback
            eventLambda.callback(filteredEvents);
        });
    }

    /**
     * Definition for the callback lambda
     */
    interface FriendsLambda
    {
        void callback(List<User> friends);
    }

    /**
     * Takes in a user and passes a list of Users that are friends of that user to the callback
     *
     * @param user the user to get friends for
     * @param friendsLambda the callback function
     */
    static void getFriends(User user, final FriendsLambda friendsLambda)
    {
        DatabaseSingleton.getInstance().collection("users").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        List<User> friends = new ArrayList<User>();
                        for(DocumentSnapshot ds : documentSnapshots) {
                            User friend = ds.toObject(User.class);

                            if (user != null && user.getFriend_ids() != null &&user.getFriend_ids().contains(friend.getId())) {
                                friends.add(friend);
                            } else {
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

    /**
     * An interface for the user lambda
     */
    interface UserLambda
    {
        public void callback(User user);
    }

    /**
     * Takes in a user id and passes the user to the callback
     *
     * @param user_id the user_id to check for
     * @param userLambda the callback
     */
    static void getUser(String user_id, final UserLambda userLambda)
    {
        DatabaseSingleton.getInstance().collection("users").document(user_id).get()
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
     * Add event to event collection
     * ID of event is "user_id + timestamp" of the event
     *
     * @param event the event to add to the database
     */
    static void addEvent(final Event event) {
        String userID = event.getUser_id();
        DatabaseSingleton.getInstance().collection("events").document(userID + event.getTimestamp()).set(event);
    }
}
