package braude.motorsequence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.List;

public class ClusterActivity extends AppCompatActivity {

    private EditText numOfCluster;
    private EditText ageFrom;
    private EditText ageTo;
    private EditText totalTime;
    private EditText maxVelocity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster);

        numOfCluster = (EditText) findViewById(R.id.edit_cluster_numOfCluster);
        ageFrom = (EditText) findViewById(R.id.edit_cluster_ageFrom);
        ageTo = (EditText) findViewById(R.id.edit_cluster_ageTo);
        totalTime = (EditText) findViewById(R.id.edit_cluster_totalTime);
        maxVelocity = (EditText) findViewById(R.id.edit_cluster_maxVelocity);

        final Button analyze = (Button) findViewById(R.id.button_cluster_analyze);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!attemptCluster())
                    Toast.makeText(ClusterActivity.this, "Input error", Toast.LENGTH_SHORT).show();
            }
        });


    };


    private boolean attemptCluster() {
        boolean inputError = false;
        Intent i = new Intent(ClusterActivity.this, ClusterResultActivity.class);

        if (numOfCluster.getText().length() != 0 && checkInteger(numOfCluster.getText()))
            i.putExtra("braude.motorsequence.K", Integer.parseInt(numOfCluster.getText().toString()));
        else
            return false;

        List<Double[]> data = new ArrayList<>();

        if (ageFrom.getText().length() != 0 && checkInteger(ageFrom.getText())
                && ageTo.getText().length() != 0 && checkInteger(ageTo.getText())) {
            int ageF = Integer.parseInt(ageFrom.getText().toString());
            int ageT = Integer.parseInt(ageTo.getText().toString());
            if (ageT >= ageF) {
//                i.putExtra("AGE_FROM", ageF);
//                i.putExtra("AGE_TO", ageT);
            }
        }
        if (ageFrom.getText().length() != 0 && checkDouble(totalTime.getText()))
//            i.putExtra("TOTAL_TIME", Double.parseDouble(totalTime.getText().toString()));
            ;
        if (maxVelocity.getText().length() != 0 && checkDouble((maxVelocity.getText())))
//            i.putExtra("MAX_VELOCITY", Double.parseDouble(maxVelocity.getText().toString()));
            ;

        Bundle bundle = new Bundle();
        final Array2DRowRealMatrix mat = new Array2DRowRealMatrix(new double[][]{
                new double[]{1, 2},
                new double[]{3, 5},
                new double[]{2, 0},
                new double[]{4, 5}
        });
        bundle.putSerializable("braude.motorsequence.DATA", mat);
        i.putExtras(bundle);

        //TODO: fatch IDs
        String[] ids = new String[]{"0", "1", "2", "3"};
        i.putExtra("braude.motorsequence.ID", ids);

        startActivity(i);

        return true;
    }

    private boolean checkDouble(Editable number){
        try
        {
            Double.parseDouble(number.toString());
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    private boolean checkInteger(Editable number){
        try
        {
            Integer.parseInt(number.toString());
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    private double[][] buildDissimilarityMatrix() {
//        final Array2DRowRealMatrix mat;
//
//        mat = new Array2DRowRealMatrix(new double[][]{
//                new double[]{1, 2},
//                new double[]{3, 5},
//                new double[]{2, 0},
//                new double[]{4, 5}
//        });
//        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
//        pe.fetchAll();
//
//        return mat;
        return null;
    }
}