package com.sample.app12306;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sample.app12306.my.ChangePasswordActivity;
import com.sample.app12306.my.PassengersActivity;
import com.sample.app12306.my.PersonalProfileActivity;
import com.sample.app12306.my.SettingsActivity;

public class MyFragment extends Fragment {
    Button mButtonPersonalProfile;
    Button mButtonChangePasswd;
    Button mButtonPassengersInfo;
    Button mButtonPreference;
    Button mButtonQuit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my, container, false);
        mButtonPersonalProfile = v.findViewById(R.id.button_personal_profile);
        mButtonPersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalProfileActivity.class);
                startActivity(intent);
            }
        });
        mButtonChangePasswd = v.findViewById(R.id.button_change_passwd);
        mButtonChangePasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        mButtonPassengersInfo = v.findViewById(R.id.button_passengers_info);
        mButtonPassengersInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PassengersActivity.class);
                startActivity(intent);
            }
        });
        mButtonPreference = v.findViewById(R.id.button_preference);
        mButtonPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        mButtonQuit = v.findViewById(R.id.button_quit);
        mButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        return v;
    }


}
