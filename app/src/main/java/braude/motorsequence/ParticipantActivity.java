package braude.motorsequence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import database.Participant;

public class ParticipantActivity extends AppCompatActivity {

    private Participant mParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mParticipant = (Participant) getIntent().getSerializableExtra(getString(R.string.key_Participent));

            TextView title = (TextView) findViewById(R.id.text_participant_title);
            title.setText("Hello, " + mParticipant.firstName);

            Button buttPractice = (Button) findViewById(R.id.button_participant_practice);
            Button buttTest = (Button) findViewById(R.id.button_participant_test);

        }
    }
}
