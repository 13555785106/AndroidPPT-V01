package com.sample.app12306.my;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.sample.app12306.R;
import com.sample.app12306.model.Passenger;

public class PassengersFragment extends Fragment {
    private RecyclerView mRecyclerViewPassengers;
    private Callbacks mCallbacks;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
        mRecyclerViewPassengers = v.findViewById(R.id.recycler_view_passengers);
        mRecyclerViewPassengers.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewPassengers.setAdapter(new PassengerAdapter());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_passengers, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.new_passenger_info:
                mCallbacks.onNewPassenger();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface Callbacks {
        void onPassengerSelected(Passenger passenger);

        void onNewPassenger();
    }

    private class PassengerAdapter extends RecyclerView.Adapter<PassengerHolder> {

        @Override
        public PassengerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PassengerHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PassengerHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    private class PassengerHolder extends RecyclerView.ViewHolder {
        ImageView mImageViewLogo;
        EditText mEditTextName;
        EditText mEditTextIdCardNumber;
        EditText mEditTextPhoneNumber;

        public PassengerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_passenger_info, parent, false));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.onPassengerSelected(null);
                }
            });
            mImageViewLogo = itemView.findViewById(R.id.image_view_logo);
            mEditTextName = itemView.findViewById(R.id.edit_text_name);
            mEditTextIdCardNumber = itemView.findViewById(R.id.edit_text_id_card_number);
            mEditTextPhoneNumber = itemView.findViewById(R.id.edit_text_phone_number);
        }
    }
}
