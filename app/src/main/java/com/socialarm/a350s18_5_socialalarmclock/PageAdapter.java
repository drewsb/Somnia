package com.socialarm.a350s18_5_socialalarmclock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments)
    {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // Show number of tabs
        return fragments.size();
    }
}