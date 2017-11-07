package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-29.
 */


public class AdapterUpcoming extends RecyclerView.Adapter<AdapterUpcoming.CustomViewHolder>{
    private List<RunRequestClass> runRequests;
    private Context mContext;
    private DatabaseReference mRequests;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public AdapterUpcoming(Context context, List<RunRequestClass> runRequests) {
        this.runRequests = runRequests;
        this.mContext = context;

        //SharedPrefs
        prefs = mContext.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();
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

        //Username set up
        TextView text = (TextView) mView4.findViewById(R.id.userTextName);
        text.setText(runRequest.getUsername());


        //choose runner button set up
        TextView choose = (TextView) mView4.findViewById(R.id.chooseRequestButton);
        choose.setText("Run with " + runRequest.getUsername());

        //picture set up
        ImageView image = (ImageView) mView4.findViewById(R.id.userImage);
        String pic_url = runRequest.getPic_url();
        if (pic_url !="") {
            Picasso.with(mContext)
                    .load(pic_url)
                    .into(image);
        }

        //Ignore button
        Button ingore = (Button) mView4.findViewById(R.id.optOutButton);
        final String deleteThis = runRequest.getId();
        ingore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequests = FirebaseDatabase.getInstance().getReference("runner_requests");
                String myI_d = prefs.getString("myI_d", "");
                mRequests.child(myI_d).child(deleteThis).removeValue();
            }
        });

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
