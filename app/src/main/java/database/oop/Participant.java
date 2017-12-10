package database.oop;

import android.database.Cursor;

import database.tables.FactoryEntry;
import database.tables.ParticipantEntry;
import database.tables.TestSetEntry;
import util.MyPair;

public class Participant implements Identifier, NewObject<TestSet> {

    private final int participantID;
    public String firstName;
    public String lastName;
    public int age;
    public String email;
    public String userName;
    public String password;
    public String group;
    public MyArrayList<TestSet> testSets;

    public Participant(int participantID) {
        this.participantID = participantID;

        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        Cursor cursor = pe.fetch(
                null,
                new MyPair[]{new MyPair(pe.PK_AI_PARTICIPANT_ID, participantID)});

        firstName = cursor.getString(cursor.getColumnIndex(pe.FIRST_NAME));
        lastName = cursor.getString(cursor.getColumnIndex(pe.LAST_NAME));
        age = cursor.getInt(cursor.getColumnIndex(pe.AGE));
        email = cursor.getString(cursor.getColumnIndex(pe.EMAIL));
        userName = cursor.getString(cursor.getColumnIndex(pe.USER_NAME));
        password = cursor.getString(cursor.getColumnIndex(pe.PASSWORD));
        group = cursor.getString(cursor.getColumnIndex(pe.GROUP));
        int testSetSeq = cursor.getInt(cursor.getColumnIndex(pe.TEST_SET_SEQ));
        cursor.close();
        testSets = new MyArrayList<>(testSetSeq, this);
    }

    @Override
    public int getID() {
        return participantID;
    }

    @Override
    public TestSet newObject(int id) {
        return new TestSet(this, id);
    }

    public TestSet createTestSet(int testTypeID){
        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        if (tse.create(participantID, testTypeID) == -1) return null;
        return testSets.add();
    }
}