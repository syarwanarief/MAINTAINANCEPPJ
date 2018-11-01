package medio.maintainanceppj;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import medio.maintainanceppj.getterSetter.JadwalKegiatan;

import static android.view.DragEvent.ACTION_DRAG_ENDED;

public class Tambah extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

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

    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //database variable
    private DatabaseReference mDatabase;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("MTPPJ");

    //list id
    private Button btnAdd;
    private EditText isiText;
    private  EditText ruangan;
    private Spinner beritahu;
    private Switch aSwitch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        //save
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnAdd = (Button) findViewById(R.id.add);
        isiText = (EditText) findViewById(R.id.text_isi);
        ruangan = (EditText) findViewById(R.id.id_ruangan);
        tampungdate = (EditText) findViewById(R.id.tampung_tanggal);
        tampungTime = (EditText) findViewById(R.id.tampung_jam);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String keg = isiText.getText().toString();
                String tgl = tampungdate.getText().toString();
                String jam = tampungTime.getText().toString();
                String room = ruangan.getText().toString();

                if (keg.isEmpty() && tgl.isEmpty() && jam.isEmpty() && room.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Data Kosong, Silahkan Isi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference mRef = ref.push();

                    DatabaseReference keyKegiatan = mRef.child("Kegiatan");
                    DatabaseReference keyTgl = mRef.child("Tanggal");
                    DatabaseReference keyJam = mRef.child("Jam");
                    DatabaseReference keyRuangan = mRef.child("Ruangan");

                    keyKegiatan.setValue(keg);
                    keyTgl.setValue(tgl);
                    keyJam.setValue(jam);
                    keyRuangan.setValue(room);

                    Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();

                    isiText.setText("");
                    Intent intent = new Intent(Tambah.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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

        // Menginisiasi  NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Mengatur Navigasi View Item yang akan dipanggil untuk menangani item klik menu navigasi
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    case R.id.beranda:
                        Intent intent = new Intent(Tambah.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.jadwal:
                        Intent intent2 = new Intent(Tambah.this, JadwalKegiatan.class);
                        startActivity(intent2);
                        return true;
                    case R.id.bantuan:
                        Toast.makeText(getApplicationContext(),"Bantuan telah dipilih",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.tentang:
                        Toast.makeText(getApplicationContext(),"Bantuan telah dipilih",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });
        // Menginisasi drawer Layout dan ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                //  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
                super.onDrawerOpened(drawerView);
            }
        };
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
        tampungdate.setText(" "+yearFinal +"/"
                +mounthFinal +"/"
                +dayFinal);
    }

    @Override
    public void onTimeSet(TimePicker view, int i, int i2) {
        hourFinal = i;
        minuteFinal = i2;

        tampungTime.setText(" "+hourFinal +":"+minuteFinal);

    }

    public void aktifAlrm(View view) {
        beritahu = (Spinner) findViewById(R.id.id_beritahu);
        aSwitch = (Switch) findViewById(R.id.switch1);

        beritahu.setVisibility(View.VISIBLE);
/*
            if (ACTION_DRAG_ENDED){
                beritahu.setVisibility(View.INVISIBLE);
                return;

        }*/

    }
}
