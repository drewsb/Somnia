package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.graphics.drawable.Drawable;

import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.Helper.Consumer;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

/**
 * Created by Henry on 4/21/2018.
 */

/**
 * Represents an achievement
 * image
 * name
 * descrption
 * is_achieved - whether or not the user has achieved this achievement
 * condition - is a callback true/false given an event
 */

public class Achievement {

    private User user;
    private Drawable image;
    private String name;
    private String description;
    private Boolean is_achieved;
    private Consumer<Event> condition;

    public Achievement(User user, Drawable image, String name, String description, Boolean is_achieved, Consumer<Event> condition) {
        this.user = user;
        this.image = image;
        this.name = name;
        this.description = description;
        this.is_achieved = is_achieved;
        this.condition = condition;
    }

    public void pushEventToDB() {
        Long tsLong = System.currentTimeMillis()/1000;
        Event event = new Event(name, "", user.getId(), user.getId() + "-" + name + "-" + is_achieved.toString(), tsLong);
        EventDatabase.addEvent(event);
    }

    /**
     * sets is achieved
     * @param is_achieved
     */
    public void setIs_achieved(Boolean is_achieved) {
        this.is_achieved = is_achieved;
    }

    /**
     * Getters
     */
    public User getUser() {
        return user;
    }

    public Drawable getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIs_achieved() {
        return is_achieved;
    }

    public Consumer<Event> getCondition() {
        return condition;
    }
}
