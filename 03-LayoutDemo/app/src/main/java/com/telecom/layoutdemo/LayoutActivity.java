package com.telecom.layoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;


public class LayoutActivity extends AppCompatActivity {
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                Button button = (Button) v;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch (v.getId()) {
                            case R.id.button_linear_layout:
                                intent = new Intent(LayoutActivity.this,LinearLayoutActivity.class);
                                break;
                            case R.id.button_frame_layout:
                                intent = new Intent(LayoutActivity.this,FrameLayoutActivity.class);
                                break;
                            case R.id.button_relative_layout:
                                intent = new Intent(LayoutActivity.this,RelativeLayoutActivity.class);
                                break;
                            case R.id.button_constraint_layout:
                                intent = new Intent(LayoutActivity.this,ConstraintLayoutActivity.class);
                                break;
                            case R.id.button_table_layout:
                                intent = new Intent(LayoutActivity.this,TableLayoutActivity.class);
                                break;
                            case R.id.button_grid_layout:
                                intent = new Intent(LayoutActivity.this,GridLayoutActivity.class);
                                break;
                            case R.id.button_tab_layout:
                                intent = new Intent(LayoutActivity.this,TabLayoutActivity.class);
                                break;
                        }
                        if (intent != null)
                            startActivity(intent);
                    }
                });
            }
        }
    }
}
