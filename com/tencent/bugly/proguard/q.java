package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tencent.bugly.a;
import java.util.List;

public final class q extends SQLiteOpenHelper {
  public static String a = "bugly_db";
  
  private static int b = 13;
  
  private Context c;
  
  private List<a> d;
  
  public q(Context paramContext, List<a> paramList) {
    super(paramContext, stringBuilder.toString(), null, b);
    this.c = paramContext;
    this.d = paramList;
  }
  
  private boolean a(SQLiteDatabase paramSQLiteDatabase) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: iconst_3
    //   5: anewarray java/lang/String
    //   8: astore_3
    //   9: aload_3
    //   10: iconst_0
    //   11: ldc 't_lr'
    //   13: aastore
    //   14: aload_3
    //   15: iconst_1
    //   16: ldc 't_ui'
    //   18: aastore
    //   19: aload_3
    //   20: iconst_2
    //   21: ldc 't_pf'
    //   23: aastore
    //   24: aload_3
    //   25: arraylength
    //   26: istore #4
    //   28: iconst_0
    //   29: istore #5
    //   31: iload_2
    //   32: istore #6
    //   34: iload #5
    //   36: iload #4
    //   38: if_icmpge -> 98
    //   41: aload_3
    //   42: iload #5
    //   44: aaload
    //   45: astore #7
    //   47: new java/lang/StringBuilder
    //   50: astore #8
    //   52: aload #8
    //   54: ldc 'DROP TABLE IF EXISTS '
    //   56: invokespecial <init> : (Ljava/lang/String;)V
    //   59: aload_1
    //   60: aload #8
    //   62: aload #7
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: iconst_0
    //   71: anewarray java/lang/String
    //   74: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   77: iinc #5, 1
    //   80: goto -> 31
    //   83: astore_1
    //   84: aload_1
    //   85: invokestatic b : (Ljava/lang/Throwable;)Z
    //   88: ifne -> 95
    //   91: aload_1
    //   92: invokevirtual printStackTrace : ()V
    //   95: iconst_0
    //   96: istore #6
    //   98: aload_0
    //   99: monitorexit
    //   100: iload #6
    //   102: ireturn
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    // Exception table:
    //   from	to	target	type
    //   4	9	83	java/lang/Throwable
    //   4	9	103	finally
    //   24	28	83	java/lang/Throwable
    //   24	28	103	finally
    //   47	77	83	java/lang/Throwable
    //   47	77	103	finally
    //   84	95	103	finally
  }
  
  public final SQLiteDatabase getReadableDatabase() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aconst_null
    //   5: astore_2
    //   6: aload_2
    //   7: ifnonnull -> 83
    //   10: iload_1
    //   11: iconst_5
    //   12: if_icmpge -> 83
    //   15: iinc #1, 1
    //   18: aload_0
    //   19: invokespecial getReadableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   22: astore_3
    //   23: aload_3
    //   24: astore_2
    //   25: goto -> 6
    //   28: astore_3
    //   29: ldc '[Database] Try to get db(count: %d).'
    //   31: iconst_1
    //   32: anewarray java/lang/Object
    //   35: dup
    //   36: iconst_0
    //   37: iload_1
    //   38: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   41: aastore
    //   42: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   45: pop
    //   46: iload_1
    //   47: iconst_5
    //   48: if_icmpne -> 61
    //   51: ldc '[Database] Failed to get db.'
    //   53: iconst_0
    //   54: anewarray java/lang/Object
    //   57: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   60: pop
    //   61: ldc2_w 200
    //   64: invokestatic sleep : (J)V
    //   67: goto -> 6
    //   70: astore_3
    //   71: aload_3
    //   72: invokevirtual printStackTrace : ()V
    //   75: goto -> 6
    //   78: astore_2
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_2
    //   82: athrow
    //   83: aload_0
    //   84: monitorexit
    //   85: aload_2
    //   86: areturn
    // Exception table:
    //   from	to	target	type
    //   18	23	28	java/lang/Throwable
    //   18	23	78	finally
    //   29	46	78	finally
    //   51	61	78	finally
    //   61	67	70	java/lang/InterruptedException
    //   61	67	78	finally
    //   71	75	78	finally
  }
  
  public final SQLiteDatabase getWritableDatabase() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aconst_null
    //   5: astore_2
    //   6: aload_2
    //   7: ifnonnull -> 83
    //   10: iload_1
    //   11: iconst_5
    //   12: if_icmpge -> 83
    //   15: iinc #1, 1
    //   18: aload_0
    //   19: invokespecial getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   22: astore_3
    //   23: aload_3
    //   24: astore_2
    //   25: goto -> 6
    //   28: astore_3
    //   29: ldc '[Database] Try to get db(count: %d).'
    //   31: iconst_1
    //   32: anewarray java/lang/Object
    //   35: dup
    //   36: iconst_0
    //   37: iload_1
    //   38: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   41: aastore
    //   42: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   45: pop
    //   46: iload_1
    //   47: iconst_5
    //   48: if_icmpne -> 61
    //   51: ldc '[Database] Failed to get db.'
    //   53: iconst_0
    //   54: anewarray java/lang/Object
    //   57: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   60: pop
    //   61: ldc2_w 200
    //   64: invokestatic sleep : (J)V
    //   67: goto -> 6
    //   70: astore_3
    //   71: aload_3
    //   72: invokevirtual printStackTrace : ()V
    //   75: goto -> 6
    //   78: astore_2
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_2
    //   82: athrow
    //   83: aload_2
    //   84: ifnonnull -> 97
    //   87: ldc '[Database] db error delay error record 1min.'
    //   89: iconst_0
    //   90: anewarray java/lang/Object
    //   93: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   96: pop
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_2
    //   100: areturn
    // Exception table:
    //   from	to	target	type
    //   18	23	28	java/lang/Throwable
    //   18	23	78	finally
    //   29	46	78	finally
    //   51	61	78	finally
    //   61	67	70	java/lang/InterruptedException
    //   61	67	78	finally
    //   71	75	78	finally
    //   87	97	78	finally
  }
  
  public final void onCreate(SQLiteDatabase paramSQLiteDatabase) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_2
    //   11: iconst_0
    //   12: invokevirtual setLength : (I)V
    //   15: aload_2
    //   16: ldc ' CREATE TABLE IF NOT EXISTS t_ui'
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc ' ( _id'
    //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: ldc ' INTEGER PRIMARY KEY'
    //   28: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: ldc ' , _tm'
    //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: ldc ' int'
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: ldc ' , _ut'
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: ldc ' int'
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: ldc ' , _tp'
    //   53: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: ldc ' int'
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: ldc ' , _dt'
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: ldc ' blob'
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: ldc ' , _pc'
    //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: ldc ' text'
    //   78: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: ldc ' ) '
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_2
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: iconst_0
    //   92: anewarray java/lang/Object
    //   95: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   98: pop
    //   99: aload_1
    //   100: aload_2
    //   101: invokevirtual toString : ()Ljava/lang/String;
    //   104: iconst_0
    //   105: anewarray java/lang/String
    //   108: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   111: aload_2
    //   112: iconst_0
    //   113: invokevirtual setLength : (I)V
    //   116: aload_2
    //   117: ldc ' CREATE TABLE IF NOT EXISTS t_lr'
    //   119: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: ldc ' ( _id'
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: ldc ' INTEGER PRIMARY KEY'
    //   129: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: ldc ' , _tp'
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: ldc ' int'
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: ldc ' , _tm'
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: ldc ' int'
    //   149: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: ldc ' , _pc'
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: ldc ' text'
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: ldc ' , _th'
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: ldc ' text'
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: ldc ' , _dt'
    //   174: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: ldc ' blob'
    //   179: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: ldc ' ) '
    //   184: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: aload_2
    //   189: invokevirtual toString : ()Ljava/lang/String;
    //   192: iconst_0
    //   193: anewarray java/lang/Object
    //   196: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   199: pop
    //   200: aload_1
    //   201: aload_2
    //   202: invokevirtual toString : ()Ljava/lang/String;
    //   205: iconst_0
    //   206: anewarray java/lang/String
    //   209: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   212: aload_2
    //   213: iconst_0
    //   214: invokevirtual setLength : (I)V
    //   217: aload_2
    //   218: ldc ' CREATE TABLE IF NOT EXISTS t_pf'
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: ldc ' ( _id'
    //   225: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: ldc ' integer'
    //   230: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: ldc ' , _tp'
    //   235: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: ldc ' text'
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: ldc ' , _tm'
    //   245: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: ldc ' int'
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: ldc ' , _dt'
    //   255: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: ldc ' blob'
    //   260: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: ldc ',primary key(_id'
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: ldc ',_tp'
    //   270: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: ldc ' )) '
    //   275: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload_2
    //   280: invokevirtual toString : ()Ljava/lang/String;
    //   283: iconst_0
    //   284: anewarray java/lang/Object
    //   287: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   290: pop
    //   291: aload_1
    //   292: aload_2
    //   293: invokevirtual toString : ()Ljava/lang/String;
    //   296: iconst_0
    //   297: anewarray java/lang/String
    //   300: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   303: aload_2
    //   304: iconst_0
    //   305: invokevirtual setLength : (I)V
    //   308: aload_2
    //   309: ldc ' CREATE TABLE IF NOT EXISTS t_cr'
    //   311: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: ldc ' ( _id'
    //   316: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: ldc ' INTEGER PRIMARY KEY'
    //   321: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   324: ldc ' , _tm'
    //   326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: ldc ' int'
    //   331: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   334: ldc ' , _s1'
    //   336: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   339: ldc ' text'
    //   341: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   344: ldc ' , _up'
    //   346: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   349: ldc ' int'
    //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: ldc ' , _me'
    //   356: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   359: ldc ' int'
    //   361: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: ldc ' , _uc'
    //   366: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   369: ldc ' int'
    //   371: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: ldc ' , _dt'
    //   376: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   379: ldc ' blob'
    //   381: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: ldc ' ) '
    //   386: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   389: pop
    //   390: aload_2
    //   391: invokevirtual toString : ()Ljava/lang/String;
    //   394: iconst_0
    //   395: anewarray java/lang/Object
    //   398: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   401: pop
    //   402: aload_1
    //   403: aload_2
    //   404: invokevirtual toString : ()Ljava/lang/String;
    //   407: iconst_0
    //   408: anewarray java/lang/String
    //   411: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   414: aload_2
    //   415: iconst_0
    //   416: invokevirtual setLength : (I)V
    //   419: aload_2
    //   420: ldc ' CREATE TABLE IF NOT EXISTS dl_1002'
    //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: ldc ' (_id'
    //   427: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   430: ldc ' integer primary key autoincrement, _dUrl'
    //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: ldc ' varchar(100), _sFile'
    //   437: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   440: ldc ' varchar(100), _sLen'
    //   442: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: ldc ' INTEGER, _tLen'
    //   447: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   450: ldc ' INTEGER, _MD5'
    //   452: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: ldc ' varchar(100), _DLTIME'
    //   457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   460: ldc ' INTEGER)'
    //   462: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   465: pop
    //   466: aload_2
    //   467: invokevirtual toString : ()Ljava/lang/String;
    //   470: iconst_0
    //   471: anewarray java/lang/Object
    //   474: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   477: pop
    //   478: aload_1
    //   479: aload_2
    //   480: invokevirtual toString : ()Ljava/lang/String;
    //   483: iconst_0
    //   484: anewarray java/lang/String
    //   487: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   490: aload_2
    //   491: iconst_0
    //   492: invokevirtual setLength : (I)V
    //   495: aload_2
    //   496: ldc 'CREATE TABLE IF NOT EXISTS ge_1002'
    //   498: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   501: ldc ' (_id'
    //   503: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: ldc ' integer primary key autoincrement, _time'
    //   508: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: ldc ' INTEGER, _datas'
    //   513: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   516: ldc ' blob)'
    //   518: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   521: pop
    //   522: aload_2
    //   523: invokevirtual toString : ()Ljava/lang/String;
    //   526: iconst_0
    //   527: anewarray java/lang/Object
    //   530: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   533: pop
    //   534: aload_1
    //   535: aload_2
    //   536: invokevirtual toString : ()Ljava/lang/String;
    //   539: iconst_0
    //   540: anewarray java/lang/String
    //   543: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   546: aload_2
    //   547: iconst_0
    //   548: invokevirtual setLength : (I)V
    //   551: aload_2
    //   552: ldc ' CREATE TABLE IF NOT EXISTS st_1002'
    //   554: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   557: ldc ' ( _id'
    //   559: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   562: ldc ' integer'
    //   564: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   567: ldc ' , _tp'
    //   569: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   572: ldc ' text'
    //   574: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   577: ldc ' , _tm'
    //   579: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: ldc ' int'
    //   584: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   587: ldc ' , _dt'
    //   589: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   592: ldc ' blob'
    //   594: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   597: ldc ',primary key(_id'
    //   599: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   602: ldc ',_tp'
    //   604: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   607: ldc ' )) '
    //   609: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   612: pop
    //   613: aload_2
    //   614: invokevirtual toString : ()Ljava/lang/String;
    //   617: iconst_0
    //   618: anewarray java/lang/Object
    //   621: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   624: pop
    //   625: aload_1
    //   626: aload_2
    //   627: invokevirtual toString : ()Ljava/lang/String;
    //   630: iconst_0
    //   631: anewarray java/lang/String
    //   634: invokevirtual execSQL : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   637: aload_0
    //   638: getfield d : Ljava/util/List;
    //   641: astore_2
    //   642: aload_2
    //   643: ifnonnull -> 669
    //   646: aload_0
    //   647: monitorexit
    //   648: return
    //   649: astore_2
    //   650: aload_2
    //   651: invokestatic b : (Ljava/lang/Throwable;)Z
    //   654: ifne -> 637
    //   657: aload_2
    //   658: invokevirtual printStackTrace : ()V
    //   661: goto -> 637
    //   664: astore_1
    //   665: aload_0
    //   666: monitorexit
    //   667: aload_1
    //   668: athrow
    //   669: aload_0
    //   670: getfield d : Ljava/util/List;
    //   673: invokeinterface iterator : ()Ljava/util/Iterator;
    //   678: astore_2
    //   679: aload_2
    //   680: invokeinterface hasNext : ()Z
    //   685: ifeq -> 646
    //   688: aload_2
    //   689: invokeinterface next : ()Ljava/lang/Object;
    //   694: checkcast com/tencent/bugly/a
    //   697: astore_3
    //   698: aload_3
    //   699: aload_1
    //   700: invokevirtual onDbCreate : (Landroid/database/sqlite/SQLiteDatabase;)V
    //   703: goto -> 679
    //   706: astore_3
    //   707: aload_3
    //   708: invokestatic b : (Ljava/lang/Throwable;)Z
    //   711: ifne -> 679
    //   714: aload_3
    //   715: invokevirtual printStackTrace : ()V
    //   718: goto -> 679
    // Exception table:
    //   from	to	target	type
    //   2	637	649	java/lang/Throwable
    //   2	637	664	finally
    //   637	642	664	finally
    //   650	661	664	finally
    //   669	679	664	finally
    //   679	698	664	finally
    //   698	703	706	java/lang/Throwable
    //   698	703	664	finally
    //   707	718	664	finally
  }
  
  @TargetApi(11)
  public final void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic c : ()I
    //   5: bipush #11
    //   7: if_icmplt -> 121
    //   10: ldc '[Database] Downgrade %d to %d drop tables.'
    //   12: iconst_2
    //   13: anewarray java/lang/Object
    //   16: dup
    //   17: iconst_0
    //   18: iload_2
    //   19: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   22: aastore
    //   23: dup
    //   24: iconst_1
    //   25: iload_3
    //   26: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   29: aastore
    //   30: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   33: pop
    //   34: aload_0
    //   35: getfield d : Ljava/util/List;
    //   38: ifnull -> 108
    //   41: aload_0
    //   42: getfield d : Ljava/util/List;
    //   45: invokeinterface iterator : ()Ljava/util/Iterator;
    //   50: astore #4
    //   52: aload #4
    //   54: invokeinterface hasNext : ()Z
    //   59: ifeq -> 108
    //   62: aload #4
    //   64: invokeinterface next : ()Ljava/lang/Object;
    //   69: checkcast com/tencent/bugly/a
    //   72: astore #5
    //   74: aload #5
    //   76: aload_1
    //   77: iload_2
    //   78: iload_3
    //   79: invokevirtual onDbDowngrade : (Landroid/database/sqlite/SQLiteDatabase;II)V
    //   82: goto -> 52
    //   85: astore #5
    //   87: aload #5
    //   89: invokestatic b : (Ljava/lang/Throwable;)Z
    //   92: ifne -> 52
    //   95: aload #5
    //   97: invokevirtual printStackTrace : ()V
    //   100: goto -> 52
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: aload_0
    //   109: aload_1
    //   110: invokespecial a : (Landroid/database/sqlite/SQLiteDatabase;)Z
    //   113: ifeq -> 124
    //   116: aload_0
    //   117: aload_1
    //   118: invokevirtual onCreate : (Landroid/database/sqlite/SQLiteDatabase;)V
    //   121: aload_0
    //   122: monitorexit
    //   123: return
    //   124: ldc '[Database] Failed to drop, delete db.'
    //   126: iconst_0
    //   127: anewarray java/lang/Object
    //   130: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   133: pop
    //   134: aload_0
    //   135: getfield c : Landroid/content/Context;
    //   138: getstatic com/tencent/bugly/proguard/q.a : Ljava/lang/String;
    //   141: invokevirtual getDatabasePath : (Ljava/lang/String;)Ljava/io/File;
    //   144: astore_1
    //   145: aload_1
    //   146: ifnull -> 121
    //   149: aload_1
    //   150: invokevirtual canWrite : ()Z
    //   153: ifeq -> 121
    //   156: aload_1
    //   157: invokevirtual delete : ()Z
    //   160: pop
    //   161: goto -> 121
    // Exception table:
    //   from	to	target	type
    //   2	52	103	finally
    //   52	74	103	finally
    //   74	82	85	java/lang/Throwable
    //   74	82	103	finally
    //   87	100	103	finally
    //   108	121	103	finally
    //   124	145	103	finally
    //   149	161	103	finally
  }
  
  public final void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc_w '[Database] Upgrade %d to %d , drop tables!'
    //   5: iconst_2
    //   6: anewarray java/lang/Object
    //   9: dup
    //   10: iconst_0
    //   11: iload_2
    //   12: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   15: aastore
    //   16: dup
    //   17: iconst_1
    //   18: iload_3
    //   19: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   22: aastore
    //   23: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   26: pop
    //   27: aload_0
    //   28: getfield d : Ljava/util/List;
    //   31: ifnull -> 101
    //   34: aload_0
    //   35: getfield d : Ljava/util/List;
    //   38: invokeinterface iterator : ()Ljava/util/Iterator;
    //   43: astore #4
    //   45: aload #4
    //   47: invokeinterface hasNext : ()Z
    //   52: ifeq -> 101
    //   55: aload #4
    //   57: invokeinterface next : ()Ljava/lang/Object;
    //   62: checkcast com/tencent/bugly/a
    //   65: astore #5
    //   67: aload #5
    //   69: aload_1
    //   70: iload_2
    //   71: iload_3
    //   72: invokevirtual onDbUpgrade : (Landroid/database/sqlite/SQLiteDatabase;II)V
    //   75: goto -> 45
    //   78: astore #5
    //   80: aload #5
    //   82: invokestatic b : (Ljava/lang/Throwable;)Z
    //   85: ifne -> 45
    //   88: aload #5
    //   90: invokevirtual printStackTrace : ()V
    //   93: goto -> 45
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    //   101: aload_0
    //   102: aload_1
    //   103: invokespecial a : (Landroid/database/sqlite/SQLiteDatabase;)Z
    //   106: ifeq -> 117
    //   109: aload_0
    //   110: aload_1
    //   111: invokevirtual onCreate : (Landroid/database/sqlite/SQLiteDatabase;)V
    //   114: aload_0
    //   115: monitorexit
    //   116: return
    //   117: ldc '[Database] Failed to drop, delete db.'
    //   119: iconst_0
    //   120: anewarray java/lang/Object
    //   123: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   126: pop
    //   127: aload_0
    //   128: getfield c : Landroid/content/Context;
    //   131: getstatic com/tencent/bugly/proguard/q.a : Ljava/lang/String;
    //   134: invokevirtual getDatabasePath : (Ljava/lang/String;)Ljava/io/File;
    //   137: astore_1
    //   138: aload_1
    //   139: ifnull -> 114
    //   142: aload_1
    //   143: invokevirtual canWrite : ()Z
    //   146: ifeq -> 114
    //   149: aload_1
    //   150: invokevirtual delete : ()Z
    //   153: pop
    //   154: goto -> 114
    // Exception table:
    //   from	to	target	type
    //   2	45	96	finally
    //   45	67	96	finally
    //   67	75	78	java/lang/Throwable
    //   67	75	96	finally
    //   80	93	96	finally
    //   101	114	96	finally
    //   117	138	96	finally
    //   142	154	96	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/q.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */