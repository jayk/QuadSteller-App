package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationMenuView extends ViewGroup implements MenuView {
  private static final Pools.Pool<BottomNavigationItemView> sItemPool = (Pools.Pool<BottomNavigationItemView>)new Pools.SynchronizedPool(5);
  
  private int mActiveButton = 0;
  
  private final int mActiveItemMaxWidth;
  
  private final BottomNavigationAnimationHelperBase mAnimationHelper;
  
  private BottomNavigationItemView[] mButtons;
  
  private final int mInactiveItemMaxWidth;
  
  private final int mInactiveItemMinWidth;
  
  private int mItemBackgroundRes;
  
  private final int mItemHeight;
  
  private ColorStateList mItemIconTint;
  
  private ColorStateList mItemTextColor;
  
  private MenuBuilder mMenu;
  
  private final View.OnClickListener mOnClickListener;
  
  private BottomNavigationPresenter mPresenter;
  
  private boolean mShiftingMode = true;
  
  private int[] mTempChildWidths;
  
  public BottomNavigationMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Resources resources = getResources();
    this.mInactiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
    this.mInactiveItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
    this.mActiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
    this.mItemHeight = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
    if (Build.VERSION.SDK_INT >= 14) {
      this.mAnimationHelper = new BottomNavigationAnimationHelperIcs();
    } else {
      this.mAnimationHelper = new BottomNavigationAnimationHelperBase();
    } 
    this.mOnClickListener = new View.OnClickListener() {
        public void onClick(View param1View) {
          BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView)param1View;
          int i = bottomNavigationItemView.getItemPosition();
          if (!BottomNavigationMenuView.this.mMenu.performItemAction((MenuItem)bottomNavigationItemView.getItemData(), BottomNavigationMenuView.this.mPresenter, 0))
            BottomNavigationMenuView.this.activateNewButton(i); 
        }
      };
    this.mTempChildWidths = new int[5];
  }
  
  private void activateNewButton(int paramInt) {
    if (this.mActiveButton != paramInt) {
      this.mAnimationHelper.beginDelayedTransition(this);
      this.mMenu.getItem(paramInt).setChecked(true);
      this.mActiveButton = paramInt;
    } 
  }
  
  private BottomNavigationItemView getNewItem() {
    BottomNavigationItemView bottomNavigationItemView1 = (BottomNavigationItemView)sItemPool.acquire();
    BottomNavigationItemView bottomNavigationItemView2 = bottomNavigationItemView1;
    if (bottomNavigationItemView1 == null)
      bottomNavigationItemView2 = new BottomNavigationItemView(getContext()); 
    return bottomNavigationItemView2;
  }
  
  public void buildMenuView() {
    if (this.mButtons != null)
      for (BottomNavigationItemView bottomNavigationItemView : this.mButtons)
        sItemPool.release(bottomNavigationItemView);  
    removeAllViews();
    if (this.mMenu.size() != 0) {
      boolean bool;
      this.mButtons = new BottomNavigationItemView[this.mMenu.size()];
      if (this.mMenu.size() > 3) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mShiftingMode = bool;
      for (byte b = 0; b < this.mMenu.size(); b++) {
        this.mPresenter.setUpdateSuspended(true);
        this.mMenu.getItem(b).setCheckable(true);
        this.mPresenter.setUpdateSuspended(false);
        BottomNavigationItemView bottomNavigationItemView = getNewItem();
        this.mButtons[b] = bottomNavigationItemView;
        bottomNavigationItemView.setIconTintList(this.mItemIconTint);
        bottomNavigationItemView.setTextColor(this.mItemTextColor);
        bottomNavigationItemView.setItemBackground(this.mItemBackgroundRes);
        bottomNavigationItemView.setShiftingMode(this.mShiftingMode);
        bottomNavigationItemView.initialize((MenuItemImpl)this.mMenu.getItem(b), 0);
        bottomNavigationItemView.setItemPosition(b);
        bottomNavigationItemView.setOnClickListener(this.mOnClickListener);
        addView((View)bottomNavigationItemView);
      } 
      this.mActiveButton = Math.min(this.mMenu.size() - 1, this.mActiveButton);
      this.mMenu.getItem(this.mActiveButton).setChecked(true);
    } 
  }
  
  @Nullable
  public ColorStateList getIconTintList() {
    return this.mItemIconTint;
  }
  
  public int getItemBackgroundRes() {
    return this.mItemBackgroundRes;
  }
  
  public ColorStateList getItemTextColor() {
    return this.mItemTextColor;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    paramInt3 -= paramInt1;
    paramInt4 -= paramInt2;
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
          view.layout(paramInt3 - paramInt2 - view.getMeasuredWidth(), 0, paramInt3 - paramInt2, paramInt4);
        } else {
          view.layout(paramInt2, 0, view.getMeasuredWidth() + paramInt2, paramInt4);
        } 
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    paramInt2 = View.MeasureSpec.getSize(paramInt1);
    int i = getChildCount();
    int j = View.MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
    if (this.mShiftingMode) {
      paramInt1 = i - 1;
      int k = Math.min(paramInt2 - this.mInactiveItemMinWidth * paramInt1, this.mActiveItemMaxWidth);
      int m = Math.min((paramInt2 - k) / paramInt1, this.mInactiveItemMaxWidth);
      paramInt2 = paramInt2 - k - m * paramInt1;
      paramInt1 = 0;
      while (paramInt1 < i) {
        int[] arrayOfInt = this.mTempChildWidths;
        if (paramInt1 == this.mActiveButton) {
          n = k;
        } else {
          n = m;
        } 
        arrayOfInt[paramInt1] = n;
        int n = paramInt2;
        if (paramInt2 > 0) {
          arrayOfInt = this.mTempChildWidths;
          arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
          n = paramInt2 - 1;
        } 
        paramInt1++;
        paramInt2 = n;
      } 
    } else {
      if (i == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = i;
      } 
      int k = Math.min(paramInt2 / paramInt1, this.mActiveItemMaxWidth);
      int m = paramInt2 - k * i;
      paramInt1 = 0;
      while (paramInt1 < i) {
        this.mTempChildWidths[paramInt1] = k;
        paramInt2 = m;
        if (m > 0) {
          int[] arrayOfInt = this.mTempChildWidths;
          arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
          paramInt2 = m - 1;
        } 
        paramInt1++;
        m = paramInt2;
      } 
    } 
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.mTempChildWidths[paramInt1], 1073741824), j);
        (view.getLayoutParams()).width = view.getMeasuredWidth();
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
    setMeasuredDimension(ViewCompat.resolveSizeAndState(paramInt2, View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824), 0), ViewCompat.resolveSizeAndState(this.mItemHeight, j, 0));
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.mItemIconTint = paramColorStateList;
    if (this.mButtons != null) {
      BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.mButtons;
      int i = arrayOfBottomNavigationItemView.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          arrayOfBottomNavigationItemView[b].setIconTintList(paramColorStateList);
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setItemBackgroundRes(int paramInt) {
    this.mItemBackgroundRes = paramInt;
    if (this.mButtons != null) {
      BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.mButtons;
      int i = arrayOfBottomNavigationItemView.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          arrayOfBottomNavigationItemView[b].setItemBackground(paramInt);
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.mItemTextColor = paramColorStateList;
    if (this.mButtons != null) {
      BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.mButtons;
      int i = arrayOfBottomNavigationItemView.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          arrayOfBottomNavigationItemView[b].setTextColor(paramColorStateList);
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setPresenter(BottomNavigationPresenter paramBottomNavigationPresenter) {
    this.mPresenter = paramBottomNavigationPresenter;
  }
  
  public void updateMenuView() {
    int i = this.mMenu.size();
    if (i != this.mButtons.length) {
      buildMenuView();
      return;
    } 
    byte b = 0;
    while (true) {
      if (b < i) {
        this.mPresenter.setUpdateSuspended(true);
        if (this.mMenu.getItem(b).isChecked())
          this.mActiveButton = b; 
        this.mButtons[b].initialize((MenuItemImpl)this.mMenu.getItem(b), 0);
        this.mPresenter.setUpdateSuspended(false);
        b++;
        continue;
      } 
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/internal/BottomNavigationMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */