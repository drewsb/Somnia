package com.socialarm.a350s18_5_socialalarmclock.Alarm;

/**
 * Created by drewboyette on 3/13/18.
 */

import java.util.Calendar;

/**
 * Holds information for every alarm. Used to push data to the Firebase database
 */
public class Alarm {

    private static final double MS_IN_DAY =  8.64 * Math.pow(10, 7);
    private static final double MS_IN_HOUR =  3.6 * Math.pow(10, 6);
    private static final double MS_IN_MIN = 60000.0;
    private static final double MAX_TIME = getTimeSinceWeekOrigin(Calendar.SATURDAY, 23, 59);

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

    public int getIntDayOfWeek() {
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
        result = 31 * result + getSnooze_interval();
        result = 31 * result + getVolume();
        return result;
    }

    public double getTimeUntilAlarm(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);

        double todayTime = getTimeSinceWeekOrigin(day, hour, min);
        double alarmTime = getTimeSinceWeekOrigin(getCalendarDayOfWeek(), this.getHour(), this.getMin());

        return alarmTime - todayTime >= 0 ? alarmTime - todayTime : MAX_TIME + (alarmTime - todayTime);
    }


    public static double getTimeSinceWeekOrigin(int day, int hour, int min) {
        return day * MS_IN_DAY + hour * MS_IN_HOUR + min * MS_IN_MIN;
    }

    public int getCalendarDayOfWeek(){
        switch(this.day_of_week) {
            case "Sunday" :
                return Calendar.SUNDAY;
            case "Monday" :
                return Calendar.MONDAY;
            case "Tuesday" :
                return Calendar.TUESDAY;
            case "Wednesday" :
                return Calendar.WEDNESDAY;
            case "Thursday" :
                return Calendar.THURSDAY;
            case "Friday" :
                return Calendar.FRIDAY;
            case "Saturday" :
                return Calendar.SATURDAY;
        }
        return 0;
    }

    public static String getTime(int min, int hour) {
        return String.format("%d:%02d", hour, min);
    }

}
