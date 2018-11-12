package com.sample.app12306.my;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.sample.app12306.R;
import com.sample.app12306.model.Passenger;

public class PassengersActivity extends AppCompatActivity implements PassengersFragment.Callbacks, PassengerFragment.Callbacks {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00CFAA")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_passengers);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new PassengersFragment())
                .commit();
    }

    @Override
    public void onPassengerSelected(Passenger passenger) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PassengerFragment())
                .commit();
    }

    @Override
    public void onNewPassenger() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PassengerFragment())
                .commit();
    }

    @Override
    public void sendPassengerInfo(Passenger passenger) {
        System.out.println(passenger);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PassengersFragment())
                .commit();
    }
}
