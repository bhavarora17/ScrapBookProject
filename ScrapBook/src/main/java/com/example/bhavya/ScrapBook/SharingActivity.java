package com.example.bhavya.ScrapBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import org.json.JSONException;

import java.io.File;

//Uploads pictures to Facebook, and uploads only those which werent uploaded before

public class SharingActivity extends AppCompatActivity {

    PhotoUploadHelper helper=new PhotoUploadHelper(this);
    private CallbackManager mCallbackManager;
    private LoginManager manager;
    int arr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_actvity);
        try {
            Bundle extras = getIntent().getExtras();
            arr = extras.getIntArray("FilesToUpload");
            try {
                uploadActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){

        }
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
try {
    SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor edit = shre.edit();

    AccessToken accessToken = loginResult.getAccessToken();

    edit.putString("accessToken", accessToken.toString());

    edit.commit();
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
            try{

            }catch(Exception e){

            }
        }
    };
    public void uploadActivity() throws JSONException {
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());

            mCallbackManager = CallbackManager.Factory.create();
            manager = LoginManager.getInstance();
            manager.registerCallback(mCallbackManager, mCallback);

            Toast.makeText(this, "Uploading to Facebook", Toast.LENGTH_LONG).show();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Log.d("AccessToken", "AccessToken " + AccessToken.getCurrentAccessToken());
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook");
            Bundle extras = getIntent().getExtras();
            arr = extras.getIntArray("FilesToUpload");
            if (f.exists()) {
                File[] files = f.listFiles();
                for (File inFile : files) {
                    int i = 0;
                    File k[] = files[i].listFiles();
                    for (i = 0; i < k.length + 1; i++) {
                        int b = arr[i];
                        Uri thumbnailUri = Uri.parse(k[b + 1].toString());//replace i with arr[i];
                        Uri fullImageUri = Uri.parse(k[b + 1].toString().replace("thumbnails/", ""));//Putting Dummy//String.valueOf(filelist[i]));
                        File inFile1 = new File(String.valueOf(fullImageUri));
                        if (helper.getPhotoInfo(String.valueOf(fullImageUri)) == null) {

                            //LoginClient.Request request= GraphRequest.newPostRequest(AccessToken.getCurrentAccessToken(),)

                            Bitmap image = BitmapFactory.decodeFile(String.valueOf(inFile1), options);
                            SharePhoto photo = new SharePhoto.Builder().setBitmap(image).setCaption("Columbus").build();
                            SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                            ShareApi.share(content, null);
                            Photo p = new Photo();
                            p.setImage_id(String.valueOf(fullImageUri));
                            p.setValue(true);
                            helper.insertPhotoInfo(p);
                        }
                    }
                }
            }

            Toast.makeText(this, "Upload Done", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomeActivity.class));

        }catch(Exception e){

        }
    }
 }
