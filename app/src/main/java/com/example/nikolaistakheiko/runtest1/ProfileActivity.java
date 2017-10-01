package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout2);
        buttonSetUp();
        genderSetUp();
        partnerSetUp();
        terrainSetUp();

//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stlsm);
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//        roundedBitmapDrawable.setCircular(true);
//        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    private void genderSetUp() {
        Spinner spinner = (Spinner) findViewById(R.id.Gender);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void partnerSetUp() {
        Spinner spinner = (Spinner) findViewById(R.id.RunPartner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.partner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void terrainSetUp() {
        Spinner spinner = (Spinner) findViewById(R.id.Terrain);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.terrain_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void openProfile(MenuItem item){
        mDrawerLayout2.closeDrawer(Gravity.LEFT);
    }

    public void openHome(MenuItem item) {
        finish();
    }
    public void openAbout(MenuItem item){
        finish();
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
