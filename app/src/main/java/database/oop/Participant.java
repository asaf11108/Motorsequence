package database.oop;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import database.tables.FactoryEntry;
import database.tables.ParticipantEntry;
import database.tables.SQLiteSequenceEntry;
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
    private static List<Participant> participants = new ArrayList<>();
    private static boolean allCheck = false;

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

    public static List<Participant> getAllParticipants(){
        if (allCheck)
            return participants;
        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        int participantSeq = 0;
        SQLiteSequenceEntry sqlSeq = FactoryEntry.getSQLiteSequenceEntry();
        Cursor cursor = sqlSeq.fetch(new String[]{sqlSeq.SEQ}, new MyPair[]{new MyPair(sqlSeq.NAME, pe.getTableName())});
        if (cursor.moveToFirst())
            participantSeq =  cursor.getInt(cursor.getColumnIndex(sqlSeq.SEQ));
        cursor.close();

        for (int i = 1; i <= participantSeq; i++)
            participants.add(new Participant(i));

        allCheck = true;
        return participants;
    }

    public static void addParticipant(long rowId) {
        if (allCheck)
            participants.add(new Participant((int) rowId));
    }
}