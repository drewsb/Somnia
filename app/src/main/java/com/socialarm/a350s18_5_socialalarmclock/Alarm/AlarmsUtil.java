package com.socialarm.a350s18_5_socialalarmclock.Alarm;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 3/14/2018.
 * This class has utilities for interacting with local alarms.
 */

public class AlarmsUtil {
    /**
     * Gets the next trigger time for an alarm with given parameters
     * @param hour when an alarm will go off
     * @param minute when an alarm will go off
     * @param days_of_week what day of the week an alarm will go off.
     * @return a Calendar based on milliseconds for the next trigger time
     */
    static public Calendar getNextTrigger(int hour, int minute, int days_of_week) {
        Set<Integer> calendar_days = getCalendarDays(days_of_week);

        Calendar trigger_time = Calendar.getInstance();
        trigger_time.set(Calendar.HOUR_OF_DAY, hour);
        trigger_time.set(Calendar.MINUTE, minute);
        trigger_time.set(Calendar.SECOND, 0);
        Calendar now = Calendar.getInstance();
        if (trigger_time.before(now)) {
            trigger_time.add(Calendar.DAY_OF_MONTH, 1); // Set for tomorrow
        }

        for (int i = 0; i < 7; ++i) {
            if (!calendar_days.contains(trigger_time.get(Calendar.DAY_OF_WEEK))) {
                trigger_time.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                break;
            }
        }

        return trigger_time;
    }

    /**
     * Gets the next trigger time after today.
     * @param hour when an alarm will go off
     * @param minute when an alarm will go off
     * @param days_of_week what day of the week an alarm will go off.
     * @return a Calendar based on milliseconds for the next trigger time
     */
    static public Calendar skipNextTrigger(int hour, int minute, int days_of_week) {
        Calendar trigger_time = getNextTrigger(hour, minute, days_of_week);
        Set<Integer> calendar_days = getCalendarDays(days_of_week);

        trigger_time.add(Calendar.DAY_OF_MONTH, 1); // Set for tomorrow

        for (int i = 0; i < 7; ++i) {
            if (!calendar_days.contains(trigger_time.get(Calendar.DAY_OF_WEEK))) {
                trigger_time.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                break;
            }
        }

        return trigger_time;

    }

    /**
     * Converts a binary encoded days of week to a set for easier manipulation
     * @param days_of_week binary encoded day of week
     * @return All the days the alarm is set to trigger.
     */
    static Set<Integer> getCalendarDays(int days_of_week) {
        Set<Integer> calendar_days = new HashSet<Integer>();
        if ((days_of_week & AlarmsOpenHelper.SUNDAY) != 0) {
            calendar_days.add(Calendar.SUNDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.MONDAY) != 0) {
            calendar_days.add(Calendar.MONDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.TUESDAY) != 0) {
            calendar_days.add(Calendar.TUESDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.WEDNESDAY) != 0) {
            calendar_days.add(Calendar.WEDNESDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.THURSDAY) != 0) {
            calendar_days.add(Calendar.THURSDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.FRIDAY) != 0) {
            calendar_days.add(Calendar.FRIDAY);
        }
        if ((days_of_week & AlarmsOpenHelper.SATURDAY) != 0) {
            calendar_days.add(Calendar.SATURDAY);
        }

        return calendar_days;
    }
}
