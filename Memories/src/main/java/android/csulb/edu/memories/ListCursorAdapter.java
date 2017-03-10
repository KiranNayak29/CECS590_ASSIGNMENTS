package android.csulb.edu.memories;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Kiran on 2/27/2017.
 */

public class ListCursorAdapter extends CursorAdapter {
    public ListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView summary = (TextView) view.findViewById(R.id.summary);
        // Extract properties from cursor
        String caption = cursor.getString(cursor.getColumnIndexOrThrow("caption"));
        String place = cursor.getString(cursor.getColumnIndexOrThrow("place"));

        // Populate fields with extracted properties
        name.setText(caption);
        summary.setText(String.valueOf(place));
    }
}
