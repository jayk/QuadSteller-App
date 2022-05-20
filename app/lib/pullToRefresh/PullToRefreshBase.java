package app.lib.pullToRefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
  static final boolean DEBUG = true;
  
  static final int DEMO_SCROLL_INTERVAL = 225;
  
  static final float FRICTION = 2.0F;
  
  static final String LOG_TAG = "PullToRefresh";
  
  public static final int SMOOTH_SCROLL_DURATION_MS = 200;
  
  public static final int SMOOTH_SCROLL_LONG_DURATION_MS = 325;
  
  static final String STATE_CURRENT_MODE = "ptr_current_mode";
  
  static final String STATE_MODE = "ptr_mode";
  
  static final String STATE_SCROLLING_REFRESHING_ENABLED = "ptr_disable_scrolling";
  
  static final String STATE_SHOW_REFRESHING_VIEW = "ptr_show_refreshing_view";
  
  static final String STATE_STATE = "ptr_state";
  
  static final String STATE_SUPER = "ptr_super";
  
  static final boolean USE_HW_LAYERS = false;
  
  private Mode mCurrentMode;
  
  private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
  
  private boolean mFilterTouchEvents = true;
  
  private LoadingLayout mFooterLayout;
  
  private LoadingLayout mHeaderLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private boolean mIsBeingDragged = false;
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private boolean mLayoutVisibilityChangesEnabled = true;
  
  private AnimationStyle mLoadingAnimationStyle = AnimationStyle.getDefault();
  
  private Mode mMode = Mode.getDefault();
  
  private OnPullEventListener<T> mOnPullEventListener;
  
  private OnRefreshListener<T> mOnRefreshListener;
  
  private OnRefreshListener2<T> mOnRefreshListener2;
  
  private boolean mOverScrollEnabled = true;
  
  T mRefreshableView;
  
  private FrameLayout mRefreshableViewWrapper;
  
  private Interpolator mScrollAnimationInterpolator;
  
  private boolean mScrollingWhileRefreshingEnabled = false;
  
  private boolean mShowViewWhileRefreshing = true;
  
  private State mState = State.RESET;
  
  private int mTouchSlop;
  
  public PullToRefreshBase(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public PullToRefreshBase(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public PullToRefreshBase(Context paramContext, Mode paramMode) {
    super(paramContext);
    this.mMode = paramMode;
    init(paramContext, (AttributeSet)null);
  }
  
  public PullToRefreshBase(Context paramContext, Mode paramMode, AnimationStyle paramAnimationStyle) {
    super(paramContext);
    this.mMode = paramMode;
    this.mLoadingAnimationStyle = paramAnimationStyle;
    init(paramContext, (AttributeSet)null);
  }
  
  private void addRefreshableView(Context paramContext, T paramT) {
    this.mRefreshableViewWrapper = new FrameLayout(paramContext);
    this.mRefreshableViewWrapper.addView((View)paramT, -1, -1);
    addViewInternal((View)this.mRefreshableViewWrapper, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -1));
  }
  
  private void callRefreshListener() {
    if (this.mOnRefreshListener != null) {
      this.mOnRefreshListener.onRefresh(this);
      return;
    } 
    if (this.mOnRefreshListener2 != null) {
      if (this.mCurrentMode == Mode.PULL_FROM_START) {
        this.mOnRefreshListener2.onPullDownToRefresh(this);
        return;
      } 
      if (this.mCurrentMode == Mode.PULL_FROM_END)
        this.mOnRefreshListener2.onPullUpToRefresh(this); 
    } 
  }
  
  private LinearLayout.LayoutParams getLoadingLayoutLayoutParams() {
    switch (getPullToRefreshScrollDirection()) {
      default:
        return new LinearLayout.LayoutParams(-1, -2);
      case ROTATE:
        break;
    } 
    return new LinearLayout.LayoutParams(-2, -1);
  }
  
  private int getMaximumPullScroll() {
    switch (getPullToRefreshScrollDirection()) {
      default:
        return Math.round(getHeight() / 2.0F);
      case ROTATE:
        break;
    } 
    return Math.round(getWidth() / 2.0F);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    // Byte code:
    //   0: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   3: aload_0
    //   4: invokevirtual getPullToRefreshScrollDirection : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   7: invokevirtual ordinal : ()I
    //   10: iaload
    //   11: tableswitch default -> 28, 1 -> 228
    //   28: aload_0
    //   29: iconst_1
    //   30: invokevirtual setOrientation : (I)V
    //   33: aload_0
    //   34: bipush #17
    //   36: invokevirtual setGravity : (I)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokestatic get : (Landroid/content/Context;)Landroid/view/ViewConfiguration;
    //   44: invokevirtual getScaledTouchSlop : ()I
    //   47: putfield mTouchSlop : I
    //   50: aload_1
    //   51: aload_2
    //   52: getstatic app/gamer/quadstellar/R$styleable.PullToRefresh : [I
    //   55: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   58: astore_3
    //   59: aload_3
    //   60: iconst_4
    //   61: invokevirtual hasValue : (I)Z
    //   64: ifeq -> 80
    //   67: aload_0
    //   68: aload_3
    //   69: iconst_4
    //   70: iconst_0
    //   71: invokevirtual getInteger : (II)I
    //   74: invokestatic mapIntToValue : (I)Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   77: putfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   80: aload_3
    //   81: bipush #12
    //   83: invokevirtual hasValue : (I)Z
    //   86: ifeq -> 103
    //   89: aload_0
    //   90: aload_3
    //   91: bipush #12
    //   93: iconst_0
    //   94: invokevirtual getInteger : (II)I
    //   97: invokestatic mapIntToValue : (I)Lapp/lib/pullToRefresh/PullToRefreshBase$AnimationStyle;
    //   100: putfield mLoadingAnimationStyle : Lapp/lib/pullToRefresh/PullToRefreshBase$AnimationStyle;
    //   103: aload_0
    //   104: aload_0
    //   105: aload_1
    //   106: aload_2
    //   107: invokevirtual createRefreshableView : (Landroid/content/Context;Landroid/util/AttributeSet;)Landroid/view/View;
    //   110: putfield mRefreshableView : Landroid/view/View;
    //   113: aload_0
    //   114: aload_1
    //   115: aload_0
    //   116: getfield mRefreshableView : Landroid/view/View;
    //   119: invokespecial addRefreshableView : (Landroid/content/Context;Landroid/view/View;)V
    //   122: aload_0
    //   123: aload_0
    //   124: aload_1
    //   125: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.PULL_FROM_START : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   128: aload_3
    //   129: invokevirtual createLoadingLayout : (Landroid/content/Context;Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;Landroid/content/res/TypedArray;)Lapp/lib/pullToRefresh/LoadingLayout;
    //   132: putfield mHeaderLayout : Lapp/lib/pullToRefresh/LoadingLayout;
    //   135: aload_0
    //   136: aload_0
    //   137: aload_1
    //   138: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.PULL_FROM_END : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   141: aload_3
    //   142: invokevirtual createLoadingLayout : (Landroid/content/Context;Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;Landroid/content/res/TypedArray;)Lapp/lib/pullToRefresh/LoadingLayout;
    //   145: putfield mFooterLayout : Lapp/lib/pullToRefresh/LoadingLayout;
    //   148: aload_3
    //   149: iconst_0
    //   150: invokevirtual hasValue : (I)Z
    //   153: ifeq -> 236
    //   156: aload_3
    //   157: iconst_0
    //   158: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   161: astore_1
    //   162: aload_1
    //   163: ifnull -> 174
    //   166: aload_0
    //   167: getfield mRefreshableView : Landroid/view/View;
    //   170: aload_1
    //   171: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   174: aload_3
    //   175: bipush #9
    //   177: invokevirtual hasValue : (I)Z
    //   180: ifeq -> 194
    //   183: aload_0
    //   184: aload_3
    //   185: bipush #9
    //   187: iconst_1
    //   188: invokevirtual getBoolean : (IZ)Z
    //   191: putfield mOverScrollEnabled : Z
    //   194: aload_3
    //   195: bipush #13
    //   197: invokevirtual hasValue : (I)Z
    //   200: ifeq -> 214
    //   203: aload_0
    //   204: aload_3
    //   205: bipush #13
    //   207: iconst_0
    //   208: invokevirtual getBoolean : (IZ)Z
    //   211: putfield mScrollingWhileRefreshingEnabled : Z
    //   214: aload_0
    //   215: aload_3
    //   216: invokevirtual handleStyledAttributes : (Landroid/content/res/TypedArray;)V
    //   219: aload_3
    //   220: invokevirtual recycle : ()V
    //   223: aload_0
    //   224: invokevirtual updateUIForMode : ()V
    //   227: return
    //   228: aload_0
    //   229: iconst_0
    //   230: invokevirtual setOrientation : (I)V
    //   233: goto -> 33
    //   236: aload_3
    //   237: bipush #16
    //   239: invokevirtual hasValue : (I)Z
    //   242: ifeq -> 174
    //   245: ldc_w 'ptrAdapterViewBackground'
    //   248: ldc_w 'ptrRefreshableViewBackground'
    //   251: invokestatic warnDeprecation : (Ljava/lang/String;Ljava/lang/String;)V
    //   254: aload_3
    //   255: bipush #16
    //   257: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   260: astore_1
    //   261: aload_1
    //   262: ifnull -> 174
    //   265: aload_0
    //   266: getfield mRefreshableView : Landroid/view/View;
    //   269: aload_1
    //   270: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   273: goto -> 174
  }
  
  private boolean isReadyForPull() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iload_1
    //   3: istore_2
    //   4: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   7: aload_0
    //   8: getfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   11: invokevirtual ordinal : ()I
    //   14: iaload
    //   15: tableswitch default -> 44, 1 -> 56, 2 -> 48, 3 -> 46, 4 -> 64
    //   44: iload_1
    //   45: istore_2
    //   46: iload_2
    //   47: ireturn
    //   48: aload_0
    //   49: invokevirtual isReadyForPullStart : ()Z
    //   52: istore_2
    //   53: goto -> 46
    //   56: aload_0
    //   57: invokevirtual isReadyForPullEnd : ()Z
    //   60: istore_2
    //   61: goto -> 46
    //   64: aload_0
    //   65: invokevirtual isReadyForPullEnd : ()Z
    //   68: ifne -> 80
    //   71: iload_1
    //   72: istore_2
    //   73: aload_0
    //   74: invokevirtual isReadyForPullStart : ()Z
    //   77: ifeq -> 46
    //   80: iconst_1
    //   81: istore_2
    //   82: goto -> 46
  }
  
  private void pullEvent() {
    // Byte code:
    //   0: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   3: aload_0
    //   4: invokevirtual getPullToRefreshScrollDirection : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   7: invokevirtual ordinal : ()I
    //   10: iaload
    //   11: tableswitch default -> 28, 1 -> 182
    //   28: aload_0
    //   29: getfield mInitialMotionY : F
    //   32: fstore_1
    //   33: aload_0
    //   34: getfield mLastMotionY : F
    //   37: fstore_2
    //   38: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   41: aload_0
    //   42: getfield mCurrentMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   45: invokevirtual ordinal : ()I
    //   48: iaload
    //   49: tableswitch default -> 68, 1 -> 195
    //   68: fload_1
    //   69: fload_2
    //   70: fsub
    //   71: fconst_0
    //   72: invokestatic min : (FF)F
    //   75: fconst_2
    //   76: fdiv
    //   77: invokestatic round : (F)I
    //   80: istore_3
    //   81: aload_0
    //   82: invokevirtual getHeaderSize : ()I
    //   85: istore #4
    //   87: aload_0
    //   88: iload_3
    //   89: invokevirtual setHeaderScroll : (I)V
    //   92: iload_3
    //   93: ifeq -> 181
    //   96: aload_0
    //   97: invokevirtual isRefreshing : ()Z
    //   100: ifne -> 181
    //   103: iload_3
    //   104: invokestatic abs : (I)I
    //   107: i2f
    //   108: iload #4
    //   110: i2f
    //   111: fdiv
    //   112: fstore_1
    //   113: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   116: aload_0
    //   117: getfield mCurrentMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   120: invokevirtual ordinal : ()I
    //   123: iaload
    //   124: tableswitch default -> 144, 1 -> 217
    //   144: aload_0
    //   145: getfield mHeaderLayout : Lapp/lib/pullToRefresh/LoadingLayout;
    //   148: fload_1
    //   149: invokevirtual onPull : (F)V
    //   152: aload_0
    //   153: getfield mState : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   156: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.PULL_TO_REFRESH : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   159: if_acmpeq -> 228
    //   162: iload #4
    //   164: iload_3
    //   165: invokestatic abs : (I)I
    //   168: if_icmplt -> 228
    //   171: aload_0
    //   172: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.PULL_TO_REFRESH : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   175: iconst_0
    //   176: newarray boolean
    //   178: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   181: return
    //   182: aload_0
    //   183: getfield mInitialMotionX : F
    //   186: fstore_1
    //   187: aload_0
    //   188: getfield mLastMotionX : F
    //   191: fstore_2
    //   192: goto -> 38
    //   195: fload_1
    //   196: fload_2
    //   197: fsub
    //   198: fconst_0
    //   199: invokestatic max : (FF)F
    //   202: fconst_2
    //   203: fdiv
    //   204: invokestatic round : (F)I
    //   207: istore_3
    //   208: aload_0
    //   209: invokevirtual getFooterSize : ()I
    //   212: istore #4
    //   214: goto -> 87
    //   217: aload_0
    //   218: getfield mFooterLayout : Lapp/lib/pullToRefresh/LoadingLayout;
    //   221: fload_1
    //   222: invokevirtual onPull : (F)V
    //   225: goto -> 152
    //   228: aload_0
    //   229: getfield mState : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   232: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.PULL_TO_REFRESH : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   235: if_acmpne -> 181
    //   238: iload #4
    //   240: iload_3
    //   241: invokestatic abs : (I)I
    //   244: if_icmpge -> 181
    //   247: aload_0
    //   248: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RELEASE_TO_REFRESH : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   251: iconst_0
    //   252: newarray boolean
    //   254: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   257: goto -> 181
  }
  
  private final void smoothScrollTo(int paramInt, long paramLong) {
    smoothScrollTo(paramInt, paramLong, 0L, (OnSmoothScrollFinishedListener)null);
  }
  
  private final void smoothScrollTo(int paramInt, long paramLong1, long paramLong2, OnSmoothScrollFinishedListener paramOnSmoothScrollFinishedListener) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mCurrentSmoothScrollRunnable : Lapp/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable;
    //   4: ifnull -> 14
    //   7: aload_0
    //   8: getfield mCurrentSmoothScrollRunnable : Lapp/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable;
    //   11: invokevirtual stop : ()V
    //   14: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   17: aload_0
    //   18: invokevirtual getPullToRefreshScrollDirection : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   21: invokevirtual ordinal : ()I
    //   24: iaload
    //   25: tableswitch default -> 44, 1 -> 111
    //   44: aload_0
    //   45: invokevirtual getScrollY : ()I
    //   48: istore #7
    //   50: iload #7
    //   52: iload_1
    //   53: if_icmpeq -> 110
    //   56: aload_0
    //   57: getfield mScrollAnimationInterpolator : Landroid/view/animation/Interpolator;
    //   60: ifnonnull -> 74
    //   63: aload_0
    //   64: new android/view/animation/DecelerateInterpolator
    //   67: dup
    //   68: invokespecial <init> : ()V
    //   71: putfield mScrollAnimationInterpolator : Landroid/view/animation/Interpolator;
    //   74: aload_0
    //   75: new app/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable
    //   78: dup
    //   79: aload_0
    //   80: iload #7
    //   82: iload_1
    //   83: lload_2
    //   84: aload #6
    //   86: invokespecial <init> : (Lapp/lib/pullToRefresh/PullToRefreshBase;IIJLapp/lib/pullToRefresh/PullToRefreshBase$OnSmoothScrollFinishedListener;)V
    //   89: putfield mCurrentSmoothScrollRunnable : Lapp/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable;
    //   92: lload #4
    //   94: lconst_0
    //   95: lcmp
    //   96: ifle -> 120
    //   99: aload_0
    //   100: aload_0
    //   101: getfield mCurrentSmoothScrollRunnable : Lapp/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable;
    //   104: lload #4
    //   106: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
    //   109: pop
    //   110: return
    //   111: aload_0
    //   112: invokevirtual getScrollX : ()I
    //   115: istore #7
    //   117: goto -> 50
    //   120: aload_0
    //   121: aload_0
    //   122: getfield mCurrentSmoothScrollRunnable : Lapp/lib/pullToRefresh/PullToRefreshBase$SmoothScrollRunnable;
    //   125: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   128: pop
    //   129: goto -> 110
  }
  
  private final void smoothScrollToAndBack(int paramInt) {
    smoothScrollTo(paramInt, 200L, 0L, new OnSmoothScrollFinishedListener() {
          public void onSmoothScrollFinished() {
            PullToRefreshBase.this.smoothScrollTo(0, 200L, 225L, (PullToRefreshBase.OnSmoothScrollFinishedListener)null);
          }
        });
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    Log.d("PullToRefresh", "addView: " + paramView.getClass().getSimpleName());
    T t = getRefreshableView();
    if (t instanceof ViewGroup) {
      ((ViewGroup)t).addView(paramView, paramInt, paramLayoutParams);
      return;
    } 
    throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
  }
  
  protected final void addViewInternal(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
  }
  
  protected final void addViewInternal(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, -1, paramLayoutParams);
  }
  
  protected LoadingLayout createLoadingLayout(Context paramContext, Mode paramMode, TypedArray paramTypedArray) {
    LoadingLayout loadingLayout = this.mLoadingAnimationStyle.createLoadingLayout(paramContext, paramMode, getPullToRefreshScrollDirection(), paramTypedArray);
    loadingLayout.setVisibility(4);
    return loadingLayout;
  }
  
  protected LoadingLayoutProxy createLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2) {
    LoadingLayoutProxy loadingLayoutProxy = new LoadingLayoutProxy();
    if (paramBoolean1 && this.mMode.showHeaderLoadingLayout())
      loadingLayoutProxy.addLayout(this.mHeaderLayout); 
    if (paramBoolean2 && this.mMode.showFooterLoadingLayout())
      loadingLayoutProxy.addLayout(this.mFooterLayout); 
    return loadingLayoutProxy;
  }
  
  protected abstract T createRefreshableView(Context paramContext, AttributeSet paramAttributeSet);
  
  public final boolean demo() {
    null = true;
    if (this.mMode.showHeaderLoadingLayout() && isReadyForPullStart()) {
      smoothScrollToAndBack(-getHeaderSize() * 2);
      return null;
    } 
    if (this.mMode.showFooterLoadingLayout() && isReadyForPullEnd()) {
      smoothScrollToAndBack(getFooterSize() * 2);
      return null;
    } 
    return false;
  }
  
  protected final void disableLoadingLayoutVisibilityChanges() {
    this.mLayoutVisibilityChangesEnabled = false;
  }
  
  public final Mode getCurrentMode() {
    return this.mCurrentMode;
  }
  
  public final boolean getFilterTouchEvents() {
    return this.mFilterTouchEvents;
  }
  
  protected final LoadingLayout getFooterLayout() {
    return this.mFooterLayout;
  }
  
  protected final int getFooterSize() {
    return this.mFooterLayout.getContentSize();
  }
  
  protected final LoadingLayout getHeaderLayout() {
    return this.mHeaderLayout;
  }
  
  protected final int getHeaderSize() {
    return this.mHeaderLayout.getContentSize();
  }
  
  public final ILoadingLayout getLoadingLayoutProxy() {
    return getLoadingLayoutProxy(true, true);
  }
  
  public final ILoadingLayout getLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2) {
    return createLoadingLayoutProxy(paramBoolean1, paramBoolean2);
  }
  
  public final Mode getMode() {
    return this.mMode;
  }
  
  public abstract Orientation getPullToRefreshScrollDirection();
  
  protected int getPullToRefreshScrollDuration() {
    return 200;
  }
  
  protected int getPullToRefreshScrollDurationLonger() {
    return 325;
  }
  
  public final T getRefreshableView() {
    return this.mRefreshableView;
  }
  
  protected FrameLayout getRefreshableViewWrapper() {
    return this.mRefreshableViewWrapper;
  }
  
  public final boolean getShowViewWhileRefreshing() {
    return this.mShowViewWhileRefreshing;
  }
  
  public final State getState() {
    return this.mState;
  }
  
  protected void handleStyledAttributes(TypedArray paramTypedArray) {}
  
  public final boolean isDisableScrollingWhileRefreshing() {
    return !isScrollingWhileRefreshingEnabled();
  }
  
  public final boolean isPullToRefreshEnabled() {
    return this.mMode.permitsPullToRefresh();
  }
  
  public final boolean isPullToRefreshOverScrollEnabled() {
    return (Build.VERSION.SDK_INT >= 9 && this.mOverScrollEnabled && OverscrollHelper.isAndroidOverScrollEnabled((View)this.mRefreshableView));
  }
  
  protected abstract boolean isReadyForPullEnd();
  
  protected abstract boolean isReadyForPullStart();
  
  public final boolean isRefreshing() {
    return (this.mState == State.REFRESHING || this.mState == State.MANUAL_REFRESHING);
  }
  
  public final boolean isScrollingWhileRefreshingEnabled() {
    return this.mScrollingWhileRefreshingEnabled;
  }
  
  public final boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: invokevirtual isPullToRefreshEnabled : ()Z
    //   6: ifne -> 11
    //   9: iload_2
    //   10: ireturn
    //   11: aload_1
    //   12: invokevirtual getAction : ()I
    //   15: istore_3
    //   16: iload_3
    //   17: iconst_3
    //   18: if_icmpeq -> 26
    //   21: iload_3
    //   22: iconst_1
    //   23: if_icmpne -> 34
    //   26: aload_0
    //   27: iconst_0
    //   28: putfield mIsBeingDragged : Z
    //   31: goto -> 9
    //   34: iload_3
    //   35: ifeq -> 50
    //   38: aload_0
    //   39: getfield mIsBeingDragged : Z
    //   42: ifeq -> 50
    //   45: iconst_1
    //   46: istore_2
    //   47: goto -> 9
    //   50: iload_3
    //   51: tableswitch default -> 76, 0 -> 351, 1 -> 76, 2 -> 84
    //   76: aload_0
    //   77: getfield mIsBeingDragged : Z
    //   80: istore_2
    //   81: goto -> 9
    //   84: aload_0
    //   85: getfield mScrollingWhileRefreshingEnabled : Z
    //   88: ifne -> 103
    //   91: aload_0
    //   92: invokevirtual isRefreshing : ()Z
    //   95: ifeq -> 103
    //   98: iconst_1
    //   99: istore_2
    //   100: goto -> 9
    //   103: aload_0
    //   104: invokespecial isReadyForPull : ()Z
    //   107: ifeq -> 76
    //   110: aload_1
    //   111: invokevirtual getY : ()F
    //   114: fstore #4
    //   116: aload_1
    //   117: invokevirtual getX : ()F
    //   120: fstore #5
    //   122: getstatic app/lib/pullToRefresh/PullToRefreshBase$4.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   125: aload_0
    //   126: invokevirtual getPullToRefreshScrollDirection : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   129: invokevirtual ordinal : ()I
    //   132: iaload
    //   133: tableswitch default -> 152, 1 -> 267
    //   152: fload #4
    //   154: aload_0
    //   155: getfield mLastMotionY : F
    //   158: fsub
    //   159: fstore #6
    //   161: fload #5
    //   163: aload_0
    //   164: getfield mLastMotionX : F
    //   167: fsub
    //   168: fstore #7
    //   170: fload #6
    //   172: invokestatic abs : (F)F
    //   175: fstore #8
    //   177: fload #8
    //   179: aload_0
    //   180: getfield mTouchSlop : I
    //   183: i2f
    //   184: fcmpl
    //   185: ifle -> 76
    //   188: aload_0
    //   189: getfield mFilterTouchEvents : Z
    //   192: ifeq -> 206
    //   195: fload #8
    //   197: fload #7
    //   199: invokestatic abs : (F)F
    //   202: fcmpl
    //   203: ifle -> 76
    //   206: aload_0
    //   207: getfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   210: invokevirtual showHeaderLoadingLayout : ()Z
    //   213: ifeq -> 288
    //   216: fload #6
    //   218: fconst_1
    //   219: fcmpl
    //   220: iflt -> 288
    //   223: aload_0
    //   224: invokevirtual isReadyForPullStart : ()Z
    //   227: ifeq -> 288
    //   230: aload_0
    //   231: fload #4
    //   233: putfield mLastMotionY : F
    //   236: aload_0
    //   237: fload #5
    //   239: putfield mLastMotionX : F
    //   242: aload_0
    //   243: iconst_1
    //   244: putfield mIsBeingDragged : Z
    //   247: aload_0
    //   248: getfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   251: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.BOTH : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   254: if_acmpne -> 76
    //   257: aload_0
    //   258: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.PULL_FROM_START : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   261: putfield mCurrentMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   264: goto -> 76
    //   267: fload #5
    //   269: aload_0
    //   270: getfield mLastMotionX : F
    //   273: fsub
    //   274: fstore #6
    //   276: fload #4
    //   278: aload_0
    //   279: getfield mLastMotionY : F
    //   282: fsub
    //   283: fstore #7
    //   285: goto -> 170
    //   288: aload_0
    //   289: getfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   292: invokevirtual showFooterLoadingLayout : ()Z
    //   295: ifeq -> 76
    //   298: fload #6
    //   300: ldc_w -1.0
    //   303: fcmpg
    //   304: ifgt -> 76
    //   307: aload_0
    //   308: invokevirtual isReadyForPullEnd : ()Z
    //   311: ifeq -> 76
    //   314: aload_0
    //   315: fload #4
    //   317: putfield mLastMotionY : F
    //   320: aload_0
    //   321: fload #5
    //   323: putfield mLastMotionX : F
    //   326: aload_0
    //   327: iconst_1
    //   328: putfield mIsBeingDragged : Z
    //   331: aload_0
    //   332: getfield mMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   335: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.BOTH : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   338: if_acmpne -> 76
    //   341: aload_0
    //   342: getstatic app/lib/pullToRefresh/PullToRefreshBase$Mode.PULL_FROM_END : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   345: putfield mCurrentMode : Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   348: goto -> 76
    //   351: aload_0
    //   352: invokespecial isReadyForPull : ()Z
    //   355: ifeq -> 76
    //   358: aload_1
    //   359: invokevirtual getY : ()F
    //   362: fstore #7
    //   364: aload_0
    //   365: fload #7
    //   367: putfield mInitialMotionY : F
    //   370: aload_0
    //   371: fload #7
    //   373: putfield mLastMotionY : F
    //   376: aload_1
    //   377: invokevirtual getX : ()F
    //   380: fstore #7
    //   382: aload_0
    //   383: fload #7
    //   385: putfield mInitialMotionX : F
    //   388: aload_0
    //   389: fload #7
    //   391: putfield mLastMotionX : F
    //   394: aload_0
    //   395: iconst_0
    //   396: putfield mIsBeingDragged : Z
    //   399: goto -> 76
  }
  
  protected void onPtrRestoreInstanceState(Bundle paramBundle) {}
  
  protected void onPtrSaveInstanceState(Bundle paramBundle) {}
  
  protected void onPullToRefresh() {
    switch (this.mCurrentMode) {
      default:
        return;
      case ROTATE:
        this.mFooterLayout.pullToRefresh();
      case FLIP:
        break;
    } 
    this.mHeaderLayout.pullToRefresh();
  }
  
  public final void onRefreshComplete() {
    if (isRefreshing())
      setState(State.RESET, new boolean[0]); 
  }
  
  protected void onRefreshing(boolean paramBoolean) {
    if (this.mMode.showHeaderLoadingLayout())
      this.mHeaderLayout.refreshing(); 
    if (this.mMode.showFooterLoadingLayout())
      this.mFooterLayout.refreshing(); 
    if (paramBoolean) {
      if (this.mShowViewWhileRefreshing) {
        OnSmoothScrollFinishedListener onSmoothScrollFinishedListener = new OnSmoothScrollFinishedListener() {
            public void onSmoothScrollFinished() {
              PullToRefreshBase.this.callRefreshListener();
            }
          };
        switch (this.mCurrentMode) {
          default:
            smoothScrollTo(-getHeaderSize(), onSmoothScrollFinishedListener);
            return;
          case ROTATE:
          case null:
            break;
        } 
        smoothScrollTo(getFooterSize(), onSmoothScrollFinishedListener);
        return;
      } 
      smoothScrollTo(0);
      return;
    } 
    callRefreshListener();
  }
  
  protected void onReleaseToRefresh() {
    switch (this.mCurrentMode) {
      default:
        return;
      case ROTATE:
        this.mFooterLayout.releaseToRefresh();
      case FLIP:
        break;
    } 
    this.mHeaderLayout.releaseToRefresh();
  }
  
  protected void onReset() {
    this.mIsBeingDragged = false;
    this.mLayoutVisibilityChangesEnabled = true;
    this.mHeaderLayout.reset();
    this.mFooterLayout.reset();
    smoothScrollTo(0);
  }
  
  protected final void onRestoreInstanceState(Parcelable paramParcelable) {
    Bundle bundle;
    if (paramParcelable instanceof Bundle) {
      bundle = (Bundle)paramParcelable;
      setMode(Mode.mapIntToValue(bundle.getInt("ptr_mode", 0)));
      this.mCurrentMode = Mode.mapIntToValue(bundle.getInt("ptr_current_mode", 0));
      this.mScrollingWhileRefreshingEnabled = bundle.getBoolean("ptr_disable_scrolling", false);
      this.mShowViewWhileRefreshing = bundle.getBoolean("ptr_show_refreshing_view", true);
      super.onRestoreInstanceState(bundle.getParcelable("ptr_super"));
      State state = State.mapIntToValue(bundle.getInt("ptr_state", 0));
      if (state == State.REFRESHING || state == State.MANUAL_REFRESHING)
        setState(state, new boolean[] { true }); 
      onPtrRestoreInstanceState(bundle);
      return;
    } 
    super.onRestoreInstanceState((Parcelable)bundle);
  }
  
  protected final Parcelable onSaveInstanceState() {
    Bundle bundle = new Bundle();
    onPtrSaveInstanceState(bundle);
    bundle.putInt("ptr_state", this.mState.getIntValue());
    bundle.putInt("ptr_mode", this.mMode.getIntValue());
    bundle.putInt("ptr_current_mode", this.mCurrentMode.getIntValue());
    bundle.putBoolean("ptr_disable_scrolling", this.mScrollingWhileRefreshingEnabled);
    bundle.putBoolean("ptr_show_refreshing_view", this.mShowViewWhileRefreshing);
    bundle.putParcelable("ptr_super", super.onSaveInstanceState());
    return (Parcelable)bundle;
  }
  
  protected final void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    Log.d("PullToRefresh", String.format("onSizeChanged. W: %d, H: %d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    refreshLoadingSize();
    refreshRefreshableViewSize(paramInt1, paramInt2);
    post(new Runnable() {
          public void run() {
            PullToRefreshBase.this.requestLayout();
          }
        });
  }
  
  public final boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: invokevirtual isPullToRefreshEnabled : ()Z
    //   6: ifne -> 13
    //   9: iload_2
    //   10: istore_3
    //   11: iload_3
    //   12: ireturn
    //   13: aload_0
    //   14: getfield mScrollingWhileRefreshingEnabled : Z
    //   17: ifne -> 32
    //   20: aload_0
    //   21: invokevirtual isRefreshing : ()Z
    //   24: ifeq -> 32
    //   27: iconst_1
    //   28: istore_3
    //   29: goto -> 11
    //   32: aload_1
    //   33: invokevirtual getAction : ()I
    //   36: ifne -> 48
    //   39: iload_2
    //   40: istore_3
    //   41: aload_1
    //   42: invokevirtual getEdgeFlags : ()I
    //   45: ifne -> 11
    //   48: aload_1
    //   49: invokevirtual getAction : ()I
    //   52: tableswitch default -> 84, 0 -> 89, 1 -> 173, 2 -> 139, 3 -> 173
    //   84: iload_2
    //   85: istore_3
    //   86: goto -> 11
    //   89: iload_2
    //   90: istore_3
    //   91: aload_0
    //   92: invokespecial isReadyForPull : ()Z
    //   95: ifeq -> 11
    //   98: aload_1
    //   99: invokevirtual getY : ()F
    //   102: fstore #4
    //   104: aload_0
    //   105: fload #4
    //   107: putfield mInitialMotionY : F
    //   110: aload_0
    //   111: fload #4
    //   113: putfield mLastMotionY : F
    //   116: aload_1
    //   117: invokevirtual getX : ()F
    //   120: fstore #4
    //   122: aload_0
    //   123: fload #4
    //   125: putfield mInitialMotionX : F
    //   128: aload_0
    //   129: fload #4
    //   131: putfield mLastMotionX : F
    //   134: iconst_1
    //   135: istore_3
    //   136: goto -> 11
    //   139: iload_2
    //   140: istore_3
    //   141: aload_0
    //   142: getfield mIsBeingDragged : Z
    //   145: ifeq -> 11
    //   148: aload_0
    //   149: aload_1
    //   150: invokevirtual getY : ()F
    //   153: putfield mLastMotionY : F
    //   156: aload_0
    //   157: aload_1
    //   158: invokevirtual getX : ()F
    //   161: putfield mLastMotionX : F
    //   164: aload_0
    //   165: invokespecial pullEvent : ()V
    //   168: iconst_1
    //   169: istore_3
    //   170: goto -> 11
    //   173: iload_2
    //   174: istore_3
    //   175: aload_0
    //   176: getfield mIsBeingDragged : Z
    //   179: ifeq -> 11
    //   182: aload_0
    //   183: iconst_0
    //   184: putfield mIsBeingDragged : Z
    //   187: aload_0
    //   188: getfield mState : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   191: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RELEASE_TO_REFRESH : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   194: if_acmpne -> 230
    //   197: aload_0
    //   198: getfield mOnRefreshListener : Lapp/lib/pullToRefresh/PullToRefreshBase$OnRefreshListener;
    //   201: ifnonnull -> 211
    //   204: aload_0
    //   205: getfield mOnRefreshListener2 : Lapp/lib/pullToRefresh/PullToRefreshBase$OnRefreshListener2;
    //   208: ifnull -> 230
    //   211: aload_0
    //   212: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.REFRESHING : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   215: iconst_1
    //   216: newarray boolean
    //   218: dup
    //   219: iconst_0
    //   220: iconst_1
    //   221: bastore
    //   222: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   225: iconst_1
    //   226: istore_3
    //   227: goto -> 11
    //   230: aload_0
    //   231: invokevirtual isRefreshing : ()Z
    //   234: ifeq -> 247
    //   237: aload_0
    //   238: iconst_0
    //   239: invokevirtual smoothScrollTo : (I)V
    //   242: iconst_1
    //   243: istore_3
    //   244: goto -> 11
    //   247: aload_0
    //   248: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RESET : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   251: iconst_0
    //   252: newarray boolean
    //   254: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   257: iconst_1
    //   258: istore_3
    //   259: goto -> 11
  }
  
  protected final void refreshLoadingSize() {
    int i1;
    int i = (int)(getMaximumPullScroll() * 1.2F);
    int j = getPaddingLeft();
    int k = getPaddingTop();
    int m = getPaddingRight();
    int n = getPaddingBottom();
    switch (getPullToRefreshScrollDirection()) {
      default:
        i1 = k;
        Log.d("PullToRefresh", String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", new Object[] { Integer.valueOf(j), Integer.valueOf(i1), Integer.valueOf(m), Integer.valueOf(n) }));
        setPadding(j, i1, m, n);
        return;
      case ROTATE:
        if (this.mMode.showHeaderLoadingLayout()) {
          this.mHeaderLayout.setWidth(i);
          i1 = -i;
        } else {
          i1 = 0;
        } 
        if (this.mMode.showFooterLoadingLayout()) {
          this.mFooterLayout.setWidth(i);
          m = -i;
          j = i1;
          i1 = k;
        } else {
          m = 0;
          j = i1;
          i1 = k;
        } 
        Log.d("PullToRefresh", String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", new Object[] { Integer.valueOf(j), Integer.valueOf(i1), Integer.valueOf(m), Integer.valueOf(n) }));
        setPadding(j, i1, m, n);
        return;
      case FLIP:
        break;
    } 
    if (this.mMode.showHeaderLoadingLayout()) {
      this.mHeaderLayout.setHeight(i);
      i1 = -i;
    } else {
      i1 = 0;
    } 
    if (this.mMode.showFooterLoadingLayout()) {
      this.mFooterLayout.setHeight(i);
      n = -i;
    } else {
      n = 0;
    } 
    Log.d("PullToRefresh", String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", new Object[] { Integer.valueOf(j), Integer.valueOf(i1), Integer.valueOf(m), Integer.valueOf(n) }));
    setPadding(j, i1, m, n);
  }
  
  protected final void refreshRefreshableViewSize(int paramInt1, int paramInt2) {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.mRefreshableViewWrapper.getLayoutParams();
    switch (getPullToRefreshScrollDirection()) {
      default:
        return;
      case ROTATE:
        if (layoutParams.width != paramInt1) {
          layoutParams.width = paramInt1;
          this.mRefreshableViewWrapper.requestLayout();
        } 
      case FLIP:
        break;
    } 
    if (layoutParams.height != paramInt2) {
      layoutParams.height = paramInt2;
      this.mRefreshableViewWrapper.requestLayout();
    } 
  }
  
  public void setDisableScrollingWhileRefreshing(boolean paramBoolean) {
    if (!paramBoolean) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    setScrollingWhileRefreshingEnabled(paramBoolean);
  }
  
  public final void setFilterTouchEvents(boolean paramBoolean) {
    this.mFilterTouchEvents = paramBoolean;
  }
  
  protected final void setHeaderScroll(int paramInt) {
    Log.d("PullToRefresh", "setHeaderScroll: " + paramInt);
    int i = getMaximumPullScroll();
    paramInt = Math.min(i, Math.max(-i, paramInt));
    if (this.mLayoutVisibilityChangesEnabled)
      if (paramInt < 0) {
        this.mHeaderLayout.setVisibility(0);
      } else if (paramInt > 0) {
        this.mFooterLayout.setVisibility(0);
      } else {
        this.mHeaderLayout.setVisibility(4);
        this.mFooterLayout.setVisibility(4);
      }  
    switch (getPullToRefreshScrollDirection()) {
      default:
        return;
      case FLIP:
        scrollTo(0, paramInt);
      case ROTATE:
        break;
    } 
    scrollTo(paramInt, 0);
  }
  
  public void setLastUpdatedLabel(CharSequence paramCharSequence) {
    getLoadingLayoutProxy().setLastUpdatedLabel(paramCharSequence);
  }
  
  public void setLoadingDrawable(Drawable paramDrawable) {
    getLoadingLayoutProxy().setLoadingDrawable(paramDrawable);
  }
  
  public void setLoadingDrawable(Drawable paramDrawable, Mode paramMode) {
    getLoadingLayoutProxy(paramMode.showHeaderLoadingLayout(), paramMode.showFooterLoadingLayout()).setLoadingDrawable(paramDrawable);
  }
  
  public void setLongClickable(boolean paramBoolean) {
    getRefreshableView().setLongClickable(paramBoolean);
  }
  
  public final void setMode(Mode paramMode) {
    if (paramMode != this.mMode) {
      Log.d("PullToRefresh", "Setting mode to: " + paramMode);
      this.mMode = paramMode;
      updateUIForMode();
    } 
  }
  
  public void setOnPullEventListener(OnPullEventListener<T> paramOnPullEventListener) {
    this.mOnPullEventListener = paramOnPullEventListener;
  }
  
  public final void setOnRefreshListener(OnRefreshListener2<T> paramOnRefreshListener2) {
    this.mOnRefreshListener2 = paramOnRefreshListener2;
    this.mOnRefreshListener = null;
  }
  
  public final void setOnRefreshListener(OnRefreshListener<T> paramOnRefreshListener) {
    this.mOnRefreshListener = paramOnRefreshListener;
    this.mOnRefreshListener2 = null;
  }
  
  public void setPullLabel(CharSequence paramCharSequence) {
    getLoadingLayoutProxy().setPullLabel(paramCharSequence);
  }
  
  public void setPullLabel(CharSequence paramCharSequence, Mode paramMode) {
    getLoadingLayoutProxy(paramMode.showHeaderLoadingLayout(), paramMode.showFooterLoadingLayout()).setPullLabel(paramCharSequence);
  }
  
  public final void setPullToRefreshEnabled(boolean paramBoolean) {
    Mode mode;
    if (paramBoolean) {
      mode = Mode.getDefault();
    } else {
      mode = Mode.DISABLED;
    } 
    setMode(mode);
  }
  
  public final void setPullToRefreshOverScrollEnabled(boolean paramBoolean) {
    this.mOverScrollEnabled = paramBoolean;
  }
  
  public final void setRefreshing() {
    setRefreshing(true);
  }
  
  public final void setRefreshing(boolean paramBoolean) {
    if (!isRefreshing())
      setState(State.MANUAL_REFRESHING, new boolean[] { paramBoolean }); 
  }
  
  public void setRefreshingLabel(CharSequence paramCharSequence) {
    getLoadingLayoutProxy().setRefreshingLabel(paramCharSequence);
  }
  
  public void setRefreshingLabel(CharSequence paramCharSequence, Mode paramMode) {
    getLoadingLayoutProxy(paramMode.showHeaderLoadingLayout(), paramMode.showFooterLoadingLayout()).setRefreshingLabel(paramCharSequence);
  }
  
  public void setReleaseLabel(CharSequence paramCharSequence) {
    setReleaseLabel(paramCharSequence, Mode.BOTH);
  }
  
  public void setReleaseLabel(CharSequence paramCharSequence, Mode paramMode) {
    getLoadingLayoutProxy(paramMode.showHeaderLoadingLayout(), paramMode.showFooterLoadingLayout()).setReleaseLabel(paramCharSequence);
  }
  
  public void setScrollAnimationInterpolator(Interpolator paramInterpolator) {
    this.mScrollAnimationInterpolator = paramInterpolator;
  }
  
  public final void setScrollingWhileRefreshingEnabled(boolean paramBoolean) {
    this.mScrollingWhileRefreshingEnabled = paramBoolean;
  }
  
  public final void setShowViewWhileRefreshing(boolean paramBoolean) {
    this.mShowViewWhileRefreshing = paramBoolean;
  }
  
  final void setState(State paramState, boolean... paramVarArgs) {
    this.mState = paramState;
    Log.d("PullToRefresh", "State: " + this.mState.name());
    switch (this.mState) {
      default:
        if (this.mOnPullEventListener != null)
          this.mOnPullEventListener.onPullEvent(this, this.mState, this.mCurrentMode); 
        return;
      case ROTATE:
        onReset();
      case FLIP:
        onPullToRefresh();
      case null:
        onReleaseToRefresh();
      case null:
      case null:
        break;
    } 
    onRefreshing(paramVarArgs[0]);
  }
  
  protected final void smoothScrollTo(int paramInt) {
    smoothScrollTo(paramInt, getPullToRefreshScrollDuration());
  }
  
  protected final void smoothScrollTo(int paramInt, OnSmoothScrollFinishedListener paramOnSmoothScrollFinishedListener) {
    smoothScrollTo(paramInt, getPullToRefreshScrollDuration(), 0L, paramOnSmoothScrollFinishedListener);
  }
  
  protected final void smoothScrollToLonger(int paramInt) {
    smoothScrollTo(paramInt, getPullToRefreshScrollDurationLonger());
  }
  
  protected void updateUIForMode() {
    Mode mode;
    LinearLayout.LayoutParams layoutParams = getLoadingLayoutLayoutParams();
    if (this == this.mHeaderLayout.getParent())
      removeView((View)this.mHeaderLayout); 
    if (this.mMode.showHeaderLoadingLayout())
      addViewInternal((View)this.mHeaderLayout, 0, (ViewGroup.LayoutParams)layoutParams); 
    if (this == this.mFooterLayout.getParent())
      removeView((View)this.mFooterLayout); 
    if (this.mMode.showFooterLoadingLayout())
      addViewInternal((View)this.mFooterLayout, (ViewGroup.LayoutParams)layoutParams); 
    refreshLoadingSize();
    if (this.mMode != Mode.BOTH) {
      mode = this.mMode;
    } else {
      mode = Mode.PULL_FROM_START;
    } 
    this.mCurrentMode = mode;
  }
  
  public enum AnimationStyle {
    FLIP, ROTATE;
    
    static {
      $VALUES = new AnimationStyle[] { ROTATE, FLIP };
    }
    
    static AnimationStyle getDefault() {
      return ROTATE;
    }
    
    static AnimationStyle mapIntToValue(int param1Int) {
      switch (param1Int) {
        default:
          return ROTATE;
        case 1:
          break;
      } 
      return FLIP;
    }
    
    LoadingLayout createLoadingLayout(Context param1Context, PullToRefreshBase.Mode param1Mode, PullToRefreshBase.Orientation param1Orientation, TypedArray param1TypedArray) {
      switch (this) {
        default:
          return new RotateLoadingLayout(param1Context, param1Mode, param1Orientation, param1TypedArray);
        case FLIP:
          break;
      } 
      return new FlipLoadingLayout(param1Context, param1Mode, param1Orientation, param1TypedArray);
    }
  }
  
  public enum Mode {
    BOTH,
    DISABLED(0),
    MANUAL_REFRESH_ONLY(0),
    PULL_FROM_END(0),
    PULL_FROM_START(1);
    
    public static Mode PULL_DOWN_TO_REFRESH;
    
    public static Mode PULL_UP_TO_REFRESH;
    
    private int mIntValue;
    
    static {
      BOTH = new Mode("BOTH", 3, 3);
      MANUAL_REFRESH_ONLY = new Mode("MANUAL_REFRESH_ONLY", 4, 4);
      $VALUES = new Mode[] { DISABLED, PULL_FROM_START, PULL_FROM_END, BOTH, MANUAL_REFRESH_ONLY };
      PULL_DOWN_TO_REFRESH = PULL_FROM_START;
      PULL_UP_TO_REFRESH = PULL_FROM_END;
    }
    
    Mode(int param1Int1) {
      this.mIntValue = param1Int1;
    }
    
    static Mode getDefault() {
      return PULL_FROM_START;
    }
    
    static Mode mapIntToValue(int param1Int) {
      // Byte code:
      //   0: invokestatic values : ()[Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
      //   3: astore_1
      //   4: aload_1
      //   5: arraylength
      //   6: istore_2
      //   7: iconst_0
      //   8: istore_3
      //   9: iload_3
      //   10: iload_2
      //   11: if_icmpge -> 37
      //   14: aload_1
      //   15: iload_3
      //   16: aaload
      //   17: astore #4
      //   19: iload_0
      //   20: aload #4
      //   22: invokevirtual getIntValue : ()I
      //   25: if_icmpne -> 31
      //   28: aload #4
      //   30: areturn
      //   31: iinc #3, 1
      //   34: goto -> 9
      //   37: invokestatic getDefault : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
      //   40: astore #4
      //   42: goto -> 28
    }
    
    int getIntValue() {
      return this.mIntValue;
    }
    
    boolean permitsPullToRefresh() {
      return (this != DISABLED && this != MANUAL_REFRESH_ONLY);
    }
    
    public boolean showFooterLoadingLayout() {
      return (this == PULL_FROM_END || this == BOTH || this == MANUAL_REFRESH_ONLY);
    }
    
    public boolean showHeaderLoadingLayout() {
      return (this == PULL_FROM_START || this == BOTH);
    }
  }
  
  public static interface OnLastItemVisibleListener {
    void onLastItemVisible();
  }
  
  public static interface OnPullEventListener<V extends View> {
    void onPullEvent(PullToRefreshBase<V> param1PullToRefreshBase, PullToRefreshBase.State param1State, PullToRefreshBase.Mode param1Mode);
  }
  
  public static interface OnRefreshListener<V extends View> {
    void onRefresh(PullToRefreshBase<V> param1PullToRefreshBase);
  }
  
  public static interface OnRefreshListener2<V extends View> {
    void onPullDownToRefresh(PullToRefreshBase<V> param1PullToRefreshBase);
    
    void onPullUpToRefresh(PullToRefreshBase<V> param1PullToRefreshBase);
  }
  
  static interface OnSmoothScrollFinishedListener {
    void onSmoothScrollFinished();
  }
  
  public enum Orientation {
    VERTICAL,
    HORIZONTAL(1);
    
    static {
      $VALUES = new Orientation[] { VERTICAL, HORIZONTAL };
    }
  }
  
  final class SmoothScrollRunnable implements Runnable {
    private boolean mContinueRunning = true;
    
    private int mCurrentY = -1;
    
    private final long mDuration;
    
    private final Interpolator mInterpolator;
    
    private PullToRefreshBase.OnSmoothScrollFinishedListener mListener;
    
    private final int mScrollFromY;
    
    private final int mScrollToY;
    
    private long mStartTime = -1L;
    
    public SmoothScrollRunnable(int param1Int1, int param1Int2, long param1Long, PullToRefreshBase.OnSmoothScrollFinishedListener param1OnSmoothScrollFinishedListener) {
      this.mScrollFromY = param1Int1;
      this.mScrollToY = param1Int2;
      this.mInterpolator = PullToRefreshBase.this.mScrollAnimationInterpolator;
      this.mDuration = param1Long;
      this.mListener = param1OnSmoothScrollFinishedListener;
    }
    
    public void run() {
      if (this.mStartTime == -1L) {
        this.mStartTime = System.currentTimeMillis();
      } else {
        long l = Math.max(Math.min((System.currentTimeMillis() - this.mStartTime) * 1000L / this.mDuration, 1000L), 0L);
        int i = Math.round((this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation((float)l / 1000.0F));
        this.mCurrentY = this.mScrollFromY - i;
        PullToRefreshBase.this.setHeaderScroll(this.mCurrentY);
      } 
      if (this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
        ViewCompat.postOnAnimation((View)PullToRefreshBase.this, this);
        return;
      } 
      if (this.mListener != null)
        this.mListener.onSmoothScrollFinished(); 
    }
    
    public void stop() {
      this.mContinueRunning = false;
      PullToRefreshBase.this.removeCallbacks(this);
    }
  }
  
  public enum State {
    MANUAL_REFRESHING,
    OVERSCROLLING,
    PULL_TO_REFRESH,
    REFRESHING,
    RELEASE_TO_REFRESH,
    RESET(0);
    
    private int mIntValue;
    
    static {
      REFRESHING = new State("REFRESHING", 3, 8);
      MANUAL_REFRESHING = new State("MANUAL_REFRESHING", 4, 9);
      OVERSCROLLING = new State("OVERSCROLLING", 5, 16);
      $VALUES = new State[] { RESET, PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING, MANUAL_REFRESHING, OVERSCROLLING };
    }
    
    State(int param1Int1) {
      this.mIntValue = param1Int1;
    }
    
    static State mapIntToValue(int param1Int) {
      // Byte code:
      //   0: invokestatic values : ()[Lapp/lib/pullToRefresh/PullToRefreshBase$State;
      //   3: astore_1
      //   4: aload_1
      //   5: arraylength
      //   6: istore_2
      //   7: iconst_0
      //   8: istore_3
      //   9: iload_3
      //   10: iload_2
      //   11: if_icmpge -> 37
      //   14: aload_1
      //   15: iload_3
      //   16: aaload
      //   17: astore #4
      //   19: iload_0
      //   20: aload #4
      //   22: invokevirtual getIntValue : ()I
      //   25: if_icmpne -> 31
      //   28: aload #4
      //   30: areturn
      //   31: iinc #3, 1
      //   34: goto -> 9
      //   37: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RESET : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
      //   40: astore #4
      //   42: goto -> 28
    }
    
    int getIntValue() {
      return this.mIntValue;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/PullToRefreshBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */