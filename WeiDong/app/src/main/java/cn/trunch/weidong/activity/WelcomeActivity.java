package cn.trunch.weidong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.lzy.ninegrid.NineGridView;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.OkHttpUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class WelcomeActivity extends AppCompatActivity {

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //根据状态栏颜色来决定状态栏文字用黑色还是白色
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        //初始化九图
        NineGridView.setImageLoader(new GlideImageLoader());
        //初始化通讯
        OkHttpUtil.init();
        //初始化地图
        SDKInitializer.initialize(getApplicationContext());
        //初始化弹框
        DialogUIUtils.init(this);

        init();
        initListener();
    }

    private void init() {
        appName = findViewById(R.id.appName);
        appName.setTypeface(appName.getTypeface(), Typeface.ITALIC);
    }

    private void initListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("wd", MODE_PRIVATE);
                        ApiUtil.USER_ID = preferences.getString("userId", "");
                        ApiUtil.USER_TOKEN = preferences.getString("userToken", "");
                        ApiUtil.USER_AVATAR = preferences.getString("userAvatar", "");
                        ApiUtil.USER_NAME = preferences.getString("userName", "未知用户");
                        boolean isPermit = preferences.getBoolean("isPermit", false);
                        if (isPermit) {
                            startActivity(new Intent(
                                    WelcomeActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(
                                    WelcomeActivity.this, SettingPhoneActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 九图 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)//
                    .placeholder(R.drawable.bg_image_default)//
                    .error(R.drawable.ic_default_image)//
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CenterCrop(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
