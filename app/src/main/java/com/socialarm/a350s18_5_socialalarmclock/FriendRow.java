package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by liamdugan on 2018/02/21.
 *
 * Row to show friends on the friends tab
 */
public class FriendRow extends LinearLayout {

    private LinearLayout myView;

    public final String TAG = "FriendRow";
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
        nameView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StatisticsActivity.class);

                //pass facebook data to statistics activity
                intent.putExtra("user", user);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * Set the time of the most recent alarm belonging to the user
     * @param time
     */
    public void setTime(final String time, final User user) {
        TextView timeView = myView.findViewById(R.id.friend_row_time_text);
        timeView.setText(time);
        timeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StatisticsActivity.class);

                //pass facebook data to statistics activity
                intent.putExtra("user", user);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * Create and inflate the "Join Alarm" button and attach it to the row
     * @param user the user for which the row corresponds
     */
    public void setJoinButton(final User user, final Alarm alarm) {
        Alarm newAlarm = new Alarm(user.getId(), alarm.getMin(), alarm.getHour(),
                alarm.getDay_of_week(), alarm.getSnooze_count(), alarm.getSnooze_interval(), alarm.getVolume());
        Button friend_button = myView.findViewById(R.id.friend_row_join_button);
        friend_button.setText(R.string.friend_join_text);
        friend_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(MyAlarms.getContextOfApplication());
                dbHelper.addAlarm(alarm.getHour(), alarm.getMin(), alarm.getIntDayOfWeek(), alarm.getSnooze_count(),
                        alarm.getSnooze_interval(), alarm.getVolume());
                AlarmDatabase.addAlarm(newAlarm);
                String text = "You have successfully joined " + user.getFirst_name() + "'s alarm!";
                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
