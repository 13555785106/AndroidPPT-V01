package com.telecom.networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView mTextViewMsg;
    Button mButtonGetNetworkState;
    ConnectivityManager mConnectivityManager;
    BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get("networkInfo");

                NetworkInfo.State state = networkInfo.getState();
                int type = networkInfo.getType();
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    mTextViewMsg.append("移动网络 ");
                    mTextViewMsg.append(getMobileNetworkTypeName(networkInfo.getSubtype()));

                } else if (type == ConnectivityManager.TYPE_WIFI) {
                    mTextViewMsg.append("WIFI网络");
                }
                if (state != null) {
                    if (state.equals(NetworkInfo.State.CONNECTED)) {
                        mTextViewMsg.append("已连接");
                    }
                    if (state.equals(NetworkInfo.State.DISCONNECTED)) {
                        mTextViewMsg.append("已断开");
                    }
                }
                mTextViewMsg.append("\r\n");
            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                //IntentUtils.printIntentInfo(intent);
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                IntentUtils.printIntentInfo(intent);
            }
        }

    };


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mNetworkStateReceiver, filter);
        if (mConnectivityManager == null)
            mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mTextViewMsg = v.findViewById(R.id.text_view_msg);
        mButtonGetNetworkState = v.findViewById(R.id.button_get_network_state);
        mButtonGetNetworkState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    mTextViewMsg.append(activeNetworkInfo.getTypeName());
                    if (activeNetworkInfo.getSubtype() != 0) {
                        mTextViewMsg.append("[" + activeNetworkInfo.getSubtypeName() + "]");
                    }
                    mTextViewMsg.append(" isConnected=" + activeNetworkInfo.isConnected());
                    mTextViewMsg.append(" isAvailable=" + activeNetworkInfo.isAvailable());
                    mTextViewMsg.append(" isRoaming=" + activeNetworkInfo.isRoaming());
                    mTextViewMsg.append("\r\n");
                } else {
                    mTextViewMsg.append("无有效连接" + "\r\n");
                }
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mNetworkStateReceiver);
    }

    private String getMobileNetworkTypeName(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "SCDMA";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "IWLAN";

        }
        return "UNKNOWN";
    }
}
