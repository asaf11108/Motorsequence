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

//        KMedoids k;

//        /* Load a dataset */
//        double[] values = new double[] { 36 };
//    /*
//     * The simplest incarnation of the DenseInstance constructor will only
//     * take a double array as argument an will create an instance with given
//     * values as attributes and no class value set. For unsupervised machine
//     * learning techniques this is probably the most convenient constructor.
//     */
//        Instance instance = new DenseInstance(values);
//        Dataset data = null;
//        try {
//            data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
///* Create a new instance of the KMeans algorithm, with no options
//  * specified. By default this will generate 4 clusters. */
//        Clusterer km = new KMedoids();
///* Cluster the data, it will be returned as an array of data sets, with
//  * each dataset representing a cluster. */
//        Dataset[] clusters = km.cluster(data);
//
//        Instance i = clusters[0].get(0);
//
//        double[][] dis = new double[1][1];
//        SilhouetteCalculator.calculate(dis, clusters.);

//        new double[] {0,    3.61,  2.24,4.24},
//                new double[] {3.61, 0,   5.1, 1},
//                new double[] {2.24, 5.1,    0, 5.39},
//                new double[] {4.24, 1,    5.39,0}

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
