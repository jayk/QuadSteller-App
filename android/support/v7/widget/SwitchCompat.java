package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;

public class SwitchCompat extends CompoundButton {
  private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  private static final int THUMB_ANIMATION_DURATION = 250;
  
  private static final int TOUCH_MODE_DOWN = 1;
  
  private static final int TOUCH_MODE_DRAGGING = 2;
  
  private static final int TOUCH_MODE_IDLE = 0;
  
  private boolean mHasThumbTint = false;
  
  private boolean mHasThumbTintMode = false;
  
  private boolean mHasTrackTint = false;
  
  private boolean mHasTrackTintMode = false;
  
  private int mMinFlingVelocity;
  
  private Layout mOffLayout;
  
  private Layout mOnLayout;
  
  ThumbAnimation mPositionAnimator;
  
  private boolean mShowText;
  
  private boolean mSplitTrack;
  
  private int mSwitchBottom;
  
  private int mSwitchHeight;
  
  private int mSwitchLeft;
  
  private int mSwitchMinWidth;
  
  private int mSwitchPadding;
  
  private int mSwitchRight;
  
  private int mSwitchTop;
  
  private TransformationMethod mSwitchTransformationMethod;
  
  private int mSwitchWidth;
  
  private final Rect mTempRect = new Rect();
  
  private ColorStateList mTextColors;
  
  private CharSequence mTextOff;
  
  private CharSequence mTextOn;
  
  private TextPaint mTextPaint = new TextPaint(1);
  
  private Drawable mThumbDrawable;
  
  private float mThumbPosition;
  
  private int mThumbTextPadding;
  
  private ColorStateList mThumbTintList = null;
  
  private PorterDuff.Mode mThumbTintMode = null;
  
  private int mThumbWidth;
  
  private int mTouchMode;
  
  private int mTouchSlop;
  
  private float mTouchX;
  
  private float mTouchY;
  
  private Drawable mTrackDrawable;
  
  private ColorStateList mTrackTintList = null;
  
  private PorterDuff.Mode mTrackTintMode = null;
  
  private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
  
  public SwitchCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.switchStyle);
  }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Resources resources = getResources();
    this.mTextPaint.density = (resources.getDisplayMetrics()).density;
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SwitchCompat, paramInt, 0);
    this.mThumbDrawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_android_thumb);
    if (this.mThumbDrawable != null)
      this.mThumbDrawable.setCallback((Drawable.Callback)this); 
    this.mTrackDrawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_track);
    if (this.mTrackDrawable != null)
      this.mTrackDrawable.setCallback((Drawable.Callback)this); 
    this.mTextOn = tintTypedArray.getText(R.styleable.SwitchCompat_android_textOn);
    this.mTextOff = tintTypedArray.getText(R.styleable.SwitchCompat_android_textOff);
    this.mShowText = tintTypedArray.getBoolean(R.styleable.SwitchCompat_showText, true);
    this.mThumbTextPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
    this.mSwitchMinWidth = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
    this.mSwitchPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
    this.mSplitTrack = tintTypedArray.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
    ColorStateList colorStateList2 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_thumbTint);
    if (colorStateList2 != null) {
      this.mThumbTintList = colorStateList2;
      this.mHasThumbTint = true;
    } 
    PorterDuff.Mode mode2 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null);
    if (this.mThumbTintMode != mode2) {
      this.mThumbTintMode = mode2;
      this.mHasThumbTintMode = true;
    } 
    if (this.mHasThumbTint || this.mHasThumbTintMode)
      applyThumbTint(); 
    ColorStateList colorStateList1 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_trackTint);
    if (colorStateList1 != null) {
      this.mTrackTintList = colorStateList1;
      this.mHasTrackTint = true;
    } 
    PorterDuff.Mode mode1 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_trackTintMode, -1), null);
    if (this.mTrackTintMode != mode1) {
      this.mTrackTintMode = mode1;
      this.mHasTrackTintMode = true;
    } 
    if (this.mHasTrackTint || this.mHasTrackTintMode)
      applyTrackTint(); 
    paramInt = tintTypedArray.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
    if (paramInt != 0)
      setSwitchTextAppearance(paramContext, paramInt); 
    tintTypedArray.recycle();
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    refreshDrawableState();
    setChecked(isChecked());
  }
  
  private void animateThumbToCheckedState(final boolean newCheckedState) {
    float f2;
    if (this.mPositionAnimator != null)
      cancelPositionAnimator(); 
    float f1 = this.mThumbPosition;
    if (newCheckedState) {
      f2 = 1.0F;
    } else {
      f2 = 0.0F;
    } 
    this.mPositionAnimator = new ThumbAnimation(f1, f2);
    this.mPositionAnimator.setDuration(250L);
    this.mPositionAnimator.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {
            if (SwitchCompat.this.mPositionAnimator == param1Animation) {
              float f;
              SwitchCompat switchCompat = SwitchCompat.this;
              if (newCheckedState) {
                f = 1.0F;
              } else {
                f = 0.0F;
              } 
              switchCompat.setThumbPosition(f);
              SwitchCompat.this.mPositionAnimator = null;
            } 
          }
          
          public void onAnimationRepeat(Animation param1Animation) {}
          
          public void onAnimationStart(Animation param1Animation) {}
        });
    startAnimation(this.mPositionAnimator);
  }
  
  private void applyThumbTint() {
    if (this.mThumbDrawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
      this.mThumbDrawable = this.mThumbDrawable.mutate();
      if (this.mHasThumbTint)
        DrawableCompat.setTintList(this.mThumbDrawable, this.mThumbTintList); 
      if (this.mHasThumbTintMode)
        DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode); 
      if (this.mThumbDrawable.isStateful())
        this.mThumbDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void applyTrackTint() {
    if (this.mTrackDrawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
      this.mTrackDrawable = this.mTrackDrawable.mutate();
      if (this.mHasTrackTint)
        DrawableCompat.setTintList(this.mTrackDrawable, this.mTrackTintList); 
      if (this.mHasTrackTintMode)
        DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode); 
      if (this.mTrackDrawable.isStateful())
        this.mTrackDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void cancelPositionAnimator() {
    if (this.mPositionAnimator != null) {
      clearAnimation();
      this.mPositionAnimator = null;
    } 
  }
  
  private void cancelSuperTouch(MotionEvent paramMotionEvent) {
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.setAction(3);
    super.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 >= paramFloat2) {
      if (paramFloat1 > paramFloat3)
        return paramFloat3; 
      paramFloat2 = paramFloat1;
    } 
    return paramFloat2;
  }
  
  private boolean getTargetCheckedState() {
    return (this.mThumbPosition > 0.5F);
  }
  
  private int getThumbOffset() {
    if (ViewUtils.isLayoutRtl((View)this)) {
      float f1 = 1.0F - this.mThumbPosition;
      return (int)(getThumbScrollRange() * f1 + 0.5F);
    } 
    float f = this.mThumbPosition;
    return (int)(getThumbScrollRange() * f + 0.5F);
  }
  
  private int getThumbScrollRange() {
    if (this.mTrackDrawable != null) {
      Rect rect2;
      Rect rect1 = this.mTempRect;
      this.mTrackDrawable.getPadding(rect1);
      if (this.mThumbDrawable != null) {
        rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
      } else {
        rect2 = DrawableUtils.INSETS_NONE;
      } 
      return this.mSwitchWidth - this.mThumbWidth - rect1.left - rect1.right - rect2.left - rect2.right;
    } 
    return 0;
  }
  
  private boolean hitThumb(float paramFloat1, float paramFloat2) {
    boolean bool1 = false;
    if (this.mThumbDrawable == null)
      return bool1; 
    int i = getThumbOffset();
    this.mThumbDrawable.getPadding(this.mTempRect);
    int j = this.mSwitchTop;
    int k = this.mTouchSlop;
    int m = this.mSwitchLeft + i - this.mTouchSlop;
    int n = this.mThumbWidth;
    i = this.mTempRect.left;
    int i1 = this.mTempRect.right;
    int i2 = this.mTouchSlop;
    int i3 = this.mSwitchBottom;
    int i4 = this.mTouchSlop;
    boolean bool2 = bool1;
    if (paramFloat1 > m) {
      bool2 = bool1;
      if (paramFloat1 < (n + m + i + i1 + i2)) {
        bool2 = bool1;
        if (paramFloat2 > (j - k)) {
          bool2 = bool1;
          if (paramFloat2 < (i3 + i4))
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  private Layout makeLayout(CharSequence paramCharSequence) {
    if (this.mSwitchTransformationMethod != null)
      paramCharSequence = this.mSwitchTransformationMethod.getTransformation(paramCharSequence, (View)this); 
    TextPaint textPaint = this.mTextPaint;
    if (paramCharSequence != null) {
      int i = (int)Math.ceil(Layout.getDesiredWidth(paramCharSequence, this.mTextPaint));
      return (Layout)new StaticLayout(paramCharSequence, textPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
    } 
    boolean bool = false;
    return (Layout)new StaticLayout(paramCharSequence, textPaint, bool, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
  }
  
  private void setSwitchTypefaceByIndex(int paramInt1, int paramInt2) {
    Typeface typeface = null;
    switch (paramInt1) {
      default:
        setSwitchTypeface(typeface, paramInt2);
        return;
      case 1:
        typeface = Typeface.SANS_SERIF;
      case 2:
        typeface = Typeface.SERIF;
      case 3:
        break;
    } 
    typeface = Typeface.MONOSPACE;
  }
  
  private void stopDrag(MotionEvent paramMotionEvent) {
    boolean bool;
    boolean bool2;
    this.mTouchMode = 0;
    if (paramMotionEvent.getAction() == 1 && isEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    boolean bool1 = isChecked();
    if (bool) {
      this.mVelocityTracker.computeCurrentVelocity(1000);
      float f = this.mVelocityTracker.getXVelocity();
      if (Math.abs(f) > this.mMinFlingVelocity) {
        if (ViewUtils.isLayoutRtl((View)this)) {
          if (f < 0.0F) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
        } else if (f > 0.0F) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
      } else {
        bool2 = getTargetCheckedState();
      } 
    } else {
      bool2 = bool1;
    } 
    if (bool2 != bool1)
      playSoundEffect(0); 
    setChecked(bool2);
    cancelSuperTouch(paramMotionEvent);
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect2;
    Rect rect1 = this.mTempRect;
    int i = this.mSwitchLeft;
    int j = this.mSwitchTop;
    int k = this.mSwitchRight;
    int m = this.mSwitchBottom;
    int n = i + getThumbOffset();
    if (this.mThumbDrawable != null) {
      rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
    } else {
      rect2 = DrawableUtils.INSETS_NONE;
    } 
    int i1 = n;
    if (this.mTrackDrawable != null) {
      this.mTrackDrawable.getPadding(rect1);
      int i2 = n + rect1.left;
      n = j;
      int i3 = m;
      int i4 = i3;
      int i5 = i;
      int i6 = k;
      int i7 = n;
      if (rect2 != null) {
        i1 = i;
        if (rect2.left > rect1.left)
          i1 = i + rect2.left - rect1.left; 
        i = n;
        if (rect2.top > rect1.top)
          i = n + rect2.top - rect1.top; 
        n = k;
        if (rect2.right > rect1.right)
          n = k - rect2.right - rect1.right; 
        i4 = i3;
        i5 = i1;
        i6 = n;
        i7 = i;
        if (rect2.bottom > rect1.bottom) {
          i4 = i3 - rect2.bottom - rect1.bottom;
          i7 = i;
          i6 = n;
          i5 = i1;
        } 
      } 
      this.mTrackDrawable.setBounds(i5, i7, i6, i4);
      i1 = i2;
    } 
    if (this.mThumbDrawable != null) {
      this.mThumbDrawable.getPadding(rect1);
      k = i1 - rect1.left;
      i1 = this.mThumbWidth + i1 + rect1.right;
      this.mThumbDrawable.setBounds(k, j, i1, m);
      Drawable drawable = getBackground();
      if (drawable != null)
        DrawableCompat.setHotspotBounds(drawable, k, j, i1, m); 
    } 
    super.draw(paramCanvas);
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21)
      super.drawableHotspotChanged(paramFloat1, paramFloat2); 
    if (this.mThumbDrawable != null)
      DrawableCompat.setHotspot(this.mThumbDrawable, paramFloat1, paramFloat2); 
    if (this.mTrackDrawable != null)
      DrawableCompat.setHotspot(this.mTrackDrawable, paramFloat1, paramFloat2); 
  }
  
  protected void drawableStateChanged() {
    boolean bool;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int i = 0;
    Drawable drawable = this.mThumbDrawable;
    int j = i;
    if (drawable != null) {
      j = i;
      if (drawable.isStateful())
        j = false | drawable.setState(arrayOfInt); 
    } 
    drawable = this.mTrackDrawable;
    i = j;
    if (drawable != null) {
      i = j;
      if (drawable.isStateful())
        bool = j | drawable.setState(arrayOfInt); 
    } 
    if (bool)
      invalidate(); 
  }
  
  public int getCompoundPaddingLeft() {
    if (!ViewUtils.isLayoutRtl((View)this))
      return super.getCompoundPaddingLeft(); 
    int j = super.getCompoundPaddingLeft() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public int getCompoundPaddingRight() {
    if (ViewUtils.isLayoutRtl((View)this))
      return super.getCompoundPaddingRight(); 
    int j = super.getCompoundPaddingRight() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public boolean getShowText() {
    return this.mShowText;
  }
  
  public boolean getSplitTrack() {
    return this.mSplitTrack;
  }
  
  public int getSwitchMinWidth() {
    return this.mSwitchMinWidth;
  }
  
  public int getSwitchPadding() {
    return this.mSwitchPadding;
  }
  
  public CharSequence getTextOff() {
    return this.mTextOff;
  }
  
  public CharSequence getTextOn() {
    return this.mTextOn;
  }
  
  public Drawable getThumbDrawable() {
    return this.mThumbDrawable;
  }
  
  public int getThumbTextPadding() {
    return this.mThumbTextPadding;
  }
  
  @Nullable
  public ColorStateList getThumbTintList() {
    return this.mThumbTintList;
  }
  
  @Nullable
  public PorterDuff.Mode getThumbTintMode() {
    return this.mThumbTintMode;
  }
  
  public Drawable getTrackDrawable() {
    return this.mTrackDrawable;
  }
  
  @Nullable
  public ColorStateList getTrackTintList() {
    return this.mTrackTintList;
  }
  
  @Nullable
  public PorterDuff.Mode getTrackTintMode() {
    return this.mTrackTintMode;
  }
  
  public void jumpDrawablesToCurrentState() {
    if (Build.VERSION.SDK_INT >= 11) {
      float f;
      super.jumpDrawablesToCurrentState();
      if (this.mThumbDrawable != null)
        this.mThumbDrawable.jumpToCurrentState(); 
      if (this.mTrackDrawable != null)
        this.mTrackDrawable.jumpToCurrentState(); 
      cancelPositionAnimator();
      if (isChecked()) {
        f = 1.0F;
      } else {
        f = 0.0F;
      } 
      setThumbPosition(f);
    } 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    Layout layout;
    super.onDraw(paramCanvas);
    Rect rect = this.mTempRect;
    Drawable drawable1 = this.mTrackDrawable;
    if (drawable1 != null) {
      drawable1.getPadding(rect);
    } else {
      rect.setEmpty();
    } 
    int i = this.mSwitchTop;
    int j = this.mSwitchBottom;
    int k = rect.top;
    int m = rect.bottom;
    Drawable drawable2 = this.mThumbDrawable;
    if (drawable1 != null)
      if (this.mSplitTrack && drawable2 != null) {
        Rect rect1 = DrawableUtils.getOpticalBounds(drawable2);
        drawable2.copyBounds(rect);
        rect.left += rect1.left;
        rect.right -= rect1.right;
        int i1 = paramCanvas.save();
        paramCanvas.clipRect(rect, Region.Op.DIFFERENCE);
        drawable1.draw(paramCanvas);
        paramCanvas.restoreToCount(i1);
      } else {
        drawable1.draw(paramCanvas);
      }  
    int n = paramCanvas.save();
    if (drawable2 != null)
      drawable2.draw(paramCanvas); 
    if (getTargetCheckedState()) {
      layout = this.mOnLayout;
    } else {
      layout = this.mOffLayout;
    } 
    if (layout != null) {
      int[] arrayOfInt = getDrawableState();
      if (this.mTextColors != null)
        this.mTextPaint.setColor(this.mTextColors.getColorForState(arrayOfInt, 0)); 
      this.mTextPaint.drawableState = arrayOfInt;
      if (drawable2 != null) {
        Rect rect1 = drawable2.getBounds();
        i1 = rect1.left + rect1.right;
      } else {
        i1 = getWidth();
      } 
      int i2 = i1 / 2;
      int i1 = layout.getWidth() / 2;
      j = (i + k + j - m) / 2;
      k = layout.getHeight() / 2;
      paramCanvas.translate((i2 - i1), (j - k));
      layout.draw(paramCanvas);
    } 
    paramCanvas.restoreToCount(n);
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("android.widget.Switch");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    CharSequence charSequence1;
    CharSequence charSequence2;
    if (Build.VERSION.SDK_INT >= 14) {
      super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
      paramAccessibilityNodeInfo.setClassName("android.widget.Switch");
      if (isChecked()) {
        charSequence1 = this.mTextOn;
      } else {
        charSequence1 = this.mTextOff;
      } 
      if (!TextUtils.isEmpty(charSequence1)) {
        charSequence2 = paramAccessibilityNodeInfo.getText();
        if (TextUtils.isEmpty(charSequence2)) {
          paramAccessibilityNodeInfo.setText(charSequence1);
          return;
        } 
      } else {
        return;
      } 
    } else {
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(charSequence2).append(' ').append(charSequence1);
    paramAccessibilityNodeInfo.setText(stringBuilder);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    paramInt2 = 0;
    if (this.mThumbDrawable != null) {
      Rect rect1 = this.mTempRect;
      if (this.mTrackDrawable != null) {
        this.mTrackDrawable.getPadding(rect1);
      } else {
        rect1.setEmpty();
      } 
      Rect rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
      paramInt1 = Math.max(0, rect2.left - rect1.left);
      paramInt2 = Math.max(0, rect2.right - rect1.right);
    } 
    if (ViewUtils.isLayoutRtl((View)this)) {
      paramInt4 = getPaddingLeft() + paramInt1;
      paramInt3 = this.mSwitchWidth + paramInt4 - paramInt1 - paramInt2;
    } else {
      paramInt3 = getWidth() - getPaddingRight() - paramInt2;
      paramInt4 = paramInt3 - this.mSwitchWidth + paramInt1 + paramInt2;
    } 
    switch (getGravity() & 0x70) {
      default:
        paramInt2 = getPaddingTop();
        paramInt1 = paramInt2 + this.mSwitchHeight;
        this.mSwitchLeft = paramInt4;
        this.mSwitchTop = paramInt2;
        this.mSwitchBottom = paramInt1;
        this.mSwitchRight = paramInt3;
        return;
      case 16:
        paramInt2 = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 - this.mSwitchHeight / 2;
        paramInt1 = paramInt2 + this.mSwitchHeight;
        this.mSwitchLeft = paramInt4;
        this.mSwitchTop = paramInt2;
        this.mSwitchBottom = paramInt1;
        this.mSwitchRight = paramInt3;
        return;
      case 80:
        break;
    } 
    paramInt1 = getHeight() - getPaddingBottom();
    paramInt2 = paramInt1 - this.mSwitchHeight;
    this.mSwitchLeft = paramInt4;
    this.mSwitchTop = paramInt2;
    this.mSwitchBottom = paramInt1;
    this.mSwitchRight = paramInt3;
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    int i;
    if (this.mShowText) {
      if (this.mOnLayout == null)
        this.mOnLayout = makeLayout(this.mTextOn); 
      if (this.mOffLayout == null)
        this.mOffLayout = makeLayout(this.mTextOff); 
    } 
    Rect rect = this.mTempRect;
    if (this.mThumbDrawable != null) {
      this.mThumbDrawable.getPadding(rect);
      i = this.mThumbDrawable.getIntrinsicWidth() - rect.left - rect.right;
      j = this.mThumbDrawable.getIntrinsicHeight();
    } else {
      i = 0;
      j = 0;
    } 
    if (this.mShowText) {
      k = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
    } else {
      k = 0;
    } 
    this.mThumbWidth = Math.max(k, i);
    if (this.mTrackDrawable != null) {
      this.mTrackDrawable.getPadding(rect);
      i = this.mTrackDrawable.getIntrinsicHeight();
    } else {
      rect.setEmpty();
      i = 0;
    } 
    int m = rect.left;
    int n = rect.right;
    int i1 = m;
    int k = n;
    if (this.mThumbDrawable != null) {
      rect = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
      i1 = Math.max(m, rect.left);
      k = Math.max(n, rect.right);
    } 
    k = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + i1 + k);
    int j = Math.max(i, j);
    this.mSwitchWidth = k;
    this.mSwitchHeight = j;
    super.onMeasure(paramInt1, paramInt2);
    if (getMeasuredHeight() < j)
      setMeasuredDimension(ViewCompat.getMeasuredWidthAndState((View)this), j); 
  }
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    CharSequence charSequence;
    super.onPopulateAccessibilityEvent(paramAccessibilityEvent);
    if (isChecked()) {
      charSequence = this.mTextOn;
    } else {
      charSequence = this.mTextOff;
    } 
    if (charSequence != null)
      paramAccessibilityEvent.getText().add(charSequence); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    float f3;
    int i;
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (MotionEventCompat.getActionMasked(paramMotionEvent)) {
      default:
      
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        if (isEnabled() && hitThumb(f1, f2)) {
          this.mTouchMode = 1;
          this.mTouchX = f1;
          this.mTouchY = f2;
        } 
      case 2:
        switch (this.mTouchMode) {
          case 0:
          case 0:
            return super.onTouchEvent(paramMotionEvent);
          case 1:
            f2 = paramMotionEvent.getX();
            f1 = paramMotionEvent.getY();
            if (Math.abs(f2 - this.mTouchX) > this.mTouchSlop || Math.abs(f1 - this.mTouchY) > this.mTouchSlop) {
              this.mTouchMode = 2;
              getParent().requestDisallowInterceptTouchEvent(true);
              this.mTouchX = f2;
              this.mTouchY = f1;
              return true;
            } 
          case 2:
            break;
        } 
        f3 = paramMotionEvent.getX();
        i = getThumbScrollRange();
        f2 = f3 - this.mTouchX;
        if (i != 0) {
          f2 /= i;
        } else if (f2 > 0.0F) {
          f2 = 1.0F;
        } else {
          f2 = -1.0F;
        } 
        f1 = f2;
        if (ViewUtils.isLayoutRtl((View)this))
          f1 = -f2; 
        f2 = constrain(this.mThumbPosition + f1, 0.0F, 1.0F);
        if (f2 != this.mThumbPosition) {
          this.mTouchX = f3;
          setThumbPosition(f2);
        } 
        return true;
      case 1:
      case 3:
        break;
    } 
    if (this.mTouchMode == 2) {
      stopDrag(paramMotionEvent);
      super.onTouchEvent(paramMotionEvent);
      return true;
    } 
    this.mTouchMode = 0;
    this.mVelocityTracker.clear();
  }
  
  public void setChecked(boolean paramBoolean) {
    float f;
    super.setChecked(paramBoolean);
    paramBoolean = isChecked();
    if (getWindowToken() != null && ViewCompat.isLaidOut((View)this) && isShown()) {
      animateThumbToCheckedState(paramBoolean);
      return;
    } 
    cancelPositionAnimator();
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    setThumbPosition(f);
  }
  
  public void setShowText(boolean paramBoolean) {
    if (this.mShowText != paramBoolean) {
      this.mShowText = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setSplitTrack(boolean paramBoolean) {
    this.mSplitTrack = paramBoolean;
    invalidate();
  }
  
  public void setSwitchMinWidth(int paramInt) {
    this.mSwitchMinWidth = paramInt;
    requestLayout();
  }
  
  public void setSwitchPadding(int paramInt) {
    this.mSwitchPadding = paramInt;
    requestLayout();
  }
  
  public void setSwitchTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
    if (colorStateList != null) {
      this.mTextColors = colorStateList;
    } else {
      this.mTextColors = getTextColors();
    } 
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
    if (paramInt != 0 && paramInt != this.mTextPaint.getTextSize()) {
      this.mTextPaint.setTextSize(paramInt);
      requestLayout();
    } 
    setSwitchTypefaceByIndex(tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, -1), tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, -1));
    if (tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)) {
      this.mSwitchTransformationMethod = (TransformationMethod)new AllCapsTransformationMethod(getContext());
    } else {
      this.mSwitchTransformationMethod = null;
    } 
    tintTypedArray.recycle();
  }
  
  public void setSwitchTypeface(Typeface paramTypeface) {
    if (this.mTextPaint.getTypeface() != paramTypeface) {
      this.mTextPaint.setTypeface(paramTypeface);
      requestLayout();
      invalidate();
    } 
  }
  
  public void setSwitchTypeface(Typeface paramTypeface, int paramInt) {
    TextPaint textPaint;
    boolean bool = false;
    if (paramInt > 0) {
      boolean bool1;
      float f;
      if (paramTypeface == null) {
        paramTypeface = Typeface.defaultFromStyle(paramInt);
      } else {
        paramTypeface = Typeface.create(paramTypeface, paramInt);
      } 
      setSwitchTypeface(paramTypeface);
      if (paramTypeface != null) {
        bool1 = paramTypeface.getStyle();
      } else {
        bool1 = false;
      } 
      paramInt &= bool1 ^ 0xFFFFFFFF;
      textPaint = this.mTextPaint;
      if ((paramInt & 0x1) != 0)
        bool = true; 
      textPaint.setFakeBoldText(bool);
      textPaint = this.mTextPaint;
      if ((paramInt & 0x2) != 0) {
        f = -0.25F;
      } else {
        f = 0.0F;
      } 
      textPaint.setTextSkewX(f);
      return;
    } 
    this.mTextPaint.setFakeBoldText(false);
    this.mTextPaint.setTextSkewX(0.0F);
    setSwitchTypeface((Typeface)textPaint);
  }
  
  public void setTextOff(CharSequence paramCharSequence) {
    this.mTextOff = paramCharSequence;
    requestLayout();
  }
  
  public void setTextOn(CharSequence paramCharSequence) {
    this.mTextOn = paramCharSequence;
    requestLayout();
  }
  
  public void setThumbDrawable(Drawable paramDrawable) {
    if (this.mThumbDrawable != null)
      this.mThumbDrawable.setCallback(null); 
    this.mThumbDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback((Drawable.Callback)this); 
    requestLayout();
  }
  
  void setThumbPosition(float paramFloat) {
    this.mThumbPosition = paramFloat;
    invalidate();
  }
  
  public void setThumbResource(int paramInt) {
    setThumbDrawable(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setThumbTextPadding(int paramInt) {
    this.mThumbTextPadding = paramInt;
    requestLayout();
  }
  
  public void setThumbTintList(@Nullable ColorStateList paramColorStateList) {
    this.mThumbTintList = paramColorStateList;
    this.mHasThumbTint = true;
    applyThumbTint();
  }
  
  public void setThumbTintMode(@Nullable PorterDuff.Mode paramMode) {
    this.mThumbTintMode = paramMode;
    this.mHasThumbTintMode = true;
    applyThumbTint();
  }
  
  public void setTrackDrawable(Drawable paramDrawable) {
    if (this.mTrackDrawable != null)
      this.mTrackDrawable.setCallback(null); 
    this.mTrackDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback((Drawable.Callback)this); 
    requestLayout();
  }
  
  public void setTrackResource(int paramInt) {
    setTrackDrawable(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setTrackTintList(@Nullable ColorStateList paramColorStateList) {
    this.mTrackTintList = paramColorStateList;
    this.mHasTrackTint = true;
    applyTrackTint();
  }
  
  public void setTrackTintMode(@Nullable PorterDuff.Mode paramMode) {
    this.mTrackTintMode = paramMode;
    this.mHasTrackTintMode = true;
    applyTrackTint();
  }
  
  public void toggle() {
    boolean bool;
    if (!isChecked()) {
      bool = true;
    } else {
      bool = false;
    } 
    setChecked(bool);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mThumbDrawable || paramDrawable == this.mTrackDrawable);
  }
  
  private class ThumbAnimation extends Animation {
    final float mDiff;
    
    final float mEndPosition;
    
    final float mStartPosition;
    
    ThumbAnimation(float param1Float1, float param1Float2) {
      this.mStartPosition = param1Float1;
      this.mEndPosition = param1Float2;
      this.mDiff = param1Float2 - param1Float1;
    }
    
    protected void applyTransformation(float param1Float, Transformation param1Transformation) {
      SwitchCompat.this.setThumbPosition(this.mStartPosition + this.mDiff * param1Float);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/SwitchCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */