package com.kyleduo.switchbutton;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

public class SwitchButton extends CompoundButton {
  private static int[] CHECKED_PRESSED_STATE = new int[] { 16842912, 16842910, 16842919 };
  
  public static final int DEFAULT_ANIMATION_DURATION = 250;
  
  public static final float DEFAULT_BACK_MEASURE_RATIO = 1.8F;
  
  public static final int DEFAULT_TEXT_MARGIN_DP = 2;
  
  public static final int DEFAULT_THUMB_MARGIN_DP = 2;
  
  public static final int DEFAULT_THUMB_SIZE_DP = 20;
  
  public static final int DEFAULT_TINT_COLOR = 3309506;
  
  private static int[] UNCHECKED_PRESSED_STATE = new int[] { -16842912, 16842910, 16842919 };
  
  private long mAnimationDuration;
  
  private ColorStateList mBackColor;
  
  private Drawable mBackDrawable;
  
  private float mBackMeasureRatio;
  
  private float mBackRadius;
  
  private RectF mBackRectF;
  
  private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
  
  private int mClickTimeout;
  
  private int mCurrBackColor;
  
  private int mCurrThumbColor;
  
  private Drawable mCurrentBackDrawable;
  
  private boolean mDrawDebugRect = false;
  
  private boolean mFadeBack;
  
  private boolean mIsBackUseDrawable;
  
  private boolean mIsThumbUseDrawable;
  
  private float mLastX;
  
  private int mNextBackColor;
  
  private Drawable mNextBackDrawable;
  
  private Layout mOffLayout;
  
  private int mOffTextColor;
  
  private Layout mOnLayout;
  
  private int mOnTextColor;
  
  private Paint mPaint;
  
  private RectF mPresentThumbRectF;
  
  private float mProcess;
  
  private ObjectAnimator mProcessAnimator;
  
  private Paint mRectPaint;
  
  private RectF mSafeRectF;
  
  private float mStartX;
  
  private float mStartY;
  
  private float mTextHeight;
  
  private float mTextMarginH;
  
  private CharSequence mTextOff;
  
  private RectF mTextOffRectF;
  
  private CharSequence mTextOn;
  
  private RectF mTextOnRectF;
  
  private TextPaint mTextPaint;
  
  private float mTextWidth;
  
  private ColorStateList mThumbColor;
  
  private Drawable mThumbDrawable;
  
  private RectF mThumbMargin;
  
  private float mThumbRadius;
  
  private RectF mThumbRectF;
  
  private PointF mThumbSizeF;
  
  private int mTintColor;
  
  private int mTouchSlop;
  
