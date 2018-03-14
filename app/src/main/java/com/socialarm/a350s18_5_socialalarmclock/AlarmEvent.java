package com.socialarm.a350s18_5_socialalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.Calendar;

public class AlarmEvent extends AppCompatActivity {

    private MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_event);
        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarm == null) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        media = new MediaPlayer();
        try {
            media.setDataSource(this, alarm);
            media.setLooping(true);
            media.prepare();
        } catch (IOException e) {
        }
        media.start();
    }

    protected void onSnooze(View view) {
        media.stop();
        long time = System.currentTimeMillis() + 5*60*1000; // 5 minutes
        //long time = System.currentTimeMillis() + 15*1000;
        Intent intent = new Intent(this, AlarmEvent.class);
        intent.putExtra("Alarm", getIntent().getIntExtra("Alarm",-1));
        PendingIntent reAlarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //manager.setExact(AlarmManager.RTC_WAKEUP, time, reAlarm);
        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(time, reAlarm), reAlarm);
        finish();
    }

    protected void onDismiss(View view) {
        media.stop();
        Intent i = getIntent();
        int alarm_id = i.getIntExtra("Alarm", -1);
        AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(this);
        Cursor cursor = dbHelper.getAlarm(alarm_id);

        final int hour = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_HOUR));
        final int minute = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_MINUTE));
        final int enabled = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_ENABLED));
        final int days_of_week = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK));
        final int id = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm._ID));

        Calendar trigger_time = AlarmsUtil.getNextTrigger(hour, minute, days_of_week);
        long tTime = trigger_time.getTimeInMillis();
        Intent intent = new Intent(this, AlarmEvent.class);
        intent.putExtra("Alarm", id);
        PendingIntent Alarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(tTime, Alarm), Alarm);
        dbHelper.close();

        finish();
    }
}
