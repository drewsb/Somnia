package com.socialarm.a350s18_5_socialalarmclock;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.socialarm.a350s18_5_socialalarmclock.LeaderBoardFragment.*;

/**
 * Created by drewboyette on 3/13/18.
 */

/**
 * Holds information for every user in the database
 */
public class User implements Serializable {

    private static User instance;

    private String id;
    private String first_name;
    private String last_name;
    private List<String> friend_ids;

    public User() {
        this.instance = this;
    }

    public User(String id, String first_name, String last_name){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.instance = this;
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

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
        this.instance = this;
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
}
