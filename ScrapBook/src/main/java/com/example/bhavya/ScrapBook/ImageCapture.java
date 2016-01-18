package com.example.bhavya.ScrapBook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Click Snap Shot , this class allows the user to take pictures and save it in the corresponding folder named by the city name
public class ImageCapture extends Activity implements NotesFragment.OnFragmentInteractionListener, ImageViewFragment.OnFragmentInteractionListener, SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback, BaseFragment.OnFragmentInteractionListener, View.OnClickListener {

    Camera mCamera;
    SurfaceView mPreview;
    final String TAG = "CAMERA_ACTIVITY";
    ImageCapture img;
    List<Address> addresses=null;
    Button b;
    public static final int SELECT_PHOTO_ACTION = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        try {

            mPreview = (SurfaceView) findViewById(R.id.preview);
            mPreview.getHolder().addCallback(this);
            mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            Toast.makeText(this, "Loading Location", Toast.LENGTH_SHORT);
            mCamera = Camera.open(getCameraId());

            img = this;
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            Camera.Size size = sizes.get(0);
            for (int i = 0; i < sizes.size(); i++) {
                if (sizes.get(i).width > size.width)
                    size = sizes.get(i);
            }
            params.setPictureSize(size.width, size.height);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            params.setExposureCompensation(0);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setJpegQuality(100);
            params.setRotation(90);
//        params.setPreviewSize(size.width, size.height);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(getCameraId(), cameraInfo);
            setCameraDisplayOrientation(this, getCameraId(), mCamera);

            mCamera.setParameters(params);


            final SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor edit = shre.edit();


            double[] MyLoc = new double[2];

            final double[] gps = new double[2];

            Location l = null;

            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            final List<String> providers = lm.getProviders(true);
            final Geocoder gcd = new Geocoder(this, Locale.getDefault());

            /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                for (int i = providers.size() - 1; i >= 0; i--) {


                    l = lm.getLastKnownLocation(providers.get(i));


                    if (l != null) break;
                }
            } catch (Exception e) {

            }

            if (l != null) {

                gps[0] = l.getLatitude();
                gps[1] = l.getLongitude();

                try {
                    boolean b = hasActiveInternetConnection(this);

                    if (b == false) {

                        String city=shre.getString("location",null);
//                        String gpsValues=shre.getString(city, null);

                        if(city!=null) {
                            Toast.makeText(this, "Best if internet is available-" + city, Toast.LENGTH_SHORT).show();

                            File direct = new File(Environment.getExternalStorageDirectory() + city);

                            if (!direct.exists()) {
                                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + city);
                                wallpaperDirectory.mkdirs();
                            }

                            File path = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + city + "/thumbnails");

                            if (!path.exists()) {
                                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + city + "/thumbnails");
                                wallpaperDirectory.mkdirs();
                            }
                        }

                        else{
                            Toast.makeText(this,"Please Use GPS/Internet Service",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        addresses = gcd.getFromLocation(gps[0], gps[1], 1);
                        String s = addresses.get(0).getLocality();
                        edit.putString("location",s);
                        edit.putString(s, String.valueOf(gps[0] + "," + gps[1]));
                        edit.putString("last known", s);
                        edit.commit();

                        Toast toast = Toast.makeText(this, addresses.get(0).getLocality(), Toast.LENGTH_SHORT);
                        toast.show();

                        File direct = new File(Environment.getExternalStorageDirectory() + addresses.get(0).getLocality());

                        if (!direct.exists()) {
                            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + addresses.get(0).getLocality());
                            wallpaperDirectory.mkdirs();
                        }

                        File path = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + addresses.get(0).getLocality() + "/thumbnails");

                        if (!path.exists()) {
                            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + addresses.get(0).getLocality() + "/thumbnails");
                            wallpaperDirectory.mkdirs();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(Exception e){

                }
            }
            else if(shre.getString("last known",null)!=null){
                String s= shre.getString("last known",null);
                String location[]=shre.getString(s,null).split(",");

                Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
                toast.show();

                File direct = new File(Environment.getExternalStorageDirectory() + s + "-no providers");

                if (!direct.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + s);
                    wallpaperDirectory.mkdirs();
                }

                File path = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + s + "/thumbnails");

                if (!path.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + s + "/thumbnails");
                    wallpaperDirectory.mkdirs();
                }
            }

            else {
                Toast.makeText(this, "Location Service Not Available", Toast.LENGTH_SHORT).show();
                finish();
            }

            Log.d(TAG, "onCreate()");
        }catch(Exception e){

        }
        b=(Button) findViewById(R.id.snap_click);
        b.setOnClickListener(this);
    }

    public boolean hasActiveInternetConnection(Context context) {
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
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onClick(View v) {
        final View v1=v;
        try {
            switch (v.getId()) {
                case R.id.snap_click:

                    final ImageCapture this1=this;
                    Runnable t =new Runnable() {
                        public void run() {
                            this1.onSnapClick(v1);
                        }
                    };
                    new Thread(t).start();
                    break;
            }
        }catch(Exception e){

        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        try {
            android.hardware.Camera.CameraInfo info =
                    new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(cameraId, info);
            int rotation = activity.getWindowManager().getDefaultDisplay()
                    .getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(result);
        }catch(Exception e){

        }
    }


    private int getCameraId() {
        int cameraId = -1;

        try {

            // Search for the front facing camera
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    // Log.d(DEBUG_TAG, "Camera found");
                    cameraId = i;
                    break;
                }
            }
        }catch(Exception e){

        }
        return cameraId;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
        mPreview.getHolder().removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;

        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if(mCamera!=null)
                mCamera.release();
        }catch(Exception e){

        }
        Log.d(TAG, "Destroy");
    }





    @Override
    public void onResume() {
        try {
            super.onResume();
            mPreview = (SurfaceView) findViewById(R.id.preview);
            mPreview.getHolder().addCallback(this);
            mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            Toast.makeText(this, "Loading Location", Toast.LENGTH_SHORT);
            mCamera = Camera.open(getCameraId());

            img = this;
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            Camera.Size size = sizes.get(0);
            for (int i = 0; i < sizes.size(); i++) {
                if (sizes.get(i).width > size.width)
                    size = sizes.get(i);
            }
            params.setPictureSize(size.width, size.height);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            params.setExposureCompensation(0);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setJpegQuality(100);
            params.setRotation(90);
//        params.setPreviewSize(size.width, size.height);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(getCameraId(), cameraInfo);
            setCameraDisplayOrientation(this, getCameraId(), mCamera);

            mCamera.setParameters(params);
            mCamera.startPreview();
        }catch(Exception e){

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");
    }


    public void onCancelClick(View v) {
        finish();
    }

    public void onSnapClick(final View v) {
        ImageCapture this1 = this;
        try {
            mCamera.takePicture(this1, null, null, this1);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onShutter() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override





    public void onPictureTaken(byte[] data, Camera camera) {
        //Here, we chose internal storage

        final Camera c=camera;

        c.startPreview();
        final ImageCapture this1=this;
        final byte[] data1=data;
        final int[] count = {0};
        Runnable t= new Runnable(){

              public void run(){
                  try{

                  final int THUMBSIZE = 64;
                  Bitmap original = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                  Bitmap resized = Bitmap.createScaledBitmap(original, THUMBSIZE, THUMBSIZE, true);
                  ByteArrayOutputStream blob = new ByteArrayOutputStream();
                  resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

                  String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                      String city;
                  //String fileName=(currentDateTimeString+System.currentTimeMillis()).replaceAll("\\s+","");
                  String fileName=java.util.UUID.randomUUID().toString();
                      if(addresses==null){
                          final SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                          final SharedPreferences.Editor edit = shre.edit();

                          city= shre.getString("location",null);
                      }
                      else{
                          city= addresses.get(0).getLocality();

                      }
                      if(city==null){
                          Toast.makeText(getBaseContext(),"Set the GPS",Toast.LENGTH_SHORT).show();
                      }
                      else {
                          File file = new File(new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + city + "/thumbnails"), fileName + ".PNG");
                          FileOutputStream out = new FileOutputStream(file);
                          out.write(blob.toByteArray());

                          out.flush();
                          out.close();

                          file = new File(new File(Environment.getExternalStorageDirectory().toString() + "/ScrapBook/" + city), fileName + ".PNG");
                          out = new FileOutputStream(file);
                          out.write(data1);

                          out.flush();
                          out.close();
                          count[0]++;
                          Log.d("bhavya", "inside thread");
                      }
                  } catch (FileNotFoundException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  catch(Exception e){

                  }

              }
            };

       new Thread(t).start();

        Log.d("bhavya", "1");


    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            Camera.Parameters params = mCamera.getParameters();

            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            Camera.Size selected = sizes.get(0);
            params.setPreviewSize(selected.width, selected.height);
            mCamera.setParameters(params);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mCamera.setDisplayOrientation(90);
            else
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        }catch(Exception e){

        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try
        {
            if (mCamera != null)
            {
                mCamera.setPreviewDisplay(holder);
            }
        }
        catch (IOException exception)
        {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }catch(Exception e ){

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try{
            if (mCamera != null)
            {
                mCamera.stopPreview();
            }
        }catch(Exception e){

        }

        Log.i("PREVIEW", "surfaceDestroyed");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentInteraction(String id) {
    }

    @Override
    public void onFragmentInteraction(int actionId) {
    }
}