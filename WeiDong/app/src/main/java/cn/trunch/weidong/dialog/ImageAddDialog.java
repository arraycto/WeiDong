package cn.trunch.weidong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SettingAccountActivity;
import cn.trunch.weidong.fragment.SquareQuestionAdd2Fragment;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UploadApi;
import cn.trunch.weidong.util.Glide4Engine;
import cn.trunch.weidong.util.PathUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class


ImageAddDialog extends BottomSheetDialog {

    private final static int REQUEST_PHOTO_CODE = 20202;

    private View view;
    private Context context;
    private SquareQuestionAdd2Fragment fragment;
    private String title;

    private TextView imageAddTitle;
    private TextView imageAddBtn;
    private ImageView imageAdd1;
    private ImageView imageAdd2;
    private ImageView imageAdd3;
    private List<ImageView> imageViews = new ArrayList<>(); //存放ImageView
    private List<Uri> imageUris = new ArrayList<>(); //存放选中照片的Uri
    private List<String> imagePaths = new ArrayList<>(); //存放选中照片真实路径

    private OnImageSelectListener onImageSelectListener;

    private Dialog dialog;

    public ImageAddDialog(@NonNull SquareQuestionAdd2Fragment fragment, String title) {
        super(fragment.getActivity());
        this.context = fragment.getActivity();
        view = LayoutInflater.from(context).inflate(R.layout.dialog_image_add, null, false);
        this.fragment = fragment;
        this.title = title;
        this.setContentView(view);
        this.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        init();
        initListener();
        DialogUIUtils.init(context);
    }

    public void setOnImageSelectListener(OnImageSelectListener onImageSelectListener) {
        this.onImageSelectListener = onImageSelectListener;
    }

    private void init() {
        imageAddTitle = view.findViewById(R.id.imageAddTitle);
        imageAddTitle.setText(title);
        imageAddBtn = view.findViewById(R.id.imageAddBtn);
        imageAdd1 = view.findViewById(R.id.imageAdd1);
        imageAdd2 = view.findViewById(R.id.imageAdd2);
        imageAdd3 = view.findViewById(R.id.imageAdd3);
        imageViews.add(imageAdd1);
        imageViews.add(imageAdd2);
        imageViews.add(imageAdd3);
    }

    private void initListener() {
        imageAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePaths.size() > 0) {
                    ImageAddDialog.this.dismiss();
                    dialog = DialogUIUtils.showLoading(context, "正在上传...", false, false, false, true).show();
                    uploadImage(imagePaths);
                }
            }
        });
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndPermission.with(fragment)
                            .runtime()
                            .permission(Permission.Group.STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Matisse.from(fragment)
                                            .choose(MimeType.ofImage()) // 选择 mime 的类型
                                            .countable(true)
                                            .maxSelectable(3) // 图片选择的最多数量
                                            .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                            .thumbnailScale(0.85f) // 缩略图的比例
                                            .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                                            .forResult(REQUEST_PHOTO_CODE); // 设置作为标记的请求码
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUIUtils.showToastCenter("请前往设置打开权限");
                                }
                            })
                            .start();
                }
            });
        }
    }

    public void setOnResult(int requestCode, int resultCode, Intent data) {
        // 选好照片了
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == RESULT_OK) {
            imageUris = Matisse.obtainResult(data);
            //清空
            for (ImageView imageView : imageViews) {
                imageView.setImageDrawable(null);
            }
            //预显示
            for (int i = 0; i < imageUris.size(); i++) {
                imagePaths.add(PathUtil.getFilePath(context, imageUris.get(i)));
                Glide.with(context)
                        .load(imageUris.get(i))
                        .apply(bitmapTransform(new MultiTransformation<>(
                                new CropSquareTransformation(),
                                new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL)
                        )))
                        .into(imageViews.get(i));
            }
        }
    }

    private void uploadImage(final List<String> imagePaths) {
        new UploadApi(ApiUtil.MULTFILE_UPLOAD, imagePaths, ApiUtil.MULT_POST).uploads(new ApiListener() {
            @Override
            public void success(Api api) {
                UploadApi u = (UploadApi) api;
                Gson gson = new Gson();
                List<String> imgs;
                imgs = gson.fromJson(u.getJsonData().toString(), new TypeToken<List<String>>() {
                }.getType());
                onImageSelectListener.onSelect(imgs.get(0));
                DialogUIUtils.dismiss(dialog);
                imageUris.clear();
                DialogUIUtils.showToastCenter("图片预传成功");
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToastCenter("请检查网络配置");
            }
        });
    }

    public interface OnImageSelectListener {
        void onSelect(String imageUrls);
    }
}
