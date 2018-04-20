package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AchievementActivity extends AppCompatActivity {

    private LinearLayout achievement_layout;
    private Drawable bronze_star;
    private Drawable silver_star;
    private Drawable gold_star;

    final double BRONZE_THRESHOLD = 0.75;
    final double SILVER_THRESHOLD = 0.90;
    final double GOLD_THRESHOLD = 0.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        //load resources
        achievement_layout = (LinearLayout)findViewById(R.id.achievement_layout);
        bronze_star = getResources().getDrawable(R.drawable.bronze_star);
        silver_star = getResources().getDrawable(R.drawable.silver_star);
        gold_star = getResources().getDrawable(R.drawable.gold_star);

        //will be replaced will actual calls to draw achievements (Drew is working on this)
        DrawAchievementForWeek(new Date(), 1.0);
        DrawAchievementForWeek(new Date(), 0.96);
        DrawAchievementForWeek(new Date(), 0.80);
        DrawAchievementForWeek(new Date(), 0.60);
    }

    /**
     * Draws badge and week text and appends it to the view
     * @param beginning_date Start date of achivement
     * @param sleep_percentage Sleep percentage (used to fetch appropriate badge picture)
     */
    private void DrawAchievementForWeek(Date beginning_date, double sleep_percentage) {

        //draw badge
        ImageView image = new ImageView(this);

        if(sleep_percentage > GOLD_THRESHOLD) {
            image.setImageDrawable(gold_star);
        } else if(sleep_percentage > SILVER_THRESHOLD) {
            image.setImageDrawable(silver_star);
        } else if(sleep_percentage > BRONZE_THRESHOLD) {
            image.setImageDrawable(bronze_star);
        }

        achievement_layout.addView(image);

        //draw text
        TextView week_text = new TextView(this);
        week_text.setTextSize(20);
        week_text.setGravity(Gravity.CENTER_HORIZONTAL);
        String beginning_date_string =
                new SimpleDateFormat("MM/dd/yyyy").format(beginning_date);
        week_text.setText(beginning_date_string);

        achievement_layout.addView(week_text);
    }
}
