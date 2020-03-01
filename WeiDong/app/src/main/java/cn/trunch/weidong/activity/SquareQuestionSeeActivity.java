package cn.trunch.weidong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.util.StatusBarUtil;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.fragment.ItemLikeFragment;
import cn.trunch.weidong.fragment.ItemReplyFragment;
import cn.trunch.weidong.util.InputUtil;
import cn.trunch.weidong.util.TextUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareQuestionSeeActivity extends AppCompatActivity {

    private String did;
    private DiaryUserVO question;
    // 头部
    private ImageView questionSeeBackBtn;
    private ImageView questionSeeUHead;
    private TextView questionSeeUName;
    private TextView questionSeeUInfo;
    private TextView questionSeeInfo;
    private TextView questionSeeTime;
    private TextView questionSeeTitle;
    private TextView questionSeeContent;
    private TextView questionSeeFollowBtn;
    private NineGridView questionSeeNine;
    private TextView questionSeeField;
    private TextView questionSeeBounty;
    // 下部导航
    private String[] titles = {"回答", "点赞"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private SlidingTabLayout questionSeeNav;
    private ViewPager questionSeePager;
    private ItemReplyFragment itemReplyFragment;
    private ItemLikeFragment itemLikeFragment;
    // 底部操作
    private View questionSeeCover;
    private LinearLayout questionSeeReplyBox;
    private LikeButton questionSeeLikeBtn;
    private LikeButton questionSeeCollectBtn;
    private EditText questionSeeReply;
    private TextView questionSeeReplySendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_question_see);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        did = getIntent().getStringExtra("did");

        init();
        initNav();
        initListener();
        initHeadData();

    }

    private void init() {
        // 顶部
        questionSeeBackBtn = findViewById(R.id.questionSeeBackBtn);
        questionSeeUHead = findViewById(R.id.questionSeeUHead);
        questionSeeUName = findViewById(R.id.questionSeeUName);
        questionSeeUInfo = findViewById(R.id.questionSeeUInfo);
        questionSeeTime = findViewById(R.id.questionSeeTime);
        questionSeeInfo = findViewById(R.id.questionSeeInfo);
        questionSeeTitle = findViewById(R.id.questionSeeTitle);
        TextUtil.setBold(questionSeeTitle);
        questionSeeContent = findViewById(R.id.questionSeeContent);
        questionSeeFollowBtn = findViewById(R.id.questionSeeFollowBtn);
        questionSeeNine = findViewById(R.id.questionSeeNine);
        questionSeeField = findViewById(R.id.questionSeeField);
        questionSeeBounty = findViewById(R.id.questionSeeBounty);
        // 下部导航
        questionSeeNav = findViewById(R.id.questionSeeNav);
        questionSeePager = findViewById(R.id.questionSeePager);
        // 底部操作
        questionSeeCover = findViewById(R.id.questionSeeCover);
        questionSeeReplyBox = findViewById(R.id.questionSeeReplyBox);
        questionSeeLikeBtn = findViewById(R.id.questionSeeLikeBtn);
        questionSeeCollectBtn = findViewById(R.id.questionSeeCollectBtn);
        questionSeeReply = findViewById(R.id.questionSeeReply);
        questionSeeReplySendBtn = findViewById(R.id.questionSeeReplySendBtn);
    }

    private void initNav() {
        itemReplyFragment = ItemReplyFragment.newInstance(did);
        itemLikeFragment = ItemLikeFragment.newInstance(did);
        fragments.add(itemReplyFragment);
        fragments.add(itemLikeFragment);
        //绑定TabLayout和ViewPager
        questionSeeNav.setViewPager(questionSeePager, titles, this, fragments);
    }

    private void initListener() {
        questionSeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        questionSeeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionSeeReply.clearFocus();
            }
        });
        questionSeeReplyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拦截事件
            }
        });
        questionSeeReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.questionSeeReply && hasFocus) {
                    questionSeeCover.setVisibility(View.VISIBLE);
                    questionSeeLikeBtn.setVisibility(View.GONE);
                    questionSeeCollectBtn.setVisibility(View.GONE);
                    questionSeeReplySendBtn.setVisibility(View.VISIBLE);
                    questionSeeReply.setMinLines(3);
                    questionSeeReply.setMaxLines(10);
                    InputUtil.showInput(questionSeeReply);
                } else {
                    questionSeeCover.setVisibility(View.GONE);
                    questionSeeLikeBtn.setVisibility(View.VISIBLE);
                    questionSeeCollectBtn.setVisibility(View.VISIBLE);
                    questionSeeReplySendBtn.setVisibility(View.GONE);
                    questionSeeReply.setMinLines(1);
                    questionSeeReply.setMaxLines(1);
                    InputUtil.hideInput(SquareQuestionSeeActivity.this);
                }
            }
        });
        questionSeeReply.addTextChangedListener(new TextWatcher() {
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
                    questionSeeReplySendBtn.setEnabled(false);
                    questionSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                } else {
                    questionSeeReplySendBtn.setEnabled(true);
                    questionSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                }
            }
        });
        questionSeeReplySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        questionSeeLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like();
            }
        });
        questionSeeCollectBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        questionSeeFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionSeeFollowBtn.setText("已关注");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                questionSeeFollowBtn.setText(" + 关注");
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initHeadData() {
        question = new DiaryUserVO();
        question.setRepositity(3);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                Gson gson = new Gson();
                UniteApi u = (UniteApi) api;
                try {
                    question = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //显示数据
                UserEntity user = question.getUser();
                Glide.with(questionSeeUHead)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(questionSeeUHead);
                questionSeeUName.setText(user.getuNickname());
                questionSeeUInfo.setText(user.getuRank() + "级达人");
                questionSeeInfo.setText(question.getDiaryCommentNum() + "个评论 " +
                        question.getDiaryReadNum() + "次浏览 "
                        + question.getDiaryLikeNum() + "个人喜欢");
                questionSeeTime.setText(question.getDiaryTime());
                questionSeeTitle.setText(question.getDiaryTitle());
                questionSeeContent.setText(question.getDiaryContent());
                questionSeeBounty.setText(question.getDiaryReward() + ".00");
                questionSeeLikeBtn.setLiked(question.getIsLike() == 1);
                questionSeeField.setText(question.getDiaryLable());
                //显示结束
                //图片开始
                List<ImageInfo> imageInfos = new ArrayList<>();
                try {
                    if (question.getImg() != null)
                        for (String imageURL : question.getImg()) {
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setThumbnailUrl(imageURL);
                            imageInfo.setBigImageUrl(imageURL);
                            imageInfos.add(imageInfo);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUIUtils.showToastCenter("未知错误 ＞.＜");
                }
                questionSeeNine.setAdapter(new NineGridViewClickAdapter(SquareQuestionSeeActivity.this, imageInfos));
                //end
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
        hm.put("id", did);
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                itemLikeFragment.initData();
            }

            @Override
            public void failure(Api api) {
            }
        });
    }

    private void comment() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("did", did);
        hm.put("content", questionSeeReply.getText().toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                questionSeeReply.clearFocus();
                questionSeeReply.setText("");
                itemReplyFragment.initData();
            }

            @Override
            public void failure(Api api) {
            }
        });
    }

}
