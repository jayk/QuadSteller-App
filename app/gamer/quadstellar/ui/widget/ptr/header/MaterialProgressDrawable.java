package app.gamer.quadstellar.ui.widget.ptr.header;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import app.gamer.quadstellar.ui.widget.ptr.PtrLocalDisplay;
import java.util.ArrayList;

public class MaterialProgressDrawable extends Drawable implements Animatable {
  private static final int ANIMATION_DURATION = 1333;
  
  private static final int ARROW_HEIGHT = 5;
  
  private static final int ARROW_HEIGHT_LARGE = 6;
  
  private static final float ARROW_OFFSET_ANGLE = 5.0F;
  
  private static final int ARROW_WIDTH = 10;
  
  private static final int ARROW_WIDTH_LARGE = 12;
  
  private static final float CENTER_RADIUS = 8.75F;
  
  private static final float CENTER_RADIUS_LARGE = 12.5F;
  
  private static final int CIRCLE_DIAMETER = 40;
  
  private static final int CIRCLE_DIAMETER_LARGE = 56;
  
  public static final int DEFAULT = 1;
  
  private static final Interpolator EASE_INTERPOLATOR;
  
  private static final Interpolator END_CURVE_INTERPOLATOR;
  
  private static final int FILL_SHADOW_COLOR = 1023410176;
  
  private static final int KEY_SHADOW_COLOR = 503316480;
  
  public static final int LARGE = 0;
  
  private static final Interpolator LINEAR_INTERPOLATOR = (Interpolator)new LinearInterpolator();
  
  private static final float MAX_PROGRESS_ARC = 0.8F;
  
  private static final float NUM_POINTS = 5.0F;
  
  private static final float SHADOW_RADIUS = 3.5F;
  
  private static final Interpolator START_CURVE_INTERPOLATOR;
  
  private static final float STROKE_WIDTH = 2.5F;
  
  private static final float STROKE_WIDTH_LARGE = 3.0F;
  
  private static final float X_OFFSET = 0.0F;
  
  private static final float Y_OFFSET = 1.75F;
  
  private final int[] COLORS = new int[] { -3591113, -13149199, -536002, -13327536 };
  
  private Animation mAnimation;
  
  private final ArrayList<Animation> mAnimators = new ArrayList<Animation>();
  
  private int mBackgroundColor;
  
  private final Drawable.Callback mCallback = new Drawable.Callback() {
      public void invalidateDrawable(Drawable param1Drawable) {
        MaterialProgressDrawable.this.invalidateSelf();
      }
      
      public void scheduleDrawable(Drawable param1Drawable, Runnable param1Runnable, long param1Long) {
        MaterialProgressDrawable.this.scheduleSelf(param1Runnable, param1Long);
      }
      
      public void unscheduleDrawable(Drawable param1Drawable, Runnable param1Runnable) {
        MaterialProgressDrawable.this.unscheduleSelf(param1Runnable);
      }
    };
  
  private Animation mFinishAnimation;
  
  private double mHeight;
  
  private View mParent;
  
  private Resources mResources;
  
  private final Ring mRing;
  
  private float mRotation;
  
  private float mRotationCount;
  
  private ShapeDrawable mShadow;
  
  private double mWidth;
  
  static {
    END_CURVE_INTERPOLATOR = (Interpolator)new EndCurveInterpolator();
    START_CURVE_INTERPOLATOR = (Interpolator)new StartCurveInterpolator();
    EASE_INTERPOLATOR = (Interpolator)new AccelerateDecelerateInterpolator();
  }
  
  public MaterialProgressDrawable(Context paramContext, View paramView) {
    this.mParent = paramView;
    this.mResources = paramContext.getResources();
    this.mRing = new Ring(this.mCallback);
    this.mRing.setColors(this.COLORS);
    updateSizes(1);
    setupAnimators();
  }
  
  private float getRotation() {
    return this.mRotation;
  }
  
  private void setSizeParameters(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, float paramFloat1, float paramFloat2) {
    Ring ring = this.mRing;
    float f = (this.mResources.getDisplayMetrics()).density;
    this.mWidth = f * paramDouble1;
    this.mHeight = f * paramDouble2;
    ring.setStrokeWidth((float)paramDouble4 * f);
    ring.setCenterRadius(f * paramDouble3);
    ring.setColorIndex(0);
    ring.setArrowDimensions(paramFloat1 * f, paramFloat2 * f);
    ring.setInsets((int)this.mWidth, (int)this.mHeight);
    setUp(this.mWidth);
  }
  
