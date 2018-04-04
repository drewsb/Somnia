package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by liamdugan on 2018/02/21.
 *
 * Row to show friends on the friends tab
 */
public class FriendRow extends LinearLayout {

    private LinearLayout myView;

    /**
     * Base constructor for a FriendRow
     * @param context the global app context
     * @param attrs a set of attributes
     */
    public FriendRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (LinearLayout) inflater.inflate(R.layout.friend_row, this);
    }

    /**
     * Set the name that appears on the row
     * @param user the user object from which we get the name string
     */
    public void setName(final User user) {
        TextView nameView = myView.findViewById(R.id.friend_row_name_text);
        String name = user.getFirst_name() + " " + user.getLast_name();
        nameView.setText(name);
    }

    /**
     * Create and inflate the "See stats" button and attach it to the row
     * @param user the user for which the row corresponds
     */
    public void setStatisticButton(final User user) {
        Button friend_button = myView.findViewById(R.id.friend_statisitic_button);
        friend_button.setText(R.string.friend_stat_button_text);
        friend_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StatisticsActivity.class);

                //pass facebook data to statistics activity
                intent.putExtra("user", user);
                getContext().startActivity(intent);
            }
        });
    }
}
