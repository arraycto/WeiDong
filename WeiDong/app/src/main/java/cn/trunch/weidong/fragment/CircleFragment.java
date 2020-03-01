package cn.trunch.weidong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.CirclePhotoAddActivity;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.activity.SettingActivity;
import cn.trunch.weidong.activity.SportDiaryAddActivity;
import cn.trunch.weidong.dialog.CircleAddDialog;
import cn.trunch.weidong.http.ApiUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CircleFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private RelativeLayout circleSettingGo;
    private ImageView circleAvatar;
    private SlidingTabLayout circleTabLayout;
    private ViewPager circleViewPager;
    private FloatingActionButton circleAddBtn;

    private CircleAddDialog circleAddDialog;

    private String[] titles = {"推荐", "同校", "热门"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private CirclePostFragment circlePostFragment1;
    private CirclePostFragment circlePostFragment2;
    private CirclePostFragment circlePostFragment3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_circle, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        //绑定TabLayout和ViewPager
        circleTabLayout.setViewPager(circleViewPager, titles, activity, fragments);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(ApiUtil.USER_AVATAR)
                .apply(bitmapTransform(new CircleCrop()))
                .into(circleAvatar);
    }

    private void init() {
        circleSettingGo = view.findViewById(R.id.circleSettingGo);
        circleAvatar = view.findViewById(R.id.circleAvatar);
        circleTabLayout = view.findViewById(R.id.circleTabLayout);
        circleViewPager = view.findViewById(R.id.circleViewPager);
        circleAddBtn = view.findViewById(R.id.circleAddBtn);
        circleAddDialog = new CircleAddDialog(context);
        circlePostFragment1 = CirclePostFragment.newInstance(1);
        circlePostFragment2 = CirclePostFragment.newInstance(2);
        circlePostFragment3 = CirclePostFragment.newInstance(3);
        fragments.add(circlePostFragment1);
        fragments.add(circlePostFragment2);
        fragments.add(circlePostFragment3);
    }

    private void initListener() {
        circleSettingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        circleAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleAddDialog.show();
            }
        });
        circleAddDialog.setOnClickListener(new CircleAddDialog.OnClickListener() {
            @Override
            public void onPhotoClick() {
                Intent intent = new Intent(context, CirclePhotoAddActivity.class);
                startActivity(intent);
                circleAddDialog.dismiss();
            }

            @Override
            public void onDiaryClick() {
                Intent intent = new Intent(context, SportDiaryAddActivity.class);
                startActivity(intent);
                circleAddDialog.dismiss();
            }
        });
    }
}
