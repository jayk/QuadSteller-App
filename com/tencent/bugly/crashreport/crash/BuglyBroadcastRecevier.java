package com.tencent.bugly.crashreport.crash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.tencent.bugly.proguard.x;

public class BuglyBroadcastRecevier extends BroadcastReceiver {
  private static BuglyBroadcastRecevier d = null;
  
  private IntentFilter a = new IntentFilter();
  
  private Context b;
  
  private String c;
  
  private boolean e = true;
  
  private boolean a(Context paramContext, Intent paramIntent) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnull -> 28
    //   8: aload_2
    //   9: ifnull -> 28
    //   12: aload_2
    //   13: invokevirtual getAction : ()Ljava/lang/String;
    //   16: ldc 'android.net.conn.CONNECTIVITY_CHANGE'
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: istore #4
    //   23: iload #4
    //   25: ifne -> 36
    //   28: iconst_0
    //   29: istore #4
    //   31: aload_0
    //   32: monitorexit
    //   33: iload #4
    //   35: ireturn
    //   36: aload_0
    //   37: getfield e : Z
    //   40: ifeq -> 59
    //   43: aload_0
    //   44: iconst_0
    //   45: putfield e : Z
    //   48: iload_3
    //   49: istore #4
    //   51: goto -> 31
    //   54: astore_1
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_1
    //   58: athrow
    //   59: aload_0
    //   60: getfield b : Landroid/content/Context;
    //   63: invokestatic f : (Landroid/content/Context;)Ljava/lang/String;
    //   66: astore_2
    //   67: new java/lang/StringBuilder
    //   70: astore #5
    //   72: aload #5
    //   74: ldc 'is Connect BC '
    //   76: invokespecial <init> : (Ljava/lang/String;)V
    //   79: aload #5
    //   81: aload_2
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: invokevirtual toString : ()Ljava/lang/String;
    //   88: iconst_0
    //   89: anewarray java/lang/Object
    //   92: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   95: pop
    //   96: new java/lang/StringBuilder
    //   99: astore #5
    //   101: aload #5
    //   103: invokespecial <init> : ()V
    //   106: aload #5
    //   108: aload_0
    //   109: getfield c : Ljava/lang/String;
    //   112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: invokevirtual toString : ()Ljava/lang/String;
    //   118: astore #5
    //   120: new java/lang/StringBuilder
    //   123: astore #6
    //   125: aload #6
    //   127: invokespecial <init> : ()V
    //   130: ldc 'network %s changed to %s'
    //   132: iconst_2
    //   133: anewarray java/lang/Object
    //   136: dup
    //   137: iconst_0
    //   138: aload #5
    //   140: aastore
    //   141: dup
    //   142: iconst_1
    //   143: aload #6
    //   145: aload_2
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: invokevirtual toString : ()Ljava/lang/String;
    //   152: aastore
    //   153: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   156: pop
    //   157: aload_2
    //   158: ifnonnull -> 172
    //   161: aload_0
    //   162: aconst_null
    //   163: putfield c : Ljava/lang/String;
    //   166: iload_3
    //   167: istore #4
    //   169: goto -> 31
    //   172: aload_0
    //   173: getfield c : Ljava/lang/String;
    //   176: astore #6
    //   178: aload_0
    //   179: aload_2
    //   180: putfield c : Ljava/lang/String;
    //   183: invokestatic currentTimeMillis : ()J
    //   186: lstore #7
    //   188: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   191: astore #9
    //   193: invokestatic a : ()Lcom/tencent/bugly/proguard/u;
    //   196: astore #5
    //   198: aload_1
    //   199: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   202: astore_1
    //   203: aload #9
    //   205: ifnull -> 217
    //   208: aload #5
    //   210: ifnull -> 217
    //   213: aload_1
    //   214: ifnonnull -> 233
    //   217: ldc 'not inited BC not work'
    //   219: iconst_0
    //   220: anewarray java/lang/Object
    //   223: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   226: pop
    //   227: iload_3
    //   228: istore #4
    //   230: goto -> 31
    //   233: iload_3
    //   234: istore #4
    //   236: aload_2
    //   237: aload #6
    //   239: invokevirtual equals : (Ljava/lang/Object;)Z
    //   242: ifne -> 31
    //   245: lload #7
    //   247: aload #5
    //   249: getstatic com/tencent/bugly/crashreport/crash/c.a : I
    //   252: invokevirtual a : (I)J
    //   255: lsub
    //   256: ldc2_w 30000
    //   259: lcmp
    //   260: ifle -> 286
    //   263: ldc 'try to upload crash on network changed.'
    //   265: iconst_0
    //   266: anewarray java/lang/Object
    //   269: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   272: pop
    //   273: invokestatic a : ()Lcom/tencent/bugly/crashreport/crash/c;
    //   276: astore_1
    //   277: aload_1
    //   278: ifnull -> 286
    //   281: aload_1
    //   282: lconst_0
    //   283: invokevirtual a : (J)V
    //   286: iload_3
    //   287: istore #4
    //   289: lload #7
    //   291: aload #5
    //   293: sipush #1001
    //   296: invokevirtual a : (I)J
    //   299: lsub
    //   300: ldc2_w 30000
    //   303: lcmp
    //   304: ifle -> 31
    //   307: ldc 'try to upload userinfo on network changed.'
    //   309: iconst_0
    //   310: anewarray java/lang/Object
    //   313: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   316: pop
    //   317: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   320: invokevirtual b : ()V
    //   323: iload_3
    //   324: istore #4
    //   326: goto -> 31
    // Exception table:
    //   from	to	target	type
    //   12	23	54	finally
    //   36	48	54	finally
    //   59	157	54	finally
    //   161	166	54	finally
    //   172	203	54	finally
    //   217	227	54	finally
    //   236	277	54	finally
    //   281	286	54	finally
    //   289	323	54	finally
  }
  
  public static BuglyBroadcastRecevier getInstance() {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier.d : Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;
    //   6: ifnonnull -> 21
    //   9: new com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier.d : Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;
    //   21: getstatic com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier.d : Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;
    //   24: astore_0
    //   25: ldc com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	25	30	finally
  }
  
  public void addFilter(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield a : Landroid/content/IntentFilter;
    //   6: aload_1
    //   7: invokevirtual hasAction : (Ljava/lang/String;)Z
    //   10: ifne -> 21
    //   13: aload_0
    //   14: getfield a : Landroid/content/IntentFilter;
    //   17: aload_1
    //   18: invokevirtual addAction : (Ljava/lang/String;)V
    //   21: ldc 'add action %s'
    //   23: iconst_1
    //   24: anewarray java/lang/Object
    //   27: dup
    //   28: iconst_0
    //   29: aload_1
    //   30: aastore
    //   31: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   34: pop
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	38	finally
    //   21	35	38	finally
  }
  
  public final void onReceive(Context paramContext, Intent paramIntent) {
    try {
      a(paramContext, paramIntent);
    } catch (Throwable throwable) {}
  }
  
  public void register(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield b : Landroid/content/Context;
    //   7: new com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier$1
    //   10: astore_1
    //   11: aload_1
    //   12: aload_0
    //   13: aload_0
    //   14: invokespecial <init> : (Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;)V
    //   17: aload_1
    //   18: invokestatic a : (Ljava/lang/Runnable;)Z
    //   21: pop
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  public void unregister(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getClass : ()Ljava/lang/Class;
    //   6: ldc 'Unregister broadcast receiver of Bugly.'
    //   8: iconst_0
    //   9: anewarray java/lang/Object
    //   12: invokestatic a : (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Z
    //   15: pop
    //   16: aload_1
    //   17: aload_0
    //   18: invokevirtual unregisterReceiver : (Landroid/content/BroadcastReceiver;)V
    //   21: aload_0
    //   22: aload_1
    //   23: putfield b : Landroid/content/Context;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_1
    //   31: invokestatic a : (Ljava/lang/Throwable;)Z
    //   34: ifne -> 26
    //   37: aload_1
    //   38: invokevirtual printStackTrace : ()V
    //   41: goto -> 26
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	29	java/lang/Throwable
    //   2	26	44	finally
    //   30	41	44	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */