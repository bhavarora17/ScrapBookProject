package com.example.bhavya.ScrapBook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by chkee on 11/19/2015.
 */
//While uploading,to set the grid view of pictures in the gallery
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    BitmapDrawable mDrawable[];

    public ImageAdapter(Context c) {

        mContext = c;

        Resources res=mContext.getResources();


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

        int totalThumbnails=0;

        for(i=0; i<directories.length; i++){
            totalThumbnails += directories[i].listFiles().length;
        }

        mDrawable=new BitmapDrawable[totalThumbnails];

        File f1;
        for(int j=0;j<directories.length;j++) {
            File filesInternal[]=directories[j].listFiles();

            for (i = 0; i < filesInternal.length; i++) {

                Uri thumbnailUri = Uri.parse(filesInternal[i].toString());
                Uri fullImageUri = Uri.parse(filesInternal[i].toString().replace("thumbnails/",""));//Putting Dummy//String.valueOf(filelist[i]));

                mDrawable[filesInternal.length * j + i]= new BitmapDrawable(res,thumbnailUri.toString());
            }
        }

    }

    public int getCount() {
        return mDrawable.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mDrawable[position].getBitmap());
        return imageView;
    }
}