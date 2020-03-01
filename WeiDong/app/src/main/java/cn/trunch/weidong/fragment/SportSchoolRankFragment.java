package cn.trunch.weidong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.adapter.SportSchoolRankAdapter;
import cn.trunch.weidong.entity.SchoolRank;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;

public class SportSchoolRankFragment extends Fragment {

    private View view;
    private Context context;
    private MainActivity activity;

    private XRecyclerView schoolRankList;
    private SportSchoolRankAdapter schoolRankAdapter;
    private List<SchoolRank> schools = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport_school_rank, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initData();

        return view;
    }

    private void init() {
        schoolRankList = view.findViewById(R.id.schoolRankList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        schoolRankList.setLayoutManager(linearLayoutManager);
        schoolRankAdapter = new SportSchoolRankAdapter(context);
        schoolRankList.setAdapter(schoolRankAdapter);
        schoolRankList.setPullRefreshEnabled(false);
        schoolRankList.setLoadingMoreEnabled(false);
        //解决数据加载不完的问题
        schoolRankList.setNestedScrollingEnabled(false);
        schoolRankList.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        schoolRankList.setFocusable(false);
    }

    private void initData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.CONSULT_TYPE));
        hashMap.put("pageNum", "1");
        hashMap.put("pageSize", "10");
        new UniteApi(ApiUtil.RANK_SCHOOL, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                schools = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<SchoolRank>>() {
                }.getType());
                schoolRankAdapter.initData(schools);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }
}
