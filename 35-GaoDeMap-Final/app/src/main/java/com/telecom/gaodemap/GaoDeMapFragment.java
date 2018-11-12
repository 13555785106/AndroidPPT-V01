package com.telecom.gaodemap;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.math.MathUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GaoDeMapFragment extends Fragment {

    private MapView mapView;
    private Button mDriveCarButton;
    public static GaoDeMapFragment newInstance() {
        return new GaoDeMapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gao_de_map, container, false);
        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mDriveCarButton = v.findViewById(R.id.drive_car_button);
        final AMap aMap = mapView.getMap();
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);//指南针
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
        final LatLng syzPos = new LatLng(41.793762, 123.396797);
        //设置中心点及缩放倍数
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(syzPos, 16.5f));
        //绘制圆
        Circle circle = aMap.addCircle(new CircleOptions().center(syzPos)
                .radius(500).strokeColor(Color.GRAY)
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(8));
        PoiSearch.Query query = new PoiSearch.Query(null, "050113", "210102");
        PoiSearch poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(syzPos.latitude, syzPos.longitude), 500));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                System.out.println("pageCount=" + poiResult.getPageCount());
                for (PoiItem poiItem : poiResult.getPois()) {
                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView textView = new TextView(getActivity());
                    textView.setText(poiItem.getTitle());
                    textView.setBackgroundColor(Color.parseColor("#ff3FE2C5"));
                    linearLayout.addView(textView);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(R.drawable.red_marker_bubble);
                    linearLayout.addView(imageView);

                    MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromView(linearLayout))
                            .title(poiItem.getTitle())
                            .snippet(poiItem.getTel())
                            .position(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()))
                            .period(10)
                            .draggable(true);
                    Marker marker = aMap.addMarker(markerOption);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                System.out.println("+++++++++++++++++++++");
            }
        });
        poiSearch.searchPOIAsyn();
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                return true;
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println(latLng);
            }
        });

        mDriveCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LatLng> points = new ArrayList<>();
                points.add(new LatLng(41.78957388996561,123.3959738910198));
                points.add(new LatLng(41.79297554070723,123.39857429265977));
                points.add(new LatLng(41.79395640646352,123.39623004198076));
                points.add(new LatLng(41.79732083193482,123.3989377319813));
                points.add(new LatLng(41.79636101384754,123.40114653110504));
                points.add(new LatLng(41.79468829651509,123.39990600943565));
                points.add(new LatLng(41.79420636985255,123.40096548199654));
                points.add(new LatLng(41.792534596164806,123.3996230363846));
                points.add(new LatLng(41.792955543287974,123.39853942394258));
                points.add(new LatLng(41.78957388996561,123.3959738910198));
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syzPos,16.5f));
                final SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
                int [] carDrawableIds = new int[]{R.drawable.car_0,R.drawable.car_1,R.drawable.car_2,R.drawable.car_3,R.drawable.car_4};

                smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(carDrawableIds[(int)(Math.random()*5)]));
                LatLng drivePoint = points.get(0);
                Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
                points.set(pair.first, drivePoint);
                List<LatLng> subList = points.subList(pair.first, points.size());
                smoothMarker.setPoints(subList);
                smoothMarker.setTotalDuration(9);
                smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
                    @Override
                    public void move(double v) {
                        System.out.println(v);
                        if(v==0)
                            smoothMarker.destroy();
                    }
                });
                smoothMarker.startSmoothMove();

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
