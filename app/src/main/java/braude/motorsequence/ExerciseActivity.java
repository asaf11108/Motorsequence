package braude.motorsequence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import database.Participant;
import database.TestType;
import util.PointCluster;
import util.TouchView;

public abstract class ExerciseActivity extends AppCompatActivity {

    protected Participant mParticipant;
    protected TextView mTextRounds;
    protected RelativeLayout mRelativeLayout;
    protected PointCluster[] mPointClusters;
    protected TouchView touchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        mParticipant = (Participant) getIntent().getSerializableExtra(getString(R.string.key_Participent));

        mTextRounds = (TextView) findViewById(R.id.text_exercise_rounds);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_exercise_drawFrame);
        mPointClusters = new PointCluster[PointCluster.POINT_CLUSTERS_SIZE];
    }

    protected void BuildTouchScreen(TestType testType){
        mPointClusters[0] = new PointCluster(getApplicationContext(), mRelativeLayout, "A", testType.A_x, testType.A_y);
        mPointClusters[1] = new PointCluster(getApplicationContext(), mRelativeLayout, "B", testType.B_x, testType.B_y);
        mPointClusters[2] = new PointCluster(getApplicationContext(), mRelativeLayout, "C", testType.C_x, testType.C_y);
        mPointClusters[3] = new PointCluster(getApplicationContext(), mRelativeLayout, "D", testType.D_x, testType.D_y);

        touchView = (TouchView) findViewById(R.id.touch_exercise_touchFrame);
        touchView.setArray(mPointClusters);
        mPointClusters[0].setClusterColor(PointCluster.COLOR_RED);
    }
}
