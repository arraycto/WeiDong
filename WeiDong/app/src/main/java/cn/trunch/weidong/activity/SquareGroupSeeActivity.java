package cn.trunch.weidong.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.dialog.MatchAddDialog;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.Glide4Engine;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.vo.TeamUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareGroupSeeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 1;

    private ImageView groupSeeBackBtn;
    private ImageView groupSeeAvatar;
    private TextView groupSeeName;
    private TextView groupSeeInfo;
    private Button groupSeeFuncBtn;
    //小组信息
    private ImageView groupSeeMasterAvatar;
    private ImageView groupSeeMeAvatar;
    private TextView groupSeeUserNum;
    private TextView groupSeeSportNum;
    private TextView groupSeeDesc;
    //活动
    private ImageView groupMatchAvatar;
    private TextView groupMatchName;
    private TextView groupMatchAddress;
    private TextView groupMatchTime;
    private TextView groupMatchDesc;
    private MatchAddDialog matchAddDialog;

    //hwf
    private String team_id = "";
    private TeamUserVO tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_group_see);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        init();
        initData();
        initListener();


    }

    private void init() {
        groupSeeBackBtn = findViewById(R.id.groupSeeBackBtn);
        groupSeeAvatar = findViewById(R.id.groupAddAvatar);
        groupSeeName = findViewById(R.id.groupSeeName);
        groupSeeInfo = findViewById(R.id.groupSeeInfo);
        groupSeeFuncBtn = findViewById(R.id.groupSeeFuncBtn);
        //小组信息
        groupSeeMasterAvatar = findViewById(R.id.groupSeeMasterAvatar);
        groupSeeMeAvatar = findViewById(R.id.groupSeeMeAvatar);
        groupSeeUserNum = findViewById(R.id.groupSeeUserNum);
        groupSeeSportNum = findViewById(R.id.groupSeeSportNum);
        groupSeeDesc = findViewById(R.id.groupSeeDesc);
        groupSeeAvatar = findViewById(R.id.groupSeeAvatar);
        //活动
        groupMatchAvatar = findViewById(R.id.groupMatchAvatar);
        groupMatchName = findViewById(R.id.groupMatchName);
        groupMatchAddress = findViewById(R.id.groupMatchAddress);
        groupMatchTime = findViewById(R.id.groupMatchTime);
        groupMatchDesc = findViewById(R.id.groupMatchDesc);
        matchAddDialog = new MatchAddDialog(this);

        team_id = getIntent().getStringExtra("team_id");
    }

    private void initListener() {
        groupSeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupSeeFuncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tv.getRepository()) {
                    case 1:
                        join();
                        break;
                    case 2:
                        out();
                        break;
                    case 3:
                        join();
                        break;
                    case 4:
                        newActivity();
                        break;
                    default:
                        break;
                }
            }
        });
        matchAddDialog.setOnImgSelectListener(new MatchAddDialog.OnImgSelectListener() {
            @Override
            public void onImgSelect() {
                Matisse.from(SquareGroupSeeActivity.this)
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
    }

    //hwg
    private void initData() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("tid", team_id);
        new UniteApi(ApiUtil.TEAM_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                try {
                    tv = gson.fromJson(u.getJsonData().get(0).toString(), TeamUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Glide.with(SquareGroupSeeActivity.this)
                        .load(tv.getActAvatar())
                        .into(groupSeeAvatar);
                groupSeeName.setText(tv.getTeamName());
                groupSeeInfo.setText("创建于" + tv.getTeamTime() + " · " + tv.getTeamLocation());
                Glide.with(SquareGroupSeeActivity.this)
                        .load(tv.getUser().getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(groupSeeMasterAvatar);
                groupSeeUserNum.setText(String.valueOf(tv.getActUserNum()));
                groupSeeDesc.setText(tv.getTeamDesc());
                switch (tv.getRepository()) {
                    case 1:
                        groupSeeFuncBtn.setText("加入小组");
                        groupSeeMeAvatar.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        groupSeeFuncBtn.setText("退出小组");
                        groupSeeMeAvatar.setVisibility(View.VISIBLE);
                        Glide.with(SquareGroupSeeActivity.this)
                                .load(ApiUtil.USER_AVATAR)
                                .apply(bitmapTransform(new CircleCrop()))
                                .into(groupSeeMeAvatar);
                        break;
                    case 3:
                        groupSeeFuncBtn.setText("重新加入");
                        groupSeeMeAvatar.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        groupSeeFuncBtn.setText("发起活动");
                        break;
                    default:
                        groupSeeFuncBtn.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("请重新加载");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            List<Uri> uriSelected = Matisse.obtainResult(data);
            matchAddDialog.setImg(uriSelected.get(0));
        }
    }

    //out
    private void out() {
        DialogUIUtils.showMdAlert(this, null, "确定要退出小组吗?", new DialogUIListener() {
            @Override
            public void onPositive() {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("token", ApiUtil.USER_TOKEN);
                hm.put("tid", team_id);
                new UniteApi(ApiUtil.TEAM_OUT, hm).post(new ApiListener() {
                    @Override
                    public void success(Api api) {
                        DialogUIUtils.showToastCenter("成功退出小组");
//                        initData();
                        finish();
                    }

                    @Override
                    public void failure(Api api) {
                        DialogUIUtils.showToastCenter("网络波动，请重试");
                    }
                });
            }

            @Override
            public void onNegative() {

            }
        }).show();
    }

    //join
    private void join() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("tid", team_id);
        new UniteApi(ApiUtil.TEAM_JOIN, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                DialogUIUtils.showToastCenter("成功加入小组");
                initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
    }

    //解散
    private void js() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("tid", team_id);
        new UniteApi(ApiUtil.TEAM_CHANGE, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                DialogUIUtils.showToastCenter("成功解散小组");
                finish();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
    }

    //发起活动
    private void newActivity() {
        DialogUIUtils.showToastCenter("敬请期待");
    }
}
