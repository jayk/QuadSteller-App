package com.tencent.bugly.crashreport.crash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.proguard.a;
import com.tencent.bugly.proguard.aj;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.am;
import com.tencent.bugly.proguard.k;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class b {
  private static int a = 0;
  
  private Context b;
  
  private u c;
  
  private p d;
  
  private a e;
  
  private o f;
  
  private BuglyStrategy.a g;
  
  public b(int paramInt, Context paramContext, u paramu, p paramp, a parama, BuglyStrategy.a parama1, o paramo) {
    a = paramInt;
    this.b = paramContext;
    this.c = paramu;
    this.d = paramp;
    this.e = parama;
    this.g = parama1;
    this.f = paramo;
  }
  
  private static CrashDetailBean a(Cursor paramCursor) {
    if (paramCursor == null)
      return null; 
    try {
      byte[] arrayOfByte = paramCursor.getBlob(paramCursor.getColumnIndex("_dt"));
      if (arrayOfByte == null)
        return null; 
      long l = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
      CrashDetailBean crashDetailBean2 = (CrashDetailBean)z.a(arrayOfByte, CrashDetailBean.CREATOR);
      CrashDetailBean crashDetailBean1 = crashDetailBean2;
      if (crashDetailBean2 != null) {
        crashDetailBean2.a = l;
        crashDetailBean1 = crashDetailBean2;
      } 
    } catch (Throwable throwable) {}
    return (CrashDetailBean)throwable;
  }
  
  private CrashDetailBean a(List<a> paramList, CrashDetailBean paramCrashDetailBean) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 13
    //   4: aload_1
    //   5: invokeinterface size : ()I
    //   10: ifne -> 17
    //   13: aload_2
    //   14: astore_1
    //   15: aload_1
    //   16: areturn
    //   17: aconst_null
    //   18: astore_3
    //   19: new java/util/ArrayList
    //   22: dup
    //   23: bipush #10
    //   25: invokespecial <init> : (I)V
    //   28: astore #4
    //   30: aload_1
    //   31: invokeinterface iterator : ()Ljava/util/Iterator;
    //   36: astore #5
    //   38: aload #5
    //   40: invokeinterface hasNext : ()Z
    //   45: ifeq -> 81
    //   48: aload #5
    //   50: invokeinterface next : ()Ljava/lang/Object;
    //   55: checkcast com/tencent/bugly/crashreport/crash/a
    //   58: astore #6
    //   60: aload #6
    //   62: getfield e : Z
    //   65: ifeq -> 38
    //   68: aload #4
    //   70: aload #6
    //   72: invokeinterface add : (Ljava/lang/Object;)Z
    //   77: pop
    //   78: goto -> 38
    //   81: aload #4
    //   83: invokeinterface size : ()I
    //   88: ifle -> 516
    //   91: aload_0
    //   92: aload #4
    //   94: invokespecial b : (Ljava/util/List;)Ljava/util/List;
    //   97: astore #4
    //   99: aload #4
    //   101: ifnull -> 516
    //   104: aload #4
    //   106: invokeinterface size : ()I
    //   111: ifle -> 516
    //   114: aload #4
    //   116: invokestatic sort : (Ljava/util/List;)V
    //   119: iconst_0
    //   120: istore #7
    //   122: iload #7
    //   124: aload #4
    //   126: invokeinterface size : ()I
    //   131: if_icmpge -> 281
    //   134: aload #4
    //   136: iload #7
    //   138: invokeinterface get : (I)Ljava/lang/Object;
    //   143: checkcast com/tencent/bugly/crashreport/crash/CrashDetailBean
    //   146: astore #6
    //   148: iload #7
    //   150: ifne -> 162
    //   153: aload #6
    //   155: astore_3
    //   156: iinc #7, 1
    //   159: goto -> 122
    //   162: aload #6
    //   164: getfield s : Ljava/lang/String;
    //   167: ifnull -> 513
    //   170: aload #6
    //   172: getfield s : Ljava/lang/String;
    //   175: ldc '\\n'
    //   177: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   180: astore #5
    //   182: aload #5
    //   184: ifnull -> 513
    //   187: aload #5
    //   189: arraylength
    //   190: istore #8
    //   192: iconst_0
    //   193: istore #9
    //   195: iload #9
    //   197: iload #8
    //   199: if_icmpge -> 513
    //   202: aload #5
    //   204: iload #9
    //   206: aaload
    //   207: astore #6
    //   209: aload_3
    //   210: getfield s : Ljava/lang/String;
    //   213: new java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial <init> : ()V
    //   220: aload #6
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: invokevirtual toString : ()Ljava/lang/String;
    //   228: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   231: ifne -> 275
    //   234: aload_3
    //   235: aload_3
    //   236: getfield t : I
    //   239: iconst_1
    //   240: iadd
    //   241: putfield t : I
    //   244: aload_3
    //   245: new java/lang/StringBuilder
    //   248: dup
    //   249: invokespecial <init> : ()V
    //   252: aload_3
    //   253: getfield s : Ljava/lang/String;
    //   256: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: aload #6
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: ldc '\\n'
    //   266: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: invokevirtual toString : ()Ljava/lang/String;
    //   272: putfield s : Ljava/lang/String;
    //   275: iinc #9, 1
    //   278: goto -> 195
    //   281: aload_3
    //   282: ifnonnull -> 510
    //   285: aload_2
    //   286: iconst_1
    //   287: putfield j : Z
    //   290: aload_2
    //   291: iconst_0
    //   292: putfield t : I
    //   295: aload_2
    //   296: ldc ''
    //   298: putfield s : Ljava/lang/String;
    //   301: aload_2
    //   302: astore_3
    //   303: aload_1
    //   304: invokeinterface iterator : ()Ljava/util/Iterator;
    //   309: astore #6
    //   311: aload #6
    //   313: invokeinterface hasNext : ()Z
    //   318: ifeq -> 419
    //   321: aload #6
    //   323: invokeinterface next : ()Ljava/lang/Object;
    //   328: checkcast com/tencent/bugly/crashreport/crash/a
    //   331: astore_1
    //   332: aload_1
    //   333: getfield e : Z
    //   336: ifne -> 311
    //   339: aload_1
    //   340: getfield d : Z
    //   343: ifne -> 311
    //   346: aload_3
    //   347: getfield s : Ljava/lang/String;
    //   350: new java/lang/StringBuilder
    //   353: dup
    //   354: invokespecial <init> : ()V
    //   357: aload_1
    //   358: getfield b : J
    //   361: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   364: invokevirtual toString : ()Ljava/lang/String;
    //   367: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   370: ifne -> 311
    //   373: aload_3
    //   374: aload_3
    //   375: getfield t : I
    //   378: iconst_1
    //   379: iadd
    //   380: putfield t : I
    //   383: aload_3
    //   384: new java/lang/StringBuilder
    //   387: dup
    //   388: invokespecial <init> : ()V
    //   391: aload_3
    //   392: getfield s : Ljava/lang/String;
    //   395: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   398: aload_1
    //   399: getfield b : J
    //   402: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   405: ldc '\\n'
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: invokevirtual toString : ()Ljava/lang/String;
    //   413: putfield s : Ljava/lang/String;
    //   416: goto -> 311
    //   419: aload_3
    //   420: astore_1
    //   421: aload_3
    //   422: getfield r : J
    //   425: aload_2
    //   426: getfield r : J
    //   429: lcmp
    //   430: ifeq -> 15
    //   433: aload_3
    //   434: astore_1
    //   435: aload_3
    //   436: getfield s : Ljava/lang/String;
    //   439: new java/lang/StringBuilder
    //   442: dup
    //   443: invokespecial <init> : ()V
    //   446: aload_2
    //   447: getfield r : J
    //   450: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   453: invokevirtual toString : ()Ljava/lang/String;
    //   456: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   459: ifne -> 15
    //   462: aload_3
    //   463: aload_3
    //   464: getfield t : I
    //   467: iconst_1
    //   468: iadd
    //   469: putfield t : I
    //   472: aload_3
    //   473: new java/lang/StringBuilder
    //   476: dup
    //   477: invokespecial <init> : ()V
    //   480: aload_3
    //   481: getfield s : Ljava/lang/String;
    //   484: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   487: aload_2
    //   488: getfield r : J
    //   491: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   494: ldc '\\n'
    //   496: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   499: invokevirtual toString : ()Ljava/lang/String;
    //   502: putfield s : Ljava/lang/String;
    //   505: aload_3
    //   506: astore_1
    //   507: goto -> 15
    //   510: goto -> 303
    //   513: goto -> 156
    //   516: aconst_null
    //   517: astore_3
    //   518: goto -> 281
  }
  
  private static aj a(String paramString1, Context paramContext, String paramString2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_2
    //   3: ifnull -> 10
    //   6: aload_1
    //   7: ifnonnull -> 24
    //   10: ldc 'rqdp{  createZipAttachment sourcePath == null || context == null ,pls check}'
    //   12: iconst_0
    //   13: anewarray java/lang/Object
    //   16: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   19: pop
    //   20: aload_3
    //   21: astore_0
    //   22: aload_0
    //   23: areturn
    //   24: ldc 'zip %s'
    //   26: iconst_1
    //   27: anewarray java/lang/Object
    //   30: dup
    //   31: iconst_0
    //   32: aload_2
    //   33: aastore
    //   34: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   37: pop
    //   38: new java/io/File
    //   41: dup
    //   42: aload_2
    //   43: invokespecial <init> : (Ljava/lang/String;)V
    //   46: astore_2
    //   47: new java/io/File
    //   50: dup
    //   51: aload_1
    //   52: invokevirtual getCacheDir : ()Ljava/io/File;
    //   55: aload_0
    //   56: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   59: astore #4
    //   61: aload_2
    //   62: aload #4
    //   64: sipush #5000
    //   67: invokestatic a : (Ljava/io/File;Ljava/io/File;I)Z
    //   70: ifne -> 88
    //   73: ldc 'zip fail!'
    //   75: iconst_0
    //   76: anewarray java/lang/Object
    //   79: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   82: pop
    //   83: aload_3
    //   84: astore_0
    //   85: goto -> 22
    //   88: new java/io/ByteArrayOutputStream
    //   91: dup
    //   92: invokespecial <init> : ()V
    //   95: astore #5
    //   97: new java/io/FileInputStream
    //   100: astore_1
    //   101: aload_1
    //   102: aload #4
    //   104: invokespecial <init> : (Ljava/io/File;)V
    //   107: aload_1
    //   108: astore_0
    //   109: sipush #4096
    //   112: newarray byte
    //   114: astore_2
    //   115: aload_1
    //   116: astore_0
    //   117: aload_1
    //   118: aload_2
    //   119: invokevirtual read : ([B)I
    //   122: istore #6
    //   124: iload #6
    //   126: ifle -> 205
    //   129: aload_1
    //   130: astore_0
    //   131: aload #5
    //   133: aload_2
    //   134: iconst_0
    //   135: iload #6
    //   137: invokevirtual write : ([BII)V
    //   140: aload_1
    //   141: astore_0
    //   142: aload #5
    //   144: invokevirtual flush : ()V
    //   147: goto -> 115
    //   150: astore_2
    //   151: aload_1
    //   152: astore_0
    //   153: aload_2
    //   154: invokestatic a : (Ljava/lang/Throwable;)Z
    //   157: ifne -> 166
    //   160: aload_1
    //   161: astore_0
    //   162: aload_2
    //   163: invokevirtual printStackTrace : ()V
    //   166: aload_1
    //   167: ifnull -> 174
    //   170: aload_1
    //   171: invokevirtual close : ()V
    //   174: aload_3
    //   175: astore_0
    //   176: aload #4
    //   178: invokevirtual exists : ()Z
    //   181: ifeq -> 22
    //   184: ldc 'del tmp'
    //   186: iconst_0
    //   187: anewarray java/lang/Object
    //   190: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   193: pop
    //   194: aload #4
    //   196: invokevirtual delete : ()Z
    //   199: pop
    //   200: aload_3
    //   201: astore_0
    //   202: goto -> 22
    //   205: aload_1
    //   206: astore_0
    //   207: aload #5
    //   209: invokevirtual toByteArray : ()[B
    //   212: astore_2
    //   213: aload_1
    //   214: astore_0
    //   215: ldc 'read bytes :%d'
    //   217: iconst_1
    //   218: anewarray java/lang/Object
    //   221: dup
    //   222: iconst_0
    //   223: aload_2
    //   224: arraylength
    //   225: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   228: aastore
    //   229: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   232: pop
    //   233: aload_1
    //   234: astore_0
    //   235: new com/tencent/bugly/proguard/aj
    //   238: dup
    //   239: iconst_2
    //   240: aload #4
    //   242: invokevirtual getName : ()Ljava/lang/String;
    //   245: aload_2
    //   246: invokespecial <init> : (BLjava/lang/String;[B)V
    //   249: astore_2
    //   250: aload_1
    //   251: invokevirtual close : ()V
    //   254: aload #4
    //   256: invokevirtual exists : ()Z
    //   259: ifeq -> 278
    //   262: ldc 'del tmp'
    //   264: iconst_0
    //   265: anewarray java/lang/Object
    //   268: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   271: pop
    //   272: aload #4
    //   274: invokevirtual delete : ()Z
    //   277: pop
    //   278: aload_2
    //   279: astore_0
    //   280: goto -> 22
    //   283: astore_0
    //   284: aload_0
    //   285: invokestatic a : (Ljava/lang/Throwable;)Z
    //   288: ifne -> 254
    //   291: aload_0
    //   292: invokevirtual printStackTrace : ()V
    //   295: goto -> 254
    //   298: astore_0
    //   299: aload_0
    //   300: invokestatic a : (Ljava/lang/Throwable;)Z
    //   303: ifne -> 174
    //   306: aload_0
    //   307: invokevirtual printStackTrace : ()V
    //   310: goto -> 174
    //   313: astore_1
    //   314: aconst_null
    //   315: astore_0
    //   316: aload_0
    //   317: ifnull -> 324
    //   320: aload_0
    //   321: invokevirtual close : ()V
    //   324: aload #4
    //   326: invokevirtual exists : ()Z
    //   329: ifeq -> 348
    //   332: ldc 'del tmp'
    //   334: iconst_0
    //   335: anewarray java/lang/Object
    //   338: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   341: pop
    //   342: aload #4
    //   344: invokevirtual delete : ()Z
    //   347: pop
    //   348: aload_1
    //   349: athrow
    //   350: astore_0
    //   351: aload_0
    //   352: invokestatic a : (Ljava/lang/Throwable;)Z
    //   355: ifne -> 324
    //   358: aload_0
    //   359: invokevirtual printStackTrace : ()V
    //   362: goto -> 324
    //   365: astore_1
    //   366: goto -> 316
    //   369: astore_2
    //   370: aconst_null
    //   371: astore_1
    //   372: goto -> 151
    // Exception table:
    //   from	to	target	type
    //   97	107	369	java/lang/Throwable
    //   97	107	313	finally
    //   109	115	150	java/lang/Throwable
    //   109	115	365	finally
    //   117	124	150	java/lang/Throwable
    //   117	124	365	finally
    //   131	140	150	java/lang/Throwable
    //   131	140	365	finally
    //   142	147	150	java/lang/Throwable
    //   142	147	365	finally
    //   153	160	365	finally
    //   162	166	365	finally
    //   170	174	298	java/io/IOException
    //   207	213	150	java/lang/Throwable
    //   207	213	365	finally
    //   215	233	150	java/lang/Throwable
    //   215	233	365	finally
    //   235	250	150	java/lang/Throwable
    //   235	250	365	finally
    //   250	254	283	java/io/IOException
    //   320	324	350	java/io/IOException
  }
  
  private static ak a(Context paramContext, CrashDetailBean paramCrashDetailBean, a parama) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: ifnull -> 14
    //   6: aload_1
    //   7: ifnull -> 14
    //   10: aload_2
    //   11: ifnonnull -> 29
    //   14: ldc_w 'enExp args == null'
    //   17: iconst_0
    //   18: anewarray java/lang/Object
    //   21: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   24: pop
    //   25: aconst_null
    //   26: astore_0
    //   27: aload_0
    //   28: areturn
    //   29: new com/tencent/bugly/proguard/ak
    //   32: dup
    //   33: invokespecial <init> : ()V
    //   36: astore #4
    //   38: aload_1
    //   39: getfield b : I
    //   42: tableswitch default -> 88, 0 -> 437, 1 -> 467, 2 -> 497, 3 -> 377, 4 -> 527, 5 -> 587, 6 -> 557, 7 -> 407
    //   88: ldc_w 'crash type error! %d'
    //   91: iconst_1
    //   92: anewarray java/lang/Object
    //   95: dup
    //   96: iconst_0
    //   97: aload_1
    //   98: getfield b : I
    //   101: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   104: aastore
    //   105: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   108: pop
    //   109: aload #4
    //   111: aload_1
    //   112: getfield r : J
    //   115: putfield b : J
    //   118: aload #4
    //   120: aload_1
    //   121: getfield n : Ljava/lang/String;
    //   124: putfield c : Ljava/lang/String;
    //   127: aload #4
    //   129: aload_1
    //   130: getfield o : Ljava/lang/String;
    //   133: putfield d : Ljava/lang/String;
    //   136: aload #4
    //   138: aload_1
    //   139: getfield p : Ljava/lang/String;
    //   142: putfield e : Ljava/lang/String;
    //   145: aload #4
    //   147: aload_1
    //   148: getfield q : Ljava/lang/String;
    //   151: putfield g : Ljava/lang/String;
    //   154: aload #4
    //   156: aload_1
    //   157: getfield y : Ljava/util/Map;
    //   160: putfield h : Ljava/util/Map;
    //   163: aload #4
    //   165: aload_1
    //   166: getfield c : Ljava/lang/String;
    //   169: putfield i : Ljava/lang/String;
    //   172: aload #4
    //   174: aconst_null
    //   175: putfield j : Lcom/tencent/bugly/proguard/ai;
    //   178: aload #4
    //   180: aload_1
    //   181: getfield m : Ljava/lang/String;
    //   184: putfield l : Ljava/lang/String;
    //   187: aload #4
    //   189: aload_1
    //   190: getfield e : Ljava/lang/String;
    //   193: putfield m : Ljava/lang/String;
    //   196: aload #4
    //   198: aload_1
    //   199: getfield A : Ljava/lang/String;
    //   202: putfield f : Ljava/lang/String;
    //   205: aload #4
    //   207: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   210: invokevirtual i : ()Ljava/lang/String;
    //   213: putfield t : Ljava/lang/String;
    //   216: aload #4
    //   218: aconst_null
    //   219: putfield n : Lcom/tencent/bugly/proguard/ah;
    //   222: aload_1
    //   223: getfield i : Ljava/util/Map;
    //   226: ifnull -> 617
    //   229: aload_1
    //   230: getfield i : Ljava/util/Map;
    //   233: invokeinterface size : ()I
    //   238: ifle -> 617
    //   241: aload #4
    //   243: new java/util/ArrayList
    //   246: dup
    //   247: invokespecial <init> : ()V
    //   250: putfield o : Ljava/util/ArrayList;
    //   253: aload_1
    //   254: getfield i : Ljava/util/Map;
    //   257: invokeinterface entrySet : ()Ljava/util/Set;
    //   262: invokeinterface iterator : ()Ljava/util/Iterator;
    //   267: astore #5
    //   269: aload #5
    //   271: invokeinterface hasNext : ()Z
    //   276: ifeq -> 617
    //   279: aload #5
    //   281: invokeinterface next : ()Ljava/lang/Object;
    //   286: checkcast java/util/Map$Entry
    //   289: astore #6
    //   291: new com/tencent/bugly/proguard/ah
    //   294: dup
    //   295: invokespecial <init> : ()V
    //   298: astore #7
    //   300: aload #7
    //   302: aload #6
    //   304: invokeinterface getValue : ()Ljava/lang/Object;
    //   309: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   312: getfield a : Ljava/lang/String;
    //   315: putfield a : Ljava/lang/String;
    //   318: aload #7
    //   320: aload #6
    //   322: invokeinterface getValue : ()Ljava/lang/Object;
    //   327: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   330: getfield c : Ljava/lang/String;
    //   333: putfield c : Ljava/lang/String;
    //   336: aload #7
    //   338: aload #6
    //   340: invokeinterface getValue : ()Ljava/lang/Object;
    //   345: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   348: getfield b : Ljava/lang/String;
    //   351: putfield d : Ljava/lang/String;
    //   354: aload #7
    //   356: aload_2
    //   357: invokevirtual r : ()Ljava/lang/String;
    //   360: putfield b : Ljava/lang/String;
    //   363: aload #4
    //   365: getfield o : Ljava/util/ArrayList;
    //   368: aload #7
    //   370: invokevirtual add : (Ljava/lang/Object;)Z
    //   373: pop
    //   374: goto -> 269
    //   377: aload_1
    //   378: getfield j : Z
    //   381: ifeq -> 399
    //   384: ldc_w '203'
    //   387: astore #5
    //   389: aload #4
    //   391: aload #5
    //   393: putfield a : Ljava/lang/String;
    //   396: goto -> 109
    //   399: ldc_w '103'
    //   402: astore #5
    //   404: goto -> 389
    //   407: aload_1
    //   408: getfield j : Z
    //   411: ifeq -> 429
    //   414: ldc_w '208'
    //   417: astore #5
    //   419: aload #4
    //   421: aload #5
    //   423: putfield a : Ljava/lang/String;
    //   426: goto -> 109
    //   429: ldc_w '108'
    //   432: astore #5
    //   434: goto -> 419
    //   437: aload_1
    //   438: getfield j : Z
    //   441: ifeq -> 459
    //   444: ldc_w '200'
    //   447: astore #5
    //   449: aload #4
    //   451: aload #5
    //   453: putfield a : Ljava/lang/String;
    //   456: goto -> 109
    //   459: ldc_w '100'
    //   462: astore #5
    //   464: goto -> 449
    //   467: aload_1
    //   468: getfield j : Z
    //   471: ifeq -> 489
    //   474: ldc_w '201'
    //   477: astore #5
    //   479: aload #4
    //   481: aload #5
    //   483: putfield a : Ljava/lang/String;
    //   486: goto -> 109
    //   489: ldc_w '101'
    //   492: astore #5
    //   494: goto -> 479
    //   497: aload_1
    //   498: getfield j : Z
    //   501: ifeq -> 519
    //   504: ldc_w '202'
    //   507: astore #5
    //   509: aload #4
    //   511: aload #5
    //   513: putfield a : Ljava/lang/String;
    //   516: goto -> 109
    //   519: ldc_w '102'
    //   522: astore #5
    //   524: goto -> 509
    //   527: aload_1
    //   528: getfield j : Z
    //   531: ifeq -> 549
    //   534: ldc_w '204'
    //   537: astore #5
    //   539: aload #4
    //   541: aload #5
    //   543: putfield a : Ljava/lang/String;
    //   546: goto -> 109
    //   549: ldc_w '104'
    //   552: astore #5
    //   554: goto -> 539
    //   557: aload_1
    //   558: getfield j : Z
    //   561: ifeq -> 579
    //   564: ldc_w '206'
    //   567: astore #5
    //   569: aload #4
    //   571: aload #5
    //   573: putfield a : Ljava/lang/String;
    //   576: goto -> 109
    //   579: ldc_w '106'
    //   582: astore #5
    //   584: goto -> 569
    //   587: aload_1
    //   588: getfield j : Z
    //   591: ifeq -> 609
    //   594: ldc_w '207'
    //   597: astore #5
    //   599: aload #4
    //   601: aload #5
    //   603: putfield a : Ljava/lang/String;
    //   606: goto -> 109
    //   609: ldc_w '107'
    //   612: astore #5
    //   614: goto -> 599
    //   617: aload_1
    //   618: getfield h : Ljava/util/Map;
    //   621: ifnull -> 763
    //   624: aload_1
    //   625: getfield h : Ljava/util/Map;
    //   628: invokeinterface size : ()I
    //   633: ifle -> 763
    //   636: aload #4
    //   638: new java/util/ArrayList
    //   641: dup
    //   642: invokespecial <init> : ()V
    //   645: putfield p : Ljava/util/ArrayList;
    //   648: aload_1
    //   649: getfield h : Ljava/util/Map;
    //   652: invokeinterface entrySet : ()Ljava/util/Set;
    //   657: invokeinterface iterator : ()Ljava/util/Iterator;
    //   662: astore #6
    //   664: aload #6
    //   666: invokeinterface hasNext : ()Z
    //   671: ifeq -> 763
    //   674: aload #6
    //   676: invokeinterface next : ()Ljava/lang/Object;
    //   681: checkcast java/util/Map$Entry
    //   684: astore #7
    //   686: new com/tencent/bugly/proguard/ah
    //   689: dup
    //   690: invokespecial <init> : ()V
    //   693: astore #5
    //   695: aload #5
    //   697: aload #7
    //   699: invokeinterface getValue : ()Ljava/lang/Object;
    //   704: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   707: getfield a : Ljava/lang/String;
    //   710: putfield a : Ljava/lang/String;
    //   713: aload #5
    //   715: aload #7
    //   717: invokeinterface getValue : ()Ljava/lang/Object;
    //   722: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   725: getfield c : Ljava/lang/String;
    //   728: putfield c : Ljava/lang/String;
    //   731: aload #5
    //   733: aload #7
    //   735: invokeinterface getValue : ()Ljava/lang/Object;
    //   740: checkcast com/tencent/bugly/crashreport/common/info/PlugInBean
    //   743: getfield b : Ljava/lang/String;
    //   746: putfield d : Ljava/lang/String;
    //   749: aload #4
    //   751: getfield p : Ljava/util/ArrayList;
    //   754: aload #5
    //   756: invokevirtual add : (Ljava/lang/Object;)Z
    //   759: pop
    //   760: goto -> 664
    //   763: aload_1
    //   764: getfield j : Z
    //   767: ifeq -> 907
    //   770: aload #4
    //   772: aload_1
    //   773: getfield t : I
    //   776: putfield k : I
    //   779: aload_1
    //   780: getfield s : Ljava/lang/String;
    //   783: ifnull -> 855
    //   786: aload_1
    //   787: getfield s : Ljava/lang/String;
    //   790: invokevirtual length : ()I
    //   793: ifle -> 855
    //   796: aload #4
    //   798: getfield q : Ljava/util/ArrayList;
    //   801: ifnonnull -> 816
    //   804: aload #4
    //   806: new java/util/ArrayList
    //   809: dup
    //   810: invokespecial <init> : ()V
    //   813: putfield q : Ljava/util/ArrayList;
    //   816: aload #4
    //   818: getfield q : Ljava/util/ArrayList;
    //   821: astore #5
    //   823: new com/tencent/bugly/proguard/aj
    //   826: astore #6
    //   828: aload #6
    //   830: iconst_1
    //   831: ldc_w 'alltimes.txt'
    //   834: aload_1
    //   835: getfield s : Ljava/lang/String;
    //   838: ldc_w 'utf-8'
    //   841: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   844: invokespecial <init> : (BLjava/lang/String;[B)V
    //   847: aload #5
    //   849: aload #6
    //   851: invokevirtual add : (Ljava/lang/Object;)Z
    //   854: pop
    //   855: aload #4
    //   857: getfield k : I
    //   860: istore #8
    //   862: aload #4
    //   864: getfield q : Ljava/util/ArrayList;
    //   867: ifnull -> 1540
    //   870: aload #4
    //   872: getfield q : Ljava/util/ArrayList;
    //   875: invokevirtual size : ()I
    //   878: istore #9
    //   880: ldc_w 'crashcount:%d sz:%d'
    //   883: iconst_2
    //   884: anewarray java/lang/Object
    //   887: dup
    //   888: iconst_0
    //   889: iload #8
    //   891: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   894: aastore
    //   895: dup
    //   896: iconst_1
    //   897: iload #9
    //   899: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   902: aastore
    //   903: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   906: pop
    //   907: aload_1
    //   908: getfield w : Ljava/lang/String;
    //   911: ifnull -> 973
    //   914: aload #4
    //   916: getfield q : Ljava/util/ArrayList;
    //   919: ifnonnull -> 934
    //   922: aload #4
    //   924: new java/util/ArrayList
    //   927: dup
    //   928: invokespecial <init> : ()V
    //   931: putfield q : Ljava/util/ArrayList;
    //   934: aload #4
    //   936: getfield q : Ljava/util/ArrayList;
    //   939: astore #6
    //   941: new com/tencent/bugly/proguard/aj
    //   944: astore #5
    //   946: aload #5
    //   948: iconst_1
    //   949: ldc_w 'log.txt'
    //   952: aload_1
    //   953: getfield w : Ljava/lang/String;
    //   956: ldc_w 'utf-8'
    //   959: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   962: invokespecial <init> : (BLjava/lang/String;[B)V
    //   965: aload #6
    //   967: aload #5
    //   969: invokevirtual add : (Ljava/lang/Object;)Z
    //   972: pop
    //   973: aload_1
    //   974: getfield T : Ljava/lang/String;
    //   977: invokestatic a : (Ljava/lang/String;)Z
    //   980: ifne -> 1054
    //   983: aload #4
    //   985: getfield q : Ljava/util/ArrayList;
    //   988: ifnonnull -> 1003
    //   991: aload #4
    //   993: new java/util/ArrayList
    //   996: dup
    //   997: invokespecial <init> : ()V
    //   1000: putfield q : Ljava/util/ArrayList;
    //   1003: new com/tencent/bugly/proguard/aj
    //   1006: astore #5
    //   1008: aload #5
    //   1010: iconst_1
    //   1011: ldc_w 'crashInfos.txt'
    //   1014: aload_1
    //   1015: getfield T : Ljava/lang/String;
    //   1018: ldc_w 'utf-8'
    //   1021: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   1024: invokespecial <init> : (BLjava/lang/String;[B)V
    //   1027: aload #5
    //   1029: ifnull -> 1054
    //   1032: ldc_w 'attach crash infos'
    //   1035: iconst_0
    //   1036: anewarray java/lang/Object
    //   1039: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1042: pop
    //   1043: aload #4
    //   1045: getfield q : Ljava/util/ArrayList;
    //   1048: aload #5
    //   1050: invokevirtual add : (Ljava/lang/Object;)Z
    //   1053: pop
    //   1054: aload_1
    //   1055: getfield U : Ljava/lang/String;
    //   1058: ifnull -> 1121
    //   1061: aload #4
    //   1063: getfield q : Ljava/util/ArrayList;
    //   1066: ifnonnull -> 1081
    //   1069: aload #4
    //   1071: new java/util/ArrayList
    //   1074: dup
    //   1075: invokespecial <init> : ()V
    //   1078: putfield q : Ljava/util/ArrayList;
    //   1081: ldc_w 'backupRecord.zip'
    //   1084: aload_0
    //   1085: aload_1
    //   1086: getfield U : Ljava/lang/String;
    //   1089: invokestatic a : (Ljava/lang/String;Landroid/content/Context;Ljava/lang/String;)Lcom/tencent/bugly/proguard/aj;
    //   1092: astore #5
    //   1094: aload #5
    //   1096: ifnull -> 1121
    //   1099: ldc_w 'attach backup record'
    //   1102: iconst_0
    //   1103: anewarray java/lang/Object
    //   1106: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1109: pop
    //   1110: aload #4
    //   1112: getfield q : Ljava/util/ArrayList;
    //   1115: aload #5
    //   1117: invokevirtual add : (Ljava/lang/Object;)Z
    //   1120: pop
    //   1121: aload_1
    //   1122: getfield x : [B
    //   1125: ifnull -> 1195
    //   1128: aload_1
    //   1129: getfield x : [B
    //   1132: arraylength
    //   1133: ifle -> 1195
    //   1136: new com/tencent/bugly/proguard/aj
    //   1139: dup
    //   1140: iconst_2
    //   1141: ldc_w 'buglylog.zip'
    //   1144: aload_1
    //   1145: getfield x : [B
    //   1148: invokespecial <init> : (BLjava/lang/String;[B)V
    //   1151: astore #5
    //   1153: ldc_w 'attach user log'
    //   1156: iconst_0
    //   1157: anewarray java/lang/Object
    //   1160: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1163: pop
    //   1164: aload #4
    //   1166: getfield q : Ljava/util/ArrayList;
    //   1169: ifnonnull -> 1184
    //   1172: aload #4
    //   1174: new java/util/ArrayList
    //   1177: dup
    //   1178: invokespecial <init> : ()V
    //   1181: putfield q : Ljava/util/ArrayList;
    //   1184: aload #4
    //   1186: getfield q : Ljava/util/ArrayList;
    //   1189: aload #5
    //   1191: invokevirtual add : (Ljava/lang/Object;)Z
    //   1194: pop
    //   1195: aload_1
    //   1196: getfield b : I
    //   1199: iconst_3
    //   1200: if_icmpne -> 1366
    //   1203: aload #4
    //   1205: getfield q : Ljava/util/ArrayList;
    //   1208: ifnonnull -> 1223
    //   1211: aload #4
    //   1213: new java/util/ArrayList
    //   1216: dup
    //   1217: invokespecial <init> : ()V
    //   1220: putfield q : Ljava/util/ArrayList;
    //   1223: aload_1
    //   1224: getfield N : Ljava/util/Map;
    //   1227: ifnull -> 1319
    //   1230: aload_1
    //   1231: getfield N : Ljava/util/Map;
    //   1234: ldc_w 'BUGLY_CR_01'
    //   1237: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   1242: ifeq -> 1319
    //   1245: aload #4
    //   1247: getfield q : Ljava/util/ArrayList;
    //   1250: astore #5
    //   1252: new com/tencent/bugly/proguard/aj
    //   1255: astore #6
    //   1257: aload #6
    //   1259: iconst_1
    //   1260: ldc_w 'anrMessage.txt'
    //   1263: aload_1
    //   1264: getfield N : Ljava/util/Map;
    //   1267: ldc_w 'BUGLY_CR_01'
    //   1270: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1275: checkcast java/lang/String
    //   1278: ldc_w 'utf-8'
    //   1281: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   1284: invokespecial <init> : (BLjava/lang/String;[B)V
    //   1287: aload #5
    //   1289: aload #6
    //   1291: invokevirtual add : (Ljava/lang/Object;)Z
    //   1294: pop
    //   1295: ldc_w 'attach anr message'
    //   1298: iconst_0
    //   1299: anewarray java/lang/Object
    //   1302: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1305: pop
    //   1306: aload_1
    //   1307: getfield N : Ljava/util/Map;
    //   1310: ldc_w 'BUGLY_CR_01'
    //   1313: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1318: pop
    //   1319: aload_1
    //   1320: getfield v : Ljava/lang/String;
    //   1323: ifnull -> 1366
    //   1326: ldc_w 'trace.zip'
    //   1329: aload_0
    //   1330: aload_1
    //   1331: getfield v : Ljava/lang/String;
    //   1334: invokestatic a : (Ljava/lang/String;Landroid/content/Context;Ljava/lang/String;)Lcom/tencent/bugly/proguard/aj;
    //   1337: astore #5
    //   1339: aload #5
    //   1341: ifnull -> 1366
    //   1344: ldc_w 'attach traces'
    //   1347: iconst_0
    //   1348: anewarray java/lang/Object
    //   1351: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1354: pop
    //   1355: aload #4
    //   1357: getfield q : Ljava/util/ArrayList;
    //   1360: aload #5
    //   1362: invokevirtual add : (Ljava/lang/Object;)Z
    //   1365: pop
    //   1366: aload_1
    //   1367: getfield b : I
    //   1370: iconst_1
    //   1371: if_icmpne -> 1438
    //   1374: aload #4
    //   1376: getfield q : Ljava/util/ArrayList;
    //   1379: ifnonnull -> 1394
    //   1382: aload #4
    //   1384: new java/util/ArrayList
    //   1387: dup
    //   1388: invokespecial <init> : ()V
    //   1391: putfield q : Ljava/util/ArrayList;
    //   1394: aload_1
    //   1395: getfield v : Ljava/lang/String;
    //   1398: ifnull -> 1438
    //   1401: ldc_w 'tomb.zip'
    //   1404: aload_0
    //   1405: aload_1
    //   1406: getfield v : Ljava/lang/String;
    //   1409: invokestatic a : (Ljava/lang/String;Landroid/content/Context;Ljava/lang/String;)Lcom/tencent/bugly/proguard/aj;
    //   1412: astore_0
    //   1413: aload_0
    //   1414: ifnull -> 1438
    //   1417: ldc_w 'attach tombs'
    //   1420: iconst_0
    //   1421: anewarray java/lang/Object
    //   1424: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1427: pop
    //   1428: aload #4
    //   1430: getfield q : Ljava/util/ArrayList;
    //   1433: aload_0
    //   1434: invokevirtual add : (Ljava/lang/Object;)Z
    //   1437: pop
    //   1438: aload_2
    //   1439: getfield C : Ljava/util/List;
    //   1442: ifnull -> 1641
    //   1445: aload_2
    //   1446: getfield C : Ljava/util/List;
    //   1449: invokeinterface isEmpty : ()Z
    //   1454: ifne -> 1641
    //   1457: aload #4
    //   1459: getfield q : Ljava/util/ArrayList;
    //   1462: ifnonnull -> 1477
    //   1465: aload #4
    //   1467: new java/util/ArrayList
    //   1470: dup
    //   1471: invokespecial <init> : ()V
    //   1474: putfield q : Ljava/util/ArrayList;
    //   1477: new java/lang/StringBuilder
    //   1480: dup
    //   1481: invokespecial <init> : ()V
    //   1484: astore_0
    //   1485: aload_2
    //   1486: getfield C : Ljava/util/List;
    //   1489: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1494: astore #5
    //   1496: aload #5
    //   1498: invokeinterface hasNext : ()Z
    //   1503: ifeq -> 1591
    //   1506: aload_0
    //   1507: aload #5
    //   1509: invokeinterface next : ()Ljava/lang/Object;
    //   1514: checkcast java/lang/String
    //   1517: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1520: pop
    //   1521: goto -> 1496
    //   1524: astore #5
    //   1526: aload #5
    //   1528: invokevirtual printStackTrace : ()V
    //   1531: aload #4
    //   1533: aconst_null
    //   1534: putfield q : Ljava/util/ArrayList;
    //   1537: goto -> 855
    //   1540: iconst_0
    //   1541: istore #9
    //   1543: goto -> 880
    //   1546: astore #5
    //   1548: aload #5
    //   1550: invokevirtual printStackTrace : ()V
    //   1553: aload #4
    //   1555: aconst_null
    //   1556: putfield q : Ljava/util/ArrayList;
    //   1559: goto -> 973
    //   1562: astore #5
    //   1564: aload #5
    //   1566: invokevirtual printStackTrace : ()V
    //   1569: aconst_null
    //   1570: astore #5
    //   1572: goto -> 1027
    //   1575: astore #5
    //   1577: aload #5
    //   1579: invokevirtual printStackTrace : ()V
    //   1582: aload #4
    //   1584: aconst_null
    //   1585: putfield q : Ljava/util/ArrayList;
    //   1588: goto -> 1306
    //   1591: aload #4
    //   1593: getfield q : Ljava/util/ArrayList;
    //   1596: astore #6
    //   1598: new com/tencent/bugly/proguard/aj
    //   1601: astore #5
    //   1603: aload #5
    //   1605: iconst_1
    //   1606: ldc_w 'martianlog.txt'
    //   1609: aload_0
    //   1610: invokevirtual toString : ()Ljava/lang/String;
    //   1613: ldc_w 'utf-8'
    //   1616: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   1619: invokespecial <init> : (BLjava/lang/String;[B)V
    //   1622: aload #6
    //   1624: aload #5
    //   1626: invokevirtual add : (Ljava/lang/Object;)Z
    //   1629: pop
    //   1630: ldc_w 'attach pageTracingList'
    //   1633: iconst_0
    //   1634: anewarray java/lang/Object
    //   1637: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1640: pop
    //   1641: aload_1
    //   1642: getfield S : [B
    //   1645: ifnull -> 1711
    //   1648: aload_1
    //   1649: getfield S : [B
    //   1652: arraylength
    //   1653: ifle -> 1711
    //   1656: aload #4
    //   1658: getfield q : Ljava/util/ArrayList;
    //   1661: ifnonnull -> 1676
    //   1664: aload #4
    //   1666: new java/util/ArrayList
    //   1669: dup
    //   1670: invokespecial <init> : ()V
    //   1673: putfield q : Ljava/util/ArrayList;
    //   1676: aload #4
    //   1678: getfield q : Ljava/util/ArrayList;
    //   1681: new com/tencent/bugly/proguard/aj
    //   1684: dup
    //   1685: iconst_1
    //   1686: ldc_w 'userExtraByteData'
    //   1689: aload_1
    //   1690: getfield S : [B
    //   1693: invokespecial <init> : (BLjava/lang/String;[B)V
    //   1696: invokevirtual add : (Ljava/lang/Object;)Z
    //   1699: pop
    //   1700: ldc_w 'attach extraData'
    //   1703: iconst_0
    //   1704: anewarray java/lang/Object
    //   1707: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1710: pop
    //   1711: aload #4
    //   1713: new java/util/HashMap
    //   1716: dup
    //   1717: invokespecial <init> : ()V
    //   1720: putfield r : Ljava/util/Map;
    //   1723: aload #4
    //   1725: getfield r : Ljava/util/Map;
    //   1728: ldc_w 'A9'
    //   1731: new java/lang/StringBuilder
    //   1734: dup
    //   1735: invokespecial <init> : ()V
    //   1738: aload_1
    //   1739: getfield B : J
    //   1742: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1745: invokevirtual toString : ()Ljava/lang/String;
    //   1748: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1753: pop
    //   1754: aload #4
    //   1756: getfield r : Ljava/util/Map;
    //   1759: ldc_w 'A11'
    //   1762: new java/lang/StringBuilder
    //   1765: dup
    //   1766: invokespecial <init> : ()V
    //   1769: aload_1
    //   1770: getfield C : J
    //   1773: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1776: invokevirtual toString : ()Ljava/lang/String;
    //   1779: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1784: pop
    //   1785: aload #4
    //   1787: getfield r : Ljava/util/Map;
    //   1790: ldc_w 'A10'
    //   1793: new java/lang/StringBuilder
    //   1796: dup
    //   1797: invokespecial <init> : ()V
    //   1800: aload_1
    //   1801: getfield D : J
    //   1804: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1807: invokevirtual toString : ()Ljava/lang/String;
    //   1810: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1815: pop
    //   1816: aload #4
    //   1818: getfield r : Ljava/util/Map;
    //   1821: ldc_w 'A23'
    //   1824: new java/lang/StringBuilder
    //   1827: dup
    //   1828: invokespecial <init> : ()V
    //   1831: aload_1
    //   1832: getfield f : Ljava/lang/String;
    //   1835: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1838: invokevirtual toString : ()Ljava/lang/String;
    //   1841: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1846: pop
    //   1847: aload #4
    //   1849: getfield r : Ljava/util/Map;
    //   1852: ldc_w 'A7'
    //   1855: new java/lang/StringBuilder
    //   1858: dup
    //   1859: invokespecial <init> : ()V
    //   1862: aload_2
    //   1863: getfield f : Ljava/lang/String;
    //   1866: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1869: invokevirtual toString : ()Ljava/lang/String;
    //   1872: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1877: pop
    //   1878: aload #4
    //   1880: getfield r : Ljava/util/Map;
    //   1883: ldc_w 'A6'
    //   1886: new java/lang/StringBuilder
    //   1889: dup
    //   1890: invokespecial <init> : ()V
    //   1893: aload_2
    //   1894: invokevirtual s : ()Ljava/lang/String;
    //   1897: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1900: invokevirtual toString : ()Ljava/lang/String;
    //   1903: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1908: pop
    //   1909: aload #4
    //   1911: getfield r : Ljava/util/Map;
    //   1914: ldc_w 'A5'
    //   1917: new java/lang/StringBuilder
    //   1920: dup
    //   1921: invokespecial <init> : ()V
    //   1924: aload_2
    //   1925: invokevirtual r : ()Ljava/lang/String;
    //   1928: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1931: invokevirtual toString : ()Ljava/lang/String;
    //   1934: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1939: pop
    //   1940: aload #4
    //   1942: getfield r : Ljava/util/Map;
    //   1945: ldc_w 'A22'
    //   1948: new java/lang/StringBuilder
    //   1951: dup
    //   1952: invokespecial <init> : ()V
    //   1955: aload_2
    //   1956: invokevirtual h : ()Ljava/lang/String;
    //   1959: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1962: invokevirtual toString : ()Ljava/lang/String;
    //   1965: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1970: pop
    //   1971: aload #4
    //   1973: getfield r : Ljava/util/Map;
    //   1976: ldc_w 'A2'
    //   1979: new java/lang/StringBuilder
    //   1982: dup
    //   1983: invokespecial <init> : ()V
    //   1986: aload_1
    //   1987: getfield F : J
    //   1990: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   1993: invokevirtual toString : ()Ljava/lang/String;
    //   1996: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2001: pop
    //   2002: aload #4
    //   2004: getfield r : Ljava/util/Map;
    //   2007: ldc_w 'A1'
    //   2010: new java/lang/StringBuilder
    //   2013: dup
    //   2014: invokespecial <init> : ()V
    //   2017: aload_1
    //   2018: getfield E : J
    //   2021: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2024: invokevirtual toString : ()Ljava/lang/String;
    //   2027: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2032: pop
    //   2033: aload #4
    //   2035: getfield r : Ljava/util/Map;
    //   2038: ldc_w 'A24'
    //   2041: new java/lang/StringBuilder
    //   2044: dup
    //   2045: invokespecial <init> : ()V
    //   2048: aload_2
    //   2049: getfield h : Ljava/lang/String;
    //   2052: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2055: invokevirtual toString : ()Ljava/lang/String;
    //   2058: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2063: pop
    //   2064: aload #4
    //   2066: getfield r : Ljava/util/Map;
    //   2069: ldc_w 'A17'
    //   2072: new java/lang/StringBuilder
    //   2075: dup
    //   2076: invokespecial <init> : ()V
    //   2079: aload_1
    //   2080: getfield G : J
    //   2083: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2086: invokevirtual toString : ()Ljava/lang/String;
    //   2089: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2094: pop
    //   2095: aload #4
    //   2097: getfield r : Ljava/util/Map;
    //   2100: ldc_w 'A3'
    //   2103: new java/lang/StringBuilder
    //   2106: dup
    //   2107: invokespecial <init> : ()V
    //   2110: aload_2
    //   2111: invokevirtual k : ()Ljava/lang/String;
    //   2114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2117: invokevirtual toString : ()Ljava/lang/String;
    //   2120: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2125: pop
    //   2126: aload #4
    //   2128: getfield r : Ljava/util/Map;
    //   2131: ldc_w 'A16'
    //   2134: new java/lang/StringBuilder
    //   2137: dup
    //   2138: invokespecial <init> : ()V
    //   2141: aload_2
    //   2142: invokevirtual m : ()Ljava/lang/String;
    //   2145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2148: invokevirtual toString : ()Ljava/lang/String;
    //   2151: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2156: pop
    //   2157: aload #4
    //   2159: getfield r : Ljava/util/Map;
    //   2162: ldc_w 'A25'
    //   2165: new java/lang/StringBuilder
    //   2168: dup
    //   2169: invokespecial <init> : ()V
    //   2172: aload_2
    //   2173: invokevirtual n : ()Ljava/lang/String;
    //   2176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2179: invokevirtual toString : ()Ljava/lang/String;
    //   2182: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2187: pop
    //   2188: aload #4
    //   2190: getfield r : Ljava/util/Map;
    //   2193: ldc_w 'A14'
    //   2196: new java/lang/StringBuilder
    //   2199: dup
    //   2200: invokespecial <init> : ()V
    //   2203: aload_2
    //   2204: invokevirtual l : ()Ljava/lang/String;
    //   2207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2210: invokevirtual toString : ()Ljava/lang/String;
    //   2213: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2218: pop
    //   2219: aload #4
    //   2221: getfield r : Ljava/util/Map;
    //   2224: ldc_w 'A15'
    //   2227: new java/lang/StringBuilder
    //   2230: dup
    //   2231: invokespecial <init> : ()V
    //   2234: aload_2
    //   2235: invokevirtual w : ()Ljava/lang/String;
    //   2238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2241: invokevirtual toString : ()Ljava/lang/String;
    //   2244: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2249: pop
    //   2250: aload #4
    //   2252: getfield r : Ljava/util/Map;
    //   2255: ldc_w 'A13'
    //   2258: new java/lang/StringBuilder
    //   2261: dup
    //   2262: invokespecial <init> : ()V
    //   2265: aload_2
    //   2266: invokevirtual x : ()Ljava/lang/Boolean;
    //   2269: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2272: invokevirtual toString : ()Ljava/lang/String;
    //   2275: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2280: pop
    //   2281: aload #4
    //   2283: getfield r : Ljava/util/Map;
    //   2286: ldc_w 'A34'
    //   2289: new java/lang/StringBuilder
    //   2292: dup
    //   2293: invokespecial <init> : ()V
    //   2296: aload_1
    //   2297: getfield z : Ljava/lang/String;
    //   2300: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2303: invokevirtual toString : ()Ljava/lang/String;
    //   2306: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2311: pop
    //   2312: aload_2
    //   2313: getfield x : Ljava/lang/String;
    //   2316: ifnull -> 2350
    //   2319: aload #4
    //   2321: getfield r : Ljava/util/Map;
    //   2324: ldc_w 'productIdentify'
    //   2327: new java/lang/StringBuilder
    //   2330: dup
    //   2331: invokespecial <init> : ()V
    //   2334: aload_2
    //   2335: getfield x : Ljava/lang/String;
    //   2338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2341: invokevirtual toString : ()Ljava/lang/String;
    //   2344: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2349: pop
    //   2350: aload #4
    //   2352: getfield r : Ljava/util/Map;
    //   2355: astore #5
    //   2357: new java/lang/StringBuilder
    //   2360: astore_0
    //   2361: aload_0
    //   2362: invokespecial <init> : ()V
    //   2365: aload #5
    //   2367: ldc_w 'A26'
    //   2370: aload_0
    //   2371: aload_1
    //   2372: getfield H : Ljava/lang/String;
    //   2375: ldc_w 'utf-8'
    //   2378: invokestatic encode : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2381: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2384: invokevirtual toString : ()Ljava/lang/String;
    //   2387: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2392: pop
    //   2393: aload_1
    //   2394: getfield b : I
    //   2397: iconst_1
    //   2398: if_icmpne -> 2494
    //   2401: aload #4
    //   2403: getfield r : Ljava/util/Map;
    //   2406: ldc_w 'A27'
    //   2409: new java/lang/StringBuilder
    //   2412: dup
    //   2413: invokespecial <init> : ()V
    //   2416: aload_1
    //   2417: getfield J : Ljava/lang/String;
    //   2420: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2423: invokevirtual toString : ()Ljava/lang/String;
    //   2426: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2431: pop
    //   2432: aload #4
    //   2434: getfield r : Ljava/util/Map;
    //   2437: ldc_w 'A28'
    //   2440: new java/lang/StringBuilder
    //   2443: dup
    //   2444: invokespecial <init> : ()V
    //   2447: aload_1
    //   2448: getfield I : Ljava/lang/String;
    //   2451: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2454: invokevirtual toString : ()Ljava/lang/String;
    //   2457: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2462: pop
    //   2463: aload #4
    //   2465: getfield r : Ljava/util/Map;
    //   2468: ldc_w 'A29'
    //   2471: new java/lang/StringBuilder
    //   2474: dup
    //   2475: invokespecial <init> : ()V
    //   2478: aload_1
    //   2479: getfield k : Z
    //   2482: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   2485: invokevirtual toString : ()Ljava/lang/String;
    //   2488: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2493: pop
    //   2494: aload #4
    //   2496: getfield r : Ljava/util/Map;
    //   2499: ldc_w 'A30'
    //   2502: new java/lang/StringBuilder
    //   2505: dup
    //   2506: invokespecial <init> : ()V
    //   2509: aload_1
    //   2510: getfield K : Ljava/lang/String;
    //   2513: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2516: invokevirtual toString : ()Ljava/lang/String;
    //   2519: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2524: pop
    //   2525: aload #4
    //   2527: getfield r : Ljava/util/Map;
    //   2530: ldc_w 'A18'
    //   2533: new java/lang/StringBuilder
    //   2536: dup
    //   2537: invokespecial <init> : ()V
    //   2540: aload_1
    //   2541: getfield L : J
    //   2544: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2547: invokevirtual toString : ()Ljava/lang/String;
    //   2550: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2555: pop
    //   2556: aload #4
    //   2558: getfield r : Ljava/util/Map;
    //   2561: astore #5
    //   2563: new java/lang/StringBuilder
    //   2566: dup
    //   2567: invokespecial <init> : ()V
    //   2570: astore_0
    //   2571: aload_1
    //   2572: getfield M : Z
    //   2575: ifne -> 3042
    //   2578: iconst_1
    //   2579: istore #10
    //   2581: aload #5
    //   2583: ldc_w 'A36'
    //   2586: aload_0
    //   2587: iload #10
    //   2589: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   2592: invokevirtual toString : ()Ljava/lang/String;
    //   2595: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2600: pop
    //   2601: aload #4
    //   2603: getfield r : Ljava/util/Map;
    //   2606: ldc_w 'F02'
    //   2609: new java/lang/StringBuilder
    //   2612: dup
    //   2613: invokespecial <init> : ()V
    //   2616: aload_2
    //   2617: getfield q : J
    //   2620: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2623: invokevirtual toString : ()Ljava/lang/String;
    //   2626: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2631: pop
    //   2632: aload #4
    //   2634: getfield r : Ljava/util/Map;
    //   2637: ldc_w 'F03'
    //   2640: new java/lang/StringBuilder
    //   2643: dup
    //   2644: invokespecial <init> : ()V
    //   2647: aload_2
    //   2648: getfield r : J
    //   2651: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2654: invokevirtual toString : ()Ljava/lang/String;
    //   2657: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2662: pop
    //   2663: aload #4
    //   2665: getfield r : Ljava/util/Map;
    //   2668: ldc_w 'F04'
    //   2671: new java/lang/StringBuilder
    //   2674: dup
    //   2675: invokespecial <init> : ()V
    //   2678: aload_2
    //   2679: invokevirtual e : ()Ljava/lang/String;
    //   2682: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2685: invokevirtual toString : ()Ljava/lang/String;
    //   2688: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2693: pop
    //   2694: aload #4
    //   2696: getfield r : Ljava/util/Map;
    //   2699: ldc_w 'F05'
    //   2702: new java/lang/StringBuilder
    //   2705: dup
    //   2706: invokespecial <init> : ()V
    //   2709: aload_2
    //   2710: getfield s : J
    //   2713: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2716: invokevirtual toString : ()Ljava/lang/String;
    //   2719: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2724: pop
    //   2725: aload #4
    //   2727: getfield r : Ljava/util/Map;
    //   2730: ldc_w 'F06'
    //   2733: new java/lang/StringBuilder
    //   2736: dup
    //   2737: invokespecial <init> : ()V
    //   2740: aload_2
    //   2741: getfield p : Ljava/lang/String;
    //   2744: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2747: invokevirtual toString : ()Ljava/lang/String;
    //   2750: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2755: pop
    //   2756: aload #4
    //   2758: getfield r : Ljava/util/Map;
    //   2761: ldc_w 'F08'
    //   2764: new java/lang/StringBuilder
    //   2767: dup
    //   2768: invokespecial <init> : ()V
    //   2771: aload_2
    //   2772: getfield v : Ljava/lang/String;
    //   2775: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2778: invokevirtual toString : ()Ljava/lang/String;
    //   2781: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2786: pop
    //   2787: aload #4
    //   2789: getfield r : Ljava/util/Map;
    //   2792: ldc_w 'F09'
    //   2795: new java/lang/StringBuilder
    //   2798: dup
    //   2799: invokespecial <init> : ()V
    //   2802: aload_2
    //   2803: getfield w : Ljava/lang/String;
    //   2806: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2809: invokevirtual toString : ()Ljava/lang/String;
    //   2812: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2817: pop
    //   2818: aload #4
    //   2820: getfield r : Ljava/util/Map;
    //   2823: ldc_w 'F10'
    //   2826: new java/lang/StringBuilder
    //   2829: dup
    //   2830: invokespecial <init> : ()V
    //   2833: aload_2
    //   2834: getfield t : J
    //   2837: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   2840: invokevirtual toString : ()Ljava/lang/String;
    //   2843: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2848: pop
    //   2849: aload_1
    //   2850: getfield O : I
    //   2853: iflt -> 2887
    //   2856: aload #4
    //   2858: getfield r : Ljava/util/Map;
    //   2861: ldc_w 'C01'
    //   2864: new java/lang/StringBuilder
    //   2867: dup
    //   2868: invokespecial <init> : ()V
    //   2871: aload_1
    //   2872: getfield O : I
    //   2875: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2878: invokevirtual toString : ()Ljava/lang/String;
    //   2881: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2886: pop
    //   2887: aload_1
    //   2888: getfield P : I
    //   2891: iflt -> 2925
    //   2894: aload #4
    //   2896: getfield r : Ljava/util/Map;
    //   2899: ldc_w 'C02'
    //   2902: new java/lang/StringBuilder
    //   2905: dup
    //   2906: invokespecial <init> : ()V
    //   2909: aload_1
    //   2910: getfield P : I
    //   2913: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2916: invokevirtual toString : ()Ljava/lang/String;
    //   2919: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2924: pop
    //   2925: aload_1
    //   2926: getfield Q : Ljava/util/Map;
    //   2929: ifnull -> 3048
    //   2932: aload_1
    //   2933: getfield Q : Ljava/util/Map;
    //   2936: invokeinterface size : ()I
    //   2941: ifle -> 3048
    //   2944: aload_1
    //   2945: getfield Q : Ljava/util/Map;
    //   2948: invokeinterface entrySet : ()Ljava/util/Set;
    //   2953: invokeinterface iterator : ()Ljava/util/Iterator;
    //   2958: astore #5
    //   2960: aload #5
    //   2962: invokeinterface hasNext : ()Z
    //   2967: ifeq -> 3048
    //   2970: aload #5
    //   2972: invokeinterface next : ()Ljava/lang/Object;
    //   2977: checkcast java/util/Map$Entry
    //   2980: astore_0
    //   2981: aload #4
    //   2983: getfield r : Ljava/util/Map;
    //   2986: new java/lang/StringBuilder
    //   2989: dup
    //   2990: ldc_w 'C03_'
    //   2993: invokespecial <init> : (Ljava/lang/String;)V
    //   2996: aload_0
    //   2997: invokeinterface getKey : ()Ljava/lang/Object;
    //   3002: checkcast java/lang/String
    //   3005: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3008: invokevirtual toString : ()Ljava/lang/String;
    //   3011: aload_0
    //   3012: invokeinterface getValue : ()Ljava/lang/Object;
    //   3017: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   3022: pop
    //   3023: goto -> 2960
    //   3026: astore_0
    //   3027: aload_0
    //   3028: invokevirtual printStackTrace : ()V
    //   3031: goto -> 1641
    //   3034: astore_0
    //   3035: aload_0
    //   3036: invokevirtual printStackTrace : ()V
    //   3039: goto -> 2393
    //   3042: iconst_0
    //   3043: istore #10
    //   3045: goto -> 2581
    //   3048: aload_1
    //   3049: getfield R : Ljava/util/Map;
    //   3052: ifnull -> 3149
    //   3055: aload_1
    //   3056: getfield R : Ljava/util/Map;
    //   3059: invokeinterface size : ()I
    //   3064: ifle -> 3149
    //   3067: aload_1
    //   3068: getfield R : Ljava/util/Map;
    //   3071: invokeinterface entrySet : ()Ljava/util/Set;
    //   3076: invokeinterface iterator : ()Ljava/util/Iterator;
    //   3081: astore_0
    //   3082: aload_0
    //   3083: invokeinterface hasNext : ()Z
    //   3088: ifeq -> 3149
    //   3091: aload_0
    //   3092: invokeinterface next : ()Ljava/lang/Object;
    //   3097: checkcast java/util/Map$Entry
    //   3100: astore #5
    //   3102: aload #4
    //   3104: getfield r : Ljava/util/Map;
    //   3107: new java/lang/StringBuilder
    //   3110: dup
    //   3111: ldc_w 'C04_'
    //   3114: invokespecial <init> : (Ljava/lang/String;)V
    //   3117: aload #5
    //   3119: invokeinterface getKey : ()Ljava/lang/Object;
    //   3124: checkcast java/lang/String
    //   3127: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3130: invokevirtual toString : ()Ljava/lang/String;
    //   3133: aload #5
    //   3135: invokeinterface getValue : ()Ljava/lang/Object;
    //   3140: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   3145: pop
    //   3146: goto -> 3082
    //   3149: aload #4
    //   3151: aconst_null
    //   3152: putfield s : Ljava/util/Map;
    //   3155: aload_1
    //   3156: getfield N : Ljava/util/Map;
    //   3159: ifnull -> 3210
    //   3162: aload_1
    //   3163: getfield N : Ljava/util/Map;
    //   3166: invokeinterface size : ()I
    //   3171: ifle -> 3210
    //   3174: aload #4
    //   3176: aload_1
    //   3177: getfield N : Ljava/util/Map;
    //   3180: putfield s : Ljava/util/Map;
    //   3183: ldc_w 'setted message size %d'
    //   3186: iconst_1
    //   3187: anewarray java/lang/Object
    //   3190: dup
    //   3191: iconst_0
    //   3192: aload #4
    //   3194: getfield s : Ljava/util/Map;
    //   3197: invokeinterface size : ()I
    //   3202: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3205: aastore
    //   3206: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   3209: pop
    //   3210: aload_1
    //   3211: getfield n : Ljava/lang/String;
    //   3214: astore_0
    //   3215: aload_1
    //   3216: getfield c : Ljava/lang/String;
    //   3219: astore #5
    //   3221: aload_2
    //   3222: invokevirtual e : ()Ljava/lang/String;
    //   3225: astore_2
    //   3226: aload_1
    //   3227: getfield r : J
    //   3230: aload_1
    //   3231: getfield L : J
    //   3234: lsub
    //   3235: ldc2_w 1000
    //   3238: ldiv
    //   3239: lstore #11
    //   3241: aload_1
    //   3242: getfield k : Z
    //   3245: istore #13
    //   3247: aload_1
    //   3248: getfield M : Z
    //   3251: istore #14
    //   3253: aload_1
    //   3254: getfield j : Z
    //   3257: istore #15
    //   3259: aload_1
    //   3260: getfield b : I
    //   3263: iconst_1
    //   3264: if_icmpne -> 3390
    //   3267: iload_3
    //   3268: istore #10
    //   3270: ldc_w '%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d'
    //   3273: bipush #12
    //   3275: anewarray java/lang/Object
    //   3278: dup
    //   3279: iconst_0
    //   3280: aload_0
    //   3281: aastore
    //   3282: dup
    //   3283: iconst_1
    //   3284: aload #5
    //   3286: aastore
    //   3287: dup
    //   3288: iconst_2
    //   3289: aload_2
    //   3290: aastore
    //   3291: dup
    //   3292: iconst_3
    //   3293: lload #11
    //   3295: invokestatic valueOf : (J)Ljava/lang/Long;
    //   3298: aastore
    //   3299: dup
    //   3300: iconst_4
    //   3301: iload #13
    //   3303: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   3306: aastore
    //   3307: dup
    //   3308: iconst_5
    //   3309: iload #14
    //   3311: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   3314: aastore
    //   3315: dup
    //   3316: bipush #6
    //   3318: iload #15
    //   3320: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   3323: aastore
    //   3324: dup
    //   3325: bipush #7
    //   3327: iload #10
    //   3329: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   3332: aastore
    //   3333: dup
    //   3334: bipush #8
    //   3336: aload_1
    //   3337: getfield t : I
    //   3340: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3343: aastore
    //   3344: dup
    //   3345: bipush #9
    //   3347: aload_1
    //   3348: getfield s : Ljava/lang/String;
    //   3351: aastore
    //   3352: dup
    //   3353: bipush #10
    //   3355: aload_1
    //   3356: getfield d : Z
    //   3359: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   3362: aastore
    //   3363: dup
    //   3364: bipush #11
    //   3366: aload #4
    //   3368: getfield r : Ljava/util/Map;
    //   3371: invokeinterface size : ()I
    //   3376: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3379: aastore
    //   3380: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   3383: pop
    //   3384: aload #4
    //   3386: astore_0
    //   3387: goto -> 27
    //   3390: iconst_0
    //   3391: istore #10
    //   3393: goto -> 3270
    // Exception table:
    //   from	to	target	type
    //   816	855	1524	java/io/UnsupportedEncodingException
    //   934	973	1546	java/io/UnsupportedEncodingException
    //   1003	1027	1562	java/io/UnsupportedEncodingException
    //   1245	1306	1575	java/io/UnsupportedEncodingException
    //   1591	1641	3026	java/io/UnsupportedEncodingException
    //   2350	2393	3034	java/io/UnsupportedEncodingException
  }
  
  private static List<a> a(List<a> paramList) {
    if (paramList == null || paramList.size() == 0)
      return null; 
    long l = System.currentTimeMillis();
    ArrayList<a> arrayList = new ArrayList();
    for (a a1 : paramList) {
      if (a1.d && a1.b <= l - 86400000L)
        arrayList.add(a1); 
    } 
    return arrayList;
  }
  
  public static void a(String paramString1, String paramString2, String paramString3, Thread paramThread, String paramString4, CrashDetailBean paramCrashDetailBean) {
    a a1 = a.b();
    if (a1 != null) {
      x.e("#++++++++++Record By Bugly++++++++++#", new Object[0]);
      x.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
      x.e("# PKG NAME: %s", new Object[] { a1.c });
      x.e("# APP VER: %s", new Object[] { a1.j });
      x.e("# LAUNCH TIME: %s", new Object[] { z.a(new Date((a.b()).a)) });
      x.e("# CRASH TYPE: %s", new Object[] { paramString1 });
      x.e("# CRASH TIME: %s", new Object[] { paramString2 });
      x.e("# CRASH PROCESS: %s", new Object[] { paramString3 });
      if (paramThread != null)
        x.e("# CRASH THREAD: %s", new Object[] { paramThread.getName() }); 
      if (paramCrashDetailBean != null) {
        x.e("# REPORT ID: %s", new Object[] { paramCrashDetailBean.c });
        paramString2 = a1.g;
        if (a1.x().booleanValue()) {
          paramString1 = "ROOTED";
        } else {
          paramString1 = "UNROOT";
        } 
        x.e("# CRASH DEVICE: %s %s", new Object[] { paramString2, paramString1 });
        x.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", new Object[] { Long.valueOf(paramCrashDetailBean.B), Long.valueOf(paramCrashDetailBean.C), Long.valueOf(paramCrashDetailBean.D) });
        x.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", new Object[] { Long.valueOf(paramCrashDetailBean.E), Long.valueOf(paramCrashDetailBean.F), Long.valueOf(paramCrashDetailBean.G) });
        if (!z.a(paramCrashDetailBean.J)) {
          x.e("# EXCEPTION FIRED BY %s %s", new Object[] { paramCrashDetailBean.J, paramCrashDetailBean.I });
        } else if (paramCrashDetailBean.b == 3) {
          if (paramCrashDetailBean.N == null) {
            paramString1 = "null";
          } else {
            paramString1 = paramCrashDetailBean.N.get("BUGLY_CR_01");
          } 
          x.e("# EXCEPTION ANR MESSAGE:\n %s", new Object[] { paramString1 });
        } 
      } 
      if (!z.a(paramString4)) {
        x.e("# CRASH STACK: ", new Object[0]);
        x.e(paramString4, new Object[0]);
      } 
      x.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    } 
  }
  
  public static void a(boolean paramBoolean, List<CrashDetailBean> paramList) {
    if (paramList != null && paramList.size() > 0) {
      x.c("up finish update state %b", new Object[] { Boolean.valueOf(paramBoolean) });
      for (CrashDetailBean crashDetailBean : paramList) {
        x.c("pre uid:%s uc:%d re:%b me:%b", new Object[] { crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j) });
        crashDetailBean.l++;
        crashDetailBean.d = paramBoolean;
        x.c("set uid:%s uc:%d re:%b me:%b", new Object[] { crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j) });
      } 
      for (CrashDetailBean crashDetailBean : paramList)
        c.a().a(crashDetailBean); 
      x.c("update state size %d", new Object[] { Integer.valueOf(paramList.size()) });
    } 
    if (!paramBoolean)
      x.b("[crash] upload fail.", new Object[0]); 
  }
  
  private static a b(Cursor paramCursor) {
    Cursor cursor = null;
    boolean bool = true;
    if (paramCursor == null)
      return (a)cursor; 
    try {
      boolean bool1;
      a a2 = new a();
      this();
      a2.a = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
      a2.b = paramCursor.getLong(paramCursor.getColumnIndex("_tm"));
      a2.c = paramCursor.getString(paramCursor.getColumnIndex("_s1"));
      if (paramCursor.getInt(paramCursor.getColumnIndex("_up")) == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      a2.d = bool1;
      if (paramCursor.getInt(paramCursor.getColumnIndex("_me")) == 1) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      a2.e = bool1;
      a2.f = paramCursor.getInt(paramCursor.getColumnIndex("_uc"));
      a a1 = a2;
    } catch (Throwable throwable) {
      paramCursor = cursor;
    } 
    return (a)paramCursor;
  }
  
  private List<a> b() {
    ArrayList<a> arrayList1;
    null = null;
    ArrayList<a> arrayList2 = new ArrayList();
    try {
      ArrayList<a> arrayList;
      Cursor cursor1;
      Cursor cursor2 = p.a().a("t_cr", new String[] { "_id", "_tm", "_s1", "_up", "_me", "_uc" }, null, null, null, true);
      null = cursor2;
      if (null == null)
        return null; 
      try {
        StringBuilder stringBuilder;
      } catch (Throwable null) {
        return arrayList;
      } finally {
        cursor2 = null;
        arrayList2 = arrayList;
        cursor1 = cursor2;
        if (arrayList2 != null)
          arrayList2.close(); 
      } 
      String str = cursor2.toString();
      return arrayList2;
    } catch (Throwable throwable) {
    
    } finally {
      arrayList2 = null;
      if (arrayList2 != null)
        arrayList2.close(); 
    } 
    try {
    
    } finally {
      arrayList2 = null;
      ArrayList<a> arrayList = arrayList1;
      arrayList1 = arrayList2;
    } 
    return arrayList1;
  }
  
  private List<CrashDetailBean> b(List<a> paramList) {
    if (paramList == null || paramList.size() == 0)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    for (a a1 : paramList)
      stringBuilder.append(" or _id").append(" = ").append(a1.a); 
    String str = stringBuilder.toString();
    null = str;
    if (str.length() > 0)
      null = str.substring(4); 
    stringBuilder.setLength(0);
    try {
      StringBuilder stringBuilder1;
      StringBuilder stringBuilder2;
      Cursor cursor = p.a().a("t_cr", null, null, null, null, true);
      if (cursor == null)
        return null; 
      try {
        ArrayList<CrashDetailBean> arrayList;
      } catch (Throwable throwable) {
        return (List<CrashDetailBean>)stringBuilder1;
      } finally {
        stringBuilder = null;
        stringBuilder2 = stringBuilder1;
        stringBuilder1 = stringBuilder;
        if (stringBuilder2 != null)
          stringBuilder2.close(); 
      } 
      String str1 = stringBuilder.toString();
      if (str1.length() > 0) {
        str1 = str1.substring(4);
        x.d("deleted %s illegle data %d", new Object[] { "t_cr", Integer.valueOf(p.a().a("t_cr", str1, null, null, true)) });
      } 
    } catch (Throwable throwable) {
    
    } finally {
      str = null;
      if (str != null)
        str.close(); 
    } 
    return (List)paramList;
  }
  
  private static void c(List<a> paramList) {
    if (paramList != null && paramList.size() != 0) {
      StringBuilder stringBuilder = new StringBuilder();
      for (a a1 : paramList)
        stringBuilder.append(" or _id").append(" = ").append(a1.a); 
      String str2 = stringBuilder.toString();
      String str1 = str2;
      if (str2.length() > 0)
        str1 = str2.substring(4); 
      stringBuilder.setLength(0);
      try {
        x.c("deleted %s data %d", new Object[] { "t_cr", Integer.valueOf(p.a().a("t_cr", str1, null, null, true)) });
      } catch (Throwable throwable) {}
    } 
  }
  
  private static void d(List<CrashDetailBean> paramList) {
    if (paramList != null)
      try {
        if (paramList.size() != 0) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          for (CrashDetailBean crashDetailBean : paramList)
            stringBuilder.append(" or _id").append(" = ").append(crashDetailBean.a); 
          String str2 = stringBuilder.toString();
          String str1 = str2;
          if (str2.length() > 0)
            str1 = str2.substring(4); 
          stringBuilder.setLength(0);
          x.c("deleted %s data %d", new Object[] { "t_cr", Integer.valueOf(p.a().a("t_cr", str1, null, null, true)) });
        } 
      } catch (Throwable throwable) {} 
  }
  
  private static ContentValues e(CrashDetailBean paramCrashDetailBean) {
    CrashDetailBean crashDetailBean = null;
    boolean bool = true;
    if (paramCrashDetailBean == null)
      return (ContentValues)crashDetailBean; 
    try {
      boolean bool1;
      ContentValues contentValues2 = new ContentValues();
      this();
      if (paramCrashDetailBean.a > 0L)
        contentValues2.put("_id", Long.valueOf(paramCrashDetailBean.a)); 
      contentValues2.put("_tm", Long.valueOf(paramCrashDetailBean.r));
      contentValues2.put("_s1", paramCrashDetailBean.u);
      if (paramCrashDetailBean.d) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      contentValues2.put("_up", Integer.valueOf(bool1));
      if (paramCrashDetailBean.j) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      contentValues2.put("_me", Integer.valueOf(bool1));
      contentValues2.put("_uc", Integer.valueOf(paramCrashDetailBean.l));
      contentValues2.put("_dt", z.a(paramCrashDetailBean));
      ContentValues contentValues1 = contentValues2;
    } catch (Throwable throwable) {
      paramCrashDetailBean = crashDetailBean;
    } 
    return (ContentValues)paramCrashDetailBean;
  }
  
  public final List<CrashDetailBean> a() {
    List<CrashDetailBean> list;
    StrategyBean strategyBean1 = null;
    StrategyBean strategyBean2 = a.a().c();
    if (strategyBean2 == null) {
      x.d("have not synced remote!", new Object[0]);
      return (List<CrashDetailBean>)strategyBean1;
    } 
    if (!strategyBean2.g) {
      x.d("Crashreport remote closed, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
      x.b("[init] WARNING! Crashreport closed by server, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
      return (List<CrashDetailBean>)strategyBean1;
    } 
    long l1 = System.currentTimeMillis();
    long l2 = z.b();
    List<a> list1 = b();
    strategyBean2 = strategyBean1;
    if (list1 != null) {
      strategyBean2 = strategyBean1;
      if (list1.size() > 0) {
        ArrayList<a> arrayList = new ArrayList();
        Iterator<a> iterator = list1.iterator();
        while (iterator.hasNext()) {
          a a1 = iterator.next();
          if (a1.b < l2 - c.g) {
            iterator.remove();
            arrayList.add(a1);
            continue;
          } 
          if (a1.d) {
            if (a1.b >= l1 - 86400000L) {
              iterator.remove();
              continue;
            } 
            if (!a1.e) {
              iterator.remove();
              arrayList.add(a1);
            } 
            continue;
          } 
          if (a1.f >= 3L && a1.b < l1 - 86400000L) {
            iterator.remove();
            arrayList.add(a1);
          } 
        } 
        if (arrayList.size() > 0)
          c(arrayList); 
        arrayList = new ArrayList<a>();
        list = b(list1);
        if (list != null && list.size() > 0) {
          String str = (a.b()).j;
          Iterator<CrashDetailBean> iterator1 = list.iterator();
          while (iterator1.hasNext()) {
            CrashDetailBean crashDetailBean = iterator1.next();
            if (!str.equals(crashDetailBean.f)) {
              iterator1.remove();
              arrayList.add(crashDetailBean);
            } 
          } 
        } 
        if (arrayList.size() > 0)
          d((List)arrayList); 
      } 
    } 
    return list;
  }
  
  public final void a(CrashDetailBean paramCrashDetailBean, long paramLong, boolean paramBoolean) {
    boolean bool = false;
    if (c.l) {
      x.a("try to upload right now", new Object[0]);
      ArrayList<CrashDetailBean> arrayList = new ArrayList();
      arrayList.add(paramCrashDetailBean);
      if (paramCrashDetailBean.b == 7)
        bool = true; 
      a(arrayList, 3000L, paramBoolean, bool, paramBoolean);
    } 
  }
  
  public final void a(List<CrashDetailBean> paramList, long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if ((a.a(this.b)).e && this.c != null && (paramBoolean3 || this.c.b(c.a))) {
      StrategyBean strategyBean = this.e.c();
      if (!strategyBean.g) {
        x.d("remote report is disable!", new Object[0]);
        x.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
        return;
      } 
      if (paramList != null && paramList.size() != 0) {
        String str1;
        String str2;
        char c;
        al al;
        try {
          if (this.c.a) {
            str1 = strategyBean.s;
          } else {
            str1 = ((StrategyBean)str1).t;
          } 
          if (this.c.a) {
            str2 = StrategyBean.c;
          } else {
            str2 = StrategyBean.a;
          } 
          if (this.c.a) {
            c = '̾';
          } else {
            c = 'ɶ';
          } 
          Context context = this.b;
          a a1 = a.b();
          if (context == null || paramList == null || paramList.size() == 0 || a1 == null) {
            x.d("enEXPPkg args == null!", new Object[0]);
            al = null;
          } else {
            al = new al();
            this();
            ArrayList arrayList = new ArrayList();
            this();
            al.a = arrayList;
            Iterator<CrashDetailBean> iterator = paramList.iterator();
            while (true) {
              if (iterator.hasNext()) {
                CrashDetailBean crashDetailBean = iterator.next();
                al.a.add(a(context, crashDetailBean, a1));
                continue;
              } 
              if (al == null) {
                x.d("create eupPkg fail!", new Object[0]);
                return;
              } 
            } 
          } 
          if (al == null) {
            x.d("create eupPkg fail!", new Object[0]);
            return;
          } 
        } catch (Throwable throwable) {
          x.e("req cr error %s", new Object[] { throwable.toString() });
          if (!x.b(throwable))
            throwable.printStackTrace(); 
          return;
        } 
        byte[] arrayOfByte = a.a((k)al);
        if (arrayOfByte == null) {
          x.d("send encode fail!", new Object[0]);
          return;
        } 
        am am = a.a(this.b, c, arrayOfByte);
        if (am == null) {
          x.d("request package is null.", new Object[0]);
          return;
        } 
        t t = new t() {
            public final void a(boolean param1Boolean) {
              b b1 = this.b;
              b.a(param1Boolean, this.a);
            }
          };
        super(this, (List)throwable);
        if (paramBoolean1) {
          this.c.a(a, am, str1, str2, t, paramLong, paramBoolean2);
          return;
        } 
        this.c.a(a, am, str1, str2, t, false);
      } 
    } 
  }
  
  public final boolean a(CrashDetailBean paramCrashDetailBean) {
    return a(paramCrashDetailBean, -123456789);
  }
  
  public final boolean a(CrashDetailBean paramCrashDetailBean, int paramInt) {
    boolean bool = true;
    if (paramCrashDetailBean != null) {
      ArrayList<a> arrayList;
      if (c.m != null && !c.m.isEmpty()) {
        x.c("Crash filter for crash stack is: %s", new Object[] { c.m });
        if (paramCrashDetailBean.q.contains(c.m)) {
          x.d("This crash contains the filter string set. It will not be record and upload.", new Object[0]);
          return bool;
        } 
      } 
      if (c.n != null && !c.n.isEmpty()) {
        x.c("Crash regular filter for crash stack is: %s", new Object[] { c.n });
        if (Pattern.compile(c.n).matcher(paramCrashDetailBean.q).find()) {
          x.d("This crash matches the regular filter string set. It will not be record and upload.", new Object[0]);
          return bool;
        } 
      } 
      paramInt = paramCrashDetailBean.b;
      String str1 = paramCrashDetailBean.n;
      str1 = paramCrashDetailBean.p;
      str1 = paramCrashDetailBean.q;
      long l = paramCrashDetailBean.r;
      str1 = paramCrashDetailBean.m;
      str1 = paramCrashDetailBean.e;
      str1 = paramCrashDetailBean.c;
      if (this.f != null) {
        o o1 = this.f;
        String str = paramCrashDetailBean.z;
        if (!o1.c()) {
          x.d("Crash listener 'onCrashSaving' return 'false' thus will not handle this crash.", new Object[0]);
          return bool;
        } 
      } 
      if (paramCrashDetailBean.b != 2) {
        r r = new r();
        r.b = 1;
        r.c = paramCrashDetailBean.z;
        r.d = paramCrashDetailBean.A;
        r.e = paramCrashDetailBean.r;
        this.d.b(1);
        this.d.a(r);
        x.b("[crash] a crash occur, handling...", new Object[0]);
      } else {
        x.b("[crash] a caught exception occur, handling...", new Object[0]);
      } 
      List<a> list = b();
      String str2 = null;
      str1 = str2;
      if (list != null) {
        str1 = str2;
        if (list.size() > 0) {
          arrayList = new ArrayList(10);
          ArrayList<a> arrayList1 = new ArrayList(10);
          arrayList.addAll(a(list));
          list.removeAll(arrayList);
          if (!com.tencent.bugly.b.c && c.d) {
            Iterator<a> iterator = list.iterator();
            for (paramInt = 0; iterator.hasNext(); paramInt = i) {
              a a1 = iterator.next();
              int i = paramInt;
              if (paramCrashDetailBean.u.equals(a1.c)) {
                if (a1.e)
                  paramInt = 1; 
                arrayList1.add(a1);
                i = paramInt;
              } 
            } 
            if (paramInt != 0 || arrayList1.size() >= c.c) {
              x.a("same crash occur too much do merged!", new Object[0]);
              paramCrashDetailBean = a(arrayList1, paramCrashDetailBean);
              for (a a1 : arrayList1) {
                if (a1.a != paramCrashDetailBean.a)
                  arrayList.add(a1); 
              } 
              d(paramCrashDetailBean);
              c(arrayList);
              x.b("[crash] save crash success. For this device crash many times, it will not upload crashes immediately", new Object[0]);
              return bool;
            } 
          } 
        } 
      } 
      d(paramCrashDetailBean);
      if (arrayList != null && !arrayList.isEmpty())
        c(arrayList); 
      x.b("[crash] save crash success", new Object[0]);
      bool = false;
    } 
    return bool;
  }
  
  public final void b(CrashDetailBean paramCrashDetailBean) {
    if (this.f != null) {
      o o1 = this.f;
      int i = paramCrashDetailBean.b;
    } 
  }
  
  public final void c(CrashDetailBean paramCrashDetailBean) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_0
    //   6: getfield g : Lcom/tencent/bugly/BuglyStrategy$a;
    //   9: ifnonnull -> 19
    //   12: aload_0
    //   13: getfield f : Lcom/tencent/bugly/proguard/o;
    //   16: ifnull -> 4
    //   19: ldc_w '[crash callback] start user's callback:onCrashHandleStart()'
    //   22: iconst_0
    //   23: anewarray java/lang/Object
    //   26: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   29: pop
    //   30: aload_1
    //   31: getfield b : I
    //   34: tableswitch default -> 80, 0 -> 83, 1 -> 508, 2 -> 503, 3 -> 492, 4 -> 513, 5 -> 518, 6 -> 523, 7 -> 497
    //   80: goto -> 4
    //   83: iconst_0
    //   84: istore_2
    //   85: aload_1
    //   86: getfield b : I
    //   89: istore_3
    //   90: aload_1
    //   91: getfield n : Ljava/lang/String;
    //   94: astore #4
    //   96: aload_1
    //   97: getfield p : Ljava/lang/String;
    //   100: astore #4
    //   102: aload_1
    //   103: getfield q : Ljava/lang/String;
    //   106: astore #4
    //   108: aload_1
    //   109: getfield r : J
    //   112: lstore #5
    //   114: aconst_null
    //   115: astore #4
    //   117: aload_0
    //   118: getfield f : Lcom/tencent/bugly/proguard/o;
    //   121: ifnull -> 529
    //   124: aload_0
    //   125: getfield f : Lcom/tencent/bugly/proguard/o;
    //   128: astore #7
    //   130: aload_0
    //   131: getfield f : Lcom/tencent/bugly/proguard/o;
    //   134: invokeinterface b : ()Ljava/lang/String;
    //   139: astore #7
    //   141: aload #7
    //   143: ifnull -> 170
    //   146: new java/util/HashMap
    //   149: astore #4
    //   151: aload #4
    //   153: iconst_1
    //   154: invokespecial <init> : (I)V
    //   157: aload #4
    //   159: ldc_w 'userData'
    //   162: aload #7
    //   164: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   169: pop
    //   170: aload #4
    //   172: ifnull -> 594
    //   175: aload #4
    //   177: invokeinterface size : ()I
    //   182: ifle -> 594
    //   185: new java/util/LinkedHashMap
    //   188: astore #7
    //   190: aload #7
    //   192: aload #4
    //   194: invokeinterface size : ()I
    //   199: invokespecial <init> : (I)V
    //   202: aload_1
    //   203: aload #7
    //   205: putfield N : Ljava/util/Map;
    //   208: aload #4
    //   210: invokeinterface entrySet : ()Ljava/util/Set;
    //   215: invokeinterface iterator : ()Ljava/util/Iterator;
    //   220: astore #8
    //   222: aload #8
    //   224: invokeinterface hasNext : ()Z
    //   229: ifeq -> 594
    //   232: aload #8
    //   234: invokeinterface next : ()Ljava/lang/Object;
    //   239: checkcast java/util/Map$Entry
    //   242: astore #9
    //   244: aload #9
    //   246: invokeinterface getKey : ()Ljava/lang/Object;
    //   251: checkcast java/lang/String
    //   254: invokestatic a : (Ljava/lang/String;)Z
    //   257: ifne -> 222
    //   260: aload #9
    //   262: invokeinterface getKey : ()Ljava/lang/Object;
    //   267: checkcast java/lang/String
    //   270: astore #7
    //   272: aload #7
    //   274: astore #4
    //   276: aload #7
    //   278: invokevirtual length : ()I
    //   281: bipush #100
    //   283: if_icmple -> 320
    //   286: aload #7
    //   288: iconst_0
    //   289: bipush #100
    //   291: invokevirtual substring : (II)Ljava/lang/String;
    //   294: astore #4
    //   296: ldc_w 'setted key length is over limit %d substring to %s'
    //   299: iconst_2
    //   300: anewarray java/lang/Object
    //   303: dup
    //   304: iconst_0
    //   305: bipush #100
    //   307: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   310: aastore
    //   311: dup
    //   312: iconst_1
    //   313: aload #4
    //   315: aastore
    //   316: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   319: pop
    //   320: aload #9
    //   322: invokeinterface getValue : ()Ljava/lang/Object;
    //   327: checkcast java/lang/String
    //   330: invokestatic a : (Ljava/lang/String;)Z
    //   333: ifne -> 561
    //   336: aload #9
    //   338: invokeinterface getValue : ()Ljava/lang/Object;
    //   343: checkcast java/lang/String
    //   346: invokevirtual length : ()I
    //   349: sipush #30000
    //   352: if_icmple -> 561
    //   355: aload #9
    //   357: invokeinterface getValue : ()Ljava/lang/Object;
    //   362: checkcast java/lang/String
    //   365: aload #9
    //   367: invokeinterface getValue : ()Ljava/lang/Object;
    //   372: checkcast java/lang/String
    //   375: invokevirtual length : ()I
    //   378: sipush #30000
    //   381: isub
    //   382: invokevirtual substring : (I)Ljava/lang/String;
    //   385: astore #7
    //   387: ldc_w 'setted %s value length is over limit %d substring'
    //   390: iconst_2
    //   391: anewarray java/lang/Object
    //   394: dup
    //   395: iconst_0
    //   396: aload #4
    //   398: aastore
    //   399: dup
    //   400: iconst_1
    //   401: sipush #30000
    //   404: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   407: aastore
    //   408: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   411: pop
    //   412: aload_1
    //   413: getfield N : Ljava/util/Map;
    //   416: aload #4
    //   418: aload #7
    //   420: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   425: pop
    //   426: ldc_w 'add setted key %s value size:%d'
    //   429: iconst_2
    //   430: anewarray java/lang/Object
    //   433: dup
    //   434: iconst_0
    //   435: aload #4
    //   437: aastore
    //   438: dup
    //   439: iconst_1
    //   440: aload #7
    //   442: invokevirtual length : ()I
    //   445: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   448: aastore
    //   449: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   452: pop
    //   453: goto -> 222
    //   456: astore_1
    //   457: ldc_w 'crash handle callback somthing wrong! %s'
    //   460: iconst_1
    //   461: anewarray java/lang/Object
    //   464: dup
    //   465: iconst_0
    //   466: aload_1
    //   467: invokevirtual getClass : ()Ljava/lang/Class;
    //   470: invokevirtual getName : ()Ljava/lang/String;
    //   473: aastore
    //   474: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   477: pop
    //   478: aload_1
    //   479: invokestatic a : (Ljava/lang/Throwable;)Z
    //   482: ifne -> 4
    //   485: aload_1
    //   486: invokevirtual printStackTrace : ()V
    //   489: goto -> 4
    //   492: iconst_4
    //   493: istore_2
    //   494: goto -> 85
    //   497: bipush #7
    //   499: istore_2
    //   500: goto -> 85
    //   503: iconst_1
    //   504: istore_2
    //   505: goto -> 85
    //   508: iconst_2
    //   509: istore_2
    //   510: goto -> 85
    //   513: iconst_3
    //   514: istore_2
    //   515: goto -> 85
    //   518: iconst_5
    //   519: istore_2
    //   520: goto -> 85
    //   523: bipush #6
    //   525: istore_2
    //   526: goto -> 85
    //   529: aload_0
    //   530: getfield g : Lcom/tencent/bugly/BuglyStrategy$a;
    //   533: ifnull -> 170
    //   536: aload_0
    //   537: getfield g : Lcom/tencent/bugly/BuglyStrategy$a;
    //   540: iload_2
    //   541: aload_1
    //   542: getfield n : Ljava/lang/String;
    //   545: aload_1
    //   546: getfield o : Ljava/lang/String;
    //   549: aload_1
    //   550: getfield q : Ljava/lang/String;
    //   553: invokevirtual onCrashHandleStart : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/util/Map;
    //   556: astore #4
    //   558: goto -> 170
    //   561: new java/lang/StringBuilder
    //   564: astore #7
    //   566: aload #7
    //   568: invokespecial <init> : ()V
    //   571: aload #7
    //   573: aload #9
    //   575: invokeinterface getValue : ()Ljava/lang/Object;
    //   580: checkcast java/lang/String
    //   583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   586: invokevirtual toString : ()Ljava/lang/String;
    //   589: astore #7
    //   591: goto -> 412
    //   594: ldc_w '[crash callback] start user's callback:onCrashHandleStart2GetExtraDatas()'
    //   597: iconst_0
    //   598: anewarray java/lang/Object
    //   601: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   604: pop
    //   605: aconst_null
    //   606: astore #4
    //   608: aload_0
    //   609: getfield f : Lcom/tencent/bugly/proguard/o;
    //   612: ifnull -> 706
    //   615: aload_0
    //   616: getfield f : Lcom/tencent/bugly/proguard/o;
    //   619: invokeinterface a : ()[B
    //   624: astore #4
    //   626: aload_1
    //   627: aload #4
    //   629: putfield S : [B
    //   632: aload_1
    //   633: getfield S : [B
    //   636: ifnull -> 4
    //   639: aload_1
    //   640: getfield S : [B
    //   643: arraylength
    //   644: sipush #30000
    //   647: if_icmple -> 681
    //   650: ldc_w 'extra bytes size %d is over limit %d will drop over part'
    //   653: iconst_2
    //   654: anewarray java/lang/Object
    //   657: dup
    //   658: iconst_0
    //   659: aload_1
    //   660: getfield S : [B
    //   663: arraylength
    //   664: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   667: aastore
    //   668: dup
    //   669: iconst_1
    //   670: sipush #30000
    //   673: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   676: aastore
    //   677: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   680: pop
    //   681: ldc_w 'add extra bytes %d '
    //   684: iconst_1
    //   685: anewarray java/lang/Object
    //   688: dup
    //   689: iconst_0
    //   690: aload_1
    //   691: getfield S : [B
    //   694: arraylength
    //   695: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   698: aastore
    //   699: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   702: pop
    //   703: goto -> 4
    //   706: aload_0
    //   707: getfield g : Lcom/tencent/bugly/BuglyStrategy$a;
    //   710: ifnull -> 626
    //   713: aload_0
    //   714: getfield g : Lcom/tencent/bugly/BuglyStrategy$a;
    //   717: iload_2
    //   718: aload_1
    //   719: getfield n : Ljava/lang/String;
    //   722: aload_1
    //   723: getfield o : Ljava/lang/String;
    //   726: aload_1
    //   727: getfield q : Ljava/lang/String;
    //   730: invokevirtual onCrashHandleStart2GetExtraDatas : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)[B
    //   733: astore #4
    //   735: goto -> 626
    // Exception table:
    //   from	to	target	type
    //   19	80	456	java/lang/Throwable
    //   85	114	456	java/lang/Throwable
    //   117	141	456	java/lang/Throwable
    //   146	170	456	java/lang/Throwable
    //   175	222	456	java/lang/Throwable
    //   222	272	456	java/lang/Throwable
    //   276	320	456	java/lang/Throwable
    //   320	412	456	java/lang/Throwable
    //   412	453	456	java/lang/Throwable
    //   529	558	456	java/lang/Throwable
    //   561	591	456	java/lang/Throwable
    //   594	605	456	java/lang/Throwable
    //   608	626	456	java/lang/Throwable
    //   626	681	456	java/lang/Throwable
    //   681	703	456	java/lang/Throwable
    //   706	735	456	java/lang/Throwable
  }
  
  public final void d(CrashDetailBean paramCrashDetailBean) {
    if (paramCrashDetailBean != null) {
      ContentValues contentValues = e(paramCrashDetailBean);
      if (contentValues != null) {
        long l = p.a().a("t_cr", contentValues, null, true);
        if (l >= 0L) {
          x.c("insert %s success!", new Object[] { "t_cr" });
          paramCrashDetailBean.a = l;
        } 
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */