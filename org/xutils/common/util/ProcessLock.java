package org.xutils.common.util;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import org.xutils.x;

public final class ProcessLock implements Closeable {
  private static final DecimalFormat FORMAT;
  
  private static final String LOCK_FILE_DIR = "process_lock";
  
  private static final DoubleKeyValueMap<String, Integer, ProcessLock> LOCK_MAP = new DoubleKeyValueMap<String, Integer, ProcessLock>();
  
  private final File mFile;
  
  private final FileLock mFileLock;
  
  private final String mLockName;
  
  private final Closeable mStream;
  
  private final boolean mWriteMode;
  
  static {
    IOUtil.deleteFileOrDir(x.app().getDir("process_lock", 0));
    FORMAT = new DecimalFormat("0.##################");
  }
  
  private ProcessLock(String paramString, File paramFile, FileLock paramFileLock, Closeable paramCloseable, boolean paramBoolean) {
    this.mLockName = paramString;
    this.mFileLock = paramFileLock;
    this.mFile = paramFile;
    this.mStream = paramCloseable;
    this.mWriteMode = paramBoolean;
  }
  
  private static String customHash(String paramString) {
    if (TextUtils.isEmpty(paramString))
      return "0"; 
    double d = 0.0D;
    byte[] arrayOfByte = paramString.getBytes();
    for (byte b = 0; b < paramString.length(); b++)
      d = (255.0D * d + arrayOfByte[b]) * 0.005D; 
    return FORMAT.format(d);
  }
  
  private static boolean isValid(FileLock paramFileLock) {
    return (paramFileLock != null && paramFileLock.isValid());
  }
  
  private static void release(String paramString, FileLock paramFileLock, File paramFile, Closeable paramCloseable) {
    // Byte code:
    //   0: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   3: astore #4
    //   5: aload #4
    //   7: monitorenter
    //   8: aload_1
    //   9: ifnull -> 71
    //   12: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual hashCode : ()I
    //   20: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   23: invokevirtual remove : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   26: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   29: aload_0
    //   30: invokevirtual get : (Ljava/lang/Object;)Ljava/util/concurrent/ConcurrentHashMap;
    //   33: astore_0
    //   34: aload_0
    //   35: ifnull -> 45
    //   38: aload_0
    //   39: invokevirtual isEmpty : ()Z
    //   42: ifeq -> 50
    //   45: aload_2
    //   46: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   49: pop
    //   50: aload_1
    //   51: invokevirtual channel : ()Ljava/nio/channels/FileChannel;
    //   54: invokevirtual isOpen : ()Z
    //   57: ifeq -> 64
    //   60: aload_1
    //   61: invokevirtual release : ()V
    //   64: aload_1
    //   65: invokevirtual channel : ()Ljava/nio/channels/FileChannel;
    //   68: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   71: aload_3
    //   72: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   75: aload #4
    //   77: monitorexit
    //   78: return
    //   79: astore_0
    //   80: aload_0
    //   81: invokevirtual getMessage : ()Ljava/lang/String;
    //   84: aload_0
    //   85: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   88: aload_1
    //   89: invokevirtual channel : ()Ljava/nio/channels/FileChannel;
    //   92: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   95: goto -> 71
    //   98: astore_0
    //   99: aload #4
    //   101: monitorexit
    //   102: aload_0
    //   103: athrow
    //   104: astore_0
    //   105: aload_1
    //   106: invokevirtual channel : ()Ljava/nio/channels/FileChannel;
    //   109: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   112: aload_0
    //   113: athrow
    // Exception table:
    //   from	to	target	type
    //   12	34	79	java/lang/Throwable
    //   12	34	104	finally
    //   38	45	79	java/lang/Throwable
    //   38	45	104	finally
    //   45	50	79	java/lang/Throwable
    //   45	50	104	finally
    //   50	64	79	java/lang/Throwable
    //   50	64	104	finally
    //   64	71	98	finally
    //   71	78	98	finally
    //   80	88	104	finally
    //   88	95	98	finally
    //   99	102	98	finally
    //   105	114	98	finally
  }
  
