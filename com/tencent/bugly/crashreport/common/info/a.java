package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Process;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class a {
  private static a ad = null;
  
  public HashMap<String, String> A = new HashMap<String, String>();
  
  public boolean B = true;
  
  public List<String> C = new ArrayList<String>();
  
  public com.tencent.bugly.crashreport.a D = null;
  
  public SharedPreferences E;
  
  private final Context F;
  
  private String G;
  
  private String H;
  
  private String I = "unknown";
  
  private String J = "unknown";
  
  private String K = "";
  
  private String L = null;
  
  private String M = null;
  
  private String N = null;
  
  private String O = null;
  
  private long P = -1L;
  
  private long Q = -1L;
  
  private long R = -1L;
  
  private String S = null;
  
  private String T = null;
  
  private Map<String, PlugInBean> U = null;
  
  private boolean V = true;
  
  private String W = null;
  
  private String X = null;
  
  private Boolean Y = null;
  
  private String Z = null;
  
  public final long a = System.currentTimeMillis();
  
  private String aa = null;
  
  private String ab = null;
  
  private Map<String, PlugInBean> ac = null;
  
  private int ae = -1;
  
  private int af = -1;
  
  private Map<String, String> ag = new HashMap<String, String>();
  
  private Map<String, String> ah = new HashMap<String, String>();
  
  private Map<String, String> ai = new HashMap<String, String>();
  
  private boolean aj;
  
  private String ak = null;
  
  private String al = null;
  
  private String am = null;
  
  private String an = null;
  
  private String ao = null;
  
  private final Object ap = new Object();
  
  private final Object aq = new Object();
  
  private final Object ar = new Object();
  
  private final Object as = new Object();
  
  private final Object at = new Object();
  
  private final Object au = new Object();
  
  private final Object av = new Object();
  
  public final byte b;
  
  public String c;
  
  public final String d;
  
  public boolean e = true;
  
  public final String f;
  
  public final String g;
  
  public final String h;
  
  public long i;
  
  public String j = null;
  
  public String k = null;
  
  public String l = null;
  
  public String m = null;
  
  public String n = null;
  
  public List<String> o = null;
  
  public String p = "unknown";
  
  public long q = 0L;
  
  public long r = 0L;
  
  public long s = 0L;
  
  public long t = 0L;
  
  public boolean u = false;
  
  public String v = null;
  
  public String w = null;
  
  public String x = null;
  
  public boolean y = false;
  
  public boolean z = false;
  
  private a(Context paramContext) {
    this.F = z.a(paramContext);
    this.b = (byte)1;
    PackageInfo packageInfo = AppInfo.b(paramContext);
    if (packageInfo != null)
      try {
        this.j = packageInfo.versionName;
        this.v = this.j;
        this.w = Integer.toString(packageInfo.versionCode);
      } catch (Throwable throwable) {} 
    this.c = AppInfo.a(paramContext);
    this.d = AppInfo.a(Process.myPid());
    this.f = b.l();
    this.g = b.a();
    this.k = AppInfo.c(paramContext);
    this.h = "Android " + b.b() + ",level " + b.c();
    this.g + ";" + this.h;
    Map<String, String> map = AppInfo.d(paramContext);
    if (map != null)
      try {
        this.o = AppInfo.a(map);
        String str2 = map.get("BUGLY_APPID");
        if (str2 != null)
          this.X = str2; 
        str2 = map.get("BUGLY_APP_VERSION");
        if (str2 != null)
          this.j = str2; 
        str2 = map.get("BUGLY_APP_CHANNEL");
        if (str2 != null)
          this.l = str2; 
        str2 = map.get("BUGLY_ENABLE_DEBUG");
        if (str2 != null)
          this.u = str2.equalsIgnoreCase("true"); 
        String str1 = map.get("com.tencent.rdm.uuid");
        if (str1 != null)
          this.x = str1; 
      } catch (Throwable throwable) {} 
    try {
      if (!paramContext.getDatabasePath("bugly_db_").exists()) {
        this.z = true;
        x.c("App is first time to be installed on the device.", new Object[0]);
      } 
    } catch (Throwable throwable) {}
    this.E = z.a("BUGLY_COMMON_VALUES", paramContext);
    x.c("com info create end", new Object[0]);
  }
  
  public static int L() {
    return b.c();
  }
  
  public static a a(Context paramContext) {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/common/info/a
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/common/info/a.ad : Lcom/tencent/bugly/crashreport/common/info/a;
    //   6: ifnonnull -> 22
    //   9: new com/tencent/bugly/crashreport/common/info/a
    //   12: astore_1
    //   13: aload_1
    //   14: aload_0
    //   15: invokespecial <init> : (Landroid/content/Context;)V
    //   18: aload_1
    //   19: putstatic com/tencent/bugly/crashreport/common/info/a.ad : Lcom/tencent/bugly/crashreport/common/info/a;
    //   22: getstatic com/tencent/bugly/crashreport/common/info/a.ad : Lcom/tencent/bugly/crashreport/common/info/a;
    //   25: astore_0
    //   26: ldc com/tencent/bugly/crashreport/common/info/a
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: astore_0
    //   32: ldc com/tencent/bugly/crashreport/common/info/a
    //   34: monitorexit
    //   35: aload_0
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	31	finally
    //   22	26	31	finally
  }
  
  public static a b() {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/common/info/a
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/common/info/a.ad : Lcom/tencent/bugly/crashreport/common/info/a;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/crashreport/common/info/a
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/crashreport/common/info/a
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static String c() {
    return "2.6.6";
  }
  
  public final String A() {
    if (this.ab == null) {
      this.ab = b.d();
      x.a("Hardware serial number: %s", new Object[] { this.ab });
    } 
    return this.ab;
  }
  
  public final Map<String, String> B() {
    synchronized (this.ar) {
      if (this.ag.size() <= 0)
        return null; 
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this((Map)this.ag);
      return (Map)hashMap;
    } 
  }
  
  public final void C() {
    synchronized (this.ar) {
      this.ag.clear();
      return;
    } 
  }
  
  public final int D() {
    synchronized (this.ar) {
      return this.ag.size();
    } 
  }
  
  public final Set<String> E() {
    synchronized (this.ar) {
      return this.ag.keySet();
    } 
  }
  
  public final Map<String, String> F() {
    synchronized (this.av) {
      if (this.ah.size() <= 0)
        return null; 
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this((Map)this.ah);
      return (Map)hashMap;
    } 
  }
  
  public final Map<String, String> G() {
    synchronized (this.as) {
      if (this.ai.size() <= 0)
        return null; 
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this((Map)this.ai);
      return (Map)hashMap;
    } 
  }
  
  public final int H() {
    synchronized (this.at) {
      return this.ae;
    } 
  }
  
  public final int I() {
    return this.af;
  }
  
  public final boolean J() {
    return AppInfo.f(this.F);
  }
  
  public final Map<String, PlugInBean> K() {
    /* monitor enter ThisExpression{ObjectType{com/tencent/bugly/crashreport/common/info/a}} */
    /* monitor exit ThisExpression{ObjectType{com/tencent/bugly/crashreport/common/info/a}} */
    return null;
  }
  
  public final String M() {
    if (this.ak == null)
      this.ak = b.m(); 
    return this.ak;
  }
  
  public final String N() {
    if (this.al == null)
      this.al = b.j(this.F); 
    return this.al;
  }
  
  public final String O() {
    if (this.am == null)
      this.am = b.k(this.F); 
    return this.am;
  }
  
  public final String P() {
    Context context = this.F;
    return b.n();
  }
  
  public final String Q() {
    if (this.an == null)
      this.an = b.l(this.F); 
    return this.an;
  }
  
  public final long R() {
    Context context = this.F;
    return b.o();
  }
  
  public final void a(int paramInt) {
    synchronized (this.at) {
      int i = this.ae;
      if (i != paramInt) {
        this.ae = paramInt;
        x.a("user scene tag %d changed to tag %d", new Object[] { Integer.valueOf(i), Integer.valueOf(this.ae) });
      } 
      return;
    } 
  }
  
  public final void a(String paramString) {
    this.X = paramString;
  }
  
  public final void a(String paramString1, String paramString2) {
    if (paramString1 != null && paramString2 != null)
      synchronized (this.aq) {
        this.A.put(paramString1, paramString2);
        return;
      }  
  }
  
  public final void a(boolean paramBoolean) {
    this.aj = paramBoolean;
    if (this.D != null)
      this.D.setNativeIsAppForeground(paramBoolean); 
  }
  
  public final boolean a() {
    return this.aj;
  }
  
  public final void b(int paramInt) {
    paramInt = this.af;
    if (paramInt != 24096) {
      this.af = 24096;
      x.a("server scene tag %d changed to tag %d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.af) });
    } 
  }
  
  public final void b(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield au : Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_1
    //   8: astore_3
    //   9: aload_1
    //   10: ifnonnull -> 17
    //   13: ldc_w '10000'
    //   16: astore_3
    //   17: new java/lang/StringBuilder
    //   20: astore_1
    //   21: aload_1
    //   22: invokespecial <init> : ()V
    //   25: aload_0
    //   26: aload_1
    //   27: aload_3
    //   28: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: invokevirtual toString : ()Ljava/lang/String;
    //   34: putfield I : Ljava/lang/String;
    //   37: aload_2
    //   38: monitorexit
    //   39: return
    //   40: astore_1
    //   41: aload_2
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   17	39	40	finally
  }
  
  public final void b(String paramString1, String paramString2) {
    if (z.a(paramString1) || z.a(paramString2)) {
      x.d("key&value should not be empty %s %s", new Object[] { paramString1, paramString2 });
      return;
    } 
    synchronized (this.ar) {
      this.ag.put(paramString1, paramString2);
      return;
    } 
  }
  
  public final void c(String paramString) {
    this.H = paramString;
    synchronized (this.av) {
      this.ah.put("E8", paramString);
      return;
    } 
  }
  
  public final void c(String paramString1, String paramString2) {
    if (z.a(paramString1) || z.a(paramString2)) {
      x.d("server key&value should not be empty %s %s", new Object[] { paramString1, paramString2 });
      return;
    } 
    synchronized (this.as) {
      this.ai.put(paramString1, paramString2);
      return;
    } 
  }
  
  public final void d() {
    synchronized (this.ap) {
      this.G = UUID.randomUUID().toString();
      return;
    } 
  }
  
  public final void d(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_2
    //   12: aload_1
    //   13: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: invokevirtual toString : ()Ljava/lang/String;
    //   19: putfield J : Ljava/lang/String;
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
  
  public final String e() {
    if (this.G == null)
      synchronized (this.ap) {
        if (this.G == null)
          this.G = UUID.randomUUID().toString(); 
        return this.G;
      }  
    return this.G;
  }
  
  public final void e(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: aload_2
    //   12: aload_1
    //   13: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: invokevirtual toString : ()Ljava/lang/String;
    //   19: putfield K : Ljava/lang/String;
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
  
  public final String f() {
    String str = null;
    if (z.a(null))
      str = this.X; 
    return str;
  }
  
  public final String f(String paramString) {
    if (z.a(paramString)) {
      x.d("key should not be empty %s", new Object[] { paramString });
      return null;
    } 
    synchronized (this.ar) {
      paramString = this.ag.remove(paramString);
    } 
    return paramString;
  }
  
  public final String g() {
    synchronized (this.au) {
      return this.I;
    } 
  }
  
  public final String g(String paramString) {
    if (z.a(paramString)) {
      x.d("key should not be empty %s", new Object[] { paramString });
      return null;
    } 
    synchronized (this.ar) {
      paramString = this.ag.get(paramString);
    } 
    return paramString;
  }
  
  public final String h() {
    if (this.H != null)
      return this.H; 
    this.H = k() + "|" + m() + "|" + n();
    return this.H;
  }
  
  public final String i() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield J : Ljava/lang/String;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final String j() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield K : Ljava/lang/String;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final String k() {
    if (!this.V)
      return ""; 
    if (this.L == null)
      this.L = b.a(this.F); 
    return this.L;
  }
  
  public final String l() {
    if (!this.V)
      return ""; 
    if (this.M == null || !this.M.contains(":"))
      this.M = b.d(this.F); 
    return this.M;
  }
  
  public final String m() {
    if (!this.V)
      return ""; 
    if (this.N == null)
      this.N = b.b(this.F); 
    return this.N;
  }
  
  public final String n() {
    if (!this.V)
      return ""; 
    if (this.O == null)
      this.O = b.c(this.F); 
    return this.O;
  }
  
  public final long o() {
    if (this.P <= 0L)
      this.P = b.e(); 
    return this.P;
  }
  
  public final long p() {
    if (this.Q <= 0L)
      this.Q = b.g(); 
    return this.Q;
  }
  
  public final long q() {
    if (this.R <= 0L)
      this.R = b.i(); 
    return this.R;
  }
  
  public final String r() {
    if (this.S == null)
      this.S = b.a(true); 
    return this.S;
  }
  
  public final String s() {
    if (this.T == null)
      this.T = b.h(this.F); 
    return this.T;
  }
  
  public final String t() {
    // Byte code:
    //   0: aload_0
    //   1: getfield F : Landroid/content/Context;
    //   4: ldc_w 'BuglySdkInfos'
    //   7: iconst_0
    //   8: invokevirtual getSharedPreferences : (Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   11: invokeinterface getAll : ()Ljava/util/Map;
    //   16: astore_1
    //   17: aload_1
    //   18: invokeinterface isEmpty : ()Z
    //   23: ifne -> 110
    //   26: aload_0
    //   27: getfield aq : Ljava/lang/Object;
    //   30: astore_2
    //   31: aload_2
    //   32: monitorenter
    //   33: aload_1
    //   34: invokeinterface entrySet : ()Ljava/util/Set;
    //   39: invokeinterface iterator : ()Ljava/util/Iterator;
    //   44: astore_1
    //   45: aload_1
    //   46: invokeinterface hasNext : ()Z
    //   51: ifeq -> 215
    //   54: aload_1
    //   55: invokeinterface next : ()Ljava/lang/Object;
    //   60: checkcast java/util/Map$Entry
    //   63: astore_3
    //   64: aload_0
    //   65: getfield A : Ljava/util/HashMap;
    //   68: aload_3
    //   69: invokeinterface getKey : ()Ljava/lang/Object;
    //   74: aload_3
    //   75: invokeinterface getValue : ()Ljava/lang/Object;
    //   80: invokevirtual toString : ()Ljava/lang/String;
    //   83: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   86: pop
    //   87: goto -> 45
    //   90: astore_3
    //   91: aload_3
    //   92: invokestatic a : (Ljava/lang/Throwable;)Z
    //   95: pop
    //   96: goto -> 45
    //   99: astore_1
    //   100: aload_2
    //   101: monitorexit
    //   102: aload_1
    //   103: athrow
    //   104: astore_2
    //   105: aload_2
    //   106: invokestatic a : (Ljava/lang/Throwable;)Z
    //   109: pop
    //   110: aload_0
    //   111: getfield A : Ljava/util/HashMap;
    //   114: invokevirtual isEmpty : ()Z
    //   117: ifne -> 238
    //   120: new java/lang/StringBuilder
    //   123: dup
    //   124: invokespecial <init> : ()V
    //   127: astore_1
    //   128: aload_0
    //   129: getfield A : Ljava/util/HashMap;
    //   132: invokevirtual entrySet : ()Ljava/util/Set;
    //   135: invokeinterface iterator : ()Ljava/util/Iterator;
    //   140: astore_3
    //   141: aload_3
    //   142: invokeinterface hasNext : ()Z
    //   147: ifeq -> 220
    //   150: aload_3
    //   151: invokeinterface next : ()Ljava/lang/Object;
    //   156: checkcast java/util/Map$Entry
    //   159: astore_2
    //   160: aload_1
    //   161: ldc_w '['
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: pop
    //   168: aload_1
    //   169: aload_2
    //   170: invokeinterface getKey : ()Ljava/lang/Object;
    //   175: checkcast java/lang/String
    //   178: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: pop
    //   182: aload_1
    //   183: ldc_w ','
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: pop
    //   190: aload_1
    //   191: aload_2
    //   192: invokeinterface getValue : ()Ljava/lang/Object;
    //   197: checkcast java/lang/String
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_1
    //   205: ldc_w '] '
    //   208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: pop
    //   212: goto -> 141
    //   215: aload_2
    //   216: monitorexit
    //   217: goto -> 110
    //   220: aload_0
    //   221: ldc_w 'SDK_INFO'
    //   224: aload_1
    //   225: invokevirtual toString : ()Ljava/lang/String;
    //   228: invokevirtual c : (Ljava/lang/String;Ljava/lang/String;)V
    //   231: aload_1
    //   232: invokevirtual toString : ()Ljava/lang/String;
    //   235: astore_2
    //   236: aload_2
    //   237: areturn
    //   238: aconst_null
    //   239: astore_2
    //   240: goto -> 236
    // Exception table:
    //   from	to	target	type
    //   0	33	104	java/lang/Throwable
    //   33	45	99	finally
    //   45	64	99	finally
    //   64	87	90	java/lang/Throwable
    //   64	87	99	finally
    //   91	96	99	finally
    //   100	104	104	java/lang/Throwable
    //   215	217	99	finally
  }
  
  public final String u() {
    if (this.ao == null)
      this.ao = AppInfo.e(this.F); 
    return this.ao;
  }
  
  public final Map<String, PlugInBean> v() {
    /* monitor enter ThisExpression{ObjectType{com/tencent/bugly/crashreport/common/info/a}} */
    /* monitor exit ThisExpression{ObjectType{com/tencent/bugly/crashreport/common/info/a}} */
    return null;
  }
  
  public final String w() {
    if (this.W == null)
      this.W = b.k(); 
    return this.W;
  }
  
  public final Boolean x() {
    if (this.Y == null)
      this.Y = Boolean.valueOf(b.i(this.F)); 
    return this.Y;
  }
  
  public final String y() {
    if (this.Z == null) {
      this.Z = b.g(this.F);
      x.a("ROM ID: %s", new Object[] { this.Z });
    } 
    return this.Z;
  }
  
  public final String z() {
    if (this.aa == null) {
      this.aa = b.e(this.F);
      x.a("SIM serial number: %s", new Object[] { this.aa });
    } 
    return this.aa;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/common/info/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */