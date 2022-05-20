package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class ListPopupWindow implements ShowableListMenu {
  private static final boolean DEBUG = false;
  
  static final int EXPAND_LIST_TIMEOUT = 250;
  
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  
  public static final int INPUT_METHOD_NEEDED = 1;
  
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  
  public static final int MATCH_PARENT = -1;
  
  public static final int POSITION_PROMPT_ABOVE = 0;
  
  public static final int POSITION_PROMPT_BELOW = 1;
  
  private static final String TAG = "ListPopupWindow";
  
  public static final int WRAP_CONTENT = -2;
  
  private static Method sClipToWindowEnabledMethod;
  
  private static Method sGetMaxAvailableHeightMethod;
  
  private static Method sSetEpicenterBoundsMethod;
  
  private ListAdapter mAdapter;
  
  private Context mContext;
  
  private boolean mDropDownAlwaysVisible = false;
  
  private View mDropDownAnchorView;
  
  private int mDropDownGravity = 0;
  
  private int mDropDownHeight = -2;
  
  private int mDropDownHorizontalOffset;
  
  DropDownListView mDropDownList;
  
  private Drawable mDropDownListHighlight;
  
  private int mDropDownVerticalOffset;
  
  private boolean mDropDownVerticalOffsetSet;
  
  private int mDropDownWidth = -2;
  
  private int mDropDownWindowLayoutType = 1002;
  
  private Rect mEpicenterBounds;
  
  private boolean mForceIgnoreOutsideTouch = false;
  
  final Handler mHandler;
  
  private final ListSelectorHider mHideSelector = new ListSelectorHider();
  
  private boolean mIsAnimatedFromAnchor = true;
  
  private AdapterView.OnItemClickListener mItemClickListener;
  
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  
  int mListItemExpandMaximum = Integer.MAX_VALUE;
  
  private boolean mModal;
  
  private DataSetObserver mObserver;
  
  PopupWindow mPopup;
  
  private int mPromptPosition = 0;
  
  private View mPromptView;
  
  final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
  
  private final PopupScrollListener mScrollListener = new PopupScrollListener();
  
  private Runnable mShowDropDownRunnable;
  
  private final Rect mTempRect = new Rect();
  
  private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
  
  static {
    try {
      sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", new Class[] { boolean.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
    } 
    try {
      sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", new Class[] { View.class, int.class, boolean.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
    } 
    try {
      sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", new Class[] { Rect.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
    } 
  }
  
  public ListPopupWindow(@NonNull Context paramContext) {
    this(paramContext, null, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt1, @StyleRes int paramInt2) {
    this.mContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper());
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPopupWindow, paramInt1, paramInt2);
    this.mDropDownHorizontalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
    this.mDropDownVerticalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
    if (this.mDropDownVerticalOffset != 0)
      this.mDropDownVerticalOffsetSet = true; 
    typedArray.recycle();
    if (Build.VERSION.SDK_INT >= 11) {
      this.mPopup = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1, paramInt2);
    } else {
      this.mPopup = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1);
    } 
    this.mPopup.setInputMethodMode(1);
  }
  
  private int buildDropDown() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_0
    //   5: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   8: ifnonnull -> 484
    //   11: aload_0
    //   12: getfield mContext : Landroid/content/Context;
    //   15: astore_3
    //   16: aload_0
    //   17: new android/support/v7/widget/ListPopupWindow$2
    //   20: dup
    //   21: aload_0
    //   22: invokespecial <init> : (Landroid/support/v7/widget/ListPopupWindow;)V
    //   25: putfield mShowDropDownRunnable : Ljava/lang/Runnable;
    //   28: aload_0
    //   29: getfield mModal : Z
    //   32: ifne -> 435
    //   35: iconst_1
    //   36: istore #4
    //   38: aload_0
    //   39: aload_0
    //   40: aload_3
    //   41: iload #4
    //   43: invokevirtual createDropDownListView : (Landroid/content/Context;Z)Landroid/support/v7/widget/DropDownListView;
    //   46: putfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   49: aload_0
    //   50: getfield mDropDownListHighlight : Landroid/graphics/drawable/Drawable;
    //   53: ifnull -> 67
    //   56: aload_0
    //   57: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   60: aload_0
    //   61: getfield mDropDownListHighlight : Landroid/graphics/drawable/Drawable;
    //   64: invokevirtual setSelector : (Landroid/graphics/drawable/Drawable;)V
    //   67: aload_0
    //   68: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   71: aload_0
    //   72: getfield mAdapter : Landroid/widget/ListAdapter;
    //   75: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   78: aload_0
    //   79: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   82: aload_0
    //   83: getfield mItemClickListener : Landroid/widget/AdapterView$OnItemClickListener;
    //   86: invokevirtual setOnItemClickListener : (Landroid/widget/AdapterView$OnItemClickListener;)V
    //   89: aload_0
    //   90: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   93: iconst_1
    //   94: invokevirtual setFocusable : (Z)V
    //   97: aload_0
    //   98: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   101: iconst_1
    //   102: invokevirtual setFocusableInTouchMode : (Z)V
    //   105: aload_0
    //   106: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   109: new android/support/v7/widget/ListPopupWindow$3
    //   112: dup
    //   113: aload_0
    //   114: invokespecial <init> : (Landroid/support/v7/widget/ListPopupWindow;)V
    //   117: invokevirtual setOnItemSelectedListener : (Landroid/widget/AdapterView$OnItemSelectedListener;)V
    //   120: aload_0
    //   121: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   124: aload_0
    //   125: getfield mScrollListener : Landroid/support/v7/widget/ListPopupWindow$PopupScrollListener;
    //   128: invokevirtual setOnScrollListener : (Landroid/widget/AbsListView$OnScrollListener;)V
    //   131: aload_0
    //   132: getfield mItemSelectedListener : Landroid/widget/AdapterView$OnItemSelectedListener;
    //   135: ifnull -> 149
    //   138: aload_0
    //   139: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   142: aload_0
    //   143: getfield mItemSelectedListener : Landroid/widget/AdapterView$OnItemSelectedListener;
    //   146: invokevirtual setOnItemSelectedListener : (Landroid/widget/AdapterView$OnItemSelectedListener;)V
    //   149: aload_0
    //   150: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   153: astore #5
    //   155: aload_0
    //   156: getfield mPromptView : Landroid/view/View;
    //   159: astore #6
    //   161: aload #5
    //   163: astore #7
    //   165: aload #6
    //   167: ifnull -> 308
    //   170: new android/widget/LinearLayout
    //   173: dup
    //   174: aload_3
    //   175: invokespecial <init> : (Landroid/content/Context;)V
    //   178: astore #7
    //   180: aload #7
    //   182: iconst_1
    //   183: invokevirtual setOrientation : (I)V
    //   186: new android/widget/LinearLayout$LayoutParams
    //   189: dup
    //   190: iconst_m1
    //   191: iconst_0
    //   192: fconst_1
    //   193: invokespecial <init> : (IIF)V
    //   196: astore_3
    //   197: aload_0
    //   198: getfield mPromptPosition : I
    //   201: tableswitch default -> 224, 0 -> 459, 1 -> 441
    //   224: ldc 'ListPopupWindow'
    //   226: new java/lang/StringBuilder
    //   229: dup
    //   230: invokespecial <init> : ()V
    //   233: ldc_w 'Invalid hint position '
    //   236: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   239: aload_0
    //   240: getfield mPromptPosition : I
    //   243: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   246: invokevirtual toString : ()Ljava/lang/String;
    //   249: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   252: pop
    //   253: aload_0
    //   254: getfield mDropDownWidth : I
    //   257: iflt -> 477
    //   260: ldc_w -2147483648
    //   263: istore_2
    //   264: aload_0
    //   265: getfield mDropDownWidth : I
    //   268: istore_1
    //   269: aload #6
    //   271: iload_1
    //   272: iload_2
    //   273: invokestatic makeMeasureSpec : (II)I
    //   276: iconst_0
    //   277: invokevirtual measure : (II)V
    //   280: aload #6
    //   282: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   285: checkcast android/widget/LinearLayout$LayoutParams
    //   288: astore #5
    //   290: aload #6
    //   292: invokevirtual getMeasuredHeight : ()I
    //   295: aload #5
    //   297: getfield topMargin : I
    //   300: iadd
    //   301: aload #5
    //   303: getfield bottomMargin : I
    //   306: iadd
    //   307: istore_2
    //   308: aload_0
    //   309: getfield mPopup : Landroid/widget/PopupWindow;
    //   312: aload #7
    //   314: invokevirtual setContentView : (Landroid/view/View;)V
    //   317: aload_0
    //   318: getfield mPopup : Landroid/widget/PopupWindow;
    //   321: invokevirtual getBackground : ()Landroid/graphics/drawable/Drawable;
    //   324: astore #7
    //   326: aload #7
    //   328: ifnull -> 540
    //   331: aload #7
    //   333: aload_0
    //   334: getfield mTempRect : Landroid/graphics/Rect;
    //   337: invokevirtual getPadding : (Landroid/graphics/Rect;)Z
    //   340: pop
    //   341: aload_0
    //   342: getfield mTempRect : Landroid/graphics/Rect;
    //   345: getfield top : I
    //   348: aload_0
    //   349: getfield mTempRect : Landroid/graphics/Rect;
    //   352: getfield bottom : I
    //   355: iadd
    //   356: istore_1
    //   357: iload_1
    //   358: istore #8
    //   360: aload_0
    //   361: getfield mDropDownVerticalOffsetSet : Z
    //   364: ifne -> 382
    //   367: aload_0
    //   368: aload_0
    //   369: getfield mTempRect : Landroid/graphics/Rect;
    //   372: getfield top : I
    //   375: ineg
    //   376: putfield mDropDownVerticalOffset : I
    //   379: iload_1
    //   380: istore #8
    //   382: aload_0
    //   383: getfield mPopup : Landroid/widget/PopupWindow;
    //   386: invokevirtual getInputMethodMode : ()I
    //   389: iconst_2
    //   390: if_icmpne -> 553
    //   393: iconst_1
    //   394: istore #4
    //   396: aload_0
    //   397: aload_0
    //   398: invokevirtual getAnchorView : ()Landroid/view/View;
    //   401: aload_0
    //   402: getfield mDropDownVerticalOffset : I
    //   405: iload #4
    //   407: invokespecial getMaxAvailableHeight : (Landroid/view/View;IZ)I
    //   410: istore #9
    //   412: aload_0
    //   413: getfield mDropDownAlwaysVisible : Z
    //   416: ifne -> 427
    //   419: aload_0
    //   420: getfield mDropDownHeight : I
    //   423: iconst_m1
    //   424: if_icmpne -> 559
    //   427: iload #9
    //   429: iload #8
    //   431: iadd
    //   432: istore_2
    //   433: iload_2
    //   434: ireturn
    //   435: iconst_0
    //   436: istore #4
    //   438: goto -> 38
    //   441: aload #7
    //   443: aload #5
    //   445: aload_3
    //   446: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   449: aload #7
    //   451: aload #6
    //   453: invokevirtual addView : (Landroid/view/View;)V
    //   456: goto -> 253
    //   459: aload #7
    //   461: aload #6
    //   463: invokevirtual addView : (Landroid/view/View;)V
    //   466: aload #7
    //   468: aload #5
    //   470: aload_3
    //   471: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   474: goto -> 253
    //   477: iconst_0
    //   478: istore_2
    //   479: iconst_0
    //   480: istore_1
    //   481: goto -> 269
    //   484: aload_0
    //   485: getfield mPopup : Landroid/widget/PopupWindow;
    //   488: invokevirtual getContentView : ()Landroid/view/View;
    //   491: checkcast android/view/ViewGroup
    //   494: astore #7
    //   496: aload_0
    //   497: getfield mPromptView : Landroid/view/View;
    //   500: astore #7
    //   502: iload_1
    //   503: istore_2
    //   504: aload #7
    //   506: ifnull -> 317
    //   509: aload #7
    //   511: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   514: checkcast android/widget/LinearLayout$LayoutParams
    //   517: astore #5
    //   519: aload #7
    //   521: invokevirtual getMeasuredHeight : ()I
    //   524: aload #5
    //   526: getfield topMargin : I
    //   529: iadd
    //   530: aload #5
    //   532: getfield bottomMargin : I
    //   535: iadd
    //   536: istore_2
    //   537: goto -> 317
    //   540: aload_0
    //   541: getfield mTempRect : Landroid/graphics/Rect;
    //   544: invokevirtual setEmpty : ()V
    //   547: iconst_0
    //   548: istore #8
    //   550: goto -> 382
    //   553: iconst_0
    //   554: istore #4
    //   556: goto -> 396
    //   559: aload_0
    //   560: getfield mDropDownWidth : I
    //   563: tableswitch default -> 584, -2 -> 648, -1 -> 687
    //   584: aload_0
    //   585: getfield mDropDownWidth : I
    //   588: ldc_w 1073741824
    //   591: invokestatic makeMeasureSpec : (II)I
    //   594: istore_1
    //   595: aload_0
    //   596: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   599: iload_1
    //   600: iconst_0
    //   601: iconst_m1
    //   602: iload #9
    //   604: iload_2
    //   605: isub
    //   606: iconst_m1
    //   607: invokevirtual measureHeightOfChildrenCompat : (IIIII)I
    //   610: istore #9
    //   612: iload_2
    //   613: istore_1
    //   614: iload #9
    //   616: ifle -> 640
    //   619: iload_2
    //   620: iload #8
    //   622: aload_0
    //   623: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   626: invokevirtual getPaddingTop : ()I
    //   629: aload_0
    //   630: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   633: invokevirtual getPaddingBottom : ()I
    //   636: iadd
    //   637: iadd
    //   638: iadd
    //   639: istore_1
    //   640: iload #9
    //   642: iload_1
    //   643: iadd
    //   644: istore_2
    //   645: goto -> 433
    //   648: aload_0
    //   649: getfield mContext : Landroid/content/Context;
    //   652: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   655: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   658: getfield widthPixels : I
    //   661: aload_0
    //   662: getfield mTempRect : Landroid/graphics/Rect;
    //   665: getfield left : I
    //   668: aload_0
    //   669: getfield mTempRect : Landroid/graphics/Rect;
    //   672: getfield right : I
    //   675: iadd
    //   676: isub
    //   677: ldc_w -2147483648
    //   680: invokestatic makeMeasureSpec : (II)I
    //   683: istore_1
    //   684: goto -> 595
    //   687: aload_0
    //   688: getfield mContext : Landroid/content/Context;
    //   691: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   694: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   697: getfield widthPixels : I
    //   700: aload_0
    //   701: getfield mTempRect : Landroid/graphics/Rect;
    //   704: getfield left : I
    //   707: aload_0
    //   708: getfield mTempRect : Landroid/graphics/Rect;
    //   711: getfield right : I
    //   714: iadd
    //   715: isub
    //   716: ldc_w 1073741824
    //   719: invokestatic makeMeasureSpec : (II)I
    //   722: istore_1
    //   723: goto -> 595
  }
  
  private int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean) {
    if (sGetMaxAvailableHeightMethod != null)
      try {
        return ((Integer)sGetMaxAvailableHeightMethod.invoke(this.mPopup, new Object[] { paramView, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) })).intValue();
      } catch (Exception exception) {
        Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
      }  
    return this.mPopup.getMaxAvailableHeight(paramView, paramInt);
  }
  
  private static boolean isConfirmKey(int paramInt) {
    return (paramInt == 66 || paramInt == 23);
  }
  
  private void removePromptView() {
    if (this.mPromptView != null) {
      ViewParent viewParent = this.mPromptView.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(this.mPromptView); 
    } 
  }
  
  private void setPopupClipToScreenEnabled(boolean paramBoolean) {
    if (sClipToWindowEnabledMethod != null)
      try {
        sClipToWindowEnabledMethod.invoke(this.mPopup, new Object[] { Boolean.valueOf(paramBoolean) });
      } catch (Exception exception) {
        Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
      }  
  }
  
  public void clearListSelection() {
    DropDownListView dropDownListView = this.mDropDownList;
    if (dropDownListView != null) {
      dropDownListView.setListSelectionHidden(true);
      dropDownListView.requestLayout();
    } 
  }
  
  public View.OnTouchListener createDragToOpenListener(View paramView) {
    return new ForwardingListener(paramView) {
        public ListPopupWindow getPopup() {
          return ListPopupWindow.this;
        }
      };
  }
  
  @NonNull
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean) {
    return new DropDownListView(paramContext, paramBoolean);
  }
  
  public void dismiss() {
    this.mPopup.dismiss();
    removePromptView();
    this.mPopup.setContentView(null);
    this.mDropDownList = null;
    this.mHandler.removeCallbacks(this.mResizePopupRunnable);
  }
  
  @Nullable
  public View getAnchorView() {
    return this.mDropDownAnchorView;
  }
  
  @StyleRes
  public int getAnimationStyle() {
    return this.mPopup.getAnimationStyle();
  }
  
  @Nullable
  public Drawable getBackground() {
    return this.mPopup.getBackground();
  }
  
  public int getHeight() {
    return this.mDropDownHeight;
  }
  
  public int getHorizontalOffset() {
    return this.mDropDownHorizontalOffset;
  }
  
  public int getInputMethodMode() {
    return this.mPopup.getInputMethodMode();
  }
  
  @Nullable
  public ListView getListView() {
    return this.mDropDownList;
  }
  
  public int getPromptPosition() {
    return this.mPromptPosition;
  }
  
  @Nullable
  public Object getSelectedItem() {
    return !isShowing() ? null : this.mDropDownList.getSelectedItem();
  }
  
  public long getSelectedItemId() {
    return !isShowing() ? Long.MIN_VALUE : this.mDropDownList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition() {
    return !isShowing() ? -1 : this.mDropDownList.getSelectedItemPosition();
  }
  
  @Nullable
  public View getSelectedView() {
    return !isShowing() ? null : this.mDropDownList.getSelectedView();
  }
  
  public int getSoftInputMode() {
    return this.mPopup.getSoftInputMode();
  }
  
  public int getVerticalOffset() {
    return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
  }
  
  public int getWidth() {
    return this.mDropDownWidth;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isDropDownAlwaysVisible() {
    return this.mDropDownAlwaysVisible;
  }
  
  public boolean isInputMethodNotNeeded() {
    return (this.mPopup.getInputMethodMode() == 2);
  }
  
  public boolean isModal() {
    return this.mModal;
  }
  
  public boolean isShowing() {
    return this.mPopup.isShowing();
  }
  
  public boolean onKeyDown(int paramInt, @NonNull KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: invokevirtual isShowing : ()Z
    //   6: ifeq -> 288
    //   9: iload_1
    //   10: bipush #62
    //   12: if_icmpeq -> 288
    //   15: aload_0
    //   16: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   19: invokevirtual getSelectedItemPosition : ()I
    //   22: ifge -> 32
    //   25: iload_1
    //   26: invokestatic isConfirmKey : (I)Z
    //   29: ifne -> 288
    //   32: aload_0
    //   33: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   36: invokevirtual getSelectedItemPosition : ()I
    //   39: istore #4
    //   41: aload_0
    //   42: getfield mPopup : Landroid/widget/PopupWindow;
    //   45: invokevirtual isAboveAnchor : ()Z
    //   48: ifne -> 161
    //   51: iconst_1
    //   52: istore #5
    //   54: aload_0
    //   55: getfield mAdapter : Landroid/widget/ListAdapter;
    //   58: astore #6
    //   60: ldc 2147483647
    //   62: istore #7
    //   64: ldc_w -2147483648
    //   67: istore #8
    //   69: aload #6
    //   71: ifnull -> 107
    //   74: aload #6
    //   76: invokeinterface areAllItemsEnabled : ()Z
    //   81: istore #9
    //   83: iload #9
    //   85: ifeq -> 167
    //   88: iconst_0
    //   89: istore #7
    //   91: iload #9
    //   93: ifeq -> 181
    //   96: aload #6
    //   98: invokeinterface getCount : ()I
    //   103: iconst_1
    //   104: isub
    //   105: istore #8
    //   107: iload #5
    //   109: ifeq -> 125
    //   112: iload_1
    //   113: bipush #19
    //   115: if_icmpne -> 125
    //   118: iload #4
    //   120: iload #7
    //   122: if_icmple -> 143
    //   125: iload #5
    //   127: ifne -> 203
    //   130: iload_1
    //   131: bipush #20
    //   133: if_icmpne -> 203
    //   136: iload #4
    //   138: iload #8
    //   140: if_icmplt -> 203
    //   143: aload_0
    //   144: invokevirtual clearListSelection : ()V
    //   147: aload_0
    //   148: getfield mPopup : Landroid/widget/PopupWindow;
    //   151: iconst_1
    //   152: invokevirtual setInputMethodMode : (I)V
    //   155: aload_0
    //   156: invokevirtual show : ()V
    //   159: iload_3
    //   160: ireturn
    //   161: iconst_0
    //   162: istore #5
    //   164: goto -> 54
    //   167: aload_0
    //   168: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   171: iconst_0
    //   172: iconst_1
    //   173: invokevirtual lookForSelectablePosition : (IZ)I
    //   176: istore #7
    //   178: goto -> 91
    //   181: aload_0
    //   182: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   185: aload #6
    //   187: invokeinterface getCount : ()I
    //   192: iconst_1
    //   193: isub
    //   194: iconst_0
    //   195: invokevirtual lookForSelectablePosition : (IZ)I
    //   198: istore #8
    //   200: goto -> 107
    //   203: aload_0
    //   204: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   207: iconst_0
    //   208: invokevirtual setListSelectionHidden : (Z)V
    //   211: aload_0
    //   212: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   215: iload_1
    //   216: aload_2
    //   217: invokevirtual onKeyDown : (ILandroid/view/KeyEvent;)Z
    //   220: ifeq -> 293
    //   223: aload_0
    //   224: getfield mPopup : Landroid/widget/PopupWindow;
    //   227: iconst_2
    //   228: invokevirtual setInputMethodMode : (I)V
    //   231: aload_0
    //   232: getfield mDropDownList : Landroid/support/v7/widget/DropDownListView;
    //   235: invokevirtual requestFocusFromTouch : ()Z
    //   238: pop
    //   239: aload_0
    //   240: invokevirtual show : ()V
    //   243: iload_1
    //   244: lookupswitch default -> 288, 19 -> 159, 20 -> 159, 23 -> 159, 66 -> 159
    //   288: iconst_0
    //   289: istore_3
    //   290: goto -> 159
    //   293: iload #5
    //   295: ifeq -> 314
    //   298: iload_1
    //   299: bipush #20
    //   301: if_icmpne -> 314
    //   304: iload #4
    //   306: iload #8
    //   308: if_icmpne -> 288
    //   311: goto -> 159
    //   314: iload #5
    //   316: ifne -> 288
    //   319: iload_1
    //   320: bipush #19
    //   322: if_icmpne -> 288
    //   325: iload #4
    //   327: iload #7
    //   329: if_icmpne -> 288
    //   332: goto -> 159
  }
  
  public boolean onKeyPreIme(int paramInt, @NonNull KeyEvent paramKeyEvent) {
    boolean bool = true;
    if (paramInt == 4 && isShowing()) {
      KeyEvent.DispatcherState dispatcherState;
      View view = this.mDropDownAnchorView;
      if (paramKeyEvent.getAction() == 0 && paramKeyEvent.getRepeatCount() == 0) {
        dispatcherState = view.getKeyDispatcherState();
        boolean bool1 = bool;
        if (dispatcherState != null) {
          dispatcherState.startTracking(paramKeyEvent, this);
          bool1 = bool;
        } 
        return bool1;
      } 
      if (paramKeyEvent.getAction() == 1) {
        dispatcherState = dispatcherState.getKeyDispatcherState();
        if (dispatcherState != null)
          dispatcherState.handleUpEvent(paramKeyEvent); 
        if (paramKeyEvent.isTracking() && !paramKeyEvent.isCanceled()) {
          dismiss();
          return bool;
        } 
      } 
    } 
    return false;
  }
  
  public boolean onKeyUp(int paramInt, @NonNull KeyEvent paramKeyEvent) {
    if (isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
      boolean bool1 = this.mDropDownList.onKeyUp(paramInt, paramKeyEvent);
      boolean bool2 = bool1;
      if (bool1) {
        bool2 = bool1;
        if (isConfirmKey(paramInt)) {
          dismiss();
          bool2 = bool1;
        } 
      } 
      return bool2;
    } 
    return false;
  }
  
  public boolean performItemClick(int paramInt) {
    if (isShowing()) {
      if (this.mItemClickListener != null) {
        DropDownListView dropDownListView = this.mDropDownList;
        View view = dropDownListView.getChildAt(paramInt - dropDownListView.getFirstVisiblePosition());
        ListAdapter listAdapter = dropDownListView.getAdapter();
        this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, paramInt, listAdapter.getItemId(paramInt));
      } 
      return true;
    } 
    return false;
  }
  
  public void postShow() {
    this.mHandler.post(this.mShowDropDownRunnable);
  }
  
  public void setAdapter(@Nullable ListAdapter paramListAdapter) {
    if (this.mObserver == null) {
      this.mObserver = new PopupDataSetObserver();
    } else if (this.mAdapter != null) {
      this.mAdapter.unregisterDataSetObserver(this.mObserver);
    } 
    this.mAdapter = paramListAdapter;
    if (this.mAdapter != null)
      paramListAdapter.registerDataSetObserver(this.mObserver); 
    if (this.mDropDownList != null)
      this.mDropDownList.setAdapter(this.mAdapter); 
  }
  
  public void setAnchorView(@Nullable View paramView) {
    this.mDropDownAnchorView = paramView;
  }
  
  public void setAnimationStyle(@StyleRes int paramInt) {
    this.mPopup.setAnimationStyle(paramInt);
  }
  
  public void setBackgroundDrawable(@Nullable Drawable paramDrawable) {
    this.mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setContentWidth(int paramInt) {
    Drawable drawable = this.mPopup.getBackground();
    if (drawable != null) {
      drawable.getPadding(this.mTempRect);
      this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + paramInt;
      return;
    } 
    setWidth(paramInt);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setDropDownAlwaysVisible(boolean paramBoolean) {
    this.mDropDownAlwaysVisible = paramBoolean;
  }
  
  public void setDropDownGravity(int paramInt) {
    this.mDropDownGravity = paramInt;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setEpicenterBounds(Rect paramRect) {
    this.mEpicenterBounds = paramRect;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setForceIgnoreOutsideTouch(boolean paramBoolean) {
    this.mForceIgnoreOutsideTouch = paramBoolean;
  }
  
  public void setHeight(int paramInt) {
    this.mDropDownHeight = paramInt;
  }
  
  public void setHorizontalOffset(int paramInt) {
    this.mDropDownHorizontalOffset = paramInt;
  }
  
  public void setInputMethodMode(int paramInt) {
    this.mPopup.setInputMethodMode(paramInt);
  }
  
  void setListItemExpandMax(int paramInt) {
    this.mListItemExpandMaximum = paramInt;
  }
  
  public void setListSelector(Drawable paramDrawable) {
    this.mDropDownListHighlight = paramDrawable;
  }
  
  public void setModal(boolean paramBoolean) {
    this.mModal = paramBoolean;
    this.mPopup.setFocusable(paramBoolean);
  }
  
  public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener paramOnDismissListener) {
    this.mPopup.setOnDismissListener(paramOnDismissListener);
  }
  
  public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener paramOnItemClickListener) {
    this.mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener paramOnItemSelectedListener) {
    this.mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setPromptPosition(int paramInt) {
    this.mPromptPosition = paramInt;
  }
  
  public void setPromptView(@Nullable View paramView) {
    boolean bool = isShowing();
    if (bool)
      removePromptView(); 
    this.mPromptView = paramView;
    if (bool)
      show(); 
  }
  
  public void setSelection(int paramInt) {
    DropDownListView dropDownListView = this.mDropDownList;
    if (isShowing() && dropDownListView != null) {
      dropDownListView.setListSelectionHidden(false);
      dropDownListView.setSelection(paramInt);
      if (Build.VERSION.SDK_INT >= 11 && dropDownListView.getChoiceMode() != 0)
        dropDownListView.setItemChecked(paramInt, true); 
    } 
  }
  
  public void setSoftInputMode(int paramInt) {
    this.mPopup.setSoftInputMode(paramInt);
  }
  
  public void setVerticalOffset(int paramInt) {
    this.mDropDownVerticalOffset = paramInt;
    this.mDropDownVerticalOffsetSet = true;
  }
  
  public void setWidth(int paramInt) {
    this.mDropDownWidth = paramInt;
  }
  
  public void setWindowLayoutType(int paramInt) {
    this.mDropDownWindowLayoutType = paramInt;
  }
  
  public void show() {
    int j;
    boolean bool1 = true;
    boolean bool2 = false;
    byte b = -1;
    int i = buildDropDown();
    boolean bool = isInputMethodNotNeeded();
    PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
    if (this.mPopup.isShowing()) {
      if (this.mDropDownWidth == -1) {
        j = -1;
      } else if (this.mDropDownWidth == -2) {
        j = getAnchorView().getWidth();
      } else {
        j = this.mDropDownWidth;
      } 
      if (this.mDropDownHeight == -1) {
        if (!bool)
          i = -1; 
        if (bool) {
          boolean bool3;
          PopupWindow popupWindow3 = this.mPopup;
          if (this.mDropDownWidth == -1) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          popupWindow3.setWidth(bool3);
          this.mPopup.setHeight(0);
        } else {
          boolean bool3;
          PopupWindow popupWindow3 = this.mPopup;
          if (this.mDropDownWidth == -1) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          popupWindow3.setWidth(bool3);
          this.mPopup.setHeight(-1);
        } 
      } else if (this.mDropDownHeight != -2) {
        i = this.mDropDownHeight;
      } 
      PopupWindow popupWindow1 = this.mPopup;
      bool1 = bool2;
      if (!this.mForceIgnoreOutsideTouch) {
        bool1 = bool2;
        if (!this.mDropDownAlwaysVisible)
          bool1 = true; 
      } 
      popupWindow1.setOutsideTouchable(bool1);
      PopupWindow popupWindow2 = this.mPopup;
      View view = getAnchorView();
      int m = this.mDropDownHorizontalOffset;
      int k = this.mDropDownVerticalOffset;
      if (j < 0)
        j = -1; 
      if (i < 0)
        i = b; 
      popupWindow2.update(view, m, k, j, i);
      return;
    } 
    if (this.mDropDownWidth == -1) {
      j = -1;
    } else if (this.mDropDownWidth == -2) {
      j = getAnchorView().getWidth();
    } else {
      j = this.mDropDownWidth;
    } 
    if (this.mDropDownHeight == -1) {
      i = -1;
    } else if (this.mDropDownHeight != -2) {
      i = this.mDropDownHeight;
    } 
    this.mPopup.setWidth(j);
    this.mPopup.setHeight(i);
    setPopupClipToScreenEnabled(true);
    PopupWindow popupWindow = this.mPopup;
    if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible)
      bool1 = false; 
    popupWindow.setOutsideTouchable(bool1);
    this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
    if (sSetEpicenterBoundsMethod != null)
      try {
        sSetEpicenterBoundsMethod.invoke(this.mPopup, new Object[] { this.mEpicenterBounds });
      } catch (Exception exception) {
        Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", exception);
      }  
    PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
    this.mDropDownList.setSelection(-1);
    if (!this.mModal || this.mDropDownList.isInTouchMode())
      clearListSelection(); 
    if (!this.mModal)
      this.mHandler.post(this.mHideSelector); 
  }
  
  private class ListSelectorHider implements Runnable {
    public void run() {
      ListPopupWindow.this.clearListSelection();
    }
  }
  
  private class PopupDataSetObserver extends DataSetObserver {
    public void onChanged() {
      if (ListPopupWindow.this.isShowing())
        ListPopupWindow.this.show(); 
    }
    
    public void onInvalidated() {
      ListPopupWindow.this.dismiss();
    }
  }
  
  private class PopupScrollListener implements AbsListView.OnScrollListener {
    public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
      if (param1Int == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
        ListPopupWindow.this.mResizePopupRunnable.run();
      } 
    }
  }
  
  private class PopupTouchInterceptor implements View.OnTouchListener {
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      int i = param1MotionEvent.getAction();
      int j = (int)param1MotionEvent.getX();
      int k = (int)param1MotionEvent.getY();
      if (i == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && j >= 0 && j < ListPopupWindow.this.mPopup.getWidth() && k >= 0 && k < ListPopupWindow.this.mPopup.getHeight()) {
        ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
        return false;
      } 
      if (i == 1)
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable); 
      return false;
    }
  }
  
  private class ResizePopupRunnable implements Runnable {
    public void run() {
      if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow((View)ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
        ListPopupWindow.this.mPopup.setInputMethodMode(2);
        ListPopupWindow.this.show();
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ListPopupWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */