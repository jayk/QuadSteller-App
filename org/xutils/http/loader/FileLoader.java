package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.DiskCacheFile;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public class FileLoader extends Loader<File> {
  private static final int CHECK_SIZE = 512;
  
  private long contentLength;
  
  private DiskCacheFile diskCacheFile;
  
  private boolean isAutoRename;
  
  private boolean isAutoResume;
  
  private String responseFileName;
  
  private String saveFilePath;
  
  private String tempSaveFilePath;
  
  private File autoRename(File paramFile) {
    if (this.isAutoRename && paramFile.exists() && !TextUtils.isEmpty(this.responseFileName)) {
      File file;
      for (file = new File(paramFile.getParent(), this.responseFileName); file.exists(); file = new File(paramFile.getParent(), System.currentTimeMillis() + this.responseFileName));
      if (!paramFile.renameTo(file))
        file = paramFile; 
      return file;
    } 
    if (!this.saveFilePath.equals(this.tempSaveFilePath)) {
      File file2 = new File(this.saveFilePath);
      File file1 = file2;
      if (!paramFile.renameTo(file2))
        file1 = paramFile; 
      return file1;
    } 
    return paramFile;
  }
  
  private static String getResponseFileName(UriRequest paramUriRequest) {
    if (paramUriRequest == null)
      return null; 
    String str = paramUriRequest.getResponseHeader("Content-Disposition");
    if (!TextUtils.isEmpty(str)) {
      int i = str.indexOf("filename=");
      if (i > 0) {
        int j = i + 9;
        int k = str.indexOf(";", j);
        i = k;
        if (k < 0)
          i = str.length(); 
        if (i > j) {
          try {
            str = URLDecoder.decode(str.substring(j, i), paramUriRequest.getParams().getCharset());
            String str1 = str;
            if (str.startsWith("\"")) {
              str1 = str;
              if (str.endsWith("\""))
                str1 = str.substring(1, str.length() - 1); 
            } 
          } catch (UnsupportedEncodingException unsupportedEncodingException) {
            LogUtil.e(unsupportedEncodingException.getMessage(), unsupportedEncodingException);
          } 
          return (String)unsupportedEncodingException;
        } 
      } 
    } 
    return null;
  }
  
  private void initDiskCacheFile(UriRequest paramUriRequest) throws Throwable {
    DiskCacheEntity diskCacheEntity = new DiskCacheEntity();
    diskCacheEntity.setKey(paramUriRequest.getCacheKey());
    this.diskCacheFile = LruDiskCache.getDiskCache(this.params.getCacheDirName()).createDiskCacheFile(diskCacheEntity);
    if (this.diskCacheFile != null) {
      this.saveFilePath = this.diskCacheFile.getAbsolutePath();
      this.tempSaveFilePath = this.saveFilePath;
      this.isAutoRename = false;
      return;
    } 
    throw new IOException("create cache file error:" + paramUriRequest.getCacheKey());
  }
  
  private static boolean isSupportRange(UriRequest paramUriRequest) {
    boolean bool1 = false;
    if (paramUriRequest == null)
      return bool1; 
    String str2 = paramUriRequest.getResponseHeader("Accept-Ranges");
    if (str2 != null)
      return str2.contains("bytes"); 
    String str1 = paramUriRequest.getResponseHeader("Content-Range");
    boolean bool2 = bool1;
    if (str1 != null) {
      bool2 = bool1;
      if (str1.contains("bytes"))
        bool2 = true; 
    } 
    return bool2;
  }
  
  public File load(InputStream paramInputStream) throws Throwable {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new java/io/File
    //   7: astore #4
    //   9: aload #4
    //   11: aload_0
    //   12: getfield tempSaveFilePath : Ljava/lang/String;
    //   15: invokespecial <init> : (Ljava/lang/String;)V
    //   18: aload #4
    //   20: invokevirtual isDirectory : ()Z
    //   23: ifeq -> 32
    //   26: aload #4
    //   28: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   31: pop
    //   32: aload #4
    //   34: invokevirtual exists : ()Z
    //   37: ifne -> 120
    //   40: aload #4
    //   42: invokevirtual getParentFile : ()Ljava/io/File;
    //   45: astore #5
    //   47: aload #5
    //   49: invokevirtual exists : ()Z
    //   52: ifne -> 120
    //   55: aload #5
    //   57: invokevirtual mkdirs : ()Z
    //   60: ifne -> 120
    //   63: new java/io/IOException
    //   66: astore_1
    //   67: new java/lang/StringBuilder
    //   70: astore #6
    //   72: aload #6
    //   74: invokespecial <init> : ()V
    //   77: aload_1
    //   78: aload #6
    //   80: ldc 'can not create dir: '
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload #5
    //   87: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: invokevirtual toString : ()Ljava/lang/String;
    //   96: invokespecial <init> : (Ljava/lang/String;)V
    //   99: aload_1
    //   100: athrow
    //   101: astore_1
    //   102: aload_3
    //   103: astore #6
    //   105: aload_2
    //   106: astore #5
    //   108: aload #5
    //   110: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   113: aload #6
    //   115: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   118: aload_1
    //   119: athrow
    //   120: aload #4
    //   122: invokevirtual length : ()J
    //   125: lstore #7
    //   127: aload_0
    //   128: getfield isAutoResume : Z
    //   131: istore #9
    //   133: iload #9
    //   135: ifeq -> 255
    //   138: lload #7
    //   140: lconst_0
    //   141: lcmp
    //   142: ifle -> 255
    //   145: aconst_null
    //   146: astore #5
    //   148: lload #7
    //   150: ldc2_w 512
    //   153: lsub
    //   154: lstore #10
    //   156: lload #10
    //   158: lconst_0
    //   159: lcmp
    //   160: ifle -> 361
    //   163: new java/io/FileInputStream
    //   166: astore #6
    //   168: aload #6
    //   170: aload #4
    //   172: invokespecial <init> : (Ljava/io/File;)V
    //   175: aload #6
    //   177: lload #10
    //   179: sipush #512
    //   182: invokestatic readBytes : (Ljava/io/InputStream;JI)[B
    //   185: astore #5
    //   187: aload_1
    //   188: lconst_0
    //   189: sipush #512
    //   192: invokestatic readBytes : (Ljava/io/InputStream;JI)[B
    //   195: aload #5
    //   197: invokestatic equals : ([B[B)Z
    //   200: ifne -> 238
    //   203: aload #6
    //   205: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   208: aload #4
    //   210: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   213: pop
    //   214: new java/lang/RuntimeException
    //   217: astore_1
    //   218: aload_1
    //   219: ldc 'need retry'
    //   221: invokespecial <init> : (Ljava/lang/String;)V
    //   224: aload_1
    //   225: athrow
    //   226: astore_1
    //   227: aload #6
    //   229: astore #5
    //   231: aload #5
    //   233: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   236: aload_1
    //   237: athrow
    //   238: aload_0
    //   239: aload_0
    //   240: getfield contentLength : J
    //   243: ldc2_w 512
    //   246: lsub
    //   247: putfield contentLength : J
    //   250: aload #6
    //   252: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   255: lconst_0
    //   256: lstore #10
    //   258: aload_0
    //   259: getfield isAutoResume : Z
    //   262: ifeq -> 383
    //   265: new java/io/FileOutputStream
    //   268: astore #6
    //   270: aload #6
    //   272: aload #4
    //   274: iconst_1
    //   275: invokespecial <init> : (Ljava/io/File;Z)V
    //   278: aload_0
    //   279: getfield contentLength : J
    //   282: lload #7
    //   284: ladd
    //   285: lstore #12
    //   287: new java/io/BufferedInputStream
    //   290: dup
    //   291: aload_1
    //   292: invokespecial <init> : (Ljava/io/InputStream;)V
    //   295: astore_1
    //   296: new java/io/BufferedOutputStream
    //   299: astore #5
    //   301: aload #5
    //   303: aload #6
    //   305: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   308: aload_0
    //   309: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   312: ifnull -> 401
    //   315: aload_0
    //   316: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   319: lload #12
    //   321: lload #7
    //   323: iconst_1
    //   324: invokeinterface updateProgress : (JJZ)Z
    //   329: ifne -> 401
    //   332: new org/xutils/common/Callback$CancelledException
    //   335: astore #6
    //   337: aload #6
    //   339: ldc_w 'download stopped!'
    //   342: invokespecial <init> : (Ljava/lang/String;)V
    //   345: aload #6
    //   347: athrow
    //   348: astore_3
    //   349: aload #5
    //   351: astore #6
    //   353: aload_1
    //   354: astore #5
    //   356: aload_3
    //   357: astore_1
    //   358: goto -> 108
    //   361: aload #4
    //   363: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   366: pop
    //   367: new java/lang/RuntimeException
    //   370: astore_1
    //   371: aload_1
    //   372: ldc 'need retry'
    //   374: invokespecial <init> : (Ljava/lang/String;)V
    //   377: aload_1
    //   378: athrow
    //   379: astore_1
    //   380: goto -> 231
    //   383: new java/io/FileOutputStream
    //   386: dup
    //   387: aload #4
    //   389: invokespecial <init> : (Ljava/io/File;)V
    //   392: astore #6
    //   394: lload #10
    //   396: lstore #7
    //   398: goto -> 278
    //   401: sipush #4096
    //   404: newarray byte
    //   406: astore #6
    //   408: aload_1
    //   409: aload #6
    //   411: invokevirtual read : ([B)I
    //   414: istore #14
    //   416: iload #14
    //   418: iconst_m1
    //   419: if_icmpeq -> 529
    //   422: aload #4
    //   424: invokevirtual getParentFile : ()Ljava/io/File;
    //   427: invokevirtual exists : ()Z
    //   430: ifne -> 458
    //   433: aload #4
    //   435: invokevirtual getParentFile : ()Ljava/io/File;
    //   438: invokevirtual mkdirs : ()Z
    //   441: pop
    //   442: new java/io/IOException
    //   445: astore #6
    //   447: aload #6
    //   449: ldc_w 'parent be deleted!'
    //   452: invokespecial <init> : (Ljava/lang/String;)V
    //   455: aload #6
    //   457: athrow
    //   458: aload #5
    //   460: aload #6
    //   462: iconst_0
    //   463: iload #14
    //   465: invokevirtual write : ([BII)V
    //   468: lload #7
    //   470: iload #14
    //   472: i2l
    //   473: ladd
    //   474: lstore #10
    //   476: lload #10
    //   478: lstore #7
    //   480: aload_0
    //   481: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   484: ifnull -> 408
    //   487: lload #10
    //   489: lstore #7
    //   491: aload_0
    //   492: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   495: lload #12
    //   497: lload #10
    //   499: iconst_0
    //   500: invokeinterface updateProgress : (JJZ)Z
    //   505: ifne -> 408
    //   508: aload #5
    //   510: invokevirtual flush : ()V
    //   513: new org/xutils/common/Callback$CancelledException
    //   516: astore #6
    //   518: aload #6
    //   520: ldc_w 'download stopped!'
    //   523: invokespecial <init> : (Ljava/lang/String;)V
    //   526: aload #6
    //   528: athrow
    //   529: aload #5
    //   531: invokevirtual flush : ()V
    //   534: aload_0
    //   535: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   538: ifnull -> 625
    //   541: aload_0
    //   542: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   545: invokevirtual commit : ()Lorg/xutils/cache/DiskCacheFile;
    //   548: astore #6
    //   550: aload_0
    //   551: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   554: ifnull -> 572
    //   557: aload_0
    //   558: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   561: lload #12
    //   563: lload #7
    //   565: iconst_1
    //   566: invokeinterface updateProgress : (JJZ)Z
    //   571: pop
    //   572: aload_1
    //   573: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   576: aload #5
    //   578: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   581: aload_0
    //   582: aload #6
    //   584: invokespecial autoRename : (Ljava/io/File;)Ljava/io/File;
    //   587: areturn
    //   588: astore_1
    //   589: aload_2
    //   590: astore #5
    //   592: aload_3
    //   593: astore #6
    //   595: goto -> 108
    //   598: astore #6
    //   600: aload_1
    //   601: astore #5
    //   603: aload #6
    //   605: astore_1
    //   606: aload_3
    //   607: astore #6
    //   609: goto -> 108
    //   612: astore_3
    //   613: aload #5
    //   615: astore #6
    //   617: aload_1
    //   618: astore #5
    //   620: aload_3
    //   621: astore_1
    //   622: goto -> 108
    //   625: aload #4
    //   627: astore #6
    //   629: goto -> 550
    // Exception table:
    //   from	to	target	type
    //   4	18	588	finally
    //   18	32	101	finally
    //   32	101	101	finally
    //   120	133	101	finally
    //   163	175	379	finally
    //   175	226	226	finally
    //   231	238	101	finally
    //   238	250	226	finally
    //   250	255	101	finally
    //   258	265	101	finally
    //   265	278	101	finally
    //   278	296	101	finally
    //   296	308	598	finally
    //   308	348	348	finally
    //   361	379	379	finally
    //   383	394	101	finally
    //   401	408	348	finally
    //   408	416	348	finally
    //   422	458	348	finally
    //   458	468	348	finally
    //   480	487	348	finally
    //   491	529	348	finally
    //   529	550	348	finally
    //   550	572	612	finally
  }
  
  public File load(UriRequest paramUriRequest) throws Throwable {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_3
    //   5: astore #4
    //   7: aload_2
    //   8: astore #5
    //   10: aload_0
    //   11: aload_0
    //   12: getfield params : Lorg/xutils/http/RequestParams;
    //   15: invokevirtual getSaveFilePath : ()Ljava/lang/String;
    //   18: putfield saveFilePath : Ljava/lang/String;
    //   21: aload_3
    //   22: astore #4
    //   24: aload_2
    //   25: astore #5
    //   27: aload_0
    //   28: aconst_null
    //   29: putfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   32: aload_3
    //   33: astore #4
    //   35: aload_2
    //   36: astore #5
    //   38: aload_0
    //   39: getfield saveFilePath : Ljava/lang/String;
    //   42: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   45: ifeq -> 310
    //   48: aload_3
    //   49: astore #4
    //   51: aload_2
    //   52: astore #5
    //   54: aload_0
    //   55: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   58: ifnull -> 216
    //   61: aload_3
    //   62: astore #4
    //   64: aload_2
    //   65: astore #5
    //   67: aload_0
    //   68: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   71: lconst_0
    //   72: lconst_0
    //   73: iconst_0
    //   74: invokeinterface updateProgress : (JJZ)Z
    //   79: ifne -> 216
    //   82: aload_3
    //   83: astore #4
    //   85: aload_2
    //   86: astore #5
    //   88: new org/xutils/common/Callback$CancelledException
    //   91: astore #6
    //   93: aload_3
    //   94: astore #4
    //   96: aload_2
    //   97: astore #5
    //   99: aload #6
    //   101: ldc_w 'download stopped!'
    //   104: invokespecial <init> : (Ljava/lang/String;)V
    //   107: aload_3
    //   108: astore #4
    //   110: aload_2
    //   111: astore #5
    //   113: aload #6
    //   115: athrow
    //   116: astore_3
    //   117: aload #4
    //   119: astore #5
    //   121: aload_3
    //   122: invokevirtual getCode : ()I
    //   125: sipush #416
    //   128: if_icmpne -> 1095
    //   131: aload #4
    //   133: astore #5
    //   135: aload_0
    //   136: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   139: ifnull -> 1012
    //   142: aload #4
    //   144: astore #5
    //   146: aload_0
    //   147: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   150: invokevirtual commit : ()Lorg/xutils/cache/DiskCacheFile;
    //   153: astore_3
    //   154: aload_3
    //   155: ifnull -> 1031
    //   158: aload #4
    //   160: astore #5
    //   162: aload_3
    //   163: invokevirtual exists : ()Z
    //   166: ifeq -> 1031
    //   169: aload #4
    //   171: astore #5
    //   173: aload_0
    //   174: getfield isAutoRename : Z
    //   177: ifeq -> 192
    //   180: aload #4
    //   182: astore #5
    //   184: aload_0
    //   185: aload_1
    //   186: invokestatic getResponseFileName : (Lorg/xutils/http/request/UriRequest;)Ljava/lang/String;
    //   189: putfield responseFileName : Ljava/lang/String;
    //   192: aload #4
    //   194: astore #5
    //   196: aload_0
    //   197: aload_3
    //   198: invokespecial autoRename : (Ljava/io/File;)Ljava/io/File;
    //   201: astore_1
    //   202: aload #4
    //   204: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   207: aload_0
    //   208: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   211: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   214: aload_1
    //   215: areturn
    //   216: aload_3
    //   217: astore #4
    //   219: aload_2
    //   220: astore #5
    //   222: aload_0
    //   223: aload_1
    //   224: invokespecial initDiskCacheFile : (Lorg/xutils/http/request/UriRequest;)V
    //   227: aload_3
    //   228: astore #4
    //   230: aload_2
    //   231: astore #5
    //   233: aload_0
    //   234: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   237: ifnull -> 363
    //   240: aload_3
    //   241: astore #4
    //   243: aload_2
    //   244: astore #5
    //   246: aload_0
    //   247: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   250: lconst_0
    //   251: lconst_0
    //   252: iconst_0
    //   253: invokeinterface updateProgress : (JJZ)Z
    //   258: ifne -> 363
    //   261: aload_3
    //   262: astore #4
    //   264: aload_2
    //   265: astore #5
    //   267: new org/xutils/common/Callback$CancelledException
    //   270: astore #6
    //   272: aload_3
    //   273: astore #4
    //   275: aload_2
    //   276: astore #5
    //   278: aload #6
    //   280: ldc_w 'download stopped!'
    //   283: invokespecial <init> : (Ljava/lang/String;)V
    //   286: aload_3
    //   287: astore #4
    //   289: aload_2
    //   290: astore #5
    //   292: aload #6
    //   294: athrow
    //   295: astore_1
    //   296: aload #5
    //   298: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   301: aload_0
    //   302: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   305: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   308: aload_1
    //   309: athrow
    //   310: aload_3
    //   311: astore #4
    //   313: aload_2
    //   314: astore #5
    //   316: new java/lang/StringBuilder
    //   319: astore #6
    //   321: aload_3
    //   322: astore #4
    //   324: aload_2
    //   325: astore #5
    //   327: aload #6
    //   329: invokespecial <init> : ()V
    //   332: aload_3
    //   333: astore #4
    //   335: aload_2
    //   336: astore #5
    //   338: aload_0
    //   339: aload #6
    //   341: aload_0
    //   342: getfield saveFilePath : Ljava/lang/String;
    //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: ldc_w '.tmp'
    //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: invokevirtual toString : ()Ljava/lang/String;
    //   357: putfield tempSaveFilePath : Ljava/lang/String;
    //   360: goto -> 227
    //   363: aload_3
    //   364: astore #4
    //   366: aload_2
    //   367: astore #5
    //   369: new java/lang/StringBuilder
    //   372: astore #6
    //   374: aload_3
    //   375: astore #4
    //   377: aload_2
    //   378: astore #5
    //   380: aload #6
    //   382: invokespecial <init> : ()V
    //   385: aload_3
    //   386: astore #4
    //   388: aload_2
    //   389: astore #5
    //   391: aload #6
    //   393: aload_0
    //   394: getfield saveFilePath : Ljava/lang/String;
    //   397: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: ldc_w '_lock'
    //   403: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   406: invokevirtual toString : ()Ljava/lang/String;
    //   409: iconst_1
    //   410: invokestatic tryLock : (Ljava/lang/String;Z)Lorg/xutils/common/util/ProcessLock;
    //   413: astore_3
    //   414: aload_3
    //   415: ifnull -> 431
    //   418: aload_3
    //   419: astore #4
    //   421: aload_3
    //   422: astore #5
    //   424: aload_3
    //   425: invokevirtual isValid : ()Z
    //   428: ifne -> 499
    //   431: aload_3
    //   432: astore #4
    //   434: aload_3
    //   435: astore #5
    //   437: new org/xutils/ex/FileLockedException
    //   440: astore_2
    //   441: aload_3
    //   442: astore #4
    //   444: aload_3
    //   445: astore #5
    //   447: new java/lang/StringBuilder
    //   450: astore #6
    //   452: aload_3
    //   453: astore #4
    //   455: aload_3
    //   456: astore #5
    //   458: aload #6
    //   460: invokespecial <init> : ()V
    //   463: aload_3
    //   464: astore #4
    //   466: aload_3
    //   467: astore #5
    //   469: aload_2
    //   470: aload #6
    //   472: ldc_w 'download exists: '
    //   475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   478: aload_0
    //   479: getfield saveFilePath : Ljava/lang/String;
    //   482: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   485: invokevirtual toString : ()Ljava/lang/String;
    //   488: invokespecial <init> : (Ljava/lang/String;)V
    //   491: aload_3
    //   492: astore #4
    //   494: aload_3
    //   495: astore #5
    //   497: aload_2
    //   498: athrow
    //   499: aload_3
    //   500: astore #4
    //   502: aload_3
    //   503: astore #5
    //   505: aload_0
    //   506: aload_1
    //   507: invokevirtual getParams : ()Lorg/xutils/http/RequestParams;
    //   510: putfield params : Lorg/xutils/http/RequestParams;
    //   513: lconst_0
    //   514: lstore #7
    //   516: aload_3
    //   517: astore #4
    //   519: aload_3
    //   520: astore #5
    //   522: aload_0
    //   523: getfield isAutoResume : Z
    //   526: ifeq -> 588
    //   529: aload_3
    //   530: astore #4
    //   532: aload_3
    //   533: astore #5
    //   535: new java/io/File
    //   538: astore_2
    //   539: aload_3
    //   540: astore #4
    //   542: aload_3
    //   543: astore #5
    //   545: aload_2
    //   546: aload_0
    //   547: getfield tempSaveFilePath : Ljava/lang/String;
    //   550: invokespecial <init> : (Ljava/lang/String;)V
    //   553: aload_3
    //   554: astore #4
    //   556: aload_3
    //   557: astore #5
    //   559: aload_2
    //   560: invokevirtual length : ()J
    //   563: lstore #7
    //   565: lload #7
    //   567: ldc2_w 512
    //   570: lcmp
    //   571: ifgt -> 721
    //   574: aload_3
    //   575: astore #4
    //   577: aload_3
    //   578: astore #5
    //   580: aload_2
    //   581: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   584: pop
    //   585: lconst_0
    //   586: lstore #7
    //   588: aload_3
    //   589: astore #4
    //   591: aload_3
    //   592: astore #5
    //   594: aload_0
    //   595: getfield params : Lorg/xutils/http/RequestParams;
    //   598: astore_2
    //   599: aload_3
    //   600: astore #4
    //   602: aload_3
    //   603: astore #5
    //   605: new java/lang/StringBuilder
    //   608: astore #6
    //   610: aload_3
    //   611: astore #4
    //   613: aload_3
    //   614: astore #5
    //   616: aload #6
    //   618: invokespecial <init> : ()V
    //   621: aload_3
    //   622: astore #4
    //   624: aload_3
    //   625: astore #5
    //   627: aload_2
    //   628: ldc_w 'RANGE'
    //   631: aload #6
    //   633: ldc_w 'bytes='
    //   636: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   639: lload #7
    //   641: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   644: ldc_w '-'
    //   647: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   650: invokevirtual toString : ()Ljava/lang/String;
    //   653: invokevirtual setHeader : (Ljava/lang/String;Ljava/lang/String;)V
    //   656: aload_3
    //   657: astore #4
    //   659: aload_3
    //   660: astore #5
    //   662: aload_0
    //   663: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   666: ifnull -> 732
    //   669: aload_3
    //   670: astore #4
    //   672: aload_3
    //   673: astore #5
    //   675: aload_0
    //   676: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   679: lconst_0
    //   680: lconst_0
    //   681: iconst_0
    //   682: invokeinterface updateProgress : (JJZ)Z
    //   687: ifne -> 732
    //   690: aload_3
    //   691: astore #4
    //   693: aload_3
    //   694: astore #5
    //   696: new org/xutils/common/Callback$CancelledException
    //   699: astore_2
    //   700: aload_3
    //   701: astore #4
    //   703: aload_3
    //   704: astore #5
    //   706: aload_2
    //   707: ldc_w 'download stopped!'
    //   710: invokespecial <init> : (Ljava/lang/String;)V
    //   713: aload_3
    //   714: astore #4
    //   716: aload_3
    //   717: astore #5
    //   719: aload_2
    //   720: athrow
    //   721: lload #7
    //   723: ldc2_w 512
    //   726: lsub
    //   727: lstore #7
    //   729: goto -> 588
    //   732: aload_3
    //   733: astore #4
    //   735: aload_3
    //   736: astore #5
    //   738: aload_1
    //   739: invokevirtual sendRequest : ()V
    //   742: aload_3
    //   743: astore #4
    //   745: aload_3
    //   746: astore #5
    //   748: aload_0
    //   749: aload_1
    //   750: invokevirtual getContentLength : ()J
    //   753: putfield contentLength : J
    //   756: aload_3
    //   757: astore #4
    //   759: aload_3
    //   760: astore #5
    //   762: aload_0
    //   763: getfield isAutoRename : Z
    //   766: ifeq -> 783
    //   769: aload_3
    //   770: astore #4
    //   772: aload_3
    //   773: astore #5
    //   775: aload_0
    //   776: aload_1
    //   777: invokestatic getResponseFileName : (Lorg/xutils/http/request/UriRequest;)Ljava/lang/String;
    //   780: putfield responseFileName : Ljava/lang/String;
    //   783: aload_3
    //   784: astore #4
    //   786: aload_3
    //   787: astore #5
    //   789: aload_0
    //   790: getfield isAutoResume : Z
    //   793: ifeq -> 810
    //   796: aload_3
    //   797: astore #4
    //   799: aload_3
    //   800: astore #5
    //   802: aload_0
    //   803: aload_1
    //   804: invokestatic isSupportRange : (Lorg/xutils/http/request/UriRequest;)Z
    //   807: putfield isAutoResume : Z
    //   810: aload_3
    //   811: astore #4
    //   813: aload_3
    //   814: astore #5
    //   816: aload_0
    //   817: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   820: ifnull -> 875
    //   823: aload_3
    //   824: astore #4
    //   826: aload_3
    //   827: astore #5
    //   829: aload_0
    //   830: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   833: lconst_0
    //   834: lconst_0
    //   835: iconst_0
    //   836: invokeinterface updateProgress : (JJZ)Z
    //   841: ifne -> 875
    //   844: aload_3
    //   845: astore #4
    //   847: aload_3
    //   848: astore #5
    //   850: new org/xutils/common/Callback$CancelledException
    //   853: astore_2
    //   854: aload_3
    //   855: astore #4
    //   857: aload_3
    //   858: astore #5
    //   860: aload_2
    //   861: ldc_w 'download stopped!'
    //   864: invokespecial <init> : (Ljava/lang/String;)V
    //   867: aload_3
    //   868: astore #4
    //   870: aload_3
    //   871: astore #5
    //   873: aload_2
    //   874: athrow
    //   875: aload_3
    //   876: astore #4
    //   878: aload_3
    //   879: astore #5
    //   881: aload_0
    //   882: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   885: ifnull -> 981
    //   888: aload_3
    //   889: astore #4
    //   891: aload_3
    //   892: astore #5
    //   894: aload_0
    //   895: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   898: invokevirtual getCacheEntity : ()Lorg/xutils/cache/DiskCacheEntity;
    //   901: astore_2
    //   902: aload_3
    //   903: astore #4
    //   905: aload_3
    //   906: astore #5
    //   908: aload_2
    //   909: invokestatic currentTimeMillis : ()J
    //   912: invokevirtual setLastAccess : (J)V
    //   915: aload_3
    //   916: astore #4
    //   918: aload_3
    //   919: astore #5
    //   921: aload_2
    //   922: aload_1
    //   923: invokevirtual getETag : ()Ljava/lang/String;
    //   926: invokevirtual setEtag : (Ljava/lang/String;)V
    //   929: aload_3
    //   930: astore #4
    //   932: aload_3
    //   933: astore #5
    //   935: aload_2
    //   936: aload_1
    //   937: invokevirtual getExpiration : ()J
    //   940: invokevirtual setExpires : (J)V
    //   943: aload_3
    //   944: astore #4
    //   946: aload_3
    //   947: astore #5
    //   949: new java/util/Date
    //   952: astore #6
    //   954: aload_3
    //   955: astore #4
    //   957: aload_3
    //   958: astore #5
    //   960: aload #6
    //   962: aload_1
    //   963: invokevirtual getLastModified : ()J
    //   966: invokespecial <init> : (J)V
    //   969: aload_3
    //   970: astore #4
    //   972: aload_3
    //   973: astore #5
    //   975: aload_2
    //   976: aload #6
    //   978: invokevirtual setLastModify : (Ljava/util/Date;)V
    //   981: aload_3
    //   982: astore #4
    //   984: aload_3
    //   985: astore #5
    //   987: aload_0
    //   988: aload_1
    //   989: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   992: invokevirtual load : (Ljava/io/InputStream;)Ljava/io/File;
    //   995: astore_2
    //   996: aload_2
    //   997: astore_1
    //   998: aload_3
    //   999: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   1002: aload_0
    //   1003: getfield diskCacheFile : Lorg/xutils/cache/DiskCacheFile;
    //   1006: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   1009: goto -> 214
    //   1012: aload #4
    //   1014: astore #5
    //   1016: new java/io/File
    //   1019: dup
    //   1020: aload_0
    //   1021: getfield tempSaveFilePath : Ljava/lang/String;
    //   1024: invokespecial <init> : (Ljava/lang/String;)V
    //   1027: astore_3
    //   1028: goto -> 154
    //   1031: aload #4
    //   1033: astore #5
    //   1035: aload_3
    //   1036: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   1039: pop
    //   1040: aload #4
    //   1042: astore #5
    //   1044: new java/lang/IllegalStateException
    //   1047: astore_3
    //   1048: aload #4
    //   1050: astore #5
    //   1052: new java/lang/StringBuilder
    //   1055: astore_2
    //   1056: aload #4
    //   1058: astore #5
    //   1060: aload_2
    //   1061: invokespecial <init> : ()V
    //   1064: aload #4
    //   1066: astore #5
    //   1068: aload_3
    //   1069: aload_2
    //   1070: ldc_w 'cache file not found'
    //   1073: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1076: aload_1
    //   1077: invokevirtual getCacheKey : ()Ljava/lang/String;
    //   1080: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1083: invokevirtual toString : ()Ljava/lang/String;
    //   1086: invokespecial <init> : (Ljava/lang/String;)V
    //   1089: aload #4
    //   1091: astore #5
    //   1093: aload_3
    //   1094: athrow
    //   1095: aload #4
    //   1097: astore #5
    //   1099: aload_3
    //   1100: athrow
    // Exception table:
    //   from	to	target	type
    //   10	21	116	org/xutils/ex/HttpException
    //   10	21	295	finally
    //   27	32	116	org/xutils/ex/HttpException
    //   27	32	295	finally
    //   38	48	116	org/xutils/ex/HttpException
    //   38	48	295	finally
    //   54	61	116	org/xutils/ex/HttpException
    //   54	61	295	finally
    //   67	82	116	org/xutils/ex/HttpException
    //   67	82	295	finally
    //   88	93	116	org/xutils/ex/HttpException
    //   88	93	295	finally
    //   99	107	116	org/xutils/ex/HttpException
    //   99	107	295	finally
    //   113	116	116	org/xutils/ex/HttpException
    //   113	116	295	finally
    //   121	131	295	finally
    //   135	142	295	finally
    //   146	154	295	finally
    //   162	169	295	finally
    //   173	180	295	finally
    //   184	192	295	finally
    //   196	202	295	finally
    //   222	227	116	org/xutils/ex/HttpException
    //   222	227	295	finally
    //   233	240	116	org/xutils/ex/HttpException
    //   233	240	295	finally
    //   246	261	116	org/xutils/ex/HttpException
    //   246	261	295	finally
    //   267	272	116	org/xutils/ex/HttpException
    //   267	272	295	finally
    //   278	286	116	org/xutils/ex/HttpException
    //   278	286	295	finally
    //   292	295	116	org/xutils/ex/HttpException
    //   292	295	295	finally
    //   316	321	116	org/xutils/ex/HttpException
    //   316	321	295	finally
    //   327	332	116	org/xutils/ex/HttpException
    //   327	332	295	finally
    //   338	360	116	org/xutils/ex/HttpException
    //   338	360	295	finally
    //   369	374	116	org/xutils/ex/HttpException
    //   369	374	295	finally
    //   380	385	116	org/xutils/ex/HttpException
    //   380	385	295	finally
    //   391	414	116	org/xutils/ex/HttpException
    //   391	414	295	finally
    //   424	431	116	org/xutils/ex/HttpException
    //   424	431	295	finally
    //   437	441	116	org/xutils/ex/HttpException
    //   437	441	295	finally
    //   447	452	116	org/xutils/ex/HttpException
    //   447	452	295	finally
    //   458	463	116	org/xutils/ex/HttpException
    //   458	463	295	finally
    //   469	491	116	org/xutils/ex/HttpException
    //   469	491	295	finally
    //   497	499	116	org/xutils/ex/HttpException
    //   497	499	295	finally
    //   505	513	116	org/xutils/ex/HttpException
    //   505	513	295	finally
    //   522	529	116	org/xutils/ex/HttpException
    //   522	529	295	finally
    //   535	539	116	org/xutils/ex/HttpException
    //   535	539	295	finally
    //   545	553	116	org/xutils/ex/HttpException
    //   545	553	295	finally
    //   559	565	116	org/xutils/ex/HttpException
    //   559	565	295	finally
    //   580	585	116	org/xutils/ex/HttpException
    //   580	585	295	finally
    //   594	599	116	org/xutils/ex/HttpException
    //   594	599	295	finally
    //   605	610	116	org/xutils/ex/HttpException
    //   605	610	295	finally
    //   616	621	116	org/xutils/ex/HttpException
    //   616	621	295	finally
    //   627	656	116	org/xutils/ex/HttpException
    //   627	656	295	finally
    //   662	669	116	org/xutils/ex/HttpException
    //   662	669	295	finally
    //   675	690	116	org/xutils/ex/HttpException
    //   675	690	295	finally
    //   696	700	116	org/xutils/ex/HttpException
    //   696	700	295	finally
    //   706	713	116	org/xutils/ex/HttpException
    //   706	713	295	finally
    //   719	721	116	org/xutils/ex/HttpException
    //   719	721	295	finally
    //   738	742	116	org/xutils/ex/HttpException
    //   738	742	295	finally
    //   748	756	116	org/xutils/ex/HttpException
    //   748	756	295	finally
    //   762	769	116	org/xutils/ex/HttpException
    //   762	769	295	finally
    //   775	783	116	org/xutils/ex/HttpException
    //   775	783	295	finally
    //   789	796	116	org/xutils/ex/HttpException
    //   789	796	295	finally
    //   802	810	116	org/xutils/ex/HttpException
    //   802	810	295	finally
    //   816	823	116	org/xutils/ex/HttpException
    //   816	823	295	finally
    //   829	844	116	org/xutils/ex/HttpException
    //   829	844	295	finally
    //   850	854	116	org/xutils/ex/HttpException
    //   850	854	295	finally
    //   860	867	116	org/xutils/ex/HttpException
    //   860	867	295	finally
    //   873	875	116	org/xutils/ex/HttpException
    //   873	875	295	finally
    //   881	888	116	org/xutils/ex/HttpException
    //   881	888	295	finally
    //   894	902	116	org/xutils/ex/HttpException
    //   894	902	295	finally
    //   908	915	116	org/xutils/ex/HttpException
    //   908	915	295	finally
    //   921	929	116	org/xutils/ex/HttpException
    //   921	929	295	finally
    //   935	943	116	org/xutils/ex/HttpException
    //   935	943	295	finally
    //   949	954	116	org/xutils/ex/HttpException
    //   949	954	295	finally
    //   960	969	116	org/xutils/ex/HttpException
    //   960	969	295	finally
    //   975	981	116	org/xutils/ex/HttpException
    //   975	981	295	finally
    //   987	996	116	org/xutils/ex/HttpException
    //   987	996	295	finally
    //   1016	1028	295	finally
    //   1035	1040	295	finally
    //   1044	1048	295	finally
    //   1052	1056	295	finally
    //   1060	1064	295	finally
    //   1068	1089	295	finally
    //   1093	1095	295	finally
    //   1099	1101	295	finally
  }
  
  public File loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    return (File)LruDiskCache.getDiskCache(this.params.getCacheDirName()).getDiskCacheFile(paramDiskCacheEntity.getKey());
  }
  
  public Loader<File> newInstance() {
    return new FileLoader();
  }
  
  public void save2Cache(UriRequest paramUriRequest) {}
  
  public void setParams(RequestParams paramRequestParams) {
    if (paramRequestParams != null) {
      this.params = paramRequestParams;
      this.isAutoResume = paramRequestParams.isAutoResume();
      this.isAutoRename = paramRequestParams.isAutoRename();
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/FileLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */