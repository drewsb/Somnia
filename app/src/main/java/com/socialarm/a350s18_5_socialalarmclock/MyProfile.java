package com.socialarm.a350s18_5_socialalarmclock;

import java.util.ArrayList;

/**
 * Created by liamdugan on 2018/03/14.
 *
 * This class is basically a giant wrapper for all of the user's profile DB calls.
 * It will store some information locally to avoid redundant DB calls but is only initialized with enough information to query the DB and nothing more
 */


// TODO: Possibly change this to singleton design pattern (?)

public class MyProfile {

    /*
        Information necessary for database calls (initialized by default)
        TODO: Figure out what info is necessary for DB calls (possibly a token?)
     */
    private String name;

    /*
        Information cached over time to avoid redundant DB calls
        TODO: Add other information here (alarm data, privacy data, preferences, settings, email, etc.)
     */
    private ArrayList<Friend> friendsList = null;

    /*
        Public constructor should take in everything necessary to make DB calls
     */
    public MyProfile(String name) {
        this.name = name;
    }

    /**
     * If we have locally stored info this function will use the locally stored info if
     * forceUpdate = false, else query database if it's true
     */
    public ArrayList<Friend> fetchFriendsList(boolean forceUpdate) {
        if (friendsList == null) {
            // TODO: Query Database and get rid of this terrible hardcoded mess
            friendsList = new ArrayList<Friend>();
            friendsList.add(new Friend("Liam Dugan"));
            friendsList.add(new Friend("Henry Zhu"));
            friendsList.add(new Friend("Drew Boyette"));
            friendsList.add(new Friend("Alex Perry"));
            return friendsList;
        } else if (forceUpdate) {
            // TODO: Query Database and get rid of this terrible hardcoded mess
            // Note: this doesn't make sense when hardcoded, but when we have a DB there will be periodic updates
            // (it will be cool I promise)
            friendsList = new ArrayList<Friend>();
            friendsList.add(new Friend("Liam Dugan"));
            friendsList.add(new Friend("Henry Zhu"));
            friendsList.add(new Friend("Drew Boyette"));
            friendsList.add(new Friend("Alex Perry"));
            return friendsList;
        } else {
            return friendsList;
        }
    }
}
