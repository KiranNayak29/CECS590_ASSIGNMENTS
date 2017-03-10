package android.csulb.edu.memories;

import android.content.ContentValues;
import android.csulb.edu.memories.dbModel.MemoEntry;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kiran on 2/27/2017.
 */

public class dbHelper extends AppCompatActivity {

    private dbInitiate mDbHelper;

    public dbHelper(dbInitiate mdbhelp) {
        mDbHelper = mdbhelp;

    }

    public boolean insert(String captions, String places, String paths) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MemoEntry.Caption, captions);
        values.put(MemoEntry.Place, places);
        values.put(MemoEntry.Image_Path, paths);


        // Insert a new row in the database, returning the ID of that new row.
        long newRowId = db.insert(MemoEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
   db.close();         // If the row ID is -1, then there was an error with insertion.
            return false;
        } else {
            db.close();// Otherwise, the insertion was successful and we can display a toast with the row ID.
          return true;
        }

    }

  public boolean delete(String fieldValue)
  {
      SQLiteDatabase db = mDbHelper.getWritableDatabase();

      try {
          String selection = MemoEntry.Caption + " LIKE ?";

          String[] selectionArgs = { fieldValue };

          db.delete(MemoEntry.TABLE_NAME, selection, selectionArgs);


          db.close();
          return true;
      }
      catch(Exception e) {
          db.close();
          return false;
      }


  }





}
