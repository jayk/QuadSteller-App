package org.xutils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.cache.DiskCacheFile;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;

public final class ImageDecoder {
  private static final int BITMAP_DECODE_MAX_WORKER;
  
  private static final byte[] GIF_HEADER;
  
  private static final LruDiskCache THUMB_CACHE;
  
  private static final Executor THUMB_CACHE_EXECUTOR;
  
  private static final Object bitmapDecodeLock;
  
  private static final AtomicInteger bitmapDecodeWorker = new AtomicInteger(0);
  
  private static final Object gifDecodeLock;
  
  private static final boolean supportWebP;
  
  static {
    bitmapDecodeLock = new Object();
    gifDecodeLock = new Object();
    GIF_HEADER = new byte[] { 71, 73, 70 };
    THUMB_CACHE_EXECUTOR = (Executor)new PriorityExecutor(1, true);
    THUMB_CACHE = LruDiskCache.getDiskCache("xUtils_img_thumb");
    if (Build.VERSION.SDK_INT > 16)
      bool = true; 
    supportWebP = bool;
    if (Runtime.getRuntime().availableProcessors() > 4)
      b = 2; 
    BITMAP_DECODE_MAX_WORKER = b;
  }
  
  public static int calculateSampleSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = 1;
    if (paramInt1 > paramInt3 || paramInt2 > paramInt4) {
      if (paramInt1 > paramInt2) {
        i = Math.round(paramInt2 / paramInt4);
      } else {
        i = Math.round(paramInt1 / paramInt3);
      } 
      int j = i;
      if (i < 1)
        j = 1; 
      float f1 = (paramInt1 * paramInt2);
      float f2 = (paramInt3 * paramInt4 * 2);
      while (true) {
        i = j;
        if (f1 / (j * j) > f2) {
          j++;
          continue;
        } 
        break;
      } 
    } 
    return i;
  }
  
  static void clearCacheFiles() {
    THUMB_CACHE.clearCacheFiles();
  }
  
  public static Bitmap cut2Circular(Bitmap paramBitmap, boolean paramBoolean) {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int k = Math.min(i, j);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    Bitmap bitmap = Bitmap.createBitmap(k, k, Bitmap.Config.ARGB_8888);
    if (bitmap != null) {
      Canvas canvas = new Canvas(bitmap);
      canvas.drawCircle((k / 2), (k / 2), (k / 2), paint);
      paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      canvas.drawBitmap(paramBitmap, ((k - i) / 2), ((k - j) / 2), paint);
      Bitmap bitmap1 = bitmap;
      if (paramBoolean) {
        paramBitmap.recycle();
        bitmap1 = bitmap;
      } 
      return bitmap1;
    } 
    return paramBitmap;
  }
  
  public static Bitmap cut2RoundCorner(Bitmap paramBitmap, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramInt > 0) {
      Bitmap bitmap2;
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      int k = i;
      int m = j;
      if (paramBoolean1) {
        m = Math.min(i, j);
        k = m;
      } 
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      Bitmap bitmap1 = Bitmap.createBitmap(k, m, Bitmap.Config.ARGB_8888);
      if (bitmap1 != null) {
        Canvas canvas = new Canvas(bitmap1);
        canvas.drawRoundRect(new RectF(0.0F, 0.0F, k, m), paramInt, paramInt, paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(paramBitmap, ((k - i) / 2), ((m - j) / 2), paint);
        bitmap2 = bitmap1;
        if (paramBoolean2) {
          paramBitmap.recycle();
          bitmap2 = bitmap1;
        } 
      } else {
        bitmap2 = paramBitmap;
      } 
      paramBitmap = bitmap2;
    } 
    return paramBitmap;
  }
  
  public static Bitmap cut2ScaleSize(Bitmap paramBitmap, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if (i != paramInt1 || j != paramInt2) {
      Bitmap bitmap1;
      Matrix matrix = new Matrix();
      float f1 = paramInt1 / i;
      float f2 = paramInt2 / j;
      if (f1 > f2) {
        f2 = f1;
        boolean bool = false;
        paramInt1 = (int)((j - paramInt2 / f1) / 2.0F);
        j = (int)((j + paramInt2 / f1) / 2.0F);
        paramInt2 = bool;
      } else {
        f1 = f2;
        paramInt2 = (int)((i - paramInt1 / f1) / 2.0F);
        i = (int)((i + paramInt1 / f1) / 2.0F);
        paramInt1 = 0;
      } 
      matrix.setScale(f1, f2);
      Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, paramInt2, paramInt1, i - paramInt2, j - paramInt1, matrix, true);
      if (bitmap2 != null) {
        bitmap1 = bitmap2;
        if (paramBoolean) {
          bitmap1 = bitmap2;
          if (bitmap2 != paramBitmap) {
            paramBitmap.recycle();
            bitmap1 = bitmap2;
          } 
        } 
      } else {
        bitmap1 = paramBitmap;
      } 
      paramBitmap = bitmap1;
    } 
    return paramBitmap;
  }
  
  public static Bitmap cut2Square(Bitmap paramBitmap, boolean paramBoolean) {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if (i != j) {
      Bitmap bitmap2;
      int k = Math.min(i, j);
      Bitmap bitmap1 = Bitmap.createBitmap(paramBitmap, (i - k) / 2, (j - k) / 2, k, k);
      if (bitmap1 != null) {
        bitmap2 = bitmap1;
        if (paramBoolean) {
          bitmap2 = bitmap1;
          if (bitmap1 != paramBitmap) {
            paramBitmap.recycle();
            bitmap2 = bitmap1;
          } 
        } 
      } else {
        bitmap2 = paramBitmap;
      } 
      paramBitmap = bitmap2;
    } 
    return paramBitmap;
  }
  
  public static Bitmap decodeBitmap(File paramFile, ImageOptions paramImageOptions, Callback.Cancelable paramCancelable) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 20
    //   4: aload_0
    //   5: invokevirtual exists : ()Z
    //   8: ifeq -> 20
    //   11: aload_0
    //   12: invokevirtual length : ()J
    //   15: lconst_1
    //   16: lcmp
    //   17: ifge -> 24
    //   20: aconst_null
    //   21: astore_0
    //   22: aload_0
    //   23: areturn
    //   24: aload_1
    //   25: astore_3
    //   26: aload_1
    //   27: ifnonnull -> 34
    //   30: getstatic org/xutils/image/ImageOptions.DEFAULT : Lorg/xutils/image/ImageOptions;
    //   33: astore_3
    //   34: aload_3
    //   35: invokevirtual getMaxWidth : ()I
    //   38: ifle -> 48
    //   41: aload_3
    //   42: invokevirtual getMaxHeight : ()I
    //   45: ifgt -> 53
    //   48: aload_3
    //   49: aconst_null
    //   50: invokevirtual optimizeMaxSize : (Landroid/widget/ImageView;)V
    //   53: aload_2
    //   54: ifnull -> 81
    //   57: aload_2
    //   58: invokeinterface isCancelled : ()Z
    //   63: ifeq -> 81
    //   66: new org/xutils/common/Callback$CancelledException
    //   69: astore_0
    //   70: aload_0
    //   71: ldc 'cancelled during decode image'
    //   73: invokespecial <init> : (Ljava/lang/String;)V
    //   76: aload_0
    //   77: athrow
    //   78: astore_0
    //   79: aload_0
    //   80: athrow
    //   81: new android/graphics/BitmapFactory$Options
    //   84: astore_1
    //   85: aload_1
    //   86: invokespecial <init> : ()V
    //   89: aload_1
    //   90: iconst_1
    //   91: putfield inJustDecodeBounds : Z
    //   94: aload_1
    //   95: iconst_1
    //   96: putfield inPurgeable : Z
    //   99: aload_1
    //   100: iconst_1
    //   101: putfield inInputShareable : Z
    //   104: aload_0
    //   105: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   108: aload_1
    //   109: invokestatic decodeFile : (Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   112: pop
    //   113: aload_1
    //   114: iconst_0
    //   115: putfield inJustDecodeBounds : Z
    //   118: aload_1
    //   119: aload_3
    //   120: invokevirtual getConfig : ()Landroid/graphics/Bitmap$Config;
    //   123: putfield inPreferredConfig : Landroid/graphics/Bitmap$Config;
    //   126: iconst_0
    //   127: istore #4
    //   129: aload_1
    //   130: getfield outWidth : I
    //   133: istore #5
    //   135: aload_1
    //   136: getfield outHeight : I
    //   139: istore #6
    //   141: aload_3
    //   142: invokevirtual getWidth : ()I
    //   145: istore #7
    //   147: aload_3
    //   148: invokevirtual getHeight : ()I
    //   151: istore #8
    //   153: iload #6
    //   155: istore #9
    //   157: iload #5
    //   159: istore #10
    //   161: aload_3
    //   162: invokevirtual isAutoRotate : ()Z
    //   165: ifeq -> 216
    //   168: aload_0
    //   169: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   172: invokestatic getRotateAngle : (Ljava/lang/String;)I
    //   175: istore #11
    //   177: iload #6
    //   179: istore #9
    //   181: iload #5
    //   183: istore #10
    //   185: iload #11
    //   187: istore #4
    //   189: iload #11
    //   191: bipush #90
    //   193: idiv
    //   194: iconst_2
    //   195: irem
    //   196: iconst_1
    //   197: if_icmpne -> 216
    //   200: aload_1
    //   201: getfield outHeight : I
    //   204: istore #10
    //   206: aload_1
    //   207: getfield outWidth : I
    //   210: istore #9
    //   212: iload #11
    //   214: istore #4
    //   216: aload_3
    //   217: invokevirtual isCrop : ()Z
    //   220: ifne -> 256
    //   223: iload #7
    //   225: ifle -> 256
    //   228: iload #8
    //   230: ifle -> 256
    //   233: iload #4
    //   235: bipush #90
    //   237: idiv
    //   238: iconst_2
    //   239: irem
    //   240: iconst_1
    //   241: if_icmpne -> 314
    //   244: aload_1
    //   245: iload #8
    //   247: putfield outWidth : I
    //   250: aload_1
    //   251: iload #7
    //   253: putfield outHeight : I
    //   256: aload_1
    //   257: iload #10
    //   259: iload #9
    //   261: aload_3
    //   262: invokevirtual getMaxWidth : ()I
    //   265: aload_3
    //   266: invokevirtual getMaxHeight : ()I
    //   269: invokestatic calculateSampleSize : (IIII)I
    //   272: putfield inSampleSize : I
    //   275: aload_2
    //   276: ifnull -> 329
    //   279: aload_2
    //   280: invokeinterface isCancelled : ()Z
    //   285: ifeq -> 329
    //   288: new org/xutils/common/Callback$CancelledException
    //   291: astore_0
    //   292: aload_0
    //   293: ldc 'cancelled during decode image'
    //   295: invokespecial <init> : (Ljava/lang/String;)V
    //   298: aload_0
    //   299: athrow
    //   300: astore_0
    //   301: aload_0
    //   302: invokevirtual getMessage : ()Ljava/lang/String;
    //   305: aload_0
    //   306: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   309: aconst_null
    //   310: astore_0
    //   311: goto -> 22
    //   314: aload_1
    //   315: iload #7
    //   317: putfield outWidth : I
    //   320: aload_1
    //   321: iload #8
    //   323: putfield outHeight : I
    //   326: goto -> 256
    //   329: aload_0
    //   330: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   333: aload_1
    //   334: invokestatic decodeFile : (Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   337: astore_1
    //   338: aload_1
    //   339: ifnonnull -> 355
    //   342: new java/io/IOException
    //   345: astore_0
    //   346: aload_0
    //   347: ldc_w 'decode image error'
    //   350: invokespecial <init> : (Ljava/lang/String;)V
    //   353: aload_0
    //   354: athrow
    //   355: aload_2
    //   356: ifnull -> 380
    //   359: aload_2
    //   360: invokeinterface isCancelled : ()Z
    //   365: ifeq -> 380
    //   368: new org/xutils/common/Callback$CancelledException
    //   371: astore_0
    //   372: aload_0
    //   373: ldc 'cancelled during decode image'
    //   375: invokespecial <init> : (Ljava/lang/String;)V
    //   378: aload_0
    //   379: athrow
    //   380: aload_1
    //   381: astore_0
    //   382: iload #4
    //   384: ifeq -> 395
    //   387: aload_1
    //   388: iload #4
    //   390: iconst_1
    //   391: invokestatic rotate : (Landroid/graphics/Bitmap;IZ)Landroid/graphics/Bitmap;
    //   394: astore_0
    //   395: aload_2
    //   396: ifnull -> 420
    //   399: aload_2
    //   400: invokeinterface isCancelled : ()Z
    //   405: ifeq -> 420
    //   408: new org/xutils/common/Callback$CancelledException
    //   411: astore_0
    //   412: aload_0
    //   413: ldc 'cancelled during decode image'
    //   415: invokespecial <init> : (Ljava/lang/String;)V
    //   418: aload_0
    //   419: athrow
    //   420: aload_0
    //   421: astore_1
    //   422: aload_3
    //   423: invokevirtual isCrop : ()Z
    //   426: ifeq -> 453
    //   429: aload_0
    //   430: astore_1
    //   431: iload #7
    //   433: ifle -> 453
    //   436: aload_0
    //   437: astore_1
    //   438: iload #8
    //   440: ifle -> 453
    //   443: aload_0
    //   444: iload #7
    //   446: iload #8
    //   448: iconst_1
    //   449: invokestatic cut2ScaleSize : (Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   452: astore_1
    //   453: aload_1
    //   454: ifnonnull -> 470
    //   457: new java/io/IOException
    //   460: astore_0
    //   461: aload_0
    //   462: ldc_w 'decode image error'
    //   465: invokespecial <init> : (Ljava/lang/String;)V
    //   468: aload_0
    //   469: athrow
    //   470: aload_2
    //   471: ifnull -> 495
    //   474: aload_2
    //   475: invokeinterface isCancelled : ()Z
    //   480: ifeq -> 495
    //   483: new org/xutils/common/Callback$CancelledException
    //   486: astore_0
    //   487: aload_0
    //   488: ldc 'cancelled during decode image'
    //   490: invokespecial <init> : (Ljava/lang/String;)V
    //   493: aload_0
    //   494: athrow
    //   495: aload_3
    //   496: invokevirtual isCircular : ()Z
    //   499: ifeq -> 525
    //   502: aload_1
    //   503: iconst_1
    //   504: invokestatic cut2Circular : (Landroid/graphics/Bitmap;Z)Landroid/graphics/Bitmap;
    //   507: astore_0
    //   508: aload_0
    //   509: ifnonnull -> 567
    //   512: new java/io/IOException
    //   515: astore_0
    //   516: aload_0
    //   517: ldc_w 'decode image error'
    //   520: invokespecial <init> : (Ljava/lang/String;)V
    //   523: aload_0
    //   524: athrow
    //   525: aload_3
    //   526: invokevirtual getRadius : ()I
    //   529: ifle -> 549
    //   532: aload_1
    //   533: aload_3
    //   534: invokevirtual getRadius : ()I
    //   537: aload_3
    //   538: invokevirtual isSquare : ()Z
    //   541: iconst_1
    //   542: invokestatic cut2RoundCorner : (Landroid/graphics/Bitmap;IZZ)Landroid/graphics/Bitmap;
    //   545: astore_0
    //   546: goto -> 508
    //   549: aload_1
    //   550: astore_0
    //   551: aload_3
    //   552: invokevirtual isSquare : ()Z
    //   555: ifeq -> 508
    //   558: aload_1
    //   559: iconst_1
    //   560: invokestatic cut2Square : (Landroid/graphics/Bitmap;Z)Landroid/graphics/Bitmap;
    //   563: astore_0
    //   564: goto -> 508
    //   567: goto -> 22
    // Exception table:
    //   from	to	target	type
    //   57	78	78	java/io/IOException
    //   57	78	300	java/lang/Throwable
    //   81	126	78	java/io/IOException
    //   81	126	300	java/lang/Throwable
    //   129	153	78	java/io/IOException
    //   129	153	300	java/lang/Throwable
    //   161	177	78	java/io/IOException
    //   161	177	300	java/lang/Throwable
    //   189	212	78	java/io/IOException
    //   189	212	300	java/lang/Throwable
    //   216	223	78	java/io/IOException
    //   216	223	300	java/lang/Throwable
    //   233	256	78	java/io/IOException
    //   233	256	300	java/lang/Throwable
    //   256	275	78	java/io/IOException
    //   256	275	300	java/lang/Throwable
    //   279	300	78	java/io/IOException
    //   279	300	300	java/lang/Throwable
    //   314	326	78	java/io/IOException
    //   314	326	300	java/lang/Throwable
    //   329	338	78	java/io/IOException
    //   329	338	300	java/lang/Throwable
    //   342	355	78	java/io/IOException
    //   342	355	300	java/lang/Throwable
    //   359	380	78	java/io/IOException
    //   359	380	300	java/lang/Throwable
    //   387	395	78	java/io/IOException
    //   387	395	300	java/lang/Throwable
    //   399	420	78	java/io/IOException
    //   399	420	300	java/lang/Throwable
    //   422	429	78	java/io/IOException
    //   422	429	300	java/lang/Throwable
    //   443	453	78	java/io/IOException
    //   443	453	300	java/lang/Throwable
    //   457	470	78	java/io/IOException
    //   457	470	300	java/lang/Throwable
    //   474	495	78	java/io/IOException
    //   474	495	300	java/lang/Throwable
    //   495	508	78	java/io/IOException
    //   495	508	300	java/lang/Throwable
    //   512	525	78	java/io/IOException
    //   512	525	300	java/lang/Throwable
    //   525	546	78	java/io/IOException
    //   525	546	300	java/lang/Throwable
    //   551	564	78	java/io/IOException
    //   551	564	300	java/lang/Throwable
  }
  
  static Drawable decodeFileWithLock(File paramFile, ImageOptions paramImageOptions, Callback.Cancelable paramCancelable) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 20
    //   4: aload_0
    //   5: invokevirtual exists : ()Z
    //   8: ifeq -> 20
    //   11: aload_0
    //   12: invokevirtual length : ()J
    //   15: lconst_1
    //   16: lcmp
    //   17: ifge -> 24
    //   20: aconst_null
    //   21: astore_1
    //   22: aload_1
    //   23: areturn
    //   24: aload_2
    //   25: ifnull -> 47
    //   28: aload_2
    //   29: invokeinterface isCancelled : ()Z
    //   34: ifeq -> 47
    //   37: new org/xutils/common/Callback$CancelledException
    //   40: dup
    //   41: ldc 'cancelled during decode image'
    //   43: invokespecial <init> : (Ljava/lang/String;)V
    //   46: athrow
    //   47: aconst_null
    //   48: astore_3
    //   49: aload_1
    //   50: invokevirtual isIgnoreGif : ()Z
    //   53: ifne -> 110
    //   56: aload_0
    //   57: invokestatic isGif : (Ljava/io/File;)Z
    //   60: ifeq -> 110
    //   63: getstatic org/xutils/image/ImageDecoder.gifDecodeLock : Ljava/lang/Object;
    //   66: astore #4
    //   68: aload #4
    //   70: monitorenter
    //   71: aload_0
    //   72: aload_1
    //   73: aload_2
    //   74: invokestatic decodeGif : (Ljava/io/File;Lorg/xutils/image/ImageOptions;Lorg/xutils/common/Callback$Cancelable;)Landroid/graphics/Movie;
    //   77: astore_2
    //   78: aload #4
    //   80: monitorexit
    //   81: aload_3
    //   82: astore_1
    //   83: aload_2
    //   84: ifnull -> 22
    //   87: new org/xutils/image/GifDrawable
    //   90: dup
    //   91: aload_2
    //   92: aload_0
    //   93: invokevirtual length : ()J
    //   96: l2i
    //   97: invokespecial <init> : (Landroid/graphics/Movie;I)V
    //   100: astore_1
    //   101: goto -> 22
    //   104: astore_0
    //   105: aload #4
    //   107: monitorexit
    //   108: aload_0
    //   109: athrow
    //   110: aconst_null
    //   111: astore #4
    //   113: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   116: astore #5
    //   118: aload #5
    //   120: monitorenter
    //   121: getstatic org/xutils/image/ImageDecoder.bitmapDecodeWorker : Ljava/util/concurrent/atomic/AtomicInteger;
    //   124: invokevirtual get : ()I
    //   127: getstatic org/xutils/image/ImageDecoder.BITMAP_DECODE_MAX_WORKER : I
    //   130: if_icmplt -> 202
    //   133: aload_2
    //   134: ifnull -> 150
    //   137: aload_2
    //   138: invokeinterface isCancelled : ()Z
    //   143: istore #6
    //   145: iload #6
    //   147: ifne -> 202
    //   150: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   153: invokevirtual wait : ()V
    //   156: goto -> 121
    //   159: astore_0
    //   160: new org/xutils/common/Callback$CancelledException
    //   163: astore_0
    //   164: aload_0
    //   165: ldc 'cancelled during decode image'
    //   167: invokespecial <init> : (Ljava/lang/String;)V
    //   170: aload_0
    //   171: athrow
    //   172: astore_0
    //   173: aload #5
    //   175: monitorexit
    //   176: aload_0
    //   177: athrow
    //   178: astore_1
    //   179: getstatic org/xutils/image/ImageDecoder.bitmapDecodeWorker : Ljava/util/concurrent/atomic/AtomicInteger;
    //   182: invokevirtual decrementAndGet : ()I
    //   185: pop
    //   186: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   189: astore_0
    //   190: aload_0
    //   191: monitorenter
    //   192: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   195: invokevirtual notifyAll : ()V
    //   198: aload_0
    //   199: monitorexit
    //   200: aload_1
    //   201: athrow
    //   202: aload #5
    //   204: monitorexit
    //   205: aload_2
    //   206: ifnull -> 230
    //   209: aload_2
    //   210: invokeinterface isCancelled : ()Z
    //   215: ifeq -> 230
    //   218: new org/xutils/common/Callback$CancelledException
    //   221: astore_0
    //   222: aload_0
    //   223: ldc 'cancelled during decode image'
    //   225: invokespecial <init> : (Ljava/lang/String;)V
    //   228: aload_0
    //   229: athrow
    //   230: getstatic org/xutils/image/ImageDecoder.bitmapDecodeWorker : Ljava/util/concurrent/atomic/AtomicInteger;
    //   233: invokevirtual incrementAndGet : ()I
    //   236: pop
    //   237: aload_1
    //   238: invokevirtual isCompress : ()Z
    //   241: ifeq -> 251
    //   244: aload_0
    //   245: aload_1
    //   246: invokestatic getThumbCache : (Ljava/io/File;Lorg/xutils/image/ImageOptions;)Landroid/graphics/Bitmap;
    //   249: astore #4
    //   251: aload #4
    //   253: astore #5
    //   255: aload #4
    //   257: ifnonnull -> 314
    //   260: aload_0
    //   261: aload_1
    //   262: aload_2
    //   263: invokestatic decodeBitmap : (Ljava/io/File;Lorg/xutils/image/ImageOptions;Lorg/xutils/common/Callback$Cancelable;)Landroid/graphics/Bitmap;
    //   266: astore_2
    //   267: aload_2
    //   268: astore #5
    //   270: aload_2
    //   271: ifnull -> 314
    //   274: aload_2
    //   275: astore #5
    //   277: aload_1
    //   278: invokevirtual isCompress : ()Z
    //   281: ifeq -> 314
    //   284: getstatic org/xutils/image/ImageDecoder.THUMB_CACHE_EXECUTOR : Ljava/util/concurrent/Executor;
    //   287: astore #4
    //   289: new org/xutils/image/ImageDecoder$1
    //   292: astore #5
    //   294: aload #5
    //   296: aload_0
    //   297: aload_1
    //   298: aload_2
    //   299: invokespecial <init> : (Ljava/io/File;Lorg/xutils/image/ImageOptions;Landroid/graphics/Bitmap;)V
    //   302: aload #4
    //   304: aload #5
    //   306: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   311: aload_2
    //   312: astore #5
    //   314: getstatic org/xutils/image/ImageDecoder.bitmapDecodeWorker : Ljava/util/concurrent/atomic/AtomicInteger;
    //   317: invokevirtual decrementAndGet : ()I
    //   320: pop
    //   321: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   324: astore_0
    //   325: aload_0
    //   326: monitorenter
    //   327: getstatic org/xutils/image/ImageDecoder.bitmapDecodeLock : Ljava/lang/Object;
    //   330: invokevirtual notifyAll : ()V
    //   333: aload_0
    //   334: monitorexit
    //   335: aload_3
    //   336: astore_1
    //   337: aload #5
    //   339: ifnull -> 22
    //   342: new org/xutils/image/ReusableBitmapDrawable
    //   345: dup
    //   346: invokestatic app : ()Landroid/app/Application;
    //   349: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   352: aload #5
    //   354: invokespecial <init> : (Landroid/content/res/Resources;Landroid/graphics/Bitmap;)V
    //   357: astore_1
    //   358: goto -> 22
    //   361: astore_1
    //   362: aload_0
    //   363: monitorexit
    //   364: aload_1
    //   365: athrow
    //   366: astore_1
    //   367: aload_0
    //   368: monitorexit
    //   369: aload_1
    //   370: athrow
    //   371: astore #7
    //   373: goto -> 121
    // Exception table:
    //   from	to	target	type
    //   71	81	104	finally
    //   105	108	104	finally
    //   113	121	178	finally
    //   121	133	172	finally
    //   137	145	172	finally
    //   150	156	159	java/lang/InterruptedException
    //   150	156	371	java/lang/Throwable
    //   150	156	172	finally
    //   160	172	172	finally
    //   173	176	172	finally
    //   176	178	178	finally
    //   192	200	366	finally
    //   202	205	172	finally
    //   209	230	178	finally
    //   230	237	178	finally
    //   237	251	178	finally
    //   260	267	178	finally
    //   277	311	178	finally
    //   327	335	361	finally
    //   362	364	361	finally
    //   367	369	366	finally
  }
  
  public static Movie decodeGif(File paramFile, ImageOptions paramImageOptions, Callback.Cancelable paramCancelable) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 20
    //   4: aload_0
    //   5: invokevirtual exists : ()Z
    //   8: ifeq -> 20
    //   11: aload_0
    //   12: invokevirtual length : ()J
    //   15: lconst_1
    //   16: lcmp
    //   17: ifge -> 24
    //   20: aconst_null
    //   21: astore_0
    //   22: aload_0
    //   23: areturn
    //   24: aconst_null
    //   25: astore_3
    //   26: aconst_null
    //   27: astore #4
    //   29: aconst_null
    //   30: astore #5
    //   32: aload_2
    //   33: ifnull -> 80
    //   36: aload_3
    //   37: astore_1
    //   38: aload_2
    //   39: invokeinterface isCancelled : ()Z
    //   44: ifeq -> 80
    //   47: aload_3
    //   48: astore_1
    //   49: new org/xutils/common/Callback$CancelledException
    //   52: astore_0
    //   53: aload_3
    //   54: astore_1
    //   55: aload_0
    //   56: ldc 'cancelled during decode image'
    //   58: invokespecial <init> : (Ljava/lang/String;)V
    //   61: aload_3
    //   62: astore_1
    //   63: aload_0
    //   64: athrow
    //   65: astore_0
    //   66: aload #5
    //   68: astore_1
    //   69: aload_0
    //   70: athrow
    //   71: astore_2
    //   72: aload_1
    //   73: astore_0
    //   74: aload_0
    //   75: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   78: aload_2
    //   79: athrow
    //   80: aload_3
    //   81: astore_1
    //   82: new java/io/FileInputStream
    //   85: astore_2
    //   86: aload_3
    //   87: astore_1
    //   88: aload_2
    //   89: aload_0
    //   90: invokespecial <init> : (Ljava/io/File;)V
    //   93: aload_3
    //   94: astore_1
    //   95: new java/io/BufferedInputStream
    //   98: dup
    //   99: aload_2
    //   100: sipush #16384
    //   103: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   106: astore_0
    //   107: aload_0
    //   108: sipush #16384
    //   111: invokevirtual mark : (I)V
    //   114: aload_0
    //   115: invokestatic decodeStream : (Ljava/io/InputStream;)Landroid/graphics/Movie;
    //   118: astore_1
    //   119: aload_1
    //   120: ifnonnull -> 144
    //   123: new java/io/IOException
    //   126: astore_1
    //   127: aload_1
    //   128: ldc_w 'decode image error'
    //   131: invokespecial <init> : (Ljava/lang/String;)V
    //   134: aload_1
    //   135: athrow
    //   136: astore_2
    //   137: aload_0
    //   138: astore_1
    //   139: aload_2
    //   140: astore_0
    //   141: goto -> 69
    //   144: aload_0
    //   145: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   148: aload_1
    //   149: astore_0
    //   150: goto -> 22
    //   153: astore_2
    //   154: aload #4
    //   156: astore_0
    //   157: aload_0
    //   158: astore_1
    //   159: aload_2
    //   160: invokevirtual getMessage : ()Ljava/lang/String;
    //   163: aload_2
    //   164: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   167: aload_0
    //   168: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   171: aconst_null
    //   172: astore_0
    //   173: goto -> 22
    //   176: astore_1
    //   177: aload_1
    //   178: astore_2
    //   179: goto -> 74
    //   182: astore_2
    //   183: goto -> 157
    // Exception table:
    //   from	to	target	type
    //   38	47	65	java/io/IOException
    //   38	47	153	java/lang/Throwable
    //   38	47	71	finally
    //   49	53	65	java/io/IOException
    //   49	53	153	java/lang/Throwable
    //   49	53	71	finally
    //   55	61	65	java/io/IOException
    //   55	61	153	java/lang/Throwable
    //   55	61	71	finally
    //   63	65	65	java/io/IOException
    //   63	65	153	java/lang/Throwable
    //   63	65	71	finally
    //   69	71	71	finally
    //   82	86	65	java/io/IOException
    //   82	86	153	java/lang/Throwable
    //   82	86	71	finally
    //   88	93	65	java/io/IOException
    //   88	93	153	java/lang/Throwable
    //   88	93	71	finally
    //   95	107	65	java/io/IOException
    //   95	107	153	java/lang/Throwable
    //   95	107	71	finally
    //   107	119	136	java/io/IOException
    //   107	119	182	java/lang/Throwable
    //   107	119	176	finally
    //   123	136	136	java/io/IOException
    //   123	136	182	java/lang/Throwable
    //   123	136	176	finally
    //   159	167	71	finally
  }
  
  public static int getRotateAngle(String paramString) {
    char c = Character.MIN_VALUE;
    try {
      ExifInterface exifInterface = new ExifInterface();
      this(paramString);
      int i = exifInterface.getAttributeInt("Orientation", 0);
      switch (i) {
        default:
          return 0;
        case 6:
          return 90;
        case 3:
          return 180;
        case 8:
          break;
      } 
      c = 'Ď';
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return c;
  }
  
  private static Bitmap getThumbCache(File paramFile, ImageOptions paramImageOptions) {
    DiskCacheFile diskCacheFile1 = null;
    DiskCacheFile diskCacheFile2 = null;
    DiskCacheFile diskCacheFile3 = diskCacheFile2;
    DiskCacheFile diskCacheFile4 = diskCacheFile1;
    try {
      LruDiskCache lruDiskCache = THUMB_CACHE;
      diskCacheFile3 = diskCacheFile2;
      diskCacheFile4 = diskCacheFile1;
      StringBuilder stringBuilder = new StringBuilder();
      diskCacheFile3 = diskCacheFile2;
      diskCacheFile4 = diskCacheFile1;
      this();
      diskCacheFile3 = diskCacheFile2;
      diskCacheFile4 = diskCacheFile1;
      DiskCacheFile diskCacheFile = lruDiskCache.getDiskCacheFile(stringBuilder.append(paramFile.getAbsolutePath()).append("@").append(paramFile.lastModified()).append(paramImageOptions.toString()).toString());
      if (diskCacheFile != null) {
        diskCacheFile3 = diskCacheFile;
        diskCacheFile4 = diskCacheFile;
        if (diskCacheFile.exists()) {
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          BitmapFactory.Options options = new BitmapFactory.Options();
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          this();
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          options.inJustDecodeBounds = false;
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          options.inPurgeable = true;
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          options.inInputShareable = true;
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          options.inPreferredConfig = Bitmap.Config.ARGB_8888;
          diskCacheFile3 = diskCacheFile;
          diskCacheFile4 = diskCacheFile;
          return BitmapFactory.decodeFile(diskCacheFile.getAbsolutePath(), options);
        } 
      } 
      return null;
    } catch (Throwable throwable) {
      diskCacheFile4 = diskCacheFile3;
      LogUtil.w(throwable.getMessage(), throwable);
      return null;
    } finally {
      IOUtil.closeQuietly((Closeable)diskCacheFile4);
    } 
  }
  
  public static boolean isGif(File paramFile) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_1
    //   5: astore_3
    //   6: new java/io/FileInputStream
    //   9: astore #4
    //   11: aload_1
    //   12: astore_3
    //   13: aload #4
    //   15: aload_0
    //   16: invokespecial <init> : (Ljava/io/File;)V
    //   19: aload #4
    //   21: lconst_0
    //   22: iconst_3
    //   23: invokestatic readBytes : (Ljava/io/InputStream;JI)[B
    //   26: astore_0
    //   27: getstatic org/xutils/image/ImageDecoder.GIF_HEADER : [B
    //   30: aload_0
    //   31: invokestatic equals : ([B[B)Z
    //   34: istore #5
    //   36: aload #4
    //   38: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   41: iload #5
    //   43: ireturn
    //   44: astore #4
    //   46: aload_2
    //   47: astore_0
    //   48: aload_0
    //   49: astore_3
    //   50: aload #4
    //   52: invokevirtual getMessage : ()Ljava/lang/String;
    //   55: aload #4
    //   57: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   60: aload_0
    //   61: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   64: iconst_0
    //   65: istore #5
    //   67: goto -> 41
    //   70: astore_0
    //   71: aload_3
    //   72: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   75: aload_0
    //   76: athrow
    //   77: astore_0
    //   78: aload #4
    //   80: astore_3
    //   81: goto -> 71
    //   84: astore_0
    //   85: aload #4
    //   87: astore_3
    //   88: aload_0
    //   89: astore #4
    //   91: aload_3
    //   92: astore_0
    //   93: goto -> 48
    // Exception table:
    //   from	to	target	type
    //   6	11	44	java/lang/Throwable
    //   6	11	70	finally
    //   13	19	44	java/lang/Throwable
    //   13	19	70	finally
    //   19	36	84	java/lang/Throwable
    //   19	36	77	finally
    //   50	60	70	finally
  }
  
  public static Bitmap rotate(Bitmap paramBitmap, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_3
    //   3: astore #4
    //   5: iload_1
    //   6: ifeq -> 44
    //   9: new android/graphics/Matrix
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: astore #4
    //   18: aload #4
    //   20: iload_1
    //   21: i2f
    //   22: invokevirtual setRotate : (F)V
    //   25: aload_0
    //   26: iconst_0
    //   27: iconst_0
    //   28: aload_0
    //   29: invokevirtual getWidth : ()I
    //   32: aload_0
    //   33: invokevirtual getHeight : ()I
    //   36: aload #4
    //   38: iconst_1
    //   39: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   42: astore #4
    //   44: aload #4
    //   46: ifnull -> 92
    //   49: aload #4
    //   51: astore_3
    //   52: iload_2
    //   53: ifeq -> 72
    //   56: aload #4
    //   58: astore_3
    //   59: aload #4
    //   61: aload_0
    //   62: if_acmpeq -> 72
    //   65: aload_0
    //   66: invokevirtual recycle : ()V
    //   69: aload #4
    //   71: astore_3
    //   72: aload_3
    //   73: areturn
    //   74: astore #4
    //   76: aload #4
    //   78: invokevirtual getMessage : ()Ljava/lang/String;
    //   81: aload #4
    //   83: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   86: aload_3
    //   87: astore #4
    //   89: goto -> 44
    //   92: aload_0
    //   93: astore_3
    //   94: goto -> 72
    // Exception table:
    //   from	to	target	type
    //   25	44	74	java/lang/Throwable
  }
  
  private static void saveThumbCache(File paramFile, ImageOptions paramImageOptions, Bitmap paramBitmap) {
    // Byte code:
    //   0: new org/xutils/cache/DiskCacheEntity
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_3
    //   9: new java/lang/StringBuilder
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: aload_0
    //   17: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: ldc_w '@'
    //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: aload_0
    //   30: invokevirtual lastModified : ()J
    //   33: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   36: aload_1
    //   37: invokevirtual toString : ()Ljava/lang/String;
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual toString : ()Ljava/lang/String;
    //   46: invokevirtual setKey : (Ljava/lang/String;)V
    //   49: aconst_null
    //   50: astore_1
    //   51: aconst_null
    //   52: astore #4
    //   54: aconst_null
    //   55: astore #5
    //   57: aconst_null
    //   58: astore #6
    //   60: aconst_null
    //   61: astore #7
    //   63: aload #6
    //   65: astore #8
    //   67: getstatic org/xutils/image/ImageDecoder.THUMB_CACHE : Lorg/xutils/cache/LruDiskCache;
    //   70: aload_3
    //   71: invokevirtual createDiskCacheFile : (Lorg/xutils/cache/DiskCacheEntity;)Lorg/xutils/cache/DiskCacheFile;
    //   74: astore_0
    //   75: aload_0
    //   76: astore_1
    //   77: aload_0
    //   78: ifnull -> 140
    //   81: aload_0
    //   82: astore #4
    //   84: aload_0
    //   85: astore_1
    //   86: aload #6
    //   88: astore #8
    //   90: new java/io/FileOutputStream
    //   93: astore #7
    //   95: aload_0
    //   96: astore #4
    //   98: aload_0
    //   99: astore_1
    //   100: aload #6
    //   102: astore #8
    //   104: aload #7
    //   106: aload_0
    //   107: invokespecial <init> : (Ljava/io/File;)V
    //   110: getstatic org/xutils/image/ImageDecoder.supportWebP : Z
    //   113: ifeq -> 150
    //   116: getstatic android/graphics/Bitmap$CompressFormat.WEBP : Landroid/graphics/Bitmap$CompressFormat;
    //   119: astore_1
    //   120: aload_2
    //   121: aload_1
    //   122: bipush #80
    //   124: aload #7
    //   126: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   129: pop
    //   130: aload #7
    //   132: invokevirtual flush : ()V
    //   135: aload_0
    //   136: invokevirtual commit : ()Lorg/xutils/cache/DiskCacheFile;
    //   139: astore_1
    //   140: aload_1
    //   141: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   144: aload #7
    //   146: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   149: return
    //   150: getstatic android/graphics/Bitmap$CompressFormat.PNG : Landroid/graphics/Bitmap$CompressFormat;
    //   153: astore_1
    //   154: goto -> 120
    //   157: astore_2
    //   158: aload #5
    //   160: astore_0
    //   161: aload #4
    //   163: astore_1
    //   164: aload_0
    //   165: astore #8
    //   167: aload #4
    //   169: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   172: pop
    //   173: aload #4
    //   175: astore_1
    //   176: aload_0
    //   177: astore #8
    //   179: aload_2
    //   180: invokevirtual getMessage : ()Ljava/lang/String;
    //   183: aload_2
    //   184: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   187: aload #4
    //   189: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   192: aload_0
    //   193: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   196: goto -> 149
    //   199: astore_2
    //   200: aload_1
    //   201: astore_0
    //   202: aload_0
    //   203: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   206: aload #8
    //   208: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   211: aload_2
    //   212: athrow
    //   213: astore_1
    //   214: aload #7
    //   216: astore #8
    //   218: aload_1
    //   219: astore_2
    //   220: goto -> 202
    //   223: astore_2
    //   224: aload_0
    //   225: astore #4
    //   227: aload #7
    //   229: astore_0
    //   230: goto -> 161
    // Exception table:
    //   from	to	target	type
    //   67	75	157	java/lang/Throwable
    //   67	75	199	finally
    //   90	95	157	java/lang/Throwable
    //   90	95	199	finally
    //   104	110	157	java/lang/Throwable
    //   104	110	199	finally
    //   110	120	223	java/lang/Throwable
    //   110	120	213	finally
    //   120	140	223	java/lang/Throwable
    //   120	140	213	finally
    //   150	154	223	java/lang/Throwable
    //   150	154	213	finally
    //   167	173	199	finally
    //   179	187	199	finally
  }
  
  static {
    boolean bool = false;
    byte b = 1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ImageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */