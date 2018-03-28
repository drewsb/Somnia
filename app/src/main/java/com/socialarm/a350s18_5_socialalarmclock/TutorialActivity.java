package com.socialarm.a350s18_5_socialalarmclock;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class TutorialActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        //will need to ask for permissions
        askForPermissions(new String[]{android.Manifest.permission.VIBRATE, android.Manifest.permission.CAMERA}, 1);

        int light_blue = Color.parseColor("#3F51B5");

        //adding the three slides for introduction app you can ad as many you needed
        addSlide(AppIntroFragment.newInstance("Alarms", "Setup your alarms", R.drawable.alarms_tutorial, light_blue));
        addSlide(AppIntroFragment.newInstance("Friends", "Check your friends alarms", R.drawable.friend_tutorial, light_blue));
        addSlide(AppIntroFragment.newInstance("Leaderboard", "See who is the worst snoozer", R.drawable.leaderboard_tutorial, light_blue));
        addSlide(AppIntroFragment.newInstance("Settings", "Look at your statistics and settings", R.drawable.statistics_tutorial, light_blue));
        addSlide(AppIntroFragment.newInstance("All set!", "Will you be the worst snoozer?", R.drawable.ready_tutorial, light_blue));

        // Override bar/separator color.
        setBarColor(light_blue);

        // Turn vibration on and set intensity
        // You will need to add VIBRATE permission in Manifest file
        setVibrate(false);
        setVibrateIntensity(30);

        //Add animation to the intro slider
        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
