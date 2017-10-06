package com.example.nikolaistakheiko.runtest1;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class RunActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMapTile;
    int tileNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        MapFragment mapFragment3 = (MapFragment) getFragmentManager().findFragmentById(R.id.mapTile3);
        mapFragment3.getMapAsync(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tileNumber++;
                MapFragment mapFragment6 = (MapFragment) getFragmentManager().findFragmentById(R.id.mapTile6);
                mapFragment6.getMapAsync(RunActivity.this);
            }
        }, 0010);
//        final Handler handler2 = new Handler();
//        handler2.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tileNumber++;
//                MapFragment mapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.mapTile1);
//                mapFragment1.getMapAsync(RunActivity.this);
//            }
//        }, 0030);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapTile = googleMap;
        MapStyleOptions style;
        LatLng startplace;
        if (tileNumber==0) {
            startplace = new LatLng(-33.8688, 151.2093);
            style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
                    //Choose map style:
                    monotone
            );
        } else {
            startplace = new LatLng(43.6532, -79.3832);
            style = MapStyleOptions.loadRawResourceStyle(this, R.raw.
                    //Choose map style:
                    monotone
            );
        }

        mMapTile.moveCamera(CameraUpdateFactory.newLatLngZoom(startplace, 15));
        mMapTile.setMapStyle(style);
        mMapTile.getUiSettings().setAllGesturesEnabled(false);
        mMapTile.setBuildingsEnabled(false);
        mMapTile.setIndoorEnabled(false);
    }
}
