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
import cn.trunch.weidong.activity.CircleDiarySeeActivity;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.util.TextUtil;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class SportDiaryAdapter extends RecyclerView.Adapter<SportDiaryAdapter.ViewHolder> {

    private Context context;
    private List<DiaryEntity> diarys = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View diaryView;
        private TextView diaryTitle;
        private TextView diaryContent;
        private ImageView diaryPreview;
        private TextView diaryInfo;
        private TextView diaryTime;

        public ViewHolder(View view) {
            super(view);
            diaryView = view;
            diaryInfo = view.findViewById(R.id.diaryInfo);
            diaryTime = view.findViewById(R.id.diaryTime);
            diaryTitle = view.findViewById(R.id.diaryTitle);
            TextUtil.setBold(diaryTitle);
            diaryContent = view.findViewById(R.id.diaryContent);
            diaryPreview = view.findViewById(R.id.diaryPreview);
        }
    }

    public SportDiaryAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<DiaryEntity> diarys) {
        if (diarys != null) {
            this.diarys.clear();
            this.diarys.addAll(diarys);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<DiaryEntity> diarys) {
        if (diarys != null) {
            this.diarys.addAll(diarys);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_sport_diary_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.diaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryEntity diary = diarys.get(viewHolder.getAdapterPosition() - 1);
                Intent intent = new Intent(context, CircleDiarySeeActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DiaryEntity diary = diarys.get(i);
        viewHolder.diaryInfo.setText((diary.getDiaryAnonymous() == 1) ? ("私密") : ("公开"));
        viewHolder.diaryTime.setText(diary.getDiaryTime());
        viewHolder.diaryTitle.setText(diary.getDiaryTitle());
        viewHolder.diaryContent.setText(diary.getDiaryContentPreview());
        if (diary.getDiaryImgPreview() == null)
            viewHolder.diaryPreview.setVisibility(View.GONE);
        else {
            viewHolder.diaryPreview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(diary.getDiaryImgPreview())
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(viewHolder.diaryPreview);
        }
    }

    @Override
    public int getItemCount() {
        return diarys.size();
    }
}
