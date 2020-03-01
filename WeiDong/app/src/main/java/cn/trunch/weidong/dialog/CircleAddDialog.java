package cn.trunch.weidong.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.trunch.weidong.R;

public class CircleAddDialog extends BottomSheetDialog {
    private View view;
    private Context context;

    private LinearLayout circlePhotoAddBtn;
    private LinearLayout circleDiaryAddBtn;

    private OnClickListener onClickListener;

    public CircleAddDialog(@NonNull Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_circle_add, null, false);
        this.context = context;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        init();
        initListener();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init() {
        circlePhotoAddBtn = view.findViewById(R.id.circlePhotoAddBtn);
        circleDiaryAddBtn = view.findViewById(R.id.circleDiaryAddBtn);
    }

    private void initListener() {
        circlePhotoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onPhotoClick();
            }
        });
        circleDiaryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onDiaryClick();
            }
        });
    }

    public interface OnClickListener {
        void onPhotoClick();
        void onDiaryClick();
    }
}
