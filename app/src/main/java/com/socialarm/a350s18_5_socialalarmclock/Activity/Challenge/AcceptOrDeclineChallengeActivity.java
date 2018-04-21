package com.socialarm.a350s18_5_socialalarmclock.Activity.Challenge;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.MessageSender;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AcceptOrDeclineChallengeActivity extends AppCompatActivity {

    private User challenge;
    private User challenger;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_or_decline_challenge);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //set the user
        challenge = (User) i.getSerializableExtra("user");

        //get friend (challenger)
        challenger = (User) i.getSerializableExtra("friend");

        days = i.getIntExtra("days", 0);

        //setup text
        TextView acceptDeclineTextView = findViewById(R.id.acceptDeclineTextView);
        acceptDeclineTextView.setText("A challenger has appeared!\n" +
                challenger.getFirst_name() + " " + challenger.getLast_name() + " wishes to challenge you to "
                + days +  " days of waking up on time");

        //setup mewtwo image
        ImageView challengerApproachesImageView = findViewById(R.id.challengerApproachesImageView);
        Drawable mewtwoDrawable = getResources().getDrawable(R.drawable.challenger_approaching_mewtwo);
        challengerApproachesImageView.setImageDrawable(mewtwoDrawable);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.super_smash_challenger_approaches);
        mediaPlayer.start();
    }

    /**
     * Called when user accepts a challenge
     * Should create event online in database and send something back to challenger to indicate
     * challenge has started
     * @param v
     */
    public void onClickAcceptChallenge(View v) {
        //send information back to challenger (notify)
        MessageSender ms = new MessageSender();
        Map<String, Object> data = new HashMap<>();
        data.put("days", ""+days);
        data.put("challenger", challenger.getId());
        data.put("challengee", challenge.getId());
        data.put("challengeType", "accept");
        ms.sendDirect(challenger.getId(), "challenge", data);

        finish();
    }

    /**
     * Called when user declines a challenge
     * Should not create event online and indicate to challenger that user has declined
     * @param v
     */
    public void onClickDeclineChallenge(View v) {
        //send information back to challenger (notify)

        MessageSender ms = new MessageSender();
        Map<String, Object> data = new HashMap<>();
        data.put("days", ""+days);
        data.put("challenger", challenger.getId());
        data.put("challengee", challenge.getId());
        data.put("challengeType", "decline");
        ms.sendDirect(challenger.getId(), "challenge", data);

        finish();
    }

}
