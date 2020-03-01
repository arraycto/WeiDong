package cn.trunch.weidong.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.fragment.ItemLikeFragment;
import cn.trunch.weidong.fragment.ItemReplyFragment;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.InputUtil;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.vo.DiaryUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CircleDiarySeeActivity extends AppCompatActivity {

    private String diaryId;
    private DiaryUserVO diary;
    private Dialog dialog;

    //头部
    private ImageView circleDiarySeeBackBtn;
    private TextView circleDiarySeeTitle;
    private ImageView circleDiarySeeUHead;
    private TextView circleDiarySeeUName;
    private TextView circleDiarySeeUInfo;
    private TextView circleDiarySeeInfo;
    private WebView circleDiarySeeContent;
    private TextView circleDiarySeeField;
    //下部导航
    private String[] titles = {"留言", "点赞"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ItemLikeFragment itemLikeFragment;
    private ItemReplyFragment itemReplyFragment;
    private SlidingTabLayout circleDiarySeeNav;
    private ViewPager circleDiarySeePager;
    // 底部操作
    private View circleDiarySeeCover;
    private LinearLayout circleDiarySeeReplyBox;
    private LikeButton circleDiarySeeLikeBtn;
    private LikeButton circleDiarySeeCollectBtn;
    private EditText circleDiarySeeReply;
    private TextView circleDiarySeeReplySendBtn;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_diary_see);
        diaryId = getIntent().getStringExtra("diaryId");
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initNav();
        initListener();
        initHeadData();

//        DialogUIUtils.init(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // 头部
        circleDiarySeeBackBtn = findViewById(R.id.circleDiarySeeBackBtn);
        circleDiarySeeTitle = findViewById(R.id.circleDiarySeeTitle);
        circleDiarySeeUHead = findViewById(R.id.circleDiarySeeUHead);
        circleDiarySeeUName = findViewById(R.id.circleDiarySeeUName);
        circleDiarySeeUInfo = findViewById(R.id.circleDiarySeeUInfo);
        circleDiarySeeInfo = findViewById(R.id.circleDiarySeeInfo);
        circleDiarySeeContent = findViewById(R.id.circleDiarySeeWebView);
        WebSettings webSettings = circleDiarySeeContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        circleDiarySeeField = findViewById(R.id.circleDiarySeeField);
        // 导航
        circleDiarySeeNav = findViewById(R.id.circleDiarySeeNav);
        circleDiarySeePager = findViewById(R.id.circleDiarySeePager);
        // 底部操作
        circleDiarySeeCover = findViewById(R.id.circleDiarySeeCover);
        circleDiarySeeReplyBox = findViewById(R.id.circleDiarySeeReplyBox);
        circleDiarySeeLikeBtn = findViewById(R.id.circleDiarySeeLikeBtn);
        circleDiarySeeCollectBtn = findViewById(R.id.circleDiarySeeCollectBtn);
        circleDiarySeeReply = findViewById(R.id.circleDiarySeeReply);
        circleDiarySeeReplySendBtn = findViewById(R.id.circleDiarySeeReplySendBtn);
    }

    private void initNav() {
        itemReplyFragment = ItemReplyFragment.newInstance(diaryId);
        itemLikeFragment = ItemLikeFragment.newInstance(diaryId);
        fragments.add(itemReplyFragment);
        fragments.add(itemLikeFragment);
        //配置并绑定 Fragment + ViewPager
        //绑定TabLayout和ViewPager
        circleDiarySeeNav.setViewPager(circleDiarySeePager, titles, this, fragments);
    }

    private void initListener() {
        circleDiarySeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        circleDiarySeeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleDiarySeeReply.clearFocus();
            }
        });
        circleDiarySeeReplyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拦截事件
            }
        });
        circleDiarySeeReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    circleDiarySeeCover.setVisibility(View.VISIBLE);
                    circleDiarySeeLikeBtn.setVisibility(View.GONE);
                    circleDiarySeeCollectBtn.setVisibility(View.GONE);
                    circleDiarySeeReplySendBtn.setVisibility(View.VISIBLE);
                    circleDiarySeeReply.setMinLines(3);
                    circleDiarySeeReply.setMaxLines(10);
                    InputUtil.showInput(circleDiarySeeReply);
                } else {
                    circleDiarySeeCover.setVisibility(View.GONE);
                    circleDiarySeeLikeBtn.setVisibility(View.VISIBLE);
                    circleDiarySeeCollectBtn.setVisibility(View.VISIBLE);
                    circleDiarySeeReplySendBtn.setVisibility(View.GONE);
                    circleDiarySeeReply.setMinLines(1);
                    circleDiarySeeReply.setMaxLines(1);
                    InputUtil.hideInput(CircleDiarySeeActivity.this);
                }
            }
        });
        circleDiarySeeReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().replaceAll(" ", "")
                        .replaceAll("\n", "").length() == 0) {
                    circleDiarySeeReplySendBtn.setEnabled(false);
                    circleDiarySeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                } else {
                    circleDiarySeeReplySendBtn.setEnabled(true);
                    circleDiarySeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                }
            }
        });
        circleDiarySeeReplySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        circleDiarySeeLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like();
            }
        });
        circleDiarySeeCollectBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    }

    private void initHeadData() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", String.valueOf(diaryId));
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson gson = new Gson();
                try {
                    diary = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserEntity user = diary.getUser();
                Glide.with(circleDiarySeeUHead)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(circleDiarySeeUHead);
                circleDiarySeeUName.setText(user.getuNickname());
                circleDiarySeeUInfo.setText(user.getuRank() + "级小白");
                circleDiarySeeInfo.setText(diary.getDiaryReadNum() + "次浏览 · "
                        + diary.getDiaryLikeNum() + "个赞同");
                circleDiarySeeTitle.setText(diary.getDiaryTitle());
                circleDiarySeeContent.loadDataWithBaseURL(null, URLDecoder.decode(diary.getDiaryContent())
                        , "text/html", "utf-8", null);
                circleDiarySeeField.setText(diary.getDiaryTime());
                circleDiarySeeLikeBtn.setLiked(diary.getIsLike() == 1);
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });

    }

    private void like() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", diaryId);
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                itemLikeFragment.initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }

    private void comment() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", diaryId);
        hm.put("content", circleDiarySeeReply.getText().toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                circleDiarySeeReply.clearFocus();
                circleDiarySeeReply.setText("");
                itemReplyFragment.initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }
}
