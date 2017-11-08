package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private String layoutCurrent = "upcoming";

    //SharePrefs
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String myI_d;

    private RecyclerView recVRequests;
    private RecyclerView recVUpcoming;
    private RecyclerView recVHistory;

    private DatabaseReference mRequestData;
    private DatabaseReference mMyChats;

    private AdapterRequests adapterRequests;
    private AdapterUpcoming adapterUpcoming;
    private AdapterHistory adapterHistory;

    private List<RunRequestClass> runRequests = new ArrayList<>();
    private List<String> runsUpcoming = new ArrayList<>();
    private List<RunHistoryClass> runsHistory = new ArrayList<>();

    boolean requestDataExists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_group);

        //SharedPrefs
        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        //RecyclerViews
        recVRequests = (RecyclerView) findViewById(R.id.recyclerViewRequests);
        recVRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recVUpcoming = (RecyclerView) findViewById(R.id.recyclerViewUpcoming);
        recVUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recVUpcoming.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recVHistory = (RecyclerView) findViewById(R.id.recyclerViewHistory);
        recVHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMenuButton();
        setUpButtons();

        //Firebase
        myI_d = prefs.getString("myI_d", "");
        mRequestData = FirebaseDatabase.getInstance().getReference("runner_requests");
        mMyChats = FirebaseDatabase.getInstance().getReference("user_data/" + myI_d);
        if(!myI_d.equals("")) {
            setUpRecyclers();
        }
    }

    private void setUpRecyclers() {
        //Recycler for upcoming runs
        mMyChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If at least one upcoming run exists
                if(dataSnapshot.hasChild("chats_upcoming")) {
                    recVUpcoming.setVisibility(View.VISIBLE);
                    for(DataSnapshot upcomingRunSnapshot : dataSnapshot.child("chats_upcoming").getChildren()) {
                        String chatRoomKey = upcomingRunSnapshot.getKey();
                        runsUpcoming.add(chatRoomKey);
                    }
                    adapterUpcoming = new AdapterUpcoming(GroupActivity.this, runsUpcoming);
                    recVUpcoming.setAdapter(adapterUpcoming);
                } else { //If no upcoming runs exist
                    recVUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Recycler for run requests to user
        mRequestData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If there exists at least one request
                if(dataSnapshot.hasChild(myI_d)) {
                    requestDataExists = true;
                    setUpRequestAdapter();
                    TextView text = (TextView) findViewById(R.id.textBanner);
                    text.setVisibility(View.VISIBLE);
                } else { //If no requests exist
                    requestDataExists = true;
                    TextView text = (TextView) findViewById(R.id.textBanner);
                    text.setText("No requests");
                    text.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Recycler for previous runs
        adapterHistory = new AdapterHistory(GroupActivity.this, runsHistory);
        recVHistory.setAdapter(adapterHistory);
    }

    private void setUpRequestAdapter() {
        mRequestData.child(myI_d).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                runRequests.clear();
                for(DataSnapshot requestSnapshot : dataSnapshot.getChildren()){
                    RunRequestClass request = requestSnapshot.getValue(RunRequestClass.class);
                    runRequests.add(request);
                }
                adapterRequests = new AdapterRequests(GroupActivity.this, runRequests);
                recVRequests.setAdapter(adapterRequests);
//                Toast.makeText(GroupActivity.this, "" + runRequests.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpButtons() {
        final Button but1 = (Button) findViewById(R.id.upcomingButton);
        final Button but2 = (Button) findViewById(R.id.historyButton);
        //Click bottom-left button
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutCurrent.equals("upcoming")){

                } else if(layoutCurrent.equals("history")) {
                    if(requestDataExists){
                        TextView text = (TextView) findViewById(R.id.textBanner);
                        text.setVisibility(View.VISIBLE);
                    }

                    but1.setBackgroundResource(R.color.colorAccent);
                    but1.setTypeface(null, Typeface.BOLD);
                    but2.setBackgroundResource(R.color.progress_gray_dark);
                    but2.setTypeface(null, Typeface.NORMAL);

                    View view1 = findViewById(R.id.upcomingLayout);
                    View view2 = findViewById(R.id.historyLayout);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);

                    layoutCurrent = "upcoming";
                }
            }
        });
        //Click bottom right button
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutCurrent.equals("history")){

                } else if(layoutCurrent.equals("upcoming")) {
                    if(requestDataExists){
                        TextView text = (TextView) findViewById(R.id.textBanner);
                        text.setVisibility(View.GONE);
                }

                    but1.setBackgroundResource(R.color.progress_gray_dark);
                    but1.setTypeface(null, Typeface.NORMAL);
                    but2.setBackgroundResource(R.color.colorAccent);
                    but2.setTypeface(null, Typeface.BOLD);

                    View view1 = findViewById(R.id.upcomingLayout);
                    View view2 = findViewById(R.id.historyLayout);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);

                    layoutCurrent = "history";
                }
            }
        });
    }

    private void setUpMenuButton() {
        Button button = (Button) findViewById(R.id.mainMenuButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                String pic_url = prefs.getString("fb_profile_pic","");
                if (pic_url !="") {
                    ImageView profile_image3 = (ImageView) findViewById(R.id.pic2);
                    Picasso.with(GroupActivity.this)
                            .load(pic_url)
                            .into(profile_image3);
                }
            }
        });
    }

    public void openProfile(View item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Menu_Option", "profile");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void openHome(MenuItem item) {
        finish();
    }

    public void openGroup(MenuItem item) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void openAbout(MenuItem item){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Menu_Option", "about");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void openLogout(MenuItem item) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
