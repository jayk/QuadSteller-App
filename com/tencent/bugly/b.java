package com.tencent.bugly;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.proguard.p;
import java.util.ArrayList;
import java.util.List;

public final class b {
  public static boolean a = true;
  
  public static List<a> b = new ArrayList<a>();
  
  public static boolean c;
  
  private static p d;
  
  private static boolean e;
  
  public static void a(Context paramContext) {
    // Byte code:
    //   0: ldc com/tencent/bugly/b
    //   2: monitorenter
    //   3: aload_0
    //   4: aconst_null
    //   5: invokestatic a : (Landroid/content/Context;Lcom/tencent/bugly/BuglyStrategy;)V
    //   8: ldc com/tencent/bugly/b
    //   10: monitorexit
    //   11: return
    //   12: astore_0
    //   13: ldc com/tencent/bugly/b
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	8	12	finally
  }
  
  public static void a(Context paramContext, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: ldc com/tencent/bugly/b
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/b.e : Z
    //   6: ifeq -> 23
    //   9: ldc '[init] initial Multi-times, ignore this.'
    //   11: iconst_0
    //   12: anewarray java/lang/Object
    //   15: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   18: pop
    //   19: ldc com/tencent/bugly/b
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: ifnonnull -> 45
    //   27: getstatic com/tencent/bugly/proguard/x.a : Ljava/lang/String;
    //   30: ldc '[init] context of init() is null, check it.'
    //   32: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   35: pop
    //   36: goto -> 19
    //   39: astore_0
    //   40: ldc com/tencent/bugly/b
    //   42: monitorexit
    //   43: aload_0
    //   44: athrow
    //   45: aload_0
    //   46: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   49: astore_2
    //   50: aload_2
    //   51: invokestatic a : (Lcom/tencent/bugly/crashreport/common/info/a;)Z
    //   54: ifeq -> 64
    //   57: iconst_0
    //   58: putstatic com/tencent/bugly/b.a : Z
    //   61: goto -> 19
    //   64: aload_2
    //   65: invokevirtual f : ()Ljava/lang/String;
    //   68: astore_3
    //   69: aload_3
    //   70: ifnonnull -> 85
    //   73: getstatic com/tencent/bugly/proguard/x.a : Ljava/lang/String;
    //   76: ldc '[init] meta data of BUGLY_APPID in AndroidManifest.xml should be set.'
    //   78: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   81: pop
    //   82: goto -> 19
    //   85: aload_0
    //   86: aload_3
    //   87: aload_2
    //   88: getfield u : Z
    //   91: aload_1
    //   92: invokestatic a : (Landroid/content/Context;Ljava/lang/String;ZLcom/tencent/bugly/BuglyStrategy;)V
    //   95: goto -> 19
    // Exception table:
    //   from	to	target	type
    //   3	19	39	finally
    //   27	36	39	finally
    //   45	61	39	finally
    //   64	69	39	finally
    //   73	82	39	finally
    //   85	95	39	finally
  }
  
