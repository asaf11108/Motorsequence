package entity;

import android.database.Cursor;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import database.ParticipantEntry;

import static android.R.attr.x;

public class Participant {

	public int id;
	public String firstName;
	public String lastName;
	public int age;
	public String email;
	public String userName;
	public String password;
	public String group;
    public int testSetSeq;

	public Participant(int id, ParticipantEntry pe) {
		this.id = id;
		//fetch
        List<Pair<String, String>> where = new ArrayList<Pair<String, String>>(1);
        where.add(new Pair<String, String>(pe.PK_AI_PARTICIPANT_ID, String.valueOf(id)));
        Cursor cursor = pe.fetch(null, where);
        firstName = cursor.getString(cursor.getColumnIndex(pe.FIRST_NAME));
        lastName = cursor.getString(cursor.getColumnIndex(pe.LAST_NAME));
        age = cursor.getInt(cursor.getColumnIndex(pe.AGE));
        email = cursor.getString(cursor.getColumnIndex(pe.EMAIL));
        userName = cursor.getString(cursor.getColumnIndex(pe.USER_NAME));
        password = cursor.getString(cursor.getColumnIndex(pe.PASSWORD));
        group = cursor.getString(cursor.getColumnIndex(pe.GROUP));
        testSetSeq = cursor.getInt(cursor.getColumnIndex(pe.TEST_SET_SEQ));
	}
}