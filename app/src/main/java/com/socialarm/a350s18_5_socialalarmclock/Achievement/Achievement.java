package com.socialarm.a350s18_5_socialalarmclock.Achievement;

import java.io.Serializable;

/**
 * Created by drewboyette on 3/13/18.
 */

/**
 * Holds information for every user in the database
 */
public class Achievement implements Serializable {

    private String week;
    private Metal metal;


    public enum Metal {
        BRONZE, SILVER, GOLD
    }

    public Achievement() {}

    public Achievement(String week, Metal metal) {
        this.week = week;
        this.metal = metal;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setMetal(Metal metal) {
        this.metal = metal;
    }

    public String getWeek() {

        return week;
    }

    public Metal getMetal() {
        return metal;
    }
}
