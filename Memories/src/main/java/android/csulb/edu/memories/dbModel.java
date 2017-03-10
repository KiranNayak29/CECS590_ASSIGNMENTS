package android.csulb.edu.memories;

import android.provider.BaseColumns;

/**
 * Created by Kiran on 2/27/2017.
 */

public final class dbModel {

    private dbModel() {}


    public static final class MemoEntry implements BaseColumns {

        public final static String TABLE_NAME = "memories";
        public final static String _ID = BaseColumns._ID;
        public final static String Caption = "caption";
        public final static String Place = "place";
        public final static String Image_Path = "imagepath";
    }
}
