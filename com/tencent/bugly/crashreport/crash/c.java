package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.a;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.crashreport.crash.anr.b;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class c {
  public static int a = 0;
  
  public static boolean b = false;
  
  public static int c = 2;
  
  public static boolean d = true;
  
  public static int e = 20000;
  
  public static int f = 20000;
  
  public static long g = 604800000L;
  
  public static String h = null;
  
  public static boolean i = false;
  
  public static String j = null;
  
  public static int k = 5000;
  
  public static boolean l = true;
  
  public static String m = null;
  
  public static String n = null;
  
  private static c q;
  
  public final b o;
  
  private final Context p;
  
  private final e r;
  
  private final NativeCrashHandler s;
  
  private a t;
  
  private w u;
  
  private final b v;
  
  private Boolean w;
  
  private c(int paramInt, Context paramContext, w paramw, boolean paramBoolean, BuglyStrategy.a parama, o paramo, String paramString) {
    a = paramInt;
    paramContext = z.a(paramContext);
    this.p = paramContext;
    this.t = a.a();
    this.u = paramw;
    this.o = new b(paramInt, paramContext, u.a(), p.a(), this.t, parama, paramo);
    a a1 = a.a(paramContext);
    this.r = new e(paramContext, this.o, this.t, a1);
    this.s = NativeCrashHandler.getInstance(paramContext, a1, this.o, this.t, paramw, paramBoolean, paramString);
    a1.D = (a)this.s;
    this.v = new b(paramContext, this.t, a1, paramw, this.o);
  }
  
  public static c a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/crash/c
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/crash/c.q : Lcom/tencent/bugly/crashreport/crash/c;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/crashreport/crash/c
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/crashreport/crash/c
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static void a(int paramInt, Context paramContext, boolean paramBoolean, BuglyStrategy.a parama, o paramo, String paramString) {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/crash/c
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/crash/c.q : Lcom/tencent/bugly/crashreport/crash/c;
    //   6: ifnonnull -> 35
    //   9: new com/tencent/bugly/crashreport/crash/c
    //   12: astore #4
    //   14: aload #4
    //   16: sipush #1004
    //   19: aload_1
    //   20: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   23: iload_2
    //   24: aload_3
    //   25: aconst_null
    //   26: aconst_null
    //   27: invokespecial <init> : (ILandroid/content/Context;Lcom/tencent/bugly/proguard/w;ZLcom/tencent/bugly/BuglyStrategy$a;Lcom/tencent/bugly/proguard/o;Ljava/lang/String;)V
    //   30: aload #4
    //   32: putstatic com/tencent/bugly/crashreport/crash/c.q : Lcom/tencent/bugly/crashreport/crash/c;
    //   35: ldc com/tencent/bugly/crashreport/crash/c
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: ldc com/tencent/bugly/crashreport/crash/c
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   3	35	39	finally
  }
  
  public final void a(long paramLong) {
    w.a().a(new Thread(this) {
          public final void run() {
            if (z.a(c.b(this.a), "local_crash_lock", 10000L)) {
              List<CrashDetailBean> list = this.a.o.a();
              if (list != null && list.size() > 0) {
                List<CrashDetailBean> list1;
                int i = list.size();
                if (i > 100L) {
                  ArrayList arrayList = new ArrayList();
                  Collections.sort(list);
                  byte b = 0;
                  while (true) {
                    list1 = arrayList;
                    if (b < 100L) {
                      arrayList.add(list.get(i - 1 - b));
                      b++;
                      continue;
                    } 
                    break;
                  } 
                } else {
                  list1 = list;
                } 
                this.a.o.a(list1, 0L, false, false, false);
              } 
              z.b(c.b(this.a), "local_crash_lock");
            } 
          }
        }0L);
  }
  
  public final void a(StrategyBean paramStrategyBean) {
    this.r.a(paramStrategyBean);
    this.s.onStrategyChanged(paramStrategyBean);
    this.v.a(paramStrategyBean);
    w.a().a(new Thread(this) {
          public final void run() {
            if (z.a(c.b(this.a), "local_crash_lock", 10000L)) {
              List<CrashDetailBean> list = this.a.o.a();
              if (list != null && list.size() > 0) {
                List<CrashDetailBean> list1;
                int i = list.size();
                if (i > 100L) {
                  ArrayList arrayList = new ArrayList();
                  Collections.sort(list);
                  byte b = 0;
                  while (true) {
                    list1 = arrayList;
                    if (b < 100L) {
                      arrayList.add(list.get(i - 1 - b));
                      b++;
                      continue;
                    } 
                    break;
                  } 
                } else {
                  list1 = list;
                } 
                this.a.o.a(list1, 0L, false, false, false);
              } 
              z.b(c.b(this.a), "local_crash_lock");
            } 
          }
        }0L);
  }
  
  public final void a(CrashDetailBean paramCrashDetailBean) {
    this.o.d(paramCrashDetailBean);
  }
  
  public final void a(Thread paramThread, Throwable paramThrowable, boolean paramBoolean1, String paramString, byte[] paramArrayOfbyte, boolean paramBoolean2) {
    this.u.a(new Runnable(this, false, paramThread, paramThrowable, null, null, paramBoolean2) {
          public final void run() {
            try {
              x.c("post a throwable %b", new Object[] { Boolean.valueOf(this.a) });
              c.a(this.g).a(this.b, this.c, false, this.d, this.e);
              if (this.f) {
                x.a("clear user datas", new Object[0]);
                a.a(c.b(this.g)).C();
              } 
            } catch (Throwable throwable) {}
          }
        });
  }
  
  public final boolean b() {
    boolean bool = false;
    Boolean bool1 = this.w;
    if (bool1 != null)
      return bool1.booleanValue(); 
    String str = (a.b()).d;
    List list = p.a().a(1);
    ArrayList<r> arrayList = new ArrayList();
    if (list != null && list.size() > 0) {
      for (r r : list) {
        if (str.equals(r.c)) {
          this.w = Boolean.valueOf(true);
          arrayList.add(r);
        } 
      } 
      if (arrayList.size() > 0)
        p.a().a(arrayList); 
      return true;
    } 
    this.w = Boolean.valueOf(false);
    return bool;
  }
  
  public final void c() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield r : Lcom/tencent/bugly/crashreport/crash/e;
    //   6: invokevirtual a : ()V
    //   9: aload_0
    //   10: getfield s : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   13: iconst_1
    //   14: invokevirtual setUserOpened : (Z)V
    //   17: aload_0
    //   18: getfield v : Lcom/tencent/bugly/crashreport/crash/anr/b;
    //   21: iconst_1
    //   22: invokevirtual a : (Z)V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	28	finally
  }
  
  public final void d() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield r : Lcom/tencent/bugly/crashreport/crash/e;
    //   6: invokevirtual b : ()V
    //   9: aload_0
    //   10: getfield s : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   13: iconst_0
    //   14: invokevirtual setUserOpened : (Z)V
    //   17: aload_0
    //   18: getfield v : Lcom/tencent/bugly/crashreport/crash/anr/b;
    //   21: iconst_0
    //   22: invokevirtual a : (Z)V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	28	finally
  }
  
  public final void e() {
    this.r.a();
  }
  
  public final void f() {
    this.s.setUserOpened(false);
  }
  
  public final void g() {
    this.s.setUserOpened(true);
  }
  
  public final void h() {
    this.v.a(true);
  }
  
  public final void i() {
    this.v.a(false);
  }
  
  public final void j() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield s : Lcom/tencent/bugly/crashreport/crash/jni/NativeCrashHandler;
    //   6: invokevirtual testNativeCrash : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public final void k() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield v : Lcom/tencent/bugly/crashreport/crash/anr/b;
    //   8: astore_2
    //   9: iload_1
    //   10: iconst_1
    //   11: iadd
    //   12: istore_3
    //   13: iload_1
    //   14: bipush #30
    //   16: if_icmpge -> 60
    //   19: ldc_w 'try main sleep for make a test anr! try:%d/30 , kill it if you don't want to wait!'
    //   22: iconst_1
    //   23: anewarray java/lang/Object
    //   26: dup
    //   27: iconst_0
    //   28: iload_3
    //   29: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   32: aastore
    //   33: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   36: pop
    //   37: ldc2_w 5000
    //   40: invokestatic b : (J)V
    //   43: iload_3
    //   44: istore_1
    //   45: goto -> 9
    //   48: astore_2
    //   49: aload_2
    //   50: invokestatic a : (Ljava/lang/Throwable;)Z
    //   53: ifne -> 60
    //   56: aload_2
    //   57: invokevirtual printStackTrace : ()V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: astore_2
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_2
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   4	9	63	finally
    //   19	43	48	java/lang/Throwable
    //   19	43	63	finally
    //   49	60	63	finally
  }
  
  public final boolean l() {
    return this.v.a();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */