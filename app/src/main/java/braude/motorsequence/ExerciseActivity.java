package braude.motorsequence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import database.oop.Participant;
import database.oop.TestType;
import util.MyApplication;
import util.board.PointCluster;
import util.board.TouchView;

public abstract class ExerciseActivity extends AppCompatActivity {

    protected Participant participant;
    protected TextView textRounds;
    protected RelativeLayout relativeLayout;
    protected PointCluster[] pointClusters;
    protected TouchView touchView;
    protected Button abort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        MyApplication app = (MyApplication) getApplicationContext();
        participant = app.getParticipant();

        textRounds = (TextView) findViewById(R.id.text_exercise_rounds);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_exercise_drawFrame);
        pointClusters = new PointCluster[PointCluster.POINT_CLUSTERS_SIZE];
        abort = (Button) findViewById(R.id.button_exercise_abort);
    }

    protected void BuildTouchScreen(TestType testType){
        pointClusters[0] = new PointCluster(getApplicationContext(), relativeLayout, "A", testType.A_x, testType.A_y);
        pointClusters[1] = new PointCluster(getApplicationContext(), relativeLayout, "B", testType.B_x, testType.B_y);
        pointClusters[2] = new PointCluster(getApplicationContext(), relativeLayout, "C", testType.C_x, testType.C_y);
        pointClusters[3] = new PointCluster(getApplicationContext(), relativeLayout, "D", testType.D_x, testType.D_y);

        touchView = (TouchView) findViewById(R.id.touch_exercise_touchFrame);
        touchView.setArray(pointClusters);
        pointClusters[0].setClusterColor(PointCluster.COLOR_RED);
    }
}
