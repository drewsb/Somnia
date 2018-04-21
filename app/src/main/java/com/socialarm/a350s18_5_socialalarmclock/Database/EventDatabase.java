package com.socialarm.a350s18_5_socialalarmclock.Database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.socialarm.a350s18_5_socialalarmclock.Activity.Leaderboard.LeaderBoardFragment.*;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Leaderboard.LeaderboardEntry;
import com.socialarm.a350s18_5_socialalarmclock.Database.DatabaseSingleton;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;

public final class EventDatabase {

    //private constructor
    private EventDatabase() {}

    public interface EventCallback
    {
        void callback(List<Event> events);
    }

    public interface LeaderboardEntryCallback
    {
        void callback(List<LeaderboardEntry> events);
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
     * Get all events since beginning
     *
     * @param eventCallback the function to run once the call is complete
     */
    public static void getAllEvents(final EventCallback eventCallback) {

        DatabaseSingleton.getInstance().collection("events").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        // Convert the whole Query Snapshot to a list
                        List<Event> events = documentSnapshots.toObjects(Event.class);

                        //callback
                        eventCallback.callback(events);
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
     * @param lbeCallback the function to run once the call is complete
     */
    public static void getLeaderboardEventsSince(List<String> friends_list,
                                                 Duration duration,
                                                 SleepStatType type,
                                                 SortDirection direction,
                                                 final LeaderboardEntryCallback lbeCallback) {
        List<LeaderboardEntry> entryList = new ArrayList<>();

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
                        lbeCallback.callback(entryList);
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
        Date date = new Date(event.getTimestamp() * 1000);

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
        Date lastDate = new Date(System.currentTimeMillis() - getInMilliSeconds(difference_days));
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
                return event.getAction().equalsIgnoreCase("Snooze");
            case OVERSLEEP:
                return event.getAction().equalsIgnoreCase("Overslept");
            case CHALLENGE:
                return event.getAction().equalsIgnoreCase("Challenge");
            case WAKE_UP:
                return event.getAction().equalsIgnoreCase("Wakeup");
            case CHALLENGE_SUCCESS:
                return event.getAction().equalsIgnoreCase("ChallengeSuccess");
            default:
                return event.getAction().equalsIgnoreCase("Overslept");
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
     * Helper function to get number of days since the epoch
     *
     * @return the number of days
     */
    private static long getDaysSinceEpoch() {
        return System.currentTimeMillis() / 86400000;
    }

    /**
     * Get events filtered for a specific user_id and TimeDifference and then pass that into the callback
     *
     * @param difference time period to filter for
     * @param user_id user to filter for
     * @param eventCallback callback function
     */
    public static void getEventsSince(TimeDifference difference, String user_id, final EventCallback eventCallback) {
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
            eventCallback.callback(filteredEvents);
        });
    }
  
    /**
     * Add event to event collection
     * ID of event is "user_id + timestamp" of the event
     *
     * @param event the event to add to the database
     */
    public static void addEvent(final Event event) {
        String userID = event.getUser_id();
        DatabaseSingleton.getInstance().collection("events").document(userID + event.getTimestamp()).set(event);
    }

    /**
     * Update the list of people who have liked an event
     * @param thisEvent the event in question
     */
    public static void updateLikedBy(Event thisEvent) {
        DatabaseSingleton.getInstance().collection("events").document(thisEvent.getEvent_id()).update("likedBy", thisEvent.getLikedBy());
    }
}
