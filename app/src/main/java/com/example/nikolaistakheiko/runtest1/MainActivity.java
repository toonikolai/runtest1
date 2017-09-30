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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setUpButton();
    }

    public void openProfile(MenuItem item){
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void openHome(MenuItem item) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
    public void openAbout(MenuItem item){
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    private void setUpButton() {
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    });
    }
}
