package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by liamdugan on 2018/04/03.
 *
 * Adapter for the LiveFeedRows
 */
public class LiveFeedRowAdapter extends RecyclerView.Adapter<LiveFeedRowAdapter.ViewHolder> {

    /**
     * A list of users to be displayed on the friends page (canonically the logged in user's friends list)
     */
    private List<Event> events;

    /**
     * Static inner view holder class
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LiveFeedRow row;
        ViewHolder(LiveFeedRow v) {
            super(v);
            row = v;
        }
    }

    LiveFeedRowAdapter(List<Event> events) {
        this.events = events;
    }

    /**
     * Create the new viewholder with the new FriendHolder inside
     *
     * @param parent the parent view group that the row is to be in
     * @param viewType ?
     * @return a new ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_live_feed, parent, false);
        LiveFeedRow newRow = new LiveFeedRow(v.getContext(), null);

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
        holder.row.setEvent(events.get(position));
    }

    /**
     * Returns the size of the dataset
     * @return size of friends list
     */
    @Override
    public int getItemCount() {
        return events.size();
    }
}
