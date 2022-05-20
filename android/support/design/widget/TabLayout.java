package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.DecorView;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@DecorView
public class TabLayout extends HorizontalScrollView {
  private static final int ANIMATION_DURATION = 300;
  
  static final int DEFAULT_GAP_TEXT_ICON = 8;
  
  private static final int DEFAULT_HEIGHT = 48;
  
  private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
  
  static final int FIXED_WRAP_GUTTER_MIN = 16;
  
  public static final int GRAVITY_CENTER = 1;
  
  public static final int GRAVITY_FILL = 0;
  
  private static final int INVALID_WIDTH = -1;
  
  public static final int MODE_FIXED = 1;
  
  public static final int MODE_SCROLLABLE = 0;
  
  static final int MOTION_NON_ADJACENT_OFFSET = 24;
  
  private static final int TAB_MIN_WIDTH_MARGIN = 56;
  
  private static final Pools.Pool<Tab> sTabPool = (Pools.Pool<Tab>)new Pools.SynchronizedPool(16);
  
  private AdapterChangeListener mAdapterChangeListener;
  
  private int mContentInsetStart;
  
  private OnTabSelectedListener mCurrentVpSelectedListener;
  
  int mMode;
  
  private TabLayoutOnPageChangeListener mPageChangeListener;
  
  private PagerAdapter mPagerAdapter;
  
  private DataSetObserver mPagerAdapterObserver;
  
  private final int mRequestedTabMaxWidth;
  
  private final int mRequestedTabMinWidth;
  
  private ValueAnimatorCompat mScrollAnimator;
  
  private final int mScrollableTabMinWidth;
  
  private OnTabSelectedListener mSelectedListener;
  
  private final ArrayList<OnTabSelectedListener> mSelectedListeners;
  
  private Tab mSelectedTab;
  
  private boolean mSetupViewPagerImplicitly;
  
  final int mTabBackgroundResId;
  
  int mTabGravity;
  
  int mTabMaxWidth;
  
  int mTabPaddingBottom;
  
  int mTabPaddingEnd;
  
  int mTabPaddingStart;
  
  int mTabPaddingTop;
  
  private final SlidingTabStrip mTabStrip;
  
  int mTabTextAppearance;
  
  ColorStateList mTabTextColors;
  
  float mTabTextMultiLineSize;
  
  float mTabTextSize;
  
  private final Pools.Pool<TabView> mTabViewPool;
  
  private final ArrayList<Tab> mTabs;
  
  ViewPager mViewPager;
  
