package cn.trunch.weidong.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import cn.trunch.weidong.R;
import me.gujun.android.taggroup.TagGroup;

public class AnonymousAddDialog extends BottomSheetDialog {

    private View view;
    private Context context;

    private String title;
    private TextView anonymousAddTitle;
    private TextView anonymousAddInfo;
    private SwitchButton anonymousAddSwitch;

    private OnAnonymousChangeListener onAnonymousChangeListener;

    public AnonymousAddDialog(@NonNull Context context, String title) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_anonymous_add, null, false);
        this.context = context;
        this.title = title;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        init();
        initListener();
    }

    public void setOnanonymousChangeListener(OnAnonymousChangeListener onAnonymousChangeListener) {
        this.onAnonymousChangeListener = onAnonymousChangeListener;
    }

    private void init() {
        anonymousAddTitle = view.findViewById(R.id.anonymousAddTitle);
        anonymousAddTitle.setText(title);
        anonymousAddInfo = view.findViewById(R.id.anonymousAddInfo);
        anonymousAddSwitch = view.findViewById(R.id.anonymousAddSwitch);
    }

    private void initListener() {
        anonymousAddSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                anonymousAddInfo.setText(isChecked ? "公开" : "私密");
                onAnonymousChangeListener.onChange(isChecked);
            }
        });
    }

    public interface OnAnonymousChangeListener {
        void onChange(boolean anonymous);
    }
}
