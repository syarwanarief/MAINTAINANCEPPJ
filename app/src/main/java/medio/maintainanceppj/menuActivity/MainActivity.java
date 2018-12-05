package medio.maintainanceppj.menuActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import medio.maintainanceppj.Database.DatabaseHandler;
import medio.maintainanceppj.R;
import medio.maintainanceppj.viewHolder;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String PutTime = "timeKey";
    SharedPreferences sharedpreferences;

    TextView text1;

    int i = 0;
    boolean exit = false;
    LayoutInflater lInflater;

    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ListView list;
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;
    SimpleCursorAdapter adapter;
    String data_array[];

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Jadwal Kegiatan ICT PPJ");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        list = (ListView)findViewById(R.id.commentlist);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        //reset sharedpreff
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(PutTime);

        //display db
        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();

        String [] from = {databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN, databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        final String [] column = {databaseHandler.COLUMN_ID, databaseHandler.COLUMN_KEGIATAN, databaseHandler.COLUMN_RUANGAN,
                databaseHandler.COLUMN_TANGGAL, databaseHandler.COLUMN_JAM};
        int[] to = {R.id.vKeg, R.id.vRuangan, R.id.vTgl, R.id.vJam};

        final Cursor cursor = db.query(databaseHandler.TABLE_NAME, column, null, null, null, null, null);
        adapter = new SimpleCursorAdapter(this, R.layout.list_entry, cursor, from, to, 0){
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position,convertView,parent);

                if (position%6 == 0){
                    view.setBackgroundColor(Color.parseColor("#b3d1ff"));

                } else if (position%6 == 1){
                    view.setBackgroundColor(Color.parseColor("#ddddbb"));

                } else if (position%6 == 2){
                    view.setBackgroundColor(Color.parseColor("#99ff99"));

                } else if (position%6 == 3){
                    view.setBackgroundColor(Color.parseColor("#ddff99"));

                }else if (position%6 == 4){
                    view.setBackgroundColor(Color.parseColor("#ffff99"));

                }else if (position%6 == 5){
                    view.setBackgroundColor(Color.parseColor("#ff9999"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#f2f2f2f2"));
                }
                return view;
            }
        };

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){
                Intent intent = new Intent(MainActivity.this, MenuEdit.class);
                intent.putExtra(getString(R.string.rodId), id);
                Toast.makeText(getApplicationContext(),"Tahan Untuk Memilih Opsi ",Toast.LENGTH_SHORT).show();
            }

        });

        //longklik
        list.setLongClickable(true);
        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_long_click, menu);

            }
        });

        if (cursor.moveToFirst()){
            EmptyActivity fragment1 = new EmptyActivity();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.remove(fragment1);
        }else {
            //displayfragm
            EmptyActivity fragment = new EmptyActivity();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
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
                    case R.id.jadwal:
                        Intent intent2 = new Intent(MainActivity.this, JadwalKegiatan.class);
                        startActivity(intent2);
                        return true;
                    case R.id.bantuan:
                        Intent intent3 = new Intent(MainActivity.this, Bantuan.class);
                        startActivity(intent3);
                        return true;
                    case R.id.tentang:
                        Intent intent4 = new Intent(MainActivity.this, Tentang.class);
                        startActivity(intent4);
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
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final Long id = menuInfo.id;

        switch (item.getItemId()){
            case R.id.edit:
                        Intent intent = new Intent(MainActivity.this, MenuEdit.class);
                        intent.putExtra(getString(R.string.intent_row_id), id);
                        startActivity(intent);
                return true;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(databaseHandler.TABLE_NAME, databaseHandler.COLUMN_ID + "=" + id, null);
                                db.close();
                                recreate();

                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)//Do nothing on no
                        .show();
                return true;
                default:
                    return super.onContextItemSelected(item);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        final int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_entry, parent, false);
        }
        if (position % 2 == 0) {
            view.setBackgroundResource(randomAndroidColor);
        } else {
            view.setBackgroundResource(randomAndroidColor);
        }
        return view;
    }
}
