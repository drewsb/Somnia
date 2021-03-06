package com.socialarm.a350s18_5_socialalarmclock.User;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by drewboyette on 3/13/18.
 */

/**
 * Holds information for every user in the database
 */
public class User implements Serializable, Comparable<User> {

    private String id;
    private String first_name;
    private String last_name;
    private List<String> friend_ids;
    private String firebase_id;

    public User() {}


    public User(Bundle b){
        this.id = b.getString("idFacebook");
        this.first_name = b.getString("first_name");
        this.last_name = b.getString("last_name");
        String user_friends = b.getString("friends");
        try {
            JSONObject jobj = new JSONObject(user_friends);
            JSONArray arr = jobj.getJSONArray("data");

            friend_ids = new ArrayList<String>();

            for (int i = 0; i < arr.length();i++) {
                friend_ids.add(arr.getJSONObject(i).getString("id"));
            }

        } catch (JSONException e) {

        }
    }

    public String getId(){
        return this.id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public List<String> getFriend_ids() { return friend_ids; }

    public void setFirebase_id(String f_id) {
        firebase_id = f_id;
    }

    String getFirebase_id() {
        return firebase_id;
    }

    @Override
    public int compareTo(User other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        int first = this.first_name.compareTo(other.first_name);
        return first == 0 ? this.last_name.compareTo(other.last_name) : first;
    }
}
