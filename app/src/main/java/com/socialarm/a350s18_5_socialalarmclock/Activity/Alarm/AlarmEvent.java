package com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.AlarmsOpenHelper;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.AlarmsUtil;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.LocalDBContract;
import com.socialarm.a350s18_5_socialalarmclock.Database.ChallengeDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.ChallengeReceiver;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.MessageSender;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Main.LoginActivity;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private int mathProblemAnswer;
    private boolean isMathProblemSolved;

    private int volume;
    private String ringtone_path;
    private String message;

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

        if (alarm_id == -1) {
            Button snooze = findViewById(R.id.snooze_button);
            snooze.setClickable(false);
            snooze.setFocusable(false);
            snooze.setVisibility(View.INVISIBLE);
        }

        // create the math problem itself
        TextView mathProblem = (TextView) findViewById(R.id.mathProblem);
        int mathProblemFirstNumber = (int) (Math.random() * 100);
        int mathProblemSecondNumber = (int) (Math.random() * 100);
        this.mathProblemAnswer = mathProblemFirstNumber + mathProblemSecondNumber;
        String problemText = mathProblemFirstNumber + " + " + mathProblemSecondNumber + " = ";
        mathProblem.setText(problemText);

        // create the countdown timer thing
        TextView countdown = (TextView) findViewById(R.id.timeRemaining);
        isMathProblemSolved = false;
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = "Seconds remaining: " + millisUntilFinished / 1000;
                countdown.setText(text);
            }

            public void onFinish() {
                // trigger the onOversleep
                if (!isMathProblemSolved) {
                    onOversleep();
                }
            }

        }.start();

        if (message != null && !message.equals("")) {
            TextView message_view = findViewById(R.id.message_view);
            message_view.setText(message);
        }

        Context applicationContext = LoginActivity.getContextOfApplication();
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        Uri alarm = Uri.parse(ringtone_path);
        if (ringtone_path.isEmpty()) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        //create media player
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
        if (alarm_id != -1) {
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
            ringtone_path = cursor.getString(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_RINGTONE_PATH));
        } else {
            ringtone_path = i.getStringExtra("audio");
            if (ringtone_path == null) {
                ringtone_path = "";
            }
            message = i.getStringExtra("message");
            if (message == null) {
                message = "";
            }
            volume = 10;
        }
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
        Map<String, Object> data = new HashMap<>();
        data.put("hour", ""+hour);
        data.put("minute", ""+minute);
        ms.notifyFriends("snooze", data);

        dbHelper.setSnooze(alarm_id, current_snooze_count+1);

        long time = System.currentTimeMillis() + snooze_interval*60*1000;
        Intent intent = new Intent(this, AlarmEvent.class);
        intent.putExtra("Alarm", alarm_id);
        intent.setType("Alarm"+alarm_id);
        PendingIntent reAlarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(time, reAlarm), reAlarm);

        ChallengeDatabase.cancelChallenges(user_id);

        dbHelper.close();
        finish();
    }

    /**
     * Trigger the oversleep and set an alarm for the next valid time
     */
    protected void onOversleep() {
        media.stop();

        // Create Event instance
        String user_id = prefs.getString("id", null);
        String dayOfWeek = dbHelper.getDayOfWeek(days_of_week);
        Alarm alarm = new Alarm(user_id, minute, hour, dayOfWeek, snooze_count, snooze_interval, volume);
        int alarmId = alarm.hashCode();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Event event = new Event("Oversleep", "" + alarmId, user_id, user_id + ts, tsLong);
        EventDatabase.addEvent(event);

        MessageSender ms = new MessageSender();
        Map<String, Object> data = new HashMap<>();
        data.put("hour", ""+hour);
        data.put("minute", ""+minute);
        ms.notifyFriends("oversleep", data);

        if (alarm_id != -1) {
            setNextAlarm();
            dbHelper.setSnooze(alarm_id, 0);
        } else {
            new File(ringtone_path).delete();
        }

        dbHelper.close();
        finish();
    }

    /**
     * Tigger a wake up event and set an alarm for the next valid time
     */
    protected void onWakeUp() {
        media.stop();

        // Create Event instance
        String user_id = prefs.getString("id", null);
        String dayOfWeek = dbHelper.getDayOfWeek(days_of_week);
        Alarm alarm = new Alarm(user_id, minute, hour, dayOfWeek, snooze_count, snooze_interval, volume);
        int alarmId = alarm.hashCode();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Event event = new Event("Wakeup", "" + alarmId, user_id, user_id + ts, tsLong);
        EventDatabase.addEvent(event);

        MessageSender ms = new MessageSender();
        Map<String, Object> data = new HashMap<>();
        data.put("hour", ""+hour);
        data.put("minute", ""+minute);
        ms.notifyFriends("wakeup", data);

        if (alarm_id != -1) {
            setNextAlarm();
            dbHelper.setSnooze(alarm_id, 0);
        } else {
            new File(ringtone_path).delete();
        }

        dbHelper.close();
        finish();
    }

    /**
     * Check to see if the user has entered the correct answer for the math problem
     */
    protected void onEnter(View view) {
        EditText answer = (EditText) findViewById(R.id.answerBox);
        String answerString = answer.getText().toString();
        try {
            int answerNumber = Integer.parseInt(answerString);
            if (answerNumber == this.mathProblemAnswer) {
                isMathProblemSolved = true;
                Toast.makeText(getApplicationContext(), "Correct! You've woken up!",
                        Toast.LENGTH_LONG).show();
                onWakeUp();
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect, Try again",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Incorrect, Try again",
                    Toast.LENGTH_SHORT).show();
        }
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
