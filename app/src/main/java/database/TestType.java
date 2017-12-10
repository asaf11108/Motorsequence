package database;

import android.database.Cursor;

import database.tables.TestTypeEntry;
import util.MyPair;

/**
 * Created by ASAF on 5/8/2017.
 */
public class TestType implements Identifier {

    private final int testTypeID;
    public int num_of_tests;
    public int num_of_rounds;
    public int A_x;
    public int A_y;
    public int B_x;
    public int B_y;
    public int C_x;
    public int C_y;
    public int D_x;
    public int D_y;
    public static final int NUM_OF_TESTS = 5;
    public static final int NUM_OF_ROUNDS = 7;

    public TestType(int testTypeID) {
        this.testTypeID = testTypeID;
        TestTypeEntry tte = FactoryEntry.getTestTypeEntry();
        Cursor cursor = tte.fetch(
                null,
                new MyPair[]{new MyPair(tte.PK_AI_TEST_TYPE_ID, testTypeID)});
        num_of_tests = cursor.getInt(cursor.getColumnIndex(tte.NUM_OF_TESTS));
        num_of_rounds = cursor.getInt(cursor.getColumnIndex(tte.NUM_OF_ROUNDS));
        A_x = cursor.getInt(cursor.getColumnIndex(tte.A_X));
        A_y = cursor.getInt(cursor.getColumnIndex(tte.A_Y));
        B_x = cursor.getInt(cursor.getColumnIndex(tte.B_X));
        B_y = cursor.getInt(cursor.getColumnIndex(tte.B_Y));
        C_x = cursor.getInt(cursor.getColumnIndex(tte.C_X));
        C_y = cursor.getInt(cursor.getColumnIndex(tte.C_Y));
        D_x = cursor.getInt(cursor.getColumnIndex(tte.D_X));
        D_y = cursor.getInt(cursor.getColumnIndex(tte.D_Y));
        cursor.close();

    }

    @Override
    public int getID() {
        return testTypeID;
    }
}
