package cn.trunch.weidong.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.trunch.weidong.activity.SquareConsultHomeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareConsultExpertAdapter extends RecyclerView.Adapter<SquareConsultExpertAdapter.ViewHolder> {

    private Context context;
    private List<UserEntity> users = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View expertView;
        private ImageView expertAvatar;
        private TextView expertName;
        private TextView expertInfo;
        private TextView expertIntro;
        private TextView expertGoBtn;

        public ViewHolder(@NonNull View view) {
            super(view);
            expertView = view;
            expertAvatar = view.findViewById(R.id.consultExpertAvatar);
            expertName = view.findViewById(R.id.consultExpertName);
            TextUtil.setBold(expertName);
            expertInfo = view.findViewById(R.id.consultExpertInfo);
            expertIntro = view.findViewById(R.id.consultExpertIntro);
            expertGoBtn = view.findViewById(R.id.consultExpertGoBtn);
        }
    }

    public SquareConsultExpertAdapter(Context context) {
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
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_square_consult_expert_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.expertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity user = users.get(viewHolder.getAdapterPosition() - 1);
                Intent intent = new Intent(viewGroup.getContext()
                        , SquareConsultHomeActivity.class);
                intent.putExtra("userId", user.getuId());
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserEntity user = users.get(i);
        Glide.with(viewHolder.expertAvatar.getContext())
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.expertAvatar);
        viewHolder.expertName.setText(user.getuNickname());
        viewHolder.expertInfo.setText(user.getuRank() + "级白菜");
        viewHolder.expertIntro.setText(user.getuSelfdes());
        viewHolder.expertGoBtn.setText("免费咨询");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
