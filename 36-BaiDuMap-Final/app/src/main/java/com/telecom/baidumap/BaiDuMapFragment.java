package com.telecom.baidumap;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaiDuMapFragment extends Fragment implements SensorEventListener {
    private final LatLng syzPos = new LatLng(41.799887057920124, 123.4028060226653);

    LocationClient mLocClient;
    BitmapDescriptor mCurrentMarker;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mSearchRestaurantButton;
    private Button mDriveCarButton;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;


    public static BaiDuMapFragment newInstance() {
        return new BaiDuMapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_baidu_du_map, container, false);
        mMapView = v.findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);//获取传感器管理服务


        mSearchRestaurantButton = v.findViewById(R.id.search_restaurant_button);
        mDriveCarButton = v.findViewById(R.id.drive_car_button);
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mBaiduMap.getUiSettings().setCompassEnabled(true);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(syzPos);
        builder.zoom(17f);
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(BaiDuMapFragment.class.getName(), latLng.latitude + "," + latLng.longitude);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red_car_32);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(bitmapDescriptor)
                .position(syzPos)
                .anchor(0.5f, 0.5f)
                .alpha(.8f)
                .period(3)
                .rotate(50)
                .animateType(MarkerOptions.MarkerAnimateType.jump)
                .draggable(true);
        Marker marker = (Marker) mMapView.getMap().addOverlay(markerOptions);
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
                .center(syzPos)
                .stroke(new Stroke(5, 0x33000000))
                .radius(500);
        Circle mCircle = (Circle) mMapView.getMap().addOverlay(ooCircle);
        final List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(41.7928016484679, 123.39737127432993));
        points.add(new LatLng(41.79984672575457, 123.40274314127959));
        points.add(new LatLng(41.79877791404827, 123.40511466782594));
        points.add(new LatLng(41.80222627976166, 123.40777365213548));
        points.add(new LatLng(41.805761836826456, 123.39950924144368));
        points.add(new LatLng(41.80217922729667, 123.39668856214234));
        OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF8800).points(points);
        Polyline mPolyline = (Polyline) mMapView.getMap().addOverlay(ooPolyline);
        mPolyline.setDottedLine(true);
        mSearchRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PoiSearch mPoiSearch = PoiSearch.newInstance();
                mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                    @Override
                    public void onGetPoiResult(PoiResult poiResult) {

                        if (poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                            System.out.println("检索失败！");
                        } else {
                            for (PoiInfo poiInfo : poiResult.getAllPoi()) {
                                LinearLayout linearLayout = new LinearLayout(getActivity());
                                ImageView imageView = new ImageView(getActivity());
                                imageView.setImageResource(R.drawable.red_marker_bubble);
                                TextView textView = new TextView(getActivity());
                                textView.setBackgroundColor(Color.YELLOW);
                                textView.setText(poiInfo.name);
                                linearLayout.addView(imageView);
                                linearLayout.addView(textView);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(linearLayout);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .icon(bitmapDescriptor)
                                        .perspective(true)
                                        .title(poiInfo.name)
                                        .position(poiInfo.location)
                                        .zIndex(9)
                                        .period(3);
                                mMapView.getMap().addOverlay(markerOptions);
                            }
                        }
                        mPoiSearch.destroy();
                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                    }

                    @Override
                    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                    }
                });
                mPoiSearch.searchNearby(new PoiNearbySearchOption()
                        .keyword("餐厅")
                        .sortType(PoiSortType.distance_from_near_to_far)
                        .location(syzPos)
                        .radius(500)
                        .pageCapacity(10)
                        .pageNum(0));
            }
        });

        mDriveCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.yellow_car_48);
                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(bitmapDescriptor)
                        .position(points.get(0))
                        .anchor(0.5f, 0.5f)
                        .alpha(.8f);
                final Marker carMarker = (Marker) mMapView.getMap().addOverlay(markerOptions);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (LatLng point : calInterpolationPoint(points, 100)) {
                            carMarker.setPosition(point);
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        carMarker.remove();
                    }
                });
                t.start();
            }
        });


        mBaiduMap.setMyLocationEnabled(true);

        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker));

        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(
                new MapStatus.Builder().overlook(0).build()
        ));
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null || mMapView == null) {
                    return;
                }
                mCurrentLat = location.getLatitude();
                mCurrentLon = location.getLongitude();
                mCurrentAccracy = location.getRadius();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(mCurrentDirection).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[0];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        mMapView.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private List<LatLng> calInterpolationPoint(List<LatLng> points, int num) {
        List<LatLng> interpolationPoints = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        List<Integer> segAmounts = new ArrayList<>();
        if (points.size() >= 2) {
            for (int i = 0; i < points.size() - 1; i++) {
                distances.add(DistanceUtil.getDistance(points.get(i), points.get(i + 1)));
            }
            double totalDistance = 0;
            for (int i = 0; i < distances.size(); i++)
                totalDistance += distances.get(i);
            for (int i = 0; i < distances.size(); i++) {
                segAmounts.add(new BigDecimal(distances.get(i) * num / totalDistance).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
            }
            for (int i = 0; i < segAmounts.size(); i++) {
                LatLng startPoint = points.get(i);
                interpolationPoints.add(startPoint);
                int segAmount = segAmounts.get(i);
                if (segAmount >= 2) {
                    LatLng endPoint = points.get(i + 1);
                    double latDelta = (endPoint.latitude - startPoint.latitude) / segAmount;
                    double longDelta = (endPoint.longitude - startPoint.longitude) / segAmount;
                    for (int k = 0; k < segAmount; k++) {
                        interpolationPoints.add(new LatLng(startPoint.latitude + latDelta * k, startPoint.longitude + longDelta * k));
                    }
                }
            }
            interpolationPoints.add(points.get(points.size() - 1));
        }
        return interpolationPoints;
    }
}
