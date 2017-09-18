package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import util.MyPair;

/**
 * Created by ASAF on 3/8/2017.
 */

public class TestSetEntry extends AbstractDbAdapter {

    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String PK_TEST_SET_ID = "testSetID";
    public static final String TEST_TYPE_ID = "testTypeID";
    public static final String RECORD_TEST_SEQ = "recordTestSeq";

    private static TestSetEntry mTestSetEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private TestSetEntry(Context ctx) { super(ctx, "TestSet"); }

    static TestSetEntry getInstance(Context context){
        if (mTestSetEntry == null) mTestSetEntry = new TestSetEntry(context);
        return mTestSetEntry;
    }

    /**
     * create test set to insert
     *
     * @return test set participanID or -1 if faild
     */
    long create(int participantID, int testTypeID){
        ContentValues values = new ContentValues();
        values.put(PK_PARTICIPANT_ID, participantID);
        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        Cursor cursor = pe.fetch(
                            new String[]{pe.TEST_SET_SEQ},
                            new MyPair[]{new MyPair(pe.PK_AI_PARTICIPANT_ID, participantID)});
        int testSetID = cursor.getInt(cursor.getColumnIndex(pe.TEST_SET_SEQ));
        testSetID++;
        values.put(PK_TEST_SET_ID, testSetID);
        values.put(TEST_TYPE_ID, testTypeID);
        values.put(RECORD_TEST_SEQ, 0);
        boolean flag = insert(values) != -1;
        if (flag){
            values.clear();
            values.put(pe.TEST_SET_SEQ, testSetID);
            flag = pe.update(
                        values,
                        new MyPair[]{new MyPair(pe.PK_AI_PARTICIPANT_ID, participantID)}) == 1;
        }
        return (flag) ? (testSetID) : (-1);
    }
}
