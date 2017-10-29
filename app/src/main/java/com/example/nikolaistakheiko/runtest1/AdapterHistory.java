package com.example.nikolaistakheiko.runtest1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-29.
 */

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.CustomViewHolder>{
    private List<RunHistoryClass> runsHistory;
    private Context mContext;

    public AdapterHistory(Context context, List<RunHistoryClass> runsHistory) {
        this.runsHistory = runsHistory;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_run_history, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        View mView4 = customViewHolder.mView4;
        RunHistoryClass userRunHistory = runsHistory.get(i);


    }

    @Override
    public int getItemCount() {
        return (null != runsHistory ? runsHistory.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView4;
        public CustomViewHolder(View view) {
            super(view);
            mView4 = view;
        }

    }
}
