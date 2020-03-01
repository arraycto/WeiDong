package cn.trunch.weidong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.entity.DiaryEntity;
import cn.trunch.weidong.entity.SchoolRank;
import cn.trunch.weidong.vo.DiaryUserVO;

public class SportSchoolRankAdapter extends RecyclerView.Adapter<SportSchoolRankAdapter.ViewHolder> {

    private Context context;
    private List<SchoolRank> schools = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View topicHotView;
        private TextView topicHotOrder;
        private TextView topicHotName;
        private TextView topicHotInfo;
        private TextView topicHotField;

        public ViewHolder(@NonNull View view) {
            super(view);
            topicHotView = view;
            topicHotOrder = view.findViewById(R.id.topicHotOrder);
            topicHotName = view.findViewById(R.id.topicHotName);
            topicHotInfo = view.findViewById(R.id.topicHotInfo);
            topicHotField = view.findViewById(R.id.topicHotField);
        }
    }

    public SportSchoolRankAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<SchoolRank> schools) {
        if (schools != null) {
            this.schools.clear();
            this.schools.addAll(schools);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<SchoolRank> schools) {
        if (schools != null) {
            this.schools.addAll(schools);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sport_school_rank_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SchoolRank school = schools.get(i);
        viewHolder.topicHotOrder.setText(String.valueOf(i + 1));
        viewHolder.topicHotName.setText(school.getsName());
        viewHolder.topicHotInfo.setText(school.getsStuNum() + "个学生 · " + school.getsExTime() + "个小时");
        viewHolder.topicHotField.setText("上榜" + school.getsHistoryNum() + "次");
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }
}
