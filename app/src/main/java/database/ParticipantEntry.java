package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.List;

import static util.Methods.convertToString;

/**
 * Created by ASAF on 28/7/2017.
 */
public class ParticipantEntry extends AbstractDbAdapter {
    //---------------columns---------------//
    public static final String PK_PARTICIPANT_ID = "participantID";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String AGE = "age";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String GROUP = "group";
    //-------------------------------------//

    private static final String DATABASE_TABLE = "participant";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ParticipantEntry(Context ctx) {
        super(ctx);
    }


    /**
     * Insert a new record.
     *
     * @return rowId or -1 if failed
     */
    public long insert(String first_name, String last_name, int age, String email, String user_name, String password, String group){
        SQLiteDatabase db = open().getWritableDatabase();
        ContentValues args = new ContentValues();

        args.put(FIRST_NAME, first_name);
        args.put(LAST_NAME, last_name);
        args.put(AGE, age);
        args.put(EMAIL, email);
        args.put(USER_NAME, user_name);
        args.put(PASSWORD, password);
        args.put(GROUP, group);

        long tmp = db.insert(DATABASE_TABLE, null,args);
        close();
        return tmp;
    }

    /**
     * Update the record.
     *
     * @param pk_participant_id
     * @param values - the values to update
     * @return the number of rows affected
     */
    public int update(int pk_participant_id, ContentValues values){
        SQLiteDatabase db = open().getWritableDatabase();

        int count = db.update(DATABASE_TABLE, values, PK_PARTICIPANT_ID + " = ?", new String[]{String.valueOf(pk_participant_id)});

        close();
        return count;
    }

    /**
     * Delete the record with the given pk_participant_id
     *
     * @param pk_participant_id
     * @return true if deleted, false otherwise
     */
    public boolean delete(int pk_participant_id) {
        SQLiteDatabase db = open().getWritableDatabase();

        boolean tmp = db.delete(DATABASE_TABLE, PK_PARTICIPANT_ID + "=" + pk_participant_id, null) > 0;

        db.close();
        return tmp;
    }

    /**
     * Return a Cursor over the list of all table in the database.
     * note - close cursor when done using!
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAll(){
        SQLiteDatabase db = open().getReadableDatabase();

        Cursor tmp = db.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        close();
        return tmp;
    }

    /**
     * Return a Cursor positioned at the record that matches the given rowId.
     * note - close cursor when done using!
     *
     * @param pairList list of all selection parameters to filter
     * @return Cursor positioned to matching record, if found
     */
    public Cursor fetch(List<Pair<String,String>> pairList){
        SQLiteDatabase db = open().getReadableDatabase();
        String where = convertToString(pairList);

        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + "WHERE " + where , null);

        if (mCursor != null)
            mCursor.moveToFirst();
        close();
        return mCursor;
    }
}
