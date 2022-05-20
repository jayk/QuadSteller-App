package app.gamer.quadstellar.crash;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
  private static CrashHandler INSTANCE = new CrashHandler();
  
  private static final String TAG = "CrashHandler";
  
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
  
  private Map<String, String> info = new HashMap<String, String>();
  
  private Context mContext;
  
  private Thread.UncaughtExceptionHandler mDefaultHandler;
  
  public static CrashHandler getInstance() {
    return INSTANCE;
  }
  
  private String saveCrashInfo2File(Throwable paramThrowable) {
    StringBuffer stringBuffer = new StringBuffer();
    for (Map.Entry<String, String> entry : this.info.entrySet()) {
      String str2 = (String)entry.getKey();
      String str1 = (String)entry.getValue();
      stringBuffer.append(str2 + "=" + str1 + "\r\n");
    } 
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    paramThrowable.printStackTrace(printWriter);
    for (paramThrowable = paramThrowable.getCause(); paramThrowable != null; paramThrowable = paramThrowable.getCause())
      paramThrowable.printStackTrace(printWriter); 
    printWriter.close();
    stringBuffer.append(stringWriter.toString());
    long l = System.currentTimeMillis();
    null = this.format.format(new Date());
    null = "crash-" + null + "-" + l + ".log";
    if (Environment.getExternalStorageState().equals("mounted"))
      try {
        Log.e("测试", "开始读写");
        File file1 = new File();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        this(stringBuilder.append(Environment.getExternalStorageDirectory()).append("/crash/").toString());
        if (!file1.exists())
          file1.mkdir(); 
        File file2 = new File();
        this(file1, null);
        FileOutputStream fileOutputStream = new FileOutputStream();
        this(file2);
        fileOutputStream.write(stringBuffer.toString().getBytes());
        fileOutputStream.close();
        Log.e("测试", "读写完毕");
        return null;
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
    return null;
  }
  
  public void collectDeviceInfo(Context paramContext) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
    //   4: aload_1
    //   5: invokevirtual getPackageName : ()Ljava/lang/String;
    //   8: iconst_1
    //   9: invokevirtual getPackageInfo : (Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   12: astore_2
    //   13: aload_2
    //   14: ifnull -> 80
    //   17: aload_2
    //   18: getfield versionName : Ljava/lang/String;
    //   21: ifnonnull -> 180
    //   24: ldc 'null'
    //   26: astore_1
    //   27: new java/lang/StringBuilder
    //   30: astore_3
    //   31: aload_3
    //   32: invokespecial <init> : ()V
    //   35: aload_3
    //   36: aload_2
    //   37: getfield versionCode : I
    //   40: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   43: ldc ''
    //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: invokevirtual toString : ()Ljava/lang/String;
    //   51: astore_2
    //   52: aload_0
    //   53: getfield info : Ljava/util/Map;
    //   56: ldc_w 'versionName'
    //   59: aload_1
    //   60: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   65: pop
    //   66: aload_0
    //   67: getfield info : Ljava/util/Map;
    //   70: ldc_w 'versionCode'
    //   73: aload_2
    //   74: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   79: pop
    //   80: ldc_w android/os/Build
    //   83: invokevirtual getDeclaredFields : ()[Ljava/lang/reflect/Field;
    //   86: astore_1
    //   87: aload_1
    //   88: arraylength
    //   89: istore #4
    //   91: iconst_0
    //   92: istore #5
    //   94: iload #5
    //   96: iload #4
    //   98: if_icmpge -> 212
    //   101: aload_1
    //   102: iload #5
    //   104: aaload
    //   105: astore_3
    //   106: aload_3
    //   107: iconst_1
    //   108: invokevirtual setAccessible : (Z)V
    //   111: aload_0
    //   112: getfield info : Ljava/util/Map;
    //   115: aload_3
    //   116: invokevirtual getName : ()Ljava/lang/String;
    //   119: aload_3
    //   120: ldc ''
    //   122: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   125: invokevirtual toString : ()Ljava/lang/String;
    //   128: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   133: pop
    //   134: new java/lang/StringBuilder
    //   137: astore_2
    //   138: aload_2
    //   139: invokespecial <init> : ()V
    //   142: ldc 'CrashHandler'
    //   144: aload_2
    //   145: aload_3
    //   146: invokevirtual getName : ()Ljava/lang/String;
    //   149: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: ldc_w ':'
    //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: aload_3
    //   159: ldc ''
    //   161: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   164: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   167: invokevirtual toString : ()Ljava/lang/String;
    //   170: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   173: pop
    //   174: iinc #5, 1
    //   177: goto -> 94
    //   180: aload_2
    //   181: getfield versionName : Ljava/lang/String;
    //   184: astore_1
    //   185: goto -> 27
    //   188: astore_1
    //   189: aload_1
    //   190: invokevirtual printStackTrace : ()V
    //   193: goto -> 80
    //   196: astore_2
    //   197: aload_2
    //   198: invokevirtual printStackTrace : ()V
    //   201: goto -> 174
    //   204: astore_2
    //   205: aload_2
    //   206: invokevirtual printStackTrace : ()V
    //   209: goto -> 174
    //   212: return
    // Exception table:
    //   from	to	target	type
    //   0	13	188	android/content/pm/PackageManager$NameNotFoundException
    //   17	24	188	android/content/pm/PackageManager$NameNotFoundException
    //   27	80	188	android/content/pm/PackageManager$NameNotFoundException
    //   106	174	196	java/lang/IllegalArgumentException
    //   106	174	204	java/lang/IllegalAccessException
    //   180	185	188	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public boolean handleException(Throwable paramThrowable) {
    if (paramThrowable == null)
      return false; 
    (new Thread() {
        public void run() {
          Looper.prepare();
          Toast.makeText(CrashHandler.this.mContext, "很抱歉,程序出现异常,即将退出", 0).show();
          Looper.loop();
        }
      }).start();
    collectDeviceInfo(this.mContext);
    saveCrashInfo2File(paramThrowable);
    return true;
  }
  
  public void init(Context paramContext) {
    this.mContext = paramContext;
    this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this);
  }
  
  public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
    if (!handleException(paramThrowable) && this.mDefaultHandler != null) {
      this.mDefaultHandler.uncaughtException(paramThread, paramThrowable);
      return;
    } 
    try {
      Thread.sleep(3000L);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
    Process.killProcess(Process.myPid());
    System.exit(1);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/crash/CrashHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */