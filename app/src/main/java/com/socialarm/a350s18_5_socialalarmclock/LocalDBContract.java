package com.socialarm.a350s18_5_socialalarmclock;

import android.provider.BaseColumns;

/**
 * Created by Alex on 2/20/2018.
 * Definition of the local SQL database.
 */

public class LocalDBContract {
    public static final class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "my_alarms";
        public static final String COLUMN_NAME_HOUR = "hour";
        public static final String COLUMN_NAME_MINUTE = "minute";
        public static final String COLUMN_NAME_ENABLED = "enabled";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_NAME_SNOOZE_COUNT = "snooze_count";
        public static final String COLUMN_NAME_SNOOZE_INTERVAL = "snooze_interval";
        public static final String COLUMN_NAME_CURRENT_SNOOZE_COUNT = "current_snooze_count";
        public static final String COLUMN_NAME_VOLUME = "volume";
    }
}
