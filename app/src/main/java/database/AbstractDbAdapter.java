package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.List;

/**
 * Created by ASAF on 26/7/2017.
 */
public abstract class AbstractDbAdapter {

    protected static final String TAG = "AbstractDbAdapter";
    protected DatabaseHelper mDbHelper;

    protected static final String TABLE_CREATE_Participant =
            "CREATE TABLE Participant (" +
                    "participantID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "first_name varchar(20) NOT NULL, " +
                    "last_name varchar(20) NOT NULL, " +
                    "age integer(10) NOT NULL, " +
                    "email varchar(50) NOT NULL, " +
                    "user_name varchar(50) NOT NULL, " +
                    "group varchar(255) NOT NULL)";
    protected static final String TABLE_CREATE_TestSet =
            "CREATE TABLE TestSet (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "testTypeID integer(10) NOT NULL, " +
                    "num_of_tests integer(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID))";
    protected static final String TABLE_CREATE_TestType =
            "CREATE TABLE TestType (" +
                    "testTypeID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "A_x double(10) NOT NULL, " +
                    "A_y double(10) NOT NULL, " +
                    "B_x double(10) NOT NULL, " +
                    "B_y double(10) NOT NULL, " +
                    "C_x double(10) NOT NULL, " +
                    "C_y double(10) NOT NULL, " +
                    "D_x double(10) NOT NULL, " +
                    "D_y double(10) NOT NULL)";
    protected static final String TABLE_CREATE_RecordTest =
            "CREATE TABLE RecordTest (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "_date date NOT NULL, " +
                    "total_time double(10) NOT NULL, " +
                    "velocity_peaks double(10) NOT NULL, " +
                    "max_velocity double(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID, recordTestID))";
    protected static final String TABLE_CREATE_RecordRound =
            "CREATE TABLE RecordRound (" +
                    "participantID integer(10) NOT NULL, " +
                    "testSetID integer(10) NOT NULL, " +
                    "recordTestID integer(10) NOT NULL, " +
                    "recordRoundID integer(10) NOT NULL, " +
                    "round_time double(10) NOT NULL, " +
                    "max_velocity double(10) NOT NULL, " +
                    "PRIMARY KEY (participantID, testSetID, recordTestID, recordRoundID))";
    protected static final String TABLE_CREATE_XYRound =
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
                    "PRIMARY KEY (participantID, testSetID, recordTestID, recordRoundID))";

    protected static final String DATABASE_NAME = "data.db";
    protected static final int DATABASE_VERSION = 1;

    protected final Context mCtx;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS routes");
//            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public AbstractDbAdapter(Context ctx) {
        this.mCtx = ctx;
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


    //-----------extra methods-----------//
    public String convertToString(List<Pair<String,String>> pairList){
        String ans = "";
        String prefix = "";
        for (Pair<String,String> pair : pairList) {
            ans += prefix + pair.first + " = " + pair.second;
            prefix = " AND ";
        }
        return ans;
    }
    //----------------------------------//
}

