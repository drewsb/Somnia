package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The adapter to be hooked into LeaderboardRow (Similar to FriendRow)
 */
public class LeaderboardRowAdapter extends RecyclerView.Adapter<LeaderboardRowAdapter.ViewHolder> {

    /**
     * A list of names and numbered statistics to display as rows
     */
    private String[] myNames;
    private String[] myOversleeps;

    /**
     * Static inner view holder class
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LeaderboardRow row;
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

    /**
     * Create the new viewholder with the new ViewHolder inside
     *
     * @param parent the parent view group that the row is to be in
     * @param viewType ?
     * @return a new ViewHolder
     */
    @Override
    public LeaderboardRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leader_board, parent, false);
        LeaderboardRow newRow = new LeaderboardRow(v.getContext(), null);

        ViewHolder rh = new ViewHolder(newRow);
        return rh;
    }

    /**
     * Get the element from the dataset at this position and replace the contents of
     * the view with that element
     *
     * @param holder the ViewHolder that contains this view
     * @param position the position in the leaderboard arrays that we want to fetch from
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.row.setName(myNames[position]);
        holder.row.setTime(myOversleeps[position]);
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return min of the names and the oversleep arrays lengths
     */
    @Override
    public int getItemCount() {
        if (myNames == null) {
            return 0;
        } else {
            return Math.min(myNames.length, myOversleeps.length);
        }
    }
}
