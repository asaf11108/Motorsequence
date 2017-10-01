package braude.motorsequence;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.FactoryEntry;
import database.Participant;
import database.ParticipantEntry;
import util.MyApplication;
import util.MyPair;
import util.MyTableRow;

public class SearchActivity extends AppCompatActivity {

    private boolean hide = true;

    private TableRow mRow2;
    private TableRow mRow3;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mUserName;
    private EditText mAge;
    private EditText mEmail;
    private Spinner mGroup;

    private TableLayout mTableSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRow2 = (TableRow) findViewById(R.id.row_search_row2);
        mRow3 = (TableRow) findViewById(R.id.row_search_row3);

        mRow2.setVisibility(View.GONE);
        mRow3.setVisibility(View.GONE);

        mFirstName = (EditText) findViewById(R.id.edit_search_firstName);
        mLastName = (EditText) findViewById(R.id.edit_search_lastName);
        mUserName = (EditText) findViewById(R.id.edit_search_userName);
        mAge = (EditText) findViewById(R.id.edit_search_age);
        mEmail = (EditText) findViewById(R.id.edit_search_email);
        mGroup = (Spinner) findViewById(R.id.edit_search_group);

        mTableSearch = (TableLayout) findViewById(R.id.table_search_searchResult);

        final TextView advanceSearch = (TextView) findViewById(R.id.text_search_advanceSearch);
        advanceSearch.setText(R.string.show_advance_search);
        advanceSearch.setOnClickListener(new View.OnClickListener() {
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

        Button search = (Button) findViewById(R.id.button_search_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSearch();
            }
        });

    }

    private void attemptSearch() {
        cleanTable(mTableSearch);

        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        List<MyPair> pairs = new ArrayList<>();
        composePairList(pairs, pe.FIRST_NAME, mFirstName);
        composePairList(pairs, pe.LAST_NAME, mLastName);
        if (!hide) {
            composePairList(pairs, pe.USER_NAME, mUserName);
            composePairList(pairs, pe.AGE, mAge);
            composePairList(pairs, pe.EMAIL, mEmail);
            if (mGroup.getSelectedItemPosition() != 0)
                pairs.add(new MyPair(pe.GROUP, mGroup.getSelectedItem().toString()));
        }
        Cursor cursor = pe.fetch(null, pairs.toArray(new MyPair[pairs.size()]));
        List<TableRow> rows = new ArrayList<>();
        String[] columns = new String[]
                {pe.FIRST_NAME, pe.LAST_NAME, pe.USER_NAME, pe.AGE, pe.EMAIL, pe.GROUP};
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            /* Create a new row to be added. */
            final MyTableRow row = new MyTableRow(this, cursor.getInt(cursor.getColumnIndex(pe.PK_AI_PARTICIPANT_ID)));
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            /* Create a TextViews to be the row-content. */
            for (String column : columns) {
                ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.tableTextView);
                TextView field = new TextView(newContext);
                field.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                field.setText(cursor.getString(cursor.getColumnIndex(column)));
                field.setBackgroundResource(R.drawable.border);
                /* Add TextView to row. */
                row.addView(field);
            }
            /* Add row to TableLayout. */
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SearchActivity.this, ParticipantAnalysisActivity.class);
                    Participant participant = new Participant(row.getMyId());
                    MyApplication app = (MyApplication) getApplicationContext();
                    app.setParticipant(participant);
                    startActivity(i);
                }
            });
            rows.add(row);
            mTableSearch.addView(row);

        }
        cursor.close();
    }

    private void composePairList(List<MyPair> pairs, String lvalue, EditText rvalue) {
        if (!TextUtils.isEmpty(rvalue.getText())) {
            pairs.add(new MyPair(lvalue, rvalue.getText().toString()));
            rvalue.getText().clear();
        }
    }

    private void cleanTable(TableLayout table) {
        int childCount = table.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
