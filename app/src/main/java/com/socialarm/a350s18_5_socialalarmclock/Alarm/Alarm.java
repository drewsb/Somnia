package com.socialarm.a350s18_5_socialalarmclock.Alarm;

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

    /**
     * @param o
     * @return Return true if the two Alarm objects have the same fields, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alarm)) return false;

        Alarm alarm = (Alarm) o;

        if (getMin() != alarm.getMin()) return false;
        if (getHour() != alarm.getHour()) return false;
        if (getSnooze_count() != alarm.getSnooze_count()) return false;
        if (getSnooze_interval() != alarm.getSnooze_interval()) return false;
        if (getVolume() != alarm.getVolume()) return false;
        if (!getUser_id().equals(alarm.getUser_id())) return false;
        return getDay_of_week().equals(alarm.getDay_of_week());
    }

    /**
     * Custom implemention of the Alarm object's hashcode. Helps distinguish different alarms, and
     * alarms with the same values will have the same hashcode.
     * @return
     */
    @Override
    public int hashCode() {
        int result = getUser_id().hashCode();
        result = 31 * result + getMin();
        result = 31 * result + getHour();
        result = 31 * result + getDay_of_week().hashCode();
        result = 31 * result + getSnooze_count();
        result = 31 * result + getSnooze_interval();
        result = 31 * result + getVolume();
        return result;
    }
}
