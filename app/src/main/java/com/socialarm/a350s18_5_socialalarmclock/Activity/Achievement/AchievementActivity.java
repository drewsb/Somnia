package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialarm.a350s18_5_socialalarmclock.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private LinearLayout achievement_layout;
    private Drawable bronze_star;
    private Drawable silver_star;
    private Drawable gold_star;
    private GridView achievementGrid;

    final double BRONZE_THRESHOLD = 0.75;
    final double SILVER_THRESHOLD = 0.90;
    final double GOLD_THRESHOLD = 0.99;

    List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        achievementGrid = findViewById(R.id.achievementGrid);
    }

    /**
     * Setups all the achievments and reads from db to see if it has been collected
     */
    private void setupAchievements() {
        Resources resources = getResources();

        Drawable bronze_star = resources.getDrawable(R.drawable.bronze_star);
        Drawable silver_star = resources.getDrawable(R.drawable.silver_star);
        Drawable gold_star = resources.getDrawable(R.drawable.gold_star);
        Drawable coffee = resources.getDrawable(R.drawable.coffee);
        Drawable dream = resources.getDrawable(R.drawable.dream);
        Drawable cookie = resources.getDrawable(R.drawable.cookie);
        Drawable ghost = resources.getDrawable(R.drawable.ghost);
        Drawable sleeping = resources.getDrawable(R.drawable.sleeping);
        Drawable night = resources.getDrawable(R.drawable.night);
        Drawable night_1 = resources.getDrawable(R.drawable.night_1);
        Drawable night_2 = resources.getDrawable(R.drawable.night_2);
        Drawable hot_drink = resources.getDrawable(R.drawable.hot_drink);
        Drawable pijamas = resources.getDrawable(R.drawable.pijamas);
        Drawable pijamas_1 = resources.getDrawable(R.drawable.pijamas_1);
        Drawable pillow = resources.getDrawable(R.drawable.pillow);
        Drawable slippers = resources.getDrawable(R.drawable.slippers);
        Drawable toothbrush = resources.getDrawable(R.drawable.toothbrush);

        //initialize achievments
        achievements = new ArrayList<Achievement>(){{
            add(new Achievement())
        }};
    }
}
