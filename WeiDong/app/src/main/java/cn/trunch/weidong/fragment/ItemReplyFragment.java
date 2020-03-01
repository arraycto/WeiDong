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
import cn.trunch.weidong.adapter.ItemReplyAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.vo.ComUserVO;

public class ItemReplyFragment extends Fragment {
    private View view;
    private Context context;
    private String did;
    private XRecyclerView itemReplyList;
    private ItemReplyAdapter itemReplyAdapter;
    private PageEntity page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null)
            did = bundle.getString("did");
        view = inflater.inflate(R.layout.fragment_item_reply, container, false);
        context = getActivity();

        init();
        initListener();
        initData();

        return view;
    }

    private void init() {
        itemReplyList = view.findViewById(R.id.replyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        itemReplyList.setLayoutManager(linearLayoutManager);
        itemReplyAdapter = new ItemReplyAdapter(context);
        itemReplyList.setAdapter(itemReplyAdapter);
        page = new PageEntity();
    }

    private void initListener() {
        itemReplyList.setLoadingListener(new XRecyclerView.LoadingListener() {
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

    public void initData() {
        page.setCurrentPage(0);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.COM_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<ComUserVO> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<ComUserVO>>() {
                }.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemReplyAdapter.initData(o);
                itemReplyList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemReplyList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            itemReplyList.setNoMore(true);
            itemReplyList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            itemReplyList.loadMoreComplete();
            return;
        }
        page.setCurrentPage(0);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.COM_LIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson g = new Gson();
                List<ComUserVO> o = g.fromJson(u.getJsonData().toString(), new TypeToken<List<ComUserVO>>() {
                }.getType());
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                itemReplyAdapter.loadData(o);
                itemReplyList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
                itemReplyList.loadMoreComplete();
            }
        });
    }

    public static ItemReplyFragment newInstance(String itemId) {
        ItemReplyFragment fragment = new ItemReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("did", itemId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
