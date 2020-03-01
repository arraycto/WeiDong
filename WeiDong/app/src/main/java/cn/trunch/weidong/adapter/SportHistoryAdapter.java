package cn.trunch.weidong.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.SportHistorySeeActivity;
import cn.trunch.weidong.entity.ExerciseEntity;
import cn.trunch.weidong.http.ApiUtil;

public class SportHistoryAdapter extends RecyclerView.Adapter<SportHistoryAdapter.ViewHolder> {

    private Context context;
    private List<ExerciseEntity> steps = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View sportHisView;
        private TextView sportHisStep;
        private TextView sportHisTime;

        public ViewHolder(View view) {
            super(view);
            sportHisView = view;
            sportHisStep = view.findViewById(R.id.sportHisStep);
            sportHisTime = view.findViewById(R.id.sportHisTime);
        }
    }

    public SportHistoryAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<ExerciseEntity> steps) {
        if (steps != null) {
            this.steps.clear();
            this.steps.addAll(steps);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<ExerciseEntity> steps) {
        if (steps != null) {
            this.steps.addAll(steps);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_sport_history_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.sportHisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SportHistorySeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", ApiUtil.USER_ID);
                bundle.putString("sTime", steps.get(viewHolder.getAdapterPosition() - 2).getExStartTime());
                bundle.putString("eTime", steps.get(viewHolder.getAdapterPosition() - 2).getExEndTime());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.sportHisStep.setText(String.valueOf(steps.get(i).getExAmount()));
        String exStartTime = steps.get(i).getExStartTime();
        String exEndTime = steps.get(i).getExEndTime();
        viewHolder.sportHisTime.setText("从"+exStartTime.substring(exStartTime.indexOf(" ")+1,exStartTime.lastIndexOf("."))+"到"+exEndTime.substring(exEndTime.indexOf(" ")+1,exEndTime.lastIndexOf(".")));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
