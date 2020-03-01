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
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.adapter.CirclePostAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;


public class CirclePostFragment extends Fragment {

    private int circleType;

    private View view;
    private Context context;
    private MainActivity activity;
    private PageEntity page = new PageEntity();
    private List<DiaryUserVO> posts = new ArrayList<>();
    private XRecyclerView circlePostList;
    private CirclePostAdapter circlePostAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null)
            circleType = bundle.getInt("circleType");

        view = inflater.inflate(R.layout.fragment_circle_post, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        circlePostList = view.findViewById(R.id.circlePostList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        circlePostList.setLayoutManager(linearLayoutManager);
        circlePostAdapter = new CirclePostAdapter(context);
        circlePostList.setAdapter(circlePostAdapter);
    }

    private void initListener() {
        circlePostList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        hashMap.put("type", String.valueOf(circleType));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_CICLE, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                posts = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                circlePostAdapter.initData(posts);
                circlePostList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                circlePostList.refreshComplete();
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            circlePostList.setNoMore(true);
            circlePostList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            circlePostList.loadMoreComplete();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(circleType));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                posts = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                circlePostAdapter.loadData(posts);
                circlePostList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                circlePostList.loadMoreComplete();
            }
        });
    }

    public static CirclePostFragment newInstance(int circleType) {
        CirclePostFragment circlePostFragment = new CirclePostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("circleType", circleType);
        circlePostFragment.setArguments(bundle);
        return circlePostFragment;
    }
}
