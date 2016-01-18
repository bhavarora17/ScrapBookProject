package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
//Upload class . which gives the grid view and helps the user to select pictures for the upload

public class NewActivity extends AppCompatActivity {

    Button upload;
    int arr[] = new int[500];
    int i=0;
@Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_new);
    try {
            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(this));
            final Bundle bundle = new Bundle();
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(NewActivity.this, "" + position,
                            Toast.LENGTH_SHORT).show();
                    arr[i++] = position;

                }
            });
            final Intent intent = new Intent(this, SharingActivity.class);
            upload = (Button) findViewById(R.id.uploadButton);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean b = hasActiveInternetConnection(getBaseContext());
                    if (b == false) {
                        Toast.makeText(getBaseContext(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    } else {

                        bundle.putIntArray("FilesToUpload", arr);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }
            });

        } catch (Exception e) {

        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
