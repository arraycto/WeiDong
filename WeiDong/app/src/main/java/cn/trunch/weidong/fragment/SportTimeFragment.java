package cn.trunch.weidong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.dou361.dialogui.DialogUIUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SettingAccountActivity;
import cn.trunch.weidong.activity.SportMapActivity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.service.SportTimeService;
import cn.trunch.weidong.util.SPUtil;
import cn.trunch.weidong.util.TimeUtil;
import cn.trunch.weidong.view.SportTimeView;

public class SportTimeFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private TextView sportTimeH;
    private TextView sportTimeM;
    private SportTimeView sportTimeCount;
    private ImageView sportTimeMap;
    private TextView sportTimeStartBtn;
    private TextView sportTimeStartInfo;

    private boolean timeCountFlag = false;
    private long timeH = 0;
    private long timeM = 0;
    private long timeS = 0;
    private IntentFilter intentFilter;
    private SportTimeReceiver sportTimeReceiver;
    private long oTimsS = 0;

    //垃圾
    LBSTraceClient mTraceClient;
    long serviceId = 212124;
    String uid = ApiUtil.USER_ID;
    boolean isNeedObjectStorage = false;
    boolean isStart = false;

    Trace mTrace;
    final OnTraceListener traceListener = new OnTraceListener() {

        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        @Override
        public void onStartTraceCallback(int i, String s) {
            Log.e("LBS", "Service start");
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            Log.e("LBS", "Service stop");
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            Log.e("LBS", "gather start");
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            Log.e("LBS", "gather stop");
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {

        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport_time, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        //垃圾
        mTraceClient = new LBSTraceClient(context);
        mTraceClient.setInterval(5, 10);
        mTrace = new Trace(serviceId, uid, isNeedObjectStorage);
        isStart = false;

        init();
        initListener();

        // 广播注册
        intentFilter = new IntentFilter();
        intentFilter.addAction(SportTimeService.STEP_TIME_ACTION);
        sportTimeReceiver = new SportTimeReceiver();
        context.registerReceiver(sportTimeReceiver, intentFilter);

        DialogUIUtils.init(context);

        return view;
    }

    class SportTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SportTimeService.STEP_TIME_ACTION.equals(intent.getAction())) {
                timeS = intent.getLongExtra("timeS", 0);
                sportTimeCount.setCurrentCount(60, (int) (timeS % 60));
                timeH = timeS / 3600;
                timeM = (timeS % 3600) / 60;
                sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(timeH));
                sportTimeM.setText(timeM == 0 ? "/" : String.valueOf(timeM));
            }
        }
    }

    private void init() {
        sportTimeH = view.findViewById(R.id.sportTimeH);
        sportTimeM = view.findViewById(R.id.sportTimeM);
        sportTimeCount = view.findViewById(R.id.sportTimeCount);
        SPUtil.init(context);
        timeS = SPUtil.getLong(TimeUtil.getCurrentDate() + "_SPORT_TIME", 0);
        sportTimeCount.setCurrentCount(60, (int) (timeS % 60));
        timeH = timeS / 3600;
        timeM = (timeS % 3600) / 60;
        sportTimeH.setText(timeH == 0 ? "/" : String.valueOf(timeS / 3600));
        sportTimeM.setText(timeM == 0 ? "/" : String.valueOf((timeS % 3600) / 60));
        sportTimeMap = view.findViewById(R.id.sportTimeMap);
        sportTimeStartBtn = view.findViewById(R.id.sportTimeStartBtn);
        sportTimeStartInfo = view.findViewById(R.id.sportTimeStartInfo);


        AndPermission.with(SportTimeFragment.this)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        DialogUIUtils.showToastCenter("请前往设置打开位置权限");
                    }
                })
                .start();
    }

    private void initListener() {
        sportTimeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportMapActivity.class);
                intent.putExtra("isTiming", timeCountFlag);
                startActivity(intent);
            }
        });
        sportTimeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCountFlag = !timeCountFlag;
                if (timeCountFlag) {
                    mTraceClient.startTrace(mTrace, traceListener);
                    mTraceClient.startGather(traceListener);
                } else {
                    mTraceClient.stopGather(traceListener);
                    mTraceClient.stopTrace(mTrace, traceListener);
                }

                if (timeCountFlag) {
                    oTimsS = timeS;
                    Intent intent = new Intent(activity, SportTimeService.class);
                    activity.startService(intent);
                    sportTimeStartBtn.setSelected(true);
                    sportTimeStartBtn.setTextSize(14);
                    sportTimeStartBtn.setText("点击暂停");
                    sportTimeStartBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                    sportTimeStartInfo.setSelected(true);
                    sportTimeStartInfo.setTextColor(getResources().getColor(R.color.colorLight));
                    sportTimeStartInfo.setText("正在计时");

                } else {
                    Intent intent = new Intent(activity, SportTimeService.class);
                    activity.stopService(intent);
                    sportTimeStartBtn.setSelected(false);
                    sportTimeStartBtn.setTextSize(24);
                    sportTimeStartBtn.setText("GO");
                    sportTimeStartBtn.setTextColor(getResources().getColor(R.color.colorLight));
                    sportTimeStartInfo.setSelected(false);
                    sportTimeStartInfo.setTextColor(getResources().getColor(R.color.colorDefaultText));
                    sportTimeStartInfo.setText("暂停计时");
                }

                if (!timeCountFlag) {
                    //获取运动时间
                    if (timeS - oTimsS > ApiUtil.thresholdS) {
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("token", ApiUtil.USER_TOKEN);
                        hm.put("time", String.valueOf(timeS - oTimsS));
                        new UniteApi(ApiUtil.EX_ADD, hm).post(new ApiListener() {
                            @Override
                            public void success(Api api) {
                                DialogUIUtils.showToastCenter("数据上传成功");
                            }

                            @Override
                            public void failure(Api api) {
                                DialogUIUtils.showToastCenter("数据上传失败，请检查网络设置");
                            }
                        });
                    } else {
                        DialogUIUtils.showToastCenter("本次运动时间低于" + ApiUtil.thresholdS + "S，记录不保存");
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(activity, SportTimeService.class);
        activity.stopService(intent);
        context.unregisterReceiver(sportTimeReceiver);
    }
}
