package database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import util.MyArrayList;
import util.MyPair;

/**
 * Created by ASAF on 5/8/2017.
 */

public class TestSet implements Identifier, Adder {

    private final int participantID;
    private final int testSetID;
    public TestType testType;
    public int recordTestSeq;
    public MyArrayList<RecordTest> recordTests;

    public TestSet(int participantID, int testSetID) {
        this.participantID = participantID;
        this.testSetID = testSetID;
        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        Cursor cursor = tse.fetch(
                null,
                new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participantID),
                        new MyPair(tse.PK_TEST_SET_ID, testSetID)});
        int testTypeID = cursor.getInt(cursor.getColumnIndex(tse.TEST_TYPE_ID));
        this.testType = new TestType(testTypeID);
        this.recordTestSeq = cursor.getInt(cursor.getColumnIndex(tse.RECORD_TEST_SEQ));
        this.recordTests = new MyArrayList<>(recordTestSeq, this);
    }

    public int getID() {
        return testSetID;
    }

    public void myAdd(int i, int id){
        recordTests.add(i, new RecordTest(participantID, testSetID, id));
    }

    public void myAdd(int id){
        recordTests.add(new RecordTest(participantID, testSetID, id));
    }

}
