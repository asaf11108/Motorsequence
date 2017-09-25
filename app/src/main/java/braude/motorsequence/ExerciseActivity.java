package braude.motorsequence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class ExerciseActivity extends AppCompatActivity {

    protected TextView textRounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        textRounds = (TextView) findViewById(R.id.text_exercise_rounds);

    }
}
