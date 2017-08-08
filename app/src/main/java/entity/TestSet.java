package entity;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import database.FactoryEntry;
import database.TestSetEntry;

/**
 * Created by ASAF on 5/8/2017.
 */

public class TestSet {

    public final int testSetID;
    public TestType testType;
    public int recordTestSeq;
    public List<RecordTest> recordTest;

    public TestSet(int participantID, int testSetID) {
        this.testSetID = testSetID;
        TestSetEntry tse = FactoryEntry.getTestSetEntry();
        tse.fetch()
        this.testType = testType;
        this.recordTestSeq = recordTestSeq;
        this.recordTest = new ArrayList<>();
    }
}
