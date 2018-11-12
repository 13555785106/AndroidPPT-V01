package com.sample.preference;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView mTextViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Build.VERSION.SDK_INT="+Build.VERSION.SDK_INT);
        System.out.println("Build.PRODUCT="+Build.PRODUCT);
        setContentView(R.layout.activity_main);
        mTextViewMessage = findViewById(R.id.text_view_message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextViewMessage.setText("");
        for (Map.Entry<String, ?> entry : PreferenceManager.getDefaultSharedPreferences(this).getAll().entrySet()) {
            mTextViewMessage.append(entry.getKey() + " = " + entry.getValue().toString() + "\r\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferenceWithHeadersActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
