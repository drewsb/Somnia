package com.socialarm.a350s18_5_socialalarmclock;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

import static com.socialarm.a350s18_5_socialalarmclock.GetPathFromURI.getPathFromURI;

public class AlarmEditActivity extends AppCompatActivity {

    AlarmsOpenHelper dbHelper;

    private int days_of_week;
    private String ringtone_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        TimePicker picker = findViewById(R.id.alarm_time_picker);
        picker.setIs24HourView(DateFormat.is24HourFormat(this));

        dbHelper = new AlarmsOpenHelper(this);
        ringtone_path = new String();
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
        long row = dbHelper.addAlarm(picker.getCurrentHour(), picker.getCurrentMinute(), days_of_week, ringtone_path);

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

    final int SELECT_SOUND = 2;

    /**
     * OnClickListener for when a user clicks the record button and brings up the recording page
     * @param view not used
     */
    public void onGoToRecordClick(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }

    /**
     * OnClickListener for when a user clicks the select button and brings up the selection page
     * @param view not used
     */
    public void onSelectMusicPlayerClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_SOUND);
    }

    final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //play the song if found
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_SOUND:

                    Uri uri = data.getData();

                    RequestReadPermission();

                    ringtone_path = getPathFromURI(getApplicationContext(), uri);

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Request read permission from user
     */
    private void RequestReadPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }


}
