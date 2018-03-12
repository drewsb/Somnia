package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //Set profile pic image
        new DownloadImageTask((ImageView) findViewById(R.id.profileView))
                .execute(extras.getString("profile_pic"));

        //Retrieve user's name and email
        String name = extras.getString("first_name") + " " + extras.getString("last_name");
        String email = extras.getString("email");

        TextView nameView =  findViewById(R.id.nameView);
        TextView emailView = findViewById(R.id.emailView);

        nameView.setText(name);
        emailView.setText(email);

        //set number statistics
        TextView statisticsView = findViewById(R.id.statisticsView);
        statisticsView.append("Average wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");
        statisticsView.append("Wake up time: " + "" +"\n");

        

    }

    //(FIX, THIS IS IN MAINACTIIVITY)
    /*
    DownloadImageTask is a private class used to convert URL's into a Bitmap asynchronously
 */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
