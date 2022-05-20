package com.larswerkman.holocolorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyColorPicker extends View {
  private static final int[] COLORS = new int[] { -65536, -256, -16711936, -16711681, -16776961, -65281 };
  
  private static final boolean ORIENTATION_DEFAULT = true;
  
  private static final boolean ORIENTATION_HORIZONTAL = true;
  
  private static final boolean ORIENTATION_VERTICAL = false;
  
  private static final String STATE_COLOR = "mycolor";
  
  private static final String STATE_ORIENTATION = "myorientation";
  
  private static final String STATE_PARENT = "myparent";
  
  private static final String STATE_SATURATION = "mysaturation";
  
  private int mBarLength;
  
  private Paint mBarPaint;
  
  private Paint mBarPointerHaloPaint;
  
  private int mBarPointerHaloRadius;
  
  private Paint mBarPointerPaint;
  
  private int mBarPointerPosition;
  
  private int mBarPointerRadius;
  
  private RectF mBarRect = new RectF();
  
  private int mBarThickness;
  
  private boolean mClickable = true;
  
  private int mColor;
  
  private float[] mHSVColor = new float[3];
  
  private boolean mIsMovingPointer;
  
  private boolean mOrientation;
  
  private float mPosToSatFactor;
  
  private int mPreferredBarLength;
  
  private float mSatToPosFactor;
  
  private MySaturationBar mSaturationBar = null;
  
  private int oldChangedListenerColor;
  
  private int oldChangedListenerSaturation;
  
  private OnColorChangedListener onColorChangedListener;
  
  private OnSaturationChangedListener onSaturationChangedListener;
  
  private Shader shader;
  
  public MyColorPicker(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null, 0);
  }
  
  public MyColorPicker(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet, 0);
  }
  
  public MyColorPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet, paramInt);
  }
  
  private int ave(int paramInt1, int paramInt2, float paramFloat) {
    return Math.round((paramInt2 - paramInt1) * paramFloat) + paramInt1;
  }
  
  private void calculateColor(int paramInt) {
    int i = paramInt - this.mBarPointerHaloRadius;
    if (i < 0) {
      paramInt = 0;
    } else {
      paramInt = i;
      if (i > this.mBarLength)
        paramInt = this.mBarLength; 
    } 
    float f = paramInt / this.mBarLength;
    paramInt = (int)(f * 5.0F);
    getColor(f * 5.0F - paramInt, paramInt);
  }
  
  private float colorToAngle(int paramInt) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    return (float)Math.toRadians(arrayOfFloat[0]);
  }
  
  private void getColor(float paramFloat, int paramInt) {
    byte b = 5;
    int i = COLORS[paramInt];
    if (paramInt + 1 > 5) {
      paramInt = b;
    } else {
      paramInt++;
    } 
    paramInt = COLORS[paramInt];
    this.mColor = Color.argb(ave(Color.alpha(i), Color.alpha(paramInt), paramFloat), ave(Color.red(i), Color.red(paramInt), paramFloat), ave(Color.green(i), Color.green(paramInt), paramFloat), ave(Color.blue(i), Color.blue(paramInt), paramFloat));
  }
  
  private void init(AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ColorBars, paramInt, 0);
    Resources resources = getContext().getResources();
    this.mBarThickness = typedArray.getDimensionPixelSize(R.styleable.ColorBars_bar_thickness, resources.getDimensionPixelSize(R.dimen.bar_thickness));
    this.mBarLength = typedArray.getDimensionPixelSize(R.styleable.ColorBars_bar_length, resources.getDimensionPixelSize(R.dimen.bar_length));
    this.mPreferredBarLength = this.mBarLength;
    this.mBarPointerRadius = typedArray.getDimensionPixelSize(R.styleable.ColorBars_bar_pointer_radius, resources.getDimensionPixelSize(R.dimen.bar_pointer_radius));
    this.mBarPointerHaloRadius = typedArray.getDimensionPixelSize(R.styleable.ColorBars_bar_pointer_halo_radius, resources.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius));
    this.mOrientation = typedArray.getBoolean(R.styleable.ColorBars_bar_orientation_horizontal, true);
    typedArray.recycle();
    this.mBarPaint = new Paint(1);
    this.mBarPaint.setShader(this.shader);
    this.mBarPointerPosition = this.mBarLength + this.mBarPointerHaloRadius;
    this.mBarPointerHaloPaint = new Paint(1);
    this.mBarPointerHaloPaint.setColor(-16777216);
    this.mBarPointerHaloPaint.setAlpha(80);
    this.mBarPointerPaint = new Paint(1);
    this.mBarPointerPaint.setColor(this.mColor);
    calculateColor(this.mBarPointerPosition);
    this.mBarPointerPaint.setColor(this.mColor);
    this.mPosToSatFactor = 1.0F / this.mBarLength;
    this.mSatToPosFactor = this.mBarLength / 1.0F;
  }
  
  public void addSaturationBar(MySaturationBar paramMySaturationBar) {
    this.mSaturationBar = paramMySaturationBar;
    this.mSaturationBar.setColorPicker(this);
    this.mSaturationBar.setColor(this.mColor);
  }
  
  public int getColor() {
    return this.mColor;
  }
  
  public OnSaturationChangedListener getOnSaturationChangedListener() {
    return this.onSaturationChangedListener;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    int i;
    int j;
    paramCanvas.drawRect(this.mBarRect, this.mBarPaint);
    if (this.mOrientation == true) {
      i = this.mBarPointerPosition;
      j = this.mBarPointerHaloRadius;
    } else {
      i = this.mBarPointerHaloRadius;
      j = this.mBarPointerPosition;
    } 
    paramCanvas.drawCircle(i, j, this.mBarPointerHaloRadius, this.mBarPointerHaloPaint);
    paramCanvas.drawCircle(i, j, this.mBarPointerRadius, this.mBarPointerPaint);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = this.mPreferredBarLength + this.mBarPointerHaloRadius * 2;
    if (this.mOrientation != true)
      paramInt1 = paramInt2; 
    paramInt2 = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    if (paramInt2 != 1073741824)
      if (paramInt2 == Integer.MIN_VALUE) {
        paramInt1 = Math.min(i, paramInt1);
      } else {
        paramInt1 = i;
      }  
    paramInt2 = this.mBarPointerHaloRadius * 2;
    this.mBarLength = paramInt1 - paramInt2;
    if (!this.mOrientation) {
      setMeasuredDimension(paramInt2, this.mBarLength + paramInt2);
      return;
    } 
    setMeasuredDimension(this.mBarLength + paramInt2, paramInt2);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    Bundle bundle = (Bundle)paramParcelable;
    super.onRestoreInstanceState(bundle.getParcelable("myparent"));
    setColor(Color.HSVToColor(bundle.getFloatArray("mycolor")));
    setSaturation(bundle.getFloat("mysaturation"));
  }
  
  protected Parcelable onSaveInstanceState() {
    Parcelable parcelable = super.onSaveInstanceState();
    Bundle bundle = new Bundle();
    bundle.putParcelable("myparent", parcelable);
    bundle.putFloatArray("mycolor", this.mHSVColor);
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(this.mColor, arrayOfFloat);
    bundle.putFloat("mysaturation", arrayOfFloat[1]);
    return (Parcelable)bundle;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mOrientation == true) {
      paramInt2 = this.mBarLength + this.mBarPointerHaloRadius;
      paramInt3 = this.mBarThickness;
      this.mBarLength = paramInt1 - this.mBarPointerHaloRadius * 2;
      this.mBarRect.set(this.mBarPointerHaloRadius, (this.mBarPointerHaloRadius - this.mBarThickness / 2), (this.mBarLength + this.mBarPointerHaloRadius), (this.mBarPointerHaloRadius + this.mBarThickness / 2));
      paramInt1 = paramInt3;
    } else {
      paramInt3 = this.mBarThickness;
      paramInt1 = this.mBarLength + this.mBarPointerHaloRadius;
      this.mBarLength = paramInt2 - this.mBarPointerHaloRadius * 2;
      this.mBarRect.set((this.mBarPointerHaloRadius - this.mBarThickness / 2), this.mBarPointerHaloRadius, (this.mBarPointerHaloRadius + this.mBarThickness / 2), (this.mBarLength + this.mBarPointerHaloRadius));
      paramInt2 = paramInt3;
    } 
    if (!isInEditMode()) {
      this.shader = (Shader)new LinearGradient(this.mBarPointerHaloRadius, 0.0F, paramInt2, paramInt1, COLORS, null, Shader.TileMode.CLAMP);
    } else {
      this.shader = (Shader)new LinearGradient(this.mBarPointerHaloRadius, 0.0F, paramInt2, paramInt1, COLORS, null, Shader.TileMode.CLAMP);
      Color.colorToHSV(-8257792, this.mHSVColor);
    } 
    this.mBarPaint.setShader(this.shader);
    this.mPosToSatFactor = 1.0F / this.mBarLength;
    this.mSatToPosFactor = this.mBarLength / 1.0F;
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(this.mColor, arrayOfFloat);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    if (!this.mClickable);
    getParent().requestDisallowInterceptTouchEvent(true);
    if (this.mOrientation == true) {
      f = paramMotionEvent.getX();
    } else {
      f = paramMotionEvent.getY();
    } 
    switch (paramMotionEvent.getAction()) {
      default:
        return true;
      case 0:
        this.mIsMovingPointer = true;
        if (f >= this.mBarPointerHaloRadius && f <= (this.mBarPointerHaloRadius + this.mBarLength)) {
          this.mBarPointerPosition = Math.round(f);
          calculateColor(Math.round(f));
          this.mBarPointerPaint.setColor(this.mColor);
          invalidate();
        } 
      case 2:
        if (this.mIsMovingPointer && f >= this.mBarPointerHaloRadius && f <= (this.mBarPointerHaloRadius + this.mBarLength)) {
          this.mBarPointerPosition = Math.round(f);
          calculateColor(Math.round(f));
          this.mBarPointerPaint.setColor(this.mColor);
          if (this.mSaturationBar != null && this.onColorChangedListener != null) {
            int i = this.mSaturationBar.setColor(this.mColor);
            this.onColorChangedListener.onColorChanged(i);
          } 
          if (this.mColor != this.oldChangedListenerColor)
            this.oldChangedListenerColor = this.mColor; 
          invalidate();
        } 
        if (this.onSaturationChangedListener != null && this.oldChangedListenerSaturation != this.mColor) {
          this.onSaturationChangedListener.onSaturationChanged(this.mColor);
          this.oldChangedListenerSaturation = this.mColor;
        } 
      case 1:
        break;
    } 
    this.mIsMovingPointer = false;
    if (this.mSaturationBar != null && this.onColorChangedListener != null) {
      int i = this.mSaturationBar.setColor(this.mColor);
      this.onColorChangedListener.onColorChanged(i);
    } 
    if (this.mColor != this.oldChangedListenerColor)
      this.oldChangedListenerColor = this.mColor; 
  }
  
  public void setClickable(boolean paramBoolean) {
    this.mClickable = paramBoolean;
  }
  
  public void setColor(int paramInt) {
    float f = colorToAngle(paramInt);
    getColor(f, (int)f);
    this.mBarPointerPosition = (int)(f / 5.0F * this.mBarLength) + this.mBarPointerHaloRadius;
    this.mBarPointerPaint.setColor(this.mColor);
    if (this.mSaturationBar != null) {
      Color.colorToHSV(paramInt, this.mHSVColor);
      this.mSaturationBar.setColor(this.mColor);
      this.mSaturationBar.setSaturation(this.mHSVColor[1]);
    } 
    invalidate();
  }
  
  public void setOnColorChangedListener(OnColorChangedListener paramOnColorChangedListener) {
    this.onColorChangedListener = paramOnColorChangedListener;
  }
  
  public void setOnSaturationChangedListener(OnSaturationChangedListener paramOnSaturationChangedListener) {
    this.onSaturationChangedListener = paramOnSaturationChangedListener;
  }
  
  public void setSaturation(float paramFloat) {
    this.mBarPointerPosition = Math.round(this.mSatToPosFactor * paramFloat) + this.mBarPointerHaloRadius;
    calculateColor(this.mBarPointerPosition);
    this.mBarPointerPaint.setColor(this.mColor);
    invalidate();
  }
  
  public static interface OnColorChangedListener {
    void onColorChanged(int param1Int);
  }
  
  public static interface OnSaturationChangedListener {
    void onSaturationChanged(int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/larswerkman/holocolorpicker/MyColorPicker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */