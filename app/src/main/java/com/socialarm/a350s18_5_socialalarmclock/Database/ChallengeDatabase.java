package com.socialarm.a350s18_5_socialalarmclock.Database;

import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.ChallengeReceiver;
import com.socialarm.a350s18_5_socialalarmclock.User.User;

import java.util.concurrent.TimeUnit;

/**
 * Created by Henry on 4/21/2018.
 */

public class ChallengeDatabase {

    /**
     * Sends a type of challenge event to db
     * @param challengee the challengee
     * @param challenger the challenger
     * @param challengeType type of challenge (send, accept, decline)
     */
    public static void SendChallengeToDB(User challengee, User challenger, String challengeType) {
        //call and make db event
        //create event of challenge
        int hashChallengeEvent = challengee.hashCode() << Integer.SIZE/2 | challenger.hashCode() & Integer.SIZE/2;
        Long currentTime = System.currentTimeMillis()/1000;
        Long finalTime = TimeUnit.DAYS.toMillis(30)/1000;

        //format of eventId is (challengerid - userid - current time - time when challenge ends)
        Event challengeEvent = new Event("Challenge", "" + hashChallengeEvent,
                challenger.getId(), challenger.getId() + "-" + challengee.getId() + "-" + challengeType + "-" + Long.toString(finalTime),
                currentTime);

        //send to database
        EventDatabase.addEvent(challengeEvent);
    }

    /**
     * Sends a type of challenge event to db
     */
    public static void cancelChallenges(String user_id) {
        EventDatabase.getAllEvents(eventList -> {
            for (Event e : eventList) {
                if(e.getAction().equals("Challenge")) {
                    String event_id = e.getEvent_id();
                    String[] split = event_id.split("-");
                    if (split[1].equals(user_id)) {
                        long time = Long.parseLong(split[3]);
                        long currTime = System.currentTimeMillis()/1000;
                        if(time > currTime) {
                            String challenger = split[0];
                            ChallengeReceiver.challengeFinish(false, 0, challenger, user_id);
                        }
                    }
                }
            }
        });
    }
    public static void SendChallengeToDBSpecific(User challengee, String type, User challenger, String challengeType) {
        //call and make db event
        //create event of challenge
        int hashChallengeEvent = challengee.hashCode() << Integer.SIZE/2 | challenger.hashCode() & Integer.SIZE/2;
        Long currentTime = System.currentTimeMillis()/1000;
        Long finalTime = TimeUnit.DAYS.toMillis(30)/1000;

        //format of eventId is (challengerid - userid - current time - time when challenge ends)
        Event challengeEvent = new Event("Challenge" + type, "" + hashChallengeEvent,
                challenger.getId(), challenger.getId() + "-" + challengee.getId() + "-" + challengeType + "-" + Long.toString(finalTime),
                currentTime);

        //send to database
        EventDatabase.addEvent(challengeEvent);
    }
}
