package util;

import android.app.Application;

import database.Participant;

/**
 * Created by ASAF on 1/10/2017.
 */

public class MyApplication extends Application {

    private static Participant mParticipant;

    public static Participant getParticipant() {
        return mParticipant;
    }

    public static void setParticipant(Participant participant) {
        mParticipant = participant;
    }
}
