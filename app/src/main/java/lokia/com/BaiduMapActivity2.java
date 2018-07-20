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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
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
    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 1;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST + 2;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;


    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_guide_main);
        latLongBtn = findViewById(R.id.latlongBtn);


        mMapView = (MapView) findViewById(R.id.bmapview);
        BaseApplication.setmContext(getApplicationContext());
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        initLocationClient();
        initLocation();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                Log.d("location", "onMapLoaded: <"+mLatitude+","+mLongitude+">");
                loadMap(latLng);
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
//                LocationHelper.getInstance().setMapStatus(arg0);
//				arg0.target.latitude  // 纬度
//				arg0.target.longitude // 经度
//                latlngToAddress(arg0.target);
//                loadMap(arg0.target);
//                LatLng latLng = new LatLng(mLatitude,mLongitude);
//                loadMap(arg0.target);

//                LatLng latLng = new LatLng(mLatitude,mLongitude);
//                loadMap(latLng);
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

        locationClient.start();
        locationClient.requestLocation();
    }

    private void initLocationClient() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener(){

            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.d("onMapLoaded", "onMapLoaded: <"+location.getLatitude()+","+location.getLongitude()+">");
                loadMap(new LatLng(location.getLatitude(),location.getLongitude()));
            }
        });
        initLocationOption();
    }

    private void initLocation() {
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceName);
        String provider = LocationManager.GPS_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);
//        Location location = locationManager.getLastKnownLocation(provider);
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("BaiduMapActivity", "fetchLocation: permisstion denied");
            latLongBtn.setText("fetchLocation: permisstion denied");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            }
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location == null){
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        loadMap(latLng);
        mLongitude = location.getLongitude();
        mLatitude = location.getLatitude();
        Log.d("location", "initLocation: lat:"+mLatitude+", longtitude:"+mLongitude);

    }

    private void initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1001;//1秒定位一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setOpenAutoNotifyMode();

        locationClient.setLocOption(option);
    }
    private void loadMap(LatLng lanlng) {


// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(Criteria.ACCURACY_HIGH)
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

        String msg = lanlng.latitude + "," + lanlng.longitude;
        latLongBtn.setText(msg);

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

    /**
     * 单击事件,开始定位,如果不关闭,会一直刷新定位信息(在服务中执行的)
     * 开发者定位场景如果是单次定位的场景，在收到定位结果之后直接调用stop函数即可。
     * 如果stop之后仍然想进行定位，可以再次start等待定位结果回调即可。
     */
    public void onStartLocation(View view){
        locationClient.start();
    }

}
