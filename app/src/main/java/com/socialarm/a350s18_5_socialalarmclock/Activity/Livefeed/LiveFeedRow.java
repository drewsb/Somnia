package com.socialarm.a350s18_5_socialalarmclock.Activity.Livefeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Activity.Statistic.StatisticsActivity;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Database.AlarmDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.Response;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.Date;
import java.util.List;

/**
 * Created by liamdugan on 2018/04/03.
 */

public class LiveFeedRow extends LinearLayout {

    final String SLEEP_EMOJI = "😴";

    private LinearLayout myView;
    private int alarmHour;
    private int alarmMin;

    /**
     * Base constructor for a FriendRow
     * @param context the global app context
     * @param attrs a set of attributes
     */
    public LiveFeedRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (LinearLayout) inflater.inflate(R.layout.live_feed_row, this);
    }

    /**
     * Set the name that appears on the row
     * @param event the event object to add
     */
    public void setEvent(final Event event, User currentUser) {
        // get the user that this event corresponds to and put it in
        TextView nameView = myView.findViewById(R.id.live_feed_name_text);
        String user_id = event.getUser_id();
        UserDatabase.getUser(user_id, user -> {
            String name = user.getFirst_name() + " " + user.getLast_name();
            nameView.setText(name);
        });

        // get the type of the event and put it in
        TextView eventTextView = myView.findViewById(R.id.live_feed_event_text);
        String eventAction = event.getAction();
        String eventText = "";
        if (eventAction.equalsIgnoreCase("snooze")) {
            eventText = " snoozed their ";
        } else if (eventAction.equalsIgnoreCase("oversleep")) {
            eventText = " overslept their ";
        } else if (eventAction.equalsIgnoreCase("wakeup")) {
            eventText = " woke up to their ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_FIRST_ACHIEVEMENT")){
            eventText = " got the first achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_COFFEE")){
            eventText = " got the coffee achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_SNOOZE_PAST_12PM")){
            eventText = " got the snooze past 12 achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGE_FRIEND")){
            eventText = " got the friend challenge achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGED_BY_FRIEND")){
            eventText = " got the challenge by friend achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES")){
            eventText = " got the challenge 5 times achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES")){
            eventText = " got the challenge 20 times achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES")){
            eventText = " got the challenge 100 times achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES")){
            eventText = " got the be challenged 20 times achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES")){
            eventText = " got the be challenged 100 times achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGED_WON")){
            eventText = " got the challenge won achievement ";
        } else if (eventAction.equalsIgnoreCase("ACHIEVEMENT_CHALLENGED_WON_5_TIMES")){
            eventText = " got the challenge won 5 times achievement ";
        } else if (eventAction.equalsIgnoreCase("Challenge")) {
            UserDatabase.getUser(event.getEvent_id().split("-")[1], user -> {
                String text = " was challenged by " + user.getFirst_name() + " " + user.getLast_name() + " ";
                eventTextView.setText(text);
            });
        } else if (eventAction.equalsIgnoreCase("ChallengeSuccess")) {
            eventText = " succeeded their challenge ";
        } else {
            eventText = eventAction;
        }
        eventTextView.setText(eventText);

        // get the time of this alarm and put it in
        if (event.getAlarm_id().equals("-1")) {
            TextView alarmView = myView.findViewById(R.id.live_feed_alarm_text);
            alarmView.setText(" retriggered alarm ");
        } else if (event.getAlarm_id().equals("")) {
            TextView alarmView = myView.findViewById(R.id.live_feed_alarm_text);
            alarmView.setText("");
        } else if (event.getAction().equalsIgnoreCase("Challenge")) {
            TextView alarmView = myView.findViewById(R.id.live_feed_alarm_text);
            alarmView.setText("");
        } else {
            AlarmDatabase.getAlarm(event.getAlarm_id(), event.getUser_id(), alarm -> {
                TextView alarmView = myView.findViewById(R.id.live_feed_alarm_text);
                if (alarm == null) {
                    Log.d("LiveFeedRow", "Error finding alarm: " + event.getUser_id() + event.getAlarm_id());
                    return;
                }
                String alarmViewText = Alarm.getTime(alarm.getMin(), alarm.getHour()) + " alarm ";
                alarmView.setText(alarmViewText);
            });
        }


        // get the date of the event's timestamp and put it in
        TextView timestampView = myView.findViewById(R.id.live_feed_timestamp_text);
        String tsText = "on " + new Date(event.getTimestamp() * 1000).toString().split("E")[0];
        timestampView.setText(tsText);

        // get the like button and get the number of likes and put it in
        Button likeButton = myView.findViewById(R.id.like_button);
        setLikeText(likeButton, event.getLikedBy());
        likeButton.setOnClickListener(v -> {
            TextView textView = (TextView) v;
            event.addToLiked(currentUser.getId());
            setLikeText(textView, event.getLikedBy());
        });

        // hook into response event
        myView.setOnClickListener(v -> {

            // trigger response activity
            if (event.getAction().equalsIgnoreCase("snooze")) {
                Intent intent = new Intent(getContext(), Response.class);

                AlarmDatabase.getAlarm(event.getAlarm_id(), event.getUser_id(), alarm -> {
                    intent.putExtra("user_id", event.getUser_id());
                    intent.putExtra("hour", alarm.getHour());
                    intent.putExtra("minute", alarm.getMin());
                    getContext().startActivity(intent);
                });
            }

        });
    }

    /**
     * setLikeText updates the text that is displayed on the like button depending on how many people likes the event
     * @param v the text view of the event
     * @param likedBy the list of users who have liked the event
     */
    private void setLikeText(TextView v, List<String> likedBy) {
        if (likedBy == null || likedBy.size() == 0) {
            String likesNewText = "Send a " + SLEEP_EMOJI;
            v.setText(likesNewText);
        } else {
            if (likedBy.size() == 1) {
                UserDatabase.getUser(likedBy.get(0), user -> {
                    String likesText = SLEEP_EMOJI + " sent by " + user.getFirst_name();
                    v.setText(likesText);
                });
            } else if (likedBy.size() == 2 ){
                UserDatabase.getUser(likedBy.get(0), user -> {
                    String likesText = SLEEP_EMOJI + " sent by " + user.getFirst_name() +
                            " and " + Integer.toString(likedBy.size() - 1) + " other";
                    v.setText(likesText);
                });
            } else {
                UserDatabase.getUser(likedBy.get(0), user -> {
                    String likesText = SLEEP_EMOJI + " sent by " + user.getFirst_name() +
                            " and " + Integer.toString(likedBy.size() - 1) + " others";
                    v.setText(likesText);
                });
            }
        }
    }
}
