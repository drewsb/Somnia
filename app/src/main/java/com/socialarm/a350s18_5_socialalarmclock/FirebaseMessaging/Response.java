package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.RecordActivity;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.HashMap;

public class Response extends AppCompatActivity {

    String other_id;

    private static final int SELECT_SOUND = 2;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Intent i = getIntent();
        other_id = i.getStringExtra("user_id");
        final int hour = i.getIntExtra("hour", 0);
        final int minute = i.getIntExtra("minute", 0);
        final String time = String.format("%2d:%2d", hour, minute);

        final TextView header = findViewById(R.id.response_header);
        header.setText("A Friend just snoozed\ntheir " + time + " alarm");
        UserDatabase.getUser(other_id, user -> {
            header.setText(user.getFirst_name() + " " + user.getLast_name() + " just snoozed\ntheir " + time + " alarm");
        });
    }

    public void sendWakeup(View view) {
        MessageSender ms = new MessageSender();
        ms.sendDirect(other_id, "alarm", new HashMap<>());
    }

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
    public void onSelectMusicPlayClick(View view) {
        RequestReadPermission();
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_SOUND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //store song url if song is foud
        if (resultCode == RESULT_OK && requestCode == SELECT_SOUND) {
            Uri uri = data.getData();

            String ringtone_path = getPathFromURI(getApplicationContext(), uri);
        }
    }


    /**
     * Request read permission from user
     */
    private void RequestReadPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
}
