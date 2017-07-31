package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.List;

import static braude.motorsequence.R.id.email;

/**
 * Created by ASAF on 28/7/2017.
 */
public class ParticipantEntry extends AbstractDbAdapter {
    //---------------columns---------------//
    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String AGE = "age";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String GROUP = "'group'";
    //-------------------------------------//

    public final String DATABASE_TABLE = "participant";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ParticipantEntry(Context ctx) {
        super(ctx);
    }


//UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='participant'
}
