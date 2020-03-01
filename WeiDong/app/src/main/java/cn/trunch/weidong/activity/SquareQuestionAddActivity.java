package cn.trunch.weidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.dou361.dialogui.DialogUIUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.adapter.FragmentAdapter;
import cn.trunch.weidong.fragment.SquareQuestionAdd1Fragment;
import cn.trunch.weidong.fragment.SquareQuestionAdd2Fragment;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.CodeEn;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.view.SlideViewPager;

public class SquareQuestionAddActivity extends AppCompatActivity {

    private TextView addCancelBtn;
    private TextView addNextBtn;
    private boolean isFirst = true;
    private boolean nextAble = false;

    private SquareQuestionAdd1Fragment addFragment1;
    private SquareQuestionAdd2Fragment addFragment2;
    private List<Fragment> fragments = new ArrayList<>();
    private SlideViewPager addViewPager;
    private FragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_question_add);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initFragments();
        initListener();

    }

    public void init() {
        addCancelBtn = findViewById(R.id.questionAddCancelBtn);
        addNextBtn = findViewById(R.id.questionAddNextBtn);
        addViewPager = findViewById(R.id.questionAddViewPager);
    }

    public void initFragments () {
        addFragment1 = new SquareQuestionAdd1Fragment();
        addFragment2 = new SquareQuestionAdd2Fragment();
        fragments.add(addFragment1);
        fragments.add(addFragment2);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setFragments(fragments);
        addViewPager.setAdapter(fragmentAdapter);
        addViewPager.setSlide(false);
    }

    public void initListener() {
        addCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirst) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    overridePendingTransition(R.anim.anim_static, R.anim.bottom_out);
                    finish();
                }
                else {
                    addViewPager.setCurrentItem(0);
                    addNextBtn.setText("下一步");
                    setNextAble(true);
                }
                isFirst = true;
            }
        });
        addNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextAble) {
                    if (isFirst) {
                        addViewPager.setCurrentItem(1);
                        addNextBtn.setText("完成");
                        isFirst = false;
                    } else {
                        // TODO 发布问题
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        overridePendingTransition(R.anim.anim_static, R.anim.bottom_out);
                        HashMap<String,String> hm=new HashMap<>();
                        String qimgs=addFragment2.getQuestionImages();
                        hm.put("token", ApiUtil.USER_TOKEN);
                        hm.put("type",String.valueOf(ApiUtil.QUESTION_TYPE));
                        hm.put("title",addFragment1.getProblemTitle());
                        hm.put("content",addFragment2.getQuestionContent());
                        try {
                            hm.put("annex", URLEncoder.encode(qimgs, CodeEn.CHARSET));
                        } catch (Exception e) {
                            //
                        }
                        try {
                            hm.put("img",qimgs.substring(0,qimgs.indexOf(",")));
                        } catch (Exception e) {
                           //
                        }
                        hm.put("label",addFragment2.getQuestionLabels());
                        hm.put("ano",addFragment2.getQuestionAnonymous());
                        //赏金，暂无
//                        hm.put("ano",addFragment2.getQuestionBounty());
                        new UniteApi(ApiUtil.DIARY_ADD,hm).post(new ApiListener() {
                            @Override
                            public void success(Api api) {
                                finish();
                            }

                            @Override
                            public void failure(Api api) {
                                DialogUIUtils.showToastCenter("网络波动哦，请重试");
                            }
                        });

                        finish();
                        setNextAble(false);
                    }
                }
            }
        });
    }
    public void setNextAble(boolean nextAble) {
        this.nextAble = nextAble;
        if (nextAble)
            addNextBtn.setTextColor(getResources().getColor(R.color.colorTheme));
        else
            addNextBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
    }
}
