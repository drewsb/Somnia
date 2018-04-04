package com.socialarm.a350s18_5_socialalarmclock;

import android.provider.BaseColumns;

/**
 * Created by Alex on 2/20/2018.
 */

public class LocalDBContract {
    public static final class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "my_alarms";
        public static final String COLUMN_NAME_HOUR = "hour";
        public static final String COLUMN_NAME_MINUTE = "minute";
        public static final String COLUMN_NAME_ENABLED = "enabled";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_NAME_RINGTONE_PATH = "ringtone_path";
    }
}
