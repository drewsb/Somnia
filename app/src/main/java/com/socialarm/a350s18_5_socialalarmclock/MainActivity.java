package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import android.support.v4.app.Fragment;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener,
        MyAlarms.OnFragmentInteractionListener, LeaderBoardFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Intent i;
    private Bundle extras;
    private EventDatabase eventDB;
    private static final String TAG = "MainActivity";

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    public boolean isFirstStart;

    //check if user opened tutorial
    private void createTutorial() {

        //  Intro App Initialize SharedPreferences
        SharedPreferences getSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

        //  Check either activity or app is open very first time or not and do action
        if (isFirstStart) {
            //  Launch application introduction screen
            Intent i = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(i);
            SharedPreferences.Editor e = getSharedPreferences.edit();
            e.putBoolean("firstStart", false);
            e.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        i = getIntent();
        extras = i.getExtras();

        //Retrieve user's name and email
        String name = extras.getString("first_name") + " " + extras.getString("last_name");
        String email = extras.getString("email");

        TextView nameView = headerView.findViewById(R.id.nameView);
        TextView emailView = headerView.findViewById(R.id.emailView);

        //Set profile pic image
        new DownloadImageTask((ImageView) headerView.findViewById(R.id.profileView))
                .execute(extras.getString("profile_pic"));

        nameView.setText(name);
        emailView.setText(email);
      
        createTutorial();
        UserDatabase.getUser(extras.getString("idFacebook"), user -> {
            List<Fragment> fragments = new ArrayList<Fragment>();
            fragments.add(MyAlarms.newInstance());
            fragments.add(FriendsFragment.newInstance(user));
            fragments.add(LeaderBoardFragment.newInstance(user));
            fragments.add(LiveFeedFragment.newInstance(user));

            // Create the adapter that will return a fragment
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

            // Set up the ViewPager with the sections adapter.
            viewPager = findViewById(R.id.viewPagerContainer);
            viewPager.setAdapter(pagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabs);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        });

        //add functionality to find friends button
        Button search_friend_button = (Button) findViewById(R.id.search_friend_button);
        search_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *  Control navigation drawer display
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle login menu options
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
        } else if (id == R.id.nav_logout) { //Logout and return to LoginActivity
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) { //Go to statisitics page for me
                UserDatabase.getUser(extras.getString("idFacebook"), user -> {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);

                //pass user data to statistics activity
                intent.putExtra("user", user);
                startActivity(intent);
            });
        } else if (id == R.id.nav_disable) {
            DisableAlarmFragment disableAlarmFragment = new DisableAlarmFragment();
            disableAlarmFragment.show(getSupportFragmentManager(), "disable");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
