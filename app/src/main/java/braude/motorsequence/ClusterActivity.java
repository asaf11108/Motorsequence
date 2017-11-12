package braude.motorsequence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clust4j.algo.KMedoids;
import com.clust4j.algo.KMedoidsParameters;
import com.clust4j.metrics.scoring.UnsupervisedMetric;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class ClusterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        final int k = 2;
        final Array2DRowRealMatrix mat = new Array2DRowRealMatrix(new double[][] {
                new double[] {1, 2},
                new double[] {3, 5},
                new double[] {2, 0},
                new double[] {4, 5}
        });
//
        KMedoids km = new KMedoidsParameters(k).fitNewModel(mat);
        final int[] results = km.getLabels();
        double r = km.silhouetteScore();
        int d = 0;
    }
}
