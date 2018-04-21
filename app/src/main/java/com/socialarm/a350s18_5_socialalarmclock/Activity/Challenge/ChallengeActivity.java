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

import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import org.w3c.dom.Text;

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
            if(numberDays <= 0) {
                Toast.makeText(this, "Please input a number greater than 0", Toast.LENGTH_LONG).show();
                return;
            }

            //Replace with challenge call to db
            Toast.makeText(this, "Challenged " + friend.getFirst_name() + " " + friend.getLast_name() + " for " + numberDays + " days!", Toast.LENGTH_LONG).show();

            finish();
        } catch (Exception exception) {
            Log.v("Failed to parse int", exception.toString());
            Toast.makeText(this, "Please input a number", Toast.LENGTH_LONG).show();
        }
    }
}
