package com.example.bhavya.ScrapBook;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by chkee on 11/23/2015.
 */
//Testing the Register Class Functionality
public class RegisterTest extends ActivityInstrumentationTestCase2<Register> {

    private Register mRegisterTestActivity;
    private EditText mFirstTestText;
    private EditText mSecondTestText;
    private EditText mThirdTestText;

    public RegisterTest() {
        super(Register.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRegisterTestActivity = getActivity();
        mFirstTestText =
                (EditText) mRegisterTestActivity
                        .findViewById(R.id.etName);
        mSecondTestText=(EditText)mRegisterTestActivity.findViewById(R.id.etUserName);
        mThirdTestText = (EditText)mRegisterTestActivity.findViewById(R.id.etPassword);
        test_text();
        test_Register_Button();

    }
    public void test_text() {
        assertNotNull(mFirstTestText);
        assertNotNull(mSecondTestText);
        assertNotNull(mThirdTestText);
    }
    public void test_Register_Button(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Login.class.getName(), null, false);

        // open current activity.
        Register myActivity = getActivity();
        final Button button = (Button) myActivity.findViewById(R.id.bRegister);
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