  public static ProcessLock tryLock(String paramString, boolean paramBoolean) {
    return tryLockInternal(paramString, customHash(paramString), paramBoolean);
  }
  
  public static ProcessLock tryLock(String paramString, boolean paramBoolean, long paramLong) throws InterruptedException {
    ProcessLock processLock = null;
    long l = System.currentTimeMillis();
    String str = customHash(paramString);
    while (true) {
      if (System.currentTimeMillis() < l + paramLong) {
        processLock = tryLockInternal(paramString, str, paramBoolean);
        if (processLock == null) {
          try {
            Thread.sleep(1L);
          } catch (InterruptedException interruptedException) {
            throw interruptedException;
          } catch (Throwable throwable) {}
          continue;
        } 
      } 
      return processLock;
    } 
  }
  
  private static ProcessLock tryLockInternal(String paramString1, String paramString2, boolean paramBoolean) {
    // Byte code:
    //   0: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   3: astore_3
    //   4: aload_3
    //   5: monitorenter
    //   6: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   9: aload_0
    //   10: invokevirtual get : (Ljava/lang/Object;)Ljava/util/concurrent/ConcurrentHashMap;
    //   13: astore #4
    //   15: aload #4
    //   17: ifnull -> 133
    //   20: aload #4
    //   22: invokevirtual isEmpty : ()Z
    //   25: ifne -> 133
    //   28: aload #4
    //   30: invokevirtual entrySet : ()Ljava/util/Set;
    //   33: invokeinterface iterator : ()Ljava/util/Iterator;
    //   38: astore #4
    //   40: aload #4
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 133
    //   50: aload #4
    //   52: invokeinterface next : ()Ljava/lang/Object;
    //   57: checkcast java/util/Map$Entry
    //   60: invokeinterface getValue : ()Ljava/lang/Object;
    //   65: checkcast org/xutils/common/util/ProcessLock
    //   68: astore #5
    //   70: aload #5
    //   72: ifnull -> 123
    //   75: aload #5
    //   77: invokevirtual isValid : ()Z
    //   80: ifne -> 98
    //   83: aload #4
    //   85: invokeinterface remove : ()V
    //   90: goto -> 40
    //   93: astore_0
    //   94: aload_3
    //   95: monitorexit
    //   96: aload_0
    //   97: athrow
    //   98: iload_2
    //   99: ifeq -> 108
    //   102: aconst_null
    //   103: astore_0
    //   104: aload_3
    //   105: monitorexit
    //   106: aload_0
    //   107: areturn
    //   108: aload #5
    //   110: getfield mWriteMode : Z
    //   113: ifeq -> 40
    //   116: aconst_null
    //   117: astore_0
    //   118: aload_3
    //   119: monitorexit
    //   120: goto -> 106
    //   123: aload #4
    //   125: invokeinterface remove : ()V
    //   130: goto -> 40
    //   133: aconst_null
    //   134: astore #4
    //   136: aconst_null
    //   137: astore #6
    //   139: aload #4
    //   141: astore #7
    //   143: aload #6
    //   145: astore #5
    //   147: new java/io/File
    //   150: astore #8
    //   152: aload #4
    //   154: astore #7
    //   156: aload #6
    //   158: astore #5
    //   160: aload #8
    //   162: invokestatic app : ()Landroid/app/Application;
    //   165: ldc 'process_lock'
    //   167: iconst_0
    //   168: invokevirtual getDir : (Ljava/lang/String;I)Ljava/io/File;
    //   171: aload_1
    //   172: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   175: aload #4
    //   177: astore #7
    //   179: aload #6
    //   181: astore #5
    //   183: aload #8
    //   185: invokevirtual exists : ()Z
    //   188: ifne -> 207
    //   191: aload #4
    //   193: astore #7
    //   195: aload #6
    //   197: astore #5
    //   199: aload #8
    //   201: invokevirtual createNewFile : ()Z
    //   204: ifeq -> 439
    //   207: iload_2
    //   208: ifeq -> 367
    //   211: aload #4
    //   213: astore #7
    //   215: aload #6
    //   217: astore #5
    //   219: new java/io/FileOutputStream
    //   222: astore #9
    //   224: aload #4
    //   226: astore #7
    //   228: aload #6
    //   230: astore #5
    //   232: aload #9
    //   234: aload #8
    //   236: iconst_0
    //   237: invokespecial <init> : (Ljava/io/File;Z)V
    //   240: aload #4
    //   242: astore #7
    //   244: aload #6
    //   246: astore #5
    //   248: aload #9
    //   250: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   253: astore_1
    //   254: aload #9
    //   256: astore #4
    //   258: aload_1
    //   259: ifnull -> 446
    //   262: iload_2
    //   263: ifne -> 416
    //   266: iconst_1
    //   267: istore #10
    //   269: aload_1
    //   270: astore #7
    //   272: aload #4
    //   274: astore #5
    //   276: aload_1
    //   277: lconst_0
    //   278: ldc2_w 9223372036854775807
    //   281: iload #10
    //   283: invokevirtual tryLock : (JJZ)Ljava/nio/channels/FileLock;
    //   286: astore #9
    //   288: aload_1
    //   289: astore #7
    //   291: aload #4
    //   293: astore #5
    //   295: aload #9
    //   297: invokestatic isValid : (Ljava/nio/channels/FileLock;)Z
    //   300: ifeq -> 422
    //   303: aload_1
    //   304: astore #7
    //   306: aload #4
    //   308: astore #5
    //   310: new org/xutils/common/util/ProcessLock
    //   313: astore #6
    //   315: aload_1
    //   316: astore #7
    //   318: aload #4
    //   320: astore #5
    //   322: aload #6
    //   324: aload_0
    //   325: aload #8
    //   327: aload #9
    //   329: aload #4
    //   331: iload_2
    //   332: invokespecial <init> : (Ljava/lang/String;Ljava/io/File;Ljava/nio/channels/FileLock;Ljava/io/Closeable;Z)V
    //   335: aload_1
    //   336: astore #7
    //   338: aload #4
    //   340: astore #5
    //   342: getstatic org/xutils/common/util/ProcessLock.LOCK_MAP : Lorg/xutils/common/util/DoubleKeyValueMap;
    //   345: aload_0
    //   346: aload #9
    //   348: invokevirtual hashCode : ()I
    //   351: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   354: aload #6
    //   356: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   359: aload_3
    //   360: monitorexit
    //   361: aload #6
    //   363: astore_0
    //   364: goto -> 106
    //   367: aload #4
    //   369: astore #7
    //   371: aload #6
    //   373: astore #5
    //   375: new java/io/FileInputStream
    //   378: astore_1
    //   379: aload #4
    //   381: astore #7
    //   383: aload #6
    //   385: astore #5
    //   387: aload_1
    //   388: aload #8
    //   390: invokespecial <init> : (Ljava/io/File;)V
    //   393: aload #4
    //   395: astore #7
    //   397: aload #6
    //   399: astore #5
    //   401: aload_1
    //   402: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   405: astore #6
    //   407: aload_1
    //   408: astore #4
    //   410: aload #6
    //   412: astore_1
    //   413: goto -> 258
    //   416: iconst_0
    //   417: istore #10
    //   419: goto -> 269
    //   422: aload_1
    //   423: astore #7
    //   425: aload #4
    //   427: astore #5
    //   429: aload_0
    //   430: aload #9
    //   432: aload #8
    //   434: aload #4
    //   436: invokestatic release : (Ljava/lang/String;Ljava/nio/channels/FileLock;Ljava/io/File;Ljava/io/Closeable;)V
    //   439: aload_3
    //   440: monitorexit
    //   441: aconst_null
    //   442: astore_0
    //   443: goto -> 106
    //   446: aload_1
    //   447: astore #7
    //   449: aload #4
    //   451: astore #5
    //   453: new java/io/IOException
    //   456: astore #9
    //   458: aload_1
    //   459: astore #7
    //   461: aload #4
    //   463: astore #5
    //   465: new java/lang/StringBuilder
    //   468: astore #6
    //   470: aload_1
    //   471: astore #7
    //   473: aload #4
    //   475: astore #5
    //   477: aload #6
    //   479: invokespecial <init> : ()V
    //   482: aload_1
    //   483: astore #7
    //   485: aload #4
    //   487: astore #5
    //   489: aload #9
    //   491: aload #6
    //   493: ldc_w 'can not get file channel:'
    //   496: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   499: aload #8
    //   501: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   504: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   507: invokevirtual toString : ()Ljava/lang/String;
    //   510: invokespecial <init> : (Ljava/lang/String;)V
    //   513: aload_1
    //   514: astore #7
    //   516: aload #4
    //   518: astore #5
    //   520: aload #9
    //   522: athrow
    //   523: astore #4
    //   525: new java/lang/StringBuilder
    //   528: astore_1
    //   529: aload_1
    //   530: invokespecial <init> : ()V
    //   533: aload_1
    //   534: ldc_w 'tryLock: '
    //   537: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   540: aload_0
    //   541: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   544: ldc_w ', '
    //   547: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   550: aload #4
    //   552: invokevirtual getMessage : ()Ljava/lang/String;
    //   555: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   558: invokevirtual toString : ()Ljava/lang/String;
    //   561: invokestatic d : (Ljava/lang/String;)V
    //   564: aload #5
    //   566: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   569: aload #7
    //   571: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   574: goto -> 439
    // Exception table:
    //   from	to	target	type
    //   6	15	93	finally
    //   20	40	93	finally
    //   40	70	93	finally
    //   75	90	93	finally
    //   94	96	93	finally
    //   104	106	93	finally
    //   108	116	93	finally
    //   118	120	93	finally
    //   123	130	93	finally
    //   147	152	523	java/lang/Throwable
    //   147	152	93	finally
    //   160	175	523	java/lang/Throwable
    //   160	175	93	finally
    //   183	191	523	java/lang/Throwable
    //   183	191	93	finally
    //   199	207	523	java/lang/Throwable
    //   199	207	93	finally
    //   219	224	523	java/lang/Throwable
    //   219	224	93	finally
    //   232	240	523	java/lang/Throwable
    //   232	240	93	finally
    //   248	254	523	java/lang/Throwable
    //   248	254	93	finally
    //   276	288	523	java/lang/Throwable
    //   276	288	93	finally
    //   295	303	523	java/lang/Throwable
    //   295	303	93	finally
    //   310	315	523	java/lang/Throwable
    //   310	315	93	finally
    //   322	335	523	java/lang/Throwable
    //   322	335	93	finally
    //   342	359	523	java/lang/Throwable
    //   342	359	93	finally
    //   359	361	93	finally
    //   375	379	523	java/lang/Throwable
    //   375	379	93	finally
    //   387	393	523	java/lang/Throwable
    //   387	393	93	finally
    //   401	407	523	java/lang/Throwable
    //   401	407	93	finally
    //   429	439	523	java/lang/Throwable
    //   429	439	93	finally
    //   439	441	93	finally
    //   453	458	523	java/lang/Throwable
    //   453	458	93	finally
    //   465	470	523	java/lang/Throwable
    //   465	470	93	finally
    //   477	482	523	java/lang/Throwable
    //   477	482	93	finally
    //   489	513	523	java/lang/Throwable
    //   489	513	93	finally
    //   520	523	523	java/lang/Throwable
    //   520	523	93	finally
    //   525	574	93	finally
  }
  
  public void close() throws IOException {
    release();
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
    release();
  }
  
  public boolean isValid() {
    return isValid(this.mFileLock);
  }
  
  public void release() {
    release(this.mLockName, this.mFileLock, this.mFile, this.mStream);
  }
  
  public String toString() {
    return this.mLockName + ": " + this.mFile.getName();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/ProcessLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */