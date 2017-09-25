package braude.motorsequence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.Participant;
import database.TestSet;

public class ParticipantActivity extends AppCompatActivity {

    private Participant mParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        mParticipant = (Participant) getIntent().getSerializableExtra(getString(R.string.key_Participent));

        TextView title = (TextView) findViewById(R.id.text_participant_title);
        title.setText("Hello, " + mParticipant.firstName);

        Button buttPractice = (Button) findViewById(R.id.button_participant_practice);
        buttPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParticipantActivity.this, PracticeActivity.class);
                i.putExtra(getString(R.string.key_Participent), mParticipant);
                startActivity(i);
            }
        });
        Button buttTest = (Button) findViewById(R.id.button_participant_test);
        buttTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestSet testSet = mParticipant.testSets.getLast();
                boolean testFlag;
                if (testSet == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please ask from diagnostician Test Set",
                            Toast.LENGTH_SHORT).show();
                    testFlag = false;
                }
                else if(testSet.recordTests.getSeq() == testSet.testType.num_of_tests) {
                    Toast.makeText(getApplicationContext(),
                            "You finished Test Set",
                            Toast.LENGTH_SHORT).show();
                    testFlag = false;
                }
                else {
                    testFlag = true;
                }
                if (testFlag) {
                    Intent i = new Intent(ParticipantActivity.this, TestActivity.class);
                    i.putExtra(getString(R.string.key_Participent), mParticipant);
                    startActivity(i);
                }
            }
        });
    }
}
