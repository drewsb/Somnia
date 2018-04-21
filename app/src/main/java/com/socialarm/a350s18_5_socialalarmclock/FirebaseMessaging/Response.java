package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.HashMap;

public class Response extends AppCompatActivity {

    String other_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Intent i = getIntent();
        other_id = i.getStringExtra("user_id");
        final int hour = i.getIntExtra("hour", 0);
        final int minute = i.getIntExtra("minute", 0);
        final String time = Alarm.getTime(minute, hour);

        final TextView header = findViewById(R.id.response_header);
        header.setText("A Friend just snoozed\ntheir " + time + " alarm");
        UserDatabase.getUser(other_id, user -> {
            header.setText(user.getFirst_name() + " " + user.getLast_name() + " just snoozed\ntheir " + time + " alarm");
        });
    }

    public void sendWakeup(View view) {
        MessageSender ms = new MessageSender();
        ms.sendDirect(other_id, "alarm", new HashMap<>());
    }
}
