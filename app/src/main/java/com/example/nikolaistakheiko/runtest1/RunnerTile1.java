package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.services.Constants;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-11.
 */

public class RunnerTile1 extends ArrayAdapter<RunnerClass> {
    private Activity context;
    private List<RunnerClass> runnerList;

    public RunnerTile1(Activity context, List<RunnerClass> runnerList){
        super(context, R.layout.tile_type_1, runnerList);
        this.context = context;
        this.runnerList = runnerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.tile_type_1, null, true);

        TextView username = (TextView) listViewItem.findViewById(R.id.listName);
        TextView timeofday = (TextView) listViewItem.findViewById(R.id.listTimeofday);
        TextView distance = (TextView) listViewItem.findViewById(R.id.listLabel1);
        TextView pace = (TextView) listViewItem.findViewById(R.id.listLabel2);
        TextView group = (TextView) listViewItem.findViewById(R.id.listLabel3);
        SeekArc distanceArc = (SeekArc) listViewItem.findViewById(R.id.listSeek1);
        SeekArc paceArc = (SeekArc) listViewItem.findViewById(R.id.listSeek2);
        SeekArc groupArc = (SeekArc) listViewItem.findViewById(R.id.listSeek3);

        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.listTile);

        RunnerClass runner = runnerList.get(position);
        Double latitude = runner.getLat();
        Double longitude = runner.getLng();

        MapboxStaticImage veniceStaticImage = new MapboxStaticImage.Builder()
                .setAccessToken("pk.eyJ1IjoidG9vbmlrb2xhaSIsImEiOiJjajVuMDF0NHcydWZuMnhtbnpvejZscW9pIn0.NcXJNGFueueFqD-Jg3Gwiw")
                .setStyleId(Constants.MAPBOX_STYLE_OUTDOORS)
                .setLat(latitude) // Image center Latitude
                .setLon(longitude) // Image center longitude
                .setZoom(13)
                .setWidth(330) // Image width
                .setHeight(320) // Image height
                .setRetina(true) // Retina 2x image will be returned
                .build();

        Picasso.with(context).load(veniceStaticImage.getUrl().toString()).into(imageView);

        username.setText(runner.getUsername());
        timeofday.setText(runner.getTimeofday());
        distance.setText(runner.getDistance()+"km");
        distanceArc.setProgress(runner.getDistance());
        pace.setText(runner.getPace()+"km/h");
        paceArc.setProgress(runner.getPace());
        int int1 = runner.getGroup();
        String groupText;
        if (int1 <= 20) {
            groupText="Partner";
        } else if (int1 > 20 && int1 <= 60) {
            groupText="Small group";
        } else {
            groupText="Large group";
        }
        group.setText(groupText);
        groupArc.setProgress(runner.getGroup());

        return listViewItem;
    }
}
