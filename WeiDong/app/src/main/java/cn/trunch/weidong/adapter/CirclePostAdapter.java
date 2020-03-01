package cn.trunch.weidong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.CircleDiarySeeActivity;
import cn.trunch.weidong.activity.CirclePhotoSeeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CirclePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DiaryUserVO> posts = new ArrayList<>();

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private View circlePhotoView;
        private ImageView circlePhotoUHead;
        private TextView circlePhotoUName;
        private TextView circlePhotoUInfo;
        private TextView circlePhotoInfo;
        private TextView circlePhotoContent;
        private NineGridView circlePhotoNine;
        private TextView circlePhotoLabel;
        private LikeButton circlePhotoLikeBtn;
        private TextView circlePhotoLikeNum;
        private LinearLayout circlePhotoCommentBtn;
        private TextView circlePhotoCommentNum;

        public PhotoViewHolder(@NonNull View view) {
            super(view);
            circlePhotoView = view;
            circlePhotoUHead = view.findViewById(R.id.circlePhotoUHead);
            circlePhotoUName = view.findViewById(R.id.circlePhotoUName);
            TextUtil.setBold(circlePhotoUName);
            circlePhotoUInfo = view.findViewById(R.id.circlePhotoUInfo);
            circlePhotoInfo = view.findViewById(R.id.circlePhotoInfo);
            circlePhotoContent = view.findViewById(R.id.circlePhotoContent);
            circlePhotoNine = view.findViewById(R.id.circlePhotoNine);
            circlePhotoLabel = view.findViewById(R.id.circlePhotoLabel);
            circlePhotoLikeBtn = view.findViewById(R.id.circlePhotoLikeBtn);
            circlePhotoLikeNum = view.findViewById(R.id.circlePhotoLikeNum);
            circlePhotoCommentBtn = view.findViewById(R.id.circlePhotoCommentBtn);
            circlePhotoCommentNum = view.findViewById(R.id.circlePhotoCommentNum);

        }
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        private View circleDiaryView;
        private ImageView circleDiaryUHead;
        private TextView circleDiaryUName;
        private TextView circleDiaryUInfo;
        private TextView circleDiaryInfo;
        private TextView circleDiaryTitle;
        private TextView circleDiaryContent;
        private ImageView circleDiaryPreview;
        private TextView circleDiaryLabel;
        private LikeButton circleDiaryLikeBtn;
        private TextView circleDiaryLikeNum;
        private LinearLayout circleDiaryCommentBtn;
        private TextView circleDiaryCommentNum;

        public DiaryViewHolder(@NonNull View view) {
            super(view);
            circleDiaryView = view;
            circleDiaryUHead = view.findViewById(R.id.circleDiaryUHead);
            circleDiaryUName = view.findViewById(R.id.circleDiaryUName);
            TextUtil.setBold(circleDiaryUName);
            circleDiaryUInfo = view.findViewById(R.id.circleDiaryUInfo);
            circleDiaryInfo = view.findViewById(R.id.circleDiaryInfo);
            circleDiaryTitle = view.findViewById(R.id.circleDiaryTitle);
            TextUtil.setBold(circleDiaryTitle);
            circleDiaryContent = view.findViewById(R.id.circleDiaryContent);
            circleDiaryPreview = view.findViewById(R.id.circleDiaryPreview);
            circleDiaryLabel = view.findViewById(R.id.circleDiaryLabel);
            circleDiaryLikeBtn = view.findViewById(R.id.circleDiaryLikeBtn);
            circleDiaryLikeNum = view.findViewById(R.id.circleDiaryLikeNum);
            circleDiaryCommentBtn = view.findViewById(R.id.circleDiaryCommentBtn);
            circleDiaryCommentNum = view.findViewById(R.id.circleDiaryCommentNum);
        }
    }

    public CirclePostAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<DiaryUserVO> posts) {
        if (posts != null) {
            this.posts.clear();
            this.posts.addAll(posts);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<DiaryUserVO> posts) {
        if (posts != null) {
            this.posts.addAll(posts);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        // 这个i是item类型 1 动态 5 日记
        if (i == 1) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_circle_photo_item, viewGroup, false);
            final PhotoViewHolder viewHolder = new PhotoViewHolder(view);
            viewHolder.circlePhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiaryUserVO post = posts.get(viewHolder.getAdapterPosition() - 1);
                    Intent intent = new Intent(context, CirclePhotoSeeActivity.class);
                    intent.putExtra("postId", post.getDiaryId());
                    context.startActivity(intent);
                }
            });
            viewHolder.circlePhotoLikeBtn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    like(viewHolder.getAdapterPosition() - 1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    like(viewHolder.getAdapterPosition() - 1);
                }
            });
            return viewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_circle_diary_item, viewGroup, false);
            final DiaryViewHolder viewHolder = new DiaryViewHolder(view);
            viewHolder.circleDiaryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiaryUserVO diary = posts.get(viewHolder.getAdapterPosition() - 1);
                    Intent intent = new Intent(context, CircleDiarySeeActivity.class);
                    intent.putExtra("diaryId", diary.getDiaryId());
                    context.startActivity(intent);
                }
            });
            viewHolder.circleDiaryLikeBtn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    like(viewHolder.getAdapterPosition() - 1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    like(viewHolder.getAdapterPosition() - 1);
                }
            });
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //这个i是item位置
        DiaryUserVO post = posts.get(i); // 发布的话题
        UserEntity postUser = post.getUser(); // 两种类型的发布人
        if (post.getDiaryType() == 1) {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) viewHolder;
            Glide.with(context)
                    .load(postUser.getuAvatar())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(photoViewHolder.circlePhotoUHead);
            photoViewHolder.circlePhotoUName.setText(postUser.getuNickname());
            photoViewHolder.circlePhotoUInfo.setText(postUser.getuRank() + "级 发了照片");
            photoViewHolder.circlePhotoInfo.setText(post.getDiaryTime());
            photoViewHolder.circlePhotoContent.setText(post.getDiaryContent());
            List<ImageInfo> imageInfos = new ArrayList<>();
            for (String imageURL : post.getImg()) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setThumbnailUrl(imageURL);
                imageInfo.setBigImageUrl(imageURL);
                imageInfos.add(imageInfo);
            }
            photoViewHolder.circlePhotoNine.setAdapter(new NineGridViewClickAdapter(context, imageInfos));
            photoViewHolder.circlePhotoLabel.setText(post.getDiaryLable());
            photoViewHolder.circlePhotoLikeBtn.setLiked(post.getIsLike() == 1);
            photoViewHolder.circlePhotoLikeNum.setText(String.valueOf(post.getDiaryLikeNum()));
            photoViewHolder.circlePhotoCommentNum.setText(String.valueOf(post.getDiaryCommentNum()));
        } else {
            DiaryViewHolder diaryViewHolder = (DiaryViewHolder) viewHolder;
            Glide.with(context)
                    .load(postUser.getuAvatar())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(diaryViewHolder.circleDiaryUHead);
            diaryViewHolder.circleDiaryUName.setText(postUser.getuNickname());
            diaryViewHolder.circleDiaryUInfo.setText(postUser.getuRank() + "级 发了照片");
            diaryViewHolder.circleDiaryInfo.setText(post.getDiaryTime());
            diaryViewHolder.circleDiaryTitle.setText(post.getDiaryTitle());
            diaryViewHolder.circleDiaryContent.setText(post.getDiaryContentPreview());
            if (post.getDiaryImgPreview().contains("default"))
                diaryViewHolder.circleDiaryPreview.setVisibility(View.GONE);
            else {
                diaryViewHolder.circleDiaryPreview.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(post.getDiaryImgPreview())
                        .apply(bitmapTransform(new MultiTransformation<>(
                                new CropTransformation(105, 70, CropTransformation.CropType.CENTER),
                                new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL)
                        )))
                        .into(diaryViewHolder.circleDiaryPreview);
            }
            diaryViewHolder.circleDiaryLabel.setText(post.getDiaryLable());
            diaryViewHolder.circleDiaryLikeBtn.setLiked(post.getIsLike() == 1);
            diaryViewHolder.circleDiaryLikeNum.setText(String.valueOf(post.getDiaryLikeNum()));
            diaryViewHolder.circleDiaryCommentNum.setText(String.valueOf(post.getDiaryCommentNum()));
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position).getDiaryType();
    }

    private void like(final int position) {
        final DiaryUserVO post = posts.get(position);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", ApiUtil.USER_TOKEN);
        hm.put("id", post.getDiaryId());
        hm.put("type", "1");
        new UniteApi(ApiUtil.LIKE_ADD, hm).post(new ApiListener() {
            @Override
            public void success(Api api) {
                posts.get(position).setIsLike(post.getIsLike() == 1 ? 0 : 1);
                posts.get(position).setDiaryLikeNum(post.getIsLike() == 1 ? post.getDiaryLikeNum() + 1 : post.getDiaryLikeNum() - 1);
                notifyDataSetChanged();
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToastCenter("网络开小差了");
            }
        });
    }

}
