package cn.trunch.weidong.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.next.easynavigation.view.EasyNavigationBar;


import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.fragment.CircleFragment;
import cn.trunch.weidong.fragment.ExploreFragment;
import cn.trunch.weidong.fragment.SportFragment;
import cn.trunch.weidong.fragment.SquareFragment;
import cn.trunch.weidong.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private String[] tabText = {"运动", "广场", "探索", "圈子"};
    private int[] normalIcon = {R.mipmap.sport_normal, R.mipmap.square_normal, R.mipmap.explore_normal, R.mipmap.circle_normal};
    private int[] selectIcon = {R.mipmap.sport_select, R.mipmap.square_select, R.mipmap.explore_select, R.mipmap.circle_select};
    private List<Fragment> fragments = new ArrayList<>();
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarMode(this, false, R.color.colorTheme);
        //底部导航
        initNav();
    }

    private void initNav() {
        fragments.add(new SportFragment());
        fragments.add(new SquareFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new CircleFragment());
        navigationBar = findViewById(R.id.navigationBar);
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .iconSize(22)     //Tab图标大小
                .tabTextSize(12)   //Tab文字大小
                .normalTextColor(getResources().getColor(R.color.colorDefaultText))   //Tab未选中时字体颜色
                .selectTextColor(getResources().getColor(R.color.colorTheme))   //Tab选中时字体颜色
                .navigationBackground(getResources().getColor(R.color.colorBotBarBack))   //导航栏背景色
                .lineColor(getResources().getColor(R.color.colorTopLine)) //分割线颜色
                .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
                .canScroll(false)    //Viewpager能否左右滑动
//                .anim(Anim.ZoomIn)  //点击Tab时的动画
                .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
                .hintPointTop(-7)
                .hintPointSize(6)    //提示红点的大小
                .msgPointLeft(-10)  //调节数字消息的位置msgPointLeft msgPointTop（看文档说明）
                .msgPointTop(-15)
                .msgPointTextSize(9)  //数字消息中字体大小
                .msgPointSize(18)    //数字消息红色背景的大小
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int i) {
                        if (i == 0) {
                            StatusBarUtil.setStatusBarMode(MainActivity.this, false, R.color.colorTheme);
                        } else {
                            StatusBarUtil.setStatusBarMode(MainActivity.this, true, R.color.colorLight);
                        }
                        return false;
                    }
                })
                .build();
        navigationBar.getmViewPager().setOffscreenPageLimit(4);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
