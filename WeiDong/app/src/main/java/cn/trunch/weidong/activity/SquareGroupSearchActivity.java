package cn.trunch.weidong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.SquareGroupAdapter;
import cn.trunch.weidong.adapter.SquareGroupSearchAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.TeamEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareGroupSearchActivity extends AppCompatActivity {

    private ImageView groupSearchCancelBtn;
    private TextView groupSearchSubmitBtn;
    private EditText groupSearchInput;
    private XRecyclerView groupSearchList;
    private SquareGroupSearchAdapter groupAdapter;
    private PageEntity page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_group_search);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        DialogUIUtils.init(this);

//        initData();
    }

    private void init() {
        groupSearchCancelBtn = findViewById(R.id.groupSearchCancelBtn);
        groupSearchSubmitBtn = findViewById(R.id.groupSearchSubmitBtn);
        groupSearchInput = findViewById(R.id.groupSearchInput);

        groupSearchList = findViewById(R.id.groupSearchList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        groupSearchList.setLayoutManager(layoutManager);
        groupAdapter = new SquareGroupSearchAdapter(this);
        groupSearchList.setAdapter(groupAdapter);
        groupSearchList.setPullRefreshEnabled(false);
        page=new PageEntity();
    }

    private void initListener() {
        groupSearchCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // TODO 搜索
                    initData();
                }
                return false;
            }
        });
        groupSearchSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 搜索
                initData();
            }
        });
        groupSearchList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        page.setPageSize(5);
        HashMap<String,String> hm=new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("key",groupSearchInput.getText().toString());
        hm.put("pageNum",String.valueOf(page.getCurrentPage()+1));
        hm.put("pageSize",String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.TEAM_SEARCH,hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson=new Gson();
                UniteApi u=(UniteApi)api;
                List<TeamEntity> teams = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<TeamEntity>>() {
                }.getType());
                page=gson.fromJson(u.getJsonPage().toString(),PageEntity.class);
                groupSearchInput.setText("");
                if (teams.size() == 0)
                    DialogUIUtils.showToastCenter("未找到相关小组");
                else
                    DialogUIUtils.showToastCenter("为您找到" + teams.size() + "条结果");
                groupAdapter.initData(teams);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
        groupSearchList.refreshComplete();
    }

    private void loadData() {
        if(!page.getNextPage()){
            DialogUIUtils.showToastCenter("已经到底了");
            groupSearchList.loadMoreComplete();
        }
        HashMap<String,String> hm=new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("key",groupSearchInput.getText().toString());
        hm.put("pageNum",String.valueOf(page.getCurrentPage()+1));
        hm.put("pageSize",String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.TEAM_SEARCH,hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson=new Gson();
                UniteApi u=(UniteApi)api;
                List<TeamEntity> o = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<TeamEntity>>() {
                }.getType());
                page=gson.fromJson(u.getJsonPage().toString(),PageEntity.class);
                groupAdapter.loadData(o);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
        groupSearchList.loadMoreComplete();
    }
}
