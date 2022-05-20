package com.blankj.utilcode.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ImageUtils {
  private ImageUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static Bitmap addBorder(Bitmap paramBitmap, @IntRange(from = 1L) int paramInt1, @ColorInt int paramInt2, boolean paramBoolean1, float paramFloat, boolean paramBoolean2) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    if (!paramBoolean2)
      paramBitmap = paramBitmap.copy(paramBitmap.getConfig(), true); 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Canvas canvas = new Canvas(paramBitmap);
    Paint paint = new Paint(1);
    paint.setColor(paramInt2);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(paramInt1);
    if (paramBoolean1) {
      float f = Math.min(i, j) / 2.0F;
      paramFloat = paramInt1 / 2.0F;
      canvas.drawCircle(i / 2.0F, j / 2.0F, f - paramFloat, paint);
      return paramBitmap;
    } 
    paramInt1 >>= 1;
    canvas.drawRoundRect(new RectF(paramInt1, paramInt1, (i - paramInt1), (j - paramInt1)), paramFloat, paramFloat, paint);
    return paramBitmap;
  }
  
  public static Bitmap addCircleBorder(Bitmap paramBitmap, @IntRange(from = 1L) int paramInt1, @ColorInt int paramInt2) {
    return addBorder(paramBitmap, paramInt1, paramInt2, true, 0.0F, false);
  }
  
  public static Bitmap addCircleBorder(Bitmap paramBitmap, @IntRange(from = 1L) int paramInt1, @ColorInt int paramInt2, boolean paramBoolean) {
    return addBorder(paramBitmap, paramInt1, paramInt2, true, 0.0F, paramBoolean);
  }
  
  public static Bitmap addCornerBorder(Bitmap paramBitmap, @IntRange(from = 1L) int paramInt1, @ColorInt int paramInt2, @FloatRange(from = 0.0D) float paramFloat) {
    return addBorder(paramBitmap, paramInt1, paramInt2, false, paramFloat, false);
  }
  
  public static Bitmap addCornerBorder(Bitmap paramBitmap, @IntRange(from = 1L) int paramInt1, @ColorInt int paramInt2, @FloatRange(from = 0.0D) float paramFloat, boolean paramBoolean) {
    return addBorder(paramBitmap, paramInt1, paramInt2, false, paramFloat, paramBoolean);
  }
  
  public static Bitmap addImageWatermark(Bitmap paramBitmap1, Bitmap paramBitmap2, int paramInt1, int paramInt2, int paramInt3) {
    return addImageWatermark(paramBitmap1, paramBitmap2, paramInt1, paramInt2, paramInt3, false);
  }
  
  public static Bitmap addImageWatermark(Bitmap paramBitmap1, Bitmap paramBitmap2, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap1))
      return null; 
    Bitmap bitmap = paramBitmap1.copy(paramBitmap1.getConfig(), true);
    if (!isEmptyBitmap(paramBitmap2)) {
      Paint paint = new Paint(1);
      Canvas canvas = new Canvas(bitmap);
      paint.setAlpha(paramInt3);
      canvas.drawBitmap(paramBitmap2, paramInt1, paramInt2, paint);
    } 
    paramBitmap2 = bitmap;
    if (paramBoolean) {
      paramBitmap2 = bitmap;
      if (!paramBitmap1.isRecycled()) {
        paramBitmap1.recycle();
        paramBitmap2 = bitmap;
      } 
    } 
    return paramBitmap2;
  }
  
  public static Bitmap addReflection(Bitmap paramBitmap, int paramInt) {
    return addReflection(paramBitmap, paramInt, false);
  }
  
  public static Bitmap addReflection(Bitmap paramBitmap, int paramInt, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Matrix matrix = new Matrix();
    matrix.preScale(1.0F, -1.0F);
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, 0, j - paramInt, i, paramInt, matrix, false);
    Bitmap bitmap3 = Bitmap.createBitmap(i, j + paramInt, paramBitmap.getConfig());
    Canvas canvas = new Canvas(bitmap3);
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
    canvas.drawBitmap(bitmap2, 0.0F, (j + 0), null);
    Paint paint = new Paint(1);
    paint.setShader((Shader)new LinearGradient(0.0F, j, 0.0F, (bitmap3.getHeight() + 0), 1895825407, 16777215, Shader.TileMode.MIRROR));
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    canvas.drawRect(0.0F, (j + 0), i, bitmap3.getHeight(), paint);
    if (!bitmap2.isRecycled())
      bitmap2.recycle(); 
    Bitmap bitmap1 = bitmap3;
    if (paramBoolean) {
      bitmap1 = bitmap3;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap3;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap addTextWatermark(Bitmap paramBitmap, String paramString, float paramFloat1, @ColorInt int paramInt, float paramFloat2, float paramFloat3, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap) || paramString == null)
      return null; 
    Bitmap bitmap2 = paramBitmap.copy(paramBitmap.getConfig(), true);
    Paint paint = new Paint(1);
    Canvas canvas = new Canvas(bitmap2);
    paint.setColor(paramInt);
    paint.setTextSize(paramFloat1);
    Rect rect = new Rect();
    paint.getTextBounds(paramString, 0, paramString.length(), rect);
    canvas.drawText(paramString, paramFloat2, paramFloat3 + paramFloat1, paint);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap addTextWatermark(Bitmap paramBitmap, String paramString, int paramInt1, @ColorInt int paramInt2, float paramFloat1, float paramFloat2) {
    return addTextWatermark(paramBitmap, paramString, paramInt1, paramInt2, paramFloat1, paramFloat2, false);
  }
  
  public static byte[] bitmap2Bytes(Bitmap paramBitmap, Bitmap.CompressFormat paramCompressFormat) {
    if (paramBitmap == null)
      return null; 
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(paramCompressFormat, 100, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  
  public static Drawable bitmap2Drawable(Bitmap paramBitmap) {
    return (Drawable)((paramBitmap == null) ? null : new BitmapDrawable(Utils.getApp().getResources(), paramBitmap));
  }
  
  public static Bitmap bytes2Bitmap(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte == null || paramArrayOfbyte.length == 0) ? null : BitmapFactory.decodeByteArray(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static Drawable bytes2Drawable(byte[] paramArrayOfbyte) {
    return bitmap2Drawable(bytes2Bitmap(paramArrayOfbyte));
  }
  
  private static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2) {
    int i = paramOptions.outHeight;
    int j = paramOptions.outWidth;
    int k = 1;
    while (true) {
      j >>= 1;
      if (j >= paramInt1) {
        i >>= 1;
        if (i >= paramInt2) {
          k <<= 1;
          continue;
        } 
      } 
      break;
    } 
    return k;
  }
  
  public static Bitmap clip(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return clip(paramBitmap, paramInt1, paramInt2, paramInt3, paramInt4, false);
  }
  
  public static Bitmap clip(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, paramInt1, paramInt2, paramInt3, paramInt4);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap compressByQuality(Bitmap paramBitmap, @IntRange(from = 0L, to = 100L) int paramInt) {
    return compressByQuality(paramBitmap, paramInt, false);
  }
  
  public static Bitmap compressByQuality(Bitmap paramBitmap, @IntRange(from = 0L, to = 100L) int paramInt, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, paramInt, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    if (paramBoolean && !paramBitmap.isRecycled())
      paramBitmap.recycle(); 
    return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
  }
  
  public static Bitmap compressByQuality(Bitmap paramBitmap, long paramLong) {
    return compressByQuality(paramBitmap, paramLong, false);
  }
  
  public static Bitmap compressByQuality(Bitmap paramBitmap, long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic isEmptyBitmap : (Landroid/graphics/Bitmap;)Z
    //   4: ifne -> 13
    //   7: lload_1
    //   8: lconst_0
    //   9: lcmp
    //   10: ifgt -> 17
    //   13: aconst_null
    //   14: astore_0
    //   15: aload_0
    //   16: areturn
    //   17: new java/io/ByteArrayOutputStream
    //   20: dup
    //   21: invokespecial <init> : ()V
    //   24: astore #4
    //   26: aload_0
    //   27: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
    //   30: bipush #100
    //   32: aload #4
    //   34: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   37: pop
    //   38: aload #4
    //   40: invokevirtual size : ()I
    //   43: i2l
    //   44: lload_1
    //   45: lcmp
    //   46: ifgt -> 84
    //   49: aload #4
    //   51: invokevirtual toByteArray : ()[B
    //   54: astore #4
    //   56: iload_3
    //   57: ifeq -> 71
    //   60: aload_0
    //   61: invokevirtual isRecycled : ()Z
    //   64: ifne -> 71
    //   67: aload_0
    //   68: invokevirtual recycle : ()V
    //   71: aload #4
    //   73: iconst_0
    //   74: aload #4
    //   76: arraylength
    //   77: invokestatic decodeByteArray : ([BII)Landroid/graphics/Bitmap;
    //   80: astore_0
    //   81: goto -> 15
    //   84: aload #4
    //   86: invokevirtual reset : ()V
    //   89: aload_0
    //   90: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
    //   93: iconst_0
    //   94: aload #4
    //   96: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   99: pop
    //   100: aload #4
    //   102: invokevirtual size : ()I
    //   105: i2l
    //   106: lload_1
    //   107: lcmp
    //   108: iflt -> 121
    //   111: aload #4
    //   113: invokevirtual toByteArray : ()[B
    //   116: astore #4
    //   118: goto -> 56
    //   121: iconst_0
    //   122: istore #5
    //   124: bipush #100
    //   126: istore #6
    //   128: iconst_0
    //   129: istore #7
    //   131: iload #5
    //   133: iload #6
    //   135: if_icmpge -> 179
    //   138: iload #5
    //   140: iload #6
    //   142: iadd
    //   143: iconst_2
    //   144: idiv
    //   145: istore #7
    //   147: aload #4
    //   149: invokevirtual reset : ()V
    //   152: aload_0
    //   153: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
    //   156: iload #7
    //   158: aload #4
    //   160: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   163: pop
    //   164: aload #4
    //   166: invokevirtual size : ()I
    //   169: istore #8
    //   171: iload #8
    //   173: i2l
    //   174: lload_1
    //   175: lcmp
    //   176: ifne -> 215
    //   179: iload #6
    //   181: iload #7
    //   183: iconst_1
    //   184: isub
    //   185: if_icmpne -> 205
    //   188: aload #4
    //   190: invokevirtual reset : ()V
    //   193: aload_0
    //   194: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
    //   197: iload #5
    //   199: aload #4
    //   201: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   204: pop
    //   205: aload #4
    //   207: invokevirtual toByteArray : ()[B
    //   210: astore #4
    //   212: goto -> 56
    //   215: iload #8
    //   217: i2l
    //   218: lload_1
    //   219: lcmp
    //   220: ifle -> 232
    //   223: iload #7
    //   225: iconst_1
    //   226: isub
    //   227: istore #6
    //   229: goto -> 131
    //   232: iload #7
    //   234: iconst_1
    //   235: iadd
    //   236: istore #5
    //   238: goto -> 131
  }
  
  public static Bitmap compressBySampleSize(Bitmap paramBitmap, int paramInt) {
    return compressBySampleSize(paramBitmap, paramInt, false);
  }
  
  public static Bitmap compressBySampleSize(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return compressBySampleSize(paramBitmap, paramInt1, paramInt2, false);
  }
  
  public static Bitmap compressBySampleSize(Bitmap paramBitmap, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt1, paramInt2);
    options.inJustDecodeBounds = false;
    if (paramBoolean && !paramBitmap.isRecycled())
      paramBitmap.recycle(); 
    return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, options);
  }
  
  public static Bitmap compressBySampleSize(Bitmap paramBitmap, int paramInt, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = paramInt;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    if (paramBoolean && !paramBitmap.isRecycled())
      paramBitmap.recycle(); 
    return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, options);
  }
  
  public static Bitmap compressByScale(Bitmap paramBitmap, float paramFloat1, float paramFloat2) {
    return scale(paramBitmap, paramFloat1, paramFloat2, false);
  }
  
  public static Bitmap compressByScale(Bitmap paramBitmap, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return scale(paramBitmap, paramFloat1, paramFloat2, paramBoolean);
  }
  
  public static Bitmap compressByScale(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return scale(paramBitmap, paramInt1, paramInt2, false);
  }
  
  public static Bitmap compressByScale(Bitmap paramBitmap, int paramInt1, int paramInt2, boolean paramBoolean) {
    return scale(paramBitmap, paramInt1, paramInt2, paramBoolean);
  }
  
  private static boolean createFileByDeleteOldFile(File paramFile) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: iload_1
    //   7: istore_2
    //   8: iload_2
    //   9: ireturn
    //   10: aload_0
    //   11: invokevirtual exists : ()Z
    //   14: ifeq -> 26
    //   17: iload_1
    //   18: istore_2
    //   19: aload_0
    //   20: invokevirtual delete : ()Z
    //   23: ifeq -> 8
    //   26: iload_1
    //   27: istore_2
    //   28: aload_0
    //   29: invokevirtual getParentFile : ()Ljava/io/File;
    //   32: invokestatic createOrExistsDir : (Ljava/io/File;)Z
    //   35: ifeq -> 8
    //   38: aload_0
    //   39: invokevirtual createNewFile : ()Z
    //   42: istore_2
    //   43: goto -> 8
    //   46: astore_0
    //   47: aload_0
    //   48: invokevirtual printStackTrace : ()V
    //   51: iload_1
    //   52: istore_2
    //   53: goto -> 8
    // Exception table:
    //   from	to	target	type
    //   38	43	46	java/io/IOException
  }
  
  private static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  public static Bitmap drawable2Bitmap(Drawable paramDrawable) {
    Bitmap bitmap;
    if (paramDrawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable)paramDrawable;
      if (bitmapDrawable.getBitmap() != null)
        return bitmapDrawable.getBitmap(); 
    } 
    if (paramDrawable.getIntrinsicWidth() <= 0 || paramDrawable.getIntrinsicHeight() <= 0) {
      Bitmap.Config config;
      if (paramDrawable.getOpacity() != -1) {
        config = Bitmap.Config.ARGB_8888;
      } else {
        config = Bitmap.Config.RGB_565;
      } 
      bitmap = Bitmap.createBitmap(1, 1, config);
    } else {
      Bitmap.Config config;
      int i = paramDrawable.getIntrinsicWidth();
      int j = paramDrawable.getIntrinsicHeight();
      if (paramDrawable.getOpacity() != -1) {
        config = Bitmap.Config.ARGB_8888;
      } else {
        config = Bitmap.Config.RGB_565;
      } 
      bitmap = Bitmap.createBitmap(i, j, config);
    } 
    Canvas canvas = new Canvas(bitmap);
    paramDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    paramDrawable.draw(canvas);
    return bitmap;
  }
  
  public static byte[] drawable2Bytes(Drawable paramDrawable, Bitmap.CompressFormat paramCompressFormat) {
    return (paramDrawable == null) ? null : bitmap2Bytes(drawable2Bitmap(paramDrawable), paramCompressFormat);
  }
  
  public static Bitmap fastBlur(Bitmap paramBitmap, @FloatRange(from = 0.0D, fromInclusive = false, to = 1.0D) float paramFloat1, @FloatRange(from = 0.0D, fromInclusive = false, to = 25.0D) float paramFloat2) {
    return fastBlur(paramBitmap, paramFloat1, paramFloat2, false);
  }
  
  public static Bitmap fastBlur(Bitmap paramBitmap, @FloatRange(from = 0.0D, fromInclusive = false, to = 1.0D) float paramFloat1, @FloatRange(from = 0.0D, fromInclusive = false, to = 25.0D) float paramFloat2, boolean paramBoolean) {
    Bitmap bitmap2;
    if (isEmptyBitmap(paramBitmap))
      return null; 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Matrix matrix = new Matrix();
    matrix.setScale(paramFloat1, paramFloat1);
    Bitmap bitmap1 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
    Paint paint = new Paint(3);
    Canvas canvas = new Canvas();
    paint.setColorFilter((ColorFilter)new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_ATOP));
    canvas.scale(paramFloat1, paramFloat1);
    canvas.drawBitmap(bitmap1, 0.0F, 0.0F, paint);
    if (Build.VERSION.SDK_INT >= 17) {
      bitmap2 = renderScriptBlur(bitmap1, paramFloat2, paramBoolean);
    } else {
      bitmap2 = stackBlur(bitmap1, (int)paramFloat2, paramBoolean);
    } 
    bitmap1 = bitmap2;
    if (paramFloat1 != 1.0F) {
      bitmap1 = Bitmap.createScaledBitmap(bitmap2, i, j, true);
      if (bitmap2 != null && !bitmap2.isRecycled())
        bitmap2.recycle(); 
      if (paramBoolean && !paramBitmap.isRecycled())
        paramBitmap.recycle(); 
    } 
    return bitmap1;
  }
  
  public static Bitmap getBitmap(@DrawableRes int paramInt) {
    Drawable drawable = ContextCompat.getDrawable((Context)Utils.getApp(), paramInt);
    Canvas canvas = new Canvas();
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    canvas.setBitmap(bitmap);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    drawable.draw(canvas);
    return bitmap;
  }
  
  public static Bitmap getBitmap(@DrawableRes int paramInt1, int paramInt2, int paramInt3) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    Resources resources = Utils.getApp().getResources();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(resources, paramInt1, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt2, paramInt3);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(resources, paramInt1, options);
  }
  
  public static Bitmap getBitmap(File paramFile) {
    return (paramFile == null) ? null : BitmapFactory.decodeFile(paramFile.getAbsolutePath());
  }
  
  public static Bitmap getBitmap(File paramFile, int paramInt1, int paramInt2) {
    if (paramFile == null)
      return null; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramFile.getAbsolutePath(), options);
    options.inSampleSize = calculateInSampleSize(options, paramInt1, paramInt2);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(paramFile.getAbsolutePath(), options);
  }
  
  public static Bitmap getBitmap(FileDescriptor paramFileDescriptor) {
    return (paramFileDescriptor == null) ? null : BitmapFactory.decodeFileDescriptor(paramFileDescriptor);
  }
  
  public static Bitmap getBitmap(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2) {
    FileDescriptor fileDescriptor = null;
    if (paramFileDescriptor == null)
      return (Bitmap)fileDescriptor; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt1, paramInt2);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, options);
  }
  
  public static Bitmap getBitmap(InputStream paramInputStream) {
    return (paramInputStream == null) ? null : BitmapFactory.decodeStream(paramInputStream);
  }
  
  public static Bitmap getBitmap(InputStream paramInputStream, int paramInt1, int paramInt2) {
    InputStream inputStream = null;
    if (paramInputStream == null)
      return (Bitmap)inputStream; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(paramInputStream, null, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt1, paramInt2);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeStream(paramInputStream, null, options);
  }
  
  public static Bitmap getBitmap(String paramString) {
    return isSpace(paramString) ? null : BitmapFactory.decodeFile(paramString);
  }
  
  public static Bitmap getBitmap(String paramString, int paramInt1, int paramInt2) {
    if (isSpace(paramString))
      return null; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt1, paramInt2);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(paramString, options);
  }
  
  public static Bitmap getBitmap(byte[] paramArrayOfbyte, int paramInt) {
    return (paramArrayOfbyte.length == 0) ? null : BitmapFactory.decodeByteArray(paramArrayOfbyte, paramInt, paramArrayOfbyte.length);
  }
  
  public static Bitmap getBitmap(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    if (paramArrayOfbyte.length == 0)
      return null; 
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(paramArrayOfbyte, paramInt1, paramArrayOfbyte.length, options);
    options.inSampleSize = calculateInSampleSize(options, paramInt2, paramInt3);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeByteArray(paramArrayOfbyte, paramInt1, paramArrayOfbyte.length, options);
  }
  
  private static File getFileByPath(String paramString) {
    return isSpace(paramString) ? null : new File(paramString);
  }
  
  public static String getImageType(File paramFile) {
    File file3;
    File file1 = null;
    if (paramFile == null)
      return (String)file1; 
    IOException iOException1 = null;
    File file2 = null;
    IOException iOException2 = iOException1;
    try {
      IOException iOException;
      FileInputStream fileInputStream = new FileInputStream();
      iOException2 = iOException1;
      this(paramFile);
      try {
        String str = getImageType(fileInputStream);
      } catch (IOException iOException3) {
        FileInputStream fileInputStream1 = fileInputStream;
      } finally {
        paramFile = null;
      } 
    } catch (IOException iOException) {
      paramFile = file2;
      file3 = paramFile;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile });
      return (String)file1;
    } finally {}
    CloseUtils.closeIO(new Closeable[] { (Closeable)file3 });
    throw paramFile;
  }
  
  public static String getImageType(InputStream paramInputStream) {
    String str2;
    String str1 = null;
    if (paramInputStream == null)
      return str1; 
    try {
      byte[] arrayOfByte = new byte[8];
      str2 = str1;
      if (paramInputStream.read(arrayOfByte, 0, 8) != -1)
        str2 = getImageType(arrayOfByte); 
    } catch (IOException iOException) {
      iOException.printStackTrace();
      str2 = str1;
    } 
    return str2;
  }
  
  public static String getImageType(String paramString) {
    return getImageType(getFileByPath(paramString));
  }
  
  public static String getImageType(byte[] paramArrayOfbyte) {
    return isJPEG(paramArrayOfbyte) ? "JPEG" : (isGIF(paramArrayOfbyte) ? "GIF" : (isPNG(paramArrayOfbyte) ? "PNG" : (isBMP(paramArrayOfbyte) ? "BMP" : null)));
  }
  
  public static int getRotateDegree(String paramString) {
    char c = Character.MIN_VALUE;
    try {
      ExifInterface exifInterface = new ExifInterface();
      this(paramString);
      int i = exifInterface.getAttributeInt("Orientation", 1);
      switch (i) {
        default:
          return 90;
        case 3:
          return 180;
        case 8:
          break;
      } 
      c = 'Ď';
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return c;
  }
  
  private static boolean isBMP(byte[] paramArrayOfbyte) {
    boolean bool = true;
    if (paramArrayOfbyte.length < 2 || paramArrayOfbyte[0] != 66 || paramArrayOfbyte[1] != 77)
      bool = false; 
    return bool;
  }
  
  private static boolean isEmptyBitmap(Bitmap paramBitmap) {
    return (paramBitmap == null || paramBitmap.getWidth() == 0 || paramBitmap.getHeight() == 0);
  }
  
  private static boolean isGIF(byte[] paramArrayOfbyte) {
    boolean bool = true;
    if (paramArrayOfbyte.length < 6 || paramArrayOfbyte[0] != 71 || paramArrayOfbyte[1] != 73 || paramArrayOfbyte[2] != 70 || paramArrayOfbyte[3] != 56 || (paramArrayOfbyte[4] != 55 && paramArrayOfbyte[4] != 57) || paramArrayOfbyte[5] != 97)
      bool = false; 
    return bool;
  }
  
  public static boolean isImage(File paramFile) {
    return (paramFile != null && isImage(paramFile.getPath()));
  }
  
  public static boolean isImage(String paramString) {
    paramString = paramString.toUpperCase();
    return (paramString.endsWith(".PNG") || paramString.endsWith(".JPG") || paramString.endsWith(".JPEG") || paramString.endsWith(".BMP") || paramString.endsWith(".GIF"));
  }
  
  private static boolean isJPEG(byte[] paramArrayOfbyte) {
    boolean bool = true;
    if (paramArrayOfbyte.length < 2 || paramArrayOfbyte[0] != -1 || paramArrayOfbyte[1] != -40)
      bool = false; 
    return bool;
  }
  
  private static boolean isPNG(byte[] paramArrayOfbyte) {
    boolean bool = true;
    if (paramArrayOfbyte.length < 8 || paramArrayOfbyte[0] != -119 || paramArrayOfbyte[1] != 80 || paramArrayOfbyte[2] != 78 || paramArrayOfbyte[3] != 71 || paramArrayOfbyte[4] != 13 || paramArrayOfbyte[5] != 10 || paramArrayOfbyte[6] != 26 || paramArrayOfbyte[7] != 10)
      bool = false; 
    return bool;
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  @TargetApi(17)
  public static Bitmap renderScriptBlur(Bitmap paramBitmap, @FloatRange(from = 0.0D, fromInclusive = false, to = 25.0D) float paramFloat) {
    return renderScriptBlur(paramBitmap, paramFloat, false);
  }
  
  @TargetApi(17)
  public static Bitmap renderScriptBlur(Bitmap paramBitmap, @FloatRange(from = 0.0D, fromInclusive = false, to = 25.0D) float paramFloat, boolean paramBoolean) {
    Bitmap bitmap;
    if (isEmptyBitmap(paramBitmap))
      return null; 
    RenderScript renderScript = null;
    if (!paramBoolean)
      paramBitmap = paramBitmap.copy(paramBitmap.getConfig(), true); 
    try {
      RenderScript renderScript1 = RenderScript.create((Context)Utils.getApp());
      renderScript = renderScript1;
      RenderScript.RSMessageHandler rSMessageHandler = new RenderScript.RSMessageHandler();
      renderScript = renderScript1;
      this();
      renderScript = renderScript1;
      renderScript1.setMessageHandler(rSMessageHandler);
      renderScript = renderScript1;
      Allocation allocation1 = Allocation.createFromBitmap(renderScript1, paramBitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
      renderScript = renderScript1;
      Allocation allocation2 = Allocation.createTyped(renderScript1, allocation1.getType());
      renderScript = renderScript1;
      ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript1, Element.U8_4(renderScript1));
      renderScript = renderScript1;
      scriptIntrinsicBlur.setInput(allocation1);
      renderScript = renderScript1;
      scriptIntrinsicBlur.setRadius(paramFloat);
      renderScript = renderScript1;
      scriptIntrinsicBlur.forEach(allocation2);
      renderScript = renderScript1;
      allocation2.copyTo(paramBitmap);
      bitmap = paramBitmap;
      if (renderScript1 != null) {
        renderScript1.destroy();
        bitmap = paramBitmap;
      } 
    } finally {}
    return bitmap;
  }
  
  public static Bitmap rotate(Bitmap paramBitmap, int paramInt, float paramFloat1, float paramFloat2) {
    return rotate(paramBitmap, paramInt, paramFloat1, paramFloat2, false);
  }
  
  public static Bitmap rotate(Bitmap paramBitmap, int paramInt, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Bitmap bitmap = paramBitmap;
    if (paramInt != 0) {
      Matrix matrix = new Matrix();
      matrix.setRotate(paramInt, paramFloat1, paramFloat2);
      bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
      if (paramBoolean && !paramBitmap.isRecycled())
        paramBitmap.recycle(); 
    } 
    return bitmap;
  }
  
  public static boolean save(Bitmap paramBitmap, File paramFile, Bitmap.CompressFormat paramCompressFormat) {
    return save(paramBitmap, paramFile, paramCompressFormat, false);
  }
  
  public static boolean save(Bitmap paramBitmap, File paramFile, Bitmap.CompressFormat paramCompressFormat, boolean paramBoolean) {
    Bitmap bitmap2;
    if (isEmptyBitmap(paramBitmap) || !createFileByDeleteOldFile(paramFile))
      return false; 
    BufferedOutputStream bufferedOutputStream1 = null;
    Bitmap bitmap1 = null;
    boolean bool1 = false;
    boolean bool2 = false;
    BufferedOutputStream bufferedOutputStream2 = bufferedOutputStream1;
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      FileOutputStream fileOutputStream = new FileOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      this(paramFile);
      bufferedOutputStream2 = bufferedOutputStream1;
      this(fileOutputStream);
      bool2 = bool1;
      try {
        bool1 = paramBitmap.compress(paramCompressFormat, 100, bufferedOutputStream);
        if (paramBoolean) {
          bool2 = bool1;
          if (!paramBitmap.isRecycled()) {
            bool2 = bool1;
            paramBitmap.recycle();
          } 
        } 
        CloseUtils.closeIO(new Closeable[] { bufferedOutputStream });
      } catch (IOException iOException) {
        BufferedOutputStream bufferedOutputStream3 = bufferedOutputStream;
      } finally {
        paramBitmap = null;
      } 
    } catch (IOException iOException) {
      paramBoolean = bool2;
      paramBitmap = bitmap1;
      bitmap2 = paramBitmap;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { (Closeable)paramBitmap });
      return paramBoolean;
    } finally {}
    CloseUtils.closeIO(new Closeable[] { (Closeable)bitmap2 });
    throw paramBitmap;
  }
  
  public static boolean save(Bitmap paramBitmap, String paramString, Bitmap.CompressFormat paramCompressFormat) {
    return save(paramBitmap, getFileByPath(paramString), paramCompressFormat, false);
  }
  
  public static boolean save(Bitmap paramBitmap, String paramString, Bitmap.CompressFormat paramCompressFormat, boolean paramBoolean) {
    return save(paramBitmap, getFileByPath(paramString), paramCompressFormat, paramBoolean);
  }
  
  public static Bitmap scale(Bitmap paramBitmap, float paramFloat1, float paramFloat2) {
    return scale(paramBitmap, paramFloat1, paramFloat2, false);
  }
  
  public static Bitmap scale(Bitmap paramBitmap, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Matrix matrix = new Matrix();
    matrix.setScale(paramFloat1, paramFloat2);
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap scale(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return scale(paramBitmap, paramInt1, paramInt2, false);
  }
  
  public static Bitmap scale(Bitmap paramBitmap, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Bitmap bitmap2 = Bitmap.createScaledBitmap(paramBitmap, paramInt1, paramInt2, true);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap skew(Bitmap paramBitmap, float paramFloat1, float paramFloat2) {
    return skew(paramBitmap, paramFloat1, paramFloat2, 0.0F, 0.0F, false);
  }
  
  public static Bitmap skew(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return skew(paramBitmap, paramFloat1, paramFloat2, paramFloat3, paramFloat4, false);
  }
  
  public static Bitmap skew(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Matrix matrix = new Matrix();
    matrix.setSkew(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap skew(Bitmap paramBitmap, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return skew(paramBitmap, paramFloat1, paramFloat2, 0.0F, 0.0F, paramBoolean);
  }
  
  public static Bitmap stackBlur(Bitmap paramBitmap, int paramInt) {
    return stackBlur(paramBitmap, paramInt, false);
  }
  
  public static Bitmap stackBlur(Bitmap paramBitmap, int paramInt, boolean paramBoolean) {
    if (!paramBoolean)
      paramBitmap = paramBitmap.copy(paramBitmap.getConfig(), true); 
    if (paramInt < 1)
      return null; 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int[] arrayOfInt1 = new int[i * j];
    paramBitmap.getPixels(arrayOfInt1, 0, i, 0, 0, i, j);
    int k = i - 1;
    int m = j - 1;
    int n = i * j;
    int i1 = paramInt + paramInt + 1;
    int[] arrayOfInt2 = new int[n];
    int[] arrayOfInt3 = new int[n];
    int[] arrayOfInt4 = new int[n];
    int[] arrayOfInt5 = new int[Math.max(i, j)];
    n = i1 + 1 >> 1;
    int i2 = n * n;
    int[] arrayOfInt6 = new int[i2 * 256];
    for (n = 0; n < i2 * 256; n++)
      arrayOfInt6[n] = n / i2; 
    int i3 = 0;
    int i4 = 0;
    int[][] arrayOfInt = new int[i1][3];
    int i5 = paramInt + 1;
    int i6;
    for (i6 = 0; i6 < j; i6++) {
      int i7 = 0;
      int i8 = 0;
      int i9 = 0;
      i2 = 0;
      int i10 = 0;
      int i11 = 0;
      n = 0;
      int i12 = 0;
      int i13 = 0;
      int i14;
      for (i14 = -paramInt; i14 <= paramInt; i14++) {
        int i16 = arrayOfInt1[Math.min(k, Math.max(i14, 0)) + i3];
        int[] arrayOfInt7 = arrayOfInt[i14 + paramInt];
        arrayOfInt7[0] = (0xFF0000 & i16) >> 16;
        arrayOfInt7[1] = (0xFF00 & i16) >> 8;
        arrayOfInt7[2] = i16 & 0xFF;
        i16 = i5 - Math.abs(i14);
        i9 += arrayOfInt7[0] * i16;
        i8 += arrayOfInt7[1] * i16;
        i7 += arrayOfInt7[2] * i16;
        if (i14 > 0) {
          i13 += arrayOfInt7[0];
          i12 += arrayOfInt7[1];
          n += arrayOfInt7[2];
        } else {
          i11 += arrayOfInt7[0];
          i10 += arrayOfInt7[1];
          i2 += arrayOfInt7[2];
        } 
      } 
      int i15 = paramInt;
      for (i14 = 0; i14 < i; i14++) {
        arrayOfInt2[i3] = arrayOfInt6[i9];
        arrayOfInt3[i3] = arrayOfInt6[i8];
        arrayOfInt4[i3] = arrayOfInt6[i7];
        int[] arrayOfInt7 = arrayOfInt[(i15 - paramInt + i1) % i1];
        int i16 = arrayOfInt7[0];
        int i17 = arrayOfInt7[1];
        int i18 = arrayOfInt7[2];
        if (i6 == 0)
          arrayOfInt5[i14] = Math.min(i14 + paramInt + 1, k); 
        int i19 = arrayOfInt1[arrayOfInt5[i14] + i4];
        arrayOfInt7[0] = (0xFF0000 & i19) >> 16;
        arrayOfInt7[1] = (0xFF00 & i19) >> 8;
        arrayOfInt7[2] = i19 & 0xFF;
        i13 += arrayOfInt7[0];
        i12 += arrayOfInt7[1];
        n += arrayOfInt7[2];
        i9 = i9 - i11 + i13;
        i8 = i8 - i10 + i12;
        i7 = i7 - i2 + n;
        i15 = (i15 + 1) % i1;
        arrayOfInt7 = arrayOfInt[i15 % i1];
        i11 = i11 - i16 + arrayOfInt7[0];
        i10 = i10 - i17 + arrayOfInt7[1];
        i2 = i2 - i18 + arrayOfInt7[2];
        i13 -= arrayOfInt7[0];
        i12 -= arrayOfInt7[1];
        n -= arrayOfInt7[2];
        i3++;
      } 
      i4 += i;
    } 
    for (n = 0; n < i; n++) {
      int i11 = 0;
      int i9 = 0;
      i4 = 0;
      int i13 = 0;
      int i7 = 0;
      int i8 = 0;
      i2 = 0;
      int i12 = 0;
      int i10 = 0;
      i6 = -paramInt * i;
      i3 = -paramInt;
      while (i3 <= paramInt) {
        int i15 = Math.max(0, i6) + n;
        int[] arrayOfInt7 = arrayOfInt[i3 + paramInt];
        arrayOfInt7[0] = arrayOfInt2[i15];
        arrayOfInt7[1] = arrayOfInt3[i15];
        arrayOfInt7[2] = arrayOfInt4[i15];
        int i16 = i5 - Math.abs(i3);
        i4 += arrayOfInt2[i15] * i16;
        i9 += arrayOfInt3[i15] * i16;
        i11 += arrayOfInt4[i15] * i16;
        if (i3 > 0) {
          i10 += arrayOfInt7[0];
          i12 += arrayOfInt7[1];
          i2 += arrayOfInt7[2];
        } else {
          i8 += arrayOfInt7[0];
          i7 += arrayOfInt7[1];
          i13 += arrayOfInt7[2];
        } 
        i15 = i6;
        if (i3 < m)
          i15 = i6 + i; 
        i3++;
        i6 = i15;
      } 
      i6 = n;
      int i14 = paramInt;
      for (i3 = 0; i3 < j; i3++) {
        arrayOfInt1[i6] = 0xFF000000 & arrayOfInt1[i6] | arrayOfInt6[i4] << 16 | arrayOfInt6[i9] << 8 | arrayOfInt6[i11];
        int[] arrayOfInt7 = arrayOfInt[(i14 - paramInt + i1) % i1];
        int i17 = arrayOfInt7[0];
        k = arrayOfInt7[1];
        int i15 = arrayOfInt7[2];
        if (n == 0)
          arrayOfInt5[i3] = Math.min(i3 + i5, m) * i; 
        int i16 = n + arrayOfInt5[i3];
        arrayOfInt7[0] = arrayOfInt2[i16];
        arrayOfInt7[1] = arrayOfInt3[i16];
        arrayOfInt7[2] = arrayOfInt4[i16];
        i10 += arrayOfInt7[0];
        i12 += arrayOfInt7[1];
        i2 += arrayOfInt7[2];
        i4 = i4 - i8 + i10;
        i9 = i9 - i7 + i12;
        i11 = i11 - i13 + i2;
        i14 = (i14 + 1) % i1;
        arrayOfInt7 = arrayOfInt[i14];
        i8 = i8 - i17 + arrayOfInt7[0];
        i7 = i7 - k + arrayOfInt7[1];
        i13 = i13 - i15 + arrayOfInt7[2];
        i10 -= arrayOfInt7[0];
        i12 -= arrayOfInt7[1];
        i2 -= arrayOfInt7[2];
        i6 += i;
      } 
    } 
    paramBitmap.setPixels(arrayOfInt1, 0, i, 0, 0, i, j);
    return paramBitmap;
  }
  
  public static Bitmap toAlpha(Bitmap paramBitmap) {
    return toAlpha(paramBitmap, Boolean.valueOf(false));
  }
  
  public static Bitmap toAlpha(Bitmap paramBitmap, Boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Bitmap bitmap2 = paramBitmap.extractAlpha();
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean.booleanValue()) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap toGray(Bitmap paramBitmap) {
    return toGray(paramBitmap, false);
  }
  
  public static Bitmap toGray(Bitmap paramBitmap, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), paramBitmap.getConfig());
    Canvas canvas = new Canvas(bitmap2);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap toRound(Bitmap paramBitmap) {
    return toRound(paramBitmap, 0, 0, false);
  }
  
  public static Bitmap toRound(Bitmap paramBitmap, @IntRange(from = 0L) int paramInt1, @ColorInt int paramInt2) {
    return toRound(paramBitmap, paramInt1, paramInt2, false);
  }
  
  public static Bitmap toRound(Bitmap paramBitmap, @IntRange(from = 0L) int paramInt1, @ColorInt int paramInt2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int k = Math.min(i, j);
    Paint paint = new Paint(1);
    Bitmap bitmap2 = Bitmap.createBitmap(i, j, paramBitmap.getConfig());
    float f = k / 2.0F;
    RectF rectF = new RectF(0.0F, 0.0F, i, j);
    rectF.inset((i - k) / 2.0F, (j - k) / 2.0F);
    Matrix matrix = new Matrix();
    matrix.setTranslate(rectF.left, rectF.top);
    matrix.preScale(k / i, k / j);
    BitmapShader bitmapShader = new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    bitmapShader.setLocalMatrix(matrix);
    paint.setShader((Shader)bitmapShader);
    Canvas canvas = new Canvas(bitmap2);
    canvas.drawRoundRect(rectF, f, f, paint);
    if (paramInt1 > 0) {
      paint.setShader(null);
      paint.setColor(paramInt2);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(paramInt1);
      float f1 = paramInt1 / 2.0F;
      canvas.drawCircle(i / 2.0F, j / 2.0F, f - f1, paint);
    } 
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap toRound(Bitmap paramBitmap, boolean paramBoolean) {
    return toRound(paramBitmap, 0, 0, paramBoolean);
  }
  
  public static Bitmap toRoundCorner(Bitmap paramBitmap, float paramFloat) {
    return toRoundCorner(paramBitmap, paramFloat, 0, 0, false);
  }
  
  public static Bitmap toRoundCorner(Bitmap paramBitmap, float paramFloat, @IntRange(from = 0L) int paramInt1, @ColorInt int paramInt2) {
    return toRoundCorner(paramBitmap, paramFloat, paramInt1, paramInt2, false);
  }
  
  public static Bitmap toRoundCorner(Bitmap paramBitmap, float paramFloat, @IntRange(from = 0L) int paramInt1, @ColorInt int paramInt2, boolean paramBoolean) {
    if (isEmptyBitmap(paramBitmap))
      return null; 
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Paint paint = new Paint(1);
    Bitmap bitmap2 = Bitmap.createBitmap(i, j, paramBitmap.getConfig());
    paint.setShader((Shader)new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    Canvas canvas = new Canvas(bitmap2);
    RectF rectF = new RectF(0.0F, 0.0F, i, j);
    float f = paramInt1 / 2.0F;
    rectF.inset(f, f);
    canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
    if (paramInt1 > 0) {
      paint.setShader(null);
      paint.setColor(paramInt2);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(paramInt1);
      paint.setStrokeCap(Paint.Cap.ROUND);
      canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
    } 
    Bitmap bitmap1 = bitmap2;
    if (paramBoolean) {
      bitmap1 = bitmap2;
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
        bitmap1 = bitmap2;
      } 
    } 
    return bitmap1;
  }
  
  public static Bitmap toRoundCorner(Bitmap paramBitmap, float paramFloat, boolean paramBoolean) {
    return toRoundCorner(paramBitmap, paramFloat, 0, 0, paramBoolean);
  }
  
  public static Bitmap view2Bitmap(View paramView) {
    if (paramView == null)
      return null; 
    Bitmap bitmap = Bitmap.createBitmap(paramView.getWidth(), paramView.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Drawable drawable = paramView.getBackground();
    if (drawable != null) {
      drawable.draw(canvas);
    } else {
      canvas.drawColor(-1);
    } 
    paramView.draw(canvas);
    return bitmap;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ImageUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */