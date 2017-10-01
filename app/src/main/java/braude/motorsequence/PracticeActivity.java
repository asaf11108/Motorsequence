package braude.motorsequence;

import android.os.Bundle;
import android.view.View;

import database.TestType;

/**
 * Created by ASAF on 27/8/2017.
 */
public class PracticeActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuildTouchScreen(new TestType(1));

        mTextRounds.setVisibility(View.INVISIBLE);


    }
}
