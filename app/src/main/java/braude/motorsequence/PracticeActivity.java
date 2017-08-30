package braude.motorsequence;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ASAF on 27/8/2017.
 */
public class PracticeActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textRounds = (TextView) findViewById(R.id.text_exercise_rounds);
        textRounds.setVisibility(View.INVISIBLE);
    }
}
