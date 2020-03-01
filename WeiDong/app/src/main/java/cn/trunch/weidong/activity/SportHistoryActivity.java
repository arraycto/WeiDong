package cn.trunch.weidong.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.SportHistoryAdapter;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.ExerciseEntity;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.step.bean.StepData;
import cn.trunch.weidong.step.utils.DbUtils;
import cn.trunch.weidong.util.SPUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.util.TimeUtil;

public class SportHistoryActivity extends AppCompatActivity {

    private XRecyclerView sportHistoryList;
    private SportHistoryAdapter sportHistoryAdapter;
    private TextView stepAll;
    private TextView timeAll;
    private TextView ocalAll;
    private TextView disAll;
    private ImageView sportHisBackBtn;

    private long steps;
    private PageEntity page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history);
        StatusBarUtil.setStatusBarMode(this, false, R.color.colorTheme);

        init();
        initListener();
        initHeadData();
        initData();

    }

    private void init() {
        sportHistoryList = findViewById(R.id.sportHisList);
        sportHisBackBtn = findViewById(R.id.sportHisBackBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sportHistoryList.setLayoutManager(linearLayoutManager);
        sportHistoryAdapter = new SportHistoryAdapter(this);
        sportHistoryList.setAdapter(sportHistoryAdapter);
        View imgHeaderView = LayoutInflater.from(this).inflate(R.layout.view_sport_history_header
                , (ViewGroup) findViewById(android.R.id.content), false);
        sportHistoryList.addHeaderView(imgHeaderView);
        sportHistoryList.setLoadingMoreEnabled(false);

        stepAll = imgHeaderView.findViewById(R.id.sportHisStepAll);
        timeAll = imgHeaderView.findViewById(R.id.sportHisTimeAll);
        ocalAll = imgHeaderView.findViewById(R.id.sportHisOcalAll);
        disAll = imgHeaderView.findViewById(R.id.sportHisDisAll);
        page=new PageEntity();
    }

    private void initListener() {
        sportHistoryList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        sportHisBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initHeadData() {
        steps = 0;
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas = DbUtils.getQueryAll(StepData.class);
        for (StepData stepData : stepDatas) {
            steps += Long.parseLong(stepData.getStep());
        }
        stepAll.setText(String.valueOf(steps));
        SPUtil.init(this);
        long timeS = SPUtil.getLong(TimeUtil.getCurrentDate() + "_SPORT_TIME", 0);
        String timeH = String.valueOf(timeS / 3600.0);
        timeAll.setText(timeH.substring(0, timeH.length() > 4 ? 4 : timeH.length()) + "h");
        ocalAll.setText(String.format("%.2f", steps * 0.04) + "cal");
        disAll.setText(String.format("%.2f", steps * 0.6) + "m");

    }
    private void initData(){
        page.setCurrentPage(0);
        page.setPageSize(30);
        HashMap<String,String> hm=new HashMap<>();
        hm.put("token",ApiUtil.USER_TOKEN);
        hm.put("pageNum",String.valueOf(page.getCurrentPage()+1));
        hm.put("pageSize",String.valueOf(page.getPageSize()+1));
        new UniteApi(ApiUtil.EX_LIST,hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u=(UniteApi)api;
                Gson gson=new Gson();
                List<ExerciseEntity> exs = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<ExerciseEntity>>() {
                }.getType());
                sportHistoryAdapter.initData(exs);
                sportHistoryList.refreshComplete();
            }
            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动~");
                sportHistoryList.refreshComplete();
            }
        });
    }
    private void loadData(){
        if(!page.getNextPage())
        {
            DialogUIUtils.showToastCenter("没有更多了");
            sportHistoryList.loadMoreComplete();
            return;
        }
        HashMap<String,String> hm=new HashMap<>();
        hm.put("token",ApiUtil.USER_TOKEN);
        hm.put("pageNum",String.valueOf(page.getCurrentPage()+1));
        hm.put("pageSize",String.valueOf(page.getPageSize()+1));
        new UniteApi(ApiUtil.EX_LIST,hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u=(UniteApi)api;
                Gson gson=new Gson();
                List<ExerciseEntity> exs = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<ExerciseEntity>>() {
                }.getType());
                sportHistoryAdapter.loadData(exs);
                sportHistoryList.loadMoreComplete();
            }
            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动~");
                sportHistoryList.loadMoreComplete();
            }
        });
    }

}
