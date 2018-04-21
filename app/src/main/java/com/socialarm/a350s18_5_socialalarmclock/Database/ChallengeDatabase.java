package com.socialarm.a350s18_5_socialalarmclock.Database;

import com.socialarm.a350s18_5_socialalarmclock.Event.Event;
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
}
