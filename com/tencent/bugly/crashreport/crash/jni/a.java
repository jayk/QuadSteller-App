package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.b;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.Map;

public final class a implements NativeExceptionHandler {
  private final Context a;
  
  private final b b;
  
  private final com.tencent.bugly.crashreport.common.info.a c;
  
  private final com.tencent.bugly.crashreport.common.strategy.a d;
  
  public a(Context paramContext, com.tencent.bugly.crashreport.common.info.a parama, b paramb, com.tencent.bugly.crashreport.common.strategy.a parama1) {
    this.a = paramContext;
    this.b = paramb;
    this.c = parama;
    this.d = parama1;
  }
  
  public final void handleNativeException(int paramInt1, int paramInt2, long paramLong1, long paramLong2, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt3, String paramString5, int paramInt4, int paramInt5, int paramInt6, String paramString6, String paramString7) {
    x.a("Native Crash Happen v1", new Object[0]);
    handleNativeException2(paramInt1, paramInt2, paramLong1, paramLong2, paramString1, paramString2, paramString3, paramString4, paramInt3, paramString5, paramInt4, paramInt5, paramInt6, paramString6, paramString7, null);
  }
  
  public final void handleNativeException2(int paramInt1, int paramInt2, long paramLong1, long paramLong2, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt3, String paramString5, int paramInt4, int paramInt5, int paramInt6, String paramString6, String paramString7, String[] paramArrayOfString) {
    // Byte code:
    //   0: ldc 'Native Crash Happen v2'
    //   2: iconst_0
    //   3: anewarray java/lang/Object
    //   6: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   9: pop
    //   10: aload_0
    //   11: getfield d : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   14: invokevirtual b : ()Z
    //   17: ifne -> 63
    //   20: ldc 'waiting for remote sync'
    //   22: iconst_0
    //   23: anewarray java/lang/Object
    //   26: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   29: pop
    //   30: iconst_0
    //   31: istore_1
    //   32: aload_0
    //   33: getfield d : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   36: invokevirtual b : ()Z
    //   39: ifne -> 63
    //   42: ldc2_w 500
    //   45: invokestatic b : (J)V
    //   48: iload_1
    //   49: sipush #500
    //   52: iadd
    //   53: istore_2
    //   54: iload_2
    //   55: istore_1
    //   56: iload_2
    //   57: sipush #3000
    //   60: if_icmplt -> 32
    //   63: aload #9
    //   65: invokestatic a : (Ljava/lang/String;)Ljava/lang/String;
    //   68: astore #19
    //   70: ldc 'UNKNOWN'
    //   72: astore #16
    //   74: iload #11
    //   76: ifle -> 254
    //   79: new java/lang/StringBuilder
    //   82: astore #9
    //   84: aload #9
    //   86: invokespecial <init> : ()V
    //   89: aload #9
    //   91: aload #7
    //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: ldc '('
    //   98: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: aload #12
    //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: ldc ')'
    //   108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: invokevirtual toString : ()Ljava/lang/String;
    //   114: astore #7
    //   116: ldc 'KERNEL'
    //   118: astore #9
    //   120: aload #16
    //   122: astore #12
    //   124: aload_0
    //   125: getfield d : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   128: invokevirtual b : ()Z
    //   131: ifne -> 144
    //   134: ldc 'no remote but still store!'
    //   136: iconst_0
    //   137: anewarray java/lang/Object
    //   140: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   143: pop
    //   144: aload_0
    //   145: getfield d : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   148: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   151: getfield g : Z
    //   154: ifne -> 333
    //   157: aload_0
    //   158: getfield d : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   161: invokevirtual b : ()Z
    //   164: ifeq -> 333
    //   167: ldc 'crash report was closed by remote , will not upload to Bugly , print local for helpful!'
    //   169: iconst_0
    //   170: anewarray java/lang/Object
    //   173: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   176: pop
    //   177: invokestatic a : ()Ljava/lang/String;
    //   180: astore #12
    //   182: aload_0
    //   183: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   186: getfield d : Ljava/lang/String;
    //   189: astore #16
    //   191: invokestatic currentThread : ()Ljava/lang/Thread;
    //   194: astore #17
    //   196: new java/lang/StringBuilder
    //   199: astore #9
    //   201: aload #9
    //   203: invokespecial <init> : ()V
    //   206: ldc 'NATIVE_CRASH'
    //   208: aload #12
    //   210: aload #16
    //   212: aload #17
    //   214: aload #9
    //   216: aload #7
    //   218: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: ldc '\\n'
    //   223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: aload #8
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: ldc '\\n'
    //   233: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: aload #19
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: invokevirtual toString : ()Ljava/lang/String;
    //   244: aconst_null
    //   245: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Thread;Ljava/lang/String;Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   248: aload #10
    //   250: invokestatic b : (Ljava/lang/String;)V
    //   253: return
    //   254: iload #13
    //   256: ifle -> 272
    //   259: aload_0
    //   260: getfield a : Landroid/content/Context;
    //   263: astore #9
    //   265: iload #13
    //   267: invokestatic a : (I)Ljava/lang/String;
    //   270: astore #16
    //   272: aload #16
    //   274: iload #13
    //   276: invokestatic valueOf : (I)Ljava/lang/String;
    //   279: invokevirtual equals : (Ljava/lang/Object;)Z
    //   282: ifne -> 895
    //   285: new java/lang/StringBuilder
    //   288: astore #9
    //   290: aload #9
    //   292: invokespecial <init> : ()V
    //   295: aload #9
    //   297: aload #16
    //   299: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: ldc '('
    //   304: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   307: iload #13
    //   309: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   312: ldc ')'
    //   314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: invokevirtual toString : ()Ljava/lang/String;
    //   320: astore #16
    //   322: aload #12
    //   324: astore #9
    //   326: aload #16
    //   328: astore #12
    //   330: goto -> 124
    //   333: new java/util/HashMap
    //   336: astore #20
    //   338: aload #20
    //   340: invokespecial <init> : ()V
    //   343: aload #18
    //   345: ifnull -> 439
    //   348: aload #18
    //   350: arraylength
    //   351: istore_2
    //   352: iconst_0
    //   353: istore_1
    //   354: iload_1
    //   355: iload_2
    //   356: if_icmpge -> 449
    //   359: aload #18
    //   361: iload_1
    //   362: aaload
    //   363: astore #16
    //   365: aload #16
    //   367: ldc '='
    //   369: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   372: astore #21
    //   374: aload #21
    //   376: arraylength
    //   377: iconst_2
    //   378: if_icmpne -> 403
    //   381: aload #20
    //   383: aload #21
    //   385: iconst_0
    //   386: aaload
    //   387: aload #21
    //   389: iconst_1
    //   390: aaload
    //   391: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   396: pop
    //   397: iinc #1, 1
    //   400: goto -> 354
    //   403: ldc 'bad extraMsg %s'
    //   405: iconst_1
    //   406: anewarray java/lang/Object
    //   409: dup
    //   410: iconst_0
    //   411: aload #16
    //   413: aastore
    //   414: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   417: pop
    //   418: goto -> 397
    //   421: astore #7
    //   423: aload #7
    //   425: invokestatic a : (Ljava/lang/Throwable;)Z
    //   428: ifne -> 253
    //   431: aload #7
    //   433: invokevirtual printStackTrace : ()V
    //   436: goto -> 253
    //   439: ldc 'not found extraMsg'
    //   441: iconst_0
    //   442: anewarray java/lang/Object
    //   445: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   448: pop
    //   449: aload #20
    //   451: ldc 'ExceptionProcessName'
    //   453: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   458: checkcast java/lang/String
    //   461: astore #18
    //   463: aload #18
    //   465: ifnull -> 476
    //   468: aload #18
    //   470: invokevirtual length : ()I
    //   473: ifne -> 629
    //   476: aload_0
    //   477: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   480: getfield d : Ljava/lang/String;
    //   483: astore #18
    //   485: aload #20
    //   487: ldc 'ExceptionThreadName'
    //   489: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   494: checkcast java/lang/String
    //   497: astore #16
    //   499: aload #16
    //   501: ifnull -> 512
    //   504: aload #16
    //   506: invokevirtual length : ()I
    //   509: ifne -> 647
    //   512: invokestatic currentThread : ()Ljava/lang/Thread;
    //   515: astore #16
    //   517: new java/lang/StringBuilder
    //   520: astore #21
    //   522: aload #21
    //   524: invokespecial <init> : ()V
    //   527: aload #21
    //   529: aload #16
    //   531: invokevirtual getName : ()Ljava/lang/String;
    //   534: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   537: ldc '('
    //   539: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   542: aload #16
    //   544: invokevirtual getId : ()J
    //   547: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   550: ldc ')'
    //   552: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   555: invokevirtual toString : ()Ljava/lang/String;
    //   558: astore #16
    //   560: aload_0
    //   561: aload #18
    //   563: aload #16
    //   565: lload #5
    //   567: ldc2_w 1000
    //   570: ldiv
    //   571: ldc2_w 1000
    //   574: lload_3
    //   575: lmul
    //   576: ladd
    //   577: aload #7
    //   579: aload #8
    //   581: aload #19
    //   583: aload #9
    //   585: aload #12
    //   587: aload #10
    //   589: aload #20
    //   591: ldc 'SysLogPath'
    //   593: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   598: checkcast java/lang/String
    //   601: aload #17
    //   603: aconst_null
    //   604: aconst_null
    //   605: iconst_1
    //   606: invokevirtual packageCrashDatas : (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/util/Map;Z)Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;
    //   609: astore #16
    //   611: aload #16
    //   613: ifnonnull -> 755
    //   616: ldc 'pkg crash datas fail!'
    //   618: iconst_0
    //   619: anewarray java/lang/Object
    //   622: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   625: pop
    //   626: goto -> 253
    //   629: ldc 'crash process name change to %s'
    //   631: iconst_1
    //   632: anewarray java/lang/Object
    //   635: dup
    //   636: iconst_0
    //   637: aload #18
    //   639: aastore
    //   640: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   643: pop
    //   644: goto -> 485
    //   647: ldc 'crash thread name change to %s'
    //   649: iconst_1
    //   650: anewarray java/lang/Object
    //   653: dup
    //   654: iconst_0
    //   655: aload #16
    //   657: aastore
    //   658: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   661: pop
    //   662: invokestatic getAllStackTraces : ()Ljava/util/Map;
    //   665: invokeinterface keySet : ()Ljava/util/Set;
    //   670: invokeinterface iterator : ()Ljava/util/Iterator;
    //   675: astore #22
    //   677: aload #22
    //   679: invokeinterface hasNext : ()Z
    //   684: ifeq -> 892
    //   687: aload #22
    //   689: invokeinterface next : ()Ljava/lang/Object;
    //   694: checkcast java/lang/Thread
    //   697: astore #21
    //   699: aload #21
    //   701: invokevirtual getName : ()Ljava/lang/String;
    //   704: aload #16
    //   706: invokevirtual equals : (Ljava/lang/Object;)Z
    //   709: ifeq -> 677
    //   712: new java/lang/StringBuilder
    //   715: astore #22
    //   717: aload #22
    //   719: invokespecial <init> : ()V
    //   722: aload #22
    //   724: aload #16
    //   726: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   729: ldc '('
    //   731: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   734: aload #21
    //   736: invokevirtual getId : ()J
    //   739: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   742: ldc ')'
    //   744: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   747: invokevirtual toString : ()Ljava/lang/String;
    //   750: astore #16
    //   752: goto -> 560
    //   755: invokestatic a : ()Ljava/lang/String;
    //   758: astore #12
    //   760: aload_0
    //   761: getfield c : Lcom/tencent/bugly/crashreport/common/info/a;
    //   764: getfield d : Ljava/lang/String;
    //   767: astore #9
    //   769: invokestatic currentThread : ()Ljava/lang/Thread;
    //   772: astore #10
    //   774: new java/lang/StringBuilder
    //   777: astore #17
    //   779: aload #17
    //   781: invokespecial <init> : ()V
    //   784: ldc 'NATIVE_CRASH'
    //   786: aload #12
    //   788: aload #9
    //   790: aload #10
    //   792: aload #17
    //   794: aload #7
    //   796: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   799: ldc '\\n'
    //   801: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   804: aload #8
    //   806: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   809: ldc '\\n'
    //   811: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   814: aload #19
    //   816: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   819: invokevirtual toString : ()Ljava/lang/String;
    //   822: aload #16
    //   824: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Thread;Ljava/lang/String;Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   827: aload_0
    //   828: getfield b : Lcom/tencent/bugly/crashreport/crash/b;
    //   831: aload #16
    //   833: iload #11
    //   835: invokevirtual a : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;I)Z
    //   838: ifne -> 854
    //   841: aload_0
    //   842: getfield b : Lcom/tencent/bugly/crashreport/crash/b;
    //   845: aload #16
    //   847: ldc2_w 3000
    //   850: iconst_1
    //   851: invokevirtual a : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;JZ)V
    //   854: aload_0
    //   855: getfield b : Lcom/tencent/bugly/crashreport/crash/b;
    //   858: aload #16
    //   860: invokevirtual b : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   863: aconst_null
    //   864: astore #7
    //   866: invokestatic getInstance : ()Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   869: astore #8
    //   871: aload #8
    //   873: ifnull -> 883
    //   876: aload #8
    //   878: invokevirtual getDumpFilePath : ()Ljava/lang/String;
    //   881: astore #7
    //   883: iconst_1
    //   884: aload #7
    //   886: invokestatic a : (ZLjava/lang/String;)V
    //   889: goto -> 253
    //   892: goto -> 560
    //   895: aload #12
    //   897: astore #9
    //   899: aload #16
    //   901: astore #12
    //   903: goto -> 124
    // Exception table:
    //   from	to	target	type
    //   10	30	421	java/lang/Throwable
    //   32	48	421	java/lang/Throwable
    //   63	70	421	java/lang/Throwable
    //   79	116	421	java/lang/Throwable
    //   124	144	421	java/lang/Throwable
    //   144	253	421	java/lang/Throwable
    //   259	272	421	java/lang/Throwable
    //   272	322	421	java/lang/Throwable
    //   333	343	421	java/lang/Throwable
    //   348	352	421	java/lang/Throwable
    //   365	397	421	java/lang/Throwable
    //   403	418	421	java/lang/Throwable
    //   439	449	421	java/lang/Throwable
    //   449	463	421	java/lang/Throwable
    //   468	476	421	java/lang/Throwable
    //   476	485	421	java/lang/Throwable
    //   485	499	421	java/lang/Throwable
    //   504	512	421	java/lang/Throwable
    //   512	560	421	java/lang/Throwable
    //   560	611	421	java/lang/Throwable
    //   616	626	421	java/lang/Throwable
    //   629	644	421	java/lang/Throwable
    //   647	677	421	java/lang/Throwable
    //   677	752	421	java/lang/Throwable
    //   755	854	421	java/lang/Throwable
    //   854	863	421	java/lang/Throwable
    //   866	871	421	java/lang/Throwable
    //   876	883	421	java/lang/Throwable
    //   883	889	421	java/lang/Throwable
  }
  
