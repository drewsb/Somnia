package com.socialarm.a350s18_5_socialalarmclock;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by drewboyette on 3/13/18.
 */

public class User {

    private String id;
    private String first_name;
    private String last_name;
    private String[] friend_ids;

    public User() {}

    public User(String id, String first_name, String last_name){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public User(Bundle b){
        this.id = b.getString("idFacebook");
        this.first_name = b.getString("first_name");
        this.last_name = b.getString("last_name");
        String user_friends = b.getString("friends");
        try {
            JSONObject jobj = new JSONObject(user_friends);
            JSONArray arr = jobj.getJSONArray("data");

            ArrayList<String> friendArrayList = new ArrayList<String>();

            for (int i = 0; i < arr.length();i++) {
                friendArrayList.add(arr.getJSONObject(i).getString("id"));
            }

            this.friend_ids = friendArrayList.toArray(new String[friendArrayList.size()]);
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

    public String[] getFriend_ids() { return friend_ids; }

}
