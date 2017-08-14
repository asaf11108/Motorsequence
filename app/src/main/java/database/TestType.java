package database;

import android.database.Cursor;

import util.MyPair;

/**
 * Created by ASAF on 5/8/2017.
 */
public class TestType implements Identifier {

    private final int testTypeID;
    public int num_of_tests;
    public double A_x;
    public double A_y;
    public double B_x;
    public double B_y;
    public double C_x;
    public double C_y;
    public double D_x;
    public double D_y;

    public TestType(int testTypeID) {
        this.testTypeID = testTypeID;
        TestTypeEntry tte = FactoryEntry.getTestTypeEntry();
        Cursor cursor = tte.fetch(
                null,
                new MyPair[]{new MyPair(tte.PK_AI_TEST_TYPE_ID, testTypeID)});
        num_of_tests = cursor.getInt(cursor.getColumnIndex(tte.NUM_OF_TESTS));
        A_x = cursor.getDouble(cursor.getColumnIndex(tte.A_X));
        A_y = cursor.getDouble(cursor.getColumnIndex(tte.A_Y));
        B_x = cursor.getDouble(cursor.getColumnIndex(tte.B_X));
        B_y = cursor.getDouble(cursor.getColumnIndex(tte.B_Y));
        C_x = cursor.getDouble(cursor.getColumnIndex(tte.C_X));
        C_y = cursor.getDouble(cursor.getColumnIndex(tte.C_Y));
        D_x = cursor.getDouble(cursor.getColumnIndex(tte.D_X));
        D_y = cursor.getDouble(cursor.getColumnIndex(tte.D_Y));
        cursor.close();

    }

    @Override
    public int getID() {
        return testTypeID;
    }
}
