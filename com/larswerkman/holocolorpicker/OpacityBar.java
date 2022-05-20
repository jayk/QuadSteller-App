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

public class OpacityBar extends View {
  private static final boolean ORIENTATION_DEFAULT = true;
  
  private static final boolean ORIENTATION_HORIZONTAL = true;
  
  private static final boolean ORIENTATION_VERTICAL = false;
  
  private static final String STATE_COLOR = "color";
  
  private static final String STATE_OPACITY = "opacity";
  
  private static final String STATE_ORIENTATION = "orientation";
  
  private static final String STATE_PARENT = "parent";
  
  private int mBarLength;
  
  private Paint mBarPaint;
  
  private Paint mBarPointerHaloPaint;
  
  private int mBarPointerHaloRadius;
  
  private Paint mBarPointerPaint;
  
  private int mBarPointerPosition;
  
  private int mBarPointerRadius;
  
  private RectF mBarRect = new RectF();
  
  private int mBarThickness;
  
  private int mColor;
  
  private float[] mHSVColor = new float[3];
  
  private boolean mIsMovingPointer;
  
  private float mOpacToPosFactor;
  
  private boolean mOrientation;
  
  private ColorPicker mPicker = null;
  
  private float mPosToOpacFactor;
  
  private int mPreferredBarLength;
  
  private int oldChangedListenerOpacity;
  
  private OnOpacityChangedListener onOpacityChangedListener;
  
  private Shader shader;
  
