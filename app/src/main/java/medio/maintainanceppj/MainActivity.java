package medio.maintainanceppj;

import android.app.Notification;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import medio.maintainanceppj.getterSetter.JadwalKegiatan;
import medio.maintainanceppj.getterSetter.detail;
import medio.maintainanceppj.getterSetter.listData;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static medio.maintainanceppj.App.notifID;

public class MainActivity extends AppCompatActivity {

    TextView text1;

    int i = 0;
    boolean exit = false;

    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ListView list;
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Jadwal Kegiatan");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        list = (ListView)findViewById(R.id.commentlist);

        notificationManagerCompat = NotificationManagerCompat.from(this);


        //display db
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();

        String [] from = {databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        final String [] column = {databaseHandler.COLUMN_ID, databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        int[] to = {R.id.vKeg, R.id.vRuangan, R.id.vTgl, R.id.vJam};

        final Cursor cursor = db.query(databaseHandler.TABLE_NAME, column, null, null, null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_entry, cursor, from, to, 0);

        list.setAdapter(adapter);
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){
                Intent intent = new Intent(MainActivity.this, View_Note.class);
                intent.putExtra(getString(R.string.rodId), id);
                startActivity(intent);
            }

        });*/

        if (cursor == null){
            //displayfragm
            EmptyActivity fragment = new EmptyActivity();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else if (cursor != null){
            EmptyActivity fragment1 = new EmptyActivity();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.remove(fragment1);
        }


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
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.jadwal:
                        Intent intent2 = new Intent(MainActivity.this, JadwalKegiatan.class);
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
        //Mensetting actionbarToggle untuk drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //memanggil synstate
        actionBarDrawerToggle.syncState();
    }

    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan Kembali Untuk Keluar Aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void tambah(View view) {
        Intent intent = new Intent(MainActivity.this, Tambah.class);
        startActivity(intent);
    }

    /*/popUp
    public void sendChannel(View v){
        String title = "Notifikasi";
        Notification notification = new NotificationCompat.Builder(this,notifID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

                notificationManagerCompat.notify(1,notification);
    }*/
}
