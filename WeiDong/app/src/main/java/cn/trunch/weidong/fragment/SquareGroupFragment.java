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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareGroupAddActivity;
import cn.trunch.weidong.activity.SquareGroupSearchActivity;
import cn.trunch.weidong.adapter.SquareGroupAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.entity.TeamEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.http.UploadApi;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareGroupFragment extends Fragment {
    private View view;
    private Context context;

    private ImageView groupHeaderImg;
    private LinearLayout groupSearchBtn;
    private LinearLayout groupAddBtn;
    private PageEntity page;
    private XRecyclerView groupList;
    private SquareGroupAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_group, container, false);
        context = getActivity();

        init();
        initListener();

        initData();

        return view;
    }

    private void init() {
        groupList = view.findViewById(R.id.groupList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        groupList.setLayoutManager(layoutManager);
        groupAdapter = new SquareGroupAdapter(context);
        groupList.setAdapter(groupAdapter);
        // init Header
        View imgHeaderView = LayoutInflater.from(context).inflate(R.layout.view_square_group_header
                , (ViewGroup) view.findViewById(android.R.id.content), false);
        groupHeaderImg = imgHeaderView.findViewById(R.id.groupHeaderImg);
        groupSearchBtn = imgHeaderView.findViewById(R.id.groupSearchBtn);
        groupAddBtn = imgHeaderView.findViewById(R.id.groupAddBtn);
        groupList.addHeaderView(imgHeaderView);
        page = new PageEntity();
    }

    private void initListener() {
        groupSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareGroupSearchActivity.class);
                startActivity(intent);
            }
        });
        groupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareGroupAddActivity.class);
                startActivity(intent);
            }
        });
        groupList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        Glide.with(context)
                .load("http://www.two2two.xyz/werunImg/slogan/group.jpg")
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CenterCrop(),
                        new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                )))
                .into(groupHeaderImg);
        page.setCurrentPage(0);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.TEAM_lIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson g = new Gson();
                UniteApi u = (UniteApi) api;
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                List<TeamEntity> teamEntities = g.fromJson(u.getJsonData().toString(), new TypeToken<List<TeamEntity>>() {
                }.getType());
                groupAdapter.initData(teamEntities);
                groupList.refreshComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                groupList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            groupList.setNoMore(true);
            groupList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            groupList.loadMoreComplete();
            return;
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hm.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.TEAM_lIST, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson g = new Gson();
                UniteApi u = (UniteApi) api;
                page = g.fromJson(u.getJsonPage().toString(), PageEntity.class);
                List<TeamEntity> teamEntities = g.fromJson(u.getJsonData().toString(), new TypeToken<List<TeamEntity>>() {
                }.getType());
                groupAdapter.loadData(teamEntities);
                groupList.loadMoreComplete();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                groupList.loadMoreComplete();
            }
        });
    }
}
