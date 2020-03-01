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
import com.bumptech.glide.load.MultiTransformation;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SquareGroupSeeActivity;
import cn.trunch.weidong.entity.ActivityEntity;
import cn.trunch.weidong.entity.TeamEntity;
import cn.trunch.weidong.util.TextUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareGroupAdapter extends RecyclerView.Adapter<SquareGroupAdapter.ViewHolder> {

    private Context context;
    private List<TeamEntity> groups = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View groupView;
        private ImageView groupAvatar;
        private TextView groupName;
        private TextView groupSite;
        private TextView groupInfo;
        private TextView groupNum;

        public ViewHolder(View view) {
            super(view);
            groupView = view;
            groupAvatar = view.findViewById(R.id.groupAvatar);
            groupName = view.findViewById(R.id.groupName);
            TextUtil.setBold(groupName);
            groupSite = view.findViewById(R.id.groupSite);
            groupInfo = view.findViewById(R.id.groupInfo);
            groupNum = view.findViewById(R.id.groupNum);
        }
    }

    public SquareGroupAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<TeamEntity> groups) {
        if (groups != null) {
            this.groups.clear();
            this.groups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<TeamEntity> groups) {
        if (groups != null) {
            this.groups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_square_group_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamEntity group = groups.get(viewHolder.getAdapterPosition() - 2);
                Intent groupSeeIntent = new Intent(context, SquareGroupSeeActivity.class);
                groupSeeIntent.putExtra("team_id", group.getTeamId());
                context.startActivity(groupSeeIntent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TeamEntity group = groups.get(i);
        Glide.with(context)
                .load(group.getActAvatar())
                .apply(bitmapTransform(new MultiTransformation<>(
                        new CropSquareTransformation(),
                        new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                )))
                .into(viewHolder.groupAvatar);
        viewHolder.groupName.setText(group.getTeamName());
        viewHolder.groupInfo.setText(group.getTeamDesc());
        viewHolder.groupSite.setText(group.getTeamLocation());
        viewHolder.groupNum.setText(group.getActUserNum() + "人");
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
