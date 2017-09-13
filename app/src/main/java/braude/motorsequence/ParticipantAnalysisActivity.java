package braude.motorsequence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import database.Participant;

public class ParticipantAnalysisActivity extends AppCompatActivity {

    private Participant mParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_analysis);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        mParticipant = (Participant) getIntent().getSerializableExtra(getString(R.string.key_Participent));
//        mParticipant.
//        ViewCompat.setLayoutDirection(findViewById(R.id.acti), ViewCompat.LAYOUT_DIRECTION_RTL);
//        String udata="Underlined Text";
//        SpannableString content = new SpannableString(udata);
//        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
//        mTextView.setText(content);
    }
}
