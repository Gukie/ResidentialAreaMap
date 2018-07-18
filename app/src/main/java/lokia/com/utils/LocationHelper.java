package lokia.com.utils;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;

public class LocationHelper {
    private static volatile LocationHelper mHelper;
    private LocationClient locationClient;
    private boolean isStart = false;

    private double latitude;
    private double longitude;
    private String mAddress;
    private String city;
    private String cityCode;;
    private boolean isLocation = false;
    private MapStatus mapStatus;

    private LocationHelper() {
        locationClient = new LocationClient(BaseApplication.mContext);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation arg0) {
                String address = arg0.getAddrStr();
                if (!TextUtils.isEmpty(address)) {
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    mAddress = address;
                    city = arg0.getCity();
                    cityCode = arg0.getCityCode();
                    isLocation = true;
                }
                Log.i("LocationHeleper", "latitude :" + latitude);
                Log.i("LocationHeleper", "longitude :" + longitude);
                Log.i("LocationHeleper", "mAddress :" + mAddress);
            }
        });
        LocationClientOption option = new LocationClientOption();
        // option.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到地址
        option.setOpenGps(true);// 打开GPS
        option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN * 60);// 多长时间进行一次请求
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度
        locationClient.setLocOption(option);// 使用设置
    }

    public static LocationHelper getInstance() {
        if (mHelper == null) {
            mHelper = new LocationHelper();
        }
        return mHelper;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public synchronized void starLocation() {
        if (!isStart) {
            locationClient.start();
            isStart = true;
        }
    }

    public synchronized void stopLocation() {
        if (locationClient != null)
            locationClient.stop();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setMapStatus(MapStatus mapStatus) {
        this.mapStatus = mapStatus;
    }
}
