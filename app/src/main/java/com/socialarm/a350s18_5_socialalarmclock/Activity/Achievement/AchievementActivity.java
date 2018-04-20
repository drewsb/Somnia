package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.Date;

public class AchievementActivity extends AppCompatActivity {

    private LinearLayout achievement_layout;
    private Drawable bronze_star;
    private Drawable silver_star;
    private Drawable gold_star;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        achievement_layout = (LinearLayout)findViewById(R.id.achievement_layout);
        bronze_star = getResources().getDrawable(R.id.)
    }

    private void DrawAchievementForWeek(Date beginning_date) {
        achievement_layout.addView();
    }
}
