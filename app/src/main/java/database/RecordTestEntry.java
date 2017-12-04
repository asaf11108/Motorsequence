package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import util.MyPair;

/**
 * Created by ASAF on 6/8/2017.
 */
public class RecordTestEntry extends AbstractDbAdapter {

    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String PK_TEST_SET_ID = "testSetID";
    public static final String PK_RECORD_TEST_ID = "recordTestID";
    public static final String RECORD_ROUND_SEQ = "recordRoundSeq";
    public static final String DATE = "_date";
    public static final String TOTAL_TIME = "total_time";
    public static final String MAX_VELOCITY = "max_velocity";
    public static final String VELOCITY_PEEKS = "velocity_peaks";

    private static RecordTestEntry mRecordTestEntry;
    private static final String TAG = "RecordTestEntry";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private RecordTestEntry(Context ctx) {
        super(ctx, "RecordTest");
    }

    static RecordTestEntry getInstance(Context context){
        if (mRecordTestEntry == null) mRecordTestEntry = new RecordTestEntry(context);
        return mRecordTestEntry;
    }

    /**
     * create record test to insert
     *
     * @return record test participanID or -1 if faild
     */
    long create(int participantID, int testSetID){
        ContentValues values = new ContentValues();
        values.put(PK_PARTICIPANT_ID, participantID);
        values.put(PK_TEST_SET_ID, testSetID);
        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        Cursor cursor = tse.fetch(
                new String[]{tse.RECORD_TEST_SEQ},
                new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participantID),
                            new MyPair(tse.PK_TEST_SET_ID, testSetID)});
        int recordTestID = cursor.getInt(cursor.getColumnIndex(tse.RECORD_TEST_SEQ));
        cursor.close();
        recordTestID++;
        Log.d(TAG, "recordTestID " + recordTestID);
        values.put(PK_RECORD_TEST_ID, recordTestID);
        values.put(RECORD_ROUND_SEQ, 0);
        values.put(DATE, System.currentTimeMillis());
        values.put(TOTAL_TIME, 0);
        values.put(MAX_VELOCITY, 0);
        values.put(VELOCITY_PEEKS, 0);
        boolean flag = insert(values) != -1;
        if (flag){
            values.clear();
            values.put(tse.RECORD_TEST_SEQ, recordTestID);
            flag = tse.update(
                    values,
                    new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participantID),
                                new MyPair(tse.PK_TEST_SET_ID, testSetID)}) == 1;
        }
        return (flag) ? (recordTestID) : (-1);
    }
    /**
     * delete record test
     *
     * @return record test participanID or -1 if faild
     */
    boolean delete(int participantID, int testSetID, int recordTestID){
        XYRoundEntry xyre = FactoryEntry.getXYRoundEntry();
        boolean flag = xyre.delete(new MyPair[]{new MyPair(xyre.PARTICIPANT_ID, participantID),
                                new MyPair(xyre.TEST_SET_ID, testSetID),
                                new MyPair(xyre.RECORD_TEST_ID, recordTestID)});
        if (flag) {
            RecordRoundEntry rre = FactoryEntry.getRecordRoundEntry();
            flag = rre.delete(new MyPair[]{new MyPair(rre.PK_PARTICIPANT_ID, participantID),
                    new MyPair(rre.PK_TEST_SET_ID, testSetID),
                    new MyPair(rre.PK_RECORD_TEST_ID, recordTestID)});
            if (flag) {
                flag = delete(new MyPair[]{new MyPair(PK_PARTICIPANT_ID, participantID),
                        new MyPair(PK_TEST_SET_ID, testSetID),
                        new MyPair(PK_RECORD_TEST_ID, recordTestID)});

                recordTestID--;
                Log.d(TAG, "recordTestID " + recordTestID);
                TestSetEntry tse = FactoryEntry.getTestSetEntry();

                if (flag) {
                    ContentValues values = new ContentValues();
                    values.put(tse.RECORD_TEST_SEQ, recordTestID);
                    flag = tse.update(
                            values,
                            new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participantID),
                                    new MyPair(tse.PK_TEST_SET_ID, testSetID)}) == 1;
                }
            }
        }
        return flag;
    }
}
