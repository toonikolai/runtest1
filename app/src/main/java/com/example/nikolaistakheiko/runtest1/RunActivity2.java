package com.example.nikolaistakheiko.runtest1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-14.
 */

public class RunActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference databaseRunners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run2);

        databaseRunners = FirebaseDatabase.getInstance().getReference("runners");


    }


    @Override
    protected void onStart() {
        super.onStart();

        recyclerView = (RecyclerView) findViewById(R.id.runRecView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<RunnerClass, RunnerViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RunnerClass, RunnerViewHolder>(
                RunnerClass.class,
                R.layout.tile_type_map,
                RunnerViewHolder.class,
                databaseRunners
        ) {
            @Override
            protected void populateViewHolder(RunnerViewHolder viewHolder, RunnerClass model, int position) {
                viewHolder.setUpMap(RunActivity2.this, model.lat, model.lng);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RunnerViewHolder extends RecyclerView.ViewHolder {

        View mView2;
        ImageView imageView;

        public RunnerViewHolder(View itemView) {
            super(itemView);
            mView2 = itemView;
        }

        public void setUpMap(Context context, Double latitude, Double longitude) {
            imageView = (ImageView) mView2.findViewById(R.id.imageView);
            MapboxStaticImage veniceStaticImage = new MapboxStaticImage.Builder()
                    .setAccessToken("pk.eyJ1IjoidG9vbmlrb2xhaSIsImEiOiJjajVuMDF0NHcydWZuMnhtbnpvejZscW9pIn0.NcXJNGFueueFqD-Jg3Gwiw")
                    .setStyleId(Constants.MAPBOX_STYLE_DARK)
                    .setLat(latitude) // Image center Latitude
                    .setLon(longitude) // Image center longitude
                    .setZoom(13)
                    .setWidth(320) // Image width
                    .setHeight(320) // Image height
                    .setRetina(true) // Retina 2x image will be returned
                    .build();

            Picasso.with(context).load(veniceStaticImage.getUrl().toString()).into(imageView);

//            Mapbox.getInstance(context, "pk.eyJ1IjoidG9vbmlrb2xhaSIsImEiOiJjajVuMDF0NHcydWZuMnhtbnpvejZscW9pIn0.NcXJNGFueueFqD-Jg3Gwiw");
//            mapView = (MapView) mView2.findViewById(R.id.mapboxView);
//            mapView.onCreate(b);
//            mapView.setStyleUrl(Style.DARK);
//
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(final MapboxMap mapboxMap) {
//                    map = mapboxMap;
//                }
//            });

        }
    }
}
