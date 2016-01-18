package com.example.bhavya.ScrapBook;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * Created by chkee on 11/23/2015.
 */
//Testing the Login Screen
public class LoginActivityTest extends ActivityInstrumentationTestCase2<Login>{

    private Login mLoginTestActivity;
    private EditText mFirstTestText;
    private EditText mSecondTestText;

    public LoginActivityTest() {
        super(Login.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLoginTestActivity = getActivity();
        mFirstTestText =
                (EditText) mLoginTestActivity
                        .findViewById(R.id.etUserName);
        mSecondTestText=(EditText)mLoginTestActivity.findViewById(R.id.etPassword);
        test_Login_text();

    }
    public void test_Login_text() {
        assertNotNull(mFirstTestText);
        assertNotNull(mSecondTestText);
    }

}
