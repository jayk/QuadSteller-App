package app.gamer.quadstellar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtil {
  public static Bitmap autoFixOrientation(Bitmap paramBitmap, ImageView paramImageView, Uri paramUri, String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #4
    //   3: aload_2
    //   4: ifnonnull -> 32
    //   7: new android/media/ExifInterface
    //   10: astore #4
    //   12: aload #4
    //   14: aload_3
    //   15: invokespecial <init> : (Ljava/lang/String;)V
    //   18: aload #4
    //   20: ifnonnull -> 52
    //   23: ldc 'exif is null check your uri or path'
    //   25: invokestatic red : (Ljava/lang/Object;)V
    //   28: aload_0
    //   29: astore_2
    //   30: aload_2
    //   31: areturn
    //   32: aload_3
    //   33: ifnonnull -> 18
    //   36: new android/media/ExifInterface
    //   39: dup
    //   40: aload_2
    //   41: invokevirtual getPath : ()Ljava/lang/String;
    //   44: invokespecial <init> : (Ljava/lang/String;)V
    //   47: astore #4
    //   49: goto -> 18
    //   52: aload #4
    //   54: ldc 'Orientation'
    //   56: invokevirtual getAttribute : (Ljava/lang/String;)Ljava/lang/String;
    //   59: invokestatic parseInt : (Ljava/lang/String;)I
    //   62: istore #5
    //   64: iload #5
    //   66: tableswitch default -> 104, 3 -> 163, 4 -> 104, 5 -> 104, 6 -> 156, 7 -> 104, 8 -> 171
    //   104: iconst_0
    //   105: istore #5
    //   107: new android/graphics/Matrix
    //   110: dup
    //   111: invokespecial <init> : ()V
    //   114: astore_2
    //   115: aload_2
    //   116: iload #5
    //   118: i2f
    //   119: invokevirtual preRotate : (F)Z
    //   122: pop
    //   123: aload_0
    //   124: iconst_0
    //   125: iconst_0
    //   126: aload_0
    //   127: invokevirtual getWidth : ()I
    //   130: aload_0
    //   131: invokevirtual getHeight : ()I
    //   134: aload_2
    //   135: iconst_1
    //   136: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   139: astore_0
    //   140: aload_0
    //   141: astore_2
    //   142: aload_1
    //   143: ifnull -> 30
    //   146: aload_1
    //   147: aload_0
    //   148: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;)V
    //   151: aload_0
    //   152: astore_2
    //   153: goto -> 30
    //   156: bipush #90
    //   158: istore #5
    //   160: goto -> 107
    //   163: sipush #180
    //   166: istore #5
    //   168: goto -> 107
    //   171: sipush #270
    //   174: istore #5
    //   176: goto -> 107
    //   179: astore_2
    //   180: ldc 'catch img error'
    //   182: ldc 'return'
    //   184: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   187: pop
    //   188: aload_0
    //   189: astore_2
    //   190: aload_1
    //   191: ifnull -> 30
    //   194: aload_1
    //   195: aload_0
    //   196: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;)V
    //   199: aload_0
    //   200: astore_2
    //   201: goto -> 30
    // Exception table:
    //   from	to	target	type
    //   7	18	179	java/lang/Exception
    //   23	28	179	java/lang/Exception
    //   36	49	179	java/lang/Exception
    //   52	64	179	java/lang/Exception
  }
  
  public static Bitmap blurImageAmeliorate(Bitmap paramBitmap) {
    long l1 = System.currentTimeMillis();
    int[] arrayOfInt1 = new int[9];
    arrayOfInt1[0] = 1;
    arrayOfInt1[1] = 2;
    arrayOfInt1[2] = 1;
    arrayOfInt1[3] = 2;
    arrayOfInt1[4] = 4;
    arrayOfInt1[5] = 2;
    arrayOfInt1[6] = 1;
    arrayOfInt1[7] = 2;
    arrayOfInt1[8] = 1;
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Bitmap bitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
    int k = 0;
    int m = 0;
    int n = 0;
    int[] arrayOfInt2 = new int[i * j];
    paramBitmap.getPixels(arrayOfInt2, 0, i, 0, 0, i, j);
    for (byte b = 1; b < j - 1; b++) {
      for (byte b1 = 1; b1 < i - 1; b1++) {
        byte b2 = 0;
        int i1 = -1;
        int i2 = m;
        int i3 = n;
        n = i1;
        while (n <= 1) {
          i1 = -1;
          m = k;
          for (k = i1; k <= 1; k++) {
            int i4 = arrayOfInt2[(b + n) * i + b1 + k];
            int i5 = Color.red(i4);
            i1 = Color.green(i4);
            i4 = Color.blue(i4);
            m += arrayOfInt1[b2] * i5;
            i2 += arrayOfInt1[b2] * i1;
            i3 += arrayOfInt1[b2] * i4;
            b2++;
          } 
          n++;
          k = m;
        } 
        n = k / 13;
        m = i2 / 13;
        k = i3 / 13;
        arrayOfInt2[b * i + b1] = Color.argb(255, Math.min(255, Math.max(0, n)), Math.min(255, Math.max(0, m)), Math.min(255, Math.max(0, k)));
        k = 0;
        m = 0;
        n = 0;
      } 
    } 
    bitmap.setPixels(arrayOfInt2, 0, i, 0, 0, i, j);
    long l2 = System.currentTimeMillis();
    Log.d("may", "used time=" + (l2 - l1));
    return bitmap;
  }
  
  private static Bitmap decode(String paramString, byte[] paramArrayOfbyte, BitmapFactory.Options paramOptions) {
    Bitmap bitmap = null;
    if (paramString != null) {
      bitmap = decodeFile(paramString, paramOptions);
    } else if (paramArrayOfbyte != null) {
      bitmap = BitmapFactory.decodeByteArray(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramOptions);
    } 
    if (bitmap == null && paramOptions != null && !paramOptions.inJustDecodeBounds)
      L.red("decode image failed" + paramString); 
    return bitmap;
  }
  
  private static Bitmap decodeFile(String paramString, BitmapFactory.Options paramOptions) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: astore_3
    //   4: aload_1
    //   5: ifnonnull -> 16
    //   8: new android/graphics/BitmapFactory$Options
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: astore_3
    //   16: aload_3
    //   17: iconst_1
    //   18: putfield inInputShareable : Z
    //   21: aload_3
    //   22: iconst_1
    //   23: putfield inPurgeable : Z
    //   26: aconst_null
    //   27: astore #4
    //   29: aconst_null
    //   30: astore #5
    //   32: aload #4
    //   34: astore_1
    //   35: new java/io/FileInputStream
    //   38: astore #6
    //   40: aload #4
    //   42: astore_1
    //   43: aload #6
    //   45: aload_0
    //   46: invokespecial <init> : (Ljava/lang/String;)V
    //   49: aload #6
    //   51: invokevirtual getFD : ()Ljava/io/FileDescriptor;
    //   54: aconst_null
    //   55: aload_3
    //   56: invokestatic decodeFileDescriptor : (Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   59: astore_1
    //   60: aload #6
    //   62: ifnull -> 70
    //   65: aload #6
    //   67: invokevirtual close : ()V
    //   70: aload_1
    //   71: areturn
    //   72: astore_0
    //   73: aload_0
    //   74: invokevirtual printStackTrace : ()V
    //   77: goto -> 70
    //   80: astore #6
    //   82: aload #5
    //   84: astore_0
    //   85: aload_0
    //   86: astore_1
    //   87: aload #6
    //   89: invokevirtual toString : ()Ljava/lang/String;
    //   92: invokestatic red : (Ljava/lang/Object;)V
    //   95: aload_2
    //   96: astore_1
    //   97: aload_0
    //   98: ifnull -> 70
    //   101: aload_0
    //   102: invokevirtual close : ()V
    //   105: aload_2
    //   106: astore_1
    //   107: goto -> 70
    //   110: astore_0
    //   111: aload_0
    //   112: invokevirtual printStackTrace : ()V
    //   115: aload_2
    //   116: astore_1
    //   117: goto -> 70
    //   120: astore_0
    //   121: aload_1
    //   122: ifnull -> 129
    //   125: aload_1
    //   126: invokevirtual close : ()V
    //   129: aload_0
    //   130: athrow
    //   131: astore_1
    //   132: aload_1
    //   133: invokevirtual printStackTrace : ()V
    //   136: goto -> 129
    //   139: astore_0
    //   140: aload #6
    //   142: astore_1
    //   143: goto -> 121
    //   146: astore_0
    //   147: aload #6
    //   149: astore_1
    //   150: aload_0
    //   151: astore #6
    //   153: aload_1
    //   154: astore_0
    //   155: goto -> 85
    // Exception table:
    //   from	to	target	type
    //   35	40	80	java/io/IOException
    //   35	40	120	finally
    //   43	49	80	java/io/IOException
    //   43	49	120	finally
    //   49	60	146	java/io/IOException
    //   49	60	139	finally
    //   65	70	72	java/io/IOException
    //   87	95	120	finally
    //   101	105	110	java/io/IOException
    //   125	129	131	java/io/IOException
  }
  
  public static Bitmap getResizedImage(String paramString, byte[] paramArrayOfbyte, int paramInt1, boolean paramBoolean, int paramInt2) {
    Bitmap bitmap;
    BitmapFactory.Options options = null;
    if (paramInt1 > 0) {
      options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      options.inInputShareable = true;
      options.inPurgeable = true;
      decode(paramString, paramArrayOfbyte, options);
      int i = options.outWidth;
      int j = i;
      if (!paramBoolean)
        j = Math.max(i, options.outHeight); 
      paramInt1 = sampleSize(j, paramInt1);
      Log.e("ssize", "getResizedImage: " + paramInt1);
      options = new BitmapFactory.Options();
      options.inSampleSize = paramInt1;
    } 
    OutOfMemoryError outOfMemoryError3 = null;
    try {
      Bitmap bitmap1 = decode(paramString, paramArrayOfbyte, options);
    } catch (OutOfMemoryError outOfMemoryError1) {
      L.red(outOfMemoryError1.toString());
      outOfMemoryError1.printStackTrace();
      outOfMemoryError1 = outOfMemoryError3;
    } 
    OutOfMemoryError outOfMemoryError2 = outOfMemoryError1;
    if (paramInt2 > 0)
      bitmap = getRoundedCornerBitmap((Bitmap)outOfMemoryError1, paramInt2); 
    return bitmap;
  }
  
  private static Bitmap getRoundedCornerBitmap(Bitmap paramBitmap, int paramInt) {
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    Rect rect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    RectF rectF = new RectF(rect);
    float f = paramInt;
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(-12434878);
    canvas.drawRoundRect(rectF, f, f, paint);
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(paramBitmap, rect, rect, paint);
    return bitmap;
  }
  
  private static int sampleSize(int paramInt1, int paramInt2) {
    int i = 1;
    for (byte b = 0;; b++) {
      if (b >= 10 || paramInt1 < paramInt2 * 2)
        return i; 
      paramInt1 /= 2;
      i *= 2;
    } 
  }
  
  public static class L {
    public static void red(Object param1Object) {
      Log.e("ImageUtil", param1Object.toString());
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/ImageUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */