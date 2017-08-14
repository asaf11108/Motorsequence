package database;

import android.database.Cursor;

import java.util.ArrayList;

import util.MyArrayList;
import util.MyPair;

import static android.R.attr.x;
import static android.media.CamcorderProfile.get;


public class Participant implements Identifier, Adder {

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

    public int getID() {
        return participantID;
    }

    public void myAdd(int i, int id){
        testSets.add(i, new TestSet(participantID, id));
    }

    public void myAdd(int id){
        testSets.add(new TestSet(participantID, id));
    }

}