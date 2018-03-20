package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liamdugan on 2018/02/21.
 */

public class FriendRow extends LinearLayout {

    private LinearLayout myView; // TODO: Have this contain a profile object and

    public FriendRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (LinearLayout) inflater.inflate(R.layout.leaderboard_row, this);
    }

    public void setName(String name) {
        TextView nameView = myView.findViewById(R.id.row_name_text);
        nameView.setText(name);
    }

    public void setTime(String time) {
        TextView timeView = myView.findViewById(R.id.time_text);
        timeView.setText(time);
    }
}
