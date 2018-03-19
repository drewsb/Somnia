package com.socialarm.a350s18_5_socialalarmclock;

import android.os.Bundle;

import java.sql.Timestamp;


public class Event {

    enum Action
    {
        SNOOZE,
        OVERSLEPT
    }

    private Action action;
    private String alarm_id;
    private String user_id;
    private String event_id;
    private Timestamp timestamp;

    //empty no constructor required for firebase
    Event() {

    }

    Event(Action action, String alarm_id, String user_id, String event_id, Timestamp timestamp) {
        this.action = action;
        this.alarm_id = alarm_id;
        this.user_id = user_id;
        this.event_id = event_id;
        this.timestamp = timestamp;
    }

}
