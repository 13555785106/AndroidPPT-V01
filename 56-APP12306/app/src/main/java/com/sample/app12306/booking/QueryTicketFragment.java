package com.sample.app12306.booking;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.app12306.DatePickerFragment;
import com.sample.app12306.MainFragment;
import com.sample.app12306.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class QueryTicketFragment extends Fragment {
    public static final String ARG_STATION_FROM = "argStationFrom";
    public static final String ARG_STATION_TO = "argStationTo";
    public static final String ARG_DEPARTURE_DATE = "argDepartureDate";
    private static final int REQUEST_DATE = 0;
    private TextView mTextViewPreviousDay;
    private TextView mTextViewNextDay;
    private TextView mTextViewDepartureDate;
    private RecyclerView mRecyclerViewTickets;
    private String mStationFrom = "";
    private String mStationTo = "";
    private Date mDepartureDate = new Date();
    private Calendar mCalendar = Calendar.getInstance();
    private TextView mTextViewToolbarTitle;

    public static QueryTicketFragment newInstance(String stationFrom, String stationTo, String departureDate) {
        QueryTicketFragment fragment = new QueryTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATION_FROM, stationFrom);
        args.putString(ARG_STATION_TO, stationTo);
        args.putString(ARG_DEPARTURE_DATE, departureDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mStationFrom = getArguments().getString(ARG_STATION_FROM);
            mStationTo = getArguments().getString(ARG_STATION_TO);
            try {
                mDepartureDate = MainFragment.simpleDateFormat.parse(getArguments().getString(ARG_DEPARTURE_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_query_ticket, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        toolbar.setTitle("");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextViewToolbarTitle = v.findViewById(R.id.toolbar_title);
        mTextViewToolbarTitle.setText(mStationFrom + " <> " + mStationTo);
        mTextViewToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mStationFrom;
                mStationFrom = mStationTo;
                mStationTo = temp;
                mTextViewToolbarTitle.setText(mStationFrom + " <> " + mStationTo);
            }
        });

        mTextViewDepartureDate = v.findViewById(R.id.text_view_departure_date);
        mTextViewDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(mDepartureDate);
                dialog.setTargetFragment(QueryTicketFragment.this, REQUEST_DATE);
                dialog.show(QueryTicketFragment.this.getFragmentManager(), "DialogDate");
            }
        });

        departureDateAddDay(0);
        mTextViewPreviousDay = v.findViewById(R.id.text_view_previous_day);
        mTextViewPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departureDateAddDay(-1);
            }
        });

        mTextViewNextDay = v.findViewById(R.id.text_view_next_day);
        mTextViewNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departureDateAddDay(1);
            }
        });

        mRecyclerViewTickets = v.findViewById(R.id.recycler_view_tickets);
        mRecyclerViewTickets.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewTickets.setAdapter(new TicketAdapter());

        return v;
    }

    private void departureDateAddDay(int days) {
        if (days != 0) {
            mCalendar.setTime(mDepartureDate);
            mCalendar.add(Calendar.DAY_OF_YEAR, days);
            mDepartureDate = mCalendar.getTime();
        }
        mTextViewDepartureDate.setText(MainFragment.simpleDateFormat.format(mDepartureDate));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(ARG_STATION_FROM, mStationFrom);
                intent.putExtra(ARG_STATION_TO, mStationTo);
                intent.putExtra(ARG_DEPARTURE_DATE, mDepartureDate);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            mDepartureDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            departureDateAddDay(0);
        }
    }

    private class TicketAdapter extends RecyclerView.Adapter<TicketHolder> {

        @Override
        public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TicketHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TicketHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    private class TicketHolder extends RecyclerView.ViewHolder {
        TextView mTextViewTrainNum;
        TextView mTextViewDepartureTime;
        TextView mTextViewArrivalTime;
        TextView mTextViewTravelTime;
        TextView mTextViewFirstClassSeatAmount;
        TextView mTextViewSecondClassSeatAmount;
        ImageView mImageViewBooking;

        public TicketHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_train_num_info, parent, false));
            mTextViewTrainNum = itemView.findViewById(R.id.text_view_train_num);
            mTextViewDepartureTime = itemView.findViewById(R.id.text_view_departure_time);
            mTextViewArrivalTime = itemView.findViewById(R.id.text_view_arrival_time);
            mTextViewTravelTime = itemView.findViewById(R.id.text_view_travel_time);
            mTextViewFirstClassSeatAmount = itemView.findViewById(R.id.text_view_first_class_seat_amount);
            mTextViewSecondClassSeatAmount = itemView.findViewById(R.id.text_view_second_class_seat_amount);
            mImageViewBooking = itemView.findViewById(R.id.image_view_booking);
            mImageViewBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookingActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
