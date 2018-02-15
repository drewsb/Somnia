package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class MyAlarms extends Fragment {
    static final int CREATE_ALARM_REQUEST = 1;
    static final String ALARM_FILENAME = "my_alarms.pb";

    View v;
    AlarmList.Builder alarms;

    public MyAlarms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarms = AlarmList.newBuilder();

        try {
            FileInputStream alarmFile = getContext().openFileInput(ALARM_FILENAME);
            AlarmList a = AlarmList.parseFrom(alarmFile);
            alarms.addAllAlarms(a.getAlarmsList());
        } catch (FileNotFoundException e) {
            // Do nothing. If file doesn't exist we will make it later.
        } catch (IOException e) {
            alarms = AlarmList.newBuilder();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            FileOutputStream alarmFile = getContext().openFileOutput(ALARM_FILENAME, Context.MODE_PRIVATE);
            alarms.build().writeTo(alarmFile);
            alarms.clearAlarms();
        } catch (FileNotFoundException e) {
            // Should never happen.
        } catch (IOException e) {
            // Drop all alarms.
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_alarms, container, false);
        UpdateValues();
        FloatingActionButton b = v.findViewById(R.id.add_alarm_button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AlarmEditActivity.class);
                startActivityForResult(i, CREATE_ALARM_REQUEST);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_ALARM_REQUEST) {
            if (resultCode == RESULT_OK) {
                Alarm new_alarm;
                try {
                    new_alarm = Alarm.parseFrom(data.getByteArrayExtra("new_alarm"));
                } catch (InvalidProtocolBufferException e) {
                    return;
                }
                alarms.addAlarms(new_alarm);
            }
            UpdateValues();
        }
    }

    private void UpdateValues() {
        ArrayList<String> times = new ArrayList<>();
        for (Alarm a : alarms.getAlarmsList()) {
            times.add(String.format("%02d:%02d", a.getHour(), a.getMinute()));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.single_alarm_layout, R.id.alarm_time);
        adapter.addAll(times);
        ListView lv = v.findViewById(R.id.my_alarm_list);
        lv.setAdapter(adapter);
    }
}
