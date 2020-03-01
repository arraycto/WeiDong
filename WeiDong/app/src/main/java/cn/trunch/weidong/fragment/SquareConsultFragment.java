package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareConsulExpertActivity;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.adapter.SquareConsultAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;

public class SquareConsultFragment extends Fragment {
    private View view;
    private Context context;
    private LinearLayout consultExpertClassify;
    private XRecyclerView consultList;
    private SquareConsultAdapter consultAdapter;
    private PageEntity page = new PageEntity();
    private List<DiaryUserVO> consults = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_consult, container, false);
        context = getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        consultList = view.findViewById(R.id.consultList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        consultList.setLayoutManager(linearLayoutManager);
        consultAdapter = new SquareConsultAdapter(context);
        consultList.setAdapter(consultAdapter);
        View imgHeaderView = LayoutInflater.from(context).inflate(R.layout.view_square_consult_header
                , (ViewGroup) view.findViewById(android.R.id.content), false);
        consultExpertClassify = imgHeaderView.findViewById(R.id.consultExpertClassify);
        consultList.addHeaderView(imgHeaderView);
    }

    private void initListener() {
        consultExpertClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareConsulExpertActivity.class);
                startActivity(intent);
            }
        });
        consultList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        hashMap.put("type", String.valueOf(ApiUtil.CONSULT_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                consults = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                consultAdapter.initData(consults);
                consultList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                consultList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            consultList.setNoMore(true);
            consultList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            consultList.loadMoreComplete();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.CONSULT_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                consults = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                consultAdapter.loadData(consults);
                consultList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                consultList.loadMoreComplete();
            }
        });
    }
}
