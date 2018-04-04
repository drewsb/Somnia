package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;


/*
 * This class has utility methods for interacting with the local Alarms db.
 */
public class AlarmsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "somnia.db";

    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 4;
    public static final int WEDNESDAY = 8;
    public static final int THURSDAY = 16;
    public static final int FRIDAY = 32;
    public static final int SATURDAY = 64;

    private static final String CREATE_ALARMS_TABLE =
      "CREATE TABLE " + LocalDBContract.Alarm.TABLE_NAME + " (" +
      LocalDBContract.Alarm._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      LocalDBContract.Alarm.COLUMN_NAME_HOUR + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_MINUTE + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_ENABLED + " TINYINT, " +
      LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK + " TINYINT, " +
      LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_COUNT + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_INTERVAL + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_CURRENT_SNOOZE_COUNT + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_VOLUME + " INTEGER);";

    private static final String DELETE_ALARMS_TABLE =
       "DROP TABLE IF EXISTS " + LocalDBContract.Alarm.TABLE_NAME;

    public AlarmsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns the day of week from the given int
     * @param day
     * @return
     */
    public String getDayOfWeek(int day){
        switch(day) {
            case 1 :
                return "Sunday";
            case 2 :
                return "Monday";
            case 4 :
                return "Tuesday";
            case 8 :
                return "Wednesday";
            case 16 :
                return "Thursday";
            case 32 :
                return "Friday";
            case 64 :
                return "Saturday";
        }
        return "";
    }

    /**
     * Get all alarms from the database.
     * @return Cursor for all alarms
     */
    public Cursor getAlarms() {
        SQLiteDatabase db_read = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LocalDBContract.Alarm.TABLE_NAME);
        String sort = LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " + LocalDBContract.Alarm.COLUMN_NAME_MINUTE;
        Cursor c = qb.query(db_read, null, null, null, null, null, sort);
        return c;
    }

    /**
     * Get a specific alarm from id.
     * @param alarm_id the id to find.
     * @return Cursor containing a single alarm
     */
    public Cursor getAlarm(int alarm_id) {
        SQLiteDatabase db_read = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LocalDBContract.Alarm.TABLE_NAME);
        String where = LocalDBContract.Alarm._ID + " = " + alarm_id;
        qb.appendWhere(where);
        Cursor c = qb.query(db_read, null, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    /**
     * Add an alarm to the database.
     * @param hour hr of alarm
     * @param minute min of alarm
     * @param active_alarms days of week this alarm is active
     * @param snooze_interval how long between each snooze
     * @param snooze_count how many snoozes
     * @param volume how loud to play an alarm
     * @return the id of the new alarm
     */
    public long addAlarm(int hour, int minute, int active_alarms, int snooze_interval,
                         int snooze_count, int volume) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "INSERT INTO " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "(" + LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_MINUTE + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_ENABLED + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_INTERVAL + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_COUNT  + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_VOLUME + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, hour);
        stmt.bindLong(2, minute);
        stmt.bindLong(3, 0);
        stmt.bindLong(4, active_alarms);
        stmt.bindLong(5, snooze_interval);
        stmt.bindLong(6, snooze_count);
        stmt.bindLong(7, volume);
        long ret = stmt.executeInsert();

        return ret;
    }

    /**
     * Sets the current snooze count for a given alarm
     * @param row_id alarm to be modified.
     * @param snooze_count count to be set.
     */
    public void setSnooze(long row_id, int snooze_count) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "UPDATE " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "SET " + LocalDBContract.Alarm.COLUMN_NAME_CURRENT_SNOOZE_COUNT + "=? " +
                       "WHERE " + LocalDBContract.Alarm._ID + "+?";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, snooze_count);
        stmt.bindLong(2, row_id);
        stmt.executeUpdateDelete();
    }

    /**
     * Sets whether an alarm is active
     * @param row_id alarm to be modified
     * @param enable whether an alarm is active
     */
    public void setActive(long row_id, boolean enable) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "UPDATE " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "SET " + LocalDBContract.Alarm.COLUMN_NAME_ENABLED + "=? " +
                       "WHERE " + LocalDBContract.Alarm._ID + "=?";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, enable ? 1 : 0);
        stmt.bindLong(2, row_id);
        stmt.executeUpdateDelete();
    }

    /**
     * Reset the database
     * @param db database to reset
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DELETE_ALARMS_TABLE);

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    /**
     * Should modify the database depending on versions.
     * Resets the database
     * @param sqLiteDatabase database to update
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

    /**
     * Should modify the database depending on versions.
     * Resets the database
     * @param sqLiteDatabase database to update
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

}
