package com.telecom.listviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;


public class ArrayAdapterListViewActivity extends AppCompatActivity {
    DecimalFormat mDecimalFormat = new DecimalFormat("000");
    ListView mListView;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_adapter_list_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);
        for (int i = 0; i < 100; i++) {
            mArrayAdapter.add(mDecimalFormat.format(i));
        }

        mListView.setAdapter(mArrayAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray sparseBooleanArray = mListView.getCheckedItemPositions();
                System.out.println(sparseBooleanArray);
            }
        });
    }
}
