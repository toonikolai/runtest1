package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class GroupActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_group);

        //SharedPrefs
        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMenuButton();
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
