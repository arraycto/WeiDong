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

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ItemLikeAdapter extends RecyclerView.Adapter<ItemLikeAdapter.ViewHolder> {

    private Context context;
    private List<UserEntity> users = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View likeView;
        private ImageView likeUHead;
        private TextView likeUName;
        private TextView likeTime;
        private TextView likeTitle;
        private TextView likeContent;

        public ViewHolder(@NonNull View view) {
            super(view);
            likeView = view;
            likeUHead = view.findViewById(R.id.likeUHead);
            likeUName = view.findViewById(R.id.likeUName);
            TextUtil.setBold(likeUName);
            likeTime = view.findViewById(R.id.likeTime);
            likeTitle = view.findViewById(R.id.likeTitle);
            likeContent = view.findViewById(R.id.likeContent);
        }
    }

    public ItemLikeAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<UserEntity> users) {
        if (users != null) {
            this.users.clear();
            this.users.addAll(users);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<UserEntity> users) {
        if (users != null) {
            this.users.addAll(users);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_like_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserEntity user = users.get(i);
        Glide.with(context)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.likeUHead);
        viewHolder.likeUName.setText(user.getuNickname());
        viewHolder.likeTime.setText("暂无数据");
        viewHolder.likeTitle.setText("");
        viewHolder.likeContent.setText("点赞了该内容");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
