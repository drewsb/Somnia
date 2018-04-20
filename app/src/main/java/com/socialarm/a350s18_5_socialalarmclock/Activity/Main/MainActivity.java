package com.socialarm.a350s18_5_socialalarmclock.Activity.Main;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.DisableAlarmFragment;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm.MyAlarms;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Friend.FriendsFragment;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Leaderboard.LeaderBoardFragment;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Livefeed.LiveFeedFragment;
import com.socialarm.a350s18_5_socialalarmclock.Activity.SearchFriend.SearchFriendActivity;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Statistic.StatisticsActivity;
import com.socialarm.a350s18_5_socialalarmclock.Activity.Tutorial.TutorialActivity;
import com.socialarm.a350s18_5_socialalarmclock.Helper.DownloadImageTask;
import com.socialarm.a350s18_5_socialalarmclock.Database.EventDatabase;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.SomniaFirebaseInstanceIDService;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;

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

    private View setupNav() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        return headerView;
    }

    private void setFirebaseId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean hasSetFirebaseId = prefs.getBoolean("setFirebaseId", false);

        if (!hasSetFirebaseId) {
            SomniaFirebaseInstanceIDService service = new SomniaFirebaseInstanceIDService();
            service.onTokenRefresh();
            SharedPreferences.Editor e = prefs.edit();
            e.putBoolean("setFirebaseId", true);
            e.apply();
        }
    }

    private void setupDrawerLayout(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupPager() {
        UserDatabase.getUser(extras.getString("idFacebook"), user -> {

            List<Fragment> fragments = new ArrayList<Fragment>();
            MyAlarms myAlarms = MyAlarms.newInstance();
            fragments.add(myAlarms);
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
            TabLayout.ViewPagerOnTabSelectedListener viewPagerListener = new TabLayout.ViewPagerOnTabSelectedListener(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int i = tab.getPosition();
                    if(i == 0) {
                        myAlarms.onResume();
                    }
                    viewPager.setCurrentItem(i);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        });
    }

    private void setupScreen() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = getIntent();
        extras = i.getExtras();

        //Retrieve user's name and email
        String name = extras.getString("first_name") + " " + extras.getString("last_name");
        String email = extras.getString("email");

        setupDrawerLayout(toolbar);
        View headerView = setupNav();

        TextView nameView = headerView.findViewById(R.id.nameView);
        TextView emailView = headerView.findViewById(R.id.emailView);

        //Set profile pic image
        new DownloadImageTask((ImageView) headerView.findViewById(R.id.profileView))
                .execute(extras.getString("profile_pic"));

        nameView.setText(name);
        emailView.setText(email);

        setFirebaseId();
        CreateTutorial();

        setupPager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupScreen();
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
     * Navigation bar selection function
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
                intent.putExtra("own_profile", false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_search_menu, menu);
        MenuItem item = menu.findItem(R.id.friendMenu);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * check if user opened tutorial
     */
    private void CreateTutorial() {

        //  Intro App Initialize SharedPreferences
        SharedPreferences getSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

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
}
