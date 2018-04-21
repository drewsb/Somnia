package com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.RecordActivity;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;
import com.socialarm.a350s18_5_socialalarmclock.R;
import static com.socialarm.a350s18_5_socialalarmclock.Helper.GetPathFromURI.getPathFromURI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Response extends AppCompatActivity {

    private String my_id;
    private String other_id;
    private String ringtone_path;

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
        final String eventType = i.getStringExtra("type");
        final String time = Alarm.getTime(minute, hour);

        final TextView header = findViewById(R.id.response_header);
        boolean snooze = eventType.equalsIgnoreCase("snooze");
        header.setText("A Friend just " + (snooze?"snoozed":"overslept") + "\ntheir " + time + " alarm");
        UserDatabase.getUser(other_id, user -> {
            header.setText(user.getFirst_name() + " " + user.getLast_name() +
                    " just " + (snooze?"snoozed":"overslept") + "\ntheir " + time + " alarm");
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        my_id = prefs.getString("id", null);
        RadioGroup rg = findViewById(R.id.response_type);
        rg.check(R.id.basic_retrigger);

        // Set the visible options depending on which check box is selected.
        rg.setOnCheckedChangeListener((RadioGroup radioGroup, int checkedId) -> {
            Button select = findViewById(R.id.select_response);
            Button record = findViewById(R.id.record_response);
            EditText message = findViewById(R.id.message_response);
            switch (checkedId) {
                case R.id.basic_retrigger:
                    setVisibility(select, false);
                    setVisibility(record, false);
                    setVisibility(message, false);
                    break;
                case R.id.voice_retrigger:
                    setVisibility(select, true);
                    setVisibility(record, true);
                    setVisibility(message, false);
                    break;
                case R.id.message_retrigger:
                    setVisibility(select, false);
                    setVisibility(record, false);
                    setVisibility(message, true);
                    break;
            }
        });

        Button select = findViewById(R.id.select_response);
        Button record = findViewById(R.id.record_response);
        EditText message = findViewById(R.id.message_response);

        setVisibility(select, false);
        setVisibility(record, false);
        setVisibility(message, false);
    }

    /**
     * Sets whether a givin view should be available for interaction.
     * Hides and disables it.
     * @param v The view to affect
     * @param should_show Whether to enable or disable the view.
     */
    private void setVisibility(View v, boolean should_show) {
        if (should_show) {
            v.setVisibility(View.VISIBLE);
            v.setClickable(true);
            v.setFocusableInTouchMode(true);
        } else {
            v.setVisibility(View.INVISIBLE);
            v.setClickable(false);
            v.setFocusable(false);
        }
    }

    /**
     * Called to collect data from activity and send to other user.
     * @param view not used
     */
    public void sendWakeup(View view) {
        RadioGroup rg = findViewById(R.id.response_type);
        int selected_id = rg.getCheckedRadioButtonId();
        switch (selected_id) {
            case R.id.basic_retrigger:
                sendMessage(new HashMap<>());
                break;
            case R.id.voice_retrigger:
                if (ringtone_path == null) {
                    Toast.makeText(this, "Must select an audio source to upload", Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(ringtone_path);

                // Get a reference to a firebase storage file for the audio clip.
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference root = storage.getReference();
                StorageReference user = root.child(my_id);
                StorageReference file = user.child(f.getName());
                UploadTask uploadTask = file.putFile(Uri.fromFile(f));

                // Create a progress dialog so the user knows what is going on.
                ProgressDialog pd = new ProgressDialog(this);
                pd.setTitle("Uploading Media");
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMax((int)uploadTask.getSnapshot().getTotalByteCount());
                pd.show();

                // Handle status of the upload.
                uploadTask.addOnProgressListener(taskSnapshot ->  {
                    long progress = taskSnapshot.getBytesTransferred();
                    pd.setProgress((int)progress);
                });
                uploadTask.addOnCompleteListener(taskSnapshot -> {
                    pd.hide();
                    pd.dismiss();
                    if (taskSnapshot.isSuccessful()) {
                        Map<String, Object> extra_data = new HashMap<>();
                        extra_data.put("audio", file.getPath());
                        sendMessage(extra_data);
                    }
                });
                uploadTask.addOnFailureListener((Exception exception) -> {
                    Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show();
                });
                break;
            case R.id.message_retrigger:
                EditText editText = findViewById(R.id.message_response);
                String message = editText.getText().toString();
                Map<String, Object> extra_data = new HashMap<>();
                extra_data.put("message", message);
                sendMessage(extra_data);
                break;
        }

    }

    /**
     * Creates a messageSender and sends a direct message.
     * @param extra_data required for complex retriggers
     */
    private void sendMessage(Map<String, Object> extra_data) {
        MessageSender ms = new MessageSender();
        ms.sendDirect(other_id, "alarm", extra_data);
        finish();
    }

    /**
     * OnClickListener for when a user clicks the record button and brings up the recording page
     * @param view not used
     */
    public void onGoToRecordClick(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivityForResult(i, RecordActivity.RECORD_SUCCESS);
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

        //store song url if song is found
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SOUND) {
                Uri uri = data.getData();

                ringtone_path = getPathFromURI(getApplicationContext(), uri);
            } else if (requestCode == RecordActivity.RECORD_SUCCESS) {
                ringtone_path = data.getStringExtra(RecordActivity.RECORDING_PATH_STRING);
            }
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
