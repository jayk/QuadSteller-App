package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.biz.UserInfoBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class a {
  protected HashMap<String, HashMap<String, byte[]>> a = new HashMap<String, HashMap<String, byte[]>>();
  
  protected String b;
  
  i c;
  
  private HashMap<String, Object> d;
  
  a() {
    new HashMap<Object, Object>();
    this.d = new HashMap<String, Object>();
    this.b = "GBK";
    this.c = new i();
  }
  
  public static ag a(int paramInt) {
    return (ag)((paramInt == 1) ? new af() : ((paramInt == 3) ? new ae() : null));
  }
  
  public static am a(Context paramContext, int paramInt, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   3: astore_3
    //   4: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   7: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   10: astore #4
    //   12: aload_3
    //   13: ifnull -> 21
    //   16: aload #4
    //   18: ifnonnull -> 35
    //   21: ldc 'Can not create request pkg for parameters is invalid.'
    //   23: iconst_0
    //   24: anewarray java/lang/Object
    //   27: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   30: pop
    //   31: aconst_null
    //   32: astore_0
    //   33: aload_0
    //   34: areturn
    //   35: new com/tencent/bugly/proguard/am
    //   38: astore #5
    //   40: aload #5
    //   42: invokespecial <init> : ()V
    //   45: aload_3
    //   46: monitorenter
    //   47: aload #5
    //   49: iconst_1
    //   50: putfield a : I
    //   53: aload #5
    //   55: aload_3
    //   56: invokevirtual f : ()Ljava/lang/String;
    //   59: putfield b : Ljava/lang/String;
    //   62: aload #5
    //   64: aload_3
    //   65: getfield c : Ljava/lang/String;
    //   68: putfield c : Ljava/lang/String;
    //   71: aload #5
    //   73: aload_3
    //   74: getfield j : Ljava/lang/String;
    //   77: putfield d : Ljava/lang/String;
    //   80: aload #5
    //   82: aload_3
    //   83: getfield l : Ljava/lang/String;
    //   86: putfield e : Ljava/lang/String;
    //   89: aload_3
    //   90: invokevirtual getClass : ()Ljava/lang/Class;
    //   93: pop
    //   94: aload #5
    //   96: ldc '2.6.6'
    //   98: putfield f : Ljava/lang/String;
    //   101: aload #5
    //   103: iload_1
    //   104: putfield g : I
    //   107: aload_2
    //   108: ifnonnull -> 902
    //   111: ldc ''
    //   113: invokevirtual getBytes : ()[B
    //   116: astore #6
    //   118: aload #5
    //   120: aload #6
    //   122: putfield h : [B
    //   125: aload #5
    //   127: aload_3
    //   128: getfield g : Ljava/lang/String;
    //   131: putfield i : Ljava/lang/String;
    //   134: aload #5
    //   136: aload_3
    //   137: getfield h : Ljava/lang/String;
    //   140: putfield j : Ljava/lang/String;
    //   143: new java/util/HashMap
    //   146: astore #6
    //   148: aload #6
    //   150: invokespecial <init> : ()V
    //   153: aload #5
    //   155: aload #6
    //   157: putfield k : Ljava/util/Map;
    //   160: aload #5
    //   162: aload_3
    //   163: invokevirtual e : ()Ljava/lang/String;
    //   166: putfield l : Ljava/lang/String;
    //   169: aload #5
    //   171: aload #4
    //   173: getfield p : J
    //   176: putfield m : J
    //   179: aload #5
    //   181: aload_3
    //   182: invokevirtual h : ()Ljava/lang/String;
    //   185: putfield o : Ljava/lang/String;
    //   188: aload #5
    //   190: aload_0
    //   191: invokestatic f : (Landroid/content/Context;)Ljava/lang/String;
    //   194: putfield p : Ljava/lang/String;
    //   197: aload #5
    //   199: invokestatic currentTimeMillis : ()J
    //   202: putfield q : J
    //   205: new java/lang/StringBuilder
    //   208: astore_0
    //   209: aload_0
    //   210: invokespecial <init> : ()V
    //   213: aload #5
    //   215: aload_0
    //   216: aload_3
    //   217: invokevirtual k : ()Ljava/lang/String;
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: invokevirtual toString : ()Ljava/lang/String;
    //   226: putfield r : Ljava/lang/String;
    //   229: aload #5
    //   231: aload_3
    //   232: invokevirtual j : ()Ljava/lang/String;
    //   235: putfield s : Ljava/lang/String;
    //   238: new java/lang/StringBuilder
    //   241: astore_0
    //   242: aload_0
    //   243: invokespecial <init> : ()V
    //   246: aload #5
    //   248: aload_0
    //   249: aload_3
    //   250: invokevirtual m : ()Ljava/lang/String;
    //   253: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   256: invokevirtual toString : ()Ljava/lang/String;
    //   259: putfield t : Ljava/lang/String;
    //   262: aload #5
    //   264: aload_3
    //   265: invokevirtual l : ()Ljava/lang/String;
    //   268: putfield u : Ljava/lang/String;
    //   271: new java/lang/StringBuilder
    //   274: astore_0
    //   275: aload_0
    //   276: invokespecial <init> : ()V
    //   279: aload #5
    //   281: aload_0
    //   282: aload_3
    //   283: invokevirtual n : ()Ljava/lang/String;
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: invokevirtual toString : ()Ljava/lang/String;
    //   292: putfield v : Ljava/lang/String;
    //   295: aload #5
    //   297: aload #5
    //   299: getfield p : Ljava/lang/String;
    //   302: putfield w : Ljava/lang/String;
    //   305: aload_3
    //   306: invokevirtual getClass : ()Ljava/lang/Class;
    //   309: pop
    //   310: aload #5
    //   312: ldc 'com.tencent.bugly'
    //   314: putfield n : Ljava/lang/String;
    //   317: aload #5
    //   319: getfield k : Ljava/util/Map;
    //   322: astore_0
    //   323: new java/lang/StringBuilder
    //   326: astore #6
    //   328: aload #6
    //   330: invokespecial <init> : ()V
    //   333: aload_0
    //   334: ldc 'A26'
    //   336: aload #6
    //   338: aload_3
    //   339: invokevirtual y : ()Ljava/lang/String;
    //   342: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   345: invokevirtual toString : ()Ljava/lang/String;
    //   348: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   353: pop
    //   354: aload #5
    //   356: getfield k : Ljava/util/Map;
    //   359: astore #6
    //   361: new java/lang/StringBuilder
    //   364: astore_0
    //   365: aload_0
    //   366: invokespecial <init> : ()V
    //   369: aload #6
    //   371: ldc 'A60'
    //   373: aload_0
    //   374: aload_3
    //   375: invokevirtual z : ()Ljava/lang/String;
    //   378: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: invokevirtual toString : ()Ljava/lang/String;
    //   384: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   389: pop
    //   390: aload #5
    //   392: getfield k : Ljava/util/Map;
    //   395: astore #6
    //   397: new java/lang/StringBuilder
    //   400: astore_0
    //   401: aload_0
    //   402: invokespecial <init> : ()V
    //   405: aload #6
    //   407: ldc 'A61'
    //   409: aload_0
    //   410: aload_3
    //   411: invokevirtual A : ()Ljava/lang/String;
    //   414: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   417: invokevirtual toString : ()Ljava/lang/String;
    //   420: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   425: pop
    //   426: aload #5
    //   428: getfield k : Ljava/util/Map;
    //   431: astore_0
    //   432: new java/lang/StringBuilder
    //   435: astore #6
    //   437: aload #6
    //   439: invokespecial <init> : ()V
    //   442: aload_0
    //   443: ldc 'F11'
    //   445: aload #6
    //   447: aload_3
    //   448: getfield z : Z
    //   451: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   454: invokevirtual toString : ()Ljava/lang/String;
    //   457: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   462: pop
    //   463: aload #5
    //   465: getfield k : Ljava/util/Map;
    //   468: astore #6
    //   470: new java/lang/StringBuilder
    //   473: astore_0
    //   474: aload_0
    //   475: invokespecial <init> : ()V
    //   478: aload #6
    //   480: ldc 'F12'
    //   482: aload_0
    //   483: aload_3
    //   484: getfield y : Z
    //   487: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   490: invokevirtual toString : ()Ljava/lang/String;
    //   493: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   498: pop
    //   499: aload #5
    //   501: getfield k : Ljava/util/Map;
    //   504: astore #6
    //   506: new java/lang/StringBuilder
    //   509: astore_0
    //   510: aload_0
    //   511: invokespecial <init> : ()V
    //   514: aload #6
    //   516: ldc 'G1'
    //   518: aload_0
    //   519: aload_3
    //   520: invokevirtual u : ()Ljava/lang/String;
    //   523: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   526: invokevirtual toString : ()Ljava/lang/String;
    //   529: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   534: pop
    //   535: aload_3
    //   536: getfield B : Z
    //   539: ifeq -> 768
    //   542: aload #5
    //   544: getfield k : Ljava/util/Map;
    //   547: astore #6
    //   549: new java/lang/StringBuilder
    //   552: astore_0
    //   553: aload_0
    //   554: invokespecial <init> : ()V
    //   557: aload #6
    //   559: ldc 'G2'
    //   561: aload_0
    //   562: aload_3
    //   563: invokevirtual M : ()Ljava/lang/String;
    //   566: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   569: invokevirtual toString : ()Ljava/lang/String;
    //   572: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   577: pop
    //   578: aload #5
    //   580: getfield k : Ljava/util/Map;
    //   583: astore_0
    //   584: new java/lang/StringBuilder
    //   587: astore #6
    //   589: aload #6
    //   591: invokespecial <init> : ()V
    //   594: aload_0
    //   595: ldc 'G3'
    //   597: aload #6
    //   599: aload_3
    //   600: invokevirtual N : ()Ljava/lang/String;
    //   603: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   606: invokevirtual toString : ()Ljava/lang/String;
    //   609: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   614: pop
    //   615: aload #5
    //   617: getfield k : Ljava/util/Map;
    //   620: astore_0
    //   621: new java/lang/StringBuilder
    //   624: astore #6
    //   626: aload #6
    //   628: invokespecial <init> : ()V
    //   631: aload_0
    //   632: ldc 'G4'
    //   634: aload #6
    //   636: aload_3
    //   637: invokevirtual O : ()Ljava/lang/String;
    //   640: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: invokevirtual toString : ()Ljava/lang/String;
    //   646: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   651: pop
    //   652: aload #5
    //   654: getfield k : Ljava/util/Map;
    //   657: astore #6
    //   659: new java/lang/StringBuilder
    //   662: astore_0
    //   663: aload_0
    //   664: invokespecial <init> : ()V
    //   667: aload #6
    //   669: ldc_w 'G5'
    //   672: aload_0
    //   673: aload_3
    //   674: invokevirtual P : ()Ljava/lang/String;
    //   677: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   680: invokevirtual toString : ()Ljava/lang/String;
    //   683: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   688: pop
    //   689: aload #5
    //   691: getfield k : Ljava/util/Map;
    //   694: astore_0
    //   695: new java/lang/StringBuilder
    //   698: astore #6
    //   700: aload #6
    //   702: invokespecial <init> : ()V
    //   705: aload_0
    //   706: ldc_w 'G6'
    //   709: aload #6
    //   711: aload_3
    //   712: invokevirtual Q : ()Ljava/lang/String;
    //   715: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   718: invokevirtual toString : ()Ljava/lang/String;
    //   721: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   726: pop
    //   727: aload #5
    //   729: getfield k : Ljava/util/Map;
    //   732: astore_0
    //   733: new java/lang/StringBuilder
    //   736: astore #6
    //   738: aload #6
    //   740: invokespecial <init> : ()V
    //   743: aload_0
    //   744: ldc_w 'G7'
    //   747: aload #6
    //   749: aload_3
    //   750: invokevirtual R : ()J
    //   753: invokestatic toString : (J)Ljava/lang/String;
    //   756: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   759: invokevirtual toString : ()Ljava/lang/String;
    //   762: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   767: pop
    //   768: aload #5
    //   770: getfield k : Ljava/util/Map;
    //   773: astore #6
    //   775: new java/lang/StringBuilder
    //   778: astore_0
    //   779: aload_0
    //   780: invokespecial <init> : ()V
    //   783: aload #6
    //   785: ldc_w 'D3'
    //   788: aload_0
    //   789: aload_3
    //   790: getfield k : Ljava/lang/String;
    //   793: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   796: invokevirtual toString : ()Ljava/lang/String;
    //   799: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   804: pop
    //   805: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   808: ifnull -> 908
    //   811: getstatic com/tencent/bugly/b.b : Ljava/util/List;
    //   814: invokeinterface iterator : ()Ljava/util/Iterator;
    //   819: astore_0
    //   820: aload_0
    //   821: invokeinterface hasNext : ()Z
    //   826: ifeq -> 908
    //   829: aload_0
    //   830: invokeinterface next : ()Ljava/lang/Object;
    //   835: checkcast com/tencent/bugly/a
    //   838: astore #6
    //   840: aload #6
    //   842: getfield versionKey : Ljava/lang/String;
    //   845: ifnull -> 820
    //   848: aload #6
    //   850: getfield version : Ljava/lang/String;
    //   853: ifnull -> 820
    //   856: aload #5
    //   858: getfield k : Ljava/util/Map;
    //   861: aload #6
    //   863: getfield versionKey : Ljava/lang/String;
    //   866: aload #6
    //   868: getfield version : Ljava/lang/String;
    //   871: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   876: pop
    //   877: goto -> 820
    //   880: astore_0
    //   881: aload_3
    //   882: monitorexit
    //   883: aload_0
    //   884: athrow
    //   885: astore_0
    //   886: aload_0
    //   887: invokestatic b : (Ljava/lang/Throwable;)Z
    //   890: ifne -> 897
    //   893: aload_0
    //   894: invokevirtual printStackTrace : ()V
    //   897: aconst_null
    //   898: astore_0
    //   899: goto -> 33
    //   902: aload_2
    //   903: astore #6
    //   905: goto -> 118
    //   908: aload #5
    //   910: getfield k : Ljava/util/Map;
    //   913: ldc_w 'G15'
    //   916: ldc_w 'G15'
    //   919: ldc ''
    //   921: invokestatic b : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   924: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   929: pop
    //   930: aload #5
    //   932: getfield k : Ljava/util/Map;
    //   935: ldc_w 'D4'
    //   938: ldc_w 'D4'
    //   941: ldc_w '0'
    //   944: invokestatic b : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   947: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   952: pop
    //   953: aload_3
    //   954: monitorexit
    //   955: invokestatic a : ()Lcom/tencent/bugly/proguard/u;
    //   958: astore_0
    //   959: aload_0
    //   960: ifnull -> 1018
    //   963: aload_0
    //   964: getfield a : Z
    //   967: ifne -> 1018
    //   970: aload_2
    //   971: ifnull -> 1018
    //   974: aload #5
    //   976: aload #5
    //   978: getfield h : [B
    //   981: iconst_2
    //   982: iconst_1
    //   983: aload #4
    //   985: getfield u : Ljava/lang/String;
    //   988: invokestatic a : ([BIILjava/lang/String;)[B
    //   991: putfield h : [B
    //   994: aload #5
    //   996: getfield h : [B
    //   999: ifnonnull -> 1018
    //   1002: ldc_w 'reqPkg sbuffer error!'
    //   1005: iconst_0
    //   1006: anewarray java/lang/Object
    //   1009: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1012: pop
    //   1013: aconst_null
    //   1014: astore_0
    //   1015: goto -> 33
    //   1018: aload_3
    //   1019: invokevirtual F : ()Ljava/util/Map;
    //   1022: astore_0
    //   1023: aload_0
    //   1024: ifnull -> 1084
    //   1027: aload_0
    //   1028: invokeinterface entrySet : ()Ljava/util/Set;
    //   1033: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1038: astore_2
    //   1039: aload_2
    //   1040: invokeinterface hasNext : ()Z
    //   1045: ifeq -> 1084
    //   1048: aload_2
    //   1049: invokeinterface next : ()Ljava/lang/Object;
    //   1054: checkcast java/util/Map$Entry
    //   1057: astore_0
    //   1058: aload #5
    //   1060: getfield k : Ljava/util/Map;
    //   1063: aload_0
    //   1064: invokeinterface getKey : ()Ljava/lang/Object;
    //   1069: aload_0
    //   1070: invokeinterface getValue : ()Ljava/lang/Object;
    //   1075: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1080: pop
    //   1081: goto -> 1039
    //   1084: aload #5
    //   1086: astore_0
    //   1087: goto -> 33
    // Exception table:
    //   from	to	target	type
    //   35	47	885	java/lang/Throwable
    //   47	107	880	finally
    //   111	118	880	finally
    //   118	768	880	finally
    //   768	820	880	finally
    //   820	877	880	finally
    //   881	885	885	java/lang/Throwable
    //   908	955	880	finally
    //   955	959	885	java/lang/Throwable
    //   963	970	885	java/lang/Throwable
    //   974	1013	885	java/lang/Throwable
    //   1018	1023	885	java/lang/Throwable
    //   1027	1039	885	java/lang/Throwable
    //   1039	1081	885	java/lang/Throwable
  }
  
  public static an a(byte[] paramArrayOfbyte, boolean paramBoolean) {
    if (paramArrayOfbyte != null)
      try {
        d d2 = new d();
        this();
        d2.b();
        d2.a("utf-8");
        d2.a(paramArrayOfbyte);
        an an = new an();
        this();
        an = d2.b("detail", an);
        if (an.class.isInstance(an)) {
          an an1 = an.class.cast(an);
        } else {
          d2 = null;
        } 
        d d1 = d2;
        if (!paramBoolean) {
          d1 = d2;
          if (d2 != null) {
            d1 = d2;
            if (((an)d2).c != null) {
              d1 = d2;
              if (((an)d2).c.length > 0) {
                x.c("resp buf %d", new Object[] { Integer.valueOf(((an)d2).c.length) });
                ((an)d2).c = z.b(((an)d2).c, 2, 1, StrategyBean.d);
                d1 = d2;
                if (((an)d2).c == null) {
                  x.e("resp sbuffer error!", new Object[0]);
                  d1 = null;
                } 
              } 
            } 
          } 
        } 
        return (an)d1;
      } catch (Throwable throwable) {
        if (!x.b(throwable))
          throwable.printStackTrace(); 
      }  
    return null;
  }
  
  public static aq a(UserInfoBean paramUserInfoBean) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 8
    //   4: aconst_null
    //   5: astore_0
    //   6: aload_0
    //   7: areturn
    //   8: new com/tencent/bugly/proguard/aq
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: astore_1
    //   16: aload_1
    //   17: aload_0
    //   18: getfield e : J
    //   21: putfield a : J
    //   24: aload_1
    //   25: aload_0
    //   26: getfield j : Ljava/lang/String;
    //   29: putfield e : Ljava/lang/String;
    //   32: aload_1
    //   33: aload_0
    //   34: getfield c : Ljava/lang/String;
    //   37: putfield d : Ljava/lang/String;
    //   40: aload_1
    //   41: aload_0
    //   42: getfield d : Ljava/lang/String;
    //   45: putfield c : Ljava/lang/String;
    //   48: aload_1
    //   49: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   52: invokevirtual i : ()Ljava/lang/String;
    //   55: putfield g : Ljava/lang/String;
    //   58: aload_0
    //   59: getfield o : I
    //   62: iconst_1
    //   63: if_icmpne -> 321
    //   66: iconst_1
    //   67: istore_2
    //   68: aload_1
    //   69: iload_2
    //   70: putfield h : Z
    //   73: aload_0
    //   74: getfield b : I
    //   77: tableswitch default -> 108, 1 -> 326, 2 -> 344, 3 -> 335, 4 -> 353
    //   108: aload_0
    //   109: getfield b : I
    //   112: bipush #10
    //   114: if_icmplt -> 362
    //   117: aload_0
    //   118: getfield b : I
    //   121: bipush #20
    //   123: if_icmpge -> 362
    //   126: aload_1
    //   127: aload_0
    //   128: getfield b : I
    //   131: i2b
    //   132: i2b
    //   133: putfield b : B
    //   136: aload_1
    //   137: new java/util/HashMap
    //   140: dup
    //   141: invokespecial <init> : ()V
    //   144: putfield f : Ljava/util/Map;
    //   147: aload_0
    //   148: getfield p : I
    //   151: iflt -> 184
    //   154: aload_1
    //   155: getfield f : Ljava/util/Map;
    //   158: ldc_w 'C01'
    //   161: new java/lang/StringBuilder
    //   164: dup
    //   165: invokespecial <init> : ()V
    //   168: aload_0
    //   169: getfield p : I
    //   172: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   175: invokevirtual toString : ()Ljava/lang/String;
    //   178: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   183: pop
    //   184: aload_0
    //   185: getfield q : I
    //   188: iflt -> 221
    //   191: aload_1
    //   192: getfield f : Ljava/util/Map;
    //   195: ldc_w 'C02'
    //   198: new java/lang/StringBuilder
    //   201: dup
    //   202: invokespecial <init> : ()V
    //   205: aload_0
    //   206: getfield q : I
    //   209: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   212: invokevirtual toString : ()Ljava/lang/String;
    //   215: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   220: pop
    //   221: aload_0
    //   222: getfield r : Ljava/util/Map;
    //   225: ifnull -> 388
    //   228: aload_0
    //   229: getfield r : Ljava/util/Map;
    //   232: invokeinterface size : ()I
    //   237: ifle -> 388
    //   240: aload_0
    //   241: getfield r : Ljava/util/Map;
    //   244: invokeinterface entrySet : ()Ljava/util/Set;
    //   249: invokeinterface iterator : ()Ljava/util/Iterator;
    //   254: astore_3
    //   255: aload_3
    //   256: invokeinterface hasNext : ()Z
    //   261: ifeq -> 388
    //   264: aload_3
    //   265: invokeinterface next : ()Ljava/lang/Object;
    //   270: checkcast java/util/Map$Entry
    //   273: astore #4
    //   275: aload_1
    //   276: getfield f : Ljava/util/Map;
    //   279: new java/lang/StringBuilder
    //   282: dup
    //   283: ldc_w 'C03_'
    //   286: invokespecial <init> : (Ljava/lang/String;)V
    //   289: aload #4
    //   291: invokeinterface getKey : ()Ljava/lang/Object;
    //   296: checkcast java/lang/String
    //   299: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: invokevirtual toString : ()Ljava/lang/String;
    //   305: aload #4
    //   307: invokeinterface getValue : ()Ljava/lang/Object;
    //   312: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   317: pop
    //   318: goto -> 255
    //   321: iconst_0
    //   322: istore_2
    //   323: goto -> 68
    //   326: aload_1
    //   327: iconst_1
    //   328: i2b
    //   329: putfield b : B
    //   332: goto -> 136
    //   335: aload_1
    //   336: iconst_2
    //   337: i2b
    //   338: putfield b : B
    //   341: goto -> 136
    //   344: aload_1
    //   345: iconst_4
    //   346: i2b
    //   347: putfield b : B
    //   350: goto -> 136
    //   353: aload_1
    //   354: iconst_3
    //   355: i2b
    //   356: putfield b : B
    //   359: goto -> 136
    //   362: ldc_w 'unknown uinfo type %d '
    //   365: iconst_1
    //   366: anewarray java/lang/Object
    //   369: dup
    //   370: iconst_0
    //   371: aload_0
    //   372: getfield b : I
    //   375: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   378: aastore
    //   379: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   382: pop
    //   383: aconst_null
    //   384: astore_0
    //   385: goto -> 6
    //   388: aload_0
    //   389: getfield s : Ljava/util/Map;
    //   392: ifnull -> 488
    //   395: aload_0
    //   396: getfield s : Ljava/util/Map;
    //   399: invokeinterface size : ()I
    //   404: ifle -> 488
    //   407: aload_0
    //   408: getfield s : Ljava/util/Map;
    //   411: invokeinterface entrySet : ()Ljava/util/Set;
    //   416: invokeinterface iterator : ()Ljava/util/Iterator;
    //   421: astore #4
    //   423: aload #4
    //   425: invokeinterface hasNext : ()Z
    //   430: ifeq -> 488
    //   433: aload #4
    //   435: invokeinterface next : ()Ljava/lang/Object;
    //   440: checkcast java/util/Map$Entry
    //   443: astore_3
    //   444: aload_1
    //   445: getfield f : Ljava/util/Map;
    //   448: new java/lang/StringBuilder
    //   451: dup
    //   452: ldc_w 'C04_'
    //   455: invokespecial <init> : (Ljava/lang/String;)V
    //   458: aload_3
    //   459: invokeinterface getKey : ()Ljava/lang/Object;
    //   464: checkcast java/lang/String
    //   467: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   470: invokevirtual toString : ()Ljava/lang/String;
    //   473: aload_3
    //   474: invokeinterface getValue : ()Ljava/lang/Object;
    //   479: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   484: pop
    //   485: goto -> 423
    //   488: aload_1
    //   489: getfield f : Ljava/util/Map;
    //   492: astore #4
    //   494: new java/lang/StringBuilder
    //   497: dup
    //   498: invokespecial <init> : ()V
    //   501: astore_3
    //   502: aload_0
    //   503: getfield l : Z
    //   506: ifne -> 751
    //   509: iconst_1
    //   510: istore_2
    //   511: aload #4
    //   513: ldc_w 'A36'
    //   516: aload_3
    //   517: iload_2
    //   518: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   521: invokevirtual toString : ()Ljava/lang/String;
    //   524: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   529: pop
    //   530: aload_1
    //   531: getfield f : Ljava/util/Map;
    //   534: ldc_w 'F02'
    //   537: new java/lang/StringBuilder
    //   540: dup
    //   541: invokespecial <init> : ()V
    //   544: aload_0
    //   545: getfield g : J
    //   548: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   551: invokevirtual toString : ()Ljava/lang/String;
    //   554: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   559: pop
    //   560: aload_1
    //   561: getfield f : Ljava/util/Map;
    //   564: ldc_w 'F03'
    //   567: new java/lang/StringBuilder
    //   570: dup
    //   571: invokespecial <init> : ()V
    //   574: aload_0
    //   575: getfield h : J
    //   578: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   581: invokevirtual toString : ()Ljava/lang/String;
    //   584: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   589: pop
    //   590: aload_1
    //   591: getfield f : Ljava/util/Map;
    //   594: ldc_w 'F04'
    //   597: new java/lang/StringBuilder
    //   600: dup
    //   601: invokespecial <init> : ()V
    //   604: aload_0
    //   605: getfield j : Ljava/lang/String;
    //   608: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   611: invokevirtual toString : ()Ljava/lang/String;
    //   614: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   619: pop
    //   620: aload_1
    //   621: getfield f : Ljava/util/Map;
    //   624: ldc_w 'F05'
    //   627: new java/lang/StringBuilder
    //   630: dup
    //   631: invokespecial <init> : ()V
    //   634: aload_0
    //   635: getfield i : J
    //   638: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   641: invokevirtual toString : ()Ljava/lang/String;
    //   644: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   649: pop
    //   650: aload_1
    //   651: getfield f : Ljava/util/Map;
    //   654: ldc_w 'F06'
    //   657: new java/lang/StringBuilder
    //   660: dup
    //   661: invokespecial <init> : ()V
    //   664: aload_0
    //   665: getfield m : Ljava/lang/String;
    //   668: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   671: invokevirtual toString : ()Ljava/lang/String;
    //   674: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   679: pop
    //   680: aload_1
    //   681: getfield f : Ljava/util/Map;
    //   684: ldc_w 'F10'
    //   687: new java/lang/StringBuilder
    //   690: dup
    //   691: invokespecial <init> : ()V
    //   694: aload_0
    //   695: getfield k : J
    //   698: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   701: invokevirtual toString : ()Ljava/lang/String;
    //   704: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   709: pop
    //   710: ldc_w 'summary type %d vm:%d'
    //   713: iconst_2
    //   714: anewarray java/lang/Object
    //   717: dup
    //   718: iconst_0
    //   719: aload_1
    //   720: getfield b : B
    //   723: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   726: aastore
    //   727: dup
    //   728: iconst_1
    //   729: aload_1
    //   730: getfield f : Ljava/util/Map;
    //   733: invokeinterface size : ()I
    //   738: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   741: aastore
    //   742: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   745: pop
    //   746: aload_1
    //   747: astore_0
    //   748: goto -> 6
    //   751: iconst_0
    //   752: istore_2
    //   753: goto -> 511
  }
  
  public static ar a(List<UserInfoBean> paramList, int paramInt) {
    if (paramList == null || paramList.size() == 0)
      return null; 
    com.tencent.bugly.crashreport.common.info.a a1 = com.tencent.bugly.crashreport.common.info.a.b();
    if (a1 == null)
      return null; 
    a1.t();
    ar ar = new ar();
    ar.b = a1.d;
    ar.c = a1.h();
    ArrayList<aq> arrayList = new ArrayList();
    Iterator<UserInfoBean> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      aq aq = a(iterator.next());
      if (aq != null)
        arrayList.add(aq); 
    } 
    ar.d = arrayList;
    ar.e = new HashMap<String, String>();
    ar.e.put("A7", a1.f);
    ar.e.put("A6", a1.s());
    ar.e.put("A5", a1.r());
    ar.e.put("A2", a1.p());
    ar.e.put("A1", a1.p());
    ar.e.put("A24", a1.h);
    ar.e.put("A17", a1.q());
    ar.e.put("A15", a1.w());
    ar.e.put("A13", a1.x());
    ar.e.put("F08", a1.v);
    ar.e.put("F09", a1.w);
    null = a1.G();
    if (null != null && null.size() > 0)
      for (Map.Entry entry : null.entrySet())
        ar.e.put("C04_" + (String)entry.getKey(), (String)entry.getValue());  
    switch (paramInt) {
      default:
        x.e("unknown up type %d ", new Object[] { Integer.valueOf(paramInt) });
        return null;
      case 1:
        ar.a = (byte)1;
        return ar;
      case 2:
        break;
    } 
    ar.a = (byte)2;
    return ar;
  }
  
  public static <T extends k> T a(byte[] paramArrayOfbyte, Class<T> paramClass) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0)
      return null; 
    try {
      k k2 = (k)paramClass.newInstance();
      i i1 = new i();
      this(paramArrayOfbyte);
      i1.a("utf-8");
      k2.a(i1);
      k k1 = k2;
    } catch (Throwable throwable) {}
    return (T)throwable;
  }
  
  public static String a(ArrayList<String> paramArrayList) {
    StringBuffer stringBuffer = new StringBuffer();
    byte b;
    for (b = 0; b < paramArrayList.size(); b++) {
      String str2;
      String str1 = paramArrayList.get(b);
      if (str1.equals("java.lang.Integer") || str1.equals("int")) {
        str2 = "int32";
      } else if (str1.equals("java.lang.Boolean") || str1.equals("boolean")) {
        str2 = "bool";
      } else if (str1.equals("java.lang.Byte") || str1.equals("byte")) {
        str2 = "char";
      } else if (str1.equals("java.lang.Double") || str1.equals("double")) {
        str2 = "double";
      } else if (str1.equals("java.lang.Float") || str1.equals("float")) {
        str2 = "float";
      } else if (str1.equals("java.lang.Long") || str1.equals("long")) {
        str2 = "int64";
      } else if (str1.equals("java.lang.Short") || str1.equals("short")) {
        str2 = "short";
      } else {
        if (str1.equals("java.lang.Character"))
          throw new IllegalArgumentException("can not support java.lang.Character"); 
        if (str1.equals("java.lang.String")) {
          str2 = "string";
        } else if (str1.equals("java.util.List")) {
          str2 = "list";
        } else {
          str2 = str1;
          if (str1.equals("java.util.Map"))
            str2 = "map"; 
        } 
      } 
      paramArrayList.set(b, str2);
    } 
    Collections.reverse(paramArrayList);
    for (b = 0; b < paramArrayList.size(); b++) {
      String str = paramArrayList.get(b);
      if (str.equals("list")) {
        paramArrayList.set(b - 1, "<" + (String)paramArrayList.get(b - 1));
        paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
      } else if (str.equals("map")) {
        paramArrayList.set(b - 1, "<" + (String)paramArrayList.get(b - 1) + ",");
        paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
      } else if (str.equals("Array")) {
        paramArrayList.set(b - 1, "<" + (String)paramArrayList.get(b - 1));
        paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
      } 
    } 
    Collections.reverse(paramArrayList);
    Iterator<String> iterator = paramArrayList.iterator();
    while (iterator.hasNext())
      stringBuffer.append(iterator.next()); 
    return stringBuffer.toString();
  }
  
  private void a(ArrayList<String> paramArrayList, Object paramObject) {
    if (paramObject.getClass().isArray()) {
      if (!paramObject.getClass().getComponentType().toString().equals("byte"))
        throw new IllegalArgumentException("only byte[] is supported"); 
      if (Array.getLength(paramObject) > 0) {
        paramArrayList.add("java.util.List");
        a(paramArrayList, Array.get(paramObject, 0));
        return;
      } 
      paramArrayList.add("Array");
      paramArrayList.add("?");
      return;
    } 
    if (paramObject instanceof Array)
      throw new IllegalArgumentException("can not support Array, please use List"); 
    if (paramObject instanceof List) {
      paramArrayList.add("java.util.List");
      paramObject = paramObject;
      if (paramObject.size() > 0) {
        a(paramArrayList, paramObject.get(0));
        return;
      } 
      paramArrayList.add("?");
      return;
    } 
    if (paramObject instanceof Map) {
      paramArrayList.add("java.util.Map");
      Map map = (Map)paramObject;
      if (map.size() > 0) {
        paramObject = map.keySet().iterator().next();
        map = (Map)map.get(paramObject);
        paramArrayList.add(paramObject.getClass().getName());
        a(paramArrayList, map);
        return;
      } 
      paramArrayList.add("?");
      paramArrayList.add("?");
      return;
    } 
    paramArrayList.add(paramObject.getClass().getName());
  }
  
  public static byte[] a(k paramk) {
    try {
      j j = new j();
      this();
      j.a("utf-8");
      paramk.a(j);
      byte[] arrayOfByte = j.b();
    } catch (Throwable throwable) {}
    return (byte[])throwable;
  }
  
  public static byte[] a(Object paramObject) {
    try {
      d d = new d();
      this();
      d.b();
      d.a("utf-8");
      d.b(1);
      d.b("RqdServer");
      d.c("sync");
      d.a("detail", paramObject);
      paramObject = d.a();
    } catch (Throwable throwable) {}
    return (byte[])throwable;
  }
  
  public void a(String paramString) {
    this.b = paramString;
  }
  
  public <T> void a(String paramString, T paramT) {
    if (paramString == null)
      throw new IllegalArgumentException("put key can not is null"); 
    if (paramT == null)
      throw new IllegalArgumentException("put value can not is null"); 
    if (paramT instanceof java.util.Set)
      throw new IllegalArgumentException("can not support Set"); 
    j j = new j();
    j.a(this.b);
    j.a(paramT, 0);
    byte[] arrayOfByte = l.a(j.a());
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(1);
    ArrayList<String> arrayList = new ArrayList(1);
    a(arrayList, paramT);
    hashMap.put(a(arrayList), arrayOfByte);
    this.d.remove(paramString);
    this.a.put(paramString, hashMap);
  }
  
  public void a(byte[] paramArrayOfbyte) {
    this.c.a(paramArrayOfbyte);
    this.c.a(this.b);
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>(1);
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>(1);
    hashMap1.put("", new byte[0]);
    hashMap2.put("", hashMap1);
    this.a = this.c.a(hashMap2, 0, false);
  }
  
  public byte[] a() {
    j j = new j(0);
    j.a(this.b);
    j.a(this.a, 0);
    return l.a(j.a());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */