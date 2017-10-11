package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;

import java.util.List;

/**
 * Created by nikolaistakheiko on 2017-10-11.
 */

public class RunnerList extends ArrayAdapter<PushData> {
    private Activity context;
    private List<PushData> runnerList;

    public RunnerList(Activity context, List<PushData> runnerList){
        super(context, R.layout.list_layout, runnerList);
        this.context = context;
        this.runnerList = runnerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView username = (TextView) listViewItem.findViewById(R.id.listName);
        TextView timeofday = (TextView) listViewItem.findViewById(R.id.listTimeofday);
        TextView distance = (TextView) listViewItem.findViewById(R.id.listLabel1);
        TextView pace = (TextView) listViewItem.findViewById(R.id.listLabel2);
        TextView group = (TextView) listViewItem.findViewById(R.id.listLabel3);
        SeekArc distanceArc = (SeekArc) listViewItem.findViewById(R.id.listSeek1);
        SeekArc paceArc = (SeekArc) listViewItem.findViewById(R.id.listSeek2);
        SeekArc groupArc = (SeekArc) listViewItem.findViewById(R.id.listSeek3);

        PushData runner = runnerList.get(position);

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
