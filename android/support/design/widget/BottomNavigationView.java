package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BottomNavigationView extends FrameLayout {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private OnNavigationItemSelectedListener mListener;
  
  private final MenuBuilder mMenu;
  
  private MenuInflater mMenuInflater;
  
  private final BottomNavigationMenuView mMenuView;
  
  private final BottomNavigationPresenter mPresenter = new BottomNavigationPresenter();
  
  public BottomNavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme(paramContext);
    this.mMenu = (MenuBuilder)new BottomNavigationMenu(paramContext);
    this.mMenuView = new BottomNavigationMenuView(paramContext);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
    layoutParams.gravity = 17;
    this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.mPresenter.setBottomNavigationMenuView(this.mMenuView);
    this.mMenuView.setPresenter(this.mPresenter);
    this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter);
    this.mPresenter.initForMenu(getContext(), this.mMenu);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.BottomNavigationView, paramInt, R.style.Widget_Design_BottomNavigationView);
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
      this.mMenuView.setIconTintList(tintTypedArray.getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
    } else {
      this.mMenuView.setIconTintList(createDefaultColorStateList(16842808));
    } 
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
      this.mMenuView.setItemTextColor(tintTypedArray.getColorStateList(R.styleable.BottomNavigationView_itemTextColor));
    } else {
      this.mMenuView.setItemTextColor(createDefaultColorStateList(16842808));
    } 
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_elevation))
      ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0)); 
    paramInt = tintTypedArray.getResourceId(R.styleable.BottomNavigationView_itemBackground, 0);
    this.mMenuView.setItemBackgroundRes(paramInt);
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.BottomNavigationView_menu, 0)); 
    tintTypedArray.recycle();
    addView((View)this.mMenuView, (ViewGroup.LayoutParams)layoutParams);
    if (Build.VERSION.SDK_INT < 21)
      addCompatibilityTopDivider(paramContext); 
    this.mMenu.setCallback(new MenuBuilder.Callback() {
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            return (BottomNavigationView.this.mListener != null && !BottomNavigationView.this.mListener.onNavigationItemSelected(param1MenuItem));
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
  }
  
  private void addCompatibilityTopDivider(Context paramContext) {
    View view = new View(paramContext);
    view.setBackgroundColor(ContextCompat.getColor(paramContext, R.color.design_bottom_navigation_shadow_color));
    view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
    addView(view);
  }
  
  private ColorStateList createDefaultColorStateList(int paramInt) {
    ColorStateList colorStateList;
    int[] arrayOfInt = null;
    TypedValue typedValue = new TypedValue();
    if (getContext().getTheme().resolveAttribute(paramInt, typedValue, true)) {
      ColorStateList colorStateList1 = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
      if (getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true)) {
        int i = typedValue.data;
        int j = colorStateList1.getDefaultColor();
        int[] arrayOfInt2 = DISABLED_STATE_SET;
        int[] arrayOfInt1 = CHECKED_STATE_SET;
        arrayOfInt = EMPTY_STATE_SET;
        paramInt = colorStateList1.getColorForState(DISABLED_STATE_SET, j);
        colorStateList = new ColorStateList(new int[][] { arrayOfInt2, arrayOfInt1, arrayOfInt }, new int[] { paramInt, i, j });
      } 
    } 
    return colorStateList;
  }
  
  private MenuInflater getMenuInflater() {
    if (this.mMenuInflater == null)
      this.mMenuInflater = (MenuInflater)new SupportMenuInflater(getContext()); 
    return this.mMenuInflater;
  }
  
  @DrawableRes
  public int getItemBackgroundResource() {
    return this.mMenuView.getItemBackgroundRes();
  }
  
  @Nullable
  public ColorStateList getItemIconTintList() {
    return this.mMenuView.getIconTintList();
  }
  
  @Nullable
  public ColorStateList getItemTextColor() {
    return this.mMenuView.getItemTextColor();
  }
  
  public int getMaxItemCount() {
    return 5;
  }
  
  @NonNull
  public Menu getMenu() {
    return (Menu)this.mMenu;
  }
  
  public void inflateMenu(int paramInt) {
    this.mPresenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.mMenu);
    this.mPresenter.setUpdateSuspended(false);
    this.mPresenter.updateMenuView(true);
  }
  
  public void setItemBackgroundResource(@DrawableRes int paramInt) {
    this.mMenuView.setItemBackgroundRes(paramInt);
  }
  
  public void setItemIconTintList(@Nullable ColorStateList paramColorStateList) {
    this.mMenuView.setIconTintList(paramColorStateList);
  }
  
  public void setItemTextColor(@Nullable ColorStateList paramColorStateList) {
    this.mMenuView.setItemTextColor(paramColorStateList);
  }
  
  public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    this.mListener = paramOnNavigationItemSelectedListener;
  }
  
  public static interface OnNavigationItemSelectedListener {
    boolean onNavigationItemSelected(@NonNull MenuItem param1MenuItem);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/BottomNavigationView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */