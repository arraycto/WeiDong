package cn.trunch.weidong.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserSchool;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.util.Glide4Engine;
import cn.trunch.weidong.util.PathUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SettingIdentifyActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 1;

    private ImageView settingStudentBackBtn;
    private TextView settingStudentSubmit;
    private TextView settingStudentSchool;
    private TextView settingStudentMajor;
    private TextView settingStudentID;
    private TextView settingStudentTime;
    private ImageView settingStudentPhoto;
    private List<Uri> uriSelected;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_identify);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        init();
        initListener();
        initData();

    }

    private void initData() {
        //获取信息
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        new UniteApi(ApiUtil.USER_SCHOOL_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                List<UserSchool> uSchool = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<UserSchool>>() {
                }.getType());
                settingStudentSchool.setText(uSchool.get(0).getuSchool());
                settingStudentMajor.setText(uSchool.get(0).getuAcademy());
                settingStudentID.setText(uSchool.get(0).getuNumber());
                settingStudentTime.setText(String.valueOf(uSchool.get(0).getuRegYear()));
                if (uSchool.get(0).getuImg() != null) {
                    Glide.with(SettingIdentifyActivity.this)
                            .load(uSchool.get(0).getuImg())
                            .apply(bitmapTransform(new MultiTransformation<>(
                                    new CenterCrop(),
                                    new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                            )))
                            .into(settingStudentPhoto);
                }
            }

            @Override
            public void failure(Api api) {

            }
        });
    }

    private void init() {
        settingStudentBackBtn = findViewById(R.id.settingStudentBackBtn);
        settingStudentSubmit = findViewById(R.id.settingStudentSubmit);
        settingStudentSchool = findViewById(R.id.settingStudentSchool);
        settingStudentMajor = findViewById(R.id.settingStudentMajor);
        settingStudentID = findViewById(R.id.settingStudentID);
        settingStudentTime = findViewById(R.id.settingStudentTime);
        settingStudentPhoto = findViewById(R.id.settingStudentPhoto);
        img = "";
    }

    private void initListener() {
        settingStudentBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settingStudentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(SettingIdentifyActivity.this)
                        .choose(MimeType.ofImage()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(1) // 图片选择的最多数量
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                        .forResult(REQUEST_IMAGE_CODE); // 设置作为标记的请求码
            }
        });
        settingStudentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            uriSelected = Matisse.obtainResult(data);
            new UploadApi(ApiUtil.FILE_UPLOAD_HEAD, PathUtil.getFilePath(SettingIdentifyActivity.this, uriSelected.get(0))).upload(new ApiListener() {
                @Override
                public void success(Api api) {
                    UploadApi u = (UploadApi) api;
                    Gson gson = new Gson();
                    List<String> imgs = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<String>>() {
                    }.getType());
                    img = imgs.get(0);
                    DialogUIUtils.showToastCenter("证件上传成功");
                }

                @Override
                public void failure(Api api) {
                    DialogUIUtils.showToastCenter("上传失败，请重试");
                }
            });
            Glide.with(this)
                    .load(uriSelected.get(0))
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CenterCrop(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(settingStudentPhoto);
        }
    }

    private void update() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("school", settingStudentSchool.getText().toString());
        hm.put("academy", settingStudentMajor.getText().toString());
        hm.put("number", settingStudentID.getText().toString());
        hm.put("year", settingStudentTime.getText().toString());
        hm.put("img", img);
        new UniteApi(ApiUtil.USER_SCHOOL_UPDATE, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                DialogUIUtils.showToastCenter("认证已提交");
                finish();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络出现波动，请重试");
            }
        });
    }
}
