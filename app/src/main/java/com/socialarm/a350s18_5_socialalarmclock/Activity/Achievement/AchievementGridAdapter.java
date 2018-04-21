package com.socialarm.a350s18_5_socialalarmclock.Activity.Achievement;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.List;

/**
 * Created by Henry on 4/21/2018.
 */

public class AchievementGridAdapter extends BaseAdapter {

    private Context context;
    private List<Achievement> achievementList;
    private LayoutInflater inflater;
    private View view;

    public AchievementGridAdapter(Context context, List<Achievement> achievementList) {
        this.context = context;
        this.achievementList = achievementList;
    }

    @Override
    public int getCount() {
        return achievementList.size();
    }

    @Override
    public Object getItem(int position) {
        return achievementList.get(0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Called to initialize the grid layout
     * @param position Current position
     * @param convertView view
     * @param parent parent view
     * @return current view of grid
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            view = inflater.inflate(R.layout.achievement_grid_element, null);

            Achievement achievement = achievementList.get(position);

            ImageView image = view.findViewById(R.id.achievementImage);

            //make image grayscale if achievement isn't accomplished yet
            if(!achievement.getIs_achieved()) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                image.setColorFilter(filter);
            }
            Drawable drawable = view.getResources().getDrawable(achievement.getImage());
            image.setImageDrawable(drawable);

            //set the name
            TextView name = view.findViewById(R.id.achievementName);
            name.setText(achievement.getName());

            //set the description
            TextView description = view.findViewById(R.id.achievementDescription);
            name.setText(achievement.getDescription());
        }
        return view;
    }
}
