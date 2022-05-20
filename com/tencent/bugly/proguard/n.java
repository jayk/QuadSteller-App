package com.tencent.bugly.proguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.tencent.bugly.crashreport.common.info.a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class n {
  public static final long a;
  
  private static n b = null;
  
  private Context c;
  
  private String d;
  
  private Map<Integer, Map<String, m>> e;
  
  private SharedPreferences f;
  
  static {
    a = System.currentTimeMillis();
  }
  
  private n(Context paramContext) {
    this.c = paramContext;
    this.e = new HashMap<Integer, Map<String, m>>();
    this.d = (a.b()).d;
    this.f = paramContext.getSharedPreferences("crashrecord", 0);
  }
  
  public static n a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/n
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/n.b : Lcom/tencent/bugly/proguard/n;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/proguard/n
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/proguard/n
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static n a(Context paramContext) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/n
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/n.b : Lcom/tencent/bugly/proguard/n;
    //   6: ifnonnull -> 22
    //   9: new com/tencent/bugly/proguard/n
    //   12: astore_1
    //   13: aload_1
    //   14: aload_0
    //   15: invokespecial <init> : (Landroid/content/Context;)V
    //   18: aload_1
    //   19: putstatic com/tencent/bugly/proguard/n.b : Lcom/tencent/bugly/proguard/n;
    //   22: getstatic com/tencent/bugly/proguard/n.b : Lcom/tencent/bugly/proguard/n;
    //   25: astore_0
    //   26: ldc com/tencent/bugly/proguard/n
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: astore_0
    //   32: ldc com/tencent/bugly/proguard/n
    //   34: monitorexit
    //   35: aload_0
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	31	finally
    //   22	26	31	finally
  }
  
  private <T extends List<?>> void a(int paramInt, T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnonnull -> 9
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: new java/io/File
    //   12: astore_3
    //   13: aload_0
    //   14: getfield c : Landroid/content/Context;
    //   17: ldc 'crashrecord'
    //   19: iconst_0
    //   20: invokevirtual getDir : (Ljava/lang/String;I)Ljava/io/File;
    //   23: astore #4
    //   25: new java/lang/StringBuilder
    //   28: astore #5
    //   30: aload #5
    //   32: invokespecial <init> : ()V
    //   35: aload_3
    //   36: aload #4
    //   38: aload #5
    //   40: iload_1
    //   41: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   44: invokevirtual toString : ()Ljava/lang/String;
    //   47: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   50: new java/io/ObjectOutputStream
    //   53: astore #4
    //   55: new java/io/FileOutputStream
    //   58: astore #5
    //   60: aload #5
    //   62: aload_3
    //   63: invokespecial <init> : (Ljava/io/File;)V
    //   66: aload #4
    //   68: aload #5
    //   70: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   73: aload #4
    //   75: astore_3
    //   76: aload #4
    //   78: aload_2
    //   79: invokevirtual writeObject : (Ljava/lang/Object;)V
    //   82: aload #4
    //   84: invokevirtual close : ()V
    //   87: goto -> 6
    //   90: astore_2
    //   91: ldc 'writeCrashRecord error'
    //   93: iconst_0
    //   94: anewarray java/lang/Object
    //   97: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   100: pop
    //   101: goto -> 6
    //   104: astore_2
    //   105: aload_0
    //   106: monitorexit
    //   107: aload_2
    //   108: athrow
    //   109: astore #5
    //   111: aconst_null
    //   112: astore_2
    //   113: aload_2
    //   114: astore_3
    //   115: aload #5
    //   117: invokevirtual printStackTrace : ()V
    //   120: aload_2
    //   121: astore_3
    //   122: ldc 'open record file error'
    //   124: iconst_0
    //   125: anewarray java/lang/Object
    //   128: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   131: pop
    //   132: aload_2
    //   133: ifnull -> 6
    //   136: aload_2
    //   137: invokevirtual close : ()V
    //   140: goto -> 6
    //   143: astore_2
    //   144: aconst_null
    //   145: astore_3
    //   146: aload_3
    //   147: ifnull -> 154
    //   150: aload_3
    //   151: invokevirtual close : ()V
    //   154: aload_2
    //   155: athrow
    //   156: astore_2
    //   157: goto -> 146
    //   160: astore #5
    //   162: aload #4
    //   164: astore_2
    //   165: goto -> 113
    // Exception table:
    //   from	to	target	type
    //   9	50	90	java/lang/Exception
    //   9	50	104	finally
    //   50	73	109	java/io/IOException
    //   50	73	143	finally
    //   76	82	160	java/io/IOException
    //   76	82	156	finally
    //   82	87	90	java/lang/Exception
    //   82	87	104	finally
    //   91	101	104	finally
    //   115	120	156	finally
    //   122	132	156	finally
    //   136	140	90	java/lang/Exception
    //   136	140	104	finally
    //   150	154	90	java/lang/Exception
    //   150	154	104	finally
    //   154	156	90	java/lang/Exception
    //   154	156	104	finally
  }
  
  private boolean b(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial c : (I)Ljava/util/List;
    //   7: astore_2
    //   8: aload_2
    //   9: ifnonnull -> 18
    //   12: iconst_0
    //   13: istore_3
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_3
    //   17: ireturn
    //   18: invokestatic currentTimeMillis : ()J
    //   21: lstore #4
    //   23: new java/util/ArrayList
    //   26: astore #6
    //   28: aload #6
    //   30: invokespecial <init> : ()V
    //   33: new java/util/ArrayList
    //   36: astore #7
    //   38: aload #7
    //   40: invokespecial <init> : ()V
    //   43: aload_2
    //   44: invokeinterface iterator : ()Ljava/util/Iterator;
    //   49: astore #8
    //   51: aload #8
    //   53: invokeinterface hasNext : ()Z
    //   58: ifeq -> 159
    //   61: aload #8
    //   63: invokeinterface next : ()Ljava/lang/Object;
    //   68: checkcast com/tencent/bugly/proguard/m
    //   71: astore #9
    //   73: aload #9
    //   75: getfield b : Ljava/lang/String;
    //   78: ifnull -> 114
    //   81: aload #9
    //   83: getfield b : Ljava/lang/String;
    //   86: aload_0
    //   87: getfield d : Ljava/lang/String;
    //   90: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   93: ifeq -> 114
    //   96: aload #9
    //   98: getfield d : I
    //   101: ifle -> 114
    //   104: aload #6
    //   106: aload #9
    //   108: invokeinterface add : (Ljava/lang/Object;)Z
    //   113: pop
    //   114: aload #9
    //   116: getfield c : J
    //   119: ldc2_w 86400000
    //   122: ladd
    //   123: lload #4
    //   125: lcmp
    //   126: ifge -> 51
    //   129: aload #7
    //   131: aload #9
    //   133: invokeinterface add : (Ljava/lang/Object;)Z
    //   138: pop
    //   139: goto -> 51
    //   142: astore #8
    //   144: ldc 'isFrequentCrash failed'
    //   146: iconst_0
    //   147: anewarray java/lang/Object
    //   150: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   153: pop
    //   154: iconst_0
    //   155: istore_3
    //   156: goto -> 14
    //   159: aload #6
    //   161: invokestatic sort : (Ljava/util/List;)V
    //   164: aload #6
    //   166: invokeinterface size : ()I
    //   171: iconst_2
    //   172: if_icmplt -> 239
    //   175: aload #6
    //   177: invokeinterface size : ()I
    //   182: ifle -> 234
    //   185: aload #6
    //   187: aload #6
    //   189: invokeinterface size : ()I
    //   194: iconst_1
    //   195: isub
    //   196: invokeinterface get : (I)Ljava/lang/Object;
    //   201: checkcast com/tencent/bugly/proguard/m
    //   204: getfield c : J
    //   207: ldc2_w 86400000
    //   210: ladd
    //   211: lload #4
    //   213: lcmp
    //   214: ifge -> 234
    //   217: aload_2
    //   218: invokeinterface clear : ()V
    //   223: aload_0
    //   224: iload_1
    //   225: aload_2
    //   226: invokespecial a : (ILjava/util/List;)V
    //   229: iconst_0
    //   230: istore_3
    //   231: goto -> 14
    //   234: iconst_1
    //   235: istore_3
    //   236: goto -> 14
    //   239: aload_2
    //   240: aload #7
    //   242: invokeinterface removeAll : (Ljava/util/Collection;)Z
    //   247: pop
    //   248: aload_0
    //   249: iload_1
    //   250: aload_2
    //   251: invokespecial a : (ILjava/util/List;)V
    //   254: iconst_0
    //   255: istore_3
    //   256: goto -> 14
    //   259: astore #8
    //   261: aload_0
    //   262: monitorexit
    //   263: aload #8
    //   265: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	142	java/lang/Exception
    //   2	8	259	finally
    //   18	51	142	java/lang/Exception
    //   18	51	259	finally
    //   51	114	142	java/lang/Exception
    //   51	114	259	finally
    //   114	139	142	java/lang/Exception
    //   114	139	259	finally
    //   144	154	259	finally
    //   159	229	142	java/lang/Exception
    //   159	229	259	finally
    //   239	254	142	java/lang/Exception
    //   239	254	259	finally
  }
  
  private <T extends List<?>> T c(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/io/File
    //   5: astore_2
    //   6: aload_0
    //   7: getfield c : Landroid/content/Context;
    //   10: ldc 'crashrecord'
    //   12: iconst_0
    //   13: invokevirtual getDir : (Ljava/lang/String;I)Ljava/io/File;
    //   16: astore_3
    //   17: new java/lang/StringBuilder
    //   20: astore #4
    //   22: aload #4
    //   24: invokespecial <init> : ()V
    //   27: aload_2
    //   28: aload_3
    //   29: aload #4
    //   31: iload_1
    //   32: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   35: invokevirtual toString : ()Ljava/lang/String;
    //   38: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   41: aload_2
    //   42: invokevirtual exists : ()Z
    //   45: istore #5
    //   47: iload #5
    //   49: ifne -> 58
    //   52: aconst_null
    //   53: astore_2
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: areturn
    //   58: new java/io/ObjectInputStream
    //   61: astore_3
    //   62: new java/io/FileInputStream
    //   65: astore #4
    //   67: aload #4
    //   69: aload_2
    //   70: invokespecial <init> : (Ljava/io/File;)V
    //   73: aload_3
    //   74: aload #4
    //   76: invokespecial <init> : (Ljava/io/InputStream;)V
    //   79: aload_3
    //   80: astore_2
    //   81: aload_3
    //   82: invokevirtual readObject : ()Ljava/lang/Object;
    //   85: checkcast java/util/List
    //   88: astore #4
    //   90: aload_3
    //   91: invokevirtual close : ()V
    //   94: aload #4
    //   96: astore_2
    //   97: goto -> 54
    //   100: astore_2
    //   101: ldc 'readCrashRecord error'
    //   103: iconst_0
    //   104: anewarray java/lang/Object
    //   107: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   110: pop
    //   111: aconst_null
    //   112: astore_2
    //   113: goto -> 54
    //   116: astore_2
    //   117: aconst_null
    //   118: astore_2
    //   119: ldc 'open record file error'
    //   121: iconst_0
    //   122: anewarray java/lang/Object
    //   125: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   128: pop
    //   129: aload_2
    //   130: ifnull -> 111
    //   133: aload_2
    //   134: invokevirtual close : ()V
    //   137: goto -> 111
    //   140: astore_2
    //   141: aload_0
    //   142: monitorexit
    //   143: aload_2
    //   144: athrow
    //   145: astore_2
    //   146: aconst_null
    //   147: astore_3
    //   148: aload_3
    //   149: astore_2
    //   150: ldc 'get object error'
    //   152: iconst_0
    //   153: anewarray java/lang/Object
    //   156: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   159: pop
    //   160: aload_3
    //   161: ifnull -> 111
    //   164: aload_3
    //   165: invokevirtual close : ()V
    //   168: goto -> 111
    //   171: astore_3
    //   172: aconst_null
    //   173: astore_2
    //   174: aload_2
    //   175: ifnull -> 182
    //   178: aload_2
    //   179: invokevirtual close : ()V
    //   182: aload_3
    //   183: athrow
    //   184: astore_3
    //   185: goto -> 174
    //   188: astore_3
    //   189: goto -> 174
    //   192: astore_2
    //   193: goto -> 148
    //   196: astore_2
    //   197: aload_3
    //   198: astore_2
    //   199: goto -> 119
    // Exception table:
    //   from	to	target	type
    //   2	47	100	java/lang/Exception
    //   2	47	140	finally
    //   58	79	116	java/io/IOException
    //   58	79	145	java/lang/ClassNotFoundException
    //   58	79	171	finally
    //   81	90	196	java/io/IOException
    //   81	90	192	java/lang/ClassNotFoundException
    //   81	90	184	finally
    //   90	94	100	java/lang/Exception
    //   90	94	140	finally
    //   101	111	140	finally
    //   119	129	188	finally
    //   133	137	100	java/lang/Exception
    //   133	137	140	finally
    //   150	160	184	finally
    //   164	168	100	java/lang/Exception
    //   164	168	140	finally
    //   178	182	100	java/lang/Exception
    //   178	182	140	finally
    //   182	184	100	java/lang/Exception
    //   182	184	140	finally
  }
  
  public final void a(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   5: astore_3
    //   6: new com/tencent/bugly/proguard/n$1
    //   9: astore #4
    //   11: aload #4
    //   13: aload_0
    //   14: sipush #1004
    //   17: iload_2
    //   18: invokespecial <init> : (Lcom/tencent/bugly/proguard/n;II)V
    //   21: aload_3
    //   22: aload #4
    //   24: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   27: pop
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: astore_3
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_3
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	31	finally
  }
  
  public final boolean a(int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: iload_2
    //   5: istore_3
    //   6: aload_0
    //   7: getfield f : Landroid/content/SharedPreferences;
    //   10: astore #4
    //   12: iload_2
    //   13: istore_3
    //   14: new java/lang/StringBuilder
    //   17: astore #5
    //   19: iload_2
    //   20: istore_3
    //   21: aload #5
    //   23: invokespecial <init> : ()V
    //   26: iload_2
    //   27: istore_3
    //   28: aload #4
    //   30: aload #5
    //   32: iload_1
    //   33: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   36: ldc '_'
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: aload_0
    //   42: getfield d : Ljava/lang/String;
    //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: invokevirtual toString : ()Ljava/lang/String;
    //   51: iconst_1
    //   52: invokeinterface getBoolean : (Ljava/lang/String;Z)Z
    //   57: istore_2
    //   58: iload_2
    //   59: istore_3
    //   60: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   63: astore #4
    //   65: iload_2
    //   66: istore_3
    //   67: new com/tencent/bugly/proguard/n$2
    //   70: astore #5
    //   72: iload_2
    //   73: istore_3
    //   74: aload #5
    //   76: aload_0
    //   77: iload_1
    //   78: invokespecial <init> : (Lcom/tencent/bugly/proguard/n;I)V
    //   81: iload_2
    //   82: istore_3
    //   83: aload #4
    //   85: aload #5
    //   87: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   90: pop
    //   91: iload_2
    //   92: istore_3
    //   93: aload_0
    //   94: monitorexit
    //   95: iload_3
    //   96: ireturn
    //   97: astore #5
    //   99: ldc 'canInit error'
    //   101: iconst_0
    //   102: anewarray java/lang/Object
    //   105: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   108: pop
    //   109: goto -> 93
    //   112: astore #5
    //   114: aload_0
    //   115: monitorexit
    //   116: aload #5
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   6	12	97	java/lang/Exception
    //   6	12	112	finally
    //   14	19	97	java/lang/Exception
    //   14	19	112	finally
    //   21	26	97	java/lang/Exception
    //   21	26	112	finally
    //   28	58	97	java/lang/Exception
    //   28	58	112	finally
    //   60	65	97	java/lang/Exception
    //   60	65	112	finally
    //   67	72	97	java/lang/Exception
    //   67	72	112	finally
    //   74	81	97	java/lang/Exception
    //   74	81	112	finally
    //   83	91	97	java/lang/Exception
    //   83	91	112	finally
    //   99	109	112	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/n.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */