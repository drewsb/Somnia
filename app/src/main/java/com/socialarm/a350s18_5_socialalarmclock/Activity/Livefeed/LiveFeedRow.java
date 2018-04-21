package com.socialarm.a350s18_5_socialalarmclock.Activity.Livefeed;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Database.AlarmDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.Date;
import java.util.List;

/**
 * Created by liamdugan on 2018/04/03.
 */

public class LiveFeedRow extends LinearLayout {

    private LinearLayout myView;

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
        } else if (eventAction.equalsIgnoreCase("overslept")) {
            eventText = " overslept their ";
        } else {
            eventText = eventAction;
        }
        eventTextView.setText(eventText);

        // get the time of this alarm and put it in
        AlarmDatabase.getAlarm(event.getAlarm_id(), event.getUser_id(), alarm -> {
            if (alarm == null) {
                Log.d("LiveFeedRow", "Error finding alarm: " + event.getAlarm_id());
                return;
            }
            String alarmText = alarm.getHour() + ":" + alarm.getMin();
            eventTextView.setText(alarmText);
        });

        // get the date of the event's timestamp and put it in
        TextView timestampView = myView.findViewById(R.id.live_feed_timestamp_text);
        String tsText = " alarm on " + new Date(event.getTimestamp() * 1000).toString().split("E")[0];
        timestampView.setText(tsText);

        // get the like button and get the number of likes and put it in
        Button likeButton = myView.findViewById(R.id.like_button);
        List<String> likedBy = event.getLikedBy();
        String likes = "\uD83D\uDE34 ";
        if (likedBy == null) {
            likes += "0";
        } else {
            likes += likedBy.size();
        }
        likeButton.setText(likes);
        likeButton.setOnClickListener(v -> {
            TextView textView = (TextView) v;
            event.addToLiked(currentUser.getId());
            List<String> likedByList = event.getLikedBy();
            String likesNewText = "\uD83D\uDE34 ";
            if (likedBy == null) {
                likesNewText += "0";
            } else {
                likesNewText += likedBy.size();
            }
            textView.setText(likesNewText);
        });
    }
}
