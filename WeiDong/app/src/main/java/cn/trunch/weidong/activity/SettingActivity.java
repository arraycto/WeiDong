package cn.trunch.weidong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.util.StatusBarUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SettingActivity extends AppCompatActivity {

    private ImageView settingBackBtn;
    private ImageView settingSetupBtn1;
    private ImageView settingAvatar;
    private TextView settingNickname;
//    private RelativeLayout settingHomeGoBtn;

    private LinearLayout settingReleaseBtn;
    private LinearLayout settingRecordBtn;
    private LinearLayout settingFollowBtn;
    private LinearLayout settingCollectBtn;
    private RelativeLayout settingIdentifyBtn;
    private RelativeLayout settingAccountBtn;
    private RelativeLayout settingWalletBtn;
    private RelativeLayout settingFansBtn;
    private RelativeLayout settingSetupBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        StatusBarUtil.setStatusBarMode(this, false, R.color.colorDarkText);

        init();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(settingAvatar);
        settingNickname.setText(ApiUtil.USER_NAME);
    }

    private void init() {
        settingBackBtn = findViewById(R.id.settingBackBtn);
        settingSetupBtn1 = findViewById(R.id.settingSetupBtn1);
        settingAvatar = findViewById(R.id.settingAvatar);
        settingNickname = findViewById(R.id.settingNickname);
//        settingHomeGoBtn = findViewById(R.id.settingHomeGoBtn);
        settingReleaseBtn = findViewById(R.id.settingReleaseBtn);
        settingRecordBtn = findViewById(R.id.settingRecordBtn);
        settingFollowBtn = findViewById(R.id.settingFollowBtn);
        settingCollectBtn = findViewById(R.id.settingCollectBtn);
        settingIdentifyBtn = findViewById(R.id.settingIdentifyBtn);
        settingAccountBtn = findViewById(R.id.settingAccountBtn);
        settingWalletBtn = findViewById(R.id.settingWalletBtn);
        settingFansBtn = findViewById(R.id.settingFansBtn);
        settingSetupBtn2 = findViewById(R.id.settingSetupBtn2);
    }

    private void initListener() {
        settingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        settingSetupBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingSetupActivity.class);
                startActivity(intent);
            }
        });
        settingAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingAccountActivity.class);
                startActivity(intent);
            }
        });
//        settingHomeGoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, SettingAccountActivity.class);
//                startActivity(intent);
//            }
//        });
        settingReleaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SportDiaryActivity.class);
                startActivity(intent);
            }
        });
        settingRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SportHistoryActivity.class);
                startActivity(intent);
            }
        });
        settingFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EmptyActivity.class);
                startActivity(intent);
            }
        });
        settingCollectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EmptyActivity.class);
                startActivity(intent);
            }
        });
        settingIdentifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingIdentifyActivity.class);
                startActivity(intent);
            }
        });
        settingAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingAccountActivity.class);
                startActivity(intent);
            }
        });
        settingWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, WebActivity.class);
                intent.putExtra("title", "我的订单");
                intent.putExtra("href", "http://47.102.200.22/#/my_order_list?token=" + ApiUtil.USER_TOKEN);
                startActivity(intent);
//                Intent intent = new Intent(SettingActivity.this, EmptyActivity.class);
//                startActivity(intent);
            }
        });
        settingFansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EmptyActivity.class);
                startActivity(intent);
            }
        });
        settingSetupBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingSetupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
