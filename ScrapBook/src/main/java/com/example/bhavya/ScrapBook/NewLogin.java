package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//If the user is not logged in with facebook before, should be logged in from here before the upload

public class NewLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        try {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                if (prefs != null && prefs.getString("accessToken", null) != null) {
                        startActivity(new Intent(this, NewActivity.class));
            }
        } catch (Exception e) {

        }
    }

    public boolean hasActiveInternetConnection(Context context) {
        try {
            if (isNetworkAvailable()) {
                try {
                    final HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    final boolean x[]=new boolean[2];
                    Runnable t = new Runnable() {
                        public void run() {
                            try {
                                urlc.connect();
                                if (urlc.getResponseCode() == 200) {
                                    x[0] = true;
                                    x[1] = true;
                                }
                                else{
                                    x[0] = false;
                                    x[1] = true;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    new Thread(t).start();
                    while(true){
                        if(x[1]==true){
                            return(x[0]);
                        }
                        else
                            continue;
                    }


                } catch (IOException e) {
                    Log.e("bhavya", "Error checking internet connection", e);
                }catch(Exception e){

                }

            } else {
                Log.d("bhavya", "No network available!");
            }
        }catch(Exception e){

        }
        return false;
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        } catch (Exception e) {

        }
        return false;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
