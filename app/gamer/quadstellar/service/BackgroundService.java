package app.gamer.quadstellar.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends Service {
  private void startPrority() {
    Notification notification = new Notification(2130837951, "eFamily", 0L);
    Intent intent = new Intent();
    intent.setAction("www.efamily.com");
    PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
    startForeground(1, notification);
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onCreate() {
    super.onCreate();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/service/BackgroundService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */