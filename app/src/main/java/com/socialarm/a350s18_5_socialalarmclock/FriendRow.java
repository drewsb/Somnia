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
 */

public class FriendRow extends LinearLayout {

    private LinearLayout myView; // TODO: Have this contain a profile object and

    public FriendRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (LinearLayout) inflater.inflate(R.layout.friend_row, this);
    }

    public void setName(final User user) {
        TextView nameView = myView.findViewById(R.id.friend_row_name_text);
        nameView.setText(user.getFirst_name() + " " + user.getLast_name());
    }

    public void setStatisticButton(final User user) {
        Button friend_button = myView.findViewById(R.id.friend_statisitic_button);
        friend_button.setText("Stats");
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
