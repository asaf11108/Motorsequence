package database.tables;

import android.content.Context;

/**
 * Created by ASAF on 17/12/2017.
 */

public class SQLiteSequenceEntry extends AbstractDbAdapter {

    public static final String NAME = "name";
    public static final String SEQ = "seq";

    private static SQLiteSequenceEntry mSQLiteSequenceEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private SQLiteSequenceEntry(Context ctx) {
        super(ctx, "sqlite_sequence");
    }

    static SQLiteSequenceEntry getInstance(Context context){
        if (mSQLiteSequenceEntry == null) mSQLiteSequenceEntry = new SQLiteSequenceEntry(context);
        return mSQLiteSequenceEntry;
    }
}
