package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import cn.trunch.weidong.activity.SquareQuestionSearchActivity;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SquareQuestionAddActivity;
import cn.trunch.weidong.adapter.SquareQuestionAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareQuestionFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private LinearLayout questionSearchBtn;
    private FloatingActionButton questionAddBtn;
    private XRecyclerView questionList;
    private ImageView questionHeaderImg;
    private SquareQuestionAdapter questionAdapter;

    private List<DiaryUserVO> questions = new ArrayList<>();
    private PageEntity page = new PageEntity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_question, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();

        initData();

        return view;
    }

    private void init() {
        questionSearchBtn = view.findViewById(R.id.questionSearchBtn);
        questionList = view.findViewById(R.id.questionList);
        questionAddBtn = view.findViewById(R.id.questionAddBtn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        questionList.setLayoutManager(layoutManager);
        questionAdapter = new SquareQuestionAdapter(context);
        questionList.setAdapter(questionAdapter);
        View imgHeaderView = LayoutInflater.from(context).inflate(R.layout.view_square_question_header
                , (ViewGroup) view.findViewById(android.R.id.content), false);
        questionHeaderImg = imgHeaderView.findViewById(R.id.questionHeaderImg);
        questionList.addHeaderView(imgHeaderView);
    }

    private void initListener() {
        questionSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareQuestionSearchActivity.class);
                startActivity(intent);
            }
        });
        questionList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        questionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SquareQuestionAddActivity.class);
                activity.overridePendingTransition(R.anim.bottom_in, R.anim.anim_static);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Glide.with(context)
                .load("http://www.two2two.xyz/werunImg/slogan/question.jpg")
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CenterCrop()
                )))
                .into(questionHeaderImg);
        page.setCurrentPage(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionList.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.initData(questions);
                        questionList.refreshComplete();
                    }
                });
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                questionList.refreshComplete();
            }
        });
    }

    private void loadData() {
        if (!page.getNextPage()) {
            questionList.setNoMore(true);
            questionList.setFootViewText("正在加载更多", "已经到底了");
            DialogUIUtils.showToastCenter("已经到底了");
            questionList.loadMoreComplete();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        new UniteApi(ApiUtil.DIARY_LIST, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionList.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.loadData(questions);
                        questionList.loadMoreComplete();
                    }
                });
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
                questionList.loadMoreComplete();
            }
        });
    }
}
