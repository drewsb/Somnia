package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;


public class AlarmsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
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
      LocalDBContract.Alarm.COLUMN_NAME_RINGTONE_PATH + " TEXT);";

    private static final String DELETE_ALARMS_TABLE =
       "DROP TABLE IF EXISTS " + LocalDBContract.Alarm.TABLE_NAME;

    public AlarmsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getAlarms() {
        SQLiteDatabase db_read = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LocalDBContract.Alarm.TABLE_NAME);
        String sort = LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " + LocalDBContract.Alarm.COLUMN_NAME_MINUTE;
        Cursor c = qb.query(db_read, null, null, null, null, null, sort);
        return c;
    }

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

    public long addAlarm(int hour, int minute, int active_alarms, String ringtone_path) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "INSERT INTO " + LocalDBContract.Alarm.TABLE_NAME + " " +
                "(" + LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " +
                LocalDBContract.Alarm.COLUMN_NAME_MINUTE + ", " +
                LocalDBContract.Alarm.COLUMN_NAME_ENABLED + ", " +
                LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK + ", " +
                LocalDBContract.Alarm.COLUMN_NAME_RINGTONE_PATH + ") VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, hour);
        stmt.bindLong(2, minute);
        stmt.bindLong(3, 0);
        stmt.bindLong(4, active_alarms);
        stmt.bindString(5, ringtone_path);
        long ret = stmt.executeInsert();
        return ret;
    }

    public void setActive(long row_id, boolean enable) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "UPDATE " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "SET " + LocalDBContract.Alarm.COLUMN_NAME_ENABLED + "=?" +
                       "WHERE " + LocalDBContract.Alarm._ID + "=?";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, enable ? 1 : 0);
        stmt.bindLong(2,row_id);
        stmt.executeUpdateDelete();
    }

    public void setRingtonePath(long row_id, String ringtone_path) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "UPDATE " + LocalDBContract.Alarm.TABLE_NAME + " " +
                "SET " + LocalDBContract.Alarm.COLUMN_NAME_RINGTONE_PATH + "=?" +
                "WHERE " + LocalDBContract.Alarm._ID + "=?";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(2, row_id);
        stmt.bindString(5, ringtone_path);
        stmt.executeUpdateDelete();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DELETE_ALARMS_TABLE);

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

}