  public final CrashDetailBean packageCrashDatas(String paramString1, String paramString2, long paramLong, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, byte[] paramArrayOfbyte, Map<String, String> paramMap, boolean paramBoolean) {
    boolean bool = c.a().l();
    if (bool)
      x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]); 
    CrashDetailBean crashDetailBean = new CrashDetailBean();
    crashDetailBean.b = 1;
    crashDetailBean.e = this.c.h();
    crashDetailBean.f = this.c.j;
    crashDetailBean.g = this.c.w();
    crashDetailBean.m = this.c.g();
    crashDetailBean.n = paramString3;
    if (bool) {
      paramString3 = " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]";
    } else {
      paramString3 = "";
    } 
    crashDetailBean.o = paramString3;
    crashDetailBean.p = paramString4;
    paramString3 = paramString5;
    if (paramString5 == null)
      paramString3 = ""; 
    crashDetailBean.q = paramString3;
    crashDetailBean.r = paramLong;
    crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
    crashDetailBean.z = paramString1;
    crashDetailBean.A = paramString2;
    crashDetailBean.H = this.c.y();
    crashDetailBean.h = this.c.v();
    crashDetailBean.i = this.c.K();
    crashDetailBean.v = paramString8;
    paramString2 = null;
    NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
    if (nativeCrashHandler != null)
      paramString2 = nativeCrashHandler.getDumpFilePath(); 
    String str = b.a(paramString2, paramString8);
    if (!z.a(str))
      crashDetailBean.T = str; 
    crashDetailBean.U = b.b(paramString2);
    crashDetailBean.w = b.a(paramString9, c.e, (String)null);
    crashDetailBean.I = paramString7;
    crashDetailBean.J = paramString6;
    crashDetailBean.K = paramString10;
    crashDetailBean.E = this.c.p();
    crashDetailBean.F = this.c.o();
    crashDetailBean.G = this.c.q();
    if (paramBoolean) {
      crashDetailBean.B = b.h();
      crashDetailBean.C = b.f();
      crashDetailBean.D = b.j();
      if (crashDetailBean.w == null)
        crashDetailBean.w = z.a(this.a, c.e, null); 
      crashDetailBean.x = y.a();
      crashDetailBean.L = this.c.a;
      crashDetailBean.M = this.c.a();
      crashDetailBean.O = this.c.H();
      crashDetailBean.P = this.c.I();
      crashDetailBean.Q = this.c.B();
      crashDetailBean.R = this.c.G();
      crashDetailBean.y = z.a(c.f, false);
      int i = crashDetailBean.q.indexOf("java:\n");
      if (i > 0) {
        i += "java:\n".length();
        if (i < crashDetailBean.q.length()) {
          str = crashDetailBean.q.substring(i, crashDetailBean.q.length() - 1);
          if (str.length() > 0 && crashDetailBean.y.containsKey(crashDetailBean.A)) {
            paramString2 = (String)crashDetailBean.y.get(crashDetailBean.A);
            int j = paramString2.indexOf(str);
            if (j > 0) {
              paramString2 = paramString2.substring(j);
              crashDetailBean.y.put(crashDetailBean.A, paramString2);
              crashDetailBean.q = crashDetailBean.q.substring(0, i);
              crashDetailBean.q += paramString2;
            } 
          } 
        } 
      } 
      if (paramString1 == null)
        crashDetailBean.z = this.c.d; 
      this.b.c(crashDetailBean);
      return crashDetailBean;
    } 
    crashDetailBean.B = -1L;
    crashDetailBean.C = -1L;
    crashDetailBean.D = -1L;
    if (crashDetailBean.w == null)
      crashDetailBean.w = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc."; 
    crashDetailBean.L = -1L;
    crashDetailBean.O = -1;
    crashDetailBean.P = -1;
    crashDetailBean.Q = paramMap;
    crashDetailBean.R = this.c.G();
    crashDetailBean.y = null;
    if (paramString1 == null)
      crashDetailBean.z = "unknown(record)"; 
    if (paramArrayOfbyte != null)
      crashDetailBean.x = paramArrayOfbyte; 
    return crashDetailBean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/jni/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */