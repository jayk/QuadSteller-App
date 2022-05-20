package app.lib.lockview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import app.gamer.quadstellar.R;
import java.util.ArrayList;

public class GlowPadView extends View {
  private static final boolean DEBUG = false;
  
  private static final int HIDE_ANIMATION_DELAY = 200;
  
  private static final int HIDE_ANIMATION_DURATION = 200;
  
  private static final int INITIAL_SHOW_HANDLE_DURATION = 200;
  
  private static final int RETURN_TO_HOME_DELAY = 1200;
  
  private static final int RETURN_TO_HOME_DURATION = 200;
  
  private static final int REVEAL_GLOW_DELAY = 0;
  
  private static final int REVEAL_GLOW_DURATION = 0;
  
  private static final float RING_SCALE_COLLAPSED = 0.5F;
  
  private static final float RING_SCALE_EXPANDED = 1.0F;
  
  private static final int SHOW_ANIMATION_DELAY = 50;
  
  private static final int SHOW_ANIMATION_DURATION = 200;
  
  private static final float SNAP_MARGIN_DEFAULT = 20.0F;
  
  private static final int STATE_FINISH = 5;
  
  private static final int STATE_FIRST_TOUCH = 2;
  
  private static final int STATE_IDLE = 0;
  
  private static final int STATE_SNAP = 4;
  
  private static final int STATE_START = 1;
  
  private static final int STATE_TRACKING = 3;
  
  private static final String TAG = "GlowPadView";
  
  private static final float TAP_RADIUS_SCALE_ACCESSIBILITY_ENABLED = 1.3F;
  
  private static final float TARGET_SCALE_COLLAPSED = 0.8F;
  
  private static final float TARGET_SCALE_EXPANDED = 1.0F;
  
  private static final int WAVE_ANIMATION_DURATION = 1350;
  
  Animation animation;
  
  private int mActiveTarget;
  
  private boolean mAlwaysTrackFinger;
  
  private boolean mAnimatingTargets;
  
  private Tweener mBackgroundAnimator;
  
  private ArrayList<String> mDirectionDescriptions;
  
  private int mDirectionDescriptionsResourceId;
  
  private boolean mDragging;
  
  private int mFeedbackCount;
  
  private AnimationBundle mGlowAnimations;
  
  private float mGlowRadius;
  
  private int mGrabbedState;
  
  private int mGravity;
  
  public TargetDrawable mHandleDrawable;
  
  private int mHorizontalInset;
  
  private boolean mInitialLayout;
  
  private float mInnerRadius;
  
  private int mMaxTargetHeight;
  
  private int mMaxTargetWidth;
  
  private int mNewTargetResources;
  
  private OnTriggerListener mOnTriggerListener;
  
  private float mOuterRadius;
  
  private TargetDrawable mOuterRing;
  
  private PointCloud mPointCloud;
  
  private int mPointerId;
  
  private Animator.AnimatorListener mResetListener;
  
  private Animator.AnimatorListener mResetListenerWithPing;
  
  private boolean mShowTargetsOnIdle;
  
  private float mSnapMargin;
  
  private AnimationBundle mTargetAnimations;
  
  private ArrayList<String> mTargetDescriptions;
  
  private int mTargetDescriptionsResourceId;
  
  private ArrayList<TargetDrawable> mTargetDrawables;
  
  private int mTargetResourceId;
  
  private Animator.AnimatorListener mTargetUpdateListener;
  
  private ValueAnimator.AnimatorUpdateListener mUpdateListener;
  
  private int mVerticalInset;
  
  private int mVibrationDuration;
  
  private Vibrator mVibrator;
  
  private AnimationBundle mWaveAnimations;
  
  private float mWaveCenterX;
  
  private float mWaveCenterY;
  
