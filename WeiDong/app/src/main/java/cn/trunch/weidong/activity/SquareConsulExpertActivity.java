package cn.trunch.weidong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.SquareConsultExpertAdapter;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;

public class SquareConsulExpertActivity extends AppCompatActivity {

    private ImageView consultExpertBackBtn;
    private TextView consultExpertTitle;

    private XRecyclerView consultExpertList;
    private SquareConsultExpertAdapter consultExpertAdapter;
    private List<UserEntity> experts;
    private PageEntity page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_consul_expert);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        initData();

        DialogUIUtils.init(this);
    }

    private void init() {
        consultExpertBackBtn = findViewById(R.id.consultExpertBackBtn);
        consultExpertTitle = findViewById(R.id.consultExpertTitle);
        consultExpertList = findViewById(R.id.consultExpertList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        consultExpertList.setLayoutManager(linearLayoutManager);
        consultExpertAdapter = new SquareConsultExpertAdapter(this);
        consultExpertList.setAdapter(consultExpertAdapter);
        consultExpertList.setLoadingMoreEnabled(true);
        page = new PageEntity();
        experts = new ArrayList<>();
    }

    private void initListener() {
        consultExpertBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        consultExpertList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
    }


    private void initData() {
        page.setCurrentPage(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.USER_EXPERT, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                experts = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<UserEntity>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                consultExpertAdapter.initData(experts);
                consultExpertList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                consultExpertList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            consultExpertList.setNoMore(true);
            consultExpertList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            consultExpertList.loadMoreComplete();
            return;
        }
        page.setCurrentPage(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.USER_EXPERT, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                experts = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<UserEntity>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                consultExpertAdapter.loadData(experts);
                consultExpertList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                consultExpertList.loadMoreComplete();
            }
        });
    }

}
