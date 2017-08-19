package database;

import android.database.Cursor;

import java.sql.Date;

import util.MyArrayList;
import util.MyPair;

import static database.RecordTest.numOfTests;

/**
 * Created by ASAF on 16/8/2017.
 */
class RecordRound implements Identifier, Adder{

    private int participantID;
    private int testSetID;
    private int recordTestID;

    public RecordRound(int participantID, int testSetID, int recordTestID, int recordRoundID) {
        this.participantID = participantID;
        this.testSetID = testSetID;
        this.recordTestID = recordTestID;
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        Cursor cursor = rte.fetch(
                null,
                new MyPair[]{new MyPair(rte.PK_PARTICIPANT_ID, participantID),
                        new MyPair(rte.PK_TEST_SET_ID, testSetID),
                        new MyPair(rte.PK_RECORD_TEST_ID, recordTestID)});
//        date = new Date(cursor.getLong(cursor.getColumnIndex(rte.DATE))*1000);
//        totalTime = cursor.getDouble(cursor.getColumnIndex(rte.VELOCITY_PEEKS));
//        velocityPeaks = cursor.getDouble(cursor.getColumnIndex(rte.MAX_VELOCITY));
//        cursor.close();
//        recordRounds = new MyArrayList<>(numOfTests, this);
    }

    @Override
    public void myAdd(int i, int id) {

    }

    @Override
    public void myAdd(int id) {

    }

    @Override
    public int getID() {
        return 0;
    }
}
