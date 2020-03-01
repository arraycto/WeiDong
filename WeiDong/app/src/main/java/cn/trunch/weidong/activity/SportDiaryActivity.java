package cn.trunch.weidong.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import cn.trunch.weidong.R;
import cn.trunch.weidong.fragment.SportDiaryFragment;
import cn.trunch.weidong.util.StatusBarUtil;

public class SportDiaryActivity extends AppCompatActivity {

    private ImageView diaryBackBtn;
    private SlidingTabLayout diaryTabLayout;
    private ViewPager diaryViewPager;
    private FloatingActionButton diaryAddBtn;
    private String[] titles = {"公开日记", "私密日记"}; //去除3：活动
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_diary);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        init();
        initListener();
        //绑定TabLayout和ViewPager
        diaryTabLayout.setViewPager(diaryViewPager, titles, this, fragments);
    }

    private void init() {
        diaryAddBtn = findViewById(R.id.diaryAddBtn);
        diaryBackBtn = findViewById(R.id.diaryBackBtn);
        diaryTabLayout = findViewById(R.id.diaryTabLayout);
        diaryViewPager = findViewById(R.id.diaryViewPager);
        fragments.add(SportDiaryFragment.newInstance(0));
        fragments.add(SportDiaryFragment.newInstance(1));
    }

    private void initListener() {
        diaryBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        diaryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SportDiaryActivity.this, SportDiaryAddActivity.class);
                overridePendingTransition(R.anim.bottom_in, R.anim.anim_static);
                startActivity(intent);
            }
        });
    }
}
