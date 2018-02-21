package com.socialarm.a350s18_5_socialalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

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
        //long time = System.currentTimeMillis() + 5*60*1000; // 5 minutes
        long time = System.currentTimeMillis() + 15*1000;
        Intent intent = new Intent(this, AlarmEvent.class);
        PendingIntent reAlarm = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP, time, reAlarm);
        finish();
    }

    protected void onDismiss(View view) {
        media.stop();
        finish();
    }
}
