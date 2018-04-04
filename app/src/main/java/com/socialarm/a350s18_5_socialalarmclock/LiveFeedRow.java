package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    public void setEvent(final Event event) {
        // get the user that this event corresponds to and put it in
        TextView nameView = myView.findViewById(R.id.live_feed_name_text);
        String name = event.getUser_id(); // TODO: Query DB for this event's user's name
        nameView.setText(name);

        // get the text of this event and put it in
        TextView eventTextView = myView.findViewById(R.id.live_feed_event_text);
        String eventText = event.serialize();
        eventTextView.setText(eventText);
    }
}
