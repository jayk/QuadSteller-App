package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
  public static final int ARROW_DIRECTION_END = 3;
  
  public static final int ARROW_DIRECTION_LEFT = 0;
  
  public static final int ARROW_DIRECTION_RIGHT = 1;
  
  public static final int ARROW_DIRECTION_START = 2;
  
  private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
  
  private float mArrowHeadLength;
  
  private float mArrowShaftLength;
  
  private float mBarGap;
  
  private float mBarLength;
  
  private int mDirection = 2;
  
  private float mMaxCutForBarSize;
  
  private final Paint mPaint = new Paint();
  
  private final Path mPath = new Path();
  
  private float mProgress;
  
  private final int mSize;
  
  private boolean mSpin;
  
  private boolean mVerticalMirror = false;
  
  public DrawerArrowDrawable(Context paramContext) {
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mPaint.setStrokeJoin(Paint.Join.MITER);
    this.mPaint.setStrokeCap(Paint.Cap.BUTT);
    this.mPaint.setAntiAlias(true);
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
    setColor(typedArray.getColor(R.styleable.DrawerArrowToggle_color, 0));
    setBarThickness(typedArray.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
    setSpinEnabled(typedArray.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
    setGapSize(Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
    this.mSize = typedArray.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
    this.mBarLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
    this.mArrowHeadLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
    this.mArrowShaftLength = typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
    typedArray.recycle();
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getBounds : ()Landroid/graphics/Rect;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield mDirection : I
    //   9: tableswitch default -> 40, 0 -> 424, 1 -> 429, 2 -> 40, 3 -> 434
    //   40: aload_0
    //   41: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   44: iconst_1
    //   45: if_icmpne -> 451
    //   48: iconst_1
    //   49: istore_3
    //   50: aload_0
    //   51: getfield mArrowHeadLength : F
    //   54: aload_0
    //   55: getfield mArrowHeadLength : F
    //   58: fmul
    //   59: fconst_2
    //   60: fmul
    //   61: f2d
    //   62: invokestatic sqrt : (D)D
    //   65: d2f
    //   66: fstore #4
    //   68: aload_0
    //   69: getfield mBarLength : F
    //   72: fload #4
    //   74: aload_0
    //   75: getfield mProgress : F
    //   78: invokestatic lerp : (FFF)F
    //   81: fstore #5
    //   83: aload_0
    //   84: getfield mBarLength : F
    //   87: aload_0
    //   88: getfield mArrowShaftLength : F
    //   91: aload_0
    //   92: getfield mProgress : F
    //   95: invokestatic lerp : (FFF)F
    //   98: fstore #6
    //   100: fconst_0
    //   101: aload_0
    //   102: getfield mMaxCutForBarSize : F
    //   105: aload_0
    //   106: getfield mProgress : F
    //   109: invokestatic lerp : (FFF)F
    //   112: invokestatic round : (F)I
    //   115: i2f
    //   116: fstore #7
    //   118: fconst_0
    //   119: getstatic android/support/v7/graphics/drawable/DrawerArrowDrawable.ARROW_HEAD_ANGLE : F
    //   122: aload_0
    //   123: getfield mProgress : F
    //   126: invokestatic lerp : (FFF)F
    //   129: fstore #8
    //   131: iload_3
    //   132: ifeq -> 456
    //   135: fconst_0
    //   136: fstore #4
    //   138: iload_3
    //   139: ifeq -> 464
    //   142: ldc 180.0
    //   144: fstore #9
    //   146: fload #4
    //   148: fload #9
    //   150: aload_0
    //   151: getfield mProgress : F
    //   154: invokestatic lerp : (FFF)F
    //   157: fstore #4
    //   159: fload #5
    //   161: f2d
    //   162: fload #8
    //   164: f2d
    //   165: invokestatic cos : (D)D
    //   168: dmul
    //   169: invokestatic round : (D)J
    //   172: l2f
    //   173: fstore #9
    //   175: fload #5
    //   177: f2d
    //   178: fload #8
    //   180: f2d
    //   181: invokestatic sin : (D)D
    //   184: dmul
    //   185: invokestatic round : (D)J
    //   188: l2f
    //   189: fstore #10
    //   191: aload_0
    //   192: getfield mPath : Landroid/graphics/Path;
    //   195: invokevirtual rewind : ()V
    //   198: aload_0
    //   199: getfield mBarGap : F
    //   202: aload_0
    //   203: getfield mPaint : Landroid/graphics/Paint;
    //   206: invokevirtual getStrokeWidth : ()F
    //   209: fadd
    //   210: aload_0
    //   211: getfield mMaxCutForBarSize : F
    //   214: fneg
    //   215: aload_0
    //   216: getfield mProgress : F
    //   219: invokestatic lerp : (FFF)F
    //   222: fstore #5
    //   224: fload #6
    //   226: fneg
    //   227: fconst_2
    //   228: fdiv
    //   229: fstore #8
    //   231: aload_0
    //   232: getfield mPath : Landroid/graphics/Path;
    //   235: fload #8
    //   237: fload #7
    //   239: fadd
    //   240: fconst_0
    //   241: invokevirtual moveTo : (FF)V
    //   244: aload_0
    //   245: getfield mPath : Landroid/graphics/Path;
    //   248: fload #6
    //   250: fconst_2
    //   251: fload #7
    //   253: fmul
    //   254: fsub
    //   255: fconst_0
    //   256: invokevirtual rLineTo : (FF)V
    //   259: aload_0
    //   260: getfield mPath : Landroid/graphics/Path;
    //   263: fload #8
    //   265: fload #5
    //   267: invokevirtual moveTo : (FF)V
    //   270: aload_0
    //   271: getfield mPath : Landroid/graphics/Path;
    //   274: fload #9
    //   276: fload #10
    //   278: invokevirtual rLineTo : (FF)V
    //   281: aload_0
    //   282: getfield mPath : Landroid/graphics/Path;
    //   285: fload #8
    //   287: fload #5
    //   289: fneg
    //   290: invokevirtual moveTo : (FF)V
    //   293: aload_0
    //   294: getfield mPath : Landroid/graphics/Path;
    //   297: fload #9
    //   299: fload #10
    //   301: fneg
    //   302: invokevirtual rLineTo : (FF)V
    //   305: aload_0
    //   306: getfield mPath : Landroid/graphics/Path;
    //   309: invokevirtual close : ()V
    //   312: aload_1
    //   313: invokevirtual save : ()I
    //   316: pop
    //   317: aload_0
    //   318: getfield mPaint : Landroid/graphics/Paint;
    //   321: invokevirtual getStrokeWidth : ()F
    //   324: fstore #9
    //   326: aload_2
    //   327: invokevirtual height : ()I
    //   330: i2f
    //   331: ldc_w 3.0
    //   334: fload #9
    //   336: fmul
    //   337: fsub
    //   338: aload_0
    //   339: getfield mBarGap : F
    //   342: fconst_2
    //   343: fmul
    //   344: fsub
    //   345: f2i
    //   346: iconst_4
    //   347: idiv
    //   348: iconst_2
    //   349: imul
    //   350: i2f
    //   351: f2d
    //   352: fload #9
    //   354: f2d
    //   355: ldc2_w 1.5
    //   358: dmul
    //   359: aload_0
    //   360: getfield mBarGap : F
    //   363: f2d
    //   364: dadd
    //   365: dadd
    //   366: d2f
    //   367: fstore #9
    //   369: aload_1
    //   370: aload_2
    //   371: invokevirtual centerX : ()I
    //   374: i2f
    //   375: fload #9
    //   377: invokevirtual translate : (FF)V
    //   380: aload_0
    //   381: getfield mSpin : Z
    //   384: ifeq -> 475
    //   387: aload_0
    //   388: getfield mVerticalMirror : Z
    //   391: iload_3
    //   392: ixor
    //   393: ifeq -> 470
    //   396: iconst_m1
    //   397: istore_3
    //   398: aload_1
    //   399: iload_3
    //   400: i2f
    //   401: fload #4
    //   403: fmul
    //   404: invokevirtual rotate : (F)V
    //   407: aload_1
    //   408: aload_0
    //   409: getfield mPath : Landroid/graphics/Path;
    //   412: aload_0
    //   413: getfield mPaint : Landroid/graphics/Paint;
    //   416: invokevirtual drawPath : (Landroid/graphics/Path;Landroid/graphics/Paint;)V
    //   419: aload_1
    //   420: invokevirtual restore : ()V
    //   423: return
    //   424: iconst_0
    //   425: istore_3
    //   426: goto -> 50
    //   429: iconst_1
    //   430: istore_3
    //   431: goto -> 50
    //   434: aload_0
    //   435: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   438: ifne -> 446
    //   441: iconst_1
    //   442: istore_3
    //   443: goto -> 50
    //   446: iconst_0
    //   447: istore_3
    //   448: goto -> 443
    //   451: iconst_0
    //   452: istore_3
    //   453: goto -> 50
    //   456: ldc_w -180.0
    //   459: fstore #4
    //   461: goto -> 138
    //   464: fconst_0
    //   465: fstore #9
    //   467: goto -> 146
    //   470: iconst_1
    //   471: istore_3
    //   472: goto -> 398
    //   475: iload_3
    //   476: ifeq -> 407
    //   479: aload_1
    //   480: ldc 180.0
    //   482: invokevirtual rotate : (F)V
    //   485: goto -> 407
  }
  
  public float getArrowHeadLength() {
    return this.mArrowHeadLength;
  }
  
  public float getArrowShaftLength() {
    return this.mArrowShaftLength;
  }
  
  public float getBarLength() {
    return this.mBarLength;
  }
  
  public float getBarThickness() {
    return this.mPaint.getStrokeWidth();
  }
  
  @ColorInt
  public int getColor() {
    return this.mPaint.getColor();
  }
  
  public int getDirection() {
    return this.mDirection;
  }
  
  public float getGapSize() {
    return this.mBarGap;
  }
  
  public int getIntrinsicHeight() {
    return this.mSize;
  }
  
  public int getIntrinsicWidth() {
    return this.mSize;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public final Paint getPaint() {
    return this.mPaint;
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getProgress() {
    return this.mProgress;
  }
  
  public boolean isSpinEnabled() {
    return this.mSpin;
  }
  
  public void setAlpha(int paramInt) {
    if (paramInt != this.mPaint.getAlpha()) {
      this.mPaint.setAlpha(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setArrowHeadLength(float paramFloat) {
    if (this.mArrowHeadLength != paramFloat) {
      this.mArrowHeadLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setArrowShaftLength(float paramFloat) {
    if (this.mArrowShaftLength != paramFloat) {
      this.mArrowShaftLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarLength(float paramFloat) {
    if (this.mBarLength != paramFloat) {
      this.mBarLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarThickness(float paramFloat) {
    if (this.mPaint.getStrokeWidth() != paramFloat) {
      this.mPaint.setStrokeWidth(paramFloat);
      this.mMaxCutForBarSize = (float)((paramFloat / 2.0F) * Math.cos(ARROW_HEAD_ANGLE));
      invalidateSelf();
    } 
  }
  
  public void setColor(@ColorInt int paramInt) {
    if (paramInt != this.mPaint.getColor()) {
      this.mPaint.setColor(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDirection(int paramInt) {
    if (paramInt != this.mDirection) {
      this.mDirection = paramInt;
      invalidateSelf();
    } 
  }
  
  public void setGapSize(float paramFloat) {
    if (paramFloat != this.mBarGap) {
      this.mBarGap = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setProgress(@FloatRange(from = 0.0D, to = 1.0D) float paramFloat) {
    if (this.mProgress != paramFloat) {
      this.mProgress = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setSpinEnabled(boolean paramBoolean) {
    if (this.mSpin != paramBoolean) {
      this.mSpin = paramBoolean;
      invalidateSelf();
    } 
  }
  
  public void setVerticalMirror(boolean paramBoolean) {
    if (this.mVerticalMirror != paramBoolean) {
      this.mVerticalMirror = paramBoolean;
      invalidateSelf();
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface ArrowDirection {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/graphics/drawable/DrawerArrowDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */