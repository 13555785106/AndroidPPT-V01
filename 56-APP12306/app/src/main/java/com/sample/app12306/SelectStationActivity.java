package com.sample.app12306;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sample.app12306.db.RecentStationDac;
import com.sample.app12306.db.StationListDac;
import com.sample.app12306.model.Station;

import java.util.ArrayList;
import java.util.List;

public class SelectStationActivity extends AppCompatActivity {
    private EditText mEditTextStationName;
    private TextView mTextViewCancel;
    private RecyclerView mRecyclerViewRecentStations;
    private RecyclerView mRecyclerViewStationsList;
    private StationsAdapter mRecentStationsAdapter;
    LoaderManager.LoaderCallbacks<List<Station>> recentStationsCallbacks = new LoaderManager.LoaderCallbacks<List<Station>>() {


        @NonNull
        @Override
        public RecentStationsDataLoader onCreateLoader(int i, @Nullable Bundle bundle) {
            return new RecentStationsDataLoader(SelectStationActivity.this);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Station>> loader, List<Station> stations) {
            mRecentStationsAdapter.swapData(stations);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Station>> loader) {
            mRecentStationsAdapter.swapData(null);
        }
    };
    private StationsAdapter mStationsListAdapter;
    LoaderManager.LoaderCallbacks<List<Station>> stationsListCallbacks = new LoaderManager.LoaderCallbacks<List<Station>>() {


        @NonNull
        @Override
        public StationsListDataLoader onCreateLoader(int i, @Nullable Bundle bundle) {
            StationsListDataLoader stationsListDataLoader = new StationsListDataLoader(SelectStationActivity.this, mEditTextStationName.getText().toString());
            return stationsListDataLoader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Station>> loader, List<Station> stations) {
            mStationsListAdapter.swapData(stations);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Station>> loader) {
            mStationsListAdapter.swapData(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
        mEditTextStationName = findViewById(R.id.edit_text_station_name);
        mEditTextStationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mTextViewCancel.setVisibility(View.VISIBLE);
                else
                    mTextViewCancel.setVisibility(View.GONE);
            }
        });
        mEditTextStationName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println(actionId);
                if (event != null)
                    System.out.println(event.getKeyCode());
                boolean ret = event == null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                System.out.println(ret);
                if (ret) {
                    mEditTextStationName.clearFocus();
                    LoaderManager.getInstance(SelectStationActivity.this).restartLoader(1, null, stationsListCallbacks).forceLoad();
                }
                return false;
            }
        });
        mTextViewCancel = findViewById(R.id.text_view_cancel);
        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextStationName.clearFocus();
            }
        });

        mRecyclerViewRecentStations = findViewById(R.id.recycler_view_recent_stations);
        mRecyclerViewRecentStations.setLayoutManager(new GridLayoutManager(this, 4));
        mRecentStationsAdapter = new StationsAdapter();
        mRecyclerViewRecentStations.setAdapter(mRecentStationsAdapter);
        LoaderManager.getInstance(this).initLoader(0, null, recentStationsCallbacks).forceLoad();

        mRecyclerViewStationsList = findViewById(R.id.recycler_view_stations_list);
        mRecyclerViewStationsList.setLayoutManager(new GridLayoutManager(this, 4));
        mStationsListAdapter = new StationsAdapter();
        mRecyclerViewStationsList.setAdapter(mStationsListAdapter);
        LoaderManager.getInstance(this).initLoader(1, null, stationsListCallbacks).forceLoad();
    }

    static class RecentStationsDataLoader extends AsyncTaskLoader<List<Station>> {
        public RecentStationsDataLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public List<Station> loadInBackground() {
            return RecentStationDac.get(getContext()).all();
        }
    }

    static class StationsListDataLoader extends AsyncTaskLoader<List<Station>> {
        private String stationName = "";

        public StationsListDataLoader(@NonNull Context context, String stationName) {
            super(context);
            this.stationName = stationName;
        }

        @Nullable
        @Override
        public List<Station> loadInBackground() {
            return StationListDac.get(getContext()).all(stationName);
        }
    }

    private class StationsAdapter extends RecyclerView.Adapter<StationHolder> {
        List<Station> stationList = new ArrayList<>();


        @Override
        public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SelectStationActivity.this);
            return new StationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StationHolder holder, int position) {
            holder.bind(stationList.get(position));
        }

        @Override
        public int getItemCount() {
            return stationList.size();
        }

        public void swapData(List<Station> data) {
            if (data == null) {
                stationList = new ArrayList<>();
            } else {
                stationList = data;
            }
            notifyDataSetChanged();
        }

    }

    private class StationHolder extends RecyclerView.ViewHolder {
        Button mButtonStationName;
        Station mStation;

        public StationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_station_name, parent, false));
            mButtonStationName = itemView.findViewById(R.id.button_station_name);
            mButtonStationName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("stationName", mButtonStationName.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    SelectStationActivity.this.finish();
                    System.out.println(mButtonStationName.getText());
                }
            });
        }

        public void bind(Station station) {
            mStation = station;
            mButtonStationName.setText(mStation.getStationName());
        }
    }
}
