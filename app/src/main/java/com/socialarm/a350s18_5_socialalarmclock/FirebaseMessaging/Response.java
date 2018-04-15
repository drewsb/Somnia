package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.HashMap;

public class Response extends AppCompatActivity {

    String other_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Intent i = getIntent();
        other_id = i.getStringExtra("id");
    }

    public void sendWakeup(View view) {
        MessageSender ms = new MessageSender();
        ms.sendDirect(other_id, "alarm", new HashMap<>());
    }
}