  private void setUp(double paramDouble) {
    PtrLocalDisplay.init(this.mParent.getContext());
    int i = PtrLocalDisplay.dp2px(1.75F);
    int j = PtrLocalDisplay.dp2px(0.0F);
    int k = PtrLocalDisplay.dp2px(3.5F);
    this.mShadow = new ShapeDrawable((Shape)new OvalShadow(k, (int)paramDouble));
    if (Build.VERSION.SDK_INT >= 11)
      this.mParent.setLayerType(1, this.mShadow.getPaint()); 
    this.mShadow.getPaint().setShadowLayer(k, j, i, 503316480);
  }
  
  private void setupAnimators() {
    final Ring ring = this.mRing;
    Animation animation1 = new Animation() {
        public void applyTransformation(float param1Float, Transformation param1Transformation) {
          float f1 = (float)(Math.floor((ring.getStartingRotation() / 0.8F)) + 1.0D);
          float f2 = ring.getStartingStartTrim();
          float f3 = ring.getStartingEndTrim();
          float f4 = ring.getStartingStartTrim();
          ring.setStartTrim(f2 + (f3 - f4) * param1Float);
          f3 = ring.getStartingRotation();
          f4 = ring.getStartingRotation();
          ring.setRotation(f3 + (f1 - f4) * param1Float);
          ring.setArrowScale(1.0F - param1Float);
        }
      };
    animation1.setInterpolator(EASE_INTERPOLATOR);
    animation1.setDuration(666L);
    animation1.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {
            ring.goToNextColor();
            ring.storeOriginals();
            ring.setShowArrow(false);
            MaterialProgressDrawable.this.mParent.startAnimation(MaterialProgressDrawable.this.mAnimation);
          }
          
          public void onAnimationRepeat(Animation param1Animation) {}
          
          public void onAnimationStart(Animation param1Animation) {}
        });
    Animation animation2 = new Animation() {
        public void applyTransformation(float param1Float, Transformation param1Transformation) {
          float f1 = (float)Math.toRadians(ring.getStrokeWidth() / 6.283185307179586D * ring.getCenterRadius());
          float f2 = ring.getStartingEndTrim();
          float f3 = ring.getStartingStartTrim();
          float f4 = ring.getStartingRotation();
          float f5 = MaterialProgressDrawable.START_CURVE_INTERPOLATOR.getInterpolation(param1Float);
          ring.setEndTrim(f2 + f5 * (0.8F - f1));
          f2 = MaterialProgressDrawable.END_CURVE_INTERPOLATOR.getInterpolation(param1Float);
          ring.setStartTrim(f3 + 0.8F * f2);
          ring.setRotation(f4 + 0.25F * param1Float);
          f3 = MaterialProgressDrawable.this.mRotationCount / 5.0F;
          MaterialProgressDrawable.this.setRotation(144.0F * param1Float + 720.0F * f3);
        }
      };
    animation2.setRepeatCount(-1);
    animation2.setRepeatMode(1);
    animation2.setInterpolator(LINEAR_INTERPOLATOR);
    animation2.setDuration(1333L);
    animation2.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {}
          
          public void onAnimationRepeat(Animation param1Animation) {
            ring.storeOriginals();
            ring.goToNextColor();
            ring.setStartTrim(ring.getEndTrim());
            MaterialProgressDrawable.access$602(MaterialProgressDrawable.this, (MaterialProgressDrawable.this.mRotationCount + 1.0F) % 5.0F);
          }
          
          public void onAnimationStart(Animation param1Animation) {
            MaterialProgressDrawable.access$602(MaterialProgressDrawable.this, 0.0F);
          }
        });
    this.mFinishAnimation = animation1;
    this.mAnimation = animation2;
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mShadow != null) {
      this.mShadow.getPaint().setColor(this.mBackgroundColor);
      this.mShadow.draw(paramCanvas);
    } 
    Rect rect = getBounds();
    int i = paramCanvas.save();
    paramCanvas.rotate(this.mRotation, rect.exactCenterX(), rect.exactCenterY());
    this.mRing.draw(paramCanvas, rect);
    paramCanvas.restoreToCount(i);
  }
  
  public int getAlpha() {
    return this.mRing.getAlpha();
  }
  
  public int getIntrinsicHeight() {
    return (int)this.mHeight;
  }
  
  public int getIntrinsicWidth() {
    return (int)this.mWidth;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public boolean isRunning() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAnimators : Ljava/util/ArrayList;
    //   4: astore_1
    //   5: aload_1
    //   6: invokevirtual size : ()I
    //   9: istore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: iload_3
    //   13: iload_2
    //   14: if_icmpge -> 55
    //   17: aload_1
    //   18: iload_3
    //   19: invokevirtual get : (I)Ljava/lang/Object;
    //   22: checkcast android/view/animation/Animation
    //   25: astore #4
    //   27: aload #4
    //   29: invokevirtual hasStarted : ()Z
    //   32: ifeq -> 49
    //   35: aload #4
    //   37: invokevirtual hasEnded : ()Z
    //   40: ifne -> 49
    //   43: iconst_1
    //   44: istore #5
    //   46: iload #5
    //   48: ireturn
    //   49: iinc #3, 1
    //   52: goto -> 12
    //   55: iconst_0
    //   56: istore #5
    //   58: goto -> 46
  }
  
  public void setAlpha(int paramInt) {
    this.mRing.setAlpha(paramInt);
  }
  
  public void setArrowScale(float paramFloat) {
    this.mRing.setArrowScale(paramFloat);
  }
  
  public void setBackgroundColor(int paramInt) {
    this.mBackgroundColor = paramInt;
    this.mRing.setBackgroundColor(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mRing.setColorFilter(paramColorFilter);
  }
  
  public void setColorSchemeColors(int... paramVarArgs) {
    this.mRing.setColors(paramVarArgs);
    this.mRing.setColorIndex(0);
  }
  
  public void setProgressRotation(float paramFloat) {
    this.mRing.setRotation(paramFloat);
  }
  
  void setRotation(float paramFloat) {
    this.mRotation = paramFloat;
    invalidateSelf();
  }
  
  public void setStartEndTrim(float paramFloat1, float paramFloat2) {
    this.mRing.setStartTrim(paramFloat1);
    this.mRing.setEndTrim(paramFloat2);
  }
  
  public void showArrow(boolean paramBoolean) {
    this.mRing.setShowArrow(paramBoolean);
  }
  
  public void start() {
    this.mAnimation.reset();
    this.mRing.storeOriginals();
    if (this.mRing.getEndTrim() != this.mRing.getStartTrim()) {
      this.mParent.startAnimation(this.mFinishAnimation);
      return;
    } 
    this.mRing.setColorIndex(0);
    this.mRing.resetOriginals();
    this.mParent.startAnimation(this.mAnimation);
  }
  
  public void stop() {
    this.mParent.clearAnimation();
    setRotation(0.0F);
    this.mRing.setShowArrow(false);
    this.mRing.setColorIndex(0);
    this.mRing.resetOriginals();
  }
  
  public void updateSizes(int paramInt) {
    if (paramInt == 0) {
      setSizeParameters(56.0D, 56.0D, 12.5D, 3.0D, 12.0F, 6.0F);
      return;
    } 
    setSizeParameters(40.0D, 40.0D, 8.75D, 2.5D, 10.0F, 5.0F);
  }
  
  private static class EndCurveInterpolator extends AccelerateDecelerateInterpolator {
    private EndCurveInterpolator() {}
    
    public float getInterpolation(float param1Float) {
      return super.getInterpolation(Math.max(0.0F, (param1Float - 0.5F) * 2.0F));
    }
  }
  
  private class OvalShadow extends OvalShape {
    private int mCircleDiameter;
    
    private RadialGradient mRadialGradient;
    
    private Paint mShadowPaint = new Paint();
    
    private int mShadowRadius;
    
    public OvalShadow(int param1Int1, int param1Int2) {
      this.mShadowRadius = param1Int1;
      this.mCircleDiameter = param1Int2;
      float f1 = (this.mCircleDiameter / 2);
      float f2 = (this.mCircleDiameter / 2);
      float f3 = this.mShadowRadius;
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      this.mRadialGradient = new RadialGradient(f1, f2, f3, new int[] { 1023410176, 0 }, null, tileMode);
      this.mShadowPaint.setShader((Shader)this.mRadialGradient);
    }
    
    public void draw(Canvas param1Canvas, Paint param1Paint) {
      int i = MaterialProgressDrawable.this.getBounds().width();
      int j = MaterialProgressDrawable.this.getBounds().height();
      param1Canvas.drawCircle((i / 2), (j / 2), (this.mCircleDiameter / 2 + this.mShadowRadius), this.mShadowPaint);
      param1Canvas.drawCircle((i / 2), (j / 2), (this.mCircleDiameter / 2), param1Paint);
    }
  }
  
  private static class Ring {
    private int mAlpha;
    
    private final Paint mArcPaint = new Paint();
    
    private Path mArrow;
    
    private int mArrowHeight;
    
    private final Paint mArrowPaint = new Paint();
    
    private float mArrowScale;
    
    private int mArrowWidth;
    
    private int mBackgroundColor;
    
    private final Paint mCirclePaint = new Paint();
    
    private int mColorIndex;
    
    private int[] mColors;
    
    private float mEndTrim = 0.0F;
    
    private final Drawable.Callback mRingCallback;
    
    private double mRingCenterRadius;
    
    private float mRotation = 0.0F;
    
    private boolean mShowArrow;
    
    private float mStartTrim = 0.0F;
    
    private float mStartingEndTrim;
    
    private float mStartingRotation;
    
    private float mStartingStartTrim;
    
    private float mStrokeInset = 2.5F;
    
    private float mStrokeWidth = 5.0F;
    
    private final RectF mTempBounds = new RectF();
    
    public Ring(Drawable.Callback param1Callback) {
      this.mRingCallback = param1Callback;
      this.mArcPaint.setStrokeCap(Paint.Cap.SQUARE);
      this.mArcPaint.setAntiAlias(true);
      this.mArcPaint.setStyle(Paint.Style.STROKE);
      this.mArrowPaint.setStyle(Paint.Style.FILL);
      this.mArrowPaint.setAntiAlias(true);
      this.mCirclePaint.setAntiAlias(true);
    }
    
    private void drawTriangle(Canvas param1Canvas, float param1Float1, float param1Float2, Rect param1Rect) {
      if (this.mShowArrow) {
        if (this.mArrow == null) {
          this.mArrow = new Path();
          this.mArrow.setFillType(Path.FillType.EVEN_ODD);
        } else {
          this.mArrow.reset();
        } 
        float f1 = ((int)this.mStrokeInset / 2);
        float f2 = this.mArrowScale;
        float f3 = (float)(this.mRingCenterRadius * Math.cos(0.0D) + param1Rect.exactCenterX());
        float f4 = (float)(this.mRingCenterRadius * Math.sin(0.0D) + param1Rect.exactCenterY());
        this.mArrow.moveTo(0.0F, 0.0F);
        this.mArrow.lineTo(this.mArrowWidth * this.mArrowScale, 0.0F);
        this.mArrow.lineTo(this.mArrowWidth * this.mArrowScale / 2.0F, this.mArrowHeight * this.mArrowScale);
        this.mArrow.offset(f3 - f1 * f2, f4);
        this.mArrow.close();
        this.mArrowPaint.setColor(this.mColors[this.mColorIndex]);
        this.mArrowPaint.setAlpha(this.mAlpha);
        param1Canvas.rotate(param1Float1 + param1Float2 - 5.0F, param1Rect.exactCenterX(), param1Rect.exactCenterY());
        param1Canvas.drawPath(this.mArrow, this.mArrowPaint);
      } 
    }
    
    private void invalidateSelf() {
      this.mRingCallback.invalidateDrawable(null);
    }
    
    public void draw(Canvas param1Canvas, Rect param1Rect) {
      this.mCirclePaint.setColor(this.mBackgroundColor);
      this.mCirclePaint.setAlpha(this.mAlpha);
      param1Canvas.drawCircle(param1Rect.exactCenterX(), param1Rect.exactCenterY(), (param1Rect.width() / 2), this.mCirclePaint);
      RectF rectF = this.mTempBounds;
      rectF.set(param1Rect);
      rectF.inset(this.mStrokeInset, this.mStrokeInset);
      float f1 = (this.mStartTrim + this.mRotation) * 360.0F;
      float f2 = (this.mEndTrim + this.mRotation) * 360.0F - f1;
      this.mArcPaint.setColor(this.mColors[this.mColorIndex]);
      this.mArcPaint.setAlpha(this.mAlpha);
      param1Canvas.drawArc(rectF, f1, f2, false, this.mArcPaint);
      drawTriangle(param1Canvas, f1, f2, param1Rect);
    }
    
    public int getAlpha() {
      return this.mAlpha;
    }
    
    public double getCenterRadius() {
      return this.mRingCenterRadius;
    }
    
    public float getEndTrim() {
      return this.mEndTrim;
    }
    
    public float getInsets() {
      return this.mStrokeInset;
    }
    
    public float getRotation() {
      return this.mRotation;
    }
    
    public float getStartTrim() {
      return this.mStartTrim;
    }
    
    public float getStartingEndTrim() {
      return this.mStartingEndTrim;
    }
    
    public float getStartingRotation() {
      return this.mStartingRotation;
    }
    
    public float getStartingStartTrim() {
      return this.mStartingStartTrim;
    }
    
    public float getStrokeWidth() {
      return this.mStrokeWidth;
    }
    
    public void goToNextColor() {
      this.mColorIndex = (this.mColorIndex + 1) % this.mColors.length;
    }
    
    public void resetOriginals() {
      this.mStartingStartTrim = 0.0F;
      this.mStartingEndTrim = 0.0F;
      this.mStartingRotation = 0.0F;
      setStartTrim(0.0F);
      setEndTrim(0.0F);
      setRotation(0.0F);
    }
    
    public void setAlpha(int param1Int) {
      this.mAlpha = param1Int;
    }
    
    public void setArrowDimensions(float param1Float1, float param1Float2) {
      this.mArrowWidth = (int)param1Float1;
      this.mArrowHeight = (int)param1Float2;
    }
    
    public void setArrowScale(float param1Float) {
      if (param1Float != this.mArrowScale) {
        this.mArrowScale = param1Float;
        invalidateSelf();
      } 
    }
    
    public void setBackgroundColor(int param1Int) {
      this.mBackgroundColor = param1Int;
    }
    
    public void setCenterRadius(double param1Double) {
      this.mRingCenterRadius = param1Double;
    }
    
    public void setColorFilter(ColorFilter param1ColorFilter) {
      this.mArcPaint.setColorFilter(param1ColorFilter);
      invalidateSelf();
    }
    
    public void setColorIndex(int param1Int) {
      this.mColorIndex = param1Int;
    }
    
    public void setColors(int[] param1ArrayOfint) {
      this.mColors = param1ArrayOfint;
      setColorIndex(0);
    }
    
    public void setEndTrim(float param1Float) {
      this.mEndTrim = param1Float;
      invalidateSelf();
    }
    
    public void setInsets(int param1Int1, int param1Int2) {
      float f = Math.min(param1Int1, param1Int2);
      if (this.mRingCenterRadius <= 0.0D || f < 0.0F) {
        f = (float)Math.ceil((this.mStrokeWidth / 2.0F));
      } else {
        f = (float)((f / 2.0F) - this.mRingCenterRadius);
      } 
      this.mStrokeInset = f;
    }
    
    public void setRotation(float param1Float) {
      this.mRotation = param1Float;
      invalidateSelf();
    }
    
    public void setShowArrow(boolean param1Boolean) {
      if (this.mShowArrow != param1Boolean) {
        this.mShowArrow = param1Boolean;
        invalidateSelf();
      } 
    }
    
    public void setStartTrim(float param1Float) {
      this.mStartTrim = param1Float;
      invalidateSelf();
    }
    
    public void setStrokeWidth(float param1Float) {
      this.mStrokeWidth = param1Float;
      this.mArcPaint.setStrokeWidth(param1Float);
      invalidateSelf();
    }
    
    public void storeOriginals() {
      this.mStartingStartTrim = this.mStartTrim;
      this.mStartingEndTrim = this.mEndTrim;
      this.mStartingRotation = this.mRotation;
    }
  }
  
  private static class StartCurveInterpolator extends AccelerateDecelerateInterpolator {
    private StartCurveInterpolator() {}
    
    public float getInterpolation(float param1Float) {
      return super.getInterpolation(Math.min(1.0F, 2.0F * param1Float));
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/header/MaterialProgressDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */