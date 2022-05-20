package com.tencent.bugly.crashreport.crash.jni;

import android.annotation.SuppressLint;
import android.content.Context;
import com.tencent.bugly.crashreport.a;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.b;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.File;

public class NativeCrashHandler implements a {
  private static NativeCrashHandler a;
  
  private static boolean l = false;
  
  private static boolean m = false;
  
  private final Context b;
  
  private final a c;
  
  private final w d;
  
  private NativeExceptionHandler e;
  
  private String f;
  
  private final boolean g;
  
  private boolean h;
  
  private boolean i;
  
  private boolean j;
  
  private boolean k;
  
  private b n;
  
  @SuppressLint({"SdCardPath"})
  private NativeCrashHandler(Context paramContext, a parama, b paramb, w paramw, boolean paramBoolean, String paramString) {
    String str;
    this.h = false;
    this.i = false;
    this.j = false;
    this.k = false;
    this.b = z.a(paramContext);
    try {
      boolean bool = z.a(paramString);
      if (bool)
        paramString = paramContext.getDir("bugly", 0).getAbsolutePath(); 
    } catch (Throwable throwable) {
      str = (a.a(paramContext)).c;
      str = "/data/data/" + str + "/app_bugly";
    } 
    this.n = paramb;
    this.f = str;
    this.c = parama;
    this.d = paramw;
    this.g = paramBoolean;
    this.e = new a(paramContext, parama, paramb, a.a());
  }
  
  private void a(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : Z
    //   6: ifeq -> 22
    //   9: ldc '[Native] Native crash report has already registered.'
    //   11: iconst_0
    //   12: anewarray java/lang/Object
    //   15: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: aload_0
    //   23: getfield i : Z
    //   26: istore_2
    //   27: iload_2
    //   28: ifeq -> 321
    //   31: aload_0
    //   32: aload_0
    //   33: getfield f : Ljava/lang/String;
    //   36: iload_1
    //   37: iconst_1
    //   38: invokevirtual regist : (Ljava/lang/String;ZI)Ljava/lang/String;
    //   41: astore_3
    //   42: aload_3
    //   43: ifnull -> 232
    //   46: ldc '[Native] Native Crash Report enable.'
    //   48: iconst_0
    //   49: anewarray java/lang/Object
    //   52: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   55: pop
    //   56: ldc '[Native] Check extra jni for Bugly NDK v%s'
    //   58: iconst_1
    //   59: anewarray java/lang/Object
    //   62: dup
    //   63: iconst_0
    //   64: aload_3
    //   65: aastore
    //   66: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   69: pop
    //   70: ldc '2.1.1'
    //   72: ldc '.'
    //   74: ldc ''
    //   76: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   79: astore #4
    //   81: ldc '2.3.0'
    //   83: ldc '.'
    //   85: ldc ''
    //   87: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   90: astore #5
    //   92: aload_3
    //   93: ldc '.'
    //   95: ldc ''
    //   97: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   100: astore #6
    //   102: aload #6
    //   104: invokevirtual length : ()I
    //   107: iconst_2
    //   108: if_icmpne -> 252
    //   111: new java/lang/StringBuilder
    //   114: astore #7
    //   116: aload #7
    //   118: invokespecial <init> : ()V
    //   121: aload #7
    //   123: aload #6
    //   125: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: ldc '0'
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: invokevirtual toString : ()Ljava/lang/String;
    //   136: astore #7
    //   138: aload #7
    //   140: invokestatic parseInt : (Ljava/lang/String;)I
    //   143: aload #4
    //   145: invokestatic parseInt : (Ljava/lang/String;)I
    //   148: if_icmplt -> 155
    //   151: iconst_1
    //   152: putstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.l : Z
    //   155: aload #7
    //   157: invokestatic parseInt : (Ljava/lang/String;)I
    //   160: aload #5
    //   162: invokestatic parseInt : (Ljava/lang/String;)I
    //   165: if_icmplt -> 172
    //   168: iconst_1
    //   169: putstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.m : Z
    //   172: getstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.m : Z
    //   175: ifeq -> 295
    //   178: ldc '[Native] Info setting jni can be accessed.'
    //   180: iconst_0
    //   181: anewarray java/lang/Object
    //   184: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   187: pop
    //   188: getstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.l : Z
    //   191: ifeq -> 308
    //   194: ldc '[Native] Extra jni can be accessed.'
    //   196: iconst_0
    //   197: anewarray java/lang/Object
    //   200: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   203: pop
    //   204: aload_0
    //   205: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   208: aload_3
    //   209: putfield n : Ljava/lang/String;
    //   212: aload_0
    //   213: iconst_1
    //   214: putfield j : Z
    //   217: goto -> 19
    //   220: astore #7
    //   222: ldc '[Native] Failed to load Bugly SO file.'
    //   224: iconst_0
    //   225: anewarray java/lang/Object
    //   228: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   231: pop
    //   232: aload_0
    //   233: iconst_0
    //   234: putfield i : Z
    //   237: aload_0
    //   238: iconst_0
    //   239: putfield h : Z
    //   242: goto -> 19
    //   245: astore #7
    //   247: aload_0
    //   248: monitorexit
    //   249: aload #7
    //   251: athrow
    //   252: aload #6
    //   254: astore #7
    //   256: aload #6
    //   258: invokevirtual length : ()I
    //   261: iconst_1
    //   262: if_icmpne -> 138
    //   265: new java/lang/StringBuilder
    //   268: astore #7
    //   270: aload #7
    //   272: invokespecial <init> : ()V
    //   275: aload #7
    //   277: aload #6
    //   279: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: ldc '00'
    //   284: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   287: invokevirtual toString : ()Ljava/lang/String;
    //   290: astore #7
    //   292: goto -> 138
    //   295: ldc '[Native] Info setting jni can not be accessed.'
    //   297: iconst_0
    //   298: anewarray java/lang/Object
    //   301: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   304: pop
    //   305: goto -> 188
    //   308: ldc '[Native] Extra jni can not be accessed.'
    //   310: iconst_0
    //   311: anewarray java/lang/Object
    //   314: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   317: pop
    //   318: goto -> 204
    //   321: aload_0
    //   322: getfield h : Z
    //   325: istore_2
    //   326: iload_2
    //   327: ifeq -> 232
    //   330: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   333: astore #4
    //   335: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   338: astore_3
    //   339: aload_0
    //   340: getfield f : Ljava/lang/String;
    //   343: astore #7
    //   345: iconst_0
    //   346: invokestatic a : (Z)Ljava/lang/String;
    //   349: astore #6
    //   351: iload_1
    //   352: ifeq -> 648
    //   355: iconst_1
    //   356: istore #8
    //   358: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   360: ldc 'registNativeExceptionHandler2'
    //   362: aconst_null
    //   363: iconst_4
    //   364: anewarray java/lang/Class
    //   367: dup
    //   368: iconst_0
    //   369: ldc java/lang/String
    //   371: aastore
    //   372: dup
    //   373: iconst_1
    //   374: ldc java/lang/String
    //   376: aastore
    //   377: dup
    //   378: iconst_2
    //   379: aload #4
    //   381: aastore
    //   382: dup
    //   383: iconst_3
    //   384: aload_3
    //   385: aastore
    //   386: iconst_4
    //   387: anewarray java/lang/Object
    //   390: dup
    //   391: iconst_0
    //   392: aload #7
    //   394: aastore
    //   395: dup
    //   396: iconst_1
    //   397: aload #6
    //   399: aastore
    //   400: dup
    //   401: iconst_2
    //   402: iload #8
    //   404: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   407: aastore
    //   408: dup
    //   409: iconst_3
    //   410: iconst_1
    //   411: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   414: aastore
    //   415: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   418: checkcast java/lang/String
    //   421: astore #6
    //   423: aload #6
    //   425: astore #7
    //   427: aload #6
    //   429: ifnonnull -> 510
    //   432: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   435: astore #6
    //   437: aload_0
    //   438: getfield f : Ljava/lang/String;
    //   441: astore #7
    //   443: iconst_0
    //   444: invokestatic a : (Z)Ljava/lang/String;
    //   447: astore_3
    //   448: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   451: pop
    //   452: invokestatic L : ()I
    //   455: istore #8
    //   457: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   459: ldc 'registNativeExceptionHandler'
    //   461: aconst_null
    //   462: iconst_3
    //   463: anewarray java/lang/Class
    //   466: dup
    //   467: iconst_0
    //   468: ldc java/lang/String
    //   470: aastore
    //   471: dup
    //   472: iconst_1
    //   473: ldc java/lang/String
    //   475: aastore
    //   476: dup
    //   477: iconst_2
    //   478: aload #6
    //   480: aastore
    //   481: iconst_3
    //   482: anewarray java/lang/Object
    //   485: dup
    //   486: iconst_0
    //   487: aload #7
    //   489: aastore
    //   490: dup
    //   491: iconst_1
    //   492: aload_3
    //   493: aastore
    //   494: dup
    //   495: iconst_2
    //   496: iload #8
    //   498: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   501: aastore
    //   502: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   505: checkcast java/lang/String
    //   508: astore #7
    //   510: aload #7
    //   512: ifnull -> 232
    //   515: aload_0
    //   516: iconst_1
    //   517: putfield j : Z
    //   520: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   523: aload #7
    //   525: putfield n : Ljava/lang/String;
    //   528: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   530: ldc 'checkExtraJni'
    //   532: aconst_null
    //   533: iconst_1
    //   534: anewarray java/lang/Class
    //   537: dup
    //   538: iconst_0
    //   539: ldc java/lang/String
    //   541: aastore
    //   542: iconst_1
    //   543: anewarray java/lang/Object
    //   546: dup
    //   547: iconst_0
    //   548: aload #7
    //   550: aastore
    //   551: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   554: checkcast java/lang/Boolean
    //   557: astore #7
    //   559: aload #7
    //   561: ifnull -> 572
    //   564: aload #7
    //   566: invokevirtual booleanValue : ()Z
    //   569: putstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.l : Z
    //   572: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   574: ldc 'enableHandler'
    //   576: aconst_null
    //   577: iconst_1
    //   578: anewarray java/lang/Class
    //   581: dup
    //   582: iconst_0
    //   583: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   586: aastore
    //   587: iconst_1
    //   588: anewarray java/lang/Object
    //   591: dup
    //   592: iconst_0
    //   593: iconst_1
    //   594: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   597: aastore
    //   598: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   601: pop
    //   602: iload_1
    //   603: ifeq -> 654
    //   606: iconst_1
    //   607: istore #8
    //   609: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   611: ldc 'setLogMode'
    //   613: aconst_null
    //   614: iconst_1
    //   615: anewarray java/lang/Class
    //   618: dup
    //   619: iconst_0
    //   620: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   623: aastore
    //   624: iconst_1
    //   625: anewarray java/lang/Object
    //   628: dup
    //   629: iconst_0
    //   630: iload #8
    //   632: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   635: aastore
    //   636: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   639: pop
    //   640: goto -> 19
    //   643: astore #7
    //   645: goto -> 232
    //   648: iconst_5
    //   649: istore #8
    //   651: goto -> 358
    //   654: iconst_5
    //   655: istore #8
    //   657: goto -> 609
    //   660: astore #7
    //   662: goto -> 172
    // Exception table:
    //   from	to	target	type
    //   2	19	245	finally
    //   22	27	245	finally
    //   31	42	220	java/lang/Throwable
    //   31	42	245	finally
    //   46	138	220	java/lang/Throwable
    //   46	138	245	finally
    //   138	155	660	java/lang/Throwable
    //   138	155	245	finally
    //   155	172	660	java/lang/Throwable
    //   155	172	245	finally
    //   172	188	220	java/lang/Throwable
    //   172	188	245	finally
    //   188	204	220	java/lang/Throwable
    //   188	204	245	finally
    //   204	217	220	java/lang/Throwable
    //   204	217	245	finally
    //   222	232	245	finally
    //   232	242	245	finally
    //   256	292	220	java/lang/Throwable
    //   256	292	245	finally
    //   295	305	220	java/lang/Throwable
    //   295	305	245	finally
    //   308	318	220	java/lang/Throwable
    //   308	318	245	finally
    //   321	326	245	finally
    //   330	351	643	java/lang/Throwable
    //   330	351	245	finally
    //   358	423	643	java/lang/Throwable
    //   358	423	245	finally
    //   432	510	643	java/lang/Throwable
    //   432	510	245	finally
    //   515	559	643	java/lang/Throwable
    //   515	559	245	finally
    //   564	572	643	java/lang/Throwable
    //   564	572	245	finally
    //   572	602	643	java/lang/Throwable
    //   572	602	245	finally
    //   609	640	643	java/lang/Throwable
    //   609	640	245	finally
  }
  
  private boolean a(int paramInt, String paramString) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.i) {
      if (!m)
        return bool1; 
    } else {
      return bool2;
    } 
    try {
      setNativeInfo(paramInt, paramString);
      bool2 = true;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      m = false;
      bool2 = bool1;
    } catch (Throwable throwable) {
      bool2 = bool1;
    } 
    return bool2;
  }
  
  private static boolean a(String paramString, boolean paramBoolean) {
    boolean bool = true;
    try {
      x.a("[Native] Trying to load so: %s", new Object[] { paramString });
      if (paramBoolean) {
        System.load(paramString);
      } else {
        System.loadLibrary(paramString);
      } 
      try {
        x.a("[Native] Successfully loaded SO: %s", new Object[] { paramString });
        return bool;
      } catch (Throwable null) {
        paramBoolean = true;
      } 
    } catch (Throwable throwable) {
      paramBoolean = false;
    } 
    x.d(throwable.getMessage(), new Object[0]);
    x.d("[Native] Failed to load so: %s", new Object[] { paramString });
    return paramBoolean;
  }
  
  private void b() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : Z
    //   6: ifne -> 23
    //   9: ldc_w '[Native] Native crash report has already unregistered.'
    //   12: iconst_0
    //   13: anewarray java/lang/Object
    //   16: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   19: pop
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: invokevirtual unregist : ()Ljava/lang/String;
    //   27: ifnull -> 61
    //   30: ldc_w '[Native] Successfully closed native crash report.'
    //   33: iconst_0
    //   34: anewarray java/lang/Object
    //   37: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   40: pop
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield j : Z
    //   46: goto -> 20
    //   49: astore_1
    //   50: ldc_w '[Native] Failed to close native crash report.'
    //   53: iconst_0
    //   54: anewarray java/lang/Object
    //   57: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   60: pop
    //   61: ldc 'com.tencent.feedback.eup.jni.NativeExceptionUpload'
    //   63: ldc 'enableHandler'
    //   65: aconst_null
    //   66: iconst_1
    //   67: anewarray java/lang/Class
    //   70: dup
    //   71: iconst_0
    //   72: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   75: aastore
    //   76: iconst_1
    //   77: anewarray java/lang/Object
    //   80: dup
    //   81: iconst_0
    //   82: iconst_0
    //   83: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   86: aastore
    //   87: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
    //   90: pop
    //   91: aload_0
    //   92: iconst_0
    //   93: putfield j : Z
    //   96: ldc_w '[Native] Successfully closed native crash report.'
    //   99: iconst_0
    //   100: anewarray java/lang/Object
    //   103: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   106: pop
    //   107: goto -> 20
    //   110: astore_1
    //   111: ldc_w '[Native] Failed to close native crash report.'
    //   114: iconst_0
    //   115: anewarray java/lang/Object
    //   118: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   121: pop
    //   122: aload_0
    //   123: iconst_0
    //   124: putfield i : Z
    //   127: aload_0
    //   128: iconst_0
    //   129: putfield h : Z
    //   132: goto -> 20
    //   135: astore_1
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_1
    //   139: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	135	finally
    //   23	46	49	java/lang/Throwable
    //   23	46	135	finally
    //   50	61	135	finally
    //   61	107	110	java/lang/Throwable
    //   61	107	135	finally
    //   111	132	135	finally
  }
  
  private void b(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 13
    //   6: aload_0
    //   7: invokevirtual startNativeMonitor : ()V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: aload_0
    //   14: invokespecial b : ()V
    //   17: goto -> 10
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   6	10	20	finally
    //   13	17	20	finally
  }
  
  private void c(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield k : Z
    //   6: iload_1
    //   7: if_icmpeq -> 33
    //   10: ldc_w 'user change native %b'
    //   13: iconst_1
    //   14: anewarray java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: iload_1
    //   20: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   23: aastore
    //   24: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   27: pop
    //   28: aload_0
    //   29: iload_1
    //   30: putfield k : Z
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  public static NativeCrashHandler getInstance() {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.a : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static NativeCrashHandler getInstance(Context paramContext, a parama, b paramb, a parama1, w paramw, boolean paramBoolean, String paramString) {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.a : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   6: ifnonnull -> 30
    //   9: new com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   12: astore_3
    //   13: aload_3
    //   14: aload_0
    //   15: aload_1
    //   16: aload_2
    //   17: aload #4
    //   19: iload #5
    //   21: aload #6
    //   23: invokespecial <init> : (Landroid/content/Context;Lcom/tencent/bugly/crashreport/common/info/a;Lcom/tencent/bugly/crashreport/crash/b;Lcom/tencent/bugly/proguard/w;ZLjava/lang/String;)V
    //   26: aload_3
    //   27: putstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.a : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   30: getstatic com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.a : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   33: astore_0
    //   34: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   36: monitorexit
    //   37: aload_0
    //   38: areturn
    //   39: astore_0
    //   40: ldc com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler
    //   42: monitorexit
    //   43: aload_0
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   3	30	39	finally
    //   30	34	39	finally
  }
  
  protected final void a() {
    long l1 = z.b();
    long l2 = c.g;
    File file = new File(this.f);
    if (file.exists() && file.isDirectory()) {
      File[] arrayOfFile = file.listFiles();
      if (arrayOfFile != null && arrayOfFile.length != 0) {
        Object object;
        int i = "tomb_".length();
        int j = arrayOfFile.length;
        byte b1 = 0;
        boolean bool = false;
        while (b1 < j) {
          File file1 = arrayOfFile[b1];
          String str = file1.getName();
          Object object1 = object;
          if (str.startsWith("tomb_")) {
            try {
              int k = str.indexOf(".txt");
              if (k > 0) {
                long l = Long.parseLong(str.substring(i, k));
                if (l >= l1 - l2) {
                  Object object2 = object;
                  continue;
                } 
              } 
            } catch (Throwable throwable) {
              x.e("[Native] Tomb file format error, delete %s", new Object[] { str });
            } 
            object1 = object;
            if (file1.delete())
              int k = object + 1; 
          } 
          continue;
          b1++;
          object = SYNTHETIC_LOCAL_VARIABLE_13;
        } 
        x.c("[Native] Clean tombs %d", new Object[] { Integer.valueOf(object) });
      } 
    } 
  }
  
  public boolean appendLogToNative(String paramString1, String paramString2, String paramString3) {
    boolean bool;
    if (!this.h && !this.i)
      return false; 
    if (!l)
      return false; 
    if (paramString1 == null || paramString2 == null || paramString3 == null)
      return false; 
    try {
      if (this.i)
        return appendNativeLog(paramString1, paramString2, paramString3); 
      Boolean bool1 = (Boolean)z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "appendNativeLog", null, new Class[] { String.class, String.class, String.class }, new Object[] { paramString1, paramString2, paramString3 });
      if (bool1 != null)
        return bool1.booleanValue(); 
      bool = false;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      l = false;
      bool = false;
    } catch (Throwable throwable) {}
    return bool;
  }
  
  protected native boolean appendNativeLog(String paramString1, String paramString2, String paramString3);
  
  protected native boolean appendWholeNativeLog(String paramString);
  
  public boolean filterSigabrtSysLog() {
    return a(998, "true");
  }
  
  public String getDumpFilePath() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield f : Ljava/lang/String;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public NativeExceptionHandler getNativeExceptionHandler() {
    return this.e;
  }
  
  protected native String getNativeKeyValueList();
  
  protected native String getNativeLog();
  
  public boolean isUserOpened() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield k : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void onStrategyChanged(StrategyBean paramStrategyBean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnull -> 40
    //   8: aload_1
    //   9: getfield g : Z
    //   12: aload_0
    //   13: getfield j : Z
    //   16: if_icmpeq -> 40
    //   19: ldc_w 'server native changed to %b'
    //   22: iconst_1
    //   23: anewarray java/lang/Object
    //   26: dup
    //   27: iconst_0
    //   28: aload_1
    //   29: getfield g : Z
    //   32: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   35: aastore
    //   36: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   39: pop
    //   40: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   43: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   46: getfield g : Z
    //   49: ifeq -> 93
    //   52: aload_0
    //   53: getfield k : Z
    //   56: ifeq -> 93
    //   59: iload_2
    //   60: aload_0
    //   61: getfield j : Z
    //   64: if_icmpeq -> 90
    //   67: ldc_w 'native changed to %b'
    //   70: iconst_1
    //   71: anewarray java/lang/Object
    //   74: dup
    //   75: iconst_0
    //   76: iload_2
    //   77: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   80: aastore
    //   81: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   84: pop
    //   85: aload_0
    //   86: iload_2
    //   87: invokespecial b : (Z)V
    //   90: aload_0
    //   91: monitorexit
    //   92: return
    //   93: iconst_0
    //   94: istore_2
    //   95: goto -> 59
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   8	40	98	finally
    //   40	59	98	finally
    //   59	90	98	finally
  }
  
  public boolean putKeyValueToNative(String paramString1, String paramString2) {
    boolean bool;
    if (!this.h && !this.i)
      return false; 
    if (!l)
      return false; 
    if (paramString1 == null || paramString2 == null)
      return false; 
    try {
      if (this.i)
        return putNativeKeyValue(paramString1, paramString2); 
      Boolean bool1 = (Boolean)z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "putNativeKeyValue", null, new Class[] { String.class, String.class }, new Object[] { paramString1, paramString2 });
      if (bool1 != null)
        return bool1.booleanValue(); 
      bool = false;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      l = false;
      bool = false;
    } catch (Throwable throwable) {}
    return bool;
  }
  
  protected native boolean putNativeKeyValue(String paramString1, String paramString2);
  
  protected native String regist(String paramString, boolean paramBoolean, int paramInt);
  
  protected native String removeNativeKeyValue(String paramString);
  
  public void setDumpFilePath(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield f : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public boolean setNativeAppChannel(String paramString) {
    return a(12, paramString);
  }
  
  public boolean setNativeAppPackage(String paramString) {
    return a(13, paramString);
  }
  
  public boolean setNativeAppVersion(String paramString) {
    return a(10, paramString);
  }
  
  protected native void setNativeInfo(int paramInt, String paramString);
  
  public boolean setNativeIsAppForeground(boolean paramBoolean) {
    if (paramBoolean) {
      String str1 = "true";
      return a(14, str1);
    } 
    String str = "false";
    return a(14, str);
  }
  
  public boolean setNativeLaunchTime(long paramLong) {
    boolean bool;
    try {
      bool = a(15, String.valueOf(paramLong));
    } catch (NumberFormatException numberFormatException) {}
    return bool;
  }
  
  public boolean setNativeUserId(String paramString) {
    return a(11, paramString);
  }
  
  public void setUserOpened(boolean paramBoolean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: iload_1
    //   6: invokespecial c : (Z)V
    //   9: aload_0
    //   10: invokevirtual isUserOpened : ()Z
    //   13: istore_1
    //   14: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   17: astore_3
    //   18: aload_3
    //   19: ifnull -> 82
    //   22: iload_1
    //   23: ifeq -> 72
    //   26: aload_3
    //   27: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   30: getfield g : Z
    //   33: ifeq -> 72
    //   36: iload_2
    //   37: istore_1
    //   38: iload_1
    //   39: aload_0
    //   40: getfield j : Z
    //   43: if_icmpeq -> 69
    //   46: ldc_w 'native changed to %b'
    //   49: iconst_1
    //   50: anewarray java/lang/Object
    //   53: dup
    //   54: iconst_0
    //   55: iload_1
    //   56: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   59: aastore
    //   60: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_0
    //   65: iload_1
    //   66: invokespecial b : (Z)V
    //   69: aload_0
    //   70: monitorexit
    //   71: return
    //   72: iconst_0
    //   73: istore_1
    //   74: goto -> 38
    //   77: astore_3
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_3
    //   81: athrow
    //   82: goto -> 38
    // Exception table:
    //   from	to	target	type
    //   4	18	77	finally
    //   26	36	77	finally
    //   38	69	77	finally
  }
  
  public void startNativeMonitor() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield i : Z
    //   6: ifne -> 16
    //   9: aload_0
    //   10: getfield h : Z
    //   13: ifeq -> 27
    //   16: aload_0
    //   17: aload_0
    //   18: getfield g : Z
    //   21: invokespecial a : (Z)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: ldc_w 'Bugly'
    //   30: astore_1
    //   31: aload_0
    //   32: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   35: getfield m : Ljava/lang/String;
    //   38: invokestatic a : (Ljava/lang/String;)Z
    //   41: ifne -> 127
    //   44: iconst_1
    //   45: istore_2
    //   46: aload_0
    //   47: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   50: getfield m : Ljava/lang/String;
    //   53: astore_3
    //   54: iload_2
    //   55: ifne -> 132
    //   58: aload_0
    //   59: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   62: invokevirtual getClass : ()Ljava/lang/Class;
    //   65: pop
    //   66: aload_1
    //   67: astore_3
    //   68: aload_0
    //   69: aload_3
    //   70: iload_2
    //   71: invokestatic a : (Ljava/lang/String;Z)Z
    //   74: putfield i : Z
    //   77: aload_0
    //   78: getfield i : Z
    //   81: ifne -> 91
    //   84: aload_0
    //   85: getfield h : Z
    //   88: ifeq -> 24
    //   91: aload_0
    //   92: aload_0
    //   93: getfield g : Z
    //   96: invokespecial a : (Z)V
    //   99: aload_0
    //   100: getfield d : Lcom/tencent/bugly/proguard/w;
    //   103: astore_1
    //   104: new com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler$1
    //   107: astore_3
    //   108: aload_3
    //   109: aload_0
    //   110: invokespecial <init> : (Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;)V
    //   113: aload_1
    //   114: aload_3
    //   115: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   118: pop
    //   119: goto -> 24
    //   122: astore_3
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_3
    //   126: athrow
    //   127: iconst_0
    //   128: istore_2
    //   129: goto -> 46
    //   132: goto -> 68
    // Exception table:
    //   from	to	target	type
    //   2	16	122	finally
    //   16	24	122	finally
    //   31	44	122	finally
    //   46	54	122	finally
    //   58	66	122	finally
    //   68	91	122	finally
    //   91	119	122	finally
  }
  
  protected native void testCrash();
  
  public void testNativeCrash() {
    if (!this.i) {
      x.d("[Native] Bugly SO file has not been load.", new Object[0]);
      return;
    } 
    testCrash();
  }
  
  protected native String unregist();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/jni/NativeCrashHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */