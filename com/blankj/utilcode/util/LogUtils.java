package com.blankj.utilcode.util;

import android.os.Environment;
import android.support.annotation.IntRange;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class LogUtils {
  public static final int A = 7;
  
  private static final String ARGS = "args";
  
  private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";
  
  private static final Config CONFIG;
  
  public static final int D = 3;
  
  public static final int E = 6;
  
  private static final int FILE = 16;
  
  private static final String FILE_SEP;
  
  private static final Format FORMAT;
  
  public static final int I = 4;
  
  private static final int JSON = 32;
  
  private static final String LEFT_BORDER = "║ ";
  
  private static final String LINE_SEP;
  
  private static final int MAX_LEN = 4000;
  
  private static final String NULL = "null";
  
  private static final String NULL_TIPS = "Log with null object.";
  
  private static final String SPLIT_BORDER = "╟───────────────────────────────────────────────────────────────────────────────────────────────────";
  
  private static final char[] T = new char[] { 'V', 'D', 'I', 'W', 'E', 'A' };
  
  private static final String TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
  
  public static final int V = 2;
  
  public static final int W = 5;
  
  private static final int XML = 48;
  
  private static int sConsoleFilter;
  
  private static String sDefaultDir;
  
  private static String sDir;
  
  private static ExecutorService sExecutor;
  
  private static int sFileFilter;
  
  private static String sFilePrefix = "util";
  
  private static String sGlobalTag;
  
  private static boolean sLog2ConsoleSwitch;
  
  private static boolean sLog2FileSwitch;
  
  private static boolean sLogBorderSwitch;
  
  private static boolean sLogHeadSwitch;
  
  private static boolean sLogSwitch = true;
  
  private static int sStackDeep;
  
  private static boolean sTagIsSpace;
  
  static {
    sLog2ConsoleSwitch = true;
    sGlobalTag = null;
    sTagIsSpace = true;
    sLogHeadSwitch = true;
    sLog2FileSwitch = false;
    sLogBorderSwitch = true;
    sConsoleFilter = 2;
    sFileFilter = 2;
    sStackDeep = 1;
    FILE_SEP = System.getProperty("file.separator");
    LINE_SEP = System.getProperty("line.separator");
    FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault());
    CONFIG = new Config();
  }
  
  private LogUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void a(Object paramObject) {
    log(7, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void a(String paramString, Object... paramVarArgs) {
    log(7, paramString, paramVarArgs);
  }
  
  private static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  private static boolean createOrExistsFile(String paramString) {
    boolean bool1 = false;
    File file = new File(paramString);
    if (file.exists())
      return file.isFile(); 
    boolean bool2 = bool1;
    if (createOrExistsDir(file.getParentFile()))
      try {
        bool2 = file.createNewFile();
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bool2 = bool1;
      }  
    return bool2;
  }
  
  public static void d(Object paramObject) {
    log(3, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void d(String paramString, Object... paramVarArgs) {
    log(3, paramString, paramVarArgs);
  }
  
  public static void e(Object paramObject) {
    log(6, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void e(String paramString, Object... paramVarArgs) {
    log(6, paramString, paramVarArgs);
  }
  
  public static void file(int paramInt, Object paramObject) {
    log(paramInt | 0x10, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void file(int paramInt, String paramString, Object paramObject) {
    log(paramInt | 0x10, paramString, new Object[] { paramObject });
  }
  
  public static void file(Object paramObject) {
    log(19, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void file(String paramString, Object paramObject) {
    log(19, paramString, new Object[] { paramObject });
  }
  
  private static String formatJson(String paramString) {
    String str;
    try {
      if (paramString.startsWith("{")) {
        JSONObject jSONObject = new JSONObject();
        this(paramString);
        return jSONObject.toString(4);
      } 
      str = paramString;
      if (paramString.startsWith("[")) {
        JSONArray jSONArray = new JSONArray();
        this(paramString);
        String str1 = jSONArray.toString(4);
      } 
    } catch (JSONException jSONException) {
      jSONException.printStackTrace();
      str = paramString;
    } 
    return str;
  }
  
  private static String formatXml(String paramString) {
    try {
      StreamSource streamSource = new StreamSource();
      StringReader stringReader = new StringReader();
      this(paramString);
      this(stringReader);
      StreamResult streamResult = new StreamResult();
      StringWriter stringWriter = new StringWriter();
      this();
      this(stringWriter);
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty("indent", "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.transform(streamSource, streamResult);
      String str = streamResult.getWriter().toString();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      str = str.replaceFirst(">", stringBuilder.append(">").append(LINE_SEP).toString());
      paramString = str;
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return paramString;
  }
  
  public static Config getConfig() {
    return CONFIG;
  }
  
  public static void i(Object paramObject) {
    log(4, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void i(String paramString, Object... paramVarArgs) {
    log(4, paramString, paramVarArgs);
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  public static void json(int paramInt, String paramString) {
    log(paramInt | 0x20, sGlobalTag, new Object[] { paramString });
  }
  
  public static void json(int paramInt, String paramString1, String paramString2) {
    log(paramInt | 0x20, paramString1, new Object[] { paramString2 });
  }
  
  public static void json(String paramString) {
    log(35, sGlobalTag, new Object[] { paramString });
  }
  
  public static void json(String paramString1, String paramString2) {
    log(35, paramString1, new Object[] { paramString2 });
  }
  
  private static void log(int paramInt, String paramString, Object... paramVarArgs) {
    if (sLogSwitch && (sLog2ConsoleSwitch || sLog2FileSwitch)) {
      int i = paramInt & 0xF;
      paramInt &= 0xF0;
      if (i >= sConsoleFilter || i >= sFileFilter) {
        TagHead tagHead = processTagAndHead(paramString);
        String str = processBody(paramInt, paramVarArgs);
        if (sLog2ConsoleSwitch && i >= sConsoleFilter && paramInt != 16)
          print2Console(i, tagHead.tag, tagHead.consoleHead, str); 
        if ((sLog2FileSwitch || paramInt == 16) && i >= sFileFilter)
          print2File(i, tagHead.tag, tagHead.fileHead + str); 
      } 
    } 
  }
  
  private static void print2Console(int paramInt, String paramString1, String[] paramArrayOfString, String paramString2) {
    printBorder(paramInt, paramString1, true);
    printHead(paramInt, paramString1, paramArrayOfString);
    printMsg(paramInt, paramString1, paramString2);
    printBorder(paramInt, paramString1, false);
  }
  
  private static void print2File(int paramInt, final String tag, final String content) {
    Date date = new Date(System.currentTimeMillis());
    final String fullPath = FORMAT.format(date);
    String str2 = str1.substring(0, 5);
    String str3 = str1.substring(6);
    StringBuilder stringBuilder2 = new StringBuilder();
    if (sDir == null) {
      str1 = sDefaultDir;
    } else {
      str1 = sDir;
    } 
    str1 = stringBuilder2.append(str1).append(sFilePrefix).append("-").append(str2).append(".txt").toString();
    if (!createOrExistsFile(str1)) {
      Log.e(tag, "log to " + str1 + " failed!");
      return;
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str3).append(T[paramInt - 2]).append("/").append(tag).append(content).append(LINE_SEP);
    content = stringBuilder1.toString();
    if (sExecutor == null)
      sExecutor = Executors.newSingleThreadExecutor(); 
    sExecutor.execute(new Runnable() {
          public void run() {
            // Byte code:
            //   0: aconst_null
            //   1: astore_1
            //   2: aconst_null
            //   3: astore_2
            //   4: aload_1
            //   5: astore_3
            //   6: new java/io/BufferedWriter
            //   9: astore #4
            //   11: aload_1
            //   12: astore_3
            //   13: new java/io/FileWriter
            //   16: astore #5
            //   18: aload_1
            //   19: astore_3
            //   20: aload #5
            //   22: aload_0
            //   23: getfield val$fullPath : Ljava/lang/String;
            //   26: iconst_1
            //   27: invokespecial <init> : (Ljava/lang/String;Z)V
            //   30: aload_1
            //   31: astore_3
            //   32: aload #4
            //   34: aload #5
            //   36: invokespecial <init> : (Ljava/io/Writer;)V
            //   39: aload #4
            //   41: aload_0
            //   42: getfield val$content : Ljava/lang/String;
            //   45: invokevirtual write : (Ljava/lang/String;)V
            //   48: aload_0
            //   49: getfield val$tag : Ljava/lang/String;
            //   52: astore_1
            //   53: new java/lang/StringBuilder
            //   56: astore_3
            //   57: aload_3
            //   58: invokespecial <init> : ()V
            //   61: aload_1
            //   62: aload_3
            //   63: ldc 'log to '
            //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   68: aload_0
            //   69: getfield val$fullPath : Ljava/lang/String;
            //   72: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   75: ldc ' success!'
            //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   80: invokevirtual toString : ()Ljava/lang/String;
            //   83: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
            //   86: pop
            //   87: aload #4
            //   89: ifnull -> 97
            //   92: aload #4
            //   94: invokevirtual close : ()V
            //   97: return
            //   98: astore_3
            //   99: aload_3
            //   100: invokevirtual printStackTrace : ()V
            //   103: goto -> 97
            //   106: astore_1
            //   107: aload_2
            //   108: astore #4
            //   110: aload #4
            //   112: astore_3
            //   113: aload_1
            //   114: invokevirtual printStackTrace : ()V
            //   117: aload #4
            //   119: astore_3
            //   120: aload_0
            //   121: getfield val$tag : Ljava/lang/String;
            //   124: astore_2
            //   125: aload #4
            //   127: astore_3
            //   128: new java/lang/StringBuilder
            //   131: astore_1
            //   132: aload #4
            //   134: astore_3
            //   135: aload_1
            //   136: invokespecial <init> : ()V
            //   139: aload #4
            //   141: astore_3
            //   142: aload_2
            //   143: aload_1
            //   144: ldc 'log to '
            //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   149: aload_0
            //   150: getfield val$fullPath : Ljava/lang/String;
            //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   156: ldc ' failed!'
            //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   161: invokevirtual toString : ()Ljava/lang/String;
            //   164: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
            //   167: pop
            //   168: aload #4
            //   170: ifnull -> 97
            //   173: aload #4
            //   175: invokevirtual close : ()V
            //   178: goto -> 97
            //   181: astore_3
            //   182: aload_3
            //   183: invokevirtual printStackTrace : ()V
            //   186: goto -> 97
            //   189: astore #4
            //   191: aload_3
            //   192: astore_1
            //   193: aload_1
            //   194: ifnull -> 201
            //   197: aload_1
            //   198: invokevirtual close : ()V
            //   201: aload #4
            //   203: athrow
            //   204: astore_3
            //   205: aload_3
            //   206: invokevirtual printStackTrace : ()V
            //   209: goto -> 201
            //   212: astore_3
            //   213: aload #4
            //   215: astore_1
            //   216: aload_3
            //   217: astore #4
            //   219: goto -> 193
            //   222: astore_3
            //   223: aload_3
            //   224: astore_1
            //   225: goto -> 110
            // Exception table:
            //   from	to	target	type
            //   6	11	106	java/io/IOException
            //   6	11	189	finally
            //   13	18	106	java/io/IOException
            //   13	18	189	finally
            //   20	30	106	java/io/IOException
            //   20	30	189	finally
            //   32	39	106	java/io/IOException
            //   32	39	189	finally
            //   39	87	222	java/io/IOException
            //   39	87	212	finally
            //   92	97	98	java/io/IOException
            //   113	117	189	finally
            //   120	125	189	finally
            //   128	132	189	finally
            //   135	139	189	finally
            //   142	168	189	finally
            //   173	178	181	java/io/IOException
            //   197	201	204	java/io/IOException
          }
        });
  }
  
  private static void printBorder(int paramInt, String paramString, boolean paramBoolean) {
    if (sLogBorderSwitch) {
      String str;
      if (paramBoolean) {
        str = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
      } else {
        str = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";
      } 
      Log.println(paramInt, paramString, str);
    } 
  }
  
  private static void printHead(int paramInt, String paramString, String[] paramArrayOfString) {
    if (paramArrayOfString != null) {
      int i = paramArrayOfString.length;
      for (byte b = 0; b < i; b++) {
        String str1 = paramArrayOfString[b];
        String str2 = str1;
        if (sLogBorderSwitch)
          str2 = "║ " + str1; 
        Log.println(paramInt, paramString, str2);
      } 
      Log.println(paramInt, paramString, "╟───────────────────────────────────────────────────────────────────────────────────────────────────");
    } 
  }
  
  private static void printMsg(int paramInt, String paramString1, String paramString2) {
    int i = paramString2.length();
    int j = i / 4000;
    if (j > 0) {
      byte b1 = 0;
      for (byte b2 = 0; b2 < j; b2++) {
        printSubMsg(paramInt, paramString1, paramString2.substring(b1, b1 + 4000));
        b1 += 4000;
      } 
      if (b1 != i)
        printSubMsg(paramInt, paramString1, paramString2.substring(b1, i)); 
      return;
    } 
    printSubMsg(paramInt, paramString1, paramString2);
  }
  
  private static void printSubMsg(int paramInt, String paramString1, String paramString2) {
    if (!sLogBorderSwitch) {
      Log.println(paramInt, paramString1, paramString2);
      return;
    } 
    new StringBuilder();
    String[] arrayOfString = paramString2.split(LINE_SEP);
    int i = arrayOfString.length;
    byte b = 0;
    while (true) {
      if (b < i) {
        paramString2 = arrayOfString[b];
        Log.println(paramInt, paramString1, "║ " + paramString2);
        b++;
        continue;
      } 
      return;
    } 
  }
  
  private static String processBody(int paramInt, Object... paramVarArgs) {
    Object object;
    null = "Log with null object.";
    if (paramVarArgs != null) {
      if (paramVarArgs.length == 1) {
        object = paramVarArgs[0];
        if (object == null) {
          object = "null";
        } else {
          object = object.toString();
        } 
        if (paramInt == 32)
          return formatJson((String)object); 
        null = object;
        if (paramInt == 48)
          null = formatXml((String)object); 
        return (String)null;
      } 
    } else {
      return (String)null;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    paramInt = 0;
    int i = object.length;
    while (paramInt < i) {
      null = object[paramInt];
      StringBuilder stringBuilder1 = stringBuilder.append("args").append("[").append(paramInt).append("]").append(" = ");
      if (null == null) {
        null = "null";
      } else {
        null = null.toString();
      } 
      stringBuilder1.append((String)null).append(LINE_SEP);
      paramInt++;
    } 
    return stringBuilder.toString();
  }
  
  private static TagHead processTagAndHead(String paramString) {
    if (!sTagIsSpace && !sLogHeadSwitch) {
      paramString = sGlobalTag;
    } else {
      String str3;
      StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
      StackTraceElement stackTraceElement = arrayOfStackTraceElement[3];
      String str1 = stackTraceElement.getFileName();
      if (str1 == null) {
        String str = stackTraceElement.getClassName();
        String[] arrayOfString = str.split("\\.");
        if (arrayOfString.length > 0)
          str = arrayOfString[arrayOfString.length - 1]; 
        int i = str.indexOf('$');
        str3 = str;
        if (i != -1)
          str3 = str.substring(0, i); 
        str1 = str3 + ".java";
      } else {
        int i = str1.indexOf('.');
        if (i == -1) {
          str3 = str1;
        } else {
          str3 = str1.substring(0, i);
        } 
      } 
      String str2 = paramString;
      if (sTagIsSpace) {
        str2 = paramString;
        if (isSpace(paramString))
          str2 = str3; 
      } 
      paramString = str2;
      if (sLogHeadSwitch) {
        str3 = Thread.currentThread().getName();
        String str = (new Formatter()).format("%s, %s(%s:%d)", new Object[] { str3, stackTraceElement.getMethodName(), str1, Integer.valueOf(stackTraceElement.getLineNumber()) }).toString();
        str1 = " [" + str + "]: ";
        if (sStackDeep <= 1)
          return new TagHead(str2, new String[] { str }, str1); 
        String[] arrayOfString = new String[Math.min(sStackDeep, arrayOfStackTraceElement.length - 3)];
        arrayOfString[0] = str;
        int i = str3.length();
        str3 = (new Formatter()).format("%" + (i + 2) + "s", new Object[] { "" }).toString();
        i = 1;
        int j = arrayOfString.length;
        while (i < j) {
          StackTraceElement stackTraceElement1 = arrayOfStackTraceElement[i + 3];
          arrayOfString[i] = (new Formatter()).format("%s%s(%s:%d)", new Object[] { str3, stackTraceElement1.getMethodName(), stackTraceElement1.getFileName(), Integer.valueOf(stackTraceElement1.getLineNumber()) }).toString();
          i++;
        } 
        return new TagHead(str2, arrayOfString, str1);
      } 
    } 
    return new TagHead(paramString, null, ": ");
  }
  
  public static void v(Object paramObject) {
    log(2, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void v(String paramString, Object... paramVarArgs) {
    log(2, paramString, paramVarArgs);
  }
  
  public static void w(Object paramObject) {
    log(5, sGlobalTag, new Object[] { paramObject });
  }
  
  public static void w(String paramString, Object... paramVarArgs) {
    log(5, paramString, paramVarArgs);
  }
  
  public static void xml(int paramInt, String paramString) {
    log(paramInt | 0x30, sGlobalTag, new Object[] { paramString });
  }
  
  public static void xml(int paramInt, String paramString1, String paramString2) {
    log(paramInt | 0x30, paramString1, new Object[] { paramString2 });
  }
  
  public static void xml(String paramString) {
    log(51, sGlobalTag, new Object[] { paramString });
  }
  
  public static void xml(String paramString1, String paramString2) {
    log(51, paramString1, new Object[] { paramString2 });
  }
  
  public static class Config {
    private Config() {
      if (LogUtils.sDefaultDir == null) {
        if ("mounted".equals(Environment.getExternalStorageState()) && Utils.getApp().getExternalCacheDir() != null) {
          LogUtils.access$102(Utils.getApp().getExternalCacheDir() + LogUtils.FILE_SEP + "log" + LogUtils.FILE_SEP);
          return;
        } 
        LogUtils.access$102(Utils.getApp().getCacheDir() + LogUtils.FILE_SEP + "log" + LogUtils.FILE_SEP);
      } 
    }
    
    public Config setBorderSwitch(boolean param1Boolean) {
      LogUtils.access$1202(param1Boolean);
      return this;
    }
    
    public Config setConsoleFilter(int param1Int) {
      LogUtils.access$1302(param1Int);
      return this;
    }
    
    public Config setConsoleSwitch(boolean param1Boolean) {
      LogUtils.access$402(param1Boolean);
      return this;
    }
    
    public Config setDir(File param1File) {
      if (param1File == null) {
        param1File = null;
        LogUtils.access$1002((String)param1File);
        return this;
      } 
      String str = param1File.getAbsolutePath() + LogUtils.FILE_SEP;
      LogUtils.access$1002(str);
      return this;
    }
    
    public Config setDir(String param1String) {
      if (LogUtils.isSpace(param1String)) {
        LogUtils.access$1002(null);
        return this;
      } 
      if (!param1String.endsWith(LogUtils.FILE_SEP))
        param1String = param1String + LogUtils.FILE_SEP; 
      LogUtils.access$1002(param1String);
      return this;
    }
    
    public Config setFileFilter(int param1Int) {
      LogUtils.access$1402(param1Int);
      return this;
    }
    
    public Config setFilePrefix(String param1String) {
      if (LogUtils.isSpace(param1String)) {
        LogUtils.access$1102("util");
        return this;
      } 
      LogUtils.access$1102(param1String);
      return this;
    }
    
    public Config setGlobalTag(String param1String) {
      if (LogUtils.isSpace(param1String)) {
        LogUtils.access$602("");
        LogUtils.access$702(true);
        return this;
      } 
      LogUtils.access$602(param1String);
      LogUtils.access$702(false);
      return this;
    }
    
    public Config setLog2FileSwitch(boolean param1Boolean) {
      LogUtils.access$902(param1Boolean);
      return this;
    }
    
    public Config setLogHeadSwitch(boolean param1Boolean) {
      LogUtils.access$802(param1Boolean);
      return this;
    }
    
    public Config setLogSwitch(boolean param1Boolean) {
      LogUtils.access$302(param1Boolean);
      return this;
    }
    
    public Config setStackDeep(@IntRange(from = 1L) int param1Int) {
      LogUtils.access$1502(param1Int);
      return this;
    }
    
    public String toString() {
      StringBuilder stringBuilder = (new StringBuilder()).append("switch: ").append(LogUtils.sLogSwitch).append(LogUtils.LINE_SEP).append("console: ").append(LogUtils.sLog2ConsoleSwitch).append(LogUtils.LINE_SEP).append("tag: ");
      if (LogUtils.sTagIsSpace) {
        str = "null";
      } else {
        str = LogUtils.sGlobalTag;
      } 
      stringBuilder = stringBuilder.append(str).append(LogUtils.LINE_SEP).append("head: ").append(LogUtils.sLogHeadSwitch).append(LogUtils.LINE_SEP).append("file: ").append(LogUtils.sLog2FileSwitch).append(LogUtils.LINE_SEP).append("dir: ");
      if (LogUtils.sDir == null) {
        str = LogUtils.sDefaultDir;
        return stringBuilder.append(str).append(LogUtils.LINE_SEP).append("filePrefix").append(LogUtils.sFilePrefix).append(LogUtils.LINE_SEP).append("border: ").append(LogUtils.sLogBorderSwitch).append(LogUtils.LINE_SEP).append("consoleFilter: ").append(LogUtils.T[LogUtils.sConsoleFilter - 2]).append(LogUtils.LINE_SEP).append("fileFilter: ").append(LogUtils.T[LogUtils.sFileFilter - 2]).append(LogUtils.LINE_SEP).append("stackDeep: ").append(LogUtils.sStackDeep).toString();
      } 
      String str = LogUtils.sDir;
      return stringBuilder.append(str).append(LogUtils.LINE_SEP).append("filePrefix").append(LogUtils.sFilePrefix).append(LogUtils.LINE_SEP).append("border: ").append(LogUtils.sLogBorderSwitch).append(LogUtils.LINE_SEP).append("consoleFilter: ").append(LogUtils.T[LogUtils.sConsoleFilter - 2]).append(LogUtils.LINE_SEP).append("fileFilter: ").append(LogUtils.T[LogUtils.sFileFilter - 2]).append(LogUtils.LINE_SEP).append("stackDeep: ").append(LogUtils.sStackDeep).toString();
    }
  }
  
  private static class TagHead {
    String[] consoleHead;
    
    String fileHead;
    
    String tag;
    
    TagHead(String param1String1, String[] param1ArrayOfString, String param1String2) {
      this.tag = param1String1;
      this.consoleHead = param1ArrayOfString;
      this.fileHead = param1String2;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/LogUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */