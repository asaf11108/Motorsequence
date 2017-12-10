package braude.motorsequence;

import android.os.Bundle;
import android.view.View;

import database.oop.TestType;

/**
 * Created by ASAF on 27/8/2017.
 */
public class PracticeActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuildTouchScreen(new TestType(1));

        textRounds.setVisibility(View.INVISIBLE);

        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
