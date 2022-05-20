package app.gamer.quadstellar.newdevices.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;

public class ImageUtils {
  public static final int BLUR_RADIUS = 50;
  
  public static Bitmap blur(Bitmap paramBitmap, int paramInt) {
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
      int i7 = 0;
      int i9 = 0;
      i4 = 0;
      int i12 = 0;
      int i11 = 0;
      int i8 = 0;
      i2 = 0;
      int i13 = 0;
      int i10 = 0;
      i6 = -paramInt * i;
      i3 = -paramInt;
      while (i3 <= paramInt) {
        int i16 = Math.max(0, i6) + n;
        int[] arrayOfInt7 = arrayOfInt[i3 + paramInt];
        arrayOfInt7[0] = arrayOfInt2[i16];
        arrayOfInt7[1] = arrayOfInt3[i16];
        arrayOfInt7[2] = arrayOfInt4[i16];
        int i15 = i5 - Math.abs(i3);
        i4 += arrayOfInt2[i16] * i15;
        i9 += arrayOfInt3[i16] * i15;
        i7 += arrayOfInt4[i16] * i15;
        if (i3 > 0) {
          i10 += arrayOfInt7[0];
          i13 += arrayOfInt7[1];
          i2 += arrayOfInt7[2];
        } else {
          i8 += arrayOfInt7[0];
          i11 += arrayOfInt7[1];
          i12 += arrayOfInt7[2];
        } 
        i15 = i6;
        if (i3 < m)
          i15 = i6 + i; 
        i3++;
        i6 = i15;
      } 
      i3 = n;
      int i14 = paramInt;
      for (i6 = 0; i6 < j; i6++) {
        arrayOfInt1[i3] = 0xFF000000 & arrayOfInt1[i3] | arrayOfInt6[i4] << 16 | arrayOfInt6[i9] << 8 | arrayOfInt6[i7];
        int[] arrayOfInt7 = arrayOfInt[(i14 - paramInt + i1) % i1];
        int i17 = arrayOfInt7[0];
        k = arrayOfInt7[1];
        int i15 = arrayOfInt7[2];
        if (n == 0)
          arrayOfInt5[i6] = Math.min(i6 + i5, m) * i; 
        int i16 = n + arrayOfInt5[i6];
        arrayOfInt7[0] = arrayOfInt2[i16];
        arrayOfInt7[1] = arrayOfInt3[i16];
        arrayOfInt7[2] = arrayOfInt4[i16];
        i10 += arrayOfInt7[0];
        i13 += arrayOfInt7[1];
        i2 += arrayOfInt7[2];
        i4 = i4 - i8 + i10;
        i9 = i9 - i11 + i13;
        i7 = i7 - i12 + i2;
        i14 = (i14 + 1) % i1;
        arrayOfInt7 = arrayOfInt[i14];
        i8 = i8 - i17 + arrayOfInt7[0];
        i11 = i11 - k + arrayOfInt7[1];
        i12 = i12 - i15 + arrayOfInt7[2];
        i10 -= arrayOfInt7[0];
        i13 -= arrayOfInt7[1];
        i2 -= arrayOfInt7[2];
        i3 += i;
      } 
    } 
    paramBitmap.setPixels(arrayOfInt1, 0, i, 0, 0, i, j);
    return paramBitmap;
  }
  
  public static Bitmap createCircleImage(Bitmap paramBitmap) {
    if (paramBitmap.getWidth() < paramBitmap.getHeight()) {
      int j = paramBitmap.getWidth();
      Paint paint1 = new Paint();
      paint1.setAntiAlias(true);
      Bitmap bitmap1 = Bitmap.createBitmap(j, j, Bitmap.Config.ARGB_8888);
      Canvas canvas1 = new Canvas(bitmap1);
      canvas1.drawCircle((j / 2), (j / 2), (j / 2), paint1);
      paint1.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      canvas1.drawBitmap(paramBitmap, 0.0F, 0.0F, paint1);
      return bitmap1;
    } 
    int i = paramBitmap.getHeight();
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    Bitmap bitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    canvas.drawCircle((i / 2), (i / 2), (i / 2), paint);
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    return bitmap;
  }
  
  public static Bitmap resizeImage(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = paramInt1 / i;
    float f2 = paramInt2 / j;
    Matrix matrix = new Matrix();
    matrix.postScale(f1, f2);
    return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, matrix, true);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/ImageUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */