package cn.trunch.weidong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dou361.dialogui.DialogUIUtils;
import com.suke.widget.SwitchButton;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;

public class SettingSetupActivity extends AppCompatActivity {

    private ImageView settingSetupBackBtn;
    private SwitchButton settingSetupNotify;
    private Button settingAccountExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_setup);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        settingSetupBackBtn = findViewById(R.id.settingSetupBackBtn);
        settingSetupNotify = findViewById(R.id.settingSetupNotify);
        settingAccountExit = findViewById(R.id.settingAccountExit);
    }

    private void initListener() {
        settingSetupBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settingSetupNotify.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    DialogUIUtils.showToastCenter("系统通知已打开");
                } else {
                    DialogUIUtils.showToastCenter("系统通知已关闭");
                }
            }
        });
        settingAccountExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                editor.putBoolean("isPermit", false);
                editor.apply();
                Intent intent = new Intent(SettingSetupActivity.this, SettingPhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
