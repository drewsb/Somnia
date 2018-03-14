package com.socialarm.a350s18_5_socialalarmclock;

import android.os.Bundle;

/**
 * Created by drewboyette on 3/13/18.
 */

public class User {

    private String id;
    private String first_name;
    private String last_name;

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
}
