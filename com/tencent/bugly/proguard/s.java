package com.tencent.bugly.proguard;

import android.content.Context;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class s {
  private static s b;
  
  public Map<String, String> a = null;
  
  private Context c;
  
  private s(Context paramContext) {
    this.c = paramContext;
  }
  
  public static s a(Context paramContext) {
    if (b == null)
      b = new s(paramContext); 
    return b;
  }
  
  private static HttpURLConnection a(String paramString1, String paramString2) {
    try {
      HttpURLConnection httpURLConnection;
      URL uRL = new URL();
      this(paramString2);
      if (paramString1 != null && paramString1.toLowerCase(Locale.US).contains("wap")) {
        String str = System.getProperty("http.proxyHost");
        paramString2 = System.getProperty("http.proxyPort");
        InetSocketAddress inetSocketAddress = new InetSocketAddress();
        this(str, Integer.parseInt(paramString2));
        Proxy proxy = new Proxy();
        this(Proxy.Type.HTTP, inetSocketAddress);
        httpURLConnection = (HttpURLConnection)uRL.openConnection(proxy);
      } else {
        httpURLConnection = (HttpURLConnection)uRL.openConnection();
      } 
      httpURLConnection.setConnectTimeout(30000);
      httpURLConnection.setReadTimeout(10000);
      httpURLConnection.setDoOutput(true);
      httpURLConnection.setDoInput(true);
      httpURLConnection.setRequestMethod("POST");
      httpURLConnection.setUseCaches(false);
      httpURLConnection.setInstanceFollowRedirects(false);
    } catch (Throwable throwable) {}
    return (HttpURLConnection)throwable;
  }
  
  private HttpURLConnection a(String paramString1, byte[] paramArrayOfbyte, String paramString2, Map<String, String> paramMap) {
    if (paramString1 == null) {
      x.e("destUrl is null.", new Object[0]);
      return null;
    } 
    HttpURLConnection httpURLConnection = a(paramString2, paramString1);
    if (httpURLConnection == null) {
      x.e("Failed to get HttpURLConnection object.", new Object[0]);
      return null;
    } 
    try {
      httpURLConnection.setRequestProperty("wup_version", "3.0");
      if (paramMap != null && paramMap.size() > 0)
        for (Map.Entry<String, String> entry : paramMap.entrySet())
          httpURLConnection.setRequestProperty((String)entry.getKey(), URLEncoder.encode((String)entry.getValue(), "utf-8"));  
    } catch (Throwable null) {
      if (!x.a(null))
        null.printStackTrace(); 
      x.e("Failed to upload, please check your network.", new Object[0]);
      return null;
    } 
    httpURLConnection.setRequestProperty("A37", URLEncoder.encode(paramString2, "utf-8"));
    httpURLConnection.setRequestProperty("A38", URLEncoder.encode(paramString2, "utf-8"));
    OutputStream outputStream = httpURLConnection.getOutputStream();
    if (paramArrayOfbyte == null) {
      outputStream.write(0);
      return httpURLConnection;
    } 
    outputStream.write(paramArrayOfbyte);
    return httpURLConnection;
  }
  
  private static Map<String, String> a(HttpURLConnection paramHttpURLConnection) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    Map<String, List<String>> map = paramHttpURLConnection.getHeaderFields();
    if (map == null || map.size() == 0)
      return null; 
    for (String str : map.keySet()) {
      List list = map.get(str);
      if (list.size() > 0)
        hashMap.put(str, list.get(0)); 
    } 
    return (Map)hashMap;
  }
  
  private static byte[] b(HttpURLConnection paramHttpURLConnection) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: aload_1
    //   7: astore_0
    //   8: aload_0
    //   9: areturn
    //   10: new java/io/BufferedInputStream
    //   13: astore_2
    //   14: aload_2
    //   15: aload_0
    //   16: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   19: invokespecial <init> : (Ljava/io/InputStream;)V
    //   22: aload_2
    //   23: astore_0
    //   24: new java/io/ByteArrayOutputStream
    //   27: astore_3
    //   28: aload_2
    //   29: astore_0
    //   30: aload_3
    //   31: invokespecial <init> : ()V
    //   34: aload_2
    //   35: astore_0
    //   36: sipush #1024
    //   39: newarray byte
    //   41: astore #4
    //   43: aload_2
    //   44: astore_0
    //   45: aload_2
    //   46: aload #4
    //   48: invokevirtual read : ([B)I
    //   51: istore #5
    //   53: iload #5
    //   55: ifle -> 116
    //   58: aload_2
    //   59: astore_0
    //   60: aload_3
    //   61: aload #4
    //   63: iconst_0
    //   64: iload #5
    //   66: invokevirtual write : ([BII)V
    //   69: goto -> 43
    //   72: astore #4
    //   74: aload_2
    //   75: astore_0
    //   76: aload #4
    //   78: invokestatic a : (Ljava/lang/Throwable;)Z
    //   81: ifne -> 91
    //   84: aload_2
    //   85: astore_0
    //   86: aload #4
    //   88: invokevirtual printStackTrace : ()V
    //   91: aload_1
    //   92: astore_0
    //   93: aload_2
    //   94: ifnull -> 8
    //   97: aload_2
    //   98: invokevirtual close : ()V
    //   101: aload_1
    //   102: astore_0
    //   103: goto -> 8
    //   106: astore_0
    //   107: aload_0
    //   108: invokevirtual printStackTrace : ()V
    //   111: aload_1
    //   112: astore_0
    //   113: goto -> 8
    //   116: aload_2
    //   117: astore_0
    //   118: aload_3
    //   119: invokevirtual flush : ()V
    //   122: aload_2
    //   123: astore_0
    //   124: aload_3
    //   125: invokevirtual toByteArray : ()[B
    //   128: astore #4
    //   130: aload #4
    //   132: astore_0
    //   133: aload_2
    //   134: invokevirtual close : ()V
    //   137: goto -> 8
    //   140: astore_2
    //   141: aload_2
    //   142: invokevirtual printStackTrace : ()V
    //   145: goto -> 8
    //   148: astore_2
    //   149: aconst_null
    //   150: astore_0
    //   151: aload_0
    //   152: ifnull -> 159
    //   155: aload_0
    //   156: invokevirtual close : ()V
    //   159: aload_2
    //   160: athrow
    //   161: astore_0
    //   162: aload_0
    //   163: invokevirtual printStackTrace : ()V
    //   166: goto -> 159
    //   169: astore_2
    //   170: goto -> 151
    //   173: astore #4
    //   175: aconst_null
    //   176: astore_2
    //   177: goto -> 74
    // Exception table:
    //   from	to	target	type
    //   10	22	173	java/lang/Throwable
    //   10	22	148	finally
    //   24	28	72	java/lang/Throwable
    //   24	28	169	finally
    //   30	34	72	java/lang/Throwable
    //   30	34	169	finally
    //   36	43	72	java/lang/Throwable
    //   36	43	169	finally
    //   45	53	72	java/lang/Throwable
    //   45	53	169	finally
    //   60	69	72	java/lang/Throwable
    //   60	69	169	finally
    //   76	84	169	finally
    //   86	91	169	finally
    //   97	101	106	java/lang/Throwable
    //   118	122	72	java/lang/Throwable
    //   118	122	169	finally
    //   124	130	72	java/lang/Throwable
    //   124	130	169	finally
    //   133	137	140	java/lang/Throwable
    //   155	159	161	java/lang/Throwable
  }
  
  public final byte[] a(String paramString, byte[] paramArrayOfbyte, v paramv, Map<String, String> paramMap) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 19
    //   4: ldc_w 'Failed for no URL.'
    //   7: iconst_0
    //   8: anewarray java/lang/Object
    //   11: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   14: pop
    //   15: aconst_null
    //   16: astore_1
    //   17: aload_1
    //   18: areturn
    //   19: iconst_0
    //   20: istore #5
    //   22: iconst_0
    //   23: istore #6
    //   25: aload_2
    //   26: ifnonnull -> 126
    //   29: lconst_0
    //   30: lstore #7
    //   32: ldc_w 'request: %s, send: %d (pid=%d | tid=%d)'
    //   35: iconst_4
    //   36: anewarray java/lang/Object
    //   39: dup
    //   40: iconst_0
    //   41: aload_1
    //   42: aastore
    //   43: dup
    //   44: iconst_1
    //   45: lload #7
    //   47: invokestatic valueOf : (J)Ljava/lang/Long;
    //   50: aastore
    //   51: dup
    //   52: iconst_2
    //   53: invokestatic myPid : ()I
    //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   59: aastore
    //   60: dup
    //   61: iconst_3
    //   62: invokestatic myTid : ()I
    //   65: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   68: aastore
    //   69: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   72: pop
    //   73: iconst_0
    //   74: istore #9
    //   76: iload #5
    //   78: ifgt -> 759
    //   81: iload #6
    //   83: ifgt -> 759
    //   86: iload #9
    //   88: ifeq -> 134
    //   91: iconst_0
    //   92: istore #10
    //   94: aload_0
    //   95: getfield c : Landroid/content/Context;
    //   98: invokestatic f : (Landroid/content/Context;)Ljava/lang/String;
    //   101: astore #11
    //   103: aload #11
    //   105: ifnonnull -> 215
    //   108: ldc_w 'Failed to request for network not avail'
    //   111: iconst_0
    //   112: anewarray java/lang/Object
    //   115: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   118: pop
    //   119: iload #10
    //   121: istore #9
    //   123: goto -> 76
    //   126: aload_2
    //   127: arraylength
    //   128: i2l
    //   129: lstore #7
    //   131: goto -> 32
    //   134: iload #5
    //   136: iconst_1
    //   137: iadd
    //   138: istore #12
    //   140: iload #9
    //   142: istore #10
    //   144: iload #12
    //   146: istore #5
    //   148: iload #12
    //   150: iconst_1
    //   151: if_icmple -> 94
    //   154: new java/lang/StringBuilder
    //   157: dup
    //   158: ldc_w 'try time: '
    //   161: invokespecial <init> : (Ljava/lang/String;)V
    //   164: iload #12
    //   166: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   169: invokevirtual toString : ()Ljava/lang/String;
    //   172: iconst_0
    //   173: anewarray java/lang/Object
    //   176: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   179: pop
    //   180: new java/util/Random
    //   183: dup
    //   184: invokestatic currentTimeMillis : ()J
    //   187: invokespecial <init> : (J)V
    //   190: sipush #10000
    //   193: invokevirtual nextInt : (I)I
    //   196: i2l
    //   197: ldc2_w 10000
    //   200: ladd
    //   201: invokestatic sleep : (J)V
    //   204: iload #9
    //   206: istore #10
    //   208: iload #12
    //   210: istore #5
    //   212: goto -> 94
    //   215: aload_3
    //   216: lload #7
    //   218: invokevirtual a : (J)V
    //   221: aload_0
    //   222: aload_1
    //   223: aload_2
    //   224: aload #11
    //   226: aload #4
    //   228: invokespecial a : (Ljava/lang/String;[BLjava/lang/String;Ljava/util/Map;)Ljava/net/HttpURLConnection;
    //   231: astore #13
    //   233: aload #13
    //   235: ifnull -> 725
    //   238: aload #13
    //   240: invokevirtual getResponseCode : ()I
    //   243: istore #12
    //   245: iload #12
    //   247: sipush #200
    //   250: if_icmpne -> 322
    //   253: aload_0
    //   254: aload #13
    //   256: invokestatic a : (Ljava/net/HttpURLConnection;)Ljava/util/Map;
    //   259: putfield a : Ljava/util/Map;
    //   262: aload #13
    //   264: invokestatic b : (Ljava/net/HttpURLConnection;)[B
    //   267: astore #11
    //   269: aload #11
    //   271: ifnonnull -> 294
    //   274: lconst_0
    //   275: lstore #14
    //   277: aload_3
    //   278: lload #14
    //   280: invokevirtual b : (J)V
    //   283: aload #13
    //   285: invokevirtual disconnect : ()V
    //   288: aload #11
    //   290: astore_1
    //   291: goto -> 17
    //   294: aload #11
    //   296: arraylength
    //   297: istore #9
    //   299: iload #9
    //   301: i2l
    //   302: lstore #14
    //   304: goto -> 277
    //   307: astore_1
    //   308: aload_1
    //   309: invokestatic a : (Ljava/lang/Throwable;)Z
    //   312: ifne -> 288
    //   315: aload_1
    //   316: invokevirtual printStackTrace : ()V
    //   319: goto -> 288
    //   322: iload #12
    //   324: sipush #301
    //   327: if_icmpeq -> 354
    //   330: iload #12
    //   332: sipush #302
    //   335: if_icmpeq -> 354
    //   338: iload #12
    //   340: sipush #303
    //   343: if_icmpeq -> 354
    //   346: iload #12
    //   348: sipush #307
    //   351: if_icmpne -> 418
    //   354: iconst_1
    //   355: istore #9
    //   357: iload #9
    //   359: ifeq -> 795
    //   362: aload #13
    //   364: ldc_w 'Location'
    //   367: invokevirtual getHeaderField : (Ljava/lang/String;)Ljava/lang/String;
    //   370: astore #11
    //   372: aload #11
    //   374: ifnonnull -> 439
    //   377: new java/lang/StringBuilder
    //   380: astore #11
    //   382: aload #11
    //   384: ldc_w 'Failed to redirect: %d'
    //   387: invokespecial <init> : (Ljava/lang/String;)V
    //   390: aload #11
    //   392: iload #12
    //   394: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   397: invokevirtual toString : ()Ljava/lang/String;
    //   400: iconst_0
    //   401: anewarray java/lang/Object
    //   404: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   407: pop
    //   408: aload #13
    //   410: invokevirtual disconnect : ()V
    //   413: aconst_null
    //   414: astore_1
    //   415: goto -> 17
    //   418: iconst_0
    //   419: istore #9
    //   421: goto -> 357
    //   424: astore_1
    //   425: aload_1
    //   426: invokestatic a : (Ljava/lang/Throwable;)Z
    //   429: ifne -> 413
    //   432: aload_1
    //   433: invokevirtual printStackTrace : ()V
    //   436: goto -> 413
    //   439: iinc #6, 1
    //   442: ldc_w 'redirect code: %d ,to:%s'
    //   445: iconst_2
    //   446: anewarray java/lang/Object
    //   449: dup
    //   450: iconst_0
    //   451: iload #12
    //   453: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   456: aastore
    //   457: dup
    //   458: iconst_1
    //   459: aload #11
    //   461: aastore
    //   462: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   465: pop
    //   466: iconst_1
    //   467: istore #10
    //   469: aload #11
    //   471: astore_1
    //   472: iconst_0
    //   473: istore #5
    //   475: new java/lang/StringBuilder
    //   478: astore #11
    //   480: aload #11
    //   482: ldc_w 'response code '
    //   485: invokespecial <init> : (Ljava/lang/String;)V
    //   488: aload #11
    //   490: iload #12
    //   492: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   495: invokevirtual toString : ()Ljava/lang/String;
    //   498: iconst_0
    //   499: anewarray java/lang/Object
    //   502: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   505: pop
    //   506: aload #13
    //   508: invokevirtual getContentLength : ()I
    //   511: i2l
    //   512: lstore #16
    //   514: lload #16
    //   516: lstore #14
    //   518: lload #16
    //   520: lconst_0
    //   521: lcmp
    //   522: ifge -> 528
    //   525: lconst_0
    //   526: lstore #14
    //   528: aload_3
    //   529: lload #14
    //   531: invokevirtual b : (J)V
    //   534: aload #13
    //   536: invokevirtual disconnect : ()V
    //   539: aload_1
    //   540: astore #11
    //   542: iload #5
    //   544: istore #18
    //   546: iload #6
    //   548: istore #12
    //   550: iload #10
    //   552: istore #9
    //   554: iload #18
    //   556: istore #5
    //   558: iload #12
    //   560: istore #6
    //   562: aload #11
    //   564: astore_1
    //   565: goto -> 76
    //   568: astore #19
    //   570: iload #10
    //   572: istore #9
    //   574: iload #6
    //   576: istore #12
    //   578: iload #5
    //   580: istore #18
    //   582: aload_1
    //   583: astore #11
    //   585: aload #19
    //   587: invokestatic a : (Ljava/lang/Throwable;)Z
    //   590: ifne -> 554
    //   593: aload #19
    //   595: invokevirtual printStackTrace : ()V
    //   598: iload #10
    //   600: istore #9
    //   602: iload #6
    //   604: istore #12
    //   606: iload #5
    //   608: istore #18
    //   610: aload_1
    //   611: astore #11
    //   613: goto -> 554
    //   616: astore #11
    //   618: aload #11
    //   620: invokestatic a : (Ljava/lang/Throwable;)Z
    //   623: ifne -> 631
    //   626: aload #11
    //   628: invokevirtual printStackTrace : ()V
    //   631: aload #13
    //   633: invokevirtual disconnect : ()V
    //   636: iload #10
    //   638: istore #9
    //   640: iload #6
    //   642: istore #12
    //   644: iload #5
    //   646: istore #18
    //   648: aload_1
    //   649: astore #11
    //   651: goto -> 554
    //   654: astore #19
    //   656: iload #10
    //   658: istore #9
    //   660: iload #6
    //   662: istore #12
    //   664: iload #5
    //   666: istore #18
    //   668: aload_1
    //   669: astore #11
    //   671: aload #19
    //   673: invokestatic a : (Ljava/lang/Throwable;)Z
    //   676: ifne -> 554
    //   679: aload #19
    //   681: invokevirtual printStackTrace : ()V
    //   684: iload #10
    //   686: istore #9
    //   688: iload #6
    //   690: istore #12
    //   692: iload #5
    //   694: istore #18
    //   696: aload_1
    //   697: astore #11
    //   699: goto -> 554
    //   702: astore_1
    //   703: aload #13
    //   705: invokevirtual disconnect : ()V
    //   708: aload_1
    //   709: athrow
    //   710: astore_2
    //   711: aload_2
    //   712: invokestatic a : (Ljava/lang/Throwable;)Z
    //   715: ifne -> 708
    //   718: aload_2
    //   719: invokevirtual printStackTrace : ()V
    //   722: goto -> 708
    //   725: ldc_w 'Failed to execute post.'
    //   728: iconst_0
    //   729: anewarray java/lang/Object
    //   732: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   735: pop
    //   736: aload_3
    //   737: lconst_0
    //   738: invokevirtual b : (J)V
    //   741: iload #10
    //   743: istore #9
    //   745: iload #6
    //   747: istore #12
    //   749: iload #5
    //   751: istore #18
    //   753: aload_1
    //   754: astore #11
    //   756: goto -> 554
    //   759: aconst_null
    //   760: astore_1
    //   761: goto -> 17
    //   764: astore #11
    //   766: iconst_1
    //   767: istore #10
    //   769: goto -> 618
    //   772: astore #19
    //   774: aload #11
    //   776: astore_1
    //   777: aload #19
    //   779: astore #11
    //   781: iconst_1
    //   782: istore #10
    //   784: iconst_0
    //   785: istore #5
    //   787: goto -> 618
    //   790: astore #11
    //   792: goto -> 618
    //   795: goto -> 475
    // Exception table:
    //   from	to	target	type
    //   238	245	616	java/io/IOException
    //   238	245	702	finally
    //   253	269	616	java/io/IOException
    //   253	269	702	finally
    //   277	283	616	java/io/IOException
    //   277	283	702	finally
    //   283	288	307	java/lang/Throwable
    //   294	299	616	java/io/IOException
    //   294	299	702	finally
    //   362	372	764	java/io/IOException
    //   362	372	702	finally
    //   377	408	764	java/io/IOException
    //   377	408	702	finally
    //   408	413	424	java/lang/Throwable
    //   442	466	772	java/io/IOException
    //   442	466	702	finally
    //   475	514	790	java/io/IOException
    //   475	514	702	finally
    //   528	534	790	java/io/IOException
    //   528	534	702	finally
    //   534	539	568	java/lang/Throwable
    //   618	631	702	finally
    //   631	636	654	java/lang/Throwable
    //   703	708	710	java/lang/Throwable
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/s.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */