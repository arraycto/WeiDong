package cn.trunch.weidong.fragment;

import android.content.Context;
import android.media.DeniedByServerException;
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
import cn.trunch.weidong.adapter.SportDiaryAdapter;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;

public class SportDiaryFragment extends Fragment {
    private View view;
    private Context context;
    private int diaryType;

    private List<DiaryEntity> diaries;
    private XRecyclerView diaryList;
    private SportDiaryAdapter diaryAdapter;
    private PageEntity page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null)
            diaryType = bundle.getInt("diaryType");

        view = inflater.inflate(R.layout.fragment_sport_diary, container, false);
        context = getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        diaryList = view.findViewById(R.id.diaryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        diaryList.setLayoutManager(linearLayoutManager);
        diaryAdapter = new SportDiaryAdapter(context);
        diaryList.setAdapter(diaryAdapter);
        page = new PageEntity();
        diaries = new ArrayList<>();
    }

    private void initListener() {
        diaryList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        hashMap.put("ano", String.valueOf(diaryType));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                diaries = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryEntity>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                diaryAdapter.initData(diaries);
                diaryList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                diaryList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            diaryList.setNoMore(true);
            diaryList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            diaryList.loadMoreComplete();
            return;
        }
        page.setCurrentPage(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("ano", String.valueOf(diaryType));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                diaries = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryEntity>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                diaryAdapter.loadData(diaries);
                diaryList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                diaryList.loadMoreComplete();
            }
        });
    }

    public static SportDiaryFragment newInstance(int diaryType) {
        SportDiaryFragment sportDiaryFragment = new SportDiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("diaryType", diaryType);
        sportDiaryFragment.setArguments(bundle);
        return sportDiaryFragment;
    }
}
