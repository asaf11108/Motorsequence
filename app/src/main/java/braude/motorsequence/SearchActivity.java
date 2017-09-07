package braude.motorsequence;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import database.Participant;

public class SearchActivity extends AppCompatActivity {

    private TableRow mRow2;
    private TableRow mRow3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRow2 = (TableRow) findViewById(R.id.row_search_row2);
        mRow3 = (TableRow) findViewById(R.id.row_search_row3);

        mRow2.setVisibility(View.GONE);
        mRow3.setVisibility(View.GONE);

        final TextView advanceSearch = (TextView) findViewById(R.id.text_search_advanceSearch);
        advanceSearch.setText(R.string.show_advance_search);
        advanceSearch.setOnClickListener(new View.OnClickListener() {
            private boolean hide = true;

            @Override
            public void onClick(View v) {
                advanceSearch.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.LinkHover));
                if (hide) {
                    mRow2.setVisibility(View.VISIBLE);
                    mRow3.setVisibility(View.VISIBLE);
                    advanceSearch.setText(R.string.hide_advance_search);
                } else {
                    mRow2.setVisibility(View.GONE);
                    mRow3.setVisibility(View.GONE);
                    advanceSearch.setText(R.string.show_advance_search);
                }
                hide = !hide;
                advanceSearch.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Link));
            }
        });
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO: need to build search activity
//                Intent i = new Intent(DiagnosticianActivity.this, ParticipantAnalysisActivity.class);
//                Participant participant = new Participant(1);
//                i.putExtra(getString(R.string.key_Participent), participant);
//                startActivity(i);
//            }
//        });
    }
}
