package com.socialarm.a350s18_5_socialalarmclock;

/**
 * Created by liamdugan on 2018/03/14.
 */

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    public String name;
    public Integer statistic;
    public LeaderBoardFragment.SortDirection sortDirection;

    public LeaderboardEntry(String name, Integer statistic, LeaderBoardFragment.SortDirection sortDirection) {
        this.name = name;
        this.statistic = statistic;
        this.sortDirection = sortDirection;
    }

    @Override
    public int compareTo(LeaderboardEntry entry) {
        if (entry == null) { // TODO: Is this the desired behavior? not sure
            return -1;
        }
        switch (sortDirection) {
            case MOST:
                return (-1) * Integer.compare(this.statistic, entry.statistic);
            case LEAST:
                return Integer.compare(this.statistic, entry.statistic);
            default:
                return Integer.compare(this.statistic, entry.statistic);
        }
    }
}
