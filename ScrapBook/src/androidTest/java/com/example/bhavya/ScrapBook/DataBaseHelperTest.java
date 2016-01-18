package com.example.bhavya.ScrapBook;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by chkee on 11/29/2015.
 */
//Testing the functions in Database helper
public class DataBaseHelperTest extends ActivityInstrumentationTestCase2<Register> {
    public DataBaseHelperTest() {
        super(Register.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDataBaseContactInsertion();
        testDataBaseSearch();
    }

    public void testDataBaseContactInsertion() {
        Register myActivity = getActivity();
        DataBaseHelper helper = new DataBaseHelper(myActivity.getApplicationContext());
        boolean success;
        Contact c = new Contact();
        c.setName("keerthi");
        c.setUserName("chkeerthi93");
        c.setPassword("password");
        success =helper.insertContact(c);
        assertTrue(success);
    }
     public void testDataBaseSearch()
     {
         Register myActivity = getActivity();
         DataBaseHelper helper = new DataBaseHelper(myActivity.getApplicationContext());
         String pass = helper.searchPassword("chkeerthi93");
         assertEquals(pass , "password");

     }
}
