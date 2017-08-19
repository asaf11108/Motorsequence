package database;

/**
 * Created by ASAF on 17/8/2017.
 */

public class TestSetID extends ParticipantID {
    private final int testSetID;

    public TestSetID(ParticipantID participantID, int testSetID) {
        super(participantID.getParticipantID());
        this.testSetID = testSetID;
    }

    public int getTestSetID(){
        return testSetID;
    }
}