  public static void a(Context paramContext, String paramString, boolean paramBoolean, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: ldc com/tencent/bugly/b
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/b.e : Z
    //   6: ifeq -> 23
    //   9: ldc '[init] initial Multi-times, ignore this.'
    //   11: iconst_0
    //   12: anewarray java/lang/Object
    //   15: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   18: pop
    //   19: ldc com/tencent/bugly/b
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: ifnonnull -> 45
    //   27: getstatic com/tencent/bugly/proguard/x.a : Ljava/lang/String;
    //   30: ldc '[init] context is null, check it.'
    //   32: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   35: pop
    //   36: goto -> 19
    //   39: astore_0
    //   40: ldc com/tencent/bugly/b
    //   42: monitorexit
    //   43: aload_0
    //   44: athrow
    //   45: aload_1
    //   46: ifnonnull -> 61
    //   49: getstatic com/tencent/bugly/proguard/x.a : Ljava/lang/String;
    //   52: ldc 'init arg 'crashReportAppID' should not be null!'
    //   54: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   57: pop
    //   58: goto -> 19
    //   61: iconst_1
    //   62: putstatic com/tencent/bugly/b.e : Z
    //   65: iload_2
    //   66: ifeq -> 157
    //   69: iconst_1
    //   70: putstatic com/tencent/bugly/b.c : Z
    //   73: iconst_1
    //   74: putstatic com/tencent/bugly/proguard/x.b : Z
    //   77: ldc 'Bugly debug模式开启，请在发布时把isDebug关闭。 -- Running in debug model for 'isDebug' is enabled. Please disable it when you release.'
    //   79: iconst_0
    //   80: anewarray java/lang/Object
    //   83: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   86: pop
    //   87: ldc '--------------------------------------------------------------------------------------------'
    //   89: iconst_0
    //   90: anewarray java/lang/Object
    //   93: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   96: pop
    //   97: ldc 'Bugly debug模式将有以下行为特性 -- The following list shows the behaviour of debug model: '
    //   99: iconst_0
    //   100: anewarray java/lang/Object
    //   103: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   106: pop
    //   107: ldc '[1] 输出详细的Bugly SDK的Log -- More detailed log of Bugly SDK will be output to logcat;'
    //   109: iconst_0
    //   110: anewarray java/lang/Object
    //   113: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   116: pop
    //   117: ldc '[2] 每一条Crash都会被立即上报 -- Every crash caught by Bugly will be uploaded immediately.'
    //   119: iconst_0
    //   120: anewarray java/lang/Object
    //   123: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   126: pop
    //   127: ldc '[3] 自定义日志将会在Logcat中输出 -- Custom log will be output to logcat.'
    //   129: iconst_0
    //   130: anewarray java/lang/Object
    //   133: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   136: pop
    //   137: ldc '--------------------------------------------------------------------------------------------'
    //   139: iconst_0
    //   140: anewarray java/lang/Object
    //   143: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   146: pop
    //   147: ldc '[init] Open debug mode of Bugly.'
    //   149: iconst_0
    //   150: anewarray java/lang/Object
    //   153: invokestatic b : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   156: pop
    //   157: ldc '[init] Bugly version: v%s'
    //   159: iconst_1
    //   160: anewarray java/lang/Object
    //   163: dup
    //   164: iconst_0
    //   165: ldc '2.6.6'
    //   167: aastore
    //   168: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   171: pop
    //   172: ldc ' crash report start initializing...'
    //   174: iconst_0
    //   175: anewarray java/lang/Object
    //   178: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   181: pop
    //   182: ldc '[init] Bugly start initializing...'
    //   184: iconst_0
    //   185: anewarray java/lang/Object
    //   188: invokestatic b : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   191: pop
    //   192: ldc '[init] Bugly complete version: v%s'
    //   194: iconst_1
    //   195: anewarray java/lang/Object
    //   198: dup
    //   199: iconst_0
    //   200: ldc '2.6.6'
    //   202: aastore
    //   203: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   206: pop
    //   207: aload_0
    //   208: invokestatic a : (Landroid/content/Context;)Landroid/content/Context;
    //   211: astore #4
    //   213: aload #4
    //   215: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   218: astore #5
    //   220: aload #5
    //   222: invokevirtual t : ()Ljava/lang/String;
    //   225: pop
    //   226: aload #4
    //   228: invokestatic a : (Landroid/content/Context;)V
    //   231: aload #4
    //   233: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   236: invokestatic a : (Landroid/content/Context;Ljava/util/List;)Lcom/tencent/bugly/proguard/p;
    //   239: putstatic com/tencent/bugly/b.d : Lcom/tencent/bugly/proguard/p;
    //   242: aload #4
    //   244: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/proguard/u;
    //   247: pop
    //   248: aload #4
    //   250: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   253: invokestatic a : (Landroid/content/Context;Ljava/util/List;)Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   256: astore #6
    //   258: aload #4
    //   260: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/proguard/n;
    //   263: astore #7
    //   265: aload #5
    //   267: invokestatic a : (Lcom/tencent/bugly/crashreport/common/info/a;)Z
    //   270: ifeq -> 280
    //   273: iconst_0
    //   274: putstatic com/tencent/bugly/b.a : Z
    //   277: goto -> 19
    //   280: aload #5
    //   282: aload_1
    //   283: invokevirtual a : (Ljava/lang/String;)V
    //   286: ldc '[param] Set APP ID:%s'
    //   288: iconst_1
    //   289: anewarray java/lang/Object
    //   292: dup
    //   293: iconst_0
    //   294: aload_1
    //   295: aastore
    //   296: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   299: pop
    //   300: aload_3
    //   301: ifnull -> 656
    //   304: aload_3
    //   305: invokevirtual getAppVersion : ()Ljava/lang/String;
    //   308: astore_0
    //   309: aload_0
    //   310: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   313: ifne -> 384
    //   316: aload_0
    //   317: invokevirtual length : ()I
    //   320: bipush #100
    //   322: if_icmple -> 861
    //   325: aload_0
    //   326: iconst_0
    //   327: bipush #100
    //   329: invokevirtual substring : (II)Ljava/lang/String;
    //   332: astore_1
    //   333: ldc 'appVersion %s length is over limit %d substring to %s'
    //   335: iconst_3
    //   336: anewarray java/lang/Object
    //   339: dup
    //   340: iconst_0
    //   341: aload_0
    //   342: aastore
    //   343: dup
    //   344: iconst_1
    //   345: bipush #100
    //   347: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   350: aastore
    //   351: dup
    //   352: iconst_2
    //   353: aload_1
    //   354: aastore
    //   355: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   358: pop
    //   359: aload_1
    //   360: astore_0
    //   361: aload #5
    //   363: aload_0
    //   364: putfield j : Ljava/lang/String;
    //   367: ldc '[param] Set App version: %s'
    //   369: iconst_1
    //   370: anewarray java/lang/Object
    //   373: dup
    //   374: iconst_0
    //   375: aload_3
    //   376: invokevirtual getAppVersion : ()Ljava/lang/String;
    //   379: aastore
    //   380: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   383: pop
    //   384: aload_3
    //   385: invokevirtual isReplaceOldChannel : ()Z
    //   388: ifeq -> 732
    //   391: aload_3
    //   392: invokevirtual getAppChannel : ()Ljava/lang/String;
    //   395: astore_0
    //   396: aload_0
    //   397: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   400: ifne -> 472
    //   403: aload_0
    //   404: invokevirtual length : ()I
    //   407: bipush #100
    //   409: if_icmple -> 858
    //   412: aload_0
    //   413: iconst_0
    //   414: bipush #100
    //   416: invokevirtual substring : (II)Ljava/lang/String;
    //   419: astore_1
    //   420: ldc 'appChannel %s length is over limit %d substring to %s'
    //   422: iconst_3
    //   423: anewarray java/lang/Object
    //   426: dup
    //   427: iconst_0
    //   428: aload_0
    //   429: aastore
    //   430: dup
    //   431: iconst_1
    //   432: bipush #100
    //   434: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   437: aastore
    //   438: dup
    //   439: iconst_2
    //   440: aload_1
    //   441: aastore
    //   442: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   445: pop
    //   446: aload_1
    //   447: astore_0
    //   448: getstatic com/tencent/bugly/b.d : Lcom/tencent/bugly/proguard/p;
    //   451: sipush #556
    //   454: ldc 'app_channel'
    //   456: aload_0
    //   457: invokevirtual getBytes : ()[B
    //   460: aconst_null
    //   461: iconst_0
    //   462: invokevirtual a : (ILjava/lang/String;[BLcom/tencent/bugly/proguard/o;Z)Z
    //   465: pop
    //   466: aload #5
    //   468: aload_0
    //   469: putfield l : Ljava/lang/String;
    //   472: ldc '[param] Set App channel: %s'
    //   474: iconst_1
    //   475: anewarray java/lang/Object
    //   478: dup
    //   479: iconst_0
    //   480: aload #5
    //   482: getfield l : Ljava/lang/String;
    //   485: aastore
    //   486: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   489: pop
    //   490: aload_3
    //   491: invokevirtual getAppPackageName : ()Ljava/lang/String;
    //   494: astore_1
    //   495: aload_1
    //   496: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   499: ifne -> 568
    //   502: aload_1
    //   503: invokevirtual length : ()I
    //   506: bipush #100
    //   508: if_icmple -> 853
    //   511: aload_1
    //   512: iconst_0
    //   513: bipush #100
    //   515: invokevirtual substring : (II)Ljava/lang/String;
    //   518: astore_0
    //   519: ldc 'appPackageName %s length is over limit %d substring to %s'
    //   521: iconst_3
    //   522: anewarray java/lang/Object
    //   525: dup
    //   526: iconst_0
    //   527: aload_1
    //   528: aastore
    //   529: dup
    //   530: iconst_1
    //   531: bipush #100
    //   533: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   536: aastore
    //   537: dup
    //   538: iconst_2
    //   539: aload_0
    //   540: aastore
    //   541: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   544: pop
    //   545: aload #5
    //   547: aload_0
    //   548: putfield c : Ljava/lang/String;
    //   551: ldc '[param] Set App package: %s'
    //   553: iconst_1
    //   554: anewarray java/lang/Object
    //   557: dup
    //   558: iconst_0
    //   559: aload_3
    //   560: invokevirtual getAppPackageName : ()Ljava/lang/String;
    //   563: aastore
    //   564: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   567: pop
    //   568: aload_3
    //   569: invokevirtual getDeviceID : ()Ljava/lang/String;
    //   572: astore_1
    //   573: aload_1
    //   574: ifnull -> 640
    //   577: aload_1
    //   578: invokevirtual length : ()I
    //   581: bipush #100
    //   583: if_icmple -> 848
    //   586: aload_1
    //   587: iconst_0
    //   588: bipush #100
    //   590: invokevirtual substring : (II)Ljava/lang/String;
    //   593: astore_0
    //   594: ldc 'deviceId %s length is over limit %d substring to %s'
    //   596: iconst_3
    //   597: anewarray java/lang/Object
    //   600: dup
    //   601: iconst_0
    //   602: aload_1
    //   603: aastore
    //   604: dup
    //   605: iconst_1
    //   606: bipush #100
    //   608: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   611: aastore
    //   612: dup
    //   613: iconst_2
    //   614: aload_0
    //   615: aastore
    //   616: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   619: pop
    //   620: aload #5
    //   622: aload_0
    //   623: invokevirtual c : (Ljava/lang/String;)V
    //   626: ldc 's[param] Set device ID: %s'
    //   628: iconst_1
    //   629: anewarray java/lang/Object
    //   632: dup
    //   633: iconst_0
    //   634: aload_0
    //   635: aastore
    //   636: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   639: pop
    //   640: aload #5
    //   642: aload_3
    //   643: invokevirtual isUploadProcess : ()Z
    //   646: putfield e : Z
    //   649: aload_3
    //   650: invokevirtual isBuglyLogUpload : ()Z
    //   653: putstatic com/tencent/bugly/proguard/y.a : Z
    //   656: aload #4
    //   658: aload_3
    //   659: invokestatic a : (Landroid/content/Context;Lcom/tencent/bugly/BuglyStrategy;)V
    //   662: iconst_0
    //   663: istore #8
    //   665: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   668: invokeinterface size : ()I
    //   673: istore #9
    //   675: iload #8
    //   677: iload #9
    //   679: if_icmpge -> 811
    //   682: aload #7
    //   684: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   687: iload #8
    //   689: invokeinterface get : (I)Ljava/lang/Object;
    //   694: checkcast com/tencent/bugly/a
    //   697: getfield id : I
    //   700: invokevirtual a : (I)Z
    //   703: ifeq -> 726
    //   706: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   709: iload #8
    //   711: invokeinterface get : (I)Ljava/lang/Object;
    //   716: checkcast com/tencent/bugly/a
    //   719: aload #4
    //   721: iload_2
    //   722: aload_3
    //   723: invokevirtual init : (Landroid/content/Context;ZLcom/tencent/bugly/BuglyStrategy;)V
    //   726: iinc #8, 1
    //   729: goto -> 665
    //   732: getstatic com/tencent/bugly/b.d : Lcom/tencent/bugly/proguard/p;
    //   735: sipush #556
    //   738: aconst_null
    //   739: iconst_1
    //   740: invokevirtual a : (ILcom/tencent/bugly/proguard/o;Z)Ljava/util/Map;
    //   743: astore_0
    //   744: aload_0
    //   745: ifnull -> 472
    //   748: aload_0
    //   749: ldc 'app_channel'
    //   751: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   756: checkcast [B
    //   759: astore_0
    //   760: aload_0
    //   761: ifnull -> 472
    //   764: new java/lang/String
    //   767: astore_1
    //   768: aload_1
    //   769: aload_0
    //   770: invokespecial <init> : ([B)V
    //   773: aload #5
    //   775: aload_1
    //   776: putfield l : Ljava/lang/String;
    //   779: goto -> 472
    //   782: astore_0
    //   783: getstatic com/tencent/bugly/b.c : Z
    //   786: ifeq -> 490
    //   789: aload_0
    //   790: invokevirtual printStackTrace : ()V
    //   793: goto -> 490
    //   796: astore_0
    //   797: aload_0
    //   798: invokestatic a : (Ljava/lang/Throwable;)Z
    //   801: ifne -> 726
    //   804: aload_0
    //   805: invokevirtual printStackTrace : ()V
    //   808: goto -> 726
    //   811: aload_3
    //   812: ifnull -> 842
    //   815: aload_3
    //   816: invokevirtual getAppReportDelay : ()J
    //   819: lstore #10
    //   821: aload #6
    //   823: lload #10
    //   825: invokevirtual a : (J)V
    //   828: ldc_w '[init] Bugly initialization finished.'
    //   831: iconst_0
    //   832: anewarray java/lang/Object
    //   835: invokestatic b : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   838: pop
    //   839: goto -> 19
    //   842: lconst_0
    //   843: lstore #10
    //   845: goto -> 821
    //   848: aload_1
    //   849: astore_0
    //   850: goto -> 620
    //   853: aload_1
    //   854: astore_0
    //   855: goto -> 545
    //   858: goto -> 448
    //   861: goto -> 361
    // Exception table:
    //   from	to	target	type
    //   3	19	39	finally
    //   27	36	39	finally
    //   49	58	39	finally
    //   61	65	39	finally
    //   69	157	39	finally
    //   157	277	39	finally
    //   280	300	39	finally
    //   304	359	39	finally
    //   361	384	39	finally
    //   384	446	782	java/lang/Exception
    //   384	446	39	finally
    //   448	472	782	java/lang/Exception
    //   448	472	39	finally
    //   472	490	782	java/lang/Exception
    //   472	490	39	finally
    //   490	545	39	finally
    //   545	568	39	finally
    //   568	573	39	finally
    //   577	620	39	finally
    //   620	640	39	finally
    //   640	656	39	finally
    //   656	662	39	finally
    //   665	675	39	finally
    //   682	726	796	java/lang/Throwable
    //   682	726	39	finally
    //   732	744	782	java/lang/Exception
    //   732	744	39	finally
    //   748	760	782	java/lang/Exception
    //   748	760	39	finally
    //   764	779	782	java/lang/Exception
    //   764	779	39	finally
    //   783	793	39	finally
    //   797	808	39	finally
    //   815	821	39	finally
    //   821	839	39	finally
  }
  
  public static void a(a parama) {
    // Byte code:
    //   0: ldc com/tencent/bugly/b
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   6: aload_0
    //   7: invokeinterface contains : (Ljava/lang/Object;)Z
    //   12: ifne -> 25
    //   15: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   18: aload_0
    //   19: invokeinterface add : (Ljava/lang/Object;)Z
    //   24: pop
    //   25: ldc com/tencent/bugly/b
    //   27: monitorexit
    //   28: return
    //   29: astore_0
    //   30: ldc com/tencent/bugly/b
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	25	29	finally
  }
  
  private static boolean a(a parama) {
    List list = parama.o;
    parama.getClass();
    return (list != null && list.contains("bugly"));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */