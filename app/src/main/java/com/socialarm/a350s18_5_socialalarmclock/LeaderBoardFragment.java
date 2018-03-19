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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class LeaderBoardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MyProfile profile;

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

    public static LeaderBoardFragment newInstance(MyProfile profile) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.profile = profile;
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

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] lbNames = getResources().getStringArray(R.array.leaderboard_name_array);
        String[] lbOversleeps = getResources().getStringArray(R.array.leaderboard_oversleep_count);
        mAdapter = new LeaderboardRowAdapter(lbNames, lbOversleeps);
        mRecyclerView.setAdapter(mAdapter);

        Spinner spinner = (Spinner) myView.findViewById(R.id.spinner_options_time);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.times_options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // Create the first spinner's action listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long row_id) {
                switch(position){
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
                // Fetch the friends list
                ArrayList<Friend> friendsList = profile.fetchFriendsList(true);

                // create array of leaderboard entries
                ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();

                // Loop through friends list and query the DB for the right statistic
                for (Friend f : friendsList) {
                    int desiredStatistic = -1;

                    switch (statDuration) {
                        case THIS_WEEK:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptPastWeek(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedPastWeek(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpPastWeek(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case THIS_MONTH:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptPastMonth(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedPastMonth(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpPastMonth(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ALL_TIME:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptAllTime(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedAllTime(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpAllTime(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }

                    // insert the string name and the int into a list in the desired sort order
                    leaderboardEntries.add(new LeaderboardEntry(f.getName(), desiredStatistic, sortDirection));
                }

                Collections.sort(leaderboardEntries);

                // get the values into arrays
                ArrayList<String> sortedFriendNames = new ArrayList<String>();
                ArrayList<String> sortedStatistic = new ArrayList<String>();

                for (LeaderboardEntry entry : leaderboardEntries) {
                    sortedFriendNames.add(entry.name);
                    sortedStatistic.add(entry.statistic.toString());
                }

                String[] friendNames = sortedFriendNames.toArray(new String[sortedFriendNames.size()]);
                String[] stats = sortedStatistic.toArray(new String[sortedStatistic.size()]);

                // instantiate a new leaderboardRowAdapter with this data
                mAdapter = new LeaderboardRowAdapter(friendNames, stats);


                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        Spinner spinner_sort = (Spinner) myView.findViewById(R.id.spinner_options_sort_by);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_sort = ArrayAdapter.createFromResource(this.getContext(),
                R.array.sorting_options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_sort.setAdapter(adapter_sort);

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long row_id) {
                switch(position){
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

                // Fetch the friends list
                ArrayList<Friend> friendsList = profile.fetchFriendsList(true);

                // create array of leaderboard entries
                ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();

                // Loop through friends list and query the DB for the right statistic
                for (Friend f : friendsList) {
                    int desiredStatistic = -1;

                    switch (statDuration) {
                        case THIS_WEEK:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptPastWeek(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedPastWeek(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpPastWeek(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case THIS_MONTH:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptPastMonth(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedPastMonth(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpPastMonth(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ALL_TIME:
                            switch(statType) {
                                case OVERSLEEP:
                                    desiredStatistic = f.getTimesOversleptAllTime(true);
                                    break;
                                case SNOOZE:
                                    desiredStatistic = f.getTimesSnoozedAllTime(true);
                                    break;
                                case WAKE_UP:
                                    desiredStatistic = f.getTimesWokenUpAllTime(true);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }

                    // insert the string name and the int into a list in the desired sort order
                    leaderboardEntries.add(new LeaderboardEntry(f.getName(), desiredStatistic, sortDirection));
                }

                Collections.sort(leaderboardEntries);

                // get the values into arrays
                ArrayList<String> sortedFriendNames = new ArrayList<String>();
                ArrayList<String> sortedStatistic = new ArrayList<String>();

                for (LeaderboardEntry entry : leaderboardEntries) {
                    sortedFriendNames.add(entry.name);
                    sortedStatistic.add(entry.statistic.toString());
                }

                String[] friendNames = sortedFriendNames.toArray(new String[sortedFriendNames.size()]);
                String[] stats = sortedStatistic.toArray(new String[sortedStatistic.size()]);

                // instantiate a new leaderboardRowAdapter with this data
                mAdapter = new LeaderboardRowAdapter(friendNames, stats);


                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
