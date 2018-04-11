package com.socialarm.a350s18_5_socialalarmclock.Activity.Leaderboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.R;

/**
 * Created by liamdugan on 2018/02/21.
 *
 * LeaderboardRow is the class that creates the row view for leaderboard
 */
public class LeaderboardRow extends LinearLayout {

    private LinearLayout myView;

    /**
     * Basic constructor, inflates the leaderboard_row xml file to serve as XML but
     * otherwise passes on arguments to the super class
     *
     * @param context global app context
     * @param attrs set of other miscellaneous attributes
     */
    public LeaderboardRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (LinearLayout) inflater.inflate(R.layout.leaderboard_row, this);
    }

    /**
     * Set the name that appears in this leaderboard row
     *
     * @param name the name to set the row to
     */
    public void setName(String name) {
        TextView nameView = myView.findViewById(R.id.row_name_text);
        nameView.setText(name);
    }

    /**
     * Set the number that appears at the end of the leaderboard row
     *
     * @param time the number to appear at the end (can be anything, doesn't have to be time)
     */
    public void setTime(String time) {
        TextView timeView = myView.findViewById(R.id.time_text);
        timeView.setText(time);
    }
}
