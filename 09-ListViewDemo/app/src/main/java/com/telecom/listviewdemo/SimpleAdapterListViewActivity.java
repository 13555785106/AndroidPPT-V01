package com.telecom.listviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapterListViewActivity extends AppCompatActivity {
    DecimalFormat mDecimalFormat = new DecimalFormat("000");
    ListView mStudentListView;
    SimpleAdapter mStudentSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_adapter_list_view);
        mStudentListView = (ListView) findViewById(R.id.student_list_view);
        mStudentListView.addHeaderView(getLayoutInflater().inflate(R.layout.student_header, null));
        mStudentListView.addFooterView(getLayoutInflater().inflate(R.layout.student_footer, null));

        List<Map<String, Object>> studentList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("student_thumb", R.drawable.student_id_icon_32);
            studentMap.put("student_id", mDecimalFormat.format(i));
            studentMap.put("student_name", "学生" + mDecimalFormat.format(i));
            studentMap.put("student_age", new Integer(20 + (int) (Math.random() * 4)));
            studentMap.put("student_checked", new Boolean(i % 2 == 0));
            studentList.add(studentMap);
        }
        mStudentSimpleAdapter = new SimpleAdapter(
                this, studentList,
                R.layout.student_item,
                new String[]{"student_thumb", "student_id", "student_name", "student_age", "student_checked"},
                new int[]{R.id.thumb_image_view, R.id.id_text_view, R.id.name_text_view, R.id.age_text_view, R.id.checked_text_view});
        mStudentSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(final View view, Object data, String textRepresentation) {
                if (view instanceof CheckedTextView) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckedTextView ctv = (CheckedTextView) v;
                            ctv.setChecked(!ctv.isChecked());
                        }
                    });
                }
                //System.out.println(view);
                //System.out.println(data);
                //System.out.println(textRepresentation);
                //返回值为true时表示自己处理了数据的显示
                return false;
            }
        });
        mStudentListView.setAdapter(mStudentSimpleAdapter);
        mStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar snackbar = Snackbar.make(view, mStudentSimpleAdapter.getItem(position).toString(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.array_adapter_list_view:
                Intent intent = new Intent(this, ArrayAdapterListViewActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
