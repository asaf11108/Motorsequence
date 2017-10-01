package database;

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
    public double velocityPeaks;
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
        date = cursor.getLong(cursor.getColumnIndex(rte.DATE));
        totalTime = cursor.getDouble(cursor.getColumnIndex(rte.VELOCITY_PEEKS));
        velocityPeaks = cursor.getDouble(cursor.getColumnIndex(rte.MAX_VELOCITY));
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
}
