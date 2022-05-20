package android.support.v7.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarContextView extends AbsActionBarView {
  private static final String TAG = "ActionBarContextView";
  
  private View mClose;
  
  private int mCloseItemLayout;
  
  private View mCustomView;
  
  private CharSequence mSubtitle;
  
  private int mSubtitleStyleRes;
  
  private TextView mSubtitleView;
  
  private CharSequence mTitle;
  
  private LinearLayout mTitleLayout;
  
  private boolean mTitleOptional;
  
  private int mTitleStyleRes;
  
  private TextView mTitleView;
  
  public ActionBarContextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.actionModeStyle);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.ActionMode, paramInt, 0);
    ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.ActionMode_background));
    this.mTitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
    this.mSubtitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
    this.mContentHeight = tintTypedArray.getLayoutDimension(R.styleable.ActionMode_height, 0);
    this.mCloseItemLayout = tintTypedArray.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
    tintTypedArray.recycle();
  }
  
  private void initTitle() {
    // Byte code:
    //   0: bipush #8
    //   2: istore_1
    //   3: aload_0
    //   4: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   7: ifnonnull -> 120
    //   10: aload_0
    //   11: invokevirtual getContext : ()Landroid/content/Context;
    //   14: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   17: getstatic android/support/v7/appcompat/R$layout.abc_action_bar_title_item : I
    //   20: aload_0
    //   21: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   24: pop
    //   25: aload_0
    //   26: aload_0
    //   27: aload_0
    //   28: invokevirtual getChildCount : ()I
    //   31: iconst_1
    //   32: isub
    //   33: invokevirtual getChildAt : (I)Landroid/view/View;
    //   36: checkcast android/widget/LinearLayout
    //   39: putfield mTitleLayout : Landroid/widget/LinearLayout;
    //   42: aload_0
    //   43: aload_0
    //   44: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   47: getstatic android/support/v7/appcompat/R$id.action_bar_title : I
    //   50: invokevirtual findViewById : (I)Landroid/view/View;
    //   53: checkcast android/widget/TextView
    //   56: putfield mTitleView : Landroid/widget/TextView;
    //   59: aload_0
    //   60: aload_0
    //   61: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   64: getstatic android/support/v7/appcompat/R$id.action_bar_subtitle : I
    //   67: invokevirtual findViewById : (I)Landroid/view/View;
    //   70: checkcast android/widget/TextView
    //   73: putfield mSubtitleView : Landroid/widget/TextView;
    //   76: aload_0
    //   77: getfield mTitleStyleRes : I
    //   80: ifeq -> 98
    //   83: aload_0
    //   84: getfield mTitleView : Landroid/widget/TextView;
    //   87: aload_0
    //   88: invokevirtual getContext : ()Landroid/content/Context;
    //   91: aload_0
    //   92: getfield mTitleStyleRes : I
    //   95: invokevirtual setTextAppearance : (Landroid/content/Context;I)V
    //   98: aload_0
    //   99: getfield mSubtitleStyleRes : I
    //   102: ifeq -> 120
    //   105: aload_0
    //   106: getfield mSubtitleView : Landroid/widget/TextView;
    //   109: aload_0
    //   110: invokevirtual getContext : ()Landroid/content/Context;
    //   113: aload_0
    //   114: getfield mSubtitleStyleRes : I
    //   117: invokevirtual setTextAppearance : (Landroid/content/Context;I)V
    //   120: aload_0
    //   121: getfield mTitleView : Landroid/widget/TextView;
    //   124: aload_0
    //   125: getfield mTitle : Ljava/lang/CharSequence;
    //   128: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   131: aload_0
    //   132: getfield mSubtitleView : Landroid/widget/TextView;
    //   135: aload_0
    //   136: getfield mSubtitle : Ljava/lang/CharSequence;
    //   139: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   142: aload_0
    //   143: getfield mTitle : Ljava/lang/CharSequence;
    //   146: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   149: ifne -> 229
    //   152: iconst_1
    //   153: istore_2
    //   154: aload_0
    //   155: getfield mSubtitle : Ljava/lang/CharSequence;
    //   158: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   161: ifne -> 234
    //   164: iconst_1
    //   165: istore_3
    //   166: aload_0
    //   167: getfield mSubtitleView : Landroid/widget/TextView;
    //   170: astore #4
    //   172: iload_3
    //   173: ifeq -> 239
    //   176: iconst_0
    //   177: istore #5
    //   179: aload #4
    //   181: iload #5
    //   183: invokevirtual setVisibility : (I)V
    //   186: aload_0
    //   187: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   190: astore #4
    //   192: iload_2
    //   193: ifne -> 202
    //   196: iload_1
    //   197: istore_2
    //   198: iload_3
    //   199: ifeq -> 204
    //   202: iconst_0
    //   203: istore_2
    //   204: aload #4
    //   206: iload_2
    //   207: invokevirtual setVisibility : (I)V
    //   210: aload_0
    //   211: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   214: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   217: ifnonnull -> 228
    //   220: aload_0
    //   221: aload_0
    //   222: getfield mTitleLayout : Landroid/widget/LinearLayout;
    //   225: invokevirtual addView : (Landroid/view/View;)V
    //   228: return
    //   229: iconst_0
    //   230: istore_2
    //   231: goto -> 154
    //   234: iconst_0
    //   235: istore_3
    //   236: goto -> 166
    //   239: bipush #8
    //   241: istore #5
    //   243: goto -> 179
  }
  
  public void closeMode() {
    if (this.mClose == null)
      killMode(); 
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new ViewGroup.MarginLayoutParams(-1, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getSubtitle() {
    return this.mSubtitle;
  }
  
  public CharSequence getTitle() {
    return this.mTitle;
  }
  
  public boolean hideOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.hideOverflowMenu() : false;
  }
  
  public void initForMode(final ActionMode mode) {
    if (this.mClose == null) {
      this.mClose = LayoutInflater.from(getContext()).inflate(this.mCloseItemLayout, this, false);
      addView(this.mClose);
    } else if (this.mClose.getParent() == null) {
      addView(this.mClose);
    } 
    this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            mode.finish();
          }
        });
    MenuBuilder menuBuilder = (MenuBuilder)mode.getMenu();
    if (this.mActionMenuPresenter != null)
      this.mActionMenuPresenter.dismissPopupMenus(); 
    this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
    this.mActionMenuPresenter.setReserveOverflow(true);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
    menuBuilder.addMenuPresenter((MenuPresenter)this.mActionMenuPresenter, this.mPopupContext);
    this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
    ViewCompat.setBackground((View)this.mMenuView, null);
    addView((View)this.mMenuView, layoutParams);
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.isOverflowMenuShowing() : false;
  }
  
  public boolean isTitleOptional() {
    return this.mTitleOptional;
  }
  
  public void killMode() {
    removeAllViews();
    this.mCustomView = null;
    this.mMenuView = null;
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mActionMenuPresenter != null) {
      this.mActionMenuPresenter.hideOverflowMenu();
      this.mActionMenuPresenter.hideSubMenus();
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (Build.VERSION.SDK_INT >= 14) {
      if (paramAccessibilityEvent.getEventType() == 32) {
        paramAccessibilityEvent.setSource((View)this);
        paramAccessibilityEvent.setClassName(getClass().getName());
        paramAccessibilityEvent.setPackageName(getContext().getPackageName());
        paramAccessibilityEvent.setContentDescription(this.mTitle);
        return;
      } 
    } else {
      return;
    } 
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i;
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    if (paramBoolean) {
      i = paramInt3 - paramInt1 - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    paramInt2 = i;
    if (this.mClose != null) {
      paramInt2 = i;
      if (this.mClose.getVisibility() != 8) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
        if (paramBoolean) {
          paramInt2 = marginLayoutParams.rightMargin;
        } else {
          paramInt2 = marginLayoutParams.leftMargin;
        } 
        if (paramBoolean) {
          paramInt4 = marginLayoutParams.leftMargin;
        } else {
          paramInt4 = marginLayoutParams.rightMargin;
        } 
        paramInt2 = next(i, paramInt2, paramBoolean);
        paramInt2 = next(paramInt2 + positionChild(this.mClose, paramInt2, j, k, paramBoolean), paramInt4, paramBoolean);
      } 
    } 
    paramInt4 = paramInt2;
    if (this.mTitleLayout != null) {
      paramInt4 = paramInt2;
      if (this.mCustomView == null) {
        paramInt4 = paramInt2;
        if (this.mTitleLayout.getVisibility() != 8)
          paramInt4 = paramInt2 + positionChild((View)this.mTitleLayout, paramInt2, j, k, paramBoolean); 
      } 
    } 
    if (this.mCustomView != null)
      positionChild(this.mCustomView, paramInt4, j, k, paramBoolean); 
    if (paramBoolean) {
      paramInt1 = getPaddingLeft();
    } else {
      paramInt1 = paramInt3 - paramInt1 - getPaddingRight();
    } 
    if (this.mMenuView != null) {
      ActionMenuView actionMenuView = this.mMenuView;
      if (!paramBoolean) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      } 
      positionChild((View)actionMenuView, paramInt1, j, k, paramBoolean);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j;
    if (View.MeasureSpec.getMode(paramInt1) != 1073741824)
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)"); 
    if (View.MeasureSpec.getMode(paramInt2) == 0)
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\""); 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mContentHeight > 0) {
      j = this.mContentHeight;
    } else {
      j = View.MeasureSpec.getSize(paramInt2);
    } 
    int k = getPaddingTop() + getPaddingBottom();
    paramInt1 = i - getPaddingLeft() - getPaddingRight();
    int m = j - k;
    int n = View.MeasureSpec.makeMeasureSpec(m, -2147483648);
    paramInt2 = paramInt1;
    if (this.mClose != null) {
      paramInt1 = measureChildView(this.mClose, paramInt1, n, 0);
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
      paramInt2 = paramInt1 - marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
    } 
    paramInt1 = paramInt2;
    if (this.mMenuView != null) {
      paramInt1 = paramInt2;
      if (this.mMenuView.getParent() == this)
        paramInt1 = measureChildView((View)this.mMenuView, paramInt2, n, 0); 
    } 
    paramInt2 = paramInt1;
    if (this.mTitleLayout != null) {
      paramInt2 = paramInt1;
      if (this.mCustomView == null)
        if (this.mTitleOptional) {
          paramInt2 = View.MeasureSpec.makeMeasureSpec(0, 0);
          this.mTitleLayout.measure(paramInt2, n);
          int i1 = this.mTitleLayout.getMeasuredWidth();
          if (i1 <= paramInt1) {
            n = 1;
          } else {
            n = 0;
          } 
          paramInt2 = paramInt1;
          if (n != 0)
            paramInt2 = paramInt1 - i1; 
          LinearLayout linearLayout = this.mTitleLayout;
          if (n != 0) {
            paramInt1 = 0;
          } else {
            paramInt1 = 8;
          } 
          linearLayout.setVisibility(paramInt1);
        } else {
          paramInt2 = measureChildView((View)this.mTitleLayout, paramInt1, n, 0);
        }  
    } 
    if (this.mCustomView != null) {
      ViewGroup.LayoutParams layoutParams = this.mCustomView.getLayoutParams();
      if (layoutParams.width != -2) {
        paramInt1 = 1073741824;
      } else {
        paramInt1 = Integer.MIN_VALUE;
      } 
      if (layoutParams.width >= 0)
        paramInt2 = Math.min(layoutParams.width, paramInt2); 
      if (layoutParams.height != -2) {
        n = 1073741824;
      } else {
        n = Integer.MIN_VALUE;
      } 
      if (layoutParams.height >= 0)
        m = Math.min(layoutParams.height, m); 
      this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, paramInt1), View.MeasureSpec.makeMeasureSpec(m, n));
    } 
    if (this.mContentHeight <= 0) {
      j = 0;
      m = getChildCount();
      paramInt1 = 0;
      while (paramInt1 < m) {
        n = getChildAt(paramInt1).getMeasuredHeight() + k;
        paramInt2 = j;
        if (n > j)
          paramInt2 = n; 
        paramInt1++;
        j = paramInt2;
      } 
      setMeasuredDimension(i, j);
      return;
    } 
    setMeasuredDimension(i, j);
  }
  
  public void setContentHeight(int paramInt) {
    this.mContentHeight = paramInt;
  }
  
  public void setCustomView(View paramView) {
    if (this.mCustomView != null)
      removeView(this.mCustomView); 
    this.mCustomView = paramView;
    if (paramView != null && this.mTitleLayout != null) {
      removeView((View)this.mTitleLayout);
      this.mTitleLayout = null;
    } 
    if (paramView != null)
      addView(paramView); 
    requestLayout();
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    this.mSubtitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitleOptional(boolean paramBoolean) {
    if (paramBoolean != this.mTitleOptional)
      requestLayout(); 
    this.mTitleOptional = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  public boolean showOverflowMenu() {
    return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.showOverflowMenu() : false;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ActionBarContextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */