package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String layoutCurrent = "upcoming";

//    private RecyclerView recVUpcoming;
//    private RecyclerView recVHistory;

//    private AdapterUpcoming adapterUpcoming;
//    private AdapterHistory adapterHistory;
//    private List<RunRequestClass> runRequests = new ArrayList<>();
//    private List<RunHistoryClass> runsHistory = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_group);

        //SharedPrefs
        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        //RecyclerView
//        recVUpcoming = (RecyclerView) findViewById(R.id.recyclerViewRequests);
//        recVUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recVHistory = (RecyclerView) findViewById(R.id.recyclerViewHistory);
//        recVHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMenuButton();
        setUpButtons();
//        setUpRecyclers();
    }

    private void setUpRecyclers() {
        //Upcoming Runs Recycler
//        adapterUpcoming = new AdapterUpcoming(GroupActivity.this, runRequests);
//        recVUpcoming.setAdapter(adapterUpcoming);
//
//        //History Runs Recycler
//        adapterHistory = new AdapterHistory(GroupActivity.this, runsHistory);
//        recVHistory.setAdapter(adapterHistory);
    }

    private void setUpButtons() {
        final Button but1 = (Button) findViewById(R.id.upcomingButton);
        final Button but2 = (Button) findViewById(R.id.historyButton);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutCurrent.equals("upcoming")){

                } else if(layoutCurrent.equals("history")) {
                    but1.setBackgroundResource(R.color.colorAccent);
//                    but1.setTypeface(null, Typeface.BOLD);
                    but2.setBackgroundResource(R.color.progress_gray_dark);
//                    but2.setTypeface(null, Typeface.NORMAL);

                    View view1 = findViewById(R.id.upcomingLayout);
                    View view2 = findViewById(R.id.historyLayout);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);

                    layoutCurrent = "upcoming";
                }
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutCurrent.equals("history")){

                } else if(layoutCurrent.equals("upcoming")) {
                    but1.setBackgroundResource(R.color.progress_gray_dark);
//                    but1.setTypeface(null, Typeface.NORMAL);
                    but2.setBackgroundResource(R.color.colorAccent);
//                    but2.setTypeface(null, Typeface.BOLD);

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
