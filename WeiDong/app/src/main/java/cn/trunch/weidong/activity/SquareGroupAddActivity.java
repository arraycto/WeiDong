package cn.trunch.weidong.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.opengl.ETC1;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
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

public class SquareGroupAddActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 1;
    private ImageView groupAddBackBtn;
    private ImageView groupAddAvatar;
    private EditText groupAddName;
    private EditText groupAddDesc;
    private EditText groupAddCity;
    private Button groupAddOkBtn;
    private List<Uri> uriSelected;
    private String head="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_group_add);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        groupAddBackBtn = findViewById(R.id.groupAddBackBtn);
        groupAddAvatar = findViewById(R.id.groupAddAvatar);
        groupAddName = findViewById(R.id.groupAddName);
        groupAddDesc = findViewById(R.id.groupAddDesc);
        groupAddCity = findViewById(R.id.groupAddCity);
        groupAddOkBtn = findViewById(R.id.groupAddOkBtn);
    }

    private void initListener() {
        groupAddBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(SquareGroupAddActivity.this)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Matisse.from(SquareGroupAddActivity.this)
                                        .choose(MimeType.ofImage()) // 选择 mime 的类型
                                        .countable(true)
                                        .maxSelectable(1) // 图片选择的最多数量
                                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f) // 缩略图的比例
                                        .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                                        .forResult(REQUEST_IMAGE_CODE); // 设置作为标记的请求码
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                DialogUIUtils.showToastCenter("请前往设置打开权限");
                            }
                        })
                        .start();
            }
        });
        groupAddOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMatchRex()) {
                    HashMap<String,String> hm=new HashMap<>();
                    hm.put("token",ApiUtil.USER_TOKEN);
                    hm.put("name",groupAddName.getText().toString());
                    hm.put("desc",groupAddDesc.getText().toString());
                    hm.put("loc",groupAddCity.getText().toString());
                    hm.put("headImg",head);
                    new UniteApi(ApiUtil.TEAM_ADD,hm).post(new ApiListener() {
                        @Override
                        public void success(Api api) {
                            DialogUIUtils.showToastCenter("创建成功");
                            finish();
                        }

                        @Override
                        public void failure(Api api) {
                            DialogUIUtils.showToastCenter("网络波动，请重试");
                        }
                    });
                } else {
                    DialogUIUtils.showToastCenter("请输入小组完整信息");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            uriSelected = Matisse.obtainResult(data);
            String path= PathUtil.getFilePath(SquareGroupAddActivity.this,uriSelected.get(0));
            new UploadApi(ApiUtil.FILE_UPLOAD_HEAD, path).upload(new ApiListener() {
                @Override
                public void success(Api api) {
                    UploadApi u = (UploadApi) api;
                    Gson gson = new Gson();
                    try {
                        String name = gson.fromJson(u.getJsonData().getString(0), String.class);
                        head=name;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(Api api) {
                    DialogUIUtils.showToastCenter("网络波动了一下");
                }
            });
            Glide.with(this)
                    .load(uriSelected.get(0))
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(groupAddAvatar);

        }
    }

    private boolean isMatchRex() {
        return !(groupAddName.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0
                || groupAddDesc.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0
                || groupAddCity.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0);
    }
}
