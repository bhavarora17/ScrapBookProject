package com.example.bhavya.ScrapBook;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by chkee on 11/30/2015.
 */
//Testing the PhotoUploadHelper functions
public class PhotoUploadHelperTest extends ActivityInstrumentationTestCase2<SharingActivity>
{
    public PhotoUploadHelperTest() {
        super(SharingActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testPhotoDetailInsertion();
    }


    public void testPhotoDetailInsertion() {
        SharingActivity myActivity = getActivity();
        PhotoUploadHelper helper = new PhotoUploadHelper(myActivity.getApplicationContext());
        boolean success;
        Photo p = new Photo();
        p.setImage_id("IMAGE_2291");
        p.setValue(true);
        success = helper.insertPhotoInfo(p);
        assertTrue(success);
        testPhotoUploadDetail();
    }

    public void testPhotoUploadDetail()
    {
        SharingActivity myActivity = getActivity();
        PhotoUploadHelper helper = new PhotoUploadHelper(myActivity.getApplicationContext());
        String value =helper.getPhotoInfo("IMAGE_2291");
        assertEquals(value ,"1");
    }

}
