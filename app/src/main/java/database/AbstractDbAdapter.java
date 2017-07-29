package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ASAF on 26/7/2017.
 */
public abstract class AbstractDbAdapter {

    protected static final String TAG = "AbstractDbAdapter";
    protected DatabaseHelper mDbHelper;

    protected static final String TABLE_CREATE_ROUTES =
            "create table routes (_id integer primary key autoincrement, "
                    + "source text not null, destination text not null);";
    protected static final String TABLE_CREATE_TIMETABLES =
            "create table timetables (_id integer primary key autoincrement, "
                    + "route_id integer, depart text not null, arrive text not null, "
                    + "train text not null);";

    protected static final String DATABASE_NAME = "data.db";
    protected static final int DATABASE_VERSION = 1;

    protected final Context mCtx;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_ROUTES);
            db.execSQL(TABLE_CREATE_TIMETABLES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS routes");
            onCreate(db);
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
    public SQLiteOpenHelper open() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mCtx);
        }
        return mDbHelper;
    }

    public void close() {
        mDbHelper.close();
    }

}

