package braude.motorsequence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import database.Participant;
import database.TestSet;
import util.MovementView;
import util.MyApplication;

public class ParticipantAnalysisActivity extends AppCompatActivity {

    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_analysis);

        MyApplication app = (MyApplication) getApplicationContext();
        participant = app.getParticipant();

        final TextView currentTestType = (TextView) findViewById(R.id.text_participantAnalysis_currentSetType);
        TestSet testSet = participant.testSets.getLast();
        boolean testSetFlag;
        if (testSet == null) {
            currentTestType.setText("None");
            testSetFlag = true;
        } else if (testSet.recordTests.getSeq() >= testSet.testType.num_of_tests) {
            currentTestType.setText("Participant finished Test Set");
            testSetFlag = true;
        } else {
            currentTestType.setText("Pattern" + testSet.testType.getID());
            testSetFlag = false;
        }

        Button pattern1 = (Button) findViewById(R.id.button_participantAnalysis_pattern1);
        pattern1.setOnClickListener(new Pattern(1, currentTestType, testSet, testSetFlag));

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_participantAnalysis_container);
        frameLayout.addView(new MovementView(getApplicationContext(),
                participant.testSets.getLast().recordTests.getLast(),
                participant.testSets.getLast().testType));

    }

    private class Pattern implements View.OnClickListener {

        private int mTestTypeID;
        private TextView mCurrentTestType;
        private TestSet mTestSet;
        private boolean mTestSetFlag;

        public Pattern(int mTestTypeID, TextView mCurrentTestType, TestSet mTestSet, boolean mTestSetFlag) {
            this.mTestTypeID = mTestTypeID;
            this.mCurrentTestType = mCurrentTestType;
            this.mTestSet = mTestSet;
            this.mTestSetFlag = mTestSetFlag;
        }

        @Override
        public void onClick(View v) {
            if (mTestSetFlag) {
                mTestSet = participant.createTestSet(mTestTypeID);
                mCurrentTestType.setText("Pattern" + mTestTypeID);
                mTestSetFlag = false;

            } else Toast.makeText(getApplicationContext(), "TestSet exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
