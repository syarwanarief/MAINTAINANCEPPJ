package medio.maintainanceppj.menuActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import medio.maintainanceppj.Database.DatabaseHandler;
import medio.maintainanceppj.R;
import medio.maintainanceppj.getterSetter.JadwalKeg;

public class JadwalKegiatan extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHandler databaseHandler;

    SimpleCursorAdapter adapter;
    int hari = 0;
    int bln = 0;
    int thn = 0;
    ListView listView;
    TextView textView;
    Calendar calendar;
    String dateString;
    TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kegiatan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cek Jadwal");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
        dateString = dateformatter.format(new Date(cal.getTimeInMillis()));
        JadwalKeg jadwalKeg = new JadwalKeg();
        jadwalKeg.setTempTgl(dateString);

        listView = (ListView) findViewById(R.id.listKegiatan);
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();

        String [] from = {databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        int[] to = {R.id.varKeg, R.id.varRuangan, R.id.varTgl, R.id.varJam};

        final Cursor c = db.rawQuery("select * from "+databaseHandler.TABLE_NAME
                + " where " +databaseHandler.COLUMN_TANGGAL+"=?",new String[] {jadwalKeg.getTempTgl()});
        adapter = new SimpleCursorAdapter(this, R.layout.list_jadwal, c, from, to, 0);

        listView.setAdapter(adapter);

        CalendarView calendarView = findViewById(R.id.cView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                dateString = dateformatter.format(new Date(calendar.getTimeInMillis()));
                a=(TextView) findViewById(R.id.tampDate);
                a.setText(dateString);
                JadwalKeg jadwalKeg = new JadwalKeg();
                jadwalKeg.setTempTgl(dateString);

                listView = (ListView) findViewById(R.id.listKegiatan);
                databaseHandler = new DatabaseHandler(JadwalKegiatan.this);
                db = databaseHandler.getWritableDatabase();

                String [] from = {databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
                int[] to = {R.id.varKeg, R.id.varRuangan, R.id.varTgl, R.id.varJam};

                final Cursor c = db.rawQuery("select * from "+databaseHandler.TABLE_NAME
                        + " where " +databaseHandler.COLUMN_TANGGAL+"=?",new String[] {jadwalKeg.getTempTgl()});
                adapter = new SimpleCursorAdapter(JadwalKegiatan.this, R.layout.list_jadwal, c, from, to, 0);

                listView.setAdapter(adapter);

                if (adapter.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Data Kosong Untuk Tanggal = "+jadwalKeg.getTempTgl(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
