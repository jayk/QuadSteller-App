package com.larswerkman.holocolorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPicker extends View {
  private static final int[] COLORS = new int[] { -65536, -65281, -16776961, -16711681, -16711936, -256, -65536 };
  
  private static final String STATE_ANGLE = "angle";
  
  private static final String STATE_OLD_COLOR = "color";
  
  private static final String STATE_PARENT = "parent";
  
  private static final String STATE_SHOW_OLD_COLOR = "showColor";
  
  private float mAngle;
  
  private Paint mCenterHaloPaint;
  
  private int mCenterNewColor;
  
  private Paint mCenterNewPaint;
  
  private int mCenterOldColor;
  
  private Paint mCenterOldPaint;
  
  private RectF mCenterRectangle = new RectF();
  
  private int mColor;
  
  private int mColorCenterHaloRadius;
  
  private int mColorCenterRadius;
  
  private int mColorPointerHaloRadius;
  
  private int mColorPointerRadius;
  
  private Paint mColorWheelPaint;
  
  private int mColorWheelRadius;
  
  private RectF mColorWheelRectangle = new RectF();
  
  private int mColorWheelThickness;
  
  private float[] mHSV = new float[3];
  
  private OpacityBar mOpacityBar = null;
  
  private Paint mPointerColor;
  
  private Paint mPointerHaloPaint;
  
  private int mPreferredColorCenterHaloRadius;
  
  private int mPreferredColorCenterRadius;
  
  private int mPreferredColorWheelRadius;
  
  private SVBar mSVbar = null;
  
  private SaturationBar mSaturationBar = null;
  
  private boolean mShowCenterOldColor;
  
  private float mSlopX;
  
  private float mSlopY;
  
  private boolean mTouchAnywhereOnColorWheelEnabled = true;
  
  private float mTranslationOffset;
  
  private boolean mUserIsMovingPointer = false;
  
  private ValueBar mValueBar = null;
  
  private int oldChangedListenerColor;
  
  private int oldSelectedListenerColor;
  
  private OnColorChangedListener onColorChangedListener;
  
  private OnColorSelectedListener onColorSelectedListener;
  
  private OnWheelColorChangeListener onWheelColorChangeListener;
  
  public ColorPicker(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null, 0);
  }
  
  public ColorPicker(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet, 0);
  }
  
  public ColorPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet, paramInt);
  }
  
  private int ave(int paramInt1, int paramInt2, float paramFloat) {
    return Math.round((paramInt2 - paramInt1) * paramFloat) + paramInt1;
  }
  
  private int calculateColor(float paramFloat) {
    float f = (float)(paramFloat / 6.283185307179586D);
    paramFloat = f;
    if (f < 0.0F)
      paramFloat = f + 1.0F; 
    if (paramFloat <= 0.0F) {
      this.mColor = COLORS[0];
      return COLORS[0];
    } 
    if (paramFloat >= 1.0F) {
      this.mColor = COLORS[COLORS.length - 1];
      return COLORS[COLORS.length - 1];
    } 
    paramFloat *= (COLORS.length - 1);
    null = (int)paramFloat;
    paramFloat -= null;
    int i = COLORS[null];
    int j = COLORS[null + 1];
    null = ave(Color.alpha(i), Color.alpha(j), paramFloat);
    int k = ave(Color.red(i), Color.red(j), paramFloat);
    int m = ave(Color.green(i), Color.green(j), paramFloat);
    i = ave(Color.blue(i), Color.blue(j), paramFloat);
    this.mColor = Color.argb(null, k, m, i);
    return Color.argb(null, k, m, i);
  }
  
  private float[] calculatePointerPosition(float paramFloat) {
    return new float[] { (float)(this.mColorWheelRadius * Math.cos(paramFloat)), (float)(this.mColorWheelRadius * Math.sin(paramFloat)) };
  }
  
  private float colorToAngle(int paramInt) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    return (float)Math.toRadians(-arrayOfFloat[0]);
  }
  
  private void init(AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ColorPicker, paramInt, 0);
    Resources resources = getContext().getResources();
    this.mColorWheelThickness = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_wheel_thickness, resources.getDimensionPixelSize(R.dimen.color_wheel_thickness));
    this.mColorWheelRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_wheel_radius, resources.getDimensionPixelSize(R.dimen.color_wheel_radius));
    this.mPreferredColorWheelRadius = this.mColorWheelRadius;
    this.mColorCenterRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_center_radius, resources.getDimensionPixelSize(R.dimen.color_center_radius));
    this.mPreferredColorCenterRadius = this.mColorCenterRadius;
    this.mColorCenterHaloRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_center_halo_radius, resources.getDimensionPixelSize(R.dimen.color_center_halo_radius));
    this.mPreferredColorCenterHaloRadius = this.mColorCenterHaloRadius;
    this.mColorPointerRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_pointer_radius, resources.getDimensionPixelSize(R.dimen.color_pointer_radius));
    this.mColorPointerHaloRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPicker_color_pointer_halo_radius, resources.getDimensionPixelSize(R.dimen.color_pointer_halo_radius));
    typedArray.recycle();
    this.mAngle = -1.5707964F;
    SweepGradient sweepGradient = new SweepGradient(0.0F, 0.0F, COLORS, null);
    this.mColorWheelPaint = new Paint(1);
    this.mColorWheelPaint.setShader((Shader)sweepGradient);
    this.mColorWheelPaint.setStyle(Paint.Style.STROKE);
    this.mColorWheelPaint.setStrokeWidth(this.mColorWheelThickness);
    this.mPointerHaloPaint = new Paint(1);
    this.mPointerHaloPaint.setColor(-16777216);
    this.mPointerHaloPaint.setAlpha(80);
    this.mPointerColor = new Paint(1);
    this.mPointerColor.setColor(calculateColor(this.mAngle));
    this.mCenterNewPaint = new Paint(1);
    this.mCenterNewPaint.setColor(calculateColor(this.mAngle));
    this.mCenterNewPaint.setStyle(Paint.Style.FILL);
    this.mCenterOldPaint = new Paint(1);
    this.mCenterOldPaint.setColor(calculateColor(this.mAngle));
    this.mCenterOldPaint.setStyle(Paint.Style.FILL);
    this.mCenterHaloPaint = new Paint(1);
    this.mCenterHaloPaint.setColor(-16777216);
    this.mCenterHaloPaint.setAlpha(0);
    this.mCenterNewColor = calculateColor(this.mAngle);
    this.mCenterOldColor = calculateColor(this.mAngle);
    this.mShowCenterOldColor = true;
  }
  
  public void addOpacityBar(OpacityBar paramOpacityBar) {
    this.mOpacityBar = paramOpacityBar;
    this.mOpacityBar.setColorPicker(this);
    this.mOpacityBar.setColor(this.mColor);
  }
  
  public void addSVBar(SVBar paramSVBar) {
    this.mSVbar = paramSVBar;
    this.mSVbar.setColorPicker(this);
    this.mSVbar.setColor(this.mColor);
  }
  
  public void addSaturationBar(SaturationBar paramSaturationBar) {
    this.mSaturationBar = paramSaturationBar;
    this.mSaturationBar.setColorPicker(this);
    this.mSaturationBar.setColor(this.mColor);
  }
  
  public void addValueBar(ValueBar paramValueBar) {
    this.mValueBar = paramValueBar;
    this.mValueBar.setColorPicker(this);
    this.mValueBar.setColor(this.mColor);
  }
  
  public void changeOpacityBarColor(int paramInt) {
    if (this.mOpacityBar != null)
      this.mOpacityBar.setColor(paramInt); 
  }
  
  public void changeSaturationBarColor(int paramInt) {
    if (this.mSaturationBar != null)
      this.mSaturationBar.setColor(paramInt); 
  }
  
  public void changeValueBarColor(int paramInt) {
    if (this.mValueBar != null)
      this.mValueBar.setColor(paramInt); 
  }
  
  public int getColor() {
    return this.mCenterNewColor;
  }
  
  public int getOldCenterColor() {
    return this.mCenterOldColor;
  }
  
  public OnColorChangedListener getOnColorChangedListener() {
    return this.onColorChangedListener;
  }
  
  public OnColorSelectedListener getOnColorSelectedListener() {
    return this.onColorSelectedListener;
  }
  
  public boolean getShowOldCenterColor() {
    return this.mShowCenterOldColor;
  }
  
  public boolean getTouchAnywhereOnColorWheel() {
    return this.mTouchAnywhereOnColorWheelEnabled;
  }
  
  public boolean hasOpacityBar() {
    return (this.mOpacityBar != null);
  }
  
  public boolean hasSVBar() {
    return (this.mSVbar != null);
  }
  
  public boolean hasSaturationBar() {
    return (this.mSaturationBar != null);
  }
  
  public boolean hasValueBar() {
    return (this.mValueBar != null);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    paramCanvas.translate(this.mTranslationOffset, this.mTranslationOffset);
    paramCanvas.drawOval(this.mColorWheelRectangle, this.mColorWheelPaint);
    float[] arrayOfFloat = calculatePointerPosition(this.mAngle);
    paramCanvas.drawCircle(arrayOfFloat[0], arrayOfFloat[1], this.mColorPointerHaloRadius, this.mPointerHaloPaint);
    paramCanvas.drawCircle(arrayOfFloat[0], arrayOfFloat[1], this.mColorPointerRadius, this.mPointerColor);
    paramCanvas.drawCircle(0.0F, 0.0F, this.mColorCenterHaloRadius, this.mCenterHaloPaint);
    if (this.mShowCenterOldColor) {
      paramCanvas.drawArc(this.mCenterRectangle, 90.0F, 180.0F, true, this.mCenterOldPaint);
      paramCanvas.drawArc(this.mCenterRectangle, 270.0F, 180.0F, true, this.mCenterNewPaint);
      return;
    } 
    paramCanvas.drawArc(this.mCenterRectangle, 0.0F, 360.0F, true, this.mCenterNewPaint);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = (this.mPreferredColorWheelRadius + this.mColorPointerHaloRadius) * 2;
    int j = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    if (j != 1073741824)
      if (j == Integer.MIN_VALUE) {
        paramInt1 = Math.min(i, paramInt1);
      } else {
        paramInt1 = i;
      }  
    if (k != 1073741824)
      if (k == Integer.MIN_VALUE) {
        paramInt2 = Math.min(i, paramInt2);
      } else {
        paramInt2 = i;
      }  
    paramInt1 = Math.min(paramInt1, paramInt2);
    setMeasuredDimension(paramInt1, paramInt1);
    this.mTranslationOffset = paramInt1 * 0.5F;
    this.mColorWheelRadius = paramInt1 / 2 - this.mColorWheelThickness - this.mColorPointerHaloRadius;
    this.mColorWheelRectangle.set(-this.mColorWheelRadius, -this.mColorWheelRadius, this.mColorWheelRadius, this.mColorWheelRadius);
    this.mColorCenterRadius = (int)(this.mPreferredColorCenterRadius * this.mColorWheelRadius / this.mPreferredColorWheelRadius);
    this.mColorCenterHaloRadius = (int)(this.mPreferredColorCenterHaloRadius * this.mColorWheelRadius / this.mPreferredColorWheelRadius);
    this.mCenterRectangle.set(-this.mColorCenterRadius, -this.mColorCenterRadius, this.mColorCenterRadius, this.mColorCenterRadius);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    Bundle bundle = (Bundle)paramParcelable;
    super.onRestoreInstanceState(bundle.getParcelable("parent"));
    this.mAngle = bundle.getFloat("angle");
    setOldCenterColor(bundle.getInt("color"));
    this.mShowCenterOldColor = bundle.getBoolean("showColor");
    int i = calculateColor(this.mAngle);
    this.mPointerColor.setColor(i);
    setNewCenterColor(i);
  }
  
  protected Parcelable onSaveInstanceState() {
    Parcelable parcelable = super.onSaveInstanceState();
    Bundle bundle = new Bundle();
    bundle.putParcelable("parent", parcelable);
    bundle.putFloat("angle", this.mAngle);
    bundle.putInt("color", this.mCenterOldColor);
    bundle.putBoolean("showColor", this.mShowCenterOldColor);
    return (Parcelable)bundle;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float[] arrayOfFloat;
    null = false;
    getParent().requestDisallowInterceptTouchEvent(true);
    float f1 = paramMotionEvent.getX() - this.mTranslationOffset;
    float f2 = paramMotionEvent.getY() - this.mTranslationOffset;
    switch (paramMotionEvent.getAction()) {
      default:
        return true;
      case 0:
        arrayOfFloat = calculatePointerPosition(this.mAngle);
        if (f1 >= arrayOfFloat[0] - this.mColorPointerHaloRadius && f1 <= arrayOfFloat[0] + this.mColorPointerHaloRadius && f2 >= arrayOfFloat[1] - this.mColorPointerHaloRadius && f2 <= arrayOfFloat[1] + this.mColorPointerHaloRadius) {
          this.mSlopX = f1 - arrayOfFloat[0];
          this.mSlopY = f2 - arrayOfFloat[1];
          this.mUserIsMovingPointer = true;
          invalidate();
        } else if (f1 >= -this.mColorCenterRadius && f1 <= this.mColorCenterRadius && f2 >= -this.mColorCenterRadius && f2 <= this.mColorCenterRadius && this.mShowCenterOldColor) {
          this.mCenterHaloPaint.setAlpha(80);
          setColor(getOldCenterColor());
          invalidate();
        } else if (Math.sqrt((f1 * f1 + f2 * f2)) <= (this.mColorWheelRadius + this.mColorPointerHaloRadius) && Math.sqrt((f1 * f1 + f2 * f2)) >= (this.mColorWheelRadius - this.mColorPointerHaloRadius) && this.mTouchAnywhereOnColorWheelEnabled) {
          this.mUserIsMovingPointer = true;
          invalidate();
        } else {
          getParent().requestDisallowInterceptTouchEvent(false);
          return SYNTHETIC_LOCAL_VARIABLE_2;
        } 
      case 2:
        if (this.mUserIsMovingPointer) {
          this.mAngle = (float)Math.atan2((f2 - this.mSlopY), (f1 - this.mSlopX));
          this.mPointerColor.setColor(calculateColor(this.mAngle));
          int i = calculateColor(this.mAngle);
          this.mCenterNewColor = i;
          setNewCenterColor(i);
          if (this.mOpacityBar != null)
            this.mOpacityBar.setColor(this.mColor); 
          if (this.mValueBar != null)
            this.mValueBar.setColor(this.mColor); 
          if (this.mSaturationBar != null)
            this.mSaturationBar.setColor(this.mColor); 
          if (this.mSVbar != null)
            this.mSVbar.setColor(this.mColor); 
          invalidate();
        } else {
          getParent().requestDisallowInterceptTouchEvent(false);
          return SYNTHETIC_LOCAL_VARIABLE_2;
        } 
      case 1:
        this.mUserIsMovingPointer = false;
        this.mCenterHaloPaint.setAlpha(0);
        if (this.onWheelColorChangeListener != null)
          this.onWheelColorChangeListener.onWheelColorChanged(this.mColor); 
        if (this.onColorSelectedListener != null && this.mCenterNewColor != this.oldSelectedListenerColor) {
          this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
          this.oldSelectedListenerColor = this.mCenterNewColor;
        } 
        invalidate();
      case 3:
        break;
    } 
    if (this.onColorSelectedListener != null && this.mCenterNewColor != this.oldSelectedListenerColor) {
      this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
      this.oldSelectedListenerColor = this.mCenterNewColor;
    } 
  }
  
  public void setColor(int paramInt) {
    this.mAngle = colorToAngle(paramInt);
    this.mPointerColor.setColor(calculateColor(this.mAngle));
    if (this.mOpacityBar != null) {
      this.mOpacityBar.setColor(this.mColor);
      this.mOpacityBar.setOpacity(Color.alpha(paramInt));
    } 
    if (this.mSVbar != null) {
      Color.colorToHSV(paramInt, this.mHSV);
      this.mSVbar.setColor(this.mColor);
      if (this.mHSV[1] < this.mHSV[2]) {
        this.mSVbar.setSaturation(this.mHSV[1]);
      } else if (this.mHSV[1] > this.mHSV[2]) {
        this.mSVbar.setValue(this.mHSV[2]);
      } 
    } 
    if (this.mSaturationBar != null) {
      Color.colorToHSV(paramInt, this.mHSV);
      this.mSaturationBar.setColor(this.mColor);
      this.mSaturationBar.setSaturation(this.mHSV[1]);
    } 
    if (this.mValueBar != null && this.mSaturationBar == null) {
      Color.colorToHSV(paramInt, this.mHSV);
      this.mValueBar.setColor(this.mColor);
      this.mValueBar.setValue(this.mHSV[2]);
    } else if (this.mValueBar != null) {
      Color.colorToHSV(paramInt, this.mHSV);
      this.mValueBar.setValue(this.mHSV[2]);
    } 
    setNewCenterColor(paramInt);
  }
  
  public void setNewCenterColor(int paramInt) {
    this.mCenterNewColor = paramInt;
    this.mCenterNewPaint.setColor(paramInt);
    if (this.mCenterOldColor == 0) {
      this.mCenterOldColor = paramInt;
      this.mCenterOldPaint.setColor(paramInt);
    } 
    if (this.onColorChangedListener != null && paramInt != this.oldChangedListenerColor) {
      this.onColorChangedListener.onColorChanged(paramInt);
      this.oldChangedListenerColor = paramInt;
    } 
    invalidate();
  }
  
  public void setOldCenterColor(int paramInt) {
    this.mCenterOldColor = paramInt;
    this.mCenterOldPaint.setColor(paramInt);
    invalidate();
  }
  
  public void setOnColorChangedListener(OnColorChangedListener paramOnColorChangedListener) {
    this.onColorChangedListener = paramOnColorChangedListener;
  }
  
  public void setOnColorSelectedListener(OnColorSelectedListener paramOnColorSelectedListener) {
    this.onColorSelectedListener = paramOnColorSelectedListener;
  }
  
  public void setOnWheelColorChangeListener(OnWheelColorChangeListener paramOnWheelColorChangeListener) {
    this.onWheelColorChangeListener = paramOnWheelColorChangeListener;
  }
  
  public void setShowOldCenterColor(boolean paramBoolean) {
    this.mShowCenterOldColor = paramBoolean;
    invalidate();
  }
  
  public void setTouchAnywhereOnColorWheelEnabled(boolean paramBoolean) {
    this.mTouchAnywhereOnColorWheelEnabled = paramBoolean;
  }
  
  public static interface OnColorChangedListener {
    void onColorChanged(int param1Int);
  }
  
  public static interface OnColorSelectedListener {
    void onColorSelected(int param1Int);
  }
  
  public static interface OnWheelColorChangeListener {
    void onWheelColorChanged(int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/larswerkman/holocolorpicker/ColorPicker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */