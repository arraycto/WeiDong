package cn.trunch.weidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;


public class SettingLoginActivity extends AppCompatActivity {

//    private ImageView settingLoginExitBtn;
    private EditText settingLoginAccount;
    private EditText settingLoginPassword;
    private Boolean accountFlag = false;
    private Boolean passwordFlag = false;
    private Button settingLoginGoBtn;
    private TextView settingLoginPhoneBtn;

    private LinearLayout settingLoginQQBtn;
    private LinearLayout settingLoginWeChatBtn;
    private LinearLayout settingLoginLinkedInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
//        settingLoginExitBtn= findViewById(R.id.settingLoginExitBtn);
        settingLoginAccount= findViewById(R.id.settingLoginAccount);
        settingLoginPassword= findViewById(R.id.settingLoginPassword);
        settingLoginGoBtn= findViewById(R.id.settingLoginGoBtn);
        settingLoginGoBtn.setEnabled(false);
        settingLoginPhoneBtn = findViewById(R.id.settingLoginPhoneBtn);
        settingLoginQQBtn= findViewById(R.id.settingLoginQQBtn);
        settingLoginWeChatBtn= findViewById(R.id.settingLoginWeChatBtn);
        settingLoginLinkedInBtn= findViewById(R.id.settingLoginLinkedInBtn);
    }

    private void initListener() {
//        settingLoginExitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        settingLoginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO login
            }
        });
        settingLoginPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPhoneIntent = new Intent(SettingLoginActivity.this, SettingPhoneActivity.class);
                startActivity(loginPhoneIntent);
            }
        });
        settingLoginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                accountFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    settingLoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    settingLoginGoBtn.setEnabled(true);
                } else {
                    settingLoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    settingLoginGoBtn.setEnabled(false);
                }
            }
        });

        settingLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    settingLoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    settingLoginGoBtn.setEnabled(true);
                } else {
                    settingLoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    settingLoginGoBtn.setEnabled(false);
                }
            }
        });
        settingLoginQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingLoginWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingLoginLinkedInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
    }
}
