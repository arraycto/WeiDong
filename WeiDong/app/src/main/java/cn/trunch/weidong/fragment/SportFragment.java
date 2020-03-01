package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SettingActivity;
import cn.trunch.weidong.activity.SportDiaryActivity;
import cn.trunch.weidong.activity.SportHistoryActivity;
import cn.trunch.weidong.http.ApiUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SportFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private RelativeLayout sportSettingGo;
    private ImageView sportAvatar;
    private ImageView sportHistoryBtn;
    private ImageView sportDiaryBtn;

    private SegmentTabLayout sportTabLayout;
    private ViewPager sportViewPager;
    private String[] titles = {"计步", "计时"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        //绑定TabLayout和ViewPager
        initNav();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(sportAvatar);
    }

    private void init() {
        sportSettingGo = view.findViewById(R.id.sportSettingGo);
        sportAvatar = view.findViewById(R.id.sportAvatar);
        sportHistoryBtn = view.findViewById(R.id.sportHistoryBtn);
        sportDiaryBtn = view.findViewById(R.id.sportDiaryBtn);
        sportTabLayout = view.findViewById(R.id.sportTabLayout);
        sportViewPager = view.findViewById(R.id.sportViewPager);
        fragments.add(new SportStepFragment());
        fragments.add(new SportTimeFragment());
        sportTabLayout.setTabData(titles);
    }

    private void initListener() {
        sportSettingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        sportHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportHistoryActivity.class);
                context.startActivity(intent);
            }
        });
        sportDiaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportDiaryActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initNav() {
        sportViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager()));

        sportTabLayout.setTabData(titles);
        sportTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                sportViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        sportViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sportTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sportViewPager.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
