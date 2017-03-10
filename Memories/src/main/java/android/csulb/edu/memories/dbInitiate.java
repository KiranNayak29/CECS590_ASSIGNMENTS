package android.csulb.edu.memories;

import android.content.Context;
import android.csulb.edu.memories.dbModel.MemoEntry;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kiran on 2/27/2017.
 */

public class dbInitiate extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 1;
    public dbInitiate(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE =  "CREATE TABLE " + MemoEntry.TABLE_NAME + " ("
                + MemoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MemoEntry.Caption + " TEXT NOT NULL, "
                + MemoEntry.Image_Path + " TEXT NOT NULL, "
                + MemoEntry.Place + " INTEGER NOT NULL);";


        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
