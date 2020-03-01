package cn.trunch.weidong.dialog;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dou361.dialogui.DialogUIUtils;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.PathUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MatchAddDialog extends BottomSheetDialog {

    private View view;
    private Context context;

    private ImageView matchAddAvatar;
    private EditText matchAddName;
    private EditText matchAddTime;
    private EditText matchAddAddress;
    private EditText matchAddDesc;
    private Button matchAddOkBtn;

    private OnImgSelectListener onImgSelectListener;


    public MatchAddDialog(@NonNull Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_match_add, null, false);
        this.context = context;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        init();
        initListener();
        DialogUIUtils.init(context);
    }

    private void init() {
        matchAddAvatar = findViewById(R.id.matchAddAvatar);
        matchAddName = findViewById(R.id.matchAddName);
        matchAddTime = findViewById(R.id.matchAddTime);
        matchAddAddress = findViewById(R.id.matchAddAddress);
        matchAddDesc = findViewById(R.id.matchAddDesc);
        matchAddOkBtn = findViewById(R.id.matchAddOkBtn);
    }

    public void setOnImgSelectListener(OnImgSelectListener onImgSelectListener) {
        this.onImgSelectListener = onImgSelectListener;
    }

    private void initListener() {
        matchAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgSelectListener.onImgSelect();
            }
        });
        matchAddOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMatchRex()) {

                } else {
                    DialogUIUtils.showToastCenter("请输入活动完整信息");
                }
            }
        });
    }

    public void setImg(Uri uri) {
        String path = PathUtil.getFilePath(context, uri);
        Glide.with(context)
                .load(uri)
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CropSquareTransformation(),
                        new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                )))
                .into(matchAddAvatar);
    }

    private boolean isMatchRex() {
        return !(matchAddName.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0
                || matchAddTime.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0
                || matchAddDesc.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0
                || matchAddAddress.getText().toString()
                .replaceAll(" ", "")
                .replaceAll("\n", "").length() == 0);
    }

    public interface OnImgSelectListener {
        void onImgSelect();
    }
}