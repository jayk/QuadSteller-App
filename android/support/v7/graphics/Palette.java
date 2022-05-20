package android.support.v7.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Palette {
  static final int DEFAULT_CALCULATE_NUMBER_COLORS = 16;
  
  static final Filter DEFAULT_FILTER = new Filter() {
      private static final float BLACK_MAX_LIGHTNESS = 0.05F;
      
      private static final float WHITE_MIN_LIGHTNESS = 0.95F;
      
      private boolean isBlack(float[] param1ArrayOffloat) {
        return (param1ArrayOffloat[2] <= 0.05F);
      }
      
      private boolean isNearRedILine(float[] param1ArrayOffloat) {
        boolean bool = true;
        if (param1ArrayOffloat[0] < 10.0F || param1ArrayOffloat[0] > 37.0F || param1ArrayOffloat[1] > 0.82F)
          bool = false; 
        return bool;
      }
      
      private boolean isWhite(float[] param1ArrayOffloat) {
        return (param1ArrayOffloat[2] >= 0.95F);
      }
      
      public boolean isAllowed(int param1Int, float[] param1ArrayOffloat) {
        return (!isWhite(param1ArrayOffloat) && !isBlack(param1ArrayOffloat) && !isNearRedILine(param1ArrayOffloat));
      }
    };
  
  static final int DEFAULT_RESIZE_BITMAP_AREA = 12544;
  
  static final String LOG_TAG = "Palette";
  
  static final boolean LOG_TIMINGS = false;
  
  static final float MIN_CONTRAST_BODY_TEXT = 4.5F;
  
  static final float MIN_CONTRAST_TITLE_TEXT = 3.0F;
  
  private final Swatch mDominantSwatch;
  
  private final Map<Target, Swatch> mSelectedSwatches;
  
  private final List<Swatch> mSwatches;
  
  private final List<Target> mTargets;
  
  private final SparseBooleanArray mUsedColors;
  
  Palette(List<Swatch> paramList, List<Target> paramList1) {
    this.mSwatches = paramList;
    this.mTargets = paramList1;
    this.mUsedColors = new SparseBooleanArray();
    this.mSelectedSwatches = (Map<Target, Swatch>)new ArrayMap();
    this.mDominantSwatch = findDominantSwatch();
  }
  
  private static float[] copyHslValues(Swatch paramSwatch) {
    float[] arrayOfFloat = new float[3];
    System.arraycopy(paramSwatch.getHsl(), 0, arrayOfFloat, 0, 3);
    return arrayOfFloat;
  }
  
  private Swatch findDominantSwatch() {
    int i = Integer.MIN_VALUE;
    Swatch swatch = null;
    byte b = 0;
    int j = this.mSwatches.size();
    while (b < j) {
      Swatch swatch1 = this.mSwatches.get(b);
      int k = i;
      if (swatch1.getPopulation() > i) {
        swatch = swatch1;
        k = swatch1.getPopulation();
      } 
      b++;
      i = k;
    } 
    return swatch;
  }
  
  public static Builder from(Bitmap paramBitmap) {
    return new Builder(paramBitmap);
  }
  
  public static Palette from(List<Swatch> paramList) {
    return (new Builder(paramList)).generate();
  }
  
  @Deprecated
  public static Palette generate(Bitmap paramBitmap) {
    return from(paramBitmap).generate();
  }
  
  @Deprecated
  public static Palette generate(Bitmap paramBitmap, int paramInt) {
    return from(paramBitmap).maximumColorCount(paramInt).generate();
  }
  
  @Deprecated
  public static AsyncTask<Bitmap, Void, Palette> generateAsync(Bitmap paramBitmap, int paramInt, PaletteAsyncListener paramPaletteAsyncListener) {
    return from(paramBitmap).maximumColorCount(paramInt).generate(paramPaletteAsyncListener);
  }
  
  @Deprecated
  public static AsyncTask<Bitmap, Void, Palette> generateAsync(Bitmap paramBitmap, PaletteAsyncListener paramPaletteAsyncListener) {
    return from(paramBitmap).generate(paramPaletteAsyncListener);
  }
  
  private float generateScore(Swatch paramSwatch, Target paramTarget) {
    boolean bool;
    float[] arrayOfFloat = paramSwatch.getHsl();
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    if (this.mDominantSwatch != null) {
      bool = this.mDominantSwatch.getPopulation();
    } else {
      bool = true;
    } 
    if (paramTarget.getSaturationWeight() > 0.0F)
      f1 = paramTarget.getSaturationWeight() * (1.0F - Math.abs(arrayOfFloat[1] - paramTarget.getTargetSaturation())); 
    if (paramTarget.getLightnessWeight() > 0.0F)
      f2 = paramTarget.getLightnessWeight() * (1.0F - Math.abs(arrayOfFloat[2] - paramTarget.getTargetLightness())); 
    if (paramTarget.getPopulationWeight() > 0.0F)
      f3 = paramTarget.getPopulationWeight() * paramSwatch.getPopulation() / bool; 
    return f1 + f2 + f3;
  }
  
  private Swatch generateScoredTarget(Target paramTarget) {
    Swatch swatch = getMaxScoredSwatchForTarget(paramTarget);
    if (swatch != null && paramTarget.isExclusive())
      this.mUsedColors.append(swatch.getRgb(), true); 
    return swatch;
  }
  
  private Swatch getMaxScoredSwatchForTarget(Target paramTarget) {
    // Byte code:
    //   0: fconst_0
    //   1: fstore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: iconst_0
    //   5: istore #4
    //   7: aload_0
    //   8: getfield mSwatches : Ljava/util/List;
    //   11: invokeinterface size : ()I
    //   16: istore #5
    //   18: iload #4
    //   20: iload #5
    //   22: if_icmpge -> 103
    //   25: aload_0
    //   26: getfield mSwatches : Ljava/util/List;
    //   29: iload #4
    //   31: invokeinterface get : (I)Ljava/lang/Object;
    //   36: checkcast android/support/v7/graphics/Palette$Swatch
    //   39: astore #6
    //   41: fload_2
    //   42: fstore #7
    //   44: aload_3
    //   45: astore #8
    //   47: aload_0
    //   48: aload #6
    //   50: aload_1
    //   51: invokespecial shouldBeScoredForTarget : (Landroid/support/v7/graphics/Palette$Swatch;Landroid/support/v7/graphics/Target;)Z
    //   54: ifeq -> 91
    //   57: aload_0
    //   58: aload #6
    //   60: aload_1
    //   61: invokespecial generateScore : (Landroid/support/v7/graphics/Palette$Swatch;Landroid/support/v7/graphics/Target;)F
    //   64: fstore #9
    //   66: aload_3
    //   67: ifnull -> 83
    //   70: fload_2
    //   71: fstore #7
    //   73: aload_3
    //   74: astore #8
    //   76: fload #9
    //   78: fload_2
    //   79: fcmpl
    //   80: ifle -> 91
    //   83: aload #6
    //   85: astore #8
    //   87: fload #9
    //   89: fstore #7
    //   91: iinc #4, 1
    //   94: fload #7
    //   96: fstore_2
    //   97: aload #8
    //   99: astore_3
    //   100: goto -> 18
    //   103: aload_3
    //   104: areturn
  }
  
  private boolean shouldBeScoredForTarget(Swatch paramSwatch, Target paramTarget) {
    boolean bool = true;
    float[] arrayOfFloat = paramSwatch.getHsl();
    if (arrayOfFloat[1] < paramTarget.getMinimumSaturation() || arrayOfFloat[1] > paramTarget.getMaximumSaturation() || arrayOfFloat[2] < paramTarget.getMinimumLightness() || arrayOfFloat[2] > paramTarget.getMaximumLightness() || this.mUsedColors.get(paramSwatch.getRgb()))
      bool = false; 
    return bool;
  }
  
  void generate() {
    byte b = 0;
    int i = this.mTargets.size();
    while (b < i) {
      Target target = this.mTargets.get(b);
      target.normalizeWeights();
      this.mSelectedSwatches.put(target, generateScoredTarget(target));
      b++;
    } 
    this.mUsedColors.clear();
  }
  
  @ColorInt
  public int getColorForTarget(@NonNull Target paramTarget, @ColorInt int paramInt) {
    Swatch swatch = getSwatchForTarget(paramTarget);
    if (swatch != null)
      paramInt = swatch.getRgb(); 
    return paramInt;
  }
  
  @ColorInt
  public int getDarkMutedColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.DARK_MUTED, paramInt);
  }
  
  @Nullable
  public Swatch getDarkMutedSwatch() {
    return getSwatchForTarget(Target.DARK_MUTED);
  }
  
  @ColorInt
  public int getDarkVibrantColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.DARK_VIBRANT, paramInt);
  }
  
  @Nullable
  public Swatch getDarkVibrantSwatch() {
    return getSwatchForTarget(Target.DARK_VIBRANT);
  }
  
  @ColorInt
  public int getDominantColor(@ColorInt int paramInt) {
    if (this.mDominantSwatch != null)
      paramInt = this.mDominantSwatch.getRgb(); 
    return paramInt;
  }
  
  @Nullable
  public Swatch getDominantSwatch() {
    return this.mDominantSwatch;
  }
  
  @ColorInt
  public int getLightMutedColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.LIGHT_MUTED, paramInt);
  }
  
  @Nullable
  public Swatch getLightMutedSwatch() {
    return getSwatchForTarget(Target.LIGHT_MUTED);
  }
  
  @ColorInt
  public int getLightVibrantColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.LIGHT_VIBRANT, paramInt);
  }
  
  @Nullable
  public Swatch getLightVibrantSwatch() {
    return getSwatchForTarget(Target.LIGHT_VIBRANT);
  }
  
  @ColorInt
  public int getMutedColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.MUTED, paramInt);
  }
  
  @Nullable
  public Swatch getMutedSwatch() {
    return getSwatchForTarget(Target.MUTED);
  }
  
  @Nullable
  public Swatch getSwatchForTarget(@NonNull Target paramTarget) {
    return this.mSelectedSwatches.get(paramTarget);
  }
  
  @NonNull
  public List<Swatch> getSwatches() {
    return Collections.unmodifiableList(this.mSwatches);
  }
  
  @NonNull
  public List<Target> getTargets() {
    return Collections.unmodifiableList(this.mTargets);
  }
  
  @ColorInt
  public int getVibrantColor(@ColorInt int paramInt) {
    return getColorForTarget(Target.VIBRANT, paramInt);
  }
  
  @Nullable
  public Swatch getVibrantSwatch() {
    return getSwatchForTarget(Target.VIBRANT);
  }
  
  public static final class Builder {
    private final Bitmap mBitmap;
    
    private final List<Palette.Filter> mFilters = new ArrayList<Palette.Filter>();
    
    private int mMaxColors = 16;
    
    private Rect mRegion;
    
    private int mResizeArea = 12544;
    
    private int mResizeMaxDimension = -1;
    
    private final List<Palette.Swatch> mSwatches;
    
    private final List<Target> mTargets = new ArrayList<Target>();
    
    public Builder(Bitmap param1Bitmap) {
      if (param1Bitmap == null || param1Bitmap.isRecycled())
        throw new IllegalArgumentException("Bitmap is not valid"); 
      this.mFilters.add(Palette.DEFAULT_FILTER);
      this.mBitmap = param1Bitmap;
      this.mSwatches = null;
      this.mTargets.add(Target.LIGHT_VIBRANT);
      this.mTargets.add(Target.VIBRANT);
      this.mTargets.add(Target.DARK_VIBRANT);
      this.mTargets.add(Target.LIGHT_MUTED);
      this.mTargets.add(Target.MUTED);
      this.mTargets.add(Target.DARK_MUTED);
    }
    
    public Builder(List<Palette.Swatch> param1List) {
      if (param1List == null || param1List.isEmpty())
        throw new IllegalArgumentException("List of Swatches is not valid"); 
      this.mFilters.add(Palette.DEFAULT_FILTER);
      this.mSwatches = param1List;
      this.mBitmap = null;
    }
    
    private int[] getPixelsFromBitmap(Bitmap param1Bitmap) {
      int i = param1Bitmap.getWidth();
      int j = param1Bitmap.getHeight();
      int[] arrayOfInt2 = new int[i * j];
      param1Bitmap.getPixels(arrayOfInt2, 0, i, 0, 0, i, j);
      if (this.mRegion == null)
        return arrayOfInt2; 
      int k = this.mRegion.width();
      int m = this.mRegion.height();
      int[] arrayOfInt1 = new int[k * m];
      j = 0;
      while (true) {
        if (j < m) {
          System.arraycopy(arrayOfInt2, (this.mRegion.top + j) * i + this.mRegion.left, arrayOfInt1, j * k, k);
          j++;
          continue;
        } 
        return arrayOfInt1;
      } 
    }
    
    private Bitmap scaleBitmapDown(Bitmap param1Bitmap) {
      double d2;
      double d1 = -1.0D;
      if (this.mResizeArea > 0) {
        int i = param1Bitmap.getWidth() * param1Bitmap.getHeight();
        d2 = d1;
        if (i > this.mResizeArea)
          d2 = Math.sqrt(this.mResizeArea / i); 
      } else {
        d2 = d1;
        if (this.mResizeMaxDimension > 0) {
          int i = Math.max(param1Bitmap.getWidth(), param1Bitmap.getHeight());
          d2 = d1;
          if (i > this.mResizeMaxDimension)
            d2 = this.mResizeMaxDimension / i; 
        } 
      } 
      if (d2 > 0.0D)
        param1Bitmap = Bitmap.createScaledBitmap(param1Bitmap, (int)Math.ceil(param1Bitmap.getWidth() * d2), (int)Math.ceil(param1Bitmap.getHeight() * d2), false); 
      return param1Bitmap;
    }
    
    @NonNull
    public Builder addFilter(Palette.Filter param1Filter) {
      if (param1Filter != null)
        this.mFilters.add(param1Filter); 
      return this;
    }
    
    @NonNull
    public Builder addTarget(@NonNull Target param1Target) {
      if (!this.mTargets.contains(param1Target))
        this.mTargets.add(param1Target); 
      return this;
    }
    
    @NonNull
    public Builder clearFilters() {
      this.mFilters.clear();
      return this;
    }
    
    @NonNull
    public Builder clearRegion() {
      this.mRegion = null;
      return this;
    }
    
    @NonNull
    public Builder clearTargets() {
      if (this.mTargets != null)
        this.mTargets.clear(); 
      return this;
    }
    
    @NonNull
    public AsyncTask<Bitmap, Void, Palette> generate(final Palette.PaletteAsyncListener listener) {
      if (listener == null)
        throw new IllegalArgumentException("listener can not be null"); 
      return AsyncTaskCompat.executeParallel(new AsyncTask<Bitmap, Void, Palette>() {
            protected Palette doInBackground(Bitmap... param2VarArgs) {
              try {
                Palette palette = Palette.Builder.this.generate();
              } catch (Exception exception) {
                Log.e("Palette", "Exception thrown during async generate", exception);
                exception = null;
              } 
              return (Palette)exception;
            }
            
            protected void onPostExecute(Palette param2Palette) {
              listener.onGenerated(param2Palette);
            }
          }(Object[])new Bitmap[] { this.mBitmap });
    }
    
    @NonNull
    public Palette generate() {
      List<Palette.Swatch> list;
      if (this.mBitmap != null) {
        Palette.Filter[] arrayOfFilter;
        Bitmap bitmap = scaleBitmapDown(this.mBitmap);
        if (false)
          throw new NullPointerException(); 
        Rect rect = this.mRegion;
        if (bitmap != this.mBitmap && rect != null) {
          double d = bitmap.getWidth() / this.mBitmap.getWidth();
          rect.left = (int)Math.floor(rect.left * d);
          rect.top = (int)Math.floor(rect.top * d);
          rect.right = Math.min((int)Math.ceil(rect.right * d), bitmap.getWidth());
          rect.bottom = Math.min((int)Math.ceil(rect.bottom * d), bitmap.getHeight());
        } 
        int[] arrayOfInt = getPixelsFromBitmap(bitmap);
        int i = this.mMaxColors;
        if (this.mFilters.isEmpty()) {
          rect = null;
        } else {
          arrayOfFilter = this.mFilters.<Palette.Filter>toArray(new Palette.Filter[this.mFilters.size()]);
        } 
        ColorCutQuantizer colorCutQuantizer = new ColorCutQuantizer(arrayOfInt, i, arrayOfFilter);
        if (bitmap != this.mBitmap)
          bitmap.recycle(); 
        list = colorCutQuantizer.getQuantizedColors();
        if (false)
          throw new NullPointerException(); 
      } else {
        list = this.mSwatches;
      } 
      Palette palette = new Palette(list, this.mTargets);
      palette.generate();
      if (false)
        throw new NullPointerException(); 
      return palette;
    }
    
    @NonNull
    public Builder maximumColorCount(int param1Int) {
      this.mMaxColors = param1Int;
      return this;
    }
    
    @NonNull
    public Builder resizeBitmapArea(int param1Int) {
      this.mResizeArea = param1Int;
      this.mResizeMaxDimension = -1;
      return this;
    }
    
    @Deprecated
    @NonNull
    public Builder resizeBitmapSize(int param1Int) {
      this.mResizeMaxDimension = param1Int;
      this.mResizeArea = -1;
      return this;
    }
    
    @NonNull
    public Builder setRegion(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mBitmap != null) {
        if (this.mRegion == null)
          this.mRegion = new Rect(); 
        this.mRegion.set(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
        if (!this.mRegion.intersect(param1Int1, param1Int2, param1Int3, param1Int4))
          throw new IllegalArgumentException("The given region must intersect with the Bitmap's dimensions."); 
      } 
      return this;
    }
  }
  
  class null extends AsyncTask<Bitmap, Void, Palette> {
    protected Palette doInBackground(Bitmap... param1VarArgs) {
      try {
        Palette palette = this.this$0.generate();
      } catch (Exception exception) {
        Log.e("Palette", "Exception thrown during async generate", exception);
        exception = null;
      } 
      return (Palette)exception;
    }
    
    protected void onPostExecute(Palette param1Palette) {
      listener.onGenerated(param1Palette);
    }
  }
  
  public static interface Filter {
    boolean isAllowed(int param1Int, float[] param1ArrayOffloat);
  }
  
  public static interface PaletteAsyncListener {
    void onGenerated(Palette param1Palette);
  }
  
  public static final class Swatch {
    private final int mBlue;
    
    private int mBodyTextColor;
    
    private boolean mGeneratedTextColors;
    
    private final int mGreen;
    
    private float[] mHsl;
    
    private final int mPopulation;
    
    private final int mRed;
    
    private final int mRgb;
    
    private int mTitleTextColor;
    
    public Swatch(@ColorInt int param1Int1, int param1Int2) {
      this.mRed = Color.red(param1Int1);
      this.mGreen = Color.green(param1Int1);
      this.mBlue = Color.blue(param1Int1);
      this.mRgb = param1Int1;
      this.mPopulation = param1Int2;
    }
    
    Swatch(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.mRed = param1Int1;
      this.mGreen = param1Int2;
      this.mBlue = param1Int3;
      this.mRgb = Color.rgb(param1Int1, param1Int2, param1Int3);
      this.mPopulation = param1Int4;
    }
    
    Swatch(float[] param1ArrayOffloat, int param1Int) {
      this(ColorUtils.HSLToColor(param1ArrayOffloat), param1Int);
      this.mHsl = param1ArrayOffloat;
    }
    
    private void ensureTextColorsGenerated() {
      int i;
      int j;
      if (!this.mGeneratedTextColors) {
        i = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 4.5F);
        j = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 3.0F);
        if (i != -1 && j != -1) {
          this.mBodyTextColor = ColorUtils.setAlphaComponent(-1, i);
          this.mTitleTextColor = ColorUtils.setAlphaComponent(-1, j);
          this.mGeneratedTextColors = true;
          return;
        } 
      } else {
        return;
      } 
      int k = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 4.5F);
      int m = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 3.0F);
      if (k != -1 && k != -1) {
        this.mBodyTextColor = ColorUtils.setAlphaComponent(-16777216, k);
        this.mTitleTextColor = ColorUtils.setAlphaComponent(-16777216, m);
        this.mGeneratedTextColors = true;
        return;
      } 
      if (i != -1) {
        k = ColorUtils.setAlphaComponent(-1, i);
      } else {
        k = ColorUtils.setAlphaComponent(-16777216, k);
      } 
      this.mBodyTextColor = k;
      if (j != -1) {
        k = ColorUtils.setAlphaComponent(-1, j);
      } else {
        k = ColorUtils.setAlphaComponent(-16777216, m);
      } 
      this.mTitleTextColor = k;
      this.mGeneratedTextColors = true;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this != param1Object) {
        if (param1Object == null || getClass() != param1Object.getClass())
          return false; 
        param1Object = param1Object;
        if (this.mPopulation != ((Swatch)param1Object).mPopulation || this.mRgb != ((Swatch)param1Object).mRgb)
          bool = false; 
      } 
      return bool;
    }
    
    @ColorInt
    public int getBodyTextColor() {
      ensureTextColorsGenerated();
      return this.mBodyTextColor;
    }
    
    public float[] getHsl() {
      if (this.mHsl == null)
        this.mHsl = new float[3]; 
      ColorUtils.RGBToHSL(this.mRed, this.mGreen, this.mBlue, this.mHsl);
      return this.mHsl;
    }
    
    public int getPopulation() {
      return this.mPopulation;
    }
    
    @ColorInt
    public int getRgb() {
      return this.mRgb;
    }
    
    @ColorInt
    public int getTitleTextColor() {
      ensureTextColorsGenerated();
      return this.mTitleTextColor;
    }
    
    public int hashCode() {
      return this.mRgb * 31 + this.mPopulation;
    }
    
    public String toString() {
      return getClass().getSimpleName() + " [RGB: #" + Integer.toHexString(getRgb()) + ']' + " [HSL: " + Arrays.toString(getHsl()) + ']' + " [Population: " + this.mPopulation + ']' + " [Title Text: #" + Integer.toHexString(getTitleTextColor()) + ']' + " [Body Text: #" + Integer.toHexString(getBodyTextColor()) + ']';
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/graphics/Palette.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */