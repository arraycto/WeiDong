package cn.trunch.weidong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.vo.ComUserVO;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.adapter.SquareConsultSeeAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.UserEntity;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareConsultSeeActivity extends AppCompatActivity {

    private final static int CONSULT_COMMENT_ADD_CODE = 22;
    private String consultId;
    private DiaryUserVO consult;

    private ImageView consultSeeBackBtn;
    private TextView consultSeeFinishBtn; // TODO 右上角结束咨询按钮
    private TextView consultSeeOperateBtn; // TODO 底部操作按钮
    // 头部
    private TextView consultSeeFee;
    private TextView consultSeeScore;
    private TextView consultSeeAudit;
    private ImageView consultSeeUHead;
    private TextView consultSeeUInfo;
    private TextView consultSeeContent;
    private NineGridView consultSeeImg;
    private TextView consultSeeInfo;
    // 下部List
    private XRecyclerView consultSeeList;
    private SquareConsultSeeAdapter consultSeeAdapter;
    private List<ComUserVO> comments = new ArrayList<>();
    private String did = "";
    private PageEntity comPage;
    private boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_consult_see);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        consultId = getIntent().getStringExtra("consultId");

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
        consultSeeBackBtn = findViewById(R.id.consultSeeBackBtn);
        consultSeeFinishBtn = findViewById(R.id.consultSeeFinishBtn);
        consultSeeOperateBtn = findViewById(R.id.consultSeeOperateBtn);
        //头部
        View consultSeeHeaderView = LayoutInflater.from(this).inflate(R.layout.view_square_consult_see_header
                , (ViewGroup) findViewById(android.R.id.content), false);

        consultSeeFee = consultSeeHeaderView.findViewById(R.id.consultSeeFee);
        consultSeeScore = consultSeeHeaderView.findViewById(R.id.consultSeeScore);
        consultSeeAudit = consultSeeHeaderView.findViewById(R.id.consultSeeAudit);
        consultSeeUHead = consultSeeHeaderView.findViewById(R.id.consultSeeUHead);
        consultSeeUInfo = consultSeeHeaderView.findViewById(R.id.consultSeeUInfo);
        consultSeeContent = consultSeeHeaderView.findViewById(R.id.consultSeeContent);
        consultSeeImg = consultSeeHeaderView.findViewById(R.id.consultSeeImg);
        consultSeeInfo = consultSeeHeaderView.findViewById(R.id.consultSeeInfo);

        //list数据
        consultSeeList = findViewById(R.id.consultSeeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        consultSeeList.setLayoutManager(linearLayoutManager);
        consultSeeAdapter = new SquareConsultSeeAdapter(this);
        consultSeeList.setAdapter(consultSeeAdapter);

        consultSeeList.setNestedScrollingEnabled(false);
        consultSeeList.setHasFixedSize(true);
        consultSeeList.setFocusable(false);
        consultSeeList.addHeaderView(consultSeeHeaderView);
        did = getIntent().getStringExtra("consultId");
        comPage = new PageEntity();
        isCompleted = false;
    }

    private void initListener() {
        consultSeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        consultSeeFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTheConsult();
            }
        });
    }

    private void endTheConsult() {

    }

    private void like() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", consultId);
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                consult.setIsLike(consult.getIsLike() == 1 ? 0 : 1);
                consult.setDiaryLikeNum(consult.getIsLike() == 1
                        ? consult.getDiaryLikeNum() + 1 : consult.getDiaryLikeNum() - 1);
                consultSeeOperateBtn.setText(consult.getIsLike() == 1 ? "已喜欢" : "喜欢");
                consultSeeScore.setText(String.valueOf(consult.getDiaryLikeNum()));
                DialogUIUtils.showToastCenter("操作成功");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }

    private void initHeadData() {
        // TODO 请求consult数据
        consult = new DiaryUserVO();
        consult.setRepositity(3);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                try {
                    consult = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //--------------------------------显示数据--------------------------------------
                consultSeeFee.setText(String.valueOf(consult.getDiaryReward()));
                consultSeeScore.setText(String.valueOf(consult.getDiaryLikeNum()));
                consultSeeAudit.setText(String.valueOf(consult.getDiaryReadNum()));
                UserEntity user = consult.getUser();
                Glide.with(SquareConsultSeeActivity.this)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(consultSeeUHead);
                consultSeeUInfo.setText(user.getuNickname() + " 的提问");
                consultSeeContent.setText(consult.getDiaryContent());
                List<ImageInfo> imageInfos = new ArrayList<>();
                List<String> imgs = consult.getImg();
                if (imgs != null)
                    for (String imageURL : imgs) {
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setThumbnailUrl(imageURL);
                        imageInfo.setBigImageUrl(imageURL);
                        imageInfos.add(imageInfo);
                    }
                consultSeeImg.setAdapter(new NineGridViewClickAdapter(SquareConsultSeeActivity.this, imageInfos));
                consultSeeInfo.setText(consult.getDiaryTime());
                initBtn();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                isCompleted = true;
            }
        });

    }

    private void initData() {
        comPage.setCurrentPage(0);
        comPage.setPageSize(20);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("pageNum", String.valueOf(comPage.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(comPage.getPageSize()));
        new UniteApi(ApiUtil.COM_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                comments = g.fromJson(u.getJsonData().toString(), new TypeToken<List<ComUserVO>>() {
                }.getType());
                comPage = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                consultSeeAdapter.initData(comments);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("评论加载失败");
            }
        });
    }

    private void initBtn() {
        switch (consult.getRepositity()) {
            case 1:
                consultSeeOperateBtn.setText("补充问题");
                consultSeeOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SquareConsultSeeActivity.this, SquareConsultAddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", CONSULT_COMMENT_ADD_CODE);
                        //补充问题 提问
                        bundle.putInt("comType", 1);
                        bundle.putString("did", consult.getDiaryId());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, CONSULT_COMMENT_ADD_CODE);
                    }
                });
                consultSeeFinishBtn.setVisibility(View.GONE);
                break;
            case 2:
                consultSeeOperateBtn.setText("补充回答");
                consultSeeOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SquareConsultSeeActivity.this, SquareConsultAddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", CONSULT_COMMENT_ADD_CODE);
                        //补充问题 提问
                        bundle.putInt("comType", 2);
                        bundle.putString("did", consult.getDiaryId());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, CONSULT_COMMENT_ADD_CODE);
                    }
                });
                consultSeeFinishBtn.setVisibility(View.GONE);
                break;
            default:
                consultSeeOperateBtn.setText(consult.getIsLike() == 1 ? "已喜欢" : "喜欢");
                consultSeeOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like();
                    }
                });
                consultSeeFinishBtn.setVisibility(View.GONE);
                break;
        }
    }
}
