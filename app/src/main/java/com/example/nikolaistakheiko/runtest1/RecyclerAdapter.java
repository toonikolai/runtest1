package com.example.nikolaistakheiko.runtest1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-20.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    private List<RunnerClass> runnersInAdapter;
    private Context mContext;

    public RecyclerAdapter(Context context, List<RunnerClass> runnerList) {
        this.runnersInAdapter = runnerList;
        this.mContext = context;
    }

    public void setRunners(List<RunnerClass> list) {
        runnersInAdapter = list;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_over_map, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        View mView3 = customViewHolder.mView3;
        RunnerClass runner = runnersInAdapter.get(i);

        //Name
        TextView name = (TextView) mView3.findViewById(R.id.userTextName);
        name.setText(runner.getUsername());

        //User Pic
        ImageView image = (ImageView) mView3.findViewById(R.id.userImage);
        String pic_url = (String) runner.getPic_url();
        if (pic_url !="") {
            Picasso.with(mContext)
                    .load(pic_url)
                    .into(image);
        }

        //Seek Bars
        SeekArc uSeek1 = (SeekArc) mView3.findViewById(R.id.userSeek1);
        SeekArc uSeek2 = (SeekArc) mView3.findViewById(R.id.userSeek2);
        SeekArc uSeek3 = (SeekArc) mView3.findViewById(R.id.userSeek3);
        uSeek1.setProgress(runner.getDistance());
        uSeek2.setProgress(runner.getPace());
        uSeek3.setProgress(runner.getGroup());

        //Seek Bar Labels
        TextView uLabel1 = (TextView) mView3.findViewById(R.id.userLabel1);
        TextView uLabel2 = (TextView) mView3.findViewById(R.id.userLabel2);
        TextView uLabel3 = (TextView) mView3.findViewById(R.id.userLabel3);
        String s1 = runner.getDistance() + " km";
        String s2 = runner.getPace() + " km/h";
        String s3 = "People: " + runner.getDistance();
        uLabel1.setText(s1);
        uLabel2.setText(s2);
        uLabel3.setText(s3);
    }

    @Override
    public int getItemCount() {
        return (null != runnersInAdapter ? runnersInAdapter.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView3;
        public CustomViewHolder(View view) {
            super(view);
            mView3 = view;
        }

    }

}
