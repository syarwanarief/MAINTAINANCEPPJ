package medio.maintainanceppj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;


public class Alert extends Activity {
MediaPlayer mp;
int reso=R.raw.chec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mp=MediaPlayer.create(getApplicationContext(),reso);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String kegiatan =getIntent().getExtras().getString(getString(R.string.title_msg) );
        String ruang = getIntent().getExtras().getString(getString(R.string.ruang_msg) );
        builder.setMessage("Kegiatan : "+kegiatan+" \n"+"Ruangan  : "+ruang+" ").setCancelable(
                false).setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Alert.this.finish();
                    }
                });
        builder.setNegativeButton(getString(R.string.cek),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Alert.this, MainActivity.class);
                        startActivity(intent);
                        Alert.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override

    public void onDestroy() {

        super.onDestroy();

        mp.release();

    }

}
