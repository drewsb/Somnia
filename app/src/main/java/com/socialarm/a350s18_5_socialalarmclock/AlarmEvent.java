package com.socialarm.a350s18_5_socialalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.MessageSender;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class AlarmEvent extends AppCompatActivity {

    private MediaPlayer media;

    private int alarm_id;

    private int hour;
    private int minute;
    private int enabled;
    private int days_of_week;

    private int snooze_count;
    private int snooze_interval;
    private int current_snooze_count;

    private int volume;

    AlarmsOpenHelper dbHelper;
    private SharedPreferences prefs;

    /**
     * Get the alarm and start ringing.
     * @param savedInstanceState Previous instantace state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        setContentView(R.layout.activity_alarm_event);

        Context applicationContext = LoginActivity.getContextOfApplication();
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarm == null) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        media = new MediaPlayer();
        float actualVolume = (float)Math.pow(((float)volume)/16, 2);
        media.setVolume(actualVolume, actualVolume);
        try {
            media.setDataSource(this, alarm);
            media.setLooping(true);
            media.prepare();
            media.start();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to start alarm ring", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Extract all the data from the local db.
     */
    private void initialize() {
        Intent i = getIntent();
        alarm_id = i.getIntExtra("Alarm", -1);
        dbHelper = new AlarmsOpenHelper(this);
        Cursor cursor = dbHelper.getAlarm(alarm_id);

        snooze_count = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_COUNT));
        snooze_interval = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_SNOOZE_INTERVAL));
        current_snooze_count = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_CURRENT_SNOOZE_COUNT));
        hour = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_HOUR));
        minute = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_MINUTE));
        enabled = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_ENABLED));
        days_of_week = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK));
        volume = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_VOLUME));
    }

    /**
     * Create a new alarm for a certain time in the future.
     * @param view snooze button
     */
    protected void onSnooze(View view) {
        media.stop();

        if (current_snooze_count >= snooze_count) {
            setNextAlarm();
            dbHelper.setSnooze(alarm_id, 0);
            dbHelper.close();
            finish();
        }

        // Create Event instance
        String user_id = prefs.getString("id", null);
        String dayOfWeek = dbHelper.getDayOfWeek(days_of_week);
        Alarm alarm = new Alarm(user_id, minute, hour, dayOfWeek, snooze_count, snooze_interval, volume);
        int alarmId = alarm.hashCode();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Event event = new Event("Snooze", "" + alarmId, user_id, user_id + ts, tsLong);
        EventDatabase.addEvent(event);

        MessageSender ms = new MessageSender();
        ms.notifyFriends("snooze", new HashMap<>());

        dbHelper.setSnooze(alarm_id, current_snooze_count+1);

        long time = System.currentTimeMillis() + snooze_interval*60*1000;
        Intent intent = new Intent(this, AlarmEvent.class);
        intent.putExtra("Alarm", alarm_id);
        intent.setType("Alarm"+alarm_id);
        PendingIntent reAlarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(time, reAlarm), reAlarm);

        dbHelper.close();
        finish();
    }

    /**
     * Set an alarm for the next valid time.
     * @param view dismiss button
     */
    protected void onDismiss(View view) {
        media.stop();
        Intent i = getIntent();
        int alarm_id = i.getIntExtra("Alarm", -1);
        AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(this);
        Cursor cursor = dbHelper.getAlarm(alarm_id);

        setNextAlarm();
        dbHelper.setSnooze(alarm_id, 0);

        dbHelper.close();

        finish();
    }

    /**
     * Compute the next valid time for the current alarm to ring.
     */
    private void setNextAlarm() {
        Calendar trigger_time = AlarmsUtil.getNextTrigger(hour, minute, days_of_week);
        long tTime = trigger_time.getTimeInMillis();
        Intent intent = new Intent(this, AlarmEvent.class);
        intent.putExtra("Alarm", alarm_id);
        intent.setType("Alarm"+alarm_id);
        PendingIntent Alarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(tTime, Alarm), Alarm);
    }
}
