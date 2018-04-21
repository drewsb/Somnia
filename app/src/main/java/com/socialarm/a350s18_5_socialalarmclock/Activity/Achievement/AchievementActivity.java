package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.content.Context;
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

    final double BRONZE_THRESHOLD = 0.75;
    final double SILVER_THRESHOLD = 0.90;
    final double GOLD_THRESHOLD = 0.99;

    final long WEEK = 1000 * 60 * 60 * 24 * 7;

    private static List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        achievementGrid = findViewById(R.id.achievementGrid);

        //set the grid adapter
        AchievementGridAdapter gridAdapter = new AchievementGridAdapter(this, achievements);
        
        achievementGrid.setAdapter(gridAdapter);
    }

    /**
     * Call to update all achievements
     * @param user the current user
     * @param context context (getApplicationContext)
     */
    public static void updateAchievements(final User user, Context context) {
        EventDatabase.getAllEvents(events -> {
            //update all achievements
            setupAchievements(user, context, events);

            for(Event e : events) {
                for (Achievement achievement : achievements) {
                    achievement.getCondition().callback(e);
                }
            }
        });
    }

    /**
     * Setups all the achievments and reads from db to see if it has been collected
     */
    public static void setupAchievements(final User user, Context context, List<Event> events) {

        //IMAGE CREDITS: https://www.flaticon.com/packs/sleep-time
        int bronze_star = R.drawable.bronze_star;
        int silver_star = R.drawable.silver_star;
        int gold_star = R.drawable.gold_star;
        int alarm_clock = R.drawable.alarm_clock;
        int coffee = R.drawable.coffee;
        int dream = R.drawable.dream;
        int cookie = R.drawable.cookie;
        int sleeping = R.drawable.sleeping;
        int ghost = R.drawable.ghost;
        int night = R.drawable.night;
        int night_1 = R.drawable.night_1;
        int pijamas = R.drawable.pijamas;
        int pijamas_1 = R.drawable.pijamas_1;
        int pillow = R.drawable.pillow;
        int slippers = R.drawable.slippers;

        //initialize achievments
        achievements = new ArrayList<Achievement>();

        //have to initialize this way because callback needs refence to achievement
        Achievement ACHIEVEMENT_FIRST_ACHIEVEMENT = new Achievement(user, alarm_clock, "ACHIEVEMENT_FIRST_ACHIEVEMENT", "First achievement", false);
        ACHIEVEMENT_FIRST_ACHIEVEMENT.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_FIRST_ACHIEVEMENT.eventString())) {
                ACHIEVEMENT_FIRST_ACHIEVEMENT.setIs_achieved(true);
                ACHIEVEMENT_FIRST_ACHIEVEMENT.pushEventToDB(context);
                return;
            }
        });
        achievements.add(ACHIEVEMENT_FIRST_ACHIEVEMENT);

        //have to initialize this way because callback needs refence to achievement

        Achievement ACHIEVEMENT_COFFEE = new Achievement(user, coffee, "ACHIEVEMENT_COFFEE", "You snoozed before 6am", false);
        ACHIEVEMENT_COFFEE.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_COFFEE.eventString())) {

                Boolean snoozeBefore6am = false;

                for(Event e : events) {
                    if(e.getAlarm_id().equals("Snooze")) {
                        Date alarm_time = new Date(e.getTimestamp() * 1000);
                        if(alarm_time.getHours() < 6) {
                            snoozeBefore6am = true;
                            break;
                        }
                    }
                }
                if(snoozeBefore6am) {
                    ACHIEVEMENT_COFFEE.setIs_achieved(true);
                    ACHIEVEMENT_COFFEE.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_COFFEE);

        Achievement ACHIEVEMENT_SNOOZE_PAST_12PM = new Achievement(user, dream, "ACHIEVEMENT_SNOOZE_PAST_12PM", "You snoozed past 12pm", false);
        ACHIEVEMENT_SNOOZE_PAST_12PM.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_SNOOZE_PAST_12PM.eventString())) {

                Boolean snoozeBefore6am = false;

                for(Event e : events) {
                    Date alarm_time = new Date(e.getTimestamp() * 1000);
                    if(alarm_time.getHours() > 12) {
                        snoozeBefore6am = true;
                        break;
                    }
                }
                if(snoozeBefore6am) {
                    ACHIEVEMENT_SNOOZE_PAST_12PM.setIs_achieved(true);
                    ACHIEVEMENT_SNOOZE_PAST_12PM.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_SNOOZE_PAST_12PM);

        Achievement ACHIEVEMENT_CHALLENGE_FRIEND = new Achievement(user, cookie, "ACHIEVEMENT_CHALLENGE_FRIEND", "You challenged a friend", false);
        ACHIEVEMENT_CHALLENGE_FRIEND.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGE_FRIEND.eventString())) {

                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            ACHIEVEMENT_CHALLENGE_FRIEND.setIs_achieved(true);
                            ACHIEVEMENT_CHALLENGE_FRIEND.pushEventToDB(context);
                        }
                    }
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGE_FRIEND);

        Achievement ACHIEVEMENT_CHALLENGED_BY_FRIEND = new Achievement(user, sleeping, "ACHIEVEMENT_CHALLENGED_BY_FRIEND", "A friend challenged you", false);
        ACHIEVEMENT_CHALLENGED_BY_FRIEND.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGED_BY_FRIEND.eventString())) {

                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[1];
                        if (user.getId() == challenger) {
                            ACHIEVEMENT_CHALLENGED_BY_FRIEND.setIs_achieved(true);
                            ACHIEVEMENT_CHALLENGED_BY_FRIEND.pushEventToDB(context);
                        }
                    }
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGED_BY_FRIEND);

        Achievement ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES = new Achievement(user, ghost, "ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES", "You challenge friends more than 5 times", false);
        ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 5) {
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGE_MORE_THAN_5_TIMES);

        Achievement ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES = new Achievement(user, night, "ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES", "You challenge friends more than 20 times", false);
        ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 20) {
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGE_MORE_THAN_20_TIMES);

        Achievement ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES = new Achievement(user, night_1, "ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES", "You challenge friends more than 100 times", false);
        ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 100) {
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGE_MORE_THAN_100_TIMES);

        Achievement ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES = new Achievement(user, pijamas, "ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES", "Friends challenged you more than 100 times", false);
        ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challengee = challenge_infos[1];
                        if (user.getId() == challengee) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 20) {
                    ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_20_TIMES);

        Achievement ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES = new Achievement(user, pijamas_1, "ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES", "Friends challenged you more than 100 times", false);
        ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("Challenge")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challengee = challenge_infos[1];
                        if (user.getId() == challengee) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 100) {
                    ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGED_BY_FRIENDS_MORE_THAN_100_TIMES);

        Achievement ACHIEVEMENT_CHALLENGED_WON = new Achievement(user, pillow, "ACHIEVEMENT_CHALLENGED_WON", "You won a challenge!", false);
        ACHIEVEMENT_CHALLENGED_WON.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGED_WON.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("ChallengeSuccess")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            challenges++;
                        }
                    }
                }
                if(challenges >= 1) {
                    ACHIEVEMENT_CHALLENGED_WON.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGED_WON.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGED_WON);

        Achievement ACHIEVEMENT_CHALLENGED_WON_5_TIMES = new Achievement(user, slippers, "ACHIEVEMENT_CHALLENGED_WON_5_TIMES", "You won a challenge 5 times!", false);
        ACHIEVEMENT_CHALLENGED_WON_5_TIMES.setCondition(event -> {
            if(!event.getEvent_id().startsWith(ACHIEVEMENT_CHALLENGED_WON_5_TIMES.eventString())) {

                int challenges = 0;
                for(Event e : events) {
                    if(e.getAction().equals("ChallengeSuccess")) {
                        //parse the challenger
                        String challenge_info = e.getEvent_id();
                        String challenge_infos[] = challenge_info.split("-");
                        String challenger = challenge_infos[0];
                        if (user.getId() == challenger) {
                            challenges++;
                        }
                    }
                }
                if(challenges > 5) {
                    ACHIEVEMENT_CHALLENGED_WON_5_TIMES.setIs_achieved(true);
                    ACHIEVEMENT_CHALLENGED_WON_5_TIMES.pushEventToDB(context);
                }
            }
        });
        achievements.add(ACHIEVEMENT_CHALLENGED_WON_5_TIMES);

    }
}
