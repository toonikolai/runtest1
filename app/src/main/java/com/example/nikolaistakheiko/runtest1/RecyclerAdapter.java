package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by nikolaistakheiko on 2017-10-20.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> implements SharedPreferences{
    private List<RunnerClass> runnersInAdapter;
    private Context mContext;

    //SharedPreferences
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //My location
    private Double myLat;
    private Double myLng;
    private String myUsername;
    private int myDistance;
    private String myTimeofday;
    private int myPace;
    private int myGroup;
    private String myPic_url;
    private String myUi_d;
    private String myI_d;



    public RecyclerAdapter(Context context, List<RunnerClass> runnerList) {
        this.runnersInAdapter = runnerList;
        this.mContext = context;

        //SharedPreferences
        prefs = mContext.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        //my location
        myLat = getDouble(prefs, "myLat", 69);
        myLng = getDouble(prefs, "myLng", -69);
        myUsername = prefs.getString("myUsername", "");
        myDistance = prefs.getInt("myDistance", 0);
        myTimeofday = prefs.getString("myTimeofday", "");
        myPace = prefs.getInt("myPace", 0);
        myGroup = prefs.getInt("myGroup", 0);
        myPic_url = prefs.getString("myPic_url", "");
        myUi_d = prefs.getString("myUi_d", "");
        myI_d = prefs.getString("myI_d", "");
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
        String pic_url = runner.getPic_url();
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

        if(myLat == 69 || myLat == null) {
            requestButton.setVisibility(View.GONE);
        } else {
            requestButton.setVisibility(View.VISIBLE);
        }

        final DatabaseReference mRequestUser = FirebaseDatabase.getInstance().getReference("runner_requests/" + runner.getI_d());

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(runner);
                addToUserDatabase(runner, mRequestUser);
                }
        });
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private void addToUserDatabase(RunnerClass runner, DatabaseReference mRequestUser) {
        //Adding runner profile instance to request category of Firebase
        RunRequestClass request = new RunRequestClass(myLat, myLng, myUsername, myDistance, myTimeofday, myPace, myGroup, myPic_url, myUi_d, myI_d);
        Map<String, Object> requestUpdate = new HashMap<>();
        requestUpdate.put(myI_d, request);
        mRequestUser.updateChildren(requestUpdate);
    }

    private void sendNotification(RunnerClass runner) {
        final String uid = runner.getUi_d();
//        final String username = runner.getUsername();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT  = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

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

                        //Notification composition
                        String strJsonBody = "{"
                                + "\"app_id\": \"47d3e689-1483-4901-9665-e476c6079bbc\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + uid + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + MainActivity.profilename + " wants to go for a run with you.\"}"
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

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return null;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView3;
        public CustomViewHolder(View view) {
            super(view);
            mView3 = view;
        }

    }

}
