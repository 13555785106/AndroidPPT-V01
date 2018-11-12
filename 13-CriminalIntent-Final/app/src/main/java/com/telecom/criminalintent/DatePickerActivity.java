package com.telecom.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;
import java.util.UUID;

public class DatePickerActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_DATE = "com.telecom.criminalintent.crime_date";

    public static Intent newIntent(Context packageContext, Date crimeDate) {
        Intent intent = new Intent(packageContext, DatePickerActivity.class);
        intent.putExtra(EXTRA_CRIME_DATE, crimeDate);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        Date crimeDate = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_DATE);
        return DatePickerFragment.newInstance(crimeDate);
    }
}
