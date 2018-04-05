package com.socialarm.a350s18_5_socialalarmclock;

/**
 * Created by liamdugan on 2018/03/14.
 *
 * Helper class to act as a paired name and statistic object for leaderboard sorting purposes
 */
public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    public String name;
    Integer statistic;
    LeaderBoardFragment.SortDirection sortDirection;

    LeaderboardEntry(String name, Integer statistic, LeaderBoardFragment.SortDirection sortDirection) {
        this.name = name;
        this.statistic = statistic;
        this.sortDirection = sortDirection;
    }

    /**
     * Override compareTo to sort according to the SortDirection that the entry was created with
     *
     * @param entry the entry to compare to
     * @return The same as normal if the Sort is "MOST" but opposite if "LEAST"
     */
    @Override
    public int compareTo(LeaderboardEntry entry) {
        if (entry == null) {
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
