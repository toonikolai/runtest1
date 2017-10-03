package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private DrawerLayout mDrawerLayout;
    private GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker mPositionMarker;
    LatLng ll;
    SeekBar slider1, slider2, slider3;
    TextView label1, label2, label3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
//            Toast.makeText(this, "Connected to play services", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setUpButton();
            initMap();
        } else {
            Toast.makeText(this, "No G-maps layout", Toast.LENGTH_SHORT).show();
        }
        slider1 = (SeekBar) findViewById(R.id.mainseek1);
        label1 = (TextView) findViewById(R.id.mainLabel1);
        slider1.setMax(20);
        slider1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label1.setText("Run Speed: " + progress + "km/h");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider2 = (SeekBar) findViewById(R.id.mainseek2);
        label2 = (TextView) findViewById(R.id.mainLabel2);
        slider2.setMax(84);
        slider2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label2.setText("Distance: " + progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider3 = (SeekBar) findViewById(R.id.mainseek3);
        label3 = (TextView) findViewById(R.id.mainLabel3);
        slider3.setMax(100);
        slider3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 20) {
                    label3.setText("Early Morning (6am - 9am)");
                } else if (progress > 20 && progress <= 40) {
                    label3.setText("Late Morning (9am - 12pm)");
                } else if (progress > 40 && progress <= 60) {
                    label3.setText("Afternoon (12am - 3pm)");
                } else if (progress > 60 && progress <= 80) {
                    label3.setText("Midday (3am - 6pm)");
                } else {
                    label3.setText("Evening (6pm-9pm)");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void openProfile(MenuItem item) {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivityForResult(profileIntent, 0);
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

    public void openAbout(MenuItem item) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("Menu_Option");
                    if (newText.contentEquals("about")) {
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setPadding(0, 0, 0, 155);
        LatLng startplace = new LatLng(-33.8688, 151.2093);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startplace, 15));
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
                //Choose map style:
                monotone
        );
        mGoogleMap.setMapStyle(style);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setBuildingsEnabled(false);
        mGoogleMap.setIndoorEnabled(false);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if (ll != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(ll)
                            .zoom(14)
                            .build()
                    ));
                } else {
                    Toast.makeText(MainActivity.this, "LatLng == null", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);

    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveCanceled() {
//        LinearLayout layout = (LinearLayout) findViewById(R.id.boxOverMap);
//        layout.setVisibility(View.VISIBLE);
//        Button run = (Button) findViewById(R.id.runButton);
//        run.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.boxOverMap);
//        layout.setVisibility(View.INVISIBLE);
        layout.animate()
                .alpha(0.0f)
                .setDuration(400)
                .translationY(-10);
        Button run = (Button) findViewById(R.id.runButton);
//        run.setVisibility(View.INVISIBLE);
        run.animate()
                .alpha(0.0f)
                .setDuration(400)
                .translationY(+5);
    }

    @Override
    public void onCameraIdle() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.boxOverMap);
//        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .alpha(1.0f)
                .setDuration(200)
                .translationY(+10);
        Button run = (Button) findViewById(R.id.runButton);
//        run.setVisibility(View.VISIBLE);
        run.animate()
                .alpha(1.0f)
                .setDuration(200)
                .translationY(-5);
    }

    LocationRequest mLocRequest;



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocRequest, this);
                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocRequest = LocationRequest.create();
        mLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocRequest.setInterval(1000);

        //After (works)
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocRequest, this);
            }
        }
        //Before
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocRequest, this);


    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }


    public Bitmap resizeMapIcons(String image_name, int w, int h) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(image_name, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, w, h, false);
        return resizedBitmap;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null) {
            Toast.makeText(this, "null location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location != null) {
            ll = new LatLng(location.getLatitude(),location.getLongitude());
            if(mPositionMarker == null) {
                mPositionMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(resizeMapIcons("blue_thin_2", 55, 55)))
                        .anchor(0.5f, 0.5f)
                        .position(ll)
                );
            } else {
                mPositionMarker.setPosition(ll);
            }
        }

    }


}


