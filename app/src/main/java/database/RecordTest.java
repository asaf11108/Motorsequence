package database;

import android.database.Cursor;

import java.sql.Date;

import util.MyArrayList;
import util.MyPair;

/**
 * Created by ASAF on 8/8/2017.
 */
class RecordTest implements Identifier, Adder {

    private final int participantID;
    private final int testSetID;
    private final int recordTestID;
    public Date date;
    public double totalTime;
    public double velocityPeaks;
    public static final int numOfTests = 7;
    public MyArrayList<RecordRound> recordRounds;

    public RecordTest(int participantID, int testSetID, int recordTestID) {
        this.participantID = participantID;
        this.testSetID = testSetID;
        this.recordTestID = recordTestID;
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        Cursor cursor = rte.fetch(
                null,
                new MyPair[]{new MyPair(rte.PK_PARTICIPANT_ID, participantID),
                        new MyPair(rte.PK_TEST_SET_ID, testSetID),
                        new MyPair(rte.PK_RECORD_TEST_ID, recordTestID)});
        date = new Date(cursor.getLong(cursor.getColumnIndex(rte.DATE))*1000);
        totalTime = cursor.getDouble(cursor.getColumnIndex(rte.VELOCITY_PEEKS));
        velocityPeaks = cursor.getDouble(cursor.getColumnIndex(rte.MAX_VELOCITY));
        cursor.close();
        recordRounds = new MyArrayList<>(numOfTests, this);
    }
}
