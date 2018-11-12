package com.sample.app12306.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.sample.app12306.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText mEditTextOldPasswd;
    private EditText mEditTextNewPasswd;
    private EditText mEditTextConfirmPasswd;
    private Button mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_change_password);
        mEditTextOldPasswd = findViewById(R.id.edit_text_old_passwd);
        mEditTextNewPasswd = findViewById(R.id.edit_text_new_passwd);
        mEditTextConfirmPasswd = findViewById(R.id.edit_text_confirm_passwd);
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
