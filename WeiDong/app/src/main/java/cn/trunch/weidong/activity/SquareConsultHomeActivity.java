package cn.trunch.weidong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.SquareConsultHomeAdapter;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TextUtil;
import cn.trunch.weidong.vo.DarenInfo;
import cn.trunch.weidong.vo.DiaryUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareConsultHomeActivity extends AppCompatActivity {

    private String userId;
    private DarenInfo expert = new DarenInfo();

    private ImageView consultHomeBackBtn;
    private ImageView consultHomeAvatar;
    private TextView consultHomeName;
    private TextView consultHomeInfo;
    private TextView consultHomeIntro;
    private TextView consultHomeMedal;
    private TextView consultHomeHonor;
    private TextView consultHomeSport;
    private Button consultAddGoBtn;

    private XRecyclerView consultHomeList;
    private SquareConsultHomeAdapter consultAdapter;
    private List<DiaryUserVO> consults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_consult_home);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        userId = getIntent().getStringExtra("userId");

        init();
        initListener();
        initHeadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void init() {
        consultHomeBackBtn = findViewById(R.id.consultHomeBackBtn);
        consultHomeAvatar = findViewById(R.id.consultHomeAvatar);
        consultHomeName = findViewById(R.id.consultHomeName);
        consultHomeInfo = findViewById(R.id.consultHomeInfo);
        consultHomeIntro = findViewById(R.id.consultHomeIntro);
        TextUtil.setBold(consultHomeIntro);
        consultHomeMedal = findViewById(R.id.consultHomeMedal);
        TextUtil.setBold(consultHomeMedal);
        consultHomeHonor = findViewById(R.id.consultHomeHonor);
        TextUtil.setBold(consultHomeHonor);
        consultHomeSport = findViewById(R.id.consultHomeSport);
        TextUtil.setBold(consultHomeSport);
        consultAddGoBtn = findViewById(R.id.consultAddGoBtn);
        consultHomeList = findViewById(R.id.consultHomeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        consultHomeList.setLayoutManager(linearLayoutManager);
        consultAdapter = new SquareConsultHomeAdapter(this);
        consultHomeList.setAdapter(consultAdapter);
        consultHomeList.setNestedScrollingEnabled(false);
        consultHomeList.setHasFixedSize(true);
        consultHomeList.setFocusable(false);
        consultHomeList.setPullRefreshEnabled(false);
        consultHomeList.setLoadingMoreEnabled(false);
        consultHomeList.setNoMore(true);
    }

    private void initListener() {
        consultHomeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        consultAddGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquareConsultHomeActivity.this, SquareConsultAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initHeadData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("uid", userId);
        new UniteApi(ApiUtil.USER_EXPERT_INFO, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                try {
                    expert = gson.fromJson(uniteApi.getJsonData().get(0).toString(), DarenInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //显示数据
                Glide.with(SquareConsultHomeActivity.this)
                        .load(expert.getUser().getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(consultHomeAvatar);
                consultHomeName.setText(expert.getUser().getuNickname());
                consultHomeInfo.setText(expert.getUser().getuRank() + "级达人");
                consultHomeIntro.setText(expert.getUser().getuSelfdes() == null ? "暂无介绍" : expert.getUser().getuSelfdes());
                consultHomeMedal.setText(expert.getUser().getuRank() + "级达人");
                consultHomeSport.setText("累计运动 " + String.valueOf(expert.getUser().getuExTime()) + " 秒");
                consultHomeHonor.setText(expert.getLikeNum() + " 点赞数 · " + (expert.getAnswerNum() * 10) + " 活跃度");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });

    }

    private void initData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("uid", userId);
        hashMap.put("pageNum", "1");
        hashMap.put("pageSize", "20");
        new UniteApi(ApiUtil.DIARY_EXPERT_REPLY, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                consults = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                consultAdapter.initData(consults);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }
}
