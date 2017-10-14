package braude.motorsequence;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import database.Participant;
import database.TestSet;
import util.ButtonGraph;
import util.DayButtonGraph;
import util.DrawView;
import util.MyApplication;
import util.TypeButtonGraph;
import java.util.List;

public class ParticipantAnalysisActivity extends AppCompatActivity {

    private Participant participant;
    private static final int TEST_DAYS = 5;
    private static final int GRAPH_TYPES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_analysis);

        MyApplication app = (MyApplication) getApplicationContext();
        participant = app.getParticipant();

        definePatten();

        if (participant.testSets.getLast() != null) defineGraph();

    }

    private void definePatten(){
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
        pattern1.setOnClickListener(new Pattern(1, currentTestType, testSetFlag));
    }

    private void defineGraph(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_participantAnalysis_container);
        final Button[] dayButtons = new Button[TEST_DAYS];
        dayButtons[0] = (Button) findViewById(R.id.button_participantAnalysis_day1);
        dayButtons[1] = (Button) findViewById(R.id.button_participantAnalysis_day2);
        dayButtons[2] = (Button) findViewById(R.id.button_participantAnalysis_day3);
        dayButtons[3] = (Button) findViewById(R.id.button_participantAnalysis_day4);
        dayButtons[4] = (Button) findViewById(R.id.button_participantAnalysis_day5);
        ButtonGraph dayButtonGraph = new DayButtonGraph(dayButtons);
        dayButtons[0].setOnClickListener(new GraphListener(0, dayButtonGraph, frameLayout));
        dayButtons[1].setOnClickListener(new GraphListener(1, dayButtonGraph, frameLayout));
        dayButtons[2].setOnClickListener(new GraphListener(2, dayButtonGraph, frameLayout));
        dayButtons[3].setOnClickListener(new GraphListener(3, dayButtonGraph, frameLayout));
        dayButtons[4].setOnClickListener(new GraphListener(4, dayButtonGraph, frameLayout));

        Button[] graphTypes = new Button[GRAPH_TYPES];
        graphTypes[0] = (Button) findViewById(R.id.button_participantAnalysis_movement);
        graphTypes[1] = (Button) findViewById(R.id.button_participantAnalysis_velocity);
        graphTypes[2] = (Button) findViewById(R.id.button_participantAnalysis_jerk);
        ButtonGraph typeButtonGraph = new TypeButtonGraph(graphTypes);
        graphTypes[0].setOnClickListener(new GraphListener(0, typeButtonGraph, frameLayout));
        graphTypes[1].setOnClickListener(new GraphListener(1, typeButtonGraph, frameLayout));
        graphTypes[2].setOnClickListener(new GraphListener(2, typeButtonGraph, frameLayout));

        dayButtons[0].setBackgroundColor(Color.GREEN);
        graphTypes[0].setBackgroundColor(Color.GREEN);
        if (participant.testSets.getLast().recordTests.get(dayButtonGraph.getVal()+1) != null)
            frameLayout.addView(new DrawView(getApplicationContext(),
                    participant.testSets.getLast().recordTests.get(dayButtonGraph.getVal()+1),
                    participant.testSets.getLast().testType));
    }

    private class Pattern implements View.OnClickListener {

        private int mTestTypeID;
        private TextView mCurrentTestType;
        private boolean mTestSetFlag;

        public Pattern(int mTestTypeID, TextView mCurrentTestType, boolean mTestSetFlag) {
            this.mTestTypeID = mTestTypeID;
            this.mCurrentTestType = mCurrentTestType;
            this.mTestSetFlag = mTestSetFlag;
        }

        @Override
        public void onClick(View v) {
            if (mTestSetFlag) {
                participant.createTestSet(mTestTypeID);
                mCurrentTestType.setText("Pattern" + mTestTypeID);
                mTestSetFlag = false;

            } else Toast.makeText(getApplicationContext(), "TestSet exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class GraphListener implements View.OnClickListener {
        private int buttID;
        private ButtonGraph buttonGraph;
        private FrameLayout frameLayout;


        public GraphListener(int buttID, ButtonGraph buttonGraph, FrameLayout frameLayout) {
            this.buttID = buttID;
            this.buttonGraph = buttonGraph;
            this.frameLayout = frameLayout;
        }

        @Override
        public void onClick(View v) {
            if (v == buttonGraph.getButton()) return;
            Button clickedButt = (Button) v;
            clickedButt.setBackgroundColor(Color.GREEN);
            buttonGraph.getButton().setBackgroundResource(android.R.drawable.btn_default);
            buttonGraph.setVal(buttID);
            frameLayout.removeAllViews();
            switch (buttonGraph.type){
                case  0:
                    if (participant.testSets.getLast().recordTests.get(buttonGraph.day+1) != null)
                        frameLayout.addView(new DrawView(getApplicationContext(),
                                participant.testSets.getLast().recordTests.get(buttonGraph.day+1),
                                participant.testSets.getLast().testType));
                    break;
                case  1:
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    GraphView graphView = new GraphView(getApplicationContext());
                    frameLayout.addView(graphView, layoutParams);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries();
                    List<Double> velocity = participant.testSets.getLast().recordTests.get(buttonGraph.day+1).recordRounds.getLast().v;
                    List<Double> s = participant.testSets.getLast().recordTests.get(buttonGraph.day+1).recordRounds.getLast().s;
                    for (int i = 0; i <velocity.size(); i++)
                        series.appendData(new DataPoint(s.get(i)-s.get(0), velocity.get(i)), true, velocity.size());
                    graphView.addSeries(series);
                    break;
                case  2:
                    frameLayout.addView(new GraphView(getApplicationContext()));
                    break;
            }
        }
    }
}
