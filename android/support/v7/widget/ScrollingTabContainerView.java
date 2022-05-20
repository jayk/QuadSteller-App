package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ScrollingTabContainerView extends HorizontalScrollView implements AdapterView.OnItemSelectedListener {
  private static final int FADE_DURATION = 200;
  
  private static final String TAG = "ScrollingTabContainerView";
  
  private static final Interpolator sAlphaInterpolator = (Interpolator)new DecelerateInterpolator();
  
  private boolean mAllowCollapse;
  
  private int mContentHeight;
  
  int mMaxTabWidth;
  
  private int mSelectedTabIndex;
  
  int mStackedTabMaxWidth;
  
  private TabClickListener mTabClickListener;
  
  LinearLayoutCompat mTabLayout;
  
  Runnable mTabSelector;
  
  private Spinner mTabSpinner;
  
  protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
  
  protected ViewPropertyAnimatorCompat mVisibilityAnim;
  
  public ScrollingTabContainerView(Context paramContext) {
    super(paramContext);
    setHorizontalScrollBarEnabled(false);
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    setContentHeight(actionBarPolicy.getTabContainerHeight());
    this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
    this.mTabLayout = createTabLayout();
    addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
  }
  
  private Spinner createSpinner() {
    AppCompatSpinner appCompatSpinner = new AppCompatSpinner(getContext(), null, R.attr.actionDropDownStyle);
    appCompatSpinner.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
    appCompatSpinner.setOnItemSelectedListener(this);
    return appCompatSpinner;
  }
  
  private LinearLayoutCompat createTabLayout() {
    LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getContext(), null, R.attr.actionBarTabBarStyle);
    linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
    linearLayoutCompat.setGravity(17);
    linearLayoutCompat.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
    return linearLayoutCompat;
  }
  
  private boolean isCollapsed() {
    return (this.mTabSpinner != null && this.mTabSpinner.getParent() == this);
  }
  
  private void performCollapse() {
    if (!isCollapsed()) {
      if (this.mTabSpinner == null)
        this.mTabSpinner = createSpinner(); 
      removeView((View)this.mTabLayout);
      addView((View)this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
      if (this.mTabSpinner.getAdapter() == null)
        this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter()); 
      if (this.mTabSelector != null) {
        removeCallbacks(this.mTabSelector);
        this.mTabSelector = null;
      } 
      this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    } 
  }
  
  private boolean performExpand() {
    if (isCollapsed()) {
      removeView((View)this.mTabSpinner);
      addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
      setTabSelected(this.mTabSpinner.getSelectedItemPosition());
    } 
    return false;
  }
  
  public void addTab(ActionBar.Tab paramTab, int paramInt, boolean paramBoolean) {
    TabView tabView = createTabView(paramTab, false);
    this.mTabLayout.addView((View)tabView, paramInt, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0F));
    if (this.mTabSpinner != null)
      ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged(); 
    if (paramBoolean)
      tabView.setSelected(true); 
    if (this.mAllowCollapse)
      requestLayout(); 
  }
  
  public void addTab(ActionBar.Tab paramTab, boolean paramBoolean) {
    TabView tabView = createTabView(paramTab, false);
    this.mTabLayout.addView((View)tabView, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0F));
    if (this.mTabSpinner != null)
      ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged(); 
    if (paramBoolean)
      tabView.setSelected(true); 
    if (this.mAllowCollapse)
      requestLayout(); 
  }
  
  public void animateToTab(int paramInt) {
    final View tabView = this.mTabLayout.getChildAt(paramInt);
    if (this.mTabSelector != null)
      removeCallbacks(this.mTabSelector); 
    this.mTabSelector = new Runnable() {
        public void run() {
          int i = tabView.getLeft();
          int j = (ScrollingTabContainerView.this.getWidth() - tabView.getWidth()) / 2;
          ScrollingTabContainerView.this.smoothScrollTo(i - j, 0);
          ScrollingTabContainerView.this.mTabSelector = null;
        }
      };
    post(this.mTabSelector);
  }
  
  public void animateToVisibility(int paramInt) {
    if (this.mVisibilityAnim != null)
      this.mVisibilityAnim.cancel(); 
    if (paramInt == 0) {
      if (getVisibility() != 0)
        ViewCompat.setAlpha((View)this, 0.0F); 
      ViewPropertyAnimatorCompat viewPropertyAnimatorCompat1 = ViewCompat.animate((View)this).alpha(1.0F);
      viewPropertyAnimatorCompat1.setDuration(200L);
      viewPropertyAnimatorCompat1.setInterpolator(sAlphaInterpolator);
      viewPropertyAnimatorCompat1.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat1, paramInt));
      viewPropertyAnimatorCompat1.start();
      return;
    } 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate((View)this).alpha(0.0F);
    viewPropertyAnimatorCompat.setDuration(200L);
    viewPropertyAnimatorCompat.setInterpolator(sAlphaInterpolator);
    viewPropertyAnimatorCompat.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat, paramInt));
    viewPropertyAnimatorCompat.start();
  }
  
  TabView createTabView(ActionBar.Tab paramTab, boolean paramBoolean) {
    TabView tabView = new TabView(getContext(), paramTab, paramBoolean);
    if (paramBoolean) {
      tabView.setBackgroundDrawable(null);
      tabView.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, this.mContentHeight));
      return tabView;
    } 
    tabView.setFocusable(true);
    if (this.mTabClickListener == null)
      this.mTabClickListener = new TabClickListener(); 
    tabView.setOnClickListener(this.mTabClickListener);
    return tabView;
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.mTabSelector != null)
      post(this.mTabSelector); 
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(getContext());
    setContentHeight(actionBarPolicy.getTabContainerHeight());
    this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mTabSelector != null)
      removeCallbacks(this.mTabSelector); 
  }
  
  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    ((TabView)paramView).getTab().select();
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    boolean bool;
    int i = View.MeasureSpec.getMode(paramInt1);
    if (i == 1073741824) {
      bool = true;
    } else {
      bool = false;
    } 
    setFillViewport(bool);
    paramInt2 = this.mTabLayout.getChildCount();
    if (paramInt2 > 1 && (i == 1073741824 || i == Integer.MIN_VALUE)) {
      if (paramInt2 > 2) {
        this.mMaxTabWidth = (int)(View.MeasureSpec.getSize(paramInt1) * 0.4F);
      } else {
        this.mMaxTabWidth = View.MeasureSpec.getSize(paramInt1) / 2;
      } 
      this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
    } else {
      this.mMaxTabWidth = -1;
    } 
    i = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
    if (!bool && this.mAllowCollapse) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0) {
      this.mTabLayout.measure(0, i);
      if (this.mTabLayout.getMeasuredWidth() > View.MeasureSpec.getSize(paramInt1)) {
        performCollapse();
      } else {
        performExpand();
      } 
    } else {
      performExpand();
    } 
    paramInt2 = getMeasuredWidth();
    super.onMeasure(paramInt1, i);
    paramInt1 = getMeasuredWidth();
    if (bool && paramInt2 != paramInt1)
      setTabSelected(this.mSelectedTabIndex); 
  }
  
  public void onNothingSelected(AdapterView<?> paramAdapterView) {}
  
  public void removeAllTabs() {
    this.mTabLayout.removeAllViews();
    if (this.mTabSpinner != null)
      ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged(); 
    if (this.mAllowCollapse)
      requestLayout(); 
  }
  
  public void removeTabAt(int paramInt) {
    this.mTabLayout.removeViewAt(paramInt);
    if (this.mTabSpinner != null)
      ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged(); 
    if (this.mAllowCollapse)
      requestLayout(); 
  }
  
  public void setAllowCollapse(boolean paramBoolean) {
    this.mAllowCollapse = paramBoolean;
  }
  
  public void setContentHeight(int paramInt) {
    this.mContentHeight = paramInt;
    requestLayout();
  }
  
  public void setTabSelected(int paramInt) {
    this.mSelectedTabIndex = paramInt;
    int i = this.mTabLayout.getChildCount();
    for (byte b = 0; b < i; b++) {
      boolean bool;
      View view = this.mTabLayout.getChildAt(b);
      if (b == paramInt) {
        bool = true;
      } else {
        bool = false;
      } 
      view.setSelected(bool);
      if (bool)
        animateToTab(paramInt); 
    } 
    if (this.mTabSpinner != null && paramInt >= 0)
      this.mTabSpinner.setSelection(paramInt); 
  }
  
  public void updateTab(int paramInt) {
    ((TabView)this.mTabLayout.getChildAt(paramInt)).update();
    if (this.mTabSpinner != null)
      ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged(); 
    if (this.mAllowCollapse)
      requestLayout(); 
  }
  
  private class TabAdapter extends BaseAdapter {
    public int getCount() {
      return ScrollingTabContainerView.this.mTabLayout.getChildCount();
    }
    
    public Object getItem(int param1Int) {
      return ((ScrollingTabContainerView.TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(param1Int)).getTab();
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      if (param1View == null)
        return (View)ScrollingTabContainerView.this.createTabView((ActionBar.Tab)getItem(param1Int), true); 
      ((ScrollingTabContainerView.TabView)param1View).bindTab((ActionBar.Tab)getItem(param1Int));
      return param1View;
    }
  }
  
  private class TabClickListener implements View.OnClickListener {
    public void onClick(View param1View) {
      ((ScrollingTabContainerView.TabView)param1View).getTab().select();
      int i = ScrollingTabContainerView.this.mTabLayout.getChildCount();
      for (byte b = 0; b < i; b++) {
        boolean bool;
        View view = ScrollingTabContainerView.this.mTabLayout.getChildAt(b);
        if (view == param1View) {
          bool = true;
        } else {
          bool = false;
        } 
        view.setSelected(bool);
      } 
    }
  }
  
  private class TabView extends LinearLayoutCompat implements View.OnLongClickListener {
    private final int[] BG_ATTRS = new int[] { 16842964 };
    
    private View mCustomView;
    
    private ImageView mIconView;
    
    private ActionBar.Tab mTab;
    
    private TextView mTextView;
    
    public TabView(Context param1Context, ActionBar.Tab param1Tab, boolean param1Boolean) {
      super(param1Context, (AttributeSet)null, R.attr.actionBarTabStyle);
      this.mTab = param1Tab;
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(param1Context, null, this.BG_ATTRS, R.attr.actionBarTabStyle, 0);
      if (tintTypedArray.hasValue(0))
        setBackgroundDrawable(tintTypedArray.getDrawable(0)); 
      tintTypedArray.recycle();
      if (param1Boolean)
        setGravity(8388627); 
      update();
    }
    
    public void bindTab(ActionBar.Tab param1Tab) {
      this.mTab = param1Tab;
      update();
    }
    
    public ActionBar.Tab getTab() {
      return this.mTab;
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ActionBar.Tab.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      super.onInitializeAccessibilityNodeInfo(param1AccessibilityNodeInfo);
      if (Build.VERSION.SDK_INT >= 14)
        param1AccessibilityNodeInfo.setClassName(ActionBar.Tab.class.getName()); 
    }
    
    public boolean onLongClick(View param1View) {
      int[] arrayOfInt = new int[2];
      getLocationOnScreen(arrayOfInt);
      Context context = getContext();
      int i = getWidth();
      int j = getHeight();
      int k = (context.getResources().getDisplayMetrics()).widthPixels;
      Toast toast = Toast.makeText(context, this.mTab.getContentDescription(), 0);
      toast.setGravity(49, arrayOfInt[0] + i / 2 - k / 2, j);
      toast.show();
      return true;
    }
    
    public void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      if (ScrollingTabContainerView.this.mMaxTabWidth > 0 && getMeasuredWidth() > ScrollingTabContainerView.this.mMaxTabWidth)
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(ScrollingTabContainerView.this.mMaxTabWidth, 1073741824), param1Int2); 
    }
    
    public void setSelected(boolean param1Boolean) {
      boolean bool;
      if (isSelected() != param1Boolean) {
        bool = true;
      } else {
        bool = false;
      } 
      super.setSelected(param1Boolean);
      if (bool && param1Boolean)
        sendAccessibilityEvent(4); 
    }
    
    public void update() {
      ViewParent viewParent;
      boolean bool;
      ActionBar.Tab tab = this.mTab;
      View view = tab.getCustomView();
      if (view != null) {
        viewParent = view.getParent();
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
        return;
      } 
      if (this.mCustomView != null) {
        removeView(this.mCustomView);
        this.mCustomView = null;
      } 
      Drawable drawable = viewParent.getIcon();
      CharSequence charSequence = viewParent.getText();
      if (drawable != null) {
        if (this.mIconView == null) {
          AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
          LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(-2, -2);
          layoutParams.gravity = 16;
          appCompatImageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          addView((View)appCompatImageView, 0);
          this.mIconView = appCompatImageView;
        } 
        this.mIconView.setImageDrawable(drawable);
        this.mIconView.setVisibility(0);
      } else if (this.mIconView != null) {
        this.mIconView.setVisibility(8);
        this.mIconView.setImageDrawable(null);
      } 
      if (!TextUtils.isEmpty(charSequence)) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        if (this.mTextView == null) {
          AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, R.attr.actionBarTabTextStyle);
          appCompatTextView.setEllipsize(TextUtils.TruncateAt.END);
          LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(-2, -2);
          layoutParams.gravity = 16;
          appCompatTextView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          addView((View)appCompatTextView);
          this.mTextView = appCompatTextView;
        } 
        this.mTextView.setText(charSequence);
        this.mTextView.setVisibility(0);
      } else if (this.mTextView != null) {
        this.mTextView.setVisibility(8);
        this.mTextView.setText(null);
      } 
      if (this.mIconView != null)
        this.mIconView.setContentDescription(viewParent.getContentDescription()); 
      if (!bool && !TextUtils.isEmpty(viewParent.getContentDescription())) {
        setOnLongClickListener(this);
        return;
      } 
      setOnLongClickListener(null);
      setLongClickable(false);
    }
  }
  
  protected class VisibilityAnimListener implements ViewPropertyAnimatorListener {
    private boolean mCanceled = false;
    
    private int mFinalVisibility;
    
    public void onAnimationCancel(View param1View) {
      this.mCanceled = true;
    }
    
    public void onAnimationEnd(View param1View) {
      if (!this.mCanceled) {
        ScrollingTabContainerView.this.mVisibilityAnim = null;
        ScrollingTabContainerView.this.setVisibility(this.mFinalVisibility);
      } 
    }
    
    public void onAnimationStart(View param1View) {
      ScrollingTabContainerView.this.setVisibility(0);
      this.mCanceled = false;
    }
    
    public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimatorCompat param1ViewPropertyAnimatorCompat, int param1Int) {
      this.mFinalVisibility = param1Int;
      ScrollingTabContainerView.this.mVisibilityAnim = param1ViewPropertyAnimatorCompat;
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ScrollingTabContainerView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */