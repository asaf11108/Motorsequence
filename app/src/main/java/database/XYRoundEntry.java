package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by ASAF on 6/8/2017.
 */
public class XYRoundEntry extends AbstractDbAdapter {

    public static final String PARTICIPANT_ID = "participantID";
    public static final String TEST_SET_ID = "testSetID";
    public static final String RECORD_TEST_ID = "recordTestID";
    public static final String RECORD_ROUND_ID = "recordRoundID";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String S = "s";
    public static final String V = "v";
    public static final String JERK = "jerk";

    private static XYRoundEntry mXYRoundEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private XYRoundEntry(Context ctx) {
        super(ctx, "XYRound");
    }

    static XYRoundEntry getInstance(Context context){
        if (mXYRoundEntry == null) mXYRoundEntry = new XYRoundEntry(context);
        return mXYRoundEntry;
    }

    /**
     * create xy record to insert
     *
     * @return row participanID or -1 if faild
     */
    long create(SQLiteStatement statement,
                       float x, float y, long s, double v, double jerk){
        statement.clearBindings();
        statement.bindDouble(1, x);
        statement.bindDouble(2, y);
        statement.bindLong(3, s);
        statement.bindDouble(4, v);
        statement.bindDouble(5, jerk);
        return statement.executeInsert();
    }
}
