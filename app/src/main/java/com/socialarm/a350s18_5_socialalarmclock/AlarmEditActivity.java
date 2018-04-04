package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.io.IOException;

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

    final int RECORD_SOUND = 1;
    final int SELECT_SOUND = 2;

    public void onGoToRecordClick(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
        /// /Intent i = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        //startActivityForResult(i, RECORD_SOUND);
        //startActivity(i);
    }

    public void onSelectMusicPlayerClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_SOUND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //play the song if found
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RECORD_SOUND:

                    break;
                case SELECT_SOUND:

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    Uri audio_uri = data.getData();
                    try {
                        mediaPlayer.setDataSource(this, audio_uri);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        Log.e("SetDataSource", e.toString());
                    }

                    //mediaPlayer.start();

                    ringtone_path = audio_uri.toString();

                    mediaPlayer = new  MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(ringtone_path);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();

                    break;
                default:
                    break;
            }
        }
    }


}
