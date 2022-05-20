package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.recyclerview.R;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild {
  static {
    CLIP_TO_PADDING_ATTR = new int[] { 16842987 };
    if (Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_INVALIDATE_DISPLAY_LIST = bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_SIZE_IN_UNSPECIFIED_SPEC = bool;
    if (Build.VERSION.SDK_INT >= 16) {
      bool = true;
    } else {
      bool = false;
    } 
    POST_UPDATES_ON_ANIMATION = bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_THREAD_GAP_WORK = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_ABS_FOCUS_SEARCH_DIRECTION = bool;
    LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, int.class, int.class };
    sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
  }
  
  public RecyclerView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RecyclerView(Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public RecyclerView(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl;
    this.mObserver = new RecyclerViewDataObserver();
    this.mRecycler = new Recycler();
    this.mViewInfoStore = new ViewInfoStore();
    this.mUpdateChildViewsRunnable = new Runnable() {
        public void run() {
          if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
            if (!RecyclerView.this.mIsAttached) {
              RecyclerView.this.requestLayout();
              return;
            } 
            if (RecyclerView.this.mLayoutFrozen) {
              RecyclerView.this.mLayoutRequestEaten = true;
              return;
            } 
            RecyclerView.this.consumePendingUpdateOperations();
          } 
        }
      };
    this.mTempRect = new Rect();
    this.mTempRect2 = new Rect();
    this.mTempRectF = new RectF();
    this.mItemDecorations = new ArrayList<ItemDecoration>();
    this.mOnItemTouchListeners = new ArrayList<OnItemTouchListener>();
    this.mEatRequestLayout = 0;
    this.mDataSetHasChangedAfterLayout = false;
    this.mLayoutOrScrollCounter = 0;
    this.mDispatchScrollCounter = 0;
    this.mItemAnimator = new DefaultItemAnimator();
    this.mScrollState = 0;
    this.mScrollPointerId = -1;
    this.mScrollFactor = Float.MIN_VALUE;
    this.mPreserveFocusAfterLayout = true;
    this.mViewFlinger = new ViewFlinger();
    if (ALLOW_THREAD_GAP_WORK) {
      layoutPrefetchRegistryImpl = new GapWorker.LayoutPrefetchRegistryImpl();
    } else {
      layoutPrefetchRegistryImpl = null;
    } 
    this.mPrefetchRegistry = layoutPrefetchRegistryImpl;
    this.mState = new State();
    this.mItemsAddedOrRemoved = false;
    this.mItemsChanged = false;
    this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
    this.mPostedAnimatorRunner = false;
    this.mMinMaxLayoutPositions = new int[2];
    this.mScrollOffset = new int[2];
    this.mScrollConsumed = new int[2];
    this.mNestedOffsets = new int[2];
    this.mPendingAccessibilityImportanceChange = new ArrayList<ViewHolder>();
    this.mItemAnimatorRunner = new Runnable() {
        public void run() {
          if (RecyclerView.this.mItemAnimator != null)
            RecyclerView.this.mItemAnimator.runPendingAnimations(); 
          RecyclerView.this.mPostedAnimatorRunner = false;
        }
      };
    this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
        public void processAppeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.animateAppearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processDisappeared(RecyclerView.ViewHolder param1ViewHolder, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.mRecycler.unscrapView(param1ViewHolder);
          RecyclerView.this.animateDisappearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processPersistent(RecyclerView.ViewHolder param1ViewHolder, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          param1ViewHolder.setIsRecyclable(false);
          if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
            if (RecyclerView.this.mItemAnimator.animateChange(param1ViewHolder, param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2))
              RecyclerView.this.postAnimationRunner(); 
            return;
          } 
          if (RecyclerView.this.mItemAnimator.animatePersistence(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2))
            RecyclerView.this.postAnimationRunner(); 
        }
        
        public void unused(RecyclerView.ViewHolder param1ViewHolder) {
          RecyclerView.this.mLayout.removeAndRecycleView(param1ViewHolder.itemView, RecyclerView.this.mRecycler);
        }
      };
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, CLIP_TO_PADDING_ATTR, paramInt, 0);
      this.mClipToPadding = typedArray.getBoolean(0, true);
      typedArray.recycle();
    } else {
      this.mClipToPadding = true;
    } 
    setScrollContainer(true);
    setFocusableInTouchMode(true);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    if (getOverScrollMode() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    this.mItemAnimator.setListener(this.mItemAnimatorListener);
    initAdapterManager();
    initChildrenHelper();
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    this.mAccessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
    setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
    boolean bool = true;
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt, 0);
      String str = typedArray.getString(R.styleable.RecyclerView_layoutManager);
      if (typedArray.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1)
        setDescendantFocusability(262144); 
      typedArray.recycle();
      createLayoutManager(paramContext, str, paramAttributeSet, paramInt, 0);
      if (Build.VERSION.SDK_INT >= 21) {
        TypedArray typedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, NESTED_SCROLLING_ATTRS, paramInt, 0);
        bool = typedArray1.getBoolean(0, true);
        typedArray1.recycle();
      } 
    } else {
      setDescendantFocusability(262144);
    } 
    setNestedScrollingEnabled(bool);
  }
  
  private void addAnimatingView(ViewHolder paramViewHolder) {
    boolean bool;
    View view = paramViewHolder.itemView;
    if (view.getParent() == this) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRecycler.unscrapView(getChildViewHolder(view));
    if (paramViewHolder.isTmpDetached()) {
      this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
      return;
    } 
    if (!bool) {
      this.mChildHelper.addView(view, true);
      return;
    } 
    this.mChildHelper.hide(view);
  }
  
  private void animateChange(@NonNull ViewHolder paramViewHolder1, @NonNull ViewHolder paramViewHolder2, @NonNull ItemAnimator.ItemHolderInfo paramItemHolderInfo1, @NonNull ItemAnimator.ItemHolderInfo paramItemHolderInfo2, boolean paramBoolean1, boolean paramBoolean2) {
    paramViewHolder1.setIsRecyclable(false);
    if (paramBoolean1)
      addAnimatingView(paramViewHolder1); 
    if (paramViewHolder1 != paramViewHolder2) {
      if (paramBoolean2)
        addAnimatingView(paramViewHolder2); 
      paramViewHolder1.mShadowedHolder = paramViewHolder2;
      addAnimatingView(paramViewHolder1);
      this.mRecycler.unscrapView(paramViewHolder1);
      paramViewHolder2.setIsRecyclable(false);
      paramViewHolder2.mShadowingHolder = paramViewHolder1;
    } 
    if (this.mItemAnimator.animateChange(paramViewHolder1, paramViewHolder2, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  private void cancelTouch() {
    resetTouch();
    setScrollState(0);
  }
  
  static void clearNestedRecyclerViewIfNotNested(@NonNull ViewHolder paramViewHolder) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mNestedRecyclerView : Ljava/lang/ref/WeakReference;
    //   4: ifnull -> 30
    //   7: aload_0
    //   8: getfield mNestedRecyclerView : Ljava/lang/ref/WeakReference;
    //   11: invokevirtual get : ()Ljava/lang/Object;
    //   14: checkcast android/view/View
    //   17: astore_1
    //   18: aload_1
    //   19: ifnull -> 56
    //   22: aload_1
    //   23: aload_0
    //   24: getfield itemView : Landroid/view/View;
    //   27: if_acmpne -> 31
    //   30: return
    //   31: aload_1
    //   32: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   35: astore_1
    //   36: aload_1
    //   37: instanceof android/view/View
    //   40: ifeq -> 51
    //   43: aload_1
    //   44: checkcast android/view/View
    //   47: astore_1
    //   48: goto -> 18
    //   51: aconst_null
    //   52: astore_1
    //   53: goto -> 18
    //   56: aload_0
    //   57: aconst_null
    //   58: putfield mNestedRecyclerView : Ljava/lang/ref/WeakReference;
    //   61: goto -> 30
  }
  
  private void createLayoutManager(Context paramContext, String paramString, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    if (paramString != null) {
      paramString = paramString.trim();
      if (paramString.length() != 0) {
        String str = getFullClassName(paramContext, paramString);
        try {
          ClassLoader classLoader;
          if (isInEditMode()) {
            classLoader = getClass().getClassLoader();
          } else {
            classLoader = paramContext.getClassLoader();
          } 
          Class<? extends LayoutManager> clazz = classLoader.loadClass(str).asSubclass(LayoutManager.class);
          NoSuchMethodException noSuchMethodException = null;
          try {
            Constructor<? extends LayoutManager> constructor = clazz.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
            Object[] arrayOfObject = { paramContext, paramAttributeSet, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) };
            constructor.setAccessible(true);
            setLayoutManager(constructor.newInstance(arrayOfObject));
            return;
          } catch (NoSuchMethodException noSuchMethodException1) {
            try {
              Constructor<? extends LayoutManager> constructor = clazz.getConstructor(new Class[0]);
              noSuchMethodException1 = noSuchMethodException;
              constructor.setAccessible(true);
              setLayoutManager(constructor.newInstance((Object[])noSuchMethodException1));
              return;
            } catch (NoSuchMethodException noSuchMethodException2) {
              noSuchMethodException2.initCause(noSuchMethodException1);
              IllegalStateException illegalStateException = new IllegalStateException();
              StringBuilder stringBuilder = new StringBuilder();
              this();
              this(stringBuilder.append(paramAttributeSet.getPositionDescription()).append(": Error creating LayoutManager ").append(str).toString(), noSuchMethodException2);
              throw illegalStateException;
            } 
          } 
        } catch (ClassNotFoundException classNotFoundException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Unable to find LayoutManager " + str, classNotFoundException);
        } catch (InvocationTargetException invocationTargetException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, invocationTargetException);
        } catch (InstantiationException instantiationException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, instantiationException);
        } catch (IllegalAccessException illegalAccessException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Cannot access non-public constructor " + str, illegalAccessException);
        } catch (ClassCastException classCastException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Class is not a LayoutManager " + str, classCastException);
        } 
      } 
    } 
  }
  
  private boolean didChildRangeChange(int paramInt1, int paramInt2) {
    boolean bool = false;
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    if (this.mMinMaxLayoutPositions[0] != paramInt1 || this.mMinMaxLayoutPositions[1] != paramInt2)
      bool = true; 
    return bool;
  }
  
  private void dispatchContentChangedIfNecessary() {
    int i = this.mEatenAccessibilityChangeFlags;
    this.mEatenAccessibilityChangeFlags = 0;
    if (i != 0 && isAccessibilityEnabled()) {
      AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
      accessibilityEvent.setEventType(2048);
      AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, i);
      sendAccessibilityEventUnchecked(accessibilityEvent);
    } 
  }
  
  private void dispatchLayoutStep1() {
    boolean bool;
    this.mState.assertLayoutStep(1);
    this.mState.mIsMeasuring = false;
    eatRequestLayout();
    this.mViewInfoStore.clear();
    onEnterLayoutOrScroll();
    processAdapterUpdatesAndSetAnimationFlags();
    saveFocusInfo();
    State state = this.mState;
    if (this.mState.mRunSimpleAnimations && this.mItemsChanged) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mTrackOldChangeHolders = bool;
    this.mItemsChanged = false;
    this.mItemsAddedOrRemoved = false;
    this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
    this.mState.mItemCount = this.mAdapter.getItemCount();
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    if (this.mState.mRunSimpleAnimations) {
      int i = this.mChildHelper.getChildCount();
      for (byte b = 0; b < i; b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && (!viewHolder.isInvalid() || this.mAdapter.hasStableIds())) {
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder), viewHolder.getUnmodifiedPayloads());
          this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
          if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore() && !viewHolder.isInvalid()) {
            long l = getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(l, viewHolder);
          } 
        } 
      } 
    } 
    if (this.mState.mRunPredictiveAnimations) {
      saveOldPositions();
      bool = this.mState.mStructureChanged;
      this.mState.mStructureChanged = false;
      this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
      this.mState.mStructureChanged = bool;
      for (byte b = 0; b < this.mChildHelper.getChildCount(); b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(viewHolder)) {
          int j = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
          bool = viewHolder.hasAnyOfTheFlags(8192);
          int i = j;
          if (!bool)
            i = j | 0x1000; 
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, i, viewHolder.getUnmodifiedPayloads());
          if (bool) {
            recordAnimationInfoIfBouncedHiddenView(viewHolder, itemHolderInfo);
          } else {
            this.mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      clearOldPositions();
    } else {
      clearOldPositions();
    } 
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
    this.mState.mLayoutStep = 2;
  }
  
  private void dispatchLayoutStep2() {
    boolean bool;
    eatRequestLayout();
    onEnterLayoutOrScroll();
    this.mState.assertLayoutStep(6);
    this.mAdapterHelper.consumeUpdatesInOnePass();
    this.mState.mItemCount = this.mAdapter.getItemCount();
    this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
    this.mState.mInPreLayout = false;
    this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
    this.mState.mStructureChanged = false;
    this.mPendingSavedState = null;
    State state = this.mState;
    if (this.mState.mRunSimpleAnimations && this.mItemAnimator != null) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mRunSimpleAnimations = bool;
    this.mState.mLayoutStep = 4;
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
  }
  
  private void dispatchLayoutStep3() {
    this.mState.assertLayoutStep(4);
    eatRequestLayout();
    onEnterLayoutOrScroll();
    this.mState.mLayoutStep = 1;
    if (this.mState.mRunSimpleAnimations) {
      for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
        if (!viewHolder.shouldIgnore()) {
          long l = getChangedHolderKey(viewHolder);
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, viewHolder);
          ViewHolder viewHolder1 = this.mViewInfoStore.getFromOldChangeHolders(l);
          if (viewHolder1 != null && !viewHolder1.shouldIgnore()) {
            boolean bool1 = this.mViewInfoStore.isDisappearing(viewHolder1);
            boolean bool2 = this.mViewInfoStore.isDisappearing(viewHolder);
            if (bool1 && viewHolder1 == viewHolder) {
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
            } else {
              ItemAnimator.ItemHolderInfo itemHolderInfo1 = this.mViewInfoStore.popFromPreLayout(viewHolder1);
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
              itemHolderInfo = this.mViewInfoStore.popFromPostLayout(viewHolder);
              if (itemHolderInfo1 == null) {
                handleMissingPreInfoForChangeError(l, viewHolder, viewHolder1);
              } else {
                animateChange(viewHolder1, viewHolder, itemHolderInfo1, itemHolderInfo, bool1, bool2);
              } 
            } 
          } else {
            this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      this.mViewInfoStore.process(this.mViewInfoProcessCallback);
    } 
    this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
    this.mDataSetHasChangedAfterLayout = false;
    this.mState.mRunSimpleAnimations = false;
    this.mState.mRunPredictiveAnimations = false;
    this.mLayout.mRequestedSimpleAnimations = false;
    if (this.mRecycler.mChangedScrap != null)
      this.mRecycler.mChangedScrap.clear(); 
    if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
      this.mLayout.mPrefetchMaxCountObserved = 0;
      this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
      this.mRecycler.updateViewCacheSize();
    } 
    this.mLayout.onLayoutCompleted(this.mState);
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
    this.mViewInfoStore.clear();
    if (didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1]))
      dispatchOnScrolled(0, 0); 
    recoverFocusFromState();
    resetFocusInfo();
  }
  
  private boolean dispatchOnItemTouch(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_1
    //   3: invokevirtual getAction : ()I
    //   6: istore_3
    //   7: aload_0
    //   8: getfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   11: ifnull -> 23
    //   14: iload_3
    //   15: ifne -> 81
    //   18: aload_0
    //   19: aconst_null
    //   20: putfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   23: iload_3
    //   24: ifeq -> 122
    //   27: aload_0
    //   28: getfield mOnItemTouchListeners : Ljava/util/ArrayList;
    //   31: invokevirtual size : ()I
    //   34: istore #4
    //   36: iconst_0
    //   37: istore_3
    //   38: iload_3
    //   39: iload #4
    //   41: if_icmpge -> 122
    //   44: aload_0
    //   45: getfield mOnItemTouchListeners : Ljava/util/ArrayList;
    //   48: iload_3
    //   49: invokevirtual get : (I)Ljava/lang/Object;
    //   52: checkcast android/support/v7/widget/RecyclerView$OnItemTouchListener
    //   55: astore #5
    //   57: aload #5
    //   59: aload_0
    //   60: aload_1
    //   61: invokeinterface onInterceptTouchEvent : (Landroid/support/v7/widget/RecyclerView;Landroid/view/MotionEvent;)Z
    //   66: ifeq -> 116
    //   69: aload_0
    //   70: aload #5
    //   72: putfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   75: iload_2
    //   76: istore #6
    //   78: iload #6
    //   80: ireturn
    //   81: aload_0
    //   82: getfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   85: aload_0
    //   86: aload_1
    //   87: invokeinterface onTouchEvent : (Landroid/support/v7/widget/RecyclerView;Landroid/view/MotionEvent;)V
    //   92: iload_3
    //   93: iconst_3
    //   94: if_icmpeq -> 105
    //   97: iload_2
    //   98: istore #6
    //   100: iload_3
    //   101: iconst_1
    //   102: if_icmpne -> 78
    //   105: aload_0
    //   106: aconst_null
    //   107: putfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   110: iload_2
    //   111: istore #6
    //   113: goto -> 78
    //   116: iinc #3, 1
    //   119: goto -> 38
    //   122: iconst_0
    //   123: istore #6
    //   125: goto -> 78
  }
  
  private boolean dispatchOnItemTouchIntercept(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getAction : ()I
    //   4: istore_2
    //   5: iload_2
    //   6: iconst_3
    //   7: if_icmpeq -> 14
    //   10: iload_2
    //   11: ifne -> 19
    //   14: aload_0
    //   15: aconst_null
    //   16: putfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   19: aload_0
    //   20: getfield mOnItemTouchListeners : Ljava/util/ArrayList;
    //   23: invokevirtual size : ()I
    //   26: istore_3
    //   27: iconst_0
    //   28: istore #4
    //   30: iload #4
    //   32: iload_3
    //   33: if_icmpge -> 85
    //   36: aload_0
    //   37: getfield mOnItemTouchListeners : Ljava/util/ArrayList;
    //   40: iload #4
    //   42: invokevirtual get : (I)Ljava/lang/Object;
    //   45: checkcast android/support/v7/widget/RecyclerView$OnItemTouchListener
    //   48: astore #5
    //   50: aload #5
    //   52: aload_0
    //   53: aload_1
    //   54: invokeinterface onInterceptTouchEvent : (Landroid/support/v7/widget/RecyclerView;Landroid/view/MotionEvent;)Z
    //   59: ifeq -> 79
    //   62: iload_2
    //   63: iconst_3
    //   64: if_icmpeq -> 79
    //   67: aload_0
    //   68: aload #5
    //   70: putfield mActiveOnItemTouchListener : Landroid/support/v7/widget/RecyclerView$OnItemTouchListener;
    //   73: iconst_1
    //   74: istore #6
    //   76: iload #6
    //   78: ireturn
    //   79: iinc #4, 1
    //   82: goto -> 30
    //   85: iconst_0
    //   86: istore #6
    //   88: goto -> 76
  }
  
  private void findMinMaxChildLayoutPositions(int[] paramArrayOfint) {
    int i = this.mChildHelper.getChildCount();
    if (i == 0) {
      paramArrayOfint[0] = -1;
      paramArrayOfint[1] = -1;
      return;
    } 
    int j = Integer.MAX_VALUE;
    int k = Integer.MIN_VALUE;
    byte b = 0;
    while (b < i) {
      int m;
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder.shouldIgnore()) {
        m = j;
        j = k;
      } else {
        int n = viewHolder.getLayoutPosition();
        int i1 = j;
        if (n < j)
          i1 = n; 
        j = k;
        m = i1;
        if (n > k) {
          j = n;
          m = i1;
        } 
      } 
      b++;
      k = j;
      j = m;
    } 
    paramArrayOfint[0] = j;
    paramArrayOfint[1] = k;
  }
  
  @Nullable
  static RecyclerView findNestedRecyclerView(@NonNull View paramView) {
    // Byte code:
    //   0: aload_0
    //   1: instanceof android/view/ViewGroup
    //   4: ifne -> 11
    //   7: aconst_null
    //   8: astore_0
    //   9: aload_0
    //   10: areturn
    //   11: aload_0
    //   12: instanceof android/support/v7/widget/RecyclerView
    //   15: ifeq -> 26
    //   18: aload_0
    //   19: checkcast android/support/v7/widget/RecyclerView
    //   22: astore_0
    //   23: goto -> 9
    //   26: aload_0
    //   27: checkcast android/view/ViewGroup
    //   30: astore_1
    //   31: aload_1
    //   32: invokevirtual getChildCount : ()I
    //   35: istore_2
    //   36: iconst_0
    //   37: istore_3
    //   38: iload_3
    //   39: iload_2
    //   40: if_icmpge -> 65
    //   43: aload_1
    //   44: iload_3
    //   45: invokevirtual getChildAt : (I)Landroid/view/View;
    //   48: invokestatic findNestedRecyclerView : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView;
    //   51: astore_0
    //   52: aload_0
    //   53: ifnull -> 59
    //   56: goto -> 9
    //   59: iinc #3, 1
    //   62: goto -> 38
    //   65: aconst_null
    //   66: astore_0
    //   67: goto -> 9
  }
  
  @Nullable
  private View findNextViewToFocus() {
    int i;
    ViewHolder viewHolder = null;
    if (this.mState.mFocusedItemPosition != -1) {
      i = this.mState.mFocusedItemPosition;
    } else {
      i = 0;
    } 
    int j = this.mState.getItemCount();
    int k = i;
    while (true) {
      if (k < j) {
        ViewHolder viewHolder1 = findViewHolderForAdapterPosition(k);
        if (viewHolder1 != null) {
          if (viewHolder1.itemView.hasFocusable()) {
            View view = viewHolder1.itemView;
            continue;
          } 
          k++;
          continue;
        } 
      } 
      for (i = Math.min(j, i) - 1;; i--) {
        ViewHolder viewHolder1 = viewHolder;
        if (i >= 0) {
          viewHolder1 = findViewHolderForAdapterPosition(i);
          if (viewHolder1 == null)
            return (View)viewHolder; 
        } else {
          return (View)viewHolder1;
        } 
        if (viewHolder1.itemView.hasFocusable())
          return viewHolder1.itemView; 
      } 
      break;
    } 
  }
  
  static ViewHolder getChildViewHolderInt(View paramView) {
    return (paramView == null) ? null : ((LayoutParams)paramView.getLayoutParams()).mViewHolder;
  }
  
  static void getDecoratedBoundsWithMarginsInt(View paramView, Rect paramRect) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    paramRect.set(paramView.getLeft() - rect.left - layoutParams.leftMargin, paramView.getTop() - rect.top - layoutParams.topMargin, paramView.getRight() + rect.right + layoutParams.rightMargin, paramView.getBottom() + rect.bottom + layoutParams.bottomMargin);
  }
  
  private int getDeepestFocusedViewWithId(View paramView) {
    int i = paramView.getId();
    while (!paramView.isFocused() && paramView instanceof ViewGroup && paramView.hasFocus()) {
      View view = ((ViewGroup)paramView).getFocusedChild();
      paramView = view;
      if (view.getId() != -1) {
        i = view.getId();
        paramView = view;
      } 
    } 
    return i;
  }
  
  private String getFullClassName(Context paramContext, String paramString) {
    if (paramString.charAt(0) == '.')
      return paramContext.getPackageName() + paramString; 
    String str = paramString;
    if (!paramString.contains("."))
      str = RecyclerView.class.getPackage().getName() + '.' + paramString; 
    return str;
  }
  
  private float getScrollFactor() {
    if (this.mScrollFactor == Float.MIN_VALUE) {
      TypedValue typedValue = new TypedValue();
      if (getContext().getTheme().resolveAttribute(16842829, typedValue, true)) {
        this.mScrollFactor = typedValue.getDimension(getContext().getResources().getDisplayMetrics());
      } else {
        return 0.0F;
      } 
    } 
    return this.mScrollFactor;
  }
  
  private NestedScrollingChildHelper getScrollingChildHelper() {
    if (this.mScrollingChildHelper == null)
      this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this); 
    return this.mScrollingChildHelper;
  }
  
  private void handleMissingPreInfoForChangeError(long paramLong, ViewHolder paramViewHolder1, ViewHolder paramViewHolder2) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder != paramViewHolder1 && getChangedHolderKey(viewHolder) == paramLong) {
        if (this.mAdapter != null && this.mAdapter.hasStableIds())
          throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + viewHolder + " \n View Holder 2:" + paramViewHolder1); 
        throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + viewHolder + " \n View Holder 2:" + paramViewHolder1);
      } 
    } 
    Log.e("RecyclerView", "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + paramViewHolder2 + " cannot be found but it is necessary for " + paramViewHolder1);
  }
  
  private boolean hasUpdatedView() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   4: invokevirtual getChildCount : ()I
    //   7: istore_1
    //   8: iconst_0
    //   9: istore_2
    //   10: iload_2
    //   11: iload_1
    //   12: if_icmpge -> 57
    //   15: aload_0
    //   16: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   19: iload_2
    //   20: invokevirtual getChildAt : (I)Landroid/view/View;
    //   23: invokestatic getChildViewHolderInt : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 38
    //   31: aload_3
    //   32: invokevirtual shouldIgnore : ()Z
    //   35: ifeq -> 44
    //   38: iinc #2, 1
    //   41: goto -> 10
    //   44: aload_3
    //   45: invokevirtual isUpdated : ()Z
    //   48: ifeq -> 38
    //   51: iconst_1
    //   52: istore #4
    //   54: iload #4
    //   56: ireturn
    //   57: iconst_0
    //   58: istore #4
    //   60: goto -> 54
  }
  
  private void initChildrenHelper() {
    this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
          public void addView(View param1View, int param1Int) {
            RecyclerView.this.addView(param1View, param1Int);
            RecyclerView.this.dispatchChildAttached(param1View);
          }
          
          public void attachViewToParent(View param1View, int param1Int, ViewGroup.LayoutParams param1LayoutParams) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null) {
              if (!viewHolder.isTmpDetached() && !viewHolder.shouldIgnore())
                throw new IllegalArgumentException("Called attach on a child which is not detached: " + viewHolder); 
              viewHolder.clearTmpDetachFlag();
            } 
            RecyclerView.this.attachViewToParent(param1View, param1Int, param1LayoutParams);
          }
          
          public void detachViewFromParent(int param1Int) {
            View view = getChildAt(param1Int);
            if (view != null) {
              RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
              if (viewHolder != null) {
                if (viewHolder.isTmpDetached() && !viewHolder.shouldIgnore())
                  throw new IllegalArgumentException("called detach on an already detached child " + viewHolder); 
                viewHolder.addFlags(256);
              } 
            } 
            RecyclerView.this.detachViewFromParent(param1Int);
          }
          
          public View getChildAt(int param1Int) {
            return RecyclerView.this.getChildAt(param1Int);
          }
          
          public int getChildCount() {
            return RecyclerView.this.getChildCount();
          }
          
          public RecyclerView.ViewHolder getChildViewHolder(View param1View) {
            return RecyclerView.getChildViewHolderInt(param1View);
          }
          
          public int indexOfChild(View param1View) {
            return RecyclerView.this.indexOfChild(param1View);
          }
          
          public void onEnteredHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onEnteredHiddenState(RecyclerView.this); 
          }
          
          public void onLeftHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onLeftHiddenState(RecyclerView.this); 
          }
          
          public void removeAllViews() {
            int i = getChildCount();
            for (byte b = 0; b < i; b++)
              RecyclerView.this.dispatchChildDetached(getChildAt(b)); 
            RecyclerView.this.removeAllViews();
          }
          
          public void removeViewAt(int param1Int) {
            View view = RecyclerView.this.getChildAt(param1Int);
            if (view != null)
              RecyclerView.this.dispatchChildDetached(view); 
            RecyclerView.this.removeViewAt(param1Int);
          }
        });
  }
  
  private boolean isPreferredNextFocus(View paramView1, View paramView2, int paramInt) {
    byte b = 0;
    boolean bool1 = true;
    if (paramView2 == null || paramView2 == this)
      return false; 
    boolean bool2 = bool1;
    if (paramView1 != null) {
      if (paramInt == 2 || paramInt == 1) {
        byte b1;
        if (this.mLayout.getLayoutDirection() == 1) {
          b1 = 1;
        } else {
          b1 = 0;
        } 
        if (paramInt == 2)
          b = 1; 
        if ((b ^ b1) != 0) {
          b1 = 66;
        } else {
          b1 = 17;
        } 
        bool2 = bool1;
        if (!isPreferredNextFocusAbsolute(paramView1, paramView2, b1)) {
          if (paramInt == 2)
            return isPreferredNextFocusAbsolute(paramView1, paramView2, 130); 
          bool2 = isPreferredNextFocusAbsolute(paramView1, paramView2, 33);
        } 
        return bool2;
      } 
      bool2 = isPreferredNextFocusAbsolute(paramView1, paramView2, paramInt);
    } 
    return bool2;
  }
  
  private boolean isPreferredNextFocusAbsolute(View paramView1, View paramView2, int paramInt) {
    boolean bool = true;
    this.mTempRect.set(0, 0, paramView1.getWidth(), paramView1.getHeight());
    this.mTempRect2.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
    offsetDescendantRectToMyCoords(paramView1, this.mTempRect);
    offsetDescendantRectToMyCoords(paramView2, this.mTempRect2);
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be absolute. received:" + paramInt);
      case 17:
        if ((this.mTempRect.right <= this.mTempRect2.right && this.mTempRect.left < this.mTempRect2.right) || this.mTempRect.left <= this.mTempRect2.left)
          bool = false; 
        return bool;
      case 66:
        if ((this.mTempRect.left >= this.mTempRect2.left && this.mTempRect.right > this.mTempRect2.left) || this.mTempRect.right >= this.mTempRect2.right)
          bool = false; 
        return bool;
      case 33:
        if ((this.mTempRect.bottom <= this.mTempRect2.bottom && this.mTempRect.top < this.mTempRect2.bottom) || this.mTempRect.top <= this.mTempRect2.top)
          bool = false; 
        return bool;
      case 130:
        break;
    } 
    if ((this.mTempRect.top >= this.mTempRect2.top && this.mTempRect.bottom > this.mTempRect2.top) || this.mTempRect.bottom >= this.mTempRect2.bottom)
      bool = false; 
    return bool;
  }
  
  private void onPointerUp(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (paramMotionEvent.getPointerId(i) == this.mScrollPointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mScrollPointerId = paramMotionEvent.getPointerId(i);
      int j = (int)(paramMotionEvent.getX(i) + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      i = (int)(paramMotionEvent.getY(i) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
    } 
  }
  
  private boolean predictiveItemAnimationsEnabled() {
    return (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations());
  }
  
  private void processAdapterUpdatesAndSetAnimationFlags() {
    boolean bool2;
    boolean bool3;
    boolean bool1 = true;
    if (this.mDataSetHasChangedAfterLayout) {
      this.mAdapterHelper.reset();
      this.mLayout.onItemsChanged(this);
    } 
    if (predictiveItemAnimationsEnabled()) {
      this.mAdapterHelper.preProcess();
    } else {
      this.mAdapterHelper.consumeUpdatesInOnePass();
    } 
    if (this.mItemsAddedOrRemoved || this.mItemsChanged) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    State state = this.mState;
    if (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || bool2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds())) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    state.mRunSimpleAnimations = bool3;
    state = this.mState;
    if (this.mState.mRunSimpleAnimations && bool2 && !this.mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled()) {
      bool3 = bool1;
    } else {
      bool3 = false;
    } 
    state.mRunPredictiveAnimations = bool3;
  }
  
  private void pullGlows(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    boolean bool2;
    boolean bool1 = false;
    if (paramFloat2 < 0.0F) {
      ensureLeftGlow();
      bool2 = bool1;
      if (this.mLeftGlow.onPull(-paramFloat2 / getWidth(), 1.0F - paramFloat3 / getHeight()))
        bool2 = true; 
    } else {
      bool2 = bool1;
      if (paramFloat2 > 0.0F) {
        ensureRightGlow();
        bool2 = bool1;
        if (this.mRightGlow.onPull(paramFloat2 / getWidth(), paramFloat3 / getHeight()))
          bool2 = true; 
      } 
    } 
    if (paramFloat4 < 0.0F) {
      ensureTopGlow();
      bool1 = bool2;
      if (this.mTopGlow.onPull(-paramFloat4 / getHeight(), paramFloat1 / getWidth()))
        bool1 = true; 
    } else {
      bool1 = bool2;
      if (paramFloat4 > 0.0F) {
        ensureBottomGlow();
        bool1 = bool2;
        if (this.mBottomGlow.onPull(paramFloat4 / getHeight(), 1.0F - paramFloat1 / getWidth()))
          bool1 = true; 
      } 
    } 
    if (bool1 || paramFloat2 != 0.0F || paramFloat4 != 0.0F)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  private void recoverFocusFromState() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPreserveFocusAfterLayout : Z
    //   4: ifeq -> 48
    //   7: aload_0
    //   8: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
    //   11: ifnull -> 48
    //   14: aload_0
    //   15: invokevirtual hasFocus : ()Z
    //   18: ifeq -> 48
    //   21: aload_0
    //   22: invokevirtual getDescendantFocusability : ()I
    //   25: ldc_w 393216
    //   28: if_icmpeq -> 48
    //   31: aload_0
    //   32: invokevirtual getDescendantFocusability : ()I
    //   35: ldc_w 131072
    //   38: if_icmpne -> 49
    //   41: aload_0
    //   42: invokevirtual isFocused : ()Z
    //   45: ifeq -> 49
    //   48: return
    //   49: aload_0
    //   50: invokevirtual isFocused : ()Z
    //   53: ifne -> 87
    //   56: aload_0
    //   57: invokevirtual getFocusedChild : ()Landroid/view/View;
    //   60: astore_1
    //   61: aload_0
    //   62: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   65: aload_1
    //   66: invokevirtual isHidden : (Landroid/view/View;)Z
    //   69: ifne -> 87
    //   72: aload_1
    //   73: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   76: aload_0
    //   77: if_acmpne -> 87
    //   80: aload_1
    //   81: invokevirtual hasFocus : ()Z
    //   84: ifne -> 48
    //   87: aconst_null
    //   88: astore_2
    //   89: aload_2
    //   90: astore_1
    //   91: aload_0
    //   92: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
    //   95: getfield mFocusedItemId : J
    //   98: ldc2_w -1
    //   101: lcmp
    //   102: ifeq -> 129
    //   105: aload_2
    //   106: astore_1
    //   107: aload_0
    //   108: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
    //   111: invokevirtual hasStableIds : ()Z
    //   114: ifeq -> 129
    //   117: aload_0
    //   118: aload_0
    //   119: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
    //   122: getfield mFocusedItemId : J
    //   125: invokevirtual findViewHolderForItemId : (J)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   128: astore_1
    //   129: aconst_null
    //   130: astore_2
    //   131: aload_1
    //   132: ifnull -> 159
    //   135: aload_0
    //   136: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   139: aload_1
    //   140: getfield itemView : Landroid/view/View;
    //   143: invokevirtual isHidden : (Landroid/view/View;)Z
    //   146: ifne -> 159
    //   149: aload_1
    //   150: getfield itemView : Landroid/view/View;
    //   153: invokevirtual hasFocusable : ()Z
    //   156: ifne -> 234
    //   159: aload_2
    //   160: astore_1
    //   161: aload_0
    //   162: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   165: invokevirtual getChildCount : ()I
    //   168: ifle -> 176
    //   171: aload_0
    //   172: invokespecial findNextViewToFocus : ()Landroid/view/View;
    //   175: astore_1
    //   176: aload_1
    //   177: ifnull -> 48
    //   180: aload_1
    //   181: astore_2
    //   182: aload_0
    //   183: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
    //   186: getfield mFocusedSubChildId : I
    //   189: i2l
    //   190: ldc2_w -1
    //   193: lcmp
    //   194: ifeq -> 226
    //   197: aload_1
    //   198: aload_0
    //   199: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
    //   202: getfield mFocusedSubChildId : I
    //   205: invokevirtual findViewById : (I)Landroid/view/View;
    //   208: astore_3
    //   209: aload_1
    //   210: astore_2
    //   211: aload_3
    //   212: ifnull -> 226
    //   215: aload_1
    //   216: astore_2
    //   217: aload_3
    //   218: invokevirtual isFocusable : ()Z
    //   221: ifeq -> 226
    //   224: aload_3
    //   225: astore_2
    //   226: aload_2
    //   227: invokevirtual requestFocus : ()Z
    //   230: pop
    //   231: goto -> 48
    //   234: aload_1
    //   235: getfield itemView : Landroid/view/View;
    //   238: astore_1
    //   239: goto -> 176
  }
  
  private void releaseGlows() {
    boolean bool1 = false;
    if (this.mLeftGlow != null)
      bool1 = this.mLeftGlow.onRelease(); 
    boolean bool2 = bool1;
    if (this.mTopGlow != null)
      bool2 = bool1 | this.mTopGlow.onRelease(); 
    bool1 = bool2;
    if (this.mRightGlow != null)
      bool1 = bool2 | this.mRightGlow.onRelease(); 
    bool2 = bool1;
    if (this.mBottomGlow != null)
      bool2 = bool1 | this.mBottomGlow.onRelease(); 
    if (bool2)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  private void resetFocusInfo() {
    this.mState.mFocusedItemId = -1L;
    this.mState.mFocusedItemPosition = -1;
    this.mState.mFocusedSubChildId = -1;
  }
  
  private void resetTouch() {
    if (this.mVelocityTracker != null)
      this.mVelocityTracker.clear(); 
    stopNestedScroll();
    releaseGlows();
  }
  
  private void saveFocusInfo() {
    ViewHolder viewHolder;
    long l;
    int i;
    View view1 = null;
    View view2 = view1;
    if (this.mPreserveFocusAfterLayout) {
      view2 = view1;
      if (hasFocus()) {
        view2 = view1;
        if (this.mAdapter != null)
          view2 = getFocusedChild(); 
      } 
    } 
    if (view2 == null) {
      view2 = null;
    } else {
      viewHolder = findContainingViewHolder(view2);
    } 
    if (viewHolder == null) {
      resetFocusInfo();
      return;
    } 
    State state = this.mState;
    if (this.mAdapter.hasStableIds()) {
      l = viewHolder.getItemId();
    } else {
      l = -1L;
    } 
    state.mFocusedItemId = l;
    state = this.mState;
    if (this.mDataSetHasChangedAfterLayout) {
      i = -1;
    } else if (viewHolder.isRemoved()) {
      i = viewHolder.mOldPosition;
    } else {
      i = viewHolder.getAdapterPosition();
    } 
    state.mFocusedItemPosition = i;
    this.mState.mFocusedSubChildId = getDeepestFocusedViewWithId(viewHolder.itemView);
  }
  
  private void setAdapterInternal(Adapter paramAdapter, boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mAdapter != null) {
      this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
      this.mAdapter.onDetachedFromRecyclerView(this);
    } 
    if (!paramBoolean1 || paramBoolean2)
      removeAndRecycleViews(); 
    this.mAdapterHelper.reset();
    Adapter adapter = this.mAdapter;
    this.mAdapter = paramAdapter;
    if (paramAdapter != null) {
      paramAdapter.registerAdapterDataObserver(this.mObserver);
      paramAdapter.onAttachedToRecyclerView(this);
    } 
    if (this.mLayout != null)
      this.mLayout.onAdapterChanged(adapter, this.mAdapter); 
    this.mRecycler.onAdapterChanged(adapter, this.mAdapter, paramBoolean1);
    this.mState.mStructureChanged = true;
    markKnownViewsInvalid();
  }
  
  private void stopScrollersInternal() {
    this.mViewFlinger.stop();
    if (this.mLayout != null)
      this.mLayout.stopSmoothScroller(); 
  }
  
  void absorbGlows(int paramInt1, int paramInt2) {
    if (paramInt1 < 0) {
      ensureLeftGlow();
      this.mLeftGlow.onAbsorb(-paramInt1);
    } else if (paramInt1 > 0) {
      ensureRightGlow();
      this.mRightGlow.onAbsorb(paramInt1);
    } 
    if (paramInt2 < 0) {
      ensureTopGlow();
      this.mTopGlow.onAbsorb(-paramInt2);
    } else if (paramInt2 > 0) {
      ensureBottomGlow();
      this.mBottomGlow.onAbsorb(paramInt2);
    } 
    if (paramInt1 != 0 || paramInt2 != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    if (this.mLayout == null || !this.mLayout.onAddFocusables(this, paramArrayList, paramInt1, paramInt2))
      super.addFocusables(paramArrayList, paramInt1, paramInt2); 
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration) {
    addItemDecoration(paramItemDecoration, -1);
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration, int paramInt) {
    if (this.mLayout != null)
      this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout"); 
    if (this.mItemDecorations.isEmpty())
      setWillNotDraw(false); 
    if (paramInt < 0) {
      this.mItemDecorations.add(paramItemDecoration);
    } else {
      this.mItemDecorations.add(paramInt, paramItemDecoration);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    if (this.mOnChildAttachStateListeners == null)
      this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>(); 
    this.mOnChildAttachStateListeners.add(paramOnChildAttachStateChangeListener);
  }
  
  public void addOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.add(paramOnItemTouchListener);
  }
  
  public void addOnScrollListener(OnScrollListener paramOnScrollListener) {
    if (this.mScrollListeners == null)
      this.mScrollListeners = new ArrayList<OnScrollListener>(); 
    this.mScrollListeners.add(paramOnScrollListener);
  }
  
  void animateAppearance(@NonNull ViewHolder paramViewHolder, @Nullable ItemAnimator.ItemHolderInfo paramItemHolderInfo1, @NonNull ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateAppearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void animateDisappearance(@NonNull ViewHolder paramViewHolder, @NonNull ItemAnimator.ItemHolderInfo paramItemHolderInfo1, @Nullable ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    addAnimatingView(paramViewHolder);
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateDisappearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void assertInLayoutOrScroll(String paramString) {
    if (!isComputingLayout()) {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling"); 
      throw new IllegalStateException(paramString);
    } 
  }
  
  void assertNotInLayoutOrScroll(String paramString) {
    if (isComputingLayout()) {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling"); 
      throw new IllegalStateException(paramString);
    } 
    if (this.mDispatchScrollCounter > 0)
      Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data. Any method call that might change the structure of the RecyclerView or the adapter contents should be postponed to the next frame.", new IllegalStateException("")); 
  }
  
  boolean canReuseUpdatedViewHolder(ViewHolder paramViewHolder) {
    return (this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(paramViewHolder, paramViewHolder.getUnmodifiedPayloads()));
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)paramLayoutParams));
  }
  
  void clearOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.clearOldPosition(); 
    } 
    this.mRecycler.clearOldPositions();
  }
  
  public void clearOnChildAttachStateChangeListeners() {
    if (this.mOnChildAttachStateListeners != null)
      this.mOnChildAttachStateListeners.clear(); 
  }
  
  public void clearOnScrollListeners() {
    if (this.mScrollListeners != null)
      this.mScrollListeners.clear(); 
  }
  
  public int computeHorizontalScrollExtent() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollOffset() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollRange() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollRange(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollExtent() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollVertically())
      i = this.mLayout.computeVerticalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollOffset() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollVertically())
      i = this.mLayout.computeVerticalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollRange() {
    int i = 0;
    if (this.mLayout != null && this.mLayout.canScrollVertically())
      i = this.mLayout.computeVerticalScrollRange(this.mState); 
    return i;
  }
  
  void considerReleasingGlowsOnScroll(int paramInt1, int paramInt2) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.mLeftGlow != null) {
      bool2 = bool1;
      if (!this.mLeftGlow.isFinished()) {
        bool2 = bool1;
        if (paramInt1 > 0)
          bool2 = this.mLeftGlow.onRelease(); 
      } 
    } 
    bool1 = bool2;
    if (this.mRightGlow != null) {
      bool1 = bool2;
      if (!this.mRightGlow.isFinished()) {
        bool1 = bool2;
        if (paramInt1 < 0)
          bool1 = bool2 | this.mRightGlow.onRelease(); 
      } 
    } 
    bool2 = bool1;
    if (this.mTopGlow != null) {
      bool2 = bool1;
      if (!this.mTopGlow.isFinished()) {
        bool2 = bool1;
        if (paramInt2 > 0)
          bool2 = bool1 | this.mTopGlow.onRelease(); 
      } 
    } 
    bool1 = bool2;
    if (this.mBottomGlow != null) {
      bool1 = bool2;
      if (!this.mBottomGlow.isFinished()) {
        bool1 = bool2;
        if (paramInt2 < 0)
          bool1 = bool2 | this.mBottomGlow.onRelease(); 
      } 
    } 
    if (bool1)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  void consumePendingUpdateOperations() {
    if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
      TraceCompat.beginSection("RV FullInvalidate");
      dispatchLayout();
      TraceCompat.endSection();
      return;
    } 
    if (this.mAdapterHelper.hasPendingUpdates()) {
      if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
        TraceCompat.beginSection("RV PartialInvalidate");
        eatRequestLayout();
        onEnterLayoutOrScroll();
        this.mAdapterHelper.preProcess();
        if (!this.mLayoutRequestEaten)
          if (hasUpdatedView()) {
            dispatchLayout();
          } else {
            this.mAdapterHelper.consumePostponedUpdates();
          }  
        resumeRequestLayout(true);
        onExitLayoutOrScroll();
        TraceCompat.endSection();
        return;
      } 
      if (this.mAdapterHelper.hasPendingUpdates()) {
        TraceCompat.beginSection("RV FullInvalidate");
        dispatchLayout();
        TraceCompat.endSection();
      } 
    } 
  }
  
  void defaultOnMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(LayoutManager.chooseSize(paramInt1, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(paramInt2, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
  }
  
  void dispatchChildAttached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildAttachedToWindow(paramView);
    if (this.mAdapter != null && viewHolder != null)
      this.mAdapter.onViewAttachedToWindow(viewHolder); 
    if (this.mOnChildAttachStateListeners != null)
      for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(paramView);  
  }
  
  void dispatchChildDetached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildDetachedFromWindow(paramView);
    if (this.mAdapter != null && viewHolder != null)
      this.mAdapter.onViewDetachedFromWindow(viewHolder); 
    if (this.mOnChildAttachStateListeners != null)
      for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(paramView);  
  }
  
  void dispatchLayout() {
    if (this.mAdapter == null) {
      Log.e("RecyclerView", "No adapter attached; skipping layout");
      return;
    } 
    if (this.mLayout == null) {
      Log.e("RecyclerView", "No layout manager attached; skipping layout");
      return;
    } 
    this.mState.mIsMeasuring = false;
    if (this.mState.mLayoutStep == 1) {
      dispatchLayoutStep1();
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else if (this.mAdapterHelper.hasUpdates() || this.mLayout.getWidth() != getWidth() || this.mLayout.getHeight() != getHeight()) {
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else {
      this.mLayout.setExactMeasureSpecsFrom(this);
    } 
    dispatchLayoutStep3();
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return getScrollingChildHelper().dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return getScrollingChildHelper().dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return getScrollingChildHelper().dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  void dispatchOnScrollStateChanged(int paramInt) {
    if (this.mLayout != null)
      this.mLayout.onScrollStateChanged(paramInt); 
    onScrollStateChanged(paramInt);
    if (this.mScrollListener != null)
      this.mScrollListener.onScrollStateChanged(this, paramInt); 
    if (this.mScrollListeners != null)
      for (int i = this.mScrollListeners.size() - 1; i >= 0; i--)
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrollStateChanged(this, paramInt);  
  }
  
  void dispatchOnScrolled(int paramInt1, int paramInt2) {
    this.mDispatchScrollCounter++;
    int i = getScrollX();
    int j = getScrollY();
    onScrollChanged(i, j, i, j);
    onScrolled(paramInt1, paramInt2);
    if (this.mScrollListener != null)
      this.mScrollListener.onScrolled(this, paramInt1, paramInt2); 
    if (this.mScrollListeners != null)
      for (j = this.mScrollListeners.size() - 1; j >= 0; j--)
        ((OnScrollListener)this.mScrollListeners.get(j)).onScrolled(this, paramInt1, paramInt2);  
    this.mDispatchScrollCounter--;
  }
  
  void dispatchPendingImportantForAccessibilityChanges() {
    for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--) {
      ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
      if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore()) {
        int j = viewHolder.mPendingAccessibilityState;
        if (j != -1) {
          ViewCompat.setImportantForAccessibility(viewHolder.itemView, j);
          viewHolder.mPendingAccessibilityState = -1;
        } 
      } 
    } 
    this.mPendingAccessibilityImportanceChange.clear();
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
  
  public void draw(Canvas paramCanvas) {
    boolean bool = true;
    super.draw(paramCanvas);
    int i = this.mItemDecorations.size();
    int j;
    for (j = 0; j < i; j++)
      ((ItemDecoration)this.mItemDecorations.get(j)).onDrawOver(paramCanvas, this, this.mState); 
    j = 0;
    i = j;
    if (this.mLeftGlow != null) {
      i = j;
      if (!this.mLeftGlow.isFinished()) {
        int k = paramCanvas.save();
        if (this.mClipToPadding) {
          j = getPaddingBottom();
        } else {
          j = 0;
        } 
        paramCanvas.rotate(270.0F);
        paramCanvas.translate((-getHeight() + j), 0.0F);
        if (this.mLeftGlow != null && this.mLeftGlow.draw(paramCanvas)) {
          i = 1;
        } else {
          i = 0;
        } 
        paramCanvas.restoreToCount(k);
      } 
    } 
    j = i;
    if (this.mTopGlow != null) {
      j = i;
      if (!this.mTopGlow.isFinished()) {
        int k = paramCanvas.save();
        if (this.mClipToPadding)
          paramCanvas.translate(getPaddingLeft(), getPaddingTop()); 
        if (this.mTopGlow != null && this.mTopGlow.draw(paramCanvas)) {
          j = 1;
        } else {
          j = 0;
        } 
        j = i | j;
        paramCanvas.restoreToCount(k);
      } 
    } 
    i = j;
    if (this.mRightGlow != null) {
      i = j;
      if (!this.mRightGlow.isFinished()) {
        int k = paramCanvas.save();
        int m = getWidth();
        if (this.mClipToPadding) {
          i = getPaddingTop();
        } else {
          i = 0;
        } 
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-i, -m);
        if (this.mRightGlow != null && this.mRightGlow.draw(paramCanvas)) {
          i = 1;
        } else {
          i = 0;
        } 
        i = j | i;
        paramCanvas.restoreToCount(k);
      } 
    } 
    j = i;
    if (this.mBottomGlow != null) {
      j = i;
      if (!this.mBottomGlow.isFinished()) {
        int k = paramCanvas.save();
        paramCanvas.rotate(180.0F);
        if (this.mClipToPadding) {
          paramCanvas.translate((-getWidth() + getPaddingRight()), (-getHeight() + getPaddingBottom()));
        } else {
          paramCanvas.translate(-getWidth(), -getHeight());
        } 
        if (this.mBottomGlow != null && this.mBottomGlow.draw(paramCanvas)) {
          j = bool;
        } else {
          j = 0;
        } 
        j = i | j;
        paramCanvas.restoreToCount(k);
      } 
    } 
    i = j;
    if (j == 0) {
      i = j;
      if (this.mItemAnimator != null) {
        i = j;
        if (this.mItemDecorations.size() > 0) {
          i = j;
          if (this.mItemAnimator.isRunning())
            i = 1; 
        } 
      } 
    } 
    if (i != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  void eatRequestLayout() {
    this.mEatRequestLayout++;
    if (this.mEatRequestLayout == 1 && !this.mLayoutFrozen)
      this.mLayoutRequestEaten = false; 
  }
  
  void ensureBottomGlow() {
    if (this.mBottomGlow == null) {
      this.mBottomGlow = new EdgeEffectCompat(getContext());
      if (this.mClipToPadding) {
        this.mBottomGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
        return;
      } 
      this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  void ensureLeftGlow() {
    if (this.mLeftGlow == null) {
      this.mLeftGlow = new EdgeEffectCompat(getContext());
      if (this.mClipToPadding) {
        this.mLeftGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        return;
      } 
      this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureRightGlow() {
    if (this.mRightGlow == null) {
      this.mRightGlow = new EdgeEffectCompat(getContext());
      if (this.mClipToPadding) {
        this.mRightGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        return;
      } 
      this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureTopGlow() {
    if (this.mTopGlow == null) {
      this.mTopGlow = new EdgeEffectCompat(getContext());
      if (this.mClipToPadding) {
        this.mTopGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
        return;
      } 
      this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  public View findChildViewUnder(float paramFloat1, float paramFloat2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   4: invokevirtual getChildCount : ()I
    //   7: iconst_1
    //   8: isub
    //   9: istore_3
    //   10: iload_3
    //   11: iflt -> 103
    //   14: aload_0
    //   15: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   18: iload_3
    //   19: invokevirtual getChildAt : (I)Landroid/view/View;
    //   22: astore #4
    //   24: aload #4
    //   26: invokestatic getTranslationX : (Landroid/view/View;)F
    //   29: fstore #5
    //   31: aload #4
    //   33: invokestatic getTranslationY : (Landroid/view/View;)F
    //   36: fstore #6
    //   38: fload_1
    //   39: aload #4
    //   41: invokevirtual getLeft : ()I
    //   44: i2f
    //   45: fload #5
    //   47: fadd
    //   48: fcmpl
    //   49: iflt -> 97
    //   52: fload_1
    //   53: aload #4
    //   55: invokevirtual getRight : ()I
    //   58: i2f
    //   59: fload #5
    //   61: fadd
    //   62: fcmpg
    //   63: ifgt -> 97
    //   66: fload_2
    //   67: aload #4
    //   69: invokevirtual getTop : ()I
    //   72: i2f
    //   73: fload #6
    //   75: fadd
    //   76: fcmpl
    //   77: iflt -> 97
    //   80: fload_2
    //   81: aload #4
    //   83: invokevirtual getBottom : ()I
    //   86: i2f
    //   87: fload #6
    //   89: fadd
    //   90: fcmpg
    //   91: ifgt -> 97
    //   94: aload #4
    //   96: areturn
    //   97: iinc #3, -1
    //   100: goto -> 10
    //   103: aconst_null
    //   104: astore #4
    //   106: goto -> 94
  }
  
  @Nullable
  public View findContainingItemView(View paramView) {
    ViewParent viewParent;
    for (viewParent = paramView.getParent(); viewParent != null && viewParent != this && viewParent instanceof View; viewParent = paramView.getParent())
      paramView = (View)viewParent; 
    if (viewParent != this)
      paramView = null; 
    return paramView;
  }
  
  @Nullable
  public ViewHolder findContainingViewHolder(View paramView) {
    paramView = findContainingItemView(paramView);
    return (paramView == null) ? null : getChildViewHolder(paramView);
  }
  
  public ViewHolder findViewHolderForAdapterPosition(int paramInt) {
    if (this.mDataSetHasChangedAfterLayout)
      return null; 
    int i = this.mChildHelper.getUnfilteredChildCount();
    ViewHolder viewHolder = null;
    byte b = 0;
    while (true) {
      if (b < i) {
        ViewHolder viewHolder1 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
        ViewHolder viewHolder2 = viewHolder;
        if (viewHolder1 != null) {
          viewHolder2 = viewHolder;
          if (!viewHolder1.isRemoved()) {
            viewHolder2 = viewHolder;
            if (getAdapterPositionFor(viewHolder1) == paramInt) {
              viewHolder = viewHolder1;
              if (this.mChildHelper.isHidden(viewHolder1.itemView)) {
                viewHolder2 = viewHolder1;
              } else {
                return viewHolder;
              } 
            } 
          } 
        } 
        b++;
        viewHolder = viewHolder2;
        continue;
      } 
      return viewHolder;
    } 
  }
  
  public ViewHolder findViewHolderForItemId(long paramLong) {
    if (this.mAdapter == null || !this.mAdapter.hasStableIds())
      return null; 
    int i = this.mChildHelper.getUnfilteredChildCount();
    ViewHolder viewHolder = null;
    byte b = 0;
    while (true) {
      if (b < i) {
        ViewHolder viewHolder1 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
        ViewHolder viewHolder2 = viewHolder;
        if (viewHolder1 != null) {
          viewHolder2 = viewHolder;
          if (!viewHolder1.isRemoved()) {
            viewHolder2 = viewHolder;
            if (viewHolder1.getItemId() == paramLong) {
              viewHolder = viewHolder1;
              if (this.mChildHelper.isHidden(viewHolder1.itemView)) {
                viewHolder2 = viewHolder1;
              } else {
                return viewHolder;
              } 
            } 
          } 
        } 
        b++;
        viewHolder = viewHolder2;
        continue;
      } 
      return viewHolder;
    } 
  }
  
  public ViewHolder findViewHolderForLayoutPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  @Deprecated
  public ViewHolder findViewHolderForPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  ViewHolder findViewHolderForPosition(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   4: invokevirtual getUnfilteredChildCount : ()I
    //   7: istore_3
    //   8: aconst_null
    //   9: astore #4
    //   11: iconst_0
    //   12: istore #5
    //   14: iload #5
    //   16: iload_3
    //   17: if_icmpge -> 121
    //   20: aload_0
    //   21: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   24: iload #5
    //   26: invokevirtual getUnfilteredChildAt : (I)Landroid/view/View;
    //   29: invokestatic getChildViewHolderInt : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   32: astore #6
    //   34: aload #4
    //   36: astore #7
    //   38: aload #6
    //   40: ifnull -> 72
    //   43: aload #4
    //   45: astore #7
    //   47: aload #6
    //   49: invokevirtual isRemoved : ()Z
    //   52: ifne -> 72
    //   55: iload_2
    //   56: ifeq -> 82
    //   59: aload #6
    //   61: getfield mPosition : I
    //   64: iload_1
    //   65: if_icmpeq -> 95
    //   68: aload #4
    //   70: astore #7
    //   72: iinc #5, 1
    //   75: aload #7
    //   77: astore #4
    //   79: goto -> 14
    //   82: aload #4
    //   84: astore #7
    //   86: aload #6
    //   88: invokevirtual getLayoutPosition : ()I
    //   91: iload_1
    //   92: if_icmpne -> 72
    //   95: aload #6
    //   97: astore #4
    //   99: aload_0
    //   100: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   103: aload #6
    //   105: getfield itemView : Landroid/view/View;
    //   108: invokevirtual isHidden : (Landroid/view/View;)Z
    //   111: ifeq -> 121
    //   114: aload #6
    //   116: astore #7
    //   118: goto -> 72
    //   121: aload #4
    //   123: areturn
  }
  
  public boolean fling(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   6: ifnonnull -> 24
    //   9: ldc 'RecyclerView'
    //   11: ldc_w 'Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.'
    //   14: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   17: pop
    //   18: iload_3
    //   19: istore #4
    //   21: iload #4
    //   23: ireturn
    //   24: iload_3
    //   25: istore #4
    //   27: aload_0
    //   28: getfield mLayoutFrozen : Z
    //   31: ifne -> 21
    //   34: aload_0
    //   35: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   38: invokevirtual canScrollHorizontally : ()Z
    //   41: istore #5
    //   43: aload_0
    //   44: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   47: invokevirtual canScrollVertically : ()Z
    //   50: istore #6
    //   52: iload #5
    //   54: ifeq -> 71
    //   57: iload_1
    //   58: istore #7
    //   60: iload_1
    //   61: invokestatic abs : (I)I
    //   64: aload_0
    //   65: getfield mMinFlingVelocity : I
    //   68: if_icmpge -> 74
    //   71: iconst_0
    //   72: istore #7
    //   74: iload #6
    //   76: ifeq -> 92
    //   79: iload_2
    //   80: istore_1
    //   81: iload_2
    //   82: invokestatic abs : (I)I
    //   85: aload_0
    //   86: getfield mMinFlingVelocity : I
    //   89: if_icmpge -> 94
    //   92: iconst_0
    //   93: istore_1
    //   94: iload #7
    //   96: ifne -> 106
    //   99: iload_3
    //   100: istore #4
    //   102: iload_1
    //   103: ifeq -> 21
    //   106: iload_3
    //   107: istore #4
    //   109: aload_0
    //   110: iload #7
    //   112: i2f
    //   113: iload_1
    //   114: i2f
    //   115: invokevirtual dispatchNestedPreFling : (FF)Z
    //   118: ifne -> 21
    //   121: iload #5
    //   123: ifne -> 131
    //   126: iload #6
    //   128: ifeq -> 172
    //   131: iconst_1
    //   132: istore #6
    //   134: aload_0
    //   135: iload #7
    //   137: i2f
    //   138: iload_1
    //   139: i2f
    //   140: iload #6
    //   142: invokevirtual dispatchNestedFling : (FFZ)Z
    //   145: pop
    //   146: aload_0
    //   147: getfield mOnFlingListener : Landroid/support/v7/widget/RecyclerView$OnFlingListener;
    //   150: ifnull -> 178
    //   153: aload_0
    //   154: getfield mOnFlingListener : Landroid/support/v7/widget/RecyclerView$OnFlingListener;
    //   157: iload #7
    //   159: iload_1
    //   160: invokevirtual onFling : (II)Z
    //   163: ifeq -> 178
    //   166: iconst_1
    //   167: istore #4
    //   169: goto -> 21
    //   172: iconst_0
    //   173: istore #6
    //   175: goto -> 134
    //   178: iload_3
    //   179: istore #4
    //   181: iload #6
    //   183: ifeq -> 21
    //   186: aload_0
    //   187: getfield mMaxFlingVelocity : I
    //   190: ineg
    //   191: iload #7
    //   193: aload_0
    //   194: getfield mMaxFlingVelocity : I
    //   197: invokestatic min : (II)I
    //   200: invokestatic max : (II)I
    //   203: istore_2
    //   204: aload_0
    //   205: getfield mMaxFlingVelocity : I
    //   208: ineg
    //   209: iload_1
    //   210: aload_0
    //   211: getfield mMaxFlingVelocity : I
    //   214: invokestatic min : (II)I
    //   217: invokestatic max : (II)I
    //   220: istore_1
    //   221: aload_0
    //   222: getfield mViewFlinger : Landroid/support/v7/widget/RecyclerView$ViewFlinger;
    //   225: iload_2
    //   226: iload_1
    //   227: invokevirtual fling : (II)V
    //   230: iconst_1
    //   231: istore #4
    //   233: goto -> 21
  }
  
  public View focusSearch(View paramView, int paramInt) {
    View view1;
    int i;
    int j;
    View view2 = this.mLayout.onInterceptFocusSearch(paramView, paramInt);
    if (view2 != null)
      return view2; 
    if (this.mAdapter != null && this.mLayout != null && !isComputingLayout() && !this.mLayoutFrozen) {
      i = 1;
    } else {
      i = 0;
    } 
    FocusFinder focusFinder = FocusFinder.getInstance();
    if (i && (paramInt == 2 || paramInt == 1)) {
      int k = 0;
      i = paramInt;
      if (this.mLayout.canScrollVertically()) {
        byte b1;
        byte b2;
        if (paramInt == 2) {
          b1 = 130;
        } else {
          b1 = 33;
        } 
        if (focusFinder.findNextFocus(this, paramView, b1) == null) {
          b2 = 1;
        } else {
          b2 = 0;
        } 
        k = b2;
        i = paramInt;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
          i = b1;
          k = b2;
        } 
      } 
      int m = k;
      j = i;
      if (!k) {
        m = k;
        j = i;
        if (this.mLayout.canScrollHorizontally()) {
          if (this.mLayout.getLayoutDirection() == 1) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (i == 2) {
            k = 1;
          } else {
            k = 0;
          } 
          if ((k ^ paramInt) != 0) {
            paramInt = 66;
          } else {
            paramInt = 17;
          } 
          if (focusFinder.findNextFocus(this, paramView, paramInt) == null) {
            k = 1;
          } else {
            k = 0;
          } 
          m = k;
          j = i;
          if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
            j = paramInt;
            m = k;
          } 
        } 
      } 
      if (m != 0) {
        consumePendingUpdateOperations();
        if (findContainingItemView(paramView) == null)
          return null; 
        eatRequestLayout();
        this.mLayout.onFocusSearchFailed(paramView, j, this.mRecycler, this.mState);
        resumeRequestLayout(false);
      } 
      view1 = focusFinder.findNextFocus(this, paramView, j);
    } else {
      View view = view1.findNextFocus(this, paramView, paramInt);
      view1 = view;
      j = paramInt;
      if (view == null) {
        view1 = view;
        j = paramInt;
        if (i != 0) {
          consumePendingUpdateOperations();
          if (findContainingItemView(paramView) == null)
            return null; 
          eatRequestLayout();
          view1 = this.mLayout.onFocusSearchFailed(paramView, paramInt, this.mRecycler, this.mState);
          resumeRequestLayout(false);
          j = paramInt;
        } 
      } 
    } 
    return isPreferredNextFocus(paramView, view1, j) ? view1 : super.focusSearch(paramView, j);
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager"); 
    return (ViewGroup.LayoutParams)this.mLayout.generateDefaultLayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager"); 
    return (ViewGroup.LayoutParams)this.mLayout.generateLayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager"); 
    return (ViewGroup.LayoutParams)this.mLayout.generateLayoutParams(paramLayoutParams);
  }
  
  public Adapter getAdapter() {
    return this.mAdapter;
  }
  
  int getAdapterPositionFor(ViewHolder paramViewHolder) {
    return (paramViewHolder.hasAnyOfTheFlags(524) || !paramViewHolder.isBound()) ? -1 : this.mAdapterHelper.applyPendingUpdatesToPosition(paramViewHolder.mPosition);
  }
  
  public int getBaseline() {
    return (this.mLayout != null) ? this.mLayout.getBaseline() : super.getBaseline();
  }
  
  long getChangedHolderKey(ViewHolder paramViewHolder) {
    return this.mAdapter.hasStableIds() ? paramViewHolder.getItemId() : paramViewHolder.mPosition;
  }
  
  public int getChildAdapterPosition(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    return (viewHolder != null) ? viewHolder.getAdapterPosition() : -1;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    return (this.mChildDrawingOrderCallback == null) ? super.getChildDrawingOrder(paramInt1, paramInt2) : this.mChildDrawingOrderCallback.onGetChildDrawingOrder(paramInt1, paramInt2);
  }
  
  public long getChildItemId(View paramView) {
    long l1 = -1L;
    long l2 = l1;
    if (this.mAdapter != null) {
      if (!this.mAdapter.hasStableIds())
        return l1; 
    } else {
      return l2;
    } 
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    l2 = l1;
    if (viewHolder != null)
      l2 = viewHolder.getItemId(); 
    return l2;
  }
  
  public int getChildLayoutPosition(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    return (viewHolder != null) ? viewHolder.getLayoutPosition() : -1;
  }
  
  @Deprecated
  public int getChildPosition(View paramView) {
    return getChildAdapterPosition(paramView);
  }
  
  public ViewHolder getChildViewHolder(View paramView) {
    ViewParent viewParent = paramView.getParent();
    if (viewParent != null && viewParent != this)
      throw new IllegalArgumentException("View " + paramView + " is not a direct child of " + this); 
    return getChildViewHolderInt(paramView);
  }
  
  public boolean getClipToPadding() {
    return this.mClipToPadding;
  }
  
  public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
    return this.mAccessibilityDelegate;
  }
  
  public void getDecoratedBoundsWithMargins(View paramView, Rect paramRect) {
    getDecoratedBoundsWithMarginsInt(paramView, paramRect);
  }
  
  public ItemAnimator getItemAnimator() {
    return this.mItemAnimator;
  }
  
  Rect getItemDecorInsetsForChild(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mInsetsDirty)
      return layoutParams.mDecorInsets; 
    if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid()))
      return layoutParams.mDecorInsets; 
    Rect rect = layoutParams.mDecorInsets;
    rect.set(0, 0, 0, 0);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++) {
      this.mTempRect.set(0, 0, 0, 0);
      ((ItemDecoration)this.mItemDecorations.get(b)).getItemOffsets(this.mTempRect, paramView, this, this.mState);
      rect.left += this.mTempRect.left;
      rect.top += this.mTempRect.top;
      rect.right += this.mTempRect.right;
      rect.bottom += this.mTempRect.bottom;
    } 
    layoutParams.mInsetsDirty = false;
    return rect;
  }
  
  public LayoutManager getLayoutManager() {
    return this.mLayout;
  }
  
  public int getMaxFlingVelocity() {
    return this.mMaxFlingVelocity;
  }
  
  public int getMinFlingVelocity() {
    return this.mMinFlingVelocity;
  }
  
  long getNanoTime() {
    return ALLOW_THREAD_GAP_WORK ? System.nanoTime() : 0L;
  }
  
  @Nullable
  public OnFlingListener getOnFlingListener() {
    return this.mOnFlingListener;
  }
  
  public boolean getPreserveFocusAfterLayout() {
    return this.mPreserveFocusAfterLayout;
  }
  
  public RecycledViewPool getRecycledViewPool() {
    return this.mRecycler.getRecycledViewPool();
  }
  
  public int getScrollState() {
    return this.mScrollState;
  }
  
  public boolean hasFixedSize() {
    return this.mHasFixedSize;
  }
  
  public boolean hasNestedScrollingParent() {
    return getScrollingChildHelper().hasNestedScrollingParent();
  }
  
  public boolean hasPendingAdapterUpdates() {
    return (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates());
  }
  
  void initAdapterManager() {
    this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
          void dispatchUpdate(AdapterHelper.UpdateOp param1UpdateOp) {
            switch (param1UpdateOp.cmd) {
              default:
                return;
              case 1:
                RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
              case 2:
                RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
              case 4:
                RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, param1UpdateOp.payload);
              case 8:
                break;
            } 
            RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, 1);
          }
          
          public RecyclerView.ViewHolder findViewHolder(int param1Int) {
            RecyclerView.ViewHolder viewHolder1 = RecyclerView.this.findViewHolderForPosition(param1Int, true);
            if (viewHolder1 == null)
              return null; 
            RecyclerView.ViewHolder viewHolder2 = viewHolder1;
            if (RecyclerView.this.mChildHelper.isHidden(viewHolder1.itemView))
              viewHolder2 = null; 
            return viewHolder2;
          }
          
          public void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object) {
            RecyclerView.this.viewRangeUpdate(param1Int1, param1Int2, param1Object);
            RecyclerView.this.mItemsChanged = true;
          }
          
          public void offsetPositionsForAdd(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForInsert(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForMove(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForMove(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, true);
            RecyclerView.this.mItemsAddedOrRemoved = true;
            RecyclerView.State state = RecyclerView.this.mState;
            state.mDeletedInvisibleItemCountSincePreviousLayout += param1Int2;
          }
          
          public void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
          
          public void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
        });
  }
  
  void invalidateGlows() {
    this.mBottomGlow = null;
    this.mTopGlow = null;
    this.mRightGlow = null;
    this.mLeftGlow = null;
  }
  
  public void invalidateItemDecorations() {
    if (this.mItemDecorations.size() != 0) {
      if (this.mLayout != null)
        this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout"); 
      markItemDecorInsetsDirty();
      requestLayout();
    } 
  }
  
  boolean isAccessibilityEnabled() {
    return (this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled());
  }
  
  public boolean isAnimating() {
    return (this.mItemAnimator != null && this.mItemAnimator.isRunning());
  }
  
  public boolean isAttachedToWindow() {
    return this.mIsAttached;
  }
  
  public boolean isComputingLayout() {
    return (this.mLayoutOrScrollCounter > 0);
  }
  
  public boolean isLayoutFrozen() {
    return this.mLayoutFrozen;
  }
  
  public boolean isNestedScrollingEnabled() {
    return getScrollingChildHelper().isNestedScrollingEnabled();
  }
  
  void jumpToPositionForSmoothScroller(int paramInt) {
    if (this.mLayout != null) {
      this.mLayout.scrollToPosition(paramInt);
      awakenScrollBars();
    } 
  }
  
  void markItemDecorInsetsDirty() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++)
      ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(b).getLayoutParams()).mInsetsDirty = true; 
    this.mRecycler.markItemDecorInsetsDirty();
  }
  
  void markKnownViewsInvalid() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        viewHolder.addFlags(6); 
    } 
    markItemDecorInsetsDirty();
    this.mRecycler.markKnownViewsInvalid();
  }
  
  public void offsetChildrenHorizontal(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetLeftAndRight(paramInt); 
  }
  
  public void offsetChildrenVertical(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetTopAndBottom(paramInt); 
  }
  
  void offsetPositionRecordsForInsert(int paramInt1, int paramInt2) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1) {
        viewHolder.offsetPosition(paramInt2, false);
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForInsert(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForMove(int paramInt1, int paramInt2) {
    int j;
    int k;
    boolean bool;
    int i = this.mChildHelper.getUnfilteredChildCount();
    if (paramInt1 < paramInt2) {
      j = paramInt1;
      k = paramInt2;
      bool = true;
    } else {
      j = paramInt2;
      k = paramInt1;
      bool = true;
    } 
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && viewHolder.mPosition >= j && viewHolder.mPosition <= k) {
        if (viewHolder.mPosition == paramInt1) {
          viewHolder.offsetPosition(paramInt2 - paramInt1, false);
        } else {
          viewHolder.offsetPosition(bool, false);
        } 
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForMove(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        if (viewHolder.mPosition >= paramInt1 + paramInt2) {
          viewHolder.offsetPosition(-paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        } else if (viewHolder.mPosition >= paramInt1) {
          viewHolder.flagRemovedAndOffsetPosition(paramInt1 - 1, -paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        }  
    } 
    this.mRecycler.offsetPositionRecordsForRemove(paramInt1, paramInt2, paramBoolean);
    requestLayout();
  }
  
  protected void onAttachedToWindow() {
    boolean bool = true;
    super.onAttachedToWindow();
    this.mLayoutOrScrollCounter = 0;
    this.mIsAttached = true;
    if (!this.mFirstLayoutComplete || isLayoutRequested())
      bool = false; 
    this.mFirstLayoutComplete = bool;
    if (this.mLayout != null)
      this.mLayout.dispatchAttachedToWindow(this); 
    this.mPostedAnimatorRunner = false;
    if (ALLOW_THREAD_GAP_WORK) {
      this.mGapWorker = GapWorker.sGapWorker.get();
      if (this.mGapWorker == null) {
        this.mGapWorker = new GapWorker();
        Display display = ViewCompat.getDisplay((View)this);
        float f1 = 60.0F;
        float f2 = f1;
        if (!isInEditMode()) {
          f2 = f1;
          if (display != null) {
            float f = display.getRefreshRate();
            f2 = f1;
            if (f >= 30.0F)
              f2 = f; 
          } 
        } 
        this.mGapWorker.mFrameIntervalNs = (long)(1.0E9F / f2);
        GapWorker.sGapWorker.set(this.mGapWorker);
      } 
      this.mGapWorker.add(this);
    } 
  }
  
  public void onChildAttachedToWindow(View paramView) {}
  
  public void onChildDetachedFromWindow(View paramView) {}
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mItemAnimator != null)
      this.mItemAnimator.endAnimations(); 
    stopScroll();
    this.mIsAttached = false;
    if (this.mLayout != null)
      this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler); 
    this.mPendingAccessibilityImportanceChange.clear();
    removeCallbacks(this.mItemAnimatorRunner);
    this.mViewInfoStore.onDetach();
    if (ALLOW_THREAD_GAP_WORK) {
      this.mGapWorker.remove(this);
      this.mGapWorker = null;
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++)
      ((ItemDecoration)this.mItemDecorations.get(b)).onDraw(paramCanvas, this, this.mState); 
  }
  
  void onEnterLayoutOrScroll() {
    this.mLayoutOrScrollCounter++;
  }
  
  void onExitLayoutOrScroll() {
    this.mLayoutOrScrollCounter--;
    if (this.mLayoutOrScrollCounter < 1) {
      this.mLayoutOrScrollCounter = 0;
      dispatchContentChangedIfNecessary();
      dispatchPendingImportantForAccessibilityChanges();
    } 
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    if (this.mLayout != null && !this.mLayoutFrozen && (paramMotionEvent.getSource() & 0x2) != 0 && paramMotionEvent.getAction() == 8) {
      float f1;
      float f2;
      if (this.mLayout.canScrollVertically()) {
        f1 = -MotionEventCompat.getAxisValue(paramMotionEvent, 9);
      } else {
        f1 = 0.0F;
      } 
      if (this.mLayout.canScrollHorizontally()) {
        f2 = MotionEventCompat.getAxisValue(paramMotionEvent, 10);
      } else {
        f2 = 0.0F;
      } 
      if (f1 != 0.0F || f2 != 0.0F) {
        float f = getScrollFactor();
        scrollByInternal((int)(f2 * f), (int)(f1 * f), paramMotionEvent);
      } 
    } 
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int[] arrayOfInt;
    if (this.mLayoutFrozen)
      return false; 
    if (dispatchOnItemTouchIntercept(paramMotionEvent)) {
      cancelTouch();
      return true;
    } 
    if (this.mLayout == null)
      return false; 
    boolean bool = this.mLayout.canScrollHorizontally();
    null = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    int j = MotionEventCompat.getActionIndex(paramMotionEvent);
    switch (i) {
      default:
        if (this.mScrollState == 1)
          return true; 
        break;
      case 0:
        if (this.mIgnoreMotionEventTillDown)
          this.mIgnoreMotionEventTillDown = false; 
        this.mScrollPointerId = paramMotionEvent.getPointerId(0);
        j = (int)(paramMotionEvent.getX() + 0.5F);
        this.mLastTouchX = j;
        this.mInitialTouchX = j;
        j = (int)(paramMotionEvent.getY() + 0.5F);
        this.mLastTouchY = j;
        this.mInitialTouchY = j;
        if (this.mScrollState == 2) {
          getParent().requestDisallowInterceptTouchEvent(true);
          setScrollState(1);
        } 
        arrayOfInt = this.mNestedOffsets;
        this.mNestedOffsets[1] = 0;
        arrayOfInt[0] = 0;
        j = 0;
        if (bool)
          j = false | true; 
        i = j;
        if (null)
          i = j | 0x2; 
        startNestedScroll(i);
      case 5:
        this.mScrollPointerId = arrayOfInt.getPointerId(j);
        i = (int)(arrayOfInt.getX(j) + 0.5F);
        this.mLastTouchX = i;
        this.mInitialTouchX = i;
        j = (int)(arrayOfInt.getY(j) + 0.5F);
        this.mLastTouchY = j;
        this.mInitialTouchY = j;
      case 2:
        j = arrayOfInt.findPointerIndex(this.mScrollPointerId);
        if (j < 0) {
          Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
          return false;
        } 
        i = (int)(arrayOfInt.getX(j) + 0.5F);
        j = (int)(arrayOfInt.getY(j) + 0.5F);
        if (this.mScrollState != 1) {
          int k = i - this.mInitialTouchX;
          int m = j - this.mInitialTouchY;
          i = 0;
          j = i;
          if (bool) {
            j = i;
            if (Math.abs(k) > this.mTouchSlop) {
              int n = this.mInitialTouchX;
              i = this.mTouchSlop;
              if (k < 0) {
                j = -1;
              } else {
                j = 1;
              } 
              this.mLastTouchX = j * i + n;
              j = 1;
            } 
          } 
          i = j;
          if (null) {
            i = j;
            if (Math.abs(m) > this.mTouchSlop) {
              i = this.mInitialTouchY;
              int n = this.mTouchSlop;
              if (m < 0) {
                j = -1;
              } else {
                j = 1;
              } 
              this.mLastTouchY = j * n + i;
              i = 1;
            } 
          } 
          if (i != 0)
            setScrollState(1); 
        } 
      case 6:
        onPointerUp((MotionEvent)arrayOfInt);
      case 1:
        this.mVelocityTracker.clear();
        stopNestedScroll();
      case 3:
        cancelTouch();
    } 
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    TraceCompat.beginSection("RV OnLayout");
    dispatchLayout();
    TraceCompat.endSection();
    this.mFirstLayoutComplete = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool = false;
    if (this.mLayout == null) {
      defaultOnMeasure(paramInt1, paramInt2);
      return;
    } 
    if (this.mLayout.mAutoMeasure) {
      int i = View.MeasureSpec.getMode(paramInt1);
      int j = View.MeasureSpec.getMode(paramInt2);
      boolean bool1 = bool;
      if (i == 1073741824) {
        bool1 = bool;
        if (j == 1073741824)
          bool1 = true; 
      } 
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      if (!bool1 && this.mAdapter != null) {
        if (this.mState.mLayoutStep == 1)
          dispatchLayoutStep1(); 
        this.mLayout.setMeasureSpecs(paramInt1, paramInt2);
        this.mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
        if (this.mLayout.shouldMeasureTwice()) {
          this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
          this.mState.mIsMeasuring = true;
          dispatchLayoutStep2();
          this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
        } 
      } 
      return;
    } 
    if (this.mHasFixedSize) {
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      return;
    } 
    if (this.mAdapterUpdateDuringMeasure) {
      eatRequestLayout();
      processAdapterUpdatesAndSetAnimationFlags();
      if (this.mState.mRunPredictiveAnimations) {
        this.mState.mInPreLayout = true;
      } else {
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mInPreLayout = false;
      } 
      this.mAdapterUpdateDuringMeasure = false;
      resumeRequestLayout(false);
    } 
    if (this.mAdapter != null) {
      this.mState.mItemCount = this.mAdapter.getItemCount();
    } else {
      this.mState.mItemCount = 0;
    } 
    eatRequestLayout();
    this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
    resumeRequestLayout(false);
    this.mState.mInPreLayout = false;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    return isComputingLayout() ? false : super.onRequestFocusInDescendants(paramInt, paramRect);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    this.mPendingSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
    if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null)
      this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState); 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (this.mPendingSavedState != null) {
      savedState.copyFrom(this.mPendingSavedState);
      return (Parcelable)savedState;
    } 
    if (this.mLayout != null) {
      savedState.mLayoutState = this.mLayout.onSaveInstanceState();
      return (Parcelable)savedState;
    } 
    savedState.mLayoutState = null;
    return (Parcelable)savedState;
  }
  
  public void onScrollStateChanged(int paramInt) {}
  
  public void onScrolled(int paramInt1, int paramInt2) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3 || paramInt2 != paramInt4)
      invalidateGlows(); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLayoutFrozen : Z
    //   4: ifne -> 14
    //   7: aload_0
    //   8: getfield mIgnoreMotionEventTillDown : Z
    //   11: ifeq -> 18
    //   14: iconst_0
    //   15: istore_2
    //   16: iload_2
    //   17: ireturn
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial dispatchOnItemTouch : (Landroid/view/MotionEvent;)Z
    //   23: ifeq -> 35
    //   26: aload_0
    //   27: invokespecial cancelTouch : ()V
    //   30: iconst_1
    //   31: istore_2
    //   32: goto -> 16
    //   35: aload_0
    //   36: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   39: ifnonnull -> 47
    //   42: iconst_0
    //   43: istore_2
    //   44: goto -> 16
    //   47: aload_0
    //   48: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   51: invokevirtual canScrollHorizontally : ()Z
    //   54: istore_3
    //   55: aload_0
    //   56: getfield mLayout : Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   59: invokevirtual canScrollVertically : ()Z
    //   62: istore_2
    //   63: aload_0
    //   64: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   67: ifnonnull -> 77
    //   70: aload_0
    //   71: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   74: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   77: iconst_0
    //   78: istore #4
    //   80: aload_1
    //   81: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
    //   84: astore #5
    //   86: aload_1
    //   87: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   90: istore #6
    //   92: aload_1
    //   93: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
    //   96: istore #7
    //   98: iload #6
    //   100: ifne -> 121
    //   103: aload_0
    //   104: getfield mNestedOffsets : [I
    //   107: astore #8
    //   109: aload_0
    //   110: getfield mNestedOffsets : [I
    //   113: iconst_1
    //   114: iconst_0
    //   115: iastore
    //   116: aload #8
    //   118: iconst_0
    //   119: iconst_0
    //   120: iastore
    //   121: aload #5
    //   123: aload_0
    //   124: getfield mNestedOffsets : [I
    //   127: iconst_0
    //   128: iaload
    //   129: i2f
    //   130: aload_0
    //   131: getfield mNestedOffsets : [I
    //   134: iconst_1
    //   135: iaload
    //   136: i2f
    //   137: invokevirtual offsetLocation : (FF)V
    //   140: iload #4
    //   142: istore #9
    //   144: iload #6
    //   146: tableswitch default -> 188, 0 -> 216, 1 -> 893, 2 -> 378, 3 -> 1007, 4 -> 192, 5 -> 311, 6 -> 881
    //   188: iload #4
    //   190: istore #9
    //   192: iload #9
    //   194: ifne -> 206
    //   197: aload_0
    //   198: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   201: aload #5
    //   203: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   206: aload #5
    //   208: invokevirtual recycle : ()V
    //   211: iconst_1
    //   212: istore_2
    //   213: goto -> 16
    //   216: aload_0
    //   217: aload_1
    //   218: iconst_0
    //   219: invokevirtual getPointerId : (I)I
    //   222: putfield mScrollPointerId : I
    //   225: aload_1
    //   226: invokevirtual getX : ()F
    //   229: ldc_w 0.5
    //   232: fadd
    //   233: f2i
    //   234: istore #9
    //   236: aload_0
    //   237: iload #9
    //   239: putfield mLastTouchX : I
    //   242: aload_0
    //   243: iload #9
    //   245: putfield mInitialTouchX : I
    //   248: aload_1
    //   249: invokevirtual getY : ()F
    //   252: ldc_w 0.5
    //   255: fadd
    //   256: f2i
    //   257: istore #9
    //   259: aload_0
    //   260: iload #9
    //   262: putfield mLastTouchY : I
    //   265: aload_0
    //   266: iload #9
    //   268: putfield mInitialTouchY : I
    //   271: iconst_0
    //   272: istore #9
    //   274: iload_3
    //   275: ifeq -> 283
    //   278: iconst_0
    //   279: iconst_1
    //   280: ior
    //   281: istore #9
    //   283: iload #9
    //   285: istore #7
    //   287: iload_2
    //   288: ifeq -> 297
    //   291: iload #9
    //   293: iconst_2
    //   294: ior
    //   295: istore #7
    //   297: aload_0
    //   298: iload #7
    //   300: invokevirtual startNestedScroll : (I)Z
    //   303: pop
    //   304: iload #4
    //   306: istore #9
    //   308: goto -> 192
    //   311: aload_0
    //   312: aload_1
    //   313: iload #7
    //   315: invokevirtual getPointerId : (I)I
    //   318: putfield mScrollPointerId : I
    //   321: aload_1
    //   322: iload #7
    //   324: invokevirtual getX : (I)F
    //   327: ldc_w 0.5
    //   330: fadd
    //   331: f2i
    //   332: istore #9
    //   334: aload_0
    //   335: iload #9
    //   337: putfield mLastTouchX : I
    //   340: aload_0
    //   341: iload #9
    //   343: putfield mInitialTouchX : I
    //   346: aload_1
    //   347: iload #7
    //   349: invokevirtual getY : (I)F
    //   352: ldc_w 0.5
    //   355: fadd
    //   356: f2i
    //   357: istore #9
    //   359: aload_0
    //   360: iload #9
    //   362: putfield mLastTouchY : I
    //   365: aload_0
    //   366: iload #9
    //   368: putfield mInitialTouchY : I
    //   371: iload #4
    //   373: istore #9
    //   375: goto -> 192
    //   378: aload_1
    //   379: aload_0
    //   380: getfield mScrollPointerId : I
    //   383: invokevirtual findPointerIndex : (I)I
    //   386: istore #9
    //   388: iload #9
    //   390: ifge -> 433
    //   393: ldc 'RecyclerView'
    //   395: new java/lang/StringBuilder
    //   398: dup
    //   399: invokespecial <init> : ()V
    //   402: ldc_w 'Error processing scroll; pointer index for id '
    //   405: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   408: aload_0
    //   409: getfield mScrollPointerId : I
    //   412: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   415: ldc_w ' not found. Did any MotionEvents get skipped?'
    //   418: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   421: invokevirtual toString : ()Ljava/lang/String;
    //   424: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   427: pop
    //   428: iconst_0
    //   429: istore_2
    //   430: goto -> 16
    //   433: aload_1
    //   434: iload #9
    //   436: invokevirtual getX : (I)F
    //   439: ldc_w 0.5
    //   442: fadd
    //   443: f2i
    //   444: istore #10
    //   446: aload_1
    //   447: iload #9
    //   449: invokevirtual getY : (I)F
    //   452: ldc_w 0.5
    //   455: fadd
    //   456: f2i
    //   457: istore #11
    //   459: aload_0
    //   460: getfield mLastTouchX : I
    //   463: iload #10
    //   465: isub
    //   466: istore #12
    //   468: aload_0
    //   469: getfield mLastTouchY : I
    //   472: iload #11
    //   474: isub
    //   475: istore #6
    //   477: iload #12
    //   479: istore #7
    //   481: iload #6
    //   483: istore #9
    //   485: aload_0
    //   486: iload #12
    //   488: iload #6
    //   490: aload_0
    //   491: getfield mScrollConsumed : [I
    //   494: aload_0
    //   495: getfield mScrollOffset : [I
    //   498: invokevirtual dispatchNestedPreScroll : (II[I[I)Z
    //   501: ifeq -> 581
    //   504: iload #12
    //   506: aload_0
    //   507: getfield mScrollConsumed : [I
    //   510: iconst_0
    //   511: iaload
    //   512: isub
    //   513: istore #7
    //   515: iload #6
    //   517: aload_0
    //   518: getfield mScrollConsumed : [I
    //   521: iconst_1
    //   522: iaload
    //   523: isub
    //   524: istore #9
    //   526: aload #5
    //   528: aload_0
    //   529: getfield mScrollOffset : [I
    //   532: iconst_0
    //   533: iaload
    //   534: i2f
    //   535: aload_0
    //   536: getfield mScrollOffset : [I
    //   539: iconst_1
    //   540: iaload
    //   541: i2f
    //   542: invokevirtual offsetLocation : (FF)V
    //   545: aload_0
    //   546: getfield mNestedOffsets : [I
    //   549: astore_1
    //   550: aload_1
    //   551: iconst_0
    //   552: aload_1
    //   553: iconst_0
    //   554: iaload
    //   555: aload_0
    //   556: getfield mScrollOffset : [I
    //   559: iconst_0
    //   560: iaload
    //   561: iadd
    //   562: iastore
    //   563: aload_0
    //   564: getfield mNestedOffsets : [I
    //   567: astore_1
    //   568: aload_1
    //   569: iconst_1
    //   570: aload_1
    //   571: iconst_1
    //   572: iaload
    //   573: aload_0
    //   574: getfield mScrollOffset : [I
    //   577: iconst_1
    //   578: iaload
    //   579: iadd
    //   580: iastore
    //   581: iload #7
    //   583: istore #6
    //   585: iload #9
    //   587: istore #12
    //   589: aload_0
    //   590: getfield mScrollState : I
    //   593: iconst_1
    //   594: if_icmpeq -> 724
    //   597: iconst_0
    //   598: istore #12
    //   600: iload #7
    //   602: istore #13
    //   604: iload #12
    //   606: istore #6
    //   608: iload_3
    //   609: ifeq -> 649
    //   612: iload #7
    //   614: istore #13
    //   616: iload #12
    //   618: istore #6
    //   620: iload #7
    //   622: invokestatic abs : (I)I
    //   625: aload_0
    //   626: getfield mTouchSlop : I
    //   629: if_icmple -> 649
    //   632: iload #7
    //   634: ifle -> 845
    //   637: iload #7
    //   639: aload_0
    //   640: getfield mTouchSlop : I
    //   643: isub
    //   644: istore #13
    //   646: iconst_1
    //   647: istore #6
    //   649: iload #9
    //   651: istore #7
    //   653: iload #6
    //   655: istore #14
    //   657: iload_2
    //   658: ifeq -> 698
    //   661: iload #9
    //   663: istore #7
    //   665: iload #6
    //   667: istore #14
    //   669: iload #9
    //   671: invokestatic abs : (I)I
    //   674: aload_0
    //   675: getfield mTouchSlop : I
    //   678: if_icmple -> 698
    //   681: iload #9
    //   683: ifle -> 857
    //   686: iload #9
    //   688: aload_0
    //   689: getfield mTouchSlop : I
    //   692: isub
    //   693: istore #7
    //   695: iconst_1
    //   696: istore #14
    //   698: iload #13
    //   700: istore #6
    //   702: iload #7
    //   704: istore #12
    //   706: iload #14
    //   708: ifeq -> 724
    //   711: aload_0
    //   712: iconst_1
    //   713: invokevirtual setScrollState : (I)V
    //   716: iload #7
    //   718: istore #12
    //   720: iload #13
    //   722: istore #6
    //   724: iload #4
    //   726: istore #9
    //   728: aload_0
    //   729: getfield mScrollState : I
    //   732: iconst_1
    //   733: if_icmpne -> 192
    //   736: aload_0
    //   737: iload #10
    //   739: aload_0
    //   740: getfield mScrollOffset : [I
    //   743: iconst_0
    //   744: iaload
    //   745: isub
    //   746: putfield mLastTouchX : I
    //   749: aload_0
    //   750: iload #11
    //   752: aload_0
    //   753: getfield mScrollOffset : [I
    //   756: iconst_1
    //   757: iaload
    //   758: isub
    //   759: putfield mLastTouchY : I
    //   762: iload_3
    //   763: ifeq -> 869
    //   766: iload #6
    //   768: istore #9
    //   770: iload_2
    //   771: ifeq -> 875
    //   774: iload #12
    //   776: istore #7
    //   778: aload_0
    //   779: iload #9
    //   781: iload #7
    //   783: aload #5
    //   785: invokevirtual scrollByInternal : (IILandroid/view/MotionEvent;)Z
    //   788: ifeq -> 801
    //   791: aload_0
    //   792: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   795: iconst_1
    //   796: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   801: iload #4
    //   803: istore #9
    //   805: aload_0
    //   806: getfield mGapWorker : Landroid/support/v7/widget/GapWorker;
    //   809: ifnull -> 192
    //   812: iload #6
    //   814: ifne -> 826
    //   817: iload #4
    //   819: istore #9
    //   821: iload #12
    //   823: ifeq -> 192
    //   826: aload_0
    //   827: getfield mGapWorker : Landroid/support/v7/widget/GapWorker;
    //   830: aload_0
    //   831: iload #6
    //   833: iload #12
    //   835: invokevirtual postFromTraversal : (Landroid/support/v7/widget/RecyclerView;II)V
    //   838: iload #4
    //   840: istore #9
    //   842: goto -> 192
    //   845: iload #7
    //   847: aload_0
    //   848: getfield mTouchSlop : I
    //   851: iadd
    //   852: istore #13
    //   854: goto -> 646
    //   857: iload #9
    //   859: aload_0
    //   860: getfield mTouchSlop : I
    //   863: iadd
    //   864: istore #7
    //   866: goto -> 695
    //   869: iconst_0
    //   870: istore #9
    //   872: goto -> 770
    //   875: iconst_0
    //   876: istore #7
    //   878: goto -> 778
    //   881: aload_0
    //   882: aload_1
    //   883: invokespecial onPointerUp : (Landroid/view/MotionEvent;)V
    //   886: iload #4
    //   888: istore #9
    //   890: goto -> 192
    //   893: aload_0
    //   894: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   897: aload #5
    //   899: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   902: iconst_1
    //   903: istore #9
    //   905: aload_0
    //   906: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   909: sipush #1000
    //   912: aload_0
    //   913: getfield mMaxFlingVelocity : I
    //   916: i2f
    //   917: invokevirtual computeCurrentVelocity : (IF)V
    //   920: iload_3
    //   921: ifeq -> 995
    //   924: aload_0
    //   925: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   928: aload_0
    //   929: getfield mScrollPointerId : I
    //   932: invokestatic getXVelocity : (Landroid/view/VelocityTracker;I)F
    //   935: fneg
    //   936: fstore #15
    //   938: iload_2
    //   939: ifeq -> 1001
    //   942: aload_0
    //   943: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   946: aload_0
    //   947: getfield mScrollPointerId : I
    //   950: invokestatic getYVelocity : (Landroid/view/VelocityTracker;I)F
    //   953: fneg
    //   954: fstore #16
    //   956: fload #15
    //   958: fconst_0
    //   959: fcmpl
    //   960: ifne -> 970
    //   963: fload #16
    //   965: fconst_0
    //   966: fcmpl
    //   967: ifeq -> 983
    //   970: aload_0
    //   971: fload #15
    //   973: f2i
    //   974: fload #16
    //   976: f2i
    //   977: invokevirtual fling : (II)Z
    //   980: ifne -> 988
    //   983: aload_0
    //   984: iconst_0
    //   985: invokevirtual setScrollState : (I)V
    //   988: aload_0
    //   989: invokespecial resetTouch : ()V
    //   992: goto -> 192
    //   995: fconst_0
    //   996: fstore #15
    //   998: goto -> 938
    //   1001: fconst_0
    //   1002: fstore #16
    //   1004: goto -> 956
    //   1007: aload_0
    //   1008: invokespecial cancelTouch : ()V
    //   1011: iload #4
    //   1013: istore #9
    //   1015: goto -> 192
  }
  
  void postAnimationRunner() {
    if (!this.mPostedAnimatorRunner && this.mIsAttached) {
      ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
      this.mPostedAnimatorRunner = true;
    } 
  }
  
  void recordAnimationInfoIfBouncedHiddenView(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    paramViewHolder.setFlags(0, 8192);
    if (this.mState.mTrackOldChangeHolders && paramViewHolder.isUpdated() && !paramViewHolder.isRemoved() && !paramViewHolder.shouldIgnore()) {
      long l = getChangedHolderKey(paramViewHolder);
      this.mViewInfoStore.addToOldChangeHolders(l, paramViewHolder);
    } 
    this.mViewInfoStore.addToPreLayout(paramViewHolder, paramItemHolderInfo);
  }
  
  void removeAndRecycleViews() {
    if (this.mItemAnimator != null)
      this.mItemAnimator.endAnimations(); 
    if (this.mLayout != null) {
      this.mLayout.removeAndRecycleAllViews(this.mRecycler);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    } 
    this.mRecycler.clear();
  }
  
  boolean removeAnimatingView(View paramView) {
    eatRequestLayout();
    boolean bool = this.mChildHelper.removeViewIfHidden(paramView);
    if (bool) {
      ViewHolder viewHolder = getChildViewHolderInt(paramView);
      this.mRecycler.unscrapView(viewHolder);
      this.mRecycler.recycleViewHolderInternal(viewHolder);
    } 
    if (!bool) {
      boolean bool2 = true;
      resumeRequestLayout(bool2);
      return bool;
    } 
    boolean bool1 = false;
    resumeRequestLayout(bool1);
    return bool;
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null)
      if (viewHolder.isTmpDetached()) {
        viewHolder.clearTmpDetachFlag();
      } else if (!viewHolder.shouldIgnore()) {
        throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + viewHolder);
      }  
    dispatchChildDetached(paramView);
    super.removeDetachedView(paramView, paramBoolean);
  }
  
  public void removeItemDecoration(ItemDecoration paramItemDecoration) {
    if (this.mLayout != null)
      this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout"); 
    this.mItemDecorations.remove(paramItemDecoration);
    if (this.mItemDecorations.isEmpty()) {
      boolean bool;
      if (getOverScrollMode() == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      setWillNotDraw(bool);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    if (this.mOnChildAttachStateListeners != null)
      this.mOnChildAttachStateListeners.remove(paramOnChildAttachStateChangeListener); 
  }
  
  public void removeOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.remove(paramOnItemTouchListener);
    if (this.mActiveOnItemTouchListener == paramOnItemTouchListener)
      this.mActiveOnItemTouchListener = null; 
  }
  
  public void removeOnScrollListener(OnScrollListener paramOnScrollListener) {
    if (this.mScrollListeners != null)
      this.mScrollListeners.remove(paramOnScrollListener); 
  }
  
  void repositionShadowingViews() {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getChildAt(b);
      ViewHolder viewHolder = getChildViewHolder(view);
      if (viewHolder != null && viewHolder.mShadowingHolder != null) {
        View view1 = viewHolder.mShadowingHolder.itemView;
        int j = view.getLeft();
        int k = view.getTop();
        if (j != view1.getLeft() || k != view1.getTop())
          view1.layout(j, k, view1.getWidth() + j, view1.getHeight() + k); 
      } 
    } 
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    boolean bool = false;
    if (!this.mLayout.onRequestChildFocus(this, this.mState, paramView1, paramView2) && paramView2 != null) {
      this.mTempRect.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
      ViewGroup.LayoutParams layoutParams = paramView2.getLayoutParams();
      if (layoutParams instanceof LayoutParams) {
        LayoutParams layoutParams1 = (LayoutParams)layoutParams;
        if (!layoutParams1.mInsetsDirty) {
          Rect rect1 = layoutParams1.mDecorInsets;
          Rect rect2 = this.mTempRect;
          rect2.left -= rect1.left;
          rect2 = this.mTempRect;
          rect2.right += rect1.right;
          rect2 = this.mTempRect;
          rect2.top -= rect1.top;
          rect2 = this.mTempRect;
          rect2.bottom += rect1.bottom;
        } 
      } 
      offsetDescendantRectToMyCoords(paramView2, this.mTempRect);
      offsetRectIntoDescendantCoords(paramView1, this.mTempRect);
      Rect rect = this.mTempRect;
      if (!this.mFirstLayoutComplete)
        bool = true; 
      requestChildRectangleOnScreen(paramView1, rect, bool);
    } 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    return this.mLayout.requestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    int i = this.mOnItemTouchListeners.size();
    for (byte b = 0; b < i; b++)
      ((OnItemTouchListener)this.mOnItemTouchListeners.get(b)).onRequestDisallowInterceptTouchEvent(paramBoolean); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    if (this.mEatRequestLayout == 0 && !this.mLayoutFrozen) {
      super.requestLayout();
      return;
    } 
    this.mLayoutRequestEaten = true;
  }
  
  void resumeRequestLayout(boolean paramBoolean) {
    if (this.mEatRequestLayout < 1)
      this.mEatRequestLayout = 1; 
    if (!paramBoolean)
      this.mLayoutRequestEaten = false; 
    if (this.mEatRequestLayout == 1) {
      if (paramBoolean && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null)
        dispatchLayout(); 
      if (!this.mLayoutFrozen)
        this.mLayoutRequestEaten = false; 
    } 
    this.mEatRequestLayout--;
  }
  
  void saveOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.saveOldPosition(); 
    } 
  }
  
  public void scrollBy(int paramInt1, int paramInt2) {
    if (this.mLayout == null) {
      Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (!this.mLayoutFrozen) {
      boolean bool1 = this.mLayout.canScrollHorizontally();
      boolean bool2 = this.mLayout.canScrollVertically();
      if (bool1 || bool2) {
        if (!bool1)
          paramInt1 = 0; 
        if (!bool2)
          paramInt2 = 0; 
        scrollByInternal(paramInt1, paramInt2, (MotionEvent)null);
      } 
    } 
  }
  
  boolean scrollByInternal(int paramInt1, int paramInt2, MotionEvent paramMotionEvent) {
    int[] arrayOfInt;
    int i = 0;
    byte b1 = 0;
    int j = 0;
    byte b2 = 0;
    int k = 0;
    byte b3 = 0;
    int m = 0;
    byte b4 = 0;
    consumePendingUpdateOperations();
    if (this.mAdapter != null) {
      eatRequestLayout();
      onEnterLayoutOrScroll();
      TraceCompat.beginSection("RV Scroll");
      k = b3;
      i = b1;
      if (paramInt1 != 0) {
        k = this.mLayout.scrollHorizontallyBy(paramInt1, this.mRecycler, this.mState);
        i = paramInt1 - k;
      } 
      m = b4;
      j = b2;
      if (paramInt2 != 0) {
        m = this.mLayout.scrollVerticallyBy(paramInt2, this.mRecycler, this.mState);
        j = paramInt2 - m;
      } 
      TraceCompat.endSection();
      repositionShadowingViews();
      onExitLayoutOrScroll();
      resumeRequestLayout(false);
    } 
    if (!this.mItemDecorations.isEmpty())
      invalidate(); 
    if (dispatchNestedScroll(k, m, i, j, this.mScrollOffset)) {
      this.mLastTouchX -= this.mScrollOffset[0];
      this.mLastTouchY -= this.mScrollOffset[1];
      if (paramMotionEvent != null)
        paramMotionEvent.offsetLocation(this.mScrollOffset[0], this.mScrollOffset[1]); 
      arrayOfInt = this.mNestedOffsets;
      arrayOfInt[0] = arrayOfInt[0] + this.mScrollOffset[0];
      arrayOfInt = this.mNestedOffsets;
      arrayOfInt[1] = arrayOfInt[1] + this.mScrollOffset[1];
    } else if (getOverScrollMode() != 2) {
      if (arrayOfInt != null)
        pullGlows(arrayOfInt.getX(), i, arrayOfInt.getY(), j); 
      considerReleasingGlowsOnScroll(paramInt1, paramInt2);
    } 
    if (k != 0 || m != 0)
      dispatchOnScrolled(k, m); 
    if (!awakenScrollBars())
      invalidate(); 
    return (k != 0 || m != 0);
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
  }
  
  public void scrollToPosition(int paramInt) {
    if (!this.mLayoutFrozen) {
      stopScroll();
      if (this.mLayout == null) {
        Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
      } 
      this.mLayout.scrollToPosition(paramInt);
      awakenScrollBars();
    } 
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent) {
    if (!shouldDeferAccessibilityEvent(paramAccessibilityEvent))
      super.sendAccessibilityEventUnchecked(paramAccessibilityEvent); 
  }
  
  public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate paramRecyclerViewAccessibilityDelegate) {
    this.mAccessibilityDelegate = paramRecyclerViewAccessibilityDelegate;
    ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate);
  }
  
  public void setAdapter(Adapter paramAdapter) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, false, true);
    requestLayout();
  }
  
  public void setChildDrawingOrderCallback(ChildDrawingOrderCallback paramChildDrawingOrderCallback) {
    if (paramChildDrawingOrderCallback != this.mChildDrawingOrderCallback) {
      boolean bool;
      this.mChildDrawingOrderCallback = paramChildDrawingOrderCallback;
      if (this.mChildDrawingOrderCallback != null) {
        bool = true;
      } else {
        bool = false;
      } 
      setChildrenDrawingOrderEnabled(bool);
    } 
  }
  
  @VisibleForTesting
  boolean setChildImportantForAccessibilityInternal(ViewHolder paramViewHolder, int paramInt) {
    if (isComputingLayout()) {
      paramViewHolder.mPendingAccessibilityState = paramInt;
      this.mPendingAccessibilityImportanceChange.add(paramViewHolder);
      return false;
    } 
    ViewCompat.setImportantForAccessibility(paramViewHolder.itemView, paramInt);
    return true;
  }
  
  public void setClipToPadding(boolean paramBoolean) {
    if (paramBoolean != this.mClipToPadding)
      invalidateGlows(); 
    this.mClipToPadding = paramBoolean;
    super.setClipToPadding(paramBoolean);
    if (this.mFirstLayoutComplete)
      requestLayout(); 
  }
  
  void setDataSetChangedAfterLayout() {
    if (!this.mDataSetHasChangedAfterLayout) {
      this.mDataSetHasChangedAfterLayout = true;
      int i = this.mChildHelper.getUnfilteredChildCount();
      for (byte b = 0; b < i; b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
        if (viewHolder != null && !viewHolder.shouldIgnore())
          viewHolder.addFlags(512); 
      } 
      this.mRecycler.setAdapterPositionsAsUnknown();
      markKnownViewsInvalid();
    } 
  }
  
  public void setHasFixedSize(boolean paramBoolean) {
    this.mHasFixedSize = paramBoolean;
  }
  
  public void setItemAnimator(ItemAnimator paramItemAnimator) {
    if (this.mItemAnimator != null) {
      this.mItemAnimator.endAnimations();
      this.mItemAnimator.setListener(null);
    } 
    this.mItemAnimator = paramItemAnimator;
    if (this.mItemAnimator != null)
      this.mItemAnimator.setListener(this.mItemAnimatorListener); 
  }
  
  public void setItemViewCacheSize(int paramInt) {
    this.mRecycler.setViewCacheSize(paramInt);
  }
  
  public void setLayoutFrozen(boolean paramBoolean) {
    if (paramBoolean != this.mLayoutFrozen) {
      assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
      if (!paramBoolean) {
        this.mLayoutFrozen = false;
        if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null)
          requestLayout(); 
        this.mLayoutRequestEaten = false;
        return;
      } 
    } else {
      return;
    } 
    long l = SystemClock.uptimeMillis();
    onTouchEvent(MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0));
    this.mLayoutFrozen = true;
    this.mIgnoreMotionEventTillDown = true;
    stopScroll();
  }
  
  public void setLayoutManager(LayoutManager paramLayoutManager) {
    if (paramLayoutManager != this.mLayout) {
      stopScroll();
      if (this.mLayout != null) {
        if (this.mItemAnimator != null)
          this.mItemAnimator.endAnimations(); 
        this.mLayout.removeAndRecycleAllViews(this.mRecycler);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mRecycler.clear();
        if (this.mIsAttached)
          this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler); 
        this.mLayout.setRecyclerView(null);
        this.mLayout = null;
      } else {
        this.mRecycler.clear();
      } 
      this.mChildHelper.removeAllViewsUnfiltered();
      this.mLayout = paramLayoutManager;
      if (paramLayoutManager != null) {
        if (paramLayoutManager.mRecyclerView != null)
          throw new IllegalArgumentException("LayoutManager " + paramLayoutManager + " is already attached to a RecyclerView: " + paramLayoutManager.mRecyclerView); 
        this.mLayout.setRecyclerView(this);
        if (this.mIsAttached)
          this.mLayout.dispatchAttachedToWindow(this); 
      } 
      this.mRecycler.updateViewCacheSize();
      requestLayout();
    } 
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    getScrollingChildHelper().setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setOnFlingListener(@Nullable OnFlingListener paramOnFlingListener) {
    this.mOnFlingListener = paramOnFlingListener;
  }
  
  @Deprecated
  public void setOnScrollListener(OnScrollListener paramOnScrollListener) {
    this.mScrollListener = paramOnScrollListener;
  }
  
  public void setPreserveFocusAfterLayout(boolean paramBoolean) {
    this.mPreserveFocusAfterLayout = paramBoolean;
  }
  
  public void setRecycledViewPool(RecycledViewPool paramRecycledViewPool) {
    this.mRecycler.setRecycledViewPool(paramRecycledViewPool);
  }
  
  public void setRecyclerListener(RecyclerListener paramRecyclerListener) {
    this.mRecyclerListener = paramRecyclerListener;
  }
  
  void setScrollState(int paramInt) {
    if (paramInt != this.mScrollState) {
      this.mScrollState = paramInt;
      if (paramInt != 2)
        stopScrollersInternal(); 
      dispatchOnScrollStateChanged(paramInt);
    } 
  }
  
  public void setScrollingTouchSlop(int paramInt) {
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    switch (paramInt) {
      default:
        Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + paramInt + "; using default value");
      case 0:
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        return;
      case 1:
        break;
    } 
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
  }
  
  public void setViewCacheExtension(ViewCacheExtension paramViewCacheExtension) {
    this.mRecycler.setViewCacheExtension(paramViewCacheExtension);
  }
  
  boolean shouldDeferAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (isComputingLayout()) {
      int i = 0;
      if (paramAccessibilityEvent != null)
        i = AccessibilityEventCompat.getContentChangeTypes(paramAccessibilityEvent); 
      int j = i;
      if (i == 0)
        j = 0; 
      this.mEatenAccessibilityChangeFlags |= j;
      return true;
    } 
    return false;
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2) {
    smoothScrollBy(paramInt1, paramInt2, (Interpolator)null);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator) {
    if (this.mLayout == null) {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (!this.mLayoutFrozen) {
      if (!this.mLayout.canScrollHorizontally())
        paramInt1 = 0; 
      if (!this.mLayout.canScrollVertically())
        paramInt2 = 0; 
      if (paramInt1 != 0 || paramInt2 != 0)
        this.mViewFlinger.smoothScrollBy(paramInt1, paramInt2, paramInterpolator); 
    } 
  }
  
  public void smoothScrollToPosition(int paramInt) {
    if (!this.mLayoutFrozen) {
      if (this.mLayout == null) {
        Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
      } 
      this.mLayout.smoothScrollToPosition(this, this.mState, paramInt);
    } 
  }
  
  public boolean startNestedScroll(int paramInt) {
    return getScrollingChildHelper().startNestedScroll(paramInt);
  }
  
  public void stopNestedScroll() {
    getScrollingChildHelper().stopNestedScroll();
  }
  
  public void stopScroll() {
    setScrollState(0);
    stopScrollersInternal();
  }
  
  public void swapAdapter(Adapter paramAdapter, boolean paramBoolean) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, true, paramBoolean);
    setDataSetChangedAfterLayout();
    requestLayout();
  }
  
  void viewRangeUpdate(int paramInt1, int paramInt2, Object paramObject) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getUnfilteredChildAt(b);
      ViewHolder viewHolder = getChildViewHolderInt(view);
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1 && viewHolder.mPosition < paramInt1 + paramInt2) {
        viewHolder.addFlags(2);
        viewHolder.addChangePayload(paramObject);
        ((LayoutParams)view.getLayoutParams()).mInsetsDirty = true;
      } 
    } 
    this.mRecycler.viewRangeUpdate(paramInt1, paramInt2);
  }
  
  static {
    boolean bool;
  }
  
  static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
  
  private static final boolean ALLOW_THREAD_GAP_WORK;
  
  private static final int[] CLIP_TO_PADDING_ATTR;
  
  static final boolean DEBUG = false;
  
  static final boolean DISPATCH_TEMP_DETACH = false;
  
  private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
  
  static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
  
  static final long FOREVER_NS = 9223372036854775807L;
  
  public static final int HORIZONTAL = 0;
  
  private static final int INVALID_POINTER = -1;
  
  public static final int INVALID_TYPE = -1;
  
  private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
  
  static final int MAX_SCROLL_DURATION = 2000;
  
  private static final int[] NESTED_SCROLLING_ATTRS = new int[] { 16843830 };
  
  public static final long NO_ID = -1L;
  
  public static final int NO_POSITION = -1;
  
  static final boolean POST_UPDATES_ON_ANIMATION;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  static final String TAG = "RecyclerView";
  
  public static final int TOUCH_SLOP_DEFAULT = 0;
  
  public static final int TOUCH_SLOP_PAGING = 1;
  
  static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
  
  static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
  
  private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
  
  static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
  
  private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
  
  private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
  
  static final String TRACE_PREFETCH_TAG = "RV Prefetch";
  
  static final String TRACE_SCROLL_TAG = "RV Scroll";
  
  public static final int VERTICAL = 1;
  
  static final Interpolator sQuinticInterpolator;
  
  RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
  
  private final AccessibilityManager mAccessibilityManager;
  
  private OnItemTouchListener mActiveOnItemTouchListener;
  
  Adapter mAdapter;
  
  AdapterHelper mAdapterHelper;
  
  boolean mAdapterUpdateDuringMeasure;
  
  private EdgeEffectCompat mBottomGlow;
  
  private ChildDrawingOrderCallback mChildDrawingOrderCallback;
  
  ChildHelper mChildHelper;
  
  boolean mClipToPadding;
  
  boolean mDataSetHasChangedAfterLayout;
  
  private int mDispatchScrollCounter;
  
  private int mEatRequestLayout;
  
  private int mEatenAccessibilityChangeFlags;
  
  @VisibleForTesting
  boolean mFirstLayoutComplete;
  
  GapWorker mGapWorker;
  
  boolean mHasFixedSize;
  
  private boolean mIgnoreMotionEventTillDown;
  
  private int mInitialTouchX;
  
  private int mInitialTouchY;
  
  boolean mIsAttached;
  
  ItemAnimator mItemAnimator;
  
  private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
  
  private Runnable mItemAnimatorRunner;
  
  final ArrayList<ItemDecoration> mItemDecorations;
  
  boolean mItemsAddedOrRemoved;
  
  boolean mItemsChanged;
  
  private int mLastTouchX;
  
  private int mLastTouchY;
  
  @VisibleForTesting
  LayoutManager mLayout;
  
  boolean mLayoutFrozen;
  
  private int mLayoutOrScrollCounter;
  
  boolean mLayoutRequestEaten;
  
  private EdgeEffectCompat mLeftGlow;
  
  private final int mMaxFlingVelocity;
  
  private final int mMinFlingVelocity;
  
  private final int[] mMinMaxLayoutPositions;
  
  private final int[] mNestedOffsets;
  
  private final RecyclerViewDataObserver mObserver;
  
  private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
  
  private OnFlingListener mOnFlingListener;
  
  private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
  
  @VisibleForTesting
  final List<ViewHolder> mPendingAccessibilityImportanceChange;
  
  private SavedState mPendingSavedState;
  
  boolean mPostedAnimatorRunner;
  
  GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
  
  private boolean mPreserveFocusAfterLayout;
  
  final Recycler mRecycler;
  
  RecyclerListener mRecyclerListener;
  
  private EdgeEffectCompat mRightGlow;
  
  private final int[] mScrollConsumed;
  
  private float mScrollFactor;
  
  private OnScrollListener mScrollListener;
  
  private List<OnScrollListener> mScrollListeners;
  
  private final int[] mScrollOffset;
  
  private int mScrollPointerId;
  
  private int mScrollState;
  
  private NestedScrollingChildHelper mScrollingChildHelper;
  
  final State mState;
  
  final Rect mTempRect;
  
  private final Rect mTempRect2;
  
  final RectF mTempRectF;
  
  private EdgeEffectCompat mTopGlow;
  
  private int mTouchSlop;
  
  final Runnable mUpdateChildViewsRunnable;
  
  private VelocityTracker mVelocityTracker;
  
  final ViewFlinger mViewFlinger;
  
  private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
  
  final ViewInfoStore mViewInfoStore;
  
  public static abstract class Adapter<VH extends ViewHolder> {
    private boolean mHasStableIds = false;
    
    private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();
    
    public final void bindViewHolder(VH param1VH, int param1Int) {
      ((RecyclerView.ViewHolder)param1VH).mPosition = param1Int;
      if (hasStableIds())
        ((RecyclerView.ViewHolder)param1VH).mItemId = getItemId(param1Int); 
      param1VH.setFlags(1, 519);
      TraceCompat.beginSection("RV OnBindView");
      onBindViewHolder(param1VH, param1Int, param1VH.getUnmodifiedPayloads());
      param1VH.clearPayload();
      ViewGroup.LayoutParams layoutParams = ((RecyclerView.ViewHolder)param1VH).itemView.getLayoutParams();
      if (layoutParams instanceof RecyclerView.LayoutParams)
        ((RecyclerView.LayoutParams)layoutParams).mInsetsDirty = true; 
      TraceCompat.endSection();
    }
    
    public final VH createViewHolder(ViewGroup param1ViewGroup, int param1Int) {
      TraceCompat.beginSection("RV CreateView");
      param1ViewGroup = (ViewGroup)onCreateViewHolder(param1ViewGroup, param1Int);
      ((RecyclerView.ViewHolder)param1ViewGroup).mItemViewType = param1Int;
      TraceCompat.endSection();
      return (VH)param1ViewGroup;
    }
    
    public abstract int getItemCount();
    
    public long getItemId(int param1Int) {
      return -1L;
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public final boolean hasObservers() {
      return this.mObservable.hasObservers();
    }
    
    public final boolean hasStableIds() {
      return this.mHasStableIds;
    }
    
    public final void notifyDataSetChanged() {
      this.mObservable.notifyChanged();
    }
    
    public final void notifyItemChanged(int param1Int) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1);
    }
    
    public final void notifyItemChanged(int param1Int, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1, param1Object);
    }
    
    public final void notifyItemInserted(int param1Int) {
      this.mObservable.notifyItemRangeInserted(param1Int, 1);
    }
    
    public final void notifyItemMoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemMoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2, param1Object);
    }
    
    public final void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeInserted(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeRemoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRemoved(int param1Int) {
      this.mObservable.notifyItemRangeRemoved(param1Int, 1);
    }
    
    public void onAttachedToRecyclerView(RecyclerView param1RecyclerView) {}
    
    public abstract void onBindViewHolder(VH param1VH, int param1Int);
    
    public void onBindViewHolder(VH param1VH, int param1Int, List<Object> param1List) {
      onBindViewHolder(param1VH, param1Int);
    }
    
    public abstract VH onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int);
    
    public void onDetachedFromRecyclerView(RecyclerView param1RecyclerView) {}
    
    public boolean onFailedToRecycleView(VH param1VH) {
      return false;
    }
    
    public void onViewAttachedToWindow(VH param1VH) {}
    
    public void onViewDetachedFromWindow(VH param1VH) {}
    
    public void onViewRecycled(VH param1VH) {}
    
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.registerObserver(param1AdapterDataObserver);
    }
    
    public void setHasStableIds(boolean param1Boolean) {
      if (hasObservers())
        throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers."); 
      this.mHasStableIds = param1Boolean;
    }
    
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.unregisterObserver(param1AdapterDataObserver);
    }
  }
  
  static class AdapterDataObservable extends Observable<AdapterDataObserver> {
    public boolean hasObservers() {
      return !this.mObservers.isEmpty();
    }
    
    public void notifyChanged() {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onChanged(); 
    }
    
    public void notifyItemMoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(param1Int1, param1Int2, 1); 
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      notifyItemRangeChanged(param1Int1, param1Int2, (Object)null);
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(param1Int1, param1Int2, param1Object); 
    }
    
    public void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(param1Int1, param1Int2); 
    }
    
    public void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(param1Int1, param1Int2); 
    }
  }
  
  public static abstract class AdapterDataObserver {
    public void onChanged() {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2) {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      onItemRangeChanged(param1Int1, param1Int2);
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {}
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {}
  }
  
  public static interface ChildDrawingOrderCallback {
    int onGetChildDrawingOrder(int param1Int1, int param1Int2);
  }
  
  public static abstract class ItemAnimator {
    public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    public static final int FLAG_CHANGED = 2;
    
    public static final int FLAG_INVALIDATED = 4;
    
    public static final int FLAG_MOVED = 2048;
    
    public static final int FLAG_REMOVED = 8;
    
    private long mAddDuration = 120L;
    
    private long mChangeDuration = 250L;
    
    private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<ItemAnimatorFinishedListener>();
    
    private ItemAnimatorListener mListener = null;
    
    private long mMoveDuration = 250L;
    
    private long mRemoveDuration = 120L;
    
    static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder param1ViewHolder) {
      int i = param1ViewHolder.mFlags & 0xE;
      if (param1ViewHolder.isInvalid())
        return 4; 
      int j = i;
      if ((i & 0x4) == 0) {
        int k = param1ViewHolder.getOldPosition();
        int m = param1ViewHolder.getAdapterPosition();
        j = i;
        if (k != -1) {
          j = i;
          if (m != -1) {
            j = i;
            if (k != m)
              j = i | 0x800; 
          } 
        } 
      } 
      return j;
    }
    
    public abstract boolean animateAppearance(@NonNull RecyclerView.ViewHolder param1ViewHolder, @Nullable ItemHolderInfo param1ItemHolderInfo1, @NonNull ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateChange(@NonNull RecyclerView.ViewHolder param1ViewHolder1, @NonNull RecyclerView.ViewHolder param1ViewHolder2, @NonNull ItemHolderInfo param1ItemHolderInfo1, @NonNull ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateDisappearance(@NonNull RecyclerView.ViewHolder param1ViewHolder, @NonNull ItemHolderInfo param1ItemHolderInfo1, @Nullable ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animatePersistence(@NonNull RecyclerView.ViewHolder param1ViewHolder, @NonNull ItemHolderInfo param1ItemHolderInfo1, @NonNull ItemHolderInfo param1ItemHolderInfo2);
    
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder param1ViewHolder) {
      return true;
    }
    
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder param1ViewHolder, @NonNull List<Object> param1List) {
      return canReuseUpdatedViewHolder(param1ViewHolder);
    }
    
    public final void dispatchAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationFinished(param1ViewHolder);
      if (this.mListener != null)
        this.mListener.onAnimationFinished(param1ViewHolder); 
    }
    
    public final void dispatchAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationStarted(param1ViewHolder);
    }
    
    public final void dispatchAnimationsFinished() {
      int i = this.mFinishedListeners.size();
      for (byte b = 0; b < i; b++)
        ((ItemAnimatorFinishedListener)this.mFinishedListeners.get(b)).onAnimationsFinished(); 
      this.mFinishedListeners.clear();
    }
    
    public abstract void endAnimation(RecyclerView.ViewHolder param1ViewHolder);
    
    public abstract void endAnimations();
    
    public long getAddDuration() {
      return this.mAddDuration;
    }
    
    public long getChangeDuration() {
      return this.mChangeDuration;
    }
    
    public long getMoveDuration() {
      return this.mMoveDuration;
    }
    
    public long getRemoveDuration() {
      return this.mRemoveDuration;
    }
    
    public abstract boolean isRunning();
    
    public final boolean isRunning(ItemAnimatorFinishedListener param1ItemAnimatorFinishedListener) {
      boolean bool = isRunning();
      if (param1ItemAnimatorFinishedListener != null) {
        if (!bool) {
          param1ItemAnimatorFinishedListener.onAnimationsFinished();
          return bool;
        } 
      } else {
        return bool;
      } 
      this.mFinishedListeners.add(param1ItemAnimatorFinishedListener);
      return bool;
    }
    
    public ItemHolderInfo obtainHolderInfo() {
      return new ItemHolderInfo();
    }
    
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {}
    
    public void onAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {}
    
    @NonNull
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State param1State, @NonNull RecyclerView.ViewHolder param1ViewHolder) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    @NonNull
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State param1State, @NonNull RecyclerView.ViewHolder param1ViewHolder, int param1Int, @NonNull List<Object> param1List) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    public abstract void runPendingAnimations();
    
    public void setAddDuration(long param1Long) {
      this.mAddDuration = param1Long;
    }
    
    public void setChangeDuration(long param1Long) {
      this.mChangeDuration = param1Long;
    }
    
    void setListener(ItemAnimatorListener param1ItemAnimatorListener) {
      this.mListener = param1ItemAnimatorListener;
    }
    
    public void setMoveDuration(long param1Long) {
      this.mMoveDuration = param1Long;
    }
    
    public void setRemoveDuration(long param1Long) {
      this.mRemoveDuration = param1Long;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface AdapterChanges {}
    
    public static interface ItemAnimatorFinishedListener {
      void onAnimationsFinished();
    }
    
    static interface ItemAnimatorListener {
      void onAnimationFinished(RecyclerView.ViewHolder param2ViewHolder);
    }
    
    public static class ItemHolderInfo {
      public int bottom;
      
      public int changeFlags;
      
      public int left;
      
      public int right;
      
      public int top;
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder) {
        return setFrom(param2ViewHolder, 0);
      }
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder, int param2Int) {
        View view = param2ViewHolder.itemView;
        this.left = view.getLeft();
        this.top = view.getTop();
        this.right = view.getRight();
        this.bottom = view.getBottom();
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdapterChanges {}
  
  public static interface ItemAnimatorFinishedListener {
    void onAnimationsFinished();
  }
  
  static interface ItemAnimatorListener {
    void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  public static class ItemHolderInfo {
    public int bottom;
    
    public int changeFlags;
    
    public int left;
    
    public int right;
    
    public int top;
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder) {
      return setFrom(param1ViewHolder, 0);
    }
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      View view = param1ViewHolder.itemView;
      this.left = view.getLeft();
      this.top = view.getTop();
      this.right = view.getRight();
      this.bottom = view.getBottom();
      return this;
    }
  }
  
  private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      param1ViewHolder.setIsRecyclable(true);
      if (param1ViewHolder.mShadowedHolder != null && param1ViewHolder.mShadowingHolder == null)
        param1ViewHolder.mShadowedHolder = null; 
      param1ViewHolder.mShadowingHolder = null;
      if (!param1ViewHolder.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(param1ViewHolder.itemView) && param1ViewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1ViewHolder.itemView, false); 
    }
  }
  
  public static abstract class ItemDecoration {
    @Deprecated
    public void getItemOffsets(Rect param1Rect, int param1Int, RecyclerView param1RecyclerView) {
      param1Rect.set(0, 0, 0, 0);
    }
    
    public void getItemOffsets(Rect param1Rect, View param1View, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      getItemOffsets(param1Rect, ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition(), param1RecyclerView);
    }
    
    @Deprecated
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDraw(param1Canvas, param1RecyclerView);
    }
    
    @Deprecated
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDrawOver(param1Canvas, param1RecyclerView);
    }
  }
  
  public static abstract class LayoutManager {
    boolean mAutoMeasure = false;
    
    ChildHelper mChildHelper;
    
    private int mHeight;
    
    private int mHeightMode;
    
    boolean mIsAttachedToWindow = false;
    
    private boolean mItemPrefetchEnabled = true;
    
    private boolean mMeasurementCacheEnabled = true;
    
    int mPrefetchMaxCountObserved;
    
    boolean mPrefetchMaxObservedInInitialPrefetch;
    
    RecyclerView mRecyclerView;
    
    boolean mRequestedSimpleAnimations = false;
    
    @Nullable
    RecyclerView.SmoothScroller mSmoothScroller;
    
    private int mWidth;
    
    private int mWidthMode;
    
    private void addViewInt(View param1View, int param1Int, boolean param1Boolean) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (param1Boolean || viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      if (viewHolder.wasReturnedFromScrap() || viewHolder.isScrap()) {
        if (viewHolder.isScrap()) {
          viewHolder.unScrap();
        } else {
          viewHolder.clearReturnedFromScrapFlag();
        } 
        this.mChildHelper.attachViewToParent(param1View, param1Int, param1View.getLayoutParams(), false);
      } else if (param1View.getParent() == this.mRecyclerView) {
        int i = this.mChildHelper.indexOfChild(param1View);
        int j = param1Int;
        if (param1Int == -1)
          j = this.mChildHelper.getChildCount(); 
        if (i == -1)
          throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(param1View)); 
        if (i != j)
          this.mRecyclerView.mLayout.moveView(i, j); 
      } else {
        this.mChildHelper.addView(param1View, param1Int, false);
        layoutParams.mInsetsDirty = true;
        if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning())
          this.mSmoothScroller.onChildAttachedToWindow(param1View); 
      } 
      if (layoutParams.mPendingInvalidate) {
        viewHolder.itemView.invalidate();
        layoutParams.mPendingInvalidate = false;
      } 
    }
    
    public static int chooseSize(int param1Int1, int param1Int2, int param1Int3) {
      int i = View.MeasureSpec.getMode(param1Int1);
      int j = View.MeasureSpec.getSize(param1Int1);
      param1Int1 = j;
      switch (i) {
        default:
          param1Int1 = Math.max(param1Int2, param1Int3);
        case 1073741824:
          return param1Int1;
        case -2147483648:
          break;
      } 
      param1Int1 = Math.min(j, Math.max(param1Int2, param1Int3));
    }
    
    private void detachViewInternal(int param1Int, View param1View) {
      this.mChildHelper.detachViewFromParent(param1Int);
    }
    
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, int param1Int4, boolean param1Boolean) {
      int i = Math.max(0, param1Int1 - param1Int3);
      param1Int3 = 0;
      param1Int1 = 0;
      if (param1Boolean) {
        if (param1Int4 >= 0) {
          param1Int3 = param1Int4;
          param1Int1 = 1073741824;
        } 
        if (param1Int4 == -1) {
          switch (param1Int2) {
            default:
              return View.MeasureSpec.makeMeasureSpec(param1Int3, param1Int1);
            case 1073741824:
            case -2147483648:
              param1Int3 = i;
              param1Int1 = param1Int2;
            case 0:
              break;
          } 
          param1Int3 = 0;
          param1Int1 = 0;
        } 
        if (param1Int4 == -2) {
          param1Int3 = 0;
          param1Int1 = 0;
        } 
      } 
      if (param1Int4 >= 0) {
        param1Int3 = param1Int4;
        param1Int1 = 1073741824;
      } 
      if (param1Int4 == -1) {
        param1Int3 = i;
        param1Int1 = param1Int2;
      } 
      if (param1Int4 == -2) {
        param1Int3 = i;
        if (param1Int2 == Integer.MIN_VALUE || param1Int2 == 1073741824)
          param1Int1 = Integer.MIN_VALUE; 
        param1Int1 = 0;
      } 
    }
    
    @Deprecated
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      int i = Math.max(0, param1Int1 - param1Int2);
      param1Int2 = 0;
      param1Int1 = 0;
      if (param1Boolean) {
        if (param1Int3 >= 0) {
          param1Int2 = param1Int3;
          param1Int1 = 1073741824;
          return View.MeasureSpec.makeMeasureSpec(param1Int2, param1Int1);
        } 
        param1Int2 = 0;
        param1Int1 = 0;
        return View.MeasureSpec.makeMeasureSpec(param1Int2, param1Int1);
      } 
      if (param1Int3 >= 0) {
        param1Int2 = param1Int3;
        param1Int1 = 1073741824;
        return View.MeasureSpec.makeMeasureSpec(param1Int2, param1Int1);
      } 
      if (param1Int3 == -1) {
        param1Int2 = i;
        param1Int1 = 1073741824;
        return View.MeasureSpec.makeMeasureSpec(param1Int2, param1Int1);
      } 
      if (param1Int3 == -2) {
        param1Int2 = i;
        param1Int1 = Integer.MIN_VALUE;
      } 
      return View.MeasureSpec.makeMeasureSpec(param1Int2, param1Int1);
    }
    
    public static Properties getProperties(Context param1Context, AttributeSet param1AttributeSet, int param1Int1, int param1Int2) {
      Properties properties = new Properties();
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.RecyclerView, param1Int1, param1Int2);
      properties.orientation = typedArray.getInt(R.styleable.RecyclerView_android_orientation, 1);
      properties.spanCount = typedArray.getInt(R.styleable.RecyclerView_spanCount, 1);
      properties.reverseLayout = typedArray.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
      properties.stackFromEnd = typedArray.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
      typedArray.recycle();
      return properties;
    }
    
    private static boolean isMeasurementUpToDate(int param1Int1, int param1Int2, int param1Int3) {
      boolean bool1 = true;
      int i = View.MeasureSpec.getMode(param1Int2);
      param1Int2 = View.MeasureSpec.getSize(param1Int2);
      if (param1Int3 > 0 && param1Int1 != param1Int3)
        boolean bool = false; 
      boolean bool2 = bool1;
      switch (i) {
        case 0:
          return bool2;
        default:
          bool2 = false;
        case -2147483648:
          bool2 = bool1;
          if (param1Int2 < param1Int1)
            bool2 = false; 
        case 1073741824:
          break;
      } 
      bool2 = bool1;
      if (param1Int2 != param1Int1)
        bool2 = false; 
    }
    
    private void onSmoothScrollerStopped(RecyclerView.SmoothScroller param1SmoothScroller) {
      if (this.mSmoothScroller == param1SmoothScroller)
        this.mSmoothScroller = null; 
    }
    
    private void scrapOrRecycleView(RecyclerView.Recycler param1Recycler, int param1Int, View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (!viewHolder.shouldIgnore()) {
        if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
          removeViewAt(param1Int);
          param1Recycler.recycleViewHolderInternal(viewHolder);
          return;
        } 
        detachViewAt(param1Int);
        param1Recycler.scrapView(param1View);
        this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
      } 
    }
    
    public void addDisappearingView(View param1View) {
      addDisappearingView(param1View, -1);
    }
    
    public void addDisappearingView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, true);
    }
    
    public void addView(View param1View) {
      addView(param1View, -1);
    }
    
    public void addView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, false);
    }
    
    public void assertInLayoutOrScroll(String param1String) {
      if (this.mRecyclerView != null)
        this.mRecyclerView.assertInLayoutOrScroll(param1String); 
    }
    
    public void assertNotInLayoutOrScroll(String param1String) {
      if (this.mRecyclerView != null)
        this.mRecyclerView.assertNotInLayoutOrScroll(param1String); 
    }
    
    public void attachView(View param1View) {
      attachView(param1View, -1);
    }
    
    public void attachView(View param1View, int param1Int) {
      attachView(param1View, param1Int, (RecyclerView.LayoutParams)param1View.getLayoutParams());
    }
    
    public void attachView(View param1View, int param1Int, RecyclerView.LayoutParams param1LayoutParams) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      this.mChildHelper.attachViewToParent(param1View, param1Int, (ViewGroup.LayoutParams)param1LayoutParams, viewHolder.isRemoved());
    }
    
    public void calculateItemDecorationsForChild(View param1View, Rect param1Rect) {
      if (this.mRecyclerView == null) {
        param1Rect.set(0, 0, 0, 0);
        return;
      } 
      param1Rect.set(this.mRecyclerView.getItemDecorInsetsForChild(param1View));
    }
    
    public boolean canScrollHorizontally() {
      return false;
    }
    
    public boolean canScrollVertically() {
      return false;
    }
    
    public boolean checkLayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      return (param1LayoutParams != null);
    }
    
    public void collectAdjacentPrefetchPositions(int param1Int1, int param1Int2, RecyclerView.State param1State, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public void collectInitialPrefetchPositions(int param1Int, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public int computeHorizontalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public void detachAndScrapAttachedViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--)
        scrapOrRecycleView(param1Recycler, i, getChildAt(i)); 
    }
    
    public void detachAndScrapView(View param1View, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, this.mChildHelper.indexOfChild(param1View), param1View);
    }
    
    public void detachAndScrapViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, param1Int, getChildAt(param1Int));
    }
    
    public void detachView(View param1View) {
      int i = this.mChildHelper.indexOfChild(param1View);
      if (i >= 0)
        detachViewInternal(i, param1View); 
    }
    
    public void detachViewAt(int param1Int) {
      detachViewInternal(param1Int, getChildAt(param1Int));
    }
    
    void dispatchAttachedToWindow(RecyclerView param1RecyclerView) {
      this.mIsAttachedToWindow = true;
      onAttachedToWindow(param1RecyclerView);
    }
    
    void dispatchDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      this.mIsAttachedToWindow = false;
      onDetachedFromWindow(param1RecyclerView, param1Recycler);
    }
    
    public void endAnimation(View param1View) {
      if (this.mRecyclerView.mItemAnimator != null)
        this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(param1View)); 
    }
    
    @Nullable
    public View findContainingItemView(View param1View) {
      if (this.mRecyclerView == null)
        return null; 
      View view = this.mRecyclerView.findContainingItemView(param1View);
      if (view == null)
        return null; 
      param1View = view;
      if (this.mChildHelper.isHidden(view))
        param1View = null; 
      return param1View;
    }
    
    public View findViewByPosition(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getChildCount : ()I
      //   4: istore_2
      //   5: iconst_0
      //   6: istore_3
      //   7: iload_3
      //   8: iload_2
      //   9: if_icmpge -> 86
      //   12: aload_0
      //   13: iload_3
      //   14: invokevirtual getChildAt : (I)Landroid/view/View;
      //   17: astore #4
      //   19: aload #4
      //   21: invokestatic getChildViewHolderInt : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
      //   24: astore #5
      //   26: aload #5
      //   28: ifnonnull -> 37
      //   31: iinc #3, 1
      //   34: goto -> 7
      //   37: aload #5
      //   39: invokevirtual getLayoutPosition : ()I
      //   42: iload_1
      //   43: if_icmpne -> 31
      //   46: aload #5
      //   48: invokevirtual shouldIgnore : ()Z
      //   51: ifne -> 31
      //   54: aload #4
      //   56: astore #6
      //   58: aload_0
      //   59: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   62: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
      //   65: invokevirtual isPreLayout : ()Z
      //   68: ifne -> 83
      //   71: aload #5
      //   73: invokevirtual isRemoved : ()Z
      //   76: ifne -> 31
      //   79: aload #4
      //   81: astore #6
      //   83: aload #6
      //   85: areturn
      //   86: aconst_null
      //   87: astore #6
      //   89: goto -> 83
    }
    
    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();
    
    public RecyclerView.LayoutParams generateLayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      return new RecyclerView.LayoutParams(param1Context, param1AttributeSet);
    }
    
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      return (param1LayoutParams instanceof RecyclerView.LayoutParams) ? new RecyclerView.LayoutParams((RecyclerView.LayoutParams)param1LayoutParams) : ((param1LayoutParams instanceof ViewGroup.MarginLayoutParams) ? new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams)param1LayoutParams) : new RecyclerView.LayoutParams(param1LayoutParams));
    }
    
    public int getBaseline() {
      return -1;
    }
    
    public int getBottomDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.bottom;
    }
    
    public View getChildAt(int param1Int) {
      return (this.mChildHelper != null) ? this.mChildHelper.getChildAt(param1Int) : null;
    }
    
    public int getChildCount() {
      return (this.mChildHelper != null) ? this.mChildHelper.getChildCount() : 0;
    }
    
    public boolean getClipToPadding() {
      return (this.mRecyclerView != null && this.mRecyclerView.mClipToPadding);
    }
    
    public int getColumnCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      byte b = 1;
      int i = b;
      if (this.mRecyclerView != null) {
        if (this.mRecyclerView.mAdapter == null)
          return b; 
      } else {
        return i;
      } 
      i = b;
      if (canScrollHorizontally())
        i = this.mRecyclerView.mAdapter.getItemCount(); 
      return i;
    }
    
    public int getDecoratedBottom(View param1View) {
      return param1View.getBottom() + getBottomDecorationHeight(param1View);
    }
    
    public void getDecoratedBoundsWithMargins(View param1View, Rect param1Rect) {
      RecyclerView.getDecoratedBoundsWithMarginsInt(param1View, param1Rect);
    }
    
    public int getDecoratedLeft(View param1View) {
      return param1View.getLeft() - getLeftDecorationWidth(param1View);
    }
    
    public int getDecoratedMeasuredHeight(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredHeight() + rect.top + rect.bottom;
    }
    
    public int getDecoratedMeasuredWidth(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredWidth() + rect.left + rect.right;
    }
    
    public int getDecoratedRight(View param1View) {
      return param1View.getRight() + getRightDecorationWidth(param1View);
    }
    
    public int getDecoratedTop(View param1View) {
      return param1View.getTop() - getTopDecorationHeight(param1View);
    }
    
    public View getFocusedChild() {
      if (this.mRecyclerView == null)
        return null; 
      View view = this.mRecyclerView.getFocusedChild();
      if (view != null) {
        View view1 = view;
        return this.mChildHelper.isHidden(view) ? null : view1;
      } 
      return null;
    }
    
    public int getHeight() {
      return this.mHeight;
    }
    
    public int getHeightMode() {
      return this.mHeightMode;
    }
    
    public int getItemCount() {
      RecyclerView.Adapter adapter;
      if (this.mRecyclerView != null) {
        adapter = this.mRecyclerView.getAdapter();
      } else {
        adapter = null;
      } 
      return (adapter != null) ? adapter.getItemCount() : 0;
    }
    
    public int getItemViewType(View param1View) {
      return RecyclerView.getChildViewHolderInt(param1View).getItemViewType();
    }
    
    public int getLayoutDirection() {
      return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
    }
    
    public int getLeftDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.left;
    }
    
    public int getMinimumHeight() {
      return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
    }
    
    public int getMinimumWidth() {
      return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
    }
    
    public int getPaddingBottom() {
      return (this.mRecyclerView != null) ? this.mRecyclerView.getPaddingBottom() : 0;
    }
    
    public int getPaddingEnd() {
      return (this.mRecyclerView != null) ? ViewCompat.getPaddingEnd((View)this.mRecyclerView) : 0;
    }
    
    public int getPaddingLeft() {
      return (this.mRecyclerView != null) ? this.mRecyclerView.getPaddingLeft() : 0;
    }
    
    public int getPaddingRight() {
      return (this.mRecyclerView != null) ? this.mRecyclerView.getPaddingRight() : 0;
    }
    
    public int getPaddingStart() {
      return (this.mRecyclerView != null) ? ViewCompat.getPaddingStart((View)this.mRecyclerView) : 0;
    }
    
    public int getPaddingTop() {
      return (this.mRecyclerView != null) ? this.mRecyclerView.getPaddingTop() : 0;
    }
    
    public int getPosition(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition();
    }
    
    public int getRightDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.right;
    }
    
    public int getRowCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      byte b = 1;
      int i = b;
      if (this.mRecyclerView != null) {
        if (this.mRecyclerView.mAdapter == null)
          return b; 
      } else {
        return i;
      } 
      i = b;
      if (canScrollVertically())
        i = this.mRecyclerView.mAdapter.getItemCount(); 
      return i;
    }
    
    public int getSelectionModeForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public int getTopDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.top;
    }
    
    public void getTransformedBoundingBox(View param1View, boolean param1Boolean, Rect param1Rect) {
      if (param1Boolean) {
        Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
        param1Rect.set(-rect.left, -rect.top, param1View.getWidth() + rect.right, param1View.getHeight() + rect.bottom);
      } else {
        param1Rect.set(0, 0, param1View.getWidth(), param1View.getHeight());
      } 
      if (this.mRecyclerView != null) {
        Matrix matrix = ViewCompat.getMatrix(param1View);
        if (matrix != null && !matrix.isIdentity()) {
          RectF rectF = this.mRecyclerView.mTempRectF;
          rectF.set(param1Rect);
          matrix.mapRect(rectF);
          param1Rect.set((int)Math.floor(rectF.left), (int)Math.floor(rectF.top), (int)Math.ceil(rectF.right), (int)Math.ceil(rectF.bottom));
        } 
      } 
      param1Rect.offset(param1View.getLeft(), param1View.getTop());
    }
    
    public int getWidth() {
      return this.mWidth;
    }
    
    public int getWidthMode() {
      return this.mWidthMode;
    }
    
    boolean hasFlexibleChildInBothOrientations() {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual getChildCount : ()I
      //   4: istore_1
      //   5: iconst_0
      //   6: istore_2
      //   7: iload_2
      //   8: iload_1
      //   9: if_icmpge -> 47
      //   12: aload_0
      //   13: iload_2
      //   14: invokevirtual getChildAt : (I)Landroid/view/View;
      //   17: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   20: astore_3
      //   21: aload_3
      //   22: getfield width : I
      //   25: ifge -> 41
      //   28: aload_3
      //   29: getfield height : I
      //   32: ifge -> 41
      //   35: iconst_1
      //   36: istore #4
      //   38: iload #4
      //   40: ireturn
      //   41: iinc #2, 1
      //   44: goto -> 7
      //   47: iconst_0
      //   48: istore #4
      //   50: goto -> 38
    }
    
    public boolean hasFocus() {
      return (this.mRecyclerView != null && this.mRecyclerView.hasFocus());
    }
    
    public void ignoreView(View param1View) {
      if (param1View.getParent() != this.mRecyclerView || this.mRecyclerView.indexOfChild(param1View) == -1)
        throw new IllegalArgumentException("View should be fully attached to be ignored"); 
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.addFlags(128);
      this.mRecyclerView.mViewInfoStore.removeViewHolder(viewHolder);
    }
    
    public boolean isAttachedToWindow() {
      return this.mIsAttachedToWindow;
    }
    
    public boolean isAutoMeasureEnabled() {
      return this.mAutoMeasure;
    }
    
    public boolean isFocused() {
      return (this.mRecyclerView != null && this.mRecyclerView.isFocused());
    }
    
    public final boolean isItemPrefetchEnabled() {
      return this.mItemPrefetchEnabled;
    }
    
    public boolean isLayoutHierarchical(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return false;
    }
    
    public boolean isMeasurementCacheEnabled() {
      return this.mMeasurementCacheEnabled;
    }
    
    public boolean isSmoothScrolling() {
      return (this.mSmoothScroller != null && this.mSmoothScroller.isRunning());
    }
    
    public void layoutDecorated(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      param1View.layout(rect.left + param1Int1, rect.top + param1Int2, param1Int3 - rect.right, param1Int4 - rect.bottom);
    }
    
    public void layoutDecoratedWithMargins(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = layoutParams.mDecorInsets;
      param1View.layout(rect.left + param1Int1 + layoutParams.leftMargin, rect.top + param1Int2 + layoutParams.topMargin, param1Int3 - rect.right - layoutParams.rightMargin, param1Int4 - rect.bottom - layoutParams.bottomMargin);
    }
    
    public void measureChild(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int i = rect.left;
      int j = rect.right;
      int k = rect.top;
      int m = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + param1Int1 + i + j, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + param1Int2 + k + m, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void measureChildWithMargins(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int i = rect.left;
      int j = rect.right;
      int k = rect.top;
      int m = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + param1Int1 + i + j, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + param1Int2 + k + m, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void moveView(int param1Int1, int param1Int2) {
      View view = getChildAt(param1Int1);
      if (view == null)
        throw new IllegalArgumentException("Cannot move a child from non-existing index:" + param1Int1); 
      detachViewAt(param1Int1);
      attachView(view, param1Int2);
    }
    
    public void offsetChildrenHorizontal(int param1Int) {
      if (this.mRecyclerView != null)
        this.mRecyclerView.offsetChildrenHorizontal(param1Int); 
    }
    
    public void offsetChildrenVertical(int param1Int) {
      if (this.mRecyclerView != null)
        this.mRecyclerView.offsetChildrenVertical(param1Int); 
    }
    
    public void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2) {}
    
    public boolean onAddFocusables(RecyclerView param1RecyclerView, ArrayList<View> param1ArrayList, int param1Int1, int param1Int2) {
      return false;
    }
    
    @CallSuper
    public void onAttachedToWindow(RecyclerView param1RecyclerView) {}
    
    @Deprecated
    public void onDetachedFromWindow(RecyclerView param1RecyclerView) {}
    
    @CallSuper
    public void onDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      onDetachedFromWindow(param1RecyclerView);
    }
    
    @Nullable
    public View onFocusSearchFailed(View param1View, int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return null;
    }
    
    public void onInitializeAccessibilityEvent(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityEvent param1AccessibilityEvent) {
      boolean bool = true;
      AccessibilityRecordCompat accessibilityRecordCompat = AccessibilityEventCompat.asRecord(param1AccessibilityEvent);
      if (this.mRecyclerView != null && accessibilityRecordCompat != null) {
        boolean bool1 = bool;
        if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, 1)) {
          bool1 = bool;
          if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, -1)) {
            bool1 = bool;
            if (!ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1))
              if (ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) {
                bool1 = bool;
              } else {
                bool1 = false;
              }  
          } 
        } 
        accessibilityRecordCompat.setScrollable(bool1);
        if (this.mRecyclerView.mAdapter != null)
          accessibilityRecordCompat.setItemCount(this.mRecyclerView.mAdapter.getItemCount()); 
      } 
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityEvent);
    }
    
    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityNodeInfoCompat);
    }
    
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (ViewCompat.canScrollVertically((View)this.mRecyclerView, -1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
        param1AccessibilityNodeInfoCompat.addAction(8192);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      if (ViewCompat.canScrollVertically((View)this.mRecyclerView, 1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) {
        param1AccessibilityNodeInfoCompat.addAction(4096);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      param1AccessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(param1Recycler, param1State), getColumnCountForAccessibility(param1Recycler, param1State), isLayoutHierarchical(param1Recycler, param1State), getSelectionModeForAccessibility(param1Recycler, param1State)));
    }
    
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool1;
      boolean bool2;
      if (canScrollVertically()) {
        bool1 = getPosition(param1View);
      } else {
        bool1 = false;
      } 
      if (canScrollHorizontally()) {
        bool2 = getPosition(param1View);
      } else {
        bool2 = false;
      } 
      param1AccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(bool1, 1, bool2, 1, false, false));
    }
    
    void onInitializeAccessibilityNodeInfoForItem(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder != null && !viewHolder.isRemoved() && !this.mChildHelper.isHidden(viewHolder.itemView))
        onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1AccessibilityNodeInfoCompat); 
    }
    
    public View onInterceptFocusSearch(View param1View, int param1Int) {
      return null;
    }
    
    public void onItemsAdded(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsChanged(RecyclerView param1RecyclerView) {}
    
    public void onItemsMoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemsRemoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, Object param1Object) {
      onItemsUpdated(param1RecyclerView, param1Int1, param1Int2);
    }
    
    public void onLayoutChildren(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
    }
    
    public void onLayoutCompleted(RecyclerView.State param1State) {}
    
    public void onMeasure(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int1, int param1Int2) {
      this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
    }
    
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, RecyclerView.State param1State, View param1View1, View param1View2) {
      return onRequestChildFocus(param1RecyclerView, param1View1, param1View2);
    }
    
    @Deprecated
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, View param1View1, View param1View2) {
      return (isSmoothScrolling() || param1RecyclerView.isComputingLayout());
    }
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public void onScrollStateChanged(int param1Int) {}
    
    boolean performAccessibilityAction(int param1Int, Bundle param1Bundle) {
      return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1Int, param1Bundle);
    }
    
    public boolean performAccessibilityAction(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int, Bundle param1Bundle) {
      // Byte code:
      //   0: iconst_0
      //   1: istore #5
      //   3: aload_0
      //   4: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   7: ifnonnull -> 13
      //   10: iload #5
      //   12: ireturn
      //   13: iconst_0
      //   14: istore #6
      //   16: iconst_0
      //   17: istore #7
      //   19: iconst_0
      //   20: istore #8
      //   22: iconst_0
      //   23: istore #9
      //   25: iload_3
      //   26: lookupswitch default -> 52, 4096 -> 149, 8192 -> 80
      //   52: iload #8
      //   54: istore_3
      //   55: iload_3
      //   56: ifne -> 64
      //   59: iload #9
      //   61: ifeq -> 10
      //   64: aload_0
      //   65: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   68: iload #9
      //   70: iload_3
      //   71: invokevirtual scrollBy : (II)V
      //   74: iconst_1
      //   75: istore #5
      //   77: goto -> 10
      //   80: iload #6
      //   82: istore #8
      //   84: aload_0
      //   85: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   88: iconst_m1
      //   89: invokestatic canScrollVertically : (Landroid/view/View;I)Z
      //   92: ifeq -> 112
      //   95: aload_0
      //   96: invokevirtual getHeight : ()I
      //   99: aload_0
      //   100: invokevirtual getPaddingTop : ()I
      //   103: isub
      //   104: aload_0
      //   105: invokevirtual getPaddingBottom : ()I
      //   108: isub
      //   109: ineg
      //   110: istore #8
      //   112: iload #8
      //   114: istore_3
      //   115: aload_0
      //   116: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   119: iconst_m1
      //   120: invokestatic canScrollHorizontally : (Landroid/view/View;I)Z
      //   123: ifeq -> 55
      //   126: aload_0
      //   127: invokevirtual getWidth : ()I
      //   130: aload_0
      //   131: invokevirtual getPaddingLeft : ()I
      //   134: isub
      //   135: aload_0
      //   136: invokevirtual getPaddingRight : ()I
      //   139: isub
      //   140: ineg
      //   141: istore #9
      //   143: iload #8
      //   145: istore_3
      //   146: goto -> 55
      //   149: iload #7
      //   151: istore #8
      //   153: aload_0
      //   154: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   157: iconst_1
      //   158: invokestatic canScrollVertically : (Landroid/view/View;I)Z
      //   161: ifeq -> 180
      //   164: aload_0
      //   165: invokevirtual getHeight : ()I
      //   168: aload_0
      //   169: invokevirtual getPaddingTop : ()I
      //   172: isub
      //   173: aload_0
      //   174: invokevirtual getPaddingBottom : ()I
      //   177: isub
      //   178: istore #8
      //   180: iload #8
      //   182: istore_3
      //   183: aload_0
      //   184: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   187: iconst_1
      //   188: invokestatic canScrollHorizontally : (Landroid/view/View;I)Z
      //   191: ifeq -> 55
      //   194: aload_0
      //   195: invokevirtual getWidth : ()I
      //   198: aload_0
      //   199: invokevirtual getPaddingLeft : ()I
      //   202: isub
      //   203: aload_0
      //   204: invokevirtual getPaddingRight : ()I
      //   207: isub
      //   208: istore #9
      //   210: iload #8
      //   212: istore_3
      //   213: goto -> 55
    }
    
    public boolean performAccessibilityActionForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, int param1Int, Bundle param1Bundle) {
      return false;
    }
    
    boolean performAccessibilityActionForItem(View param1View, int param1Int, Bundle param1Bundle) {
      return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1Int, param1Bundle);
    }
    
    public void postOnAnimation(Runnable param1Runnable) {
      if (this.mRecyclerView != null)
        ViewCompat.postOnAnimation((View)this.mRecyclerView, param1Runnable); 
    }
    
    public void removeAllViews() {
      for (int i = getChildCount() - 1; i >= 0; i--)
        this.mChildHelper.removeViewAt(i); 
    }
    
    public void removeAndRecycleAllViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore())
          removeAndRecycleViewAt(i, param1Recycler); 
      } 
    }
    
    void removeAndRecycleScrapInt(RecyclerView.Recycler param1Recycler) {
      int i = param1Recycler.getScrapCount();
      for (int j = i - 1; j >= 0; j--) {
        View view = param1Recycler.getScrapViewAt(j);
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (!viewHolder.shouldIgnore()) {
          viewHolder.setIsRecyclable(false);
          if (viewHolder.isTmpDetached())
            this.mRecyclerView.removeDetachedView(view, false); 
          if (this.mRecyclerView.mItemAnimator != null)
            this.mRecyclerView.mItemAnimator.endAnimation(viewHolder); 
          viewHolder.setIsRecyclable(true);
          param1Recycler.quickRecycleScrapView(view);
        } 
      } 
      param1Recycler.clearScrap();
      if (i > 0)
        this.mRecyclerView.invalidate(); 
    }
    
    public void removeAndRecycleView(View param1View, RecyclerView.Recycler param1Recycler) {
      removeView(param1View);
      param1Recycler.recycleView(param1View);
    }
    
    public void removeAndRecycleViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      View view = getChildAt(param1Int);
      removeViewAt(param1Int);
      param1Recycler.recycleView(view);
    }
    
    public boolean removeCallbacks(Runnable param1Runnable) {
      return (this.mRecyclerView != null) ? this.mRecyclerView.removeCallbacks(param1Runnable) : false;
    }
    
    public void removeDetachedView(View param1View) {
      this.mRecyclerView.removeDetachedView(param1View, false);
    }
    
    public void removeView(View param1View) {
      this.mChildHelper.removeView(param1View);
    }
    
    public void removeViewAt(int param1Int) {
      if (getChildAt(param1Int) != null)
        this.mChildHelper.removeViewAt(param1Int); 
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean) {
      int i = getPaddingLeft();
      int j = getPaddingTop();
      int k = getWidth() - getPaddingRight();
      int m = getHeight();
      int n = getPaddingBottom();
      int i1 = param1View.getLeft() + param1Rect.left - param1View.getScrollX();
      int i2 = param1View.getTop() + param1Rect.top - param1View.getScrollY();
      int i3 = i1 + param1Rect.width();
      int i4 = param1Rect.height();
      int i5 = Math.min(0, i1 - i);
      int i6 = Math.min(0, i2 - j);
      int i7 = Math.max(0, i3 - k);
      i4 = Math.max(0, i2 + i4 - m - n);
      if (getLayoutDirection() == 1) {
        if (i7 == 0)
          i7 = Math.max(i5, i3 - k); 
      } else if (i5 != 0) {
        i7 = i5;
      } else {
        i7 = Math.min(i1 - i, i7);
      } 
      if (i6 == 0)
        i6 = Math.min(i2 - j, i4); 
      if (i7 != 0 || i6 != 0) {
        if (param1Boolean) {
          param1RecyclerView.scrollBy(i7, i6);
        } else {
          param1RecyclerView.smoothScrollBy(i7, i6);
        } 
        return true;
      } 
      return false;
    }
    
    public void requestLayout() {
      if (this.mRecyclerView != null)
        this.mRecyclerView.requestLayout(); 
    }
    
    public void requestSimpleAnimationsInNextLayout() {
      this.mRequestedSimpleAnimations = true;
    }
    
    public int scrollHorizontallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public void scrollToPosition(int param1Int) {}
    
    public int scrollVerticallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public void setAutoMeasureEnabled(boolean param1Boolean) {
      this.mAutoMeasure = param1Boolean;
    }
    
    void setExactMeasureSpecsFrom(RecyclerView param1RecyclerView) {
      setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getHeight(), 1073741824));
    }
    
    public final void setItemPrefetchEnabled(boolean param1Boolean) {
      if (param1Boolean != this.mItemPrefetchEnabled) {
        this.mItemPrefetchEnabled = param1Boolean;
        this.mPrefetchMaxCountObserved = 0;
        if (this.mRecyclerView != null)
          this.mRecyclerView.mRecycler.updateViewCacheSize(); 
      } 
    }
    
    void setMeasureSpecs(int param1Int1, int param1Int2) {
      this.mWidth = View.MeasureSpec.getSize(param1Int1);
      this.mWidthMode = View.MeasureSpec.getMode(param1Int1);
      if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mWidth = 0; 
      this.mHeight = View.MeasureSpec.getSize(param1Int2);
      this.mHeightMode = View.MeasureSpec.getMode(param1Int2);
      if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mHeight = 0; 
    }
    
    public void setMeasuredDimension(int param1Int1, int param1Int2) {
      this.mRecyclerView.setMeasuredDimension(param1Int1, param1Int2);
    }
    
    public void setMeasuredDimension(Rect param1Rect, int param1Int1, int param1Int2) {
      int i = param1Rect.width();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = param1Rect.height();
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      setMeasuredDimension(chooseSize(param1Int1, i + j + k, getMinimumWidth()), chooseSize(param1Int2, m + n + i1, getMinimumHeight()));
    }
    
    void setMeasuredDimensionFromChildren(int param1Int1, int param1Int2) {
      int i = getChildCount();
      if (i == 0) {
        this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
        return;
      } 
      int j = Integer.MAX_VALUE;
      int k = Integer.MAX_VALUE;
      int m = Integer.MIN_VALUE;
      int n = Integer.MIN_VALUE;
      byte b = 0;
      while (b < i) {
        View view = getChildAt(b);
        Rect rect = this.mRecyclerView.mTempRect;
        getDecoratedBoundsWithMargins(view, rect);
        int i1 = j;
        if (rect.left < j)
          i1 = rect.left; 
        j = m;
        if (rect.right > m)
          j = rect.right; 
        int i2 = k;
        if (rect.top < k)
          i2 = rect.top; 
        k = n;
        if (rect.bottom > n)
          k = rect.bottom; 
        b++;
        m = j;
        n = k;
        j = i1;
        k = i2;
      } 
      this.mRecyclerView.mTempRect.set(j, k, m, n);
      setMeasuredDimension(this.mRecyclerView.mTempRect, param1Int1, param1Int2);
    }
    
    public void setMeasurementCacheEnabled(boolean param1Boolean) {
      this.mMeasurementCacheEnabled = param1Boolean;
    }
    
    void setRecyclerView(RecyclerView param1RecyclerView) {
      if (param1RecyclerView == null) {
        this.mRecyclerView = null;
        this.mChildHelper = null;
        this.mWidth = 0;
        this.mHeight = 0;
      } else {
        this.mRecyclerView = param1RecyclerView;
        this.mChildHelper = param1RecyclerView.mChildHelper;
        this.mWidth = param1RecyclerView.getWidth();
        this.mHeight = param1RecyclerView.getHeight();
      } 
      this.mWidthMode = 1073741824;
      this.mHeightMode = 1073741824;
    }
    
    boolean shouldMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (param1View.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getHeight(), param1Int2, param1LayoutParams.height));
    }
    
    boolean shouldMeasureTwice() {
      return false;
    }
    
    boolean shouldReMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (!this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getMeasuredWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getMeasuredHeight(), param1Int2, param1LayoutParams.height));
    }
    
    public void smoothScrollToPosition(RecyclerView param1RecyclerView, RecyclerView.State param1State, int param1Int) {
      Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
    }
    
    public void startSmoothScroll(RecyclerView.SmoothScroller param1SmoothScroller) {
      if (this.mSmoothScroller != null && param1SmoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning())
        this.mSmoothScroller.stop(); 
      this.mSmoothScroller = param1SmoothScroller;
      this.mSmoothScroller.start(this.mRecyclerView, this);
    }
    
    public void stopIgnoringView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.stopIgnoring();
      viewHolder.resetInternal();
      viewHolder.addFlags(4);
    }
    
    void stopSmoothScroller() {
      if (this.mSmoothScroller != null)
        this.mSmoothScroller.stop(); 
    }
    
    public boolean supportsPredictiveItemAnimations() {
      return false;
    }
    
    public static interface LayoutPrefetchRegistry {
      void addPosition(int param2Int1, int param2Int2);
    }
    
    public static class Properties {
      public int orientation;
      
      public boolean reverseLayout;
      
      public int spanCount;
      
      public boolean stackFromEnd;
    }
  }
  
  public static interface LayoutPrefetchRegistry {
    void addPosition(int param1Int1, int param1Int2);
  }
  
  public static class Properties {
    public int orientation;
    
    public boolean reverseLayout;
    
    public int spanCount;
    
    public boolean stackFromEnd;
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    final Rect mDecorInsets = new Rect();
    
    boolean mInsetsDirty = true;
    
    boolean mPendingInvalidate = false;
    
    RecyclerView.ViewHolder mViewHolder;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public int getViewAdapterPosition() {
      return this.mViewHolder.getAdapterPosition();
    }
    
    public int getViewLayoutPosition() {
      return this.mViewHolder.getLayoutPosition();
    }
    
    @Deprecated
    public int getViewPosition() {
      return this.mViewHolder.getPosition();
    }
    
    public boolean isItemChanged() {
      return this.mViewHolder.isUpdated();
    }
    
    public boolean isItemRemoved() {
      return this.mViewHolder.isRemoved();
    }
    
    public boolean isViewInvalid() {
      return this.mViewHolder.isInvalid();
    }
    
    public boolean viewNeedsUpdate() {
      return this.mViewHolder.needsUpdate();
    }
  }
  
  public static interface OnChildAttachStateChangeListener {
    void onChildViewAttachedToWindow(View param1View);
    
    void onChildViewDetachedFromWindow(View param1View);
  }
  
  public static abstract class OnFlingListener {
    public abstract boolean onFling(int param1Int1, int param1Int2);
  }
  
  public static interface OnItemTouchListener {
    boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
    
    void onRequestDisallowInterceptTouchEvent(boolean param1Boolean);
    
    void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
  }
  
  public static abstract class OnScrollListener {
    public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {}
    
    public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
  }
  
  public static class RecycledViewPool {
    private static final int DEFAULT_MAX_SCRAP = 5;
    
    private int mAttachCount = 0;
    
    SparseArray<ScrapData> mScrap = new SparseArray();
    
    private ScrapData getScrapDataForType(int param1Int) {
      ScrapData scrapData1 = (ScrapData)this.mScrap.get(param1Int);
      ScrapData scrapData2 = scrapData1;
      if (scrapData1 == null) {
        scrapData2 = new ScrapData();
        this.mScrap.put(param1Int, scrapData2);
      } 
      return scrapData2;
    }
    
    void attach(RecyclerView.Adapter param1Adapter) {
      this.mAttachCount++;
    }
    
    public void clear() {
      for (byte b = 0; b < this.mScrap.size(); b++)
        ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap.clear(); 
    }
    
    void detach() {
      this.mAttachCount--;
    }
    
    void factorInBindTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mBindRunningAverageNs = runningAverage(scrapData.mBindRunningAverageNs, param1Long);
    }
    
    void factorInCreateTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mCreateRunningAverageNs = runningAverage(scrapData.mCreateRunningAverageNs, param1Long);
    }
    
    public RecyclerView.ViewHolder getRecycledView(int param1Int) {
      null = (ScrapData)this.mScrap.get(param1Int);
      if (null != null && !null.mScrapHeap.isEmpty()) {
        ArrayList<RecyclerView.ViewHolder> arrayList = null.mScrapHeap;
        return arrayList.remove(arrayList.size() - 1);
      } 
      return null;
    }
    
    public int getRecycledViewCount(int param1Int) {
      return (getScrapDataForType(param1Int)).mScrapHeap.size();
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      if (param1Adapter1 != null)
        detach(); 
      if (!param1Boolean && this.mAttachCount == 0)
        clear(); 
      if (param1Adapter2 != null)
        attach(param1Adapter2); 
    }
    
    public void putRecycledView(RecyclerView.ViewHolder param1ViewHolder) {
      int i = param1ViewHolder.getItemViewType();
      ArrayList<RecyclerView.ViewHolder> arrayList = (getScrapDataForType(i)).mScrapHeap;
      if (((ScrapData)this.mScrap.get(i)).mMaxScrap > arrayList.size()) {
        param1ViewHolder.resetInternal();
        arrayList.add(param1ViewHolder);
      } 
    }
    
    long runningAverage(long param1Long1, long param1Long2) {
      if (param1Long1 != 0L)
        param1Long2 = param1Long1 / 4L * 3L + param1Long2 / 4L; 
      return param1Long2;
    }
    
    public void setMaxRecycledViews(int param1Int1, int param1Int2) {
      ScrapData scrapData = getScrapDataForType(param1Int1);
      scrapData.mMaxScrap = param1Int2;
      ArrayList<RecyclerView.ViewHolder> arrayList = scrapData.mScrapHeap;
      if (arrayList != null)
        while (arrayList.size() > param1Int2)
          arrayList.remove(arrayList.size() - 1);  
    }
    
    int size() {
      int i = 0;
      byte b = 0;
      while (b < this.mScrap.size()) {
        ArrayList<RecyclerView.ViewHolder> arrayList = ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap;
        int j = i;
        if (arrayList != null)
          j = i + arrayList.size(); 
        b++;
        i = j;
      } 
      return i;
    }
    
    boolean willBindInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mBindRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    boolean willCreateInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mCreateRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    static class ScrapData {
      long mBindRunningAverageNs = 0L;
      
      long mCreateRunningAverageNs = 0L;
      
      int mMaxScrap = 5;
      
      ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<RecyclerView.ViewHolder>();
    }
  }
  
  static class ScrapData {
    long mBindRunningAverageNs = 0L;
    
    long mCreateRunningAverageNs = 0L;
    
    int mMaxScrap = 5;
    
    ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<RecyclerView.ViewHolder>();
  }
  
  public final class Recycler {
    static final int DEFAULT_CACHE_SIZE = 2;
    
    final ArrayList<RecyclerView.ViewHolder> mAttachedScrap = new ArrayList<RecyclerView.ViewHolder>();
    
    final ArrayList<RecyclerView.ViewHolder> mCachedViews = new ArrayList<RecyclerView.ViewHolder>();
    
    ArrayList<RecyclerView.ViewHolder> mChangedScrap = null;
    
    RecyclerView.RecycledViewPool mRecyclerPool;
    
    private int mRequestedCacheMax = 2;
    
    private final List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
    
    private RecyclerView.ViewCacheExtension mViewCacheExtension;
    
    int mViewCacheMax = 2;
    
    private void attachAccessibilityDelegate(View param1View) {
      if (RecyclerView.this.isAccessibilityEnabled()) {
        if (ViewCompat.getImportantForAccessibility(param1View) == 0)
          ViewCompat.setImportantForAccessibility(param1View, 1); 
        if (!ViewCompat.hasAccessibilityDelegate(param1View))
          ViewCompat.setAccessibilityDelegate(param1View, RecyclerView.this.mAccessibilityDelegate.getItemDelegate()); 
      } 
    }
    
    private void invalidateDisplayListInt(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.itemView instanceof ViewGroup)
        invalidateDisplayListInt((ViewGroup)param1ViewHolder.itemView, false); 
    }
    
    private void invalidateDisplayListInt(ViewGroup param1ViewGroup, boolean param1Boolean) {
      int i;
      for (i = param1ViewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = param1ViewGroup.getChildAt(i);
        if (view instanceof ViewGroup)
          invalidateDisplayListInt((ViewGroup)view, true); 
      } 
      if (param1Boolean) {
        if (param1ViewGroup.getVisibility() == 4) {
          param1ViewGroup.setVisibility(0);
          param1ViewGroup.setVisibility(4);
          return;
        } 
        i = param1ViewGroup.getVisibility();
        param1ViewGroup.setVisibility(4);
        param1ViewGroup.setVisibility(i);
      } 
    }
    
    private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, long param1Long) {
      param1ViewHolder.mOwnerRecyclerView = RecyclerView.this;
      int i = param1ViewHolder.getItemViewType();
      long l = RecyclerView.this.getNanoTime();
      if (param1Long != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(i, l, param1Long))
        return false; 
      RecyclerView.this.mAdapter.bindViewHolder(param1ViewHolder, param1Int1);
      param1Long = RecyclerView.this.getNanoTime();
      this.mRecyclerPool.factorInBindTime(param1ViewHolder.getItemViewType(), param1Long - l);
      attachAccessibilityDelegate(param1ViewHolder.itemView);
      if (RecyclerView.this.mState.isPreLayout())
        param1ViewHolder.mPreLayoutPosition = param1Int2; 
      return true;
    }
    
    void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder param1ViewHolder, boolean param1Boolean) {
      RecyclerView.clearNestedRecyclerViewIfNotNested(param1ViewHolder);
      ViewCompat.setAccessibilityDelegate(param1ViewHolder.itemView, null);
      if (param1Boolean)
        dispatchViewRecycled(param1ViewHolder); 
      param1ViewHolder.mOwnerRecyclerView = null;
      getRecycledViewPool().putRecycledView(param1ViewHolder);
    }
    
    public void bindViewToPosition(View param1View, int param1Int) {
      RecyclerView.LayoutParams layoutParams;
      boolean bool;
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder == null)
        throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter"); 
      int i = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
      if (i < 0 || i >= RecyclerView.this.mAdapter.getItemCount())
        throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + param1Int + "(offset:" + i + ")." + "state:" + RecyclerView.this.mState.getItemCount()); 
      tryBindViewHolderByDeadline(viewHolder, i, param1Int, Long.MAX_VALUE);
      ViewGroup.LayoutParams layoutParams1 = viewHolder.itemView.getLayoutParams();
      if (layoutParams1 == null) {
        layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
        viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
        layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
        viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } else {
        layoutParams = layoutParams;
      } 
      layoutParams.mInsetsDirty = true;
      layoutParams.mViewHolder = viewHolder;
      if (viewHolder.itemView.getParent() == null) {
        bool = true;
      } else {
        bool = false;
      } 
      layoutParams.mPendingInvalidate = bool;
    }
    
    public void clear() {
      this.mAttachedScrap.clear();
      recycleAndClearCachedViews();
    }
    
    void clearOldPositions() {
      int i = this.mCachedViews.size();
      byte b;
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mCachedViews.get(b)).clearOldPosition(); 
      i = this.mAttachedScrap.size();
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mAttachedScrap.get(b)).clearOldPosition(); 
      if (this.mChangedScrap != null) {
        i = this.mChangedScrap.size();
        for (b = 0; b < i; b++)
          ((RecyclerView.ViewHolder)this.mChangedScrap.get(b)).clearOldPosition(); 
      } 
    }
    
    void clearScrap() {
      this.mAttachedScrap.clear();
      if (this.mChangedScrap != null)
        this.mChangedScrap.clear(); 
    }
    
    public int convertPreLayoutPositionToPostLayout(int param1Int) {
      if (param1Int < 0 || param1Int >= RecyclerView.this.mState.getItemCount())
        throw new IndexOutOfBoundsException("invalid position " + param1Int + ". State " + "item count is " + RecyclerView.this.mState.getItemCount()); 
      if (RecyclerView.this.mState.isPreLayout())
        param1Int = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int); 
      return param1Int;
    }
    
    void dispatchViewRecycled(RecyclerView.ViewHolder param1ViewHolder) {
      if (RecyclerView.this.mRecyclerListener != null)
        RecyclerView.this.mRecyclerListener.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mAdapter != null)
        RecyclerView.this.mAdapter.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mState != null)
        RecyclerView.this.mViewInfoStore.removeViewHolder(param1ViewHolder); 
    }
    
    RecyclerView.ViewHolder getChangedScrapViewForPosition(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mChangedScrap : Ljava/util/ArrayList;
      //   4: ifnull -> 19
      //   7: aload_0
      //   8: getfield mChangedScrap : Ljava/util/ArrayList;
      //   11: invokevirtual size : ()I
      //   14: istore_2
      //   15: iload_2
      //   16: ifne -> 23
      //   19: aconst_null
      //   20: astore_3
      //   21: aload_3
      //   22: areturn
      //   23: iconst_0
      //   24: istore #4
      //   26: iload #4
      //   28: iload_2
      //   29: if_icmpge -> 75
      //   32: aload_0
      //   33: getfield mChangedScrap : Ljava/util/ArrayList;
      //   36: iload #4
      //   38: invokevirtual get : (I)Ljava/lang/Object;
      //   41: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   44: astore_3
      //   45: aload_3
      //   46: invokevirtual wasReturnedFromScrap : ()Z
      //   49: ifne -> 69
      //   52: aload_3
      //   53: invokevirtual getLayoutPosition : ()I
      //   56: iload_1
      //   57: if_icmpne -> 69
      //   60: aload_3
      //   61: bipush #32
      //   63: invokevirtual addFlags : (I)V
      //   66: goto -> 21
      //   69: iinc #4, 1
      //   72: goto -> 26
      //   75: aload_0
      //   76: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   79: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
      //   82: invokevirtual hasStableIds : ()Z
      //   85: ifeq -> 182
      //   88: aload_0
      //   89: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   92: getfield mAdapterHelper : Landroid/support/v7/widget/AdapterHelper;
      //   95: iload_1
      //   96: invokevirtual findPositionOffset : (I)I
      //   99: istore_1
      //   100: iload_1
      //   101: ifle -> 182
      //   104: iload_1
      //   105: aload_0
      //   106: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   109: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
      //   112: invokevirtual getItemCount : ()I
      //   115: if_icmpge -> 182
      //   118: aload_0
      //   119: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   122: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
      //   125: iload_1
      //   126: invokevirtual getItemId : (I)J
      //   129: lstore #5
      //   131: iconst_0
      //   132: istore_1
      //   133: iload_1
      //   134: iload_2
      //   135: if_icmpge -> 182
      //   138: aload_0
      //   139: getfield mChangedScrap : Ljava/util/ArrayList;
      //   142: iload_1
      //   143: invokevirtual get : (I)Ljava/lang/Object;
      //   146: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   149: astore_3
      //   150: aload_3
      //   151: invokevirtual wasReturnedFromScrap : ()Z
      //   154: ifne -> 176
      //   157: aload_3
      //   158: invokevirtual getItemId : ()J
      //   161: lload #5
      //   163: lcmp
      //   164: ifne -> 176
      //   167: aload_3
      //   168: bipush #32
      //   170: invokevirtual addFlags : (I)V
      //   173: goto -> 21
      //   176: iinc #1, 1
      //   179: goto -> 133
      //   182: aconst_null
      //   183: astore_3
      //   184: goto -> 21
    }
    
    RecyclerView.RecycledViewPool getRecycledViewPool() {
      if (this.mRecyclerPool == null)
        this.mRecyclerPool = new RecyclerView.RecycledViewPool(); 
      return this.mRecyclerPool;
    }
    
    int getScrapCount() {
      return this.mAttachedScrap.size();
    }
    
    public List<RecyclerView.ViewHolder> getScrapList() {
      return this.mUnmodifiableAttachedScrap;
    }
    
    RecyclerView.ViewHolder getScrapOrCachedViewForId(long param1Long, int param1Int, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mAttachedScrap : Ljava/util/ArrayList;
      //   4: invokevirtual size : ()I
      //   7: iconst_1
      //   8: isub
      //   9: istore #5
      //   11: iload #5
      //   13: iflt -> 151
      //   16: aload_0
      //   17: getfield mAttachedScrap : Ljava/util/ArrayList;
      //   20: iload #5
      //   22: invokevirtual get : (I)Ljava/lang/Object;
      //   25: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   28: astore #6
      //   30: aload #6
      //   32: invokevirtual getItemId : ()J
      //   35: lload_1
      //   36: lcmp
      //   37: ifne -> 145
      //   40: aload #6
      //   42: invokevirtual wasReturnedFromScrap : ()Z
      //   45: ifne -> 145
      //   48: iload_3
      //   49: aload #6
      //   51: invokevirtual getItemViewType : ()I
      //   54: if_icmpne -> 108
      //   57: aload #6
      //   59: bipush #32
      //   61: invokevirtual addFlags : (I)V
      //   64: aload #6
      //   66: astore #7
      //   68: aload #6
      //   70: invokevirtual isRemoved : ()Z
      //   73: ifeq -> 105
      //   76: aload #6
      //   78: astore #7
      //   80: aload_0
      //   81: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   84: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
      //   87: invokevirtual isPreLayout : ()Z
      //   90: ifne -> 105
      //   93: aload #6
      //   95: iconst_2
      //   96: bipush #14
      //   98: invokevirtual setFlags : (II)V
      //   101: aload #6
      //   103: astore #7
      //   105: aload #7
      //   107: areturn
      //   108: iload #4
      //   110: ifne -> 145
      //   113: aload_0
      //   114: getfield mAttachedScrap : Ljava/util/ArrayList;
      //   117: iload #5
      //   119: invokevirtual remove : (I)Ljava/lang/Object;
      //   122: pop
      //   123: aload_0
      //   124: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   127: aload #6
      //   129: getfield itemView : Landroid/view/View;
      //   132: iconst_0
      //   133: invokevirtual removeDetachedView : (Landroid/view/View;Z)V
      //   136: aload_0
      //   137: aload #6
      //   139: getfield itemView : Landroid/view/View;
      //   142: invokevirtual quickRecycleScrapView : (Landroid/view/View;)V
      //   145: iinc #5, -1
      //   148: goto -> 11
      //   151: aload_0
      //   152: getfield mCachedViews : Ljava/util/ArrayList;
      //   155: invokevirtual size : ()I
      //   158: iconst_1
      //   159: isub
      //   160: istore #5
      //   162: iload #5
      //   164: iflt -> 249
      //   167: aload_0
      //   168: getfield mCachedViews : Ljava/util/ArrayList;
      //   171: iload #5
      //   173: invokevirtual get : (I)Ljava/lang/Object;
      //   176: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   179: astore #6
      //   181: aload #6
      //   183: invokevirtual getItemId : ()J
      //   186: lload_1
      //   187: lcmp
      //   188: ifne -> 243
      //   191: iload_3
      //   192: aload #6
      //   194: invokevirtual getItemViewType : ()I
      //   197: if_icmpne -> 226
      //   200: aload #6
      //   202: astore #7
      //   204: iload #4
      //   206: ifne -> 105
      //   209: aload_0
      //   210: getfield mCachedViews : Ljava/util/ArrayList;
      //   213: iload #5
      //   215: invokevirtual remove : (I)Ljava/lang/Object;
      //   218: pop
      //   219: aload #6
      //   221: astore #7
      //   223: goto -> 105
      //   226: iload #4
      //   228: ifne -> 243
      //   231: aload_0
      //   232: iload #5
      //   234: invokevirtual recycleCachedViewAt : (I)V
      //   237: aconst_null
      //   238: astore #7
      //   240: goto -> 105
      //   243: iinc #5, -1
      //   246: goto -> 162
      //   249: aconst_null
      //   250: astore #7
      //   252: goto -> 105
    }
    
    RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int param1Int, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mAttachedScrap : Ljava/util/ArrayList;
      //   4: invokevirtual size : ()I
      //   7: istore_3
      //   8: iconst_0
      //   9: istore #4
      //   11: iload #4
      //   13: iload_3
      //   14: if_icmpge -> 93
      //   17: aload_0
      //   18: getfield mAttachedScrap : Ljava/util/ArrayList;
      //   21: iload #4
      //   23: invokevirtual get : (I)Ljava/lang/Object;
      //   26: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   29: astore #5
      //   31: aload #5
      //   33: invokevirtual wasReturnedFromScrap : ()Z
      //   36: ifne -> 87
      //   39: aload #5
      //   41: invokevirtual getLayoutPosition : ()I
      //   44: iload_1
      //   45: if_icmpne -> 87
      //   48: aload #5
      //   50: invokevirtual isInvalid : ()Z
      //   53: ifne -> 87
      //   56: aload_0
      //   57: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   60: getfield mState : Landroid/support/v7/widget/RecyclerView$State;
      //   63: getfield mInPreLayout : Z
      //   66: ifne -> 77
      //   69: aload #5
      //   71: invokevirtual isRemoved : ()Z
      //   74: ifne -> 87
      //   77: aload #5
      //   79: bipush #32
      //   81: invokevirtual addFlags : (I)V
      //   84: aload #5
      //   86: areturn
      //   87: iinc #4, 1
      //   90: goto -> 11
      //   93: iload_2
      //   94: ifne -> 209
      //   97: aload_0
      //   98: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   101: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
      //   104: iload_1
      //   105: invokevirtual findHiddenNonRemovedView : (I)Landroid/view/View;
      //   108: astore #6
      //   110: aload #6
      //   112: ifnull -> 209
      //   115: aload #6
      //   117: invokestatic getChildViewHolderInt : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
      //   120: astore #5
      //   122: aload_0
      //   123: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   126: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
      //   129: aload #6
      //   131: invokevirtual unhide : (Landroid/view/View;)V
      //   134: aload_0
      //   135: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   138: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
      //   141: aload #6
      //   143: invokevirtual indexOfChild : (Landroid/view/View;)I
      //   146: istore_1
      //   147: iload_1
      //   148: iconst_m1
      //   149: if_icmpne -> 181
      //   152: new java/lang/IllegalStateException
      //   155: dup
      //   156: new java/lang/StringBuilder
      //   159: dup
      //   160: invokespecial <init> : ()V
      //   163: ldc_w 'layout index should not be -1 after unhiding a view:'
      //   166: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   169: aload #5
      //   171: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   174: invokevirtual toString : ()Ljava/lang/String;
      //   177: invokespecial <init> : (Ljava/lang/String;)V
      //   180: athrow
      //   181: aload_0
      //   182: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   185: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
      //   188: iload_1
      //   189: invokevirtual detachViewFromParent : (I)V
      //   192: aload_0
      //   193: aload #6
      //   195: invokevirtual scrapView : (Landroid/view/View;)V
      //   198: aload #5
      //   200: sipush #8224
      //   203: invokevirtual addFlags : (I)V
      //   206: goto -> 84
      //   209: aload_0
      //   210: getfield mCachedViews : Ljava/util/ArrayList;
      //   213: invokevirtual size : ()I
      //   216: istore_3
      //   217: iconst_0
      //   218: istore #4
      //   220: iload #4
      //   222: iload_3
      //   223: if_icmpge -> 288
      //   226: aload_0
      //   227: getfield mCachedViews : Ljava/util/ArrayList;
      //   230: iload #4
      //   232: invokevirtual get : (I)Ljava/lang/Object;
      //   235: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   238: astore #6
      //   240: aload #6
      //   242: invokevirtual isInvalid : ()Z
      //   245: ifne -> 282
      //   248: aload #6
      //   250: invokevirtual getLayoutPosition : ()I
      //   253: iload_1
      //   254: if_icmpne -> 282
      //   257: aload #6
      //   259: astore #5
      //   261: iload_2
      //   262: ifne -> 84
      //   265: aload_0
      //   266: getfield mCachedViews : Ljava/util/ArrayList;
      //   269: iload #4
      //   271: invokevirtual remove : (I)Ljava/lang/Object;
      //   274: pop
      //   275: aload #6
      //   277: astore #5
      //   279: goto -> 84
      //   282: iinc #4, 1
      //   285: goto -> 220
      //   288: aconst_null
      //   289: astore #5
      //   291: goto -> 84
    }
    
    View getScrapViewAt(int param1Int) {
      return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(param1Int)).itemView;
    }
    
    public View getViewForPosition(int param1Int) {
      return getViewForPosition(param1Int, false);
    }
    
    View getViewForPosition(int param1Int, boolean param1Boolean) {
      return (tryGetViewHolderForPositionByDeadline(param1Int, param1Boolean, Long.MAX_VALUE)).itemView;
    }
    
    void markItemDecorInsetsDirty() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(b)).itemView.getLayoutParams();
        if (layoutParams != null)
          layoutParams.mInsetsDirty = true; 
      } 
    }
    
    void markKnownViewsInvalid() {
      if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
        int i = this.mCachedViews.size();
        for (byte b = 0; b < i; b++) {
          RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
          if (viewHolder != null) {
            viewHolder.addFlags(6);
            viewHolder.addChangePayload(null);
          } 
        } 
      } else {
        recycleAndClearCachedViews();
      } 
    }
    
    void offsetPositionRecordsForInsert(int param1Int1, int param1Int2) {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= param1Int1)
          viewHolder.offsetPosition(param1Int2, true); 
      } 
    }
    
    void offsetPositionRecordsForMove(int param1Int1, int param1Int2) {
      int i;
      int j;
      boolean bool;
      if (param1Int1 < param1Int2) {
        i = param1Int1;
        j = param1Int2;
        bool = true;
      } else {
        i = param1Int2;
        j = param1Int1;
        bool = true;
      } 
      int k = this.mCachedViews.size();
      for (byte b = 0; b < k; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= i && viewHolder.mPosition <= j)
          if (viewHolder.mPosition == param1Int1) {
            viewHolder.offsetPosition(param1Int2 - param1Int1, false);
          } else {
            viewHolder.offsetPosition(bool, false);
          }  
      } 
    }
    
    void offsetPositionRecordsForRemove(int param1Int1, int param1Int2, boolean param1Boolean) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null)
          if (viewHolder.mPosition >= param1Int1 + param1Int2) {
            viewHolder.offsetPosition(-param1Int2, param1Boolean);
          } else if (viewHolder.mPosition >= param1Int1) {
            viewHolder.addFlags(8);
            recycleCachedViewAt(i);
          }  
      } 
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      clear();
      getRecycledViewPool().onAdapterChanged(param1Adapter1, param1Adapter2, param1Boolean);
    }
    
    void quickRecycleScrapView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      RecyclerView.ViewHolder.access$802(viewHolder, null);
      RecyclerView.ViewHolder.access$902(viewHolder, false);
      viewHolder.clearReturnedFromScrapFlag();
      recycleViewHolderInternal(viewHolder);
    }
    
    void recycleAndClearCachedViews() {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--)
        recycleCachedViewAt(i); 
      this.mCachedViews.clear();
      if (RecyclerView.ALLOW_THREAD_GAP_WORK)
        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
    }
    
    void recycleCachedViewAt(int param1Int) {
      addViewHolderToRecycledViewPool(this.mCachedViews.get(param1Int), true);
      this.mCachedViews.remove(param1Int);
    }
    
    public void recycleView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1View, false); 
      if (viewHolder.isScrap()) {
        viewHolder.unScrap();
      } else if (viewHolder.wasReturnedFromScrap()) {
        viewHolder.clearReturnedFromScrapFlag();
      } 
      recycleViewHolderInternal(viewHolder);
    }
    
    void recycleViewHolderInternal(RecyclerView.ViewHolder param1ViewHolder) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: aload_1
      //   3: invokevirtual isScrap : ()Z
      //   6: ifne -> 19
      //   9: aload_1
      //   10: getfield itemView : Landroid/view/View;
      //   13: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   16: ifnull -> 74
      //   19: new java/lang/StringBuilder
      //   22: dup
      //   23: invokespecial <init> : ()V
      //   26: ldc_w 'Scrapped or attached views may not be recycled. isScrap:'
      //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   32: aload_1
      //   33: invokevirtual isScrap : ()Z
      //   36: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   39: ldc_w ' isAttached:'
      //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   45: astore_3
      //   46: aload_1
      //   47: getfield itemView : Landroid/view/View;
      //   50: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   53: ifnull -> 58
      //   56: iconst_1
      //   57: istore_2
      //   58: new java/lang/IllegalArgumentException
      //   61: dup
      //   62: aload_3
      //   63: iload_2
      //   64: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   67: invokevirtual toString : ()Ljava/lang/String;
      //   70: invokespecial <init> : (Ljava/lang/String;)V
      //   73: athrow
      //   74: aload_1
      //   75: invokevirtual isTmpDetached : ()Z
      //   78: ifeq -> 109
      //   81: new java/lang/IllegalArgumentException
      //   84: dup
      //   85: new java/lang/StringBuilder
      //   88: dup
      //   89: invokespecial <init> : ()V
      //   92: ldc_w 'Tmp detached view should be removed from RecyclerView before it can be recycled: '
      //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   98: aload_1
      //   99: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   102: invokevirtual toString : ()Ljava/lang/String;
      //   105: invokespecial <init> : (Ljava/lang/String;)V
      //   108: athrow
      //   109: aload_1
      //   110: invokevirtual shouldIgnore : ()Z
      //   113: ifeq -> 127
      //   116: new java/lang/IllegalArgumentException
      //   119: dup
      //   120: ldc_w 'Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.'
      //   123: invokespecial <init> : (Ljava/lang/String;)V
      //   126: athrow
      //   127: aload_1
      //   128: invokestatic access$700 : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)Z
      //   131: istore_2
      //   132: aload_0
      //   133: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   136: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
      //   139: ifnull -> 415
      //   142: iload_2
      //   143: ifeq -> 415
      //   146: aload_0
      //   147: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   150: getfield mAdapter : Landroid/support/v7/widget/RecyclerView$Adapter;
      //   153: aload_1
      //   154: invokevirtual onFailedToRecycleView : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)Z
      //   157: ifeq -> 415
      //   160: iconst_1
      //   161: istore #4
      //   163: iconst_0
      //   164: istore #5
      //   166: iconst_0
      //   167: istore #6
      //   169: iconst_0
      //   170: istore #7
      //   172: iload #4
      //   174: ifne -> 188
      //   177: iload #7
      //   179: istore #8
      //   181: aload_1
      //   182: invokevirtual isRecyclable : ()Z
      //   185: ifeq -> 384
      //   188: iload #6
      //   190: istore #4
      //   192: aload_0
      //   193: getfield mViewCacheMax : I
      //   196: ifle -> 358
      //   199: iload #6
      //   201: istore #4
      //   203: aload_1
      //   204: sipush #526
      //   207: invokevirtual hasAnyOfTheFlags : (I)Z
      //   210: ifne -> 358
      //   213: aload_0
      //   214: getfield mCachedViews : Ljava/util/ArrayList;
      //   217: invokevirtual size : ()I
      //   220: istore #8
      //   222: iload #8
      //   224: istore #4
      //   226: iload #8
      //   228: aload_0
      //   229: getfield mViewCacheMax : I
      //   232: if_icmplt -> 255
      //   235: iload #8
      //   237: istore #4
      //   239: iload #8
      //   241: ifle -> 255
      //   244: aload_0
      //   245: iconst_0
      //   246: invokevirtual recycleCachedViewAt : (I)V
      //   249: iload #8
      //   251: iconst_1
      //   252: isub
      //   253: istore #4
      //   255: iload #4
      //   257: istore #8
      //   259: iload #8
      //   261: istore #5
      //   263: invokestatic access$600 : ()Z
      //   266: ifeq -> 345
      //   269: iload #8
      //   271: istore #5
      //   273: iload #4
      //   275: ifle -> 345
      //   278: iload #8
      //   280: istore #5
      //   282: aload_0
      //   283: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   286: getfield mPrefetchRegistry : Landroid/support/v7/widget/GapWorker$LayoutPrefetchRegistryImpl;
      //   289: aload_1
      //   290: getfield mPosition : I
      //   293: invokevirtual lastPrefetchIncludedPosition : (I)Z
      //   296: ifne -> 345
      //   299: iinc #4, -1
      //   302: iload #4
      //   304: iflt -> 339
      //   307: aload_0
      //   308: getfield mCachedViews : Ljava/util/ArrayList;
      //   311: iload #4
      //   313: invokevirtual get : (I)Ljava/lang/Object;
      //   316: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   319: getfield mPosition : I
      //   322: istore #8
      //   324: aload_0
      //   325: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   328: getfield mPrefetchRegistry : Landroid/support/v7/widget/GapWorker$LayoutPrefetchRegistryImpl;
      //   331: iload #8
      //   333: invokevirtual lastPrefetchIncludedPosition : (I)Z
      //   336: ifne -> 421
      //   339: iload #4
      //   341: iconst_1
      //   342: iadd
      //   343: istore #5
      //   345: aload_0
      //   346: getfield mCachedViews : Ljava/util/ArrayList;
      //   349: iload #5
      //   351: aload_1
      //   352: invokevirtual add : (ILjava/lang/Object;)V
      //   355: iconst_1
      //   356: istore #4
      //   358: iload #4
      //   360: istore #5
      //   362: iload #7
      //   364: istore #8
      //   366: iload #4
      //   368: ifne -> 384
      //   371: aload_0
      //   372: aload_1
      //   373: iconst_1
      //   374: invokevirtual addViewHolderToRecycledViewPool : (Landroid/support/v7/widget/RecyclerView$ViewHolder;Z)V
      //   377: iconst_1
      //   378: istore #8
      //   380: iload #4
      //   382: istore #5
      //   384: aload_0
      //   385: getfield this$0 : Landroid/support/v7/widget/RecyclerView;
      //   388: getfield mViewInfoStore : Landroid/support/v7/widget/ViewInfoStore;
      //   391: aload_1
      //   392: invokevirtual removeViewHolder : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
      //   395: iload #5
      //   397: ifne -> 414
      //   400: iload #8
      //   402: ifne -> 414
      //   405: iload_2
      //   406: ifeq -> 414
      //   409: aload_1
      //   410: aconst_null
      //   411: putfield mOwnerRecyclerView : Landroid/support/v7/widget/RecyclerView;
      //   414: return
      //   415: iconst_0
      //   416: istore #4
      //   418: goto -> 163
      //   421: iinc #4, -1
      //   424: goto -> 302
    }
    
    void recycleViewInternal(View param1View) {
      recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(param1View));
    }
    
    void scrapView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.hasAnyOfTheFlags(12) || !viewHolder.isUpdated() || RecyclerView.this.canReuseUpdatedViewHolder(viewHolder)) {
        if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds())
          throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool."); 
        viewHolder.setScrapContainer(this, false);
        this.mAttachedScrap.add(viewHolder);
        return;
      } 
      if (this.mChangedScrap == null)
        this.mChangedScrap = new ArrayList<RecyclerView.ViewHolder>(); 
      viewHolder.setScrapContainer(this, true);
      this.mChangedScrap.add(viewHolder);
    }
    
    void setAdapterPositionsAsUnknown() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null)
          viewHolder.addFlags(512); 
      } 
    }
    
    void setRecycledViewPool(RecyclerView.RecycledViewPool param1RecycledViewPool) {
      if (this.mRecyclerPool != null)
        this.mRecyclerPool.detach(); 
      this.mRecyclerPool = param1RecycledViewPool;
      if (param1RecycledViewPool != null)
        this.mRecyclerPool.attach(RecyclerView.this.getAdapter()); 
    }
    
    void setViewCacheExtension(RecyclerView.ViewCacheExtension param1ViewCacheExtension) {
      this.mViewCacheExtension = param1ViewCacheExtension;
    }
    
    public void setViewCacheSize(int param1Int) {
      this.mRequestedCacheMax = param1Int;
      updateViewCacheSize();
    }
    
    @Nullable
    RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int param1Int, boolean param1Boolean, long param1Long) {
      RecyclerView.LayoutParams layoutParams;
      if (param1Int < 0 || param1Int >= RecyclerView.this.mState.getItemCount())
        throw new IndexOutOfBoundsException("Invalid item position " + param1Int + "(" + param1Int + "). Item count:" + RecyclerView.this.mState.getItemCount()); 
      int i = 0;
      RecyclerView.ViewHolder viewHolder1 = null;
      if (RecyclerView.this.mState.isPreLayout()) {
        viewHolder1 = getChangedScrapViewForPosition(param1Int);
        if (viewHolder1 != null) {
          i = 1;
        } else {
          i = 0;
        } 
      } 
      RecyclerView.ViewHolder viewHolder2 = viewHolder1;
      int j = i;
      if (viewHolder1 == null) {
        viewHolder1 = getScrapOrHiddenOrCachedHolderForPosition(param1Int, param1Boolean);
        viewHolder2 = viewHolder1;
        j = i;
        if (viewHolder1 != null)
          if (!validateViewHolderForOffsetPosition(viewHolder1)) {
            if (!param1Boolean) {
              viewHolder1.addFlags(4);
              if (viewHolder1.isScrap()) {
                RecyclerView.this.removeDetachedView(viewHolder1.itemView, false);
                viewHolder1.unScrap();
              } else if (viewHolder1.wasReturnedFromScrap()) {
                viewHolder1.clearReturnedFromScrapFlag();
              } 
              recycleViewHolderInternal(viewHolder1);
            } 
            viewHolder2 = null;
            j = i;
          } else {
            j = 1;
            viewHolder2 = viewHolder1;
          }  
      } 
      viewHolder1 = viewHolder2;
      i = j;
      if (viewHolder2 == null) {
        int k = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
        if (k < 0 || k >= RecyclerView.this.mAdapter.getItemCount())
          throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + param1Int + "(offset:" + k + ")." + "state:" + RecyclerView.this.mState.getItemCount()); 
        int m = RecyclerView.this.mAdapter.getItemViewType(k);
        viewHolder1 = viewHolder2;
        i = j;
        if (RecyclerView.this.mAdapter.hasStableIds()) {
          viewHolder2 = getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(k), m, param1Boolean);
          viewHolder1 = viewHolder2;
          i = j;
          if (viewHolder2 != null) {
            viewHolder2.mPosition = k;
            i = 1;
            viewHolder1 = viewHolder2;
          } 
        } 
        viewHolder2 = viewHolder1;
        if (viewHolder1 == null) {
          viewHolder2 = viewHolder1;
          if (this.mViewCacheExtension != null) {
            View view = this.mViewCacheExtension.getViewForPositionAndType(this, param1Int, m);
            viewHolder2 = viewHolder1;
            if (view != null) {
              viewHolder1 = RecyclerView.this.getChildViewHolder(view);
              if (viewHolder1 == null)
                throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder"); 
              viewHolder2 = viewHolder1;
              if (viewHolder1.shouldIgnore())
                throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view."); 
            } 
          } 
        } 
        viewHolder1 = viewHolder2;
        if (viewHolder2 == null) {
          viewHolder2 = getRecycledViewPool().getRecycledView(m);
          viewHolder1 = viewHolder2;
          if (viewHolder2 != null) {
            viewHolder2.resetInternal();
            viewHolder1 = viewHolder2;
            if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
              invalidateDisplayListInt(viewHolder2);
              viewHolder1 = viewHolder2;
            } 
          } 
        } 
        if (viewHolder1 == null) {
          long l1 = RecyclerView.this.getNanoTime();
          if (param1Long != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(m, l1, param1Long))
            return null; 
          viewHolder1 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, m);
          if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            RecyclerView recyclerView = RecyclerView.findNestedRecyclerView(viewHolder1.itemView);
            if (recyclerView != null)
              viewHolder1.mNestedRecyclerView = new WeakReference<RecyclerView>(recyclerView); 
          } 
          long l2 = RecyclerView.this.getNanoTime();
          this.mRecyclerPool.factorInCreateTime(m, l2 - l1);
        } 
      } 
      if (i && !RecyclerView.this.mState.isPreLayout() && viewHolder1.hasAnyOfTheFlags(8192)) {
        viewHolder1.setFlags(0, 8192);
        if (RecyclerView.this.mState.mRunSimpleAnimations) {
          j = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder1);
          RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, viewHolder1, j | 0x1000, viewHolder1.getUnmodifiedPayloads());
          RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(viewHolder1, itemHolderInfo);
        } 
      } 
      param1Boolean = false;
      if (RecyclerView.this.mState.isPreLayout() && viewHolder1.isBound()) {
        viewHolder1.mPreLayoutPosition = param1Int;
      } else if (!viewHolder1.isBound() || viewHolder1.needsUpdate() || viewHolder1.isInvalid()) {
        param1Boolean = tryBindViewHolderByDeadline(viewHolder1, RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int), param1Int, param1Long);
      } 
      ViewGroup.LayoutParams layoutParams1 = viewHolder1.itemView.getLayoutParams();
      if (layoutParams1 == null) {
        layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
        viewHolder1.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
        layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
        viewHolder1.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } else {
        layoutParams = layoutParams;
      } 
      layoutParams.mViewHolder = viewHolder1;
      if (i != 0 && param1Boolean) {
        param1Boolean = true;
      } else {
        param1Boolean = false;
      } 
      layoutParams.mPendingInvalidate = param1Boolean;
      return viewHolder1;
    }
    
    void unscrapView(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.mInChangeScrap) {
        this.mChangedScrap.remove(param1ViewHolder);
      } else {
        this.mAttachedScrap.remove(param1ViewHolder);
      } 
      RecyclerView.ViewHolder.access$802(param1ViewHolder, null);
      RecyclerView.ViewHolder.access$902(param1ViewHolder, false);
      param1ViewHolder.clearReturnedFromScrapFlag();
    }
    
    void updateViewCacheSize() {
      if (RecyclerView.this.mLayout != null) {
        i = RecyclerView.this.mLayout.mPrefetchMaxCountObserved;
      } else {
        i = 0;
      } 
      this.mViewCacheMax = this.mRequestedCacheMax + i;
      int i;
      for (i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > this.mViewCacheMax; i--)
        recycleCachedViewAt(i); 
    }
    
    boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder param1ViewHolder) {
      boolean bool1 = true;
      if (param1ViewHolder.isRemoved())
        return RecyclerView.this.mState.isPreLayout(); 
      if (param1ViewHolder.mPosition < 0 || param1ViewHolder.mPosition >= RecyclerView.this.mAdapter.getItemCount())
        throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + param1ViewHolder); 
      if (!RecyclerView.this.mState.isPreLayout() && RecyclerView.this.mAdapter.getItemViewType(param1ViewHolder.mPosition) != param1ViewHolder.getItemViewType())
        return false; 
      boolean bool2 = bool1;
      if (RecyclerView.this.mAdapter.hasStableIds()) {
        bool2 = bool1;
        if (param1ViewHolder.getItemId() != RecyclerView.this.mAdapter.getItemId(param1ViewHolder.mPosition))
          bool2 = false; 
      } 
      return bool2;
    }
    
    void viewRangeUpdate(int param1Int1, int param1Int2) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null) {
          int j = viewHolder.getLayoutPosition();
          if (j >= param1Int1 && j < param1Int1 + param1Int2) {
            viewHolder.addFlags(2);
            recycleCachedViewAt(i);
          } 
        } 
      } 
    }
  }
  
  public static interface RecyclerListener {
    void onViewRecycled(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  private class RecyclerViewDataObserver extends AdapterDataObserver {
    public void onChanged() {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      RecyclerView.this.mState.mStructureChanged = true;
      RecyclerView.this.setDataSetChangedAfterLayout();
      if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates())
        RecyclerView.this.requestLayout(); 
    }
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(param1Int1, param1Int2, param1Object))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(param1Int1, param1Int2, param1Int3))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    void triggerUpdateProcessor() {
      if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
        ViewCompat.postOnAnimation((View)RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
        return;
      } 
      RecyclerView.this.mAdapterUpdateDuringMeasure = true;
      RecyclerView.this.requestLayout();
    }
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public RecyclerView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new RecyclerView.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public RecyclerView.SavedState[] newArray(int param2Int) {
            return new RecyclerView.SavedState[param2Int];
          }
        });
    
    Parcelable mLayoutState;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      if (param1ClassLoader == null)
        param1ClassLoader = RecyclerView.LayoutManager.class.getClassLoader(); 
      this.mLayoutState = param1Parcel.readParcelable(param1ClassLoader);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    void copyFrom(SavedState param1SavedState) {
      this.mLayoutState = param1SavedState.mLayoutState;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeParcelable(this.mLayoutState, 0);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public RecyclerView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new RecyclerView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public RecyclerView.SavedState[] newArray(int param1Int) {
      return new RecyclerView.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnItemTouchListener implements OnItemTouchListener {
    public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
      return false;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {}
    
    public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {}
  }
  
  public static abstract class SmoothScroller {
    private RecyclerView.LayoutManager mLayoutManager;
    
    private boolean mPendingInitialRun;
    
    private RecyclerView mRecyclerView;
    
    private final Action mRecyclingAction = new Action(0, 0);
    
    private boolean mRunning;
    
    private int mTargetPosition = -1;
    
    private View mTargetView;
    
    private void onAnimation(int param1Int1, int param1Int2) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (!this.mRunning || this.mTargetPosition == -1 || recyclerView == null)
        stop(); 
      this.mPendingInitialRun = false;
      if (this.mTargetView != null)
        if (getChildPosition(this.mTargetView) == this.mTargetPosition) {
          onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
          this.mRecyclingAction.runIfNecessary(recyclerView);
          stop();
        } else {
          Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
          this.mTargetView = null;
        }  
      if (this.mRunning) {
        onSeekTargetStep(param1Int1, param1Int2, recyclerView.mState, this.mRecyclingAction);
        boolean bool = this.mRecyclingAction.hasJumpTarget();
        this.mRecyclingAction.runIfNecessary(recyclerView);
        if (bool) {
          if (this.mRunning) {
            this.mPendingInitialRun = true;
            recyclerView.mViewFlinger.postOnAnimation();
            return;
          } 
        } else {
          return;
        } 
      } else {
        return;
      } 
      stop();
    }
    
    public View findViewByPosition(int param1Int) {
      return this.mRecyclerView.mLayout.findViewByPosition(param1Int);
    }
    
    public int getChildCount() {
      return this.mRecyclerView.mLayout.getChildCount();
    }
    
    public int getChildPosition(View param1View) {
      return this.mRecyclerView.getChildLayoutPosition(param1View);
    }
    
    @Nullable
    public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayoutManager;
    }
    
    public int getTargetPosition() {
      return this.mTargetPosition;
    }
    
    @Deprecated
    public void instantScrollToPosition(int param1Int) {
      this.mRecyclerView.scrollToPosition(param1Int);
    }
    
    public boolean isPendingInitialRun() {
      return this.mPendingInitialRun;
    }
    
    public boolean isRunning() {
      return this.mRunning;
    }
    
    protected void normalize(PointF param1PointF) {
      double d = Math.sqrt((param1PointF.x * param1PointF.x + param1PointF.y * param1PointF.y));
      param1PointF.x = (float)(param1PointF.x / d);
      param1PointF.y = (float)(param1PointF.y / d);
    }
    
    protected void onChildAttachedToWindow(View param1View) {
      if (getChildPosition(param1View) == getTargetPosition())
        this.mTargetView = param1View; 
    }
    
    protected abstract void onSeekTargetStep(int param1Int1, int param1Int2, RecyclerView.State param1State, Action param1Action);
    
    protected abstract void onStart();
    
    protected abstract void onStop();
    
    protected abstract void onTargetFound(View param1View, RecyclerView.State param1State, Action param1Action);
    
    public void setTargetPosition(int param1Int) {
      this.mTargetPosition = param1Int;
    }
    
    void start(RecyclerView param1RecyclerView, RecyclerView.LayoutManager param1LayoutManager) {
      this.mRecyclerView = param1RecyclerView;
      this.mLayoutManager = param1LayoutManager;
      if (this.mTargetPosition == -1)
        throw new IllegalArgumentException("Invalid target position"); 
      RecyclerView.State.access$1102(this.mRecyclerView.mState, this.mTargetPosition);
      this.mRunning = true;
      this.mPendingInitialRun = true;
      this.mTargetView = findViewByPosition(getTargetPosition());
      onStart();
      this.mRecyclerView.mViewFlinger.postOnAnimation();
    }
    
    protected final void stop() {
      if (this.mRunning) {
        onStop();
        RecyclerView.State.access$1102(this.mRecyclerView.mState, -1);
        this.mTargetView = null;
        this.mTargetPosition = -1;
        this.mPendingInitialRun = false;
        this.mRunning = false;
        this.mLayoutManager.onSmoothScrollerStopped(this);
        this.mLayoutManager = null;
        this.mRecyclerView = null;
      } 
    }
    
    public static class Action {
      public static final int UNDEFINED_DURATION = -2147483648;
      
      private boolean changed = false;
      
      private int consecutiveUpdates = 0;
      
      private int mDuration;
      
      private int mDx;
      
      private int mDy;
      
      private Interpolator mInterpolator;
      
      private int mJumpToPosition = -1;
      
      public Action(int param2Int1, int param2Int2) {
        this(param2Int1, param2Int2, -2147483648, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3) {
        this(param2Int1, param2Int2, param2Int3, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
      }
      
      private void validate() {
        if (this.mInterpolator != null && this.mDuration < 1)
          throw new IllegalStateException("If you provide an interpolator, you must set a positive duration"); 
        if (this.mDuration < 1)
          throw new IllegalStateException("Scroll duration must be a positive number"); 
      }
      
      public int getDuration() {
        return this.mDuration;
      }
      
      public int getDx() {
        return this.mDx;
      }
      
      public int getDy() {
        return this.mDy;
      }
      
      public Interpolator getInterpolator() {
        return this.mInterpolator;
      }
      
      boolean hasJumpTarget() {
        return (this.mJumpToPosition >= 0);
      }
      
      public void jumpTo(int param2Int) {
        this.mJumpToPosition = param2Int;
      }
      
      void runIfNecessary(RecyclerView param2RecyclerView) {
        if (this.mJumpToPosition >= 0) {
          int i = this.mJumpToPosition;
          this.mJumpToPosition = -1;
          param2RecyclerView.jumpToPositionForSmoothScroller(i);
          this.changed = false;
          return;
        } 
        if (this.changed) {
          validate();
          if (this.mInterpolator == null) {
            if (this.mDuration == Integer.MIN_VALUE) {
              param2RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
            } else {
              param2RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
            } 
          } else {
            param2RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
          } 
          this.consecutiveUpdates++;
          if (this.consecutiveUpdates > 10)
            Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
          this.changed = false;
          return;
        } 
        this.consecutiveUpdates = 0;
      }
      
      public void setDuration(int param2Int) {
        this.changed = true;
        this.mDuration = param2Int;
      }
      
      public void setDx(int param2Int) {
        this.changed = true;
        this.mDx = param2Int;
      }
      
      public void setDy(int param2Int) {
        this.changed = true;
        this.mDy = param2Int;
      }
      
      public void setInterpolator(Interpolator param2Interpolator) {
        this.changed = true;
        this.mInterpolator = param2Interpolator;
      }
      
      public void update(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
        this.changed = true;
      }
    }
    
    public static interface ScrollVectorProvider {
      PointF computeScrollVectorForPosition(int param2Int);
    }
  }
  
  public static class Action {
    public static final int UNDEFINED_DURATION = -2147483648;
    
    private boolean changed = false;
    
    private int consecutiveUpdates = 0;
    
    private int mDuration;
    
    private int mDx;
    
    private int mDy;
    
    private Interpolator mInterpolator;
    
    private int mJumpToPosition = -1;
    
    public Action(int param1Int1, int param1Int2) {
      this(param1Int1, param1Int2, -2147483648, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2, param1Int3, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
    }
    
    private void validate() {
      if (this.mInterpolator != null && this.mDuration < 1)
        throw new IllegalStateException("If you provide an interpolator, you must set a positive duration"); 
      if (this.mDuration < 1)
        throw new IllegalStateException("Scroll duration must be a positive number"); 
    }
    
    public int getDuration() {
      return this.mDuration;
    }
    
    public int getDx() {
      return this.mDx;
    }
    
    public int getDy() {
      return this.mDy;
    }
    
    public Interpolator getInterpolator() {
      return this.mInterpolator;
    }
    
    boolean hasJumpTarget() {
      return (this.mJumpToPosition >= 0);
    }
    
    public void jumpTo(int param1Int) {
      this.mJumpToPosition = param1Int;
    }
    
    void runIfNecessary(RecyclerView param1RecyclerView) {
      if (this.mJumpToPosition >= 0) {
        int i = this.mJumpToPosition;
        this.mJumpToPosition = -1;
        param1RecyclerView.jumpToPositionForSmoothScroller(i);
        this.changed = false;
        return;
      } 
      if (this.changed) {
        validate();
        if (this.mInterpolator == null) {
          if (this.mDuration == Integer.MIN_VALUE) {
            param1RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
          } else {
            param1RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
          } 
        } else {
          param1RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
        } 
        this.consecutiveUpdates++;
        if (this.consecutiveUpdates > 10)
          Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
        this.changed = false;
        return;
      } 
      this.consecutiveUpdates = 0;
    }
    
    public void setDuration(int param1Int) {
      this.changed = true;
      this.mDuration = param1Int;
    }
    
    public void setDx(int param1Int) {
      this.changed = true;
      this.mDx = param1Int;
    }
    
    public void setDy(int param1Int) {
      this.changed = true;
      this.mDy = param1Int;
    }
    
    public void setInterpolator(Interpolator param1Interpolator) {
      this.changed = true;
      this.mInterpolator = param1Interpolator;
    }
    
    public void update(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
      this.changed = true;
    }
  }
  
  public static interface ScrollVectorProvider {
    PointF computeScrollVectorForPosition(int param1Int);
  }
  
  public static class State {
    static final int STEP_ANIMATIONS = 4;
    
    static final int STEP_LAYOUT = 2;
    
    static final int STEP_START = 1;
    
    private SparseArray<Object> mData;
    
    int mDeletedInvisibleItemCountSincePreviousLayout = 0;
    
    long mFocusedItemId;
    
    int mFocusedItemPosition;
    
    int mFocusedSubChildId;
    
    boolean mInPreLayout = false;
    
    boolean mIsMeasuring = false;
    
    int mItemCount = 0;
    
    int mLayoutStep = 1;
    
    int mPreviousLayoutItemCount = 0;
    
    boolean mRunPredictiveAnimations = false;
    
    boolean mRunSimpleAnimations = false;
    
    boolean mStructureChanged = false;
    
    private int mTargetPosition = -1;
    
    boolean mTrackOldChangeHolders = false;
    
    void assertLayoutStep(int param1Int) {
      if ((this.mLayoutStep & param1Int) == 0)
        throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(param1Int) + " but it is " + Integer.toBinaryString(this.mLayoutStep)); 
    }
    
    public boolean didStructureChange() {
      return this.mStructureChanged;
    }
    
    public <T> T get(int param1Int) {
      return (T)((this.mData == null) ? null : this.mData.get(param1Int));
    }
    
    public int getItemCount() {
      return this.mInPreLayout ? (this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout) : this.mItemCount;
    }
    
    public int getTargetScrollPosition() {
      return this.mTargetPosition;
    }
    
    public boolean hasTargetScrollPosition() {
      return (this.mTargetPosition != -1);
    }
    
    public boolean isMeasuring() {
      return this.mIsMeasuring;
    }
    
    public boolean isPreLayout() {
      return this.mInPreLayout;
    }
    
    void prepareForNestedPrefetch(RecyclerView.Adapter param1Adapter) {
      this.mLayoutStep = 1;
      this.mItemCount = param1Adapter.getItemCount();
      this.mStructureChanged = false;
      this.mInPreLayout = false;
      this.mTrackOldChangeHolders = false;
      this.mIsMeasuring = false;
    }
    
    public void put(int param1Int, Object param1Object) {
      if (this.mData == null)
        this.mData = new SparseArray(); 
      this.mData.put(param1Int, param1Object);
    }
    
    public void remove(int param1Int) {
      if (this.mData != null)
        this.mData.remove(param1Int); 
    }
    
    State reset() {
      this.mTargetPosition = -1;
      if (this.mData != null)
        this.mData.clear(); 
      this.mItemCount = 0;
      this.mStructureChanged = false;
      this.mIsMeasuring = false;
      return this;
    }
    
    public String toString() {
      return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
    }
    
    public boolean willRunPredictiveAnimations() {
      return this.mRunPredictiveAnimations;
    }
    
    public boolean willRunSimpleAnimations() {
      return this.mRunSimpleAnimations;
    }
  }
  
  public static abstract class ViewCacheExtension {
    public abstract View getViewForPositionAndType(RecyclerView.Recycler param1Recycler, int param1Int1, int param1Int2);
  }
  
  class ViewFlinger implements Runnable {
    private boolean mEatRunOnAnimationRequest = false;
    
    Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
    
    private int mLastFlingX;
    
    private int mLastFlingY;
    
    private boolean mReSchedulePostAnimationCallback = false;
    
    private ScrollerCompat mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
    
    private int computeScrollDuration(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool;
      int i = Math.abs(param1Int1);
      int j = Math.abs(param1Int2);
      if (i > j) {
        bool = true;
      } else {
        bool = false;
      } 
      param1Int3 = (int)Math.sqrt((param1Int3 * param1Int3 + param1Int4 * param1Int4));
      param1Int2 = (int)Math.sqrt((param1Int1 * param1Int1 + param1Int2 * param1Int2));
      if (bool) {
        param1Int1 = RecyclerView.this.getWidth();
      } else {
        param1Int1 = RecyclerView.this.getHeight();
      } 
      param1Int4 = param1Int1 / 2;
      float f1 = Math.min(1.0F, 1.0F * param1Int2 / param1Int1);
      float f2 = param1Int4;
      float f3 = param1Int4;
      f1 = distanceInfluenceForSnapDuration(f1);
      if (param1Int3 > 0) {
        param1Int1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / param1Int3)) * 4;
        return Math.min(param1Int1, 2000);
      } 
      if (bool) {
        param1Int2 = i;
      } else {
        param1Int2 = j;
      } 
      param1Int1 = (int)((param1Int2 / param1Int1 + 1.0F) * 300.0F);
      return Math.min(param1Int1, 2000);
    }
    
    private void disableRunOnAnimationRequests() {
      this.mReSchedulePostAnimationCallback = false;
      this.mEatRunOnAnimationRequest = true;
    }
    
    private float distanceInfluenceForSnapDuration(float param1Float) {
      return (float)Math.sin((float)((param1Float - 0.5F) * 0.4712389167638204D));
    }
    
    private void enableRunOnAnimationRequests() {
      this.mEatRunOnAnimationRequest = false;
      if (this.mReSchedulePostAnimationCallback)
        postOnAnimation(); 
    }
    
    public void fling(int param1Int1, int param1Int2) {
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      this.mScroller.fling(0, 0, param1Int1, param1Int2, -2147483648, 2147483647, -2147483648, 2147483647);
      postOnAnimation();
    }
    
    void postOnAnimation() {
      if (this.mEatRunOnAnimationRequest) {
        this.mReSchedulePostAnimationCallback = true;
        return;
      } 
      RecyclerView.this.removeCallbacks(this);
      ViewCompat.postOnAnimation((View)RecyclerView.this, this);
    }
    
    public void run() {
      if (RecyclerView.this.mLayout == null) {
        stop();
        return;
      } 
      disableRunOnAnimationRequests();
      RecyclerView.this.consumePendingUpdateOperations();
      ScrollerCompat scrollerCompat = this.mScroller;
      RecyclerView.SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
      if (scrollerCompat.computeScrollOffset()) {
        int i = scrollerCompat.getCurrX();
        int j = scrollerCompat.getCurrY();
        int k = i - this.mLastFlingX;
        int m = j - this.mLastFlingY;
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        this.mLastFlingX = i;
        this.mLastFlingY = j;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        if (RecyclerView.this.mAdapter != null) {
          RecyclerView.this.eatRequestLayout();
          RecyclerView.this.onEnterLayoutOrScroll();
          TraceCompat.beginSection("RV Scroll");
          if (k != 0) {
            i1 = RecyclerView.this.mLayout.scrollHorizontallyBy(k, RecyclerView.this.mRecycler, RecyclerView.this.mState);
            i5 = k - i1;
          } 
          if (m != 0) {
            i3 = RecyclerView.this.mLayout.scrollVerticallyBy(m, RecyclerView.this.mRecycler, RecyclerView.this.mState);
            i7 = m - i3;
          } 
          TraceCompat.endSection();
          RecyclerView.this.repositionShadowingViews();
          RecyclerView.this.onExitLayoutOrScroll();
          RecyclerView.this.resumeRequestLayout(false);
          n = i1;
          i4 = i5;
          i6 = i7;
          i2 = i3;
          if (smoothScroller != null) {
            n = i1;
            i4 = i5;
            i6 = i7;
            i2 = i3;
            if (!smoothScroller.isPendingInitialRun()) {
              n = i1;
              i4 = i5;
              i6 = i7;
              i2 = i3;
              if (smoothScroller.isRunning()) {
                n = RecyclerView.this.mState.getItemCount();
                if (n == 0) {
                  smoothScroller.stop();
                  i2 = i3;
                  i6 = i7;
                  i4 = i5;
                  n = i1;
                } else if (smoothScroller.getTargetPosition() >= n) {
                  smoothScroller.setTargetPosition(n - 1);
                  smoothScroller.onAnimation(k - i5, m - i7);
                  n = i1;
                  i4 = i5;
                  i6 = i7;
                  i2 = i3;
                } else {
                  smoothScroller.onAnimation(k - i5, m - i7);
                  n = i1;
                  i4 = i5;
                  i6 = i7;
                  i2 = i3;
                } 
              } 
            } 
          } 
        } 
        if (!RecyclerView.this.mItemDecorations.isEmpty())
          RecyclerView.this.invalidate(); 
        if (RecyclerView.this.getOverScrollMode() != 2)
          RecyclerView.this.considerReleasingGlowsOnScroll(k, m); 
        if (i4 != 0 || i6 != 0) {
          i3 = (int)scrollerCompat.getCurrVelocity();
          i1 = 0;
          if (i4 != i)
            if (i4 < 0) {
              i1 = -i3;
            } else if (i4 > 0) {
              i1 = i3;
            } else {
              i1 = 0;
            }  
          i5 = 0;
          if (i6 != j)
            if (i6 < 0) {
              i5 = -i3;
            } else if (i6 > 0) {
              i5 = i3;
            } else {
              i5 = 0;
            }  
          if (RecyclerView.this.getOverScrollMode() != 2)
            RecyclerView.this.absorbGlows(i1, i5); 
          if ((i1 != 0 || i4 == i || scrollerCompat.getFinalX() == 0) && (i5 != 0 || i6 == j || scrollerCompat.getFinalY() == 0))
            scrollerCompat.abortAnimation(); 
        } 
        if (n != 0 || i2 != 0)
          RecyclerView.this.dispatchOnScrolled(n, i2); 
        if (!RecyclerView.this.awakenScrollBars())
          RecyclerView.this.invalidate(); 
        if (m != 0 && RecyclerView.this.mLayout.canScrollVertically() && i2 == m) {
          i1 = 1;
        } else {
          i1 = 0;
        } 
        if (k != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && n == k) {
          i5 = 1;
        } else {
          i5 = 0;
        } 
        if ((k == 0 && m == 0) || i5 != 0 || i1 != 0) {
          i1 = 1;
        } else {
          i1 = 0;
        } 
        if (scrollerCompat.isFinished() || i1 == 0) {
          RecyclerView.this.setScrollState(0);
          if (RecyclerView.ALLOW_THREAD_GAP_WORK)
            RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
        } else {
          postOnAnimation();
          if (RecyclerView.this.mGapWorker != null)
            RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, k, m); 
        } 
      } 
      if (smoothScroller != null) {
        if (smoothScroller.isPendingInitialRun())
          smoothScroller.onAnimation(0, 0); 
        if (!this.mReSchedulePostAnimationCallback)
          smoothScroller.stop(); 
      } 
      enableRunOnAnimationRequests();
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2) {
      smoothScrollBy(param1Int1, param1Int2, 0, 0);
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, int param1Int3) {
      smoothScrollBy(param1Int1, param1Int2, param1Int3, RecyclerView.sQuinticInterpolator);
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      smoothScrollBy(param1Int1, param1Int2, computeScrollDuration(param1Int1, param1Int2, param1Int3, param1Int4));
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      if (this.mInterpolator != param1Interpolator) {
        this.mInterpolator = param1Interpolator;
        this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), param1Interpolator);
      } 
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      this.mScroller.startScroll(0, 0, param1Int1, param1Int2, param1Int3);
      postOnAnimation();
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, Interpolator param1Interpolator) {
      int i = computeScrollDuration(param1Int1, param1Int2, 0, 0);
      Interpolator interpolator = param1Interpolator;
      if (param1Interpolator == null)
        interpolator = RecyclerView.sQuinticInterpolator; 
      smoothScrollBy(param1Int1, param1Int2, i, interpolator);
    }
    
    public void stop() {
      RecyclerView.this.removeCallbacks(this);
      this.mScroller.abortAnimation();
    }
  }
  
  public static abstract class ViewHolder {
    static final int FLAG_ADAPTER_FULLUPDATE = 1024;
    
    static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
    
    static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
    
    static final int FLAG_BOUND = 1;
    
    static final int FLAG_IGNORE = 128;
    
    static final int FLAG_INVALID = 4;
    
    static final int FLAG_MOVED = 2048;
    
    static final int FLAG_NOT_RECYCLABLE = 16;
    
    static final int FLAG_REMOVED = 8;
    
    static final int FLAG_RETURNED_FROM_SCRAP = 32;
    
    static final int FLAG_TMP_DETACHED = 256;
    
    static final int FLAG_UPDATE = 2;
    
    private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
    
    static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
    
    public final View itemView;
    
    private int mFlags;
    
    private boolean mInChangeScrap = false;
    
    private int mIsRecyclableCount = 0;
    
    long mItemId = -1L;
    
    int mItemViewType = -1;
    
    WeakReference<RecyclerView> mNestedRecyclerView;
    
    int mOldPosition = -1;
    
    RecyclerView mOwnerRecyclerView;
    
    List<Object> mPayloads = null;
    
    @VisibleForTesting
    int mPendingAccessibilityState = -1;
    
    int mPosition = -1;
    
    int mPreLayoutPosition = -1;
    
    private RecyclerView.Recycler mScrapContainer = null;
    
    ViewHolder mShadowedHolder = null;
    
    ViewHolder mShadowingHolder = null;
    
    List<Object> mUnmodifiedPayloads = null;
    
    private int mWasImportantForAccessibilityBeforeHidden = 0;
    
    public ViewHolder(View param1View) {
      if (param1View == null)
        throw new IllegalArgumentException("itemView may not be null"); 
      this.itemView = param1View;
    }
    
    private void createPayloadsIfNeeded() {
      if (this.mPayloads == null) {
        this.mPayloads = new ArrayList();
        this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
      } 
    }
    
    private boolean doesTransientStatePreventRecycling() {
      return ((this.mFlags & 0x10) == 0 && ViewCompat.hasTransientState(this.itemView));
    }
    
    private void onEnteredHiddenState(RecyclerView param1RecyclerView) {
      this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, 4);
    }
    
    private void onLeftHiddenState(RecyclerView param1RecyclerView) {
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
      this.mWasImportantForAccessibilityBeforeHidden = 0;
    }
    
    private boolean shouldBeKeptAsChild() {
      return ((this.mFlags & 0x10) != 0);
    }
    
    void addChangePayload(Object param1Object) {
      if (param1Object == null) {
        addFlags(1024);
        return;
      } 
      if ((this.mFlags & 0x400) == 0) {
        createPayloadsIfNeeded();
        this.mPayloads.add(param1Object);
      } 
    }
    
    void addFlags(int param1Int) {
      this.mFlags |= param1Int;
    }
    
    void clearOldPosition() {
      this.mOldPosition = -1;
      this.mPreLayoutPosition = -1;
    }
    
    void clearPayload() {
      if (this.mPayloads != null)
        this.mPayloads.clear(); 
      this.mFlags &= 0xFFFFFBFF;
    }
    
    void clearReturnedFromScrapFlag() {
      this.mFlags &= 0xFFFFFFDF;
    }
    
    void clearTmpDetachFlag() {
      this.mFlags &= 0xFFFFFEFF;
    }
    
    void flagRemovedAndOffsetPosition(int param1Int1, int param1Int2, boolean param1Boolean) {
      addFlags(8);
      offsetPosition(param1Int2, param1Boolean);
      this.mPosition = param1Int1;
    }
    
    public final int getAdapterPosition() {
      return (this.mOwnerRecyclerView == null) ? -1 : this.mOwnerRecyclerView.getAdapterPositionFor(this);
    }
    
    public final long getItemId() {
      return this.mItemId;
    }
    
    public final int getItemViewType() {
      return this.mItemViewType;
    }
    
    public final int getLayoutPosition() {
      return (this.mPreLayoutPosition == -1) ? this.mPosition : this.mPreLayoutPosition;
    }
    
    public final int getOldPosition() {
      return this.mOldPosition;
    }
    
    @Deprecated
    public final int getPosition() {
      return (this.mPreLayoutPosition == -1) ? this.mPosition : this.mPreLayoutPosition;
    }
    
    List<Object> getUnmodifiedPayloads() {
      return ((this.mFlags & 0x400) == 0) ? ((this.mPayloads == null || this.mPayloads.size() == 0) ? FULLUPDATE_PAYLOADS : this.mUnmodifiedPayloads) : FULLUPDATE_PAYLOADS;
    }
    
    boolean hasAnyOfTheFlags(int param1Int) {
      return ((this.mFlags & param1Int) != 0);
    }
    
    boolean isAdapterPositionUnknown() {
      return ((this.mFlags & 0x200) != 0 || isInvalid());
    }
    
    boolean isBound() {
      return ((this.mFlags & 0x1) != 0);
    }
    
    boolean isInvalid() {
      return ((this.mFlags & 0x4) != 0);
    }
    
    public final boolean isRecyclable() {
      return ((this.mFlags & 0x10) == 0 && !ViewCompat.hasTransientState(this.itemView));
    }
    
    boolean isRemoved() {
      return ((this.mFlags & 0x8) != 0);
    }
    
    boolean isScrap() {
      return (this.mScrapContainer != null);
    }
    
    boolean isTmpDetached() {
      return ((this.mFlags & 0x100) != 0);
    }
    
    boolean isUpdated() {
      return ((this.mFlags & 0x2) != 0);
    }
    
    boolean needsUpdate() {
      return ((this.mFlags & 0x2) != 0);
    }
    
    void offsetPosition(int param1Int, boolean param1Boolean) {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
      if (this.mPreLayoutPosition == -1)
        this.mPreLayoutPosition = this.mPosition; 
      if (param1Boolean)
        this.mPreLayoutPosition += param1Int; 
      this.mPosition += param1Int;
      if (this.itemView.getLayoutParams() != null)
        ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true; 
    }
    
    void resetInternal() {
      this.mFlags = 0;
      this.mPosition = -1;
      this.mOldPosition = -1;
      this.mItemId = -1L;
      this.mPreLayoutPosition = -1;
      this.mIsRecyclableCount = 0;
      this.mShadowedHolder = null;
      this.mShadowingHolder = null;
      clearPayload();
      this.mWasImportantForAccessibilityBeforeHidden = 0;
      this.mPendingAccessibilityState = -1;
      RecyclerView.clearNestedRecyclerViewIfNotNested(this);
    }
    
    void saveOldPosition() {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
    }
    
    void setFlags(int param1Int1, int param1Int2) {
      this.mFlags = this.mFlags & (param1Int2 ^ 0xFFFFFFFF) | param1Int1 & param1Int2;
    }
    
    public final void setIsRecyclable(boolean param1Boolean) {
      int i;
      if (param1Boolean) {
        i = this.mIsRecyclableCount - 1;
      } else {
        i = this.mIsRecyclableCount + 1;
      } 
      this.mIsRecyclableCount = i;
      if (this.mIsRecyclableCount < 0) {
        this.mIsRecyclableCount = 0;
        Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
        return;
      } 
      if (!param1Boolean && this.mIsRecyclableCount == 1) {
        this.mFlags |= 0x10;
        return;
      } 
      if (param1Boolean && this.mIsRecyclableCount == 0)
        this.mFlags &= 0xFFFFFFEF; 
    }
    
    void setScrapContainer(RecyclerView.Recycler param1Recycler, boolean param1Boolean) {
      this.mScrapContainer = param1Recycler;
      this.mInChangeScrap = param1Boolean;
    }
    
    boolean shouldIgnore() {
      return ((this.mFlags & 0x80) != 0);
    }
    
    void stopIgnoring() {
      this.mFlags &= 0xFFFFFF7F;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
      if (isScrap()) {
        String str;
        StringBuilder stringBuilder1 = stringBuilder.append(" scrap ");
        if (this.mInChangeScrap) {
          str = "[changeScrap]";
        } else {
          str = "[attachedScrap]";
        } 
        stringBuilder1.append(str);
      } 
      if (isInvalid())
        stringBuilder.append(" invalid"); 
      if (!isBound())
        stringBuilder.append(" unbound"); 
      if (needsUpdate())
        stringBuilder.append(" update"); 
      if (isRemoved())
        stringBuilder.append(" removed"); 
      if (shouldIgnore())
        stringBuilder.append(" ignored"); 
      if (isTmpDetached())
        stringBuilder.append(" tmpDetached"); 
      if (!isRecyclable())
        stringBuilder.append(" not recyclable(" + this.mIsRecyclableCount + ")"); 
      if (isAdapterPositionUnknown())
        stringBuilder.append(" undefined adapter position"); 
      if (this.itemView.getParent() == null)
        stringBuilder.append(" no parent"); 
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    void unScrap() {
      this.mScrapContainer.unscrapView(this);
    }
    
    boolean wasReturnedFromScrap() {
      return ((this.mFlags & 0x20) != 0);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/RecyclerView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */