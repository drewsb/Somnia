package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.socialarm.a350s18_5_socialalarmclock.EventDatabase.TimeDifference.MONTH;
import static com.socialarm.a350s18_5_socialalarmclock.EventDatabase.TimeDifference.WEEK;
import static com.socialarm.a350s18_5_socialalarmclock.EventDatabase.TimeDifference.YEAR;

public class StatisticsActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        //set the user
        user = (User) i.getSerializableExtra("user");

        //draw information on the screen
        DrawUserInfo();
        DrawNumberStats();
        DrawGraphs(WEEK);
        SetupSpinner();
    }

    /**
     * Setups the spinner
     */
    private void SetupSpinner() {
        Spinner notifications = (Spinner)findViewById(R.id.notifcationDropdown);

        notifications.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateFriendNotifications(notifications.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner privileges = (Spinner)findViewById(R.id.privilegeDropdown);

        privileges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateFriendPrivileges(privileges.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateFriendNotifications(Object o){

    }

    private void updateFriendPrivileges(Object o){

    }

    /**
     * Draw the user profile pic
     */
    private void DrawUserInfo()
    {
        try {
            URL profile_pic = new URL("https://graph.facebook.com/" + user.getId() + "/picture?type=large");

            //Set profile pic image
            new DownloadImageTask((ImageView) findViewById(R.id.profileView))
                    .execute(profile_pic.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Retrieve user's name and email
        String name = user.getFirst_name() + " " + user.getLast_name();

        TextView nameView = findViewById(R.id.nameView);

        nameView.setText(name);
    }

    /**
     * Calculate statistics and updates text conntaining average
     */
    private void DrawNumberStats()
    {
        //set number statistics
        TextView statisticsView = findViewById(R.id.statisticsView);

        //fetch events for user
        EventDatabase.getEventsSince(EventDatabase.TimeDifference.YEAR, user.getId(), events -> {
            Map<Long, Long> wakeup_map = new HashMap<Long, Long>();
            Map<Long, Long> snooze_map = new HashMap<Long, Long>();
            Map<Long, Long> overslept_map = new HashMap<Long, Long>();
            for(Event e : events)
            {
                String action = e.getAction();
                Long ts = e.getTimestamp();
                Map<Long, Long> map = null;
                if(action.equals("wakeup"))
                {
                    map = wakeup_map;
                }
                else if(action.equals("snooze"))
                {
                    map = snooze_map;
                }
                else if(action.equals("overslept"))
                {
                    map = overslept_map;
                }

            }

            //calculate average
            double wakeup_avg = 0;
            double snooze_avg = 0;
            double overslept_avg = 0;

            for(Map.Entry<Long, Long> entry : wakeup_map.entrySet())
            {
                wakeup_avg += entry.getValue().doubleValue();
            }

            for(Map.Entry<Long, Long> entry : snooze_map.entrySet())
            {
                snooze_avg += entry.getValue().doubleValue();
            }

            for(Map.Entry<Long, Long> entry : overslept_map.entrySet())
            {
                overslept_avg += entry.getValue().doubleValue();
            }

            wakeup_avg /= wakeup_map.size() > 0 ? wakeup_map.size() : 1;
            snooze_avg /= snooze_map.size() > 0 ? snooze_map.size() : 1;
            overslept_avg /= overslept_map.size() > 0 ? overslept_map.size() : 1;

            statisticsView.append("Average wake up time: " + wakeup_avg + "\n");
            statisticsView.append("Average snooze time: " + snooze_avg + "\n");
            statisticsView.append("Average overslept time: " + overslept_avg + "\n");
        });
    }

    /**
     * appends one to value if key exists and starts with 1 if not (Used for map)
     * @param map
     * @param key
     * @param default_value
     * @param <A>
     * @param <B>
     */
    private <A, B> void IncrementMapDefault(Map<A, B> map, A key, B default_value)
    {
        //compute put entries with same timestamp in map
        if(map.containsKey(key)) {
            map.put(key, map.get(key));
        } else {
            map.put(key, default_value);
        }
    }

    /**
     * Obtains a mapping from events to date
     * @param events
     * @param eventToFind
     * @return
     */
    private HashMap<Long, Long> GetMapOfCertainEvent(List<Event> events, String eventToFind) {
        //populate data points
        HashMap<Long, Long> map = new HashMap<Long, Long>();
        for(Event event : events) {
            //go through and check type and populate
            if(event.getAction().equals(eventToFind))
            {
                //round to nearest day
                Date date = new Date(event.getTimestamp());
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                long time = date.getTime();
                IncrementMapDefault(map, time, 1L);
            }
        }
        return map;
    }

    /**
     * Draws a graph provided events, the event to find and how much data needs to be filtered
     * @param graph
     * @param events
     * @param eventToFind
     * @param graphDrawState
     */
    private void UpdateGraph(GraphView graph, List<Event> events, String eventToFind, final EventDatabase.TimeDifference graphDrawState) {
        //edit graphs
        graph.removeAllSeries();

        //set titles
        graph.setTitle("Overslept");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Times overslept");

        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setPadding(100);

        Map<Long, Long> eventsDateMap = GetMapOfCertainEvent(events, eventToFind);

        //convert to array
        List<DataPoint> oversleptDataPoints = new ArrayList<DataPoint>();
        Double max = 0.0;
        for(Map.Entry<Long, Long> entry : eventsDateMap.entrySet())
        {
            oversleptDataPoints.add(new DataPoint(entry.getKey(), entry.getValue()));
            if(entry.getValue() > max)
            {
                max = entry.getValue().doubleValue();
            }
        }

        LineGraphSeries<DataPoint> oversleptSeries = new LineGraphSeries<DataPoint>(oversleptDataPoints.toArray(new DataPoint[oversleptDataPoints.size()]));
        oversleptSeries.setDrawDataPoints(true);
        oversleptSeries.setDataPointsRadius(10);
        oversleptSeries.setThickness(8);

        //manually set range from 0 to max
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(max);

        graph.getViewport().setYAxisBoundsManual(true);

        graph.addSeries(oversleptSeries);

        // set date label formatter
        switch (graphDrawState)
        {
            case WEEK:
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("EEE")));
                break;
            case MONTH:
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("dd")));
                break;
            case YEAR:
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new java.text.SimpleDateFormat("yyyy")));
                graph.getGridLabelRenderer().setNumHorizontalLabels(2);
                break;
        }
    }

    /**
     * Draws the graphs by making a request to db
     * @param graphDrawState
     */
    private void DrawGraphs(final EventDatabase.TimeDifference graphDrawState)
    {
        String user_id = user.getId(); //get user id

        EventDatabase.getEventsSince(graphDrawState, user_id, events -> {

            //edit graphs
            GraphView oversleptGraph = (GraphView) findViewById(R.id.oversleptGraph);
            oversleptGraph.removeAllSeries();

            //set titles
            oversleptGraph.setTitle("Overslept");
            oversleptGraph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
            oversleptGraph.getGridLabelRenderer().setVerticalAxisTitle("Times overslept");

            UpdateGraph(oversleptGraph, events, "overslept", graphDrawState);

            //------------------------------------

            GraphView snoozeGraph = (GraphView) findViewById(R.id.snoozeGraph);
            snoozeGraph.removeAllSeries();

            //set titles
            snoozeGraph.setTitle("Snoozes");
            snoozeGraph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
            snoozeGraph.getGridLabelRenderer().setVerticalAxisTitle("Times snoozed");

            UpdateGraph(snoozeGraph, events, "snooze", graphDrawState);
        });
    }

    /**
     * Radio button for week, month, year - selects a mode to draw
     * @param view
     */
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
