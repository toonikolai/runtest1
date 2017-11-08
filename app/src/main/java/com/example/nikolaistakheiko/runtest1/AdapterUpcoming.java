package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-11-08.
 */

public class AdapterUpcoming extends RecyclerView.Adapter<AdapterUpcoming.CustomViewHolder>{
    private List<String> runsUpcoming;
    private Context mContext;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public AdapterUpcoming(Context context, List<String> runsUpcoming) {
        this.mContext = context;
        this.runsUpcoming = runsUpcoming;

        //SharedPrefs
        prefs = mContext.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_run_upcoming, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        View mView = customViewHolder.mView5;
        String chatRoomKey = runsUpcoming.get(i);

        //do stuff with chatroom key...
    }


    @Override
    public int getItemCount() {
        return (null != runsUpcoming ? runsUpcoming.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView5;
        public CustomViewHolder(View view) {
            super(view);
            mView5 = view;
        }

    }
}
