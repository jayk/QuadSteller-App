package app.gamer.quadstellar.newdevices.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import app.gamer.quadstellar.R;

public class CircularSeekBar extends View {
  private static final int DEFAULT_CIRCLE_COLOR = -12303292;
  
  private static final int DEFAULT_CIRCLE_FILL_COLOR = 0;
  
  private static final int DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255);
  
  private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5.0F;
  
  private static final float DEFAULT_CIRCLE_X_RADIUS = 30.0F;
  
  private static final float DEFAULT_CIRCLE_Y_RADIUS = 30.0F;
  
  private static final float DEFAULT_END_ANGLE = 270.0F;
  
  private static final boolean DEFAULT_LOCK_ENABLED = true;
  
  private static final boolean DEFAULT_MAINTAIN_EQUAL_CIRCLE = true;
  
  private static final int DEFAULT_MAX = 100;
  
  private static final boolean DEFAULT_MOVE_OUTSIDE_CIRCLE = false;
  
  private static final int DEFAULT_POINTER_ALPHA = 135;
  
  private static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
  
  private static final int DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
  
  private static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2.0F;
  
  private static final int DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255);
  
  private static final int DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(135, 74, 138, 255);
  
  private static final float DEFAULT_POINTER_HALO_WIDTH = 6.0F;
  
  private static final float DEFAULT_POINTER_RADIUS = 7.0F;
  
  private static final int DEFAULT_PROGRESS = 0;
  
  private static final float DEFAULT_START_ANGLE = 270.0F;
  
  private static final boolean DEFAULT_USE_CUSTOM_RADII = false;
  
  private final float DPTOPX_SCALE = (getResources().getDisplayMetrics()).density;
  
  private final float MIN_TOUCH_TARGET_DP = 48.0F;
  
  private float ccwDistanceFromEnd;
  
  private float ccwDistanceFromPointer;
  
  private float ccwDistanceFromStart;
  
  private float cwDistanceFromEnd;
  
  private float cwDistanceFromPointer;
  
  private float cwDistanceFromStart;
  
  private float lastCWDistanceFromStart;
  
  private boolean lockAtEnd = false;
  
  private boolean lockAtStart = true;
  
  private boolean lockEnabled = true;
  
  private int mCircleColor = -12303292;
  
  private int mCircleFillColor = 0;
  
  private Paint mCircleFillPaint;
  
  private float mCircleHeight;
  
  private Paint mCirclePaint;
  
  private Path mCirclePath;
  
  private int mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;
  
  private Paint mCircleProgressGlowPaint;
  
  private Paint mCircleProgressPaint;
  
  private Path mCircleProgressPath;
  
  private RectF mCircleRectF = new RectF();
  
  private float mCircleStrokeWidth;
  
  private float mCircleWidth;
  
  private float mCircleXRadius;
  
  private float mCircleYRadius;
  
  private boolean mCustomRadii;
  
  private float mEndAngle;
  
  private boolean mIsMovingCW;
  
  private boolean mMaintainEqualCircle;
  
  private int mMax;
  
  private boolean mMoveOutsideCircle;
  
  private OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;
  
  private int mPointerAlpha = 135;
  
  private int mPointerAlphaOnTouch = 100;
  
  private int mPointerColor = DEFAULT_POINTER_COLOR;
  
  private Paint mPointerHaloBorderPaint;
  
  private float mPointerHaloBorderWidth;
  
  private int mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;
  
  private int mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
  
  private Paint mPointerHaloPaint;
  
  private float mPointerHaloWidth;
  
  private Paint mPointerPaint;
  
  private float mPointerPosition;
  
  private float[] mPointerPositionXY = new float[2];
  
  private float mPointerRadius;
  
  private int mProgress;
  
  private float mProgressDegrees;
  
  private float mStartAngle;
  
  private float mTotalCircleDegrees;
  
  private boolean mUserIsMovingPointer = false;
  
  public CircularSeekBar(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null, 0);
  }
  
  public CircularSeekBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet, 0);
  }
  
  public CircularSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet, paramInt);
  }
  
  private void calculatePointerAngle() {
    float f = this.mProgress / this.mMax;
    this.mPointerPosition = this.mTotalCircleDegrees * f + this.mStartAngle;
    this.mPointerPosition %= 360.0F;
  }
  
  private void calculatePointerXYPosition() {
    PathMeasure pathMeasure = new PathMeasure(this.mCircleProgressPath, false);
    if (!pathMeasure.getPosTan(pathMeasure.getLength(), this.mPointerPositionXY, null))
      (new PathMeasure(this.mCirclePath, false)).getPosTan(0.0F, this.mPointerPositionXY, null); 
  }
  
  private void calculateProgressDegrees() {
    float f;
    this.mProgressDegrees = this.mPointerPosition - this.mStartAngle;
    if (this.mProgressDegrees < 0.0F) {
      f = 360.0F + this.mProgressDegrees;
    } else {
      f = this.mProgressDegrees;
    } 
    this.mProgressDegrees = f;
  }
  
  private void calculateTotalDegrees() {
    this.mTotalCircleDegrees = (360.0F - this.mStartAngle - this.mEndAngle) % 360.0F;
    if (this.mTotalCircleDegrees <= 0.0F)
      this.mTotalCircleDegrees = 360.0F; 
  }
  
  private void init(AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.CircularSeekBar, paramInt, 0);
    initAttributes(typedArray);
    typedArray.recycle();
    initPaints();
  }
  
  private void initAttributes(TypedArray paramTypedArray) {
    this.mCircleXRadius = paramTypedArray.getFloat(6, 30.0F) * this.DPTOPX_SCALE;
    this.mCircleYRadius = paramTypedArray.getFloat(7, 30.0F) * this.DPTOPX_SCALE;
    this.mPointerRadius = paramTypedArray.getFloat(9, 7.0F) * this.DPTOPX_SCALE;
    this.mPointerHaloWidth = paramTypedArray.getFloat(10, 6.0F) * this.DPTOPX_SCALE;
    this.mPointerHaloBorderWidth = paramTypedArray.getFloat(11, 2.0F) * this.DPTOPX_SCALE;
    this.mCircleStrokeWidth = paramTypedArray.getFloat(8, 5.0F) * this.DPTOPX_SCALE;
    String str = paramTypedArray.getString(14);
    if (str != null)
      try {
        this.mPointerColor = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mPointerColor = DEFAULT_POINTER_COLOR;
      }  
    str = paramTypedArray.getString(15);
    if (str != null)
      try {
        this.mPointerHaloColor = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;
      }  
    str = paramTypedArray.getString(16);
    if (str != null)
      try {
        this.mPointerHaloColorOnTouch = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
      }  
    str = paramTypedArray.getString(12);
    if (str != null)
      try {
        this.mCircleColor = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mCircleColor = -12303292;
      }  
    str = paramTypedArray.getString(13);
    if (str != null)
      try {
        this.mCircleProgressColor = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;
      }  
    str = paramTypedArray.getString(20);
    if (str != null)
      try {
        this.mCircleFillColor = Color.parseColor(str);
      } catch (IllegalArgumentException illegalArgumentException) {
        this.mCircleFillColor = 0;
      }  
    this.mPointerAlpha = Color.alpha(this.mPointerHaloColor);
    this.mPointerAlphaOnTouch = paramTypedArray.getInt(17, 100);
    if (this.mPointerAlphaOnTouch > 255 || this.mPointerAlphaOnTouch < 0)
      this.mPointerAlphaOnTouch = 100; 
    this.mMax = paramTypedArray.getInt(1, 100);
    this.mProgress = paramTypedArray.getInt(0, 0);
    this.mCustomRadii = paramTypedArray.getBoolean(4, false);
    this.mMaintainEqualCircle = paramTypedArray.getBoolean(3, true);
    this.mMoveOutsideCircle = paramTypedArray.getBoolean(2, false);
    this.lockEnabled = paramTypedArray.getBoolean(5, true);
    this.mStartAngle = (paramTypedArray.getFloat(18, 270.0F) % 360.0F + 360.0F) % 360.0F;
    this.mEndAngle = (paramTypedArray.getFloat(19, 270.0F) % 360.0F + 360.0F) % 360.0F;
    if (this.mStartAngle == this.mEndAngle)
      this.mEndAngle -= 0.1F; 
  }
  
  private void initPaints() {
    this.mCirclePaint = new Paint();
    this.mCirclePaint.setAntiAlias(true);
    this.mCirclePaint.setDither(true);
    this.mCirclePaint.setColor(this.mCircleColor);
    this.mCirclePaint.setStrokeWidth(this.mCircleStrokeWidth);
    this.mCirclePaint.setStyle(Paint.Style.STROKE);
    this.mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
    this.mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    this.mCircleFillPaint = new Paint();
    this.mCircleFillPaint.setAntiAlias(true);
    this.mCircleFillPaint.setDither(true);
    this.mCircleFillPaint.setColor(this.mCircleFillColor);
    this.mCircleFillPaint.setStyle(Paint.Style.FILL);
    this.mCircleProgressPaint = new Paint();
    this.mCircleProgressPaint.setAntiAlias(true);
    this.mCircleProgressPaint.setDither(true);
    this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
    this.mCircleProgressPaint.setStrokeWidth(this.mCircleStrokeWidth);
    this.mCircleProgressPaint.setStyle(Paint.Style.STROKE);
    this.mCircleProgressPaint.setStrokeJoin(Paint.Join.ROUND);
    this.mCircleProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    this.mCircleProgressGlowPaint = new Paint();
    this.mCircleProgressGlowPaint.set(this.mCircleProgressPaint);
    this.mCircleProgressGlowPaint.setMaskFilter((MaskFilter)new BlurMaskFilter(5.0F * this.DPTOPX_SCALE, BlurMaskFilter.Blur.NORMAL));
    this.mPointerPaint = new Paint();
    this.mPointerPaint.setAntiAlias(true);
    this.mPointerPaint.setDither(true);
    this.mPointerPaint.setStyle(Paint.Style.FILL);
    this.mPointerPaint.setColor(this.mPointerColor);
    this.mPointerPaint.setStrokeWidth(this.mPointerRadius);
    this.mPointerHaloPaint = new Paint();
    this.mPointerHaloPaint.set(this.mPointerPaint);
    this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
    this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
    this.mPointerHaloPaint.setStrokeWidth(this.mPointerRadius + this.mPointerHaloWidth);
    this.mPointerHaloBorderPaint = new Paint();
    this.mPointerHaloBorderPaint.set(this.mPointerPaint);
    this.mPointerHaloBorderPaint.setStrokeWidth(this.mPointerHaloBorderWidth);
    this.mPointerHaloBorderPaint.setStyle(Paint.Style.STROKE);
  }
  
  private void initPaths() {
    this.mCirclePath = new Path();
    this.mCirclePath.addArc(this.mCircleRectF, this.mStartAngle, this.mTotalCircleDegrees);
    this.mCircleProgressPath = new Path();
    this.mCircleProgressPath.addArc(this.mCircleRectF, this.mStartAngle, this.mProgressDegrees);
  }
  
  private void initRects() {
    this.mCircleRectF.set(-this.mCircleWidth, -this.mCircleHeight, this.mCircleWidth, this.mCircleHeight);
  }
  
  private void recalculateAll() {
    calculateTotalDegrees();
    calculatePointerAngle();
    calculateProgressDegrees();
    initRects();
    initPaths();
    calculatePointerXYPosition();
  }
  
  private void setProgressBasedOnAngle(float paramFloat) {
    this.mPointerPosition = paramFloat;
    calculateProgressDegrees();
    this.mProgress = Math.round(this.mMax * this.mProgressDegrees / this.mTotalCircleDegrees);
  }
  
  public int getCircleColor() {
    return this.mCircleColor;
  }
  
  public int getCircleFillColor() {
    return this.mCircleFillColor;
  }
  
  public int getCircleProgressColor() {
    return this.mCircleProgressColor;
  }
  
  public int getMax() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mMax : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int getPointerAlpha() {
    return this.mPointerAlpha;
  }
  
  public int getPointerAlphaOnTouch() {
    return this.mPointerAlphaOnTouch;
  }
  
  public int getPointerColor() {
    return this.mPointerColor;
  }
  
  public int getPointerHaloColor() {
    return this.mPointerHaloColor;
  }
  
  public int getProgress() {
    return Math.round(this.mMax * this.mProgressDegrees / this.mTotalCircleDegrees);
  }
  
  public boolean isLockEnabled() {
    return this.lockEnabled;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    paramCanvas.translate((getWidth() / 2), (getHeight() / 2));
    paramCanvas.drawPath(this.mCirclePath, this.mCirclePaint);
    paramCanvas.drawPath(this.mCircleProgressPath, this.mCircleProgressGlowPaint);
    paramCanvas.drawPath(this.mCircleProgressPath, this.mCircleProgressPaint);
    paramCanvas.drawPath(this.mCirclePath, this.mCircleFillPaint);
    paramCanvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius + this.mPointerHaloWidth, this.mPointerHaloPaint);
    paramCanvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius, this.mPointerPaint);
    if (this.mUserIsMovingPointer)
      paramCanvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius + this.mPointerHaloWidth + this.mPointerHaloBorderWidth / 2.0F, this.mPointerHaloBorderPaint); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    paramInt2 = getDefaultSize(getSuggestedMinimumHeight(), paramInt2);
    paramInt1 = getDefaultSize(getSuggestedMinimumWidth(), paramInt1);
    if (this.mMaintainEqualCircle) {
      int i = Math.min(paramInt1, paramInt2);
      setMeasuredDimension(i, i);
    } else {
      setMeasuredDimension(paramInt1, paramInt2);
    } 
    this.mCircleHeight = paramInt2 / 2.0F - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5F;
    this.mCircleWidth = paramInt1 / 2.0F - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5F;
    if (this.mCustomRadii) {
      if (this.mCircleYRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth < this.mCircleHeight)
        this.mCircleHeight = this.mCircleYRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5F; 
      if (this.mCircleXRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth < this.mCircleWidth)
        this.mCircleWidth = this.mCircleXRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5F; 
    } 
    if (this.mMaintainEqualCircle) {
      float f = Math.min(this.mCircleHeight, this.mCircleWidth);
      this.mCircleHeight = f;
      this.mCircleWidth = f;
    } 
    recalculateAll();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    Bundle bundle = (Bundle)paramParcelable;
    super.onRestoreInstanceState(bundle.getParcelable("PARENT"));
    this.mMax = bundle.getInt("MAX");
    this.mProgress = bundle.getInt("PROGRESS");
    this.mCircleColor = bundle.getInt("mCircleColor");
    this.mCircleProgressColor = bundle.getInt("mCircleProgressColor");
    this.mPointerColor = bundle.getInt("mPointerColor");
    this.mPointerHaloColor = bundle.getInt("mPointerHaloColor");
    this.mPointerHaloColorOnTouch = bundle.getInt("mPointerHaloColorOnTouch");
    this.mPointerAlpha = bundle.getInt("mPointerAlpha");
    this.mPointerAlphaOnTouch = bundle.getInt("mPointerAlphaOnTouch");
    this.lockEnabled = bundle.getBoolean("lockEnabled");
    initPaints();
    recalculateAll();
  }
  
  protected Parcelable onSaveInstanceState() {
    Parcelable parcelable = super.onSaveInstanceState();
    Bundle bundle = new Bundle();
    bundle.putParcelable("PARENT", parcelable);
    bundle.putInt("MAX", this.mMax);
    bundle.putInt("PROGRESS", this.mProgress);
    bundle.putInt("mCircleColor", this.mCircleColor);
    bundle.putInt("mCircleProgressColor", this.mCircleProgressColor);
    bundle.putInt("mPointerColor", this.mPointerColor);
    bundle.putInt("mPointerHaloColor", this.mPointerHaloColor);
    bundle.putInt("mPointerHaloColorOnTouch", this.mPointerHaloColorOnTouch);
    bundle.putInt("mPointerAlpha", this.mPointerAlpha);
    bundle.putInt("mPointerAlphaOnTouch", this.mPointerAlphaOnTouch);
    bundle.putBoolean("lockEnabled", this.lockEnabled);
    return (Parcelable)bundle;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getX : ()F
    //   4: aload_0
    //   5: invokevirtual getWidth : ()I
    //   8: iconst_2
    //   9: idiv
    //   10: i2f
    //   11: fsub
    //   12: fstore_2
    //   13: aload_1
    //   14: invokevirtual getY : ()F
    //   17: aload_0
    //   18: invokevirtual getHeight : ()I
    //   21: iconst_2
    //   22: idiv
    //   23: i2f
    //   24: fsub
    //   25: fstore_3
    //   26: aload_0
    //   27: getfield mCircleRectF : Landroid/graphics/RectF;
    //   30: invokevirtual centerX : ()F
    //   33: fstore #4
    //   35: aload_0
    //   36: getfield mCircleRectF : Landroid/graphics/RectF;
    //   39: invokevirtual centerY : ()F
    //   42: fstore #5
    //   44: fload #4
    //   46: fload_2
    //   47: fsub
    //   48: f2d
    //   49: ldc2_w 2.0
    //   52: invokestatic pow : (DD)D
    //   55: fload #5
    //   57: fload_3
    //   58: fsub
    //   59: f2d
    //   60: ldc2_w 2.0
    //   63: invokestatic pow : (DD)D
    //   66: dadd
    //   67: invokestatic sqrt : (D)D
    //   70: d2f
    //   71: fstore #5
    //   73: ldc 48.0
    //   75: aload_0
    //   76: getfield DPTOPX_SCALE : F
    //   79: fmul
    //   80: fstore #6
    //   82: aload_0
    //   83: getfield mCircleStrokeWidth : F
    //   86: fload #6
    //   88: fcmpg
    //   89: ifge -> 339
    //   92: fload #6
    //   94: fconst_2
    //   95: fdiv
    //   96: fstore #4
    //   98: aload_0
    //   99: getfield mCircleHeight : F
    //   102: aload_0
    //   103: getfield mCircleWidth : F
    //   106: invokestatic max : (FF)F
    //   109: fload #4
    //   111: fadd
    //   112: fstore #7
    //   114: aload_0
    //   115: getfield mCircleHeight : F
    //   118: aload_0
    //   119: getfield mCircleWidth : F
    //   122: invokestatic min : (FF)F
    //   125: fload #4
    //   127: fsub
    //   128: fstore #8
    //   130: aload_0
    //   131: getfield mPointerRadius : F
    //   134: fload #6
    //   136: fconst_2
    //   137: fdiv
    //   138: fcmpg
    //   139: ifge -> 350
    //   142: fload #6
    //   144: fconst_2
    //   145: fdiv
    //   146: fstore #4
    //   148: fload_3
    //   149: f2d
    //   150: fload_2
    //   151: f2d
    //   152: invokestatic atan2 : (DD)D
    //   155: ldc2_w 3.141592653589793
    //   158: ddiv
    //   159: ldc2_w 180.0
    //   162: dmul
    //   163: ldc2_w 360.0
    //   166: drem
    //   167: d2f
    //   168: fstore_3
    //   169: fload_3
    //   170: fstore #4
    //   172: fload_3
    //   173: fconst_0
    //   174: fcmpg
    //   175: ifge -> 184
    //   178: fload_3
    //   179: ldc 360.0
    //   181: fadd
    //   182: fstore #4
    //   184: aload_0
    //   185: fload #4
    //   187: aload_0
    //   188: getfield mStartAngle : F
    //   191: fsub
    //   192: putfield cwDistanceFromStart : F
    //   195: aload_0
    //   196: getfield cwDistanceFromStart : F
    //   199: fconst_0
    //   200: fcmpg
    //   201: ifge -> 359
    //   204: ldc 360.0
    //   206: aload_0
    //   207: getfield cwDistanceFromStart : F
    //   210: fadd
    //   211: fstore_3
    //   212: aload_0
    //   213: fload_3
    //   214: putfield cwDistanceFromStart : F
    //   217: aload_0
    //   218: ldc 360.0
    //   220: aload_0
    //   221: getfield cwDistanceFromStart : F
    //   224: fsub
    //   225: putfield ccwDistanceFromStart : F
    //   228: aload_0
    //   229: fload #4
    //   231: aload_0
    //   232: getfield mEndAngle : F
    //   235: fsub
    //   236: putfield cwDistanceFromEnd : F
    //   239: aload_0
    //   240: getfield cwDistanceFromEnd : F
    //   243: fconst_0
    //   244: fcmpg
    //   245: ifge -> 367
    //   248: ldc 360.0
    //   250: aload_0
    //   251: getfield cwDistanceFromEnd : F
    //   254: fadd
    //   255: fstore_3
    //   256: aload_0
    //   257: fload_3
    //   258: putfield cwDistanceFromEnd : F
    //   261: aload_0
    //   262: ldc 360.0
    //   264: aload_0
    //   265: getfield cwDistanceFromEnd : F
    //   268: fsub
    //   269: putfield ccwDistanceFromEnd : F
    //   272: aload_1
    //   273: invokevirtual getAction : ()I
    //   276: tableswitch default -> 308, 0 -> 375, 1 -> 1156, 2 -> 724, 3 -> 1220
    //   308: aload_1
    //   309: invokevirtual getAction : ()I
    //   312: iconst_2
    //   313: if_icmpne -> 333
    //   316: aload_0
    //   317: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   320: ifnull -> 333
    //   323: aload_0
    //   324: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   327: iconst_1
    //   328: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   333: iconst_1
    //   334: istore #9
    //   336: iload #9
    //   338: ireturn
    //   339: aload_0
    //   340: getfield mCircleStrokeWidth : F
    //   343: fconst_2
    //   344: fdiv
    //   345: fstore #4
    //   347: goto -> 98
    //   350: aload_0
    //   351: getfield mPointerRadius : F
    //   354: fstore #4
    //   356: goto -> 148
    //   359: aload_0
    //   360: getfield cwDistanceFromStart : F
    //   363: fstore_3
    //   364: goto -> 212
    //   367: aload_0
    //   368: getfield cwDistanceFromEnd : F
    //   371: fstore_3
    //   372: goto -> 256
    //   375: aload_0
    //   376: getfield mPointerRadius : F
    //   379: ldc_w 180.0
    //   382: fmul
    //   383: f2d
    //   384: ldc2_w 3.141592653589793
    //   387: aload_0
    //   388: getfield mCircleHeight : F
    //   391: aload_0
    //   392: getfield mCircleWidth : F
    //   395: invokestatic max : (FF)F
    //   398: f2d
    //   399: dmul
    //   400: ddiv
    //   401: d2f
    //   402: fstore_2
    //   403: aload_0
    //   404: fload #4
    //   406: aload_0
    //   407: getfield mPointerPosition : F
    //   410: fsub
    //   411: putfield cwDistanceFromPointer : F
    //   414: aload_0
    //   415: getfield cwDistanceFromPointer : F
    //   418: fconst_0
    //   419: fcmpg
    //   420: ifge -> 567
    //   423: ldc 360.0
    //   425: aload_0
    //   426: getfield cwDistanceFromPointer : F
    //   429: fadd
    //   430: fstore_3
    //   431: aload_0
    //   432: fload_3
    //   433: putfield cwDistanceFromPointer : F
    //   436: aload_0
    //   437: ldc 360.0
    //   439: aload_0
    //   440: getfield cwDistanceFromPointer : F
    //   443: fsub
    //   444: putfield ccwDistanceFromPointer : F
    //   447: fload #5
    //   449: fload #8
    //   451: fcmpl
    //   452: iflt -> 575
    //   455: fload #5
    //   457: fload #7
    //   459: fcmpg
    //   460: ifgt -> 575
    //   463: aload_0
    //   464: getfield cwDistanceFromPointer : F
    //   467: fload_2
    //   468: fcmpg
    //   469: ifle -> 481
    //   472: aload_0
    //   473: getfield ccwDistanceFromPointer : F
    //   476: fload_2
    //   477: fcmpg
    //   478: ifgt -> 575
    //   481: aload_0
    //   482: aload_0
    //   483: getfield mPointerPosition : F
    //   486: invokespecial setProgressBasedOnAngle : (F)V
    //   489: aload_0
    //   490: aload_0
    //   491: getfield cwDistanceFromStart : F
    //   494: putfield lastCWDistanceFromStart : F
    //   497: aload_0
    //   498: iconst_1
    //   499: putfield mIsMovingCW : Z
    //   502: aload_0
    //   503: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   506: aload_0
    //   507: getfield mPointerAlphaOnTouch : I
    //   510: invokevirtual setAlpha : (I)V
    //   513: aload_0
    //   514: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   517: aload_0
    //   518: getfield mPointerHaloColorOnTouch : I
    //   521: invokevirtual setColor : (I)V
    //   524: aload_0
    //   525: invokespecial recalculateAll : ()V
    //   528: aload_0
    //   529: invokevirtual invalidate : ()V
    //   532: aload_0
    //   533: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   536: ifnull -> 549
    //   539: aload_0
    //   540: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   543: aload_0
    //   544: invokeinterface onStartTrackingTouch : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;)V
    //   549: aload_0
    //   550: iconst_1
    //   551: putfield mUserIsMovingPointer : Z
    //   554: aload_0
    //   555: iconst_0
    //   556: putfield lockAtEnd : Z
    //   559: aload_0
    //   560: iconst_0
    //   561: putfield lockAtStart : Z
    //   564: goto -> 308
    //   567: aload_0
    //   568: getfield cwDistanceFromPointer : F
    //   571: fstore_3
    //   572: goto -> 431
    //   575: aload_0
    //   576: getfield cwDistanceFromStart : F
    //   579: aload_0
    //   580: getfield mTotalCircleDegrees : F
    //   583: fcmpl
    //   584: ifle -> 598
    //   587: aload_0
    //   588: iconst_0
    //   589: putfield mUserIsMovingPointer : Z
    //   592: iconst_0
    //   593: istore #9
    //   595: goto -> 336
    //   598: fload #5
    //   600: fload #8
    //   602: fcmpl
    //   603: iflt -> 713
    //   606: fload #5
    //   608: fload #7
    //   610: fcmpg
    //   611: ifgt -> 713
    //   614: aload_0
    //   615: fload #4
    //   617: invokespecial setProgressBasedOnAngle : (F)V
    //   620: aload_0
    //   621: aload_0
    //   622: getfield cwDistanceFromStart : F
    //   625: putfield lastCWDistanceFromStart : F
    //   628: aload_0
    //   629: iconst_1
    //   630: putfield mIsMovingCW : Z
    //   633: aload_0
    //   634: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   637: aload_0
    //   638: getfield mPointerAlphaOnTouch : I
    //   641: invokevirtual setAlpha : (I)V
    //   644: aload_0
    //   645: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   648: aload_0
    //   649: getfield mPointerHaloColorOnTouch : I
    //   652: invokevirtual setColor : (I)V
    //   655: aload_0
    //   656: invokespecial recalculateAll : ()V
    //   659: aload_0
    //   660: invokevirtual invalidate : ()V
    //   663: aload_0
    //   664: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   667: ifnull -> 695
    //   670: aload_0
    //   671: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   674: aload_0
    //   675: invokeinterface onStartTrackingTouch : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;)V
    //   680: aload_0
    //   681: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   684: aload_0
    //   685: aload_0
    //   686: getfield mProgress : I
    //   689: iconst_1
    //   690: invokeinterface onProgressChanged : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;IZ)V
    //   695: aload_0
    //   696: iconst_1
    //   697: putfield mUserIsMovingPointer : Z
    //   700: aload_0
    //   701: iconst_0
    //   702: putfield lockAtEnd : Z
    //   705: aload_0
    //   706: iconst_0
    //   707: putfield lockAtStart : Z
    //   710: goto -> 308
    //   713: aload_0
    //   714: iconst_0
    //   715: putfield mUserIsMovingPointer : Z
    //   718: iconst_0
    //   719: istore #9
    //   721: goto -> 336
    //   724: aload_0
    //   725: getfield mUserIsMovingPointer : Z
    //   728: ifeq -> 1150
    //   731: aload_0
    //   732: getfield lastCWDistanceFromStart : F
    //   735: aload_0
    //   736: getfield cwDistanceFromStart : F
    //   739: fcmpg
    //   740: ifge -> 985
    //   743: aload_0
    //   744: getfield cwDistanceFromStart : F
    //   747: aload_0
    //   748: getfield lastCWDistanceFromStart : F
    //   751: fsub
    //   752: ldc_w 180.0
    //   755: fcmpl
    //   756: ifle -> 977
    //   759: aload_0
    //   760: getfield mIsMovingCW : Z
    //   763: ifne -> 977
    //   766: aload_0
    //   767: iconst_1
    //   768: putfield lockAtStart : Z
    //   771: aload_0
    //   772: iconst_0
    //   773: putfield lockAtEnd : Z
    //   776: aload_0
    //   777: getfield lockAtStart : Z
    //   780: ifeq -> 795
    //   783: aload_0
    //   784: getfield mIsMovingCW : Z
    //   787: ifeq -> 795
    //   790: aload_0
    //   791: iconst_0
    //   792: putfield lockAtStart : Z
    //   795: aload_0
    //   796: getfield lockAtEnd : Z
    //   799: ifeq -> 814
    //   802: aload_0
    //   803: getfield mIsMovingCW : Z
    //   806: ifne -> 814
    //   809: aload_0
    //   810: iconst_0
    //   811: putfield lockAtEnd : Z
    //   814: aload_0
    //   815: getfield lockAtStart : Z
    //   818: ifeq -> 844
    //   821: aload_0
    //   822: getfield mIsMovingCW : Z
    //   825: ifne -> 844
    //   828: aload_0
    //   829: getfield ccwDistanceFromStart : F
    //   832: ldc_w 90.0
    //   835: fcmpl
    //   836: ifle -> 844
    //   839: aload_0
    //   840: iconst_0
    //   841: putfield lockAtStart : Z
    //   844: aload_0
    //   845: getfield lockAtEnd : Z
    //   848: ifeq -> 874
    //   851: aload_0
    //   852: getfield mIsMovingCW : Z
    //   855: ifeq -> 874
    //   858: aload_0
    //   859: getfield cwDistanceFromEnd : F
    //   862: ldc_w 90.0
    //   865: fcmpl
    //   866: ifle -> 874
    //   869: aload_0
    //   870: iconst_0
    //   871: putfield lockAtEnd : Z
    //   874: aload_0
    //   875: getfield lockAtEnd : Z
    //   878: ifne -> 917
    //   881: aload_0
    //   882: getfield cwDistanceFromStart : F
    //   885: aload_0
    //   886: getfield mTotalCircleDegrees : F
    //   889: fcmpl
    //   890: ifle -> 917
    //   893: aload_0
    //   894: getfield mIsMovingCW : Z
    //   897: ifeq -> 917
    //   900: aload_0
    //   901: getfield lastCWDistanceFromStart : F
    //   904: aload_0
    //   905: getfield mTotalCircleDegrees : F
    //   908: fcmpg
    //   909: ifge -> 917
    //   912: aload_0
    //   913: iconst_1
    //   914: putfield lockAtEnd : Z
    //   917: aload_0
    //   918: getfield lockAtStart : Z
    //   921: ifeq -> 1029
    //   924: aload_0
    //   925: getfield lockEnabled : Z
    //   928: ifeq -> 1029
    //   931: aload_0
    //   932: iconst_0
    //   933: putfield mProgress : I
    //   936: aload_0
    //   937: invokespecial recalculateAll : ()V
    //   940: aload_0
    //   941: invokevirtual invalidate : ()V
    //   944: aload_0
    //   945: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   948: ifnull -> 966
    //   951: aload_0
    //   952: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   955: aload_0
    //   956: aload_0
    //   957: getfield mProgress : I
    //   960: iconst_1
    //   961: invokeinterface onProgressChanged : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;IZ)V
    //   966: aload_0
    //   967: aload_0
    //   968: getfield cwDistanceFromStart : F
    //   971: putfield lastCWDistanceFromStart : F
    //   974: goto -> 308
    //   977: aload_0
    //   978: iconst_1
    //   979: putfield mIsMovingCW : Z
    //   982: goto -> 776
    //   985: aload_0
    //   986: getfield lastCWDistanceFromStart : F
    //   989: aload_0
    //   990: getfield cwDistanceFromStart : F
    //   993: fsub
    //   994: ldc_w 180.0
    //   997: fcmpl
    //   998: ifle -> 1021
    //   1001: aload_0
    //   1002: getfield mIsMovingCW : Z
    //   1005: ifeq -> 1021
    //   1008: aload_0
    //   1009: iconst_1
    //   1010: putfield lockAtEnd : Z
    //   1013: aload_0
    //   1014: iconst_0
    //   1015: putfield lockAtStart : Z
    //   1018: goto -> 776
    //   1021: aload_0
    //   1022: iconst_0
    //   1023: putfield mIsMovingCW : Z
    //   1026: goto -> 776
    //   1029: aload_0
    //   1030: getfield lockAtEnd : Z
    //   1033: ifeq -> 1084
    //   1036: aload_0
    //   1037: getfield lockEnabled : Z
    //   1040: ifeq -> 1084
    //   1043: aload_0
    //   1044: aload_0
    //   1045: getfield mMax : I
    //   1048: putfield mProgress : I
    //   1051: aload_0
    //   1052: invokespecial recalculateAll : ()V
    //   1055: aload_0
    //   1056: invokevirtual invalidate : ()V
    //   1059: aload_0
    //   1060: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1063: ifnull -> 966
    //   1066: aload_0
    //   1067: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1070: aload_0
    //   1071: aload_0
    //   1072: getfield mProgress : I
    //   1075: iconst_1
    //   1076: invokeinterface onProgressChanged : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;IZ)V
    //   1081: goto -> 966
    //   1084: aload_0
    //   1085: getfield mMoveOutsideCircle : Z
    //   1088: ifne -> 1099
    //   1091: fload #5
    //   1093: fload #7
    //   1095: fcmpg
    //   1096: ifgt -> 308
    //   1099: aload_0
    //   1100: getfield cwDistanceFromStart : F
    //   1103: aload_0
    //   1104: getfield mTotalCircleDegrees : F
    //   1107: fcmpl
    //   1108: ifgt -> 1117
    //   1111: aload_0
    //   1112: fload #4
    //   1114: invokespecial setProgressBasedOnAngle : (F)V
    //   1117: aload_0
    //   1118: invokespecial recalculateAll : ()V
    //   1121: aload_0
    //   1122: invokevirtual invalidate : ()V
    //   1125: aload_0
    //   1126: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1129: ifnull -> 966
    //   1132: aload_0
    //   1133: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1136: aload_0
    //   1137: aload_0
    //   1138: getfield mProgress : I
    //   1141: iconst_1
    //   1142: invokeinterface onProgressChanged : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;IZ)V
    //   1147: goto -> 966
    //   1150: iconst_0
    //   1151: istore #9
    //   1153: goto -> 336
    //   1156: aload_0
    //   1157: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   1160: aload_0
    //   1161: getfield mPointerAlpha : I
    //   1164: invokevirtual setAlpha : (I)V
    //   1167: aload_0
    //   1168: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   1171: aload_0
    //   1172: getfield mPointerHaloColor : I
    //   1175: invokevirtual setColor : (I)V
    //   1178: aload_0
    //   1179: getfield mUserIsMovingPointer : Z
    //   1182: ifeq -> 1214
    //   1185: aload_0
    //   1186: iconst_0
    //   1187: putfield mUserIsMovingPointer : Z
    //   1190: aload_0
    //   1191: invokevirtual invalidate : ()V
    //   1194: aload_0
    //   1195: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1198: ifnull -> 308
    //   1201: aload_0
    //   1202: getfield mOnCircularSeekBarChangeListener : Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar$OnCircularSeekBarChangeListener;
    //   1205: aload_0
    //   1206: invokeinterface onStopTrackingTouch : (Lapp/gamer/quadstellar/newdevices/view/CircularSeekBar;)V
    //   1211: goto -> 308
    //   1214: iconst_0
    //   1215: istore #9
    //   1217: goto -> 336
    //   1220: aload_0
    //   1221: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   1224: aload_0
    //   1225: getfield mPointerAlpha : I
    //   1228: invokevirtual setAlpha : (I)V
    //   1231: aload_0
    //   1232: getfield mPointerHaloPaint : Landroid/graphics/Paint;
    //   1235: aload_0
    //   1236: getfield mPointerHaloColor : I
    //   1239: invokevirtual setColor : (I)V
    //   1242: aload_0
    //   1243: iconst_0
    //   1244: putfield mUserIsMovingPointer : Z
    //   1247: aload_0
    //   1248: invokevirtual invalidate : ()V
    //   1251: goto -> 308
  }
  
  public void setCircleColor(int paramInt) {
    this.mCircleColor = paramInt;
    this.mCirclePaint.setColor(this.mCircleColor);
    invalidate();
  }
  
  public void setCircleFillColor(int paramInt) {
    this.mCircleFillColor = paramInt;
    this.mCircleFillPaint.setColor(this.mCircleFillColor);
    invalidate();
  }
  
  public void setCircleProgressColor(int paramInt) {
    this.mCircleProgressColor = paramInt;
    this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
    invalidate();
  }
  
  public void setLockEnabled(boolean paramBoolean) {
    this.lockEnabled = paramBoolean;
  }
  
  public void setMax(int paramInt) {
    if (paramInt > 0) {
      if (paramInt <= this.mProgress) {
        this.mProgress = 0;
        if (this.mOnCircularSeekBarChangeListener != null)
          this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, false); 
      } 
      this.mMax = paramInt;
      recalculateAll();
      invalidate();
    } 
  }
  
  public void setOnSeekBarChangeListener(OnCircularSeekBarChangeListener paramOnCircularSeekBarChangeListener) {
    this.mOnCircularSeekBarChangeListener = paramOnCircularSeekBarChangeListener;
  }
  
  public void setPointerAlpha(int paramInt) {
    if (paramInt >= 0 && paramInt <= 255) {
      this.mPointerAlpha = paramInt;
      this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
      invalidate();
    } 
  }
  
  public void setPointerAlphaOnTouch(int paramInt) {
    if (paramInt >= 0 && paramInt <= 255)
      this.mPointerAlphaOnTouch = paramInt; 
  }
  
  public void setPointerColor(int paramInt) {
    this.mPointerColor = paramInt;
    this.mPointerPaint.setColor(this.mPointerColor);
    invalidate();
  }
  
  public void setPointerHaloColor(int paramInt) {
    this.mPointerHaloColor = paramInt;
    this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
    invalidate();
  }
  
  public void setProgress(int paramInt) {
    if (this.mProgress != paramInt) {
      this.mProgress = paramInt;
      if (this.mOnCircularSeekBarChangeListener != null)
        this.mOnCircularSeekBarChangeListener.onProgressChanged(this, paramInt, false); 
      recalculateAll();
      invalidate();
    } 
  }
  
  public static interface OnCircularSeekBarChangeListener {
    void onProgressChanged(CircularSeekBar param1CircularSeekBar, int param1Int, boolean param1Boolean);
    
    void onStartTrackingTouch(CircularSeekBar param1CircularSeekBar);
    
    void onStopTrackingTouch(CircularSeekBar param1CircularSeekBar);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/CircularSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */