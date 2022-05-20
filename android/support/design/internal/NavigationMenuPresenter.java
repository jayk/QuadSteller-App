package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenuPresenter implements MenuPresenter {
  private static final String STATE_ADAPTER = "android:menu:adapter";
  
  private static final String STATE_HEADER = "android:menu:header";
  
  private static final String STATE_HIERARCHY = "android:menu:list";
  
  NavigationMenuAdapter mAdapter;
  
  private MenuPresenter.Callback mCallback;
  
  LinearLayout mHeaderLayout;
  
  ColorStateList mIconTintList;
  
  private int mId;
  
  Drawable mItemBackground;
  
  LayoutInflater mLayoutInflater;
  
  MenuBuilder mMenu;
  
  private NavigationMenuView mMenuView;
  
  final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)param1View;
        NavigationMenuPresenter.this.setUpdateSuspended(true);
        MenuItemImpl menuItemImpl = navigationMenuItemView.getItemData();
        boolean bool = NavigationMenuPresenter.this.mMenu.performItemAction((MenuItem)menuItemImpl, NavigationMenuPresenter.this, 0);
        if (menuItemImpl != null && menuItemImpl.isCheckable() && bool)
          NavigationMenuPresenter.this.mAdapter.setCheckedItem(menuItemImpl); 
        NavigationMenuPresenter.this.setUpdateSuspended(false);
        NavigationMenuPresenter.this.updateMenuView(false);
      }
    };
  
  int mPaddingSeparator;
  
  private int mPaddingTopDefault;
  
  int mTextAppearance;
  
  boolean mTextAppearanceSet;
  
  ColorStateList mTextColor;
  
  public void addHeaderView(@NonNull View paramView) {
    this.mHeaderLayout.addView(paramView);
    this.mMenuView.setPadding(0, 0, 0, this.mMenuView.getPaddingBottom());
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public void dispatchApplyWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    int i = paramWindowInsetsCompat.getSystemWindowInsetTop();
    if (this.mPaddingTopDefault != i) {
      this.mPaddingTopDefault = i;
      if (this.mHeaderLayout.getChildCount() == 0)
        this.mMenuView.setPadding(0, this.mPaddingTopDefault, 0, this.mMenuView.getPaddingBottom()); 
    } 
    ViewCompat.dispatchApplyWindowInsets((View)this.mHeaderLayout, paramWindowInsetsCompat);
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public int getHeaderCount() {
    return this.mHeaderLayout.getChildCount();
  }
  
  public View getHeaderView(int paramInt) {
    return this.mHeaderLayout.getChildAt(paramInt);
  }
  
  public int getId() {
    return this.mId;
  }
  
  @Nullable
  public Drawable getItemBackground() {
    return this.mItemBackground;
  }
  
  @Nullable
  public ColorStateList getItemTextColor() {
    return this.mTextColor;
  }
  
  @Nullable
  public ColorStateList getItemTintList() {
    return this.mIconTintList;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    if (this.mMenuView == null) {
      this.mMenuView = (NavigationMenuView)this.mLayoutInflater.inflate(R.layout.design_navigation_menu, paramViewGroup, false);
      if (this.mAdapter == null)
        this.mAdapter = new NavigationMenuAdapter(); 
      this.mHeaderLayout = (LinearLayout)this.mLayoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.mMenuView, false);
      this.mMenuView.setAdapter(this.mAdapter);
    } 
    return this.mMenuView;
  }
  
  public View inflateHeaderView(@LayoutRes int paramInt) {
    View view = this.mLayoutInflater.inflate(paramInt, (ViewGroup)this.mHeaderLayout, false);
    addHeaderView(view);
    return view;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    this.mMenu = paramMenuBuilder;
    this.mPaddingSeparator = paramContext.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    if (this.mCallback != null)
      this.mCallback.onCloseMenu(paramMenuBuilder, paramBoolean); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof Bundle) {
      Bundle bundle1 = (Bundle)paramParcelable;
      SparseArray sparseArray2 = bundle1.getSparseParcelableArray("android:menu:list");
      if (sparseArray2 != null)
        this.mMenuView.restoreHierarchyState(sparseArray2); 
      Bundle bundle2 = bundle1.getBundle("android:menu:adapter");
      if (bundle2 != null)
        this.mAdapter.restoreInstanceState(bundle2); 
      SparseArray sparseArray1 = bundle1.getSparseParcelableArray("android:menu:header");
      if (sparseArray1 != null)
        this.mHeaderLayout.restoreHierarchyState(sparseArray1); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    if (Build.VERSION.SDK_INT >= 11) {
      Bundle bundle1 = new Bundle();
      if (this.mMenuView != null) {
        SparseArray sparseArray = new SparseArray();
        this.mMenuView.saveHierarchyState(sparseArray);
        bundle1.putSparseParcelableArray("android:menu:list", sparseArray);
      } 
      if (this.mAdapter != null)
        bundle1.putBundle("android:menu:adapter", this.mAdapter.createInstanceState()); 
      Bundle bundle2 = bundle1;
      if (this.mHeaderLayout != null) {
        SparseArray sparseArray = new SparseArray();
        this.mHeaderLayout.saveHierarchyState(sparseArray);
        bundle1.putSparseParcelableArray("android:menu:header", sparseArray);
        bundle2 = bundle1;
      } 
      return (Parcelable)bundle2;
    } 
    return null;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    return false;
  }
  
  public void removeHeaderView(@NonNull View paramView) {
    this.mHeaderLayout.removeView(paramView);
    if (this.mHeaderLayout.getChildCount() == 0)
      this.mMenuView.setPadding(0, this.mPaddingTopDefault, 0, this.mMenuView.getPaddingBottom()); 
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  public void setCheckedItem(MenuItemImpl paramMenuItemImpl) {
    this.mAdapter.setCheckedItem(paramMenuItemImpl);
  }
  
  public void setId(int paramInt) {
    this.mId = paramInt;
  }
  
  public void setItemBackground(@Nullable Drawable paramDrawable) {
    this.mItemBackground = paramDrawable;
    updateMenuView(false);
  }
  
  public void setItemIconTintList(@Nullable ColorStateList paramColorStateList) {
    this.mIconTintList = paramColorStateList;
    updateMenuView(false);
  }
  
  public void setItemTextAppearance(@StyleRes int paramInt) {
    this.mTextAppearance = paramInt;
    this.mTextAppearanceSet = true;
    updateMenuView(false);
  }
  
  public void setItemTextColor(@Nullable ColorStateList paramColorStateList) {
    this.mTextColor = paramColorStateList;
    updateMenuView(false);
  }
  
  public void setUpdateSuspended(boolean paramBoolean) {
    if (this.mAdapter != null)
      this.mAdapter.setUpdateSuspended(paramBoolean); 
  }
  
  public void updateMenuView(boolean paramBoolean) {
    if (this.mAdapter != null)
      this.mAdapter.update(); 
  }
  
  private static class HeaderViewHolder extends ViewHolder {
    public HeaderViewHolder(View param1View) {
      super(param1View);
    }
  }
  
  private class NavigationMenuAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
    
    private static final String STATE_CHECKED_ITEM = "android:menu:checked";
    
    private static final int VIEW_TYPE_HEADER = 3;
    
    private static final int VIEW_TYPE_NORMAL = 0;
    
    private static final int VIEW_TYPE_SEPARATOR = 2;
    
    private static final int VIEW_TYPE_SUBHEADER = 1;
    
    private MenuItemImpl mCheckedItem;
    
    private final ArrayList<NavigationMenuPresenter.NavigationMenuItem> mItems = new ArrayList<NavigationMenuPresenter.NavigationMenuItem>();
    
    private boolean mUpdateSuspended;
    
    NavigationMenuAdapter() {
      prepareMenuItems();
    }
    
    private void appendTransparentIconIfMissing(int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        ((NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(param1Int1)).needsEmptyIcon = true;
        param1Int1++;
      } 
    }
    
    private void prepareMenuItems() {
      if (!this.mUpdateSuspended) {
        this.mUpdateSuspended = true;
        this.mItems.clear();
        this.mItems.add(new NavigationMenuPresenter.NavigationMenuHeaderItem());
        int i = -1;
        int j = 0;
        boolean bool = false;
        byte b = 0;
        int k = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();
        while (b < k) {
          boolean bool1;
          int m;
          int n;
          MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.mMenu.getVisibleItems().get(b);
          if (menuItemImpl.isChecked())
            setCheckedItem(menuItemImpl); 
          if (menuItemImpl.isCheckable())
            menuItemImpl.setExclusiveCheckable(false); 
          if (menuItemImpl.hasSubMenu()) {
            SubMenu subMenu = menuItemImpl.getSubMenu();
            bool1 = bool;
            m = i;
            n = j;
            if (subMenu.hasVisibleItems()) {
              if (b != 0)
                this.mItems.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, 0)); 
              this.mItems.add(new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl));
              int i1 = 0;
              int i2 = this.mItems.size();
              n = 0;
              int i3 = subMenu.size();
              while (n < i3) {
                MenuItemImpl menuItemImpl1 = (MenuItemImpl)subMenu.getItem(n);
                m = i1;
                if (menuItemImpl1.isVisible()) {
                  m = i1;
                  if (!i1) {
                    m = i1;
                    if (menuItemImpl1.getIcon() != null)
                      m = 1; 
                  } 
                  if (menuItemImpl1.isCheckable())
                    menuItemImpl1.setExclusiveCheckable(false); 
                  if (menuItemImpl.isChecked())
                    setCheckedItem(menuItemImpl); 
                  this.mItems.add(new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl1));
                } 
                n++;
                i1 = m;
              } 
              bool1 = bool;
              m = i;
              n = j;
              if (i1 != 0) {
                appendTransparentIconIfMissing(i2, this.mItems.size());
                n = j;
                m = i;
                bool1 = bool;
              } 
            } 
          } else {
            int i1;
            m = menuItemImpl.getGroupId();
            if (m != i) {
              j = this.mItems.size();
              if (menuItemImpl.getIcon() != null) {
                bool = true;
              } else {
                bool = false;
              } 
              bool1 = bool;
              i1 = j;
              if (b != 0) {
                i1 = j + 1;
                this.mItems.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                bool1 = bool;
              } 
            } else {
              bool1 = bool;
              i1 = j;
              if (!bool) {
                bool1 = bool;
                i1 = j;
                if (menuItemImpl.getIcon() != null) {
                  bool1 = true;
                  appendTransparentIconIfMissing(j, this.mItems.size());
                  i1 = j;
                } 
              } 
            } 
            NavigationMenuPresenter.NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuPresenter.NavigationMenuTextItem(menuItemImpl);
            navigationMenuTextItem.needsEmptyIcon = bool1;
            this.mItems.add(navigationMenuTextItem);
            n = i1;
          } 
          b++;
          bool = bool1;
          i = m;
          j = n;
        } 
        this.mUpdateSuspended = false;
      } 
    }
    
    public Bundle createInstanceState() {
      Bundle bundle = new Bundle();
      if (this.mCheckedItem != null)
        bundle.putInt("android:menu:checked", this.mCheckedItem.getItemId()); 
      SparseArray sparseArray = new SparseArray();
      for (NavigationMenuPresenter.NavigationMenuItem navigationMenuItem : this.mItems) {
        if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
          MenuItemImpl menuItemImpl = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
          if (menuItemImpl != null) {
            View view = menuItemImpl.getActionView();
          } else {
            navigationMenuItem = null;
          } 
          if (navigationMenuItem != null) {
            ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
            navigationMenuItem.saveHierarchyState(parcelableSparseArray);
            sparseArray.put(menuItemImpl.getItemId(), parcelableSparseArray);
          } 
        } 
      } 
      bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
      return bundle;
    }
    
    public int getItemCount() {
      return this.mItems.size();
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public int getItemViewType(int param1Int) {
      NavigationMenuPresenter.NavigationMenuItem navigationMenuItem = this.mItems.get(param1Int);
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuSeparatorItem)
        return 2; 
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuHeaderItem)
        return 3; 
      if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem)
        return ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu() ? 1 : 0; 
      throw new RuntimeException("Unknown item type.");
    }
    
    public void onBindViewHolder(NavigationMenuPresenter.ViewHolder param1ViewHolder, int param1Int) {
      NavigationMenuPresenter.NavigationMenuTextItem navigationMenuTextItem;
      NavigationMenuItemView navigationMenuItemView;
      switch (getItemViewType(param1Int)) {
        default:
          return;
        case 0:
          navigationMenuItemView = (NavigationMenuItemView)param1ViewHolder.itemView;
          navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
          if (NavigationMenuPresenter.this.mTextAppearanceSet)
            navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.mTextAppearance); 
          if (NavigationMenuPresenter.this.mTextColor != null)
            navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor); 
          if (NavigationMenuPresenter.this.mItemBackground != null) {
            Drawable drawable = NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable();
          } else {
            param1ViewHolder = null;
          } 
          ViewCompat.setBackground((View)navigationMenuItemView, (Drawable)param1ViewHolder);
          navigationMenuTextItem = (NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(param1Int);
          navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
          navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
        case 1:
          ((TextView)((NavigationMenuPresenter.ViewHolder)navigationMenuTextItem).itemView).setText(((NavigationMenuPresenter.NavigationMenuTextItem)this.mItems.get(param1Int)).getMenuItem().getTitle());
        case 2:
          break;
      } 
      NavigationMenuPresenter.NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuPresenter.NavigationMenuSeparatorItem)this.mItems.get(param1Int);
      ((NavigationMenuPresenter.ViewHolder)navigationMenuTextItem).itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
    }
    
    public NavigationMenuPresenter.ViewHolder onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int) {
      switch (param1Int) {
        default:
          return null;
        case 0:
          return new NavigationMenuPresenter.NormalViewHolder(NavigationMenuPresenter.this.mLayoutInflater, param1ViewGroup, NavigationMenuPresenter.this.mOnClickListener);
        case 1:
          return new NavigationMenuPresenter.SubheaderViewHolder(NavigationMenuPresenter.this.mLayoutInflater, param1ViewGroup);
        case 2:
          return new NavigationMenuPresenter.SeparatorViewHolder(NavigationMenuPresenter.this.mLayoutInflater, param1ViewGroup);
        case 3:
          break;
      } 
      return new NavigationMenuPresenter.HeaderViewHolder((View)NavigationMenuPresenter.this.mHeaderLayout);
    }
    
    public void onViewRecycled(NavigationMenuPresenter.ViewHolder param1ViewHolder) {
      if (param1ViewHolder instanceof NavigationMenuPresenter.NormalViewHolder)
        ((NavigationMenuItemView)param1ViewHolder.itemView).recycle(); 
    }
    
    public void restoreInstanceState(Bundle param1Bundle) {
      int i = param1Bundle.getInt("android:menu:checked", 0);
      if (i != 0) {
        this.mUpdateSuspended = true;
        for (NavigationMenuPresenter.NavigationMenuItem navigationMenuItem : this.mItems) {
          if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
            MenuItemImpl menuItemImpl = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
            if (menuItemImpl != null && menuItemImpl.getItemId() == i) {
              setCheckedItem(menuItemImpl);
              break;
            } 
          } 
        } 
        this.mUpdateSuspended = false;
        prepareMenuItems();
      } 
      SparseArray sparseArray = param1Bundle.getSparseParcelableArray("android:menu:action_views");
      for (NavigationMenuPresenter.NavigationMenuItem navigationMenuItem : this.mItems) {
        if (navigationMenuItem instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
          MenuItemImpl menuItemImpl = ((NavigationMenuPresenter.NavigationMenuTextItem)navigationMenuItem).getMenuItem();
          if (menuItemImpl != null) {
            View view = menuItemImpl.getActionView();
          } else {
            navigationMenuItem = null;
          } 
          if (navigationMenuItem != null)
            navigationMenuItem.restoreHierarchyState((SparseArray)sparseArray.get(menuItemImpl.getItemId())); 
        } 
      } 
    }
    
    public void setCheckedItem(MenuItemImpl param1MenuItemImpl) {
      if (this.mCheckedItem != param1MenuItemImpl && param1MenuItemImpl.isCheckable()) {
        if (this.mCheckedItem != null)
          this.mCheckedItem.setChecked(false); 
        this.mCheckedItem = param1MenuItemImpl;
        param1MenuItemImpl.setChecked(true);
      } 
    }
    
    public void setUpdateSuspended(boolean param1Boolean) {
      this.mUpdateSuspended = param1Boolean;
    }
    
    public void update() {
      prepareMenuItems();
      notifyDataSetChanged();
    }
  }
  
  private static class NavigationMenuHeaderItem implements NavigationMenuItem {}
  
  private static interface NavigationMenuItem {}
  
  private static class NavigationMenuSeparatorItem implements NavigationMenuItem {
    private final int mPaddingBottom;
    
    private final int mPaddingTop;
    
    public NavigationMenuSeparatorItem(int param1Int1, int param1Int2) {
      this.mPaddingTop = param1Int1;
      this.mPaddingBottom = param1Int2;
    }
    
    public int getPaddingBottom() {
      return this.mPaddingBottom;
    }
    
    public int getPaddingTop() {
      return this.mPaddingTop;
    }
  }
  
  private static class NavigationMenuTextItem implements NavigationMenuItem {
    private final MenuItemImpl mMenuItem;
    
    boolean needsEmptyIcon;
    
    NavigationMenuTextItem(MenuItemImpl param1MenuItemImpl) {
      this.mMenuItem = param1MenuItemImpl;
    }
    
    public MenuItemImpl getMenuItem() {
      return this.mMenuItem;
    }
  }
  
  private static class NormalViewHolder extends ViewHolder {
    public NormalViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup, View.OnClickListener param1OnClickListener) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item, param1ViewGroup, false));
      this.itemView.setOnClickListener(param1OnClickListener);
    }
  }
  
  private static class SeparatorViewHolder extends ViewHolder {
    public SeparatorViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item_separator, param1ViewGroup, false));
    }
  }
  
  private static class SubheaderViewHolder extends ViewHolder {
    public SubheaderViewHolder(LayoutInflater param1LayoutInflater, ViewGroup param1ViewGroup) {
      super(param1LayoutInflater.inflate(R.layout.design_navigation_item_subheader, param1ViewGroup, false));
    }
  }
  
  private static abstract class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View param1View) {
      super(param1View);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/internal/NavigationMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */