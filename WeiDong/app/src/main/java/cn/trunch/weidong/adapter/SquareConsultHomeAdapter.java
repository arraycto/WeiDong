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
import cn.trunch.weidong.activity.SquareConsultSeeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;
import cn.trunch.weidong.vo.DiaryUserVO;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SquareConsultHomeAdapter extends RecyclerView.Adapter<SquareConsultHomeAdapter.ViewHolder> {

    private Context context;
    private List<DiaryUserVO> consults = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View consultView;
        private TextView consultContent;
        private TextView consultInfo;
        private TextView consultGoBtn;

        public ViewHolder(View view) {
            super(view);
            consultView = view;
            consultContent = view.findViewById(R.id.consultHomeContent);
            consultInfo = view.findViewById(R.id.consultHomeInfo);
            consultGoBtn = view.findViewById(R.id.consultHomeGoBtn);
        }
    }

    public SquareConsultHomeAdapter(Context context) {
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
                .inflate(R.layout.view_square_consult_home_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.consultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryUserVO consult = consults.get(viewHolder.getAdapterPosition() - 1);
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
        viewHolder.consultContent.setText(consult.getDiaryContent());
        viewHolder.consultInfo.setText(consult.getDiaryReadNum() + "人旁听 · " + consult.getDiaryReward() + "元价值");
        viewHolder.consultGoBtn.setText("免费旁听");
    }

    @Override
    public int getItemCount() {
        return consults.size();
    }
}
