package com.socialarm.a350s18_5_socialalarmclock;

/**
 * Created by liamdugan on 2018/03/14.
 *
 * This class is purely for use with the leaderboard,
 * it allows us to keep a running local statistic for times overslept in past week / month etc.
 * and it allows us to save redundant DB calls.
 *
 * Mainly used in an array in MyProfile
 */

public class Friend {

    private String name;
    private int timesOversleptPastWeek = -1;
    private int timesOversleptPastMonth = -1;
    private int timesOverslept = -1;

    private int timesSnoozedPastWeek = -1;
    private int timesSnoozedPastMonth = -1;
    private int timesSnoozed = -1;

    private int timesWokenUpPastWeek = -1;
    private int timesWokenUpPastMonth = -1;
    private int timesWokenUp = -1;

    public Friend(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    // TODO: Possible optimization for this suite of functions is to check if past week < past month < all time and if not, force update to those (?)

    public int getTimesOversleptPastWeek(boolean forceUpdate) {
        if (this.timesOversleptPastWeek == -1) {
            // TODO: query database
            if (name.equals("Liam Dugan")) {
                this.timesOversleptPastWeek = 20;
            } else if (name.equals("Henry Zhu")) {
                this.timesOversleptPastWeek = 15;

            } else {
                this.timesOversleptPastWeek = 1;
            }
        } else if (forceUpdate) {
            // TODO: query database
            if (name.equals("Liam Dugan")) {
                this.timesOversleptPastWeek = 20;
            } else if (name.equals("Henry Zhu")) {
                this.timesOversleptPastWeek = 15;
            } else {
                this.timesOversleptPastWeek = 1;
            }
        }

        return timesOversleptPastWeek;
    }

    public int getTimesOversleptPastMonth(boolean forceUpdate) {
        if (this.timesOversleptPastMonth == -1) {
            // TODO: query database
            this.timesOversleptPastMonth = 2;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesOversleptPastMonth = 2;
        }

        return timesOversleptPastMonth;
    }

    public int getTimesOversleptAllTime(boolean forceUpdate) {
        if (this.timesOverslept == -1) {
            // TODO: query database
            this.timesOverslept = 3;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesOverslept = 3;
        }

        return timesOverslept;
    }

    public int getTimesSnoozedPastWeek(boolean forceUpdate) {
        if (this.timesSnoozedPastWeek == -1) {
            // TODO: query database
            this.timesSnoozedPastWeek = 4;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesSnoozedPastWeek = 4;
        }

        return timesSnoozedPastWeek;
    }

    public int getTimesSnoozedPastMonth(boolean forceUpdate) {
        if (this.timesSnoozedPastMonth == -1) {
            // TODO: query database
            this.timesSnoozedPastMonth = 5;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesSnoozedPastMonth = 5;
        }

        return timesSnoozedPastMonth;
    }

    public int getTimesSnoozedAllTime(boolean forceUpdate) {
        if (this.timesSnoozed == -1) {
            // TODO: query database
            this.timesSnoozed = 6;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesSnoozed = 6;
        }

        return timesSnoozed;
    }

    public int getTimesWokenUpPastWeek(boolean forceUpdate) {
        if (this.timesWokenUpPastWeek == -1) {
            // TODO: query database
            this.timesWokenUpPastWeek = 7;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesWokenUpPastWeek = 7;
        }

        return timesWokenUpPastWeek;
    }

    public int getTimesWokenUpPastMonth(boolean forceUpdate) {
        if (this.timesWokenUpPastMonth == -1) {
            // TODO: query database
            this.timesWokenUpPastMonth = 8;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesWokenUpPastMonth = 8;
        }

        return timesWokenUpPastMonth;
    }

    public int getTimesWokenUpAllTime(boolean forceUpdate) {
        if (this.timesWokenUp == -1) {
            // TODO: query database
            this.timesWokenUp = 9;
        } else if (forceUpdate) {
            // TODO: query database
            this.timesWokenUp = 9;
        }

        return timesWokenUp;
    }

}
