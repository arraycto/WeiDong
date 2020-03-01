package cn.trunch.weidong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.http.Api;
import cn.trunch.weidong.http.ApiListener;
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.http.UniteApi;
import cn.trunch.weidong.vo.ComUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ItemReplyAdapter extends RecyclerView.Adapter<ItemReplyAdapter.ViewHolder> {

    private Context context;
    private List<ComUserVO> replies = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View replyView;
        private ImageView replyUHead;
        private TextView replyUName;
        private TextView replyTitle;
        private TextView replyContent;
        private TextView replyInfo;
        private LikeButton replyLikeBtn;
        private TextView replyLikeNum;

        public ViewHolder(@NonNull View view) {
            super(view);
            replyView = view;
            replyUHead = view.findViewById(R.id.replyUHead);
            replyUName = view.findViewById(R.id.replyUName);
            replyTitle = view.findViewById(R.id.replyTitle);
            replyContent = view.findViewById(R.id.replyContent);
            replyInfo = view.findViewById(R.id.replyInfo);
            replyLikeBtn = view.findViewById(R.id.replyLikeBtn);
            replyLikeNum = view.findViewById(R.id.replyLikeNum);
        }
    }

    public ItemReplyAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<ComUserVO> replies) {
        if (replies != null) {
            this.replies.clear();
            this.replies.addAll(replies);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<ComUserVO> replies) {
        if (replies != null) {
            this.replies.addAll(replies);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_reply_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.replyLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                postLike(viewHolder.getAdapterPosition() - 1);
                viewHolder.replyLikeNum.setText(
                        String.valueOf(Integer.valueOf(viewHolder.replyLikeNum.getText().toString()) + 1));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                postLike(viewHolder.getAdapterPosition() - 1);
                viewHolder.replyLikeNum.setText(
                        String.valueOf(Integer.valueOf(viewHolder.replyLikeNum.getText().toString()) - 1));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ComUserVO reply = replies.get(i);
        UserEntity user = reply.getUser();
        Glide.with(context)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.replyUHead);
        viewHolder.replyUName.setText(user.getuNickname());
        viewHolder.replyTitle.setText("");
        viewHolder.replyContent.setText(reply.getComContent());
        viewHolder.replyInfo.setText((i + 1) + "楼 " + reply.getComTime().substring(5, 16));
        viewHolder.replyLikeBtn.setLiked((reply.getRepository() == 1));
        viewHolder.replyLikeNum.setText(String.valueOf(reply.getComLikeNum()));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    private void postLike(int i) {
        ComUserVO reply = replies.get(i);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", ApiUtil.USER_TOKEN);
        hashMap.put("id", reply.getComId());
        hashMap.put("type", "2");
        new UniteApi(ApiUtil.LIKE_ADD, hashMap).post(new ApiListener() {
            @Override
            public void success(Api api) {
            }

            @Override
            public void failure(Api api) {
                DialogUIUtils.showToast("操作失败，请重试");
            }
        });
    }
}
