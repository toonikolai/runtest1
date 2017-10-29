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


public class AdapterUpcoming extends RecyclerView.Adapter<AdapterUpcoming.CustomViewHolder>{
    private List<RunRequestClass> runRequests;
    private Context mContext;

    public AdapterUpcoming(Context context, List<RunRequestClass> runRequests) {
        this.runRequests = runRequests;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_run_request, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        View mView4 = customViewHolder.mView4;
        RunRequestClass runRequest = runRequests.get(i);


    }

    @Override
    public int getItemCount() {
        return (null != runRequests ? runRequests.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView4;
        public CustomViewHolder(View view) {
            super(view);
            mView4 = view;
        }

    }
}
