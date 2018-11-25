package medio.maintainanceppj;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.VoiceInteractor;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MenuEdit extends AppCompatActivity
            implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //datetime
    EditText tampungdate;
    EditText tampungTime;
    Button date;
    Button time;
    int day,month,year,hour,minute;
    int dayFinal, mounthFinal, yearFinal, hourFinal, minuteFinal;

    private DatabaseReference mFirebase;


    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //database variable
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;

    //list id
    private FloatingActionButton btnAdd;
    private EditText isiText;
    private Spinner beritahu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ubah Jadwal");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final long id = getIntent().getExtras().getLong(getString(R.string.row_id_log));
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();
        isiText = (EditText) findViewById(R.id.text_isi);
        final Spinner ruang = (Spinner) findViewById(R.id.dRuagan);
        String tRuangan = ruang.getSelectedItem().toString();
        tampungdate = (EditText) findViewById(R.id.tampung_tanggal);
        tampungTime = (EditText) findViewById(R.id.tampung_jam);

        Cursor cursor = db.rawQuery("select * from " + databaseHandler.TABLE_NAME + " where " + databaseHandler.COLUMN_ID + "=" + id, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                isiText.setText(cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_KEGIATAN)));
                tampungdate.setText(cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_TANGGAL)));
                tampungTime.setText(cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_JAM)));
                if (cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_RUANGAN)).equals(ruang.getItemAtPosition(0))){
                    ruang.setSelection(0);
                }
                else if (cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_RUANGAN)).equals(ruang.getItemAtPosition(1))){
                    ruang.setSelection(1);
                }
                else if (cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_RUANGAN)).equals(ruang.getItemAtPosition(2))){
                    ruang.setSelection(2);
                }
                else if (cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_RUANGAN)).equals(ruang.getItemAtPosition(3))){
                    ruang.setSelection(3);
                }
                else if (cursor.getString(cursor.getColumnIndex(databaseHandler.COLUMN_RUANGAN)).equals(ruang.getItemAtPosition(4))){
                    ruang.setSelection(4);
                }
            }
            cursor.close();
        }

        tampungdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MenuEdit.this, (DatePickerDialog.OnDateSetListener) MenuEdit.this,
                        year,month,day);
                datePickerDialog.show();

                tampungdate.setEnabled(false);
                return true;
            }
        });
        tampungTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MenuEdit.this, (TimePickerDialog.OnTimeSetListener) MenuEdit.this,
                        hour, minute, android.text.format.DateFormat.is24HourFormat(MenuEdit.this));
                timePickerDialog.show();

                tampungTime.setEnabled(false);
                return true;
            }
        });
    }

    //lanjutan datetime
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        mounthFinal = i1 + 1;
        dayFinal = i2;

        tampungdate.setText(dayFinal+"/"+mounthFinal+"/"+yearFinal);
    }

    public void onTimeSet(TimePicker view, int i, int i2) {
        hourFinal = i;
        minuteFinal = i2;

        tampungTime.setText(hourFinal+":"+minuteFinal);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        //final Long id = menuInfo.id;
        final Spinner ruang = (Spinner) findViewById(R.id.dRuagan);

        switch (item.getItemId()) {
            case R.id.action_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuEdit.this);
                builder
                        .setTitle(getString(R.string.edit_title))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final long id = getIntent().getExtras().getLong(getString(R.string.row_id_long));
                                String keg = isiText.getText().toString();
                                String ruangan = ruang.getSelectedItem().toString();
                                String tanggal = tampungdate.getText().toString();
                                String jam = tampungTime.getText().toString();

                                ContentValues cv = new ContentValues();
                                cv.put(databaseHandler.COLUMN_KEGIATAN, keg);
                                cv.put(databaseHandler.COLUMN_RUANGAN, ruangan);
                                cv.put(databaseHandler.COLUMN_TANGGAL, tanggal);
                                cv.put(databaseHandler.COLUMN_JAM, jam);
                                db.update(databaseHandler.TABLE_NAME, cv, databaseHandler.COLUMN_ID + "=" + id, null);

                                Intent openMainScreen = new Intent(MenuEdit.this, MainActivity.class);
                                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(openMainScreen);

                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)//Do nothing on no
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
