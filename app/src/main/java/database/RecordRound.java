package database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import util.MyPair;

/**
 * Created by ASAF on 16/8/2017.
 */
public class RecordRound implements Identifier {

    private final RecordTest recordTest;
    private int recordRoundID;

    public double roundTime;
    public double maxVelocity;

    public List<Float> x;
    public List<Float> y;
    public List<Long> s;
    public List<Double> v;
    public List<Double> jerk;

    public RecordRound(RecordTest recordTest, int recordRoundID) {
        this.recordTest = recordTest;
        this.recordRoundID = recordRoundID;
        x = new ArrayList<>();
        y = new ArrayList<>();
        s = new ArrayList<>();
        v = new ArrayList<>();
        jerk = new ArrayList<>();
        RecordRoundEntry rre = FactoryEntry.getRecordRoundEntry();
        Cursor cursor = rre.fetch(
                null,
                new MyPair[]{new MyPair(rre.PK_PARTICIPANT_ID, recordTest.getParent().getParent().getID()),
                        new MyPair(rre.PK_TEST_SET_ID, recordTest.getParent().getID()),
                        new MyPair(rre.PK_RECORD_TEST_ID, recordTest.getID()),
                        new MyPair(rre.PK_RECORD_ROUND_ID, recordRoundID)});
        roundTime = cursor.getDouble(cursor.getColumnIndex(rre.ROUND_TIME));
        maxVelocity = cursor.getDouble(cursor.getColumnIndex(rre.MAX_VELOCITY));
        cursor.close();
        XYRoundEntry xyre = FactoryEntry.getXYRoundEntry();
        cursor = xyre.fetch(
                null,
                new MyPair[]{new MyPair(xyre.PARTICIPANT_ID, recordTest.getParent().getParent().getID()),
                        new MyPair(xyre.TEST_SET_ID, recordTest.getParent().getID()),
                        new MyPair(xyre.RECORD_TEST_ID, recordTest.getID()),
                        new MyPair(xyre.RECORD_ROUND_ID, recordRoundID)});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            x.add(cursor.getFloat(cursor.getColumnIndex(xyre.X)));
            y.add(cursor.getFloat(cursor.getColumnIndex(xyre.Y)));
            s.add(cursor.getLong(cursor.getColumnIndex(xyre.S)));
            v.add(cursor.getDouble(cursor.getColumnIndex(xyre.V)));
            jerk.add(cursor.getDouble(cursor.getColumnIndex(xyre.JERK)));
        }
        cursor.close();
    }

    public RecordTest getParent() {
        return recordTest;
    }

    @Override
    public int getID() {
        return recordRoundID;
    }

    public void saveXYRound() {
        XYRoundEntry xyre = FactoryEntry.getXYRoundEntry();
        SQLiteDatabase db = xyre.getDB();
        try {
            db.beginTransaction();
            String sql = "INSERT INTO " + xyre.getTableName() +
                    " VALUES (" + recordTest.getParent().getParent().getID() +
                    ", " + recordTest.getParent().getID() +
                    ", " + recordTest.getID() +
                    ", " + recordRoundID + ", ?, ?, ?, ?, ?) ";
            SQLiteStatement statement = db.compileStatement(sql);
            for (int i = 0; i < x.size(); i++)
                xyre.create(statement, x.get(i), y.get(i), s.get(i), v.get(i), jerk.get(i));

            db.setTransactionSuccessful(); // This commits the transaction if there were no exceptions

        } catch (Exception e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }
}
