package database;

/**
 * Created by ASAF on 17/8/2017.
 */

public class RecordTestID extends TestSet {
    private final int recordTestID;

    public RecordTestID(TestSetID testSetID, int recordTestID) {
        super(testSetID.getParticipantID(), testSetID.getTestSetID());
        this.recordTestID = recordTestID;
    }


}
