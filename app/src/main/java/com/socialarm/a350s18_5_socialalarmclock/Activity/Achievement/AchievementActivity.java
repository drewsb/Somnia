package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.Helper.Consumer;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private LinearLayout achievement_layout;
    private Drawable bronze_star;
    private Drawable silver_star;
    private Drawable gold_star;
    private GridView achievementGrid;
    private User user;

    final double BRONZE_THRESHOLD = 0.75;
    final double SILVER_THRESHOLD = 0.90;
    final double GOLD_THRESHOLD = 0.99;

    final long WEEK = 1000 * 60 * 60 * 24 * 7;

    List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //set the user
        user = (User) i.getSerializableExtra("user");

        achievementGrid = findViewById(R.id.achievementGrid);

        EventDatabase.getAllEvents(events -> {
            //find all achievements
            setupAchievements(events);

            //update all achivements
            for(Event e : events) {
                for (Achievement achievement : achievements) {
                    achievement.getCondition().callback(e);
                }
            }

            //set the grid adapter
            AchievementGridAdapter gridAdapter = new AchievementGridAdapter(this, achievements);
            achievementGrid.setAdapter(gridAdapter);
        });
    }

    /**
     * Setups all the achievments and reads from db to see if it has been collected
     */
    private void setupAchievements(List<Event> events) {
        Resources resources = getResources();

        //IMAGE CREDITS: https://www.flaticon.com/packs/sleep-time
        int bronze_star = R.drawable.bronze_star;
        int silver_star = R.drawable.silver_star;
        int gold_star = R.drawable.gold_star;
        int alarm_clock = R.drawable.alarm_clock;
        int coffee = R.drawable.coffee;
        int dream = R.drawable.dream;
        int cookie = R.drawable.cookie;
        int ghost = R.drawable.ghost;
        int sleeping = R.drawable.sleeping;
        int night = R.drawable.night;
        int night_1 = R.drawable.night_1;
        int pijamas = R.drawable.pijamas;
        int pijamas_1 = R.drawable.pijamas_1;
        int pillow = R.drawable.pillow;
        int slippers = R.drawable.slippers;
        int toothbrush = R.drawable.toothbrush;

        //initialize achievments
        achievements = new ArrayList<Achievement>();

        Achievement ACHIEVEMENT_FIRST_ACHIEVEMENT = new Achievement(user, alarm_clock, "ACHIEVEMENT_FIRST_ACHIEVEMENT", "First achievement", false);
        ACHIEVEMENT_FIRST_ACHIEVEMENT.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_FIRST_ACHIEVEMENT.eventString())) {
                ACHIEVEMENT_FIRST_ACHIEVEMENT.setIs_achieved(true);
                ACHIEVEMENT_FIRST_ACHIEVEMENT.pushEventToDB(this);
                return;
            }
        });
        achievements.add(ACHIEVEMENT_FIRST_ACHIEVEMENT);

        //have to initialize this way because callback needs refence to achievement
        Achievement ACHIEVEMENT_SNOOZE_WEEK_BRONZE = new Achievement(user, bronze_star, "ACHIEVEMENT_WAKE_UP_WEEK_BRONZE", "In the past week, snooze more than 5 times", false);
        ACHIEVEMENT_SNOOZE_WEEK_BRONZE.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_SNOOZE_WEEK_BRONZE.eventString())) {
                //get all snoozes
                long snoozes = 0;
                for(Event e : events) {
                    if(e.getAlarm_id().equals("Snooze")) {
                        long now = new Date().getTime();
                        long previous_week = now - WEEK;
                        if(e.getTimestamp() < now - previous_week) {
                            snoozes++;
                        }
                    }
                }
                if(snoozes > 5) {
                    ACHIEVEMENT_SNOOZE_WEEK_BRONZE.setIs_achieved(true);
                    ACHIEVEMENT_SNOOZE_WEEK_BRONZE.pushEventToDB(this);
                }
            }
        });
        achievements.add(ACHIEVEMENT_SNOOZE_WEEK_BRONZE);

        Achievement ACHIEVEMENT_SNOOZE_WEEK_SILVER = new Achievement(user, silver_star, "ACHIEVEMENT_SNOOZE_WEEK_SILVER", "In the past week, snooze more than 7 times", false);
        ACHIEVEMENT_SNOOZE_WEEK_SILVER.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_SNOOZE_WEEK_SILVER.eventString())) {
                //get all snoozes
                long snoozes = 0;
                for(Event e : events) {
                    if(e.getAlarm_id().equals("Snooze")) {
                        long now = new Date().getTime();
                        long previous_week = now - WEEK;
                        if(e.getTimestamp() < now - previous_week) {
                            snoozes++;
                        }
                    }
                }
                if(snoozes > 7) {
                    ACHIEVEMENT_SNOOZE_WEEK_SILVER.setIs_achieved(true);
                    ACHIEVEMENT_SNOOZE_WEEK_SILVER.pushEventToDB(this);
                }
            }
        });
        achievements.add(ACHIEVEMENT_SNOOZE_WEEK_SILVER);

        Achievement ACHIEVEMENT_SNOOZE_WEEK_GOLD = new Achievement(user, gold_star, "ACHIEVEMENT_SNOOZE_WEEK_GOLD", "In the past week, snooze more than 9 times", false);
        ACHIEVEMENT_SNOOZE_WEEK_GOLD.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_SNOOZE_WEEK_GOLD.eventString())) {
                //get all snoozes
                long snoozes = 0;
                for(Event e : events) {
                    if(e.getAlarm_id().equals("Snooze")) {
                        long now = new Date().getTime();
                        long previous_week = now - WEEK;
                        if(e.getTimestamp() < now - previous_week) {
                            snoozes++;
                        }
                    }
                }
                if(snoozes > 9) {
                    ACHIEVEMENT_SNOOZE_WEEK_GOLD.setIs_achieved(true);
                    ACHIEVEMENT_SNOOZE_WEEK_GOLD.pushEventToDB(this);
                }
            }
        });
        achievements.add(ACHIEVEMENT_SNOOZE_WEEK_GOLD);

    }
}
