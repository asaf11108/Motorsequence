package database;

import android.database.Cursor;

import java.util.List;

import util.MyPair;

/**
 * Created by ASAF on 16/8/2017.
 */
class RecordRound implements Identifier {

    private final RecordTest recordTest;
    private int recordRoundID;

    public double roundTime;
    public double maxVelocity;

    public List<Double> x;
    public List<Double> y;
    public List<Double> s;
    public List<Double> v;
    public List<Double> jerk;

    public RecordRound(RecordTest recordTest, int recordRoundID) {
        this.recordTest = recordTest;
        this.recordRoundID = recordRoundID;
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
                new MyPair[]{new MyPair(xyre.PK_PARTICIPANT_ID, recordTest.getParent().getParent().getID()),
                        new MyPair(xyre.PK_TEST_SET_ID, recordTest.getParent().getID()),
                        new MyPair(xyre.PK_RECORD_TEST_ID, recordTest.getID()),
                        new MyPair(xyre.PK_RECORD_ROUND_ID, recordRoundID)});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            x.add(cursor.getDouble(cursor.getColumnIndex(xyre.X)));
            y.add(cursor.getDouble(cursor.getColumnIndex(xyre.Y)));
            s.add(cursor.getDouble(cursor.getColumnIndex(xyre.S)));
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
}
