package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

//import java.time.*;


public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout2;
    SeekBar profileseek1, profileseek2, profileseek3, profileseek4;
    TextView profilelabel1, profilelabel2, profilelabel3, profilelabel4;
    int savedDistance, savedTime, savedPace, savedGroupSize;

    //Initializing variables
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ImageView profile_image_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_profile);
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout2);

        //Putting information into these variables
        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        nameEditorSetUp();
        ageEditorSetUp();
        buttonSetUp();
        genderSetUp();
        partnerSetUp();
        terrainSetUp();
        seekBarSetUp();
//        loginSetUp();
        fbPicSetup();

    }



    private void fbPicSetup() {

        String pic_url = prefs.getString("fb_profile_pic", "");
        if (pic_url !="") {
            ImageView profile_image = (ImageView) findViewById(R.id.pic);
            Picasso.with(this)
                    .load(pic_url)
                    .into(profile_image);
        }

    }

//    private void loginSetUp() {
//        ImageView image=(ImageView)findViewById(R.id.circle_crop);
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LoginManager.getInstance().logOut();
//                    Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finishAffinity();
//                }
//            });


    private void nameEditorSetUp() {
        TextView nameTextView = (TextView) findViewById(R.id.Name);
        nameTextView.setText(prefs.getString("name", ""));
//        Toast.makeText(this, prefs.getString("name",""), Toast.LENGTH_SHORT).show();
        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("name", s.toString());
                editor.commit();
            }
        });
    }

    private void ageEditorSetUp() {

//        LocalDate today             = LocalDate.now();
//        LocalDate birthday          = LocalDate.of(1982, 9, 26);
//        LocalDate thisYearsBirthday = birthday.with(Year.now());
//
//        long age = ChronoUnit.YEARS.between(birthday, today);
//
//        if (thisYearsBirthday.equals(today))
//        {
//            System.out.println("It is your birthday, and your Age is " + age);
//        }
//        else
//        {
//            long daysUntilBirthday = ChronoUnit.DAYS.between(today, thisYearsBirthday);
//            System.out.println("Your age is " + age + ". " + daysUntilBirthday + " more days until your birthday!");
//        }

            TextView ageTextView = (TextView) findViewById(R.id.Age);
            ageTextView.setText(prefs.getString("birthday", ""));
            ageTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editor.putString("birthday", s.toString());
                    editor.commit();
                }
            });
    }

    private void seekBarSetUp() {

        //Distance
        profileseek1 = (SeekBar) findViewById(R.id.DistanceSeekProfile);
        profilelabel1 = (TextView) findViewById(R.id.DistanceLabelProfile);
        profileseek1.setMax(42);
        //Set bar progress to last saved distance or 0
        int lengthInt = prefs.getInt("distance", 0);
        profileseek1.setProgress(lengthInt);

        //Set initial text according to last saved distance
        if (lengthInt == 42) {
            profilelabel1.setText("Distance: " + lengthInt + "+ km");
        } else {
            profilelabel1.setText("Distance: " + lengthInt + " km");
        }

        profileseek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 42) {
                    profilelabel1.setText("Distance: " + progress + "+ km");
                } else {
                    profilelabel1.setText("Distance: " + progress + " km");
                }
                savedDistance=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Save distance
                editor.putInt("distance", savedDistance);
                editor.commit();
            }
        });

        //Time
        profileseek2 = (SeekBar) findViewById(R.id.TimeSeekProfile);
        profilelabel2 = (TextView) findViewById(R.id.TimeLabelProfile);
        profileseek2.setMax(100);
        int timeInt = prefs.getInt("time", 0);
        profileseek2.setProgress(timeInt);
        if (timeInt <= 20) {
            profilelabel2.setText("Early Morning (6am - 9am)");
        } else if (timeInt > 20 && timeInt <= 40) {
            profilelabel2.setText("Late Morning (9am - 12pm)");
        } else if (timeInt > 40 && timeInt <= 60) {
            profilelabel2.setText("Afternoon (12am - 3pm)");
        } else if (timeInt > 60 && timeInt <= 80) {
            profilelabel2.setText("Midday (3am - 6pm)");
        } else {
            profilelabel2.setText("Evening (6pm-9pm)");
        }
        profileseek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 20) {
                    profilelabel2.setText("Early Morning (6am - 9am)");
                } else if (progress > 20 && progress <= 40) {
                    profilelabel2.setText("Late Morning (9am - 12pm)");
                } else if (progress > 40 && progress <= 60) {
                    profilelabel2.setText("Afternoon (12am - 3pm)");
                } else if (progress > 60 && progress <= 80) {
                    profilelabel2.setText("Midday (3am - 6pm)");
                } else {
                    profilelabel2.setText("Evening (6pm-9pm)");
                }
                savedTime = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("time", savedTime);
                editor.commit();
            }
        });

        //Pace
        profileseek3 = (SeekBar) findViewById(R.id.PaceSeekProfile);
        profilelabel3 = (TextView) findViewById(R.id.PaceLabelProfile);
        profileseek3.setMax(20);
        int paceInt = prefs.getInt("pace", 0);
        profileseek3.setProgress(paceInt);
        profilelabel3.setText("Your Pace: " + paceInt + "km/h");
        profileseek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                profilelabel3.setText("Your Pace: " + progress + "km/h");
                savedPace = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("pace", savedPace);
                editor.commit();
            }
        });

        //Group Size
        profileseek4 = (SeekBar) findViewById(R.id.GroupSizeSeekProfile);
        profilelabel4 = (TextView) findViewById(R.id.GroupSizeLabelProfile);
        profileseek4.setMax(100);
        int groupInt = prefs.getInt("group", 0);
        profileseek4.setProgress(groupInt);
        if (groupInt <= 20) {
            profilelabel4.setText("Running partner (2 people)");
        } else if (groupInt > 20 && groupInt <= 60) {
            profilelabel4.setText("Small group (2-4 people)");
        } else {
            profilelabel4.setText("Large group (5-10 people)");
        }
        profileseek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 20) {
                    profilelabel4.setText("Running partner (2 people)");
                } else if (progress > 20 && progress <= 60) {
                    profilelabel4.setText("Small group (2-4 people)");
                } else {
                    profilelabel4.setText("Large group (5-10 people)");
                }
                savedGroupSize = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("group", savedGroupSize);
                editor.commit();
            }
        });
    }

    private void genderSetUp() {
        TextView nameTextView = (TextView) findViewById(R.id.Gender);
        nameTextView.setText(prefs.getString("gender", ""));
//        Spinner spinner = (Spinner) findViewById(R.id.Gender);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.gender_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
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

    public void openProfile(View item){
        mDrawerLayout2.closeDrawer(Gravity.LEFT);
    }

    public void openHome(MenuItem item) {
        finish();
    }

    public void openAbout(MenuItem item){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Menu_Option", "about");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void openLogout(MenuItem item) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finishAffinity();


    }

    private void buttonSetUp() {

        Button button = (Button) findViewById(R.id.profilebutton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout2.openDrawer(Gravity.LEFT);
                String pic_url2 = prefs.getString("fb_profile_pic","");
                if (pic_url2 !="") {
                    ImageView profile_image2 = (ImageView) findViewById(R.id.pic2);
                    Picasso.with(ProfileActivity.this)
                            .load(pic_url2)
                            .into(profile_image2);
                }
            }

        }

        );


//        Button button = (Button) findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}
