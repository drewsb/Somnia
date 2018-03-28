package com.socialarm.a350s18_5_socialalarmclock;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.socialarm.a350s18_5_socialalarmclock.LeaderBoardFragment.*;

/**
 * Created by drewboyette on 3/13/18.
 */


/**
 * Holds information for every alarm. Used to push data to the Firebase database
 */
public class Alarm {

    String user_id;
    int min;
    int hour;
    String period;
    int snooze_count;
    int snooze_interval;

    public String getUser_id(){
        return user_id;
    }

    public int getMin() {
        return min;
    }

    public int getHour() {
        return hour;
    }

    public String getPeriod() {
        return period;
    }

    public int getSnooze_count() {
        return snooze_count;
    }

    public int getSnooze_interval() {
        return snooze_interval;
    }

    public Alarm() {}

    public Alarm(String user_id, int min, int hour, String period, int snooze_count, int snooze_interval){
        this.user_id = user_id;
        this.min = min;
        this.hour = hour;
        this.period = period;
        this.snooze_count = snooze_count;
        this.snooze_interval = snooze_interval;
    }
}
