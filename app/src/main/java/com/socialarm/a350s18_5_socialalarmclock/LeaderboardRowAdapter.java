package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeaderboardRowAdapter extends RecyclerView.Adapter<LeaderboardRowAdapter.ViewHolder> {

    private String[] myNames;
    private String[] myOversleeps;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LeaderboardRow row; // TODO: Customize this to be a Profile object
                            // TODO: Figure out how we query the DB and then
        ViewHolder(LeaderboardRow v) {
            super(v);
            row = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    LeaderboardRowAdapter(String[] names, String[] oversleeps) {
        myNames = names;
        myOversleeps = oversleeps;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LeaderboardRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leader_board, parent, false); // TODO: Change this?
        LeaderboardRow newRow = new LeaderboardRow(v.getContext(), null);

        ViewHolder rh = new ViewHolder(newRow);
        return rh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.row.setName(myNames[position]);
        holder.row.setTime(myOversleeps[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (myNames == null) {
            return 0;
        } else {
            return Math.min(myNames.length, myOversleeps.length);
        }
    }
}
