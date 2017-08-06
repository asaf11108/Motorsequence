package entity;

import android.database.Cursor;

import database.FactoryEntry;
import database.ParticipantEntry;
import util.MyPair;


public class Participant {

	public final int id;
	public String firstName;
	public String lastName;
	public int age;
	public String email;
	public String userName;
	public String password;
	public String group;
    public int testSetSeq;

	public Participant(int id) {
        this.id = id;
        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        Cursor cursor = pe.fetch(
                            null,
                            new MyPair[]{new MyPair(pe.PK_AI_PARTICIPANT_ID, id)});
        firstName = cursor.getString(cursor.getColumnIndex(pe.FIRST_NAME));
        lastName = cursor.getString(cursor.getColumnIndex(pe.LAST_NAME));
        age = cursor.getInt(cursor.getColumnIndex(pe.AGE));
        email = cursor.getString(cursor.getColumnIndex(pe.EMAIL));
        userName = cursor.getString(cursor.getColumnIndex(pe.USER_NAME));
        password = cursor.getString(cursor.getColumnIndex(pe.PASSWORD));
        group = cursor.getString(cursor.getColumnIndex(pe.GROUP));
        testSetSeq = cursor.getInt(cursor.getColumnIndex(pe.TEST_SET_SEQ));
        cursor.close();
	}
}