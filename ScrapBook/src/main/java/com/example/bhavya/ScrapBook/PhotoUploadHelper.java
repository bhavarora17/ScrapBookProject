package com.example.bhavya.ScrapBook;

/**
 * Created by chkee on 11/6/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PhotoUploadHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =2;
    private static final String DATABASE_NAME ="photos.db";
    private static final String TABLE_NAME ="photos";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_IMAGE_ID="image_id";
    private static final String COLUMN__BOOLEAN="value";


    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table photos (id integer primary key not null , " +
            "image_id text not null , value text not null);";
    public  PhotoUploadHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public boolean insertPhotoInfo(Photo c)
    {
        db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "select * from photos";
        Cursor cursor=db.rawQuery(query,null);
        int count=cursor.getCount();
        values.put(COLUMN_ID,count);
        values.put(COLUMN_IMAGE_ID,c.getImage_id());
        values.put(COLUMN__BOOLEAN,c.getValue());
        db.insert(TABLE_NAME, null, values);
        return true;
    }
    public String getPhotoInfo(String image_id)
    {
        db= this.getWritableDatabase();
        String query = "select image_id , value from "+TABLE_NAME;
        Cursor cursor;
        cursor = db.rawQuery(query,null);
        String a,b = null;
        if(cursor.moveToFirst())
        {
            do {
                a=cursor.getString(0);
                if(a.equals(image_id))
                {
                    b = cursor.getString(1);
                    break;
                }
            }while(cursor.moveToNext());
        }
        return b;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query ="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}