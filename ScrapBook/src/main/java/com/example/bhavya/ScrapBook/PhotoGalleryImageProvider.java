package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chkee on 11/4/2015.
 */
public class PhotoGalleryImageProvider {
    // Consts
    public static final int IMAGE_RESOLUTION = 15;
    // Buckets where we are fetching images from.
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);
    /**
     * Fetch both full sized images and thumbnails via a single query.
     * Returns all images not in the Camera Roll.
     * @param context
     * @return
     */
    public static List<PhotoItem> getAlbumThumbnails(Context context){
//
//        File dir = new File(CAMERA_IMAGE_BUCKET_NAME);
//        String[] filelist = dir.list();
//        String path = CAMERA_IMAGE_BUCKET_NAME;

        File f = new File(Environment.getExternalStorageDirectory().toString()+"/ScrapBook");
        File[] files = f.listFiles();
        File directories[]=new File[files.length];
        int i=0;
        for (File inFile : files) {
            File k[] = files[i].listFiles();
            for (File inFile1 : k) {
                if (inFile1.isDirectory()) {
                    directories[i] = inFile1;
                    i++;
                }
            }
        }

//        File dir = new File("/sdcard/ScrapBook");
        //File path=new File(Constants.SCREENSHOT_DIRECTORY);

//        if (!path.exists()) {
//            File wallpaperDirectory = new File(Constants.SCREENSHOT_DIRECTORY);
//            wallpaperDirectory.mkdirs();
//        }

//        File file;
//
//        File[] filelist = dir.listFiles();
//        //path.mkdirs();
        int totalThumbnails=0;
//        for(int j=0;j<directories.length;i++) {
//            File filesInternal[] = directories[j].listFiles();
//        }

        for(i=0; i<directories.length; i++){
            totalThumbnails += directories[i].listFiles().length;
        }

        ArrayList<PhotoItem> result = new ArrayList<PhotoItem>(totalThumbnails);
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        File f1;
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=shre.edit();
        for(int j=0;j<directories.length;j++) {
            File filesInternal[]=directories[j].listFiles();

            for (i = 0; i < filesInternal.length; i++) {

                //int i = 1;
//            ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(String.valueOf(filelist[i])), THUMBSIZE, THUMBSIZE);
//            ThumbImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//            file = new File(new File("/sdcard/Pictures"), i + ".jpg");
//            out = new FileOutputStream(file);
//            out.write(bytes.toByteArray());
//            out.flush();
//
//// remember close de FileOutput
//            out.close();
                //Uri thumbnailUri = Uri.parse(String.valueOf(filelist[i]));
                Uri thumbnailUri = Uri.parse(filesInternal[i].toString());
                //filesInternal[i].toString().replace("thumbnail/","");
                Uri fullImageUri = Uri.parse(filesInternal[i].toString().replace("thumbnails/",""));//Putting Dummy//String.valueOf(filelist[i]));

                edit.putString("imagepath"+String.valueOf(i),String.valueOf(fullImageUri));
                edit.commit();

// Create the list item.
                PhotoItem newItem = new PhotoItem(thumbnailUri, fullImageUri);
                result.add(newItem);
            }
        }

//
        // final String[] projection = {MediaStore.Images.Thumbnails.DATA,MediaStore.Images.Thumbnails.IMAGE_ID};
//
//        final String[] projection = {,
//        };
//
//        Uri myUri = Uri.parse(CAMERA_IMAGE_BUCKET_NAME);
////
////        Uri capturedImage = Uri.parse(
////                android.provider.MediaStore.Images.Media.insertImage(
////                        getContentResolver(),
////                        file.getAbsolutePath(), null, null));
//
//        Cursor thumbnailsCursor = context.getContentResolver().query( myUri,
//                projection, // Which columns to return
//                null, // Return all rows
//                null,
//                null);
//// Extract the proper column thumbnails
//        int thumbnailColumnIndex = thumbnailsCursor.getColumnIndex(MediaStore.Images.Media.DATA);
//        ArrayList<PhotoItem> result = new ArrayList<PhotoItem>(filelist.length);
//        if (thumbnailsCursor.moveToFirst()) {
//            do {
//// Generate a tiny thumbnail version.
//                int thumbnailImageID = thumbnailsCursor.getInt(thumbnailColumnIndex);
//                String thumbnailPath = thumbnailsCursor.getString(thumbnailImageID);
//                Uri thumbnailUri = Uri.parse(thumbnailPath);
//                Uri fullImageUri = uriToFullImage(thumbnailsCursor,context);
//// Create the list item.
//                PhotoItem newItem = new PhotoItem(thumbnailUri,fullImageUri);
//                result.add(newItem);
//            } while (thumbnailsCursor.moveToNext());
//        }
//        thumbnailsCursor.close();
        return result;
    }


    /**
     * Get the path to the full image for a given thumbnail.
     */
    private static Uri uriToFullImage(Cursor thumbnailsCursor, Context context){
        String imageId = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
// Request image related to this thumbnail
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor imagesCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);
        if (imagesCursor != null && imagesCursor.moveToFirst()) {
            int columnIndex = imagesCursor.getColumnIndex(filePathColumn[0]);
            String filePath = imagesCursor.getString(columnIndex);
            imagesCursor.close();
            return Uri.parse(filePath);
        } else {
            imagesCursor.close();
            return Uri.parse("");
        }
    }
    /**
     * Render a thumbnail photo and scale it down to a smaller size.
     * @param path
     * @return
     */
    private static Bitmap bitmapFromPath(String path){
        File imgFile = new File(path);
        Bitmap imageBitmap = null;
        if(imgFile.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = IMAGE_RESOLUTION;
            imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        }
        return imageBitmap;
    }
    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

}
