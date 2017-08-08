package database;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by ASAF on 6/8/2017.
 */
public class XYRoundEntry extends AbstractDbAdapter {

    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String PK_TEST_SET_ID = "testSetID";
    public static final String PK_RECORD_TEST_ID = "recordTestID";
    public static final String PK_RECORD_ROUND_ID = "recordRoundID";
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
    public long create(int participantID, int testSetID, int recordTestID, int recordRoundID,
                       double x, double y, double s, double v, double jerk){
        ContentValues values = new ContentValues();
        values.put(PK_PARTICIPANT_ID, participantID);
        values.put(PK_TEST_SET_ID, testSetID);
        values.put(PK_RECORD_TEST_ID, recordTestID);
        values.put(PK_RECORD_ROUND_ID, recordRoundID);
        values.put(X, x);
        values.put(Y, y);
        values.put(S, s);
        values.put(V, v);
        values.put(JERK, jerk);
        return insert(values);
    }
}
