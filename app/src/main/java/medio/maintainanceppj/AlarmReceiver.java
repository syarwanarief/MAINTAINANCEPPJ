package medio.maintainanceppj;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
    
        String Title = intent.getStringExtra(context.getString(R.string.titttle));
        Intent x = new Intent(context, Alert.class);
        x.putExtra(context.getString(R.string.titttle), Title);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(x);
    }
}

