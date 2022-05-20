package org.xutils.cache;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import org.xutils.DbManager;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.FileUtil;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.common.util.MD5;
import org.xutils.common.util.ProcessLock;
import org.xutils.config.DbConfigs;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.ex.FileLockedException;
import org.xutils.x;

public final class LruDiskCache {
  private static final String CACHE_DIR_NAME = "xUtils_cache";
  
  private static final HashMap<String, LruDiskCache> DISK_CACHE_MAP = new HashMap<String, LruDiskCache>(5);
  
  private static final int LIMIT_COUNT = 5000;
  
  private static final long LIMIT_SIZE = 104857600L;
  
  private static final int LOCK_WAIT = 3000;
  
  private static final String TEMP_FILE_SUFFIX = ".tmp";
  
  private static final long TRIM_TIME_SPAN = 1000L;
  
  private boolean available = false;
  
  private final DbManager cacheDb = x.getDb(DbConfigs.HTTP.getConfig());
  
  private File cacheDir;
  
  private long diskCacheSize = 104857600L;
  
  private long lastTrimTime = 0L;
  
  private final Executor trimExecutor = (Executor)new PriorityExecutor(1, true);
  
  private LruDiskCache(String paramString) {
    this.cacheDir = FileUtil.getCacheDir(paramString);
    if (this.cacheDir != null && (this.cacheDir.exists() || this.cacheDir.mkdirs()))
      this.available = true; 
    deleteNoIndexFiles();
  }
  
