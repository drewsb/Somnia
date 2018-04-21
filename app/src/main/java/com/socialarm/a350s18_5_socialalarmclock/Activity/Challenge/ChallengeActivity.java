package com.socialarm.a350s18_5_socialalarmclock.Activity.Challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.MessageSender;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for sending a challenge to a friend
 */

public class ChallengeActivity extends AppCompatActivity {

    private User user;
    private User friend;
    private EditText challengeDaysEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //set the user
        user = (User) i.getSerializableExtra("user");

        //get friend
        friend = (User) i.getSerializableExtra("friend");

        //get text
        challengeDaysEditText = findViewById(R.id.challengeDaysEditText);
    }

    /**
     * Is called when user wants to challenge a friend by clicking button
     * @param v
     */
    public void onClickChallenge(View v)
    {
        //attempt to parse user input
        try {
            int numberDays = Integer.parseInt(challengeDaysEditText.getText().toString());
            if(numberDays < 0) {
                Toast.makeText(this, "Please input a number that is not negative", Toast.LENGTH_LONG).show();
                return;
            }

            //challenge call to messeenger firebase
            MessageSender ms = new MessageSender();
            Map<String, Object> data = new HashMap<>();
            data.put("days", ""+numberDays);
            data.put("challenger", user.getId());
            data.put("challengee", friend.getId());
            data.put("challengeType", "send");
            ms.sendDirect(friend.getId(), "challenge", data);

            Toast.makeText(this, "Challenged " + friend.getFirst_name() + " " + friend.getLast_name() + " for " + numberDays + " days!", Toast.LENGTH_LONG).show();

            finish();
        } catch (Exception exception) {
            Log.v("Failed to parse int", exception.toString());
            Toast.makeText(this, "Please input a number", Toast.LENGTH_LONG).show();
        }
    }
}
