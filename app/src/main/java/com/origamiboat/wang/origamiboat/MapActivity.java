package com.origamiboat.wang.origamiboat; /**
 * Created by wang on 2017/4/23.
 */


import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.origamiboat.wang.origamiboat.R;

import java.io.IOException;

public class MapActivity extends Activity {
    private MapView mapView;
    private AMap aMap;
    private LinearLayout.LayoutParams mParams;
    private RelativeLayout mContainerLayout;
    public  static  final  String CITI_KEY="city";
    public  static  final int SHANGHAI=0;
    public  static  final int BEIJING=1;
    public  static  final int GUANGZHOU=2;
    double output1;
    double output2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mContainerLayout = (RelativeLayout) findViewById(R.id.activity_main);
        AMapOptions aOptions = new AMapOptions();


        output1=1.111111;
        output2=106.295284;
        Bundle extras = getIntent().getExtras();
        output1 = Double.parseDouble(extras.getString("Latitude"));
        output2 = Double.parseDouble(extras.getString("Longitude"));
        LatLng center= new LatLng(output1,output2);// 照片信息的经纬度

        aOptions.camera(new CameraPosition(center, 10f, 0, 0));
        mapView = new MapView(this, aOptions);
        mapView.onCreate(savedInstanceState);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.addView(mapView, mParams);
        init();

        //绘制点标记
        Marker marker1 = aMap.addMarker(new MarkerOptions().position(center).title("重庆").snippet("DefaultMarker"));
        Marker marker2 = aMap.addMarker(new MarkerOptions().position(center).title("重庆").snippet("DefaultMarker"));
        Marker marker3 = aMap.addMarker(new MarkerOptions().position(center).title("重庆").snippet("DefaultMarker"));

    }
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
