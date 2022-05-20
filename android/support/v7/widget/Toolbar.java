package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
  private static final String TAG = "Toolbar";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  int mButtonGravity;
  
  ImageButton mCollapseButtonView;
  
  private CharSequence mCollapseDescription;
  
  private Drawable mCollapseIcon;
  
  private boolean mCollapsible;
  
  private int mContentInsetEndWithActions;
  
  private int mContentInsetStartWithNavigation;
  
  private RtlSpacingHelper mContentInsets;
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  View mExpandedActionView;
  
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  
  private int mGravity = 8388627;
  
  private final ArrayList<View> mHiddenViews = new ArrayList<View>();
  
  private ImageView mLogoView;
  
  private int mMaxButtonHeight;
  
  private MenuBuilder.Callback mMenuBuilderCallback;
  
  private ActionMenuView mMenuView;
  
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem param1MenuItem) {
        return (Toolbar.this.mOnMenuItemClickListener != null) ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem) : false;
      }
    };
  
  private ImageButton mNavButtonView;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private ActionMenuPresenter mOuterActionMenuPresenter;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private final Runnable mShowOverflowMenuRunnable = new Runnable() {
      public void run() {
        Toolbar.this.showOverflowMenu();
      }
    };
  
  private CharSequence mSubtitleText;
  
  private int mSubtitleTextAppearance;
  
  private int mSubtitleTextColor;
  
  private TextView mSubtitleTextView;
  
  private final int[] mTempMargins = new int[2];
  
  private final ArrayList<View> mTempViews = new ArrayList<View>();
  
  private int mTitleMarginBottom;
  
  private int mTitleMarginEnd;
  
  private int mTitleMarginStart;
  
  private int mTitleMarginTop;
  
  private CharSequence mTitleText;
  
  private int mTitleTextAppearance;
  
  private int mTitleTextColor;
  
  private TextView mTitleTextView;
  
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.toolbarStyle);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.Toolbar, paramInt, 0);
    this.mTitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
    this.mSubtitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
    this.mGravity = tintTypedArray.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
    this.mButtonGravity = tintTypedArray.getInteger(R.styleable.Toolbar_buttonGravity, 48);
    int i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
    paramInt = i;
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleMargins))
      paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, i); 
    this.mTitleMarginBottom = paramInt;
    this.mTitleMarginTop = paramInt;
    this.mTitleMarginEnd = paramInt;
    this.mTitleMarginStart = paramInt;
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
    if (paramInt >= 0)
      this.mTitleMarginStart = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
    if (paramInt >= 0)
      this.mTitleMarginEnd = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
    if (paramInt >= 0)
      this.mTitleMarginTop = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
    if (paramInt >= 0)
      this.mTitleMarginBottom = paramInt; 
    this.mMaxButtonHeight = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
    int j = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, -2147483648);
    int k = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, -2147483648);
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
    i = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt, i);
    if (j != Integer.MIN_VALUE || k != Integer.MIN_VALUE)
      this.mContentInsets.setRelative(j, k); 
    this.mContentInsetStartWithNavigation = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, -2147483648);
    this.mContentInsetEndWithActions = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, -2147483648);
    this.mCollapseIcon = tintTypedArray.getDrawable(R.styleable.Toolbar_collapseIcon);
    this.mCollapseDescription = tintTypedArray.getText(R.styleable.Toolbar_collapseContentDescription);
    CharSequence charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_title);
    if (!TextUtils.isEmpty(charSequence3))
      setTitle(charSequence3); 
    charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_subtitle);
    if (!TextUtils.isEmpty(charSequence3))
      setSubtitle(charSequence3); 
    this.mPopupContext = getContext();
    setPopupTheme(tintTypedArray.getResourceId(R.styleable.Toolbar_popupTheme, 0));
    Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.Toolbar_navigationIcon);
    if (drawable2 != null)
      setNavigationIcon(drawable2); 
    CharSequence charSequence2 = tintTypedArray.getText(R.styleable.Toolbar_navigationContentDescription);
    if (!TextUtils.isEmpty(charSequence2))
      setNavigationContentDescription(charSequence2); 
    Drawable drawable1 = tintTypedArray.getDrawable(R.styleable.Toolbar_logo);
    if (drawable1 != null)
      setLogo(drawable1); 
    CharSequence charSequence1 = tintTypedArray.getText(R.styleable.Toolbar_logoDescription);
    if (!TextUtils.isEmpty(charSequence1))
      setLogoDescription(charSequence1); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleTextColor))
      setTitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_titleTextColor, -1)); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_subtitleTextColor))
      setSubtitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_subtitleTextColor, -1)); 
    tintTypedArray.recycle();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt) {
    boolean bool = true;
    if (ViewCompat.getLayoutDirection((View)this) != 1)
      bool = false; 
    int i = getChildCount();
    int j = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    paramList.clear();
    if (bool) {
      for (paramInt = i - 1; paramInt >= 0; paramInt--) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == j)
          paramList.add(view); 
      } 
    } else {
      for (paramInt = 0; paramInt < i; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == j)
          paramList.add(view); 
      } 
    } 
  }
  
  private void addSystemView(View paramView, boolean paramBoolean) {
    LayoutParams layoutParams;
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (layoutParams1 == null) {
      layoutParams = generateDefaultLayoutParams();
    } else if (!checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
      layoutParams = generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } else {
      layoutParams = layoutParams;
    } 
    layoutParams.mViewType = 1;
    if (paramBoolean && this.mExpandedActionView != null) {
      paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mHiddenViews.add(paramView);
      return;
    } 
    addView(paramView, (ViewGroup.LayoutParams)layoutParams);
  }
  
  private void ensureContentInsets() {
    if (this.mContentInsets == null)
      this.mContentInsets = new RtlSpacingHelper(); 
  }
  
  private void ensureLogoView() {
    if (this.mLogoView == null)
      this.mLogoView = new AppCompatImageView(getContext()); 
  }
  
  private void ensureMenu() {
    ensureMenuView();
    if (this.mMenuView.peekMenu() == null) {
      MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
      if (this.mExpandedMenuPresenter == null)
        this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
      this.mMenuView.setExpandedActionViewsExclusive(true);
      menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } 
  }
  
  private void ensureMenuView() {
    if (this.mMenuView == null) {
      this.mMenuView = new ActionMenuView(getContext());
      this.mMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
      this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800005 | this.mButtonGravity & 0x70;
      this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      addSystemView((View)this.mMenuView, false);
    } 
  }
  
  private void ensureNavButtonView() {
    if (this.mNavButtonView == null) {
      this.mNavButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      this.mNavButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
  }
  
  private int getChildHorizontalGravity(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    int j = GravityCompat.getAbsoluteGravity(paramInt, i) & 0x7;
    paramInt = j;
    switch (j) {
      default:
        if (i == 1)
          paramInt = 5; 
        break;
      case 1:
      case 3:
      case 5:
        return paramInt;
    } 
    paramInt = 3;
  }
  
  private int getChildTop(View paramView, int paramInt) {
    int j;
    int k;
    int m;
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredHeight();
    if (paramInt > 0) {
      paramInt = (i - paramInt) / 2;
    } else {
      paramInt = 0;
    } 
    switch (getChildVerticalGravity(layoutParams.gravity)) {
      default:
        j = getPaddingTop();
        paramInt = getPaddingBottom();
        k = getHeight();
        m = (k - j - paramInt - i) / 2;
        if (m < layoutParams.topMargin) {
          paramInt = layoutParams.topMargin;
        } else {
          break;
        } 
        return j + paramInt;
      case 48:
        return getPaddingTop() - paramInt;
      case 80:
        return getHeight() - getPaddingBottom() - i - layoutParams.bottomMargin - paramInt;
    } 
    i = k - paramInt - i - m - j;
    paramInt = m;
    if (i < layoutParams.bottomMargin)
      paramInt = Math.max(0, m - layoutParams.bottomMargin - i); 
    return j + paramInt;
  }
  
  private int getChildVerticalGravity(int paramInt) {
    int i = paramInt & 0x70;
    paramInt = i;
    switch (i) {
      default:
        paramInt = this.mGravity & 0x70;
        break;
      case 16:
      case 48:
      case 80:
        break;
    } 
    return paramInt;
  }
  
  private int getHorizontalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
  }
  
  private MenuInflater getMenuInflater() {
    return (MenuInflater)new SupportMenuInflater(getContext());
  }
  
  private int getVerticalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfint) {
    int i = paramArrayOfint[0];
    int j = paramArrayOfint[1];
    int k = 0;
    int m = paramList.size();
    for (byte b = 0; b < m; b++) {
      View view = paramList.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      i = layoutParams.leftMargin - i;
      j = layoutParams.rightMargin - j;
      int n = Math.max(0, i);
      int i1 = Math.max(0, j);
      i = Math.max(0, -i);
      j = Math.max(0, -j);
      k += view.getMeasuredWidth() + n + i1;
    } 
    return k;
  }
  
  private boolean isChildOrHidden(View paramView) {
    return (paramView.getParent() == this || this.mHiddenViews.contains(paramView));
  }
  
  private static boolean isCustomView(View paramView) {
    return (((LayoutParams)paramView.getLayoutParams()).mViewType == 0);
  }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.leftMargin - paramArrayOfint[0];
    paramInt1 += Math.max(0, i);
    paramArrayOfint[0] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 + layoutParams.rightMargin + i;
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.rightMargin - paramArrayOfint[1];
    paramInt1 -= Math.max(0, i);
    paramArrayOfint[1] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 - layoutParams.leftMargin + i;
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = marginLayoutParams.leftMargin - paramArrayOfint[0];
    int j = marginLayoutParams.rightMargin - paramArrayOfint[1];
    int k = Math.max(0, i) + Math.max(0, j);
    paramArrayOfint[0] = Math.max(0, -i);
    paramArrayOfint[1] = Math.max(0, -j);
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + k + paramInt2, marginLayoutParams.width), getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height));
    return paramView.getMeasuredWidth() + k;
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width);
    paramInt2 = getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height);
    paramInt3 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = paramInt2;
    if (paramInt3 != 1073741824) {
      paramInt1 = paramInt2;
      if (paramInt5 >= 0) {
        if (paramInt3 != 0)
          paramInt5 = Math.min(View.MeasureSpec.getSize(paramInt2), paramInt5); 
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt5, 1073741824);
      } 
    } 
    paramView.measure(i, paramInt1);
  }
  
  private void postShowOverflowMenu() {
    removeCallbacks(this.mShowOverflowMenuRunnable);
    post(this.mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mCollapsible : Z
    //   6: ifne -> 13
    //   9: iload_1
    //   10: istore_2
    //   11: iload_2
    //   12: ireturn
    //   13: aload_0
    //   14: invokevirtual getChildCount : ()I
    //   17: istore_3
    //   18: iconst_0
    //   19: istore #4
    //   21: iload #4
    //   23: iload_3
    //   24: if_icmpge -> 68
    //   27: aload_0
    //   28: iload #4
    //   30: invokevirtual getChildAt : (I)Landroid/view/View;
    //   33: astore #5
    //   35: aload_0
    //   36: aload #5
    //   38: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   41: ifeq -> 62
    //   44: aload #5
    //   46: invokevirtual getMeasuredWidth : ()I
    //   49: ifle -> 62
    //   52: iload_1
    //   53: istore_2
    //   54: aload #5
    //   56: invokevirtual getMeasuredHeight : ()I
    //   59: ifgt -> 11
    //   62: iinc #4, 1
    //   65: goto -> 21
    //   68: iconst_1
    //   69: istore_2
    //   70: goto -> 11
  }
  
  private boolean shouldLayout(View paramView) {
    return (paramView != null && paramView.getParent() == this && paramView.getVisibility() != 8);
  }
  
  void addChildrenForExpandedActionView() {
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--)
      addView(this.mHiddenViews.get(i)); 
    this.mHiddenViews.clear();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean canShowOverflowMenu() {
    return (getVisibility() == 0 && this.mMenuView != null && this.mMenuView.isOverflowReserved());
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (super.checkLayoutParams(paramLayoutParams) && paramLayoutParams instanceof LayoutParams);
  }
  
  public void collapseActionView() {
    MenuItemImpl menuItemImpl;
    if (this.mExpandedMenuPresenter == null) {
      menuItemImpl = null;
    } else {
      menuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem;
    } 
    if (menuItemImpl != null)
      menuItemImpl.collapseActionView(); 
  }
  
  public void dismissPopupMenus() {
    if (this.mMenuView != null)
      this.mMenuView.dismissPopupMenus(); 
  }
  
  void ensureCollapseButtonView() {
    if (this.mCollapseButtonView == null) {
      this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
      this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      layoutParams.mViewType = 2;
      this.mCollapseButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              Toolbar.this.collapseActionView();
            }
          });
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ActionBar.LayoutParams) ? new LayoutParams((ActionBar.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)));
  }
  
  public int getContentInsetEnd() {
    return (this.mContentInsets != null) ? this.mContentInsets.getEnd() : 0;
  }
  
  public int getContentInsetEndWithActions() {
    return (this.mContentInsetEndWithActions != Integer.MIN_VALUE) ? this.mContentInsetEndWithActions : getContentInsetEnd();
  }
  
  public int getContentInsetLeft() {
    return (this.mContentInsets != null) ? this.mContentInsets.getLeft() : 0;
  }
  
  public int getContentInsetRight() {
    return (this.mContentInsets != null) ? this.mContentInsets.getRight() : 0;
  }
  
  public int getContentInsetStart() {
    return (this.mContentInsets != null) ? this.mContentInsets.getStart() : 0;
  }
  
  public int getContentInsetStartWithNavigation() {
    return (this.mContentInsetStartWithNavigation != Integer.MIN_VALUE) ? this.mContentInsetStartWithNavigation : getContentInsetStart();
  }
  
  public int getCurrentContentInsetEnd() {
    null = 0;
    if (this.mMenuView != null) {
      MenuBuilder menuBuilder = this.mMenuView.peekMenu();
      if (menuBuilder != null && menuBuilder.hasVisibleItems()) {
        null = 1;
      } else {
        null = 0;
      } 
    } 
    return null ? Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0)) : getContentInsetEnd();
  }
  
  public int getCurrentContentInsetLeft() {
    return (ViewCompat.getLayoutDirection((View)this) == 1) ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
  }
  
  public int getCurrentContentInsetRight() {
    return (ViewCompat.getLayoutDirection((View)this) == 1) ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
  }
  
  public int getCurrentContentInsetStart() {
    return (getNavigationIcon() != null) ? Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0)) : getContentInsetStart();
  }
  
  public Drawable getLogo() {
    return (this.mLogoView != null) ? this.mLogoView.getDrawable() : null;
  }
  
  public CharSequence getLogoDescription() {
    return (this.mLogoView != null) ? this.mLogoView.getContentDescription() : null;
  }
  
  public Menu getMenu() {
    ensureMenu();
    return this.mMenuView.getMenu();
  }
  
  @Nullable
  public CharSequence getNavigationContentDescription() {
    return (this.mNavButtonView != null) ? this.mNavButtonView.getContentDescription() : null;
  }
  
  @Nullable
  public Drawable getNavigationIcon() {
    return (this.mNavButtonView != null) ? this.mNavButtonView.getDrawable() : null;
  }
  
  @Nullable
  public Drawable getOverflowIcon() {
    ensureMenu();
    return this.mMenuView.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public CharSequence getSubtitle() {
    return this.mSubtitleText;
  }
  
  public CharSequence getTitle() {
    return this.mTitleText;
  }
  
  public int getTitleMarginBottom() {
    return this.mTitleMarginBottom;
  }
  
  public int getTitleMarginEnd() {
    return this.mTitleMarginEnd;
  }
  
  public int getTitleMarginStart() {
    return this.mTitleMarginStart;
  }
  
  public int getTitleMarginTop() {
    return this.mTitleMarginTop;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public DecorToolbar getWrapper() {
    if (this.mWrapper == null)
      this.mWrapper = new ToolbarWidgetWrapper(this, true); 
    return this.mWrapper;
  }
  
  public boolean hasExpandedActionView() {
    return (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null);
  }
  
  public boolean hideOverflowMenu() {
    return (this.mMenuView != null && this.mMenuView.hideOverflowMenu());
  }
  
  public void inflateMenu(@MenuRes int paramInt) {
    getMenuInflater().inflate(paramInt, getMenu());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowMenuShowPending() {
    return (this.mMenuView != null && this.mMenuView.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mMenuView != null && this.mMenuView.isOverflowMenuShowing());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isTitleTruncated() {
    boolean bool1 = false;
    if (this.mTitleTextView == null)
      return bool1; 
    Layout layout = this.mTitleTextView.getLayout();
    boolean bool2 = bool1;
    if (layout != null) {
      int i = layout.getLineCount();
      byte b = 0;
      while (true) {
        bool2 = bool1;
        if (b < i) {
          if (layout.getEllipsisCount(b) > 0)
            return true; 
          b++;
          continue;
        } 
        return bool2;
      } 
    } 
    return bool2;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeCallbacks(this.mShowOverflowMenuRunnable);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 9)
      this.mEatingHover = false; 
    if (!this.mEatingHover) {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if (i == 9 && !bool)
        this.mEatingHover = true; 
    } 
    if (i == 10 || i == 3)
      this.mEatingHover = false; 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   4: iconst_1
    //   5: if_icmpne -> 909
    //   8: iconst_1
    //   9: istore #6
    //   11: aload_0
    //   12: invokevirtual getWidth : ()I
    //   15: istore #7
    //   17: aload_0
    //   18: invokevirtual getHeight : ()I
    //   21: istore #8
    //   23: aload_0
    //   24: invokevirtual getPaddingLeft : ()I
    //   27: istore #9
    //   29: aload_0
    //   30: invokevirtual getPaddingRight : ()I
    //   33: istore #10
    //   35: aload_0
    //   36: invokevirtual getPaddingTop : ()I
    //   39: istore #11
    //   41: aload_0
    //   42: invokevirtual getPaddingBottom : ()I
    //   45: istore #12
    //   47: iload #9
    //   49: istore_2
    //   50: iload #7
    //   52: iload #10
    //   54: isub
    //   55: istore #13
    //   57: aload_0
    //   58: getfield mTempMargins : [I
    //   61: astore #14
    //   63: aload #14
    //   65: iconst_1
    //   66: iconst_0
    //   67: iastore
    //   68: aload #14
    //   70: iconst_0
    //   71: iconst_0
    //   72: iastore
    //   73: aload_0
    //   74: invokestatic getMinimumHeight : (Landroid/view/View;)I
    //   77: istore #4
    //   79: iload #4
    //   81: iflt -> 915
    //   84: iload #4
    //   86: iload #5
    //   88: iload_3
    //   89: isub
    //   90: invokestatic min : (II)I
    //   93: istore #15
    //   95: iload_2
    //   96: istore_3
    //   97: iload #13
    //   99: istore #4
    //   101: aload_0
    //   102: aload_0
    //   103: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   106: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   109: ifeq -> 135
    //   112: iload #6
    //   114: ifeq -> 921
    //   117: aload_0
    //   118: aload_0
    //   119: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   122: iload #13
    //   124: aload #14
    //   126: iload #15
    //   128: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   131: istore #4
    //   133: iload_2
    //   134: istore_3
    //   135: iload_3
    //   136: istore #5
    //   138: iload #4
    //   140: istore_2
    //   141: aload_0
    //   142: aload_0
    //   143: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   146: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   149: ifeq -> 175
    //   152: iload #6
    //   154: ifeq -> 942
    //   157: aload_0
    //   158: aload_0
    //   159: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   162: iload #4
    //   164: aload #14
    //   166: iload #15
    //   168: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   171: istore_2
    //   172: iload_3
    //   173: istore #5
    //   175: iload #5
    //   177: istore #4
    //   179: iload_2
    //   180: istore_3
    //   181: aload_0
    //   182: aload_0
    //   183: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   186: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   189: ifeq -> 215
    //   192: iload #6
    //   194: ifeq -> 963
    //   197: aload_0
    //   198: aload_0
    //   199: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   202: iload #5
    //   204: aload #14
    //   206: iload #15
    //   208: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   211: istore #4
    //   213: iload_2
    //   214: istore_3
    //   215: aload_0
    //   216: invokevirtual getCurrentContentInsetLeft : ()I
    //   219: istore #5
    //   221: aload_0
    //   222: invokevirtual getCurrentContentInsetRight : ()I
    //   225: istore_2
    //   226: aload #14
    //   228: iconst_0
    //   229: iconst_0
    //   230: iload #5
    //   232: iload #4
    //   234: isub
    //   235: invokestatic max : (II)I
    //   238: iastore
    //   239: aload #14
    //   241: iconst_1
    //   242: iconst_0
    //   243: iload_2
    //   244: iload #7
    //   246: iload #10
    //   248: isub
    //   249: iload_3
    //   250: isub
    //   251: isub
    //   252: invokestatic max : (II)I
    //   255: iastore
    //   256: iload #4
    //   258: iload #5
    //   260: invokestatic max : (II)I
    //   263: istore #4
    //   265: iload_3
    //   266: iload #7
    //   268: iload #10
    //   270: isub
    //   271: iload_2
    //   272: isub
    //   273: invokestatic min : (II)I
    //   276: istore #5
    //   278: iload #4
    //   280: istore_2
    //   281: iload #5
    //   283: istore_3
    //   284: aload_0
    //   285: aload_0
    //   286: getfield mExpandedActionView : Landroid/view/View;
    //   289: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   292: ifeq -> 318
    //   295: iload #6
    //   297: ifeq -> 984
    //   300: aload_0
    //   301: aload_0
    //   302: getfield mExpandedActionView : Landroid/view/View;
    //   305: iload #5
    //   307: aload #14
    //   309: iload #15
    //   311: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   314: istore_3
    //   315: iload #4
    //   317: istore_2
    //   318: iload_2
    //   319: istore #5
    //   321: iload_3
    //   322: istore #4
    //   324: aload_0
    //   325: aload_0
    //   326: getfield mLogoView : Landroid/widget/ImageView;
    //   329: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   332: ifeq -> 358
    //   335: iload #6
    //   337: ifeq -> 1005
    //   340: aload_0
    //   341: aload_0
    //   342: getfield mLogoView : Landroid/widget/ImageView;
    //   345: iload_3
    //   346: aload #14
    //   348: iload #15
    //   350: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   353: istore #4
    //   355: iload_2
    //   356: istore #5
    //   358: aload_0
    //   359: aload_0
    //   360: getfield mTitleTextView : Landroid/widget/TextView;
    //   363: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   366: istore_1
    //   367: aload_0
    //   368: aload_0
    //   369: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   372: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   375: istore #16
    //   377: iconst_0
    //   378: istore_2
    //   379: iload_1
    //   380: ifeq -> 417
    //   383: aload_0
    //   384: getfield mTitleTextView : Landroid/widget/TextView;
    //   387: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   390: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   393: astore #17
    //   395: iconst_0
    //   396: aload #17
    //   398: getfield topMargin : I
    //   401: aload_0
    //   402: getfield mTitleTextView : Landroid/widget/TextView;
    //   405: invokevirtual getMeasuredHeight : ()I
    //   408: iadd
    //   409: aload #17
    //   411: getfield bottomMargin : I
    //   414: iadd
    //   415: iadd
    //   416: istore_2
    //   417: iload_2
    //   418: istore #18
    //   420: iload #16
    //   422: ifeq -> 460
    //   425: aload_0
    //   426: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   429: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   432: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   435: astore #17
    //   437: iload_2
    //   438: aload #17
    //   440: getfield topMargin : I
    //   443: aload_0
    //   444: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   447: invokevirtual getMeasuredHeight : ()I
    //   450: iadd
    //   451: aload #17
    //   453: getfield bottomMargin : I
    //   456: iadd
    //   457: iadd
    //   458: istore #18
    //   460: iload_1
    //   461: ifne -> 475
    //   464: iload #5
    //   466: istore_3
    //   467: iload #4
    //   469: istore_2
    //   470: iload #16
    //   472: ifeq -> 853
    //   475: iload_1
    //   476: ifeq -> 1026
    //   479: aload_0
    //   480: getfield mTitleTextView : Landroid/widget/TextView;
    //   483: astore #17
    //   485: iload #16
    //   487: ifeq -> 1035
    //   490: aload_0
    //   491: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   494: astore #19
    //   496: aload #17
    //   498: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   501: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   504: astore #17
    //   506: aload #19
    //   508: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   511: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   514: astore #19
    //   516: iload_1
    //   517: ifeq -> 530
    //   520: aload_0
    //   521: getfield mTitleTextView : Landroid/widget/TextView;
    //   524: invokevirtual getMeasuredWidth : ()I
    //   527: ifgt -> 545
    //   530: iload #16
    //   532: ifeq -> 1044
    //   535: aload_0
    //   536: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   539: invokevirtual getMeasuredWidth : ()I
    //   542: ifle -> 1044
    //   545: iconst_1
    //   546: istore #13
    //   548: aload_0
    //   549: getfield mGravity : I
    //   552: bipush #112
    //   554: iand
    //   555: lookupswitch default -> 580, 48 -> 1050, 80 -> 1124
    //   580: iload #8
    //   582: iload #11
    //   584: isub
    //   585: iload #12
    //   587: isub
    //   588: iload #18
    //   590: isub
    //   591: iconst_2
    //   592: idiv
    //   593: istore_3
    //   594: iload_3
    //   595: aload #17
    //   597: getfield topMargin : I
    //   600: aload_0
    //   601: getfield mTitleMarginTop : I
    //   604: iadd
    //   605: if_icmpge -> 1069
    //   608: aload #17
    //   610: getfield topMargin : I
    //   613: aload_0
    //   614: getfield mTitleMarginTop : I
    //   617: iadd
    //   618: istore_2
    //   619: iload #11
    //   621: iload_2
    //   622: iadd
    //   623: istore_2
    //   624: iload #6
    //   626: ifeq -> 1152
    //   629: iload #13
    //   631: ifeq -> 1147
    //   634: aload_0
    //   635: getfield mTitleMarginStart : I
    //   638: istore_3
    //   639: iload_3
    //   640: aload #14
    //   642: iconst_1
    //   643: iaload
    //   644: isub
    //   645: istore_3
    //   646: iload #4
    //   648: iconst_0
    //   649: iload_3
    //   650: invokestatic max : (II)I
    //   653: isub
    //   654: istore #4
    //   656: aload #14
    //   658: iconst_1
    //   659: iconst_0
    //   660: iload_3
    //   661: ineg
    //   662: invokestatic max : (II)I
    //   665: iastore
    //   666: iload #4
    //   668: istore #11
    //   670: iload #4
    //   672: istore_3
    //   673: iload #11
    //   675: istore #6
    //   677: iload_2
    //   678: istore #18
    //   680: iload_1
    //   681: ifeq -> 752
    //   684: aload_0
    //   685: getfield mTitleTextView : Landroid/widget/TextView;
    //   688: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   691: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   694: astore #17
    //   696: iload #11
    //   698: aload_0
    //   699: getfield mTitleTextView : Landroid/widget/TextView;
    //   702: invokevirtual getMeasuredWidth : ()I
    //   705: isub
    //   706: istore #6
    //   708: iload_2
    //   709: aload_0
    //   710: getfield mTitleTextView : Landroid/widget/TextView;
    //   713: invokevirtual getMeasuredHeight : ()I
    //   716: iadd
    //   717: istore #18
    //   719: aload_0
    //   720: getfield mTitleTextView : Landroid/widget/TextView;
    //   723: iload #6
    //   725: iload_2
    //   726: iload #11
    //   728: iload #18
    //   730: invokevirtual layout : (IIII)V
    //   733: iload #6
    //   735: aload_0
    //   736: getfield mTitleMarginEnd : I
    //   739: isub
    //   740: istore #6
    //   742: iload #18
    //   744: aload #17
    //   746: getfield bottomMargin : I
    //   749: iadd
    //   750: istore #18
    //   752: iload_3
    //   753: istore #11
    //   755: iload #16
    //   757: ifeq -> 831
    //   760: aload_0
    //   761: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   764: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   767: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   770: astore #17
    //   772: iload #18
    //   774: aload #17
    //   776: getfield topMargin : I
    //   779: iadd
    //   780: istore #11
    //   782: aload_0
    //   783: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   786: invokevirtual getMeasuredWidth : ()I
    //   789: istore #18
    //   791: iload #11
    //   793: aload_0
    //   794: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   797: invokevirtual getMeasuredHeight : ()I
    //   800: iadd
    //   801: istore_2
    //   802: aload_0
    //   803: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   806: iload_3
    //   807: iload #18
    //   809: isub
    //   810: iload #11
    //   812: iload_3
    //   813: iload_2
    //   814: invokevirtual layout : (IIII)V
    //   817: iload_3
    //   818: aload_0
    //   819: getfield mTitleMarginEnd : I
    //   822: isub
    //   823: istore #11
    //   825: aload #17
    //   827: getfield bottomMargin : I
    //   830: istore_2
    //   831: iload #5
    //   833: istore_3
    //   834: iload #4
    //   836: istore_2
    //   837: iload #13
    //   839: ifeq -> 853
    //   842: iload #6
    //   844: iload #11
    //   846: invokestatic min : (II)I
    //   849: istore_2
    //   850: iload #5
    //   852: istore_3
    //   853: aload_0
    //   854: aload_0
    //   855: getfield mTempViews : Ljava/util/ArrayList;
    //   858: iconst_3
    //   859: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   862: aload_0
    //   863: getfield mTempViews : Ljava/util/ArrayList;
    //   866: invokevirtual size : ()I
    //   869: istore #5
    //   871: iconst_0
    //   872: istore #4
    //   874: iload #4
    //   876: iload #5
    //   878: if_icmpge -> 1385
    //   881: aload_0
    //   882: aload_0
    //   883: getfield mTempViews : Ljava/util/ArrayList;
    //   886: iload #4
    //   888: invokevirtual get : (I)Ljava/lang/Object;
    //   891: checkcast android/view/View
    //   894: iload_3
    //   895: aload #14
    //   897: iload #15
    //   899: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   902: istore_3
    //   903: iinc #4, 1
    //   906: goto -> 874
    //   909: iconst_0
    //   910: istore #6
    //   912: goto -> 11
    //   915: iconst_0
    //   916: istore #15
    //   918: goto -> 95
    //   921: aload_0
    //   922: aload_0
    //   923: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   926: iload_2
    //   927: aload #14
    //   929: iload #15
    //   931: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   934: istore_3
    //   935: iload #13
    //   937: istore #4
    //   939: goto -> 135
    //   942: aload_0
    //   943: aload_0
    //   944: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   947: iload_3
    //   948: aload #14
    //   950: iload #15
    //   952: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   955: istore #5
    //   957: iload #4
    //   959: istore_2
    //   960: goto -> 175
    //   963: aload_0
    //   964: aload_0
    //   965: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   968: iload_2
    //   969: aload #14
    //   971: iload #15
    //   973: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   976: istore_3
    //   977: iload #5
    //   979: istore #4
    //   981: goto -> 215
    //   984: aload_0
    //   985: aload_0
    //   986: getfield mExpandedActionView : Landroid/view/View;
    //   989: iload #4
    //   991: aload #14
    //   993: iload #15
    //   995: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   998: istore_2
    //   999: iload #5
    //   1001: istore_3
    //   1002: goto -> 318
    //   1005: aload_0
    //   1006: aload_0
    //   1007: getfield mLogoView : Landroid/widget/ImageView;
    //   1010: iload_2
    //   1011: aload #14
    //   1013: iload #15
    //   1015: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   1018: istore #5
    //   1020: iload_3
    //   1021: istore #4
    //   1023: goto -> 358
    //   1026: aload_0
    //   1027: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1030: astore #17
    //   1032: goto -> 485
    //   1035: aload_0
    //   1036: getfield mTitleTextView : Landroid/widget/TextView;
    //   1039: astore #19
    //   1041: goto -> 496
    //   1044: iconst_0
    //   1045: istore #13
    //   1047: goto -> 548
    //   1050: aload_0
    //   1051: invokevirtual getPaddingTop : ()I
    //   1054: aload #17
    //   1056: getfield topMargin : I
    //   1059: iadd
    //   1060: aload_0
    //   1061: getfield mTitleMarginTop : I
    //   1064: iadd
    //   1065: istore_2
    //   1066: goto -> 624
    //   1069: iload #8
    //   1071: iload #12
    //   1073: isub
    //   1074: iload #18
    //   1076: isub
    //   1077: iload_3
    //   1078: isub
    //   1079: iload #11
    //   1081: isub
    //   1082: istore #18
    //   1084: iload_3
    //   1085: istore_2
    //   1086: iload #18
    //   1088: aload #17
    //   1090: getfield bottomMargin : I
    //   1093: aload_0
    //   1094: getfield mTitleMarginBottom : I
    //   1097: iadd
    //   1098: if_icmpge -> 619
    //   1101: iconst_0
    //   1102: iload_3
    //   1103: aload #19
    //   1105: getfield bottomMargin : I
    //   1108: aload_0
    //   1109: getfield mTitleMarginBottom : I
    //   1112: iadd
    //   1113: iload #18
    //   1115: isub
    //   1116: isub
    //   1117: invokestatic max : (II)I
    //   1120: istore_2
    //   1121: goto -> 619
    //   1124: iload #8
    //   1126: iload #12
    //   1128: isub
    //   1129: aload #19
    //   1131: getfield bottomMargin : I
    //   1134: isub
    //   1135: aload_0
    //   1136: getfield mTitleMarginBottom : I
    //   1139: isub
    //   1140: iload #18
    //   1142: isub
    //   1143: istore_2
    //   1144: goto -> 624
    //   1147: iconst_0
    //   1148: istore_3
    //   1149: goto -> 639
    //   1152: iload #13
    //   1154: ifeq -> 1380
    //   1157: aload_0
    //   1158: getfield mTitleMarginStart : I
    //   1161: istore_3
    //   1162: iload_3
    //   1163: aload #14
    //   1165: iconst_0
    //   1166: iaload
    //   1167: isub
    //   1168: istore #6
    //   1170: iload #5
    //   1172: iconst_0
    //   1173: iload #6
    //   1175: invokestatic max : (II)I
    //   1178: iadd
    //   1179: istore_3
    //   1180: aload #14
    //   1182: iconst_0
    //   1183: iconst_0
    //   1184: iload #6
    //   1186: ineg
    //   1187: invokestatic max : (II)I
    //   1190: iastore
    //   1191: iload_3
    //   1192: istore #11
    //   1194: iload_3
    //   1195: istore #5
    //   1197: iload #11
    //   1199: istore #6
    //   1201: iload_2
    //   1202: istore #18
    //   1204: iload_1
    //   1205: ifeq -> 1276
    //   1208: aload_0
    //   1209: getfield mTitleTextView : Landroid/widget/TextView;
    //   1212: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1215: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   1218: astore #17
    //   1220: iload #11
    //   1222: aload_0
    //   1223: getfield mTitleTextView : Landroid/widget/TextView;
    //   1226: invokevirtual getMeasuredWidth : ()I
    //   1229: iadd
    //   1230: istore #6
    //   1232: iload_2
    //   1233: aload_0
    //   1234: getfield mTitleTextView : Landroid/widget/TextView;
    //   1237: invokevirtual getMeasuredHeight : ()I
    //   1240: iadd
    //   1241: istore #18
    //   1243: aload_0
    //   1244: getfield mTitleTextView : Landroid/widget/TextView;
    //   1247: iload #11
    //   1249: iload_2
    //   1250: iload #6
    //   1252: iload #18
    //   1254: invokevirtual layout : (IIII)V
    //   1257: iload #6
    //   1259: aload_0
    //   1260: getfield mTitleMarginEnd : I
    //   1263: iadd
    //   1264: istore #6
    //   1266: iload #18
    //   1268: aload #17
    //   1270: getfield bottomMargin : I
    //   1273: iadd
    //   1274: istore #18
    //   1276: iload #5
    //   1278: istore #11
    //   1280: iload #16
    //   1282: ifeq -> 1358
    //   1285: aload_0
    //   1286: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1289: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1292: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   1295: astore #17
    //   1297: iload #18
    //   1299: aload #17
    //   1301: getfield topMargin : I
    //   1304: iadd
    //   1305: istore #11
    //   1307: iload #5
    //   1309: aload_0
    //   1310: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1313: invokevirtual getMeasuredWidth : ()I
    //   1316: iadd
    //   1317: istore_2
    //   1318: iload #11
    //   1320: aload_0
    //   1321: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1324: invokevirtual getMeasuredHeight : ()I
    //   1327: iadd
    //   1328: istore #18
    //   1330: aload_0
    //   1331: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1334: iload #5
    //   1336: iload #11
    //   1338: iload_2
    //   1339: iload #18
    //   1341: invokevirtual layout : (IIII)V
    //   1344: iload_2
    //   1345: aload_0
    //   1346: getfield mTitleMarginEnd : I
    //   1349: iadd
    //   1350: istore #11
    //   1352: aload #17
    //   1354: getfield bottomMargin : I
    //   1357: istore_2
    //   1358: iload #4
    //   1360: istore_2
    //   1361: iload #13
    //   1363: ifeq -> 853
    //   1366: iload #6
    //   1368: iload #11
    //   1370: invokestatic max : (II)I
    //   1373: istore_3
    //   1374: iload #4
    //   1376: istore_2
    //   1377: goto -> 853
    //   1380: iconst_0
    //   1381: istore_3
    //   1382: goto -> 1162
    //   1385: aload_0
    //   1386: aload_0
    //   1387: getfield mTempViews : Ljava/util/ArrayList;
    //   1390: iconst_5
    //   1391: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1394: aload_0
    //   1395: getfield mTempViews : Ljava/util/ArrayList;
    //   1398: invokevirtual size : ()I
    //   1401: istore #13
    //   1403: iconst_0
    //   1404: istore #5
    //   1406: iload_2
    //   1407: istore #4
    //   1409: iload #5
    //   1411: istore_2
    //   1412: iload_2
    //   1413: iload #13
    //   1415: if_icmpge -> 1447
    //   1418: aload_0
    //   1419: aload_0
    //   1420: getfield mTempViews : Ljava/util/ArrayList;
    //   1423: iload_2
    //   1424: invokevirtual get : (I)Ljava/lang/Object;
    //   1427: checkcast android/view/View
    //   1430: iload #4
    //   1432: aload #14
    //   1434: iload #15
    //   1436: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   1439: istore #4
    //   1441: iinc #2, 1
    //   1444: goto -> 1412
    //   1447: aload_0
    //   1448: aload_0
    //   1449: getfield mTempViews : Ljava/util/ArrayList;
    //   1452: iconst_1
    //   1453: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1456: aload_0
    //   1457: aload_0
    //   1458: getfield mTempViews : Ljava/util/ArrayList;
    //   1461: aload #14
    //   1463: invokespecial getViewListMeasuredWidth : (Ljava/util/List;[I)I
    //   1466: istore_2
    //   1467: iload #9
    //   1469: iload #7
    //   1471: iload #9
    //   1473: isub
    //   1474: iload #10
    //   1476: isub
    //   1477: iconst_2
    //   1478: idiv
    //   1479: iadd
    //   1480: iload_2
    //   1481: iconst_2
    //   1482: idiv
    //   1483: isub
    //   1484: istore #5
    //   1486: iload #5
    //   1488: iload_2
    //   1489: iadd
    //   1490: istore #13
    //   1492: iload #5
    //   1494: iload_3
    //   1495: if_icmpge -> 1544
    //   1498: iload_3
    //   1499: istore_2
    //   1500: aload_0
    //   1501: getfield mTempViews : Ljava/util/ArrayList;
    //   1504: invokevirtual size : ()I
    //   1507: istore #4
    //   1509: iconst_0
    //   1510: istore_3
    //   1511: iload_3
    //   1512: iload #4
    //   1514: if_icmpge -> 1566
    //   1517: aload_0
    //   1518: aload_0
    //   1519: getfield mTempViews : Ljava/util/ArrayList;
    //   1522: iload_3
    //   1523: invokevirtual get : (I)Ljava/lang/Object;
    //   1526: checkcast android/view/View
    //   1529: iload_2
    //   1530: aload #14
    //   1532: iload #15
    //   1534: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   1537: istore_2
    //   1538: iinc #3, 1
    //   1541: goto -> 1511
    //   1544: iload #5
    //   1546: istore_2
    //   1547: iload #13
    //   1549: iload #4
    //   1551: if_icmple -> 1500
    //   1554: iload #5
    //   1556: iload #13
    //   1558: iload #4
    //   1560: isub
    //   1561: isub
    //   1562: istore_2
    //   1563: goto -> 1500
    //   1566: aload_0
    //   1567: getfield mTempViews : Ljava/util/ArrayList;
    //   1570: invokevirtual clear : ()V
    //   1573: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = 0;
    int j = 0;
    int[] arrayOfInt = this.mTempMargins;
    if (ViewUtils.isLayoutRtl((View)this)) {
      k = 1;
      m = 0;
    } else {
      k = 0;
      m = 1;
    } 
    int n = 0;
    if (shouldLayout((View)this.mNavButtonView)) {
      measureChildConstrained((View)this.mNavButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mNavButtonView);
      i = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mNavButtonView));
      j = ViewUtils.combineMeasuredStates(0, ViewCompat.getMeasuredState((View)this.mNavButtonView));
    } 
    int i1 = j;
    int i2 = i;
    if (shouldLayout((View)this.mCollapseButtonView)) {
      measureChildConstrained((View)this.mCollapseButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mCollapseButtonView);
      i2 = Math.max(i, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mCollapseButtonView));
      i1 = ViewUtils.combineMeasuredStates(j, ViewCompat.getMeasuredState((View)this.mCollapseButtonView));
    } 
    j = getCurrentContentInsetStart();
    int i3 = 0 + Math.max(j, n);
    arrayOfInt[k] = Math.max(0, j - n);
    n = 0;
    j = i1;
    i = i2;
    if (shouldLayout((View)this.mMenuView)) {
      measureChildConstrained((View)this.mMenuView, paramInt1, i3, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mMenuView.getMeasuredWidth() + getHorizontalMargins((View)this.mMenuView);
      i = Math.max(i2, this.mMenuView.getMeasuredHeight() + getVerticalMargins((View)this.mMenuView));
      j = ViewUtils.combineMeasuredStates(i1, ViewCompat.getMeasuredState((View)this.mMenuView));
    } 
    i1 = getCurrentContentInsetEnd();
    int k = i3 + Math.max(i1, n);
    arrayOfInt[m] = Math.max(0, i1 - n);
    int m = k;
    i1 = j;
    i2 = i;
    if (shouldLayout(this.mExpandedActionView)) {
      m = k + measureChildCollapseMargins(this.mExpandedActionView, paramInt1, k, paramInt2, 0, arrayOfInt);
      i2 = Math.max(i, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
      i1 = ViewUtils.combineMeasuredStates(j, ViewCompat.getMeasuredState(this.mExpandedActionView));
    } 
    n = m;
    i = i1;
    j = i2;
    if (shouldLayout((View)this.mLogoView)) {
      n = m + measureChildCollapseMargins((View)this.mLogoView, paramInt1, m, paramInt2, 0, arrayOfInt);
      j = Math.max(i2, this.mLogoView.getMeasuredHeight() + getVerticalMargins((View)this.mLogoView));
      i = ViewUtils.combineMeasuredStates(i1, ViewCompat.getMeasuredState((View)this.mLogoView));
    } 
    i3 = getChildCount();
    i2 = 0;
    m = j;
    i1 = i;
    while (i2 < i3) {
      View view = getChildAt(i2);
      j = n;
      i = i1;
      k = m;
      if (((LayoutParams)view.getLayoutParams()).mViewType == 0)
        if (!shouldLayout(view)) {
          k = m;
          i = i1;
          j = n;
        } else {
          j = n + measureChildCollapseMargins(view, paramInt1, n, paramInt2, 0, arrayOfInt);
          k = Math.max(m, view.getMeasuredHeight() + getVerticalMargins(view));
          i = ViewUtils.combineMeasuredStates(i1, ViewCompat.getMeasuredState(view));
        }  
      i2++;
      n = j;
      i1 = i;
      m = k;
    } 
    i = 0;
    j = 0;
    int i4 = this.mTitleMarginTop + this.mTitleMarginBottom;
    int i5 = this.mTitleMarginStart + this.mTitleMarginEnd;
    i2 = i1;
    if (shouldLayout((View)this.mTitleTextView)) {
      measureChildCollapseMargins((View)this.mTitleTextView, paramInt1, n + i5, paramInt2, i4, arrayOfInt);
      i = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins((View)this.mTitleTextView);
      j = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins((View)this.mTitleTextView);
      i2 = ViewUtils.combineMeasuredStates(i1, ViewCompat.getMeasuredState((View)this.mTitleTextView));
    } 
    k = i2;
    i3 = j;
    i1 = i;
    if (shouldLayout((View)this.mSubtitleTextView)) {
      i1 = Math.max(i, measureChildCollapseMargins((View)this.mSubtitleTextView, paramInt1, n + i5, paramInt2, j + i4, arrayOfInt));
      i3 = j + this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins((View)this.mSubtitleTextView);
      k = ViewUtils.combineMeasuredStates(i2, ViewCompat.getMeasuredState((View)this.mSubtitleTextView));
    } 
    i2 = Math.max(m, i3);
    m = getPaddingLeft();
    i3 = getPaddingRight();
    i = getPaddingTop();
    j = getPaddingBottom();
    i1 = ViewCompat.resolveSizeAndState(Math.max(n + i1 + m + i3, getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & k);
    paramInt1 = ViewCompat.resolveSizeAndState(Math.max(i2 + i + j, getSuggestedMinimumHeight()), paramInt2, k << 16);
    if (shouldCollapse())
      paramInt1 = 0; 
    setMeasuredDimension(i1, paramInt1);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (this.mMenuView != null) {
      MenuBuilder menuBuilder = this.mMenuView.peekMenu();
    } else {
      paramParcelable = null;
    } 
    if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && paramParcelable != null) {
      MenuItem menuItem = paramParcelable.findItem(savedState.expandedMenuItemId);
      if (menuItem != null)
        MenuItemCompat.expandActionView(menuItem); 
    } 
    if (savedState.isOverflowOpen)
      postShowOverflowMenu(); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    boolean bool = true;
    if (Build.VERSION.SDK_INT >= 17)
      super.onRtlPropertiesChanged(paramInt); 
    ensureContentInsets();
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (paramInt != 1)
      bool = false; 
    rtlSpacingHelper.setDirection(bool);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null)
      savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId(); 
    savedState.isOverflowOpen = isOverflowMenuShowing();
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0)
      this.mEatingTouch = false; 
    if (!this.mEatingTouch) {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (i == 0 && !bool)
        this.mEatingTouch = true; 
    } 
    if (i == 1 || i == 3)
      this.mEatingTouch = false; 
    return true;
  }
  
  void removeChildrenForExpandedActionView() {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      View view = getChildAt(i);
      if (((LayoutParams)view.getLayoutParams()).mViewType != 2 && view != this.mMenuView) {
        removeViewAt(i);
        this.mHiddenViews.add(view);
      } 
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setCollapsible(boolean paramBoolean) {
    this.mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetEndWithActions(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetEndWithActions) {
      this.mContentInsetEndWithActions = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetStartWithNavigation(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetStartWithNavigation) {
      this.mContentInsetStartWithNavigation = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(@DrawableRes int paramInt) {
    setLogo(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setLogo(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureLogoView();
      if (!isChildOrHidden((View)this.mLogoView))
        addSystemView((View)this.mLogoView, true); 
    } else if (this.mLogoView != null && isChildOrHidden((View)this.mLogoView)) {
      removeView((View)this.mLogoView);
      this.mHiddenViews.remove(this.mLogoView);
    } 
    if (this.mLogoView != null)
      this.mLogoView.setImageDrawable(paramDrawable); 
  }
  
  public void setLogoDescription(@StringRes int paramInt) {
    setLogoDescription(getContext().getText(paramInt));
  }
  
  public void setLogoDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureLogoView(); 
    if (this.mLogoView != null)
      this.mLogoView.setContentDescription(paramCharSequence); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter) {
    if (paramMenuBuilder != null || this.mMenuView != null) {
      ensureMenuView();
      MenuBuilder menuBuilder = this.mMenuView.peekMenu();
      if (menuBuilder != paramMenuBuilder) {
        if (menuBuilder != null) {
          menuBuilder.removeMenuPresenter((MenuPresenter)this.mOuterActionMenuPresenter);
          menuBuilder.removeMenuPresenter(this.mExpandedMenuPresenter);
        } 
        if (this.mExpandedMenuPresenter == null)
          this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
        paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (paramMenuBuilder != null) {
          paramMenuBuilder.addMenuPresenter((MenuPresenter)paramActionMenuPresenter, this.mPopupContext);
          paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        } else {
          paramActionMenuPresenter.initForMenu(this.mPopupContext, null);
          this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
          paramActionMenuPresenter.updateMenuView(true);
          this.mExpandedMenuPresenter.updateMenuView(true);
        } 
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(paramActionMenuPresenter);
        this.mOuterActionMenuPresenter = paramActionMenuPresenter;
      } 
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
    if (this.mMenuView != null)
      this.mMenuView.setMenuCallbacks(paramCallback, paramCallback1); 
  }
  
  public void setNavigationContentDescription(@StringRes int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getContext().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setNavigationContentDescription(charSequence);
  }
  
  public void setNavigationContentDescription(@Nullable CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureNavButtonView(); 
    if (this.mNavButtonView != null)
      this.mNavButtonView.setContentDescription(paramCharSequence); 
  }
  
  public void setNavigationIcon(@DrawableRes int paramInt) {
    setNavigationIcon(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setNavigationIcon(@Nullable Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureNavButtonView();
      if (!isChildOrHidden((View)this.mNavButtonView))
        addSystemView((View)this.mNavButtonView, true); 
    } else if (this.mNavButtonView != null && isChildOrHidden((View)this.mNavButtonView)) {
      removeView((View)this.mNavButtonView);
      this.mHiddenViews.remove(this.mNavButtonView);
    } 
    if (this.mNavButtonView != null)
      this.mNavButtonView.setImageDrawable(paramDrawable); 
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener) {
    ensureNavButtonView();
    this.mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable) {
    ensureMenu();
    this.mMenuView.setOverflowIcon(paramDrawable);
  }
  
  public void setPopupTheme(@StyleRes int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
        return;
      } 
    } else {
      return;
    } 
    this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
  }
  
  public void setSubtitle(@StringRes int paramInt) {
    setSubtitle(getContext().getText(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mSubtitleTextView == null) {
        Context context = getContext();
        this.mSubtitleTextView = new AppCompatTextView(context);
        this.mSubtitleTextView.setSingleLine();
        this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mSubtitleTextAppearance != 0)
          this.mSubtitleTextView.setTextAppearance(context, this.mSubtitleTextAppearance); 
        if (this.mSubtitleTextColor != 0)
          this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor); 
      } 
      if (!isChildOrHidden((View)this.mSubtitleTextView))
        addSystemView((View)this.mSubtitleTextView, true); 
    } else if (this.mSubtitleTextView != null && isChildOrHidden((View)this.mSubtitleTextView)) {
      removeView((View)this.mSubtitleTextView);
      this.mHiddenViews.remove(this.mSubtitleTextView);
    } 
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setText(paramCharSequence); 
    this.mSubtitleText = paramCharSequence;
  }
  
  public void setSubtitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mSubtitleTextAppearance = paramInt;
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setSubtitleTextColor(@ColorInt int paramInt) {
    this.mSubtitleTextColor = paramInt;
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setTextColor(paramInt); 
  }
  
  public void setTitle(@StringRes int paramInt) {
    setTitle(getContext().getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mTitleTextView == null) {
        Context context = getContext();
        this.mTitleTextView = new AppCompatTextView(context);
        this.mTitleTextView.setSingleLine();
        this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mTitleTextAppearance != 0)
          this.mTitleTextView.setTextAppearance(context, this.mTitleTextAppearance); 
        if (this.mTitleTextColor != 0)
          this.mTitleTextView.setTextColor(this.mTitleTextColor); 
      } 
      if (!isChildOrHidden((View)this.mTitleTextView))
        addSystemView((View)this.mTitleTextView, true); 
    } else if (this.mTitleTextView != null && isChildOrHidden((View)this.mTitleTextView)) {
      removeView((View)this.mTitleTextView);
      this.mHiddenViews.remove(this.mTitleTextView);
    } 
    if (this.mTitleTextView != null)
      this.mTitleTextView.setText(paramCharSequence); 
    this.mTitleText = paramCharSequence;
  }
  
  public void setTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mTitleMarginStart = paramInt1;
    this.mTitleMarginTop = paramInt2;
    this.mTitleMarginEnd = paramInt3;
    this.mTitleMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setTitleMarginBottom(int paramInt) {
    this.mTitleMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginEnd(int paramInt) {
    this.mTitleMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginStart(int paramInt) {
    this.mTitleMarginStart = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginTop(int paramInt) {
    this.mTitleMarginTop = paramInt;
    requestLayout();
  }
  
  public void setTitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mTitleTextAppearance = paramInt;
    if (this.mTitleTextView != null)
      this.mTitleTextView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setTitleTextColor(@ColorInt int paramInt) {
    this.mTitleTextColor = paramInt;
    if (this.mTitleTextView != null)
      this.mTitleTextView.setTextColor(paramInt); 
  }
  
  public boolean showOverflowMenu() {
    return (this.mMenuView != null && this.mMenuView.showOverflowMenu());
  }
  
  private class ExpandedActionViewMenuPresenter implements MenuPresenter {
    MenuItemImpl mCurrentExpandedItem;
    
    MenuBuilder mMenu;
    
    public boolean collapseItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed(); 
      Toolbar.this.removeView(Toolbar.this.mExpandedActionView);
      Toolbar.this.removeView((View)Toolbar.this.mCollapseButtonView);
      Toolbar.this.mExpandedActionView = null;
      Toolbar.this.addChildrenForExpandedActionView();
      this.mCurrentExpandedItem = null;
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      Toolbar.this.ensureCollapseButtonView();
      if (Toolbar.this.mCollapseButtonView.getParent() != Toolbar.this)
        Toolbar.this.addView((View)Toolbar.this.mCollapseButtonView); 
      Toolbar.this.mExpandedActionView = param1MenuItemImpl.getActionView();
      this.mCurrentExpandedItem = param1MenuItemImpl;
      if (Toolbar.this.mExpandedActionView.getParent() != Toolbar.this) {
        Toolbar.LayoutParams layoutParams = Toolbar.this.generateDefaultLayoutParams();
        layoutParams.gravity = 0x800003 | Toolbar.this.mButtonGravity & 0x70;
        layoutParams.mViewType = 2;
        Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        Toolbar.this.addView(Toolbar.this.mExpandedActionView);
      } 
      Toolbar.this.removeChildrenForExpandedActionView();
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(true);
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded(); 
      return true;
    }
    
    public boolean flagActionItems() {
      return false;
    }
    
    public int getId() {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup param1ViewGroup) {
      return null;
    }
    
    public void initForMenu(Context param1Context, MenuBuilder param1MenuBuilder) {
      if (this.mMenu != null && this.mCurrentExpandedItem != null)
        this.mMenu.collapseItemActionView(this.mCurrentExpandedItem); 
      this.mMenu = param1MenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder param1SubMenuBuilder) {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback param1Callback) {}
    
    public void updateMenuView(boolean param1Boolean) {
      boolean bool;
      if (this.mCurrentExpandedItem != null) {
        boolean bool1 = false;
        bool = bool1;
        if (this.mMenu != null) {
          int i = this.mMenu.size();
          byte b = 0;
          while (true) {
            bool = bool1;
            if (b < i)
              if (this.mMenu.getItem(b) == this.mCurrentExpandedItem) {
                bool = true;
              } else {
                b++;
                continue;
              }  
            if (!bool)
              collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
            return;
          } 
        } 
      } else {
        return;
      } 
      if (!bool)
        collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
    }
  }
  
  public static class LayoutParams extends ActionBar.LayoutParams {
    static final int CUSTOM = 0;
    
    static final int EXPANDED = 2;
    
    static final int SYSTEM = 1;
    
    int mViewType = 0;
    
    public LayoutParams(int param1Int) {
      this(-2, -1, param1Int);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.gravity = 8388627;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(@NonNull Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ActionBar.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.mViewType = param1LayoutParams.mViewType;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super((ViewGroup.LayoutParams)param1MarginLayoutParams);
      copyMarginsFromCompat(param1MarginLayoutParams);
    }
    
    void copyMarginsFromCompat(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      this.leftMargin = param1MarginLayoutParams.leftMargin;
      this.topMargin = param1MarginLayoutParams.topMargin;
      this.rightMargin = param1MarginLayoutParams.rightMargin;
      this.bottomMargin = param1MarginLayoutParams.bottomMargin;
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public Toolbar.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new Toolbar.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public Toolbar.SavedState[] newArray(int param2Int) {
            return new Toolbar.SavedState[param2Int];
          }
        });
    
    int expandedMenuItemId;
    
    boolean isOverflowOpen;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.expandedMenuItemId = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOverflowOpen = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.expandedMenuItemId);
      if (this.isOverflowOpen) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new Toolbar.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public Toolbar.SavedState[] newArray(int param1Int) {
      return new Toolbar.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/Toolbar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */