package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener, MyAlarmFragment.OnFragmentInteractionListener, LeaderBoardFragment.OnFragmentInteractionListener {

    private PagerAdapter pagerAdapter;

    private ViewPager viewPager;

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(MyAlarmFragment.newInstance());
        fragments.add(FriendsFragment.newInstance());
        fragments.add(LeaderBoardFragment.newInstance());

        // Create the adapter that will return a fragment
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //add functionality to find friends button
        Button search_friend_button = (Button)findViewById(R.id.search_friend_button);
        search_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchFriendActivity.class);
                startActivity(intent);
            }
        });
    }
}
