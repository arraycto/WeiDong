package cn.trunch.weidong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dou361.dialogui.DialogUIUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SettingAccountActivity;
import cn.trunch.weidong.util.Glide4Engine;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ImageAddAdapter extends RecyclerView.Adapter<ImageAddAdapter.ViewHolder> {

    private static final int REQUEST_IMAGE_CODE = 1;
    private Context context;
    private Activity activity;
    private Fragment fragment;
    private int imageNum = 9;
    private List<Uri> uriList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView imageAdd;
        private ImageView imageDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            imageAdd = view.findViewById(R.id.imageAdd);
            imageDelete = view.findViewById(R.id.imageDelete);
        }
    }

    public ImageAddAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public ImageAddAdapter(Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
    }

    public void updateData(List<Uri> uriList) {
        if (uriList != null) {
            this.uriList.addAll(uriList);
            notifyItemRangeChanged(this.uriList.size() - uriList.size(), uriList.size() + 1);
        }
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public List<Uri> getData() {
        return uriList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_image_add_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewHolder.getAdapterPosition() == uriList.size())
                    return true;
                viewHolder.imageDelete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.getAdapterPosition() == uriList.size()) {
                    AndPermission.with(activity)
                            .runtime()
                            .permission(Permission.Group.STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Matisse.from(activity)
                                            .choose(MimeType.ofImage()) // 选择 mime 的类型
                                            .countable(true)
                                            .maxSelectable(imageNum - uriList.size()) // 图片选择的最多数量
                                            .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                            .thumbnailScale(0.85f) // 缩略图的比例
                                            .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                                            .forResult(REQUEST_IMAGE_CODE); // 设置作为标记的请求码
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
            }
        });
        viewHolder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.imageDelete.setVisibility(View.INVISIBLE);
                uriList.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i == uriList.size()) {
            Glide.with(context)
                    .load(R.drawable.bg_image_add)
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(viewHolder.imageAdd);
            return;

        }
        Glide.with(context)
                .load(uriList.get(i))
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CropSquareTransformation(),
                        new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                )))
                .into(viewHolder.imageAdd);
    }

    @Override
    public int getItemCount() {
        return uriList.size() >= imageNum ? imageNum : uriList.size() + 1;
    }
}
