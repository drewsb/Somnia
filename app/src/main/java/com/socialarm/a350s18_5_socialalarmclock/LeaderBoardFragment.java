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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private User user;

    /*
        state related to the leaderboard
     */
    enum Duration { THIS_WEEK, THIS_MONTH, ALL_TIME }
    private Duration statDuration = Duration.THIS_WEEK; // default to this week

    enum SleepStatType { OVERSLEEP, SNOOZE, WAKE_UP }
    private SleepStatType statType = SleepStatType.OVERSLEEP; // default to oversleep

    enum SortDirection { MOST, LEAST }
    private SortDirection sortDirection = SortDirection.MOST; // default to most

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    public static LeaderBoardFragment newInstance(User user) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.user = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_leader_board, container, false);
        mRecyclerView = myView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for the spinners
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Create the first spinner with its drop down
        Spinner times_spinner = createSpinnerWithDropDown(R.id.spinner_options_time,
                R.array.times_options_array, myView);

        // Create the first spinner's action listener
        times_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {

                // update state based on first spinner choice
                updateStatDuration(position);

                // make database call and display statistic asynchronously
                EventDatabase.getLeaderboardEventsSince(user.getFriend_ids(),
                        statDuration, statType, sortDirection, entries -> {displayRanking(entries);});

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        // Create the spinner with its drop down
        Spinner sort_spinner = createSpinnerWithDropDown(R.id.spinner_options_sort_by,
                R.array.sorting_options_array, myView);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {

                // update state based on second spinner choice
                updateStatTypeAndSortDirection(position);

                // make database call and display statistic asynchronously
                EventDatabase.getLeaderboardEventsSince(user.getFriend_ids(),
                        statDuration, statType, sortDirection, entries -> {displayRanking(entries);});
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        return myView;
    }

    /**
     * Based on spinner selection update the fragment's "statDuration" field
     *
     * @param selection the index in the time_spinner
     */
    private void updateStatDuration(int selection) {
        switch(selection){
            case 0:
                statDuration = Duration.THIS_WEEK;
                break;
            case 1:
                statDuration = Duration.THIS_MONTH;
                break;
            case 2:
                statDuration = Duration.ALL_TIME;
                break;
            default:
                break;
        }
    }

    /**
     * Based on spinner selection update the fragment's "statType" and "sortDirection" fields
     *
     * @param selection the index in the sort_spinner
     */
    private void updateStatTypeAndSortDirection(int selection) {
        switch(selection){
            case 0:
                statType = SleepStatType.OVERSLEEP;
                sortDirection = SortDirection.MOST;
                break;
            case 1:
                statType = SleepStatType.OVERSLEEP;
                sortDirection = SortDirection.LEAST;
                break;
            case 2:
                statType = SleepStatType.SNOOZE;
                sortDirection = SortDirection.MOST;
                break;
            case 3:
                statType = SleepStatType.SNOOZE;
                sortDirection = SortDirection.LEAST;
                break;
            case 4:
                statType = SleepStatType.WAKE_UP;
                sortDirection = SortDirection.MOST;
                break;
            case 5:
                statType = SleepStatType.WAKE_UP;
                sortDirection = SortDirection.LEAST;
            default:
                break;
        }
    }

    /**
     * Create and attach a dropdown menu to a spinner
     *
     * @param view ID for the view the spinner is in
     * @param options what should the dropdown menu display?
     * @param myView the parent view of the spinner
     * @return the spinner
     */
    private Spinner createSpinnerWithDropDown(int view, int options, View myView) {

        Spinner spinner = (Spinner) myView.findViewById(view);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return spinner;
    }

    /**
     * This function takes in a sorted list of leaderboard entries and displays them
     *
     * displayRanking is usually used as a callback after fetching from the DB
     *
     * @param entries the list of leaderboard entries
     */
    public void displayRanking(List<LeaderboardEntry> entries) {

        // get the values into arrays
        ArrayList<String> sortedFriendNames = new ArrayList<String>();
        ArrayList<String> sortedStatistic = new ArrayList<String>();

        for (LeaderboardEntry entry : entries) {
            sortedFriendNames.add(entry.name);
            sortedStatistic.add(entry.statistic.toString());
        }

        String[] friendNames = sortedFriendNames.toArray(new String[sortedFriendNames.size()]);
        String[] stats = sortedStatistic.toArray(new String[sortedStatistic.size()]);

        // instantiate a new leaderboardRowAdapter with this data
        mAdapter = new LeaderboardRowAdapter(friendNames, stats);
        mRecyclerView.setAdapter(mAdapter);
    }

    // ---------------------------------------

    /**
     *   Possibly could rename method, update argument and hook method into UI event
     *   if we wanted to have a global onButtonPressed
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Have to implement this one
     * @param context
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
     * Forced to implement this one also
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
