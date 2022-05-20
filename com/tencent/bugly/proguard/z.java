package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.info.a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class z {
  private static Map<String, String> a = null;
  
  private static boolean b = false;
  
  public static Context a(Context paramContext) {
    if (paramContext != null) {
      Context context = paramContext.getApplicationContext();
      if (context != null)
        paramContext = context; 
    } 
    return paramContext;
  }
  
  public static SharedPreferences a(String paramString, Context paramContext) {
    return (paramContext != null) ? paramContext.getSharedPreferences(paramString, 0) : null;
  }
  
  public static BufferedReader a(String paramString1, String paramString2) {
    String str1;
    String str2 = null;
    if (paramString1 == null)
      return (BufferedReader)str2; 
    try {
      File file = new File();
      this(paramString1, paramString2);
      paramString1 = str2;
      if (file.exists()) {
        paramString1 = str2;
        if (file.canRead())
          BufferedReader bufferedReader = b(file); 
      } 
    } catch (NullPointerException nullPointerException) {
      x.a(nullPointerException);
      str1 = str2;
    } 
    return (BufferedReader)str1;
  }
  
  public static Object a(String paramString1, String paramString2, Object paramObject, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject) {
    Object object;
    paramObject = null;
    try {
      Method method = Class.forName(paramString1).getDeclaredMethod(paramString2, paramArrayOfClass);
      method.setAccessible(true);
      object = method.invoke((Object)null, paramArrayOfObject);
    } catch (Exception exception) {
      object = paramObject;
    } 
    return object;
  }
  
  public static <T> T a(byte[] paramArrayOfbyte, Parcelable.Creator<T> paramCreator) {
    Parcel parcel = Parcel.obtain();
    parcel.unmarshall(paramArrayOfbyte, 0, paramArrayOfbyte.length);
    parcel.setDataPosition(0);
    try {
      Object object1 = paramCreator.createFromParcel(parcel);
      Object object2 = object1;
      return (T)object2;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return null;
    } finally {
      if (parcel != null)
        parcel.recycle(); 
    } 
  }
  
  public static String a() {
    return a(System.currentTimeMillis());
  }
  
  public static String a(long paramLong) {
    String str;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
      this("yyyy-MM-dd HH:mm:ss", Locale.US);
      Date date = new Date();
      this(paramLong);
      str = simpleDateFormat.format(date);
    } catch (Exception exception) {
      str = (new Date()).toString();
    } 
    return str;
  }
  
  public static String a(Context paramContext, int paramInt, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: ldc 'android.permission.READ_LOGS'
    //   3: invokestatic a : (Landroid/content/Context;Ljava/lang/String;)Z
    //   6: ifne -> 23
    //   9: ldc 'no read_log permission!'
    //   11: iconst_0
    //   12: anewarray java/lang/Object
    //   15: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   18: pop
    //   19: aconst_null
    //   20: astore_2
    //   21: aload_2
    //   22: areturn
    //   23: aload_2
    //   24: ifnonnull -> 258
    //   27: iconst_4
    //   28: anewarray java/lang/String
    //   31: astore_0
    //   32: aload_0
    //   33: iconst_0
    //   34: ldc 'logcat'
    //   36: aastore
    //   37: aload_0
    //   38: iconst_1
    //   39: ldc '-d'
    //   41: aastore
    //   42: aload_0
    //   43: iconst_2
    //   44: ldc '-v'
    //   46: aastore
    //   47: aload_0
    //   48: iconst_3
    //   49: ldc 'threadtime'
    //   51: aastore
    //   52: aconst_null
    //   53: astore_2
    //   54: aconst_null
    //   55: astore_3
    //   56: new java/lang/StringBuilder
    //   59: dup
    //   60: invokespecial <init> : ()V
    //   63: astore #4
    //   65: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   68: aload_0
    //   69: invokevirtual exec : ([Ljava/lang/String;)Ljava/lang/Process;
    //   72: astore_0
    //   73: new java/io/BufferedReader
    //   76: astore_2
    //   77: new java/io/InputStreamReader
    //   80: astore #5
    //   82: aload #5
    //   84: aload_0
    //   85: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   88: invokespecial <init> : (Ljava/io/InputStream;)V
    //   91: aload_2
    //   92: aload #5
    //   94: invokespecial <init> : (Ljava/io/Reader;)V
    //   97: aload_2
    //   98: invokevirtual readLine : ()Ljava/lang/String;
    //   101: astore #5
    //   103: aload #5
    //   105: ifnull -> 296
    //   108: aload #4
    //   110: aload #5
    //   112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: ldc '\\n'
    //   117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: iload_1
    //   122: ifle -> 97
    //   125: aload #4
    //   127: invokevirtual length : ()I
    //   130: iload_1
    //   131: if_icmple -> 97
    //   134: aload #4
    //   136: iconst_0
    //   137: aload #4
    //   139: invokevirtual length : ()I
    //   142: iload_1
    //   143: isub
    //   144: invokevirtual delete : (II)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: goto -> 97
    //   151: astore #5
    //   153: aload_0
    //   154: astore_2
    //   155: aload #5
    //   157: invokestatic a : (Ljava/lang/Throwable;)Z
    //   160: ifne -> 170
    //   163: aload_0
    //   164: astore_2
    //   165: aload #5
    //   167: invokevirtual printStackTrace : ()V
    //   170: aload_0
    //   171: astore_2
    //   172: new java/lang/StringBuilder
    //   175: astore_3
    //   176: aload_0
    //   177: astore_2
    //   178: aload_3
    //   179: ldc '\\n[error:'
    //   181: invokespecial <init> : (Ljava/lang/String;)V
    //   184: aload_0
    //   185: astore_2
    //   186: aload #4
    //   188: aload_3
    //   189: aload #5
    //   191: invokevirtual toString : ()Ljava/lang/String;
    //   194: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: ldc ']'
    //   199: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: invokevirtual toString : ()Ljava/lang/String;
    //   205: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: invokevirtual toString : ()Ljava/lang/String;
    //   211: astore #5
    //   213: aload #5
    //   215: astore_2
    //   216: aload_0
    //   217: ifnull -> 21
    //   220: aload_0
    //   221: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   224: invokevirtual close : ()V
    //   227: aload_0
    //   228: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   231: invokevirtual close : ()V
    //   234: aload_0
    //   235: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   238: invokevirtual close : ()V
    //   241: aload #5
    //   243: astore_2
    //   244: goto -> 21
    //   247: astore_0
    //   248: aload_0
    //   249: invokevirtual printStackTrace : ()V
    //   252: aload #5
    //   254: astore_2
    //   255: goto -> 21
    //   258: bipush #6
    //   260: anewarray java/lang/String
    //   263: astore_0
    //   264: aload_0
    //   265: iconst_0
    //   266: ldc 'logcat'
    //   268: aastore
    //   269: aload_0
    //   270: iconst_1
    //   271: ldc '-d'
    //   273: aastore
    //   274: aload_0
    //   275: iconst_2
    //   276: ldc '-v'
    //   278: aastore
    //   279: aload_0
    //   280: iconst_3
    //   281: ldc 'threadtime'
    //   283: aastore
    //   284: aload_0
    //   285: iconst_4
    //   286: ldc '-s'
    //   288: aastore
    //   289: aload_0
    //   290: iconst_5
    //   291: aload_2
    //   292: aastore
    //   293: goto -> 52
    //   296: aload #4
    //   298: invokevirtual toString : ()Ljava/lang/String;
    //   301: astore #5
    //   303: aload #5
    //   305: astore_2
    //   306: aload_0
    //   307: ifnull -> 21
    //   310: aload_0
    //   311: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   314: invokevirtual close : ()V
    //   317: aload_0
    //   318: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   321: invokevirtual close : ()V
    //   324: aload_0
    //   325: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   328: invokevirtual close : ()V
    //   331: aload #5
    //   333: astore_2
    //   334: goto -> 21
    //   337: astore_0
    //   338: aload_0
    //   339: invokevirtual printStackTrace : ()V
    //   342: aload #5
    //   344: astore_2
    //   345: goto -> 21
    //   348: astore_2
    //   349: aload_2
    //   350: invokevirtual printStackTrace : ()V
    //   353: goto -> 317
    //   356: astore_2
    //   357: aload_2
    //   358: invokevirtual printStackTrace : ()V
    //   361: goto -> 324
    //   364: astore_2
    //   365: aload_2
    //   366: invokevirtual printStackTrace : ()V
    //   369: goto -> 227
    //   372: astore_2
    //   373: aload_2
    //   374: invokevirtual printStackTrace : ()V
    //   377: goto -> 234
    //   380: astore_0
    //   381: aload_2
    //   382: ifnull -> 406
    //   385: aload_2
    //   386: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   389: invokevirtual close : ()V
    //   392: aload_2
    //   393: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   396: invokevirtual close : ()V
    //   399: aload_2
    //   400: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   403: invokevirtual close : ()V
    //   406: aload_0
    //   407: athrow
    //   408: astore #5
    //   410: aload #5
    //   412: invokevirtual printStackTrace : ()V
    //   415: goto -> 392
    //   418: astore #5
    //   420: aload #5
    //   422: invokevirtual printStackTrace : ()V
    //   425: goto -> 399
    //   428: astore_2
    //   429: aload_2
    //   430: invokevirtual printStackTrace : ()V
    //   433: goto -> 406
    //   436: astore_2
    //   437: aload_0
    //   438: astore #5
    //   440: aload_2
    //   441: astore_0
    //   442: aload #5
    //   444: astore_2
    //   445: goto -> 381
    //   448: astore #5
    //   450: aload_3
    //   451: astore_0
    //   452: goto -> 153
    // Exception table:
    //   from	to	target	type
    //   65	73	448	java/lang/Throwable
    //   65	73	380	finally
    //   73	97	151	java/lang/Throwable
    //   73	97	436	finally
    //   97	103	151	java/lang/Throwable
    //   97	103	436	finally
    //   108	121	151	java/lang/Throwable
    //   108	121	436	finally
    //   125	148	151	java/lang/Throwable
    //   125	148	436	finally
    //   155	163	380	finally
    //   165	170	380	finally
    //   172	176	380	finally
    //   178	184	380	finally
    //   186	213	380	finally
    //   220	227	364	java/io/IOException
    //   227	234	372	java/io/IOException
    //   234	241	247	java/io/IOException
    //   296	303	151	java/lang/Throwable
    //   296	303	436	finally
    //   310	317	348	java/io/IOException
    //   317	324	356	java/io/IOException
    //   324	331	337	java/io/IOException
    //   385	392	408	java/io/IOException
    //   392	399	418	java/io/IOException
    //   399	406	428	java/io/IOException
  }
  
  public static String a(Context paramContext, String paramString) {
    if (paramString == null || paramString.trim().equals(""))
      return ""; 
    if (a == null) {
      a = new HashMap<String, String>();
      ArrayList<String> arrayList = a(paramContext, new String[] { "/system/bin/sh", "-c", "getprop" });
      if (arrayList != null && arrayList.size() > 0) {
        x.b(z.class, "Successfully get 'getprop' list.", new Object[0]);
        Pattern pattern = Pattern.compile("\\[(.+)\\]: \\[(.*)\\]");
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
          Matcher matcher = pattern.matcher(iterator.next());
          if (matcher.find())
            a.put(matcher.group(1), matcher.group(2)); 
        } 
        x.b(z.class, "System properties number: %d.", new Object[] { Integer.valueOf(a.size()) });
      } 
    } 
    return a.containsKey(paramString) ? a.get(paramString) : "fail";
  }
  
  public static String a(File paramFile) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_1
    //   3: astore_2
    //   4: aload_0
    //   5: ifnull -> 26
    //   8: aload_1
    //   9: astore_2
    //   10: aload_0
    //   11: invokevirtual exists : ()Z
    //   14: ifeq -> 26
    //   17: aload_0
    //   18: invokevirtual canRead : ()Z
    //   21: ifne -> 28
    //   24: aload_1
    //   25: astore_2
    //   26: aload_2
    //   27: areturn
    //   28: new java/lang/StringBuilder
    //   31: astore_2
    //   32: aload_2
    //   33: invokespecial <init> : ()V
    //   36: new java/io/BufferedReader
    //   39: astore_3
    //   40: new java/io/InputStreamReader
    //   43: astore #4
    //   45: new java/io/FileInputStream
    //   48: astore #5
    //   50: aload #5
    //   52: aload_0
    //   53: invokespecial <init> : (Ljava/io/File;)V
    //   56: aload #4
    //   58: aload #5
    //   60: ldc_w 'utf-8'
    //   63: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   66: aload_3
    //   67: aload #4
    //   69: invokespecial <init> : (Ljava/io/Reader;)V
    //   72: aload_3
    //   73: astore_0
    //   74: aload_3
    //   75: invokevirtual readLine : ()Ljava/lang/String;
    //   78: astore #4
    //   80: aload #4
    //   82: ifnull -> 140
    //   85: aload_3
    //   86: astore_0
    //   87: aload_2
    //   88: aload #4
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: aload_3
    //   95: astore_0
    //   96: aload_2
    //   97: ldc '\\n'
    //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: goto -> 72
    //   106: astore_2
    //   107: aload_3
    //   108: astore_0
    //   109: aload_2
    //   110: invokestatic a : (Ljava/lang/Throwable;)Z
    //   113: pop
    //   114: aload_1
    //   115: astore_2
    //   116: aload_3
    //   117: ifnull -> 26
    //   120: aload_3
    //   121: invokevirtual close : ()V
    //   124: aload_1
    //   125: astore_2
    //   126: goto -> 26
    //   129: astore_0
    //   130: aload_0
    //   131: invokestatic a : (Ljava/lang/Throwable;)Z
    //   134: pop
    //   135: aload_1
    //   136: astore_2
    //   137: goto -> 26
    //   140: aload_3
    //   141: astore_0
    //   142: aload_2
    //   143: invokevirtual toString : ()Ljava/lang/String;
    //   146: astore_2
    //   147: aload_3
    //   148: invokevirtual close : ()V
    //   151: goto -> 26
    //   154: astore_0
    //   155: aload_0
    //   156: invokestatic a : (Ljava/lang/Throwable;)Z
    //   159: pop
    //   160: goto -> 26
    //   163: astore_3
    //   164: aconst_null
    //   165: astore_0
    //   166: aload_0
    //   167: ifnull -> 174
    //   170: aload_0
    //   171: invokevirtual close : ()V
    //   174: aload_3
    //   175: athrow
    //   176: astore_0
    //   177: aload_0
    //   178: invokestatic a : (Ljava/lang/Throwable;)Z
    //   181: pop
    //   182: goto -> 174
    //   185: astore_3
    //   186: goto -> 166
    //   189: astore_2
    //   190: aconst_null
    //   191: astore_3
    //   192: goto -> 107
    // Exception table:
    //   from	to	target	type
    //   28	72	189	java/lang/Throwable
    //   28	72	163	finally
    //   74	80	106	java/lang/Throwable
    //   74	80	185	finally
    //   87	94	106	java/lang/Throwable
    //   87	94	185	finally
    //   96	103	106	java/lang/Throwable
    //   96	103	185	finally
    //   109	114	185	finally
    //   120	124	129	java/lang/Exception
    //   142	147	106	java/lang/Throwable
    //   142	147	185	finally
    //   147	151	154	java/lang/Exception
    //   170	174	176	java/lang/Exception
  }
  
  public static String a(Throwable paramThrowable) {
    if (paramThrowable == null)
      return ""; 
    try {
      StringWriter stringWriter = new StringWriter();
      this();
      PrintWriter printWriter = new PrintWriter();
      this(stringWriter);
      paramThrowable.printStackTrace(printWriter);
      String str = stringWriter.getBuffer().toString();
    } catch (Throwable throwable) {}
    return (String)throwable;
  }
  
  public static String a(Date paramDate) {
    String str;
    if (paramDate == null)
      return null; 
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
      this("yyyy-MM-dd HH:mm:ss", Locale.US);
      str = simpleDateFormat.format(paramDate);
    } catch (Exception exception) {
      str = (new Date()).toString();
    } 
    return str;
  }
  
  public static String a(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return ""; 
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
      if (str.length() == 1)
        stringBuffer.append("0"); 
      stringBuffer.append(str);
    } 
    return stringBuffer.toString().toUpperCase();
  }
  
  public static Thread a(Runnable paramRunnable, String paramString) {
    try {
      Thread thread = new Thread();
      this(paramRunnable);
      thread.setName(paramString);
      thread.start();
      paramRunnable = thread;
    } catch (Throwable throwable) {
      x.e("[Util] Failed to start a thread to execute task with message: %s", new Object[] { throwable.getMessage() });
      throwable = null;
    } 
    return (Thread)throwable;
  }
  
  public static ArrayList<String> a(Context paramContext, String[] paramArrayOfString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore_3
    //   10: aload_0
    //   11: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   14: invokevirtual J : ()Z
    //   17: ifeq -> 45
    //   20: new java/util/ArrayList
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: astore_0
    //   28: aload_0
    //   29: new java/lang/String
    //   32: dup
    //   33: ldc_w 'unknown(low memory)'
    //   36: invokespecial <init> : (Ljava/lang/String;)V
    //   39: invokevirtual add : (Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_0
    //   44: areturn
    //   45: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   48: aload_1
    //   49: invokevirtual exec : ([Ljava/lang/String;)Ljava/lang/Process;
    //   52: astore_0
    //   53: new java/io/BufferedReader
    //   56: astore_1
    //   57: new java/io/InputStreamReader
    //   60: astore #4
    //   62: aload #4
    //   64: aload_0
    //   65: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   68: invokespecial <init> : (Ljava/io/InputStream;)V
    //   71: aload_1
    //   72: aload #4
    //   74: invokespecial <init> : (Ljava/io/Reader;)V
    //   77: aload_1
    //   78: invokevirtual readLine : ()Ljava/lang/String;
    //   81: astore #4
    //   83: aload #4
    //   85: ifnull -> 143
    //   88: aload_3
    //   89: aload #4
    //   91: invokevirtual add : (Ljava/lang/Object;)Z
    //   94: pop
    //   95: goto -> 77
    //   98: astore_3
    //   99: aconst_null
    //   100: astore_0
    //   101: aload_0
    //   102: astore_2
    //   103: aload_1
    //   104: astore #4
    //   106: aload_3
    //   107: invokestatic a : (Ljava/lang/Throwable;)Z
    //   110: ifne -> 122
    //   113: aload_0
    //   114: astore_2
    //   115: aload_1
    //   116: astore #4
    //   118: aload_3
    //   119: invokevirtual printStackTrace : ()V
    //   122: aload_1
    //   123: ifnull -> 130
    //   126: aload_1
    //   127: invokevirtual close : ()V
    //   130: aload_0
    //   131: ifnull -> 138
    //   134: aload_0
    //   135: invokevirtual close : ()V
    //   138: aconst_null
    //   139: astore_0
    //   140: goto -> 43
    //   143: new java/io/InputStreamReader
    //   146: astore #4
    //   148: aload #4
    //   150: aload_0
    //   151: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   154: invokespecial <init> : (Ljava/io/InputStream;)V
    //   157: new java/io/BufferedReader
    //   160: dup
    //   161: aload #4
    //   163: invokespecial <init> : (Ljava/io/Reader;)V
    //   166: astore_0
    //   167: aload_0
    //   168: astore_2
    //   169: aload_1
    //   170: astore #4
    //   172: aload_0
    //   173: invokevirtual readLine : ()Ljava/lang/String;
    //   176: astore #5
    //   178: aload #5
    //   180: ifnull -> 202
    //   183: aload_0
    //   184: astore_2
    //   185: aload_1
    //   186: astore #4
    //   188: aload_3
    //   189: aload #5
    //   191: invokevirtual add : (Ljava/lang/Object;)Z
    //   194: pop
    //   195: goto -> 167
    //   198: astore_3
    //   199: goto -> 101
    //   202: aload_1
    //   203: invokevirtual close : ()V
    //   206: aload_0
    //   207: invokevirtual close : ()V
    //   210: aload_3
    //   211: astore_0
    //   212: goto -> 43
    //   215: astore_0
    //   216: aload_0
    //   217: invokevirtual printStackTrace : ()V
    //   220: aload_3
    //   221: astore_0
    //   222: goto -> 43
    //   225: astore_1
    //   226: aload_1
    //   227: invokevirtual printStackTrace : ()V
    //   230: goto -> 206
    //   233: astore_1
    //   234: aload_1
    //   235: invokevirtual printStackTrace : ()V
    //   238: goto -> 130
    //   241: astore_0
    //   242: aload_0
    //   243: invokevirtual printStackTrace : ()V
    //   246: goto -> 138
    //   249: astore_0
    //   250: aconst_null
    //   251: astore_1
    //   252: aload_1
    //   253: ifnull -> 260
    //   256: aload_1
    //   257: invokevirtual close : ()V
    //   260: aload_2
    //   261: ifnull -> 268
    //   264: aload_2
    //   265: invokevirtual close : ()V
    //   268: aload_0
    //   269: athrow
    //   270: astore_1
    //   271: aload_1
    //   272: invokevirtual printStackTrace : ()V
    //   275: goto -> 260
    //   278: astore_1
    //   279: aload_1
    //   280: invokevirtual printStackTrace : ()V
    //   283: goto -> 268
    //   286: astore_0
    //   287: goto -> 252
    //   290: astore_0
    //   291: aload #4
    //   293: astore_1
    //   294: goto -> 252
    //   297: astore_3
    //   298: aconst_null
    //   299: astore_0
    //   300: aconst_null
    //   301: astore_1
    //   302: goto -> 101
    // Exception table:
    //   from	to	target	type
    //   45	77	297	java/lang/Throwable
    //   45	77	249	finally
    //   77	83	98	java/lang/Throwable
    //   77	83	286	finally
    //   88	95	98	java/lang/Throwable
    //   88	95	286	finally
    //   106	113	290	finally
    //   118	122	290	finally
    //   126	130	233	java/io/IOException
    //   134	138	241	java/io/IOException
    //   143	167	98	java/lang/Throwable
    //   143	167	286	finally
    //   172	178	198	java/lang/Throwable
    //   172	178	290	finally
    //   188	195	198	java/lang/Throwable
    //   188	195	290	finally
    //   202	206	225	java/io/IOException
    //   206	210	215	java/io/IOException
    //   256	260	270	java/io/IOException
    //   264	268	278	java/io/IOException
  }
  
  public static Map<String, String> a(int paramInt, boolean paramBoolean) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(12);
    Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
    if (map == null)
      return null; 
    Thread.currentThread().getId();
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Map.Entry> iterator = map.entrySet().iterator();
    label23: while (true) {
      if (iterator.hasNext()) {
        Map.Entry entry = iterator.next();
        stringBuilder.setLength(0);
        if (entry.getValue() != null && ((StackTraceElement[])entry.getValue()).length != 0) {
          StackTraceElement[] arrayOfStackTraceElement = (StackTraceElement[])entry.getValue();
          int i = arrayOfStackTraceElement.length;
          byte b = 0;
          while (true) {
            if (b < i) {
              StackTraceElement stackTraceElement = arrayOfStackTraceElement[b];
              if (paramInt > 0 && stringBuilder.length() >= paramInt) {
                stringBuilder.append("\n[Stack over limit size :" + paramInt + " , has been cutted !]");
              } else {
                stringBuilder.append(stackTraceElement.toString()).append("\n");
                b++;
                continue;
              } 
            } 
            hashMap.put(((Thread)entry.getKey()).getName() + "(" + ((Thread)entry.getKey()).getId() + ")", stringBuilder.toString());
            continue label23;
          } 
          break;
        } 
        continue;
      } 
      return (Map)hashMap;
    } 
  }
  
  public static Map<String, PlugInBean> a(Parcel paramParcel) {
    Bundle bundle = null;
    null = paramParcel.readBundle();
    if (null == null)
      return (Map<String, PlugInBean>)bundle; 
    ArrayList<String> arrayList1 = new ArrayList();
    ArrayList<PlugInBean> arrayList = new ArrayList();
    int i = ((Integer)null.get("pluginNum")).intValue();
    byte b;
    for (b = 0; b < i; b++)
      arrayList1.add(null.getString("pluginKey" + b)); 
    for (b = 0; b < i; b++) {
      String str1 = null.getString("pluginVal" + b + "plugInId");
      String str2 = null.getString("pluginVal" + b + "plugInUUID");
      arrayList.add(new PlugInBean(str1, null.getString("pluginVal" + b + "plugInVersion"), str2));
    } 
    if (arrayList1.size() == arrayList.size()) {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>(arrayList1.size());
      b = 0;
      while (true) {
        if (b < arrayList1.size()) {
          hashMap.put(arrayList1.get(b), PlugInBean.class.cast(arrayList.get(b)));
          b++;
          continue;
        } 
        return (Map)hashMap;
      } 
    } 
    x.e("map plugin parcel error!", new Object[0]);
    return null;
  }
  
  public static void a(Parcel paramParcel, Map<String, PlugInBean> paramMap) {
    boolean bool = false;
    if (paramMap == null || paramMap.size() <= 0) {
      paramParcel.writeBundle(null);
      return;
    } 
    int i = paramMap.size();
    ArrayList<String> arrayList = new ArrayList(i);
    ArrayList arrayList1 = new ArrayList(i);
    for (Map.Entry<String, PlugInBean> entry : paramMap.entrySet()) {
      arrayList.add(entry.getKey());
      arrayList1.add(entry.getValue());
    } 
    Bundle bundle = new Bundle();
    bundle.putInt("pluginNum", arrayList.size());
    byte b = 0;
    while (true) {
      i = bool;
      if (b < arrayList.size()) {
        bundle.putString("pluginKey" + b, arrayList.get(b));
        b++;
        continue;
      } 
      break;
    } 
    while (i < arrayList.size()) {
      bundle.putString("pluginVal" + i + "plugInId", ((PlugInBean)arrayList1.get(i)).a);
      bundle.putString("pluginVal" + i + "plugInUUID", ((PlugInBean)arrayList1.get(i)).c);
      bundle.putString("pluginVal" + i + "plugInVersion", ((PlugInBean)arrayList1.get(i)).b);
      i++;
    } 
    paramParcel.writeBundle(bundle);
  }
  
  public static void a(Class<?> paramClass, String paramString, Object paramObject1, Object paramObject2) {
    try {
      Field field = paramClass.getDeclaredField(paramString);
      field.setAccessible(true);
      field.set((Object)null, paramObject1);
    } catch (Exception exception) {}
  }
  
  public static boolean a(Context paramContext, String paramString, long paramLong) {
    boolean bool = false;
    x.c("[Util] try to lock file:%s (pid=%d | tid=%d)", new Object[] { paramString, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      String str = stringBuilder.append(paramContext.getFilesDir()).append(File.separator).append(paramString).toString();
      File file = new File();
      this(str);
      if (file.exists()) {
        if (System.currentTimeMillis() - file.lastModified() < paramLong)
          return bool; 
        x.c("[Util] lock file(%s) is expired, unlock it", new Object[] { paramString });
        b(paramContext, paramString);
      } 
      if (file.createNewFile()) {
        x.c("[Util] successfully locked file:%s (pid=%d | tid=%d)", new Object[] { paramString, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
        return true;
      } 
      x.c("[Util] Failed to locked file:%s (pid=%d | tid=%d)", new Object[] { paramString, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    } catch (Throwable throwable) {
      x.a(throwable);
    } 
    return bool;
  }
  
  public static boolean a(File paramFile1, File paramFile2, int paramInt) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: ldc_w 'rqdp{  ZF start}'
    //   8: iconst_0
    //   9: anewarray java/lang/Object
    //   12: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   15: pop
    //   16: aload_0
    //   17: ifnull -> 32
    //   20: aload_1
    //   21: ifnull -> 32
    //   24: aload_0
    //   25: aload_1
    //   26: invokevirtual equals : (Ljava/lang/Object;)Z
    //   29: ifeq -> 50
    //   32: ldc_w 'rqdp{  err ZF 1R!}'
    //   35: iconst_0
    //   36: anewarray java/lang/Object
    //   39: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   42: pop
    //   43: iload #4
    //   45: istore #5
    //   47: iload #5
    //   49: ireturn
    //   50: aload_0
    //   51: invokevirtual exists : ()Z
    //   54: ifeq -> 64
    //   57: aload_0
    //   58: invokevirtual canRead : ()Z
    //   61: ifne -> 82
    //   64: ldc_w 'rqdp{  !sFile.exists() || !sFile.canRead(),pls check ,return!}'
    //   67: iconst_0
    //   68: anewarray java/lang/Object
    //   71: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   74: pop
    //   75: iload #4
    //   77: istore #5
    //   79: goto -> 47
    //   82: aload_1
    //   83: invokevirtual getParentFile : ()Ljava/io/File;
    //   86: ifnull -> 107
    //   89: aload_1
    //   90: invokevirtual getParentFile : ()Ljava/io/File;
    //   93: invokevirtual exists : ()Z
    //   96: ifne -> 107
    //   99: aload_1
    //   100: invokevirtual getParentFile : ()Ljava/io/File;
    //   103: invokevirtual mkdirs : ()Z
    //   106: pop
    //   107: aload_1
    //   108: invokevirtual exists : ()Z
    //   111: ifne -> 119
    //   114: aload_1
    //   115: invokevirtual createNewFile : ()Z
    //   118: pop
    //   119: iload #4
    //   121: istore #5
    //   123: aload_1
    //   124: invokevirtual exists : ()Z
    //   127: ifeq -> 47
    //   130: iload #4
    //   132: istore #5
    //   134: aload_1
    //   135: invokevirtual canRead : ()Z
    //   138: ifeq -> 47
    //   141: new java/io/FileInputStream
    //   144: astore #6
    //   146: aload #6
    //   148: aload_0
    //   149: invokespecial <init> : (Ljava/io/File;)V
    //   152: new java/util/zip/ZipOutputStream
    //   155: astore_3
    //   156: new java/io/BufferedOutputStream
    //   159: astore #7
    //   161: new java/io/FileOutputStream
    //   164: astore #8
    //   166: aload #8
    //   168: aload_1
    //   169: invokespecial <init> : (Ljava/io/File;)V
    //   172: aload #7
    //   174: aload #8
    //   176: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   179: aload_3
    //   180: aload #7
    //   182: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   185: aload_3
    //   186: bipush #8
    //   188: invokevirtual setMethod : (I)V
    //   191: new java/util/zip/ZipEntry
    //   194: astore_1
    //   195: aload_1
    //   196: aload_0
    //   197: invokevirtual getName : ()Ljava/lang/String;
    //   200: invokespecial <init> : (Ljava/lang/String;)V
    //   203: aload_3
    //   204: aload_1
    //   205: invokevirtual putNextEntry : (Ljava/util/zip/ZipEntry;)V
    //   208: sipush #5000
    //   211: newarray byte
    //   213: astore_0
    //   214: aload #6
    //   216: aload_0
    //   217: invokevirtual read : ([B)I
    //   220: istore_2
    //   221: iload_2
    //   222: ifle -> 311
    //   225: aload_3
    //   226: aload_0
    //   227: iconst_0
    //   228: iload_2
    //   229: invokevirtual write : ([BII)V
    //   232: goto -> 214
    //   235: astore #7
    //   237: aload #6
    //   239: astore_1
    //   240: aload_3
    //   241: astore_0
    //   242: aload #7
    //   244: astore #6
    //   246: aload #6
    //   248: invokestatic a : (Ljava/lang/Throwable;)Z
    //   251: ifne -> 259
    //   254: aload #6
    //   256: invokevirtual printStackTrace : ()V
    //   259: aload_1
    //   260: ifnull -> 267
    //   263: aload_1
    //   264: invokevirtual close : ()V
    //   267: aload_0
    //   268: ifnull -> 275
    //   271: aload_0
    //   272: invokevirtual close : ()V
    //   275: ldc_w 'rqdp{  ZF end}'
    //   278: iconst_0
    //   279: anewarray java/lang/Object
    //   282: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   285: pop
    //   286: iload #4
    //   288: istore #5
    //   290: goto -> 47
    //   293: astore #6
    //   295: aload #6
    //   297: invokestatic a : (Ljava/lang/Throwable;)Z
    //   300: ifne -> 119
    //   303: aload #6
    //   305: invokevirtual printStackTrace : ()V
    //   308: goto -> 119
    //   311: aload_3
    //   312: invokevirtual flush : ()V
    //   315: aload_3
    //   316: invokevirtual closeEntry : ()V
    //   319: aload #6
    //   321: invokevirtual close : ()V
    //   324: aload_3
    //   325: invokevirtual close : ()V
    //   328: ldc_w 'rqdp{  ZF end}'
    //   331: iconst_0
    //   332: anewarray java/lang/Object
    //   335: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   338: pop
    //   339: iconst_1
    //   340: istore #5
    //   342: goto -> 47
    //   345: astore_0
    //   346: aload_0
    //   347: invokevirtual printStackTrace : ()V
    //   350: goto -> 324
    //   353: astore_0
    //   354: aload_0
    //   355: invokevirtual printStackTrace : ()V
    //   358: goto -> 328
    //   361: astore_1
    //   362: aload_1
    //   363: invokevirtual printStackTrace : ()V
    //   366: goto -> 267
    //   369: astore_0
    //   370: aload_0
    //   371: invokevirtual printStackTrace : ()V
    //   374: goto -> 275
    //   377: astore_1
    //   378: aconst_null
    //   379: astore_0
    //   380: aconst_null
    //   381: astore #6
    //   383: aload #6
    //   385: ifnull -> 393
    //   388: aload #6
    //   390: invokevirtual close : ()V
    //   393: aload_0
    //   394: ifnull -> 401
    //   397: aload_0
    //   398: invokevirtual close : ()V
    //   401: ldc_w 'rqdp{  ZF end}'
    //   404: iconst_0
    //   405: anewarray java/lang/Object
    //   408: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   411: pop
    //   412: aload_1
    //   413: athrow
    //   414: astore #6
    //   416: aload #6
    //   418: invokevirtual printStackTrace : ()V
    //   421: goto -> 393
    //   424: astore_0
    //   425: aload_0
    //   426: invokevirtual printStackTrace : ()V
    //   429: goto -> 401
    //   432: astore_1
    //   433: aconst_null
    //   434: astore_0
    //   435: goto -> 383
    //   438: astore_1
    //   439: aload_3
    //   440: astore_0
    //   441: goto -> 383
    //   444: astore_3
    //   445: aload_1
    //   446: astore #6
    //   448: aload_3
    //   449: astore_1
    //   450: goto -> 383
    //   453: astore #6
    //   455: aconst_null
    //   456: astore_0
    //   457: aload_3
    //   458: astore_1
    //   459: goto -> 246
    //   462: astore_0
    //   463: aconst_null
    //   464: astore_3
    //   465: aload #6
    //   467: astore_1
    //   468: aload_0
    //   469: astore #6
    //   471: aload_3
    //   472: astore_0
    //   473: goto -> 246
    // Exception table:
    //   from	to	target	type
    //   82	107	293	java/lang/Throwable
    //   107	119	293	java/lang/Throwable
    //   141	152	453	java/lang/Throwable
    //   141	152	377	finally
    //   152	185	462	java/lang/Throwable
    //   152	185	432	finally
    //   185	214	235	java/lang/Throwable
    //   185	214	438	finally
    //   214	221	235	java/lang/Throwable
    //   214	221	438	finally
    //   225	232	235	java/lang/Throwable
    //   225	232	438	finally
    //   246	259	444	finally
    //   263	267	361	java/io/IOException
    //   271	275	369	java/io/IOException
    //   311	319	235	java/lang/Throwable
    //   311	319	438	finally
    //   319	324	345	java/io/IOException
    //   324	328	353	java/io/IOException
    //   388	393	414	java/io/IOException
    //   397	401	424	java/io/IOException
  }
  
  public static boolean a(Runnable paramRunnable) {
    if (paramRunnable != null) {
      w w = w.a();
      if (w != null)
        return w.a(paramRunnable); 
      String[] arrayOfString = paramRunnable.getClass().getName().split("\\.");
      if (a(paramRunnable, arrayOfString[arrayOfString.length - 1]) != null)
        return true; 
    } 
    return false;
  }
  
  public static boolean a(String paramString) {
    return !(paramString != null && paramString.trim().length() > 0);
  }
  
  public static byte[] a(int paramInt) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/z
    //   2: monitorenter
    //   3: bipush #16
    //   5: newarray byte
    //   7: astore_1
    //   8: new java/io/DataInputStream
    //   11: astore_2
    //   12: new java/io/FileInputStream
    //   15: astore_3
    //   16: new java/io/File
    //   19: astore #4
    //   21: aload #4
    //   23: ldc_w '/dev/urandom'
    //   26: invokespecial <init> : (Ljava/lang/String;)V
    //   29: aload_3
    //   30: aload #4
    //   32: invokespecial <init> : (Ljava/io/File;)V
    //   35: aload_2
    //   36: aload_3
    //   37: invokespecial <init> : (Ljava/io/InputStream;)V
    //   40: aload_2
    //   41: astore #4
    //   43: aload_2
    //   44: aload_1
    //   45: invokevirtual readFully : ([B)V
    //   48: aload_2
    //   49: invokevirtual close : ()V
    //   52: aload_1
    //   53: astore #4
    //   55: ldc com/tencent/bugly/proguard/z
    //   57: monitorexit
    //   58: aload #4
    //   60: areturn
    //   61: astore_1
    //   62: aconst_null
    //   63: astore_2
    //   64: aload_2
    //   65: astore #4
    //   67: ldc_w 'Failed to read from /dev/urandom : %s'
    //   70: iconst_1
    //   71: anewarray java/lang/Object
    //   74: dup
    //   75: iconst_0
    //   76: aload_1
    //   77: aastore
    //   78: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   81: pop
    //   82: aload_2
    //   83: ifnull -> 90
    //   86: aload_2
    //   87: invokevirtual close : ()V
    //   90: ldc_w 'AES'
    //   93: invokestatic getInstance : (Ljava/lang/String;)Ljavax/crypto/KeyGenerator;
    //   96: astore #4
    //   98: new java/security/SecureRandom
    //   101: astore_2
    //   102: aload_2
    //   103: invokespecial <init> : ()V
    //   106: aload #4
    //   108: sipush #128
    //   111: aload_2
    //   112: invokevirtual init : (ILjava/security/SecureRandom;)V
    //   115: aload #4
    //   117: invokevirtual generateKey : ()Ljavax/crypto/SecretKey;
    //   120: invokeinterface getEncoded : ()[B
    //   125: astore #4
    //   127: goto -> 55
    //   130: astore #4
    //   132: aconst_null
    //   133: astore_2
    //   134: aload #4
    //   136: astore_1
    //   137: aload_2
    //   138: ifnull -> 145
    //   141: aload_2
    //   142: invokevirtual close : ()V
    //   145: aload_1
    //   146: athrow
    //   147: astore #4
    //   149: aload #4
    //   151: invokestatic b : (Ljava/lang/Throwable;)Z
    //   154: ifne -> 162
    //   157: aload #4
    //   159: invokevirtual printStackTrace : ()V
    //   162: aconst_null
    //   163: astore #4
    //   165: goto -> 55
    //   168: astore #4
    //   170: ldc com/tencent/bugly/proguard/z
    //   172: monitorexit
    //   173: aload #4
    //   175: athrow
    //   176: astore_1
    //   177: aload #4
    //   179: astore_2
    //   180: goto -> 137
    //   183: astore_1
    //   184: goto -> 64
    // Exception table:
    //   from	to	target	type
    //   3	40	61	java/lang/Exception
    //   3	40	130	finally
    //   43	48	183	java/lang/Exception
    //   43	48	176	finally
    //   48	52	147	java/lang/Exception
    //   48	52	168	finally
    //   67	82	176	finally
    //   86	90	147	java/lang/Exception
    //   86	90	168	finally
    //   90	127	147	java/lang/Exception
    //   90	127	168	finally
    //   141	145	147	java/lang/Exception
    //   141	145	168	finally
    //   145	147	147	java/lang/Exception
    //   145	147	168	finally
    //   149	162	168	finally
  }
  
  @TargetApi(19)
  public static byte[] a(int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramArrayOfbyte2, "AES");
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      if (Build.VERSION.SDK_INT < 21 || b) {
        IvParameterSpec ivParameterSpec = new IvParameterSpec();
        this(paramArrayOfbyte2);
        cipher.init(paramInt, secretKeySpec, ivParameterSpec);
      } else {
        int i = cipher.getBlockSize();
        GCMParameterSpec gCMParameterSpec = new GCMParameterSpec();
        this(i << 3, paramArrayOfbyte2);
        try {
          cipher.init(paramInt, secretKeySpec, gCMParameterSpec);
          paramArrayOfbyte1 = cipher.doFinal(paramArrayOfbyte1);
        } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {}
        return (byte[])invalidAlgorithmParameterException;
      } 
      byte[] arrayOfByte = cipher.doFinal((byte[])invalidAlgorithmParameterException);
    } catch (Exception exception) {}
    return (byte[])exception;
  }
  
  public static byte[] a(Parcelable paramParcelable) {
    Parcel parcel = Parcel.obtain();
    paramParcelable.writeToParcel(parcel, 0);
    byte[] arrayOfByte = parcel.marshall();
    parcel.recycle();
    return arrayOfByte;
  }
  
  public static byte[] a(File paramFile, String paramString1, String paramString2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: ldc_w 'rqdp{  ZF start}'
    //   5: iconst_0
    //   6: anewarray java/lang/Object
    //   9: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: ifnull -> 483
    //   17: aload_0
    //   18: invokevirtual exists : ()Z
    //   21: ifeq -> 483
    //   24: aload_0
    //   25: invokevirtual canRead : ()Z
    //   28: ifeq -> 483
    //   31: new java/io/FileInputStream
    //   34: astore_2
    //   35: aload_2
    //   36: aload_0
    //   37: invokespecial <init> : (Ljava/io/File;)V
    //   40: aload_0
    //   41: invokevirtual getName : ()Ljava/lang/String;
    //   44: astore_0
    //   45: aload_2
    //   46: astore #4
    //   48: aload_0
    //   49: astore_2
    //   50: aload #4
    //   52: astore_0
    //   53: aload_1
    //   54: ldc_w 'UTF-8'
    //   57: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   60: astore_1
    //   61: new java/io/ByteArrayInputStream
    //   64: astore #5
    //   66: aload #5
    //   68: aload_1
    //   69: invokespecial <init> : ([B)V
    //   72: new java/io/ByteArrayOutputStream
    //   75: astore #6
    //   77: aload #6
    //   79: invokespecial <init> : ()V
    //   82: new java/util/zip/ZipOutputStream
    //   85: astore_1
    //   86: aload_1
    //   87: aload #6
    //   89: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   92: aload_1
    //   93: astore #4
    //   95: aload_0
    //   96: astore #7
    //   98: aload_1
    //   99: bipush #8
    //   101: invokevirtual setMethod : (I)V
    //   104: aload_1
    //   105: astore #4
    //   107: aload_0
    //   108: astore #7
    //   110: new java/util/zip/ZipEntry
    //   113: astore #8
    //   115: aload_1
    //   116: astore #4
    //   118: aload_0
    //   119: astore #7
    //   121: aload #8
    //   123: aload_2
    //   124: invokespecial <init> : (Ljava/lang/String;)V
    //   127: aload_1
    //   128: astore #4
    //   130: aload_0
    //   131: astore #7
    //   133: aload_1
    //   134: aload #8
    //   136: invokevirtual putNextEntry : (Ljava/util/zip/ZipEntry;)V
    //   139: aload_1
    //   140: astore #4
    //   142: aload_0
    //   143: astore #7
    //   145: sipush #1024
    //   148: newarray byte
    //   150: astore_2
    //   151: aload_0
    //   152: ifnull -> 245
    //   155: aload_1
    //   156: astore #4
    //   158: aload_0
    //   159: astore #7
    //   161: aload_0
    //   162: aload_2
    //   163: invokevirtual read : ([B)I
    //   166: istore #9
    //   168: iload #9
    //   170: ifle -> 245
    //   173: aload_1
    //   174: astore #4
    //   176: aload_0
    //   177: astore #7
    //   179: aload_1
    //   180: aload_2
    //   181: iconst_0
    //   182: iload #9
    //   184: invokevirtual write : ([BII)V
    //   187: goto -> 155
    //   190: astore_2
    //   191: aload_1
    //   192: astore #4
    //   194: aload_0
    //   195: astore #7
    //   197: aload_2
    //   198: invokestatic a : (Ljava/lang/Throwable;)Z
    //   201: ifne -> 214
    //   204: aload_1
    //   205: astore #4
    //   207: aload_0
    //   208: astore #7
    //   210: aload_2
    //   211: invokevirtual printStackTrace : ()V
    //   214: aload_0
    //   215: ifnull -> 222
    //   218: aload_0
    //   219: invokevirtual close : ()V
    //   222: aload_1
    //   223: ifnull -> 230
    //   226: aload_1
    //   227: invokevirtual close : ()V
    //   230: ldc_w 'rqdp{  ZF end}'
    //   233: iconst_0
    //   234: anewarray java/lang/Object
    //   237: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   240: pop
    //   241: aload_3
    //   242: astore_2
    //   243: aload_2
    //   244: areturn
    //   245: aload_1
    //   246: astore #4
    //   248: aload_0
    //   249: astore #7
    //   251: aload #5
    //   253: aload_2
    //   254: invokevirtual read : ([B)I
    //   257: istore #9
    //   259: iload #9
    //   261: ifle -> 317
    //   264: aload_1
    //   265: astore #4
    //   267: aload_0
    //   268: astore #7
    //   270: aload_1
    //   271: aload_2
    //   272: iconst_0
    //   273: iload #9
    //   275: invokevirtual write : ([BII)V
    //   278: goto -> 245
    //   281: astore_1
    //   282: aload #7
    //   284: astore_0
    //   285: aload #4
    //   287: astore_2
    //   288: aload_0
    //   289: ifnull -> 296
    //   292: aload_0
    //   293: invokevirtual close : ()V
    //   296: aload_2
    //   297: ifnull -> 304
    //   300: aload_2
    //   301: invokevirtual close : ()V
    //   304: ldc_w 'rqdp{  ZF end}'
    //   307: iconst_0
    //   308: anewarray java/lang/Object
    //   311: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   314: pop
    //   315: aload_1
    //   316: athrow
    //   317: aload_1
    //   318: astore #4
    //   320: aload_0
    //   321: astore #7
    //   323: aload_1
    //   324: invokevirtual closeEntry : ()V
    //   327: aload_1
    //   328: astore #4
    //   330: aload_0
    //   331: astore #7
    //   333: aload_1
    //   334: invokevirtual flush : ()V
    //   337: aload_1
    //   338: astore #4
    //   340: aload_0
    //   341: astore #7
    //   343: aload_1
    //   344: invokevirtual finish : ()V
    //   347: aload_1
    //   348: astore #4
    //   350: aload_0
    //   351: astore #7
    //   353: aload #6
    //   355: invokevirtual toByteArray : ()[B
    //   358: astore_2
    //   359: aload_0
    //   360: ifnull -> 367
    //   363: aload_0
    //   364: invokevirtual close : ()V
    //   367: aload_1
    //   368: invokevirtual close : ()V
    //   371: ldc_w 'rqdp{  ZF end}'
    //   374: iconst_0
    //   375: anewarray java/lang/Object
    //   378: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   381: pop
    //   382: goto -> 243
    //   385: astore_0
    //   386: aload_0
    //   387: invokevirtual printStackTrace : ()V
    //   390: goto -> 367
    //   393: astore_0
    //   394: aload_0
    //   395: invokevirtual printStackTrace : ()V
    //   398: goto -> 371
    //   401: astore_0
    //   402: aload_0
    //   403: invokevirtual printStackTrace : ()V
    //   406: goto -> 222
    //   409: astore_0
    //   410: aload_0
    //   411: invokevirtual printStackTrace : ()V
    //   414: goto -> 230
    //   417: astore_0
    //   418: aload_0
    //   419: invokevirtual printStackTrace : ()V
    //   422: goto -> 296
    //   425: astore_0
    //   426: aload_0
    //   427: invokevirtual printStackTrace : ()V
    //   430: goto -> 304
    //   433: astore_1
    //   434: aconst_null
    //   435: astore_2
    //   436: aconst_null
    //   437: astore_0
    //   438: goto -> 288
    //   441: astore_1
    //   442: aload_2
    //   443: astore_0
    //   444: aconst_null
    //   445: astore_2
    //   446: goto -> 288
    //   449: astore_1
    //   450: aconst_null
    //   451: astore_2
    //   452: goto -> 288
    //   455: astore_2
    //   456: aconst_null
    //   457: astore_1
    //   458: aconst_null
    //   459: astore_0
    //   460: goto -> 191
    //   463: astore_1
    //   464: aload_2
    //   465: astore_0
    //   466: aconst_null
    //   467: astore #4
    //   469: aload_1
    //   470: astore_2
    //   471: aload #4
    //   473: astore_1
    //   474: goto -> 191
    //   477: astore_2
    //   478: aconst_null
    //   479: astore_1
    //   480: goto -> 191
    //   483: aconst_null
    //   484: astore_0
    //   485: goto -> 53
    // Exception table:
    //   from	to	target	type
    //   17	40	455	java/lang/Throwable
    //   17	40	433	finally
    //   40	45	463	java/lang/Throwable
    //   40	45	441	finally
    //   53	92	477	java/lang/Throwable
    //   53	92	449	finally
    //   98	104	190	java/lang/Throwable
    //   98	104	281	finally
    //   110	115	190	java/lang/Throwable
    //   110	115	281	finally
    //   121	127	190	java/lang/Throwable
    //   121	127	281	finally
    //   133	139	190	java/lang/Throwable
    //   133	139	281	finally
    //   145	151	190	java/lang/Throwable
    //   145	151	281	finally
    //   161	168	190	java/lang/Throwable
    //   161	168	281	finally
    //   179	187	190	java/lang/Throwable
    //   179	187	281	finally
    //   197	204	281	finally
    //   210	214	281	finally
    //   218	222	401	java/io/IOException
    //   226	230	409	java/io/IOException
    //   251	259	190	java/lang/Throwable
    //   251	259	281	finally
    //   270	278	190	java/lang/Throwable
    //   270	278	281	finally
    //   292	296	417	java/io/IOException
    //   300	304	425	java/io/IOException
    //   323	327	190	java/lang/Throwable
    //   323	327	281	finally
    //   333	337	190	java/lang/Throwable
    //   333	337	281	finally
    //   343	347	190	java/lang/Throwable
    //   343	347	281	finally
    //   353	359	190	java/lang/Throwable
    //   353	359	281	finally
    //   363	367	385	java/io/IOException
    //   367	371	393	java/io/IOException
  }
  
  public static byte[] a(byte[] paramArrayOfbyte, int paramInt) {
    String str;
    byte[] arrayOfByte1;
    byte[] arrayOfByte2 = paramArrayOfbyte;
    if (paramArrayOfbyte != null) {
      if (paramInt == -1)
        return paramArrayOfbyte; 
    } else {
      return arrayOfByte2;
    } 
    int i = paramArrayOfbyte.length;
    if (paramInt == 2) {
      str = "Gzip";
    } else {
      str = "zip";
    } 
    x.c("[Util] Zip %d bytes data with type %s", new Object[] { Integer.valueOf(i), str });
    try {
      ab ab = aa.a(paramInt);
      if (ab == null)
        return null; 
      arrayOfByte1 = ab.a(paramArrayOfbyte);
    } catch (Throwable throwable) {}
    return arrayOfByte1;
  }
  
  public static byte[] a(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, String paramString) {
    byte[] arrayOfByte = null;
    if (paramArrayOfbyte == null)
      return arrayOfByte; 
    try {
      paramArrayOfbyte = a(a(paramArrayOfbyte, 2), 1, paramString);
    } catch (Throwable throwable) {
      paramArrayOfbyte = arrayOfByte;
    } 
    return paramArrayOfbyte;
  }
  
  private static byte[] a(byte[] paramArrayOfbyte, int paramInt, String paramString) {
    byte[] arrayOfByte = paramArrayOfbyte;
    if (paramArrayOfbyte != null) {
      if (paramInt == -1)
        return paramArrayOfbyte; 
    } else {
      return arrayOfByte;
    } 
    x.c("rqdp{  enD:} %d %d", new Object[] { Integer.valueOf(paramArrayOfbyte.length), Integer.valueOf(paramInt) });
    try {
      ag ag = a.a(paramInt);
      if (ag == null)
        return null; 
      ag.a(paramString);
      arrayOfByte = ag.b(paramArrayOfbyte);
    } catch (Throwable throwable) {}
    return arrayOfByte;
  }
  
  public static long b() {
    long l;
    try {
      l = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 86400000L;
      int i = TimeZone.getDefault().getRawOffset();
      l = l * 86400000L - i;
    } catch (Throwable throwable) {}
    return l;
  }
  
  private static BufferedReader b(File paramFile) {
    if (paramFile == null || !paramFile.exists() || !paramFile.canRead())
      return null; 
    try {
      BufferedReader bufferedReader2 = new BufferedReader();
      InputStreamReader inputStreamReader = new InputStreamReader();
      FileInputStream fileInputStream = new FileInputStream();
      this(paramFile);
      this(fileInputStream, "utf-8");
      this(inputStreamReader);
      BufferedReader bufferedReader1 = bufferedReader2;
    } catch (Throwable throwable) {
      x.a(throwable);
      throwable = null;
    } 
    return (BufferedReader)throwable;
  }
  
  public static String b(String paramString1, String paramString2) {
    return (a.b() != null && (a.b()).E != null) ? (a.b()).E.getString(paramString1, paramString2) : "";
  }
  
  public static String b(Throwable paramThrowable) {
    if (paramThrowable == null)
      return ""; 
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    paramThrowable.printStackTrace(printWriter);
    printWriter.flush();
    return stringWriter.toString();
  }
  
  public static String b(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length == 0)
      return "NULL"; 
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
      messageDigest.update(paramArrayOfbyte);
      String str = a(messageDigest.digest());
    } catch (Throwable throwable) {}
    return (String)throwable;
  }
  
  public static Map<String, String> b(Parcel paramParcel) {
    Bundle bundle = null;
    byte b = 0;
    null = paramParcel.readBundle();
    if (null == null)
      return (Map<String, String>)bundle; 
    ArrayList arrayList1 = null.getStringArrayList("keys");
    ArrayList arrayList2 = null.getStringArrayList("values");
    if (arrayList1 != null && arrayList2 != null && arrayList1.size() == arrayList2.size()) {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>(arrayList1.size());
      while (true) {
        if (b < arrayList1.size()) {
          hashMap.put(arrayList1.get(b), arrayList2.get(b));
          b++;
          continue;
        } 
        return (Map)hashMap;
      } 
    } 
    x.e("map parcel error!", new Object[0]);
    return null;
  }
  
  public static void b(long paramLong) {
    try {
      Thread.sleep(paramLong);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
  }
  
  public static void b(Parcel paramParcel, Map<String, String> paramMap) {
    if (paramMap == null || paramMap.size() <= 0) {
      paramParcel.writeBundle(null);
      return;
    } 
    int i = paramMap.size();
    ArrayList arrayList1 = new ArrayList(i);
    ArrayList arrayList2 = new ArrayList(i);
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      arrayList1.add(entry.getKey());
      arrayList2.add(entry.getValue());
    } 
    Bundle bundle = new Bundle();
    bundle.putStringArrayList("keys", arrayList1);
    bundle.putStringArrayList("values", arrayList2);
    paramParcel.writeBundle(bundle);
  }
  
  public static void b(String paramString) {
    if (paramString != null) {
      File file = new File(paramString);
      if (file.isFile() && file.exists() && file.canWrite())
        file.delete(); 
    } 
  }
  
  public static boolean b(Context paramContext, String paramString) {
    boolean bool2;
    boolean bool1 = true;
    x.c("[Util] try to unlock file:%s (pid=%d | tid=%d)", new Object[] { paramString, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      String str = stringBuilder.append(paramContext.getFilesDir()).append(File.separator).append(paramString).toString();
      File file = new File();
      this(str);
      bool2 = bool1;
      if (file.exists()) {
        if (file.delete()) {
          x.c("[Util] successfully unlocked file:%s (pid=%d | tid=%d)", new Object[] { paramString, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
          return bool1;
        } 
      } else {
        return bool2;
      } 
      bool2 = false;
    } catch (Throwable throwable) {
      x.a(throwable);
      bool2 = false;
    } 
    return bool2;
  }
  
  public static byte[] b(int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec();
      this(paramArrayOfbyte2);
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(1, publicKey);
      paramArrayOfbyte1 = cipher.doFinal(paramArrayOfbyte1);
    } catch (Exception exception) {}
    return (byte[])exception;
  }
  
  public static byte[] b(byte[] paramArrayOfbyte, int paramInt) {
    String str;
    byte[] arrayOfByte1;
    byte[] arrayOfByte2 = paramArrayOfbyte;
    if (paramArrayOfbyte != null) {
      if (paramInt == -1)
        return paramArrayOfbyte; 
    } else {
      return arrayOfByte2;
    } 
    int i = paramArrayOfbyte.length;
    if (paramInt == 2) {
      str = "Gzip";
    } else {
      str = "zip";
    } 
    x.c("[Util] Unzip %d bytes data with type %s", new Object[] { Integer.valueOf(i), str });
    try {
      ab ab = aa.a(paramInt);
      if (ab == null)
        return null; 
      arrayOfByte1 = ab.b(paramArrayOfbyte);
    } catch (Throwable throwable) {}
    return arrayOfByte1;
  }
  
  public static byte[] b(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, String paramString) {
    try {
      paramArrayOfbyte = b(b(paramArrayOfbyte, 1, paramString), 2);
    } catch (Exception exception) {}
    return (byte[])exception;
  }
  
  private static byte[] b(byte[] paramArrayOfbyte, int paramInt, String paramString) {
    byte[] arrayOfByte = paramArrayOfbyte;
    if (paramArrayOfbyte != null) {
      if (paramInt == -1)
        return paramArrayOfbyte; 
    } else {
      return arrayOfByte;
    } 
    try {
      ag ag = a.a(paramInt);
      if (ag == null)
        return null; 
      ag.a(paramString);
      arrayOfByte = ag.a(paramArrayOfbyte);
    } catch (Throwable throwable) {}
    return arrayOfByte;
  }
  
  public static long c(byte[] paramArrayOfbyte) {
    long l2;
    long l1 = -1L;
    if (paramArrayOfbyte == null)
      return l1; 
    try {
      String str = new String();
      this(paramArrayOfbyte, "utf-8");
      l2 = Long.parseLong(str);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      l2 = l1;
    } 
    return l2;
  }
  
  public static boolean c(String paramString) {
    boolean bool2;
    boolean bool1 = false;
    if (paramString != null && paramString.trim().length() > 0) {
      bool2 = false;
    } else {
      bool2 = true;
    } 
    if (!bool2) {
      if (paramString.length() > 255) {
        x.a("URL(%s)'s length is larger than 255.", new Object[] { paramString });
        return bool1;
      } 
      if (!paramString.toLowerCase().startsWith("http")) {
        x.a("URL(%s) is not start with \"http\".", new Object[] { paramString });
        return bool1;
      } 
      if (!paramString.toLowerCase().contains("qq.com")) {
        x.a("URL(%s) does not contain \"qq.com\".", new Object[] { paramString });
        return bool1;
      } 
      bool1 = true;
    } 
    return bool1;
  }
  
  public static byte[] c(long paramLong) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      byte[] arrayOfByte = stringBuilder.append(paramLong).toString().getBytes("utf-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      unsupportedEncodingException = null;
    } 
    return (byte[])unsupportedEncodingException;
  }
  
  public static String d(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return "null"; 
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      if (b != 0)
        stringBuffer.append(':'); 
      String str1 = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
      String str2 = str1;
      if (str1.length() == 1)
        str2 = "0" + str1; 
      stringBuffer.append(str2);
    } 
    return stringBuffer.toString().toUpperCase();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/z.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */