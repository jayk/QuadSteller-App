package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.util.Pools;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent {
  static final Class<?>[] CONSTRUCTOR_PARAMS;
  
  static final int EVENT_NESTED_SCROLL = 1;
  
  static final int EVENT_PRE_DRAW = 0;
  
  static final int EVENT_VIEW_REMOVED = 2;
  
  static final String TAG = "CoordinatorLayout";
  
  static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
  
  private static final int TYPE_ON_INTERCEPT = 0;
  
  private static final int TYPE_ON_TOUCH = 1;
  
  static final String WIDGET_PACKAGE_NAME;
  
  static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
  
  private static final Pools.Pool<Rect> sRectPool;
  
  private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
  
  private View mBehaviorTouchView;
  
  private final DirectedAcyclicGraph<View> mChildDag = new DirectedAcyclicGraph<View>();
  
  private final List<View> mDependencySortedChildren = new ArrayList<View>();
  
  private boolean mDisallowInterceptReset;
  
  private boolean mDrawStatusBarBackground;
  
  private boolean mIsAttachedToWindow;
  
  private int[] mKeylines;
  
  private WindowInsetsCompat mLastInsets;
  
  private boolean mNeedsPreDrawListener;
  
  private View mNestedScrollingDirectChild;
  
  private final NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
  
  private View mNestedScrollingTarget;
  
  ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
  
  private OnPreDrawListener mOnPreDrawListener;
  
  private Paint mScrimPaint;
  
  private Drawable mStatusBarBackground;
  
  private final List<View> mTempDependenciesList = new ArrayList<View>();
  
  private final int[] mTempIntPair = new int[2];
  
  private final List<View> mTempList1 = new ArrayList<View>();
  
  static {
    Package package_ = CoordinatorLayout.class.getPackage();
    if (package_ != null) {
      String str = package_.getName();
    } else {
      package_ = null;
    } 
    WIDGET_PACKAGE_NAME = (String)package_;
    if (Build.VERSION.SDK_INT >= 21) {
      TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
    } else {
      TOP_SORTED_CHILDREN_COMPARATOR = null;
    } 
    CONSTRUCTOR_PARAMS = new Class[] { Context.class, AttributeSet.class };
    sConstructors = new ThreadLocal<Map<String, Constructor<Behavior>>>();
    sRectPool = (Pools.Pool<Rect>)new Pools.SynchronizedPool(12);
  }
  
  public CoordinatorLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CoordinatorLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CoordinatorLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme(paramContext);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CoordinatorLayout, paramInt, R.style.Widget_Design_CoordinatorLayout);
    paramInt = typedArray.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
    if (paramInt != 0) {
      Resources resources = paramContext.getResources();
      this.mKeylines = resources.getIntArray(paramInt);
      float f = (resources.getDisplayMetrics()).density;
      int i = this.mKeylines.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        int[] arrayOfInt = this.mKeylines;
        arrayOfInt[paramInt] = (int)(arrayOfInt[paramInt] * f);
      } 
    } 
    this.mStatusBarBackground = typedArray.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
    typedArray.recycle();
    setupForInsets();
    super.setOnHierarchyChangeListener(new HierarchyChangeListener());
  }
  
  @NonNull
  private static Rect acquireTempRect() {
    Rect rect1 = (Rect)sRectPool.acquire();
    Rect rect2 = rect1;
    if (rect1 == null)
      rect2 = new Rect(); 
    return rect2;
  }
  
  private void constrainChildRect(LayoutParams paramLayoutParams, Rect paramRect, int paramInt1, int paramInt2) {
    int i = getWidth();
    int j = getHeight();
    i = Math.max(getPaddingLeft() + paramLayoutParams.leftMargin, Math.min(paramRect.left, i - getPaddingRight() - paramInt1 - paramLayoutParams.rightMargin));
    j = Math.max(getPaddingTop() + paramLayoutParams.topMargin, Math.min(paramRect.top, j - getPaddingBottom() - paramInt2 - paramLayoutParams.bottomMargin));
    paramRect.set(i, j, i + paramInt1, j + paramInt2);
  }
  
  private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat paramWindowInsetsCompat) {
    if (!paramWindowInsetsCompat.isConsumed()) {
      Object object;
      byte b = 0;
      int i = getChildCount();
      while (true) {
        Object object1 = object;
        if (b < i) {
          View view = getChildAt(b);
          object1 = object;
          if (ViewCompat.getFitsSystemWindows(view)) {
            Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
            object1 = object;
            if (behavior != null) {
              object = behavior.onApplyWindowInsets(this, view, (WindowInsetsCompat)object);
              object1 = object;
              if (object.isConsumed()) {
                object1 = object;
              } else {
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            continue;
          } 
        } 
        return (WindowInsetsCompat)object1;
        b++;
        object = SYNTHETIC_LOCAL_VARIABLE_4;
      } 
    } 
    return paramWindowInsetsCompat;
  }
  
  private void getDesiredAnchoredChildRectWithoutConstraints(View paramView, int paramInt1, Rect paramRect1, Rect paramRect2, LayoutParams paramLayoutParams, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload #5
    //   2: getfield gravity : I
    //   5: invokestatic resolveAnchoredChildGravity : (I)I
    //   8: iload_2
    //   9: invokestatic getAbsoluteGravity : (II)I
    //   12: istore #8
    //   14: aload #5
    //   16: getfield anchorGravity : I
    //   19: invokestatic resolveGravity : (I)I
    //   22: iload_2
    //   23: invokestatic getAbsoluteGravity : (II)I
    //   26: istore #9
    //   28: iload #9
    //   30: bipush #7
    //   32: iand
    //   33: lookupswitch default -> 60, 1 -> 208, 5 -> 200
    //   60: aload_3
    //   61: getfield left : I
    //   64: istore_2
    //   65: iload #9
    //   67: bipush #112
    //   69: iand
    //   70: lookupswitch default -> 96, 16 -> 232, 80 -> 223
    //   96: aload_3
    //   97: getfield top : I
    //   100: istore #9
    //   102: iload_2
    //   103: istore #10
    //   105: iload #8
    //   107: bipush #7
    //   109: iand
    //   110: lookupswitch default -> 136, 1 -> 248, 5 -> 142
    //   136: iload_2
    //   137: iload #6
    //   139: isub
    //   140: istore #10
    //   142: iload #9
    //   144: istore_2
    //   145: iload #8
    //   147: bipush #112
    //   149: iand
    //   150: lookupswitch default -> 176, 16 -> 259, 80 -> 182
    //   176: iload #9
    //   178: iload #7
    //   180: isub
    //   181: istore_2
    //   182: aload #4
    //   184: iload #10
    //   186: iload_2
    //   187: iload #10
    //   189: iload #6
    //   191: iadd
    //   192: iload_2
    //   193: iload #7
    //   195: iadd
    //   196: invokevirtual set : (IIII)V
    //   199: return
    //   200: aload_3
    //   201: getfield right : I
    //   204: istore_2
    //   205: goto -> 65
    //   208: aload_3
    //   209: getfield left : I
    //   212: aload_3
    //   213: invokevirtual width : ()I
    //   216: iconst_2
    //   217: idiv
    //   218: iadd
    //   219: istore_2
    //   220: goto -> 65
    //   223: aload_3
    //   224: getfield bottom : I
    //   227: istore #9
    //   229: goto -> 102
    //   232: aload_3
    //   233: getfield top : I
    //   236: aload_3
    //   237: invokevirtual height : ()I
    //   240: iconst_2
    //   241: idiv
    //   242: iadd
    //   243: istore #9
    //   245: goto -> 102
    //   248: iload_2
    //   249: iload #6
    //   251: iconst_2
    //   252: idiv
    //   253: isub
    //   254: istore #10
    //   256: goto -> 142
    //   259: iload #9
    //   261: iload #7
    //   263: iconst_2
    //   264: idiv
    //   265: isub
    //   266: istore_2
    //   267: goto -> 182
  }
  
  private int getKeyline(int paramInt) {
    boolean bool = false;
    if (this.mKeylines == null) {
      Log.e("CoordinatorLayout", "No keylines defined for " + this + " - attempted index lookup " + paramInt);
      return bool;
    } 
    if (paramInt < 0 || paramInt >= this.mKeylines.length) {
      Log.e("CoordinatorLayout", "Keyline index " + paramInt + " out of range for " + this);
      return bool;
    } 
    return this.mKeylines[paramInt];
  }
  
  private void getTopSortedChildren(List<View> paramList) {
    paramList.clear();
    boolean bool = isChildrenDrawingOrderEnabled();
    int i = getChildCount();
    for (int j = i - 1; j >= 0; j--) {
      int k;
      if (bool) {
        k = getChildDrawingOrder(i, j);
      } else {
        k = j;
      } 
      paramList.add(getChildAt(k));
    } 
    if (TOP_SORTED_CHILDREN_COMPARATOR != null)
      Collections.sort(paramList, TOP_SORTED_CHILDREN_COMPARATOR); 
  }
  
  private boolean hasDependencies(View paramView) {
    return this.mChildDag.hasOutgoingEdges(paramView);
  }
  
  private void layoutChild(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect1 = acquireTempRect();
    rect1.set(getPaddingLeft() + layoutParams.leftMargin, getPaddingTop() + layoutParams.topMargin, getWidth() - getPaddingRight() - layoutParams.rightMargin, getHeight() - getPaddingBottom() - layoutParams.bottomMargin);
    if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) && !ViewCompat.getFitsSystemWindows(paramView)) {
      rect1.left += this.mLastInsets.getSystemWindowInsetLeft();
      rect1.top += this.mLastInsets.getSystemWindowInsetTop();
      rect1.right -= this.mLastInsets.getSystemWindowInsetRight();
      rect1.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
    } 
    Rect rect2 = acquireTempRect();
    GravityCompat.apply(resolveGravity(layoutParams.gravity), paramView.getMeasuredWidth(), paramView.getMeasuredHeight(), rect1, rect2, paramInt);
    paramView.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
    releaseTempRect(rect1);
    releaseTempRect(rect2);
  }
  
  private void layoutChildWithAnchor(View paramView1, View paramView2, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView1.getLayoutParams();
    Rect rect1 = acquireTempRect();
    Rect rect2 = acquireTempRect();
    try {
      getDescendantRect(paramView2, rect1);
      getDesiredAnchoredChildRect(paramView1, paramInt, rect1, rect2);
      paramView1.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
      return;
    } finally {
      releaseTempRect(rect1);
      releaseTempRect(rect2);
    } 
  }
  
  private void layoutChildWithKeyline(View paramView, int paramInt1, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), paramInt2);
    int j = getWidth();
    int k = getHeight();
    int m = paramView.getMeasuredWidth();
    int n = paramView.getMeasuredHeight();
    int i1 = paramInt1;
    if (paramInt2 == 1)
      i1 = j - paramInt1; 
    paramInt1 = getKeyline(i1) - m;
    paramInt2 = 0;
    switch (i & 0x7) {
      default:
        switch (i & 0x70) {
          default:
            paramInt1 = Math.max(getPaddingLeft() + layoutParams.leftMargin, Math.min(paramInt1, j - getPaddingRight() - m - layoutParams.rightMargin));
            paramInt2 = Math.max(getPaddingTop() + layoutParams.topMargin, Math.min(paramInt2, k - getPaddingBottom() - n - layoutParams.bottomMargin));
            paramView.layout(paramInt1, paramInt2, paramInt1 + m, paramInt2 + n);
            return;
          case 80:
            paramInt2 = 0 + n;
          case 16:
            break;
        } 
        break;
      case 5:
        paramInt1 += m;
      case 1:
        paramInt1 += m / 2;
    } 
    paramInt2 = 0 + n / 2;
  }
  
  private void offsetChildByInset(View paramView, Rect paramRect, int paramInt) {
    if (ViewCompat.isLaidOut(paramView) && paramView.getWidth() > 0 && paramView.getHeight() > 0) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      Behavior<View> behavior = layoutParams.getBehavior();
      Rect rect1 = acquireTempRect();
      Rect rect2 = acquireTempRect();
      rect2.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
      if (behavior != null && behavior.getInsetDodgeRect(this, paramView, rect1)) {
        if (!rect2.contains(rect1))
          throw new IllegalArgumentException("Rect should be within the child's bounds. Rect:" + rect1.toShortString() + " | Bounds:" + rect2.toShortString()); 
      } else {
        rect1.set(rect2);
      } 
      releaseTempRect(rect2);
      if (rect1.isEmpty()) {
        releaseTempRect(rect1);
        return;
      } 
      int i = GravityCompat.getAbsoluteGravity(layoutParams.dodgeInsetEdges, paramInt);
      int j = 0;
      paramInt = j;
      if ((i & 0x30) == 48) {
        int k = rect1.top - layoutParams.topMargin - layoutParams.mInsetOffsetY;
        paramInt = j;
        if (k < paramRect.top) {
          setInsetOffsetY(paramView, paramRect.top - k);
          paramInt = 1;
        } 
      } 
      j = paramInt;
      if ((i & 0x50) == 80) {
        int k = getHeight() - rect1.bottom - layoutParams.bottomMargin + layoutParams.mInsetOffsetY;
        j = paramInt;
        if (k < paramRect.bottom) {
          setInsetOffsetY(paramView, k - paramRect.bottom);
          j = 1;
        } 
      } 
      if (j == 0)
        setInsetOffsetY(paramView, 0); 
      j = 0;
      paramInt = j;
      if ((i & 0x3) == 3) {
        int k = rect1.left - layoutParams.leftMargin - layoutParams.mInsetOffsetX;
        paramInt = j;
        if (k < paramRect.left) {
          setInsetOffsetX(paramView, paramRect.left - k);
          paramInt = 1;
        } 
      } 
      j = paramInt;
      if ((i & 0x5) == 5) {
        i = getWidth() - rect1.right - layoutParams.rightMargin + layoutParams.mInsetOffsetX;
        j = paramInt;
        if (i < paramRect.right) {
          setInsetOffsetX(paramView, i - paramRect.right);
          j = 1;
        } 
      } 
      if (j == 0)
        setInsetOffsetX(paramView, 0); 
      releaseTempRect(rect1);
    } 
  }
  
  static Behavior parseBehavior(Context paramContext, AttributeSet paramAttributeSet, String paramString) {
    if (TextUtils.isEmpty(paramString))
      return null; 
    if (paramString.startsWith(".")) {
      paramString = paramContext.getPackageName() + paramString;
    } else if (paramString.indexOf('.') < 0 && !TextUtils.isEmpty(WIDGET_PACKAGE_NAME)) {
      paramString = WIDGET_PACKAGE_NAME + '.' + paramString;
    } 
    try {
      Map<Object, Object> map1 = (Map)sConstructors.get();
      Map<Object, Object> map2 = map1;
      if (map1 == null) {
        map2 = new HashMap<Object, Object>();
        super();
        sConstructors.set(map2);
      } 
      Constructor<?> constructor2 = (Constructor)map2.get(paramString);
      Constructor<?> constructor1 = constructor2;
      if (constructor2 == null) {
        constructor1 = Class.forName(paramString, true, paramContext.getClassLoader()).getConstructor(CONSTRUCTOR_PARAMS);
        constructor1.setAccessible(true);
        map2.put(paramString, constructor1);
      } 
      Behavior behavior = (Behavior)constructor1.newInstance(new Object[] { paramContext, paramAttributeSet });
    } catch (Exception exception) {
      throw new RuntimeException("Could not inflate Behavior subclass " + paramString, exception);
    } 
    return (Behavior)exception;
  }
  
  private boolean performIntercept(MotionEvent paramMotionEvent, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: aconst_null
    //   6: astore #5
    //   8: aload_1
    //   9: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   12: istore #6
    //   14: aload_0
    //   15: getfield mTempList1 : Ljava/util/List;
    //   18: astore #7
    //   20: aload_0
    //   21: aload #7
    //   23: invokespecial getTopSortedChildren : (Ljava/util/List;)V
    //   26: aload #7
    //   28: invokeinterface size : ()I
    //   33: istore #8
    //   35: iconst_0
    //   36: istore #9
    //   38: iload_3
    //   39: istore #10
    //   41: iload #9
    //   43: iload #8
    //   45: if_icmpge -> 341
    //   48: aload #7
    //   50: iload #9
    //   52: invokeinterface get : (I)Ljava/lang/Object;
    //   57: checkcast android/view/View
    //   60: astore #11
    //   62: aload #11
    //   64: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   67: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   70: astore #12
    //   72: aload #12
    //   74: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   77: astore #13
    //   79: iload_3
    //   80: ifne -> 88
    //   83: iload #4
    //   85: ifeq -> 226
    //   88: iload #6
    //   90: ifeq -> 226
    //   93: aload #5
    //   95: astore #12
    //   97: iload_3
    //   98: istore #14
    //   100: iload #4
    //   102: istore #15
    //   104: aload #13
    //   106: ifnull -> 167
    //   109: aload #5
    //   111: astore #12
    //   113: aload #5
    //   115: ifnonnull -> 136
    //   118: invokestatic uptimeMillis : ()J
    //   121: lstore #16
    //   123: lload #16
    //   125: lload #16
    //   127: iconst_3
    //   128: fconst_0
    //   129: fconst_0
    //   130: iconst_0
    //   131: invokestatic obtain : (JJIFFI)Landroid/view/MotionEvent;
    //   134: astore #12
    //   136: iload_2
    //   137: tableswitch default -> 160, 0 -> 184, 1 -> 205
    //   160: iload #4
    //   162: istore #15
    //   164: iload_3
    //   165: istore #14
    //   167: iinc #9, 1
    //   170: aload #12
    //   172: astore #5
    //   174: iload #14
    //   176: istore_3
    //   177: iload #15
    //   179: istore #4
    //   181: goto -> 38
    //   184: aload #13
    //   186: aload_0
    //   187: aload #11
    //   189: aload #12
    //   191: invokevirtual onInterceptTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   194: pop
    //   195: iload_3
    //   196: istore #14
    //   198: iload #4
    //   200: istore #15
    //   202: goto -> 167
    //   205: aload #13
    //   207: aload_0
    //   208: aload #11
    //   210: aload #12
    //   212: invokevirtual onTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   215: pop
    //   216: iload_3
    //   217: istore #14
    //   219: iload #4
    //   221: istore #15
    //   223: goto -> 167
    //   226: iload_3
    //   227: istore #10
    //   229: iload_3
    //   230: ifne -> 280
    //   233: iload_3
    //   234: istore #10
    //   236: aload #13
    //   238: ifnull -> 280
    //   241: iload_2
    //   242: tableswitch default -> 264, 0 -> 351, 1 -> 364
    //   264: iload_3
    //   265: istore #10
    //   267: iload_3
    //   268: ifeq -> 280
    //   271: aload_0
    //   272: aload #11
    //   274: putfield mBehaviorTouchView : Landroid/view/View;
    //   277: iload_3
    //   278: istore #10
    //   280: aload #12
    //   282: invokevirtual didBlockInteraction : ()Z
    //   285: istore #14
    //   287: aload #12
    //   289: aload_0
    //   290: aload #11
    //   292: invokevirtual isBlockingInteractionBelow : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;)Z
    //   295: istore_3
    //   296: iload_3
    //   297: ifeq -> 377
    //   300: iload #14
    //   302: ifne -> 377
    //   305: iconst_1
    //   306: istore #4
    //   308: aload #5
    //   310: astore #12
    //   312: iload #10
    //   314: istore #14
    //   316: iload #4
    //   318: istore #15
    //   320: iload_3
    //   321: ifeq -> 167
    //   324: aload #5
    //   326: astore #12
    //   328: iload #10
    //   330: istore #14
    //   332: iload #4
    //   334: istore #15
    //   336: iload #4
    //   338: ifne -> 167
    //   341: aload #7
    //   343: invokeinterface clear : ()V
    //   348: iload #10
    //   350: ireturn
    //   351: aload #13
    //   353: aload_0
    //   354: aload #11
    //   356: aload_1
    //   357: invokevirtual onInterceptTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   360: istore_3
    //   361: goto -> 264
    //   364: aload #13
    //   366: aload_0
    //   367: aload #11
    //   369: aload_1
    //   370: invokevirtual onTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   373: istore_3
    //   374: goto -> 264
    //   377: iconst_0
    //   378: istore #4
    //   380: goto -> 308
  }
  
  private void prepareChildren() {
    this.mDependencySortedChildren.clear();
    this.mChildDag.clear();
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      getResolvedLayoutParams(view).findAnchorView(this, view);
      this.mChildDag.addNode(view);
      for (byte b1 = 0; b1 < i; b1++) {
        if (b1 != b) {
          View view1 = getChildAt(b1);
          if (getResolvedLayoutParams(view1).dependsOn(this, view1, view)) {
            if (!this.mChildDag.contains(view1))
              this.mChildDag.addNode(view1); 
            this.mChildDag.addEdge(view, view1);
          } 
        } 
      } 
      b++;
    } 
    this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
    Collections.reverse(this.mDependencySortedChildren);
  }
  
  private static void releaseTempRect(@NonNull Rect paramRect) {
    paramRect.setEmpty();
    sRectPool.release(paramRect);
  }
  
  private void resetTouchBehaviors() {
    if (this.mBehaviorTouchView != null) {
      Behavior<View> behavior = ((LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
      if (behavior != null) {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        behavior.onTouchEvent(this, this.mBehaviorTouchView, motionEvent);
        motionEvent.recycle();
      } 
      this.mBehaviorTouchView = null;
    } 
    int i = getChildCount();
    for (byte b = 0; b < i; b++)
      ((LayoutParams)getChildAt(b).getLayoutParams()).resetTouchBehaviorTracking(); 
    this.mDisallowInterceptReset = false;
  }
  
  private static int resolveAnchoredChildGravity(int paramInt) {
    int i = paramInt;
    if (paramInt == 0)
      i = 17; 
    return i;
  }
  
  private static int resolveGravity(int paramInt) {
    int i = paramInt;
    if (paramInt == 0)
      i = 8388659; 
    return i;
  }
  
  private static int resolveKeylineGravity(int paramInt) {
    int i = paramInt;
    if (paramInt == 0)
      i = 8388661; 
    return i;
  }
  
  private void setInsetOffsetX(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetX != paramInt) {
      ViewCompat.offsetLeftAndRight(paramView, paramInt - layoutParams.mInsetOffsetX);
      layoutParams.mInsetOffsetX = paramInt;
    } 
  }
  
  private void setInsetOffsetY(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetY != paramInt) {
      ViewCompat.offsetTopAndBottom(paramView, paramInt - layoutParams.mInsetOffsetY);
      layoutParams.mInsetOffsetY = paramInt;
    } 
  }
  
  private void setupForInsets() {
    if (Build.VERSION.SDK_INT >= 21) {
      if (ViewCompat.getFitsSystemWindows((View)this)) {
        if (this.mApplyWindowInsetsListener == null)
          this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
              public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                return CoordinatorLayout.this.setWindowInsets(param1WindowInsetsCompat);
              }
            }; 
        ViewCompat.setOnApplyWindowInsetsListener((View)this, this.mApplyWindowInsetsListener);
        setSystemUiVisibility(1280);
        return;
      } 
      ViewCompat.setOnApplyWindowInsetsListener((View)this, null);
    } 
  }
  
  void addPreDrawListener() {
    if (this.mIsAttachedToWindow) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    this.mNeedsPreDrawListener = true;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
  }
  
  public void dispatchDependentViewsChanged(View paramView) {
    List<View> list = this.mChildDag.getIncomingEdges(paramView);
    if (list != null && !list.isEmpty())
      for (byte b = 0; b < list.size(); b++) {
        View view = list.get(b);
        Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior != null)
          behavior.onDependentViewChanged(this, view, paramView); 
      }  
  }
  
  public boolean doViewsOverlap(View paramView1, View paramView2) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_1
    //   3: invokevirtual getVisibility : ()I
    //   6: ifne -> 165
    //   9: aload_2
    //   10: invokevirtual getVisibility : ()I
    //   13: ifne -> 165
    //   16: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   19: astore #4
    //   21: aload_1
    //   22: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   25: aload_0
    //   26: if_acmpeq -> 135
    //   29: iconst_1
    //   30: istore #5
    //   32: aload_0
    //   33: aload_1
    //   34: iload #5
    //   36: aload #4
    //   38: invokevirtual getChildRect : (Landroid/view/View;ZLandroid/graphics/Rect;)V
    //   41: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   44: astore_1
    //   45: aload_2
    //   46: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   49: aload_0
    //   50: if_acmpeq -> 141
    //   53: iconst_1
    //   54: istore #5
    //   56: aload_0
    //   57: aload_2
    //   58: iload #5
    //   60: aload_1
    //   61: invokevirtual getChildRect : (Landroid/view/View;ZLandroid/graphics/Rect;)V
    //   64: aload #4
    //   66: getfield left : I
    //   69: aload_1
    //   70: getfield right : I
    //   73: if_icmpgt -> 147
    //   76: aload #4
    //   78: getfield top : I
    //   81: aload_1
    //   82: getfield bottom : I
    //   85: if_icmpgt -> 147
    //   88: aload #4
    //   90: getfield right : I
    //   93: aload_1
    //   94: getfield left : I
    //   97: if_icmplt -> 147
    //   100: aload #4
    //   102: getfield bottom : I
    //   105: istore #6
    //   107: aload_1
    //   108: getfield top : I
    //   111: istore #7
    //   113: iload #6
    //   115: iload #7
    //   117: if_icmplt -> 147
    //   120: iload_3
    //   121: istore #5
    //   123: aload #4
    //   125: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   128: aload_1
    //   129: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   132: iload #5
    //   134: ireturn
    //   135: iconst_0
    //   136: istore #5
    //   138: goto -> 32
    //   141: iconst_0
    //   142: istore #5
    //   144: goto -> 56
    //   147: iconst_0
    //   148: istore #5
    //   150: goto -> 123
    //   153: astore_2
    //   154: aload #4
    //   156: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   159: aload_1
    //   160: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   163: aload_2
    //   164: athrow
    //   165: iconst_0
    //   166: istore #5
    //   168: goto -> 132
    // Exception table:
    //   from	to	target	type
    //   64	113	153	finally
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mBehavior != null) {
      float f = layoutParams.mBehavior.getScrimOpacity(this, paramView);
      if (f > 0.0F) {
        if (this.mScrimPaint == null)
          this.mScrimPaint = new Paint(); 
        this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, paramView));
        this.mScrimPaint.setAlpha(MathUtils.constrain(Math.round(255.0F * f), 0, 255));
        int i = paramCanvas.save();
        if (paramView.isOpaque())
          paramCanvas.clipRect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom(), Region.Op.DIFFERENCE); 
        paramCanvas.drawRect(getPaddingLeft(), getPaddingTop(), (getWidth() - getPaddingRight()), (getHeight() - getPaddingBottom()), this.mScrimPaint);
        paramCanvas.restoreToCount(i);
      } 
    } 
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    byte b = 0;
    Drawable drawable = this.mStatusBarBackground;
    int i = b;
    if (drawable != null) {
      i = b;
      if (drawable.isStateful())
        i = false | drawable.setState(arrayOfInt); 
    } 
    if (i != 0)
      invalidate(); 
  }
  
  void ensurePreDrawListener() {
    boolean bool = false;
    int i = getChildCount();
    byte b = 0;
    while (true) {
      boolean bool1 = bool;
      if (b < i)
        if (hasDependencies(getChildAt(b))) {
          bool1 = true;
        } else {
          b++;
          continue;
        }  
      if (bool1 != this.mNeedsPreDrawListener) {
        if (bool1) {
          addPreDrawListener();
          return;
        } 
      } else {
        return;
      } 
      removePreDrawListener();
      return;
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams));
  }
  
  void getChildRect(View paramView, boolean paramBoolean, Rect paramRect) {
    if (paramView.isLayoutRequested() || paramView.getVisibility() == 8) {
      paramRect.setEmpty();
      return;
    } 
    if (paramBoolean) {
      getDescendantRect(paramView, paramRect);
      return;
    } 
    paramRect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
  }
  
  @NonNull
  public List<View> getDependencies(@NonNull View paramView) {
    List<? extends View> list = this.mChildDag.getOutgoingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  @VisibleForTesting
  final List<View> getDependencySortedChildren() {
    prepareChildren();
    return Collections.unmodifiableList(this.mDependencySortedChildren);
  }
  
  @NonNull
  public List<View> getDependents(@NonNull View paramView) {
    List<? extends View> list = this.mChildDag.getIncomingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  void getDescendantRect(View paramView, Rect paramRect) {
    ViewGroupUtils.getDescendantRect(this, paramView, paramRect);
  }
  
  void getDesiredAnchoredChildRect(View paramView, int paramInt, Rect paramRect1, Rect paramRect2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    getDesiredAnchoredChildRectWithoutConstraints(paramView, paramInt, paramRect1, paramRect2, layoutParams, i, j);
    constrainChildRect(layoutParams, paramRect2, i, j);
  }
  
  void getLastChildRect(View paramView, Rect paramRect) {
    paramRect.set(((LayoutParams)paramView.getLayoutParams()).getLastChildRect());
  }
  
  final WindowInsetsCompat getLastWindowInsets() {
    return this.mLastInsets;
  }
  
  public int getNestedScrollAxes() {
    return this.mNestedScrollingParentHelper.getNestedScrollAxes();
  }
  
  LayoutParams getResolvedLayoutParams(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mBehaviorResolved) {
      DefaultBehavior defaultBehavior;
      Class<?> clazz = paramView.getClass();
      paramView = null;
      while (clazz != null) {
        DefaultBehavior defaultBehavior1 = clazz.<DefaultBehavior>getAnnotation(DefaultBehavior.class);
        defaultBehavior = defaultBehavior1;
        if (defaultBehavior1 == null) {
          clazz = clazz.getSuperclass();
          defaultBehavior = defaultBehavior1;
        } 
      } 
      if (defaultBehavior != null)
        try {
          layoutParams.setBehavior(defaultBehavior.value().newInstance());
        } catch (Exception exception) {
          Log.e("CoordinatorLayout", "Default behavior class " + defaultBehavior.value().getName() + " could not be instantiated. Did you forget a default constructor?", exception);
        }  
      layoutParams.mBehaviorResolved = true;
    } 
    return layoutParams;
  }
  
  @Nullable
  public Drawable getStatusBarBackground() {
    return this.mStatusBarBackground;
  }
  
  protected int getSuggestedMinimumHeight() {
    return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
  }
  
  protected int getSuggestedMinimumWidth() {
    return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
  }
  
  public boolean isPointInChildBounds(View paramView, int paramInt1, int paramInt2) {
    Rect rect = acquireTempRect();
    getDescendantRect(paramView, rect);
    try {
      return rect.contains(paramInt1, paramInt2);
    } finally {
      releaseTempRect(rect);
    } 
  }
  
  void offsetChildToAnchor(View paramView, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_1
    //   3: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   6: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   9: astore #4
    //   11: aload #4
    //   13: getfield mAnchorView : Landroid/view/View;
    //   16: ifnull -> 212
    //   19: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   22: astore #5
    //   24: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   27: astore #6
    //   29: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   32: astore #7
    //   34: aload_0
    //   35: aload #4
    //   37: getfield mAnchorView : Landroid/view/View;
    //   40: aload #5
    //   42: invokevirtual getDescendantRect : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   45: aload_0
    //   46: aload_1
    //   47: iconst_0
    //   48: aload #6
    //   50: invokevirtual getChildRect : (Landroid/view/View;ZLandroid/graphics/Rect;)V
    //   53: aload_1
    //   54: invokevirtual getMeasuredWidth : ()I
    //   57: istore #8
    //   59: aload_1
    //   60: invokevirtual getMeasuredHeight : ()I
    //   63: istore #9
    //   65: aload_0
    //   66: aload_1
    //   67: iload_2
    //   68: aload #5
    //   70: aload #7
    //   72: aload #4
    //   74: iload #8
    //   76: iload #9
    //   78: invokespecial getDesiredAnchoredChildRectWithoutConstraints : (Landroid/view/View;ILandroid/graphics/Rect;Landroid/graphics/Rect;Landroid/support/design/widget/CoordinatorLayout$LayoutParams;II)V
    //   81: aload #7
    //   83: getfield left : I
    //   86: aload #6
    //   88: getfield left : I
    //   91: if_icmpne -> 109
    //   94: iload_3
    //   95: istore_2
    //   96: aload #7
    //   98: getfield top : I
    //   101: aload #6
    //   103: getfield top : I
    //   106: if_icmpeq -> 111
    //   109: iconst_1
    //   110: istore_2
    //   111: aload_0
    //   112: aload #4
    //   114: aload #7
    //   116: iload #8
    //   118: iload #9
    //   120: invokespecial constrainChildRect : (Landroid/support/design/widget/CoordinatorLayout$LayoutParams;Landroid/graphics/Rect;II)V
    //   123: aload #7
    //   125: getfield left : I
    //   128: aload #6
    //   130: getfield left : I
    //   133: isub
    //   134: istore_3
    //   135: aload #7
    //   137: getfield top : I
    //   140: aload #6
    //   142: getfield top : I
    //   145: isub
    //   146: istore #8
    //   148: iload_3
    //   149: ifeq -> 157
    //   152: aload_1
    //   153: iload_3
    //   154: invokestatic offsetLeftAndRight : (Landroid/view/View;I)V
    //   157: iload #8
    //   159: ifeq -> 168
    //   162: aload_1
    //   163: iload #8
    //   165: invokestatic offsetTopAndBottom : (Landroid/view/View;I)V
    //   168: iload_2
    //   169: ifeq -> 197
    //   172: aload #4
    //   174: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   177: astore #10
    //   179: aload #10
    //   181: ifnull -> 197
    //   184: aload #10
    //   186: aload_0
    //   187: aload_1
    //   188: aload #4
    //   190: getfield mAnchorView : Landroid/view/View;
    //   193: invokevirtual onDependentViewChanged : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/View;)Z
    //   196: pop
    //   197: aload #5
    //   199: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   202: aload #6
    //   204: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   207: aload #7
    //   209: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   212: return
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    resetTouchBehaviors();
    if (this.mNeedsPreDrawListener) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows((View)this))
      ViewCompat.requestApplyInsets((View)this); 
    this.mIsAttachedToWindow = true;
  }
  
  final void onChildViewsChanged(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    int j = this.mDependencySortedChildren.size();
    Rect rect1 = acquireTempRect();
    Rect rect2 = acquireTempRect();
    Rect rect3 = acquireTempRect();
    byte b = 0;
    label57: while (b < j) {
      View view = this.mDependencySortedChildren.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (paramInt == 0 && view.getVisibility() == 8)
        continue; 
      int k;
      for (k = 0; k < b; k++) {
        View view1 = this.mDependencySortedChildren.get(k);
        if (layoutParams.mAnchorDirectChild == view1)
          offsetChildToAnchor(view, i); 
      } 
      getChildRect(view, true, rect2);
      if (layoutParams.insetEdge != 0 && !rect2.isEmpty()) {
        k = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, i);
        switch (k & 0x70) {
          default:
            switch (k & 0x7) {
              default:
                if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0)
                  offsetChildByInset(view, rect1, i); 
                if (paramInt == 0) {
                  getLastChildRect(view, rect3);
                  if (!rect3.equals(rect2)) {
                    recordLastChildRect(view, rect2);
                  } else {
                    continue;
                  } 
                } 
                k = b + 1;
                while (true) {
                  if (k < j) {
                    View view1 = this.mDependencySortedChildren.get(k);
                    LayoutParams layoutParams1 = (LayoutParams)view1.getLayoutParams();
                    Behavior<View> behavior = layoutParams1.getBehavior();
                    if (behavior != null && behavior.layoutDependsOn(this, view1, view))
                      if (paramInt == 0 && layoutParams1.getChangedAfterNestedScroll()) {
                        layoutParams1.resetChangedAfterNestedScroll();
                      } else {
                        boolean bool;
                        switch (paramInt) {
                          case 2:
                            behavior.onDependentViewRemoved(this, view1, view);
                            bool = true;
                            if (paramInt == 1)
                              layoutParams1.setChangedAfterNestedScroll(bool); 
                            k++;
                            break;
                        } 
                        continue;
                      }  
                  } else {
                    b++;
                    continue label57;
                  } 
                  k++;
                  break;
                } 
              case 3:
                rect1.left = Math.max(rect1.left, rect2.right);
              case 5:
                break;
            } 
            break;
          case 48:
            rect1.top = Math.max(rect1.top, rect2.bottom);
          case 80:
            rect1.bottom = Math.max(rect1.bottom, getHeight() - rect2.top);
        } 
        rect1.right = Math.max(rect1.right, getWidth() - rect2.left);
      } 
    } 
    releaseTempRect(rect1);
    releaseTempRect(rect2);
    releaseTempRect(rect3);
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    resetTouchBehaviors();
    if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    if (this.mNestedScrollingTarget != null)
      onStopNestedScroll(this.mNestedScrollingTarget); 
    this.mIsAttachedToWindow = false;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      boolean bool;
      if (this.mLastInsets != null) {
        bool = this.mLastInsets.getSystemWindowInsetTop();
      } else {
        bool = false;
      } 
      if (bool) {
        this.mStatusBarBackground.setBounds(0, 0, getWidth(), bool);
        this.mStatusBarBackground.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0)
      resetTouchBehaviors(); 
    boolean bool = performIntercept(paramMotionEvent, 0);
    if (false)
      throw new NullPointerException(); 
    if (i == 1 || i == 3)
      resetTouchBehaviors(); 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = ViewCompat.getLayoutDirection((View)this);
    paramInt3 = this.mDependencySortedChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = this.mDependencySortedChildren.get(paramInt1);
      if (view.getVisibility() != 8) {
        Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior == null || !behavior.onLayoutChild(this, view, paramInt2))
          onLayoutChild(view, paramInt2); 
      } 
    } 
  }
  
  public void onLayoutChild(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.checkAnchorChanged())
      throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete."); 
    if (layoutParams.mAnchorView != null) {
      layoutChildWithAnchor(paramView, layoutParams.mAnchorView, paramInt);
      return;
    } 
    if (layoutParams.keyline >= 0) {
      layoutChildWithKeyline(paramView, layoutParams.keyline, paramInt);
      return;
    } 
    layoutChild(paramView, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial prepareChildren : ()V
    //   4: aload_0
    //   5: invokevirtual ensurePreDrawListener : ()V
    //   8: aload_0
    //   9: invokevirtual getPaddingLeft : ()I
    //   12: istore_3
    //   13: aload_0
    //   14: invokevirtual getPaddingTop : ()I
    //   17: istore #4
    //   19: aload_0
    //   20: invokevirtual getPaddingRight : ()I
    //   23: istore #5
    //   25: aload_0
    //   26: invokevirtual getPaddingBottom : ()I
    //   29: istore #6
    //   31: aload_0
    //   32: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   35: istore #7
    //   37: iload #7
    //   39: iconst_1
    //   40: if_icmpne -> 155
    //   43: iconst_1
    //   44: istore #8
    //   46: iload_1
    //   47: invokestatic getMode : (I)I
    //   50: istore #9
    //   52: iload_1
    //   53: invokestatic getSize : (I)I
    //   56: istore #10
    //   58: iload_2
    //   59: invokestatic getMode : (I)I
    //   62: istore #11
    //   64: iload_2
    //   65: invokestatic getSize : (I)I
    //   68: istore #12
    //   70: aload_0
    //   71: invokevirtual getSuggestedMinimumWidth : ()I
    //   74: istore #13
    //   76: aload_0
    //   77: invokevirtual getSuggestedMinimumHeight : ()I
    //   80: istore #14
    //   82: iconst_0
    //   83: istore #15
    //   85: aload_0
    //   86: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   89: ifnull -> 161
    //   92: aload_0
    //   93: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   96: ifeq -> 161
    //   99: iconst_1
    //   100: istore #16
    //   102: aload_0
    //   103: getfield mDependencySortedChildren : Ljava/util/List;
    //   106: invokeinterface size : ()I
    //   111: istore #17
    //   113: iconst_0
    //   114: istore #18
    //   116: iload #18
    //   118: iload #17
    //   120: if_icmpge -> 527
    //   123: aload_0
    //   124: getfield mDependencySortedChildren : Ljava/util/List;
    //   127: iload #18
    //   129: invokeinterface get : (I)Ljava/lang/Object;
    //   134: checkcast android/view/View
    //   137: astore #19
    //   139: aload #19
    //   141: invokevirtual getVisibility : ()I
    //   144: bipush #8
    //   146: if_icmpne -> 167
    //   149: iinc #18, 1
    //   152: goto -> 116
    //   155: iconst_0
    //   156: istore #8
    //   158: goto -> 46
    //   161: iconst_0
    //   162: istore #16
    //   164: goto -> 102
    //   167: aload #19
    //   169: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   172: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   175: astore #20
    //   177: iconst_0
    //   178: istore #21
    //   180: iload #21
    //   182: istore #22
    //   184: aload #20
    //   186: getfield keyline : I
    //   189: iflt -> 266
    //   192: iload #21
    //   194: istore #22
    //   196: iload #9
    //   198: ifeq -> 266
    //   201: aload_0
    //   202: aload #20
    //   204: getfield keyline : I
    //   207: invokespecial getKeyline : (I)I
    //   210: istore #23
    //   212: aload #20
    //   214: getfield gravity : I
    //   217: invokestatic resolveKeylineGravity : (I)I
    //   220: iload #7
    //   222: invokestatic getAbsoluteGravity : (II)I
    //   225: bipush #7
    //   227: iand
    //   228: istore #24
    //   230: iload #24
    //   232: iconst_3
    //   233: if_icmpne -> 241
    //   236: iload #8
    //   238: ifeq -> 252
    //   241: iload #24
    //   243: iconst_5
    //   244: if_icmpne -> 484
    //   247: iload #8
    //   249: ifeq -> 484
    //   252: iconst_0
    //   253: iload #10
    //   255: iload #5
    //   257: isub
    //   258: iload #23
    //   260: isub
    //   261: invokestatic max : (II)I
    //   264: istore #22
    //   266: iload_1
    //   267: istore #24
    //   269: iload_2
    //   270: istore #23
    //   272: iload #24
    //   274: istore #25
    //   276: iload #23
    //   278: istore #21
    //   280: iload #16
    //   282: ifeq -> 367
    //   285: iload #24
    //   287: istore #25
    //   289: iload #23
    //   291: istore #21
    //   293: aload #19
    //   295: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   298: ifne -> 367
    //   301: aload_0
    //   302: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   305: invokevirtual getSystemWindowInsetLeft : ()I
    //   308: istore #25
    //   310: aload_0
    //   311: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   314: invokevirtual getSystemWindowInsetRight : ()I
    //   317: istore #23
    //   319: aload_0
    //   320: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   323: invokevirtual getSystemWindowInsetTop : ()I
    //   326: istore #24
    //   328: aload_0
    //   329: getfield mLastInsets : Landroid/support/v4/view/WindowInsetsCompat;
    //   332: invokevirtual getSystemWindowInsetBottom : ()I
    //   335: istore #21
    //   337: iload #10
    //   339: iload #25
    //   341: iload #23
    //   343: iadd
    //   344: isub
    //   345: iload #9
    //   347: invokestatic makeMeasureSpec : (II)I
    //   350: istore #25
    //   352: iload #12
    //   354: iload #24
    //   356: iload #21
    //   358: iadd
    //   359: isub
    //   360: iload #11
    //   362: invokestatic makeMeasureSpec : (II)I
    //   365: istore #21
    //   367: aload #20
    //   369: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   372: astore #26
    //   374: aload #26
    //   376: ifnull -> 397
    //   379: aload #26
    //   381: aload_0
    //   382: aload #19
    //   384: iload #25
    //   386: iload #22
    //   388: iload #21
    //   390: iconst_0
    //   391: invokevirtual onMeasureChild : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;IIII)Z
    //   394: ifne -> 410
    //   397: aload_0
    //   398: aload #19
    //   400: iload #25
    //   402: iload #22
    //   404: iload #21
    //   406: iconst_0
    //   407: invokevirtual onMeasureChild : (Landroid/view/View;IIII)V
    //   410: iload #13
    //   412: aload #19
    //   414: invokevirtual getMeasuredWidth : ()I
    //   417: iload_3
    //   418: iload #5
    //   420: iadd
    //   421: iadd
    //   422: aload #20
    //   424: getfield leftMargin : I
    //   427: iadd
    //   428: aload #20
    //   430: getfield rightMargin : I
    //   433: iadd
    //   434: invokestatic max : (II)I
    //   437: istore #13
    //   439: iload #14
    //   441: aload #19
    //   443: invokevirtual getMeasuredHeight : ()I
    //   446: iload #4
    //   448: iload #6
    //   450: iadd
    //   451: iadd
    //   452: aload #20
    //   454: getfield topMargin : I
    //   457: iadd
    //   458: aload #20
    //   460: getfield bottomMargin : I
    //   463: iadd
    //   464: invokestatic max : (II)I
    //   467: istore #14
    //   469: iload #15
    //   471: aload #19
    //   473: invokestatic getMeasuredState : (Landroid/view/View;)I
    //   476: invokestatic combineMeasuredStates : (II)I
    //   479: istore #15
    //   481: goto -> 149
    //   484: iload #24
    //   486: iconst_5
    //   487: if_icmpne -> 495
    //   490: iload #8
    //   492: ifeq -> 514
    //   495: iload #21
    //   497: istore #22
    //   499: iload #24
    //   501: iconst_3
    //   502: if_icmpne -> 266
    //   505: iload #21
    //   507: istore #22
    //   509: iload #8
    //   511: ifeq -> 266
    //   514: iconst_0
    //   515: iload #23
    //   517: iload_3
    //   518: isub
    //   519: invokestatic max : (II)I
    //   522: istore #22
    //   524: goto -> 266
    //   527: aload_0
    //   528: iload #13
    //   530: iload_1
    //   531: ldc_w -16777216
    //   534: iload #15
    //   536: iand
    //   537: invokestatic resolveSizeAndState : (III)I
    //   540: iload #14
    //   542: iload_2
    //   543: iload #15
    //   545: bipush #16
    //   547: ishl
    //   548: invokestatic resolveSizeAndState : (III)I
    //   551: invokevirtual setMeasuredDimension : (II)V
    //   554: return
  }
  
  public void onMeasureChild(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    boolean bool = false;
    int i = getChildCount();
    byte b = 0;
    while (b < i) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        bool1 = bool;
        if (layoutParams.isNestedScrollAccepted()) {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = bool | behavior.onNestedFling(this, view, paramView, paramFloat1, paramFloat2, paramBoolean); 
        } 
      } 
      b++;
      bool = bool1;
    } 
    if (bool)
      onChildViewsChanged(1); 
    return bool;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool = false;
    int i = getChildCount();
    byte b = 0;
    while (b < i) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        bool1 = bool;
        if (layoutParams.isNestedScrollAccepted()) {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = bool | behavior.onNestedPreFling(this, view, paramView, paramFloat1, paramFloat2); 
        } 
      } 
      b++;
      bool = bool1;
    } 
    return bool;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    int i = 0;
    int j = 0;
    boolean bool = false;
    int k = getChildCount();
    byte b = 0;
    while (b < k) {
      int m;
      int n;
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        m = j;
        n = i;
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        bool1 = bool;
        n = i;
        m = j;
        if (layoutParams.isNestedScrollAccepted()) {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          n = i;
          m = j;
          if (behavior != null) {
            int[] arrayOfInt = this.mTempIntPair;
            this.mTempIntPair[1] = 0;
            arrayOfInt[0] = 0;
            behavior.onNestedPreScroll(this, view, paramView, paramInt1, paramInt2, this.mTempIntPair);
            if (paramInt1 > 0) {
              n = Math.max(i, this.mTempIntPair[0]);
            } else {
              n = Math.min(i, this.mTempIntPair[0]);
            } 
            if (paramInt2 > 0) {
              j = Math.max(j, this.mTempIntPair[1]);
            } else {
              j = Math.min(j, this.mTempIntPair[1]);
            } 
            bool1 = true;
            m = j;
          } 
        } 
      } 
      b++;
      bool = bool1;
      i = n;
      j = m;
    } 
    paramArrayOfint[0] = i;
    paramArrayOfint[1] = j;
    if (bool)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    boolean bool = false;
    byte b = 0;
    while (b < i) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        bool1 = bool;
        if (layoutParams.isNestedScrollAccepted()) {
          Behavior<View> behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null) {
            behavior.onNestedScroll(this, view, paramView, paramInt1, paramInt2, paramInt3, paramInt4);
            bool1 = true;
          } 
        } 
      } 
      b++;
      bool = bool1;
    } 
    if (bool)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {
    this.mNestedScrollingParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    this.mNestedScrollingDirectChild = paramView1;
    this.mNestedScrollingTarget = paramView2;
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted()) {
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onNestedScrollAccepted(this, view, paramView1, paramView2, paramInt); 
      } 
    } 
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    SparseArray<Parcelable> sparseArray = savedState.behaviorStates;
    byte b = 0;
    int i = getChildCount();
    while (true) {
      if (b < i) {
        View view = getChildAt(b);
        int j = view.getId();
        Behavior<View> behavior = getResolvedLayoutParams(view).getBehavior();
        if (j != -1 && behavior != null) {
          Parcelable parcelable = (Parcelable)sparseArray.get(j);
          if (parcelable != null)
            behavior.onRestoreInstanceState(this, view, parcelable); 
        } 
        b++;
        continue;
      } 
      return;
    } 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    SparseArray<Parcelable> sparseArray = new SparseArray();
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      int j = view.getId();
      Behavior<View> behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
      if (j != -1 && behavior != null) {
        Parcelable parcelable = behavior.onSaveInstanceState(this, view);
        if (parcelable != null)
          sparseArray.append(j, parcelable); 
      } 
      b++;
    } 
    savedState.behaviorStates = sparseArray;
    return (Parcelable)savedState;
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) {
    boolean bool = false;
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null) {
          boolean bool1 = behavior.onStartNestedScroll(this, view, paramView1, paramView2, paramInt);
          bool |= bool1;
          layoutParams.acceptNestedScroll(bool1);
        } else {
          layoutParams.acceptNestedScroll(false);
        } 
      } 
    } 
    return bool;
  }
  
  public void onStopNestedScroll(View paramView) {
    this.mNestedScrollingParentHelper.onStopNestedScroll(paramView);
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted()) {
        Behavior<View> behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onStopNestedScroll(this, view, paramView); 
        layoutParams.resetNestedScroll();
        layoutParams.resetChangedAfterNestedScroll();
      } 
    } 
    this.mNestedScrollingDirectChild = null;
    this.mNestedScrollingTarget = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: aconst_null
    //   5: astore #4
    //   7: aconst_null
    //   8: astore #5
    //   10: aload_1
    //   11: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   14: istore #6
    //   16: aload_0
    //   17: getfield mBehaviorTouchView : Landroid/view/View;
    //   20: ifnonnull -> 40
    //   23: aload_0
    //   24: aload_1
    //   25: iconst_1
    //   26: invokespecial performIntercept : (Landroid/view/MotionEvent;I)Z
    //   29: istore_3
    //   30: iload_3
    //   31: istore #7
    //   33: iload_2
    //   34: istore #8
    //   36: iload_3
    //   37: ifeq -> 82
    //   40: aload_0
    //   41: getfield mBehaviorTouchView : Landroid/view/View;
    //   44: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   47: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   50: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   53: astore #9
    //   55: iload_3
    //   56: istore #7
    //   58: iload_2
    //   59: istore #8
    //   61: aload #9
    //   63: ifnull -> 82
    //   66: aload #9
    //   68: aload_0
    //   69: aload_0
    //   70: getfield mBehaviorTouchView : Landroid/view/View;
    //   73: aload_1
    //   74: invokevirtual onTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   77: istore #8
    //   79: iload_3
    //   80: istore #7
    //   82: aload_0
    //   83: getfield mBehaviorTouchView : Landroid/view/View;
    //   86: ifnonnull -> 136
    //   89: iload #8
    //   91: aload_0
    //   92: aload_1
    //   93: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   96: ior
    //   97: istore_3
    //   98: aload #5
    //   100: astore_1
    //   101: iload_3
    //   102: ifne -> 110
    //   105: iload #6
    //   107: ifne -> 110
    //   110: aload_1
    //   111: ifnull -> 118
    //   114: aload_1
    //   115: invokevirtual recycle : ()V
    //   118: iload #6
    //   120: iconst_1
    //   121: if_icmpeq -> 130
    //   124: iload #6
    //   126: iconst_3
    //   127: if_icmpne -> 134
    //   130: aload_0
    //   131: invokespecial resetTouchBehaviors : ()V
    //   134: iload_3
    //   135: ireturn
    //   136: aload #5
    //   138: astore_1
    //   139: iload #8
    //   141: istore_3
    //   142: iload #7
    //   144: ifeq -> 101
    //   147: aload #4
    //   149: astore_1
    //   150: iconst_0
    //   151: ifne -> 171
    //   154: invokestatic uptimeMillis : ()J
    //   157: lstore #10
    //   159: lload #10
    //   161: lload #10
    //   163: iconst_3
    //   164: fconst_0
    //   165: fconst_0
    //   166: iconst_0
    //   167: invokestatic obtain : (JJIFFI)Landroid/view/MotionEvent;
    //   170: astore_1
    //   171: aload_0
    //   172: aload_1
    //   173: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   176: pop
    //   177: iload #8
    //   179: istore_3
    //   180: goto -> 101
  }
  
  void recordLastChildRect(View paramView, Rect paramRect) {
    ((LayoutParams)paramView.getLayoutParams()).setLastChildRect(paramRect);
  }
  
  void removePreDrawListener() {
    if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    this.mNeedsPreDrawListener = false;
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    Behavior<View> behavior = ((LayoutParams)paramView.getLayoutParams()).getBehavior();
    return (behavior != null && behavior.onRequestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean)) ? true : super.requestChildRectangleOnScreen(paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    if (paramBoolean && !this.mDisallowInterceptReset) {
      resetTouchBehaviors();
      this.mDisallowInterceptReset = true;
    } 
  }
  
  public void setFitsSystemWindows(boolean paramBoolean) {
    super.setFitsSystemWindows(paramBoolean);
    setupForInsets();
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener) {
    this.mOnHierarchyChangeListener = paramOnHierarchyChangeListener;
  }
  
  public void setStatusBarBackground(@Nullable Drawable paramDrawable) {
    Drawable drawable = null;
    if (this.mStatusBarBackground != paramDrawable) {
      if (this.mStatusBarBackground != null)
        this.mStatusBarBackground.setCallback(null); 
      if (paramDrawable != null)
        drawable = paramDrawable.mutate(); 
      this.mStatusBarBackground = drawable;
      if (this.mStatusBarBackground != null) {
        boolean bool;
        if (this.mStatusBarBackground.isStateful())
          this.mStatusBarBackground.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection((View)this));
        paramDrawable = this.mStatusBarBackground;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.mStatusBarBackground.setCallback((Drawable.Callback)this);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) {
    setStatusBarBackground((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setStatusBarBackgroundResource(@DrawableRes int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setStatusBarBackground(drawable);
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mStatusBarBackground != null && this.mStatusBarBackground.isVisible() != bool)
      this.mStatusBarBackground.setVisible(bool, false); 
  }
  
  final WindowInsetsCompat setWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    boolean bool = true;
    WindowInsetsCompat windowInsetsCompat = paramWindowInsetsCompat;
    if (!ViewUtils.objectEquals(this.mLastInsets, paramWindowInsetsCompat)) {
      boolean bool1;
      this.mLastInsets = paramWindowInsetsCompat;
      if (paramWindowInsetsCompat != null && paramWindowInsetsCompat.getSystemWindowInsetTop() > 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.mDrawStatusBarBackground = bool1;
      if (!this.mDrawStatusBarBackground && getBackground() == null) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      setWillNotDraw(bool1);
      windowInsetsCompat = dispatchApplyWindowInsetsToBehaviors(paramWindowInsetsCompat);
      requestLayout();
    } 
    return windowInsetsCompat;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mStatusBarBackground);
  }
  
  public static abstract class Behavior<V extends View> {
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {}
    
    public static Object getTag(View param1View) {
      return ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag;
    }
    
    public static void setTag(View param1View, Object param1Object) {
      ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag = param1Object;
    }
    
    public boolean blocksInteractionBelow(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      return (getScrimOpacity(param1CoordinatorLayout, param1V) > 0.0F);
    }
    
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Rect param1Rect) {
      return false;
    }
    
    @ColorInt
    public int getScrimColor(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      return -16777216;
    }
    
    @FloatRange(from = 0.0D, to = 1.0D)
    public float getScrimOpacity(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      return 0.0F;
    }
    
    @Deprecated
    public boolean isDirty(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      return false;
    }
    
    public boolean layoutDependsOn(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View) {
      return false;
    }
    
    @NonNull
    public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout param1CoordinatorLayout, V param1V, WindowInsetsCompat param1WindowInsetsCompat) {
      return param1WindowInsetsCompat;
    }
    
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams param1LayoutParams) {}
    
    public boolean onDependentViewChanged(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View) {
      return false;
    }
    
    public void onDependentViewRemoved(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View) {}
    
    public void onDetachedFromLayoutParams() {}
    
    public boolean onInterceptTouchEvent(CoordinatorLayout param1CoordinatorLayout, V param1V, MotionEvent param1MotionEvent) {
      return false;
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, V param1V, int param1Int) {
      return false;
    }
    
    public boolean onMeasureChild(CoordinatorLayout param1CoordinatorLayout, V param1V, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return false;
    }
    
    public boolean onNestedFling(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View, float param1Float1, float param1Float2, boolean param1Boolean) {
      return false;
    }
    
    public boolean onNestedPreFling(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View, float param1Float1, float param1Float2) {
      return false;
    }
    
    public void onNestedPreScroll(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View, int param1Int1, int param1Int2, int[] param1ArrayOfint) {}
    
    public void onNestedScroll(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onNestedScrollAccepted(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View1, View param1View2, int param1Int) {}
    
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout param1CoordinatorLayout, V param1V, Rect param1Rect, boolean param1Boolean) {
      return false;
    }
    
    public void onRestoreInstanceState(CoordinatorLayout param1CoordinatorLayout, V param1V, Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      return (Parcelable)View.BaseSavedState.EMPTY_STATE;
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View1, View param1View2, int param1Int) {
      return false;
    }
    
    public void onStopNestedScroll(CoordinatorLayout param1CoordinatorLayout, V param1V, View param1View) {}
    
    public boolean onTouchEvent(CoordinatorLayout param1CoordinatorLayout, V param1V, MotionEvent param1MotionEvent) {
      return false;
    }
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface DefaultBehavior {
    Class<? extends CoordinatorLayout.Behavior> value();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DispatchChangeEvent {}
  
  private class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
    public void onChildViewAdded(View param1View1, View param1View2) {
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(param1View1, param1View2); 
    }
    
    public void onChildViewRemoved(View param1View1, View param1View2) {
      CoordinatorLayout.this.onChildViewsChanged(2);
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(param1View1, param1View2); 
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int anchorGravity = 0;
    
    public int dodgeInsetEdges = 0;
    
    public int gravity = 0;
    
    public int insetEdge = 0;
    
    public int keyline = -1;
    
    View mAnchorDirectChild;
    
    int mAnchorId = -1;
    
    View mAnchorView;
    
    CoordinatorLayout.Behavior mBehavior;
    
    boolean mBehaviorResolved = false;
    
    Object mBehaviorTag;
    
    private boolean mDidAcceptNestedScroll;
    
    private boolean mDidBlockInteraction;
    
    private boolean mDidChangeAfterNestedScroll;
    
    int mInsetOffsetX;
    
    int mInsetOffsetY;
    
    final Rect mLastChildRect = new Rect();
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.CoordinatorLayout_Layout);
      this.gravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
      this.mAnchorId = typedArray.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
      this.anchorGravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
      this.keyline = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
      this.insetEdge = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
      this.dodgeInsetEdges = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
      this.mBehaviorResolved = typedArray.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
      if (this.mBehaviorResolved)
        this.mBehavior = CoordinatorLayout.parseBehavior(param1Context, param1AttributeSet, typedArray.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior)); 
      typedArray.recycle();
      if (this.mBehavior != null)
        this.mBehavior.onAttachedToLayoutParams(this); 
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    private void resolveAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      // Byte code:
      //   0: aload_0
      //   1: aload_2
      //   2: aload_0
      //   3: getfield mAnchorId : I
      //   6: invokevirtual findViewById : (I)Landroid/view/View;
      //   9: putfield mAnchorView : Landroid/view/View;
      //   12: aload_0
      //   13: getfield mAnchorView : Landroid/view/View;
      //   16: ifnull -> 150
      //   19: aload_0
      //   20: getfield mAnchorView : Landroid/view/View;
      //   23: aload_2
      //   24: if_acmpne -> 55
      //   27: aload_2
      //   28: invokevirtual isInEditMode : ()Z
      //   31: ifeq -> 45
      //   34: aload_0
      //   35: aconst_null
      //   36: putfield mAnchorDirectChild : Landroid/view/View;
      //   39: aload_0
      //   40: aconst_null
      //   41: putfield mAnchorView : Landroid/view/View;
      //   44: return
      //   45: new java/lang/IllegalStateException
      //   48: dup
      //   49: ldc 'View can not be anchored to the the parent CoordinatorLayout'
      //   51: invokespecial <init> : (Ljava/lang/String;)V
      //   54: athrow
      //   55: aload_0
      //   56: getfield mAnchorView : Landroid/view/View;
      //   59: astore_3
      //   60: aload_0
      //   61: getfield mAnchorView : Landroid/view/View;
      //   64: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   67: astore #4
      //   69: aload #4
      //   71: aload_2
      //   72: if_acmpeq -> 142
      //   75: aload #4
      //   77: ifnull -> 142
      //   80: aload #4
      //   82: aload_1
      //   83: if_acmpne -> 116
      //   86: aload_2
      //   87: invokevirtual isInEditMode : ()Z
      //   90: ifeq -> 106
      //   93: aload_0
      //   94: aconst_null
      //   95: putfield mAnchorDirectChild : Landroid/view/View;
      //   98: aload_0
      //   99: aconst_null
      //   100: putfield mAnchorView : Landroid/view/View;
      //   103: goto -> 44
      //   106: new java/lang/IllegalStateException
      //   109: dup
      //   110: ldc 'Anchor must not be a descendant of the anchored view'
      //   112: invokespecial <init> : (Ljava/lang/String;)V
      //   115: athrow
      //   116: aload #4
      //   118: instanceof android/view/View
      //   121: ifeq -> 130
      //   124: aload #4
      //   126: checkcast android/view/View
      //   129: astore_3
      //   130: aload #4
      //   132: invokeinterface getParent : ()Landroid/view/ViewParent;
      //   137: astore #4
      //   139: goto -> 69
      //   142: aload_0
      //   143: aload_3
      //   144: putfield mAnchorDirectChild : Landroid/view/View;
      //   147: goto -> 44
      //   150: aload_2
      //   151: invokevirtual isInEditMode : ()Z
      //   154: ifeq -> 170
      //   157: aload_0
      //   158: aconst_null
      //   159: putfield mAnchorDirectChild : Landroid/view/View;
      //   162: aload_0
      //   163: aconst_null
      //   164: putfield mAnchorView : Landroid/view/View;
      //   167: goto -> 44
      //   170: new java/lang/IllegalStateException
      //   173: dup
      //   174: new java/lang/StringBuilder
      //   177: dup
      //   178: invokespecial <init> : ()V
      //   181: ldc 'Could not find CoordinatorLayout descendant view with id '
      //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   186: aload_2
      //   187: invokevirtual getResources : ()Landroid/content/res/Resources;
      //   190: aload_0
      //   191: getfield mAnchorId : I
      //   194: invokevirtual getResourceName : (I)Ljava/lang/String;
      //   197: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   200: ldc ' to anchor view '
      //   202: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   205: aload_1
      //   206: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   209: invokevirtual toString : ()Ljava/lang/String;
      //   212: invokespecial <init> : (Ljava/lang/String;)V
      //   215: athrow
    }
    
    private boolean shouldDodge(View param1View, int param1Int) {
      int i = GravityCompat.getAbsoluteGravity(((LayoutParams)param1View.getLayoutParams()).insetEdge, param1Int);
      return (i != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, param1Int) & i) == i);
    }
    
    private boolean verifyAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_3
      //   2: aload_0
      //   3: getfield mAnchorView : Landroid/view/View;
      //   6: invokevirtual getId : ()I
      //   9: aload_0
      //   10: getfield mAnchorId : I
      //   13: if_icmpeq -> 18
      //   16: iload_3
      //   17: ireturn
      //   18: aload_0
      //   19: getfield mAnchorView : Landroid/view/View;
      //   22: astore #4
      //   24: aload_0
      //   25: getfield mAnchorView : Landroid/view/View;
      //   28: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   31: astore #5
      //   33: aload #5
      //   35: aload_2
      //   36: if_acmpeq -> 90
      //   39: aload #5
      //   41: ifnull -> 50
      //   44: aload #5
      //   46: aload_1
      //   47: if_acmpne -> 63
      //   50: aload_0
      //   51: aconst_null
      //   52: putfield mAnchorDirectChild : Landroid/view/View;
      //   55: aload_0
      //   56: aconst_null
      //   57: putfield mAnchorView : Landroid/view/View;
      //   60: goto -> 16
      //   63: aload #5
      //   65: instanceof android/view/View
      //   68: ifeq -> 78
      //   71: aload #5
      //   73: checkcast android/view/View
      //   76: astore #4
      //   78: aload #5
      //   80: invokeinterface getParent : ()Landroid/view/ViewParent;
      //   85: astore #5
      //   87: goto -> 33
      //   90: aload_0
      //   91: aload #4
      //   93: putfield mAnchorDirectChild : Landroid/view/View;
      //   96: iconst_1
      //   97: istore_3
      //   98: goto -> 16
    }
    
    void acceptNestedScroll(boolean param1Boolean) {
      this.mDidAcceptNestedScroll = param1Boolean;
    }
    
    boolean checkAnchorChanged() {
      return (this.mAnchorView == null && this.mAnchorId != -1);
    }
    
    boolean dependsOn(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      return (param1View2 == this.mAnchorDirectChild || shouldDodge(param1View2, ViewCompat.getLayoutDirection((View)param1CoordinatorLayout)) || (this.mBehavior != null && this.mBehavior.layoutDependsOn(param1CoordinatorLayout, param1View1, param1View2)));
    }
    
    boolean didBlockInteraction() {
      if (this.mBehavior == null)
        this.mDidBlockInteraction = false; 
      return this.mDidBlockInteraction;
    }
    
    View findAnchorView(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      CoordinatorLayout coordinatorLayout = null;
      if (this.mAnchorId == -1) {
        this.mAnchorDirectChild = null;
        this.mAnchorView = null;
        return (View)coordinatorLayout;
      } 
      if (this.mAnchorView == null || !verifyAnchorView(param1View, param1CoordinatorLayout))
        resolveAnchorView(param1View, param1CoordinatorLayout); 
      return this.mAnchorView;
    }
    
    @IdRes
    public int getAnchorId() {
      return this.mAnchorId;
    }
    
    @Nullable
    public CoordinatorLayout.Behavior getBehavior() {
      return this.mBehavior;
    }
    
    boolean getChangedAfterNestedScroll() {
      return this.mDidChangeAfterNestedScroll;
    }
    
    Rect getLastChildRect() {
      return this.mLastChildRect;
    }
    
    void invalidateAnchor() {
      this.mAnchorDirectChild = null;
      this.mAnchorView = null;
    }
    
    boolean isBlockingInteractionBelow(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      boolean bool1;
      if (this.mDidBlockInteraction)
        return true; 
      boolean bool2 = this.mDidBlockInteraction;
      if (this.mBehavior != null) {
        bool1 = this.mBehavior.blocksInteractionBelow(param1CoordinatorLayout, param1View);
      } else {
        bool1 = false;
      } 
      bool1 |= bool2;
      this.mDidBlockInteraction = bool1;
      return bool1;
    }
    
    boolean isNestedScrollAccepted() {
      return this.mDidAcceptNestedScroll;
    }
    
    void resetChangedAfterNestedScroll() {
      this.mDidChangeAfterNestedScroll = false;
    }
    
    void resetNestedScroll() {
      this.mDidAcceptNestedScroll = false;
    }
    
    void resetTouchBehaviorTracking() {
      this.mDidBlockInteraction = false;
    }
    
    public void setAnchorId(@IdRes int param1Int) {
      invalidateAnchor();
      this.mAnchorId = param1Int;
    }
    
    public void setBehavior(@Nullable CoordinatorLayout.Behavior param1Behavior) {
      if (this.mBehavior != param1Behavior) {
        if (this.mBehavior != null)
          this.mBehavior.onDetachedFromLayoutParams(); 
        this.mBehavior = param1Behavior;
        this.mBehaviorTag = null;
        this.mBehaviorResolved = true;
        if (param1Behavior != null)
          param1Behavior.onAttachedToLayoutParams(this); 
      } 
    }
    
    void setChangedAfterNestedScroll(boolean param1Boolean) {
      this.mDidChangeAfterNestedScroll = param1Boolean;
    }
    
    void setLastChildRect(Rect param1Rect) {
      this.mLastChildRect.set(param1Rect);
    }
  }
  
  class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
    public boolean onPreDraw() {
      CoordinatorLayout.this.onChildViewsChanged(0);
      return true;
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public CoordinatorLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new CoordinatorLayout.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public CoordinatorLayout.SavedState[] newArray(int param2Int) {
            return new CoordinatorLayout.SavedState[param2Int];
          }
        });
    
    SparseArray<Parcelable> behaviorStates;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      int i = param1Parcel.readInt();
      int[] arrayOfInt = new int[i];
      param1Parcel.readIntArray(arrayOfInt);
      Parcelable[] arrayOfParcelable = param1Parcel.readParcelableArray(param1ClassLoader);
      this.behaviorStates = new SparseArray(i);
      for (byte b = 0; b < i; b++)
        this.behaviorStates.append(arrayOfInt[b], arrayOfParcelable[b]); 
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      byte b1;
      super.writeToParcel(param1Parcel, param1Int);
      if (this.behaviorStates != null) {
        b1 = this.behaviorStates.size();
      } else {
        b1 = 0;
      } 
      param1Parcel.writeInt(b1);
      int[] arrayOfInt = new int[b1];
      Parcelable[] arrayOfParcelable = new Parcelable[b1];
      for (byte b2 = 0; b2 < b1; b2++) {
        arrayOfInt[b2] = this.behaviorStates.keyAt(b2);
        arrayOfParcelable[b2] = (Parcelable)this.behaviorStates.valueAt(b2);
      } 
      param1Parcel.writeIntArray(arrayOfInt);
      param1Parcel.writeParcelableArray(arrayOfParcelable, param1Int);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public CoordinatorLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new CoordinatorLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public CoordinatorLayout.SavedState[] newArray(int param1Int) {
      return new CoordinatorLayout.SavedState[param1Int];
    }
  }
  
  static class ViewElevationComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      float f1 = ViewCompat.getZ(param1View1);
      float f2 = ViewCompat.getZ(param1View2);
      return (f1 > f2) ? -1 : ((f1 < f2) ? 1 : 0);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/CoordinatorLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */