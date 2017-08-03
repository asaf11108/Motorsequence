package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.List;

import util.MyPair;

/**
 * Created by ASAF on 26/7/2017.
 */
public abstract class AbstractDbAdapter {

    private static final String TABLE_CREATE_Participant =
            "CREATE TABLE Participant (" +
                    "participantID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name varchar(20) NOT NULL, " +
                    "last_name varchar(20) NOT NULL, " +
                    "age integer(10) NOT NULL, " +
                    "email varchar(50) NOT NULL, " +
                    "user_name varchar(50) NOT NULL UNIQUE, " +
                    "password varchar(50) NOT NULL, " +
                    "_group varchar(255) NOT NULL, " +
                    "testSetSeq integer(10) NOT NULL)";
    private static final String TABLE_CREATE_TestSet =
            "CREATE TABLE TestSet (participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "testTypeID integer(10) NOT NULL, " +
                    "recordTestSeq integer(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID), " +
                    "FOREIGN KEY(participantID) REFERENCES Participant(participantID), " +
                    "FOREIGN KEY(testTypeID) REFERENCES TestType(testTypeID))";
    private static final String TABLE_CREATE_TestType =
            "CREATE TABLE TestType (" +
                    "testTypeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "num_of_tests integer(10) NOT NULL, " +
                    "A_x double(10) NOT NULL, " +
                    "A_y double(10) NOT NULL, " +
                    "B_x double(10) NOT NULL, " +
                    "B_y double(10) NOT NULL, " +
                    "C_x double(10) NOT NULL, " +
                    "C_y double(10) NOT NULL, " +
                    "D_x double(10) NOT NULL, " +
                    "D_y double(10) NOT NULL)";
    private static final String TABLE_CREATE_RecordTest =
            "CREATE TABLE RecordTest (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "_date date NOT NULL, " +
                    "total_time double(10) NOT NULL, " +
                    "velocity_peaks double(10) NOT NULL, " +
                    "max_velocity double(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID, recordTestID), " +
                    "FOREIGN KEY(participantID, testSetID) REFERENCES TestSet(participantID, testSetID))";
    private static final String TABLE_CREATE_RecordRound =
            "CREATE TABLE RecordRound (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "recordRoundID integer(10) NOT NULL, " +
                    "round_time double(10) NOT NULL, " +
                    "max_velocity double(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID, recordTestID, recordRoundID), " +
                    "FOREIGN KEY(participantID, testSetID, recordTestID) REFERENCES RecordTest(participantID, testSetID, recordTestID))";
    private static final String TABLE_CREATE_XYRound =
            "CREATE TABLE XYRound (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "recordRoundID integer(10) NOT NULL, " +
                    "x double(10) NOT NULL, " +
                    "y double(10) NOT NULL, " +
                    "s double(10) NOT NULL, " +
                    "v double(10) NOT NULL, " +
                    "jerk double(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID, recordTestID, recordRoundID), " +
                    "FOREIGN KEY(participantID, testSetID, recordTestID, recordRoundID) REFERENCES RecordRound(participantID, testSetID, recordTestID, recordRoundID))";
    private static final String INSERT_1_TestType =
            "INSERT INTO";

    protected static final String TAG = "AbstractDbAdapter";
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 3;

    private final String tableName;
    private DatabaseHelper mDbHelper;
    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     * @param tableName database table name
     */
    public AbstractDbAdapter(Context ctx, String tableName) {
        this.mCtx = ctx;
        this.tableName = tableName;
    }

    /**
     * @return tableName - database table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Insert a new record.
     *
     * @return rowId or -1 if failed
     */
    public long insert(ContentValues values){
        SQLiteDatabase db = open().getWritableDatabase();
        long tmp = db.insert(tableName, null,values);
        close();
        return tmp;
    }

    /**
     * Update the record.
     *
     * @param values the columns to update
     * @param where list of all where rows to update
     * @return the number of rows affected
     */
    public int update(ContentValues values, List<MyPair> where){
        SQLiteDatabase db = open().getWritableDatabase();
        StringBuilder whereClause =  new StringBuilder();
        String[] whereArgs = convertToWhereStr(where, whereClause);
        int count = db.update(tableName, values, whereClause.toString(), whereArgs);
        close();
        return count;
    }

    /**
     * Delete the record.
     *
     * @param where list of all where rows to delete
     * @return true if deleted, false otherwise
     */
    public boolean delete(List<MyPair> where) {
        SQLiteDatabase db = open().getWritableDatabase();
        StringBuilder whereClause =  new StringBuilder();
        String[] whereArgs = convertToWhereStr(where, whereClause);
        boolean tmp = db.delete(tableName, whereClause.toString(), whereArgs) > 0;
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
        Cursor tmp = db.rawQuery("SELECT * FROM " + tableName, null);
        close();
        return tmp;
    }

    /**
     * Return a Cursor positioned at the record that matches the given where list.
     * note - close cursor when done using!
     *
     * @param columns list of all selection columns to filter. null indicates: select * ...
     * @param where list of all where rows to filter
     * @return Cursor positioned to matching record, if found
     */
    public Cursor fetch(String[] columns, List<MyPair> where){
        SQLiteDatabase db = open().getReadableDatabase();
        StringBuilder whereClause =  new StringBuilder();
        String[] whereArgs = convertToWhereStr(where, whereClause);
        Cursor mCursor = db.query(false, tableName, columns, whereClause.toString(), whereArgs, null, null, null, null);
        if (mCursor != null)
            mCursor.moveToFirst();
        close();
        return mCursor;
    }

    /**
     * Open or create the data database.
     *
     * @return this
     */
    protected SQLiteOpenHelper open() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mCtx);
        }
        return mDbHelper;
    }

    protected void close() {
        mDbHelper.close();
    }

    protected String[] convertToWhereStr(List<MyPair> pairList, StringBuilder whereClause){
        String[] whereArgs = new String[pairList.size()];
        String prefix = "";
        for (int i = 0; i <pairList.size(); i++){
            MyPair pair = pairList.get(i);
            whereClause.append(prefix).append(pair.first).append(" = ?");
            prefix = " AND ";
            whereArgs[i] = pair.second;

        }
        return whereArgs;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_Participant);
            db.execSQL(TABLE_CREATE_TestSet);
            db.execSQL(TABLE_CREATE_TestType);
            db.execSQL(TABLE_CREATE_RecordTest);
            db.execSQL(TABLE_CREATE_RecordRound);
            db.execSQL(TABLE_CREATE_XYRound);
            Log.w(TAG, "Upgrading database from version " + DATABASE_VERSION + ", which will destroy all old data");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

//            db.execSQL("DROP TABLE IF EXISTS participant");
//            db.execSQL(TABLE_CREATE_Participant);
        }
    }
}

