package cn.trunch.weidong.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import cn.trunch.weidong.R;
import me.gujun.android.taggroup.TagGroup;

public class LabelAddDialog extends BottomSheetDialog {

    private View view;
    private Context context;

    private String title;
    private TextView labelAddTitle;
    private TagGroup labelAddGroup;
    private OnLabelChangeListener onLabelChangeListener;

    private String labels = "";

    public LabelAddDialog(@NonNull Context context, String title) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_label_add, null, false);
        this.context = context;
        this.title = title;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        init();
        initListener();
    }

    public void setOnLabelChangeListener(OnLabelChangeListener onLabelChangeListener) {
        this.onLabelChangeListener = onLabelChangeListener;
    }

    private void init() {
        labelAddTitle = view.findViewById(R.id.labelAddTitle);
        labelAddTitle.setText(title);
        labelAddGroup = view.findViewById(R.id.labelAddGroup);
    }

    private void initListener() {
        labelAddGroup.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend(TagGroup tagGroup, String tag) {
                if (tagGroup.getTags().length <= 5) {
                    if (onLabelChangeListener != null) {
                        onLabelChangeListener.onChange(labels + " #" + tag + "# ");
                    }
                } else {

                }
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                if (onLabelChangeListener != null) {
                    onLabelChangeListener.onChange(labels);
                }
            }
        });
    }

    public interface OnLabelChangeListener {
        void onChange(String labels);
    }
}
