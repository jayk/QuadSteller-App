package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NavigationView extends ScrimInsetsFrameLayout {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
  
  OnNavigationItemSelectedListener mListener;
  
  private int mMaxWidth;
  
  private final NavigationMenu mMenu;
  
  private MenuInflater mMenuInflater;
  
  private final NavigationMenuPresenter mPresenter;
  
  public NavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ColorStateList colorStateList1;
    ColorStateList colorStateList2;
    this.mPresenter = new NavigationMenuPresenter();
    ThemeUtils.checkAppCompatTheme(paramContext);
    this.mMenu = new NavigationMenu(paramContext);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.NavigationView, paramInt, R.style.Widget_Design_NavigationView);
    ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.NavigationView_android_background));
    if (tintTypedArray.hasValue(R.styleable.NavigationView_elevation))
      ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0)); 
    ViewCompat.setFitsSystemWindows((View)this, tintTypedArray.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
    this.mMaxWidth = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemIconTint)) {
      colorStateList2 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemIconTint);
    } else {
      colorStateList2 = createDefaultColorStateList(16842808);
    } 
    boolean bool = false;
    paramInt = 0;
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
      paramInt = tintTypedArray.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
      bool = true;
    } 
    paramAttributeSet = null;
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextColor))
      colorStateList1 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemTextColor); 
    ColorStateList colorStateList3 = colorStateList1;
    if (!bool) {
      colorStateList3 = colorStateList1;
      if (colorStateList1 == null)
        colorStateList3 = createDefaultColorStateList(16842806); 
    } 
    Drawable drawable = tintTypedArray.getDrawable(R.styleable.NavigationView_itemBackground);
    this.mMenu.setCallback(new MenuBuilder.Callback() {
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            return (NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(param1MenuItem));
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
    this.mPresenter.setId(1);
    this.mPresenter.initForMenu(paramContext, (MenuBuilder)this.mMenu);
    this.mPresenter.setItemIconTintList(colorStateList2);
    if (bool)
      this.mPresenter.setItemTextAppearance(paramInt); 
    this.mPresenter.setItemTextColor(colorStateList3);
    this.mPresenter.setItemBackground(drawable);
    this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter);
    addView((View)this.mPresenter.getMenuView((ViewGroup)this));
    if (tintTypedArray.hasValue(R.styleable.NavigationView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationView_menu, 0)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_headerLayout))
      inflateHeaderView(tintTypedArray.getResourceId(R.styleable.NavigationView_headerLayout, 0)); 
    tintTypedArray.recycle();
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
  
  public void addHeaderView(@NonNull View paramView) {
    this.mPresenter.addHeaderView(paramView);
  }
  
  public int getHeaderCount() {
    return this.mPresenter.getHeaderCount();
  }
  
  public View getHeaderView(int paramInt) {
    return this.mPresenter.getHeaderView(paramInt);
  }
  
  @Nullable
  public Drawable getItemBackground() {
    return this.mPresenter.getItemBackground();
  }
  
  @Nullable
  public ColorStateList getItemIconTintList() {
    return this.mPresenter.getItemTintList();
  }
  
  @Nullable
  public ColorStateList getItemTextColor() {
    return this.mPresenter.getItemTextColor();
  }
  
  public Menu getMenu() {
    return (Menu)this.mMenu;
  }
  
  public View inflateHeaderView(@LayoutRes int paramInt) {
    return this.mPresenter.inflateHeaderView(paramInt);
  }
  
  public void inflateMenu(int paramInt) {
    this.mPresenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.mMenu);
    this.mPresenter.setUpdateSuspended(false);
    this.mPresenter.updateMenuView(false);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void onInsetsChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    this.mPresenter.dispatchApplyWindowInsets(paramWindowInsetsCompat);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = paramInt1;
    switch (View.MeasureSpec.getMode(paramInt1)) {
      default:
        i = paramInt1;
      case 1073741824:
        super.onMeasure(i, paramInt2);
        return;
      case -2147483648:
        i = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(paramInt1), this.mMaxWidth), 1073741824);
      case 0:
        break;
    } 
    i = View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mMenu.restorePresenterStates(savedState.menuState);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.menuState = new Bundle();
    this.mMenu.savePresenterStates(savedState.menuState);
    return (Parcelable)savedState;
  }
  
  public void removeHeaderView(@NonNull View paramView) {
    this.mPresenter.removeHeaderView(paramView);
  }
  
  public void setCheckedItem(@IdRes int paramInt) {
    MenuItem menuItem = this.mMenu.findItem(paramInt);
    if (menuItem != null)
      this.mPresenter.setCheckedItem((MenuItemImpl)menuItem); 
  }
  
  public void setItemBackground(@Nullable Drawable paramDrawable) {
    this.mPresenter.setItemBackground(paramDrawable);
  }
  
  public void setItemBackgroundResource(@DrawableRes int paramInt) {
    setItemBackground(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setItemIconTintList(@Nullable ColorStateList paramColorStateList) {
    this.mPresenter.setItemIconTintList(paramColorStateList);
  }
  
  public void setItemTextAppearance(@StyleRes int paramInt) {
    this.mPresenter.setItemTextAppearance(paramInt);
  }
  
  public void setItemTextColor(@Nullable ColorStateList paramColorStateList) {
    this.mPresenter.setItemTextColor(paramColorStateList);
  }
  
  public void setNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    this.mListener = paramOnNavigationItemSelectedListener;
  }
  
  public static interface OnNavigationItemSelectedListener {
    boolean onNavigationItemSelected(@NonNull MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public NavigationView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new NavigationView.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public NavigationView.SavedState[] newArray(int param2Int) {
            return new NavigationView.SavedState[param2Int];
          }
        });
    
    public Bundle menuState;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.menuState = param1Parcel.readBundle(param1ClassLoader);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(@NonNull Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeBundle(this.menuState);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public NavigationView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new NavigationView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public NavigationView.SavedState[] newArray(int param1Int) {
      return new NavigationView.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/NavigationView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */