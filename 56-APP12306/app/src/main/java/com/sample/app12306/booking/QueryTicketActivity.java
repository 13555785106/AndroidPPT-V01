package com.sample.app12306.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.sample.app12306.MainFragment;
import com.sample.app12306.R;


public class QueryTicketActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query_ticket);
        Intent intent = getIntent();
        if (intent != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, QueryTicketFragment.newInstance(
                            intent.getStringExtra(MainFragment.ARG_STATION_FROM),
                            intent.getStringExtra(MainFragment.ARG_STATION_TO),
                            intent.getStringExtra(MainFragment.ARG_DEPARTURE_DATE)
                    ))
                    .commit();
        }
    }
}




/*ActionBar actionBar = getSupportActionBar();
actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View actionBarView = inflater.inflate(R.layout.query_ticket_action_bar, null);

ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
        Gravity.CENTER);
actionBar.setCustomView(actionBarView, layoutParams);
actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00CFAA")));
actionBar.setDisplayHomeAsUpEnabled(true);
actionBar.setHomeAsUpIndicator(R.drawable.arrows_left_icon);*/