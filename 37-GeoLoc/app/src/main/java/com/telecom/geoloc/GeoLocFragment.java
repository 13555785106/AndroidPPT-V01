package com.telecom.geoloc;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeoLocFragment extends Fragment {
    private static final String TAG = "GeoLocFragment";
    LocationManager mLocationManager;
    TextView mGpsLatTextView;
    TextView mGpsLongTextView;
    TextView mGpsTimeTextView;
    TextView mNetworkLatTextView;
    TextView mNetworkLongTextView;
    TextView mNetworkTimeTextView;
    TextView mMsgTextView;
    LocationListener mGpsLocationListener;
    LocationListener mNetworkLocationListener;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
    String mBestLocationProvider = null;
    public static GeoLocFragment newInstance() {
        return new GeoLocFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mGpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "GPS onLocationChanged " + location);
                mGpsLatTextView.setText(Double.toString(location.getLatitude()));
                mGpsLongTextView.setText(Double.toString(location.getLongitude()));
                mGpsTimeTextView.setText(sdf.format(new Date(location.getTime())));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "GPS onStatusChanged");
                switch (status) {
                    // GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        Log.i(TAG, "当前GPS状态为可见状态");
                        break;
                    // GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.i(TAG, "当前GPS状态为服务区外状态");
                        break;
                    // GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.i(TAG, "当前GPS状态为暂停服务状态");
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, provider + " onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i(TAG, provider + " onProviderDisabled");
            }
        };
        mNetworkLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("NETWORK onLocationChanged " + location);
                mNetworkLatTextView.setText(Double.toString(location.getLatitude()));
                mNetworkLongTextView.setText(Double.toString(location.getLongitude()));
                mNetworkTimeTextView.setText(sdf.format(new Date(location.getTime())));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "NETWORK onStatusChanged");
                switch (status) {
                    // NETWORK状态为可见时
                    case LocationProvider.AVAILABLE:
                        Log.i(TAG, "当前NETWORK状态为可见状态");
                        break;
                    // GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.i(TAG, "当前NETWORK状态为服务区外状态");
                        break;
                    // GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.i(TAG, "当前NETWORK状态为暂停服务状态");
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, provider + " onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i(TAG, provider + " onProviderDisabled");
            }
        };

        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mBestLocationProvider = mLocationManager.getBestProvider(criteria,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_geo_loc, container, false);
        mGpsLatTextView = v.findViewById(R.id.gps_lat_text_view);
        mGpsLongTextView = v.findViewById(R.id.gps_long_text_view);
        mGpsTimeTextView = v.findViewById(R.id.gps_time_text_view);
        mNetworkLatTextView = v.findViewById(R.id.network_lat_text_view);
        mNetworkLongTextView = v.findViewById(R.id.network_long_text_view);
        mNetworkTimeTextView = v.findViewById(R.id.network_time_text_view);
        mMsgTextView = v.findViewById(R.id.msg_text_view);
        mMsgTextView.append("BestProvider:"+mBestLocationProvider+"\r\n");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mGpsLocationListener);
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mNetworkLocationListener);
        } else {
            Toast.makeText(getActivity(), "需要 ACCESS_FINE_LOCATION 和 ACCESS_COARSE_LOCATION 权限", Toast.LENGTH_LONG).show();
        }
        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationManager!= null){
            if(mGpsLocationListener != null)
                mLocationManager.removeUpdates(mGpsLocationListener);
            if(mNetworkLocationListener != null)
                mLocationManager.removeUpdates(mNetworkLocationListener);
        }
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
