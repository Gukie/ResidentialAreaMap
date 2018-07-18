package lokia.com;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import lokia.com.utils.BaseApplication;
import lokia.com.utils.LocationHelper;
import lokia.com.utils.ToastUtils;

public class BaiduMapActivity2 extends Activity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private double mLatitude, mLongitude;
    private MarkerOptions mMarkerOptions;
    private Button latLongBtn = null;
    private LocationManager locationManager;
    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_guide_main);
        latLongBtn = findViewById(R.id.latlongBtn);
//        LocationHelper.getInstance().starLocation();


        mMapView = (MapView) findViewById(R.id.bmapview);
        BaseApplication.setmContext(getApplicationContext());
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        initLocation();
//        MarkerOptions mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));
//        mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
//        final LatLng ll = new LatLng(Double.valueOf(mLatitude), Double.valueOf(mLongitude));
//        mMarkerOptions.position(ll);
//        mBaiduMap.addOverlay(mMarkerOptions);
//
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(ll).zoom(18.0f);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

//        mBaiduMap.setMyLocationData();
//        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
////                double lat = LocationHelper.getInstance().getLatitude();
////                double lng = LocationHelper.getInstance().getLongitude();
////
////                String msg = lat+","+lng;
////                ToastUtils.showText(msg);
////                LatLng latLng = new LatLng(mLatitude,mLongitude);
////
////                loadMap(latLng);
//
//            }
//        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                LocationHelper.getInstance().setMapStatus(arg0);
//				arg0.target.latitude  // 纬度
//				arg0.target.longitude // 经度
//                latlngToAddress(arg0.target);
//                loadMap(arg0.target);
//                LatLng latLng = new LatLng(mLatitude,mLongitude);

//                loadMap(arg0.target);
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

//        findViewById(R.id.iv_control_map_back).setOnClickListener(this);
//        LocationHelper.getInstance().starLocation();

    }

    private void initLocation() {

//        mLatitude = LocationHelper.getInstance().getLatitude();
//        mLongitude = LocationHelper.getInstance().getLongitude();


        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceName);
        String provider = LocationManager.GPS_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);
//        Location location = locationManager.getLastKnownLocation(provider);
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("BaiduMapActivity", "fetchLocation: permisstion denied");
            latLongBtn.setText("fetchLocation: permisstion denied");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_REQUEST);
            }
//            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        mLatitude = location.getLatitude();
//        mLongitude = location.getLongitude();

        loadMap(latLng);

    }

    private void loadMap(LatLng lanlng) {



// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(Criteria.ACCURACY_COARSE)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(lanlng.latitude)
                .longitude(lanlng.longitude).build();

// 设置定位数据
        mBaiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marker);
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);

        String msg = lanlng.latitude+","+lanlng.longitude;
        latLongBtn.setText(msg);

    }

    // 百度地图通过坐标获取地址，（ 要签名打包才能得到地址）
    private void latlngToAddress(LatLng latlng) {
        GeoCoder geoCoder = GeoCoder.newInstance();
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));

        // 设置地址或经纬度反编译后的监听,这里有两个回调方法
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showText("找不到该地址!");
                } else {
                    ToastUtils.showText(result.getAddress());
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                ToastUtils.showText(result.getAddress());

            }
        });

    }

    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_control_map_back:
//                finish();
//                break;
        }

    }

}
