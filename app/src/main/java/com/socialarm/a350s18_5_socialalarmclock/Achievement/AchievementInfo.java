package com.socialarm.a350s18_5_socialalarmclock.Achievement;

/**
 * Created by drewboyette on 4/20/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.Helper.Consumer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AchievementInfo is used to store the current user's achievement data to the App's SharedPreferences.
 * This data is persistent across all classes, and can be accessed by calling:
 */
public class AchievementInfo {

    private Context context;

    // Constructor
    public AchievementInfo(Context context) {
        this.context = context;
    }


    /**
     * Save Achievement data to the App's SharedPreferences.
     * @param achievement
     */
    public void saveAchievementInfo(Achievement achievement){
        String week = achievement.getWeek();
        String rating = achievement.getMetal().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> achievements = prefs.getStringSet("achievements", null);
        if(achievements == null) {
            achievements = new HashSet<>();
        }
        achievements.add(week+ "," + rating);
        editor.putStringSet("achievements", achievements);
        editor.apply();
        Log.d("AchievementInfo", "Week: " + week + "/nRating: " + rating);
    }

    /**
     * Get all achievements
     * @return
     */
    public ArrayList<Achievement> getAchievements() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> achievements = prefs.getStringSet("achievements", null);
        ArrayList<Achievement> achievementList = new ArrayList<>();
        for(String s : achievements) {
            String[] split = s.split(",");
            String week = split[0];
            String rating = split[1];
            achievementList.add(new Achievement(week, Achievement.Metal.valueOf(rating)));
        }
        return achievementList;
    }

    /**
     * Get most recent achievement from the past week of events.
     * @param user_id
     * @param consumer
     */
    public void getMostRecentAchievement(String user_id, Consumer<Achievement> consumer) {
        Log.d("AchievementInfo", "GETTING MOST RECENT");
        EventDatabase.getEventsSince(EventDatabase.TimeDifference.WEEK, user_id, eventList -> {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            String week = dateFormat.format(cal.getTime());

            double percentage = getAlarmPercentage(eventList);
            Achievement a = new Achievement(week, Achievement.Metal.NONE);

            if (percentage >= .99 ) {
                a.setMetal(Achievement.Metal.GOLD);
            } else if (percentage >= .9) {
                a.setMetal(Achievement.Metal.SILVER);
            } else if (percentage >= .75) {
                a.setMetal(Achievement.Metal.BRONZE);
            }
            consumer.callback(a);
        });
    }

    private static double getAlarmPercentage(List<Event> eventList) {
        double total = eventList.size();
        double wakeupCounter = total;
        for(Event e : eventList) {
            if (e.getAction().equals("Oversleep")){
                wakeupCounter--;
            }
        }
        return wakeupCounter/total;
    }

}