  public SwitchButton(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public SwitchButton(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public SwitchButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private void catchView() {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(true); 
  }
  
  private int ceil(double paramDouble) {
    return (int)Math.ceil(paramDouble);
  }
  
  private boolean getStatusBasedOnPos() {
    return (getProcess() > 0.5F);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    TypedArray typedArray;
    boolean bool1;
    this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
    this.mPaint = new Paint(1);
    this.mRectPaint = new Paint(1);
    this.mRectPaint.setStyle(Paint.Style.STROKE);
    this.mRectPaint.setStrokeWidth((getResources().getDisplayMetrics()).density);
    this.mTextPaint = getPaint();
    this.mThumbRectF = new RectF();
    this.mBackRectF = new RectF();
    this.mSafeRectF = new RectF();
    this.mThumbSizeF = new PointF();
    this.mThumbMargin = new RectF();
    this.mTextOnRectF = new RectF();
    this.mTextOffRectF = new RectF();
    this.mProcessAnimator = ObjectAnimator.ofFloat(this, "process", new float[] { 0.0F, 0.0F }).setDuration(250L);
    this.mProcessAnimator.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
    this.mPresentThumbRectF = new RectF();
    float f1 = (getResources().getDisplayMetrics()).density;
    Drawable drawable1 = null;
    ColorStateList colorStateList1 = null;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = f1 * 20.0F;
    float f7 = f1 * 20.0F;
    float f8 = 20.0F * f1 / 2.0F;
    float f9 = f8;
    Drawable drawable2 = null;
    ColorStateList colorStateList2 = null;
    float f10 = 1.8F;
    int i = 250;
    boolean bool = true;
    int j = 0;
    String str1 = null;
    String str2 = null;
    float f11 = f1 * 2.0F;
    if (paramAttributeSet == null) {
      paramAttributeSet = null;
    } else {
      typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.SwitchButton);
    } 
    float f12 = f11;
    float f13 = f7;
    float f14 = f6;
    if (typedArray != null) {
      drawable1 = typedArray.getDrawable(R.styleable.SwitchButton_kswThumbDrawable);
      colorStateList1 = typedArray.getColorStateList(R.styleable.SwitchButton_kswThumbColor);
      f10 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbMargin, f1 * 2.0F);
      f2 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbMarginLeft, f10);
      f3 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbMarginRight, f10);
      f4 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbMarginTop, f10);
      f5 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbMarginBottom, f10);
      f14 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbWidth, f6);
      f13 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbHeight, f7);
      f8 = typedArray.getDimension(R.styleable.SwitchButton_kswThumbRadius, Math.min(f14, f13) / 2.0F);
      f9 = typedArray.getDimension(R.styleable.SwitchButton_kswBackRadius, 2.0F * f1 + f8);
      drawable2 = typedArray.getDrawable(R.styleable.SwitchButton_kswBackDrawable);
      colorStateList2 = typedArray.getColorStateList(R.styleable.SwitchButton_kswBackColor);
      f10 = typedArray.getFloat(R.styleable.SwitchButton_kswBackMeasureRatio, 1.8F);
      i = typedArray.getInteger(R.styleable.SwitchButton_kswAnimationDuration, 250);
      bool = typedArray.getBoolean(R.styleable.SwitchButton_kswFadeBack, true);
      j = typedArray.getColor(R.styleable.SwitchButton_kswTintColor, 0);
      str1 = typedArray.getString(R.styleable.SwitchButton_kswTextOn);
      str2 = typedArray.getString(R.styleable.SwitchButton_kswTextOff);
      f12 = typedArray.getDimension(R.styleable.SwitchButton_kswTextMarginH, f11);
      typedArray.recycle();
    } 
    this.mTextOn = str1;
    this.mTextOff = str2;
    this.mTextMarginH = f12;
    this.mThumbDrawable = drawable1;
    this.mThumbColor = colorStateList1;
    if (this.mThumbDrawable != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mIsThumbUseDrawable = bool1;
    this.mTintColor = j;
    if (this.mTintColor == 0) {
      TypedValue typedValue = new TypedValue();
      if (getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true)) {
        this.mTintColor = typedValue.data;
      } else {
        this.mTintColor = 3309506;
      } 
    } 
    if (!this.mIsThumbUseDrawable && this.mThumbColor == null) {
      this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
      this.mCurrThumbColor = this.mThumbColor.getDefaultColor();
    } 
    f11 = f13;
    f12 = f14;
    if (this.mIsThumbUseDrawable) {
      f12 = Math.max(f14, this.mThumbDrawable.getMinimumWidth());
      f11 = Math.max(f13, this.mThumbDrawable.getMinimumHeight());
    } 
    this.mThumbSizeF.set(f12, f11);
    this.mBackDrawable = drawable2;
    this.mBackColor = colorStateList2;
    if (this.mBackDrawable != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mIsBackUseDrawable = bool1;
    if (!this.mIsBackUseDrawable && this.mBackColor == null) {
      this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
      this.mCurrBackColor = this.mBackColor.getDefaultColor();
      this.mNextBackColor = this.mBackColor.getColorForState(CHECKED_PRESSED_STATE, this.mCurrBackColor);
    } 
    this.mThumbMargin.set(f2, f4, f3, f5);
    f14 = f10;
    if (this.mThumbMargin.width() >= 0.0F)
      f14 = Math.max(f10, 1.0F); 
    this.mBackMeasureRatio = f14;
    this.mThumbRadius = f8;
    this.mBackRadius = f9;
    this.mAnimationDuration = i;
    this.mFadeBack = bool;
    this.mProcessAnimator.setDuration(this.mAnimationDuration);
    setFocusable(true);
    setClickable(true);
    if (isChecked())
      setProcess(1.0F); 
  }
  
  private Layout makeLayout(CharSequence paramCharSequence) {
    return (Layout)new StaticLayout(paramCharSequence, this.mTextPaint, (int)Math.ceil(Layout.getDesiredWidth(paramCharSequence, this.mTextPaint)), Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
  }
  
  private int measureHeight(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_2
    //   5: iload_1
    //   6: invokestatic getSize : (I)I
    //   9: istore_3
    //   10: aload_0
    //   11: aload_0
    //   12: getfield mThumbSizeF : Landroid/graphics/PointF;
    //   15: getfield y : F
    //   18: aload_0
    //   19: getfield mThumbSizeF : Landroid/graphics/PointF;
    //   22: getfield y : F
    //   25: aload_0
    //   26: getfield mThumbMargin : Landroid/graphics/RectF;
    //   29: getfield top : F
    //   32: fadd
    //   33: aload_0
    //   34: getfield mThumbMargin : Landroid/graphics/RectF;
    //   37: getfield right : F
    //   40: fadd
    //   41: invokestatic max : (FF)F
    //   44: f2d
    //   45: invokespecial ceil : (D)I
    //   48: istore #4
    //   50: aload_0
    //   51: getfield mOnLayout : Landroid/text/Layout;
    //   54: ifnull -> 170
    //   57: aload_0
    //   58: getfield mOnLayout : Landroid/text/Layout;
    //   61: invokevirtual getHeight : ()I
    //   64: i2f
    //   65: fstore #5
    //   67: aload_0
    //   68: getfield mOffLayout : Landroid/text/Layout;
    //   71: ifnull -> 176
    //   74: aload_0
    //   75: getfield mOffLayout : Landroid/text/Layout;
    //   78: invokevirtual getHeight : ()I
    //   81: i2f
    //   82: fstore #6
    //   84: fload #5
    //   86: fconst_0
    //   87: fcmpl
    //   88: ifne -> 101
    //   91: iload #4
    //   93: istore_1
    //   94: fload #6
    //   96: fconst_0
    //   97: fcmpl
    //   98: ifeq -> 128
    //   101: aload_0
    //   102: fload #5
    //   104: fload #6
    //   106: invokestatic max : (FF)F
    //   109: putfield mTextHeight : F
    //   112: aload_0
    //   113: iload #4
    //   115: i2f
    //   116: aload_0
    //   117: getfield mTextHeight : F
    //   120: invokestatic max : (FF)F
    //   123: f2d
    //   124: invokespecial ceil : (D)I
    //   127: istore_1
    //   128: iload_1
    //   129: aload_0
    //   130: invokevirtual getSuggestedMinimumHeight : ()I
    //   133: invokestatic max : (II)I
    //   136: istore_1
    //   137: iload_1
    //   138: aload_0
    //   139: invokevirtual getPaddingTop : ()I
    //   142: iload_1
    //   143: iadd
    //   144: aload_0
    //   145: invokevirtual getPaddingBottom : ()I
    //   148: iadd
    //   149: invokestatic max : (II)I
    //   152: istore #4
    //   154: iload_2
    //   155: ldc_w 1073741824
    //   158: if_icmpne -> 182
    //   161: iload #4
    //   163: iload_3
    //   164: invokestatic max : (II)I
    //   167: istore_1
    //   168: iload_1
    //   169: ireturn
    //   170: fconst_0
    //   171: fstore #5
    //   173: goto -> 67
    //   176: fconst_0
    //   177: fstore #6
    //   179: goto -> 84
    //   182: iload #4
    //   184: istore_1
    //   185: iload_2
    //   186: ldc_w -2147483648
    //   189: if_icmpne -> 168
    //   192: iload #4
    //   194: iload_3
    //   195: invokestatic min : (II)I
    //   198: istore_1
    //   199: goto -> 168
  }
  
  private int measureWidth(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getSize : (I)I
    //   4: istore_2
    //   5: iload_1
    //   6: invokestatic getMode : (I)I
    //   9: istore_3
    //   10: aload_0
    //   11: aload_0
    //   12: getfield mThumbSizeF : Landroid/graphics/PointF;
    //   15: getfield x : F
    //   18: aload_0
    //   19: getfield mBackMeasureRatio : F
    //   22: fmul
    //   23: f2d
    //   24: invokespecial ceil : (D)I
    //   27: istore #4
    //   29: iload #4
    //   31: istore_1
    //   32: aload_0
    //   33: getfield mIsBackUseDrawable : Z
    //   36: ifeq -> 52
    //   39: iload #4
    //   41: aload_0
    //   42: getfield mBackDrawable : Landroid/graphics/drawable/Drawable;
    //   45: invokevirtual getMinimumWidth : ()I
    //   48: invokestatic max : (II)I
    //   51: istore_1
    //   52: aload_0
    //   53: getfield mOnLayout : Landroid/text/Layout;
    //   56: ifnull -> 229
    //   59: aload_0
    //   60: getfield mOnLayout : Landroid/text/Layout;
    //   63: invokevirtual getWidth : ()I
    //   66: i2f
    //   67: fstore #5
    //   69: aload_0
    //   70: getfield mOffLayout : Landroid/text/Layout;
    //   73: ifnull -> 235
    //   76: aload_0
    //   77: getfield mOffLayout : Landroid/text/Layout;
    //   80: invokevirtual getWidth : ()I
    //   83: i2f
    //   84: fstore #6
    //   86: fload #5
    //   88: fconst_0
    //   89: fcmpl
    //   90: ifne -> 103
    //   93: iload_1
    //   94: istore #4
    //   96: fload #6
    //   98: fconst_0
    //   99: fcmpl
    //   100: ifeq -> 159
    //   103: aload_0
    //   104: fload #5
    //   106: fload #6
    //   108: invokestatic max : (FF)F
    //   111: aload_0
    //   112: getfield mTextMarginH : F
    //   115: fconst_2
    //   116: fmul
    //   117: fadd
    //   118: putfield mTextWidth : F
    //   121: iload_1
    //   122: i2f
    //   123: aload_0
    //   124: getfield mThumbSizeF : Landroid/graphics/PointF;
    //   127: getfield x : F
    //   130: fsub
    //   131: fstore #5
    //   133: iload_1
    //   134: istore #4
    //   136: fload #5
    //   138: aload_0
    //   139: getfield mTextWidth : F
    //   142: fcmpg
    //   143: ifge -> 159
    //   146: iload_1
    //   147: i2f
    //   148: aload_0
    //   149: getfield mTextWidth : F
    //   152: fload #5
    //   154: fsub
    //   155: fadd
    //   156: f2i
    //   157: istore #4
    //   159: iload #4
    //   161: aload_0
    //   162: iload #4
    //   164: i2f
    //   165: aload_0
    //   166: getfield mThumbMargin : Landroid/graphics/RectF;
    //   169: getfield left : F
    //   172: fadd
    //   173: aload_0
    //   174: getfield mThumbMargin : Landroid/graphics/RectF;
    //   177: getfield right : F
    //   180: fadd
    //   181: f2d
    //   182: invokespecial ceil : (D)I
    //   185: invokestatic max : (II)I
    //   188: istore_1
    //   189: iload_1
    //   190: aload_0
    //   191: invokevirtual getPaddingLeft : ()I
    //   194: iload_1
    //   195: iadd
    //   196: aload_0
    //   197: invokevirtual getPaddingRight : ()I
    //   200: iadd
    //   201: invokestatic max : (II)I
    //   204: aload_0
    //   205: invokevirtual getSuggestedMinimumWidth : ()I
    //   208: invokestatic max : (II)I
    //   211: istore #4
    //   213: iload_3
    //   214: ldc_w 1073741824
    //   217: if_icmpne -> 241
    //   220: iload #4
    //   222: iload_2
    //   223: invokestatic max : (II)I
    //   226: istore_1
    //   227: iload_1
    //   228: ireturn
    //   229: fconst_0
    //   230: fstore #5
    //   232: goto -> 69
    //   235: fconst_0
    //   236: fstore #6
    //   238: goto -> 86
    //   241: iload #4
    //   243: istore_1
    //   244: iload_3
    //   245: ldc_w -2147483648
    //   248: if_icmpne -> 227
    //   251: iload #4
    //   253: iload_2
    //   254: invokestatic min : (II)I
    //   257: istore_1
    //   258: goto -> 227
  }
  
  private void setDrawableState(Drawable paramDrawable) {
    if (paramDrawable != null) {
      paramDrawable.setState(getDrawableState());
      invalidate();
    } 
  }
  
  private void setup() {
    float f1 = getPaddingTop() + Math.max(0.0F, this.mThumbMargin.top);
    float f2 = getPaddingLeft() + Math.max(0.0F, this.mThumbMargin.left);
    float f3 = f1;
    if (this.mOnLayout != null) {
      f3 = f1;
      if (this.mOffLayout != null) {
        f3 = f1;
        if (this.mThumbMargin.top + this.mThumbMargin.bottom > 0.0F)
          f3 = f1 + ((getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) - this.mThumbSizeF.y - this.mThumbMargin.top - this.mThumbMargin.bottom) / 2.0F; 
      } 
    } 
    if (this.mIsThumbUseDrawable) {
      this.mThumbSizeF.x = Math.max(this.mThumbSizeF.x, this.mThumbDrawable.getMinimumWidth());
      this.mThumbSizeF.y = Math.max(this.mThumbSizeF.y, this.mThumbDrawable.getMinimumHeight());
    } 
    this.mThumbRectF.set(f2, f3, this.mThumbSizeF.x + f2, this.mThumbSizeF.y + f3);
    f1 = this.mThumbRectF.left - this.mThumbMargin.left;
    f2 = Math.min(0.0F, (Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth) - this.mThumbRectF.width() - this.mTextWidth) / 2.0F);
    f3 = Math.min(0.0F, (this.mThumbRectF.height() + this.mThumbMargin.top + this.mThumbMargin.bottom - this.mTextHeight) / 2.0F);
    this.mBackRectF.set(f1 + f2, this.mThumbRectF.top - this.mThumbMargin.top + f3, this.mThumbMargin.left + f1 + Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth) + this.mThumbMargin.right - f2, this.mThumbRectF.bottom + this.mThumbMargin.bottom - f3);
    this.mSafeRectF.set(this.mThumbRectF.left, 0.0F, this.mBackRectF.right - this.mThumbMargin.right - this.mThumbRectF.width(), 0.0F);
    this.mBackRadius = Math.min(Math.min(this.mBackRectF.width(), this.mBackRectF.height()) / 2.0F, this.mBackRadius);
    if (this.mBackDrawable != null)
      this.mBackDrawable.setBounds((int)this.mBackRectF.left, (int)this.mBackRectF.top, ceil(this.mBackRectF.right), ceil(this.mBackRectF.bottom)); 
    if (this.mOnLayout != null) {
      byte b;
      float f = this.mBackRectF.left;
      f3 = (this.mBackRectF.width() - this.mThumbRectF.width() - this.mOnLayout.getWidth()) / 2.0F;
      f1 = this.mThumbMargin.left;
      f2 = this.mTextMarginH;
      if (this.mThumbMargin.left > 0.0F) {
        b = 1;
      } else {
        b = -1;
      } 
      f3 = f + f3 - f1 + b * f2;
      f1 = this.mBackRectF.top + (this.mBackRectF.height() - this.mOnLayout.getHeight()) / 2.0F;
      this.mTextOnRectF.set(f3, f1, this.mOnLayout.getWidth() + f3, this.mOnLayout.getHeight() + f1);
    } 
    if (this.mOffLayout != null) {
      byte b;
      f2 = this.mBackRectF.right;
      float f5 = (this.mBackRectF.width() - this.mThumbRectF.width() - this.mOffLayout.getWidth()) / 2.0F;
      f1 = this.mThumbMargin.right;
      f3 = this.mOffLayout.getWidth();
      float f4 = this.mTextMarginH;
      if (this.mThumbMargin.right > 0.0F) {
        b = 1;
      } else {
        b = -1;
      } 
      f3 = f2 - f5 + f1 - f3 - b * f4;
      f1 = this.mBackRectF.top + (this.mBackRectF.height() - this.mOffLayout.getHeight()) / 2.0F;
      this.mTextOffRectF.set(f3, f1, this.mOffLayout.getWidth() + f3, this.mOffLayout.getHeight() + f1);
    } 
  }
  
  protected void animateToState(boolean paramBoolean) {
    if (this.mProcessAnimator != null) {
      if (this.mProcessAnimator.isRunning())
        this.mProcessAnimator.cancel(); 
      this.mProcessAnimator.setDuration(this.mAnimationDuration);
      if (paramBoolean) {
        this.mProcessAnimator.setFloatValues(new float[] { this.mProcess, 1.0F });
      } else {
        this.mProcessAnimator.setFloatValues(new float[] { this.mProcess, 0.0F });
      } 
      this.mProcessAnimator.start();
    } 
  }
  
  protected void drawableStateChanged() {
    int[] arrayOfInt;
    super.drawableStateChanged();
    if (!this.mIsThumbUseDrawable && this.mThumbColor != null) {
      this.mCurrThumbColor = this.mThumbColor.getColorForState(getDrawableState(), this.mCurrThumbColor);
    } else {
      setDrawableState(this.mThumbDrawable);
    } 
    if (isChecked()) {
      arrayOfInt = UNCHECKED_PRESSED_STATE;
    } else {
      arrayOfInt = CHECKED_PRESSED_STATE;
    } 
    ColorStateList colorStateList = getTextColors();
    if (colorStateList != null) {
      int i = colorStateList.getDefaultColor();
      this.mOnTextColor = colorStateList.getColorForState(CHECKED_PRESSED_STATE, i);
      this.mOffTextColor = colorStateList.getColorForState(UNCHECKED_PRESSED_STATE, i);
    } 
    if (!this.mIsBackUseDrawable && this.mBackColor != null) {
      this.mCurrBackColor = this.mBackColor.getColorForState(getDrawableState(), this.mCurrBackColor);
      this.mNextBackColor = this.mBackColor.getColorForState(arrayOfInt, this.mCurrBackColor);
      return;
    } 
    if (this.mBackDrawable instanceof android.graphics.drawable.StateListDrawable && this.mFadeBack) {
      this.mBackDrawable.setState(arrayOfInt);
      this.mNextBackDrawable = this.mBackDrawable.getCurrent().mutate();
    } else {
      this.mNextBackDrawable = null;
    } 
    setDrawableState(this.mBackDrawable);
    if (this.mBackDrawable != null)
      this.mCurrentBackDrawable = this.mBackDrawable.getCurrent().mutate(); 
  }
  
  public long getAnimationDuration() {
    return this.mAnimationDuration;
  }
  
  public ColorStateList getBackColor() {
    return this.mBackColor;
  }
  
  public Drawable getBackDrawable() {
    return this.mBackDrawable;
  }
  
  public float getBackMeasureRatio() {
    return this.mBackMeasureRatio;
  }
  
  public float getBackRadius() {
    return this.mBackRadius;
  }
  
  public PointF getBackSizeF() {
    return new PointF(this.mBackRectF.width(), this.mBackRectF.height());
  }
  
  public final float getProcess() {
    return this.mProcess;
  }
  
  public ColorStateList getThumbColor() {
    return this.mThumbColor;
  }
  
  public Drawable getThumbDrawable() {
    return this.mThumbDrawable;
  }
  
  public float getThumbHeight() {
    return this.mThumbSizeF.y;
  }
  
  public RectF getThumbMargin() {
    return this.mThumbMargin;
  }
  
  public float getThumbRadius() {
    return this.mThumbRadius;
  }
  
  public PointF getThumbSizeF() {
    return this.mThumbSizeF;
  }
  
  public float getThumbWidth() {
    return this.mThumbSizeF.x;
  }
  
  public int getTintColor() {
    return this.mTintColor;
  }
  
  public boolean isDrawDebugRect() {
    return this.mDrawDebugRect;
  }
  
  public boolean isFadeBack() {
    return this.mFadeBack;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    Layout layout;
    RectF rectF;
    super.onDraw(paramCanvas);
    if (this.mIsBackUseDrawable) {
      if (this.mFadeBack && this.mCurrentBackDrawable != null && this.mNextBackDrawable != null) {
        float f;
        if (isChecked()) {
          f = getProcess();
        } else {
          f = 1.0F - getProcess();
        } 
        int i = (int)(f * 255.0F);
        this.mCurrentBackDrawable.setAlpha(i);
        this.mCurrentBackDrawable.draw(paramCanvas);
        this.mNextBackDrawable.setAlpha(255 - i);
        this.mNextBackDrawable.draw(paramCanvas);
      } else {
        this.mBackDrawable.setAlpha(255);
        this.mBackDrawable.draw(paramCanvas);
      } 
    } else if (this.mFadeBack) {
      float f;
      if (isChecked()) {
        f = getProcess();
      } else {
        f = 1.0F - getProcess();
      } 
      int i = (int)(f * 255.0F);
      int j = Color.alpha(this.mCurrBackColor) * i / 255;
      this.mPaint.setARGB(j, Color.red(this.mCurrBackColor), Color.green(this.mCurrBackColor), Color.blue(this.mCurrBackColor));
      paramCanvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
      i = Color.alpha(this.mNextBackColor) * (255 - i) / 255;
      this.mPaint.setARGB(i, Color.red(this.mNextBackColor), Color.green(this.mNextBackColor), Color.blue(this.mNextBackColor));
      paramCanvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
      this.mPaint.setAlpha(255);
    } else {
      this.mPaint.setColor(this.mCurrBackColor);
      paramCanvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
    } 
    if (getProcess() > 0.5D) {
      layout = this.mOnLayout;
    } else {
      layout = this.mOffLayout;
    } 
    if (getProcess() > 0.5D) {
      rectF = this.mTextOnRectF;
    } else {
      rectF = this.mTextOffRectF;
    } 
    if (layout != null && rectF != null) {
      float f;
      int i;
      if (getProcess() >= 0.75D) {
        f = getProcess() * 4.0F - 3.0F;
      } else if (getProcess() < 0.25D) {
        f = 1.0F - getProcess() * 4.0F;
      } else {
        f = 0.0F;
      } 
      int j = (int)(f * 255.0F);
      if (getProcess() > 0.5D) {
        i = this.mOnTextColor;
      } else {
        i = this.mOffTextColor;
      } 
      j = Color.alpha(i) * j / 255;
      layout.getPaint().setARGB(j, Color.red(i), Color.green(i), Color.blue(i));
      paramCanvas.save();
      paramCanvas.translate(rectF.left, rectF.top);
      layout.draw(paramCanvas);
      paramCanvas.restore();
    } 
    this.mPresentThumbRectF.set(this.mThumbRectF);
    this.mPresentThumbRectF.offset(this.mProcess * this.mSafeRectF.width(), 0.0F);
    if (this.mIsThumbUseDrawable) {
      this.mThumbDrawable.setBounds((int)this.mPresentThumbRectF.left, (int)this.mPresentThumbRectF.top, ceil(this.mPresentThumbRectF.right), ceil(this.mPresentThumbRectF.bottom));
      this.mThumbDrawable.draw(paramCanvas);
    } else {
      this.mPaint.setColor(this.mCurrThumbColor);
      paramCanvas.drawRoundRect(this.mPresentThumbRectF, this.mThumbRadius, this.mThumbRadius, this.mPaint);
    } 
    if (this.mDrawDebugRect) {
      RectF rectF1;
      this.mRectPaint.setColor(Color.parseColor("#AA0000"));
      paramCanvas.drawRect(this.mBackRectF, this.mRectPaint);
      this.mRectPaint.setColor(Color.parseColor("#0000FF"));
      paramCanvas.drawRect(this.mPresentThumbRectF, this.mRectPaint);
      this.mRectPaint.setColor(Color.parseColor("#00CC00"));
      if (getProcess() > 0.5D) {
        rectF1 = this.mTextOnRectF;
      } else {
        rectF1 = this.mTextOffRectF;
      } 
      paramCanvas.drawRect(rectF1, this.mRectPaint);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOnLayout == null && this.mTextOn != null)
      this.mOnLayout = makeLayout(this.mTextOn); 
    if (this.mOffLayout == null && this.mTextOff != null)
      this.mOffLayout = makeLayout(this.mTextOff); 
    setMeasuredDimension(measureWidth(paramInt1), measureHeight(paramInt2));
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    setText(savedState.onText, savedState.offText);
    super.onRestoreInstanceState(savedState.getSuperState());
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.onText = this.mTextOn;
    savedState.offText = this.mTextOff;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3 || paramInt2 != paramInt4)
      setup(); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = true;
    if (!isEnabled() || !isClickable())
      bool = false; 
    int i = paramMotionEvent.getAction();
    float f1 = paramMotionEvent.getX();
    float f2 = this.mStartX;
    float f3 = paramMotionEvent.getY();
    float f4 = this.mStartY;
    switch (i) {
      default:
        return bool;
      case 0:
        catchView();
        this.mStartX = paramMotionEvent.getX();
        this.mStartY = paramMotionEvent.getY();
        this.mLastX = this.mStartX;
        setPressed(true);
      case 2:
        f4 = paramMotionEvent.getX();
        setProcess(getProcess() + (f4 - this.mLastX) / this.mSafeRectF.width());
        this.mLastX = f4;
      case 1:
      case 3:
        break;
    } 
    setPressed(false);
    boolean bool1 = getStatusBasedOnPos();
    float f5 = (float)(paramMotionEvent.getEventTime() - paramMotionEvent.getDownTime());
    if (f1 - f2 < this.mTouchSlop && f3 - f4 < this.mTouchSlop && f5 < this.mClickTimeout)
      performClick(); 
    if (bool1 != isChecked()) {
      playSoundEffect(0);
      setChecked(bool1);
    } 
    animateToState(bool1);
  }
  
  public boolean performClick() {
    return super.performClick();
  }
  
  public void setAnimationDuration(long paramLong) {
    this.mAnimationDuration = paramLong;
  }
  
  public void setBackColor(ColorStateList paramColorStateList) {
    this.mBackColor = paramColorStateList;
    if (this.mBackColor != null)
      setBackDrawable((Drawable)null); 
    invalidate();
  }
  
  public void setBackColorRes(int paramInt) {
    setBackColor(ContextCompat.getColorStateList(getContext(), paramInt));
  }
  
  public void setBackDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mBackDrawable = paramDrawable;
    if (this.mBackDrawable != null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mIsBackUseDrawable = bool;
    setup();
    refreshDrawableState();
    requestLayout();
    invalidate();
  }
  
  public void setBackDrawableRes(int paramInt) {
    setBackDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setBackMeasureRatio(float paramFloat) {
    this.mBackMeasureRatio = paramFloat;
    requestLayout();
  }
  
  public void setBackRadius(float paramFloat) {
    this.mBackRadius = paramFloat;
    if (!this.mIsBackUseDrawable)
      invalidate(); 
  }
  
  public void setChecked(boolean paramBoolean) {
    if (isChecked() != paramBoolean)
      animateToState(paramBoolean); 
    super.setChecked(paramBoolean);
  }
  
  public void setCheckedImmediately(boolean paramBoolean) {
    float f;
    super.setChecked(paramBoolean);
    if (this.mProcessAnimator != null && this.mProcessAnimator.isRunning())
      this.mProcessAnimator.cancel(); 
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    setProcess(f);
    invalidate();
  }
  
  public void setCheckedImmediatelyNoEvent(boolean paramBoolean) {
    if (this.mChildOnCheckedChangeListener == null) {
      setCheckedImmediately(paramBoolean);
      return;
    } 
    super.setOnCheckedChangeListener(null);
    setCheckedImmediately(paramBoolean);
    setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
  }
  
  public void setCheckedNoEvent(boolean paramBoolean) {
    if (this.mChildOnCheckedChangeListener == null) {
      setChecked(paramBoolean);
      return;
    } 
    super.setOnCheckedChangeListener(null);
    setChecked(paramBoolean);
    setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
  }
  
  public void setDrawDebugRect(boolean paramBoolean) {
    this.mDrawDebugRect = paramBoolean;
    invalidate();
  }
  
  public void setFadeBack(boolean paramBoolean) {
    this.mFadeBack = paramBoolean;
  }
  
  public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener paramOnCheckedChangeListener) {
    super.setOnCheckedChangeListener(paramOnCheckedChangeListener);
    this.mChildOnCheckedChangeListener = paramOnCheckedChangeListener;
  }
  
  public final void setProcess(float paramFloat) {
    float f = paramFloat;
    if (f > 1.0F) {
      paramFloat = 1.0F;
    } else {
      paramFloat = f;
      if (f < 0.0F)
        paramFloat = 0.0F; 
    } 
    this.mProcess = paramFloat;
    invalidate();
  }
  
  public void setText(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
    this.mTextOn = paramCharSequence1;
    this.mTextOff = paramCharSequence2;
    this.mOnLayout = null;
    this.mOffLayout = null;
    requestLayout();
  }
  
  public void setThumbColor(ColorStateList paramColorStateList) {
    this.mThumbColor = paramColorStateList;
    if (this.mThumbColor != null)
      setThumbDrawable((Drawable)null); 
  }
  
  public void setThumbColorRes(int paramInt) {
    setThumbColor(ContextCompat.getColorStateList(getContext(), paramInt));
  }
  
  public void setThumbDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mThumbDrawable = paramDrawable;
    if (this.mThumbDrawable != null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mIsThumbUseDrawable = bool;
    setup();
    refreshDrawableState();
    requestLayout();
    invalidate();
  }
  
  public void setThumbDrawableRes(int paramInt) {
    setThumbDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setThumbMargin(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.mThumbMargin.set(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    requestLayout();
  }
  
  public void setThumbMargin(RectF paramRectF) {
    if (paramRectF == null) {
      setThumbMargin(0.0F, 0.0F, 0.0F, 0.0F);
      return;
    } 
    setThumbMargin(paramRectF.left, paramRectF.top, paramRectF.right, paramRectF.bottom);
  }
  
  public void setThumbRadius(float paramFloat) {
    this.mThumbRadius = paramFloat;
    if (!this.mIsThumbUseDrawable)
      invalidate(); 
  }
  
  public void setThumbSize(float paramFloat1, float paramFloat2) {
    this.mThumbSizeF.set(paramFloat1, paramFloat2);
    setup();
    requestLayout();
  }
  
  public void setThumbSize(PointF paramPointF) {
    if (paramPointF == null) {
      float f = (getResources().getDisplayMetrics()).density * 20.0F;
      setThumbSize(f, f);
      return;
    } 
    setThumbSize(paramPointF.x, paramPointF.y);
  }
  
  public void setTintColor(int paramInt) {
    this.mTintColor = paramInt;
    this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
    this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
    this.mIsBackUseDrawable = false;
    this.mIsThumbUseDrawable = false;
    refreshDrawableState();
    invalidate();
  }
  
  public void toggleImmediately() {
    boolean bool;
    if (!isChecked()) {
      bool = true;
    } else {
      bool = false;
    } 
    setCheckedImmediately(bool);
  }
  
  public void toggleImmediatelyNoEvent() {
    if (this.mChildOnCheckedChangeListener == null) {
      toggleImmediately();
      return;
    } 
    super.setOnCheckedChangeListener(null);
    toggleImmediately();
    setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
  }
  
  public void toggleNoEvent() {
    if (this.mChildOnCheckedChangeListener == null) {
      toggle();
      return;
    } 
    super.setOnCheckedChangeListener(null);
    toggle();
    setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public SwitchButton.SavedState createFromParcel(Parcel param2Parcel) {
          return new SwitchButton.SavedState(param2Parcel);
        }
        
        public SwitchButton.SavedState[] newArray(int param2Int) {
          return new SwitchButton.SavedState[param2Int];
        }
      };
    
    CharSequence offText;
    
    CharSequence onText;
    
    private SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.onText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
      this.offText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      TextUtils.writeToParcel(this.onText, param1Parcel, param1Int);
      TextUtils.writeToParcel(this.offText, param1Parcel, param1Int);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public SwitchButton.SavedState createFromParcel(Parcel param1Parcel) {
      return new SwitchButton.SavedState(param1Parcel);
    }
    
    public SwitchButton.SavedState[] newArray(int param1Int) {
      return new SwitchButton.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/kyleduo/switchbutton/SwitchButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */