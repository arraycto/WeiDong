package cn.trunch.weidong.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;

public class SportHistorySeeActivity extends AppCompatActivity {
    private FloatingActionButton closeBtn;
    MapView mapView;
    BaiduMap baiduMap;
    public LBSTraceClient mTraceClient;
    LatLng l2;
    long serviceId = 212124;
    String uid = "Trace_Demo";
    String st = "2019-04-25 12:00:00";
    String et= "2019-04-25 12:00:00";
    int pageSize = 3000;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history_map);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        this.InitLocation();
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        st = bundle.getString("sTime");
        et = bundle.getString("eTime");
//        et=bundle.getString("eTime");
        mTraceClient = new LBSTraceClient(getApplicationContext());
        this.queryHistory();
    }

    public void InitLocation() {
        closeBtn = findViewById(R.id.sportHistoryMapClose);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // TODO 获取地图控件
        mapView = findViewById(R.id.sportHistoryMapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
    }

    public void queryHistory() {
        long sTime = TimeUtil.s2l(st);
        long eTime = TimeUtil.s2l(et);
        HistoryTrackRequest hrt = new HistoryTrackRequest(1, serviceId, uid);
        hrt.setProcessed(true);
        hrt.setPageIndex(pageIndex);
        hrt.setPageSize(pageSize);
        hrt.setStartTime(sTime);
        hrt.setEndTime(eTime);
        int a = 20;
        OnTrackListener onTrackListener = new OnTrackListener() {
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

        };

        mTraceClient.queryHistoryTrack(hrt, onTrackListener);


    }

    private void drawHistoryTrack(List<TrackPoint> list_tp, Point startPoint,
                                  Point endPoint) {

        showMarker(startPoint, 0);
        showMarker(endPoint, 1);
        if (list_tp.size() <= 3)
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
        MapStatusUpdate mapupdate = MapStatusUpdateFactory.newLatLngZoom(l2, 18);
        baiduMap.animateMapStatus(mapupdate);
        baiduMap.setMyLocationEnabled(false);
    }

    private void showMarker(Point point, int i) {

        // 定义Maker坐标点
        LatLng latlng = new LatLng(point.getLocation().getLatitude(), point
                .getLocation().getLongitude());
        // 构建Marker图标
        BitmapDescriptor bitmap;
        if (i == 0) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
            MyLocationData locationData = new MyLocationData.Builder()
                    .longitude(point.getLocation().getLongitude())
                    .latitude(point.getLocation().getLatitude())
                    .direction(point.getDirection())
                    .accuracy((float) point.getRadius())
                    .build();
            baiduMap.setMyLocationData(locationData);
            l2 = new LatLng(point.getLocation().getLatitude(), point.getLocation().getLongitude());
        } else if (i == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_end);
            ;
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        }

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latlng).icon(
                bitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("daladala", "-----------destory()---------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("daladala", "-----------start()---------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("daladala", "-----------pause()---------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("daladala", "-----------resume()---------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("daladala", "-----------stop()---------");
    }
}
