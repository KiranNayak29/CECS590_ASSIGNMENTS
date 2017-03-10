package android.csulb.edu.memories;

import android.Manifest;
import android.content.Intent;
import android.csulb.edu.memories.dbModel.MemoEntry;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private dbInitiate mDbHelper;
    Log abc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

    mDbHelper = new dbInitiate(this);

        }

    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final Cursor ListCursor = db.rawQuery("SELECT  * FROM memories", null);

        ListView lvItems = (ListView) findViewById(R.id.list);

        final ListCursorAdapter todoAdapter = new ListCursorAdapter(this, ListCursor);

        lvItems.setAdapter(todoAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) todoAdapter.getItem(position);
                String caption = cursor.getString(cursor.getColumnIndexOrThrow(MemoEntry.Caption));
                String place = cursor.getString(cursor.getColumnIndexOrThrow(MemoEntry.Place));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MemoEntry.Image_Path));
                Log.d("heym in main", caption);
                Log.d("heym in main", place);
                Intent edit = new Intent(MainActivity.this, EditActivity.class);
                edit.putExtra("caption",caption);
                edit.putExtra("place",place);
                edit.putExtra("path",path);
                startActivity(edit);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uninstall, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uninstall:

                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivity(intent);

                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
