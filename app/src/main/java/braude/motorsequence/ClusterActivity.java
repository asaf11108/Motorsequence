package braude.motorsequence;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clust4j.algo.KMedoids;
import com.clust4j.algo.KMedoidsParameters;
import com.google.common.primitives.Ints;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import database.oop.Participant;

public class ClusterActivity extends AppCompatActivity {

    private NumberPicker numOfCluster;
    private SortedSet<Integer> clusterAtt;
    private SortedSet<Integer> graphAtt;

    private static final int ATTRIBUTES_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        clusterAtt = new TreeSet<>();
        graphAtt = new TreeSet<>();

        Button r1c1 = (Button) findViewById(R.id.button_cluster_r1c1);
        Button r2c1 = (Button) findViewById(R.id.button_cluster_r2c1);
        Button r3c1 = (Button) findViewById(R.id.button_cluster_r3c1);
        Button r1c2 = (Button) findViewById(R.id.button_cluster_r1c2);
        Button r2c2 = (Button) findViewById(R.id.button_cluster_r2c2);
        Button r3c2 = (Button) findViewById(R.id.button_cluster_r3c2);

        r1c1.setOnClickListener(new OnClickCluster(0, r1c2));
        r2c1.setOnClickListener(new OnClickCluster(1, r2c2));
        r3c1.setOnClickListener(new OnClickCluster(2, r3c2));
        r1c2.setOnClickListener(new OnClickGraph(0, r1c1));
        r2c2.setOnClickListener(new OnClickGraph(1, r2c1));
        r3c2.setOnClickListener(new OnClickGraph(2, r3c1));

