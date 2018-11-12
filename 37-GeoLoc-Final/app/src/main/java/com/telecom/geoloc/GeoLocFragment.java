package com.telecom.geoloc;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeoLocFragment extends Fragment {
    private static final String TAG = "GeoLocFragment";

    private LocationManager mLocationManager;
    private TextView mGpsLatTextView;
    private TextView mGpsLongTextView;
    private TextView mGpsTimeTextView;
    private TextView mGpsTotalDistanceTextView;
    private MapView mapView;
    private AMap aMap;
    private LocationListener mGpsLocationListener;
    private List<Location> mLocations = new ArrayList<>();
    private List<LatLng> mPoints = new ArrayList<LatLng>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
    private Calendar cal = Calendar.getInstance();

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
                cal.setTimeInMillis(location.getTime());
                mGpsTimeTextView.setText(sdf.format(cal.getTime()));
                mLocations.add(location);
                mPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                if (mLocations.size() > 1) {
                    float totalDistance = 0f;

                    for (int i = 0; i < mLocations.size() - 1; i++) {
                        Location locStart = mLocations.get(i);
                        Location locEnd = mLocations.get(i + 1);
                        float[] results = new float[1];
                        Location.distanceBetween(locStart.getLatitude(), locStart.getLongitude(), locEnd.getLatitude(), locEnd.getLongitude(), results);
                        if (results != null) {
                            totalDistance += results[0];
                        }
                    }
                    mGpsTotalDistanceTextView.setText(Float.toString(totalDistance));
                }
                aMap.clear();
                Polyline polyline = aMap.addPolyline(new PolylineOptions()
                        .addAll(mPoints).width(8).color(Color.argb(255, 200, 100, 100)));
                LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
                for (int i = 0; i < mPoints.size(); i++) {
                    newbounds.include(mPoints.get(i));
                }
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 15));
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_geo_loc, container, false);
        mGpsLatTextView = v.findViewById(R.id.gps_lat_text_view);
        mGpsLongTextView = v.findViewById(R.id.gps_long_text_view);
        mGpsTimeTextView = v.findViewById(R.id.gps_time_text_view);
        mGpsTotalDistanceTextView = v.findViewById(R.id.gps_total_distance_text_view);
        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mGpsLocationListener);
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
        if (mLocationManager != null) {
            if (mGpsLocationListener != null)
                mLocationManager.removeUpdates(mGpsLocationListener);
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
