package com.telecom.phonecall;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhoneCallFragment extends Fragment {
    private static final String TAG = "PhoneCallFragment";
    Button mDialButton;
    EditText mPhoneNumEditText;
    TextView mIncomingCallStateTextView;
    private TelephonyManager mTelephonyManager = null;
    private PhoneStateListener mPhoneStateListener = null;
    public static PhoneCallFragment newInstance() {
        return new PhoneCallFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mTelephonyManager == null)
            mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_call, container, false);
        mIncomingCallStateTextView = v.findViewById(R.id.incoming_call_state_text_view);
        mPhoneNumEditText = v.findViewById(R.id.phone_num_edit_text);
        mDialButton = v.findViewById(R.id.dial_button);
        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + mPhoneNumEditText.getText()));
                startActivity(intent);
            }
        });

        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        mIncomingCallStateTextView.append(incomingNumber + " 来电\r\n");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        mIncomingCallStateTextView.append("拨打 " + incomingNumber + "\r\n");
                        break;
                    default:
                        mIncomingCallStateTextView.append(incomingNumber + " state=" + state + "\r\n");
                        break;
                }
            }
        };
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