        final Button analyze = (Button) findViewById(R.id.button_cluster_analyze);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graphAtt.size() < 2 && numOfCluster.getValue() != 1) {
                    Toast.makeText(ClusterActivity.this, "Please choose more graph attributes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!attemptCluster())
                    Toast.makeText(ClusterActivity.this, "Not enough data for clustering", Toast.LENGTH_SHORT).show();
            }
        });

        final LinearLayout attributes = (LinearLayout) findViewById(R.id.linear_cluster_attributes);
        attributes.animate()
                .alpha(0.0f)
                .setDuration(0);

        numOfCluster = (NumberPicker) findViewById(R.id.number_picker);
        numOfCluster.setValueChangedListener(new DefaultValueChangedListener() {
            public void valueChanged(int value, ActionEnum action) {
                if (value != 1) {
                    attributes.setVisibility(View.VISIBLE);
                    attributes.animate()
                            .alpha(1.0f)
                            .setDuration(700);
                } else {
                    attributes.animate()
                            .alpha(0.0f)
                            .setDuration(700);
                }
            }
        });

    }

    private boolean attemptCluster() {
        //fatch data
        List<Participant> participants = Participant.getAllParticipants();
        //init data
        List<Integer> rows = new ArrayList<>();
        for (int x = 0; x < participants.size(); x++) {
            if (participants.get(x).testSets.getLast() != null && participants.get(x).testSets.getLast().recordTests.getLast() != null)
                rows.add(x);
        }
        if (rows.size() < 4)
            return false;

        Intent i = new Intent(ClusterActivity.this, ClusterResultActivity.class);
        if (numOfCluster.getValue() == 1)
            autoClustering(i, participants, rows);
        else {
            manualClustering(i, participants, rows);
        }
        String[] names = new String[rows.size()];
        String[] groups = new String[rows.size()];
        for (int x = 0; x < rows.size(); x++) {
            names[x] = participants.get(rows.get(x)).firstName + ' ' + participants.get(rows.get(x)).lastName;
            groups[x] = participants.get(rows.get(x)).group;
        }
        i.putExtra(getString(R.string.cluster_names), names);
        i.putExtra(getString(R.string.cluster_groups), groups);

        startActivity(i);
        return true;
    }

    private void manualClustering(Intent i, List<Participant> participants, List<Integer> rows) {
        i.putExtra(getString(R.string.cluster_k), numOfCluster.getValue());
        i.putExtra(getString(R.string.cluster_clusterAtt), Ints.toArray(clusterAtt));
        i.putExtra(getString(R.string.cluster_graphAtt), Ints.toArray(graphAtt));
        double[][] data = new double[rows.size()][clusterAtt.size()];
        //set data
        int y = 0;
        if (clusterAtt.contains(0)) {
            for (int x = 0; x < rows.size(); x++)
                data[x][y] = participants.get(rows.get(x)).age;
            y++;
        }
        if (clusterAtt.contains(1)) {
            for (int x = 0; x < rows.size(); x++)
                data[x][y] = participants.get(rows.get(x)).testSets.getLast().recordTests.getLast().totalTime;
            y++;
        }
        if (clusterAtt.contains(2)) {
            for (int x = 0; x < rows.size(); x++)
                data[x][y] = participants.get(rows.get(x)).testSets.getLast().recordTests.getLast().maxVelocity;
            y++;
        }
        Bundle bundle = new Bundle();
        final Array2DRowRealMatrix mat = new Array2DRowRealMatrix(data);
        bundle.putSerializable(getString(R.string.cluster_data), mat);
        i.putExtras(bundle);

        //do clusterring
        KMedoids km = new KMedoidsParameters(numOfCluster.getValue()).fitNewModel(mat);
        final int[] clusters = km.getLabels();
        double silhouetteScore = km.silhouetteScore();
        i.putExtra(getString(R.string.cluster_clusters), clusters);
        i.putExtra(getString(R.string.cluster_silhouetteScore), silhouetteScore);
    }

    private void autoClustering(Intent i, List<Participant> participants, List<Integer> rows) {
        int[] kValues = {2, 3, 4};
        BitSet[] attributes = setAttributes();
        int attMax = 0;
        Array2DRowRealMatrix mat = null;
        KMedoids km = null;
        int[] clusters = null;
        double silhouetteScore = -2;

        //clusterring
        for (int att = 0; att < attributes.length; att++) {
            double[][] data = new double[rows.size()][attributes[att].cardinality()];
            //set data
            int y = 0;
            int attIndex = 0;
            if (attIndex != -1 && attributes[att].nextSetBit(attIndex) == 0) {
                for (int x = 0; x < rows.size(); x++)
                    data[x][y] = participants.get(rows.get(x)).age;
                y++;
                attIndex = attributes[att].nextSetBit(attributes[att].nextSetBit(attIndex) + 1);
            }
            if (attIndex != -1 && attributes[att].nextSetBit(attIndex) == 1) {
                for (int x = 0; x < rows.size(); x++)
                    data[x][y] = participants.get(rows.get(x)).testSets.getLast().recordTests.getLast().totalTime;
                y++;
                attIndex = attributes[att].nextSetBit(attributes[att].nextSetBit(attIndex) + 1);
            }
            if (attIndex != -1 && attributes[att].nextSetBit(attIndex) == 2) {
                for (int x = 0; x < rows.size(); x++)
                    data[x][y] = participants.get(rows.get(x)).testSets.getLast().recordTests.getLast().maxVelocity;
                y++;
                attIndex = attributes[att].nextSetBit(attributes[att].nextSetBit(attIndex) + 1);
            }
            Array2DRowRealMatrix matTemp = new Array2DRowRealMatrix(data);

            for (int k = 0; k < kValues.length; k++) {
                KMedoids kmTemp = new KMedoidsParameters(kValues[k]).fitNewModel(matTemp);
                final int[] resultsTemp = kmTemp.getLabels();
                double scoreTemp = kmTemp.silhouetteScore();
                if (scoreTemp > silhouetteScore) {
                    km = kmTemp;
                    clusters = resultsTemp;
                    silhouetteScore = scoreTemp;
                    attMax = att;
                    mat = matTemp;
                }
            }
        }
        i.putExtra(getString(R.string.cluster_k), km.getK());
        List<Integer> clusterAtt = new ArrayList<>();
        for (int x = attributes[attMax].nextSetBit(0); x >= 0; x = attributes[attMax].nextSetBit(x+1)) {
            clusterAtt.add(x);
            if (x >= ATTRIBUTES_SIZE) {
                break; // or (i+1) would overflow
            }
        }
        i.putExtra(getString(R.string.cluster_clusterAtt), Ints.toArray(clusterAtt));
        i.putExtra(getString(R.string.cluster_graphAtt), Arrays.copyOfRange(Ints.toArray(clusterAtt), 0, ATTRIBUTES_SIZE-1));

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.cluster_data), mat);
        i.putExtras(bundle);
        
        i.putExtra(getString(R.string.cluster_clusters), clusters);
        i.putExtra(getString(R.string.cluster_silhouetteScore), silhouetteScore);
    }

    private BitSet[] setAttributes() {
        BitSet[] attributes = new BitSet[3];

        attributes[0] = new BitSet(ATTRIBUTES_SIZE);
        attributes[1] = new BitSet(ATTRIBUTES_SIZE);
        attributes[2] = new BitSet(ATTRIBUTES_SIZE);

        attributes[0].set(0);
        attributes[0].set(1);

        attributes[1].set(0);
        attributes[1].set(1);
        attributes[1].set(2);

        attributes[2].set(1);
        attributes[2].set(2);

        return attributes;
    }

    private class OnClickCluster implements View.OnClickListener {
        private final Integer i;
        private Button adjButton;
        private final int MAXSIZE = 3;
        private boolean clicked;

        public OnClickCluster(int i, Button adjButton) {
            this.i = i;
            this.adjButton = adjButton;
            clicked = false;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (clicked) {
                button.setBackgroundColor(Color.parseColor("#D8D8D8"));
                clusterAtt.remove(i);
                if (graphAtt.contains(i))
                    adjButton.callOnClick();
            } else {
                if (MAXSIZE == clusterAtt.size())
                    return;
                button.setBackgroundColor(Color.GREEN);
                clusterAtt.add(i);
            }
            clicked = !clicked;
        }
    }

    private class OnClickGraph implements View.OnClickListener {
        private final Integer i;
        private Button adjButton;
        private final int MAXSIZE = 2;
        private boolean clicked;

        public OnClickGraph(int i, Button adjButton) {
            this.i = i;
            this.adjButton = adjButton;
            clicked = false;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (clicked) {
                button.setBackgroundColor(Color.parseColor("#D8D8D8"));
                graphAtt.remove(i);
            } else {
                if (MAXSIZE == graphAtt.size())
                    return;
                button.setBackgroundColor(Color.GREEN);
                graphAtt.add(i);
                if (!clusterAtt.contains(i))
                    adjButton.callOnClick();
            }
            clicked = !clicked;
        }
    }
}