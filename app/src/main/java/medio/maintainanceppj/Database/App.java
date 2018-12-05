package medio.maintainanceppj.Database;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String notifID = "chanell";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            NotificationChannel channels = new NotificationChannel(notifID,
                    "chanell",NotificationManager.IMPORTANCE_HIGH);
            channels.setDescription("Notification");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channels);
        }
    }
}
