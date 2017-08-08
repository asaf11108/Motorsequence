package database;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by ASAF on 6/8/2017.
 */
public class RecordRoundEntry extends AbstractDbAdapter{

    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String PK_TEST_SET_ID = "testSetID";
    public static final String PK_RECORD_TEST_ID = "recordTestID";
    public static final String PK_RECORD_ROUND_ID = "recordRoundID";
    public static final String ROUND_TIME = "round_time";
    public static final String MAX_VELOCITY = "max_velocity";

    private static RecordRoundEntry mRecordRoundEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private RecordRoundEntry(Context ctx) {
        super(ctx, "RecordRound");
    }

    static RecordRoundEntry getInstance(Context context){
        if (mRecordRoundEntry == null) mRecordRoundEntry = new RecordRoundEntry(context);
        return mRecordRoundEntry;
    }

    /**
     * create record test to insert
     *
     * @return record round participanID or -1 if faild
     */
    public long create(int participantID, int testSetID, int recordTestID, int recordRoundID,
                       double round_time, double max_velocity){
        ContentValues values = new ContentValues();
        values.put(PK_PARTICIPANT_ID, participantID);
        values.put(PK_TEST_SET_ID, testSetID);
        values.put(PK_RECORD_TEST_ID, recordTestID);
        values.put(PK_RECORD_ROUND_ID, recordRoundID);
        values.put(ROUND_TIME, round_time);
        values.put(MAX_VELOCITY, max_velocity);
        return insert(values);
    }
}
