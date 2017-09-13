package database;

import android.database.Cursor;

import util.MyPair;

/**
 * Created by ASAF on 5/8/2017.
 */

public class TestSet implements Identifier, NewObject<RecordTest> {

    private final Participant participant;
    private final int testSetID;
    public TestType testType;
    public int recordTestSeq;
    public MyArrayList<RecordTest> recordTests;

    TestSet(Participant participant, int testSetID) {
        this.participant = participant;
        this.testSetID = testSetID;
        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        Cursor cursor = tse.fetch(
                null,
                new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participant.getID()),
                        new MyPair(tse.PK_TEST_SET_ID, testSetID)});
        int testTypeID = cursor.getInt(cursor.getColumnIndex(tse.TEST_TYPE_ID));
        this.testType = new TestType(testTypeID);
        this.recordTestSeq = cursor.getInt(cursor.getColumnIndex(tse.RECORD_TEST_SEQ));
        this.recordTests = new MyArrayList<>(recordTestSeq, this);
    }

    Participant getParent() {
        return participant;
    }

    @Override
    public int getID() {
        return testSetID;
    }

    @Override
    public RecordTest newObject(int id) {
        return new RecordTest(this, id);
    }
}
