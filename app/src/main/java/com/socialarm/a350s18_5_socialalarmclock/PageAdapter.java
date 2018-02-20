package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Fatman on 2/19/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter{
    int numTabs;

    public PageAdapter(FragmentManager fragmentManager, int numTabs)
    {
        super(fragmentManager);

        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int pos)
    {
        switch (pos)
        {
            case 0:
                MyAlarmFragment myAlarmFragment = new MyAlarmFragment();
                return myAlarmFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 2:
                LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
                return leaderBoardFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }
}
