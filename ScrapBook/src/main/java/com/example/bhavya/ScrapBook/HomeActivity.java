package com.example.bhavya.ScrapBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

//From here , user can click image, edit pictures,
public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    Button cameraButton,logoutButton,galleryButton;
    Button timerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_home);
        else
        setContentView(R.layout.content_new_home);
        try {
            cameraButton = (Button) findViewById(R.id.image_button);
            galleryButton = (Button) findViewById(R.id.gallery_button);
            logoutButton = (Button) findViewById(R.id.logout_button);
            timerButton = (Button) findViewById(R.id.schedule_timer);
            timerButton.setOnClickListener(this);
            cameraButton.setOnClickListener(this);
            galleryButton.setOnClickListener(this);
            logoutButton.setOnClickListener(this);

        }catch(Exception e){

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.content_new_home);
            cameraButton = (Button) findViewById(R.id.image_button);
            galleryButton = (Button) findViewById(R.id.gallery_button);
            logoutButton = (Button) findViewById(R.id.logout_button);
            timerButton = (Button) findViewById(R.id.schedule_timer);
            timerButton.setOnClickListener(this);
            cameraButton.setOnClickListener(this);
            galleryButton.setOnClickListener(this);
            logoutButton.setOnClickListener(this);

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.content_home);
            cameraButton = (Button) findViewById(R.id.image_button);
            galleryButton = (Button) findViewById(R.id.gallery_button);
            logoutButton = (Button) findViewById(R.id.logout_button);
            timerButton = (Button) findViewById(R.id.schedule_timer);
            timerButton.setOnClickListener(this);
            cameraButton.setOnClickListener(this);
            galleryButton.setOnClickListener(this);
            logoutButton.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.image_button:
                    startActivity(new Intent(this, ImageCapture.class));
                    break;

                case R.id.gallery_button:
                    File f = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook");
                    File[] files = f.listFiles();
                    if (files != null) {
                        startActivity(new Intent(this, AddNotes.class));
                    } else {
                        Toast.makeText(this, "No Images", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.logout_button:
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("accessToken", null);
                    edit.commit();
                    startActivity(new Intent(this, Login.class));
                    break;
                case R.id.schedule_timer:
                    // Create a new calendar set to the date chosen
                    // we set the time to midnight (i.e. the first minute of that day)
                    // if no ids were returned, something is wrong. get out.
                    //  if (ids.length == 0)
                    //    System.exit(0);
                    startActivity(new Intent(this, DateActivity.class));
                    // begin output
                    //   System.out.println("Current Time");
                    //  Calendar c= Calendar.getInstance();
                    //c.setTimeInMillis(System.currentTimeMillis());
                    // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                    //scheduleClient.setAlarmForNotification(c);
                    //Toast.makeText(this, "Notification set for: " + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH) +  "/" + c.get(Calendar.YEAR),Toast.LENGTH_LONG).show();
                    break;
            }
        }catch(Exception e){

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
