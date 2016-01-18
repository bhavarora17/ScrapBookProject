package com.example.bhavya.ScrapBook;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.ShareActionProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//This class helps in users to add noes to the pictures


public class AddNotes extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener, ImageViewFragment.OnFragmentInteractionListener, NotesFragment.OnFragmentInteractionListener{

    String imagePathFull;


    FragmentManager fm1 = null;
    BaseFragment targetFragment1 = null;
    private EditText noteContentText;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        try {
            fm1 = this.getFragmentManager();
            targetFragment1 = null;
            targetFragment1 = HorizontalPhotoGalleryFragment.newInstance(1);

            FragmentTransaction ft = fm1.beginTransaction();
            ft.replace(R.id.container1, targetFragment1)
                    .commit();
        }catch(Exception e){

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void inflatePicture(String imagePath) throws FileNotFoundException {
        try {
            imagePathFull = imagePath;
            //BitmapFactory.decodePath(imagePath);

//        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
//        byte[] b = byteArrayBitmapStream.toByteArray();
            FragmentManager fm = getFragmentManager();
            ImageViewFragment targetFragment = null;
            targetFragment = ImageViewFragment.newInstance(imagePath, "abc");


            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.ImageFrame, targetFragment)
                    .commit();

            fm = getFragmentManager();
            NotesFragment targetFragment2 = null;
            targetFragment2 = NotesFragment.newInstance(imagePath, "abc");


            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.replace(R.id.Notes, targetFragment2)
                    .commit();


            if (targetFragment.isHidden()) {
                ft.show(targetFragment);
                Log.d("hidden", "Show");
            }


            if (targetFragment1.isHidden()) {
                ft.show(targetFragment1);
                Log.d("hidden", "Show");
            } else {
                ft.hide(targetFragment1);
                Log.d("Shown", "Hide");
            }
        }catch(Exception e){

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

    try {
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_save) {

            noteContentText = (EditText) findViewById(R.id.note_content);
            String s1 = noteContentText.getText().toString();
            Draw2d d = new Draw2d(this, s1, imagePathFull);
            Draw2d d1 = new Draw2d(this);
            d1.s = d.s;
            d1.imagePathF = d.imagePathF;

            d.invalidate();
            d1.invalidate();
            d.postInvalidate();
            setContentView(d);

    //            setContentView(R.layout.activity_add_notes);
    //
    //            fm1 = getFragmentManager();
    //            targetFragment1 = null;
    //
    //            targetFragment1 = HorizontalPhotoGalleryFragment.newInstance(1);
    //
    //            FragmentTransaction ft = fm1.beginTransaction();
    //            ft.replace(R.id.container1, targetFragment1)
    //                    .commit();

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("ImageSaved");
            ad.setMessage("Continue");
            ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    setContentView(R.layout.activity_add_notes);

                    fm1 = getFragmentManager();
                    targetFragment1 = null;

                    targetFragment1 = HorizontalPhotoGalleryFragment.newInstance(1);

                    FragmentTransaction ft = fm1.beginTransaction();
                    ft.replace(R.id.container1, targetFragment1)
                            .commit();
                    // continue with delete
                }
            })
    //                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
    //                        public void onClick(DialogInterface dialog, int which) {
    //                            setContentView(R.layout.activity_add_notes);
    //
    //                            // do nothing
    //                        }
    //                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


