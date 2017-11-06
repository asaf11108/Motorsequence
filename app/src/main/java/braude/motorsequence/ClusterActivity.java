package braude.motorsequence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.io.IOException;

public class ClusterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

//        KMedoids k;
//
//        /* Load a dataset */
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
    }
}
