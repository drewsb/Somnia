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

    public void setAction(String action) {
        this.action = action;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
