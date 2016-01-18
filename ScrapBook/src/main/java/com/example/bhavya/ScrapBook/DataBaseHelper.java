package com.example.bhavya.ScrapBook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chkee on 11/3/2015.
 */
// Data Base Helper class , to store the user name , name and password of the user
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME ="contacts.db";
    private static final String TABLE_NAME ="contacts";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_USERNAME="userName";
    private static final String COLUMN_PASSWORD="password";

    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table contacts (id integer primary key not null , " +
            "name text not null , userName text not null , password text not null);";
    public  DataBaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public boolean insertContact(Contact c)
    {
        db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "select * from contacts";
        Cursor cursor=db.rawQuery(query,null);
        int count=cursor.getCount();
        values.put(COLUMN_ID,count);
        values.put(COLUMN_NAME,c.getName());
        values.put(COLUMN_USERNAME,c.getUserName());
        values.put(COLUMN_PASSWORD,c.getPassword());
        db.insert(TABLE_NAME, null, values);
        return true;
    }
    public String searchPassword(String userName)
    {
        db= this.getWritableDatabase();
        String query = "select userName, password from "+TABLE_NAME;
        Cursor cursor;
        cursor = db.rawQuery(query,null);
        String a,b = null;
        if(cursor.moveToFirst())
        {
           do {
               a=cursor.getString(0);
               if(a.equals(userName))
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