        if (id == R.id.menu_item_share) {
            // Fetch and store ShareActionProvider
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            // Return true to display menu
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("image/jpeg");
            String s = "";
            boolean flag = false;

            //BitmapFactory.Options options = new BitmapFactory.Options();
            //Bitmap original1 = BitmapFactory.decodeFile(imagePathFull, options);

            //Bitmap icon = original1;

            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + imagePathFull));

            //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
            startActivity(Intent.createChooser(sharingIntent, "Share").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    //        mShareActionProvider.setShareIntent(sharingIntent);

            }
        }catch (Exception e){

        }
        return true;

    }


        public class Draw2d extends View {

        private Drawable mDrawable;
        //private Drawable mD2;
        String s;
        String imagePathF;
        int yOffsetFinal;


        public Draw2d(Context context, String s1, String imagePath) {

            super(context);
            try {
                imagePathF = imagePath;
                s = s1;
                setDrawingCacheEnabled(true);
                setWillNotDraw(false);
            }catch(Exception e){

            }
        }

        public Draw2d(Context context) {

            super(context);
            try {
                setWillNotDraw(false);
                setDrawingCacheEnabled(true);
            } catch (Exception e) {

            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
         try{
             BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width2 = size.x;
            float height2 = size.y;
            String gText= s;
            Rect bounds = new Rect();

            canvas.drawColor(Color.WHITE);

            Bitmap original1 = BitmapFactory.decodeFile(imagePathF, options);
            int width = original1.getWidth();
            int height = original1.getHeight();
            int bounding = dpToPx(Math.round(height));

            Paint paint = new Paint();
            paint.setColor(Color.BLACK); // Text Color
            float xScale = ((float) bounding) / width;
            float yScale = ((float) bounding) / height;
            float scale = (xScale <= yScale) ? xScale : yScale;
            //Paint alphaChannel = new Paint();
            //alphaChannel.setAlpha(100);
            paint.setAlpha(100);
            paint.setTextSize(20 * scale);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);

            paint.getTextBounds(gText, 0, gText.length(), bounds);
            Typeface font = Typeface.createFromAsset(getAssets(), "Chantelli_Antiqua.ttf");
            paint.setTypeface(font);

            int x = (width2)/2;
            //int y = Math.round(height1)+(Math.round(height1) - Math.round(height2) / 2);
            int y = Math.round(height);
            int yOffset=calcYoff(gText, 0, y, paint, canvas, Math.round(20 * scale), width);




//            Bitmap scaledBitmap = Bitmap.createBitmap(original1, 0, 0, width, height, matrix, true);


            try {

                int x1 = 0;
                int y1 = 0;
                int width1 = width2;
                int height1 = Math.round((height2-yOffset));// * 60 / 100.0f);

//                float scaleWidth = ((float) width) / width;
//                float scaleHeight = ((float) height1) / height;
//
//
//                Matrix matrix = new Matrix();
//                matrix.postScale(scaleWidth, scaleHeight);

                Resources res = this.getResources();


                mDrawable = new BitmapDrawable(res, original1);//scaledBitmap);
                mDrawable.setBounds(x1, y1, width2, height);//Math.round(height2-yOffset));

//                Bitmap b = Bitmap.createBitmap(original1,0,0, width, Math.round(height+yOffset));
//
//                canvas = new Canvas(b);
                canvas.drawColor(Color.TRANSPARENT);
                canvas.drawBitmap(original1, 0, 0, paint);
                mDrawable.draw(canvas);

                Rect rect = new Rect(0, (y), (int) Math.round((0.9)*canvas.getWidth()), height + yOffset);
                drawMultilineText(gText, 0, y + Math.round(20 * scale) * 2, paint, canvas, Math.round(20 * scale), rect);

                //canvas.drawBitmap(canvas., 100, 40, paint);




                //canvas.drawBitmap(original1, 0, 0, paint);
                //canvas.drawText(gText, 0, y, paint);
                //canvas.drawText("This is", 0, y+100, paint);
                //canvas.drawText("multi-line", 0, y+150, paint);
                //canvas.drawText("text", 0, y+200, paint);


                //canvas.drawCircle(50, 50, 30, paint);

                getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(imagePathF)));


                super.onDraw(canvas);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Error--------->", e.toString());
            }
         }catch(Exception e){

         }
        }


        public int calcYoff(String str, int x, int y, Paint paint, Canvas canvas, int fontSize, int width){

          try{
              int      lineHeight = 0;

            int      yoffset    = 0;
            String[] lines      = str.split(" ");

            lineHeight = (int) (calculateHeightFromFontSize(str, fontSize) * 1.2);
            // draw each line
            String line = "";


            for (int i = 0; i < lines.length; ++i) {

                if(calculateWidthFromFontSize(line + " " + lines[i], fontSize) <= width){
                    line = line + " " + lines[i];

                }else{
                    yoffset = yoffset + lineHeight;
                    line = lines[i];
                }
            }
            yOffsetFinal=yoffset+lineHeight+lineHeight;
          }catch(Exception e){

          }
            return yOffsetFinal;
        }
    }

    private int dpToPx(int dp) {
        float density=0.0f;
        try {
            density = getApplicationContext().getResources().getDisplayMetrics().density;

        } catch (Exception e) {

        }
        return Math.round((float) dp * density);

    }

//    private boolean isNoteFormOk() {
//        return !Strings.isNullOrBlank(noteTitleText.getText().toString()) && !Strings.isNullOrBlank(noteContentText.getText().toString());
//    }


    private void drawMultilineText(String str, int x, int y, Paint paint, Canvas canvas, int fontSize, Rect drawSpace) {
        int      lineHeight = 0;
        int      yoffset    = 0;
        String[] lines      = str.split(" ");
try {
    // set height of each line (height of text + 20%)
    lineHeight = (int) (calculateHeightFromFontSize(str, fontSize) * 1.2);
    // draw each line
    String line = "";
    for (int i = 0; i < lines.length; ++i) {

        if (calculateWidthFromFontSize(line + " " + lines[i], fontSize) <= drawSpace.width()) {
            line = line + " " + lines[i];

        } else {
            canvas.drawText(line, x, y + yoffset, paint);
            yoffset = yoffset + lineHeight;
            line = lines[i];
        }
    }
    canvas.drawText(line, x, y + yoffset, paint);
}catch(Exception e){

}

    }



    private int calculateWidthFromFontSize(String testString, int currentSize) {
        Rect bounds = new Rect();
        try {

            Paint paint = new Paint();
            paint.setTextSize(currentSize);
            paint.getTextBounds(testString, 0, testString.length(), bounds);

            return (int) Math.ceil(bounds.width());
        }catch(Exception e){

        }
        return (int) Math.ceil(bounds.width());
    }

    private int calculateHeightFromFontSize(String testString, int currentSize) {
        Rect bounds = new Rect();
        try {


            Paint paint = new Paint();
            paint.setTextSize(currentSize);
            paint.getTextBounds(testString, 0, testString.length(), bounds);

        } catch (Exception e) {

        }
        return (int) Math.ceil(bounds.height());

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(int actionId) {

    }
}
