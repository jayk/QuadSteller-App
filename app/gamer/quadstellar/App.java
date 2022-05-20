package app.gamer.quadstellar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.observer.AllLampSubject;
import app.gamer.quadstellar.observer.ConnectSubject;
import app.gamer.quadstellar.observer.LampSubject;
import app.gamer.quadstellar.observer.LanguageSubject;
import app.gamer.quadstellar.observer.SceneSubject;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import org.xutils.x;

public class App extends Application {
  public static String DEVICE_IP;
  
  public static final int NOTIFICATION_DOWN_ID = 1394959714;
  
  private static final String TAG = "APP";
  
  public static App app;
  
  public static HashMap<String, Long> heartDeviceTime;
  
  private static App instance;
  
  public static boolean isActive;
  
  public static FatherDeviceInfo mDevice;
  
  public static String macDress = "FFFFFFFFFFFF";
  
  public static Map<String, Long> map;
  
  public static String uniqueId;
  
  private Stack<Activity> activties;
  
  public boolean auth;
  
  public boolean isOnline;
  
  private AllLampSubject mAllLampSubject;
  
  private ConnectSubject mConnectSubject;
  
  private FatherDeviceInfo mCurrentDevice = null;
  
  Handler mHandler;
  
  private LampSubject mLampSubject;
  
  private LanguageSubject mLanguageSubject;
  
  private SceneSubject mSceneSubject;
  
  private int requestCode = -1;
  
  public int versionCode;
  
  public String versionName;
  
  static {
    DEVICE_IP = "255.255.255.255";
    heartDeviceTime = new HashMap<String, Long>();
  }
  
  public static Context getAppContext() {
    return (Context)instance;
  }
  
  public static Resources getAppResources() {
    return instance.getResources();
  }
  
  public static App getInstance() {
    return instance;
  }
  
  public Stack<Activity> getActivities() {
    if (this.activties == null)
      this.activties = new Stack<Activity>(); 
    return this.activties;
  }
  
  public AllLampSubject getAllLampSubject() {
    if (this.mAllLampSubject == null)
      this.mAllLampSubject = new AllLampSubject(); 
    return this.mAllLampSubject;
  }
  
  public ConnectSubject getConnectSubject() {
    if (this.mConnectSubject == null)
      this.mConnectSubject = new ConnectSubject(); 
    return this.mConnectSubject;
  }
  
  public Activity getCurrentActivity() {
    return getActivities().peek();
  }
  
  public FatherDeviceInfo getCurrentDevice() {
    return this.mCurrentDevice;
  }
  
  public String getCurrentDeviceMac() {
    String str = "";
    if (this.mCurrentDevice != null)
      str = this.mCurrentDevice.getMacAdrass(); 
    return str;
  }
  
  public LampSubject getLampSubject() {
    if (this.mLampSubject == null)
      this.mLampSubject = new LampSubject(); 
    return this.mLampSubject;
  }
  
  public LanguageSubject getLanguageSubject() {
    if (this.mLanguageSubject == null)
      this.mLanguageSubject = new LanguageSubject(); 
    return this.mLanguageSubject;
  }
  
  public int getRequestCode() {
    return this.requestCode;
  }
  
  public SceneSubject getSceneSubject() {
    if (this.mSceneSubject == null)
      this.mSceneSubject = new SceneSubject(); 
    return this.mSceneSubject;
  }
  
  public void onCreate() {
    TelephonyManager telephonyManager = (TelephonyManager)getBaseContext().getSystemService("phone");
    String str2 = "" + telephonyManager.getDeviceId();
    String str1 = "" + telephonyManager.getSimSerialNumber();
    uniqueId = (new UUID(("" + Settings.Secure.getString(getContentResolver(), "android_id")).hashCode(), str2.hashCode() << 32L | str1.hashCode())).toString();
    String[] arrayOfString = uniqueId.split("-");
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < arrayOfString.length; b++)
      stringBuilder.append(arrayOfString[b]); 
    uniqueId = stringBuilder.toString();
    app = this;
    super.onCreate();
    isActive = true;
    instance = this;
    x.Ext.init(this);
    x.Ext.setDebug(true);
    Utils.init(this);
    CrashReport.initCrashReport((Context)this, "d13faf0801", false);
    try {
      PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      this.versionCode = packageInfo.versionCode;
      this.versionName = packageInfo.versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {}
    this.auth = false;
    List list = FatherDeviceInfoDB.getInstance().queryUserList();
    if (list != null && list.size() > 0)
      for (FatherDeviceInfo fatherDeviceInfo : list)
        heartDeviceTime.put(fatherDeviceInfo.getMacAdrass(), Long.valueOf(System.currentTimeMillis()));  
  }
  
  public void onLowMemory() {
    super.onLowMemory();
  }
  
  public void setCurrentDevice(FatherDeviceInfo paramFatherDeviceInfo) {
    this.mCurrentDevice = paramFatherDeviceInfo;
    getConnectSubject().connectDeviceChange(this.mCurrentDevice);
  }
  
  public void setRequestCode(int paramInt) {
    this.requestCode = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/App.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */