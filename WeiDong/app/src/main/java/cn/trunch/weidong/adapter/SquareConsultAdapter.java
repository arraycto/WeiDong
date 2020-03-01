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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.vo.DiaryUserVO;
import cn.trunch.weidong.activity.SquareConsultSeeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareConsultAdapter extends RecyclerView.Adapter<SquareConsultAdapter.ViewHolder> {

    private Context context;
    private List<DiaryUserVO> consults = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View consultView;
        private ImageView consultUHead;
        private TextView consultUName;
        private TextView consultUInfo;
        private TextView consultContent;
        private TextView consultInfo;
        private TextView consultGoBtn;

        public ViewHolder(View view) {
            super(view);
            consultView = view;
            consultUHead = view.findViewById(R.id.consultUHead);
            consultUName = view.findViewById(R.id.consultUName);
            TextUtil.setBold(consultUName);
            consultUInfo = view.findViewById(R.id.consultUInfo);
            consultContent = view.findViewById(R.id.consultContent);
            consultInfo = view.findViewById(R.id.consultInfo);
            consultGoBtn = view.findViewById(R.id.consultGoBtn);
        }
    }

    public SquareConsultAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<DiaryUserVO> consults) {
        if (consults != null) {
            this.consults.clear();
            this.consults.addAll(consults);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<DiaryUserVO> consults) {
        if (consults != null) {
            this.consults.addAll(consults);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_square_consult_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.consultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryUserVO consult = consults.get(viewHolder.getAdapterPosition() - 2);
                Intent consultSeeIntent = new Intent(context, SquareConsultSeeActivity.class);
                consultSeeIntent.putExtra("consultId", consult.getDiaryId());
                context.startActivity(consultSeeIntent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DiaryUserVO consult = consults.get(i);
        UserEntity user = consult.getBuser();
        Glide.with(context)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.consultUHead);
        viewHolder.consultUName.setText(user.getuNickname() + " 回答的咨询");
        viewHolder.consultUInfo.setText(user.getuRank() + "级达人");
        viewHolder.consultContent.setText(consult.getDiaryContent());
        viewHolder.consultInfo.setText(consult.getDiaryReadNum() + "人旁听 · " + consult.getDiaryReward() + "元价值");
        viewHolder.consultGoBtn.setText("免费旁听");
    }

    @Override
    public int getItemCount() {
        return consults.size();
    }
}
