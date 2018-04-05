package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
            ArrayList<Alarm> alarms = new ArrayList<Alarm>();
            for(User f : friends) {
                alarms.add(UserDatabase.getMostRecentAlarm(f));
            }
            mAdapter = new FriendRowAdapter(friends, alarms);
            mRecyclerView.setAdapter(mAdapter);
        });

        return myView;
    }

    /**
     * Does an action when a row is pressed
     * @param uri the uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Attach the onfragmentinteractionlistener or throw an error if there isn't one
     *
     * @param context the global context of the app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Called when the view is detached from the view (set listener to null)
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
