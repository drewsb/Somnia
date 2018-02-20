package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

public class AlarmEditActivity extends AppCompatActivity {

    AlarmsOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        picker.setIs24HourView(DateFormat.is24HourFormat(this));

        dbHelper = new AlarmsOpenHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        picker.setIs24HourView(DateFormat.is24HourFormat(this));
    }

    protected void onCancel(final View v) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    protected void onAccept(final View v) {
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        long row = dbHelper.addAlarm(picker.getCurrentHour(), picker.getCurrentMinute());

        Intent i = new Intent();
        i.putExtra("new_alarm", row);
        setResult(RESULT_OK, i);
        finish();
    }
}
