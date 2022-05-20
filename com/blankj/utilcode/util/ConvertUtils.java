package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class ConvertUtils {
  private static final char[] hexDigits = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  private ConvertUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
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
  
  public static byte[] bits2Bytes(String paramString) {
    int i = paramString.length() % 8;
    int j = paramString.length() / 8;
    int k = j;
    String str = paramString;
    if (i != 0) {
      for (k = i; k < 8; k++)
        paramString = "0" + paramString; 
      k = j + 1;
      str = paramString;
    } 
    byte[] arrayOfByte = new byte[k];
    for (j = 0; j < k; j++) {
      for (i = 0; i < 8; i++) {
        arrayOfByte[j] = (byte)(byte)(arrayOfByte[j] << 1);
        arrayOfByte[j] = (byte)(byte)(arrayOfByte[j] | str.charAt(j * 8 + i) - 48);
      } 
    } 
    return arrayOfByte;
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String byte2FitMemorySize(long paramLong) {
    return (paramLong < 0L) ? "shouldn't be less than zero!" : ((paramLong < 1024L) ? String.format("%.3fB", new Object[] { Double.valueOf(paramLong) }) : ((paramLong < 1048576L) ? String.format("%.3fKB", new Object[] { Double.valueOf(paramLong / 1024.0D) }) : ((paramLong < 1073741824L) ? String.format("%.3fMB", new Object[] { Double.valueOf(paramLong / 1048576.0D) }) : String.format("%.3fGB", new Object[] { Double.valueOf(paramLong / 1.073741824E9D) }))));
  }
  
  public static double byte2MemorySize(long paramLong, int paramInt) {
    return (paramLong < 0L) ? -1.0D : (paramLong / paramInt);
  }
  
  public static Bitmap bytes2Bitmap(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte == null || paramArrayOfbyte.length == 0) ? null : BitmapFactory.decodeByteArray(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static String bytes2Bits(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      byte b1 = paramArrayOfbyte[b];
      for (byte b2 = 7; b2 >= 0; b2--) {
        byte b3;
        if ((b1 >> b2 & 0x1) == 0) {
          byte b4 = 48;
          b3 = b4;
        } else {
          byte b4 = 49;
          b3 = b4;
        } 
        stringBuilder.append(b3);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public static char[] bytes2Chars(byte[] paramArrayOfbyte) {
    char[] arrayOfChar = null;
    if (paramArrayOfbyte != null) {
      int i = paramArrayOfbyte.length;
      if (i > 0) {
        char[] arrayOfChar1 = new char[i];
        byte b = 0;
        while (true) {
          arrayOfChar = arrayOfChar1;
          if (b < i) {
            arrayOfChar1[b] = (char)(char)(paramArrayOfbyte[b] & 0xFF);
            b++;
            continue;
          } 
          return arrayOfChar;
        } 
      } 
    } 
    return arrayOfChar;
  }
  
  public static Drawable bytes2Drawable(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte == null) ? null : bitmap2Drawable(bytes2Bitmap(paramArrayOfbyte));
  }
  
  public static String bytes2HexString(byte[] paramArrayOfbyte) {
    String str;
    char[] arrayOfChar = null;
    if (paramArrayOfbyte != null) {
      int i = paramArrayOfbyte.length;
      if (i > 0) {
        arrayOfChar = new char[i << 1];
        byte b = 0;
        int j = 0;
        while (b < i) {
          int k = j + 1;
          arrayOfChar[j] = (char)hexDigits[paramArrayOfbyte[b] >>> 4 & 0xF];
          j = k + 1;
          arrayOfChar[k] = (char)hexDigits[paramArrayOfbyte[b] & 0xF];
          b++;
        } 
        str = new String(arrayOfChar);
      } 
    } 
    return str;
  }
  
  public static InputStream bytes2InputStream(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0) ? null : new ByteArrayInputStream(paramArrayOfbyte);
  }
  
  public static OutputStream bytes2OutputStream(byte[] paramArrayOfbyte) {
    byte[] arrayOfByte2;
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0)
      return null; 
    IOException iOException1 = null;
    byte[] arrayOfByte1 = null;
    IOException iOException2 = iOException1;
    try {
      IOException iOException;
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      iOException2 = iOException1;
      this();
      try {
        byteArrayOutputStream.write(paramArrayOfbyte);
      } catch (IOException iOException3) {
        ByteArrayOutputStream byteArrayOutputStream1 = byteArrayOutputStream;
      } finally {
        paramArrayOfbyte = null;
      } 
    } catch (IOException null) {
      paramArrayOfbyte = arrayOfByte1;
      arrayOfByte2 = paramArrayOfbyte;
      null.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { (Closeable)paramArrayOfbyte });
      return null;
    } finally {}
    CloseUtils.closeIO(new Closeable[] { (Closeable)arrayOfByte2 });
    throw paramArrayOfbyte;
  }
  
  public static byte[] chars2Bytes(char[] paramArrayOfchar) {
    if (paramArrayOfchar == null || paramArrayOfchar.length <= 0)
      return null; 
    int i = paramArrayOfchar.length;
    byte[] arrayOfByte = new byte[i];
    byte b = 0;
    while (true) {
      byte[] arrayOfByte1 = arrayOfByte;
      if (b < i) {
        arrayOfByte[b] = (byte)(byte)paramArrayOfchar[b];
        b++;
        continue;
      } 
      return arrayOfByte1;
    } 
  }
  
  public static int dp2px(float paramFloat) {
    return (int)(paramFloat * (Utils.getApp().getResources().getDisplayMetrics()).density + 0.5F);
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
  
  private static int hex2Dec(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9') {
      paramChar -= '0';
      return paramChar;
    } 
    if (paramChar >= 'A' && paramChar <= 'F')
      return paramChar - 65 + 10; 
    throw new IllegalArgumentException();
  }
  
  public static byte[] hexString2Bytes(String paramString) {
    if (isSpace(paramString))
      return null; 
    int i = paramString.length();
    int j = i;
    String str = paramString;
    if (i % 2 != 0) {
      str = "0" + paramString;
      j = i + 1;
    } 
    char[] arrayOfChar = str.toUpperCase().toCharArray();
    byte[] arrayOfByte = new byte[j >> 1];
    i = 0;
    while (true) {
      byte[] arrayOfByte1 = arrayOfByte;
      if (i < j) {
        arrayOfByte[i >> 1] = (byte)(byte)(hex2Dec(arrayOfChar[i]) << 4 | hex2Dec(arrayOfChar[i + 1]));
        i += 2;
        continue;
      } 
      return arrayOfByte1;
    } 
  }
  
  public static ByteArrayOutputStream input2OutputStream(InputStream paramInputStream) {
    if (paramInputStream == null)
      return null; 
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        int i = paramInputStream.read(arrayOfByte, 0, 1024);
        if (i != -1) {
          byteArrayOutputStream.write(arrayOfByte, 0, i);
          continue;
        } 
        return byteArrayOutputStream;
      } 
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return null;
    } finally {
      CloseUtils.closeIO(new Closeable[] { paramInputStream });
    } 
  }
  
  public static byte[] inputStream2Bytes(InputStream paramInputStream) {
    return (paramInputStream == null) ? null : input2OutputStream(paramInputStream).toByteArray();
  }
  
  public static String inputStream2String(InputStream paramInputStream, String paramString) {
    if (paramInputStream == null || isSpace(paramString))
      return null; 
    try {
      String str2 = new String();
      this(inputStream2Bytes(paramInputStream), paramString);
      String str1 = str2;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      unsupportedEncodingException = null;
    } 
    return (String)unsupportedEncodingException;
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
  
  public static long memorySize2Byte(long paramLong, int paramInt) {
    return (paramLong < 0L) ? -1L : (paramInt * paramLong);
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String millis2FitTimeSpan(long paramLong, int paramInt) {
    if (paramLong <= 0L || paramInt <= 0)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    int[] arrayOfInt = new int[5];
    arrayOfInt[0] = 86400000;
    arrayOfInt[1] = 3600000;
    arrayOfInt[2] = 60000;
    arrayOfInt[3] = 1000;
    arrayOfInt[4] = 1;
    int i = Math.min(paramInt, 5);
    paramInt = 0;
    while (paramInt < i) {
      long l = paramLong;
      if (paramLong >= arrayOfInt[paramInt]) {
        long l1 = paramLong / arrayOfInt[paramInt];
        l = paramLong - arrayOfInt[paramInt] * l1;
        (new String[5])[0] = "天";
        (new String[5])[1] = "小时";
        (new String[5])[2] = "分钟";
        (new String[5])[3] = "秒";
        (new String[5])[4] = "毫秒";
        stringBuilder.append(l1).append((new String[5])[paramInt]);
      } 
      paramInt++;
      paramLong = l;
    } 
    return stringBuilder.toString();
  }
  
  public static long millis2TimeSpan(long paramLong, int paramInt) {
    return paramLong / paramInt;
  }
  
  public static byte[] outputStream2Bytes(OutputStream paramOutputStream) {
    return (paramOutputStream == null) ? null : ((ByteArrayOutputStream)paramOutputStream).toByteArray();
  }
  
  public static String outputStream2String(OutputStream paramOutputStream, String paramString) {
    if (paramOutputStream == null || isSpace(paramString))
      return null; 
    try {
      String str2 = new String();
      this(outputStream2Bytes(paramOutputStream), paramString);
      String str1 = str2;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      unsupportedEncodingException = null;
    } 
    return (String)unsupportedEncodingException;
  }
  
  public static int px2dp(float paramFloat) {
    return (int)(paramFloat / (Utils.getApp().getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int px2sp(float paramFloat) {
    return (int)(paramFloat / (Utils.getApp().getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
  
  public static int sp2px(float paramFloat) {
    return (int)(paramFloat * (Utils.getApp().getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
  
  public static InputStream string2InputStream(String paramString1, String paramString2) {
    if (paramString1 == null || isSpace(paramString2))
      return null; 
    try {
      ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream();
      this(paramString1.getBytes(paramString2));
      ByteArrayInputStream byteArrayInputStream1 = byteArrayInputStream2;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      unsupportedEncodingException = null;
    } 
    return (InputStream)unsupportedEncodingException;
  }
  
  public static OutputStream string2OutputStream(String paramString1, String paramString2) {
    OutputStream outputStream1 = null;
    OutputStream outputStream2 = outputStream1;
    if (paramString1 != null) {
      if (isSpace(paramString2))
        return outputStream1; 
    } else {
      return outputStream2;
    } 
    try {
      outputStream2 = bytes2OutputStream(paramString1.getBytes(paramString2));
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
      outputStream2 = outputStream1;
    } 
    return outputStream2;
  }
  
  public static long timeSpan2Millis(long paramLong, int paramInt) {
    return paramInt * paramLong;
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
  
  public ByteArrayInputStream output2InputStream(OutputStream paramOutputStream) {
    return (paramOutputStream == null) ? null : new ByteArrayInputStream(((ByteArrayOutputStream)paramOutputStream).toByteArray());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ConvertUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */