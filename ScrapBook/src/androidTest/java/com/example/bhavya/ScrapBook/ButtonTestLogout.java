package com.example.bhavya.ScrapBook;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by chkee on 11/29/2015.
 */
//Testing the Logout Button
public class ButtonTestLogout extends ActivityInstrumentationTestCase2<HomeActivity> {
    public ButtonTestLogout() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testLogout();
    }

    public void testLogout() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Login.class.getName(), null, false);

        // open current activity.
        HomeActivity myActivity = getActivity();
        final Button button = (Button) myActivity.findViewById(R.id.logout_button);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        Login nextActivity = (Login) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}
