package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                    "num_of_rounds integer(10) NOT NULL, " +
                    "A_x integer(10) NOT NULL, " +
                    "A_y integer(10) NOT NULL, " +
                    "B_x integer(10) NOT NULL, " +
                    "B_y integer(10) NOT NULL, " +
                    "C_x integer(10) NOT NULL, " +
                    "C_y integer(10) NOT NULL, " +
                    "D_x integer(10) NOT NULL, " +
                    "D_y integer(10) NOT NULL)";
    private static final String TABLE_CREATE_RecordTest =
            "CREATE TABLE RecordTest (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "recordRoundSeq integer(10) NOT NULL, " +
                    "_date INTEGER NOT NULL, " +
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
                    "x REAL NOT NULL, " +
                    "y REAL NOT NULL, " +
                    "s INTEGER NOT NULL, " +
                    "v double(10) NOT NULL, " +
                    "jerk double(10) NOT NULL, " +
                    "FOREIGN KEY(participantID, testSetID, recordTestID, recordRoundID) REFERENCES RecordRound(participantID, testSetID, recordTestID, recordRoundID))";
    private static final String INSERT_ASAF =
            "INSERT INTO Participant (first_name, last_name, age, email, user_name, password, _group, testSetSeq) " +
                    "VALUES ('Asaf', 'Regev', 27, 'asaf11108@gmail.com', 'asaf', '123', 'ADHD', 0)";
    private static final String INSERT_1_TestType =
            "INSERT INTO TestType (num_of_tests, num_of_rounds, A_x, A_y, B_x, B_y, C_x, C_y, D_x, D_y) " +
                    "VALUES ( 5, 7, 850, 240, 400, 170, 150, 50, 650, 380)";

    protected static final String TAG = "AbstractDbAdapter";
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

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
    protected AbstractDbAdapter(Context ctx, String tableName) {
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
    public int update(ContentValues values, MyPair[] where){
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
    boolean delete(MyPair[] where) {
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
    public Cursor fetch(String[] columns, MyPair[] where){
        SQLiteDatabase db = open().getWritableDatabase();
        StringBuilder whereClause =  new StringBuilder();
        String[] whereArgs = convertToWhereStr(where, whereClause);
        Cursor mCursor = db.query(false, tableName, columns, whereClause.toString(), whereArgs, null, null, null, null);
        if (mCursor != null)
            mCursor.moveToFirst();
        close();
        return mCursor;
    }

    public SQLiteDatabase getDB(){
        return open().getWritableDatabase();
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

    protected String[] convertToWhereStr(MyPair[] pairList, StringBuilder whereClause){
        String[] whereArgs = new String[pairList.length];
        String prefix = "";
        for (int i = 0; i <pairList.length; i++){
            MyPair pair = pairList[i];
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

            db.execSQL(INSERT_ASAF);
            db.execSQL(INSERT_1_TestType);
//            db.execSQL(INSERT_2_TestType);
//            db.execSQL(INSERT_3_TestType);
//            db.execSQL(INSERT_4_TestType);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS XYRound");
            db.execSQL("DROP TABLE IF EXISTS RecordRound");
            db.execSQL("DROP TABLE IF EXISTS RecordTest");
            db.execSQL("DROP TABLE IF EXISTS TestType");
            db.execSQL("DROP TABLE IF EXISTS TestSet");
            db.execSQL("DROP TABLE IF EXISTS participant");
            onCreate(db);
        }
    }
}

