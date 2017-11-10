package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-11-08.
 */

public class AdapterUpcoming extends RecyclerView.Adapter<AdapterUpcoming.CustomViewHolder>{
    private List<String> runsUpcoming;
    private Context mContext;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private DatabaseReference mChatRoom;
    private DatabaseReference mMyRef;

    private List<String> pics = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();

    public AdapterUpcoming(Context context, List<String> runsUpcoming) {
        this.mContext = context;
        this.runsUpcoming = runsUpcoming;

        //SharedPrefs
        prefs = mContext.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        //Firebase
        mMyRef = FirebaseDatabase.getInstance().getReference("runners");
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

        //Find the chat room
        String chatRoomKey = runsUpcoming.get(i);
        mChatRoom = FirebaseDatabase.getInstance().getReference("message_rooms/" + chatRoomKey + "/users");

        //Find view elements
        final ImageView initiatorPic = (ImageView) mView.findViewById(R.id.initiatorPic);
        final ImageView hostPic = (ImageView) mView.findViewById(R.id.hostPic);
        final ImageView addedUserPic = (ImageView) mView.findViewById(R.id.addedUserPic);
        final ImageView more = (ImageView) mView.findViewById(R.id.more);
        final TextView chatName = (TextView) mView.findViewById(R.id.chatName);

        //Store all users' pics and usernames into resepective lists
        pics.clear();
        usernames.clear();
        mChatRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Get username
                    mMyRef.child(userSnapshot.getKey()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            usernames.add(dataSnapshot.getValue(String.class));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Get pic
                    mMyRef.child(userSnapshot.getKey()).child("pic_url").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pics.add(dataSnapshot.getValue(String.class));

                            //Show chat-user info on top of chatbox
                            if(usernames.size() == 1){
                                Picasso.with(mContext)
                                        .load(pics.get(0))
                                        .into(initiatorPic);
                                chatName.setText(usernames.get(0));
                            } else if(usernames.size() == 2){
                                Picasso.with(mContext)
                                        .load(pics.get(0))
                                        .into(initiatorPic);
                                Picasso.with(mContext)
                                        .load(pics.get(1))
                                        .into(hostPic);
                                hostPic.setVisibility(View.VISIBLE);
                                chatName.setText(usernames.get(0) + " & " + usernames.get(1));
                                chatName.setTextSize(16);
                            } else if(usernames.size() >= 3){
                                Picasso.with(mContext)
                                        .load(pics.get(0))
                                        .into(initiatorPic);
                                initiatorPic.getLayoutParams().height = 45;
                                initiatorPic.getLayoutParams().width = 45;
                                initiatorPic.requestLayout();
                                Picasso.with(mContext)
                                        .load(pics.get(1))
                                        .into(hostPic);
                                hostPic.setVisibility(View.VISIBLE);
                                hostPic.getLayoutParams().height = 45;
                                hostPic.getLayoutParams().width = 45;
                                hostPic.requestLayout();
                                Picasso.with(mContext)
                                        .load(pics.get(2))
                                        .into(addedUserPic);
                                addedUserPic.setVisibility(View.VISIBLE);
                                chatName.setTextSize(12);
                                if(usernames.size() == 3) {
                                    chatName.setText(usernames.get(0) + ", " + usernames.get(1) + " & " + usernames.get(2));
                                } else {
                                    chatName.setText(usernames.get(0) + ", " + usernames.get(1) + " & " + usernames.get(2) + "...");
                                    more.setVisibility(View.VISIBLE);
                                }
                            } else {
//            Toast.makeText(mContext, "" + usernames.size(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
