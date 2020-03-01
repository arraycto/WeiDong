package cn.trunch.weidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.net.URLDecoder;
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

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CirclePhotoSeeActivity extends AppCompatActivity {

    private String postId;
    private DiaryUserVO post;
    // 头部
    private ImageView circlePhotoSeeBackBtn;
    private ImageView circlePhotoSeeUHead;
    private TextView circlePhotoSeeUName;
    private TextView circlePhotoSeeUInfo;
    private TextView circlePhotoSeeInfo;
    private TextView circlePhotoSeeTime;
    private TextView circlePhotoSeeContent;
    private NineGridView circlePhotoSeeNine;
    // 下部导航
    private String[] titles = {"回答", "点赞"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ItemReplyFragment itemReplyFragment;
    private ItemLikeFragment itemLikeFragment;
    private SlidingTabLayout circlePhotoSeeNav;
    private ViewPager circlePhotoSeePager;
    // 底部操作
    private View circlePhotoSeeCover;
    private LinearLayout circlePhotoSeeReplyBox;
    private LikeButton circlePhotoSeeLikeBtn;
    private LikeButton circlePhotoSeeCollectBtn;
    private EditText circlePhotoSeeReply;
    private TextView circlePhotoSeeReplySendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_photo_see);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        postId = getIntent().getStringExtra("postId");

        init();
        initNav();
        initListener();
        initHeadData();

    }

    private void init() {
        // 顶部
        circlePhotoSeeBackBtn = findViewById(R.id.circlePhotoSeeBackBtn);
        circlePhotoSeeUHead = findViewById(R.id.circlePhotoSeeUHead);
        circlePhotoSeeUName = findViewById(R.id.circlePhotoSeeUName);
        circlePhotoSeeUInfo = findViewById(R.id.circlePhotoSeeUInfo);
        circlePhotoSeeContent = findViewById(R.id.circlePhotoSeeContent);
        circlePhotoSeeNine = findViewById(R.id.circlePhotoSeeNine);
        circlePhotoSeeTime = findViewById(R.id.circlePhotoSeeTime);
        circlePhotoSeeInfo = findViewById(R.id.circlePhotoSeeInfo);
        // 下部导航
        circlePhotoSeeNav = findViewById(R.id.circlePhotoSeeNav);
        circlePhotoSeePager = findViewById(R.id.circlePhotoSeePager);
        // 底部操作
        circlePhotoSeeCover = findViewById(R.id.circlePhotoSeeCover);
        circlePhotoSeeReplyBox = findViewById(R.id.circlePhotoSeeReplyBox);
        circlePhotoSeeLikeBtn = findViewById(R.id.circlePhotoSeeLikeBtn);
        circlePhotoSeeCollectBtn = findViewById(R.id.circlePhotoSeeCollectBtn);
        circlePhotoSeeReply = findViewById(R.id.circlePhotoSeeReply);
        circlePhotoSeeReplySendBtn = findViewById(R.id.circlePhotoSeeReplySendBtn);
    }

    private void initNav() {
        itemReplyFragment = ItemReplyFragment.newInstance(postId);
        itemLikeFragment = ItemLikeFragment.newInstance(postId);
        fragments.add(itemReplyFragment);
        fragments.add(itemLikeFragment);
        //绑定TabLayout和ViewPager
        circlePhotoSeeNav.setViewPager(circlePhotoSeePager, titles, this, fragments);
    }

    private void initListener() {
        circlePhotoSeeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        circlePhotoSeeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circlePhotoSeeReply.clearFocus();
            }
        });
        circlePhotoSeeReplyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拦截事件
            }
        });
        circlePhotoSeeReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.circlePhotoSeeReply && hasFocus) {
                    circlePhotoSeeCover.setVisibility(View.VISIBLE);
                    circlePhotoSeeLikeBtn.setVisibility(View.GONE);
                    circlePhotoSeeCollectBtn.setVisibility(View.GONE);
                    circlePhotoSeeReplySendBtn.setVisibility(View.VISIBLE);
                    circlePhotoSeeReply.setMinLines(3);
                    circlePhotoSeeReply.setMaxLines(10);
                    InputUtil.showInput(circlePhotoSeeReply);
                } else {
                    circlePhotoSeeCover.setVisibility(View.GONE);
                    circlePhotoSeeLikeBtn.setVisibility(View.VISIBLE);
                    circlePhotoSeeCollectBtn.setVisibility(View.VISIBLE);
                    circlePhotoSeeReplySendBtn.setVisibility(View.GONE);
                    circlePhotoSeeReply.setMinLines(1);
                    circlePhotoSeeReply.setMaxLines(1);
                    InputUtil.hideInput(CirclePhotoSeeActivity.this);
                }
            }
        });
        circlePhotoSeeReply.addTextChangedListener(new TextWatcher() {
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
                    circlePhotoSeeReplySendBtn.setEnabled(false);
                    circlePhotoSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorDefaultText));
                } else {
                    circlePhotoSeeReplySendBtn.setEnabled(true);
                    circlePhotoSeeReplySendBtn.setTextColor(getResources().getColor(R.color.colorTheme));
                }
            }
        });
        circlePhotoSeeReplySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        circlePhotoSeeLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like();
            }
        });
        circlePhotoSeeCollectBtn.setOnLikeListener(new OnLikeListener() {
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
        hm.put("did", String.valueOf(postId));
        new UniteApi(ApiUtil.DIARY_INFO, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                UniteApi u = (UniteApi) api;
                Gson gson = new Gson();
                try {
                    post = gson.fromJson(u.getJsonData().get(0).toString(), DiaryUserVO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserEntity user = post.getUser();
                Glide.with(circlePhotoSeeUHead)
                        .load(user.getuAvatar())
                        .apply(bitmapTransform(new CircleCrop()))
                        .into(circlePhotoSeeUHead);
                circlePhotoSeeUName.setText(user.getuNickname());
                circlePhotoSeeUInfo.setText(user.getuRank() + "级达人");
                circlePhotoSeeTime.setText(post.getDiaryTime());
                circlePhotoSeeContent.setText(post.getDiaryContent());
                circlePhotoSeeLikeBtn.setLiked(post.getIsLike() == 1);
                //九图
                List<ImageInfo> imageInfos = new ArrayList<>();
                if (post.getImg() != null)
                    for (String imageURL : post.getImg()) {
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setThumbnailUrl(imageURL);
                        imageInfo.setBigImageUrl(imageURL);
                        imageInfos.add(imageInfo);
                    }
                circlePhotoSeeNine.setAdapter(new NineGridViewClickAdapter(CirclePhotoSeeActivity.this, imageInfos));
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
        hm.put("id", postId);
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
        hm.put("did", postId);
        hm.put("content", circlePhotoSeeReply.getText().toString());
        new UniteApi(ApiUtil.COM_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                circlePhotoSeeReply.clearFocus();
                circlePhotoSeeReply.setText("");
                itemReplyFragment.initData();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }
}
