package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;


public class AlarmsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
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
      LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK + " TINYINT);";
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

    public long addAlarm(int hour, int minute, int active_alarms) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "INSERT INTO " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "(" + LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_MINUTE + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_ENABLED + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK + ") VALUES (?, ?, ?, ?)";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, hour);
        stmt.bindLong(2, minute);
        stmt.bindLong(3, 0);
        stmt.bindLong(4, active_alarms);
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
