package medio.maintainanceppj.menuActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import medio.maintainanceppj.Alarm.AlarmReceiver;
import medio.maintainanceppj.Database.DatabaseHandler;
import medio.maintainanceppj.R;

public class Tambah extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String PutTime = "timeKey";
    SharedPreferences sharedpreferences;

    Button tambah;
    EditText editText;

    //datetime
    EditText tampungdate;
    EditText tampungTime;
    Button date;
    Button time;
    int day,month,year,hour,minute;
    int dayFinal, mounthFinal, yearFinal, hourFinal, minuteFinal;
    Calendar c;
    DatePickerDialog datePickerDialog;

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

        //date time
        tampungdate=(EditText) findViewById(R.id.tampung_tanggal);
        tampungTime = (EditText) findViewById(R.id.tampung_jam);
        tampungdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Tambah.this, Tambah.this,
                        year,month,day);
                datePickerDialog.show();
                tampungdate.setEnabled(false);
                return true;
            }
        });
        tampungTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Tambah.this, Tambah.this,
                        hour, minute, android.text.format.DateFormat.is24HourFormat(Tambah.this));
                timePickerDialog.show();

                tampungTime.setEnabled(false);
                return true;
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

                if (keg.isEmpty() || tgl.isEmpty() || jam.isEmpty() || room.equals("Pilih Ruangan")) {
                    Toast.makeText(getApplicationContext(), "Data Tidak Lengkap, Silahkan Isi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put(databaseHandler.COLUMN_KEGIATAN, keg);
                    cv.put(databaseHandler.COLUMN_RUANGAN, room);
                    cv.put(databaseHandler.COLUMN_TANGGAL, tgl);
                    cv.put(databaseHandler.COLUMN_JAM, jam);

                    db.insert(databaseHandler.TABLE_NAME, null, cv);

                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    sharedpreferences.edit();
                    Long time = sharedpreferences.getLong(PutTime, 0);

                    Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(Tambah.this, AlarmReceiver.class);
                    intent.putExtra(getString(R.string.alert_title), keg);

                    //intent.putExtra(getString(R.string.nRuang), room);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Tambah.this, 0, intent, 0);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

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

        c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1+1);
        c.set(Calendar.DAY_OF_MONTH, i2);

        tampungdate.setText(dayFinal+"/"+mounthFinal+"/"+yearFinal);
    }

    @Override
    public void onTimeSet(TimePicker view, int i, int i2) {
        hourFinal = i;
        minuteFinal = i2;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, i);
        calendar.set(Calendar.MINUTE, i2);
        calendar.set(Calendar.SECOND,00);
        tampungTime.setText(hourFinal+":"+minuteFinal);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(PutTime, calendar.getTimeInMillis());


    }
}
