package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

//Activity class which sets alarm task for the notification.

public class DateActivity extends AppCompatActivity {
    DatePicker timerButton;
    private ScheduleClient scheduleClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_date);
        else
        setContentView(R.layout.content_date_new);

        try{

              scheduleClient = new ScheduleClient(this);

              scheduleClient.doBindService();
              Calendar c = Calendar.getInstance();
              timerButton = (DatePicker) findViewById(R.id.date_picker);
              timerButton.init(
                      c.get(Calendar.YEAR),
                      c.get(Calendar.MONTH),
                      c.get(Calendar.DAY_OF_MONTH),
                      new DatePicker.OnDateChangedListener() {

                          @Override
                          public void onDateChanged(DatePicker view,
                                                    int year, int monthOfYear, int dayOfMonth) {

                              Calendar c = Calendar.getInstance();
                             c.set(Calendar.DAY_OF_MONTH,4);
                              c.set(Calendar.YEAR,2015);
                              c.set(Calendar.MONTH,11);
                              c.set(Calendar.HOUR_OF_DAY,20);
                              c.set(Calendar.MINUTE, 34);
                              c.set(Calendar.SECOND,0);
                               scheduleClient.setAlarmForNotification(c);
                            //  c.setTimeInMillis(System.currentTimeMillis());
                              //scheduleClient.setAlarmForNotification(c);
                              Toast.makeText(getApplicationContext(), "Notification set for: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                          }
                      });

      }catch(Exception e){

      }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.content_date_new);
            try{

                scheduleClient = new ScheduleClient(this);

                scheduleClient.doBindService();
                Calendar c = Calendar.getInstance();
                timerButton = (DatePicker) findViewById(R.id.date_picker);
                timerButton.init(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH),
                        new DatePicker.OnDateChangedListener() {

                            @Override
                            public void onDateChanged(DatePicker view,
                                                      int year, int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                c.set(Calendar.YEAR,year);
                                c.set(Calendar.MONTH,monthOfYear);
                                c.set(Calendar.HOUR_OF_DAY,12);
                                c.set(Calendar.MINUTE, 0);
                                c.set(Calendar.SECOND,0);
                                Log.d("Time", String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
                                 scheduleClient.setAlarmForNotification(c);
                                //c.setTimeInMillis(System.currentTimeMillis());
                                //scheduleClient.setAlarmForNotification(c);
                                Toast.makeText(getApplicationContext(), "Notification set for: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                            }
                        });

            }catch(Exception e){

            }

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.content_date);
            try{

                scheduleClient = new ScheduleClient(this);
                scheduleClient.doBindService();
                Calendar c = Calendar.getInstance();
                timerButton = (DatePicker) findViewById(R.id.date_picker);
                timerButton.init(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH),
                        new DatePicker.OnDateChangedListener() {

                            @Override
                            public void onDateChanged(DatePicker view,
                                                      int year, int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
                                //  c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                //c.set(Calendar.YEAR,year);
                                //c.set(Calendar.MONTH,monthOfYear);
                                //c.set(Calendar.HOUR_OF_DAY,12);
                                //c.set(Calendar.MINUTE, 0);
                                //c.set(Calendar.SECOND,0);
                                // scheduleClient.setAlarmForNotification(c);
                                c.setTimeInMillis(System.currentTimeMillis());
                                scheduleClient.setAlarmForNotification(c);
                                Toast.makeText(getApplicationContext(), "Notification set for: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                            }
                        });

            }catch(Exception e){

            }
        }

    }

    public boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("bhavya", "Error checking internet connection", e);
            }
        } else {
            Log.d("bhavya", "No network available!");
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public void backPress(View view) {
        try {
            startActivity(new Intent(this, HomeActivity.class));

        } catch (Exception e) {

        }
    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        try {
            if (scheduleClient != null)
                scheduleClient.doUnbindService();

            super.onStop();
        }catch(Exception e){

        }
    }

}
