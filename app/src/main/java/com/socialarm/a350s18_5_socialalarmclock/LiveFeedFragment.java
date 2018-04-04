package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liamdugan on 2018/04/03.
 */

public class LiveFeedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private User user;

    public LiveFeedFragment() {
        // Required empty public constructor
    }

    public static LiveFeedFragment newInstance(User user) {
        LiveFeedFragment fragment = new LiveFeedFragment();
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
     * TODO: comments
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

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get user
        Bundle extras = getArguments();
        User user = (User) extras.getSerializable("user");

        // fetch events
        EventDatabase.getAllEvents(events -> {
            // specify an adapter (see also next example)
            mAdapter = new LiveFeedRowAdapter(events);
            mRecyclerView.setAdapter(mAdapter);
        });

        return myView;
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
