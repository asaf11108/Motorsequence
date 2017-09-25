package braude.motorsequence;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import util.PointCluster;
import util.TouchView;

/**
 * Created by ASAF on 27/8/2017.
 */
public class PracticeActivity extends ExerciseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textRounds.setVisibility(View.INVISIBLE);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_exercise_drawFrame);
        PointCluster[] pointClusters = new PointCluster[PointCluster.POINT_CLUSTERS_SIZE];
        pointClusters[0] = new PointCluster(getApplicationContext(), relativeLayout, "A", 800, 200);
        pointClusters[1] = new PointCluster(getApplicationContext(), relativeLayout, "B", 450, 160);
        pointClusters[2] = new PointCluster(getApplicationContext(), relativeLayout, "C", 250, 80);
        pointClusters[3] = new PointCluster(getApplicationContext(), relativeLayout, "D", 650, 340);

        TouchView touchView = (TouchView) findViewById(R.id.touch_exercise_touchFrame);
        touchView.setArray(pointClusters);
        pointClusters[0].setClusterColor(PointCluster.COLOR_RED);
    }
}