  public GlowPadView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public GlowPadView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Drawable drawable;
    this.mTargetDrawables = new ArrayList<TargetDrawable>();
    this.mWaveAnimations = new AnimationBundle();
    this.mTargetAnimations = new AnimationBundle();
    this.mGlowAnimations = new AnimationBundle();
    this.mFeedbackCount = 3;
    this.mVibrationDuration = 20;
    this.mActiveTarget = -1;
    this.mOuterRadius = 0.0F;
    this.mSnapMargin = 0.0F;
    this.mResetListener = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator param1Animator) {
          GlowPadView.this.switchToState(0, GlowPadView.this.mWaveCenterX, GlowPadView.this.mWaveCenterY);
          GlowPadView.this.dispatchOnFinishFinalAnimation();
        }
      };
    this.mResetListenerWithPing = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator param1Animator) {
          GlowPadView.this.ping();
          GlowPadView.this.switchToState(0, GlowPadView.this.mWaveCenterX, GlowPadView.this.mWaveCenterY);
          GlowPadView.this.dispatchOnFinishFinalAnimation();
        }
      };
    this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
          GlowPadView.this.invalidate();
        }
      };
    this.mTargetUpdateListener = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator param1Animator) {
          if (GlowPadView.this.mNewTargetResources != 0) {
            GlowPadView.this.internalSetTargetResources(GlowPadView.this.mNewTargetResources);
            GlowPadView.access$502(GlowPadView.this, 0);
            GlowPadView.this.hideTargets(false, false);
          } 
          GlowPadView.access$802(GlowPadView.this, false);
        }
      };
    this.mGravity = 17;
    this.mInitialLayout = true;
    Resources resources = paramContext.getResources();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.GlowPadView);
    this.mInnerRadius = typedArray.getDimension(7, this.mInnerRadius);
    this.mOuterRadius = typedArray.getDimension(8, this.mOuterRadius);
    this.mSnapMargin = typedArray.getDimension(11, this.mSnapMargin);
    this.mVibrationDuration = typedArray.getInt(10, this.mVibrationDuration);
    this.mFeedbackCount = typedArray.getInt(12, this.mFeedbackCount);
    TypedValue typedValue1 = typedArray.peekValue(4);
    if (typedValue1 != null) {
      i = typedValue1.resourceId;
    } else {
      i = 0;
    } 
    this.mHandleDrawable = new TargetDrawable(resources, i, 2);
    this.mHandleDrawable.setState(TargetDrawable.STATE_INACTIVE);
    this.mOuterRing = new TargetDrawable(resources, getResourceId(typedArray, 5), 1);
    this.mAlwaysTrackFinger = typedArray.getBoolean(13, false);
    int i = getResourceId(typedArray, 6);
    typedValue1 = typedValue2;
    if (i != 0)
      drawable = resources.getDrawable(i); 
    this.mGlowRadius = typedArray.getDimension(9, 0.0F);
    typedValue2 = new TypedValue();
    if (typedArray.getValue(1, typedValue2))
      internalSetTargetResources(typedValue2.resourceId); 
    if (this.mTargetDrawables == null || this.mTargetDrawables.size() == 0)
      throw new IllegalStateException("Must specify at least one target drawable"); 
    if (typedArray.getValue(2, typedValue2)) {
      i = typedValue2.resourceId;
      if (i == 0)
        throw new IllegalStateException("Must specify target descriptions"); 
      setTargetDescriptionsResourceId(i);
    } 
    if (typedArray.getValue(3, typedValue2)) {
      i = typedValue2.resourceId;
      if (i == 0)
        throw new IllegalStateException("Must specify direction descriptions"); 
      setDirectionDescriptionsResourceId(i);
    } 
    this.mGravity = typedArray.getInt(0, 48);
    typedArray.recycle();
    if (this.mVibrationDuration > 0)
      bool = true; 
    setVibrateEnabled(bool);
    assignDefaultsIfNeeded();
    this.mPointCloud = new PointCloud(drawable);
    this.mPointCloud.makePointCloud(this.mInnerRadius, this.mInnerRadius);
    this.mPointCloud.glowManager.setRadius(this.mGlowRadius);
  }
  
  @TargetApi(16)
  private void announceTargets() {
    StringBuilder stringBuilder = new StringBuilder();
    int i = this.mTargetDrawables.size();
    for (byte b = 0; b < i; b++) {
      String str1 = getTargetDescription(b);
      String str2 = getDirectionDescription(b);
      if (!TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2))
        stringBuilder.append(String.format(str2, new Object[] { str1 })); 
    } 
    if (stringBuilder.length() > 0 && Build.VERSION.SDK_INT >= 16)
      announceForAccessibility(stringBuilder.toString()); 
  }
  
  private void assignDefaultsIfNeeded() {
    if (this.mOuterRadius == 0.0F)
      this.mOuterRadius = Math.max(this.mOuterRing.getWidth(), this.mOuterRing.getHeight()) / 2.0F; 
    if (this.mSnapMargin == 0.0F)
      this.mSnapMargin = TypedValue.applyDimension(1, 20.0F, getContext().getResources().getDisplayMetrics()); 
    if (this.mInnerRadius == 0.0F)
      this.mInnerRadius = this.mHandleDrawable.getWidth() / 10.0F; 
  }
  
  @TargetApi(17)
  private void computeInsets(int paramInt1, int paramInt2) {
    int i = getLayoutDirection();
    i = Gravity.getAbsoluteGravity(this.mGravity, i);
    switch (i & 0x7) {
      default:
        this.mHorizontalInset = paramInt1 / 2;
        switch (i & 0x70) {
          default:
            this.mVerticalInset = paramInt2 / 2;
            return;
          case 48:
            this.mVerticalInset = 0;
            return;
          case 80:
            break;
        } 
        break;
      case 3:
        this.mHorizontalInset = 0;
        switch (i & 0x70) {
          default:
            this.mVerticalInset = paramInt2 / 2;
            return;
          case 48:
            this.mVerticalInset = 0;
            return;
          case 80:
            break;
        } 
        break;
      case 5:
        this.mHorizontalInset = paramInt1;
        switch (i & 0x70) {
          default:
            this.mVerticalInset = paramInt2 / 2;
            return;
          case 48:
            this.mVerticalInset = 0;
            return;
          case 80:
            break;
        } 
        break;
    } 
    this.mVerticalInset = paramInt2;
  }
  
  private void deactivateTargets() {
    int i = this.mTargetDrawables.size();
    for (byte b = 0; b < i; b++)
      ((TargetDrawable)this.mTargetDrawables.get(b)).setState(TargetDrawable.STATE_INACTIVE); 
    this.mActiveTarget = -1;
  }
  
  private void dispatchOnFinishFinalAnimation() {
    if (this.mOnTriggerListener != null)
      this.mOnTriggerListener.onFinishFinalAnimation(); 
  }
  
  private void dispatchTriggerEvent(int paramInt) {
    vibrate();
    if (this.mOnTriggerListener != null)
      this.mOnTriggerListener.onTrigger(this, paramInt); 
  }
  
  private float dist2(float paramFloat1, float paramFloat2) {
    return paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2;
  }
  
  private void doFinish() {
    boolean bool;
    int i = this.mActiveTarget;
    if (i != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      highlightSelected(i);
      hideGlow(200, 1200, 0.0F, this.mResetListener);
      dispatchTriggerEvent(i);
      if (!this.mAlwaysTrackFinger)
        this.mTargetAnimations.stop(); 
    } else {
      hideGlow(200, 0, 0.0F, this.mResetListenerWithPing);
      if (!this.mShowTargetsOnIdle)
        hideTargets(true, false); 
    } 
    setGrabbedState(0);
  }
  
  private void dump() {
    Log.v("GlowPadView", "Outer Radius = " + this.mOuterRadius);
    Log.v("GlowPadView", "SnapMargin = " + this.mSnapMargin);
    Log.v("GlowPadView", "FeedbackCount = " + this.mFeedbackCount);
    Log.v("GlowPadView", "VibrationDuration = " + this.mVibrationDuration);
    Log.v("GlowPadView", "GlowRadius = " + this.mGlowRadius);
    Log.v("GlowPadView", "WaveCenterX = " + this.mWaveCenterX);
    Log.v("GlowPadView", "WaveCenterY = " + this.mWaveCenterY);
  }
  
  private String getDirectionDescription(int paramInt) {
    if (this.mDirectionDescriptions == null || this.mDirectionDescriptions.isEmpty()) {
      this.mDirectionDescriptions = loadDescriptions(this.mDirectionDescriptionsResourceId);
      if (this.mTargetDrawables.size() != this.mDirectionDescriptions.size()) {
        Log.w("GlowPadView", "The number of target drawables must be equal to the number of direction descriptions.");
        return null;
      } 
    } 
    return this.mDirectionDescriptions.get(paramInt);
  }
  
  private int getResourceId(TypedArray paramTypedArray, int paramInt) {
    TypedValue typedValue = paramTypedArray.peekValue(paramInt);
    return (typedValue == null) ? 0 : typedValue.resourceId;
  }
  
  private float getScaledGlowRadiusSquared() {
    if (((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled()) {
      float f1 = 1.3F * this.mGlowRadius;
      return square(f1);
    } 
    float f = this.mGlowRadius;
    return square(f);
  }
  
  private String getTargetDescription(int paramInt) {
    if (this.mTargetDescriptions == null || this.mTargetDescriptions.isEmpty()) {
      this.mTargetDescriptions = loadDescriptions(this.mTargetDescriptionsResourceId);
      if (this.mTargetDrawables.size() != this.mTargetDescriptions.size()) {
        Log.w("GlowPadView", "The number of target drawables must be equal to the number of target descriptions.");
        return null;
      } 
    } 
    return this.mTargetDescriptions.get(paramInt);
  }
  
  private void handleCancel(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.findPointerIndex(this.mPointerId);
    int j = i;
    if (i == -1)
      j = 0; 
    switchToState(5, paramMotionEvent.getX(j), paramMotionEvent.getY(j));
  }
  
  private void handleDown(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    float f1 = paramMotionEvent.getX(i);
    float f2 = paramMotionEvent.getY(i);
    switchToState(1, f1, f2);
    if (!trySwitchToFirstTouchState(f1, f2)) {
      this.mDragging = false;
      return;
    } 
    this.mPointerId = paramMotionEvent.getPointerId(i);
    updateGlowPosition(f1, f2);
  }
  
  private void handleMove(MotionEvent paramMotionEvent) {
    byte b = -1;
    int i = paramMotionEvent.getHistorySize();
    ArrayList<TargetDrawable> arrayList = this.mTargetDrawables;
    int j = arrayList.size();
    float f1 = 0.0F;
    float f2 = 0.0F;
    int k = paramMotionEvent.findPointerIndex(this.mPointerId);
    if (k != -1) {
      byte b1 = 0;
      while (b1 < i + 1) {
        if (b1 < i) {
          f2 = paramMotionEvent.getHistoricalX(k, b1);
        } else {
          f2 = paramMotionEvent.getX(k);
        } 
        if (b1 < i) {
          f1 = paramMotionEvent.getHistoricalY(k, b1);
        } else {
          f1 = paramMotionEvent.getY(k);
        } 
        float f3 = f2 - this.mWaveCenterX;
        float f4 = f1 - this.mWaveCenterY;
        float f5 = (float)Math.sqrt(dist2(f3, f4));
        if (f5 > this.mOuterRadius) {
          f5 = this.mOuterRadius / f5;
        } else {
          f5 = 1.0F;
        } 
        double d = Math.atan2(-f4, f3);
        if (!this.mDragging)
          trySwitchToFirstTouchState(f2, f1); 
        byte b2 = b;
        if (this.mDragging) {
          f2 = this.mOuterRadius - this.mSnapMargin;
          byte b3 = 0;
          while (true) {
            b2 = b;
            if (b3 < j) {
              TargetDrawable targetDrawable = arrayList.get(b3);
              double d1 = (b3 - 0.5D) * 2.0D * Math.PI / j;
              double d2 = (b3 + 0.5D) * 2.0D * Math.PI / j;
              b2 = b;
              if (targetDrawable.isEnabled()) {
                boolean bool;
                if ((d > d1 && d <= d2) || (6.283185307179586D + d > d1 && 6.283185307179586D + d <= d2)) {
                  bool = true;
                } else {
                  bool = false;
                } 
                b2 = b;
                if (bool) {
                  b2 = b;
                  if (dist2(f3, f4) > f2 * f2)
                    b2 = b3; 
                } 
              } 
              b3++;
              b = b2;
              continue;
            } 
            break;
          } 
        } 
        f1 = f3 * f5;
        f2 = f4 * f5;
        b1++;
        b = b2;
      } 
      if (this.mDragging) {
        if (b != -1) {
          switchToState(4, f1, f2);
          updateGlowPosition(f1, f2);
        } else {
          switchToState(3, f1, f2);
          updateGlowPosition(f1, f2);
        } 
        if (this.mActiveTarget != b) {
          if (this.mActiveTarget != -1)
            ((TargetDrawable)arrayList.get(this.mActiveTarget)).setState(TargetDrawable.STATE_INACTIVE); 
          if (b != -1) {
            ((TargetDrawable)arrayList.get(b)).setState(TargetDrawable.STATE_FOCUSED);
            AccessibilityManager accessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
          } 
        } 
        this.mActiveTarget = b;
      } 
    } 
  }
  
  private void handleUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mPointerId)
      switchToState(5, paramMotionEvent.getX(i), paramMotionEvent.getY(i)); 
  }
  
  private void hideGlow(int paramInt1, int paramInt2, float paramFloat, Animator.AnimatorListener paramAnimatorListener) {
    this.mGlowAnimations.cancel();
    this.mGlowAnimations.add(Tweener.to(this.mPointCloud.glowManager, paramInt1, new Object[] { 
            "ease", Ease.Quart.easeOut, "delay", Integer.valueOf(paramInt2), "alpha", Float.valueOf(paramFloat), "x", Float.valueOf(0.0F), "y", Float.valueOf(0.0F), 
            "onUpdate", this.mUpdateListener, "onComplete", paramAnimatorListener }));
    this.mGlowAnimations.start();
  }
  
  private void hideTargets(boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool1;
    boolean bool2;
    float f;
    Log.e("GGGGGGGGGG", "隐藏");
    this.mTargetAnimations.cancel();
    this.mAnimatingTargets = paramBoolean1;
    if (paramBoolean1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramBoolean1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramBoolean2) {
      f = 1.0F;
    } else {
      f = 0.8F;
    } 
    int i = this.mTargetDrawables.size();
    TimeInterpolator timeInterpolator = Ease.Cubic.easeOut;
    for (byte b = 0; b < i; b++) {
      TargetDrawable targetDrawable = this.mTargetDrawables.get(b);
      targetDrawable.setState(TargetDrawable.STATE_INACTIVE);
      this.mTargetAnimations.add(Tweener.to(targetDrawable, bool1, new Object[] { 
              "ease", timeInterpolator, "alpha", Float.valueOf(0.0F), "scaleX", Float.valueOf(f), "scaleY", Float.valueOf(f), "delay", Integer.valueOf(bool2), 
              "onUpdate", this.mUpdateListener }));
    } 
    if (paramBoolean2) {
      f = 1.0F;
    } else {
      f = 0.5F;
    } 
    this.mTargetAnimations.add(Tweener.to(this.mOuterRing, bool1, new Object[] { 
            "ease", timeInterpolator, "alpha", Float.valueOf(0.0F), "scaleX", Float.valueOf(f), "scaleY", Float.valueOf(f), "delay", Integer.valueOf(bool2), 
            "onUpdate", this.mUpdateListener, "onComplete", this.mTargetUpdateListener }));
    this.mTargetAnimations.start();
  }
  
  private void hideUnselected(int paramInt) {
    for (byte b = 0; b < this.mTargetDrawables.size(); b++) {
      if (b != paramInt)
        ((TargetDrawable)this.mTargetDrawables.get(b)).setAlpha(0.0F); 
    } 
  }
  
  private void highlightSelected(int paramInt) {
    ((TargetDrawable)this.mTargetDrawables.get(paramInt)).setState(TargetDrawable.STATE_ACTIVE);
    hideUnselected(paramInt);
  }
  
  private void internalSetTargetResources(int paramInt) {
    ArrayList<TargetDrawable> arrayList = loadDrawableArray(paramInt);
    this.mTargetDrawables = arrayList;
    this.mTargetResourceId = paramInt;
    paramInt = this.mHandleDrawable.getWidth();
    int i = this.mHandleDrawable.getHeight();
    int j = arrayList.size();
    for (byte b = 0; b < j; b++) {
      TargetDrawable targetDrawable = arrayList.get(b);
      paramInt = Math.max(paramInt, targetDrawable.getWidth());
      i = Math.max(i, targetDrawable.getHeight());
    } 
    if (this.mMaxTargetWidth != paramInt || this.mMaxTargetHeight != i) {
      this.mMaxTargetWidth = paramInt;
      this.mMaxTargetHeight = i;
      requestLayout();
      return;
    } 
    updateTargetPositions(this.mWaveCenterX, this.mWaveCenterY);
    updatePointCloudPosition(this.mWaveCenterX, this.mWaveCenterY);
  }
  
  private ArrayList<String> loadDescriptions(int paramInt) {
    TypedArray typedArray = getContext().getResources().obtainTypedArray(paramInt);
    int i = typedArray.length();
    ArrayList<String> arrayList = new ArrayList(i);
    for (paramInt = 0; paramInt < i; paramInt++)
      arrayList.add(typedArray.getString(paramInt)); 
    typedArray.recycle();
    return arrayList;
  }
  
  private ArrayList<TargetDrawable> loadDrawableArray(int paramInt) {
    Resources resources = getContext().getResources();
    TypedArray typedArray = resources.obtainTypedArray(paramInt);
    int i = typedArray.length();
    ArrayList<TargetDrawable> arrayList = new ArrayList(i);
    for (paramInt = 0; paramInt < i; paramInt++) {
      boolean bool;
      TypedValue typedValue = typedArray.peekValue(paramInt);
      if (typedValue != null) {
        bool = typedValue.resourceId;
      } else {
        bool = false;
      } 
      arrayList.add(new TargetDrawable(resources, bool, 3));
    } 
    typedArray.recycle();
    return arrayList;
  }
  
  private boolean replaceTargetDrawables(Resources paramResources, int paramInt1, int paramInt2) {
    if (paramInt1 == 0 || paramInt2 == 0)
      return false; 
    boolean bool2 = false;
    ArrayList<TargetDrawable> arrayList = this.mTargetDrawables;
    int i = arrayList.size();
    byte b = 0;
    while (b < i) {
      TargetDrawable targetDrawable = arrayList.get(b);
      boolean bool = bool2;
      if (targetDrawable != null) {
        bool = bool2;
        if (targetDrawable.getResourceId() == paramInt1) {
          targetDrawable.setDrawable(paramResources, paramInt2);
          bool = true;
        } 
      } 
      b++;
      bool2 = bool;
    } 
    boolean bool1 = bool2;
    if (bool2) {
      requestLayout();
      bool1 = bool2;
    } 
    return bool1;
  }
  
  private int resolveMeasured(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt1);
    switch (View.MeasureSpec.getMode(paramInt1)) {
      default:
        return i;
      case 0:
        return paramInt2;
      case -2147483648:
        break;
    } 
    return Math.min(i, paramInt2);
  }
  
  private void setGrabbedState(int paramInt) {
    if (paramInt != this.mGrabbedState) {
      if (paramInt != 0)
        vibrate(); 
      this.mGrabbedState = paramInt;
      if (this.mOnTriggerListener != null) {
        if (paramInt == 0) {
          this.mOnTriggerListener.onReleased(this, 1);
        } else {
          this.mOnTriggerListener.onGrabbed(this, 1);
        } 
        this.mOnTriggerListener.onGrabbedStateChange(this, paramInt);
      } 
    } 
  }
  
  private void showGlow(int paramInt1, int paramInt2, float paramFloat, Animator.AnimatorListener paramAnimatorListener) {
    this.mGlowAnimations.cancel();
    this.mGlowAnimations.add(Tweener.to(this.mPointCloud.glowManager, paramInt1, new Object[] { "ease", Ease.Cubic.easeIn, "delay", Integer.valueOf(paramInt2), "alpha", Float.valueOf(paramFloat), "onUpdate", this.mUpdateListener, "onComplete", paramAnimatorListener }));
    this.mGlowAnimations.start();
  }
  
  private void showTargets(boolean paramBoolean) {
    boolean bool1;
    boolean bool2;
    Log.e("GGGGGGGGGG", "显示");
    clearAnimation();
    this.mTargetAnimations.stop();
    this.mAnimatingTargets = paramBoolean;
    if (paramBoolean) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramBoolean) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    int i = this.mTargetDrawables.size();
    for (byte b = 0; b < i; b++) {
      TargetDrawable targetDrawable = this.mTargetDrawables.get(b);
      targetDrawable.setState(TargetDrawable.STATE_INACTIVE);
      this.mTargetAnimations.add(Tweener.to(targetDrawable, bool2, new Object[] { 
              "ease", Ease.Cubic.easeOut, "alpha", Float.valueOf(1.0F), "scaleX", Float.valueOf(1.0F), "scaleY", Float.valueOf(1.0F), "delay", Integer.valueOf(bool1), 
              "onUpdate", this.mUpdateListener }));
    } 
    this.mTargetAnimations.add(Tweener.to(this.mOuterRing, bool2, new Object[] { 
            "ease", Ease.Cubic.easeOut, "alpha", Float.valueOf(1.0F), "scaleX", Float.valueOf(1.0F), "scaleY", Float.valueOf(1.0F), "delay", Integer.valueOf(bool1), 
            "onUpdate", this.mUpdateListener, "onComplete", this.mTargetUpdateListener }));
    this.mTargetAnimations.start();
  }
  
  private float square(float paramFloat) {
    return paramFloat * paramFloat;
  }
  
  @SuppressLint({"NewApi"})
  private void startBackgroundAnimation(int paramInt, float paramFloat) {
    Drawable drawable = getBackground();
    if (this.mAlwaysTrackFinger && drawable != null) {
      if (this.mBackgroundAnimator != null)
        this.mBackgroundAnimator.animator.cancel(); 
      this.mBackgroundAnimator = Tweener.to(drawable, paramInt, new Object[] { "ease", Ease.Cubic.easeIn, "alpha", Integer.valueOf((int)(255.0F * paramFloat)), "delay", Integer.valueOf(50) });
      this.mBackgroundAnimator.animator.start();
    } 
  }
  
  private void startWaveAnimation() {
    this.mWaveAnimations.cancel();
    this.mPointCloud.waveManager.setAlpha(1.0F);
    this.mPointCloud.waveManager.setRadius(this.mHandleDrawable.getWidth() / 2.0F);
    this.mWaveAnimations.add(Tweener.to(this.mPointCloud.waveManager, 1350L, new Object[] { "ease", Ease.Quad.easeOut, "delay", Integer.valueOf(0), "radius", Float.valueOf(this.mOuterRadius * 2.0F), "onUpdate", this.mUpdateListener, "onComplete", new AnimatorListenerAdapter() {
              public void onAnimationEnd(Animator param1Animator) {
                GlowPadView.this.mPointCloud.waveManager.setRadius(0.0F);
                GlowPadView.this.mPointCloud.waveManager.setAlpha(0.0F);
              }
            } }));
    this.mWaveAnimations.start();
  }
  
  private void stopAndHideWaveAnimation() {
    this.mWaveAnimations.cancel();
    this.mPointCloud.waveManager.setAlpha(0.0F);
  }
  
  private void switchToState(int paramInt, float paramFloat1, float paramFloat2) {
    switch (paramInt) {
      default:
        return;
      case 0:
        Log.i("GlowPadView", "STATE_IDLE");
        deactivateTargets();
        hideGlow(0, 0, 0.0F, (Animator.AnimatorListener)null);
        startBackgroundAnimation(0, 0.0F);
        this.mHandleDrawable.setState(TargetDrawable.STATE_INACTIVE);
        this.mHandleDrawable.setAlpha(1.0F);
        if (this.mShowTargetsOnIdle)
          showTargets(true); 
      case 1:
        Log.i("GlowPadView", "STATE_START");
        startBackgroundAnimation(0, 0.0F);
        if (this.mShowTargetsOnIdle)
          showTargets(false); 
      case 2:
        Log.i("GlowPadView", "STATE_FIRST_TOUCH");
        this.mHandleDrawable.setAlpha(0.0F);
        deactivateTargets();
        if (this.mShowTargetsOnIdle) {
          showTargets(false);
        } else {
          showTargets(true);
        } 
        startBackgroundAnimation(200, 1.0F);
        setGrabbedState(1);
        if (((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled())
          announceTargets(); 
      case 3:
        Log.i("GlowPadView", "STATE_TRACKING");
        this.mHandleDrawable.setAlpha(0.0F);
        showGlow(0, 0, 1.0F, (Animator.AnimatorListener)null);
      case 4:
        Log.i("GlowPadView", "STATE_SNAP");
        this.mHandleDrawable.setAlpha(0.0F);
        showGlow(0, 0, 0.0F, (Animator.AnimatorListener)null);
      case 5:
        break;
    } 
    Log.i("GlowPadView", "STATE_FINISH");
    doFinish();
  }
  
  private boolean trySwitchToFirstTouchState(float paramFloat1, float paramFloat2) {
    null = true;
    float f1 = paramFloat1 - this.mWaveCenterX;
    float f2 = paramFloat2 - this.mWaveCenterY;
    if (this.mAlwaysTrackFinger || dist2(f1, f2) <= getScaledGlowRadiusSquared()) {
      switchToState(2, paramFloat1, paramFloat2);
      updateGlowPosition(f1, f2);
      this.mDragging = true;
      return null;
    } 
    return false;
  }
  
  private void updateGlowPosition(float paramFloat1, float paramFloat2) {
    this.mPointCloud.glowManager.setX(paramFloat1);
    this.mPointCloud.glowManager.setY(paramFloat2);
  }
  
  private void updatePointCloudPosition(float paramFloat1, float paramFloat2) {
    this.mPointCloud.setCenter(paramFloat1, paramFloat2);
  }
  
  private void updateTargetPositions(float paramFloat1, float paramFloat2) {
    ArrayList<TargetDrawable> arrayList = this.mTargetDrawables;
    int i = arrayList.size();
    float f = (float)(-6.283185307179586D / i);
    for (byte b = 0; b < i; b++) {
      TargetDrawable targetDrawable = arrayList.get(b);
      float f1 = f * b;
      targetDrawable.setPositionX(paramFloat1);
      targetDrawable.setPositionY(paramFloat2);
      targetDrawable.setX(this.mOuterRadius * (float)Math.cos(f1));
      targetDrawable.setY(this.mOuterRadius * (float)Math.sin(f1));
    } 
  }
  
  private void vibrate() {
    if (this.mVibrator != null)
      this.mVibrator.vibrate(this.mVibrationDuration); 
  }
  
  public int getDirectionDescriptionsResourceId() {
    return this.mDirectionDescriptionsResourceId;
  }
  
  public int getResourceIdForTarget(int paramInt) {
    TargetDrawable targetDrawable = this.mTargetDrawables.get(paramInt);
    return (targetDrawable == null) ? 0 : targetDrawable.getResourceId();
  }
  
  protected int getSuggestedMinimumHeight() {
    return (int)(Math.max(this.mOuterRing.getHeight(), 2.0F * this.mOuterRadius) + this.mMaxTargetHeight);
  }
  
  protected int getSuggestedMinimumWidth() {
    return (int)(Math.max(this.mOuterRing.getWidth(), 2.0F * this.mOuterRadius) + this.mMaxTargetWidth);
  }
  
  public int getTargetDescriptionsResourceId() {
    return this.mTargetDescriptionsResourceId;
  }
  
  public int getTargetPosition(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iload_2
    //   3: aload_0
    //   4: getfield mTargetDrawables : Ljava/util/ArrayList;
    //   7: invokevirtual size : ()I
    //   10: if_icmpge -> 39
    //   13: aload_0
    //   14: getfield mTargetDrawables : Ljava/util/ArrayList;
    //   17: iload_2
    //   18: invokevirtual get : (I)Ljava/lang/Object;
    //   21: checkcast app/lib/lockview/TargetDrawable
    //   24: invokevirtual getResourceId : ()I
    //   27: iload_1
    //   28: if_icmpne -> 33
    //   31: iload_2
    //   32: ireturn
    //   33: iinc #2, 1
    //   36: goto -> 2
    //   39: iconst_m1
    //   40: istore_2
    //   41: goto -> 31
  }
  
  public int getTargetResourceId() {
    return this.mTargetResourceId;
  }
  
  public boolean isShowTargetsOnIdle() {
    return this.mShowTargetsOnIdle;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    this.mPointCloud.draw(paramCanvas);
    this.mOuterRing.draw(paramCanvas);
    int i = this.mTargetDrawables.size();
    for (byte b = 0; b < i; b++) {
      TargetDrawable targetDrawable = this.mTargetDrawables.get(b);
      if (targetDrawable != null)
        targetDrawable.draw(paramCanvas); 
    } 
    this.mHandleDrawable.draw(paramCanvas);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    float f1 = Math.max(this.mOuterRing.getWidth(), 2.0F * this.mOuterRadius);
    float f2 = Math.max(this.mOuterRing.getHeight(), 2.0F * this.mOuterRadius);
    f1 = this.mHorizontalInset + Math.max((paramInt3 - paramInt1), this.mMaxTargetWidth + f1) / 2.0F;
    f2 = this.mVerticalInset + Math.max((paramInt4 - paramInt2), this.mMaxTargetHeight + f2) / 2.0F;
    if (this.mInitialLayout) {
      stopAndHideWaveAnimation();
      if (this.mShowTargetsOnIdle) {
        showTargets(false);
      } else {
        hideTargets(false, false);
      } 
      this.mInitialLayout = false;
    } 
    this.mOuterRing.setPositionX(f1);
    this.mOuterRing.setPositionY(f2);
    this.mHandleDrawable.setPositionX(f1);
    this.mHandleDrawable.setPositionY(f2);
    updateTargetPositions(f1, f2);
    updatePointCloudPosition(f1, f2);
    updateGlowPosition(f1, f2);
    this.mWaveCenterX = f1;
    this.mWaveCenterY = f2;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getSuggestedMinimumWidth();
    int j = getSuggestedMinimumHeight();
    paramInt1 = resolveMeasured(paramInt1, i);
    paramInt2 = resolveMeasured(paramInt2, j);
    if (Build.VERSION.SDK_INT >= 11)
      computeInsets(paramInt1 - i, paramInt2 - j); 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    boolean bool1 = false;
    boolean bool2 = bool1;
    switch (i) {
      default:
        bool2 = bool1;
      case 4:
        invalidate();
        if (bool2)
          return true; 
        break;
      case 0:
      case 5:
        handleDown(paramMotionEvent);
        handleMove(paramMotionEvent);
        bool2 = true;
      case 2:
        handleMove(paramMotionEvent);
        bool2 = true;
      case 1:
      case 6:
        handleMove(paramMotionEvent);
        handleUp(paramMotionEvent);
        bool2 = true;
      case 3:
        handleMove(paramMotionEvent);
        handleCancel(paramMotionEvent);
        bool2 = true;
    } 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  @SuppressLint({"NewApi"})
  public void ping() {
    if (this.mFeedbackCount > 0) {
      boolean bool1 = true;
      AnimationBundle animationBundle = this.mWaveAnimations;
      boolean bool2 = bool1;
      if (animationBundle.size() > 0) {
        bool2 = bool1;
        if ((animationBundle.get(0)).animator.isRunning()) {
          bool2 = bool1;
          if ((animationBundle.get(0)).animator.getCurrentPlayTime() < 675L)
            bool2 = false; 
        } 
      } 
      if (bool2)
        startWaveAnimation(); 
    } 
  }
  
  public boolean replaceTargetDrawablesIfPresent(ComponentName paramComponentName, String paramString, int paramInt) {
    // Byte code:
    //   0: iload_3
    //   1: ifne -> 10
    //   4: iconst_0
    //   5: istore #4
    //   7: iload #4
    //   9: ireturn
    //   10: iconst_0
    //   11: istore #4
    //   13: iload #4
    //   15: istore #5
    //   17: aload_1
    //   18: ifnull -> 85
    //   21: aload_0
    //   22: invokevirtual getContext : ()Landroid/content/Context;
    //   25: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
    //   28: astore #6
    //   30: aload #6
    //   32: aload_1
    //   33: sipush #128
    //   36: invokevirtual getActivityInfo : (Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;
    //   39: getfield metaData : Landroid/os/Bundle;
    //   42: astore #7
    //   44: iload #4
    //   46: istore #5
    //   48: aload #7
    //   50: ifnull -> 85
    //   53: aload #7
    //   55: aload_2
    //   56: invokevirtual getInt : (Ljava/lang/String;)I
    //   59: istore #8
    //   61: iload #4
    //   63: istore #5
    //   65: iload #8
    //   67: ifeq -> 85
    //   70: aload_0
    //   71: aload #6
    //   73: aload_1
    //   74: invokevirtual getResourcesForActivity : (Landroid/content/ComponentName;)Landroid/content/res/Resources;
    //   77: iload_3
    //   78: iload #8
    //   80: invokespecial replaceTargetDrawables : (Landroid/content/res/Resources;II)Z
    //   83: istore #5
    //   85: iload #5
    //   87: istore #4
    //   89: iload #5
    //   91: ifne -> 7
    //   94: aload_0
    //   95: aload_0
    //   96: invokevirtual getContext : ()Landroid/content/Context;
    //   99: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   102: iload_3
    //   103: iload_3
    //   104: invokespecial replaceTargetDrawables : (Landroid/content/res/Resources;II)Z
    //   107: pop
    //   108: iload #5
    //   110: istore #4
    //   112: goto -> 7
    //   115: astore_2
    //   116: ldc 'GlowPadView'
    //   118: new java/lang/StringBuilder
    //   121: dup
    //   122: invokespecial <init> : ()V
    //   125: ldc_w 'Failed to swap drawable; '
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: aload_1
    //   132: invokevirtual flattenToShortString : ()Ljava/lang/String;
    //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: ldc_w ' not found'
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: invokevirtual toString : ()Ljava/lang/String;
    //   147: aload_2
    //   148: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   151: pop
    //   152: iload #4
    //   154: istore #5
    //   156: goto -> 85
    //   159: astore_2
    //   160: ldc 'GlowPadView'
    //   162: new java/lang/StringBuilder
    //   165: dup
    //   166: invokespecial <init> : ()V
    //   169: ldc_w 'Failed to swap drawable from '
    //   172: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: aload_1
    //   176: invokevirtual flattenToShortString : ()Ljava/lang/String;
    //   179: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: invokevirtual toString : ()Ljava/lang/String;
    //   185: aload_2
    //   186: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   189: pop
    //   190: iload #4
    //   192: istore #5
    //   194: goto -> 85
    // Exception table:
    //   from	to	target	type
    //   21	44	115	android/content/pm/PackageManager$NameNotFoundException
    //   21	44	159	android/content/res/Resources$NotFoundException
    //   53	61	115	android/content/pm/PackageManager$NameNotFoundException
    //   53	61	159	android/content/res/Resources$NotFoundException
    //   70	85	115	android/content/pm/PackageManager$NameNotFoundException
    //   70	85	159	android/content/res/Resources$NotFoundException
  }
  
  public void reset(boolean paramBoolean) {
    this.mGlowAnimations.stop();
    this.mTargetAnimations.stop();
    startBackgroundAnimation(0, 0.0F);
    stopAndHideWaveAnimation();
    hideTargets(paramBoolean, false);
    hideGlow(0, 0, 0.0F, (Animator.AnimatorListener)null);
    Tweener.reset();
  }
  
  public void resumeAnimations() {
    this.mWaveAnimations.setSuspended(false);
    this.mTargetAnimations.setSuspended(false);
    this.mGlowAnimations.setSuspended(false);
    this.mWaveAnimations.start();
    this.mTargetAnimations.start();
    this.mGlowAnimations.start();
  }
  
  public void setDirectionDescriptionsResourceId(int paramInt) {
    this.mDirectionDescriptionsResourceId = paramInt;
    if (this.mDirectionDescriptions != null)
      this.mDirectionDescriptions.clear(); 
  }
  
  public void setEnableTarget(int paramInt, boolean paramBoolean) {
    for (byte b = 0;; b++) {
      if (b < this.mTargetDrawables.size()) {
        TargetDrawable targetDrawable = this.mTargetDrawables.get(b);
        if (targetDrawable.getResourceId() == paramInt) {
          targetDrawable.setEnabled(paramBoolean);
          return;
        } 
      } else {
        return;
      } 
    } 
  }
  
  public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener) {
    this.mOnTriggerListener = paramOnTriggerListener;
  }
  
  public void setShowTargetsOnIdle(boolean paramBoolean) {
    this.mShowTargetsOnIdle = paramBoolean;
  }
  
  public void setTargetDescriptionsResourceId(int paramInt) {
    this.mTargetDescriptionsResourceId = paramInt;
    if (this.mTargetDescriptions != null)
      this.mTargetDescriptions.clear(); 
  }
  
  public void setTargetResources(int paramInt) {
    if (this.mAnimatingTargets) {
      this.mNewTargetResources = paramInt;
      return;
    } 
    internalSetTargetResources(paramInt);
  }
  
  public void setVibrateEnabled(boolean paramBoolean) {
    if (paramBoolean && this.mVibrator == null) {
      this.mVibrator = (Vibrator)getContext().getSystemService("vibrator");
      return;
    } 
    this.mVibrator = null;
  }
  
  public void suspendAnimations() {
    this.mWaveAnimations.setSuspended(true);
    this.mTargetAnimations.setSuspended(true);
    this.mGlowAnimations.setSuspended(true);
  }
  
  private class AnimationBundle extends ArrayList<Tweener> {
    private static final long serialVersionUID = -6319262269245852568L;
    
    private boolean mSuspended;
    
    private AnimationBundle() {}
    
    public void cancel() {
      int i = size();
      for (byte b = 0; b < i; b++)
        (get(b)).animator.cancel(); 
      clear();
    }
    
    public void setSuspended(boolean param1Boolean) {
      this.mSuspended = param1Boolean;
    }
    
    public void start() {
      if (!this.mSuspended) {
        int i = size();
        byte b = 0;
        while (true) {
          if (b < i) {
            (get(b)).animator.start();
            b++;
            continue;
          } 
          return;
        } 
      } 
    }
    
    public void stop() {
      int i = size();
      for (byte b = 0; b < i; b++)
        (get(b)).animator.end(); 
      clear();
    }
  }
  
  public static interface OnTriggerListener {
    public static final int CENTER_HANDLE = 1;
    
    public static final int NO_HANDLE = 0;
    
    void onFinishFinalAnimation();
    
    void onGrabbed(View param1View, int param1Int);
    
    void onGrabbedStateChange(View param1View, int param1Int);
    
    void onReleased(View param1View, int param1Int);
    
    void onTrigger(View param1View, int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/lockview/GlowPadView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */