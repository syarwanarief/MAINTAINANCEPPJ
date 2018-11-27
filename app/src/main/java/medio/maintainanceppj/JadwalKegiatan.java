package medio.maintainanceppj;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import medio.maintainanceppj.R;

public class JadwalKegiatan extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHandler databaseHandler;

    SimpleCursorAdapter adapter;
    int hari = 0;
    int bln = 0;
    int thn = 0;
    ListView listView;
    TextView textView;
    Cursor cursor;
    Calendar calendar;
    String kal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cek Jadwal");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.listKegiatan);
        textView = (TextView) findViewById(R.id.textini);
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();
        cursor = db.rawQuery("select * from " + databaseHandler.TABLE_NAME + " where "
                + databaseHandler.COLUMN_TANGGAL + " = " +kal, null);
        final String[] column = {databaseHandler.COLUMN_ID, databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        int[] to = {R.id.vKeg, R.id.vRuangan, R.id.vTgl, R.id.vJam};
        adapter = new SimpleCursorAdapter(this, R.layout.list_entry, cursor, column, to, 0);
        listView.setAdapter(adapter);

        CalendarView calendarView = findViewById(R.id.cView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month+1);
                calendar.set(Calendar.YEAR, year);
                kal = calendar.toString();
            }
        });
    }

}
