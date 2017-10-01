package com.example.nikolaistakheiko.runtest1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout mDrawerLayout;
    GoogleMap mGoogleMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(googleServicesAvailable()) {
            Toast.makeText(this, "Connected to play services", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setUpButton();
            initMap();
        } else {
            Toast.makeText(this, "No G-maps layout", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void openProfile(MenuItem item){
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        }, 1000);
    }

    public void openHome(MenuItem item) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
    public void openAbout(MenuItem item){
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        }, 1000);
    }

    private void setUpButton() {
        Button button = (Button) findViewById(R.id.mainMenuButton);
        button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }
}
