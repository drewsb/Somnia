package com.socialarm.a350s18_5_socialalarmclock.Activity.Friend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.socialarm.a350s18_5_socialalarmclock.Alarm.Alarm;
import com.socialarm.a350s18_5_socialalarmclock.R;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.List;
import java.util.Map;

/**
 * The adapter to be hooked into FriendRow (Similar to LeaderboardRow)
 */
public class FriendRowAdapter extends RecyclerView.Adapter<FriendRowAdapter.ViewHolder> {

    /**
     * A list of users to be displayed on the friends page (canonically the logged in user's friends list)
     */
    private List<User> users;
    private Map<User, Alarm> alarmMap;

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

    public FriendRowAdapter(List<User> users, Map<User, Alarm> alarmMap) {
        this.users = users;
        this.alarmMap = alarmMap;
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
        User user  = users.get(position);
        Alarm alarm = alarmMap.get(user);
        holder.row.setName(user);
        if (alarm != null) {
            holder.row.setTime(Alarm.getTime(alarm.getMin(), alarm.getHour()), user);
            holder.row.setJoinButton(user, alarm);
        } else {
            holder.row.setTime("No alarms", user);
        }
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
