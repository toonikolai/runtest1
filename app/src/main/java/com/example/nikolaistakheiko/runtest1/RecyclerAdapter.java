package com.example.nikolaistakheiko.runtest1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nikolaistakheiko on 2017-10-20.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    private List<RunnerClass> runnersInAdapter;
    private Context mContext;

    public RecyclerAdapter(Context context, List<RunnerClass> runnerList) {
        this.runnersInAdapter = runnerList;
        this.mContext = context;
    }

    public void setRunners(List<RunnerClass> list) {
        runnersInAdapter = list;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_over_map, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        View mView3 = customViewHolder.mView3;
        final RunnerClass runner = runnersInAdapter.get(i);

        //Name
        TextView name = (TextView) mView3.findViewById(R.id.userTextName);
        name.setText(runner.getUsername());

        //User Pic
        ImageView image = (ImageView) mView3.findViewById(R.id.userImage);
        String pic_url = (String) runner.getPic_url();
        if (pic_url !="") {
            Picasso.with(mContext)
                    .load(pic_url)
                    .into(image);
        }

        //Seek Bars
        SeekArc uSeek1 = (SeekArc) mView3.findViewById(R.id.userSeek1);
        SeekArc uSeek2 = (SeekArc) mView3.findViewById(R.id.userSeek2);
        SeekArc uSeek3 = (SeekArc) mView3.findViewById(R.id.userSeek3);
        uSeek1.setProgress(runner.getDistance());
        uSeek2.setProgress(runner.getPace());
        uSeek3.setProgress(runner.getGroup());

        //Seek Bar Labels
        TextView uLabel1 = (TextView) mView3.findViewById(R.id.userLabel1);
        TextView uLabel2 = (TextView) mView3.findViewById(R.id.userLabel2);
        TextView uLabel3 = (TextView) mView3.findViewById(R.id.userLabel3);
        String s1 = runner.getDistance() + " km";
        String s2 = runner.getPace() + " km/h";
        String s3 = "People: " + runner.getDistance();
        uLabel1.setText(s1);
        uLabel2.setText(s2);
        uLabel3.setText(s3);

        //Request run
        Button requestButton = (Button) mView3.findViewById(R.id.requestJoinButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(runner);
            }
        });
    }

    private void sendNotification(RunnerClass runner) {
        final String uid = runner.getUi_d();
        final String username = runner.getUsername();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT  = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String receivingUID = uid;

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NTkxYWEwYTktNzI4MS00ZDZmLTg0ZWYtNWZmYzRhMmRlYzc4");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"47d3e689-1483-4901-9665-e476c6079bbc\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + receivingUID + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + username + " wants to go for a run with you.\"}"
                                + "}";

                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);


                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != runnersInAdapter ? runnersInAdapter.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView3;
        public CustomViewHolder(View view) {
            super(view);
            mView3 = view;
        }

    }

}
