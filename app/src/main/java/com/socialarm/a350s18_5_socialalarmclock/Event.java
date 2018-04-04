package com.socialarm.a350s18_5_socialalarmclock;

public class Event {

    private String action;
    private String alarm_id;
    private String user_id;
    private String event_id;
    private long timestamp;

    //empty no constructor required for firebase
    Event() {

    }

    Event(String action, String alarmId, String userId, String eventId, long timestamp) {
        this.action = action;
        this.alarm_id = alarmId;
        this.user_id = userId;
        this.event_id = eventId;
        this.timestamp = timestamp;
    }


    public String getAction() {
        return action;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
