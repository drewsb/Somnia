package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Toast;

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
    private int image;
    private String name;
    private String description;
    private Boolean is_achieved;

    private Consumer<Event> condition;

    public Achievement(User user, int image, String name, String description, Boolean is_achieved) {
        this.user = user;
        this.image = image;
        this.name = name;
        this.description = description;
        this.is_achieved = is_achieved;
    }

    /**
     * Push achievement to db and show that user has achieved achievement
     * @param context
     */
    public void pushEventToDB(Context context) {
        if(is_achieved) {
            //check if the entry exists already
            EventDatabase.getAllEvents(events -> {
                for(Event e : events) {
                    if(e.getEvent_id().startsWith(eventString())) {
                        return;
                    }
                }

                //should now drew achievement
                AchievementActivity.displayAchievements = true;

                //draw achievement popup
                Toast.makeText(context, "You have obtained " + name + " achievement", Toast.LENGTH_SHORT).show();

                Long tsLong = System.currentTimeMillis() / 1000;
                Event event = new Event(name, "", user.getId(), user.getId() + "-" + name + "-" + is_achieved.toString(), tsLong);
                EventDatabase.addEvent(event);
            });
        }
    }

    /**
     * Returns the event string (beginning part) to check if event is achievement
     * @return event string
     */
    public String eventString() {
        return user.getId() + "-" + name;
    }

    /**
     * sets is achieved
     * @param is_achieved
     */
    public void setIs_achieved(Boolean is_achieved) {
        this.is_achieved = is_achieved;
    }

    public void setCondition(Consumer<Event> condition) {
        this.condition = condition;
    }

    /**
     * Getters
     */
    public User getUser() {
        return user;
    }

    public int getImage() {
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
