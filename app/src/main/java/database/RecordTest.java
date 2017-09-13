package database;

import android.database.Cursor;

import java.sql.Date;

import util.MyPair;

/**
 * Created by ASAF on 8/8/2017.
 */
class RecordTest implements Identifier, NewObject<RecordRound> {

    private final TestSet testSet;
    private final int recordTestID;
    public Date date;
    public double totalTime;
    public double velocityPeaks;
    public static final int NUM_OF_TESTS = 7;
    public MyArrayList<RecordRound> recordRounds;

    public RecordTest(TestSet testSet, int recordTestID) {
        this.testSet = testSet;
        this.recordTestID = recordTestID;
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        Cursor cursor = rte.fetch(
                null,
                new MyPair[]{new MyPair(rte.PK_PARTICIPANT_ID, testSet.getParent().getID()),
                        new MyPair(rte.PK_TEST_SET_ID, testSet.getID()),
                        new MyPair(rte.PK_RECORD_TEST_ID, recordTestID)});
        date = new Date(cursor.getLong(cursor.getColumnIndex(rte.DATE)) * 1000);
        totalTime = cursor.getDouble(cursor.getColumnIndex(rte.VELOCITY_PEEKS));
        velocityPeaks = cursor.getDouble(cursor.getColumnIndex(rte.MAX_VELOCITY));
        cursor.close();
        recordRounds = new MyArrayList<>(NUM_OF_TESTS, this);
    }

    public TestSet getParent() {
        return testSet;
    }

    @Override
    public int getID() {
        return recordTestID;
    }

    @Override
    public RecordRound newObject(int id) {
        return new RecordRound(this, id);
    }
}
