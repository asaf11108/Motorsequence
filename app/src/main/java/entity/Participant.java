package entity;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import database.FactoryEntry;
import database.ParticipantEntry;
import util.MyPair;

import static android.media.CamcorderProfile.get;


public class Participant {

	public final int participantID;
	public String firstName;
	public String lastName;
	public int age;
	public String email;
	public String userName;
	public String password;
	public String group;
    public int testSetSeq;
    public List<TestSet> testSets;

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
        testSetSeq = cursor.getInt(cursor.getColumnIndex(pe.TEST_SET_SEQ));
        cursor.close();
        testSets = new ArrayList<>();
	}

    /**
     * add new test set to list.
     * @param i thr disirable test set index
     * @return true or false if the parameter i is invalid
     */
	public boolean attachTestSet(int i){
        if (i <= 0 || i > testSetSeq) return false;
        insert(i);
        return true;
    }

    public void attachLastTestSet(){
        attachTestSet(testSetSeq);
    }

    public void attachAllTestSet(){
        for (int i = 1; i <= testSetSeq; i++) attachTestSet(i);
    }

    private void insert(int x){
        for (int i = 0; i < testSets.size(); i++) {
            if (testSets.get(i).testSetID < x) continue;
            if (testSets.get(i).testSetID == x) return;
            testSets.add(i, new TestSet(participantID, x));
            return;
        }
        testSets.add(new TestSet(participantID, x));
    }
}