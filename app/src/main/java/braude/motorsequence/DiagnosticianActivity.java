package braude.motorsequence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.Participant;

public class DiagnosticianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostician);

        Button search = (Button) findViewById(R.id.button_diagnostician_search);
        Button analysis = (Button) findViewById(R.id.button_diagnostician_analysis);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DiagnosticianActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DiagnosticianActivity.this, ClusterActivity.class);
                startActivity(i);
            }
        });
    }
}
