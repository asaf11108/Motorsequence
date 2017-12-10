package braude.motorsequence;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import database.FactoryEntry;
import database.tables.ParticipantEntry;
import util.MyPair;

public class RegisterActivity extends AppCompatActivity {

    private TextView mUserName;
    private TextView mPassword;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mAge;
    private TextView mEmail;
    private Spinner mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = (TextView) findViewById(R.id.edit_register_userName);
        mPassword = (TextView) findViewById(R.id.edit_register_password);
        mFirstName = (TextView) findViewById(R.id.edit_register_firstName);
        mLastName = (TextView) findViewById(R.id.edit_register_lastName);
        mAge = (TextView) findViewById(R.id.edit_register_age);
        mEmail = (TextView) findViewById(R.id.edit_register_email);
        mGroup = (Spinner) findViewById(R.id.edit_register_group);

        Button register = (Button) findViewById(R.id.button_register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {

        // Reset errors.
        mUserName.setError(null);
        mPassword.setError(null);
        mFirstName.setError(null);
        mLastName.setError(null);
        mAge.setError(null);
        mEmail.setError(null);

        boolean flag = true;

        // Check fields
        flag &= !checkEmptyFiled(mUserName);
        flag &= !checkEmptyFiled(mPassword);
        flag &= !checkEmptyFiled(mFirstName);
        flag &= !checkEmptyFiled(mLastName);
        flag &= !checkEmptyFiled(mAge);
        flag &= !checkEmptyFiled(mEmail);
        flag &= isUserValid(mUserName);
        flag &= isGroupValid(mGroup);

        if (flag) {
            ParticipantEntry pe = FactoryEntry.getParticipantEntry();
            pe.create(mFirstName.getText().toString(),
                    mLastName.getText().toString(),
                    Integer.parseInt(mAge.getText().toString()),
                    mEmail.getText().toString(),
                    mUserName.getText().toString(),
                    mPassword.getText().toString(),
                    mGroup.getSelectedItem().toString());
            finish();
        }
    }

    private boolean isGroupValid(Spinner mGroup) {
        return mGroup.getSelectedItemPosition() != 0;
    }

    private boolean checkEmptyFiled(TextView field) {
        if (TextUtils.isEmpty(field.getText())) {
            field.setError("Please fill this field");
            field.requestFocus();
            return true;
        } else return false;
    }

    private boolean isUserValid(TextView field) {
        ParticipantEntry pe = FactoryEntry.getParticipantEntry();
        Cursor cursor = pe.fetch(null, new MyPair[]{new MyPair(pe.USER_NAME, field.getText().toString())});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        field.setError("This username already exists");
        field.requestFocus();
        return false;
    }
}