  public TabLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Resources resources;
    this.mTabs = new ArrayList<Tab>();
    this.mTabMaxWidth = Integer.MAX_VALUE;
    this.mSelectedListeners = new ArrayList<OnTabSelectedListener>();
    this.mTabViewPool = (Pools.Pool<TabView>)new Pools.SimplePool(12);
    ThemeUtils.checkAppCompatTheme(paramContext);
    setHorizontalScrollBarEnabled(false);
    this.mTabStrip = new SlidingTabStrip(paramContext);
    super.addView((View)this.mTabStrip, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
    null = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TabLayout, paramInt, R.style.Widget_Design_TabLayout);
    this.mTabStrip.setSelectedIndicatorHeight(null.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0));
    this.mTabStrip.setSelectedIndicatorColor(null.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
    paramInt = null.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
    this.mTabPaddingBottom = paramInt;
    this.mTabPaddingEnd = paramInt;
    this.mTabPaddingTop = paramInt;
    this.mTabPaddingStart = paramInt;
    this.mTabPaddingStart = null.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
    this.mTabPaddingTop = null.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
    this.mTabPaddingEnd = null.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
    this.mTabPaddingBottom = null.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
    this.mTabTextAppearance = null.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
    TypedArray typedArray = paramContext.obtainStyledAttributes(this.mTabTextAppearance, R.styleable.TextAppearance);
    try {
      this.mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
      this.mTabTextColors = typedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
      typedArray.recycle();
      if (null.hasValue(R.styleable.TabLayout_tabTextColor))
        this.mTabTextColors = null.getColorStateList(R.styleable.TabLayout_tabTextColor); 
      if (null.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
        paramInt = null.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
        this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), paramInt);
      } 
      this.mRequestedTabMinWidth = null.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
      this.mRequestedTabMaxWidth = null.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
      this.mTabBackgroundResId = null.getResourceId(R.styleable.TabLayout_tabBackground, 0);
      this.mContentInsetStart = null.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
      this.mMode = null.getInt(R.styleable.TabLayout_tabMode, 1);
      this.mTabGravity = null.getInt(R.styleable.TabLayout_tabGravity, 0);
      null.recycle();
      resources = getResources();
      this.mTabTextMultiLineSize = resources.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
      this.mScrollableTabMinWidth = resources.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
      return;
    } finally {
      resources.recycle();
    } 
  }
  
  private void addTabFromItemView(@NonNull TabItem paramTabItem) {
    Tab tab = newTab();
    if (paramTabItem.mText != null)
      tab.setText(paramTabItem.mText); 
    if (paramTabItem.mIcon != null)
      tab.setIcon(paramTabItem.mIcon); 
    if (paramTabItem.mCustomLayout != 0)
      tab.setCustomView(paramTabItem.mCustomLayout); 
    if (!TextUtils.isEmpty(paramTabItem.getContentDescription()))
      tab.setContentDescription(paramTabItem.getContentDescription()); 
    addTab(tab);
  }
  
  private void addTabView(Tab paramTab) {
    TabView tabView = paramTab.mView;
    this.mTabStrip.addView((View)tabView, paramTab.getPosition(), (ViewGroup.LayoutParams)createLayoutParamsForTabs());
  }
  
  private void addViewInternal(View paramView) {
    if (paramView instanceof TabItem) {
      addTabFromItemView((TabItem)paramView);
      return;
    } 
    throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
  }
  
  private void animateToTab(int paramInt) {
    if (paramInt != -1) {
      if (getWindowToken() == null || !ViewCompat.isLaidOut((View)this) || this.mTabStrip.childrenNeedLayout()) {
        setScrollPosition(paramInt, 0.0F, true);
        return;
      } 
      int i = getScrollX();
      int j = calculateScrollXForTab(paramInt, 0.0F);
      if (i != j) {
        if (this.mScrollAnimator == null) {
          this.mScrollAnimator = ViewUtils.createAnimator();
          this.mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
          this.mScrollAnimator.setDuration(300L);
          this.mScrollAnimator.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
                  TabLayout.this.scrollTo(param1ValueAnimatorCompat.getAnimatedIntValue(), 0);
                }
              });
        } 
        this.mScrollAnimator.setIntValues(i, j);
        this.mScrollAnimator.start();
      } 
      this.mTabStrip.animateIndicatorToPosition(paramInt, 300);
    } 
  }
  
  private void applyModeAndGravity() {
    int i = 0;
    if (this.mMode == 0)
      i = Math.max(0, this.mContentInsetStart - this.mTabPaddingStart); 
    ViewCompat.setPaddingRelative((View)this.mTabStrip, i, 0, 0, 0);
    switch (this.mMode) {
      default:
        updateTabViews(true);
        return;
      case 1:
        this.mTabStrip.setGravity(1);
      case 0:
        break;
    } 
    this.mTabStrip.setGravity(8388611);
  }
  
  private int calculateScrollXForTab(int paramInt, float paramFloat) {
    int i = 0;
    byte b = 0;
    if (this.mMode == 0) {
      View view2;
      View view1 = this.mTabStrip.getChildAt(paramInt);
      if (paramInt + 1 < this.mTabStrip.getChildCount()) {
        view2 = this.mTabStrip.getChildAt(paramInt + 1);
      } else {
        view2 = null;
      } 
      if (view1 != null) {
        paramInt = view1.getWidth();
      } else {
        paramInt = 0;
      } 
      i = b;
      if (view2 != null)
        i = view2.getWidth(); 
      i = view1.getLeft() + (int)((paramInt + i) * paramFloat * 0.5F) + view1.getWidth() / 2 - getWidth() / 2;
    } 
    return i;
  }
  
  private void configureTab(Tab paramTab, int paramInt) {
    paramTab.setPosition(paramInt);
    this.mTabs.add(paramInt, paramTab);
    int i = this.mTabs.size();
    while (++paramInt < i) {
      ((Tab)this.mTabs.get(paramInt)).setPosition(paramInt);
      paramInt++;
    } 
  }
  
  private static ColorStateList createColorStateList(int paramInt1, int paramInt2) {
    int[][] arrayOfInt = new int[2][];
    int[] arrayOfInt1 = new int[2];
    arrayOfInt[0] = SELECTED_STATE_SET;
    arrayOfInt1[0] = paramInt2;
    paramInt2 = 0 + 1;
    arrayOfInt[paramInt2] = EMPTY_STATE_SET;
    arrayOfInt1[paramInt2] = paramInt1;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private LinearLayout.LayoutParams createLayoutParamsForTabs() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
    updateTabViewLayoutParams(layoutParams);
    return layoutParams;
  }
  
  private TabView createTabView(@NonNull Tab paramTab) {
    TabView tabView1;
    if (this.mTabViewPool != null) {
      tabView1 = (TabView)this.mTabViewPool.acquire();
    } else {
      tabView1 = null;
    } 
    TabView tabView2 = tabView1;
    if (tabView1 == null)
      tabView2 = new TabView(getContext()); 
    tabView2.setTab(paramTab);
    tabView2.setFocusable(true);
    tabView2.setMinimumWidth(getTabMinWidth());
    return tabView2;
  }
  
  private void dispatchTabReselected(@NonNull Tab paramTab) {
    for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--)
      ((OnTabSelectedListener)this.mSelectedListeners.get(i)).onTabReselected(paramTab); 
  }
  
  private void dispatchTabSelected(@NonNull Tab paramTab) {
    for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--)
      ((OnTabSelectedListener)this.mSelectedListeners.get(i)).onTabSelected(paramTab); 
  }
  
  private void dispatchTabUnselected(@NonNull Tab paramTab) {
    for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--)
      ((OnTabSelectedListener)this.mSelectedListeners.get(i)).onTabUnselected(paramTab); 
  }
  
  private int getDefaultHeight() {
    byte b1 = 0;
    byte b2 = 0;
    int i = this.mTabs.size();
    while (true) {
      null = b1;
      if (b2 < i) {
        Tab tab = this.mTabs.get(b2);
        if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
          null = 1;
        } else {
          b2++;
          continue;
        } 
      } 
      return null ? 72 : 48;
    } 
  }
  
  private float getScrollPosition() {
    return this.mTabStrip.getIndicatorPosition();
  }
  
  private int getTabMinWidth() {
    return (this.mRequestedTabMinWidth != -1) ? this.mRequestedTabMinWidth : ((this.mMode == 0) ? this.mScrollableTabMinWidth : 0);
  }
  
  private int getTabScrollRange() {
    return Math.max(0, this.mTabStrip.getWidth() - getWidth() - getPaddingLeft() - getPaddingRight());
  }
  
  private void removeTabViewAt(int paramInt) {
    TabView tabView = (TabView)this.mTabStrip.getChildAt(paramInt);
    this.mTabStrip.removeViewAt(paramInt);
    if (tabView != null) {
      tabView.reset();
      this.mTabViewPool.release(tabView);
    } 
    requestLayout();
  }
  
  private void setSelectedTabView(int paramInt) {
    int i = this.mTabStrip.getChildCount();
    if (paramInt < i)
      for (byte b = 0; b < i; b++) {
        boolean bool;
        View view = this.mTabStrip.getChildAt(b);
        if (b == paramInt) {
          bool = true;
        } else {
          bool = false;
        } 
        view.setSelected(bool);
      }  
  }
  
  private void setupWithViewPager(@Nullable ViewPager paramViewPager, boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mViewPager != null) {
      if (this.mPageChangeListener != null)
        this.mViewPager.removeOnPageChangeListener(this.mPageChangeListener); 
      if (this.mAdapterChangeListener != null)
        this.mViewPager.removeOnAdapterChangeListener(this.mAdapterChangeListener); 
    } 
    if (this.mCurrentVpSelectedListener != null) {
      removeOnTabSelectedListener(this.mCurrentVpSelectedListener);
      this.mCurrentVpSelectedListener = null;
    } 
    if (paramViewPager != null) {
      this.mViewPager = paramViewPager;
      if (this.mPageChangeListener == null)
        this.mPageChangeListener = new TabLayoutOnPageChangeListener(this); 
      this.mPageChangeListener.reset();
      paramViewPager.addOnPageChangeListener(this.mPageChangeListener);
      this.mCurrentVpSelectedListener = new ViewPagerOnTabSelectedListener(paramViewPager);
      addOnTabSelectedListener(this.mCurrentVpSelectedListener);
      PagerAdapter pagerAdapter = paramViewPager.getAdapter();
      if (pagerAdapter != null)
        setPagerAdapter(pagerAdapter, paramBoolean1); 
      if (this.mAdapterChangeListener == null)
        this.mAdapterChangeListener = new AdapterChangeListener(); 
      this.mAdapterChangeListener.setAutoRefresh(paramBoolean1);
      paramViewPager.addOnAdapterChangeListener(this.mAdapterChangeListener);
      setScrollPosition(paramViewPager.getCurrentItem(), 0.0F, true);
    } else {
      this.mViewPager = null;
      setPagerAdapter((PagerAdapter)null, false);
    } 
    this.mSetupViewPagerImplicitly = paramBoolean2;
  }
  
  private void updateAllTabs() {
    byte b = 0;
    int i = this.mTabs.size();
    while (b < i) {
      ((Tab)this.mTabs.get(b)).updateView();
      b++;
    } 
  }
  
  private void updateTabViewLayoutParams(LinearLayout.LayoutParams paramLayoutParams) {
    if (this.mMode == 1 && this.mTabGravity == 0) {
      paramLayoutParams.width = 0;
      paramLayoutParams.weight = 1.0F;
      return;
    } 
    paramLayoutParams.width = -2;
    paramLayoutParams.weight = 0.0F;
  }
  
  public void addOnTabSelectedListener(@NonNull OnTabSelectedListener paramOnTabSelectedListener) {
    if (!this.mSelectedListeners.contains(paramOnTabSelectedListener))
      this.mSelectedListeners.add(paramOnTabSelectedListener); 
  }
  
  public void addTab(@NonNull Tab paramTab) {
    addTab(paramTab, this.mTabs.isEmpty());
  }
  
  public void addTab(@NonNull Tab paramTab, int paramInt) {
    addTab(paramTab, paramInt, this.mTabs.isEmpty());
  }
  
  public void addTab(@NonNull Tab paramTab, int paramInt, boolean paramBoolean) {
    if (paramTab.mParent != this)
      throw new IllegalArgumentException("Tab belongs to a different TabLayout."); 
    configureTab(paramTab, paramInt);
    addTabView(paramTab);
    if (paramBoolean)
      paramTab.select(); 
  }
  
  public void addTab(@NonNull Tab paramTab, boolean paramBoolean) {
    addTab(paramTab, this.mTabs.size(), paramBoolean);
  }
  
  public void addView(View paramView) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void clearOnTabSelectedListeners() {
    this.mSelectedListeners.clear();
  }
  
  int dpToPx(int paramInt) {
    return Math.round((getResources().getDisplayMetrics()).density * paramInt);
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return generateDefaultLayoutParams();
  }
  
  public int getSelectedTabPosition() {
    return (this.mSelectedTab != null) ? this.mSelectedTab.getPosition() : -1;
  }
  
  @Nullable
  public Tab getTabAt(int paramInt) {
    return (paramInt < 0 || paramInt >= getTabCount()) ? null : this.mTabs.get(paramInt);
  }
  
  public int getTabCount() {
    return this.mTabs.size();
  }
  
  public int getTabGravity() {
    return this.mTabGravity;
  }
  
  int getTabMaxWidth() {
    return this.mTabMaxWidth;
  }
  
  public int getTabMode() {
    return this.mMode;
  }
  
  @Nullable
  public ColorStateList getTabTextColors() {
    return this.mTabTextColors;
  }
  
  @NonNull
  public Tab newTab() {
    Tab tab1 = (Tab)sTabPool.acquire();
    Tab tab2 = tab1;
    if (tab1 == null)
      tab2 = new Tab(); 
    tab2.mParent = this;
    tab2.mView = createTabView(tab2);
    return tab2;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.mViewPager == null) {
      ViewParent viewParent = getParent();
      if (viewParent instanceof ViewPager)
        setupWithViewPager((ViewPager)viewParent, true, true); 
    } 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mSetupViewPagerImplicitly) {
      setupWithViewPager((ViewPager)null);
      this.mSetupViewPagerImplicitly = false;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    View view;
    int i = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
    switch (View.MeasureSpec.getMode(paramInt2)) {
      default:
        i = View.MeasureSpec.getSize(paramInt1);
        if (View.MeasureSpec.getMode(paramInt1) != 0) {
          if (this.mRequestedTabMaxWidth > 0) {
            i = this.mRequestedTabMaxWidth;
          } else {
            i -= dpToPx(56);
          } 
          this.mTabMaxWidth = i;
        } 
        super.onMeasure(paramInt1, paramInt2);
        if (getChildCount() == 1) {
          view = getChildAt(0);
          paramInt1 = 0;
          switch (this.mMode) {
            default:
              if (paramInt1 != 0) {
                paramInt1 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom(), (view.getLayoutParams()).height);
                view.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), paramInt1);
              } 
              return;
            case 0:
              if (view.getMeasuredWidth() < getMeasuredWidth()) {
                paramInt1 = 1;
              } else {
                paramInt1 = 0;
              } 
            case 1:
              break;
          } 
          break;
        } 
        return;
      case -2147483648:
        paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.min(i, View.MeasureSpec.getSize(paramInt2)), 1073741824);
      case 0:
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    } 
    if (view.getMeasuredWidth() != getMeasuredWidth())
      paramInt1 = 1; 
    paramInt1 = 0;
  }
  
  void populateFromPagerAdapter() {
    removeAllTabs();
    if (this.mPagerAdapter != null) {
      int i = this.mPagerAdapter.getCount();
      int j;
      for (j = 0; j < i; j++)
        addTab(newTab().setText(this.mPagerAdapter.getPageTitle(j)), false); 
      if (this.mViewPager != null && i > 0) {
        j = this.mViewPager.getCurrentItem();
        if (j != getSelectedTabPosition() && j < getTabCount())
          selectTab(getTabAt(j)); 
      } 
    } 
  }
  
  public void removeAllTabs() {
    for (int i = this.mTabStrip.getChildCount() - 1; i >= 0; i--)
      removeTabViewAt(i); 
    Iterator<Tab> iterator = this.mTabs.iterator();
    while (iterator.hasNext()) {
      Tab tab = iterator.next();
      iterator.remove();
      tab.reset();
      sTabPool.release(tab);
    } 
    this.mSelectedTab = null;
  }
  
  public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener paramOnTabSelectedListener) {
    this.mSelectedListeners.remove(paramOnTabSelectedListener);
  }
  
  public void removeTab(Tab paramTab) {
    if (paramTab.mParent != this)
      throw new IllegalArgumentException("Tab does not belong to this TabLayout."); 
    removeTabAt(paramTab.getPosition());
  }
  
  public void removeTabAt(int paramInt) {
    int i;
    if (this.mSelectedTab != null) {
      i = this.mSelectedTab.getPosition();
    } else {
      i = 0;
    } 
    removeTabViewAt(paramInt);
    Tab tab = this.mTabs.remove(paramInt);
    if (tab != null) {
      tab.reset();
      sTabPool.release(tab);
    } 
    int j = this.mTabs.size();
    for (int k = paramInt; k < j; k++)
      ((Tab)this.mTabs.get(k)).setPosition(k); 
    if (i == paramInt) {
      if (this.mTabs.isEmpty()) {
        tab = null;
      } else {
        tab = this.mTabs.get(Math.max(0, paramInt - 1));
      } 
      selectTab(tab);
    } 
  }
  
  void selectTab(Tab paramTab) {
    selectTab(paramTab, true);
  }
  
  void selectTab(Tab paramTab, boolean paramBoolean) {
    byte b;
    Tab tab = this.mSelectedTab;
    if (tab == paramTab) {
      if (tab != null) {
        dispatchTabReselected(paramTab);
        animateToTab(paramTab.getPosition());
      } 
      return;
    } 
    if (paramTab != null) {
      b = paramTab.getPosition();
    } else {
      b = -1;
    } 
    if (paramBoolean) {
      if ((tab == null || tab.getPosition() == -1) && b != -1) {
        setScrollPosition(b, 0.0F, true);
      } else {
        animateToTab(b);
      } 
      if (b != -1)
        setSelectedTabView(b); 
    } 
    if (tab != null)
      dispatchTabUnselected(tab); 
    this.mSelectedTab = paramTab;
    if (paramTab != null)
      dispatchTabSelected(paramTab); 
  }
  
  @Deprecated
  public void setOnTabSelectedListener(@Nullable OnTabSelectedListener paramOnTabSelectedListener) {
    if (this.mSelectedListener != null)
      removeOnTabSelectedListener(this.mSelectedListener); 
    this.mSelectedListener = paramOnTabSelectedListener;
    if (paramOnTabSelectedListener != null)
      addOnTabSelectedListener(paramOnTabSelectedListener); 
  }
  
  void setPagerAdapter(@Nullable PagerAdapter paramPagerAdapter, boolean paramBoolean) {
    if (this.mPagerAdapter != null && this.mPagerAdapterObserver != null)
      this.mPagerAdapter.unregisterDataSetObserver(this.mPagerAdapterObserver); 
    this.mPagerAdapter = paramPagerAdapter;
    if (paramBoolean && paramPagerAdapter != null) {
      if (this.mPagerAdapterObserver == null)
        this.mPagerAdapterObserver = new PagerAdapterObserver(); 
      paramPagerAdapter.registerDataSetObserver(this.mPagerAdapterObserver);
    } 
    populateFromPagerAdapter();
  }
  
  public void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean) {
    setScrollPosition(paramInt, paramFloat, paramBoolean, true);
  }
  
  void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    int i = Math.round(paramInt + paramFloat);
    if (i >= 0 && i < this.mTabStrip.getChildCount()) {
      if (paramBoolean2)
        this.mTabStrip.setIndicatorPositionFromTabPosition(paramInt, paramFloat); 
      if (this.mScrollAnimator != null && this.mScrollAnimator.isRunning())
        this.mScrollAnimator.cancel(); 
      scrollTo(calculateScrollXForTab(paramInt, paramFloat), 0);
      if (paramBoolean1)
        setSelectedTabView(i); 
    } 
  }
  
  public void setSelectedTabIndicatorColor(@ColorInt int paramInt) {
    this.mTabStrip.setSelectedIndicatorColor(paramInt);
  }
  
  public void setSelectedTabIndicatorHeight(int paramInt) {
    this.mTabStrip.setSelectedIndicatorHeight(paramInt);
  }
  
  public void setTabGravity(int paramInt) {
    if (this.mTabGravity != paramInt) {
      this.mTabGravity = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabMode(int paramInt) {
    if (paramInt != this.mMode) {
      this.mMode = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabTextColors(int paramInt1, int paramInt2) {
    setTabTextColors(createColorStateList(paramInt1, paramInt2));
  }
  
  public void setTabTextColors(@Nullable ColorStateList paramColorStateList) {
    if (this.mTabTextColors != paramColorStateList) {
      this.mTabTextColors = paramColorStateList;
      updateAllTabs();
    } 
  }
  
  @Deprecated
  public void setTabsFromPagerAdapter(@Nullable PagerAdapter paramPagerAdapter) {
    setPagerAdapter(paramPagerAdapter, false);
  }
  
  public void setupWithViewPager(@Nullable ViewPager paramViewPager) {
    setupWithViewPager(paramViewPager, true);
  }
  
  public void setupWithViewPager(@Nullable ViewPager paramViewPager, boolean paramBoolean) {
    setupWithViewPager(paramViewPager, paramBoolean, false);
  }
  
  public boolean shouldDelayChildPressedState() {
    return (getTabScrollRange() > 0);
  }
  
  void updateTabViews(boolean paramBoolean) {
    for (byte b = 0; b < this.mTabStrip.getChildCount(); b++) {
      View view = this.mTabStrip.getChildAt(b);
      view.setMinimumWidth(getTabMinWidth());
      updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
      if (paramBoolean)
        view.requestLayout(); 
    } 
  }
  
  private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
    private boolean mAutoRefresh;
    
    public void onAdapterChanged(@NonNull ViewPager param1ViewPager, @Nullable PagerAdapter param1PagerAdapter1, @Nullable PagerAdapter param1PagerAdapter2) {
      if (TabLayout.this.mViewPager == param1ViewPager)
        TabLayout.this.setPagerAdapter(param1PagerAdapter2, this.mAutoRefresh); 
    }
    
    void setAutoRefresh(boolean param1Boolean) {
      this.mAutoRefresh = param1Boolean;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface Mode {}
  
  public static interface OnTabSelectedListener {
    void onTabReselected(TabLayout.Tab param1Tab);
    
    void onTabSelected(TabLayout.Tab param1Tab);
    
    void onTabUnselected(TabLayout.Tab param1Tab);
  }
  
  private class PagerAdapterObserver extends DataSetObserver {
    public void onChanged() {
      TabLayout.this.populateFromPagerAdapter();
    }
    
    public void onInvalidated() {
      TabLayout.this.populateFromPagerAdapter();
    }
  }
  
  private class SlidingTabStrip extends LinearLayout {
    private ValueAnimatorCompat mIndicatorAnimator;
    
    private int mIndicatorLeft = -1;
    
    private int mIndicatorRight = -1;
    
    private int mSelectedIndicatorHeight;
    
    private final Paint mSelectedIndicatorPaint;
    
    int mSelectedPosition = -1;
    
    float mSelectionOffset;
    
    SlidingTabStrip(Context param1Context) {
      super(param1Context);
      setWillNotDraw(false);
      this.mSelectedIndicatorPaint = new Paint();
    }
    
    private void updateIndicatorPosition() {
      byte b1;
      byte b2;
      View view = getChildAt(this.mSelectedPosition);
      if (view != null && view.getWidth() > 0) {
        int i = view.getLeft();
        int j = view.getRight();
        b1 = i;
        b2 = j;
        if (this.mSelectionOffset > 0.0F) {
          b1 = i;
          b2 = j;
          if (this.mSelectedPosition < getChildCount() - 1) {
            view = getChildAt(this.mSelectedPosition + 1);
            b1 = (int)(this.mSelectionOffset * view.getLeft() + (1.0F - this.mSelectionOffset) * i);
            b2 = (int)(this.mSelectionOffset * view.getRight() + (1.0F - this.mSelectionOffset) * j);
          } 
        } 
      } else {
        b2 = -1;
        b1 = -1;
      } 
      setIndicatorPosition(b1, b2);
    }
    
    void animateIndicatorToPosition(final int position, int param1Int2) {
      final int startLeft;
      final int startRight;
      if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning())
        this.mIndicatorAnimator.cancel(); 
      if (ViewCompat.getLayoutDirection((View)this) == 1) {
        i = 1;
      } else {
        i = 0;
      } 
      View view = getChildAt(position);
      if (view == null) {
        updateIndicatorPosition();
        return;
      } 
      final int targetLeft = view.getLeft();
      final int targetRight = view.getRight();
      if (Math.abs(position - this.mSelectedPosition) <= 1) {
        i = this.mIndicatorLeft;
        m = this.mIndicatorRight;
      } else {
        m = TabLayout.this.dpToPx(24);
        if (position < this.mSelectedPosition) {
          if (i != 0) {
            m = j - m;
            i = m;
          } else {
            m = k + m;
            i = m;
          } 
        } else if (i != 0) {
          m = k + m;
          i = m;
        } else {
          m = j - m;
          i = m;
        } 
      } 
      if (i != j || m != k) {
        ValueAnimatorCompat valueAnimatorCompat = ViewUtils.createAnimator();
        this.mIndicatorAnimator = valueAnimatorCompat;
        valueAnimatorCompat.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimatorCompat.setDuration(param1Int2);
        valueAnimatorCompat.setFloatValues(0.0F, 1.0F);
        valueAnimatorCompat.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
              public void onAnimationUpdate(ValueAnimatorCompat param2ValueAnimatorCompat) {
                float f = param2ValueAnimatorCompat.getAnimatedFraction();
                TabLayout.SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(startLeft, targetLeft, f), AnimationUtils.lerp(startRight, targetRight, f));
              }
            });
        valueAnimatorCompat.addListener(new ValueAnimatorCompat.AnimatorListenerAdapter() {
              public void onAnimationEnd(ValueAnimatorCompat param2ValueAnimatorCompat) {
                TabLayout.SlidingTabStrip.this.mSelectedPosition = position;
                TabLayout.SlidingTabStrip.this.mSelectionOffset = 0.0F;
              }
            });
        valueAnimatorCompat.start();
      } 
    }
    
    boolean childrenNeedLayout() {
      // Byte code:
      //   0: iconst_0
      //   1: istore_1
      //   2: aload_0
      //   3: invokevirtual getChildCount : ()I
      //   6: istore_2
      //   7: iload_1
      //   8: iload_2
      //   9: if_icmpge -> 33
      //   12: aload_0
      //   13: iload_1
      //   14: invokevirtual getChildAt : (I)Landroid/view/View;
      //   17: invokevirtual getWidth : ()I
      //   20: ifgt -> 27
      //   23: iconst_1
      //   24: istore_3
      //   25: iload_3
      //   26: ireturn
      //   27: iinc #1, 1
      //   30: goto -> 7
      //   33: iconst_0
      //   34: istore_3
      //   35: goto -> 25
    }
    
    public void draw(Canvas param1Canvas) {
      super.draw(param1Canvas);
      if (this.mIndicatorLeft >= 0 && this.mIndicatorRight > this.mIndicatorLeft)
        param1Canvas.drawRect(this.mIndicatorLeft, (getHeight() - this.mSelectedIndicatorHeight), this.mIndicatorRight, getHeight(), this.mSelectedIndicatorPaint); 
    }
    
    float getIndicatorPosition() {
      return this.mSelectedPosition + this.mSelectionOffset;
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.onLayout(param1Boolean, param1Int1, param1Int2, param1Int3, param1Int4);
      if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning()) {
        this.mIndicatorAnimator.cancel();
        long l = this.mIndicatorAnimator.getDuration();
        animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0F - this.mIndicatorAnimator.getAnimatedFraction()) * (float)l));
        return;
      } 
      updateIndicatorPosition();
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      if (View.MeasureSpec.getMode(param1Int1) == 1073741824 && TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
        int i = getChildCount();
        int j = 0;
        int k = 0;
        while (k < i) {
          View view = getChildAt(k);
          int m = j;
          if (view.getVisibility() == 0)
            m = Math.max(j, view.getMeasuredWidth()); 
          k++;
          j = m;
        } 
        if (j > 0) {
          boolean bool2;
          k = TabLayout.this.dpToPx(16);
          boolean bool1 = false;
          if (j * i <= getMeasuredWidth() - k * 2) {
            k = 0;
            while (true) {
              bool2 = bool1;
              if (k < i) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getChildAt(k).getLayoutParams();
                if (layoutParams.width != j || layoutParams.weight != 0.0F) {
                  layoutParams.width = j;
                  layoutParams.weight = 0.0F;
                  bool1 = true;
                } 
                k++;
                continue;
              } 
              break;
            } 
          } else {
            TabLayout.this.mTabGravity = 0;
            TabLayout.this.updateTabViews(false);
            bool2 = true;
          } 
          if (bool2)
            super.onMeasure(param1Int1, param1Int2); 
        } 
      } 
    }
    
    void setIndicatorPosition(int param1Int1, int param1Int2) {
      if (param1Int1 != this.mIndicatorLeft || param1Int2 != this.mIndicatorRight) {
        this.mIndicatorLeft = param1Int1;
        this.mIndicatorRight = param1Int2;
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
    
    void setIndicatorPositionFromTabPosition(int param1Int, float param1Float) {
      if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning())
        this.mIndicatorAnimator.cancel(); 
      this.mSelectedPosition = param1Int;
      this.mSelectionOffset = param1Float;
      updateIndicatorPosition();
    }
    
    void setSelectedIndicatorColor(int param1Int) {
      if (this.mSelectedIndicatorPaint.getColor() != param1Int) {
        this.mSelectedIndicatorPaint.setColor(param1Int);
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
    
    void setSelectedIndicatorHeight(int param1Int) {
      if (this.mSelectedIndicatorHeight != param1Int) {
        this.mSelectedIndicatorHeight = param1Int;
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
  }
  
  class null implements ValueAnimatorCompat.AnimatorUpdateListener {
    public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
      float f = param1ValueAnimatorCompat.getAnimatedFraction();
      this.this$1.setIndicatorPosition(AnimationUtils.lerp(startLeft, targetLeft, f), AnimationUtils.lerp(startRight, targetRight, f));
    }
  }
  
  class null extends ValueAnimatorCompat.AnimatorListenerAdapter {
    public void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat) {
      this.this$1.mSelectedPosition = position;
      this.this$1.mSelectionOffset = 0.0F;
    }
  }
  
  public static final class Tab {
    public static final int INVALID_POSITION = -1;
    
    private CharSequence mContentDesc;
    
    private View mCustomView;
    
    private Drawable mIcon;
    
    TabLayout mParent;
    
    private int mPosition = -1;
    
    private Object mTag;
    
    private CharSequence mText;
    
    TabLayout.TabView mView;
    
    @Nullable
    public CharSequence getContentDescription() {
      return this.mContentDesc;
    }
    
    @Nullable
    public View getCustomView() {
      return this.mCustomView;
    }
    
    @Nullable
    public Drawable getIcon() {
      return this.mIcon;
    }
    
    public int getPosition() {
      return this.mPosition;
    }
    
    @Nullable
    public Object getTag() {
      return this.mTag;
    }
    
    @Nullable
    public CharSequence getText() {
      return this.mText;
    }
    
    public boolean isSelected() {
      if (this.mParent == null)
        throw new IllegalArgumentException("Tab not attached to a TabLayout"); 
      return (this.mParent.getSelectedTabPosition() == this.mPosition);
    }
    
    void reset() {
      this.mParent = null;
      this.mView = null;
      this.mTag = null;
      this.mIcon = null;
      this.mText = null;
      this.mContentDesc = null;
      this.mPosition = -1;
      this.mCustomView = null;
    }
    
    public void select() {
      if (this.mParent == null)
        throw new IllegalArgumentException("Tab not attached to a TabLayout"); 
      this.mParent.selectTab(this);
    }
    
    @NonNull
    public Tab setContentDescription(@StringRes int param1Int) {
      if (this.mParent == null)
        throw new IllegalArgumentException("Tab not attached to a TabLayout"); 
      return setContentDescription(this.mParent.getResources().getText(param1Int));
    }
    
    @NonNull
    public Tab setContentDescription(@Nullable CharSequence param1CharSequence) {
      this.mContentDesc = param1CharSequence;
      updateView();
      return this;
    }
    
    @NonNull
    public Tab setCustomView(@LayoutRes int param1Int) {
      return setCustomView(LayoutInflater.from(this.mView.getContext()).inflate(param1Int, (ViewGroup)this.mView, false));
    }
    
    @NonNull
    public Tab setCustomView(@Nullable View param1View) {
      this.mCustomView = param1View;
      updateView();
      return this;
    }
    
    @NonNull
    public Tab setIcon(@DrawableRes int param1Int) {
      if (this.mParent == null)
        throw new IllegalArgumentException("Tab not attached to a TabLayout"); 
      return setIcon(AppCompatResources.getDrawable(this.mParent.getContext(), param1Int));
    }
    
    @NonNull
    public Tab setIcon(@Nullable Drawable param1Drawable) {
      this.mIcon = param1Drawable;
      updateView();
      return this;
    }
    
    void setPosition(int param1Int) {
      this.mPosition = param1Int;
    }
    
    @NonNull
    public Tab setTag(@Nullable Object param1Object) {
      this.mTag = param1Object;
      return this;
    }
    
    @NonNull
    public Tab setText(@StringRes int param1Int) {
      if (this.mParent == null)
        throw new IllegalArgumentException("Tab not attached to a TabLayout"); 
      return setText(this.mParent.getResources().getText(param1Int));
    }
    
    @NonNull
    public Tab setText(@Nullable CharSequence param1CharSequence) {
      this.mText = param1CharSequence;
      updateView();
      return this;
    }
    
    void updateView() {
      if (this.mView != null)
        this.mView.update(); 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface TabGravity {}
  
  public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private int mPreviousScrollState;
    
    private int mScrollState;
    
    private final WeakReference<TabLayout> mTabLayoutRef;
    
    public TabLayoutOnPageChangeListener(TabLayout param1TabLayout) {
      this.mTabLayoutRef = new WeakReference<TabLayout>(param1TabLayout);
    }
    
    public void onPageScrollStateChanged(int param1Int) {
      this.mPreviousScrollState = this.mScrollState;
      this.mScrollState = param1Int;
    }
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
      TabLayout tabLayout = this.mTabLayoutRef.get();
      if (tabLayout != null) {
        boolean bool1;
        boolean bool2;
        if (this.mScrollState != 2 || this.mPreviousScrollState == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (this.mScrollState != 2 || this.mPreviousScrollState != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        tabLayout.setScrollPosition(param1Int1, param1Float, bool1, bool2);
      } 
    }
    
    public void onPageSelected(int param1Int) {
      TabLayout tabLayout = this.mTabLayoutRef.get();
      if (tabLayout != null && tabLayout.getSelectedTabPosition() != param1Int && param1Int < tabLayout.getTabCount()) {
        boolean bool;
        if (this.mScrollState == 0 || (this.mScrollState == 2 && this.mPreviousScrollState == 0)) {
          bool = true;
        } else {
          bool = false;
        } 
        tabLayout.selectTab(tabLayout.getTabAt(param1Int), bool);
      } 
    }
    
    void reset() {
      this.mScrollState = 0;
      this.mPreviousScrollState = 0;
    }
  }
  
  class TabView extends LinearLayout implements View.OnLongClickListener {
    private ImageView mCustomIconView;
    
    private TextView mCustomTextView;
    
    private View mCustomView;
    
    private int mDefaultMaxLines = 2;
    
    private ImageView mIconView;
    
    private TabLayout.Tab mTab;
    
    private TextView mTextView;
    
    public TabView(Context param1Context) {
      super(param1Context);
      if (TabLayout.this.mTabBackgroundResId != 0)
        ViewCompat.setBackground((View)this, AppCompatResources.getDrawable(param1Context, TabLayout.this.mTabBackgroundResId)); 
      ViewCompat.setPaddingRelative((View)this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
      setGravity(17);
      setOrientation(1);
      setClickable(true);
      ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(getContext(), 1002));
    }
    
    private float approximateLineWidth(Layout param1Layout, int param1Int, float param1Float) {
      return param1Layout.getLineWidth(param1Int) * param1Float / param1Layout.getPaint().getTextSize();
    }
    
    private void updateTextAndIcon(@Nullable TextView param1TextView, @Nullable ImageView param1ImageView) {
      Drawable drawable;
      CharSequence charSequence1;
      CharSequence charSequence2;
      boolean bool;
      if (this.mTab != null) {
        drawable = this.mTab.getIcon();
      } else {
        drawable = null;
      } 
      if (this.mTab != null) {
        charSequence1 = this.mTab.getText();
      } else {
        charSequence1 = null;
      } 
      if (this.mTab != null) {
        charSequence2 = this.mTab.getContentDescription();
      } else {
        charSequence2 = null;
      } 
      if (param1ImageView != null) {
        if (drawable != null) {
          param1ImageView.setImageDrawable(drawable);
          param1ImageView.setVisibility(0);
          setVisibility(0);
        } else {
          param1ImageView.setVisibility(8);
          param1ImageView.setImageDrawable(null);
        } 
        param1ImageView.setContentDescription(charSequence2);
      } 
      if (!TextUtils.isEmpty(charSequence1)) {
        bool = true;
      } else {
        bool = false;
      } 
      if (param1TextView != null) {
        if (bool) {
          param1TextView.setText(charSequence1);
          param1TextView.setVisibility(0);
          setVisibility(0);
        } else {
          param1TextView.setVisibility(8);
          param1TextView.setText(null);
        } 
        param1TextView.setContentDescription(charSequence2);
      } 
      if (param1ImageView != null) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)param1ImageView.getLayoutParams();
        byte b = 0;
        int i = b;
        if (bool) {
          i = b;
          if (param1ImageView.getVisibility() == 0)
            i = TabLayout.this.dpToPx(8); 
        } 
        if (i != marginLayoutParams.bottomMargin) {
          marginLayoutParams.bottomMargin = i;
          param1ImageView.requestLayout();
        } 
      } 
      if (!bool && !TextUtils.isEmpty(charSequence2)) {
        setOnLongClickListener(this);
        return;
      } 
      setOnLongClickListener(null);
      setLongClickable(false);
    }
    
    public TabLayout.Tab getTab() {
      return this.mTab;
    }
    
    @TargetApi(14)
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ActionBar.Tab.class.getName());
    }
    
    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      super.onInitializeAccessibilityNodeInfo(param1AccessibilityNodeInfo);
      param1AccessibilityNodeInfo.setClassName(ActionBar.Tab.class.getName());
    }
    
    public boolean onLongClick(View param1View) {
      int[] arrayOfInt = new int[2];
      Rect rect = new Rect();
      getLocationOnScreen(arrayOfInt);
      getWindowVisibleDisplayFrame(rect);
      Context context = getContext();
      int i = getWidth();
      int j = getHeight();
      int k = arrayOfInt[1];
      int m = j / 2;
      int n = arrayOfInt[0] + i / 2;
      i = n;
      if (ViewCompat.getLayoutDirection(param1View) == 0)
        i = (context.getResources().getDisplayMetrics()).widthPixels - n; 
      Toast toast = Toast.makeText(context, this.mTab.getContentDescription(), 0);
      if (k + m < rect.height()) {
        toast.setGravity(8388661, i, arrayOfInt[1] + j - rect.top);
        toast.show();
        return true;
      } 
      toast.setGravity(81, 0, j);
      toast.show();
      return true;
    }
    
    public void onMeasure(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_1
      //   1: invokestatic getSize : (I)I
      //   4: istore_3
      //   5: iload_1
      //   6: invokestatic getMode : (I)I
      //   9: istore #4
      //   11: aload_0
      //   12: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   15: invokevirtual getTabMaxWidth : ()I
      //   18: istore #5
      //   20: iload #5
      //   22: ifle -> 271
      //   25: iload #4
      //   27: ifeq -> 36
      //   30: iload_3
      //   31: iload #5
      //   33: if_icmple -> 271
      //   36: aload_0
      //   37: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   40: getfield mTabMaxWidth : I
      //   43: ldc_w -2147483648
      //   46: invokestatic makeMeasureSpec : (II)I
      //   49: istore_1
      //   50: aload_0
      //   51: iload_1
      //   52: iload_2
      //   53: invokespecial onMeasure : (II)V
      //   56: aload_0
      //   57: getfield mTextView : Landroid/widget/TextView;
      //   60: ifnull -> 270
      //   63: aload_0
      //   64: invokevirtual getResources : ()Landroid/content/res/Resources;
      //   67: pop
      //   68: aload_0
      //   69: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   72: getfield mTabTextSize : F
      //   75: fstore #6
      //   77: aload_0
      //   78: getfield mDefaultMaxLines : I
      //   81: istore #5
      //   83: aload_0
      //   84: getfield mIconView : Landroid/widget/ImageView;
      //   87: ifnull -> 274
      //   90: aload_0
      //   91: getfield mIconView : Landroid/widget/ImageView;
      //   94: invokevirtual getVisibility : ()I
      //   97: ifne -> 274
      //   100: iconst_1
      //   101: istore_3
      //   102: fload #6
      //   104: fstore #7
      //   106: aload_0
      //   107: getfield mTextView : Landroid/widget/TextView;
      //   110: invokevirtual getTextSize : ()F
      //   113: fstore #6
      //   115: aload_0
      //   116: getfield mTextView : Landroid/widget/TextView;
      //   119: invokevirtual getLineCount : ()I
      //   122: istore #8
      //   124: aload_0
      //   125: getfield mTextView : Landroid/widget/TextView;
      //   128: invokestatic getMaxLines : (Landroid/widget/TextView;)I
      //   131: istore #5
      //   133: fload #7
      //   135: fload #6
      //   137: fcmpl
      //   138: ifne -> 152
      //   141: iload #5
      //   143: iflt -> 270
      //   146: iload_3
      //   147: iload #5
      //   149: if_icmpeq -> 270
      //   152: iconst_1
      //   153: istore #4
      //   155: iload #4
      //   157: istore #5
      //   159: aload_0
      //   160: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   163: getfield mMode : I
      //   166: iconst_1
      //   167: if_icmpne -> 241
      //   170: iload #4
      //   172: istore #5
      //   174: fload #7
      //   176: fload #6
      //   178: fcmpl
      //   179: ifle -> 241
      //   182: iload #4
      //   184: istore #5
      //   186: iload #8
      //   188: iconst_1
      //   189: if_icmpne -> 241
      //   192: aload_0
      //   193: getfield mTextView : Landroid/widget/TextView;
      //   196: invokevirtual getLayout : ()Landroid/text/Layout;
      //   199: astore #9
      //   201: aload #9
      //   203: ifnull -> 238
      //   206: iload #4
      //   208: istore #5
      //   210: aload_0
      //   211: aload #9
      //   213: iconst_0
      //   214: fload #7
      //   216: invokespecial approximateLineWidth : (Landroid/text/Layout;IF)F
      //   219: aload_0
      //   220: invokevirtual getMeasuredWidth : ()I
      //   223: aload_0
      //   224: invokevirtual getPaddingLeft : ()I
      //   227: isub
      //   228: aload_0
      //   229: invokevirtual getPaddingRight : ()I
      //   232: isub
      //   233: i2f
      //   234: fcmpl
      //   235: ifle -> 241
      //   238: iconst_0
      //   239: istore #5
      //   241: iload #5
      //   243: ifeq -> 270
      //   246: aload_0
      //   247: getfield mTextView : Landroid/widget/TextView;
      //   250: iconst_0
      //   251: fload #7
      //   253: invokevirtual setTextSize : (IF)V
      //   256: aload_0
      //   257: getfield mTextView : Landroid/widget/TextView;
      //   260: iload_3
      //   261: invokevirtual setMaxLines : (I)V
      //   264: aload_0
      //   265: iload_1
      //   266: iload_2
      //   267: invokespecial onMeasure : (II)V
      //   270: return
      //   271: goto -> 50
      //   274: iload #5
      //   276: istore_3
      //   277: fload #6
      //   279: fstore #7
      //   281: aload_0
      //   282: getfield mTextView : Landroid/widget/TextView;
      //   285: ifnull -> 106
      //   288: iload #5
      //   290: istore_3
      //   291: fload #6
      //   293: fstore #7
      //   295: aload_0
      //   296: getfield mTextView : Landroid/widget/TextView;
      //   299: invokevirtual getLineCount : ()I
      //   302: iconst_1
      //   303: if_icmple -> 106
      //   306: aload_0
      //   307: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   310: getfield mTabTextMultiLineSize : F
      //   313: fstore #7
      //   315: iload #5
      //   317: istore_3
      //   318: goto -> 106
    }
    
    public boolean performClick() {
      boolean bool1 = super.performClick();
      boolean bool2 = bool1;
      if (this.mTab != null) {
        if (!bool1)
          playSoundEffect(0); 
        this.mTab.select();
        bool2 = true;
      } 
      return bool2;
    }
    
    void reset() {
      setTab((TabLayout.Tab)null);
      setSelected(false);
    }
    
    public void setSelected(boolean param1Boolean) {
      boolean bool;
      if (isSelected() != param1Boolean) {
        bool = true;
      } else {
        bool = false;
      } 
      super.setSelected(param1Boolean);
      if (bool && param1Boolean && Build.VERSION.SDK_INT < 16)
        sendAccessibilityEvent(4); 
      if (this.mTextView != null)
        this.mTextView.setSelected(param1Boolean); 
      if (this.mIconView != null)
        this.mIconView.setSelected(param1Boolean); 
      if (this.mCustomView != null)
        this.mCustomView.setSelected(param1Boolean); 
    }
    
    void setTab(@Nullable TabLayout.Tab param1Tab) {
      if (param1Tab != this.mTab) {
        this.mTab = param1Tab;
        update();
      } 
    }
    
    final void update() {
      View view;
      boolean bool;
      TabLayout.Tab tab = this.mTab;
      if (tab != null) {
        view = tab.getCustomView();
      } else {
        view = null;
      } 
      if (view != null) {
        ViewParent viewParent = view.getParent();
        if (viewParent != this) {
          if (viewParent != null)
            ((ViewGroup)viewParent).removeView(view); 
          addView(view);
        } 
        this.mCustomView = view;
        if (this.mTextView != null)
          this.mTextView.setVisibility(8); 
        if (this.mIconView != null) {
          this.mIconView.setVisibility(8);
          this.mIconView.setImageDrawable(null);
        } 
        this.mCustomTextView = (TextView)view.findViewById(16908308);
        if (this.mCustomTextView != null)
          this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mCustomTextView); 
        this.mCustomIconView = (ImageView)view.findViewById(16908294);
      } else {
        if (this.mCustomView != null) {
          removeView(this.mCustomView);
          this.mCustomView = null;
        } 
        this.mCustomTextView = null;
        this.mCustomIconView = null;
      } 
      if (this.mCustomView == null) {
        if (this.mIconView == null) {
          ImageView imageView = (ImageView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
          addView((View)imageView, 0);
          this.mIconView = imageView;
        } 
        if (this.mTextView == null) {
          TextView textView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
          addView((View)textView);
          this.mTextView = textView;
          this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mTextView);
        } 
        TextViewCompat.setTextAppearance(this.mTextView, TabLayout.this.mTabTextAppearance);
        if (TabLayout.this.mTabTextColors != null)
          this.mTextView.setTextColor(TabLayout.this.mTabTextColors); 
        updateTextAndIcon(this.mTextView, this.mIconView);
      } else if (this.mCustomTextView != null || this.mCustomIconView != null) {
        updateTextAndIcon(this.mCustomTextView, this.mCustomIconView);
      } 
      if (tab != null && tab.isSelected()) {
        bool = true;
      } else {
        bool = false;
      } 
      setSelected(bool);
    }
  }
  
  public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
    private final ViewPager mViewPager;
    
    public ViewPagerOnTabSelectedListener(ViewPager param1ViewPager) {
      this.mViewPager = param1ViewPager;
    }
    
    public void onTabReselected(TabLayout.Tab param1Tab) {}
    
    public void onTabSelected(TabLayout.Tab param1Tab) {
      this.mViewPager.setCurrentItem(param1Tab.getPosition());
    }
    
    public void onTabUnselected(TabLayout.Tab param1Tab) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/TabLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */