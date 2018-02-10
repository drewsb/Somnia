package com.socialarm.a350s18_5_socialalarmclock;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetupTabs();
    }


    private void SetupTabs() {
        TabHost top_tab = (TabHost)findViewById(R.id.top_tab);
    }
}
