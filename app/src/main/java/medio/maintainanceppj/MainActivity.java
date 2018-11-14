package medio.maintainanceppj;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {

    TextView text1;

    int i = 0;
    boolean exit = false;

    //Mendefinisikan variabel
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    List<FireModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display db
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.getKey();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<FireModel>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
/*
                    if (dataSnapshot1.exists()){
                        //hidefragm
                        TextView tv = (TextView) findViewById(R.id.textKosong);
                        EmptyActivity fragment1 = new EmptyActivity();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.remove(fragment1);
                        tv.setText("");
                    }else{
                        //displayfragm
                        EmptyActivity fragment = new EmptyActivity();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                    }*/

                    FireModel value = dataSnapshot1.getValue(FireModel.class);
                    FireModel fire = new FireModel();
                    String vkeg = value.getKegiatan();
                    String vtgl = value.getTanggal();
                    String vjam = value.getJam();
                    String vruang = value.getRuangan();
                    fire.setKegiatan(vkeg);
                    fire.setTanggal(vtgl);
                    fire.setJam(vjam);
                    fire.setRuangan(vruang);
                    list.add(fire);

                    RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(list,MainActivity.this);
                    RecyclerView.LayoutManager recyce = new GridLayoutManager(MainActivity.this,1);
                    recyclerView.setLayoutManager(recyce);
                    recyclerView.setItemAnimator( new DefaultItemAnimator());
                    recyclerView.setAdapter(recyclerAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public void onBackPressed(){
        if (exit){
            super.onBackPressed();
        }else if(exit ){

        }else {
            exit = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            },400);
        }
    }

    public void tambah(View view) {
        Intent intent = new Intent(MainActivity.this, Tambah.class);
        startActivity(intent);
    }

    private class GetDataFromFirebase extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
