package database;

import android.database.Cursor;

import database.tables.RecordTestEntry;
import database.tables.TestSetEntry;
import util.MyPair;

/**
 * Created by ASAF on 5/8/2017.
 */

public class TestSet implements Identifier, NewObject<RecordTest> {

    private final Participant participant;
    private final int testSetID;
    public TestType testType;
    public MyArrayList<RecordTest> recordTests;

    TestSet(Participant participant, int testSetID) {
        this.participant = participant;
        this.testSetID = testSetID;

        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        Cursor cursor = tse.fetch(
                null,
                new MyPair[]{new MyPair(tse.PK_PARTICIPANT_ID, participant.getID()),
                        new MyPair(tse.PK_TEST_SET_ID, testSetID)});
        this.testType = new TestType(cursor.getInt(cursor.getColumnIndex(tse.TEST_TYPE_ID)));
        int recordTestSeq = cursor.getInt(cursor.getColumnIndex(tse.RECORD_TEST_SEQ));

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

    public RecordTest createRecordTest(){
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        if (rte.create(participant.getID(), testSetID) == -1) return null;
        return recordTests.add();
    }

    public void deleteRecordTest() {
        RecordTestEntry rte = FactoryEntry.getRecordTestEntry();
        rte.delete(participant.getID(), testSetID, recordTests.getSeq());
        recordTests.remove();
    }
}
