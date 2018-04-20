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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.RecordActivity;
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
        final String time = String.format("%2d:%2d", hour, minute);

        final TextView header = findViewById(R.id.response_header);
        header.setText("A Friend just snoozed\ntheir " + time + " alarm");
        UserDatabase.getUser(other_id, user -> {
            header.setText(user.getFirst_name() + " " + user.getLast_name() + " just snoozed\ntheir " + time + " alarm");
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        my_id = prefs.getString("id", null);
        RadioGroup rg = findViewById(R.id.response_type);
        rg.check(R.id.basic_retrigger);
    }

    public void sendWakeup(View view) {
        RadioGroup rg = findViewById(R.id.response_type);
        int selected_id = rg.getCheckedRadioButtonId();
        switch (selected_id) {
            case R.id.basic_retrigger:
                sendMessage(new HashMap<>());
                break;
            case R.id.voice_retrigger:
                if (ringtone_path == null) {
                    Toast.makeText(this, "Must select an audio souce to upload", Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(ringtone_path);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference root = storage.getReference();
                StorageReference user = root.child(my_id);
                StorageReference file = user.child(f.getName());
                UploadTask uploadTask = file.putFile(Uri.fromFile(f));
                ProgressDialog pd = new ProgressDialog(this);
                pd.setTitle("Uploading Media");
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMax((int)uploadTask.getSnapshot().getTotalByteCount());
                pd.show();
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
        }

    }

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

            ringtone_path = getPathFromURI(getApplicationContext(), uri);
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
