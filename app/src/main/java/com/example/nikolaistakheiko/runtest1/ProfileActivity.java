package com.example.nikolaistakheiko.runtest1;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout2);
        buttonSetUp();
    }

    public void openProfile(MenuItem item){
        mDrawerLayout2.closeDrawer(Gravity.LEFT);
    }

    public void openHome(MenuItem item) {
        finish();
    }

    public void openAbout(MenuItem item){
    }

    private void buttonSetUp() {

        Button button = (Button) findViewById(R.id.profilebutton2);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout2.openDrawer(Gravity.LEFT);
            }
        });

//        Button button = (Button) findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}
