package com.sample.a14_toolbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class NormalToolbarActivity extends AppCompatActivity {
    private final static String TAG = NormalToolbarActivity.class.getSimpleName();
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去掉标题，及ActionBar。此行代码必须在setContentView之前调用，否则无效！！！
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_normal_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_normal, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        Log.e(TAG, searchItem.toString());
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.e(TAG, "onQueryTextChange:" + queryText);
                //当长度为零时，回车提交无效。
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Log.e(TAG, "onQueryTextSubmit:" + queryText);
                //SearchView存在bug,回车之后会提交两次。
                //此处的解决办法是立刻使其失去焦点。
                mSearchView.clearFocus();
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.e(TAG, "item.getItemId()=" + item.getItemId());
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                Log.e(TAG, "action_settings");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
