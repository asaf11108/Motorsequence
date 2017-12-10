package braude.motorsequence;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import database.oop.Participant;
import database.oop.RecordTest;
import database.oop.TestSet;
import util.graph.ButtonGraph;
import util.graph.DayButtonGraph;
import util.MyApplication;
import util.graph.MyGraphView;
import util.graph.TypeButtonGraph;
import util.board.DrawView;

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

        if (participant.testSets.getLast() != null) {
            defineGraph();
            defineTable();
        }

    }

    private void definePatten(){
        final TextView currentTestType = (TextView) findViewById(R.id.text_participantAnalysis_currentSetType);
        final LinearLayout patterns = (LinearLayout) findViewById(R.id.linear_participantAnalysis_patterns);
        TestSet testSet = participant.testSets.getLast();
        if (testSet == null)
            currentTestType.setVisibility(View.GONE);
        else if (testSet.recordTests.getSeq() >= testSet.testType.num_of_tests)
            currentTestType.setVisibility(View.GONE);
        else {
            currentTestType.setText("Pattern" + testSet.testType.getID());
            patterns.setVisibility(View.GONE);
        }

        Button pattern1 = (Button) findViewById(R.id.button_participantAnalysis_pattern1);
        pattern1.setOnClickListener(new Pattern(1, currentTestType, patterns));
    }

    private void defineGraph(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_participantAnalysis_container);
        int finishedTests = participant.testSets.getLast().recordTests.getSeq();
        final Button[] dayButtons = new Button[finishedTests];
        ButtonGraph dayButtonGraph = new DayButtonGraph(dayButtons);
        for (int i = 0; i < finishedTests; i++) {
            int resId = getResources().getIdentifier("button_participantAnalysis_day" + (i+1), "id", getPackageName());
            dayButtons[i] = (Button) findViewById(resId);
            dayButtons[i].setOnClickListener(new GraphListener(i, dayButtonGraph, frameLayout));
        }
        for (int i = finishedTests; i < TEST_DAYS; i++) {
            int resId = getResources().getIdentifier("button_participantAnalysis_day" + (i+1), "id", getPackageName());
            Button butt = (Button) findViewById(resId);
            butt.setEnabled(false);
        }

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

    private void defineTable(){
        int seq = participant.testSets.getLast().recordTests.getSeq();
        double totalTimeAvg = 0.0, maxVelocityAvg = 0.0;
        int velocityPeaksAvg = 0;
        int currColumns = 3;
        TableLayout summeryTable = (TableLayout) findViewById(R.id.table_participantAnalysis_summery);
        if (seq != 0) {
            for (int i = 0; i < seq; i++) {
                RecordTest recordTest = participant.testSets.getLast().recordTests.get(i + 1);
                double totalTimeDay = Math.round(recordTest.totalTime * 100.0) / 100.0;
                double maxVelocityDay = Math.round(recordTest.maxVelocity * 100.0) / 100.0;
                int velocityPeaksDay = recordTest.velocityPeaks;
                getCellAtPos(summeryTable, i+1, 1).setText(String.valueOf(totalTimeDay));
                getCellAtPos(summeryTable, i+1, 2).setText(String.valueOf(maxVelocityDay));
                getCellAtPos(summeryTable, i+1, 3).setText(String.valueOf(velocityPeaksDay));

                totalTimeAvg += totalTimeDay;
                maxVelocityAvg += maxVelocityDay;
                velocityPeaksAvg += velocityPeaksDay;
            }
            totalTimeAvg = Math.round(totalTimeAvg / seq * 100.0) / 100.0;
            maxVelocityAvg = Math.round(maxVelocityAvg / seq * 100.0) / 100.0;
            velocityPeaksAvg = Math.round(velocityPeaksAvg / seq * 100) / 100;

            getCellAtPos(summeryTable, TEST_DAYS+1, 1).setText(String.valueOf(totalTimeAvg));
            getCellAtPos(summeryTable, TEST_DAYS+1, 2).setText(String.valueOf(maxVelocityAvg));
            getCellAtPos(summeryTable, TEST_DAYS+1, 3).setText(String.valueOf(velocityPeaksAvg));
        }

    }

    private class Pattern implements View.OnClickListener {

        private int mTestTypeID;
        private TextView mCurrentTestType;
        private LinearLayout patterns;

        public Pattern(int mTestTypeID, TextView mCurrentTestType, LinearLayout patterns) {
            this.mTestTypeID = mTestTypeID;
            this.mCurrentTestType = mCurrentTestType;
            this.patterns = patterns;
        }

        @Override
        public void onClick(View v) {
            participant.createTestSet(mTestTypeID);
            mCurrentTestType.setText("Pattern" + mTestTypeID);
            mCurrentTestType.setVisibility(View.VISIBLE);
            patterns.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "TestSet created", Toast.LENGTH_SHORT).show();
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
                    if (participant.testSets.getLast().recordTests.get(buttonGraph.day+1) != null)
                        createGraph("mm / s",
                            participant.testSets.getLast().recordTests.get(buttonGraph.day+1).recordRounds.getLast().v);
                    break;
                case  2:
                    if (participant.testSets.getLast().recordTests.get(buttonGraph.day+1) != null)
                        createGraph("mm / s^2",
                            participant.testSets.getLast().recordTests.get(buttonGraph.day+1).recordRounds.getLast().jerk);
                    break;
            }
        }

        private void createGraph(String verticalAxisName, List<Double> ySeries){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            GraphView graphView = new MyGraphView(getApplicationContext(), verticalAxisName);
            frameLayout.addView(graphView, layoutParams);
            LineGraphSeries<DataPoint> series = new LineGraphSeries();
            series.setColor(Color.RED);
            List<Double> s = participant.testSets.getLast().recordTests.get(buttonGraph.day+1).recordRounds.getLast().s;
            for (int i = 0; i <ySeries.size(); i++)
                series.appendData(new DataPoint(s.get(i)-s.get(0), ySeries.get(i)), true, ySeries.size());
            graphView.addSeries(series);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(myCeil(s.get(s.size()-1) - s.get(0)));
            graphView.getViewport().setXAxisBoundsManual(true);
        }
    }

    private TextView getCellAtPos(TableLayout table, int x, int y) {
        TableRow row = (TableRow) table.getChildAt(x);
        return (TextView) row.getChildAt(y);
    }

    private double myCeil(double sec){
        int sInt = (int) sec;
        if (sec-sInt >= 0.5)
            return sInt + 1;
        else
            return sInt + 0.5;
    }
}
