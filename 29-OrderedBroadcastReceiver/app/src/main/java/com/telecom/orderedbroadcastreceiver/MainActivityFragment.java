package com.telecom.orderedbroadcastreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String ACTION_OBR_TEST="com.telecom.orderedbroadcastreceiver.OBRTEST";
    private Button mSendOrderedBroadcastButton;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mSendOrderedBroadcastButton = v.findViewById(R.id.send_ordered_broadcast_button);
        mSendOrderedBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_OBR_TEST);
                Bundle bundle = new Bundle();
                bundle.putString("name","约翰");
                getActivity().sendOrderedBroadcast(intent, null, null, null, Activity.RESULT_OK, null, bundle);
            }
        });
        return v;
    }
}
