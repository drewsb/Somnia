package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * The adapter to be hooked into FriendRow (Similar to LeaderboardRow)
 */
public class FriendRowAdapter extends RecyclerView.Adapter<FriendRowAdapter.ViewHolder> {

    /**
     * A list of users to be displayed on the friends page (canonically the logged in user's friends list)
     */
    private List<User> users;

    /**
     * Static inner view holder class
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        FriendRow row;
        ViewHolder(FriendRow v) {
            super(v);
            row = v;
        }
    }

    FriendRowAdapter(List<User> users) {
        this.users = users;
    }

    /**
     * Create the new viewholder with the new FriendHolder inside
     *
     * @param parent the parent view group that the row is to be in
     * @param viewType ?
     * @return a new ViewHolder
     */
    @Override
    public FriendRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends, parent, false);
        FriendRow newRow = new FriendRow(v.getContext(), null);

        ViewHolder rh = new ViewHolder(newRow);
        return rh;
    }

    /**
     * Replace the contents of the View (specifically the FriendRow) with the given user and statistic button
     *
     * @param holder the ViewHolder that contains this view
     * @param position the position in the list of this view
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.row.setName(users.get(position));
        holder.row.setStatisticButton(users.get(position));
    }

    /**
     * Returns the size of the dataset
     * @return size of friends list
     */
    @Override
    public int getItemCount() {
        return users.size();
    }
}
