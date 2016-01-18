package com.example.bhavya.ScrapBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

// First Activity in the Application
//We Check if the user is already logged in facebook
//If yes,we directly go to Home Screen , else login activity

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs != null && prefs.getString("accessToken", null) != null)
                startActivity(new Intent(this, HomeActivity.class));
            else
                startActivity(new Intent(this, Login.class));
        }catch(Exception e){

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onKeyDown(int keycode, KeyEvent event) {
        try {
            if (keycode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(true);
            }
        }catch(Exception e){

        }
        return super.onKeyDown(keycode, event);
    }
}
