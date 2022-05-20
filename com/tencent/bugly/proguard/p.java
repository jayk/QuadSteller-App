package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.List;
import java.util.Map;

public final class p {
  private static p a = null;
  
  private static q b = null;
  
  private static boolean c = false;
  
  private p(Context paramContext, List<com.tencent.bugly.a> paramList) {
    b = new q(paramContext, paramList);
  }
  
  private int a(String paramString1, String paramString2, String[] paramArrayOfString, o paramo) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iconst_0
    //   4: istore #6
    //   6: aload_0
    //   7: monitorenter
    //   8: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   11: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   14: astore #7
    //   16: aload #7
    //   18: ifnull -> 31
    //   21: aload #7
    //   23: aload_1
    //   24: aload_2
    //   25: aload_3
    //   26: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   29: istore #6
    //   31: iload #6
    //   33: istore #8
    //   35: aload #4
    //   37: ifnull -> 44
    //   40: iload #6
    //   42: istore #8
    //   44: aload_0
    //   45: monitorexit
    //   46: iload #8
    //   48: ireturn
    //   49: astore_1
    //   50: aload_1
    //   51: invokestatic a : (Ljava/lang/Throwable;)Z
    //   54: ifne -> 61
    //   57: aload_1
    //   58: invokevirtual printStackTrace : ()V
    //   61: iload #5
    //   63: istore #8
    //   65: aload #4
    //   67: ifnull -> 44
    //   70: iload #5
    //   72: istore #8
    //   74: goto -> 44
    //   77: astore_1
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_1
    //   81: athrow
    //   82: astore_1
    //   83: aload #4
    //   85: ifnull -> 88
    //   88: aload_1
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   8	16	49	java/lang/Throwable
    //   8	16	82	finally
    //   21	31	49	java/lang/Throwable
    //   21	31	82	finally
    //   50	61	82	finally
    //   88	90	77	finally
  }
  
  private long a(String paramString, ContentValues paramContentValues, o paramo) {
    // Byte code:
    //   0: lconst_0
    //   1: lstore #4
    //   3: aload_0
    //   4: monitorenter
    //   5: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   8: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   11: astore #6
    //   13: lload #4
    //   15: lstore #7
    //   17: aload #6
    //   19: ifnull -> 62
    //   22: lload #4
    //   24: lstore #7
    //   26: aload_2
    //   27: ifnull -> 62
    //   30: aload #6
    //   32: aload_1
    //   33: ldc '_id'
    //   35: aload_2
    //   36: invokevirtual replace : (Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   39: lstore #7
    //   41: lload #7
    //   43: lconst_0
    //   44: lcmp
    //   45: iflt -> 79
    //   48: ldc '[Database] insert %s success.'
    //   50: iconst_1
    //   51: anewarray java/lang/Object
    //   54: dup
    //   55: iconst_0
    //   56: aload_1
    //   57: aastore
    //   58: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   61: pop
    //   62: lload #7
    //   64: lstore #9
    //   66: aload_3
    //   67: ifnull -> 74
    //   70: lload #7
    //   72: lstore #9
    //   74: aload_0
    //   75: monitorexit
    //   76: lload #9
    //   78: lreturn
    //   79: ldc '[Database] replace %s error.'
    //   81: iconst_1
    //   82: anewarray java/lang/Object
    //   85: dup
    //   86: iconst_0
    //   87: aload_1
    //   88: aastore
    //   89: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   92: pop
    //   93: goto -> 62
    //   96: astore_1
    //   97: aload_1
    //   98: invokestatic a : (Ljava/lang/Throwable;)Z
    //   101: ifne -> 108
    //   104: aload_1
    //   105: invokevirtual printStackTrace : ()V
    //   108: lload #4
    //   110: lstore #9
    //   112: aload_3
    //   113: ifnull -> 74
    //   116: lload #4
    //   118: lstore #9
    //   120: goto -> 74
    //   123: astore_1
    //   124: aload_0
    //   125: monitorexit
    //   126: aload_1
    //   127: athrow
    //   128: astore_1
    //   129: aload_3
    //   130: ifnull -> 133
    //   133: aload_1
    //   134: athrow
    // Exception table:
    //   from	to	target	type
    //   5	13	96	java/lang/Throwable
    //   5	13	128	finally
    //   30	41	96	java/lang/Throwable
    //   30	41	128	finally
    //   48	62	96	java/lang/Throwable
    //   48	62	128	finally
    //   79	93	96	java/lang/Throwable
    //   79	93	128	finally
    //   97	108	128	finally
    //   133	135	123	finally
  }
  
  private Cursor a(boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6, o paramo) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   5: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   8: astore #11
    //   10: aload #11
    //   12: ifnull -> 80
    //   15: aload #11
    //   17: iload_1
    //   18: aload_2
    //   19: aload_3
    //   20: aload #4
    //   22: aload #5
    //   24: aload #6
    //   26: aload #7
    //   28: aload #8
    //   30: aload #9
    //   32: invokevirtual query : (ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   35: astore_2
    //   36: aload #10
    //   38: ifnull -> 41
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_2
    //   44: areturn
    //   45: astore_2
    //   46: aload_2
    //   47: invokestatic a : (Ljava/lang/Throwable;)Z
    //   50: ifne -> 57
    //   53: aload_2
    //   54: invokevirtual printStackTrace : ()V
    //   57: aload #10
    //   59: ifnull -> 75
    //   62: aconst_null
    //   63: astore_2
    //   64: goto -> 41
    //   67: astore_2
    //   68: aload_2
    //   69: athrow
    //   70: astore_2
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_2
    //   74: athrow
    //   75: aconst_null
    //   76: astore_2
    //   77: goto -> 41
    //   80: aconst_null
    //   81: astore_2
    //   82: goto -> 36
    // Exception table:
    //   from	to	target	type
    //   2	10	45	java/lang/Throwable
    //   2	10	67	finally
    //   15	36	45	java/lang/Throwable
    //   15	36	67	finally
    //   46	57	67	finally
    //   68	70	70	finally
  }
  
  public static p a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/p
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/p.a : Lcom/tencent/bugly/proguard/p;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/proguard/p
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/proguard/p
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static p a(Context paramContext, List<com.tencent.bugly.a> paramList) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/p
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/p.a : Lcom/tencent/bugly/proguard/p;
    //   6: ifnonnull -> 23
    //   9: new com/tencent/bugly/proguard/p
    //   12: astore_2
    //   13: aload_2
    //   14: aload_0
    //   15: aload_1
    //   16: invokespecial <init> : (Landroid/content/Context;Ljava/util/List;)V
    //   19: aload_2
    //   20: putstatic com/tencent/bugly/proguard/p.a : Lcom/tencent/bugly/proguard/p;
    //   23: getstatic com/tencent/bugly/proguard/p.a : Lcom/tencent/bugly/proguard/p;
    //   26: astore_0
    //   27: ldc com/tencent/bugly/proguard/p
    //   29: monitorexit
    //   30: aload_0
    //   31: areturn
    //   32: astore_0
    //   33: ldc com/tencent/bugly/proguard/p
    //   35: monitorexit
    //   36: aload_0
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   3	23	32	finally
    //   23	27	32	finally
  }
  
  private static r a(Cursor paramCursor) {
    Cursor cursor = null;
    if (paramCursor == null)
      return (r)cursor; 
    try {
      r r2 = new r();
      this();
      r2.a = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
      r2.b = paramCursor.getInt(paramCursor.getColumnIndex("_tp"));
      r2.c = paramCursor.getString(paramCursor.getColumnIndex("_pc"));
      r2.d = paramCursor.getString(paramCursor.getColumnIndex("_th"));
      r2.e = paramCursor.getLong(paramCursor.getColumnIndex("_tm"));
      r2.g = paramCursor.getBlob(paramCursor.getColumnIndex("_dt"));
      r r1 = r2;
    } catch (Throwable throwable) {
      paramCursor = cursor;
    } 
    return (r)paramCursor;
  }
  
  private Map<String, byte[]> a(int paramInt, o paramo) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial c : (I)Ljava/util/List;
    //   5: astore_3
    //   6: aload_3
    //   7: ifnull -> 121
    //   10: new java/util/HashMap
    //   13: astore #4
    //   15: aload #4
    //   17: invokespecial <init> : ()V
    //   20: aload_3
    //   21: invokeinterface iterator : ()Ljava/util/Iterator;
    //   26: astore_3
    //   27: aload_3
    //   28: invokeinterface hasNext : ()Z
    //   33: ifeq -> 98
    //   36: aload_3
    //   37: invokeinterface next : ()Ljava/lang/Object;
    //   42: checkcast com/tencent/bugly/proguard/r
    //   45: astore #5
    //   47: aload #5
    //   49: getfield g : [B
    //   52: astore #6
    //   54: aload #6
    //   56: ifnull -> 27
    //   59: aload #4
    //   61: aload #5
    //   63: getfield f : Ljava/lang/String;
    //   66: aload #6
    //   68: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   73: pop
    //   74: goto -> 27
    //   77: astore_3
    //   78: aload_3
    //   79: invokestatic a : (Ljava/lang/Throwable;)Z
    //   82: ifne -> 89
    //   85: aload_3
    //   86: invokevirtual printStackTrace : ()V
    //   89: aload #4
    //   91: astore_3
    //   92: aload_2
    //   93: ifnull -> 96
    //   96: aload_3
    //   97: areturn
    //   98: aload #4
    //   100: astore_3
    //   101: aload_2
    //   102: ifnull -> 96
    //   105: aload #4
    //   107: astore_3
    //   108: goto -> 96
    //   111: astore_2
    //   112: aload_2
    //   113: athrow
    //   114: astore_3
    //   115: aconst_null
    //   116: astore #4
    //   118: goto -> 78
    //   121: aconst_null
    //   122: astore #4
    //   124: goto -> 98
    // Exception table:
    //   from	to	target	type
    //   0	6	114	java/lang/Throwable
    //   0	6	111	finally
    //   10	20	114	java/lang/Throwable
    //   10	20	111	finally
    //   20	27	77	java/lang/Throwable
    //   20	27	111	finally
    //   27	54	77	java/lang/Throwable
    //   27	54	111	finally
    //   59	74	77	java/lang/Throwable
    //   59	74	111	finally
    //   78	89	111	finally
  }
  
  private boolean a(int paramInt, String paramString, o paramo) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: iconst_0
    //   4: istore #5
    //   6: iconst_0
    //   7: istore #6
    //   9: aload_0
    //   10: monitorenter
    //   11: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   14: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   17: astore #7
    //   19: aload #7
    //   21: ifnull -> 90
    //   24: aload_2
    //   25: invokestatic a : (Ljava/lang/String;)Z
    //   28: ifeq -> 107
    //   31: new java/lang/StringBuilder
    //   34: astore_2
    //   35: aload_2
    //   36: ldc '_id = '
    //   38: invokespecial <init> : (Ljava/lang/String;)V
    //   41: aload_2
    //   42: iload_1
    //   43: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   46: invokevirtual toString : ()Ljava/lang/String;
    //   49: astore_2
    //   50: aload #7
    //   52: ldc 't_pf'
    //   54: aload_2
    //   55: aconst_null
    //   56: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   59: istore_1
    //   60: ldc '[Database] deleted %s data %d'
    //   62: iconst_2
    //   63: anewarray java/lang/Object
    //   66: dup
    //   67: iconst_0
    //   68: ldc 't_pf'
    //   70: aastore
    //   71: dup
    //   72: iconst_1
    //   73: iload_1
    //   74: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   77: aastore
    //   78: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   81: pop
    //   82: iload_1
    //   83: ifle -> 151
    //   86: iload #4
    //   88: istore #6
    //   90: iload #6
    //   92: istore #4
    //   94: aload_3
    //   95: ifnull -> 102
    //   98: iload #6
    //   100: istore #4
    //   102: aload_0
    //   103: monitorexit
    //   104: iload #4
    //   106: ireturn
    //   107: new java/lang/StringBuilder
    //   110: astore #8
    //   112: aload #8
    //   114: ldc '_id = '
    //   116: invokespecial <init> : (Ljava/lang/String;)V
    //   119: aload #8
    //   121: iload_1
    //   122: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   125: ldc ' and _tp'
    //   127: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: ldc ' = "'
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: aload_2
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: ldc '"'
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: invokevirtual toString : ()Ljava/lang/String;
    //   147: astore_2
    //   148: goto -> 50
    //   151: iconst_0
    //   152: istore #6
    //   154: goto -> 90
    //   157: astore_2
    //   158: aload_2
    //   159: invokestatic a : (Ljava/lang/Throwable;)Z
    //   162: ifne -> 169
    //   165: aload_2
    //   166: invokevirtual printStackTrace : ()V
    //   169: iload #5
    //   171: istore #4
    //   173: aload_3
    //   174: ifnull -> 102
    //   177: iload #5
    //   179: istore #4
    //   181: goto -> 102
    //   184: astore_2
    //   185: aload_0
    //   186: monitorexit
    //   187: aload_2
    //   188: athrow
    //   189: astore_2
    //   190: aload_3
    //   191: ifnull -> 194
    //   194: aload_2
    //   195: athrow
    // Exception table:
    //   from	to	target	type
    //   11	19	157	java/lang/Throwable
    //   11	19	189	finally
    //   24	50	157	java/lang/Throwable
    //   24	50	189	finally
    //   50	82	157	java/lang/Throwable
    //   50	82	189	finally
    //   107	148	157	java/lang/Throwable
    //   107	148	189	finally
    //   158	169	189	finally
    //   194	196	184	finally
  }
  
  private boolean a(int paramInt, String paramString, byte[] paramArrayOfbyte, o paramo) {
    boolean bool = false;
    try {
      r r = new r();
      this();
      r.a = paramInt;
      r.f = paramString;
      r.e = System.currentTimeMillis();
      r.g = paramArrayOfbyte;
      return b(r);
    } catch (Throwable throwable) {
      if (!x.a(throwable))
        throwable.printStackTrace(); 
      boolean bool1 = bool;
      return bool1;
    } finally {
      if (paramo != null);
    } 
  }
  
  private static r b(Cursor paramCursor) {
    Cursor cursor = null;
    if (paramCursor == null)
      return (r)cursor; 
    try {
      r r2 = new r();
      this();
      r2.a = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
      r2.e = paramCursor.getLong(paramCursor.getColumnIndex("_tm"));
      r2.f = paramCursor.getString(paramCursor.getColumnIndex("_tp"));
      r2.g = paramCursor.getBlob(paramCursor.getColumnIndex("_dt"));
      r r1 = r2;
    } catch (Throwable throwable) {
      paramCursor = cursor;
    } 
    return (r)paramCursor;
  }
  
  private boolean b(r paramr) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnonnull -> 14
    //   8: iload_2
    //   9: istore_3
    //   10: aload_0
    //   11: monitorexit
    //   12: iload_3
    //   13: ireturn
    //   14: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   17: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   20: astore #4
    //   22: iload_2
    //   23: istore_3
    //   24: aload #4
    //   26: ifnull -> 10
    //   29: aload_1
    //   30: invokestatic d : (Lcom/tencent/bugly/proguard/r;)Landroid/content/ContentValues;
    //   33: astore #5
    //   35: iload_2
    //   36: istore_3
    //   37: aload #5
    //   39: ifnull -> 10
    //   42: aload #4
    //   44: ldc 't_pf'
    //   46: ldc '_id'
    //   48: aload #5
    //   50: invokevirtual replace : (Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   53: lstore #6
    //   55: iload_2
    //   56: istore_3
    //   57: lload #6
    //   59: lconst_0
    //   60: lcmp
    //   61: iflt -> 10
    //   64: ldc '[Database] insert %s success.'
    //   66: iconst_1
    //   67: anewarray java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: ldc 't_pf'
    //   74: aastore
    //   75: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   78: pop
    //   79: aload_1
    //   80: lload #6
    //   82: putfield a : J
    //   85: iconst_1
    //   86: istore_3
    //   87: goto -> 10
    //   90: astore_1
    //   91: iload_2
    //   92: istore_3
    //   93: aload_1
    //   94: invokestatic a : (Ljava/lang/Throwable;)Z
    //   97: ifne -> 10
    //   100: aload_1
    //   101: invokevirtual printStackTrace : ()V
    //   104: iload_2
    //   105: istore_3
    //   106: goto -> 10
    //   109: astore_1
    //   110: aload_1
    //   111: athrow
    //   112: astore_1
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_1
    //   116: athrow
    // Exception table:
    //   from	to	target	type
    //   14	22	90	java/lang/Throwable
    //   14	22	109	finally
    //   29	35	90	java/lang/Throwable
    //   29	35	109	finally
    //   42	55	90	java/lang/Throwable
    //   42	55	109	finally
    //   64	85	90	java/lang/Throwable
    //   64	85	109	finally
    //   93	104	109	finally
    //   110	112	112	finally
  }
  
  private static ContentValues c(r paramr) {
    r r1 = null;
    if (paramr == null)
      return (ContentValues)r1; 
    try {
      ContentValues contentValues2 = new ContentValues();
      this();
      if (paramr.a > 0L)
        contentValues2.put("_id", Long.valueOf(paramr.a)); 
      contentValues2.put("_tp", Integer.valueOf(paramr.b));
      contentValues2.put("_pc", paramr.c);
      contentValues2.put("_th", paramr.d);
      contentValues2.put("_tm", Long.valueOf(paramr.e));
      if (paramr.g != null)
        contentValues2.put("_dt", paramr.g); 
      ContentValues contentValues1 = contentValues2;
    } catch (Throwable throwable) {
      paramr = r1;
    } 
    return (ContentValues)paramr;
  }
  
  private List<r> c(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   5: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull -> 145
    //   13: new java/lang/StringBuilder
    //   16: astore_3
    //   17: aload_3
    //   18: ldc '_id = '
    //   20: invokespecial <init> : (Ljava/lang/String;)V
    //   23: aload_3
    //   24: iload_1
    //   25: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   28: invokevirtual toString : ()Ljava/lang/String;
    //   31: astore #4
    //   33: aload_2
    //   34: ldc 't_pf'
    //   36: aconst_null
    //   37: aload #4
    //   39: aconst_null
    //   40: aconst_null
    //   41: aconst_null
    //   42: aconst_null
    //   43: invokevirtual query : (Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   46: astore_3
    //   47: aload_3
    //   48: ifnonnull -> 67
    //   51: aload_3
    //   52: ifnull -> 61
    //   55: aload_3
    //   56: invokeinterface close : ()V
    //   61: aconst_null
    //   62: astore_3
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_3
    //   66: areturn
    //   67: new java/lang/StringBuilder
    //   70: astore #5
    //   72: aload #5
    //   74: invokespecial <init> : ()V
    //   77: new java/util/ArrayList
    //   80: astore #6
    //   82: aload #6
    //   84: invokespecial <init> : ()V
    //   87: aload_3
    //   88: invokeinterface moveToNext : ()Z
    //   93: ifeq -> 230
    //   96: aload_3
    //   97: invokestatic b : (Landroid/database/Cursor;)Lcom/tencent/bugly/proguard/r;
    //   100: astore #7
    //   102: aload #7
    //   104: ifnull -> 150
    //   107: aload #6
    //   109: aload #7
    //   111: invokeinterface add : (Ljava/lang/Object;)Z
    //   116: pop
    //   117: goto -> 87
    //   120: astore #6
    //   122: aload #6
    //   124: invokestatic a : (Ljava/lang/Throwable;)Z
    //   127: ifne -> 135
    //   130: aload #6
    //   132: invokevirtual printStackTrace : ()V
    //   135: aload_3
    //   136: ifnull -> 145
    //   139: aload_3
    //   140: invokeinterface close : ()V
    //   145: aconst_null
    //   146: astore_3
    //   147: goto -> 63
    //   150: aload_3
    //   151: aload_3
    //   152: ldc '_tp'
    //   154: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   159: invokeinterface getString : (I)Ljava/lang/String;
    //   164: astore #7
    //   166: aload #5
    //   168: ldc_w ' or _tp'
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: ldc_w ' = '
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: aload #7
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: goto -> 87
    //   189: astore #7
    //   191: ldc_w '[Database] unknown id.'
    //   194: iconst_0
    //   195: anewarray java/lang/Object
    //   198: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   201: pop
    //   202: goto -> 87
    //   205: astore_2
    //   206: aload_3
    //   207: astore #6
    //   209: aload_2
    //   210: astore_3
    //   211: aload #6
    //   213: ifnull -> 223
    //   216: aload #6
    //   218: invokeinterface close : ()V
    //   223: aload_3
    //   224: athrow
    //   225: astore_3
    //   226: aload_0
    //   227: monitorexit
    //   228: aload_3
    //   229: athrow
    //   230: aload #5
    //   232: invokevirtual length : ()I
    //   235: ifle -> 292
    //   238: aload #5
    //   240: ldc_w ' and _id'
    //   243: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   246: ldc_w ' = '
    //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: iload_1
    //   253: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   256: pop
    //   257: ldc_w '[Database] deleted %s illegal data %d.'
    //   260: iconst_2
    //   261: anewarray java/lang/Object
    //   264: dup
    //   265: iconst_0
    //   266: ldc 't_pf'
    //   268: aastore
    //   269: dup
    //   270: iconst_1
    //   271: aload_2
    //   272: ldc 't_pf'
    //   274: aload #4
    //   276: iconst_4
    //   277: invokevirtual substring : (I)Ljava/lang/String;
    //   280: aconst_null
    //   281: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   284: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   287: aastore
    //   288: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   291: pop
    //   292: aload_3
    //   293: ifnull -> 302
    //   296: aload_3
    //   297: invokeinterface close : ()V
    //   302: aload #6
    //   304: astore_3
    //   305: goto -> 63
    //   308: astore_3
    //   309: aconst_null
    //   310: astore #6
    //   312: goto -> 211
    //   315: astore #6
    //   317: aload_3
    //   318: astore_2
    //   319: aload #6
    //   321: astore_3
    //   322: aload_2
    //   323: astore #6
    //   325: goto -> 211
    //   328: astore #6
    //   330: aconst_null
    //   331: astore_3
    //   332: goto -> 122
    // Exception table:
    //   from	to	target	type
    //   2	9	328	java/lang/Throwable
    //   2	9	308	finally
    //   13	47	328	java/lang/Throwable
    //   13	47	308	finally
    //   55	61	225	finally
    //   67	87	120	java/lang/Throwable
    //   67	87	205	finally
    //   87	102	120	java/lang/Throwable
    //   87	102	205	finally
    //   107	117	120	java/lang/Throwable
    //   107	117	205	finally
    //   122	135	315	finally
    //   139	145	225	finally
    //   150	186	189	java/lang/Throwable
    //   150	186	205	finally
    //   191	202	120	java/lang/Throwable
    //   191	202	205	finally
    //   216	223	225	finally
    //   223	225	225	finally
    //   230	292	120	java/lang/Throwable
    //   230	292	205	finally
    //   296	302	225	finally
  }
  
  private static ContentValues d(r paramr) {
    ContentValues contentValues;
    if (paramr == null || z.a(paramr.f))
      return null; 
    try {
      ContentValues contentValues1 = new ContentValues();
      this();
      if (paramr.a > 0L)
        contentValues1.put("_id", Long.valueOf(paramr.a)); 
      contentValues1.put("_tp", paramr.f);
      contentValues1.put("_tm", Long.valueOf(paramr.e));
      contentValues = contentValues1;
      if (paramr.g != null) {
        contentValues1.put("_dt", paramr.g);
        contentValues = contentValues1;
      } 
    } catch (Throwable throwable) {}
    return contentValues;
  }
  
  public final int a(String paramString1, String paramString2, String[] paramArrayOfString, o paramo, boolean paramBoolean) {
    return a(paramString1, paramString2, (String[])null, (o)null);
  }
  
  public final long a(String paramString, ContentValues paramContentValues, o paramo, boolean paramBoolean) {
    return a(paramString, paramContentValues, (o)null);
  }
  
  public final Cursor a(String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, o paramo, boolean paramBoolean) {
    return a(false, paramString1, paramArrayOfString1, paramString2, null, null, null, null, null, null);
  }
  
  public final List<r> a(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   5: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull -> 154
    //   13: iload_1
    //   14: iflt -> 71
    //   17: new java/lang/StringBuilder
    //   20: astore_3
    //   21: aload_3
    //   22: ldc_w '_tp = '
    //   25: invokespecial <init> : (Ljava/lang/String;)V
    //   28: aload_3
    //   29: iload_1
    //   30: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   33: invokevirtual toString : ()Ljava/lang/String;
    //   36: astore_3
    //   37: aload_2
    //   38: ldc_w 't_lr'
    //   41: aconst_null
    //   42: aload_3
    //   43: aconst_null
    //   44: aconst_null
    //   45: aconst_null
    //   46: aconst_null
    //   47: invokevirtual query : (Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   50: astore_3
    //   51: aload_3
    //   52: ifnonnull -> 76
    //   55: aload_3
    //   56: ifnull -> 65
    //   59: aload_3
    //   60: invokeinterface close : ()V
    //   65: aconst_null
    //   66: astore_3
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_3
    //   70: areturn
    //   71: aconst_null
    //   72: astore_3
    //   73: goto -> 37
    //   76: new java/lang/StringBuilder
    //   79: astore #4
    //   81: aload #4
    //   83: invokespecial <init> : ()V
    //   86: new java/util/ArrayList
    //   89: astore #5
    //   91: aload #5
    //   93: invokespecial <init> : ()V
    //   96: aload_3
    //   97: invokeinterface moveToNext : ()Z
    //   102: ifeq -> 239
    //   105: aload_3
    //   106: invokestatic a : (Landroid/database/Cursor;)Lcom/tencent/bugly/proguard/r;
    //   109: astore #6
    //   111: aload #6
    //   113: ifnull -> 159
    //   116: aload #5
    //   118: aload #6
    //   120: invokeinterface add : (Ljava/lang/Object;)Z
    //   125: pop
    //   126: goto -> 96
    //   129: astore #5
    //   131: aload #5
    //   133: invokestatic a : (Ljava/lang/Throwable;)Z
    //   136: ifne -> 144
    //   139: aload #5
    //   141: invokevirtual printStackTrace : ()V
    //   144: aload_3
    //   145: ifnull -> 154
    //   148: aload_3
    //   149: invokeinterface close : ()V
    //   154: aconst_null
    //   155: astore_3
    //   156: goto -> 67
    //   159: aload_3
    //   160: aload_3
    //   161: ldc '_id'
    //   163: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   168: invokeinterface getLong : (I)J
    //   173: lstore #7
    //   175: aload #4
    //   177: ldc_w ' or _id'
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: ldc_w ' = '
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: lload #7
    //   191: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: goto -> 96
    //   198: astore #6
    //   200: ldc_w '[Database] unknown id.'
    //   203: iconst_0
    //   204: anewarray java/lang/Object
    //   207: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   210: pop
    //   211: goto -> 96
    //   214: astore_2
    //   215: aload_3
    //   216: astore #5
    //   218: aload_2
    //   219: astore_3
    //   220: aload #5
    //   222: ifnull -> 232
    //   225: aload #5
    //   227: invokeinterface close : ()V
    //   232: aload_3
    //   233: athrow
    //   234: astore_3
    //   235: aload_0
    //   236: monitorexit
    //   237: aload_3
    //   238: athrow
    //   239: aload #4
    //   241: invokevirtual toString : ()Ljava/lang/String;
    //   244: astore #4
    //   246: aload #4
    //   248: invokevirtual length : ()I
    //   251: ifle -> 291
    //   254: ldc_w '[Database] deleted %s illegal data %d'
    //   257: iconst_2
    //   258: anewarray java/lang/Object
    //   261: dup
    //   262: iconst_0
    //   263: ldc_w 't_lr'
    //   266: aastore
    //   267: dup
    //   268: iconst_1
    //   269: aload_2
    //   270: ldc_w 't_lr'
    //   273: aload #4
    //   275: iconst_4
    //   276: invokevirtual substring : (I)Ljava/lang/String;
    //   279: aconst_null
    //   280: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   283: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   286: aastore
    //   287: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   290: pop
    //   291: aload_3
    //   292: ifnull -> 301
    //   295: aload_3
    //   296: invokeinterface close : ()V
    //   301: aload #5
    //   303: astore_3
    //   304: goto -> 67
    //   307: astore_3
    //   308: aconst_null
    //   309: astore #5
    //   311: goto -> 220
    //   314: astore #5
    //   316: aload_3
    //   317: astore_2
    //   318: aload #5
    //   320: astore_3
    //   321: aload_2
    //   322: astore #5
    //   324: goto -> 220
    //   327: astore #5
    //   329: aconst_null
    //   330: astore_3
    //   331: goto -> 131
    // Exception table:
    //   from	to	target	type
    //   2	9	234	finally
    //   17	37	327	java/lang/Throwable
    //   17	37	307	finally
    //   37	51	327	java/lang/Throwable
    //   37	51	307	finally
    //   59	65	234	finally
    //   76	96	129	java/lang/Throwable
    //   76	96	214	finally
    //   96	111	129	java/lang/Throwable
    //   96	111	214	finally
    //   116	126	129	java/lang/Throwable
    //   116	126	214	finally
    //   131	144	314	finally
    //   148	154	234	finally
    //   159	195	198	java/lang/Throwable
    //   159	195	214	finally
    //   200	211	129	java/lang/Throwable
    //   200	211	214	finally
    //   225	232	234	finally
    //   232	234	234	finally
    //   239	291	129	java/lang/Throwable
    //   239	291	214	finally
    //   295	301	234	finally
  }
  
  public final Map<String, byte[]> a(int paramInt, o paramo, boolean paramBoolean) {
    return a(paramInt, (o)null);
  }
  
  public final void a(List<r> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 17
    //   6: aload_1
    //   7: invokeinterface size : ()I
    //   12: istore_2
    //   13: iload_2
    //   14: ifne -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   23: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 17
    //   31: new java/lang/StringBuilder
    //   34: astore #4
    //   36: aload #4
    //   38: invokespecial <init> : ()V
    //   41: aload_1
    //   42: invokeinterface iterator : ()Ljava/util/Iterator;
    //   47: astore #5
    //   49: aload #5
    //   51: invokeinterface hasNext : ()Z
    //   56: ifeq -> 100
    //   59: aload #5
    //   61: invokeinterface next : ()Ljava/lang/Object;
    //   66: checkcast com/tencent/bugly/proguard/r
    //   69: astore_1
    //   70: aload #4
    //   72: ldc_w ' or _id'
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: ldc_w ' = '
    //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: aload_1
    //   85: getfield a : J
    //   88: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   91: pop
    //   92: goto -> 49
    //   95: astore_1
    //   96: aload_0
    //   97: monitorexit
    //   98: aload_1
    //   99: athrow
    //   100: aload #4
    //   102: invokevirtual toString : ()Ljava/lang/String;
    //   105: astore #5
    //   107: aload #5
    //   109: astore_1
    //   110: aload #5
    //   112: invokevirtual length : ()I
    //   115: ifle -> 125
    //   118: aload #5
    //   120: iconst_4
    //   121: invokevirtual substring : (I)Ljava/lang/String;
    //   124: astore_1
    //   125: aload #4
    //   127: iconst_0
    //   128: invokevirtual setLength : (I)V
    //   131: ldc '[Database] deleted %s data %d'
    //   133: iconst_2
    //   134: anewarray java/lang/Object
    //   137: dup
    //   138: iconst_0
    //   139: ldc_w 't_lr'
    //   142: aastore
    //   143: dup
    //   144: iconst_1
    //   145: aload_3
    //   146: ldc_w 't_lr'
    //   149: aload_1
    //   150: aconst_null
    //   151: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   154: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   157: aastore
    //   158: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   161: pop
    //   162: goto -> 17
    //   165: astore_1
    //   166: aload_1
    //   167: invokestatic a : (Ljava/lang/Throwable;)Z
    //   170: ifne -> 17
    //   173: aload_1
    //   174: invokevirtual printStackTrace : ()V
    //   177: goto -> 17
    //   180: astore_1
    //   181: aload_1
    //   182: athrow
    // Exception table:
    //   from	to	target	type
    //   6	13	95	finally
    //   20	27	95	finally
    //   31	49	95	finally
    //   49	92	95	finally
    //   100	107	95	finally
    //   110	125	95	finally
    //   125	131	95	finally
    //   131	162	165	java/lang/Throwable
    //   131	162	180	finally
    //   166	177	180	finally
    //   181	183	95	finally
  }
  
  public final boolean a(int paramInt, String paramString, o paramo, boolean paramBoolean) {
    return a(555, paramString, (o)null);
  }
  
  public final boolean a(int paramInt, String paramString, byte[] paramArrayOfbyte, o paramo, boolean paramBoolean) {
    if (!paramBoolean) {
      a a = new a(this, 4, null);
      a.a(paramInt, paramString, paramArrayOfbyte);
      w.a().a(a);
      return true;
    } 
    return a(paramInt, paramString, paramArrayOfbyte, (o)null);
  }
  
  public final boolean a(r paramr) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnonnull -> 14
    //   8: iload_2
    //   9: istore_3
    //   10: aload_0
    //   11: monitorexit
    //   12: iload_3
    //   13: ireturn
    //   14: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   17: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   20: astore #4
    //   22: iload_2
    //   23: istore_3
    //   24: aload #4
    //   26: ifnull -> 10
    //   29: aload_1
    //   30: invokestatic c : (Lcom/tencent/bugly/proguard/r;)Landroid/content/ContentValues;
    //   33: astore #5
    //   35: iload_2
    //   36: istore_3
    //   37: aload #5
    //   39: ifnull -> 10
    //   42: aload #4
    //   44: ldc_w 't_lr'
    //   47: ldc '_id'
    //   49: aload #5
    //   51: invokevirtual replace : (Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   54: lstore #6
    //   56: iload_2
    //   57: istore_3
    //   58: lload #6
    //   60: lconst_0
    //   61: lcmp
    //   62: iflt -> 10
    //   65: ldc '[Database] insert %s success.'
    //   67: iconst_1
    //   68: anewarray java/lang/Object
    //   71: dup
    //   72: iconst_0
    //   73: ldc_w 't_lr'
    //   76: aastore
    //   77: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   80: pop
    //   81: aload_1
    //   82: lload #6
    //   84: putfield a : J
    //   87: iconst_1
    //   88: istore_3
    //   89: goto -> 10
    //   92: astore_1
    //   93: iload_2
    //   94: istore_3
    //   95: aload_1
    //   96: invokestatic a : (Ljava/lang/Throwable;)Z
    //   99: ifne -> 10
    //   102: aload_1
    //   103: invokevirtual printStackTrace : ()V
    //   106: iload_2
    //   107: istore_3
    //   108: goto -> 10
    //   111: astore_1
    //   112: aload_1
    //   113: athrow
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   14	22	92	java/lang/Throwable
    //   14	22	111	finally
    //   29	35	92	java/lang/Throwable
    //   29	35	111	finally
    //   42	56	92	java/lang/Throwable
    //   42	56	111	finally
    //   65	87	92	java/lang/Throwable
    //   65	87	111	finally
    //   95	106	111	finally
    //   112	114	114	finally
  }
  
  public final void b(int paramInt) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: getstatic com/tencent/bugly/proguard/p.b : Lcom/tencent/bugly/proguard/q;
    //   7: invokevirtual getWritableDatabase : ()Landroid/database/sqlite/SQLiteDatabase;
    //   10: astore_3
    //   11: aload_3
    //   12: ifnull -> 70
    //   15: iload_1
    //   16: iflt -> 39
    //   19: new java/lang/StringBuilder
    //   22: astore_2
    //   23: aload_2
    //   24: ldc_w '_tp = '
    //   27: invokespecial <init> : (Ljava/lang/String;)V
    //   30: aload_2
    //   31: iload_1
    //   32: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   35: invokevirtual toString : ()Ljava/lang/String;
    //   38: astore_2
    //   39: ldc '[Database] deleted %s data %d'
    //   41: iconst_2
    //   42: anewarray java/lang/Object
    //   45: dup
    //   46: iconst_0
    //   47: ldc_w 't_lr'
    //   50: aastore
    //   51: dup
    //   52: iconst_1
    //   53: aload_3
    //   54: ldc_w 't_lr'
    //   57: aload_2
    //   58: aconst_null
    //   59: invokevirtual delete : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   62: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   65: aastore
    //   66: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   69: pop
    //   70: aload_0
    //   71: monitorexit
    //   72: return
    //   73: astore_2
    //   74: aload_2
    //   75: invokestatic a : (Ljava/lang/Throwable;)Z
    //   78: ifne -> 70
    //   81: aload_2
    //   82: invokevirtual printStackTrace : ()V
    //   85: goto -> 70
    //   88: astore_2
    //   89: aload_2
    //   90: athrow
    //   91: astore_2
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_2
    //   95: athrow
    // Exception table:
    //   from	to	target	type
    //   4	11	91	finally
    //   19	39	73	java/lang/Throwable
    //   19	39	88	finally
    //   39	70	73	java/lang/Throwable
    //   39	70	88	finally
    //   74	85	88	finally
    //   89	91	91	finally
  }
  
  final class a extends Thread {
    private int a;
    
    private o b;
    
    private String c;
    
    private ContentValues d;
    
    private boolean e;
    
    private String[] f;
    
    private String g;
    
    private String[] h;
    
    private String i;
    
    private String j;
    
    private String k;
    
    private String l;
    
    private String m;
    
    private String[] n;
    
    private int o;
    
    private String p;
    
    private byte[] q;
    
    public a(p this$0, int param1Int, o param1o) {
      this.a = param1Int;
      this.b = param1o;
    }
    
    public final void a(int param1Int, String param1String, byte[] param1ArrayOfbyte) {
      this.o = param1Int;
      this.p = param1String;
      this.q = param1ArrayOfbyte;
    }
    
    public final void a(boolean param1Boolean, String param1String1, String[] param1ArrayOfString1, String param1String2, String[] param1ArrayOfString2, String param1String3, String param1String4, String param1String5, String param1String6) {
      this.e = param1Boolean;
      this.c = param1String1;
      this.f = param1ArrayOfString1;
      this.g = param1String2;
      this.h = param1ArrayOfString2;
      this.i = param1String3;
      this.j = param1String4;
      this.k = param1String5;
      this.l = param1String6;
    }
    
    public final void run() {
      switch (this.a) {
        default:
          return;
        case 1:
          p.a(this.r, this.c, this.d, this.b);
        case 2:
          p.a(this.r, this.c, this.m, this.n, this.b);
        case 3:
          p.a(this.r, this.e, this.c, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.b);
        case 4:
          p.a(this.r, this.o, this.p, this.q, this.b);
        case 5:
          p.a(this.r, this.o, this.b);
        case 6:
          break;
      } 
      p.a(this.r, this.o, this.p, this.b);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/p.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */