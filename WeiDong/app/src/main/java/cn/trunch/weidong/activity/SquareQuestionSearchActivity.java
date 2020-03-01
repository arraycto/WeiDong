package cn.trunch.weidong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import cn.trunch.weidong.adapter.SquareQuestionSearchAdapter;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.adapter.SquareQuestionAdapter;
import cn.trunch.weidong.entity.PageEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;

public class SquareQuestionSearchActivity extends AppCompatActivity {

    private ImageView questionSearchCancelBtn;
    private TextView questionSearchSubmitBtn;
    private EditText questionSearchInput;
    private XRecyclerView questionSearchList;
    private SquareQuestionSearchAdapter questionAdapter;

    private List<DiaryUserVO> questions = new ArrayList<>();
    private PageEntity page = new PageEntity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_question_search);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        DialogUIUtils.init(this);

//        initData();
    }

    private void init() {
        questionSearchCancelBtn = findViewById(R.id.questionSearchCancelBtn);
        questionSearchSubmitBtn = findViewById(R.id.questionSearchSubmitBtn);
        questionSearchInput = findViewById(R.id.questionSearchInput);
        questionSearchList = findViewById(R.id.questionSearchList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        questionSearchList.setLayoutManager(layoutManager);
        questionAdapter = new SquareQuestionSearchAdapter(this);
        questionSearchList.setAdapter(questionAdapter);
        questionSearchList.setPullRefreshEnabled(false);
    }

    private void initListener() {
        questionSearchCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        questionSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    initData();
                }
                return false;
            }
        });
        questionSearchSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        questionSearchList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        hashMap.put("key", questionSearchInput.getText().toString());
        new UniteApi(ApiUtil.DIARY_SEARCH, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionSearchInput.setText("");
                if (questions.size() == 0)
                    DialogUIUtils.showToastCenter("未找到相关问题");
                else
                    DialogUIUtils.showToastCenter("为您找到" + questions.size() + "条结果");
                questionAdapter.initData(questions);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络波动，请重试");
            }
        });
    }

    private void loadData() {
        //TODO
        if (!page.getNextPage()) {
            DialogUIUtils.showToastCenter("已经到底了");
            questionSearchList.loadMoreComplete();
            return;
        }
        // TODO 网络请求
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("type", String.valueOf(ApiUtil.QUESTION_TYPE));
        hashMap.put("pageNum", String.valueOf(page.getCurrentPage() + 1));
        hashMap.put("pageSize", String.valueOf(page.getPageSize()));
        hashMap.put("key", questionSearchInput.getText().toString());
        new UniteApi(ApiUtil.DIARY_SEARCH, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi uniteApi = (UniteApi) api;
                Gson gson = new Gson();
                questions = gson.fromJson(uniteApi.getJsonData().toString(), new TypeToken<List<DiaryUserVO>>() {
                }.getType());
                page = gson.fromJson(uniteApi.getJsonPage().toString(), PageEntity.class);
                questionSearchList.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.loadData(questions);
                        questionSearchList.loadMoreComplete();
                    }
                });
            }

            @Override
            public void failure(Api api) {

            }
        });
    }
}
