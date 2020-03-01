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
import cn.trunch.weidong.activity.SquareQuestionSeeActivity;
import cn.trunch.weidong.entity.UserEntity;
import cn.trunch.weidong.util.TextUtil;
import cn.trunch.weidong.vo.DiaryUserVO;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class SquareQuestionSearchAdapter extends RecyclerView.Adapter<SquareQuestionSearchAdapter.ViewHolder> {

    private Context context;
    private List<DiaryUserVO> questions = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View questionView;
        private TextView questionTitle;
        private TextView questionContent;
        private ImageView questionPreview;
        private ImageView questionUHead;
        private TextView questionInfo;
        private TextView questionBounty;

        public ViewHolder(View view) {
            super(view);
            questionView = view;
            questionTitle = view.findViewById(R.id.questionTitle);
            TextUtil.setBold(questionTitle);
            questionContent = view.findViewById(R.id.questionContent);
            questionPreview = view.findViewById(R.id.questionPreview);
            questionUHead = view.findViewById(R.id.questionUHead);
            questionInfo = view.findViewById(R.id.questionInfo);
            questionBounty = view.findViewById(R.id.questionBounty);
        }
    }

    public SquareQuestionSearchAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<DiaryUserVO> questions) {
        if (questions != null) {
            this.questions.clear();
            this.questions.addAll(questions);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<DiaryUserVO> questions) {
        if (questions != null) {
            this.questions.addAll(questions);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_square_question_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.questionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryUserVO question = questions.get(viewHolder.getAdapterPosition() - 1);
                Intent questionSeeIntent = new Intent(context, SquareQuestionSeeActivity.class);
                questionSeeIntent.putExtra("did", question.getDiaryId());
                context.startActivity(questionSeeIntent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DiaryUserVO question = questions.get(i);
        UserEntity user = question.getUser();
        viewHolder.questionTitle.setText(question.getDiaryTitle());
        viewHolder.questionContent.setText(question.getDiaryContent());
        //预览图
        if (question.getDiaryImgPreview().contains("default"))
            viewHolder.questionPreview.setVisibility(View.GONE);
        else {
            viewHolder.questionPreview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(question.getDiaryImgPreview())
                    .apply(bitmapTransform(new MultiTransformation<>(
                            new CropSquareTransformation(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
                    )))
                    .into(viewHolder.questionPreview);
        }
        Glide.with(context)
                .load(user.getuAvatar())
                .apply(bitmapTransform(new CircleCrop()))
                .into(viewHolder.questionUHead);
        viewHolder.questionInfo.setText(user.getuNickname()
                + "    " + question.getDiaryReadNum() + "人浏览");
        viewHolder.questionBounty.setText(question.getDiaryReward() + ".00");
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
