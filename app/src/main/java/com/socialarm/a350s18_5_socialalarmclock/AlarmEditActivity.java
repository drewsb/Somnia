package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AlarmEditActivity extends AppCompatActivity {

    AlarmsOpenHelper dbHelper;

    private int days_of_week;

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

    public void onCancel(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    public void onAccept(View view) {
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        long row = dbHelper.addAlarm(picker.getCurrentHour(), picker.getCurrentMinute(), days_of_week);

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
