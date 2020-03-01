package cn.trunch.weidong.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SportMapActivity;
import cn.trunch.weidong.util.SPUtil;
import cn.trunch.weidong.util.TimeUtil;


public class SportTimeService extends Service {

    public static final String STEP_TIME_ACTION = "cn.trunch.broadcast.steptime";
    private boolean isTiming;
    private long timeS;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        SPUtil.init(this);
        timeS = SPUtil.getLong(TimeUtil.getCurrentDate() + "_SPORT_TIME", 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "sport";
            String channelName = "运动消息提醒";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        Intent intent = new Intent(this, SportMapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationBuilder.setContentTitle("微动正在运动计时")
                .setContentText("点击查看运动轨迹")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent);
        startForeground(1, notificationBuilder.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isTiming) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("timing", "run: ==:" + timeS);
                    timeS++;
                    sendBroadcast();
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setTiming(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        setTiming(false);
        SPUtil.putLong(TimeUtil.getCurrentDate() + "_SPORT_TIME", timeS);
    }

    private void sendBroadcast() {
        Intent intent = new Intent(SportTimeService.STEP_TIME_ACTION);
        intent.putExtra("timeS", timeS);
        sendBroadcast(intent);
    }

    public boolean isTiming() {
        return isTiming;
    }

    public void setTiming(boolean timing) {
        isTiming = timing;
    }
}
