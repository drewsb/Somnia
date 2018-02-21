package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;


public class AlarmsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "somnia.db";

    private static final String CREATE_ALARMS_TABLE =
      "CREATE TABLE " + LocalDBContract.Alarm.TABLE_NAME + " (" +
      LocalDBContract.Alarm._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      LocalDBContract.Alarm.COLUMN_NAME_HOUR + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_MINUTE + " INTEGER, " +
      LocalDBContract.Alarm.COLUMN_NAME_ENABLED + " TINYINT);";
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
        //db_read.close();
        return c;
    }

    public long addAlarm(int hour, int minute) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "INSERT INTO " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "(" + LocalDBContract.Alarm.COLUMN_NAME_HOUR + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_MINUTE + ", " +
                       LocalDBContract.Alarm.COLUMN_NAME_ENABLED + ") VALUES (?, ?, ?)";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, hour);
        stmt.bindLong(2, minute);
        stmt.bindLong(3, 0);
        long ret = stmt.executeInsert();
        //db_write.close();
        return ret;
    }

    public int setActive(long row_id, boolean enable) {
        SQLiteDatabase db_write = getWritableDatabase();
        String query = "UPDATE " + LocalDBContract.Alarm.TABLE_NAME + " " +
                       "SET " + LocalDBContract.Alarm.COLUMN_NAME_ENABLED + "=?" +
                       "WHERE " + LocalDBContract.Alarm._ID + "=?";
        SQLiteStatement stmt = db_write.compileStatement(query);
        stmt.bindLong(1, enable ? 1 : 0);
        stmt.bindLong(2,row_id);
        int r = stmt.executeUpdateDelete();
        //db_write.close();
        return r;
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
