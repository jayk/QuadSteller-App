package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.transition.ActionBarTransition;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private View mScrapActionButtonView;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   4: checkcast android/view/ViewGroup
    //   7: astore_2
    //   8: aload_2
    //   9: ifnonnull -> 16
    //   12: aconst_null
    //   13: astore_3
    //   14: aload_3
    //   15: areturn
    //   16: aload_2
    //   17: invokevirtual getChildCount : ()I
    //   20: istore #4
    //   22: iconst_0
    //   23: istore #5
    //   25: iload #5
    //   27: iload #4
    //   29: if_icmpge -> 71
    //   32: aload_2
    //   33: iload #5
    //   35: invokevirtual getChildAt : (I)Landroid/view/View;
    //   38: astore #6
    //   40: aload #6
    //   42: instanceof android/support/v7/view/menu/MenuView$ItemView
    //   45: ifeq -> 65
    //   48: aload #6
    //   50: astore_3
    //   51: aload #6
    //   53: checkcast android/support/v7/view/menu/MenuView$ItemView
    //   56: invokeinterface getItemData : ()Landroid/support/v7/view/menu/MenuItemImpl;
    //   61: aload_1
    //   62: if_acmpeq -> 14
    //   65: iinc #5, 1
    //   68: goto -> 25
    //   71: aconst_null
    //   72: astore_3
    //   73: goto -> 14
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   4: ifnull -> 123
    //   7: aload_0
    //   8: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   11: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   14: astore_1
    //   15: aload_1
    //   16: invokevirtual size : ()I
    //   19: istore_2
    //   20: aload_0
    //   21: getfield mMaxItems : I
    //   24: istore_3
    //   25: aload_0
    //   26: getfield mActionItemWidthLimit : I
    //   29: istore #4
    //   31: iconst_0
    //   32: iconst_0
    //   33: invokestatic makeMeasureSpec : (II)I
    //   36: istore #5
    //   38: aload_0
    //   39: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   42: checkcast android/view/ViewGroup
    //   45: astore #6
    //   47: iconst_0
    //   48: istore #7
    //   50: iconst_0
    //   51: istore #8
    //   53: iconst_0
    //   54: istore #9
    //   56: iconst_0
    //   57: istore #10
    //   59: iconst_0
    //   60: istore #11
    //   62: iload #11
    //   64: iload_2
    //   65: if_icmpge -> 150
    //   68: aload_1
    //   69: iload #11
    //   71: invokevirtual get : (I)Ljava/lang/Object;
    //   74: checkcast android/support/v7/view/menu/MenuItemImpl
    //   77: astore #12
    //   79: aload #12
    //   81: invokevirtual requiresActionButton : ()Z
    //   84: ifeq -> 130
    //   87: iinc #7, 1
    //   90: iload_3
    //   91: istore #13
    //   93: aload_0
    //   94: getfield mExpandedActionViewsExclusive : Z
    //   97: ifeq -> 114
    //   100: iload_3
    //   101: istore #13
    //   103: aload #12
    //   105: invokevirtual isActionViewExpanded : ()Z
    //   108: ifeq -> 114
    //   111: iconst_0
    //   112: istore #13
    //   114: iinc #11, 1
    //   117: iload #13
    //   119: istore_3
    //   120: goto -> 62
    //   123: aconst_null
    //   124: astore_1
    //   125: iconst_0
    //   126: istore_2
    //   127: goto -> 20
    //   130: aload #12
    //   132: invokevirtual requestsActionButton : ()Z
    //   135: ifeq -> 144
    //   138: iinc #8, 1
    //   141: goto -> 90
    //   144: iconst_1
    //   145: istore #10
    //   147: goto -> 90
    //   150: iload_3
    //   151: istore #11
    //   153: aload_0
    //   154: getfield mReserveOverflow : Z
    //   157: ifeq -> 182
    //   160: iload #10
    //   162: ifne -> 177
    //   165: iload_3
    //   166: istore #11
    //   168: iload #7
    //   170: iload #8
    //   172: iadd
    //   173: iload_3
    //   174: if_icmple -> 182
    //   177: iload_3
    //   178: iconst_1
    //   179: isub
    //   180: istore #11
    //   182: iload #11
    //   184: iload #7
    //   186: isub
    //   187: istore #11
    //   189: aload_0
    //   190: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   193: astore #14
    //   195: aload #14
    //   197: invokevirtual clear : ()V
    //   200: iconst_0
    //   201: istore #15
    //   203: iconst_0
    //   204: istore #7
    //   206: aload_0
    //   207: getfield mStrictWidthLimit : Z
    //   210: ifeq -> 241
    //   213: iload #4
    //   215: aload_0
    //   216: getfield mMinCellSize : I
    //   219: idiv
    //   220: istore #7
    //   222: aload_0
    //   223: getfield mMinCellSize : I
    //   226: istore_3
    //   227: aload_0
    //   228: getfield mMinCellSize : I
    //   231: iload #4
    //   233: iload_3
    //   234: irem
    //   235: iload #7
    //   237: idiv
    //   238: iadd
    //   239: istore #15
    //   241: iconst_0
    //   242: istore_3
    //   243: iload #4
    //   245: istore #10
    //   247: iload_3
    //   248: istore #4
    //   250: iload #9
    //   252: istore_3
    //   253: iload #4
    //   255: iload_2
    //   256: if_icmpge -> 795
    //   259: aload_1
    //   260: iload #4
    //   262: invokevirtual get : (I)Ljava/lang/Object;
    //   265: checkcast android/support/v7/view/menu/MenuItemImpl
    //   268: astore #12
    //   270: aload #12
    //   272: invokevirtual requiresActionButton : ()Z
    //   275: ifeq -> 402
    //   278: aload_0
    //   279: aload #12
    //   281: aload_0
    //   282: getfield mScrapActionButtonView : Landroid/view/View;
    //   285: aload #6
    //   287: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   290: astore #16
    //   292: aload_0
    //   293: getfield mScrapActionButtonView : Landroid/view/View;
    //   296: ifnonnull -> 305
    //   299: aload_0
    //   300: aload #16
    //   302: putfield mScrapActionButtonView : Landroid/view/View;
    //   305: aload_0
    //   306: getfield mStrictWidthLimit : Z
    //   309: ifeq -> 390
    //   312: iload #7
    //   314: aload #16
    //   316: iload #15
    //   318: iload #7
    //   320: iload #5
    //   322: iconst_0
    //   323: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   326: isub
    //   327: istore #7
    //   329: aload #16
    //   331: invokevirtual getMeasuredWidth : ()I
    //   334: istore #13
    //   336: iload #10
    //   338: iload #13
    //   340: isub
    //   341: istore #8
    //   343: iload_3
    //   344: istore #10
    //   346: iload_3
    //   347: ifne -> 354
    //   350: iload #13
    //   352: istore #10
    //   354: aload #12
    //   356: invokevirtual getGroupId : ()I
    //   359: istore_3
    //   360: iload_3
    //   361: ifeq -> 371
    //   364: aload #14
    //   366: iload_3
    //   367: iconst_1
    //   368: invokevirtual put : (IZ)V
    //   371: aload #12
    //   373: iconst_1
    //   374: invokevirtual setIsActionButton : (Z)V
    //   377: iload #10
    //   379: istore_3
    //   380: iinc #4, 1
    //   383: iload #8
    //   385: istore #10
    //   387: goto -> 253
    //   390: aload #16
    //   392: iload #5
    //   394: iload #5
    //   396: invokevirtual measure : (II)V
    //   399: goto -> 329
    //   402: aload #12
    //   404: invokevirtual requestsActionButton : ()Z
    //   407: ifeq -> 782
    //   410: aload #12
    //   412: invokevirtual getGroupId : ()I
    //   415: istore #17
    //   417: aload #14
    //   419: iload #17
    //   421: invokevirtual get : (I)Z
    //   424: istore #18
    //   426: iload #11
    //   428: ifgt -> 436
    //   431: iload #18
    //   433: ifeq -> 647
    //   436: iload #10
    //   438: ifle -> 647
    //   441: aload_0
    //   442: getfield mStrictWidthLimit : Z
    //   445: ifeq -> 453
    //   448: iload #7
    //   450: ifle -> 647
    //   453: iconst_1
    //   454: istore #19
    //   456: iload #7
    //   458: istore #9
    //   460: iload_3
    //   461: istore #13
    //   463: iload #19
    //   465: istore #20
    //   467: iload #10
    //   469: istore #8
    //   471: iload #19
    //   473: ifeq -> 596
    //   476: aload_0
    //   477: aload #12
    //   479: aload_0
    //   480: getfield mScrapActionButtonView : Landroid/view/View;
    //   483: aload #6
    //   485: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   488: astore #16
    //   490: aload_0
    //   491: getfield mScrapActionButtonView : Landroid/view/View;
    //   494: ifnonnull -> 503
    //   497: aload_0
    //   498: aload #16
    //   500: putfield mScrapActionButtonView : Landroid/view/View;
    //   503: aload_0
    //   504: getfield mStrictWidthLimit : Z
    //   507: ifeq -> 653
    //   510: aload #16
    //   512: iload #15
    //   514: iload #7
    //   516: iload #5
    //   518: iconst_0
    //   519: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   522: istore #13
    //   524: iload #7
    //   526: iload #13
    //   528: isub
    //   529: istore #8
    //   531: iload #8
    //   533: istore #7
    //   535: iload #13
    //   537: ifne -> 547
    //   540: iconst_0
    //   541: istore #19
    //   543: iload #8
    //   545: istore #7
    //   547: aload #16
    //   549: invokevirtual getMeasuredWidth : ()I
    //   552: istore #9
    //   554: iload #10
    //   556: iload #9
    //   558: isub
    //   559: istore #8
    //   561: iload_3
    //   562: istore #13
    //   564: iload_3
    //   565: ifne -> 572
    //   568: iload #9
    //   570: istore #13
    //   572: aload_0
    //   573: getfield mStrictWidthLimit : Z
    //   576: ifeq -> 670
    //   579: iload #8
    //   581: iflt -> 665
    //   584: iconst_1
    //   585: istore_3
    //   586: iload #19
    //   588: iload_3
    //   589: iand
    //   590: istore #20
    //   592: iload #7
    //   594: istore #9
    //   596: iload #20
    //   598: ifeq -> 698
    //   601: iload #17
    //   603: ifeq -> 698
    //   606: aload #14
    //   608: iload #17
    //   610: iconst_1
    //   611: invokevirtual put : (IZ)V
    //   614: iload #11
    //   616: istore_3
    //   617: iload_3
    //   618: istore #11
    //   620: iload #20
    //   622: ifeq -> 630
    //   625: iload_3
    //   626: iconst_1
    //   627: isub
    //   628: istore #11
    //   630: aload #12
    //   632: iload #20
    //   634: invokevirtual setIsActionButton : (Z)V
    //   637: iload #9
    //   639: istore #7
    //   641: iload #13
    //   643: istore_3
    //   644: goto -> 380
    //   647: iconst_0
    //   648: istore #19
    //   650: goto -> 456
    //   653: aload #16
    //   655: iload #5
    //   657: iload #5
    //   659: invokevirtual measure : (II)V
    //   662: goto -> 547
    //   665: iconst_0
    //   666: istore_3
    //   667: goto -> 586
    //   670: iload #8
    //   672: iload #13
    //   674: iadd
    //   675: ifle -> 693
    //   678: iconst_1
    //   679: istore_3
    //   680: iload #19
    //   682: iload_3
    //   683: iand
    //   684: istore #20
    //   686: iload #7
    //   688: istore #9
    //   690: goto -> 596
    //   693: iconst_0
    //   694: istore_3
    //   695: goto -> 680
    //   698: iload #11
    //   700: istore_3
    //   701: iload #18
    //   703: ifeq -> 617
    //   706: aload #14
    //   708: iload #17
    //   710: iconst_0
    //   711: invokevirtual put : (IZ)V
    //   714: iconst_0
    //   715: istore #7
    //   717: iload #11
    //   719: istore_3
    //   720: iload #7
    //   722: iload #4
    //   724: if_icmpge -> 617
    //   727: aload_1
    //   728: iload #7
    //   730: invokevirtual get : (I)Ljava/lang/Object;
    //   733: checkcast android/support/v7/view/menu/MenuItemImpl
    //   736: astore #16
    //   738: iload #11
    //   740: istore_3
    //   741: aload #16
    //   743: invokevirtual getGroupId : ()I
    //   746: iload #17
    //   748: if_icmpne -> 773
    //   751: iload #11
    //   753: istore_3
    //   754: aload #16
    //   756: invokevirtual isActionButton : ()Z
    //   759: ifeq -> 767
    //   762: iload #11
    //   764: iconst_1
    //   765: iadd
    //   766: istore_3
    //   767: aload #16
    //   769: iconst_0
    //   770: invokevirtual setIsActionButton : (Z)V
    //   773: iinc #7, 1
    //   776: iload_3
    //   777: istore #11
    //   779: goto -> 717
    //   782: aload #12
    //   784: iconst_0
    //   785: invokevirtual setIsActionButton : (Z)V
    //   788: iload #10
    //   790: istore #8
    //   792: goto -> 380
    //   795: iconst_1
    //   796: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    return (this.mOverflowButton != null) ? this.mOverflowButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    if (this.mActionButtonPopup != null) {
      this.mActionButtonPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(@NonNull Context paramContext, @Nullable MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
        if (this.mPendingOverflowIconSet) {
          this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)(56.0F * (resources.getDisplayMetrics()).density);
    this.mScrapActionButtonView = null;
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mOverflowPopup != null && this.mOverflowPopup.isShowing());
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof SavedState) {
      paramParcelable = paramParcelable;
      if (((SavedState)paramParcelable).openSubMenuId > 0) {
        MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
        if (menuItem != null)
          onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
      } 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool = false;
    if (paramSubMenuBuilder.hasVisibleItems()) {
      SubMenuBuilder subMenuBuilder;
      for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
      View view = findViewForItem(subMenuBuilder.getItem());
      if (view != null) {
        this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
        boolean bool1 = false;
        int i = paramSubMenuBuilder.size();
        byte b = 0;
        while (true) {
          bool = bool1;
          if (b < i) {
            MenuItem menuItem = paramSubMenuBuilder.getItem(b);
            if (menuItem.isVisible() && menuItem.getIcon() != null) {
              bool = true;
            } else {
              b++;
              continue;
            } 
          } 
          this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
          this.mActionButtonPopup.setForceShowIcon(bool);
          this.mActionButtonPopup.show();
          super.onSubMenuSelected(paramSubMenuBuilder);
          return true;
        } 
      } 
    } 
    return bool;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
      return;
    } 
    if (this.mMenu != null)
      this.mMenu.close(false); 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    if (this.mOverflowButton != null) {
      this.mOverflowButton.setImageDrawable(paramDrawable);
      return;
    } 
    this.mPendingOverflowIconSet = true;
    this.mPendingOverflowIcon = paramDrawable;
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    null = true;
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return null;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    ViewGroup<MenuItemImpl> viewGroup = (ViewGroup)((View)this.mMenuView).getParent();
    if (viewGroup != null)
      ActionBarTransition.beginDelayedTransition(viewGroup); 
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    if (this.mMenu != null) {
      ArrayList<MenuItemImpl> arrayList = this.mMenu.getActionItems();
      int j = arrayList.size();
      for (byte b1 = 0; b1 < j; b1++) {
        ActionProvider actionProvider = ((MenuItemImpl)arrayList.get(b1)).getSupportActionProvider();
        if (actionProvider != null)
          actionProvider.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      ArrayList arrayList = this.mMenu.getNonActionItems();
    } else {
      viewGroup = null;
    } 
    byte b = 0;
    int i = b;
    if (this.mReserveOverflow) {
      i = b;
      if (viewGroup != null) {
        i = viewGroup.size();
        if (i == 1) {
          if (!((MenuItemImpl)viewGroup.get(0)).isActionViewExpanded()) {
            i = 1;
          } else {
            i = 0;
          } 
        } else if (i > 0) {
          i = 1;
        } else {
          i = 0;
        } 
      } 
    } 
    if (i != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      viewGroup = (ViewGroup<MenuItemImpl>)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
      ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter.this.mActionButtonPopup = null;
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mActionButtonPopup != null) ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null);
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      setOnTouchListener(new ForwardingListener((View)this) {
            public ShowableListMenu getPopup() {
              return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (!super.performClick()) {
        playSoundEffect(0);
        ActionMenuPresenter.this.showOverflowMenu();
      } 
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int i = getWidth();
        param1Int3 = getHeight();
        param1Int1 = Math.max(i, param1Int3) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        param1Int2 = getPaddingTop();
        param1Int4 = getPaddingBottom();
        j = (i + j - k) / 2;
        param1Int2 = (param1Int3 + param1Int2 - param1Int4) / 2;
        DrawableCompat.setHotspotBounds(drawable2, j - param1Int1, param1Int2 - param1Int1, j + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    null(View param1View) {
      super(param1View);
    }
    
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      boolean bool = false;
      if (param1MenuBuilder != null) {
        ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
        MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
        if (callback != null)
          return callback.onOpenSubMenu(param1MenuBuilder); 
        bool = false;
      } 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ActionMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */