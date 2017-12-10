package braude.motorsequence;

import android.os.Bundle;
import android.view.View;


/**
 * Created by ASAF on 16/9/2017.
 */

public class TestActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuildTouchScreen(participant.testSets.getLast().testType);

        touchView.initTest(participant.testSets.getLast().createRecordTest(), textRounds, this);

        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                participant.testSets.getLast().deleteRecordTest();
            }
        });

    }

}
