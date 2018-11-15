package medio.maintainanceppj;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class View_Note extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHandler dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DatabaseHandler(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + dbHelper.TABLE_NAME + " where " + dbHelper.COLUMN_ID + "=" + id, null);
        TextView title = (TextView) findViewById(R.id.vKeg);
        TextView ruangan = (TextView) findViewById(R.id.vRuangan);
        TextView tgl = (TextView) findViewById(R.id.vTgl);
        TextView jm = (TextView) findViewById(R.id.vJam);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_KEGIATAN)));
                ruangan.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_RUANGAN)));
                tgl.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TANGGAL)));
                jm.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_JAM)));

            }
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong(getString(R.string.rowID));

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
                return true;
            case R.id.action_edit:
                return true;

            case R.id.action_discard:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