  private void deleteExpiry() {
    try {
      WhereBuilder whereBuilder = WhereBuilder.b("expires", "<", Long.valueOf(System.currentTimeMillis()));
      List list = this.cacheDb.selector(DiskCacheEntity.class).where(whereBuilder).findAll();
      this.cacheDb.delete(DiskCacheEntity.class, whereBuilder);
      if (list != null && list.size() > 0) {
        Iterator<DiskCacheEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
          String str = ((DiskCacheEntity)iterator.next()).getPath();
          if (!TextUtils.isEmpty(str))
            deleteFileWithLock(str); 
        } 
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  private boolean deleteFileWithLock(String paramString) {
    ProcessLock processLock = null;
    try {
      ProcessLock processLock1 = ProcessLock.tryLock(paramString, true);
      if (processLock1 != null) {
        processLock = processLock1;
        if (processLock1.isValid()) {
          processLock = processLock1;
          File file = new File();
          processLock = processLock1;
          this(paramString);
          processLock = processLock1;
          return IOUtil.deleteFileOrDir(file);
        } 
      } 
      return false;
    } finally {
      IOUtil.closeQuietly((Closeable)processLock);
    } 
  }
  
  private void deleteNoIndexFiles() {
    this.trimExecutor.execute(new Runnable() {
          public void run() {
            if (LruDiskCache.this.available)
              try {
                File[] arrayOfFile = LruDiskCache.this.cacheDir.listFiles();
                if (arrayOfFile != null) {
                  int i = arrayOfFile.length;
                  for (byte b = 0;; b++) {
                    if (b < i) {
                      File file = arrayOfFile[b];
                      try {
                        if (LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).where("path", "=", file.getAbsolutePath()).count() < 1L)
                          IOUtil.deleteFileOrDir(file); 
                      } catch (Throwable throwable) {}
                    } else {
                      break;
                    } 
                  } 
                } 
              } catch (Throwable throwable) {
                LogUtil.e(throwable.getMessage(), throwable);
              }  
          }
        });
  }
  
  public static LruDiskCache getDiskCache(String paramString) {
    // Byte code:
    //   0: ldc org/xutils/cache/LruDiskCache
    //   2: monitorenter
    //   3: aload_0
    //   4: astore_1
    //   5: aload_0
    //   6: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   9: ifeq -> 15
    //   12: ldc 'xUtils_cache'
    //   14: astore_1
    //   15: getstatic org/xutils/cache/LruDiskCache.DISK_CACHE_MAP : Ljava/util/HashMap;
    //   18: aload_1
    //   19: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   22: checkcast org/xutils/cache/LruDiskCache
    //   25: astore_2
    //   26: aload_2
    //   27: astore_0
    //   28: aload_2
    //   29: ifnonnull -> 50
    //   32: new org/xutils/cache/LruDiskCache
    //   35: astore_0
    //   36: aload_0
    //   37: aload_1
    //   38: invokespecial <init> : (Ljava/lang/String;)V
    //   41: getstatic org/xutils/cache/LruDiskCache.DISK_CACHE_MAP : Ljava/util/HashMap;
    //   44: aload_1
    //   45: aload_0
    //   46: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   49: pop
    //   50: ldc org/xutils/cache/LruDiskCache
    //   52: monitorexit
    //   53: aload_0
    //   54: areturn
    //   55: astore_0
    //   56: ldc org/xutils/cache/LruDiskCache
    //   58: monitorexit
    //   59: aload_0
    //   60: athrow
    // Exception table:
    //   from	to	target	type
    //   5	12	55	finally
    //   15	26	55	finally
    //   32	50	55	finally
  }
  
  private void trimSize() {
    this.trimExecutor.execute(new Runnable() {
          public void run() {
            long l;
            if (LruDiskCache.this.available) {
              l = System.currentTimeMillis();
              if (l - LruDiskCache.this.lastTrimTime < 1000L)
                return; 
            } else {
              return;
            } 
            LruDiskCache.access$202(LruDiskCache.this, l);
            LruDiskCache.this.deleteExpiry();
            try {
              int i = (int)LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).count();
              if (i > 5010) {
                List list = LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).orderBy("lastAccess").orderBy("hits").limit(i - 5000).offset(0).findAll();
                if (list != null && list.size() > 0)
                  for (DiskCacheEntity diskCacheEntity : list) {
                    try {
                      LruDiskCache.this.cacheDb.delete(diskCacheEntity);
                      String str = diskCacheEntity.getPath();
                      if (!TextUtils.isEmpty(str)) {
                        LruDiskCache.this.deleteFileWithLock(str);
                        LruDiskCache lruDiskCache = LruDiskCache.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        this();
                        lruDiskCache.deleteFileWithLock(stringBuilder.append(str).append(".tmp").toString());
                      } 
                    } catch (DbException dbException) {
                      LogUtil.e(dbException.getMessage(), (Throwable)dbException);
                    } 
                  }  
              } 
            } catch (DbException dbException) {
              LogUtil.e(dbException.getMessage(), (Throwable)dbException);
            } 
            while (true) {
              if (FileUtil.getFileOrDirSize(LruDiskCache.this.cacheDir) > LruDiskCache.this.diskCacheSize) {
                List list = LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).orderBy("lastAccess").orderBy("hits").limit(10).offset(0).findAll();
                if (list != null && list.size() > 0)
                  for (DiskCacheEntity diskCacheEntity : list) {
                    try {
                      LruDiskCache.this.cacheDb.delete(diskCacheEntity);
                      String str = diskCacheEntity.getPath();
                      if (!TextUtils.isEmpty(str)) {
                        LruDiskCache.this.deleteFileWithLock(str);
                        LruDiskCache lruDiskCache = LruDiskCache.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        this();
                        lruDiskCache.deleteFileWithLock(stringBuilder.append(str).append(".tmp").toString());
                      } 
                    } catch (DbException dbException) {
                      LogUtil.e(dbException.getMessage(), (Throwable)dbException);
                    } 
                  }  
                continue;
              } 
              return;
            } 
          }
        });
  }
  
  public void clearCacheFiles() {
    IOUtil.deleteFileOrDir(this.cacheDir);
  }
  
  DiskCacheFile commitDiskCacheFile(DiskCacheFile paramDiskCacheFile) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: ifnull -> 23
    //   6: aload_1
    //   7: invokevirtual length : ()J
    //   10: lconst_1
    //   11: lcmp
    //   12: ifge -> 23
    //   15: aload_1
    //   16: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   19: aload_2
    //   20: astore_3
    //   21: aload_3
    //   22: areturn
    //   23: aload_2
    //   24: astore_3
    //   25: aload_0
    //   26: getfield available : Z
    //   29: ifeq -> 21
    //   32: aload_2
    //   33: astore_3
    //   34: aload_1
    //   35: ifnull -> 21
    //   38: aconst_null
    //   39: astore #4
    //   41: aconst_null
    //   42: astore #5
    //   44: aload_1
    //   45: getfield cacheEntity : Lorg/xutils/cache/DiskCacheEntity;
    //   48: astore #6
    //   50: aload_1
    //   51: invokevirtual getName : ()Ljava/lang/String;
    //   54: ldc '.tmp'
    //   56: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   59: ifeq -> 515
    //   62: aconst_null
    //   63: astore #7
    //   65: aconst_null
    //   66: astore_3
    //   67: aconst_null
    //   68: astore #8
    //   70: aconst_null
    //   71: astore #9
    //   73: aload_3
    //   74: astore #10
    //   76: aload #8
    //   78: astore #11
    //   80: aload #7
    //   82: astore_2
    //   83: aload #4
    //   85: astore #12
    //   87: aload #6
    //   89: invokevirtual getPath : ()Ljava/lang/String;
    //   92: astore #13
    //   94: aload_3
    //   95: astore #10
    //   97: aload #8
    //   99: astore #11
    //   101: aload #7
    //   103: astore_2
    //   104: aload #4
    //   106: astore #12
    //   108: aload #13
    //   110: iconst_1
    //   111: ldc2_w 3000
    //   114: invokestatic tryLock : (Ljava/lang/String;ZJ)Lorg/xutils/common/util/ProcessLock;
    //   117: astore_3
    //   118: aload_3
    //   119: ifnull -> 407
    //   122: aload_3
    //   123: astore #10
    //   125: aload #8
    //   127: astore #11
    //   129: aload_3
    //   130: astore_2
    //   131: aload #4
    //   133: astore #12
    //   135: aload_3
    //   136: invokevirtual isValid : ()Z
    //   139: ifeq -> 407
    //   142: aload_3
    //   143: astore #10
    //   145: aload #8
    //   147: astore #11
    //   149: aload_3
    //   150: astore_2
    //   151: aload #4
    //   153: astore #12
    //   155: new org/xutils/cache/DiskCacheFile
    //   158: astore #7
    //   160: aload_3
    //   161: astore #10
    //   163: aload #8
    //   165: astore #11
    //   167: aload_3
    //   168: astore_2
    //   169: aload #4
    //   171: astore #12
    //   173: aload #7
    //   175: aload #6
    //   177: aload #13
    //   179: aload_3
    //   180: invokespecial <init> : (Lorg/xutils/cache/DiskCacheEntity;Ljava/lang/String;Lorg/xutils/common/util/ProcessLock;)V
    //   183: aload #5
    //   185: astore_2
    //   186: aload_1
    //   187: aload #7
    //   189: invokevirtual renameTo : (Ljava/io/File;)Z
    //   192: istore #14
    //   194: iload #14
    //   196: ifeq -> 317
    //   199: aload #7
    //   201: astore #12
    //   203: aload #12
    //   205: astore_2
    //   206: aload_0
    //   207: getfield cacheDb : Lorg/xutils/DbManager;
    //   210: aload #6
    //   212: invokeinterface replace : (Ljava/lang/Object;)V
    //   217: aload #12
    //   219: astore_2
    //   220: aload_0
    //   221: invokespecial trimSize : ()V
    //   224: aload #12
    //   226: ifnonnull -> 473
    //   229: aload #7
    //   231: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   234: aload_3
    //   235: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   238: aload #7
    //   240: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   243: pop
    //   244: aload_1
    //   245: astore_3
    //   246: goto -> 21
    //   249: astore #11
    //   251: aload #12
    //   253: astore_2
    //   254: aload #11
    //   256: invokevirtual getMessage : ()Ljava/lang/String;
    //   259: aload #11
    //   261: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   264: goto -> 217
    //   267: astore #5
    //   269: aload_1
    //   270: astore #10
    //   272: aload #7
    //   274: astore #11
    //   276: aload_3
    //   277: astore_2
    //   278: aload #10
    //   280: astore #12
    //   282: aload #5
    //   284: invokevirtual getMessage : ()Ljava/lang/String;
    //   287: aload #5
    //   289: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   292: aload #10
    //   294: ifnonnull -> 488
    //   297: aload #7
    //   299: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   302: aload_3
    //   303: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   306: aload #7
    //   308: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   311: pop
    //   312: aload_1
    //   313: astore_3
    //   314: goto -> 21
    //   317: aload #5
    //   319: astore_2
    //   320: new java/io/IOException
    //   323: astore #12
    //   325: aload #5
    //   327: astore_2
    //   328: new java/lang/StringBuilder
    //   331: astore #11
    //   333: aload #5
    //   335: astore_2
    //   336: aload #11
    //   338: invokespecial <init> : ()V
    //   341: aload #5
    //   343: astore_2
    //   344: aload #12
    //   346: aload #11
    //   348: ldc_w 'rename:'
    //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: aload_1
    //   355: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   358: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: invokevirtual toString : ()Ljava/lang/String;
    //   364: invokespecial <init> : (Ljava/lang/String;)V
    //   367: aload #5
    //   369: astore_2
    //   370: aload #12
    //   372: athrow
    //   373: astore #12
    //   375: aload_2
    //   376: astore #10
    //   378: aload_3
    //   379: astore_2
    //   380: aload #7
    //   382: astore #11
    //   384: aload #10
    //   386: ifnonnull -> 503
    //   389: aload #11
    //   391: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   394: aload_2
    //   395: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   398: aload #11
    //   400: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   403: pop
    //   404: aload #12
    //   406: athrow
    //   407: aload_3
    //   408: astore #10
    //   410: aload #8
    //   412: astore #11
    //   414: aload_3
    //   415: astore_2
    //   416: aload #4
    //   418: astore #12
    //   420: new org/xutils/ex/FileLockedException
    //   423: astore #7
    //   425: aload_3
    //   426: astore #10
    //   428: aload #8
    //   430: astore #11
    //   432: aload_3
    //   433: astore_2
    //   434: aload #4
    //   436: astore #12
    //   438: aload #7
    //   440: aload #13
    //   442: invokespecial <init> : (Ljava/lang/String;)V
    //   445: aload_3
    //   446: astore #10
    //   448: aload #8
    //   450: astore #11
    //   452: aload_3
    //   453: astore_2
    //   454: aload #4
    //   456: astore #12
    //   458: aload #7
    //   460: athrow
    //   461: astore #5
    //   463: aload #9
    //   465: astore #7
    //   467: aload #10
    //   469: astore_3
    //   470: goto -> 269
    //   473: aload_1
    //   474: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   477: aload_1
    //   478: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   481: pop
    //   482: aload #12
    //   484: astore_3
    //   485: goto -> 21
    //   488: aload_1
    //   489: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   492: aload_1
    //   493: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   496: pop
    //   497: aload #10
    //   499: astore_3
    //   500: goto -> 21
    //   503: aload_1
    //   504: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   507: aload_1
    //   508: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   511: pop
    //   512: goto -> 404
    //   515: aload_1
    //   516: astore_3
    //   517: goto -> 21
    //   520: astore_3
    //   521: aload #12
    //   523: astore #10
    //   525: aload_3
    //   526: astore #12
    //   528: goto -> 384
    // Exception table:
    //   from	to	target	type
    //   87	94	461	java/lang/InterruptedException
    //   87	94	520	finally
    //   108	118	461	java/lang/InterruptedException
    //   108	118	520	finally
    //   135	142	461	java/lang/InterruptedException
    //   135	142	520	finally
    //   155	160	461	java/lang/InterruptedException
    //   155	160	520	finally
    //   173	183	461	java/lang/InterruptedException
    //   173	183	520	finally
    //   186	194	267	java/lang/InterruptedException
    //   186	194	373	finally
    //   206	217	249	org/xutils/ex/DbException
    //   206	217	267	java/lang/InterruptedException
    //   206	217	373	finally
    //   220	224	267	java/lang/InterruptedException
    //   220	224	373	finally
    //   254	264	267	java/lang/InterruptedException
    //   254	264	373	finally
    //   282	292	520	finally
    //   320	325	267	java/lang/InterruptedException
    //   320	325	373	finally
    //   328	333	267	java/lang/InterruptedException
    //   328	333	373	finally
    //   336	341	267	java/lang/InterruptedException
    //   336	341	373	finally
    //   344	367	267	java/lang/InterruptedException
    //   344	367	373	finally
    //   370	373	267	java/lang/InterruptedException
    //   370	373	373	finally
    //   420	425	461	java/lang/InterruptedException
    //   420	425	520	finally
    //   438	445	461	java/lang/InterruptedException
    //   438	445	520	finally
    //   458	461	461	java/lang/InterruptedException
    //   458	461	520	finally
  }
  
  public DiskCacheFile createDiskCacheFile(DiskCacheEntity paramDiskCacheEntity) throws IOException {
    DiskCacheFile diskCacheFile;
    if (!this.available || paramDiskCacheEntity == null)
      return null; 
    paramDiskCacheEntity.setPath((new File(this.cacheDir, MD5.md5(paramDiskCacheEntity.getKey()))).getAbsolutePath());
    String str = paramDiskCacheEntity.getPath() + ".tmp";
    ProcessLock processLock = ProcessLock.tryLock(str, true);
    if (processLock != null && processLock.isValid()) {
      DiskCacheFile diskCacheFile1 = new DiskCacheFile(paramDiskCacheEntity, str, processLock);
      diskCacheFile = diskCacheFile1;
      if (!diskCacheFile1.getParentFile().exists()) {
        diskCacheFile1.mkdirs();
        diskCacheFile = diskCacheFile1;
      } 
      return diskCacheFile;
    } 
    throw new FileLockedException(diskCacheFile.getPath());
  }
  
  public DiskCacheEntity get(String paramString) {
    if (!this.available || TextUtils.isEmpty(paramString))
      return null; 
    DiskCacheEntity diskCacheEntity = null;
    try {
      final DiskCacheEntity finalResult = (DiskCacheEntity)this.cacheDb.selector(DiskCacheEntity.class).where("key", "=", paramString).findFirst();
      diskCacheEntity = diskCacheEntity1;
      if (diskCacheEntity1 != null) {
        if (diskCacheEntity1.getExpires() < System.currentTimeMillis())
          return null; 
        this.trimExecutor.execute(new Runnable() {
              public void run() {
                finalResult.setHits(finalResult.getHits() + 1L);
                finalResult.setLastAccess(System.currentTimeMillis());
                try {
                  LruDiskCache.this.cacheDb.update(finalResult, new String[] { "hits", "lastAccess" });
                } catch (Throwable throwable) {
                  LogUtil.e(throwable.getMessage(), throwable);
                } 
              }
            });
        diskCacheEntity = diskCacheEntity1;
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
      final DiskCacheEntity finalResult = diskCacheEntity;
    } 
    return diskCacheEntity;
  }
  
  public DiskCacheFile getDiskCacheFile(String paramString) throws InterruptedException {
    if (!this.available || TextUtils.isEmpty(paramString))
      return null; 
    String str = null;
    DiskCacheEntity diskCacheEntity = get(paramString);
    paramString = str;
    paramString = str;
    if (diskCacheEntity != null && (new File(diskCacheEntity.getPath())).exists()) {
      ProcessLock processLock = ProcessLock.tryLock(diskCacheEntity.getPath(), false, 3000L);
      paramString = str;
      if (processLock != null) {
        paramString = str;
        if (processLock.isValid()) {
          DiskCacheFile diskCacheFile2 = new DiskCacheFile(diskCacheEntity, diskCacheEntity.getPath(), processLock);
          DiskCacheFile diskCacheFile1 = diskCacheFile2;
          if (!diskCacheFile2.exists())
            try {
              this.cacheDb.delete(diskCacheEntity);
              diskCacheFile1 = null;
            } catch (DbException dbException) {
              LogUtil.e(dbException.getMessage(), (Throwable)dbException);
            }  
        } 
      } 
    } 
    return (DiskCacheFile)dbException;
  }
  
  public void put(DiskCacheEntity paramDiskCacheEntity) {
    if (this.available && paramDiskCacheEntity != null && !TextUtils.isEmpty(paramDiskCacheEntity.getTextContent()) && paramDiskCacheEntity.getExpires() >= System.currentTimeMillis()) {
      try {
        this.cacheDb.replace(paramDiskCacheEntity);
      } catch (DbException dbException) {
        LogUtil.e(dbException.getMessage(), (Throwable)dbException);
      } 
      trimSize();
    } 
  }
  
  public LruDiskCache setMaxSize(long paramLong) {
    long l;
    if (paramLong > 0L) {
      l = FileUtil.getDiskAvailableSize();
      if (l > paramLong) {
        this.diskCacheSize = paramLong;
        return this;
      } 
    } else {
      return this;
    } 
    this.diskCacheSize = l;
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/cache/LruDiskCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */