package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Date;

import util.MyPair;

/**
 * Created by ASAF on 8/8/2017.
 */
public class RecordTest implements Identifier, NewObject<RecordRound> {

    private final TestSet testSet;
    private final int recordTestID;
    public long date;
    public double totalTime;
    public double maxVelocity;
    public double velocityPeaks;
    public MyArrayList<RecordRound> recordRounds;
//
    public RecordTest(TestSet testSet, int recordTestID) {
        this.testSet = testSet;
        this.recordTestID = recordTestID;
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        Cursor cursor = rte.fetch(
                null,
                new MyPair[]{new MyPair(rte.PK_PARTICIPANT_ID, testSet.getParent().getID()),
                        new MyPair(rte.PK_TEST_SET_ID, testSet.getID()),
                        new MyPair(rte.PK_RECORD_TEST_ID, recordTestID)});
        date = cursor.getLong(cursor.getColumnIndex(rte.DATE));
        totalTime = cursor.getDouble(cursor.getColumnIndex(rte.TOTAL_TIME));
        maxVelocity = cursor.getDouble(cursor.getColumnIndex(rte.MAX_VELOCITY));
        velocityPeaks = cursor.getDouble(cursor.getColumnIndex(rte.VELOCITY_PEEKS));
        int recordRoundSeq = cursor.getInt(cursor.getColumnIndex(rte.RECORD_ROUND_SEQ));
        cursor.close();
        recordRounds = new MyArrayList<>(recordRoundSeq, this);
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

    public RecordRound createRecordRound(){
        RecordRoundEntry rre = FactoryEntry.getRecordRoundEntry();
        if (rre.create(testSet.getParent().getID(), testSet.getID(), recordTestID) == -1) return null;
        return  recordRounds.add();
    }

    public void updateTestParameters(double totalTime, double maxVeloocity, int velocityPeaks) {
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        this.totalTime = totalTime;
        this.maxVelocity = maxVeloocity;
        this.velocityPeaks = velocityPeaks;
        ContentValues values = new ContentValues();
        values.put(rte.TOTAL_TIME, totalTime);
        values.put(rte.MAX_VELOCITY, maxVeloocity);
        values.put(rte.VELOCITY_PEEKS, velocityPeaks);
        rte.update(values,
                new MyPair[]{new MyPair(rte.PK_PARTICIPANT_ID, testSet.getParent().getID()),
                        new MyPair(rte.PK_TEST_SET_ID, testSet.getID()),
                        new MyPair(rte.PK_RECORD_TEST_ID, recordTestID)});
    }
}
