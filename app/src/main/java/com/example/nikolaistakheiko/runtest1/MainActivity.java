package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker mPositionMarker;
    LatLng ll;
    Double lat;
    Double lng;
    SeekArc slider1, slider2, slider3, slider4;
    TextView label1, label2, label3, label4;
    String profilename;
    int paceInt, timeInt, distanceInt, groupSizeInt;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private RecyclerView recV;
    boolean mainMenuView = true;

    DatabaseReference mRunnerData;

    //Geofire stuff
    DatabaseReference mGeo;
    GeoFire geoFire;
    private int initialListSize;
    private boolean queReadyCalledOnce = false;
    private Map<String, Location> userIdsToLocations = new HashMap<>();
    private Set<String> userIdsWithListeners = new HashSet<>();
    private List<RunnerClass> runners = new ArrayList<>();
    private ValueEventListener userValueListener;
    private RecyclerAdapter adapter;
    private int iterationCount;
    private Set<GeoQuery> geoQueries = new HashSet<>();

    //Weather stuff
    public static TextView weather;
    public static String weatherString;
    public static JSONArray weatherJSONArray;
    boolean weatherCalled = false;
    int hourTenNext;
    List<Double> temps = new ArrayList<>();
    List<String> weathers = new ArrayList<>();
    long dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_main);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            //SharedPrefs
            prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
            editor = prefs.edit();
            initMap();
            //RecyclerView
            recV = (RecyclerView) findViewById(R.id.recyclerViewInsideBox);
            recV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        } else {
            Toast.makeText(this, "No G-maps layout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Database
        mRunnerData = FirebaseDatabase.getInstance().getReference("runners");
        mGeo = FirebaseDatabase.getInstance().getReference("runner_locations");
        geoFire = new GeoFire(mGeo);
//        setUpListeners();
        setName();
        setUpButton();
        setUpRunButton();
        setUpSeekBars();
//        setUpWeather();
//        setUpTerrains();
    }

    private void setUpWeather() {
        fetchData process = new fetchData(lat, lng);
        process.execute();
//        Toast.makeText(MainActivity.this, weatherString, Toast.LENGTH_SHORT).show();
        //get hours from JSONArray
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Current Hour (24h)
                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
//                Toast.makeText(MainActivity.this, "" + currentHour, Toast.LENGTH_SHORT).show();
                try {

                    //Get first time from openweathermap
                    LinearLayout linlay = (LinearLayout) findViewById(R.id.weatherLayout);
                    linlay.setVisibility(View.VISIBLE);
                    JSONObject JO = weatherJSONArray.getJSONObject(0);
                    dt = JO.getLong("dt");
                    Date df = new java.util.Date(dt*1000);
                    String vv = new SimpleDateFormat("HH").format(df);
                    hourTenNext = Integer.parseInt(vv);


                    slider3.setProgress(hourTenNext*10);

                    for (int i = 0; i < weatherJSONArray.length(); i++) {
                        JSONObject Jobj = weatherJSONArray.getJSONObject(i);

                        //Temperature
                        JSONObject J2 = Jobj.getJSONObject("main");
                        double kelvin = (Double) J2.get("temp");
                        double celsius = kelvin-273.15;
                        temps.add(i, celsius);

                        //Weather Type
                        JSONArray JA = Jobj.getJSONArray("weather");
                        JSONObject JAO = JA.getJSONObject(0);
                        String wea = (String) JAO.get("description");
                        weathers.add(i, wea);

                        //Initiate ArcSeek temperature change action
                        weatherCalled = true;
                    }
//                    Toast.makeText(MainActivity.this, weathers.toString(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        if(!mainMenuView) {
            clickBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setUpListeners() {
        userValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                RunnerClass rc = dataSnapshot.getValue(RunnerClass.class);
//                rc.i_d = dataSnapshot.getKey();
//                Location location = userIdsToLocations.get(dataSnapshot.getKey());
//                rc.lat = location.getLatitude();
//                rc.lng = location.getLongitude();
//                //May need to link other stuff from dataSnapshot to rc
//
//                if (runners.contains(rc)) {
//                    userUpdated(rc);
//                } else {
//                    newUser(rc);
//                }

            }

            private void newUser(RunnerClass rc) {
                iterationCount++;
                runners.add(0, rc);
                if (!queReadyCalledOnce && iterationCount == initialListSize) {
                    queReadyCalledOnce = true;
//                    sortByDistanceFromMe();
                    adapter.setRunners(runners);
                } else if (queReadyCalledOnce) {
//                    sortByDistanceFromMe();
                    adapter.notifyItemInserted(getIndexOfNewRunner(rc));
                }
            }

            private void userUpdated(RunnerClass u) {
                int position = getRunnerPosition(u.getI_d());
                runners.set(position, u);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private int getIndexOfNewRunner(RunnerClass rc) {
        for (int i = 0; i < runners.size(); i++) {
            if (runners.get(i).i_d.equals(rc.i_d)) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private int getRunnerPosition(String i_d) {
        for (int i = 0; i < runners.size(); i++) {
            if (runners.get(i).i_d.equals(i_d)) {
                return i;
            }
        }
        return -1;
    }

    private void setUpTerrains() {
//        ImageView terrain1 = (ImageView) findViewById(R.id.activity1);
//        ImageView terrain2 = (ImageView) findViewById(R.id.activity2);
//        ImageView terrain3 = (ImageView) findViewById(R.id.activity3);
//        ImageView terrain4 = (ImageView) findViewById(R.id.activity4);
//        ImageView terrain5 = (ImageView) findViewById(R.id.activity5);
//        terrain1.setImageAlpha(90);
//        terrain2.setImageAlpha(90);
//        terrain3.setImageAlpha(90);
//        terrain4.setImageAlpha(90);
//        terrain5.setImageAlpha(90);
    }

    private void setName() {
        profilename = prefs.getString("name", "nothing");
        ImageView image = (ImageView) findViewById(R.id.userImage);
        String pic_url = prefs.getString("fb_profile_pic", "");
        if (pic_url !="") {
            Picasso.with(this)
                    .load(pic_url)
                    .into(image);
        }

        weather = (TextView) findViewById(R.id.userName);

        ImageView imageAdd = (ImageView) findViewById(R.id.userAddImage);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpWeather();
            }
        });
    }

    private void setUpSeekBars() {
        paceInt = prefs.getInt("pace", 0);

        slider2 = (SeekArc) findViewById(R.id.mainseek2);
        label2 = (TextView) findViewById(R.id.mainLabel2);
        slider2.setArcColor(R.color.colorAccent);
        distanceInt = prefs.getInt("distance", 0);
        slider2.setProgress(distanceInt);
        if (distanceInt == 42) {
            label2.setText("Distance: " + distanceInt + "+ km");
        } else {
            label2.setText("Distance: " + distanceInt + " km");
        }
        slider2.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if (i == 42) {
                    label2.setText("Distance: " + i + "+ km");
                } else {
                    label2.setText("Distance: " + i + " km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
        });

        slider3 = (SeekArc) findViewById(R.id.mainseek3);
        label3 = (TextView) findViewById(R.id.mainLabel3);
        slider3.setArcColor(R.color.colorAccent);
        timeInt = prefs.getInt("time", 0);
        slider3.setProgress(timeInt);
//        timeCheck(timeInt);
        slider3.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if(!weatherCalled) {
//                    timeCheck(i);
                }
                else {
                    timeWeather(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });


        groupSizeInt = prefs.getInt("group", 0);
    }

    private void timeCheck(int i) {
        if (i <= 48) {
            label3.setText("Dawn (6am - 9am)");
        } else if (i > 48 && i <= 96) {
            label3.setText("Morning (9am - 12pm)");
        } else if (i > 96 && i <= 144) {
            label3.setText("Afternoon (12am - 3pm)");
        } else if (i > 144 && i <= 192) {
            label3.setText("Midday (3am - 6pm)");
        } else {
            label3.setText("Evening (6pm-9pm)");
        }
    }

    private void timeWeather(int i) {
        TextView temp = (TextView) findViewById(R.id.temperature);
        ImageView icon = (ImageView) findViewById(R.id.weatherIcon);
        int j;
        if (i <= 30) {
            j = 0;
        } else if (i > 30 && i <= 60) {
            j = 1;
        } else if (i > 60 && i <= 90) {
            j = 2;
        } else if (i > 90 && i <= 120) {
            j = 3;
        } else if (i > 120 && i <= 150) {
            j = 4;
        } else if (i > 150 && i <= 180) {
            j = 5;
        } else if (i > 180 && i <= 210) {
            j = 6;
        } else {
            j = 7;
        }

        Date df = new java.util.Date((j*10800 + dt) * 1000);
        String vv = new SimpleDateFormat("MM/dd HH").format(df);

        temps.get(j);
        temp.setText(temps.get(j).intValue() + " C");
        label3.setText(vv + ":00");

        String s = weathers.get(j);
        if (s.equals("scattered clouds")) {
            icon.setBackgroundResource(R.mipmap.weather_cloud_sun);
        } else if (s.equals("light rain")) {
            icon.setBackgroundResource(R.mipmap.weather_rain);
        } else if (s.equals("broken clouds")) {
            icon.setBackgroundResource(R.mipmap.weather_cloud_one);
        } else if (s.equals("few clouds")) {
            icon.setBackgroundResource(R.mipmap.weather_cloud_sun);
        } else if (s.equals("overcast clouds")) {
            icon.setBackgroundResource(R.mipmap.weather_cloud_one);
        } else if (s.equals("moderate rain")) {
            icon.setBackgroundResource(R.mipmap.weather_rain);
        } else if (s.equals("shower rain")) {
            icon.setBackgroundResource(R.mipmap.weather_rain);
        } else if (s.equals("drizzle")) {
            icon.setBackgroundResource(R.mipmap.weather_rain);
        } else if (s.equals("thunderstorm")) {
            icon.setBackgroundResource(R.mipmap.weather_thunder);
        } else {
            icon.setBackgroundResource(R.mipmap.weather_sun);
        }
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

    public void openProfile(View item) {
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

    public void openLogout(MenuItem item) {

        LoginManager.getInstance().logOut();

        Intent profileIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(profileIntent, 0);
        finishAffinity();

    }

//    public void activityRunning(View vieww) {
//        ImageView view1 = (ImageView) vieww.findViewById(R.id.activity1);
//        if (view1.getImageAlpha()==90) {
//            setUpTerrains();
//            view1.setImageAlpha(255);
//            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
//                    //Choose map style:
//                    cool_map_thick
//            );
//            mGoogleMap.setMapStyle(style);
//        } else {
//            view1.setImageAlpha(90);
//        }
//    }
//
//    public void activityBiking(View vieww) {
//        ImageView view2 = (ImageView) vieww.findViewById(R.id.activity2);
//        if (view2.getImageAlpha()==90) {
//            setUpTerrains();
//            view2.setImageAlpha(255);
//            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
//                    //Choose map style:
//                    colorful1
//            );
//            mGoogleMap.setMapStyle(style);
//        } else {
//            view2.setImageAlpha(90);
//        }
//    }
//
//    public void activityGym(View vieww) {
//        ImageView view3 = (ImageView) vieww.findViewById(R.id.activity3);
//        if (view3.getImageAlpha()==90) {
//            setUpTerrains();
//            view3.setImageAlpha(255);
//            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
//                    //Choose map style:
//                    blue_hues
//            );
//            mGoogleMap.setMapStyle(style);
//        } else {
//            view3.setImageAlpha(90);
//        }
//    }
//
//    public void activityYoga(View vieww) {
//        ImageView view4 = (ImageView) vieww.findViewById(R.id.activity4);
//        if (view4.getImageAlpha()==90) {
//            setUpTerrains();
//            view4.setImageAlpha(255);
//            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
//                    //Choose map style:
//                    monotone
//            );
//            mGoogleMap.setMapStyle(style);
//        } else {
//            view4.setImageAlpha(90);
//        }
//    }
//
//    public void activityZuma(View vieww) {
//        ImageView view5 = (ImageView) vieww.findViewById(R.id.activity5);
//        if (view5.getImageAlpha() == 90) {
//            setUpTerrains();
//            view5.setImageAlpha(255);
//            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
//                    //Choose map style:
//                    miastra
//            );
//            mGoogleMap.setMapStyle(style);
//        } else {
//            view5.setImageAlpha(90);
//        }
//    }

    private void setUpButton() {
        Button button = (Button) findViewById(R.id.mainMenuButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                String pic_url = prefs.getString("fb_profile_pic","");
                if (pic_url !="") {
                    ImageView profile_image = (ImageView) findViewById(R.id.pic2);
                    Picasso.with(MainActivity.this)
                            .load(pic_url)
                            .into(profile_image);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        removeListeners();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        removeListeners();
        super.onDestroy();
    }

    private void removeListeners() {
        queReadyCalledOnce=false;

        for (GeoQuery geoQuery : geoQueries) {
            geoQuery.removeAllListeners();
        }

//        for (String userId : userIdsWithListeners) {
//            mRunnerData.child(userId)
//                    .removeEventListener(userValueListener);
//        }
        for (RunnerClass runner : runners) {
            if (runner != null) {
                mRunnerData.child(runner.i_d).removeEventListener(userValueListener);
            }
        }
    }

    private void setUpRunButton() {
        final Button runButton = (Button) findViewById(R.id.runButton);

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuView = false;
                //Change view layout
                View box = findViewById(R.id.boxOverMap);
                View box2 = findViewById(R.id.recyclerViewInsideBox);
                Button buttonMenu = (Button) findViewById(R.id.mainMenuButton);
                Button buttonBack = (Button) findViewById(R.id.goBackButton);
                Button buttonJoin = (Button) findViewById(R.id.joinRunButton);

                box.setVisibility(View.GONE);
                box2.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.GONE);
                buttonBack.setVisibility(View.VISIBLE);
                setUpBackButton();
                runButton.setVisibility(View.GONE);
                buttonJoin.setVisibility(View.VISIBLE);
                buttonJoin.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        newEntryInFirebase();
                        return false;
                    }
                });

                //Set up adapter
                adapter = new RecyclerAdapter(MainActivity.this, runners);
                recV.setAdapter(adapter);

                //Find users around you
                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 10.0);
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        //Before queReady called (each user is new)
                        if (!queReadyCalledOnce) {

                            runners.clear();
                            userValueListener = mRunnerData.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    RunnerClass rc = dataSnapshot.getValue(RunnerClass.class);
                                    //When new user joins
                                    if (!runners.contains(rc)) {
                                        runners.add(0, rc);
                                    }
                                    //When user changes data
                                    else {
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        //New users after queReady called
                        else {
                            Toast.makeText(MainActivity.this, "A new runner is active in your area", Toast.LENGTH_SHORT).show();
                            userValueListener = mRunnerData.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    RunnerClass rc = dataSnapshot.getValue(RunnerClass.class);
                                    //When new user joins
                                    if (!runners.contains(rc)) {
                                        runners.add(0, rc);
                                        adapter.notifyItemInserted(0);
                                    }
                                    //When user changes data
                                    else {
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
//                        Location to = new Location("to");
//                        to.setLatitude(location.latitude);
//                        to.setLongitude(location.longitude);
//                        if (!queReadyCalledOnce) {
//                            userIdsToLocations.put(key, to);
//                        } else {
//                            userIdsToLocations.put(key, to);
//                            mRunnerData.child(key).addValueEventListener(userValueListener);
//                            userIdsWithListeners.add(key);
//                        }
                    }

                    @Override
                    public void onKeyExited (String key) {
                        Toast.makeText(MainActivity.this, "Someone here has exited", Toast.LENGTH_SHORT).show();
//                        mRunnerData.child(key).removeEventListener(userValueListener);
                        int position = getRunnerPosition(key);
                        runners.remove(position);
                        adapter.notifyItemRemoved(position);
//                        mRunnerData.child(key).removeValue();

//                        if (userIdsWithListeners.contains(key)) {
//                            int position = getRunnerPosition(key);
//                            runners.remove(position);
//                            adapter.notifyItemRemoved(position);
//                        }
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        Toast.makeText(MainActivity.this, "A user has changed route", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGeoQueryReady() {
                        if (!queReadyCalledOnce) {
                            //Quick delay so all initial entries catch up
                            final Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setRunners(runners);
                                    queReadyCalledOnce = true;
                                }

                            }, 500);
                        } else {
                            Toast.makeText(MainActivity.this, "queready2", Toast.LENGTH_SHORT).show();
                        }
//                        initialListSize = userIdsToLocations.size();
//                        if (initialListSize == 0) {
//                            queReadyCalledOnce = true;
//                        }
//                        iterationCount = 0;
//                        for (String userId : userIdsToLocations.keySet()){
//                            mRunnerData.child(userId).addValueEventListener(userValueListener);
//                            userIdsWithListeners.add(userId);
//                        }
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
                geoQueries.add(geoQuery);
//
//                // query around current user location with 1.6km radius
//                final Set<String> runnersNearby = new HashSet<String>();
//
//                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 1.6);
//
//                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                    @Override
//                    public void onKeyEntered(String username, GeoLocation location) {
//                        runnersNearby.add(username);
//                        // additional code, like displaying a pin on the map
//                        // and adding Firebase listeners for this user
//                    }
//
//                    @Override
//                    public void onKeyExited(String username) {
//                        runnersNearby.remove(username);
//                        // additional code, like removing a pin from the map
//                        // and removing any Firebase listener for this user
//                    }
//
//                    @Override
//                    public void onKeyMoved(String key, GeoLocation location) {
//
//                    }
//
//                    @Override
//                    public void onGeoQueryReady() {
//
//                    }
//
//                    @Override
//                    public void onGeoQueryError(DatabaseError error) {
//
//                    }
//                });
            }

            private void setUpBackButton() {
                Button buttonBack = (Button) findViewById(R.id.goBackButton);
                buttonBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBack();

                    }
                });
            }
        });
        runButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                newEntryInFirebase();

                //Start mapbox tile activity
                Intent runIntent = new Intent(MainActivity.this, RunActivity.class);
                startActivity(runIntent);
                return false;
            }
        });


    }

    private void newEntryInFirebase() {
        String pic_url = prefs.getString("fb_profile_pic","");
        String id = mRunnerData.push().getKey();
        RunnerClass runner = new RunnerClass(lat, lng, profilename, slider2.getProgress(), label3.getText().toString(), paceInt, groupSizeInt, pic_url, id);

        //Update runners and runner_locations in Firebase
        mRunnerData.child(id).setValue(runner);
        geoFire.setLocation(id, new GeoLocation(lat, lng));
    }

    private void clickBack() {
        mainMenuView = true;
        removeListeners();

        View box = findViewById(R.id.boxOverMap);
        View box2 = findViewById(R.id.recyclerViewInsideBox);
        Button buttonMenu = (Button) findViewById(R.id.mainMenuButton);
        Button buttonBack = (Button) findViewById(R.id.goBackButton);
        Button buttonJoin = (Button) findViewById(R.id.joinRunButton);
        Button runButton = (Button) findViewById(R.id.runButton);

        box.setVisibility(View.VISIBLE);
        box2.setVisibility(View.GONE);
        buttonMenu.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.GONE);
        runButton.setVisibility(View.VISIBLE);
        buttonJoin.setVisibility(View.GONE);
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
        LatLng startplace2 = new LatLng(40, -74);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startplace2, 1));
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
                //Choose map style:
                colorful1
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
                        final Handler handler4 = new Handler();
                        handler4.postDelayed(new Runnable() {
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
                                    Toast.makeText(MainActivity.this, "LatLng == null #2", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 3500);
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
            lat = ll.latitude;
            lng = ll.longitude;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}


