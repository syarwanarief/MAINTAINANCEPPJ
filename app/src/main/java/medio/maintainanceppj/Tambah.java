package medio.maintainanceppj;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Tambah extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {

    Button tambah;
    EditText editText;

    //datetime
    EditText tampungdate;
    EditText tampungTime;
    Button date;
    Button time;
    int day,month,year,hour,minute;
    int dayFinal, mounthFinal, yearFinal, hourFinal, minuteFinal;

    private DatabaseReference mFirebase;
    SQLiteDatabase db;

    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //database variable
    private DatabaseReference mDatabase;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    //list id
    private FloatingActionButton btnAdd;
    private EditText isiText;
    private  EditText ruangan;
    private Spinner beritahu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Kegiatan");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //action switch
        Switch s = (Switch) findViewById(R.id.switch1);
        if (s != null); {
            s.setOnCheckedChangeListener(this);
        }

        //date time
        tampungdate=(EditText) findViewById(R.id.tampung_tanggal);
        tampungTime = (EditText) findViewById(R.id.tampung_jam);
        tampungdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Tambah.this, Tambah.this,
                        year,month,day);
                datePickerDialog.show();

                tampungdate.setEnabled(false);
                return false;
            }
        });
        tampungTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Tambah.this, Tambah.this,
                        hour, minute, android.text.format.DateFormat.is24HourFormat(Tambah.this));
                timePickerDialog.show();

                tampungTime.setEnabled(false);
                return false;
            }
        });

    }

    //display layout Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tambah, menu);
        return true;
    }
    //save
    public boolean onOptionsItemSelected(MenuItem item){
        final DatabaseHandler databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnAdd = (FloatingActionButton) findViewById(R.id.add);
        isiText = (EditText) findViewById(R.id.text_isi);
        final Spinner ruang = (Spinner) findViewById(R.id.dRuagan);
        tampungdate = (EditText) findViewById(R.id.tampung_tanggal);
        tampungTime = (EditText) findViewById(R.id.tampung_jam);
        switch (item.getItemId()){
            case R.id.action_save:
                String keg = isiText.getText().toString();
                String tgl = tampungdate.getText().toString();
                String jam = tampungTime.getText().toString();
                String room = ruang.getSelectedItem().toString();

                if (keg.isEmpty() || tgl.isEmpty() || jam.isEmpty() || room == "Pilih Ruangan") {
                    Toast.makeText(getApplicationContext(), "Data Tidak Lengkap, Silahkan Isi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put(databaseHandler.COLUMN_KEGIATAN, keg);
                    cv.put(databaseHandler.COLUMN_RUANGAN, room);
                    cv.put(databaseHandler.COLUMN_TANGGAL, tgl);
                    cv.put(databaseHandler.COLUMN_JAM, jam);

                    db.insert(databaseHandler.TABLE_NAME, null, cv);

                    Intent openMainScreen = new Intent(Tambah.this,MainActivity.class);
                    openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(openMainScreen);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void back(View view) {
        Intent intent = new Intent(Tambah.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //lanjutan datetime
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        mounthFinal = i1 + 1;
        dayFinal = i2;

        tampungdate.setText(dayFinal+"/"+mounthFinal+"/"+yearFinal);
    }

    @Override
    public void onTimeSet(TimePicker view, int i, int i2) {
        hourFinal = i;
        minuteFinal = i2;

        tampungTime.setText(hourFinal+":"+minuteFinal);

    }

    public void aktifAlrm(View view) {

    }

    //action switch
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Spinner b = (Spinner) findViewById(R.id.id_beritahu);
        if(isChecked) {
            //do stuff when Switch is ON
            b.setVisibility(View.VISIBLE);
        } else {
            //do stuff when Switch if OFF
            b.setVisibility(View.INVISIBLE);
        }
    }
}
