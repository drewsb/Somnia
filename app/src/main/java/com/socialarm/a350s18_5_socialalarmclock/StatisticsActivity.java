package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.socialarm.a350s18_5_socialalarmclock.Statistic.TimeDifference.MONTH;
import static com.socialarm.a350s18_5_socialalarmclock.Statistic.TimeDifference.WEEK;
import static com.socialarm.a350s18_5_socialalarmclock.Statistic.TimeDifference.YEAR;

public class StatisticsActivity extends AppCompatActivity {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        extras = i.getExtras();

        String id = extras.getString("idFacebook");

        Log.v("AYYO", id);
        Statistic.GetEvents(id, (events) -> Log.v("AAAAAAAA", events.toString()) );

        DrawUserInfo(extras);

        DrawNumberStats();

        DrawGraphs(WEEK);
    }

    private void DrawUserInfo(Bundle extras)
    {
        //Set profile pic image
        new DownloadImageTask((ImageView) findViewById(R.id.profileView))
                .execute(extras.getString("profile_pic"));

        //Retrieve user's name and email
        String name = extras.getString("first_name") + " " + extras.getString("last_name");
        String email = extras.getString("email");

        TextView nameView = findViewById(R.id.nameView);
        TextView emailView = findViewById(R.id.emailView);

        nameView.setText(name);
        emailView.setText(email);
    }

    private void DrawNumberStats()
    {
        //set number statistics
        TextView statisticsView = findViewById(R.id.statisticsView);
        statisticsView.setBackgroundColor(Color.LTGRAY);
        statisticsView.append("Average wake up time: " + "" + "\n");
        statisticsView.append("Average snooze time: " + "" + "\n");
        statisticsView.append("Average overslept time: " + "" + "\n");
    }

    private void DrawGraphs(final Statistic.TimeDifference graphDrawState)
    {
        String user_id = "1521517281462"; //alex's user id

        Statistic.GetEventsSince(graphDrawState, user_id, events -> {

            //edit graphs
            GraphView oversleptGraph = (GraphView) findViewById(R.id.oversleptGraph);
            oversleptGraph.removeAllSeries();

            //set titles
            oversleptGraph.setTitle("Overslept");
            oversleptGraph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
            oversleptGraph.getGridLabelRenderer().setVerticalAxisTitle("Times overslept");

            // activate horizontal scrolling
            oversleptGraph.getViewport().setScrollable(true);

            //populate data points
            HashMap<Long, Long> oversleptMap = new HashMap<Long, Long>();
            for(Event event : events) {
                //go through and check type and populate
                if(event.getAction().equals("overslept"))
                {
                    //round to nearest day
                    Date date = new Date(event.getTimestamp());
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    long time = date.getTime();
                    if(oversleptMap.containsKey(time)) {
                        oversleptMap.put(time, oversleptMap.get(time) + 1);
                    } else {
                        oversleptMap.put(time, 1L);
                    }
                }
            }

            //convert to array
            List<DataPoint> oversleptDataPoints = new ArrayList<DataPoint>();
            for(Map.Entry<Long, Long> entry : oversleptMap.entrySet())
            {
                oversleptDataPoints.add(new DataPoint(entry.getKey(), entry.getValue()));
            }

            LineGraphSeries<DataPoint> oversleptSeries = new LineGraphSeries<DataPoint>(oversleptDataPoints.toArray(new DataPoint[oversleptDataPoints.size()]));

            oversleptGraph.addSeries(oversleptSeries);

            // set date label formatter
            switch (graphDrawState)
            {
                case WEEK:
                    oversleptGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("EEE")));
                    break;
                case MONTH:
                    oversleptGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("dd")));
                    break;
                case YEAR:
                    oversleptGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("mm/yyyy")));
                    break;
            }

            //------------------------------------

            GraphView snoozeGraph = (GraphView) findViewById(R.id.snoozeGraph);
            snoozeGraph.removeAllSeries();

            //set titles
            snoozeGraph.setTitle("Snoozes");
            snoozeGraph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
            snoozeGraph.getGridLabelRenderer().setVerticalAxisTitle("Times snoozed");

            // activate horizontal scrolling
            snoozeGraph.getViewport().setScrollable(true);

            //populate data points
            HashMap<Long, Long> snoozeMap = new HashMap<Long, Long>();
            for(Event event : events) {
                //go through and check type and populate
                if(event.getAction().equals("snooze"))
                {
                    //round to nearest day
                    Date date = new Date(event.getTimestamp());
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    long time = date.getTime();
                    if(snoozeMap.containsKey(time)) {
                        snoozeMap.put(time, snoozeMap.get(time) + 1);
                    } else {
                        snoozeMap.put(time, 1L);
                    }
                }
            }

            //convert to array
            List<DataPoint> snoozeDataPoints = new ArrayList<DataPoint>();
            for(Map.Entry<Long, Long> entry : snoozeMap.entrySet())
            {
                snoozeDataPoints.add(new DataPoint(entry.getKey(), entry.getValue()));
            }

            LineGraphSeries<DataPoint> snoozeSeries = new LineGraphSeries<DataPoint>(snoozeDataPoints.toArray(new DataPoint[snoozeDataPoints.size()]));

            snoozeGraph.addSeries(snoozeSeries);

            // set date label formatter
            switch (graphDrawState)
            {
                case WEEK:
                    snoozeGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("EEE")));
                    break;
                case MONTH:
                    snoozeGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("dd")));
                    break;
                case YEAR:
                    snoozeGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("mm/yyyy")));
                    break;
            }
        });
    }

    public void onRadioOverSleptGraph(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.oversleptGraphWeek:
                if (checked) {
                    DrawGraphs(WEEK);
                }
                break;
            case R.id.oversleptGraphMonth:
                if (checked) {
                    DrawGraphs(MONTH);
                }
                break;
            case R.id.oversleptGraphYear:
                if (checked) {
                    DrawGraphs(YEAR);
                }
                break;
            default:
                break;
        }
    }
}
