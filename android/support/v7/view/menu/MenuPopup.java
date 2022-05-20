package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener {
  private Rect mEpicenterBounds;
  
  protected static int measureIndividualMenuWidth(ListAdapter paramListAdapter, ViewGroup paramViewGroup, Context paramContext, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aconst_null
    //   4: astore #5
    //   6: iconst_0
    //   7: istore #6
    //   9: iconst_0
    //   10: iconst_0
    //   11: invokestatic makeMeasureSpec : (II)I
    //   14: istore #7
    //   16: iconst_0
    //   17: iconst_0
    //   18: invokestatic makeMeasureSpec : (II)I
    //   21: istore #8
    //   23: aload_0
    //   24: invokeinterface getCount : ()I
    //   29: istore #9
    //   31: iconst_0
    //   32: istore #10
    //   34: aload_1
    //   35: astore #11
    //   37: aload #5
    //   39: astore_1
    //   40: iload #10
    //   42: iload #9
    //   44: if_icmpge -> 160
    //   47: aload_0
    //   48: iload #10
    //   50: invokeinterface getItemViewType : (I)I
    //   55: istore #12
    //   57: iload #6
    //   59: istore #13
    //   61: iload #12
    //   63: iload #6
    //   65: if_icmpeq -> 74
    //   68: iload #12
    //   70: istore #13
    //   72: aconst_null
    //   73: astore_1
    //   74: aload #11
    //   76: astore #5
    //   78: aload #11
    //   80: ifnonnull -> 93
    //   83: new android/widget/FrameLayout
    //   86: dup
    //   87: aload_2
    //   88: invokespecial <init> : (Landroid/content/Context;)V
    //   91: astore #5
    //   93: aload_0
    //   94: iload #10
    //   96: aload_1
    //   97: aload #5
    //   99: invokeinterface getView : (ILandroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   104: astore_1
    //   105: aload_1
    //   106: iload #7
    //   108: iload #8
    //   110: invokevirtual measure : (II)V
    //   113: aload_1
    //   114: invokevirtual getMeasuredWidth : ()I
    //   117: istore #6
    //   119: iload #6
    //   121: iload_3
    //   122: if_icmplt -> 127
    //   125: iload_3
    //   126: ireturn
    //   127: iload #4
    //   129: istore #12
    //   131: iload #6
    //   133: iload #4
    //   135: if_icmple -> 142
    //   138: iload #6
    //   140: istore #12
    //   142: iinc #10, 1
    //   145: iload #13
    //   147: istore #6
    //   149: iload #12
    //   151: istore #4
    //   153: aload #5
    //   155: astore #11
    //   157: goto -> 40
    //   160: iload #4
    //   162: istore_3
    //   163: goto -> 125
  }
  
  protected static boolean shouldPreserveIconSpacing(MenuBuilder paramMenuBuilder) {
    boolean bool = false;
    int i = paramMenuBuilder.size();
    for (byte b = 0;; b++) {
      boolean bool1 = bool;
      if (b < i) {
        MenuItem menuItem = paramMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null)
          return true; 
      } else {
        return bool1;
      } 
    } 
  }
  
  protected static MenuAdapter toMenuAdapter(ListAdapter paramListAdapter) {
    return (paramListAdapter instanceof HeaderViewListAdapter) ? (MenuAdapter)((HeaderViewListAdapter)paramListAdapter).getWrappedAdapter() : (MenuAdapter)paramListAdapter;
  }
  
  public abstract void addMenu(MenuBuilder paramMenuBuilder);
  
  protected boolean closeMenuOnSubMenuOpened() {
    return true;
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public Rect getEpicenterBounds() {
    return this.mEpicenterBounds;
  }
  
  public int getId() {
    return 0;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    throw new UnsupportedOperationException("MenuPopups manage their own views");
  }
  
  public void initForMenu(@NonNull Context paramContext, @Nullable MenuBuilder paramMenuBuilder) {}
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    ListAdapter listAdapter = (ListAdapter)paramAdapterView.getAdapter();
    MenuBuilder menuBuilder = (toMenuAdapter(listAdapter)).mAdapterMenu;
    MenuItem menuItem = (MenuItem)listAdapter.getItem(paramInt);
    if (closeMenuOnSubMenuOpened()) {
      paramInt = 0;
    } else {
      paramInt = 4;
    } 
    menuBuilder.performItemAction(menuItem, this, paramInt);
  }
  
  public abstract void setAnchorView(View paramView);
  
  public void setEpicenterBounds(Rect paramRect) {
    this.mEpicenterBounds = paramRect;
  }
  
  public abstract void setForceShowIcon(boolean paramBoolean);
  
  public abstract void setGravity(int paramInt);
  
  public abstract void setHorizontalOffset(int paramInt);
  
  public abstract void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener);
  
  public abstract void setShowTitle(boolean paramBoolean);
  
  public abstract void setVerticalOffset(int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/view/menu/MenuPopup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */