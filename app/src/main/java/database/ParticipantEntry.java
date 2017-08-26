package database;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by ASAF on 28/7/2017.
 */
public class ParticipantEntry extends AbstractDbAdapter {

    public static final String PK_AI_PARTICIPANT_ID = "participantID";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String AGE = "age";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String GROUP = "_group";
    public static final String TEST_SET_SEQ = "testSetSeq";

    private static ParticipantEntry mParticipantEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private ParticipantEntry(Context ctx) {
        super(ctx, "participant");
    }

    static ParticipantEntry getInstance(Context context){
        if (mParticipantEntry == null) mParticipantEntry = new ParticipantEntry(context);
        return mParticipantEntry;
    }

    /**
     * create participant to insert
     *
     * @return rowId or -1 if failed
     */
    public long create(String first_name, String last_name, int age, String email, String user_name, String password, String group){
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, first_name);
        values.put(LAST_NAME, last_name);
        values.put(AGE, age);
        values.put(EMAIL, email);
        values.put(USER_NAME, user_name);
        values.put(PASSWORD, password);
        values.put(GROUP, group);
        values.put(TEST_SET_SEQ, 0);
        long tmp = insert(values);
        return tmp;
    }


//UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='participant'
}
