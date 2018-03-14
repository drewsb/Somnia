package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //Set profile pic image
        new DownloadImageTask((ImageView) findViewById(R.id.profileView))
                .execute(extras.getString("profile_pic"));

        //Retrieve user's name and email
        String name = extras.getString("first_name") + " " + extras.getString("last_name");
        String email = extras.getString("email");

        TextView nameView =  findViewById(R.id.nameView);
        TextView emailView = findViewById(R.id.emailView);

        nameView.setText(name);
        emailView.setText(email);

        //set number statistics
        TextView statisticsView = findViewById(R.id.statisticsView);
        statisticsView.append("Average wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");

    }

}
