package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class b {
  private static String a = null;
  
  public static CrashDetailBean a(Context paramContext, String paramString, NativeExceptionHandler paramNativeExceptionHandler) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: ifnull -> 14
    //   6: aload_1
    //   7: ifnull -> 14
    //   10: aload_2
    //   11: ifnonnull -> 28
    //   14: ldc 'get eup record file args error'
    //   16: iconst_0
    //   17: anewarray java/lang/Object
    //   20: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   23: pop
    //   24: aload_3
    //   25: astore_1
    //   26: aload_1
    //   27: areturn
    //   28: new java/io/File
    //   31: dup
    //   32: aload_1
    //   33: ldc 'rqd_record.eup'
    //   35: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   38: astore #4
    //   40: aload_3
    //   41: astore_1
    //   42: aload #4
    //   44: invokevirtual exists : ()Z
    //   47: ifeq -> 26
    //   50: aload_3
    //   51: astore_1
    //   52: aload #4
    //   54: invokevirtual canRead : ()Z
    //   57: ifeq -> 26
    //   60: new java/io/BufferedInputStream
    //   63: astore #5
    //   65: new java/io/FileInputStream
    //   68: astore_1
    //   69: aload_1
    //   70: aload #4
    //   72: invokespecial <init> : (Ljava/io/File;)V
    //   75: aload #5
    //   77: aload_1
    //   78: invokespecial <init> : (Ljava/io/InputStream;)V
    //   81: aload #5
    //   83: astore_1
    //   84: aload #5
    //   86: invokestatic a : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   89: astore #4
    //   91: aload #4
    //   93: ifnull -> 109
    //   96: aload #5
    //   98: astore_1
    //   99: aload #4
    //   101: ldc 'NATIVE_RQD_REPORT'
    //   103: invokevirtual equals : (Ljava/lang/Object;)Z
    //   106: ifne -> 147
    //   109: aload #5
    //   111: astore_1
    //   112: ldc 'record read fail! %s'
    //   114: iconst_1
    //   115: anewarray java/lang/Object
    //   118: dup
    //   119: iconst_0
    //   120: aload #4
    //   122: aastore
    //   123: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   126: pop
    //   127: aload #5
    //   129: invokevirtual close : ()V
    //   132: aload_3
    //   133: astore_1
    //   134: goto -> 26
    //   137: astore_0
    //   138: aload_0
    //   139: invokevirtual printStackTrace : ()V
    //   142: aload_3
    //   143: astore_1
    //   144: goto -> 26
    //   147: aload #5
    //   149: astore_1
    //   150: new java/util/HashMap
    //   153: astore #6
    //   155: aload #5
    //   157: astore_1
    //   158: aload #6
    //   160: invokespecial <init> : ()V
    //   163: aconst_null
    //   164: astore #4
    //   166: aload #5
    //   168: astore_1
    //   169: aload #5
    //   171: invokestatic a : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   174: astore #7
    //   176: aload #7
    //   178: ifnull -> 214
    //   181: aload #4
    //   183: ifnonnull -> 193
    //   186: aload #7
    //   188: astore #4
    //   190: goto -> 166
    //   193: aload #5
    //   195: astore_1
    //   196: aload #6
    //   198: aload #4
    //   200: aload #7
    //   202: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   207: pop
    //   208: aconst_null
    //   209: astore #4
    //   211: goto -> 166
    //   214: aload #4
    //   216: ifnull -> 257
    //   219: aload #5
    //   221: astore_1
    //   222: ldc 'record not pair! drop! %s'
    //   224: iconst_1
    //   225: anewarray java/lang/Object
    //   228: dup
    //   229: iconst_0
    //   230: aload #4
    //   232: aastore
    //   233: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   236: pop
    //   237: aload #5
    //   239: invokevirtual close : ()V
    //   242: aload_3
    //   243: astore_1
    //   244: goto -> 26
    //   247: astore_0
    //   248: aload_0
    //   249: invokevirtual printStackTrace : ()V
    //   252: aload_3
    //   253: astore_1
    //   254: goto -> 26
    //   257: aload #5
    //   259: astore_1
    //   260: aload_0
    //   261: aload #6
    //   263: aload_2
    //   264: invokestatic a : (Landroid/content/Context;Ljava/util/Map;Lcom/tencent/bugly/crashreport/crash/jni/NativeExceptionHandler;)Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;
    //   267: astore_0
    //   268: aload_0
    //   269: astore_1
    //   270: aload #5
    //   272: invokevirtual close : ()V
    //   275: goto -> 26
    //   278: astore_0
    //   279: aload_0
    //   280: invokevirtual printStackTrace : ()V
    //   283: goto -> 26
    //   286: astore_2
    //   287: aconst_null
    //   288: astore_0
    //   289: aload_0
    //   290: astore_1
    //   291: aload_2
    //   292: invokevirtual printStackTrace : ()V
    //   295: aload_3
    //   296: astore_1
    //   297: aload_0
    //   298: ifnull -> 26
    //   301: aload_0
    //   302: invokevirtual close : ()V
    //   305: aload_3
    //   306: astore_1
    //   307: goto -> 26
    //   310: astore_0
    //   311: aload_0
    //   312: invokevirtual printStackTrace : ()V
    //   315: aload_3
    //   316: astore_1
    //   317: goto -> 26
    //   320: astore_0
    //   321: aconst_null
    //   322: astore_1
    //   323: aload_1
    //   324: ifnull -> 331
    //   327: aload_1
    //   328: invokevirtual close : ()V
    //   331: aload_0
    //   332: athrow
    //   333: astore_1
    //   334: aload_1
    //   335: invokevirtual printStackTrace : ()V
    //   338: goto -> 331
    //   341: astore_0
    //   342: goto -> 323
    //   345: astore_2
    //   346: aload #5
    //   348: astore_0
    //   349: goto -> 289
    // Exception table:
    //   from	to	target	type
    //   60	81	286	java/io/IOException
    //   60	81	320	finally
    //   84	91	345	java/io/IOException
    //   84	91	341	finally
    //   99	109	345	java/io/IOException
    //   99	109	341	finally
    //   112	127	345	java/io/IOException
    //   112	127	341	finally
    //   127	132	137	java/io/IOException
    //   150	155	345	java/io/IOException
    //   150	155	341	finally
    //   158	163	345	java/io/IOException
    //   158	163	341	finally
    //   169	176	345	java/io/IOException
    //   169	176	341	finally
    //   196	208	345	java/io/IOException
    //   196	208	341	finally
    //   222	237	345	java/io/IOException
    //   222	237	341	finally
    //   237	242	247	java/io/IOException
    //   260	268	345	java/io/IOException
    //   260	268	341	finally
    //   270	275	278	java/io/IOException
    //   291	295	341	finally
    //   301	305	310	java/io/IOException
    //   327	331	333	java/io/IOException
  }
  
  private static CrashDetailBean a(Context paramContext, Map<String, String> paramMap, NativeExceptionHandler paramNativeExceptionHandler) {
    if (paramMap == null)
      return null; 
    if (a.a(paramContext) == null) {
      x.e("abnormal com info not created", new Object[0]);
      return null;
    } 
    String str = paramMap.get("intStateStr");
    if (str == null || str.trim().length() <= 0) {
      x.e("no intStateStr", new Object[0]);
      return null;
    } 
    Map<String, Integer> map = c(str);
    if (map == null) {
      x.e("parse intSateMap fail", new Object[] { Integer.valueOf(paramMap.size()) });
      return null;
    } 
    try {
      String[] arrayOfString;
      byte[] arrayOfByte;
      ((Integer)map.get("sino")).intValue();
      ((Integer)map.get("sud")).intValue();
      String str1 = paramMap.get("soVersion");
      if (str1 == null) {
        x.e("error format at version", new Object[0]);
        return null;
      } 
      String str2 = paramMap.get("errorAddr");
      if (str2 == null)
        str2 = "unknown"; 
      str = paramMap.get("codeMsg");
      if (str == null)
        str = "unknown"; 
      String str3 = paramMap.get("tombPath");
      if (str3 == null)
        str3 = "unknown"; 
      String str4 = paramMap.get("signalName");
      if (str4 == null)
        str4 = "unknown"; 
      paramMap.get("errnoMsg");
      String str5 = paramMap.get("stack");
      if (str5 == null)
        str5 = "unknown"; 
      String str6 = paramMap.get("jstack");
      String str7 = str5;
      if (str6 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        str7 = stringBuilder.append(str5).append("java:\n").append(str6).toString();
      } 
      Integer integer1 = map.get("sico");
      str6 = str4;
      str5 = str;
      if (integer1 != null) {
        str6 = str4;
        str5 = str;
        if (integer1.intValue() > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          str6 = stringBuilder.append(str4).append("(").append(str).append(")").toString();
          str5 = "KERNEL";
        } 
      } 
      String str8 = paramMap.get("nativeLog");
      str = null;
      str4 = str;
      if (str8 != null) {
        str4 = str;
        if (!str8.isEmpty())
          arrayOfByte = z.a(null, str8, "BuglyNativeLog.txt"); 
      } 
      str = paramMap.get("sendingProcess");
      if (str == null)
        str = "unknown"; 
      Integer integer2 = map.get("spd");
      str8 = str;
      if (integer2 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        str8 = stringBuilder.append(str).append("(").append(integer2).append(")").toString();
      } 
      str = paramMap.get("threadName");
      if (str == null)
        str = "unknown"; 
      Integer integer3 = map.get("et");
      String str9 = str;
      if (integer3 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        str9 = stringBuilder.append(str).append("(").append(integer3).append(")").toString();
      } 
      str = paramMap.get("processName");
      if (str == null)
        str = "unknown"; 
      Integer integer4 = map.get("ep");
      String str10 = str;
      if (integer4 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        str10 = stringBuilder.append(str).append("(").append(integer4).append(")").toString();
      } 
      str = null;
      String str11 = paramMap.get("key-value");
      if (str11 != null) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        this();
        String[] arrayOfString1 = str11.split("\n");
        int i = arrayOfString1.length;
        byte b1 = 0;
        while (true) {
          HashMap<Object, Object> hashMap1 = hashMap;
          if (b1 < i) {
            arrayOfString = arrayOfString1[b1].split("=");
            if (arrayOfString.length == 2)
              hashMap.put(arrayOfString[0], arrayOfString[1]); 
            b1++;
            continue;
          } 
          break;
        } 
      } 
      long l = ((Integer)map.get("ets")).intValue();
      CrashDetailBean crashDetailBean = paramNativeExceptionHandler.packageCrashDatas(str10, str9, ((Integer)map.get("etms")).intValue() / 1000L + l * 1000L, str6, str2, a(str7), str5, str8, str3, paramMap.get("sysLogPath"), str1, arrayOfByte, (Map<String, String>)arrayOfString, false);
      if (crashDetailBean != null) {
        String str13 = paramMap.get("userId");
        if (str13 != null) {
          x.c("[Native record info] userId: %s", new Object[] { str13 });
          crashDetailBean.m = str13;
        } 
        str13 = paramMap.get("sysLog");
        if (str13 != null)
          crashDetailBean.w = str13; 
        str13 = paramMap.get("appVersion");
        if (str13 != null) {
          x.c("[Native record info] appVersion: %s", new Object[] { str13 });
          crashDetailBean.f = str13;
        } 
        str13 = paramMap.get("isAppForeground");
        if (str13 != null) {
          x.c("[Native record info] isAppForeground: %s", new Object[] { str13 });
          crashDetailBean.M = str13.equalsIgnoreCase("true");
        } 
        String str12 = paramMap.get("launchTime");
        if (str12 != null) {
          x.c("[Native record info] launchTime: %s", new Object[] { str12 });
          try {
            crashDetailBean.L = Long.parseLong(str12);
            crashDetailBean.y = null;
            crashDetailBean.k = true;
          } catch (NumberFormatException numberFormatException) {}
          return crashDetailBean;
        } 
      } else {
        return crashDetailBean;
      } 
      crashDetailBean.y = null;
      crashDetailBean.k = true;
    } catch (Throwable throwable) {
      x.e("error format", new Object[0]);
      throwable.printStackTrace();
      throwable = null;
    } 
    return (CrashDetailBean)throwable;
  }
  
  private static String a(BufferedInputStream paramBufferedInputStream) throws IOException {
    String str = null;
    if (paramBufferedInputStream == null)
      return str; 
    StringBuilder stringBuilder = new StringBuilder();
    while (true) {
      int i = paramBufferedInputStream.read();
      String str1 = str;
      if (i != -1) {
        if (i == 0)
          return stringBuilder.toString(); 
        stringBuilder.append((char)i);
        continue;
      } 
      return str1;
    } 
  }
  
  protected static String a(String paramString) {
    if (paramString == null)
      return ""; 
    String[] arrayOfString = paramString.split("\n");
    String str = paramString;
    if (arrayOfString != null) {
      str = paramString;
      if (arrayOfString.length != 0) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = arrayOfString.length;
        for (byte b1 = 0; b1 < i; b1++) {
          str = arrayOfString[b1];
          if (!str.contains("java.lang.Thread.getStackTrace("))
            stringBuilder.append(str).append("\n"); 
        } 
        str = stringBuilder.toString();
      } 
    } 
    return str;
  }
  
  public static String a(String paramString1, int paramInt, String paramString2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_3
    //   3: astore #4
    //   5: aload_0
    //   6: ifnull -> 16
    //   9: iload_1
    //   10: ifgt -> 19
    //   13: aload_3
    //   14: astore #4
    //   16: aload #4
    //   18: areturn
    //   19: new java/io/File
    //   22: dup
    //   23: aload_0
    //   24: invokespecial <init> : (Ljava/lang/String;)V
    //   27: astore #5
    //   29: aload_3
    //   30: astore #4
    //   32: aload #5
    //   34: invokevirtual exists : ()Z
    //   37: ifeq -> 16
    //   40: aload_3
    //   41: astore #4
    //   43: aload #5
    //   45: invokevirtual canRead : ()Z
    //   48: ifeq -> 16
    //   51: aload_0
    //   52: putstatic com/tencent/bugly/crashreport/crash/jni/b.a : Ljava/lang/String;
    //   55: ldc_w 'Read system log from native record file(length: %s bytes): %s'
    //   58: iconst_2
    //   59: anewarray java/lang/Object
    //   62: dup
    //   63: iconst_0
    //   64: aload #5
    //   66: invokevirtual length : ()J
    //   69: invokestatic valueOf : (J)Ljava/lang/Long;
    //   72: aastore
    //   73: dup
    //   74: iconst_1
    //   75: aload #5
    //   77: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   80: aastore
    //   81: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   84: pop
    //   85: aload_2
    //   86: ifnonnull -> 134
    //   89: new java/io/File
    //   92: dup
    //   93: aload_0
    //   94: invokespecial <init> : (Ljava/lang/String;)V
    //   97: invokestatic a : (Ljava/io/File;)Ljava/lang/String;
    //   100: astore_0
    //   101: aload_0
    //   102: astore #4
    //   104: aload_0
    //   105: ifnull -> 16
    //   108: aload_0
    //   109: astore #4
    //   111: aload_0
    //   112: invokevirtual length : ()I
    //   115: iload_1
    //   116: if_icmple -> 16
    //   119: aload_0
    //   120: aload_0
    //   121: invokevirtual length : ()I
    //   124: iload_1
    //   125: isub
    //   126: invokevirtual substring : (I)Ljava/lang/String;
    //   129: astore #4
    //   131: goto -> 16
    //   134: new java/lang/StringBuilder
    //   137: astore #6
    //   139: aload #6
    //   141: invokespecial <init> : ()V
    //   144: new java/io/BufferedReader
    //   147: astore #4
    //   149: new java/io/InputStreamReader
    //   152: astore_0
    //   153: new java/io/FileInputStream
    //   156: astore #7
    //   158: aload #7
    //   160: aload #5
    //   162: invokespecial <init> : (Ljava/io/File;)V
    //   165: aload_0
    //   166: aload #7
    //   168: ldc_w 'utf-8'
    //   171: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   174: aload #4
    //   176: aload_0
    //   177: invokespecial <init> : (Ljava/io/Reader;)V
    //   180: aload #4
    //   182: astore_0
    //   183: aload #4
    //   185: invokevirtual readLine : ()Ljava/lang/String;
    //   188: astore #7
    //   190: aload #7
    //   192: ifnull -> 312
    //   195: aload #4
    //   197: astore_0
    //   198: new java/lang/StringBuilder
    //   201: astore #5
    //   203: aload #4
    //   205: astore_0
    //   206: aload #5
    //   208: invokespecial <init> : ()V
    //   211: aload #4
    //   213: astore_0
    //   214: aload #5
    //   216: aload_2
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: ldc_w '[ ]*:'
    //   223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: invokevirtual toString : ()Ljava/lang/String;
    //   229: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   232: aload #7
    //   234: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   237: invokevirtual find : ()Z
    //   240: ifeq -> 180
    //   243: aload #4
    //   245: astore_0
    //   246: aload #6
    //   248: aload #7
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload #4
    //   256: astore_0
    //   257: aload #6
    //   259: ldc '\\n'
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: pop
    //   265: goto -> 180
    //   268: astore_0
    //   269: aload #4
    //   271: astore_2
    //   272: aload_0
    //   273: astore #4
    //   275: aload_2
    //   276: astore_0
    //   277: aload #4
    //   279: invokestatic a : (Ljava/lang/Throwable;)Z
    //   282: pop
    //   283: aload_3
    //   284: astore #4
    //   286: aload_2
    //   287: ifnull -> 16
    //   290: aload_2
    //   291: invokevirtual close : ()V
    //   294: aload_3
    //   295: astore #4
    //   297: goto -> 16
    //   300: astore_0
    //   301: aload_0
    //   302: invokestatic a : (Ljava/lang/Throwable;)Z
    //   305: pop
    //   306: aload_3
    //   307: astore #4
    //   309: goto -> 16
    //   312: aload #4
    //   314: astore_0
    //   315: aload #6
    //   317: invokevirtual toString : ()Ljava/lang/String;
    //   320: astore_2
    //   321: aload_2
    //   322: astore_0
    //   323: aload #4
    //   325: invokevirtual close : ()V
    //   328: goto -> 101
    //   331: astore_2
    //   332: aload_2
    //   333: invokestatic a : (Ljava/lang/Throwable;)Z
    //   336: pop
    //   337: goto -> 101
    //   340: astore_2
    //   341: aconst_null
    //   342: astore_0
    //   343: aload_0
    //   344: ifnull -> 351
    //   347: aload_0
    //   348: invokevirtual close : ()V
    //   351: aload_2
    //   352: athrow
    //   353: astore_0
    //   354: aload_0
    //   355: invokestatic a : (Ljava/lang/Throwable;)Z
    //   358: pop
    //   359: goto -> 351
    //   362: astore_2
    //   363: goto -> 343
    //   366: astore #4
    //   368: aconst_null
    //   369: astore_2
    //   370: goto -> 275
    // Exception table:
    //   from	to	target	type
    //   134	180	366	java/lang/Throwable
    //   134	180	340	finally
    //   183	190	268	java/lang/Throwable
    //   183	190	362	finally
    //   198	203	268	java/lang/Throwable
    //   198	203	362	finally
    //   206	211	268	java/lang/Throwable
    //   206	211	362	finally
    //   214	243	268	java/lang/Throwable
    //   214	243	362	finally
    //   246	254	268	java/lang/Throwable
    //   246	254	362	finally
    //   257	265	268	java/lang/Throwable
    //   257	265	362	finally
    //   277	283	362	finally
    //   290	294	300	java/lang/Exception
    //   315	321	268	java/lang/Throwable
    //   315	321	362	finally
    //   323	328	331	java/lang/Exception
    //   347	351	353	java/lang/Exception
  }
  
  public static String a(String paramString1, String paramString2) {
    if (paramString1 == null || paramString2 == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    String str = b(paramString1, paramString2);
    if (str != null && !str.isEmpty()) {
      stringBuilder.append("Register infos:\n");
      stringBuilder.append(str);
    } 
    paramString1 = c(paramString1, paramString2);
    if (paramString1 != null && !paramString1.isEmpty()) {
      if (stringBuilder.length() > 0)
        stringBuilder.append("\n"); 
      stringBuilder.append("System SO infos:\n");
      stringBuilder.append(paramString1);
    } 
    return stringBuilder.toString();
  }
  
  public static void a(boolean paramBoolean, String paramString) {
    if (paramString != null) {
      File file = new File(paramString, "rqd_record.eup");
      if (file.exists() && file.canWrite()) {
        file.delete();
        x.c("delete record file %s", new Object[] { file.getAbsoluteFile() });
      } 
      file = new File(paramString, "reg_record.txt");
      if (file.exists() && file.canWrite()) {
        file.delete();
        x.c("delete record file %s", new Object[] { file.getAbsoluteFile() });
      } 
      file = new File(paramString, "map_record.txt");
      if (file.exists() && file.canWrite()) {
        file.delete();
        x.c("delete record file %s", new Object[] { file.getAbsoluteFile() });
      } 
      file = new File(paramString, "backup_record.txt");
      if (file.exists() && file.canWrite()) {
        file.delete();
        x.c("delete record file %s", new Object[] { file.getAbsoluteFile() });
      } 
      if (a != null) {
        file = new File(a);
        if (file.exists() && file.canWrite()) {
          file.delete();
          x.c("delete record file %s", new Object[] { file.getAbsoluteFile() });
        } 
      } 
      if (paramBoolean) {
        File file1 = new File(paramString);
        if (file1.canRead() && file1.isDirectory()) {
          File[] arrayOfFile = file1.listFiles();
          if (arrayOfFile != null) {
            int i = arrayOfFile.length;
            for (byte b1 = 0; b1 < i; b1++) {
              file = arrayOfFile[b1];
              if (file.canRead() && file.canWrite() && file.length() == 0L) {
                file.delete();
                x.c("delete invalid record file %s", new Object[] { file.getAbsoluteFile() });
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public static String b(String paramString) {
    String str = null;
    if (paramString == null)
      return str; 
    File file = new File(paramString, "backup_record.txt");
    paramString = str;
    if (file.exists())
      paramString = file.getAbsolutePath(); 
    return paramString;
  }
  
  private static String b(String paramString1, String paramString2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: ldc_w 'reg_record.txt'
    //   6: invokestatic a : (Ljava/lang/String;Ljava/lang/String;)Ljava/io/BufferedReader;
    //   9: astore_3
    //   10: aload_3
    //   11: ifnonnull -> 18
    //   14: aload_2
    //   15: astore_0
    //   16: aload_0
    //   17: areturn
    //   18: new java/lang/StringBuilder
    //   21: astore_0
    //   22: aload_0
    //   23: invokespecial <init> : ()V
    //   26: aload_3
    //   27: invokevirtual readLine : ()Ljava/lang/String;
    //   30: astore #4
    //   32: aload #4
    //   34: ifnull -> 50
    //   37: aload #4
    //   39: aload_1
    //   40: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   43: istore #5
    //   45: iload #5
    //   47: ifne -> 76
    //   50: aload_2
    //   51: astore_0
    //   52: aload_3
    //   53: ifnull -> 16
    //   56: aload_3
    //   57: invokevirtual close : ()V
    //   60: aload_2
    //   61: astore_0
    //   62: goto -> 16
    //   65: astore_0
    //   66: aload_0
    //   67: invokestatic a : (Ljava/lang/Throwable;)Z
    //   70: pop
    //   71: aload_2
    //   72: astore_0
    //   73: goto -> 16
    //   76: iconst_0
    //   77: istore #6
    //   79: bipush #18
    //   81: istore #7
    //   83: iconst_0
    //   84: istore #8
    //   86: aload_3
    //   87: invokevirtual readLine : ()Ljava/lang/String;
    //   90: astore_1
    //   91: aload_1
    //   92: ifnull -> 205
    //   95: iload #8
    //   97: iconst_4
    //   98: irem
    //   99: ifne -> 140
    //   102: iload #8
    //   104: ifle -> 114
    //   107: aload_0
    //   108: ldc '\\n'
    //   110: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload_0
    //   115: ldc_w '  '
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload_1
    //   123: invokevirtual length : ()I
    //   126: istore #6
    //   128: aload_0
    //   129: aload_1
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: iinc #8, 1
    //   137: goto -> 86
    //   140: aload_1
    //   141: invokevirtual length : ()I
    //   144: bipush #16
    //   146: if_icmple -> 153
    //   149: bipush #28
    //   151: istore #7
    //   153: aload_0
    //   154: ldc_w '                '
    //   157: iconst_0
    //   158: iload #7
    //   160: iload #6
    //   162: isub
    //   163: invokevirtual substring : (II)Ljava/lang/String;
    //   166: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: pop
    //   170: goto -> 122
    //   173: astore_0
    //   174: aload_0
    //   175: invokestatic a : (Ljava/lang/Throwable;)Z
    //   178: pop
    //   179: aload_2
    //   180: astore_0
    //   181: aload_3
    //   182: ifnull -> 16
    //   185: aload_3
    //   186: invokevirtual close : ()V
    //   189: aload_2
    //   190: astore_0
    //   191: goto -> 16
    //   194: astore_0
    //   195: aload_0
    //   196: invokestatic a : (Ljava/lang/Throwable;)Z
    //   199: pop
    //   200: aload_2
    //   201: astore_0
    //   202: goto -> 16
    //   205: aload_0
    //   206: ldc '\\n'
    //   208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: pop
    //   212: aload_0
    //   213: invokevirtual toString : ()Ljava/lang/String;
    //   216: astore_1
    //   217: aload_1
    //   218: astore_0
    //   219: aload_3
    //   220: ifnull -> 16
    //   223: aload_3
    //   224: invokevirtual close : ()V
    //   227: aload_1
    //   228: astore_0
    //   229: goto -> 16
    //   232: astore_0
    //   233: aload_0
    //   234: invokestatic a : (Ljava/lang/Throwable;)Z
    //   237: pop
    //   238: aload_1
    //   239: astore_0
    //   240: goto -> 16
    //   243: astore_0
    //   244: aload_3
    //   245: ifnull -> 252
    //   248: aload_3
    //   249: invokevirtual close : ()V
    //   252: aload_0
    //   253: athrow
    //   254: astore_1
    //   255: aload_1
    //   256: invokestatic a : (Ljava/lang/Throwable;)Z
    //   259: pop
    //   260: goto -> 252
    // Exception table:
    //   from	to	target	type
    //   18	32	173	java/lang/Throwable
    //   18	32	243	finally
    //   37	45	173	java/lang/Throwable
    //   37	45	243	finally
    //   56	60	65	java/lang/Exception
    //   86	91	173	java/lang/Throwable
    //   86	91	243	finally
    //   107	114	173	java/lang/Throwable
    //   107	114	243	finally
    //   114	122	173	java/lang/Throwable
    //   114	122	243	finally
    //   122	134	173	java/lang/Throwable
    //   122	134	243	finally
    //   140	149	173	java/lang/Throwable
    //   140	149	243	finally
    //   153	170	173	java/lang/Throwable
    //   153	170	243	finally
    //   174	179	243	finally
    //   185	189	194	java/lang/Exception
    //   205	217	173	java/lang/Throwable
    //   205	217	243	finally
    //   223	227	232	java/lang/Exception
    //   248	252	254	java/lang/Exception
  }
  
  private static String c(String paramString1, String paramString2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: ldc_w 'map_record.txt'
    //   6: invokestatic a : (Ljava/lang/String;Ljava/lang/String;)Ljava/io/BufferedReader;
    //   9: astore_3
    //   10: aload_3
    //   11: ifnonnull -> 18
    //   14: aload_2
    //   15: astore_1
    //   16: aload_1
    //   17: areturn
    //   18: new java/lang/StringBuilder
    //   21: astore_0
    //   22: aload_0
    //   23: invokespecial <init> : ()V
    //   26: aload_3
    //   27: invokevirtual readLine : ()Ljava/lang/String;
    //   30: astore #4
    //   32: aload #4
    //   34: ifnull -> 50
    //   37: aload #4
    //   39: aload_1
    //   40: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   43: istore #5
    //   45: iload #5
    //   47: ifne -> 76
    //   50: aload_2
    //   51: astore_1
    //   52: aload_3
    //   53: ifnull -> 16
    //   56: aload_3
    //   57: invokevirtual close : ()V
    //   60: aload_2
    //   61: astore_1
    //   62: goto -> 16
    //   65: astore_0
    //   66: aload_0
    //   67: invokestatic a : (Ljava/lang/Throwable;)Z
    //   70: pop
    //   71: aload_2
    //   72: astore_1
    //   73: goto -> 16
    //   76: aload_3
    //   77: invokevirtual readLine : ()Ljava/lang/String;
    //   80: astore_1
    //   81: aload_1
    //   82: ifnull -> 141
    //   85: aload_0
    //   86: ldc_w '  '
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload_0
    //   94: aload_1
    //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_0
    //   100: ldc '\\n'
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: goto -> 76
    //   109: astore_0
    //   110: aload_0
    //   111: invokestatic a : (Ljava/lang/Throwable;)Z
    //   114: pop
    //   115: aload_2
    //   116: astore_1
    //   117: aload_3
    //   118: ifnull -> 16
    //   121: aload_3
    //   122: invokevirtual close : ()V
    //   125: aload_2
    //   126: astore_1
    //   127: goto -> 16
    //   130: astore_0
    //   131: aload_0
    //   132: invokestatic a : (Ljava/lang/Throwable;)Z
    //   135: pop
    //   136: aload_2
    //   137: astore_1
    //   138: goto -> 16
    //   141: aload_0
    //   142: invokevirtual toString : ()Ljava/lang/String;
    //   145: astore_0
    //   146: aload_0
    //   147: astore_1
    //   148: aload_3
    //   149: ifnull -> 16
    //   152: aload_3
    //   153: invokevirtual close : ()V
    //   156: aload_0
    //   157: astore_1
    //   158: goto -> 16
    //   161: astore_1
    //   162: aload_1
    //   163: invokestatic a : (Ljava/lang/Throwable;)Z
    //   166: pop
    //   167: aload_0
    //   168: astore_1
    //   169: goto -> 16
    //   172: astore_0
    //   173: aload_3
    //   174: ifnull -> 181
    //   177: aload_3
    //   178: invokevirtual close : ()V
    //   181: aload_0
    //   182: athrow
    //   183: astore_1
    //   184: aload_1
    //   185: invokestatic a : (Ljava/lang/Throwable;)Z
    //   188: pop
    //   189: goto -> 181
    // Exception table:
    //   from	to	target	type
    //   18	32	109	java/lang/Throwable
    //   18	32	172	finally
    //   37	45	109	java/lang/Throwable
    //   37	45	172	finally
    //   56	60	65	java/lang/Exception
    //   76	81	109	java/lang/Throwable
    //   76	81	172	finally
    //   85	106	109	java/lang/Throwable
    //   85	106	172	finally
    //   110	115	172	finally
    //   121	125	130	java/lang/Exception
    //   141	146	109	java/lang/Throwable
    //   141	146	172	finally
    //   152	156	161	java/lang/Exception
    //   177	181	183	java/lang/Exception
  }
  
  private static Map<String, Integer> c(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: aload_1
    //   7: astore_0
    //   8: aload_0
    //   9: areturn
    //   10: new java/util/HashMap
    //   13: astore_2
    //   14: aload_2
    //   15: invokespecial <init> : ()V
    //   18: aload_0
    //   19: ldc_w ','
    //   22: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   25: astore_3
    //   26: aload_3
    //   27: arraylength
    //   28: istore #4
    //   30: iconst_0
    //   31: istore #5
    //   33: iload #5
    //   35: iload #4
    //   37: if_icmpge -> 140
    //   40: aload_3
    //   41: iload #5
    //   43: aaload
    //   44: astore #6
    //   46: aload #6
    //   48: ldc_w ':'
    //   51: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   54: astore #7
    //   56: aload #7
    //   58: arraylength
    //   59: iconst_2
    //   60: if_icmpeq -> 109
    //   63: ldc_w 'error format at %s'
    //   66: iconst_1
    //   67: anewarray java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: aload #6
    //   74: aastore
    //   75: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   78: pop
    //   79: aload_1
    //   80: astore_0
    //   81: goto -> 8
    //   84: astore_2
    //   85: ldc_w 'error format intStateStr %s'
    //   88: iconst_1
    //   89: anewarray java/lang/Object
    //   92: dup
    //   93: iconst_0
    //   94: aload_0
    //   95: aastore
    //   96: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_2
    //   101: invokevirtual printStackTrace : ()V
    //   104: aload_1
    //   105: astore_0
    //   106: goto -> 8
    //   109: aload #7
    //   111: iconst_1
    //   112: aaload
    //   113: invokestatic parseInt : (Ljava/lang/String;)I
    //   116: istore #8
    //   118: aload_2
    //   119: aload #7
    //   121: iconst_0
    //   122: aaload
    //   123: iload #8
    //   125: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   128: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   133: pop
    //   134: iinc #5, 1
    //   137: goto -> 33
    //   140: aload_2
    //   141: astore_0
    //   142: goto -> 8
    // Exception table:
    //   from	to	target	type
    //   10	30	84	java/lang/Exception
    //   46	79	84	java/lang/Exception
    //   109	134	84	java/lang/Exception
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/jni/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */