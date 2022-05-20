package com.tencent.bugly;

import java.util.Map;

public class BuglyStrategy {
  private String a;
  
  private String b;
  
  private String c;
  
  private long d;
  
  private String e;
  
  private String f;
  
  private boolean g = true;
  
  private boolean h = true;
  
  private boolean i = true;
  
  private Class<?> j = null;
  
  private boolean k = true;
  
  private boolean l = true;
  
  private boolean m = true;
  
  private boolean n = false;
  
  private a o;
  
  public String getAppChannel() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield b : Ljava/lang/String;
    //   6: ifnonnull -> 20
    //   9: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   12: getfield l : Ljava/lang/String;
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: aload_0
    //   21: getfield b : Ljava/lang/String;
    //   24: astore_1
    //   25: goto -> 16
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	28	finally
    //   20	25	28	finally
  }
  
  public String getAppPackageName() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield c : Ljava/lang/String;
    //   6: ifnonnull -> 20
    //   9: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   12: getfield c : Ljava/lang/String;
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: aload_0
    //   21: getfield c : Ljava/lang/String;
    //   24: astore_1
    //   25: goto -> 16
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	28	finally
    //   20	25	28	finally
  }
  
  public long getAppReportDelay() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield d : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public String getAppVersion() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield a : Ljava/lang/String;
    //   6: ifnonnull -> 20
    //   9: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   12: getfield j : Ljava/lang/String;
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: aload_0
    //   21: getfield a : Ljava/lang/String;
    //   24: astore_1
    //   25: goto -> 16
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	28	finally
    //   20	25	28	finally
  }
  
  public a getCrashHandleCallback() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield o : Lcom/tencent/bugly/BuglyStrategy$a;
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
  
  public String getDeviceID() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield f : Ljava/lang/String;
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
  
  public String getLibBuglySOFilePath() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield e : Ljava/lang/String;
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
  
  public Class<?> getUserInfoActivity() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : Ljava/lang/Class;
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
  
  public boolean isBuglyLogUpload() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield k : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isEnableANRCrashMonitor() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield h : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isEnableNativeCrashMonitor() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield g : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isEnableUserInfo() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield i : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isReplaceOldChannel() {
    return this.l;
  }
  
  public boolean isUploadProcess() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield m : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean recordUserInfoOnceADay() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield n : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setAppChannel(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield b : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setAppPackageName(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield c : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setAppReportDelay(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: putfield d : J
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setAppVersion(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield a : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setBuglyLogUpload(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield k : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setCrashHandleCallback(a parama) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield o : Lcom/tencent/bugly/BuglyStrategy$a;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setDeviceID(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield f : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setEnableANRCrashMonitor(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield h : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setEnableNativeCrashMonitor(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield g : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setEnableUserInfo(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield i : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setLibBuglySOFilePath(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield e : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public BuglyStrategy setRecordUserInfoOnceADay(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield n : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void setReplaceOldChannel(boolean paramBoolean) {
    this.l = paramBoolean;
  }
  
  public BuglyStrategy setUploadProcess(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield m : Z
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public BuglyStrategy setUserInfoActivity(Class<?> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield j : Ljava/lang/Class;
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
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
  
  public static class a {
    public static final int CRASHTYPE_ANR = 4;
    
    public static final int CRASHTYPE_BLOCK = 7;
    
    public static final int CRASHTYPE_COCOS2DX_JS = 5;
    
    public static final int CRASHTYPE_COCOS2DX_LUA = 6;
    
    public static final int CRASHTYPE_JAVA_CATCH = 1;
    
    public static final int CRASHTYPE_JAVA_CRASH = 0;
    
    public static final int CRASHTYPE_NATIVE = 2;
    
    public static final int CRASHTYPE_U3D = 3;
    
    public static final int MAX_USERDATA_KEY_LENGTH = 100;
    
    public static final int MAX_USERDATA_VALUE_LENGTH = 30000;
    
    public Map<String, String> onCrashHandleStart(int param1Int, String param1String1, String param1String2, String param1String3) {
      /* monitor enter ThisExpression{InnerObjectType{ObjectType{com/tencent/bugly/BuglyStrategy}.Lcom/tencent/bugly/BuglyStrategy$a;}} */
      /* monitor exit ThisExpression{InnerObjectType{ObjectType{com/tencent/bugly/BuglyStrategy}.Lcom/tencent/bugly/BuglyStrategy$a;}} */
      return null;
    }
    
    public byte[] onCrashHandleStart2GetExtraDatas(int param1Int, String param1String1, String param1String2, String param1String3) {
      /* monitor enter ThisExpression{InnerObjectType{ObjectType{com/tencent/bugly/BuglyStrategy}.Lcom/tencent/bugly/BuglyStrategy$a;}} */
      /* monitor exit ThisExpression{InnerObjectType{ObjectType{com/tencent/bugly/BuglyStrategy}.Lcom/tencent/bugly/BuglyStrategy$a;}} */
      return null;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/BuglyStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */