package com.socialarm.a350s18_5_socialalarmclock.User;

/**
 * Created by drewboyette on 2/20/18.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * UserInfo is used to store the current user's data to the App's SharedPreferences.
 * This data is persistent across all classes, and can be accessed by calling:
 * PreferenceManager.getDefaultSharedPreferences(LoginActivity.getContextOfApplication());
 */
public class UserInfo {

    private Activity activity;

    // Constructor
    public UserInfo(Activity activity) {
        this.activity = activity;
    }

    /**
     * Save facebook access token
     * @param token
     */
    public void saveAccessToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_access_token", token);
        editor.apply(); // This line is IMPORTANT !!!
    }

    /**
     * Retrieve Faceboook token
     * @return
     */
    public String getToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("fb_access_token", null);
    }

    /**
     * Clear token
     */
    public void clearToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply(); // This line is IMPORTANT !!!
    }

    /**
     * Save User data to the App's SharedPreferences.
     * @param id
     * @param first_name
     * @param last_name
     * @param email
     * @param gender
     * @param profileURL
     */
    public void saveFacebookUserInfo(String id, String first_name,String last_name, String email, String gender, String profileURL){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.putString("first_name", first_name);
        editor.putString("last_name", last_name);
        editor.putString("email", email);
        editor.putString("gender", gender);
        editor.putString("profileURL", profileURL);
        editor.apply();
        Log.d("UserInfo", "Shared Name : "+first_name+"\nLast Name : "+last_name+"\nEmail : "+email+"\nGender : "+gender+"\nProfile Pic : "+profileURL);
    }

    /**
     * Log user data to the console for testing purposes.
     */
    public void getFacebookUserInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.d("UserInfo", "ID : " + prefs.getString("id", null) + "Name : " + prefs.getString("fb_name", null) + "\nEmail : " + prefs.getString("fb_email", null));
    }

}