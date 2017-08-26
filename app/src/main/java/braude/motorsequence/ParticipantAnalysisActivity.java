package braude.motorsequence;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class ParticipantAnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_analysis);
//        ViewCompat.setLayoutDirection(findViewById(R.id.acti), ViewCompat.LAYOUT_DIRECTION_RTL);
//        String udata="Underlined Text";
//        SpannableString content = new SpannableString(udata);
//        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
//        mTextView.setText(content);
    }
}
