package android.csulb.edu.memories;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    EditText caption ;
    EditText place ;
    EditText path ;
    static String old_caption;
    dbInitiate mdbhelper = new dbInitiate(this);
    Log abcd;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath = "nothing";
    static Bundle a = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent edit = getIntent();
        String captiondata = edit.getStringExtra("caption");
        String placedata = edit.getStringExtra("place");
        String pathdata = edit.getStringExtra("path");
        old_caption = captiondata;
        caption = (EditText) findViewById(R.id.editCaption);
        place = (EditText) findViewById(R.id.editPlace);
        caption.setText(captiondata);
        place.setText(placedata);



        if(pathdata != null && !pathdata.isEmpty()) {

            mCurrentPhotoPath = pathdata;

            File imgFile = new  File(mCurrentPhotoPath);


            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                try {
                    ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                }
                catch (Exception e) {

                }



                ImageView myImage = (ImageView) findViewById(R.id.seeimage);

                myImage.setImageBitmap(myBitmap);

            }
        }
        else
        {

            mCurrentPhotoPath = a.getString("path");
            if(mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty()) {
                File imgFile = new File(mCurrentPhotoPath);


                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    try {
                        ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                    } catch (Exception e) {

                    }


                    ImageView myImage = (ImageView) findViewById(R.id.seeimage);

                    myImage.setImageBitmap(myBitmap);

                }
            }

        }
    }

    public void pic(View view)
    {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);



        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        File image = File.createTempFile(imageFileName,".jpg",path);


        mCurrentPhotoPath = image.getAbsolutePath();

        a.putString("path",mCurrentPhotoPath);

        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        File imgFile = new  File(mCurrentPhotoPath);


        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            try {
                ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }



            ImageView myImage = (ImageView) findViewById(R.id.seeimage);

            myImage.setImageBitmap(myBitmap);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        caption = (EditText) findViewById(R.id.editCaption);
         place = (EditText) findViewById(R.id.editPlace);


        String captions = caption.getText().toString().trim();
        String places = place.getText().toString().trim();

        dbHelper helper = new dbHelper(mdbhelper);
        boolean result;

        switch (item.getItemId()) {

            case R.id.action_save:

                if(old_caption != null && !old_caption.isEmpty()) {

                    helper.delete(old_caption);
                    Log.d("in else!","its is : "+old_caption);
                    old_caption=null;

                }
                else
                {
                    helper.delete(captions);
                    Log.d("in if!",captions);
                }

                if(captions.length()!=0 && places.length()!=0 && mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty())
                {
                    result = helper.insert(captions, places, mCurrentPhotoPath);

                    // Exit activity
                    if (result == true)
                        Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Error while saving!!", Toast.LENGTH_SHORT).show();
                    a.clear();
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Caption or Place or Picture cant be null", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_delete:

                helper.delete(captions);
                a.clear();
                finish();
                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
