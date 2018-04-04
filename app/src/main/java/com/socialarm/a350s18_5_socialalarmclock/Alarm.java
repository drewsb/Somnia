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
    int volume;

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

    public int getInt_Day_of_week() {
        switch(day_of_week) {
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 4;
            case "Wednesday":
                return 8;
            case "Thursday":
                return 16;
            case "Friday":
                return 32;
            case "Saturday":
                return 64;
        }
        return -1;
    }

    public int getSnooze_count() {
        return snooze_count;
    }

    public int getSnooze_interval() {
        return snooze_interval;
    }

    public int getVolume(){ return volume; }

    public String getTime() {
        return hour + ":" + min;
    }


    public Alarm() {}

    public Alarm(String user_id, int min, int hour, String day_of_week, int snooze_count, int snooze_interval, int volume){
        this.user_id = user_id;
        this.min = min;
        this.hour = hour;
        this.day_of_week = day_of_week;
        this.snooze_count = snooze_count;
        this.snooze_interval = snooze_interval;
        this.volume = volume;
    }
}
