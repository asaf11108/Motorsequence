package braude.motorsequence;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import database.RecordRound;
import database.RecordTest;

/**
 * Created by ASAF on 16/9/2017.
 */

public class TestActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuildTouchScreen(mParticipant.testSets.getLast().testType);

        touchView.initTest(mParticipant.testSets.getLast().createRecordTest(), mTextRounds, this);

    }

}
