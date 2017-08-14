package database;

import android.content.ContentValues;
import android.content.Context;


/**
 * Created by ASAF on 5/8/2017.
 */
public class TestTypeEntry extends AbstractDbAdapter{

    public static final String PK_AI_TEST_TYPE_ID = "testTypeID";
    public static final String NUM_OF_TESTS = "num_of_tests";
    public static final String A_X = "A_x";
    public static final String A_Y = "A_y";
    public static final String B_X = "B_x";
    public static final String B_Y = "B_y";
    public static final String C_X = "C_x";
    public static final String C_Y = "C_y";
    public static final String D_X = "D_x";
    public static final String D_Y = "D_y";

    private static TestTypeEntry mTestTypeEntry;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    private TestTypeEntry(Context ctx) {
        super(ctx, "TestType");
    }

    static TestTypeEntry getInstance(Context context){
        if (mTestTypeEntry == null) mTestTypeEntry = new TestTypeEntry(context);
        return mTestTypeEntry;
    }

    /**
     * create test type to insert
     *
     * @return rowId or -1 if failed
     */
    public long create(int num_of_tests,
                       double A_x, double A_y,
                       double B_x, double B_y,
                       double C_x, double C_y,
                       double D_x, double D_y){
        ContentValues values = new ContentValues();
        values.put(NUM_OF_TESTS, num_of_tests);
        values.put(A_X, A_x);
        values.put(A_Y, A_y);
        values.put(B_X, B_x);
        values.put(B_Y, B_y);
        values.put(C_X, C_x);
        values.put(C_Y, C_y);
        values.put(D_X, D_x);
        values.put(D_Y, D_y);
        long tmp = insert(values);
        return tmp;
    }
}