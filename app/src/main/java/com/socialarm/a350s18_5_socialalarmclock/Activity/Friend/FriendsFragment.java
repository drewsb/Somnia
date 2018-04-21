package com.socialarm.a350s18_5_socialalarmclock.Activity.Friend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;
import com.socialarm.a350s18_5_socialalarmclock.Database.UserDatabase;

import java.util.Collections;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public final String TAG = "FriendsFragment";

    private User user;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(User user) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreateView fetches friends and then creates the recyclerview to hold the FriendRows
     *
     * @param inflater the tool to inflate the xml objects
     * @param container the container that is the parent view of this view
     * @param savedInstanceState any other important state
     * @return the finished leaderboard view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_friends, container, false);
        mRecyclerView = myView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "PRESSED");
            }
        });

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get user
        Bundle extras = getArguments();
        User user = (User) extras.getSerializable("user");

        // fetch friends and their most recent alarms
        UserDatabase.getFriends(user, friends -> {
                // specify an adapter (see also next example)
            Collections.sort(friends);
            HashMap<User, Alarm> alarmMap = new HashMap<>();
            for(User f : friends) {
                UserDatabase.getMostRecentAlarm(f, alarm -> {
                    alarmMap.put(f, alarm);
                    if (alarmMap.size() == friends.size()) {
                        mAdapter = new FriendRowAdapter(user, friends, alarmMap);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }   
        });

        return myView;
    }

    /**
     * Get recycler view of friends
     * @return
     */
    public RecyclerView getmRecyclerView() {
        return this.mRecyclerView;
    }
}
