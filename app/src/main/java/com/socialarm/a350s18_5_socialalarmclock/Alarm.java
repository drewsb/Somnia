package com.socialarm.a350s18_5_socialalarmclock;

/**
 * Created by drewboyette on 3/13/18.
 */


import java.util.Calendar;

/**
 * Holds information for every alarm. Used to push data to the Firebase database
 */
public class Alarm {

    private static final int maxTime = getTimeSinceWeekOrigin(Calendar.SATURDAY, 23, 59);

    String user_id;
    int min;
    int hour;
    String day_of_week;
    int snooze_count;
    int snooze_interval;
    int volume;
    String time;

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

    public String getTime() {
        if (time == null) {
            if(min < 10) {
                return hour + ":0" + min;
            }
            return hour + ":" + min;
        }
        return time;
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
        this.time = hour + ":" + min;
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

    public Integer getTimeUntilAlarm(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);

        Integer todayTime = getTimeSinceWeekOrigin(day, hour, min);
        Integer alarmTime = getTimeSinceWeekOrigin(getCalendarDayOfWeek(), this.getHour(), this.getMin());

        return alarmTime - todayTime >= 0 ? alarmTime - todayTime : maxTime + (alarmTime - todayTime);
    }


    public static Integer getTimeSinceWeekOrigin(int day, int hour, int min) {
        return Double.valueOf(day * 8.64 * Math.pow(10, 7) + hour * 3.6 * Math.pow(10, 6)
                + min * 60000).intValue();
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

}
