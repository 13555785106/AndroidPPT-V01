package com.telecom.criminalintent;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaojf on 17/12/26.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.telecom.criminalintent.date";
    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Button mOkButton;
    private Date mDate;
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_DATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("-------------------------onCreateView-------------------------");
        View v = inflater.inflate(R.layout.dialog_date, container, false);
        updateDtPicker(v);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mDate);
        getActivity().setResult(Activity.RESULT_OK,intent);
        return v;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("-------------------------onCreateDialog-------------------------");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        updateDtPicker(v);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setIcon(R.drawable.calendar)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment()!= null) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_DATE, mDate);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create();
    }
    private void updateDtPicker(View v){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                mDate.setTime(calendar.getTime().getTime());
            }
        });
        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                mDate.setTime(calendar.getTime().getTime());
            }
        });
    }
}
