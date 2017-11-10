package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nikolaistakheiko on 2017-11-10.
 */

public class ChatRoom extends AppCompatActivity {
    private String chat_key;

    //Firebase
    private DatabaseReference mChatRoomUsers;
    private DatabaseReference mMyRef;
    private DatabaseReference mChatRoom;

    //SharedPrefs
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String myUsername;

    private List<String> usernames = new ArrayList<>();
    private List<String> picUrls = new ArrayList<>();

    private EditText enterText;
    private TextView tempChatText;
    private Button sendTextButton;
    private ScrollView scroll;

    private String newTextKey;
    private String chat_msg, chat_username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        chat_key = (String) getIntent().getExtras().get("chat_key");

        //SharedPrefs
        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        //Firebase
        mChatRoomUsers = FirebaseDatabase.getInstance().getReference("message_rooms/" + chat_key + "/users");
        mChatRoom = FirebaseDatabase.getInstance().getReference("message_rooms/" + chat_key + "/chat");
        mMyRef = FirebaseDatabase.getInstance().getReference("runners");

        //set up view elements
        enterText = (EditText) findViewById(R.id.enterText);
        tempChatText = (TextView) findViewById(R.id.tempChatText);
        sendTextButton = (Button) findViewById(R.id.sendText);
        scroll = (ScrollView) findViewById(R.id.scrollView2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpUsersIconsInBanner();

        //get my id
        myUsername = prefs.getString("myUsername", "noUsernameReceived");

        //Send to message, message key, and username to database on each new entry
        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                newTextKey = mChatRoom.push().getKey();
                mChatRoom.updateChildren(map);

                DatabaseReference message = mChatRoom.child(newTextKey);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("username", myUsername);
                map2.put("message", enterText.getText().toString());
                message.updateChildren(map2);

                enterText.setText("");
                scroll.scrollTo(0, scroll.getBottom());
            }
        });

        mChatRoom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_username = (String) ((DataSnapshot) i.next()).getValue();
            tempChatText.append(chat_username + ":\n" + chat_msg + "\n \n");
        }
    }

    private void setUpUsersIconsInBanner() {

        //Find view elements
        final ImageView initiatorPic = (ImageView) findViewById(R.id.initiatorPic);
        final ImageView hostPic = (ImageView) findViewById(R.id.hostPic);
        final ImageView addedUserPic = (ImageView) findViewById(R.id.addedUserPic);
        final ImageView more = (ImageView) findViewById(R.id.more);

        picUrls.clear();
        usernames.clear();
        mChatRoomUsers.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            picUrls.add(dataSnapshot.getValue(String.class));

                            //Show chat-user info on top of chatbox
                            if(usernames.size() == 1){
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(0))
                                        .into(initiatorPic);
                            } else if(usernames.size() == 2){
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(0))
                                        .into(initiatorPic);
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(1))
                                        .into(hostPic);
                                hostPic.setVisibility(View.VISIBLE);
                            } else if(usernames.size() >= 3){
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(0))
                                        .into(initiatorPic);
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(1))
                                        .into(hostPic);
                                hostPic.setVisibility(View.VISIBLE);
                                Picasso.with(ChatRoom.this)
                                        .load(picUrls.get(2))
                                        .into(addedUserPic);
                                addedUserPic.setVisibility(View.VISIBLE);
                                if(usernames.size() >= 3) {
                                    more.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ChatRoom.this, "watdafuq", Toast.LENGTH_SHORT).show();
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
}
