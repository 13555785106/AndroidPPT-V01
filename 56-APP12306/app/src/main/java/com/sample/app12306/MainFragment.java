package com.sample.app12306;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.app12306.booking.QueryTicketActivity;
import com.sample.app12306.booking.QueryTicketFragment;
import com.sample.app12306.db.DbSchema;
import com.sample.app12306.db.RecentStationDac;
import com.sample.app12306.db.StationPairDac;
import com.sample.app12306.model.Station;
import com.sample.app12306.model.StationPair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<StationPair>> {

    public static final String ARG_DEPARTURE_DATE = "departureDate";
    public static final String ARG_STATION_FROM = "stationFrom";
    public static final String ARG_STATION_TO = "stationTo";
    public static final int REQUEST_STATION_FROM_CODE = 1;
    public static final int REQUEST_STATION_TO_CODE = 2;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_QUERY = 0;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINESE);
    private Button mButtonStationFrom;
    private Button mButtonStationTo;
    private ImageView mImageViewExchangeStation;
    private TextView mTextViewDepartureDate;
    private Button mButtonQuery;
    private RecyclerView mRecyclerViewQueryHistory;
    private QueryHistoryAdapter mQueryHistoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mButtonStationFrom = v.findViewById(R.id.button_station_from);
        mButtonStationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectStationIntent = new Intent(getActivity(), SelectStationActivity.class);
                startActivityForResult(selectStationIntent, REQUEST_STATION_FROM_CODE);

            }
        });
        mButtonStationTo = v.findViewById(R.id.button_station_to);
        mButtonStationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectStationIntent = new Intent(getActivity(), SelectStationActivity.class);
                startActivityForResult(selectStationIntent, REQUEST_STATION_TO_CODE);
            }
        });

        mImageViewExchangeStation = v.findViewById(R.id.image_view_exchange_station);
        mImageViewExchangeStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textStationFrom = mButtonStationFrom.getText().toString();
                String textStationTo = mButtonStationTo.getText().toString();
                mButtonStationFrom.setText(textStationTo);
                mButtonStationTo.setText(textStationFrom);
            }
        });
        mTextViewDepartureDate = v.findViewById(R.id.text_view_departure_date);
        mButtonQuery = v.findViewById(R.id.button_query);
        mButtonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationFrom = mButtonStationFrom.getText().toString();
                String stationTo = mButtonStationTo.getText().toString();
                StationPair stationPair = new StationPair();
                stationPair.setStationFrom(stationFrom);
                stationPair.setStationTo(stationTo);
                stationPair.setDateTime(DbSchema.timeStampFormat.format(new Date()));
                StationPairDac stationPairDac = StationPairDac.get(getContext());
                stationPairDac.del(stationFrom, stationTo);
                stationPairDac.add(stationPair);
                LoaderManager.getInstance(MainFragment.this).restartLoader(0, null, MainFragment.this).forceLoad();

                RecentStationDac recentStationDac = RecentStationDac.get(getActivity());
                recentStationDac.del(stationFrom);
                recentStationDac.del(stationTo);
                Station station = new Station();
                station.setStationName(stationFrom);
                station.setDateTime(DbSchema.timeStampFormat.format(new Date()));
                recentStationDac.add(station);
                station.setStationName(stationTo);
                station.setDateTime(DbSchema.timeStampFormat.format(new Date()));
                recentStationDac.add(station);

                Intent intent = new Intent(getActivity(), QueryTicketActivity.class);
                intent.putExtra(ARG_DEPARTURE_DATE, mTextViewDepartureDate.getText().toString());
                intent.putExtra(ARG_STATION_FROM, mButtonStationFrom.getText().toString());
                intent.putExtra(ARG_STATION_TO, mButtonStationTo.getText().toString());
                startActivityForResult(intent, REQUEST_QUERY);
            }
        });

        mTextViewDepartureDate.setText(simpleDateFormat.format(new Date()));
        mTextViewDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date curDate = null;
                try {
                    curDate = simpleDateFormat.parse(mTextViewDepartureDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (curDate == null)
                    curDate = new Date();
                DatePickerFragment dialog = DatePickerFragment.newInstance(curDate);
                dialog.setTargetFragment(MainFragment.this, REQUEST_QUERY);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        mRecyclerViewQueryHistory = v.findViewById(R.id.recycler_view_query_history);
        mRecyclerViewQueryHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mQueryHistoryAdapter = new QueryHistoryAdapter();
        mRecyclerViewQueryHistory.setAdapter(mQueryHistoryAdapter);
        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //IntentUtils.printIntentInfo(data);
        if (requestCode == REQUEST_QUERY && resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getSerializableExtra(QueryTicketFragment.ARG_DEPARTURE_DATE);
            mTextViewDepartureDate.setText(simpleDateFormat.format(date));
            mButtonStationFrom.setText(data.getStringExtra(QueryTicketFragment.ARG_STATION_FROM));
            mButtonStationTo.setText(data.getStringExtra(QueryTicketFragment.ARG_STATION_TO));

        } else if (requestCode == REQUEST_STATION_FROM_CODE && resultCode == Activity.RESULT_OK) {
            mButtonStationFrom.setText(data.getStringExtra("stationName"));
        } else if (requestCode == REQUEST_STATION_TO_CODE && resultCode == Activity.RESULT_OK) {
            mButtonStationTo.setText(data.getStringExtra("stationName"));
        }
    }

    @NonNull
    @Override
    public Loader<List<StationPair>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new StationPairDataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<StationPair>> loader, List<StationPair> stationPairs) {
        mQueryHistoryAdapter.swapData(stationPairs);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<StationPair>> loader) {
        mQueryHistoryAdapter.swapData(null);
    }

    static class StationPairDataLoader extends AsyncTaskLoader<List<StationPair>> {
        public StationPairDataLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public List<StationPair> loadInBackground() {
            return StationPairDac.get(getContext()).all();
        }
    }

    private class QueryHistoryAdapter extends RecyclerView.Adapter<QueryHistoryHolder> {
        List<StationPair> mStationPairList = new ArrayList<>();

        @Override
        public QueryHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new QueryHistoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(QueryHistoryHolder holder, int position) {
            StationPair stationPair = mStationPairList.get(position);
            holder.bind(stationPair);
        }

        @Override
        public int getItemCount() {
            return mStationPairList.size();
        }

        public void swapData(List<StationPair> data) {
            if (data == null) {
                mStationPairList = new ArrayList<>();
            } else {
                mStationPairList = data;
            }
            notifyDataSetChanged();
        }
    }

    private class QueryHistoryHolder extends RecyclerView.ViewHolder {
        TextView mTextViewStationFrom;
        TextView mTextViewStationTo;
        StationPair mStationPair;

        public QueryHistoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_query_history, parent, false));
            mTextViewStationFrom = itemView.findViewById(R.id.text_view_station_from);
            mTextViewStationTo = itemView.findViewById(R.id.text_view_station_to);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mButtonStationFrom.setText(mTextViewStationFrom.getText());
                    mButtonStationTo.setText(mTextViewStationTo.getText());
                }
            });
        }

        public void bind(StationPair stationPair) {
            mStationPair = stationPair;
            mTextViewStationFrom.setText(mStationPair.getStationFrom());
            mTextViewStationTo.setText(mStationPair.getStationTo());
        }
    }


}
