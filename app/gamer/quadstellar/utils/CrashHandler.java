package app.gamer.quadstellar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import app.gamer.quadstellar.App;
import io.xlink.wifi.sdk.XlinkAgent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
  private static final String DISK_CACHE_PATH = "/xlink/";
  
  private static CrashHandler crashHandler;
  
  private Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
  
  private CrashHandler(Context paramContext) {
    Thread.setDefaultUncaughtExceptionHandler(this);
  }
  
  public static CrashHandler init(Context paramContext) {
    if (crashHandler == null)
      crashHandler = new CrashHandler(paramContext); 
    return crashHandler;
  }
  
  private void write2ErrorLog(String paramString) {
    // Byte code:
    //   0: new java/io/File
    //   3: dup
    //   4: new java/lang/StringBuilder
    //   7: dup
    //   8: invokespecial <init> : ()V
    //   11: aload_0
    //   12: invokevirtual getFilePath : ()Ljava/lang/String;
    //   15: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: ldc '/crash.txt'
    //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: invokevirtual toString : ()Ljava/lang/String;
    //   26: invokespecial <init> : (Ljava/lang/String;)V
    //   29: astore_2
    //   30: aconst_null
    //   31: astore_3
    //   32: aconst_null
    //   33: astore #4
    //   35: aload_3
    //   36: astore #5
    //   38: aload_2
    //   39: invokevirtual exists : ()Z
    //   42: ifeq -> 98
    //   45: aload_3
    //   46: astore #5
    //   48: aload_2
    //   49: invokevirtual delete : ()Z
    //   52: pop
    //   53: aload_3
    //   54: astore #5
    //   56: aload_2
    //   57: invokevirtual createNewFile : ()Z
    //   60: pop
    //   61: aload_3
    //   62: astore #5
    //   64: new java/io/FileOutputStream
    //   67: astore #6
    //   69: aload_3
    //   70: astore #5
    //   72: aload #6
    //   74: aload_2
    //   75: invokespecial <init> : (Ljava/io/File;)V
    //   78: aload #6
    //   80: aload_1
    //   81: invokevirtual getBytes : ()[B
    //   84: invokevirtual write : ([B)V
    //   87: aload #6
    //   89: ifnull -> 97
    //   92: aload #6
    //   94: invokevirtual close : ()V
    //   97: return
    //   98: aload_3
    //   99: astore #5
    //   101: aload_2
    //   102: invokevirtual getParentFile : ()Ljava/io/File;
    //   105: invokevirtual mkdirs : ()Z
    //   108: pop
    //   109: goto -> 53
    //   112: astore #6
    //   114: aload #4
    //   116: astore_1
    //   117: aload_1
    //   118: astore #5
    //   120: aload #6
    //   122: invokevirtual printStackTrace : ()V
    //   125: aload_1
    //   126: ifnull -> 97
    //   129: aload_1
    //   130: invokevirtual close : ()V
    //   133: goto -> 97
    //   136: astore_1
    //   137: aload_1
    //   138: invokevirtual printStackTrace : ()V
    //   141: goto -> 97
    //   144: astore_1
    //   145: aload_1
    //   146: invokevirtual printStackTrace : ()V
    //   149: goto -> 97
    //   152: astore_1
    //   153: aload #5
    //   155: ifnull -> 163
    //   158: aload #5
    //   160: invokevirtual close : ()V
    //   163: aload_1
    //   164: athrow
    //   165: astore #5
    //   167: aload #5
    //   169: invokevirtual printStackTrace : ()V
    //   172: goto -> 163
    //   175: astore_1
    //   176: aload #6
    //   178: astore #5
    //   180: goto -> 153
    //   183: astore #5
    //   185: aload #6
    //   187: astore_1
    //   188: aload #5
    //   190: astore #6
    //   192: goto -> 117
    // Exception table:
    //   from	to	target	type
    //   38	45	112	java/lang/Exception
    //   38	45	152	finally
    //   48	53	112	java/lang/Exception
    //   48	53	152	finally
    //   56	61	112	java/lang/Exception
    //   56	61	152	finally
    //   64	69	112	java/lang/Exception
    //   64	69	152	finally
    //   72	78	112	java/lang/Exception
    //   72	78	152	finally
    //   78	87	183	java/lang/Exception
    //   78	87	175	finally
    //   92	97	144	java/lang/Exception
    //   101	109	112	java/lang/Exception
    //   101	109	152	finally
    //   120	125	152	finally
    //   129	133	136	java/lang/Exception
    //   158	163	165	java/lang/Exception
  }
  
  public String getFilePath() {
    String str;
    if (Environment.getExternalStorageState().equals("mounted")) {
      str = Environment.getExternalStorageDirectory() + "/xlink/";
    } else {
      str = App.getInstance().getCacheDir() + "/xlink/";
    } 
    File file = new File(str);
    if (!file.exists())
      file.mkdirs(); 
    return str;
  }
  
  @SuppressLint({"SimpleDateFormat"})
  public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Date: ").append(simpleDateFormat.format(new Date())).append("\n");
    stringBuilder.append("===========\n");
    stringBuilder.append("Stacktrace:\n\n");
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    paramThrowable.printStackTrace(printWriter);
    stringBuilder.append(stringWriter.toString());
    stringBuilder.append("===========\n");
    printWriter.close();
    write2ErrorLog(stringBuilder.toString());
    if (this.handler != null)
      this.handler.uncaughtException(paramThread, paramThrowable); 
    XlinkAgent.getInstance().stop();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/CrashHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */