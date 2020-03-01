package cn.trunch.weidong.activity;

import android.app.Dialog;
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


import com.dou361.dialogui.DialogUIUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;


public class SettingPhoneActivity extends AppCompatActivity {

//    private ImageView settingPhoneExitBtn;
    private EditText settingPhoneInput;
    private Button settingPhoneGoBtn;

    private LinearLayout settingPhoneQQBtn;
    private LinearLayout settingPhoneWeChatBtn;
    private LinearLayout settingPhoneLinkedInBtn;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_phone);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);

    }

    private void init() {
//        settingPhoneExitBtn = findViewById(R.id.settingPhoneExitBtn);
        settingPhoneInput = findViewById(R.id.settingPhoneInput);
        settingPhoneGoBtn = findViewById(R.id.settingPhoneGoBtn);
        settingPhoneGoBtn.setEnabled(false);
        settingPhoneQQBtn = findViewById(R.id.settingPhoneQQBtn);
        settingPhoneWeChatBtn = findViewById(R.id.settingPhoneWeChatBtn);
        settingPhoneLinkedInBtn = findViewById(R.id.settingPhoneLinkedInBtn);
    }

    private void initListener() {
//        settingPhoneExitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        settingPhoneGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DialogUIUtils.showLoading(SettingPhoneActivity.this,
                        "验证中...", false, false,
                        false, false)
                        .show();
                HashMap<String, String> hash = new HashMap<>();
                hash.put("phone", settingPhoneInput.getText().toString());
                new UniteApi(ApiUtil.USER_SENDSMS, hash).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        dialog.dismiss();
                        Intent settingCodeIntent = new Intent(SettingPhoneActivity.this, SettingCodeActivity.class);
                        settingCodeIntent.putExtra("phone", settingPhoneInput.getText().toString());
                        startActivity(settingCodeIntent);
                    }

                    @Override
                    public void failure(Api api) {
                        UniteApi u = (UniteApi) api;
                        dialog.dismiss();
                        String jsonCode = u.getJsonCode();
                        if ("10007".equals(jsonCode)) {
                            DialogUIUtils.showToastCenter("验证码获取频繁");
                        } else {
                            DialogUIUtils.showToastCenter("网络异常");
                        }
                    }
                });

            }
        });
        settingPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isMobile(s.toString())) {
                    settingPhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    settingPhoneGoBtn.setEnabled(true);
                } else {
                    settingPhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    settingPhoneGoBtn.setEnabled(false);
                }
            }
        });
        settingPhoneQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingPhoneWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        settingPhoneLinkedInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
    }

    public static boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        p = Pattern.compile(s2);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}
