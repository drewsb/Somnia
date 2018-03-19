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
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

import static com.socialarm.a350s18_5_socialalarmclock.StatisticsActivity.GraphDrawState.MONTH;
import static com.socialarm.a350s18_5_socialalarmclock.StatisticsActivity.GraphDrawState.WEEK;
import static com.socialarm.a350s18_5_socialalarmclock.StatisticsActivity.GraphDrawState.YEAR;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

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

    enum GraphDrawState {
        WEEK,
        MONTH,
        YEAR
    }

    private void DrawGraphs(final GraphDrawState graphDrawState)
    {
        //edit graphs
        GraphView oversleptGraph = (GraphView) findViewById(R.id.oversleptGraph);
        oversleptGraph.removeAllSeries();

        LineGraphSeries<DataPoint> oversleptSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1),
                new DataPoint(2, 5),
                new DataPoint(3, 3)
        });

        oversleptGraph.addSeries(oversleptSeries);

        // set date label formatter
        oversleptGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    switch (graphDrawState)
                    {
                        case WEEK: {
                            // transform number to time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
                            return simpleDateFormat.format(Calendar.getInstance().getTime());
                        }
                        case MONTH: {
                            // transform number to time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd");
                            return simpleDateFormat.format(Calendar.getInstance().getTime());
                        }
                        case YEAR: {
                            // transform number to time
                            return "";
                        }
                    }
                }
                return super.formatLabel(value, isValueX);
            }
        });

        //------------------------------------

        GraphView snoozeGraph = (GraphView) findViewById(R.id.snoozeGraph);
        snoozeGraph.removeAllSeries();

        LineGraphSeries<DataPoint> snoozeSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1),
                new DataPoint(2, 5),
                new DataPoint(3, 3)
        });

        snoozeGraph.addSeries(snoozeSeries);

        // set date label formatter
        snoozeGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    switch (graphDrawState)
                    {
                        case WEEK: {
                            // transform number to time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
                            return simpleDateFormat.format(Calendar.getInstance().getTime());
                        }
                        case MONTH: {
                            // transform number to time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd");
                            return simpleDateFormat.format(Calendar.getInstance().getTime());
                        }
                        case YEAR: {
                            // transform number to time
                            return "";
                        }
                    }
                } else {
                    return super.formatLabel(value, isValueX);
                }
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
