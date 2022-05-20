package android.support.v7.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MenuBuilder implements SupportMenu {
  private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
  
  private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
  
  private static final String PRESENTER_KEY = "android:menu:presenters";
  
  private static final String TAG = "MenuBuilder";
  
  private static final int[] sCategoryToOrder = new int[] { 1, 4, 5, 3, 2, 0 };
  
  private ArrayList<MenuItemImpl> mActionItems;
  
  private Callback mCallback;
  
  private final Context mContext;
  
  private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
  
  private int mDefaultShowAsAction = 0;
  
  private MenuItemImpl mExpandedItem;
  
  private SparseArray<Parcelable> mFrozenViewStates;
  
  Drawable mHeaderIcon;
  
  CharSequence mHeaderTitle;
  
  View mHeaderView;
  
  private boolean mIsActionItemsStale;
  
  private boolean mIsClosing = false;
  
  private boolean mIsVisibleItemsStale;
  
  private ArrayList<MenuItemImpl> mItems;
  
  private boolean mItemsChangedWhileDispatchPrevented = false;
  
  private ArrayList<MenuItemImpl> mNonActionItems;
  
  private boolean mOptionalIconsVisible = false;
  
  private boolean mOverrideVisibleItems;
  
  private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters = new CopyOnWriteArrayList<WeakReference<MenuPresenter>>();
  
  private boolean mPreventDispatchingItemsChanged = false;
  
  private boolean mQwertyMode;
  
  private final Resources mResources;
  
  private boolean mShortcutsVisible;
  
  private boolean mStructureChangedWhileDispatchPrevented = false;
  
  private ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList<MenuItemImpl>();
  
  private ArrayList<MenuItemImpl> mVisibleItems;
  
  public MenuBuilder(Context paramContext) {
    this.mContext = paramContext;
    this.mResources = paramContext.getResources();
    this.mItems = new ArrayList<MenuItemImpl>();
    this.mVisibleItems = new ArrayList<MenuItemImpl>();
    this.mIsVisibleItemsStale = true;
    this.mActionItems = new ArrayList<MenuItemImpl>();
    this.mNonActionItems = new ArrayList<MenuItemImpl>();
    this.mIsActionItemsStale = true;
    setShortcutsVisibleInner(true);
  }
  
  private MenuItemImpl createNewMenuItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence, int paramInt5) {
    return new MenuItemImpl(this, paramInt1, paramInt2, paramInt3, paramInt4, paramCharSequence, paramInt5);
  }
  
  private void dispatchPresenterUpdate(boolean paramBoolean) {
    if (!this.mPresenters.isEmpty()) {
      stopDispatchingItemsChanged();
      for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        menuPresenter.updateMenuView(paramBoolean);
      } 
      startDispatchingItemsChanged();
    } 
  }
  
  private void dispatchRestoreInstanceState(Bundle paramBundle) {
    SparseArray sparseArray = paramBundle.getSparseParcelableArray("android:menu:presenters");
    if (sparseArray != null && !this.mPresenters.isEmpty()) {
      Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
      while (true) {
        if (iterator.hasNext()) {
          WeakReference<MenuPresenter> weakReference = iterator.next();
          MenuPresenter menuPresenter = weakReference.get();
          if (menuPresenter == null) {
            this.mPresenters.remove(weakReference);
            continue;
          } 
          int i = menuPresenter.getId();
          if (i > 0) {
            Parcelable parcelable = (Parcelable)sparseArray.get(i);
            if (parcelable != null)
              menuPresenter.onRestoreInstanceState(parcelable); 
          } 
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void dispatchSaveInstanceState(Bundle paramBundle) {
    if (!this.mPresenters.isEmpty()) {
      SparseArray sparseArray = new SparseArray();
      for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        int i = menuPresenter.getId();
        if (i > 0) {
          Parcelable parcelable = menuPresenter.onSaveInstanceState();
          if (parcelable != null)
            sparseArray.put(i, parcelable); 
        } 
      } 
      paramBundle.putSparseParcelableArray("android:menu:presenters", sparseArray);
    } 
  }
  
  private boolean dispatchSubMenuSelected(SubMenuBuilder paramSubMenuBuilder, MenuPresenter paramMenuPresenter) {
    if (this.mPresenters.isEmpty())
      return false; 
    boolean bool = false;
    if (paramMenuPresenter != null)
      bool = paramMenuPresenter.onSubMenuSelected(paramSubMenuBuilder); 
    Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
    while (true) {
      boolean bool1 = bool;
      if (iterator.hasNext()) {
        WeakReference<MenuPresenter> weakReference = iterator.next();
        paramMenuPresenter = weakReference.get();
        if (paramMenuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        if (!bool)
          bool = paramMenuPresenter.onSubMenuSelected(paramSubMenuBuilder); 
        continue;
      } 
      return bool1;
    } 
  }
  
  private static int findInsertIndex(ArrayList<MenuItemImpl> paramArrayList, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: iconst_1
    //   5: isub
    //   6: istore_2
    //   7: iload_2
    //   8: iflt -> 38
    //   11: aload_0
    //   12: iload_2
    //   13: invokevirtual get : (I)Ljava/lang/Object;
    //   16: checkcast android/support/v7/view/menu/MenuItemImpl
    //   19: invokevirtual getOrdering : ()I
    //   22: iload_1
    //   23: if_icmpgt -> 32
    //   26: iload_2
    //   27: iconst_1
    //   28: iadd
    //   29: istore_1
    //   30: iload_1
    //   31: ireturn
    //   32: iinc #2, -1
    //   35: goto -> 7
    //   38: iconst_0
    //   39: istore_1
    //   40: goto -> 30
  }
  
  private static int getOrdering(int paramInt) {
    int i = (0xFFFF0000 & paramInt) >> 16;
    if (i < 0 || i >= sCategoryToOrder.length)
      throw new IllegalArgumentException("order does not contain a valid category."); 
    return sCategoryToOrder[i] << 16 | 0xFFFF & paramInt;
  }
  
  private void removeItemAtInt(int paramInt, boolean paramBoolean) {
    if (paramInt >= 0 && paramInt < this.mItems.size()) {
      this.mItems.remove(paramInt);
      if (paramBoolean)
        onItemsChanged(true); 
    } 
  }
  
  private void setHeaderInternal(int paramInt1, CharSequence paramCharSequence, int paramInt2, Drawable paramDrawable, View paramView) {
    Resources resources = getResources();
    if (paramView != null) {
      this.mHeaderView = paramView;
      this.mHeaderTitle = null;
      this.mHeaderIcon = null;
    } else {
      if (paramInt1 > 0) {
        this.mHeaderTitle = resources.getText(paramInt1);
      } else if (paramCharSequence != null) {
        this.mHeaderTitle = paramCharSequence;
      } 
      if (paramInt2 > 0) {
        this.mHeaderIcon = ContextCompat.getDrawable(getContext(), paramInt2);
      } else if (paramDrawable != null) {
        this.mHeaderIcon = paramDrawable;
      } 
      this.mHeaderView = null;
    } 
    onItemsChanged(false);
  }
  
  private void setShortcutsVisibleInner(boolean paramBoolean) {
    boolean bool = true;
    if (paramBoolean && (this.mResources.getConfiguration()).keyboard != 1 && this.mResources.getBoolean(R.bool.abc_config_showMenuShortcutsWhenKeyboardPresent)) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    } 
    this.mShortcutsVisible = paramBoolean;
  }
  
  public MenuItem add(int paramInt) {
    return addInternal(0, 0, 0, this.mResources.getString(paramInt));
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return addInternal(paramInt1, paramInt2, paramInt3, this.mResources.getString(paramInt4));
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
    return addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
  }
  
  public MenuItem add(CharSequence paramCharSequence) {
    return addInternal(0, 0, 0, paramCharSequence);
  }
  
  public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3, ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt4, MenuItem[] paramArrayOfMenuItem) {
    byte b;
    PackageManager packageManager = this.mContext.getPackageManager();
    List<ResolveInfo> list = packageManager.queryIntentActivityOptions(paramComponentName, paramArrayOfIntent, paramIntent, 0);
    if (list != null) {
      b = list.size();
    } else {
      b = 0;
    } 
    if ((paramInt4 & 0x1) == 0)
      removeGroup(paramInt1); 
    for (paramInt4 = 0; paramInt4 < b; paramInt4++) {
      ResolveInfo resolveInfo = list.get(paramInt4);
      if (resolveInfo.specificIndex < 0) {
        intent = paramIntent;
      } else {
        intent = paramArrayOfIntent[resolveInfo.specificIndex];
      } 
      Intent intent = new Intent(intent);
      intent.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
      MenuItem menuItem = add(paramInt1, paramInt2, paramInt3, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent);
      if (paramArrayOfMenuItem != null && resolveInfo.specificIndex >= 0)
        paramArrayOfMenuItem[resolveInfo.specificIndex] = menuItem; 
    } 
    return b;
  }
  
  protected MenuItem addInternal(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
    int i = getOrdering(paramInt3);
    MenuItemImpl menuItemImpl = createNewMenuItem(paramInt1, paramInt2, paramInt3, i, paramCharSequence, this.mDefaultShowAsAction);
    if (this.mCurrentMenuInfo != null)
      menuItemImpl.setMenuInfo(this.mCurrentMenuInfo); 
    this.mItems.add(findInsertIndex(this.mItems, i), menuItemImpl);
    onItemsChanged(true);
    return (MenuItem)menuItemImpl;
  }
  
  public void addMenuPresenter(MenuPresenter paramMenuPresenter) {
    addMenuPresenter(paramMenuPresenter, this.mContext);
  }
  
  public void addMenuPresenter(MenuPresenter paramMenuPresenter, Context paramContext) {
    this.mPresenters.add(new WeakReference<MenuPresenter>(paramMenuPresenter));
    paramMenuPresenter.initForMenu(paramContext, this);
    this.mIsActionItemsStale = true;
  }
  
  public SubMenu addSubMenu(int paramInt) {
    return addSubMenu(0, 0, 0, this.mResources.getString(paramInt));
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return addSubMenu(paramInt1, paramInt2, paramInt3, this.mResources.getString(paramInt4));
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
    MenuItemImpl menuItemImpl = (MenuItemImpl)addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
    SubMenuBuilder subMenuBuilder = new SubMenuBuilder(this.mContext, this, menuItemImpl);
    menuItemImpl.setSubMenu(subMenuBuilder);
    return subMenuBuilder;
  }
  
  public SubMenu addSubMenu(CharSequence paramCharSequence) {
    return addSubMenu(0, 0, 0, paramCharSequence);
  }
  
  public void changeMenuMode() {
    if (this.mCallback != null)
      this.mCallback.onMenuModeChange(this); 
  }
  
  public void clear() {
    if (this.mExpandedItem != null)
      collapseItemActionView(this.mExpandedItem); 
    this.mItems.clear();
    onItemsChanged(true);
  }
  
  public void clearAll() {
    this.mPreventDispatchingItemsChanged = true;
    clear();
    clearHeader();
    this.mPreventDispatchingItemsChanged = false;
    this.mItemsChangedWhileDispatchPrevented = false;
    this.mStructureChangedWhileDispatchPrevented = false;
    onItemsChanged(true);
  }
  
  public void clearHeader() {
    this.mHeaderIcon = null;
    this.mHeaderTitle = null;
    this.mHeaderView = null;
    onItemsChanged(false);
  }
  
  public void close() {
    close(true);
  }
  
  public final void close(boolean paramBoolean) {
    if (!this.mIsClosing) {
      this.mIsClosing = true;
      for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        menuPresenter.onCloseMenu(this, paramBoolean);
      } 
      this.mIsClosing = false;
    } 
  }
  
  public boolean collapseItemActionView(MenuItemImpl paramMenuItemImpl) {
    boolean bool2;
    if (this.mPresenters.isEmpty() || this.mExpandedItem != paramMenuItemImpl)
      return false; 
    boolean bool1 = false;
    stopDispatchingItemsChanged();
    Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
    while (true) {
      bool2 = bool1;
      if (iterator.hasNext()) {
        WeakReference<MenuPresenter> weakReference = iterator.next();
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        bool2 = menuPresenter.collapseItemActionView(this, paramMenuItemImpl);
        bool1 = bool2;
        if (bool2)
          break; 
        continue;
      } 
      break;
    } 
    startDispatchingItemsChanged();
    bool1 = bool2;
    if (bool2) {
      this.mExpandedItem = null;
      bool1 = bool2;
    } 
    return bool1;
  }
  
  boolean dispatchMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    return (this.mCallback != null && this.mCallback.onMenuItemSelected(paramMenuBuilder, paramMenuItem));
  }
  
  public boolean expandItemActionView(MenuItemImpl paramMenuItemImpl) {
    boolean bool2;
    if (this.mPresenters.isEmpty())
      return false; 
    boolean bool1 = false;
    stopDispatchingItemsChanged();
    Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
    while (true) {
      bool2 = bool1;
      if (iterator.hasNext()) {
        WeakReference<MenuPresenter> weakReference = iterator.next();
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        bool2 = menuPresenter.expandItemActionView(this, paramMenuItemImpl);
        bool1 = bool2;
        if (bool2)
          break; 
        continue;
      } 
      break;
    } 
    startDispatchingItemsChanged();
    bool1 = bool2;
    if (bool2) {
      this.mExpandedItem = paramMenuItemImpl;
      bool1 = bool2;
    } 
    return bool1;
  }
  
  public int findGroupIndex(int paramInt) {
    return findGroupIndex(paramInt, 0);
  }
  
  public int findGroupIndex(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore_3
    //   5: iload_2
    //   6: istore #4
    //   8: iload_2
    //   9: ifge -> 15
    //   12: iconst_0
    //   13: istore #4
    //   15: iload #4
    //   17: istore_2
    //   18: iload_2
    //   19: iload_3
    //   20: if_icmpge -> 49
    //   23: aload_0
    //   24: getfield mItems : Ljava/util/ArrayList;
    //   27: iload_2
    //   28: invokevirtual get : (I)Ljava/lang/Object;
    //   31: checkcast android/support/v7/view/menu/MenuItemImpl
    //   34: invokevirtual getGroupId : ()I
    //   37: iload_1
    //   38: if_icmpne -> 43
    //   41: iload_2
    //   42: ireturn
    //   43: iinc #2, 1
    //   46: goto -> 18
    //   49: iconst_m1
    //   50: istore_2
    //   51: goto -> 41
  }
  
  public MenuItem findItem(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: iload_2
    //   9: if_icmpge -> 72
    //   12: aload_0
    //   13: getfield mItems : Ljava/util/ArrayList;
    //   16: iload_3
    //   17: invokevirtual get : (I)Ljava/lang/Object;
    //   20: checkcast android/support/v7/view/menu/MenuItemImpl
    //   23: astore #4
    //   25: aload #4
    //   27: invokevirtual getItemId : ()I
    //   30: iload_1
    //   31: if_icmpne -> 37
    //   34: aload #4
    //   36: areturn
    //   37: aload #4
    //   39: invokevirtual hasSubMenu : ()Z
    //   42: ifeq -> 66
    //   45: aload #4
    //   47: invokevirtual getSubMenu : ()Landroid/view/SubMenu;
    //   50: iload_1
    //   51: invokeinterface findItem : (I)Landroid/view/MenuItem;
    //   56: astore #4
    //   58: aload #4
    //   60: ifnull -> 66
    //   63: goto -> 34
    //   66: iinc #3, 1
    //   69: goto -> 7
    //   72: aconst_null
    //   73: astore #4
    //   75: goto -> 34
  }
  
  public int findItemIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: iload_2
    //   9: if_icmpge -> 38
    //   12: aload_0
    //   13: getfield mItems : Ljava/util/ArrayList;
    //   16: iload_3
    //   17: invokevirtual get : (I)Ljava/lang/Object;
    //   20: checkcast android/support/v7/view/menu/MenuItemImpl
    //   23: invokevirtual getItemId : ()I
    //   26: iload_1
    //   27: if_icmpne -> 32
    //   30: iload_3
    //   31: ireturn
    //   32: iinc #3, 1
    //   35: goto -> 7
    //   38: iconst_m1
    //   39: istore_3
    //   40: goto -> 30
  }
  
  MenuItemImpl findItemWithShortcutForKey(int paramInt, KeyEvent paramKeyEvent) {
    KeyEvent keyEvent = null;
    ArrayList<MenuItemImpl> arrayList = this.mTempShortcutItemList;
    arrayList.clear();
    findItemsWithShortcutForKey(arrayList, paramInt, paramKeyEvent);
    if (arrayList.isEmpty())
      return (MenuItemImpl)keyEvent; 
    int i = paramKeyEvent.getMetaState();
    KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
    paramKeyEvent.getKeyData(keyData);
    int j = arrayList.size();
    if (j == 1)
      return arrayList.get(0); 
    boolean bool = isQwertyMode();
    byte b = 0;
    while (true) {
      MenuItemImpl menuItemImpl;
      paramKeyEvent = keyEvent;
      if (b < j) {
        char c;
        menuItemImpl = arrayList.get(b);
        if (bool) {
          c = menuItemImpl.getAlphabeticShortcut();
        } else {
          c = menuItemImpl.getNumericShortcut();
        } 
        if ((c != keyData.meta[0] || (i & 0x2) != 0) && (c != keyData.meta[2] || (i & 0x2) == 0) && (!bool || c != '\b' || paramInt != 67)) {
          b++;
          continue;
        } 
      } 
      return menuItemImpl;
    } 
  }
  
  void findItemsWithShortcutForKey(List<MenuItemImpl> paramList, int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = isQwertyMode();
    int i = paramKeyEvent.getMetaState();
    KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
    if (paramKeyEvent.getKeyData(keyData) || paramInt == 67) {
      int j = this.mItems.size();
      byte b = 0;
      while (true) {
        if (b < j) {
          char c;
          MenuItemImpl menuItemImpl = this.mItems.get(b);
          if (menuItemImpl.hasSubMenu())
            ((MenuBuilder)menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(paramList, paramInt, paramKeyEvent); 
          if (bool) {
            c = menuItemImpl.getAlphabeticShortcut();
          } else {
            c = menuItemImpl.getNumericShortcut();
          } 
          if ((i & 0x5) == 0 && c != '\000' && (c == keyData.meta[0] || c == keyData.meta[2] || (bool && c == '\b' && paramInt == 67)) && menuItemImpl.isEnabled())
            paramList.add(menuItemImpl); 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void flagActionItems() {
    ArrayList<MenuItemImpl> arrayList = getVisibleItems();
    if (this.mIsActionItemsStale) {
      boolean bool = false;
      for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
        MenuPresenter menuPresenter = weakReference.get();
        if (menuPresenter == null) {
          this.mPresenters.remove(weakReference);
          continue;
        } 
        bool |= menuPresenter.flagActionItems();
      } 
      if (bool) {
        this.mActionItems.clear();
        this.mNonActionItems.clear();
        int i = arrayList.size();
        for (bool = false; bool < i; bool++) {
          MenuItemImpl menuItemImpl = arrayList.get(bool);
          if (menuItemImpl.isActionButton()) {
            this.mActionItems.add(menuItemImpl);
          } else {
            this.mNonActionItems.add(menuItemImpl);
          } 
        } 
      } else {
        this.mActionItems.clear();
        this.mNonActionItems.clear();
        this.mNonActionItems.addAll(getVisibleItems());
      } 
      this.mIsActionItemsStale = false;
    } 
  }
  
  public ArrayList<MenuItemImpl> getActionItems() {
    flagActionItems();
    return this.mActionItems;
  }
  
  protected String getActionViewStatesKey() {
    return "android:menu:actionviewstates";
  }
  
  public Context getContext() {
    return this.mContext;
  }
  
  public MenuItemImpl getExpandedItem() {
    return this.mExpandedItem;
  }
  
  public Drawable getHeaderIcon() {
    return this.mHeaderIcon;
  }
  
  public CharSequence getHeaderTitle() {
    return this.mHeaderTitle;
  }
  
  public View getHeaderView() {
    return this.mHeaderView;
  }
  
  public MenuItem getItem(int paramInt) {
    return (MenuItem)this.mItems.get(paramInt);
  }
  
  public ArrayList<MenuItemImpl> getNonActionItems() {
    flagActionItems();
    return this.mNonActionItems;
  }
  
  boolean getOptionalIconsVisible() {
    return this.mOptionalIconsVisible;
  }
  
  Resources getResources() {
    return this.mResources;
  }
  
  public MenuBuilder getRootMenu() {
    return this;
  }
  
  @NonNull
  public ArrayList<MenuItemImpl> getVisibleItems() {
    if (!this.mIsVisibleItemsStale)
      return this.mVisibleItems; 
    this.mVisibleItems.clear();
    int i = this.mItems.size();
    for (byte b = 0; b < i; b++) {
      MenuItemImpl menuItemImpl = this.mItems.get(b);
      if (menuItemImpl.isVisible())
        this.mVisibleItems.add(menuItemImpl); 
    } 
    this.mIsVisibleItemsStale = false;
    this.mIsActionItemsStale = true;
    return this.mVisibleItems;
  }
  
  public boolean hasVisibleItems() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mOverrideVisibleItems : Z
    //   6: ifeq -> 13
    //   9: iload_1
    //   10: istore_2
    //   11: iload_2
    //   12: ireturn
    //   13: aload_0
    //   14: invokevirtual size : ()I
    //   17: istore_3
    //   18: iconst_0
    //   19: istore #4
    //   21: iload #4
    //   23: iload_3
    //   24: if_icmpge -> 53
    //   27: iload_1
    //   28: istore_2
    //   29: aload_0
    //   30: getfield mItems : Ljava/util/ArrayList;
    //   33: iload #4
    //   35: invokevirtual get : (I)Ljava/lang/Object;
    //   38: checkcast android/support/v7/view/menu/MenuItemImpl
    //   41: invokevirtual isVisible : ()Z
    //   44: ifne -> 11
    //   47: iinc #4, 1
    //   50: goto -> 21
    //   53: iconst_0
    //   54: istore_2
    //   55: goto -> 11
  }
  
  boolean isQwertyMode() {
    return this.mQwertyMode;
  }
  
  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent) {
    return (findItemWithShortcutForKey(paramInt, paramKeyEvent) != null);
  }
  
  public boolean isShortcutsVisible() {
    return this.mShortcutsVisible;
  }
  
  void onItemActionRequestChanged(MenuItemImpl paramMenuItemImpl) {
    this.mIsActionItemsStale = true;
    onItemsChanged(true);
  }
  
  void onItemVisibleChanged(MenuItemImpl paramMenuItemImpl) {
    this.mIsVisibleItemsStale = true;
    onItemsChanged(true);
  }
  
  public void onItemsChanged(boolean paramBoolean) {
    if (!this.mPreventDispatchingItemsChanged) {
      if (paramBoolean) {
        this.mIsVisibleItemsStale = true;
        this.mIsActionItemsStale = true;
      } 
      dispatchPresenterUpdate(paramBoolean);
      return;
    } 
    this.mItemsChangedWhileDispatchPrevented = true;
    if (paramBoolean)
      this.mStructureChangedWhileDispatchPrevented = true; 
  }
  
  public boolean performIdentifierAction(int paramInt1, int paramInt2) {
    return performItemAction(findItem(paramInt1), paramInt2);
  }
  
  public boolean performItemAction(MenuItem paramMenuItem, int paramInt) {
    return performItemAction(paramMenuItem, null, paramInt);
  }
  
  public boolean performItemAction(MenuItem paramMenuItem, MenuPresenter paramMenuPresenter, int paramInt) {
    boolean bool;
    MenuItemImpl menuItemImpl = (MenuItemImpl)paramMenuItem;
    if (menuItemImpl == null || !menuItemImpl.isEnabled())
      return false; 
    boolean bool2 = menuItemImpl.invoke();
    ActionProvider actionProvider = menuItemImpl.getSupportActionProvider();
    if (actionProvider != null && actionProvider.hasSubMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (menuItemImpl.hasCollapsibleActionView()) {
      bool2 |= menuItemImpl.expandActionView();
      boolean bool3 = bool2;
      if (bool2) {
        close(true);
        bool3 = bool2;
      } 
      return bool3;
    } 
    if (menuItemImpl.hasSubMenu() || bool) {
      if ((paramInt & 0x4) == 0)
        close(false); 
      if (!menuItemImpl.hasSubMenu())
        menuItemImpl.setSubMenu(new SubMenuBuilder(getContext(), this, menuItemImpl)); 
      SubMenuBuilder subMenuBuilder = (SubMenuBuilder)menuItemImpl.getSubMenu();
      if (bool)
        actionProvider.onPrepareSubMenu(subMenuBuilder); 
      bool2 |= dispatchSubMenuSelected(subMenuBuilder, paramMenuPresenter);
      boolean bool3 = bool2;
      if (!bool2) {
        close(true);
        bool3 = bool2;
      } 
      return bool3;
    } 
    boolean bool1 = bool2;
    if ((paramInt & 0x1) == 0) {
      close(true);
      bool1 = bool2;
    } 
    return bool1;
  }
  
  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2) {
    MenuItemImpl menuItemImpl = findItemWithShortcutForKey(paramInt1, paramKeyEvent);
    boolean bool = false;
    if (menuItemImpl != null)
      bool = performItemAction((MenuItem)menuItemImpl, paramInt2); 
    if ((paramInt2 & 0x2) != 0)
      close(true); 
    return bool;
  }
  
  public void removeGroup(int paramInt) {
    int i = findGroupIndex(paramInt);
    if (i >= 0) {
      int j = this.mItems.size();
      for (byte b = 0; b < j - i && ((MenuItemImpl)this.mItems.get(i)).getGroupId() == paramInt; b++)
        removeItemAtInt(i, false); 
      onItemsChanged(true);
    } 
  }
  
  public void removeItem(int paramInt) {
    removeItemAtInt(findItemIndex(paramInt), true);
  }
  
  public void removeItemAt(int paramInt) {
    removeItemAtInt(paramInt, true);
  }
  
  public void removeMenuPresenter(MenuPresenter paramMenuPresenter) {
    for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
      MenuPresenter menuPresenter = weakReference.get();
      if (menuPresenter == null || menuPresenter == paramMenuPresenter)
        this.mPresenters.remove(weakReference); 
    } 
  }
  
  public void restoreActionViewStates(Bundle paramBundle) {
    if (paramBundle != null) {
      SparseArray sparseArray = paramBundle.getSparseParcelableArray(getActionViewStatesKey());
      int i = size();
      int j;
      for (j = 0; j < i; j++) {
        MenuItem menuItem = getItem(j);
        View view = MenuItemCompat.getActionView(menuItem);
        if (view != null && view.getId() != -1)
          view.restoreHierarchyState(sparseArray); 
        if (menuItem.hasSubMenu())
          ((SubMenuBuilder)menuItem.getSubMenu()).restoreActionViewStates(paramBundle); 
      } 
      j = paramBundle.getInt("android:menu:expandedactionview");
      if (j > 0) {
        MenuItem menuItem = findItem(j);
        if (menuItem != null)
          MenuItemCompat.expandActionView(menuItem); 
      } 
    } 
  }
  
  public void restorePresenterStates(Bundle paramBundle) {
    dispatchRestoreInstanceState(paramBundle);
  }
  
  public void saveActionViewStates(Bundle paramBundle) {
    SparseArray sparseArray = null;
    int i = size();
    byte b = 0;
    while (b < i) {
      MenuItem menuItem = getItem(b);
      View view = MenuItemCompat.getActionView(menuItem);
      SparseArray sparseArray1 = sparseArray;
      if (view != null) {
        sparseArray1 = sparseArray;
        if (view.getId() != -1) {
          SparseArray sparseArray2 = sparseArray;
          if (sparseArray == null)
            sparseArray2 = new SparseArray(); 
          view.saveHierarchyState(sparseArray2);
          sparseArray1 = sparseArray2;
          if (MenuItemCompat.isActionViewExpanded(menuItem)) {
            paramBundle.putInt("android:menu:expandedactionview", menuItem.getItemId());
            sparseArray1 = sparseArray2;
          } 
        } 
      } 
      if (menuItem.hasSubMenu())
        ((SubMenuBuilder)menuItem.getSubMenu()).saveActionViewStates(paramBundle); 
      b++;
      sparseArray = sparseArray1;
    } 
    if (sparseArray != null)
      paramBundle.putSparseParcelableArray(getActionViewStatesKey(), sparseArray); 
  }
  
  public void savePresenterStates(Bundle paramBundle) {
    dispatchSaveInstanceState(paramBundle);
  }
  
  public void setCallback(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo paramContextMenuInfo) {
    this.mCurrentMenuInfo = paramContextMenuInfo;
  }
  
  public MenuBuilder setDefaultShowAsAction(int paramInt) {
    this.mDefaultShowAsAction = paramInt;
    return this;
  }
  
  void setExclusiveItemChecked(MenuItem paramMenuItem) {
    int i = paramMenuItem.getGroupId();
    int j = this.mItems.size();
    stopDispatchingItemsChanged();
    for (byte b = 0; b < j; b++) {
      MenuItemImpl menuItemImpl = this.mItems.get(b);
      if (menuItemImpl.getGroupId() == i && menuItemImpl.isExclusiveCheckable() && menuItemImpl.isCheckable()) {
        boolean bool;
        if (menuItemImpl == paramMenuItem) {
          bool = true;
        } else {
          bool = false;
        } 
        menuItemImpl.setCheckedInt(bool);
      } 
    } 
    startDispatchingItemsChanged();
  }
  
  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    int i = this.mItems.size();
    for (byte b = 0; b < i; b++) {
      MenuItemImpl menuItemImpl = this.mItems.get(b);
      if (menuItemImpl.getGroupId() == paramInt) {
        menuItemImpl.setExclusiveCheckable(paramBoolean2);
        menuItemImpl.setCheckable(paramBoolean1);
      } 
    } 
  }
  
  public void setGroupEnabled(int paramInt, boolean paramBoolean) {
    int i = this.mItems.size();
    for (byte b = 0; b < i; b++) {
      MenuItemImpl menuItemImpl = this.mItems.get(b);
      if (menuItemImpl.getGroupId() == paramInt)
        menuItemImpl.setEnabled(paramBoolean); 
    } 
  }
  
  public void setGroupVisible(int paramInt, boolean paramBoolean) {
    int i = this.mItems.size();
    boolean bool = false;
    byte b = 0;
    while (b < i) {
      MenuItemImpl menuItemImpl = this.mItems.get(b);
      boolean bool1 = bool;
      if (menuItemImpl.getGroupId() == paramInt) {
        bool1 = bool;
        if (menuItemImpl.setVisibleInt(paramBoolean))
          bool1 = true; 
      } 
      b++;
      bool = bool1;
    } 
    if (bool)
      onItemsChanged(true); 
  }
  
  protected MenuBuilder setHeaderIconInt(int paramInt) {
    setHeaderInternal(0, null, paramInt, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderIconInt(Drawable paramDrawable) {
    setHeaderInternal(0, null, 0, paramDrawable, null);
    return this;
  }
  
  protected MenuBuilder setHeaderTitleInt(int paramInt) {
    setHeaderInternal(paramInt, null, 0, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderTitleInt(CharSequence paramCharSequence) {
    setHeaderInternal(0, paramCharSequence, 0, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderViewInt(View paramView) {
    setHeaderInternal(0, null, 0, null, paramView);
    return this;
  }
  
  public void setOptionalIconsVisible(boolean paramBoolean) {
    this.mOptionalIconsVisible = paramBoolean;
  }
  
  public void setOverrideVisibleItems(boolean paramBoolean) {
    this.mOverrideVisibleItems = paramBoolean;
  }
  
  public void setQwertyMode(boolean paramBoolean) {
    this.mQwertyMode = paramBoolean;
    onItemsChanged(false);
  }
  
  public void setShortcutsVisible(boolean paramBoolean) {
    if (this.mShortcutsVisible != paramBoolean) {
      setShortcutsVisibleInner(paramBoolean);
      onItemsChanged(false);
    } 
  }
  
  public int size() {
    return this.mItems.size();
  }
  
  public void startDispatchingItemsChanged() {
    this.mPreventDispatchingItemsChanged = false;
    if (this.mItemsChangedWhileDispatchPrevented) {
      this.mItemsChangedWhileDispatchPrevented = false;
      onItemsChanged(this.mStructureChangedWhileDispatchPrevented);
    } 
  }
  
  public void stopDispatchingItemsChanged() {
    if (!this.mPreventDispatchingItemsChanged) {
      this.mPreventDispatchingItemsChanged = true;
      this.mItemsChangedWhileDispatchPrevented = false;
      this.mStructureChangedWhileDispatchPrevented = false;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static interface Callback {
    boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem);
    
    void onMenuModeChange(MenuBuilder param1MenuBuilder);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static interface ItemInvoker {
    boolean invokeItem(MenuItemImpl param1MenuItemImpl);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/view/menu/MenuBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */