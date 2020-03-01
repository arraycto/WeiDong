package cn.trunch.weidong.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.Point;
import com.dou361.dialogui.DialogUIUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.service.SportTimeService;
import cn.trunch.weidong.util.StatusBarUtil;

public class SportMapActivity extends AppCompatActivity {

    //------------------------
    private TextView sportMapSpeed;
    private TextView sportMapTime;
    private TextView sportMapDistance;
    private FloatingActionButton sportMapClose;
    private TextView sportMapTimeInfo;

    private long timeH = 0;
    private long timeM = 0;
    private long timeS = 0;
    private IntentFilter intentFilter;
    private SportTimeReceiver sportTimeReceiver;
    //------------------------
    MapView mapView;
    BaiduMap baiduMap;
    LocationClient locationClient;
    LBSTraceClient mTraceClient;
    LocationClientOption option;
    long serviceId = 212124;
    long startTime = 0;
    long endTime = 0;
    String uid = ApiUtil.USER_ID;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK
    // v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    int pageSize = 5000;
    int pageIndex = 1;
    boolean queryHis = true;
    boolean isQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_map);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        this.InitLocation();
        mTraceClient = new LBSTraceClient(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        DateFormat df = (DateFormat) sdf;
        Date parse;
        try {
            parse = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            parse = new Date();
        }
        startTime = parse.getTime() / 1000;
        // 控件操作
        sportMapSpeed = findViewById(R.id.sportMapSpeed);
        sportMapTime = findViewById(R.id.sportMapTime);
        sportMapDistance = findViewById(R.id.sportMapDistance);
        sportMapClose = findViewById(R.id.sportMapClose);
        sportMapTimeInfo = findViewById(R.id.sportMapTimeInfo);
        boolean isTiming = getIntent().getBooleanExtra("isTiming", false);
        if (isTiming) {
            sportMapTimeInfo.setSelected(true);
            sportMapTimeInfo.setTextColor(getResources().getColor(R.color.colorLight));
            sportMapTimeInfo.setText("正在计时");
        } else {
            sportMapTimeInfo.setSelected(false);
            sportMapTimeInfo.setTextColor(getResources().getColor(R.color.colorDefaultText));
            sportMapTimeInfo.setText("暂停计时");
        }
        sportMapClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 广播注册
        intentFilter = new IntentFilter();
        intentFilter.addAction(SportTimeService.STEP_TIME_ACTION);
        sportTimeReceiver = new SportTimeReceiver();
        registerReceiver(sportTimeReceiver, intentFilter);
    }

    public void InitLocation() {
        // TODO 获取地图控件
        mapView = findViewById(R.id.sportMapView);
        baiduMap = mapView.getMap();
        // TODO 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext());
        // TODO 注册监听
        this.setLocationOption();
        locationClient.registerLocationListener(myListener);
        locationClient.start();
    }

    public BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locationData);

            LatLng l2;
            l2 = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate mapupdate = MapStatusUpdateFactory.newLatLngZoom(l2, 18);
            baiduMap.animateMapStatus(mapupdate);
        }

    };

    private void setLocationOption() {
        option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(2000);
        option.setIsNeedAddress(false);
        option.setLocationNotify(true);
        option.setNeedDeviceDirect(false);
        option.setOpenAutoNotifyMode();
        locationClient.setLocOption(option);
    }

    public void queryHistory() {
        endTime = (new Date()).getTime() / 1000;

        HistoryTrackRequest hrt = new HistoryTrackRequest(1, serviceId, uid);
        hrt.setProcessed(true);
        hrt.setPageIndex(pageIndex);
        hrt.setPageSize(pageSize);
        hrt.setStartTime(startTime);
        hrt.setEndTime(endTime);
        mTraceClient.queryHistoryTrack(hrt, new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                super.onHistoryTrackCallback(response);
                if (response.getSize() > 0) {//如果当前日期范围内有数据点，则准备绘制
                    List<TrackPoint> tp = response.getTrackPoints();// 所有点的坐标信息数据集
                    Point startPoint = response.getStartPoint();// 起点的坐标信息
                    Point endPoint = response.getEndPoint();// 终点的坐标信息
                    drawHistoryTrack(tp, startPoint, endPoint);// 绘制折线
                } else {
                }
            }
        });

    }

    private void drawHistoryTrack(List<TrackPoint> list_tp, Point startPoint,
                                  Point endPoint) {
        BDLocation location=new BDLocation();
        location.setLatitude(endPoint.getLocation().getLatitude());
        location.setLongitude(endPoint.getLocation().getLongitude());
        location.setRadius((float) endPoint.getRadius());
        myListener.onReceiveLocation(location);
        showMarker(startPoint, 0);
        showMarker(endPoint, 1);
        if(list_tp.size()<=3)
            return;
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < list_tp.size(); i++) {
            points.add(new LatLng(list_tp.get(i).getLocation().getLatitude(),
                    list_tp.get(i).getLocation().getLongitude()));
        }
        // 构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        // 添加到地图
        baiduMap.addOverlay(ooPolyline);
    }

    private void showMarker(Point point, int i) {
        // 定义Maker坐标点
        LatLng latlng = new LatLng(point.getLocation().getLatitude(), point
                .getLocation().getLongitude());
        // 构建Marker图标
        BitmapDescriptor bitmap;
        if (i == 0) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_start);
        } else if (i == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_road);
            ;
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_road);
        }

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latlng).icon(
                bitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        queryHis = false;
        isQuery = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        isQuery = true;
        startQ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        queryHis = false;
        isQuery = false;
        //注销广播
        unregisterReceiver(sportTimeReceiver);
    }


    public void startQ() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isQuery) {
                    try {
                        queryHistory();
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    class SportTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SportTimeService.STEP_TIME_ACTION.equals(intent.getAction())) {
                timeS = intent.getLongExtra("timeS", 0);
                timeH = timeS / 3600;
                timeM = (timeS % 3600) / 60;
                sportMapTime.setText(getFormatDate());
            }
        }
    }

    private String getFormatDate() {
        String timeStringS;
        String timeStringM;
        String timeStringH;
        if (timeM < 10) {
            timeStringM = "0" + String.valueOf(timeM);
        } else {
            timeStringM = String.valueOf(timeM);
        }
        if (timeS % 60 < 10) {
            timeStringS = "0" + String.valueOf(timeS % 60);
        } else {
            timeStringS = String.valueOf(timeS % 60);
        }
        if (timeH > 0 && timeH < 10) {
            timeStringH = "0" + String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else if (timeH >= 10) {
            timeStringH = String.valueOf(timeH);
            return timeStringH + ":" + timeStringM + ":" + timeStringS;
        } else {
            return timeStringM + ":" + timeStringS;
        }
    }
}
