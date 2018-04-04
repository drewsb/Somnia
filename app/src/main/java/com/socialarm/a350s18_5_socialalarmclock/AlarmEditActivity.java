package com.socialarm.a350s18_5_socialalarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AlarmEditActivity extends AppCompatActivity {

    AlarmsOpenHelper dbHelper;
    private SharedPreferences prefs;

    private int days_of_week;

    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        picker.setIs24HourView(DateFormat.is24HourFormat(this));

        NumberPicker interval = findViewById(R.id.interval_selector);
        interval.setMinValue(1);
        interval.setMaxValue(30);
        interval.setWrapSelectorWheel(true);
        interval.setValue(5);

        NumberPicker count = findViewById(R.id.snooze_count);
        count.setMinValue(1);
        count.setMaxValue(5);
        count.setWrapSelectorWheel(true);
        count.setValue(3);

        dbHelper = new AlarmsOpenHelper(this);
        Context applicationContext = LoginActivity.getContextOfApplication();
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        picker.setIs24HourView(DateFormat.is24HourFormat(this));
    }

    public void onCancel(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    public void onAccept(View view) {
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        NumberPicker interval = findViewById(R.id.interval_selector);
        NumberPicker count = findViewById(R.id.snooze_count);
        String user_id = prefs.getString("id", null);
        Alarm alarm = new Alarm(user_id, picker.getCurrentMinute(), picker.getCurrentHour(),
                dbHelper.getDayOfWeek(days_of_week), interval.getValue(), count.getValue());
        AlarmDatabase.addAlarm(alarm);
        SeekBar volume = findViewById(R.id.volume_slider);
        long row = dbHelper.addAlarm(picker.getCurrentHour(), picker.getCurrentMinute(),
                days_of_week, interval.getValue(), count.getValue(), volume.getProgress());

        Intent i = new Intent();
        i.putExtra("new_alarm", row);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onDayOfWeekClick(View view) {
        ToggleButton b = (ToggleButton) view;
        if (b.isChecked()) {
            b.setBackgroundColor(0xff999999);
        } else {
            b.setBackgroundColor(0xffffffff);
        }
        switch (b.getId()) {
            case R.id.DoWSelectSunday:
                days_of_week ^= AlarmsOpenHelper.SUNDAY;
                break;
            case R.id.DoWSelectMonday:
                days_of_week ^= AlarmsOpenHelper.MONDAY;
                break;
            case R.id.DoWSelectTuesday:
                days_of_week ^= AlarmsOpenHelper.TUESDAY;
                break;
            case R.id.DoWSelectWednesday:
                days_of_week ^= AlarmsOpenHelper.WEDNESDAY;
                break;
            case R.id.DoWSelectThursday:
                days_of_week ^= AlarmsOpenHelper.THURSDAY;
                break;
            case R.id.DoWSelectFriday:
                days_of_week ^= AlarmsOpenHelper.FRIDAY;
                break;
            case R.id.DoWSelectSaturday:
                days_of_week ^= AlarmsOpenHelper.SATURDAY;
                break;
        }
    }

}
