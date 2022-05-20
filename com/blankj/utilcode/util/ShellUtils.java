package com.blankj.utilcode.util;

import java.util.List;

public final class ShellUtils {
  private ShellUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static CommandResult execCmd(String paramString, boolean paramBoolean) {
    return execCmd(new String[] { paramString }, paramBoolean, true);
  }
  
  public static CommandResult execCmd(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    return execCmd(new String[] { paramString }, paramBoolean1, paramBoolean2);
  }
  
  public static CommandResult execCmd(List<String> paramList, boolean paramBoolean) {
    if (paramList == null) {
      paramList = null;
      return execCmd((String[])paramList, paramBoolean, true);
    } 
    String[] arrayOfString = paramList.<String>toArray(new String[0]);
    return execCmd(arrayOfString, paramBoolean, true);
  }
  
  public static CommandResult execCmd(List<String> paramList, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramList == null) {
      paramList = null;
      return execCmd((String[])paramList, paramBoolean1, paramBoolean2);
    } 
    String[] arrayOfString = paramList.<String>toArray(new String[0]);
    return execCmd(arrayOfString, paramBoolean1, paramBoolean2);
  }
  
  public static CommandResult execCmd(String[] paramArrayOfString, boolean paramBoolean) {
    return execCmd(paramArrayOfString, paramBoolean, true);
  }
  
  public static CommandResult execCmd(String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: iconst_m1
    //   1: istore_3
    //   2: aload_0
    //   3: ifnull -> 11
    //   6: aload_0
    //   7: arraylength
    //   8: ifne -> 24
    //   11: new com/blankj/utilcode/util/ShellUtils$CommandResult
    //   14: dup
    //   15: iconst_m1
    //   16: aconst_null
    //   17: aconst_null
    //   18: invokespecial <init> : (ILjava/lang/String;Ljava/lang/String;)V
    //   21: astore_0
    //   22: aload_0
    //   23: areturn
    //   24: aconst_null
    //   25: astore #4
    //   27: aconst_null
    //   28: astore #5
    //   30: aconst_null
    //   31: astore #6
    //   33: aconst_null
    //   34: astore #7
    //   36: aconst_null
    //   37: astore #8
    //   39: aconst_null
    //   40: astore #9
    //   42: aconst_null
    //   43: astore #10
    //   45: aconst_null
    //   46: astore #11
    //   48: aconst_null
    //   49: astore #12
    //   51: aconst_null
    //   52: astore #13
    //   54: aconst_null
    //   55: astore #14
    //   57: aconst_null
    //   58: astore #15
    //   60: aconst_null
    //   61: astore #16
    //   63: aconst_null
    //   64: astore #17
    //   66: aconst_null
    //   67: astore #18
    //   69: aconst_null
    //   70: astore #19
    //   72: aload #12
    //   74: astore #20
    //   76: aload #18
    //   78: astore #21
    //   80: aload #5
    //   82: astore #22
    //   84: aload #8
    //   86: astore #23
    //   88: aload #4
    //   90: astore #24
    //   92: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   95: astore #25
    //   97: iload_1
    //   98: ifeq -> 223
    //   101: ldc 'su'
    //   103: astore #26
    //   105: aload #12
    //   107: astore #20
    //   109: aload #18
    //   111: astore #21
    //   113: aload #5
    //   115: astore #22
    //   117: aload #8
    //   119: astore #23
    //   121: aload #4
    //   123: astore #24
    //   125: aload #25
    //   127: aload #26
    //   129: invokevirtual exec : (Ljava/lang/String;)Ljava/lang/Process;
    //   132: astore #5
    //   134: aload #12
    //   136: astore #20
    //   138: aload #18
    //   140: astore #21
    //   142: aload #5
    //   144: astore #22
    //   146: aload #8
    //   148: astore #23
    //   150: aload #5
    //   152: astore #24
    //   154: new java/io/DataOutputStream
    //   157: astore #26
    //   159: aload #12
    //   161: astore #20
    //   163: aload #18
    //   165: astore #21
    //   167: aload #5
    //   169: astore #22
    //   171: aload #8
    //   173: astore #23
    //   175: aload #5
    //   177: astore #24
    //   179: aload #26
    //   181: aload #5
    //   183: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   186: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   189: iload_3
    //   190: istore #27
    //   192: aload_0
    //   193: arraylength
    //   194: istore #28
    //   196: iconst_0
    //   197: istore #29
    //   199: iload #29
    //   201: iload #28
    //   203: if_icmpge -> 381
    //   206: aload_0
    //   207: iload #29
    //   209: aaload
    //   210: astore #24
    //   212: aload #24
    //   214: ifnonnull -> 230
    //   217: iinc #29, 1
    //   220: goto -> 199
    //   223: ldc 'sh'
    //   225: astore #26
    //   227: goto -> 105
    //   230: iload_3
    //   231: istore #27
    //   233: aload #26
    //   235: aload #24
    //   237: invokevirtual getBytes : ()[B
    //   240: invokevirtual write : ([B)V
    //   243: iload_3
    //   244: istore #27
    //   246: aload #26
    //   248: ldc '\\n'
    //   250: invokevirtual writeBytes : (Ljava/lang/String;)V
    //   253: iload_3
    //   254: istore #27
    //   256: aload #26
    //   258: invokevirtual flush : ()V
    //   261: goto -> 217
    //   264: astore #22
    //   266: aload #26
    //   268: astore_0
    //   269: aload #15
    //   271: astore #24
    //   273: aload #17
    //   275: astore #11
    //   277: aload #22
    //   279: astore #26
    //   281: aload #13
    //   283: astore #20
    //   285: aload_0
    //   286: astore #21
    //   288: aload #5
    //   290: astore #22
    //   292: aload #9
    //   294: astore #23
    //   296: aload #26
    //   298: invokevirtual printStackTrace : ()V
    //   301: iconst_3
    //   302: anewarray java/io/Closeable
    //   305: dup
    //   306: iconst_0
    //   307: aload_0
    //   308: aastore
    //   309: dup
    //   310: iconst_1
    //   311: aload #9
    //   313: aastore
    //   314: dup
    //   315: iconst_2
    //   316: aload #13
    //   318: aastore
    //   319: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   322: aload #11
    //   324: astore #26
    //   326: iload #27
    //   328: istore_3
    //   329: aload #24
    //   331: astore_0
    //   332: aload #5
    //   334: ifnull -> 352
    //   337: aload #5
    //   339: invokevirtual destroy : ()V
    //   342: aload #24
    //   344: astore_0
    //   345: iload #27
    //   347: istore_3
    //   348: aload #11
    //   350: astore #26
    //   352: aload_0
    //   353: ifnonnull -> 687
    //   356: aconst_null
    //   357: astore_0
    //   358: aload #26
    //   360: ifnonnull -> 695
    //   363: aconst_null
    //   364: astore #26
    //   366: new com/blankj/utilcode/util/ShellUtils$CommandResult
    //   369: dup
    //   370: iload_3
    //   371: aload_0
    //   372: aload #26
    //   374: invokespecial <init> : (ILjava/lang/String;Ljava/lang/String;)V
    //   377: astore_0
    //   378: goto -> 22
    //   381: iload_3
    //   382: istore #27
    //   384: aload #26
    //   386: ldc 'exit\\n'
    //   388: invokevirtual writeBytes : (Ljava/lang/String;)V
    //   391: iload_3
    //   392: istore #27
    //   394: aload #26
    //   396: invokevirtual flush : ()V
    //   399: iload_3
    //   400: istore #27
    //   402: aload #5
    //   404: invokevirtual waitFor : ()I
    //   407: istore_3
    //   408: aload #16
    //   410: astore #24
    //   412: aload #14
    //   414: astore_0
    //   415: aload #7
    //   417: astore #22
    //   419: iload_2
    //   420: ifeq -> 648
    //   423: iload_3
    //   424: istore #27
    //   426: new java/lang/StringBuilder
    //   429: dup
    //   430: invokespecial <init> : ()V
    //   433: astore #24
    //   435: new java/lang/StringBuilder
    //   438: astore #11
    //   440: aload #11
    //   442: invokespecial <init> : ()V
    //   445: new java/io/BufferedReader
    //   448: astore_0
    //   449: new java/io/InputStreamReader
    //   452: astore #22
    //   454: aload #22
    //   456: aload #5
    //   458: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   461: ldc 'UTF-8'
    //   463: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   466: aload_0
    //   467: aload #22
    //   469: invokespecial <init> : (Ljava/io/Reader;)V
    //   472: new java/io/BufferedReader
    //   475: astore #22
    //   477: new java/io/InputStreamReader
    //   480: astore #9
    //   482: aload #9
    //   484: aload #5
    //   486: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   489: ldc 'UTF-8'
    //   491: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   494: aload #22
    //   496: aload #9
    //   498: invokespecial <init> : (Ljava/io/Reader;)V
    //   501: aload_0
    //   502: invokevirtual readLine : ()Ljava/lang/String;
    //   505: astore #9
    //   507: aload #9
    //   509: ifnull -> 553
    //   512: aload #24
    //   514: aload #9
    //   516: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   519: pop
    //   520: goto -> 501
    //   523: astore #23
    //   525: aload #26
    //   527: astore #9
    //   529: aload #22
    //   531: astore #13
    //   533: aload_0
    //   534: astore #22
    //   536: aload #23
    //   538: astore #26
    //   540: aload #9
    //   542: astore_0
    //   543: iload_3
    //   544: istore #27
    //   546: aload #22
    //   548: astore #9
    //   550: goto -> 281
    //   553: aload #22
    //   555: invokevirtual readLine : ()Ljava/lang/String;
    //   558: astore #9
    //   560: aload #9
    //   562: ifnull -> 630
    //   565: aload #11
    //   567: aload #9
    //   569: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   572: pop
    //   573: goto -> 553
    //   576: astore #24
    //   578: aload #26
    //   580: astore #11
    //   582: aload #24
    //   584: astore #26
    //   586: aload_0
    //   587: astore #24
    //   589: aload #11
    //   591: astore_0
    //   592: aload #22
    //   594: astore #20
    //   596: iconst_3
    //   597: anewarray java/io/Closeable
    //   600: dup
    //   601: iconst_0
    //   602: aload_0
    //   603: aastore
    //   604: dup
    //   605: iconst_1
    //   606: aload #24
    //   608: aastore
    //   609: dup
    //   610: iconst_2
    //   611: aload #20
    //   613: aastore
    //   614: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   617: aload #5
    //   619: ifnull -> 627
    //   622: aload #5
    //   624: invokevirtual destroy : ()V
    //   627: aload #26
    //   629: athrow
    //   630: aload #11
    //   632: astore #9
    //   634: aload #22
    //   636: astore #11
    //   638: aload_0
    //   639: astore #22
    //   641: aload #24
    //   643: astore_0
    //   644: aload #9
    //   646: astore #24
    //   648: iconst_3
    //   649: anewarray java/io/Closeable
    //   652: dup
    //   653: iconst_0
    //   654: aload #26
    //   656: aastore
    //   657: dup
    //   658: iconst_1
    //   659: aload #22
    //   661: aastore
    //   662: dup
    //   663: iconst_2
    //   664: aload #11
    //   666: aastore
    //   667: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   670: aload #5
    //   672: ifnull -> 879
    //   675: aload #5
    //   677: invokevirtual destroy : ()V
    //   680: aload #24
    //   682: astore #26
    //   684: goto -> 352
    //   687: aload_0
    //   688: invokevirtual toString : ()Ljava/lang/String;
    //   691: astore_0
    //   692: goto -> 358
    //   695: aload #26
    //   697: invokevirtual toString : ()Ljava/lang/String;
    //   700: astore #26
    //   702: goto -> 366
    //   705: astore #26
    //   707: aload #21
    //   709: astore_0
    //   710: aload #22
    //   712: astore #5
    //   714: aload #23
    //   716: astore #24
    //   718: goto -> 596
    //   721: astore #11
    //   723: aload #26
    //   725: astore_0
    //   726: aload #10
    //   728: astore #20
    //   730: aload #6
    //   732: astore #24
    //   734: aload #11
    //   736: astore #26
    //   738: goto -> 596
    //   741: astore #11
    //   743: aload #26
    //   745: astore_0
    //   746: aload #10
    //   748: astore #20
    //   750: aload #6
    //   752: astore #24
    //   754: aload #11
    //   756: astore #26
    //   758: goto -> 596
    //   761: astore #11
    //   763: aload #26
    //   765: astore_0
    //   766: aload #10
    //   768: astore #20
    //   770: aload #6
    //   772: astore #24
    //   774: aload #11
    //   776: astore #26
    //   778: goto -> 596
    //   781: astore #11
    //   783: aload_0
    //   784: astore #24
    //   786: aload #10
    //   788: astore #20
    //   790: aload #26
    //   792: astore_0
    //   793: aload #11
    //   795: astore #26
    //   797: goto -> 596
    //   800: astore #26
    //   802: aload #17
    //   804: astore #11
    //   806: aload #19
    //   808: astore_0
    //   809: aload #24
    //   811: astore #5
    //   813: iload_3
    //   814: istore #27
    //   816: aload #15
    //   818: astore #24
    //   820: goto -> 281
    //   823: astore #11
    //   825: aload #26
    //   827: astore_0
    //   828: aload #11
    //   830: astore #26
    //   832: aload #17
    //   834: astore #11
    //   836: iload_3
    //   837: istore #27
    //   839: goto -> 281
    //   842: astore #22
    //   844: aload #26
    //   846: astore_0
    //   847: aload #22
    //   849: astore #26
    //   851: iload_3
    //   852: istore #27
    //   854: goto -> 281
    //   857: astore #23
    //   859: aload #26
    //   861: astore #22
    //   863: aload_0
    //   864: astore #9
    //   866: aload #23
    //   868: astore #26
    //   870: aload #22
    //   872: astore_0
    //   873: iload_3
    //   874: istore #27
    //   876: goto -> 281
    //   879: aload #24
    //   881: astore #26
    //   883: goto -> 352
    // Exception table:
    //   from	to	target	type
    //   92	97	800	java/lang/Exception
    //   92	97	705	finally
    //   125	134	800	java/lang/Exception
    //   125	134	705	finally
    //   154	159	800	java/lang/Exception
    //   154	159	705	finally
    //   179	189	800	java/lang/Exception
    //   179	189	705	finally
    //   192	196	264	java/lang/Exception
    //   192	196	721	finally
    //   233	243	264	java/lang/Exception
    //   233	243	721	finally
    //   246	253	264	java/lang/Exception
    //   246	253	721	finally
    //   256	261	264	java/lang/Exception
    //   256	261	721	finally
    //   296	301	705	finally
    //   384	391	264	java/lang/Exception
    //   384	391	721	finally
    //   394	399	264	java/lang/Exception
    //   394	399	721	finally
    //   402	408	264	java/lang/Exception
    //   402	408	721	finally
    //   426	435	264	java/lang/Exception
    //   426	435	721	finally
    //   435	445	823	java/lang/Exception
    //   435	445	741	finally
    //   445	472	842	java/lang/Exception
    //   445	472	761	finally
    //   472	501	857	java/lang/Exception
    //   472	501	781	finally
    //   501	507	523	java/lang/Exception
    //   501	507	576	finally
    //   512	520	523	java/lang/Exception
    //   512	520	576	finally
    //   553	560	523	java/lang/Exception
    //   553	560	576	finally
    //   565	573	523	java/lang/Exception
    //   565	573	576	finally
  }
  
  public static class CommandResult {
    public String errorMsg;
    
    public int result;
    
    public String successMsg;
    
    public CommandResult(int param1Int, String param1String1, String param1String2) {
      this.result = param1Int;
      this.successMsg = param1String1;
      this.errorMsg = param1String2;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ShellUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */