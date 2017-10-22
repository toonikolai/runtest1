package com.example.nikolaistakheiko.runtest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static java.net.Proxy.Type.HTTP;

public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String name;
    String gender;
    String birthday;
    String email;
    GraphRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getBooleanExtra("EXIT", false)) {
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        prefs = this.getSharedPreferences("myPrefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        loginButton = (LoginButton) findViewById(R.id.fb_login_btn);
        callbackManager = CallbackManager.Factory.create();

        //get permissions for these things
//        loginButton.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends"));

        accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }

        };

        accessToken = AccessToken.getCurrentAccessToken();

        if (AccessToken.getCurrentAccessToken() == null) {

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    //saved userId in shared prefs and called it in Profile Activity to get profile picture from fb URL
                    editor.putString("fb_profile_pic", "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");
                    editor.commit();



                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    finish();

                }

                @Override
                public void onCancel() {

                    Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {

                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();

                }
            });

        } else {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
