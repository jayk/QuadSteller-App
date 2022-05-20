package android.support.v7.graphics;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.TimingLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

final class ColorCutQuantizer {
  static final int COMPONENT_BLUE = -1;
  
  static final int COMPONENT_GREEN = -2;
  
  static final int COMPONENT_RED = -3;
  
  private static final String LOG_TAG = "ColorCutQuantizer";
  
  private static final boolean LOG_TIMINGS = false;
  
  private static final int QUANTIZE_WORD_MASK = 31;
  
  private static final int QUANTIZE_WORD_WIDTH = 5;
  
  private static final Comparator<Vbox> VBOX_COMPARATOR_VOLUME = new Comparator<Vbox>() {
      public int compare(ColorCutQuantizer.Vbox param1Vbox1, ColorCutQuantizer.Vbox param1Vbox2) {
        return param1Vbox2.getVolume() - param1Vbox1.getVolume();
      }
    };
  
  final int[] mColors;
  
  final Palette.Filter[] mFilters;
  
  final int[] mHistogram;
  
  final List<Palette.Swatch> mQuantizedColors;
  
  private final float[] mTempHsl = new float[3];
  
  final TimingLogger mTimingLogger = null;
  
  ColorCutQuantizer(int[] paramArrayOfint, int paramInt, Palette.Filter[] paramArrayOfFilter) {
    this.mFilters = paramArrayOfFilter;
    int[] arrayOfInt = new int[32768];
    this.mHistogram = arrayOfInt;
    int i;
    for (i = 0; i < paramArrayOfint.length; i++) {
      int m = quantizeFromRgb888(paramArrayOfint[i]);
      paramArrayOfint[i] = m;
      arrayOfInt[m] = arrayOfInt[m] + 1;
    } 
    i = 0;
    int j = 0;
    while (j < arrayOfInt.length) {
      if (arrayOfInt[j] > 0 && shouldIgnoreColor(j))
        arrayOfInt[j] = 0; 
      int m = i;
      if (arrayOfInt[j] > 0)
        m = i + 1; 
      j++;
      i = m;
    } 
    paramArrayOfint = new int[i];
    this.mColors = paramArrayOfint;
    int k = 0;
    byte b = 0;
    while (b < arrayOfInt.length) {
      j = k;
      if (arrayOfInt[b] > 0) {
        paramArrayOfint[k] = b;
        j = k + 1;
      } 
      b++;
      k = j;
    } 
    if (i <= paramInt) {
      this.mQuantizedColors = new ArrayList<Palette.Swatch>();
      i = paramArrayOfint.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        j = paramArrayOfint[paramInt];
        this.mQuantizedColors.add(new Palette.Swatch(approximateToRgb888(j), arrayOfInt[j]));
      } 
    } else {
      this.mQuantizedColors = quantizePixels(paramInt);
    } 
  }
  
  private static int approximateToRgb888(int paramInt) {
    return approximateToRgb888(quantizedRed(paramInt), quantizedGreen(paramInt), quantizedBlue(paramInt));
  }
  
  static int approximateToRgb888(int paramInt1, int paramInt2, int paramInt3) {
    return Color.rgb(modifyWordWidth(paramInt1, 5, 8), modifyWordWidth(paramInt2, 5, 8), modifyWordWidth(paramInt3, 5, 8));
  }
  
  private List<Palette.Swatch> generateAverageColors(Collection<Vbox> paramCollection) {
    ArrayList<Palette.Swatch> arrayList = new ArrayList(paramCollection.size());
    Iterator<Vbox> iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      Palette.Swatch swatch = ((Vbox)iterator.next()).getAverageColor();
      if (!shouldIgnoreColor(swatch))
        arrayList.add(swatch); 
    } 
    return arrayList;
  }
  
  static void modifySignificantOctet(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    switch (paramInt1) {
      default:
        return;
      case -2:
        paramInt1 = paramInt2;
        while (true) {
          if (paramInt1 <= paramInt3) {
            paramInt2 = paramArrayOfint[paramInt1];
            paramArrayOfint[paramInt1] = quantizedGreen(paramInt2) << 10 | quantizedRed(paramInt2) << 5 | quantizedBlue(paramInt2);
            paramInt1++;
          } 
        } 
      case -1:
        break;
    } 
    paramInt1 = paramInt2;
    while (true) {
      if (paramInt1 <= paramInt3) {
        paramInt2 = paramArrayOfint[paramInt1];
        paramArrayOfint[paramInt1] = quantizedBlue(paramInt2) << 10 | quantizedGreen(paramInt2) << 5 | quantizedRed(paramInt2);
        paramInt1++;
      } 
    } 
  }
  
  private static int modifyWordWidth(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 > paramInt2) {
      paramInt1 <<= paramInt3 - paramInt2;
      return (1 << paramInt3) - 1 & paramInt1;
    } 
    paramInt1 >>= paramInt2 - paramInt3;
    return (1 << paramInt3) - 1 & paramInt1;
  }
  
  private static int quantizeFromRgb888(int paramInt) {
    return modifyWordWidth(Color.red(paramInt), 8, 5) << 10 | modifyWordWidth(Color.green(paramInt), 8, 5) << 5 | modifyWordWidth(Color.blue(paramInt), 8, 5);
  }
  
  private List<Palette.Swatch> quantizePixels(int paramInt) {
    PriorityQueue<Vbox> priorityQueue = new PriorityQueue<Vbox>(paramInt, VBOX_COMPARATOR_VOLUME);
    priorityQueue.offer(new Vbox(0, this.mColors.length - 1));
    splitBoxes(priorityQueue, paramInt);
    return generateAverageColors(priorityQueue);
  }
  
  static int quantizedBlue(int paramInt) {
    return paramInt & 0x1F;
  }
  
  static int quantizedGreen(int paramInt) {
    return paramInt >> 5 & 0x1F;
  }
  
  static int quantizedRed(int paramInt) {
    return paramInt >> 10 & 0x1F;
  }
  
  private boolean shouldIgnoreColor(int paramInt) {
    paramInt = approximateToRgb888(paramInt);
    ColorUtils.colorToHSL(paramInt, this.mTempHsl);
    return shouldIgnoreColor(paramInt, this.mTempHsl);
  }
  
  private boolean shouldIgnoreColor(int paramInt, float[] paramArrayOffloat) {
    byte b;
    int i;
    if (this.mFilters != null && this.mFilters.length > 0) {
      b = 0;
      i = this.mFilters.length;
      while (b < i) {
        if (!this.mFilters[b].isAllowed(paramInt, paramArrayOffloat))
          return true; 
        b++;
      } 
    } 
    boolean bool = false;
    while (b < i) {
      if (!this.mFilters[b].isAllowed(paramInt, paramArrayOffloat))
        return true; 
      b++;
    } 
  }
  
  private boolean shouldIgnoreColor(Palette.Swatch paramSwatch) {
    return shouldIgnoreColor(paramSwatch.getRgb(), paramSwatch.getHsl());
  }
  
  private void splitBoxes(PriorityQueue<Vbox> paramPriorityQueue, int paramInt) {
    while (paramPriorityQueue.size() < paramInt) {
      Vbox vbox = paramPriorityQueue.poll();
      if (vbox != null && vbox.canSplit()) {
        paramPriorityQueue.offer(vbox.splitBox());
        paramPriorityQueue.offer(vbox);
      } 
    } 
  }
  
  List<Palette.Swatch> getQuantizedColors() {
    return this.mQuantizedColors;
  }
  
  private class Vbox {
    private int mLowerIndex;
    
    private int mMaxBlue;
    
    private int mMaxGreen;
    
    private int mMaxRed;
    
    private int mMinBlue;
    
    private int mMinGreen;
    
    private int mMinRed;
    
    private int mPopulation;
    
    private int mUpperIndex;
    
    Vbox(int param1Int1, int param1Int2) {
      this.mLowerIndex = param1Int1;
      this.mUpperIndex = param1Int2;
      fitBox();
    }
    
    final boolean canSplit() {
      boolean bool = true;
      if (getColorCount() <= 1)
        bool = false; 
      return bool;
    }
    
    final int findSplitPoint() {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getLongestColorDimension : ()I
      //   4: istore_1
      //   5: aload_0
      //   6: getfield this$0 : Landroid/support/v7/graphics/ColorCutQuantizer;
      //   9: getfield mColors : [I
      //   12: astore_2
      //   13: aload_0
      //   14: getfield this$0 : Landroid/support/v7/graphics/ColorCutQuantizer;
      //   17: getfield mHistogram : [I
      //   20: astore_3
      //   21: aload_2
      //   22: iload_1
      //   23: aload_0
      //   24: getfield mLowerIndex : I
      //   27: aload_0
      //   28: getfield mUpperIndex : I
      //   31: invokestatic modifySignificantOctet : ([IIII)V
      //   34: aload_2
      //   35: aload_0
      //   36: getfield mLowerIndex : I
      //   39: aload_0
      //   40: getfield mUpperIndex : I
      //   43: iconst_1
      //   44: iadd
      //   45: invokestatic sort : ([III)V
      //   48: aload_2
      //   49: iload_1
      //   50: aload_0
      //   51: getfield mLowerIndex : I
      //   54: aload_0
      //   55: getfield mUpperIndex : I
      //   58: invokestatic modifySignificantOctet : ([IIII)V
      //   61: aload_0
      //   62: getfield mPopulation : I
      //   65: iconst_2
      //   66: idiv
      //   67: istore #4
      //   69: aload_0
      //   70: getfield mLowerIndex : I
      //   73: istore_1
      //   74: iconst_0
      //   75: istore #5
      //   77: iload_1
      //   78: aload_0
      //   79: getfield mUpperIndex : I
      //   82: if_icmpgt -> 110
      //   85: iload #5
      //   87: aload_3
      //   88: aload_2
      //   89: iload_1
      //   90: iaload
      //   91: iaload
      //   92: iadd
      //   93: istore #5
      //   95: iload #5
      //   97: iload #4
      //   99: if_icmplt -> 104
      //   102: iload_1
      //   103: ireturn
      //   104: iinc #1, 1
      //   107: goto -> 77
      //   110: aload_0
      //   111: getfield mLowerIndex : I
      //   114: istore_1
      //   115: goto -> 102
    }
    
    final void fitBox() {
      int[] arrayOfInt1 = ColorCutQuantizer.this.mColors;
      int[] arrayOfInt2 = ColorCutQuantizer.this.mHistogram;
      int i = Integer.MAX_VALUE;
      int j = Integer.MAX_VALUE;
      int k = Integer.MAX_VALUE;
      int m = Integer.MIN_VALUE;
      int n = Integer.MIN_VALUE;
      int i1 = Integer.MIN_VALUE;
      int i2 = 0;
      int i3 = this.mLowerIndex;
      while (i3 <= this.mUpperIndex) {
        int i4 = arrayOfInt1[i3];
        int i5 = i2 + arrayOfInt2[i4];
        int i6 = ColorCutQuantizer.quantizedRed(i4);
        int i7 = ColorCutQuantizer.quantizedGreen(i4);
        i2 = ColorCutQuantizer.quantizedBlue(i4);
        i4 = i1;
        if (i6 > i1)
          i4 = i6; 
        int i8 = k;
        if (i6 < k)
          i8 = i6; 
        i1 = n;
        if (i7 > n)
          i1 = i7; 
        k = j;
        if (i7 < j)
          k = i7; 
        n = m;
        if (i2 > m)
          n = i2; 
        j = i;
        if (i2 < i)
          j = i2; 
        i3++;
        i2 = i5;
        m = n;
        n = i1;
        i1 = i4;
        i = j;
        j = k;
        k = i8;
      } 
      this.mMinRed = k;
      this.mMaxRed = i1;
      this.mMinGreen = j;
      this.mMaxGreen = n;
      this.mMinBlue = i;
      this.mMaxBlue = m;
      this.mPopulation = i2;
    }
    
    final Palette.Swatch getAverageColor() {
      int[] arrayOfInt1 = ColorCutQuantizer.this.mColors;
      int[] arrayOfInt2 = ColorCutQuantizer.this.mHistogram;
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      for (int n = this.mLowerIndex; n <= this.mUpperIndex; n++) {
        int i1 = arrayOfInt1[n];
        int i2 = arrayOfInt2[i1];
        m += i2;
        i += ColorCutQuantizer.quantizedRed(i1) * i2;
        j += ColorCutQuantizer.quantizedGreen(i1) * i2;
        k += ColorCutQuantizer.quantizedBlue(i1) * i2;
      } 
      return new Palette.Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(i / m), Math.round(j / m), Math.round(k / m)), m);
    }
    
    final int getColorCount() {
      return this.mUpperIndex + 1 - this.mLowerIndex;
    }
    
    final int getLongestColorDimension() {
      int i = this.mMaxRed - this.mMinRed;
      null = this.mMaxGreen - this.mMinGreen;
      int j = this.mMaxBlue - this.mMinBlue;
      return (i >= null && i >= j) ? -3 : ((null >= i && null >= j) ? -2 : -1);
    }
    
    final int getVolume() {
      return (this.mMaxRed - this.mMinRed + 1) * (this.mMaxGreen - this.mMinGreen + 1) * (this.mMaxBlue - this.mMinBlue + 1);
    }
    
    final Vbox splitBox() {
      if (!canSplit())
        throw new IllegalStateException("Can not split a box with only 1 color"); 
      int i = findSplitPoint();
      Vbox vbox = new Vbox(i + 1, this.mUpperIndex);
      this.mUpperIndex = i;
      fitBox();
      return vbox;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/graphics/ColorCutQuantizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */