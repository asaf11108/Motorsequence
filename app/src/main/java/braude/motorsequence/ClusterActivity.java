package braude.motorsequence;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.primitives.Ints;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import database.oop.Participant;
import database.oop.RecordTest;
import database.tables.FactoryEntry;
import database.tables.ParticipantEntry;
import database.tables.RecordTestEntry;

public class ClusterActivity extends AppCompatActivity {

    private NumberPicker numOfCluster;
    private SortedSet<Integer> clusterAtt;
    private SortedSet<Integer> graphAtt;

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
                if (graphAtt.size() < 2) {
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
                if (value != 0) {
                    attributes.setVisibility(View.VISIBLE);
                    attributes.animate()
                            .alpha(1.0f)
                            .setDuration(700);
                } else if (value == 0) {
                    attributes.animate()
                            .alpha(0.0f)
                            .setDuration(700);
                }
            }
        });

    }

    private boolean attemptCluster() {
        boolean goodInput = true;
        Intent i = new Intent(ClusterActivity.this, ClusterResultActivity.class);
        i.putExtra(getString(R.string.cluster_k), numOfCluster.getValue());
        i.putExtra(getString(R.string.cluster_clusterAtt), Ints.toArray(clusterAtt));
        i.putExtra(getString(R.string.cluster_graphAtt), Ints.toArray(graphAtt));

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
        bundle.putSerializable("braude.motorsequence.DATA", mat);
        i.putExtras(bundle);
        String[] names = new String[rows.size()];
        for (int x = 0; x < rows.size(); x++)
            names[x] = participants.get(rows.get(x)).firstName +' '+ participants.get(rows.get(x)).lastName;
        i.putExtra("braude.motorsequence.NAMES", names);
/*
        Bundle bundle = new Bundle();
        final Array2DRowRealMatrix mat = new Array2DRowRealMatrix(new double[][]{
                new double[]{1, 2},
                new double[]{3, 5},
                new double[]{2, 0},
                new double[]{4, 5}
        });
        bundle.putSerializable("braude.motorsequence.DATA", mat);
        button.putExtras(bundle);

        //TODO: fatch IDs
        String[] ids = new String[]{"0", "1", "2", "3"};
        button.putExtra("braude.motorsequence.ID", ids);
*/
        startActivity(i);

        return true;
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
            if (clicked){
                button.setBackgroundColor(Color.parseColor("#D8D8D8"));
                clusterAtt.remove(i);
                if (graphAtt.contains(i))
                    adjButton.callOnClick();
            }else {
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
            if (clicked){
                button.setBackgroundColor(Color.parseColor("#D8D8D8"));
                graphAtt.remove(i);
            }else {
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