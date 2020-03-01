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
import cn.trunch.weidong.http.ApiUtil;
import cn.trunch.weidong.step.bean.StepData;

public class SportPunchAdapter extends RecyclerView.Adapter<SportPunchAdapter.ViewHolder> {

    private Context context;
    private List<StepData> steps = new ArrayList<>();

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

    public SportPunchAdapter(Context context) {
        this.context = context;
    }

    public void initData(List<StepData> steps) {
        if (steps != null) {
            this.steps.clear();
            this.steps.addAll(steps);
            notifyDataSetChanged();
        }
    }

    public void loadData(List<StepData> steps) {
        if (steps != null) {
            this.steps.addAll(steps);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_sport_punch_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.sportHisStep.setText(steps.get(i).getStep());
        viewHolder.sportHisTime.setText(steps.get(i).getToday().replaceAll("-", "/"));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
