package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

public class FriendRowAdapter extends RecyclerView.Adapter<FriendRowAdapter.ViewHolder> {

    private List<User> users;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        FriendRow row;
        ViewHolder(FriendRow v) {
            super(v);
            row = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    FriendRowAdapter(List<User> users) {
        this.users = users;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends, parent, false); // TODO: Change this?
        FriendRow newRow = new FriendRow(v.getContext(), null);

        ViewHolder rh = new ViewHolder(newRow);
        return rh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.row.setName(users.get(position));
        holder.row.setStatisticButton(users.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
}
