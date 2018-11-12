package com.telecom.baidumap;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaiDuMapFragment extends Fragment {
    private final LatLng syzPos = new LatLng(41.799887057920124, 123.4028060226653);
    private TextureMapView mMapView;
    private Button mSearchRestaurantButton;

    public static BaiDuMapFragment newInstance() {
        return new BaiDuMapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_baidu_du_map, container, false);
        mMapView = v.findViewById(R.id.mapView);
        mSearchRestaurantButton = v.findViewById(R.id.search_restaurant_button);

        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mMapView.getMap().getUiSettings().setCompassEnabled(true);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(syzPos);
        builder.zoom(17f);
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(builder.build());
        mMapView.getMap().setMapStatus(msu);
        mMapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(BaiDuMapFragment.class.getName(), latLng.latitude + "," + latLng.longitude);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        mMapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TextView textView = new TextView(getActivity());
                textView.setBackgroundColor(Color.YELLOW);
                textView.setText(marker.getTitle());
                InfoWindow infoWindow = new InfoWindow(textView, marker.getPosition(), -128);
                mMapView.getMap().showInfoWindow(infoWindow);
                return true;
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

        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(41.79646545493021, 123.40029076723737));
        points.add(new LatLng(41.795517591290896, 123.4025275479572));
        points.add(new LatLng(41.79880480261331, 123.40507873560554));
        points.add(new LatLng(41.79834097327307, 123.40610280388692));
        OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points);
        Polyline mPolyline = (Polyline) mMapView.getMap().addOverlay(ooPolyline);
        mPolyline.setDottedLine(true);

        mSearchRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PoiSearch mPoiSearch = PoiSearch.newInstance();
                mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                    @Override
                    public void onGetPoiResult(PoiResult poiResult) {
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red_marker_bubble);
                        if (poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                            System.out.println("检索失败！");
                        } else {
                            for (PoiInfo poiInfo : poiResult.getAllPoi()) {
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
                        System.out.println(poiDetailResult);
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
                /*LatLng southwest = new LatLng(41.802461541564426,123.39686822324434);
                LatLng northeast = new LatLng(41.7971713016566,123.40881568652705);
                LatLngBounds searchbound = new LatLngBounds.Builder()
                        .include(southwest).include(northeast)
                        .build();
                mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(searchbound)
                        .pageCapacity(10)
                        .pageNum(0)
                        .keyword("餐厅"));*/
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