  public OpacityBar(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null, 0);
  }
  
  public OpacityBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet, 0);
  }
  
  public OpacityBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet, paramInt);
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
    this.mColor = Color.HSVToColor(Math.round(this.mPosToOpacFactor * paramInt), this.mHSVColor);
    if (Color.alpha(this.mColor) > 250) {
      this.mColor = Color.HSVToColor(this.mHSVColor);
      return;
    } 
    if (Color.alpha(this.mColor) < 5)
      this.mColor = 0; 
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
    this.mBarPointerPaint.setColor(-8257792);
    this.mPosToOpacFactor = 255.0F / this.mBarLength;
    this.mOpacToPosFactor = this.mBarLength / 255.0F;
  }
  
  public int getColor() {
    return this.mColor;
  }
  
  public OnOpacityChangedListener getOnOpacityChangedListener() {
    return this.onOpacityChangedListener;
  }
  
  public int getOpacity() {
    int i = Math.round(this.mPosToOpacFactor * (this.mBarPointerPosition - this.mBarPointerHaloRadius));
    if (i < 5)
      return 0; 
    int j = i;
    if (i > 250)
      j = 255; 
    return j;
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
    super.onRestoreInstanceState(bundle.getParcelable("parent"));
    setColor(Color.HSVToColor(bundle.getFloatArray("color")));
    setOpacity(bundle.getInt("opacity"));
  }
  
  protected Parcelable onSaveInstanceState() {
    Parcelable parcelable = super.onSaveInstanceState();
    Bundle bundle = new Bundle();
    bundle.putParcelable("parent", parcelable);
    bundle.putFloatArray("color", this.mHSVColor);
    bundle.putInt("opacity", getOpacity());
    return (Parcelable)bundle;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mOrientation == true) {
      paramInt3 = this.mBarLength + this.mBarPointerHaloRadius;
      paramInt2 = this.mBarThickness;
      this.mBarLength = paramInt1 - this.mBarPointerHaloRadius * 2;
      this.mBarRect.set(this.mBarPointerHaloRadius, (this.mBarPointerHaloRadius - this.mBarThickness / 2), (this.mBarLength + this.mBarPointerHaloRadius), (this.mBarPointerHaloRadius + this.mBarThickness / 2));
      paramInt1 = paramInt3;
    } else {
      paramInt1 = this.mBarThickness;
      paramInt3 = this.mBarLength + this.mBarPointerHaloRadius;
      this.mBarLength = paramInt2 - this.mBarPointerHaloRadius * 2;
      this.mBarRect.set((this.mBarPointerHaloRadius - this.mBarThickness / 2), this.mBarPointerHaloRadius, (this.mBarPointerHaloRadius + this.mBarThickness / 2), (this.mBarLength + this.mBarPointerHaloRadius));
      paramInt2 = paramInt3;
    } 
    if (!isInEditMode()) {
      float f1 = this.mBarPointerHaloRadius;
      float f2 = paramInt1;
      float f3 = paramInt2;
      paramInt2 = Color.HSVToColor(0, this.mHSVColor);
      paramInt1 = Color.HSVToColor(255, this.mHSVColor);
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      this.shader = (Shader)new LinearGradient(f1, 0.0F, f2, f3, new int[] { paramInt2, paramInt1 }, null, tileMode);
    } else {
      float f1 = this.mBarPointerHaloRadius;
      float f2 = paramInt1;
      float f3 = paramInt2;
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      this.shader = (Shader)new LinearGradient(f1, 0.0F, f2, f3, new int[] { 8519424, -8257792 }, null, tileMode);
      Color.colorToHSV(-8257792, this.mHSVColor);
    } 
    this.mBarPaint.setShader(this.shader);
    this.mPosToOpacFactor = 255.0F / this.mBarLength;
    this.mOpacToPosFactor = this.mBarLength / 255.0F;
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(this.mColor, arrayOfFloat);
    if (!isInEditMode()) {
      this.mBarPointerPosition = Math.round(this.mOpacToPosFactor * Color.alpha(this.mColor) + this.mBarPointerHaloRadius);
      return;
    } 
    this.mBarPointerPosition = this.mBarLength + this.mBarPointerHaloRadius;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f;
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
        if (this.mIsMovingPointer)
          if (f >= this.mBarPointerHaloRadius && f <= (this.mBarPointerHaloRadius + this.mBarLength)) {
            this.mBarPointerPosition = Math.round(f);
            calculateColor(Math.round(f));
            this.mBarPointerPaint.setColor(this.mColor);
            if (this.mPicker != null)
              this.mPicker.setNewCenterColor(this.mColor); 
            invalidate();
          } else if (f < this.mBarPointerHaloRadius) {
            this.mBarPointerPosition = this.mBarPointerHaloRadius;
            this.mColor = 0;
            this.mBarPointerPaint.setColor(this.mColor);
            if (this.mPicker != null)
              this.mPicker.setNewCenterColor(this.mColor); 
            invalidate();
          } else if (f > (this.mBarPointerHaloRadius + this.mBarLength)) {
            this.mBarPointerPosition = this.mBarPointerHaloRadius + this.mBarLength;
            this.mColor = Color.HSVToColor(this.mHSVColor);
            this.mBarPointerPaint.setColor(this.mColor);
            if (this.mPicker != null)
              this.mPicker.setNewCenterColor(this.mColor); 
            invalidate();
          }  
        if (this.onOpacityChangedListener != null && this.oldChangedListenerOpacity != getOpacity()) {
          this.onOpacityChangedListener.onOpacityChanged(getOpacity());
          this.oldChangedListenerOpacity = getOpacity();
        } 
      case 1:
        break;
    } 
    this.mIsMovingPointer = false;
  }
  
  public void setColor(int paramInt) {
    int j;
    if (this.mOrientation == true) {
      i = this.mBarLength + this.mBarPointerHaloRadius;
      j = this.mBarThickness;
    } else {
      i = this.mBarThickness;
      j = this.mBarLength + this.mBarPointerHaloRadius;
    } 
    Color.colorToHSV(paramInt, this.mHSVColor);
    float f1 = this.mBarPointerHaloRadius;
    float f2 = i;
    float f3 = j;
    int i = Color.HSVToColor(0, this.mHSVColor);
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    this.shader = (Shader)new LinearGradient(f1, 0.0F, f2, f3, new int[] { i, paramInt }, null, tileMode);
    this.mBarPaint.setShader(this.shader);
    calculateColor(this.mBarPointerPosition);
    this.mBarPointerPaint.setColor(this.mColor);
    if (this.mPicker != null)
      this.mPicker.setNewCenterColor(this.mColor); 
    invalidate();
  }
  
  public void setColorPicker(ColorPicker paramColorPicker) {
    this.mPicker = paramColorPicker;
  }
  
  public void setOnOpacityChangedListener(OnOpacityChangedListener paramOnOpacityChangedListener) {
    this.onOpacityChangedListener = paramOnOpacityChangedListener;
  }
  
  public void setOpacity(int paramInt) {
    this.mBarPointerPosition = Math.round(this.mOpacToPosFactor * paramInt) + this.mBarPointerHaloRadius;
    calculateColor(this.mBarPointerPosition);
    this.mBarPointerPaint.setColor(this.mColor);
    if (this.mPicker != null)
      this.mPicker.setNewCenterColor(this.mColor); 
    invalidate();
  }
  
  public static interface OnOpacityChangedListener {
    void onOpacityChanged(int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/larswerkman/holocolorpicker/OpacityBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */