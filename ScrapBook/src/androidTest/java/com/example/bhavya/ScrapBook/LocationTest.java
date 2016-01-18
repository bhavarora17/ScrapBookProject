package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by chkee on 11/30/2015.
 */
//Testing if the current location is "columbus"
public class LocationTest extends ActivityInstrumentationTestCase2<ImageCapture> {
    public LocationTest() {
        super(ImageCapture.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testCurrentLocation();
    }

    public void testCurrentLocation() throws IOException {
        ImageCapture myActivity = getActivity();
        final double[] gps = new double[2];

        Location l = null;
        List<Address> addresses = null;

        final LocationManager lm = (LocationManager) myActivity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        final List<String> providers = lm.getProviders(true);
        final Geocoder gcd = new Geocoder(myActivity.getApplicationContext(), Locale.getDefault());

            /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(myActivity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(myActivity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            for (int i = providers.size() - 1; i >= 0; i--) {


                l = lm.getLastKnownLocation(providers.get(i));


                if (l != null) break;
            }
        } catch (Exception e) {

        }

        String s= null;

        if (l != null) {

            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
            addresses = gcd.getFromLocation(gps[0], gps[1], 1);
             s = addresses.get(0).getLocality();
        }
        if(s!=null)
        assertEquals(s,"NewYork");
        else assertEquals(s,null);
    }
}

