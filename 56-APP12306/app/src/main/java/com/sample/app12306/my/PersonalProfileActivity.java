package com.sample.app12306.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.sample.app12306.R;

public class PersonalProfileActivity extends AppCompatActivity {
    private EditText mEditTextName;
    private EditText mEditTextPhone;
    private EditText mEditTextEmail;
    private Button mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_personal_profile);
        mEditTextName = findViewById(R.id.edit_text_name);
        mEditTextName.setError("dddddd");
        mEditTextPhone = findViewById(R.id.edit_text_phone);
        mEditTextEmail = findViewById(R.id.edit_text_email);
        mButtonOk = findViewById(R.id.button_ok);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
