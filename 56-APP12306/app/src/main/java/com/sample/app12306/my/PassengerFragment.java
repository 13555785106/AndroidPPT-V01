package com.sample.app12306.my;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.app12306.R;
import com.sample.app12306.util.SimpleTextWatcher;
import com.sample.app12306.model.Passenger;

public class PassengerFragment extends Fragment {
    private static final String ARG_PASSENGER = "passenger";

    private Passenger mPassenger = new Passenger();
    private EditText mEditTextName;
    private EditText mEditTextIdCardNumber;
    private EditText mEditTextPhoneNumber;
    private Callbacks mCallbacks;

    public static PassengerFragment newInstance(Passenger passenger) {
        PassengerFragment fragment = new PassengerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PASSENGER, passenger);
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        if (getArguments() != null) {
            mPassenger = getArguments().getParcelable(ARG_PASSENGER);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("onCreateView");
        View v = inflater.inflate(R.layout.fragment_passenger, container, false);
        mEditTextName = v.findViewById(R.id.edit_text_name);
        mEditTextName.setText(mPassenger.getName());
        mEditTextName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPassenger.setName(s.toString());
            }
        });
        mEditTextIdCardNumber = v.findViewById(R.id.edit_text_id_card_number);
        mEditTextIdCardNumber.setText(mPassenger.getIdCardNumber());
        mEditTextIdCardNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPassenger.setIdCardNumber(s.toString());
            }
        });

        mEditTextPhoneNumber = v.findViewById(R.id.edit_text_phone_number);
        mEditTextPhoneNumber.setText(mPassenger.getPhoneNumber());
        mEditTextPhoneNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPassenger.setPhoneNumber(s.toString());
            }
        });
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mCallbacks.sendPassengerInfo(mPassenger);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface Callbacks {
        void sendPassengerInfo(Passenger passenger);
    }
}
