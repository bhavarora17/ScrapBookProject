package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhotoContent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 * Facebook Login Fragment , for users who login just before the upload
 */
public class NewMain extends Fragment {
    private CallbackManager mCallbackManager;
    private LoginManager manager;
    Button shareButton;
    SharePhotoContent content;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            try {
                SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = shre.edit();


                AccessToken accessToken = loginResult.getAccessToken();

                edit.putString("accessToken", accessToken.toString());

                edit.commit();
                //  Toast.makeText(getContext(), "Uploading to Facebook", Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "Uploading Done", Toast.LENGTH_LONG).show();

                startActivity(new Intent(getContext(), NewActivity.class));

                // LoginManager.getInstance().logInWithPublishPermissions(MainFragment, Arrays.asList("publish_actions"));
                // shareButton.setVisibility(View.VISIBLE);
                // shareButton.setOnClickListener(this);


            }catch(Exception e){

            }
        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            //shareButton.setVisibility(View.INVISIBLE);
        }
    };

    public NewMain() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
            try {
                FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
                mCallbackManager = CallbackManager.Factory.create();
                //    List<String> permissionNeeds = Arrays.asList("publish_actions");
                manager = LoginManager.getInstance();
                manager.registerCallback(mCallbackManager, mCallback);

            } catch (Exception e) {

            }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_main, container, false);

    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
 try{
        LoginButton loginButton=(LoginButton) view.findViewById(R.id.new_login_button);
        // shareButton=(Button)view.findViewById(R.id.fb_share_button);
        // shareButton.se
        // shareButton.setReadPermissions(Arrays.asList("publish_actions"));
        // shareButton.setFragment(this);
        //  loginButton.setReadPermissions("user_photos");
        // loginButton.setReadPermissions(Arrays.asList("user_checkins"));
        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
 }catch(Exception e){

 }
 }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
  try{
      boolean b = hasActiveInternetConnection(getActivity().getBaseContext());
        if (b == false) {
            Toast.makeText(getActivity().getBaseContext(), "Internet Not Available", Toast.LENGTH_SHORT).show();
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
  }catch(Exception e){

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
        boolean b=false;
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo != null)
                b=true;
            return b;


    }
}

