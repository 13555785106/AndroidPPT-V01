package com.telecom.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiaojf on 17/12/24.
 */

public class CrimeListFragment extends Fragment {
    private static String TAG = CrimeListFragment.class.getSimpleName();
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private AtomicInteger mItemViewCount = new AtomicInteger(0);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });

        }
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }
    }
    private class CrimePlusHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mRequiresPoliceButton;
        private Crime mCrime;
        public CrimePlusHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_plus, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mRequiresPoliceButton = itemView.findViewById(R.id.crime_requires_police);
            mRequiresPoliceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mCrime.getTitle() + " 已经发送给警方！", Toast.LENGTH_SHORT)
                            .show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });

        }
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG,"mItemViewCount=" + mItemViewCount.incrementAndGet());
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType==1)
                return new CrimePlusHolder(layoutInflater, parent);
            else
                return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if(getItemViewType(position) ==1)
                ((CrimePlusHolder)holder).bind(crime);
            else
                ((CrimeHolder)holder).bind(crime);
        }

        @Override
        public int getItemViewType(int position) {
            Crime curItem = mCrimes.get(position);
            if(curItem.isRequiresPolice())
                return 1;
            else
                return 0;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
