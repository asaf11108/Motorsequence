package braude.motorsequence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.oop.Participant;
import database.oop.TestSet;
import util.MyApplication;

public class ParticipantActivity extends AppCompatActivity {

    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        MyApplication app = (MyApplication) getApplicationContext();
        participant = app.getParticipant();

        TextView title = (TextView) findViewById(R.id.text_participant_title);
        title.setText("Hello, " + participant.firstName);

        Button buttPractice = (Button) findViewById(R.id.button_participant_practice);
        buttPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParticipantActivity.this, PracticeActivity.class);
                startActivity(i);
            }
        });
        Button buttTest = (Button) findViewById(R.id.button_participant_test);
        buttTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestSet testSet = participant.testSets.getLast();
                boolean testFlag;
                if (testSet == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please ask from diagnostician Test Set",
                            Toast.LENGTH_SHORT).show();
                    testFlag = false;
                }
                else if(testSet.recordTests.getSeq() >= testSet.testType.num_of_tests) {
                    Toast.makeText(getApplicationContext(),
                            "You finished Test Set",
                            Toast.LENGTH_SHORT).show();
                    testFlag = false;
                }
//                else if(testSet.recordTests.getLast() != null &&
//                        (testSet.recordTests.getLast().date + 1.728e+8) >= System.currentTimeMillis()) {
//                    double wait = (testSet.recordTests.getLast().date + 1.728e+8 - System.currentTimeMillis()) / 8.64e+7;
//                    Toast.makeText(getApplicationContext(),
//                            "Please wait " + new DecimalFormat("#.##").format(wait) + " days for the next test",
//                            Toast.LENGTH_SHORT).show();
//                    testFlag = false;
//                }
                else {
                    testFlag = true;
                }
                if (testFlag) {
                    Intent i = new Intent(ParticipantActivity.this, TestActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
