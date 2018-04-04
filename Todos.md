![logo](https://github.com/cis-upenn/350S18-5-SocialAlarmClock/blob/master/app/src/main/res/drawable/somnia.png)

## Issues and TODOs broken down by class

### EventDatabase
- [ ] getLeaderboardEventsSince - make more modular / combine with other get events since
- [ ] getLeaderboardEventsSince - make less slow
- [ ] TimeDifference - combine with Duration enum
- [ ] getEventsSince - Refactor to be less slow
- [ ] getFriends - Refactor to be less slow
- [ ] getUser - Refactor to be less slow

### FriendRowAdapter
- [ ] onCreateViewHolder - investigate what the value viewType corresponds to for comment reasons

### FriendsFragment
- [ ] onButtonPressed - Have this trigger the OptionsActivity (when it gets created) or something rather than nothing - possibly have this function trigger the stats page rather than the button
- [ ] onFragmentInteractionListener - Update argument type and name

### LeaderboardRowAdapter
- [ ] onCreateViewHolder - investigate what the value viewType corresponds to for comment reasons

### MainActivity
- [ ] onNavigationItemSelection - Implemenet the case when id == nav_settings

### MyAlarms
- [ ] OnFragmentInteractionListener - Update argument type and name

### SearchFriendActivity
- [ ] onCreate - Change the array of friends to be from the database instead of hardcoded

### StatisticsActivity
- [ ] updateFriendNotifications - Update database / implement
- [ ] updateFriendPrivileges - Update databse / implement
