package com.socialarm.a350s18_5_socialalarmclock;

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
    String day_of_week;
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

    public String getDay_of_week() {return day_of_week;}

    public int getSnooze_count() {
        return snooze_count;
    }

    public int getSnooze_interval() {
        return snooze_interval;
    }

    public Alarm() {}

    public Alarm(String user_id, int min, int hour, String day_of_week, int snooze_count, int snooze_interval){
        this.user_id = user_id;
        this.min = min;
        this.hour = hour;
        this.day_of_week = day_of_week;
        this.snooze_count = snooze_count;
        this.snooze_interval = snooze_interval;
    }
}
