package app.gamer.quadstellar.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AppUpgradeService extends Service {
  public static final String DOWNLOAD_CANCEL = "download_cancel";
  
  private static final int NOTIFY_ID = 0;
  
  private static final String saveFileName = "IntelligentLight.apk";
  
  private static final String savePath = Environment.getExternalStorageDirectory().getPath() + "/IntelligentLight/";
  
  private String apkUrl = null;
  
  private DownloadBinder binder;
  
  private boolean canceled;
  
  private Thread downLoadThread;
  
  private int lastRate = 0;
  
  private Context mContext = (Context)this;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        int i;
        super.handleMessage(param1Message);
        switch (param1Message.what) {
          default:
            return;
          case 0:
            AppUpgradeService.this.mNotificationManager.cancel(0);
            AppUpgradeService.this.installApk();
          case 2:
            AppUpgradeService.this.mNotificationManager.cancel(0);
          case 1:
            i = param1Message.arg1;
            if (i < 100) {
              RemoteViews remoteViews = AppUpgradeService.this.mNotification.contentView;
              remoteViews.setTextViewText(2131756531, i + "%");
              remoteViews.setProgressBar(2131756530, 100, i, false);
            } else {
              AppUpgradeService.this.mNotification.flags = 16;
              AppUpgradeService.this.mNotification.contentView = null;
              Intent intent = new Intent("android.intent.action.VIEW");
              intent.setFlags(268435456);
              intent.setDataAndType(Uri.parse("file://" + AppUpgradeService.savePath + "IntelligentLight.apk"), "application/vnd.android.package-archive");
              intent.putExtra("completed", "yes");
              PendingIntent.getActivity(AppUpgradeService.this.mContext, 0, intent, 134217728);
              AppUpgradeService.access$402(AppUpgradeService.this, true);
              AppUpgradeService.this.stopSelf();
            } 
            AppUpgradeService.this.mNotificationManager.notify(0, AppUpgradeService.this.mNotification);
          case 4:
            break;
        } 
        AppUpgradeService.this.mNotification.contentView.setTextViewText(2131756532, "下载失败!");
        AppUpgradeService.this.mNotificationManager.notify(0, AppUpgradeService.this.mNotification);
      }
    };
  
  Notification mNotification;
  
  private NotificationManager mNotificationManager;
  
  private Runnable mdownApkRunnable = new Runnable() {
      public void run() {
        try {
          URL uRL = new URL();
          this(AppUpgradeService.this.apkUrl);
          HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
          httpURLConnection.connect();
          int i = httpURLConnection.getContentLength();
          InputStream inputStream = httpURLConnection.getInputStream();
          File file1 = new File();
          this(AppUpgradeService.savePath);
          if (!file1.exists())
            file1.mkdirs(); 
          StringBuilder stringBuilder = new StringBuilder();
          this();
          String str = stringBuilder.append(AppUpgradeService.savePath).append("IntelligentLight.apk").toString();
          File file2 = new File();
          this(str);
          FileOutputStream fileOutputStream = new FileOutputStream();
          this(file2);
          int j = 0;
          byte[] arrayOfByte = new byte[1024];
          while (true) {
            int k = inputStream.read(arrayOfByte);
            j += k;
            AppUpgradeService.access$702(AppUpgradeService.this, (int)(j / i * 100.0F));
            Message message = AppUpgradeService.this.mHandler.obtainMessage();
            message.what = 1;
            message.arg1 = AppUpgradeService.this.progress;
            if (AppUpgradeService.this.progress >= AppUpgradeService.this.lastRate + 1) {
              AppUpgradeService.this.mHandler.sendMessage(message);
              AppUpgradeService.access$1202(AppUpgradeService.this, AppUpgradeService.this.progress);
            } 
            if (k <= 0) {
              AppUpgradeService.this.mHandler.sendEmptyMessage(0);
              AppUpgradeService.access$902(AppUpgradeService.this, true);
            } else {
              fileOutputStream.write(arrayOfByte, 0, k);
              boolean bool = AppUpgradeService.this.canceled;
              if (bool) {
                fileOutputStream.close();
                inputStream.close();
                return;
              } 
              continue;
            } 
            fileOutputStream.close();
            inputStream.close();
            return;
          } 
        } catch (MalformedURLException malformedURLException) {
          malformedURLException.printStackTrace();
          AppUpgradeService.this.mHandler.sendEmptyMessage(4);
        } catch (IOException iOException) {
          iOException.printStackTrace();
          AppUpgradeService.this.mHandler.sendEmptyMessage(4);
        } catch (Exception exception) {
          exception.printStackTrace();
          AppUpgradeService.this.mHandler.sendEmptyMessage(4);
        } 
      }
    };
  
  private int progress;
  
  private boolean serviceIsDestroy = false;
  
  private void downloadApk() {
    this.downLoadThread = new Thread(this.mdownApkRunnable);
    this.downLoadThread.start();
  }
  
  private void installApk() {
    File file = new File(savePath + "IntelligentLight.apk");
    if (file.exists()) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
      this.mContext.startActivity(intent);
    } 
  }
  
  private void setUpNotification() {
    this.mNotification = new Notification(2130837951, getString(2131297153), System.currentTimeMillis());
    this.mNotification.flags = 2;
    RemoteViews remoteViews = new RemoteViews(getPackageName(), 2130903113);
    remoteViews.setTextViewText(2131756531, "0%");
    remoteViews.setTextViewText(2131756438, "IntelligentLight.apk");
    remoteViews.setTextViewText(2131756532, getString(2131296632));
    this.mNotification.contentView = remoteViews;
    this.mNotificationManager.notify(0, this.mNotification);
  }
  
  private void startDownload() {
    this.canceled = false;
    downloadApk();
  }
  
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)this.binder;
  }
  
  public void onCreate() {
    super.onCreate();
    this.binder = new DownloadBinder();
    this.mNotificationManager = (NotificationManager)getSystemService("notification");
  }
  
  public void onDestroy() {
    super.onDestroy();
  }
  
  public void onRebind(Intent paramIntent) {
    super.onRebind(paramIntent);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    if (paramIntent != null)
      this.apkUrl = paramIntent.getStringExtra("downloadUrl"); 
    if (this.apkUrl == null) {
      stopSelf();
      return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    } 
    if (this.downLoadThread == null || !this.downLoadThread.isAlive()) {
      this.progress = 0;
      setUpNotification();
      (new Thread() {
          public void run() {
            AppUpgradeService.this.startDownload();
          }
        }).start();
    } 
    return super.onStartCommand(paramIntent, paramInt1, paramInt2);
  }
  
  public boolean onUnbind(Intent paramIntent) {
    return super.onUnbind(paramIntent);
  }
  
  public class DownloadBinder extends Binder {
    public void cancel() {
      AppUpgradeService.access$902(AppUpgradeService.this, true);
    }
    
    public void cancelNotification() {
      AppUpgradeService.this.mHandler.sendEmptyMessage(2);
    }
    
    public int getProgress() {
      return AppUpgradeService.this.progress;
    }
    
    public boolean isCanceled() {
      return AppUpgradeService.this.canceled;
    }
    
    public boolean serviceIsDestroy() {
      return AppUpgradeService.this.serviceIsDestroy;
    }
    
    public void start() {
      if (AppUpgradeService.this.downLoadThread == null || !AppUpgradeService.this.downLoadThread.isAlive()) {
        AppUpgradeService.access$702(AppUpgradeService.this, 0);
        AppUpgradeService.this.setUpNotification();
        (new Thread() {
            public void run() {
              AppUpgradeService.this.startDownload();
            }
          }).start();
      } 
    }
  }
  
  class null extends Thread {
    public void run() {
      AppUpgradeService.this.startDownload();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/AppUpgradeService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */