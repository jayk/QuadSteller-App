package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(4.0F * f);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   7: astore #5
    //   9: iload_3
    //   10: invokestatic getSize : (I)I
    //   13: iload #4
    //   15: isub
    //   16: iload_3
    //   17: invokestatic getMode : (I)I
    //   20: invokestatic makeMeasureSpec : (II)I
    //   23: istore #6
    //   25: aload_0
    //   26: instanceof android/support/v7/view/menu/ActionMenuItemView
    //   29: ifeq -> 176
    //   32: aload_0
    //   33: checkcast android/support/v7/view/menu/ActionMenuItemView
    //   36: astore #7
    //   38: aload #7
    //   40: ifnull -> 182
    //   43: aload #7
    //   45: invokevirtual hasText : ()Z
    //   48: ifeq -> 182
    //   51: iconst_1
    //   52: istore #4
    //   54: iconst_0
    //   55: istore #8
    //   57: iload #8
    //   59: istore_3
    //   60: iload_2
    //   61: ifle -> 131
    //   64: iload #4
    //   66: ifeq -> 77
    //   69: iload #8
    //   71: istore_3
    //   72: iload_2
    //   73: iconst_2
    //   74: if_icmplt -> 131
    //   77: aload_0
    //   78: iload_1
    //   79: iload_2
    //   80: imul
    //   81: ldc -2147483648
    //   83: invokestatic makeMeasureSpec : (II)I
    //   86: iload #6
    //   88: invokevirtual measure : (II)V
    //   91: aload_0
    //   92: invokevirtual getMeasuredWidth : ()I
    //   95: istore #8
    //   97: iload #8
    //   99: iload_1
    //   100: idiv
    //   101: istore_3
    //   102: iload_3
    //   103: istore_2
    //   104: iload #8
    //   106: iload_1
    //   107: irem
    //   108: ifeq -> 115
    //   111: iload_3
    //   112: iconst_1
    //   113: iadd
    //   114: istore_2
    //   115: iload_2
    //   116: istore_3
    //   117: iload #4
    //   119: ifeq -> 131
    //   122: iload_2
    //   123: istore_3
    //   124: iload_2
    //   125: iconst_2
    //   126: if_icmpge -> 131
    //   129: iconst_2
    //   130: istore_3
    //   131: aload #5
    //   133: getfield isOverflowButton : Z
    //   136: ifne -> 188
    //   139: iload #4
    //   141: ifeq -> 188
    //   144: iconst_1
    //   145: istore #9
    //   147: aload #5
    //   149: iload #9
    //   151: putfield expandable : Z
    //   154: aload #5
    //   156: iload_3
    //   157: putfield cellsUsed : I
    //   160: aload_0
    //   161: iload_3
    //   162: iload_1
    //   163: imul
    //   164: ldc 1073741824
    //   166: invokestatic makeMeasureSpec : (II)I
    //   169: iload #6
    //   171: invokevirtual measure : (II)V
    //   174: iload_3
    //   175: ireturn
    //   176: aconst_null
    //   177: astore #7
    //   179: goto -> 38
    //   182: iconst_0
    //   183: istore #4
    //   185: goto -> 54
    //   188: iconst_0
    //   189: istore #9
    //   191: goto -> 147
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_2
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_1
    //   6: invokestatic getSize : (I)I
    //   9: istore #4
    //   11: iload_2
    //   12: invokestatic getSize : (I)I
    //   15: istore #5
    //   17: aload_0
    //   18: invokevirtual getPaddingLeft : ()I
    //   21: istore #6
    //   23: aload_0
    //   24: invokevirtual getPaddingRight : ()I
    //   27: istore_1
    //   28: aload_0
    //   29: invokevirtual getPaddingTop : ()I
    //   32: aload_0
    //   33: invokevirtual getPaddingBottom : ()I
    //   36: iadd
    //   37: istore #7
    //   39: iload_2
    //   40: iload #7
    //   42: bipush #-2
    //   44: invokestatic getChildMeasureSpec : (III)I
    //   47: istore #8
    //   49: iload #4
    //   51: iload #6
    //   53: iload_1
    //   54: iadd
    //   55: isub
    //   56: istore #9
    //   58: iload #9
    //   60: aload_0
    //   61: getfield mMinCellSize : I
    //   64: idiv
    //   65: istore_1
    //   66: aload_0
    //   67: getfield mMinCellSize : I
    //   70: istore_2
    //   71: iload_1
    //   72: ifne -> 83
    //   75: aload_0
    //   76: iload #9
    //   78: iconst_0
    //   79: invokevirtual setMeasuredDimension : (II)V
    //   82: return
    //   83: aload_0
    //   84: getfield mMinCellSize : I
    //   87: iload #9
    //   89: iload_2
    //   90: irem
    //   91: iload_1
    //   92: idiv
    //   93: iadd
    //   94: istore #10
    //   96: iconst_0
    //   97: istore #6
    //   99: iconst_0
    //   100: istore #11
    //   102: iconst_0
    //   103: istore #12
    //   105: iconst_0
    //   106: istore #13
    //   108: iconst_0
    //   109: istore #4
    //   111: lconst_0
    //   112: lstore #14
    //   114: aload_0
    //   115: invokevirtual getChildCount : ()I
    //   118: istore #16
    //   120: iconst_0
    //   121: istore #17
    //   123: iload #17
    //   125: iload #16
    //   127: if_icmpge -> 431
    //   130: aload_0
    //   131: iload #17
    //   133: invokevirtual getChildAt : (I)Landroid/view/View;
    //   136: astore #18
    //   138: aload #18
    //   140: invokevirtual getVisibility : ()I
    //   143: bipush #8
    //   145: if_icmpne -> 170
    //   148: lload #14
    //   150: lstore #19
    //   152: iload #4
    //   154: istore #21
    //   156: iinc #17, 1
    //   159: iload #21
    //   161: istore #4
    //   163: lload #19
    //   165: lstore #14
    //   167: goto -> 123
    //   170: aload #18
    //   172: instanceof android/support/v7/view/menu/ActionMenuItemView
    //   175: istore #22
    //   177: iload #13
    //   179: iconst_1
    //   180: iadd
    //   181: istore #23
    //   183: iload #22
    //   185: ifeq -> 203
    //   188: aload #18
    //   190: aload_0
    //   191: getfield mGeneratedItemPadding : I
    //   194: iconst_0
    //   195: aload_0
    //   196: getfield mGeneratedItemPadding : I
    //   199: iconst_0
    //   200: invokevirtual setPadding : (IIII)V
    //   203: aload #18
    //   205: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   208: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   211: astore #24
    //   213: aload #24
    //   215: iconst_0
    //   216: putfield expanded : Z
    //   219: aload #24
    //   221: iconst_0
    //   222: putfield extraPixels : I
    //   225: aload #24
    //   227: iconst_0
    //   228: putfield cellsUsed : I
    //   231: aload #24
    //   233: iconst_0
    //   234: putfield expandable : Z
    //   237: aload #24
    //   239: iconst_0
    //   240: putfield leftMargin : I
    //   243: aload #24
    //   245: iconst_0
    //   246: putfield rightMargin : I
    //   249: iload #22
    //   251: ifeq -> 420
    //   254: aload #18
    //   256: checkcast android/support/v7/view/menu/ActionMenuItemView
    //   259: invokevirtual hasText : ()Z
    //   262: ifeq -> 420
    //   265: iconst_1
    //   266: istore #22
    //   268: aload #24
    //   270: iload #22
    //   272: putfield preventEdgeOffset : Z
    //   275: aload #24
    //   277: getfield isOverflowButton : Z
    //   280: ifeq -> 426
    //   283: iconst_1
    //   284: istore_2
    //   285: aload #18
    //   287: iload #10
    //   289: iload_2
    //   290: iload #8
    //   292: iload #7
    //   294: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   297: istore #25
    //   299: iload #11
    //   301: iload #25
    //   303: invokestatic max : (II)I
    //   306: istore #26
    //   308: iload #12
    //   310: istore_2
    //   311: aload #24
    //   313: getfield expandable : Z
    //   316: ifeq -> 324
    //   319: iload #12
    //   321: iconst_1
    //   322: iadd
    //   323: istore_2
    //   324: aload #24
    //   326: getfield isOverflowButton : Z
    //   329: ifeq -> 335
    //   332: iconst_1
    //   333: istore #4
    //   335: iload_1
    //   336: iload #25
    //   338: isub
    //   339: istore #27
    //   341: iload #6
    //   343: aload #18
    //   345: invokevirtual getMeasuredHeight : ()I
    //   348: invokestatic max : (II)I
    //   351: istore #28
    //   353: iload #27
    //   355: istore_1
    //   356: iload_2
    //   357: istore #12
    //   359: iload #4
    //   361: istore #21
    //   363: iload #26
    //   365: istore #11
    //   367: iload #28
    //   369: istore #6
    //   371: lload #14
    //   373: lstore #19
    //   375: iload #23
    //   377: istore #13
    //   379: iload #25
    //   381: iconst_1
    //   382: if_icmpne -> 156
    //   385: lload #14
    //   387: iconst_1
    //   388: iload #17
    //   390: ishl
    //   391: i2l
    //   392: lor
    //   393: lstore #19
    //   395: iload #27
    //   397: istore_1
    //   398: iload_2
    //   399: istore #12
    //   401: iload #4
    //   403: istore #21
    //   405: iload #26
    //   407: istore #11
    //   409: iload #28
    //   411: istore #6
    //   413: iload #23
    //   415: istore #13
    //   417: goto -> 156
    //   420: iconst_0
    //   421: istore #22
    //   423: goto -> 268
    //   426: iload_1
    //   427: istore_2
    //   428: goto -> 285
    //   431: iload #4
    //   433: ifeq -> 538
    //   436: iload #13
    //   438: iconst_2
    //   439: if_icmpne -> 538
    //   442: iconst_1
    //   443: istore #17
    //   445: iconst_0
    //   446: istore_2
    //   447: iload_1
    //   448: istore #21
    //   450: lload #14
    //   452: lstore #19
    //   454: iload #12
    //   456: ifle -> 634
    //   459: lload #14
    //   461: lstore #19
    //   463: iload #21
    //   465: ifle -> 634
    //   468: ldc 2147483647
    //   470: istore #23
    //   472: lconst_0
    //   473: lstore #29
    //   475: iconst_0
    //   476: istore #28
    //   478: iconst_0
    //   479: istore #27
    //   481: iload #27
    //   483: iload #16
    //   485: if_icmpge -> 616
    //   488: aload_0
    //   489: iload #27
    //   491: invokevirtual getChildAt : (I)Landroid/view/View;
    //   494: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   497: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   500: astore #24
    //   502: aload #24
    //   504: getfield expandable : Z
    //   507: ifne -> 544
    //   510: lload #29
    //   512: lstore #19
    //   514: iload #28
    //   516: istore_1
    //   517: iload #23
    //   519: istore #26
    //   521: iinc #27, 1
    //   524: iload #26
    //   526: istore #23
    //   528: iload_1
    //   529: istore #28
    //   531: lload #19
    //   533: lstore #29
    //   535: goto -> 481
    //   538: iconst_0
    //   539: istore #17
    //   541: goto -> 445
    //   544: aload #24
    //   546: getfield cellsUsed : I
    //   549: iload #23
    //   551: if_icmpge -> 573
    //   554: aload #24
    //   556: getfield cellsUsed : I
    //   559: istore #26
    //   561: iconst_1
    //   562: iload #27
    //   564: ishl
    //   565: i2l
    //   566: lstore #19
    //   568: iconst_1
    //   569: istore_1
    //   570: goto -> 521
    //   573: iload #23
    //   575: istore #26
    //   577: iload #28
    //   579: istore_1
    //   580: lload #29
    //   582: lstore #19
    //   584: aload #24
    //   586: getfield cellsUsed : I
    //   589: iload #23
    //   591: if_icmpne -> 521
    //   594: lload #29
    //   596: iconst_1
    //   597: iload #27
    //   599: ishl
    //   600: i2l
    //   601: lor
    //   602: lstore #19
    //   604: iload #28
    //   606: iconst_1
    //   607: iadd
    //   608: istore_1
    //   609: iload #23
    //   611: istore #26
    //   613: goto -> 521
    //   616: lload #14
    //   618: lload #29
    //   620: lor
    //   621: lstore #14
    //   623: iload #28
    //   625: iload #21
    //   627: if_icmple -> 846
    //   630: lload #14
    //   632: lstore #19
    //   634: iload #4
    //   636: ifne -> 999
    //   639: iload #13
    //   641: iconst_1
    //   642: if_icmpne -> 999
    //   645: iconst_1
    //   646: istore #4
    //   648: iload_2
    //   649: istore_1
    //   650: iload #21
    //   652: ifle -> 1155
    //   655: iload_2
    //   656: istore_1
    //   657: lload #19
    //   659: lconst_0
    //   660: lcmp
    //   661: ifeq -> 1155
    //   664: iload #21
    //   666: iload #13
    //   668: iconst_1
    //   669: isub
    //   670: if_icmplt -> 686
    //   673: iload #4
    //   675: ifne -> 686
    //   678: iload_2
    //   679: istore_1
    //   680: iload #11
    //   682: iconst_1
    //   683: if_icmple -> 1155
    //   686: lload #19
    //   688: invokestatic bitCount : (J)I
    //   691: i2f
    //   692: fstore #31
    //   694: fload #31
    //   696: fstore #32
    //   698: iload #4
    //   700: ifne -> 794
    //   703: fload #31
    //   705: fstore #33
    //   707: lconst_1
    //   708: lload #19
    //   710: land
    //   711: lconst_0
    //   712: lcmp
    //   713: ifeq -> 744
    //   716: fload #31
    //   718: fstore #33
    //   720: aload_0
    //   721: iconst_0
    //   722: invokevirtual getChildAt : (I)Landroid/view/View;
    //   725: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   728: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   731: getfield preventEdgeOffset : Z
    //   734: ifne -> 744
    //   737: fload #31
    //   739: ldc 0.5
    //   741: fsub
    //   742: fstore #33
    //   744: fload #33
    //   746: fstore #32
    //   748: iconst_1
    //   749: iload #16
    //   751: iconst_1
    //   752: isub
    //   753: ishl
    //   754: i2l
    //   755: lload #19
    //   757: land
    //   758: lconst_0
    //   759: lcmp
    //   760: ifeq -> 794
    //   763: fload #33
    //   765: fstore #32
    //   767: aload_0
    //   768: iload #16
    //   770: iconst_1
    //   771: isub
    //   772: invokevirtual getChildAt : (I)Landroid/view/View;
    //   775: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   778: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   781: getfield preventEdgeOffset : Z
    //   784: ifne -> 794
    //   787: fload #33
    //   789: ldc 0.5
    //   791: fsub
    //   792: fstore #32
    //   794: fload #32
    //   796: fconst_0
    //   797: fcmpl
    //   798: ifle -> 1005
    //   801: iload #21
    //   803: iload #10
    //   805: imul
    //   806: i2f
    //   807: fload #32
    //   809: fdiv
    //   810: f2i
    //   811: istore #4
    //   813: iconst_0
    //   814: istore #12
    //   816: iload #12
    //   818: iload #16
    //   820: if_icmpge -> 1153
    //   823: iconst_1
    //   824: iload #12
    //   826: ishl
    //   827: i2l
    //   828: lload #19
    //   830: land
    //   831: lconst_0
    //   832: lcmp
    //   833: ifne -> 1011
    //   836: iload_2
    //   837: istore_1
    //   838: iinc #12, 1
    //   841: iload_1
    //   842: istore_2
    //   843: goto -> 816
    //   846: iconst_0
    //   847: istore_1
    //   848: iload_1
    //   849: iload #16
    //   851: if_icmpge -> 994
    //   854: aload_0
    //   855: iload_1
    //   856: invokevirtual getChildAt : (I)Landroid/view/View;
    //   859: astore #24
    //   861: aload #24
    //   863: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   866: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   869: astore #18
    //   871: iconst_1
    //   872: iload_1
    //   873: ishl
    //   874: i2l
    //   875: lload #29
    //   877: land
    //   878: lconst_0
    //   879: lcmp
    //   880: ifne -> 927
    //   883: iload #21
    //   885: istore_2
    //   886: lload #14
    //   888: lstore #19
    //   890: aload #18
    //   892: getfield cellsUsed : I
    //   895: iload #23
    //   897: iconst_1
    //   898: iadd
    //   899: if_icmpne -> 914
    //   902: lload #14
    //   904: iconst_1
    //   905: iload_1
    //   906: ishl
    //   907: i2l
    //   908: lor
    //   909: lstore #19
    //   911: iload #21
    //   913: istore_2
    //   914: iinc #1, 1
    //   917: iload_2
    //   918: istore #21
    //   920: lload #19
    //   922: lstore #14
    //   924: goto -> 848
    //   927: iload #17
    //   929: ifeq -> 964
    //   932: aload #18
    //   934: getfield preventEdgeOffset : Z
    //   937: ifeq -> 964
    //   940: iload #21
    //   942: iconst_1
    //   943: if_icmpne -> 964
    //   946: aload #24
    //   948: aload_0
    //   949: getfield mGeneratedItemPadding : I
    //   952: iload #10
    //   954: iadd
    //   955: iconst_0
    //   956: aload_0
    //   957: getfield mGeneratedItemPadding : I
    //   960: iconst_0
    //   961: invokevirtual setPadding : (IIII)V
    //   964: aload #18
    //   966: aload #18
    //   968: getfield cellsUsed : I
    //   971: iconst_1
    //   972: iadd
    //   973: putfield cellsUsed : I
    //   976: aload #18
    //   978: iconst_1
    //   979: putfield expanded : Z
    //   982: iload #21
    //   984: iconst_1
    //   985: isub
    //   986: istore_2
    //   987: lload #14
    //   989: lstore #19
    //   991: goto -> 914
    //   994: iconst_1
    //   995: istore_2
    //   996: goto -> 450
    //   999: iconst_0
    //   1000: istore #4
    //   1002: goto -> 648
    //   1005: iconst_0
    //   1006: istore #4
    //   1008: goto -> 813
    //   1011: aload_0
    //   1012: iload #12
    //   1014: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1017: astore #24
    //   1019: aload #24
    //   1021: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1024: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   1027: astore #18
    //   1029: aload #24
    //   1031: instanceof android/support/v7/view/menu/ActionMenuItemView
    //   1034: ifeq -> 1078
    //   1037: aload #18
    //   1039: iload #4
    //   1041: putfield extraPixels : I
    //   1044: aload #18
    //   1046: iconst_1
    //   1047: putfield expanded : Z
    //   1050: iload #12
    //   1052: ifne -> 1073
    //   1055: aload #18
    //   1057: getfield preventEdgeOffset : Z
    //   1060: ifne -> 1073
    //   1063: aload #18
    //   1065: iload #4
    //   1067: ineg
    //   1068: iconst_2
    //   1069: idiv
    //   1070: putfield leftMargin : I
    //   1073: iconst_1
    //   1074: istore_1
    //   1075: goto -> 838
    //   1078: aload #18
    //   1080: getfield isOverflowButton : Z
    //   1083: ifeq -> 1114
    //   1086: aload #18
    //   1088: iload #4
    //   1090: putfield extraPixels : I
    //   1093: aload #18
    //   1095: iconst_1
    //   1096: putfield expanded : Z
    //   1099: aload #18
    //   1101: iload #4
    //   1103: ineg
    //   1104: iconst_2
    //   1105: idiv
    //   1106: putfield rightMargin : I
    //   1109: iconst_1
    //   1110: istore_1
    //   1111: goto -> 838
    //   1114: iload #12
    //   1116: ifeq -> 1128
    //   1119: aload #18
    //   1121: iload #4
    //   1123: iconst_2
    //   1124: idiv
    //   1125: putfield leftMargin : I
    //   1128: iload_2
    //   1129: istore_1
    //   1130: iload #12
    //   1132: iload #16
    //   1134: iconst_1
    //   1135: isub
    //   1136: if_icmpeq -> 838
    //   1139: aload #18
    //   1141: iload #4
    //   1143: iconst_2
    //   1144: idiv
    //   1145: putfield rightMargin : I
    //   1148: iload_2
    //   1149: istore_1
    //   1150: goto -> 838
    //   1153: iload_2
    //   1154: istore_1
    //   1155: iload_1
    //   1156: ifeq -> 1227
    //   1159: iconst_0
    //   1160: istore_1
    //   1161: iload_1
    //   1162: iload #16
    //   1164: if_icmpge -> 1227
    //   1167: aload_0
    //   1168: iload_1
    //   1169: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1172: astore #24
    //   1174: aload #24
    //   1176: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1179: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   1182: astore #18
    //   1184: aload #18
    //   1186: getfield expanded : Z
    //   1189: ifne -> 1198
    //   1192: iinc #1, 1
    //   1195: goto -> 1161
    //   1198: aload #24
    //   1200: aload #18
    //   1202: getfield cellsUsed : I
    //   1205: iload #10
    //   1207: imul
    //   1208: aload #18
    //   1210: getfield extraPixels : I
    //   1213: iadd
    //   1214: ldc 1073741824
    //   1216: invokestatic makeMeasureSpec : (II)I
    //   1219: iload #8
    //   1221: invokevirtual measure : (II)V
    //   1224: goto -> 1192
    //   1227: iload #5
    //   1229: istore_1
    //   1230: iload_3
    //   1231: ldc 1073741824
    //   1233: if_icmpeq -> 1239
    //   1236: iload #6
    //   1238: istore_1
    //   1239: aload_0
    //   1240: iload #9
    //   1242: iload_1
    //   1243: invokevirtual setMeasuredDimension : (II)V
    //   1246: goto -> 82
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams != null && paramLayoutParams instanceof LayoutParams);
  }
  
  public void dismissPopupMenus() {
    if (this.mPresenter != null)
      this.mPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams1;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams1 = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams1 = new LayoutParams((ViewGroup.LayoutParams)layoutParams1);
      } 
      LayoutParams layoutParams2 = layoutParams1;
      if (layoutParams1.gravity <= 0) {
        layoutParams1.gravity = 16;
        layoutParams2 = layoutParams1;
      } 
      return layoutParams2;
    } 
    return generateDefaultLayoutParams();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      MenuPresenter.Callback callback;
      Context context = getContext();
      this.mMenu = new MenuBuilder(context);
      this.mMenu.setCallback(new MenuBuilderCallback());
      this.mPresenter = new ActionMenuPresenter(context);
      this.mPresenter.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter = this.mPresenter;
      if (this.mActionMenuPresenterCallback != null) {
        callback = this.mActionMenuPresenterCallback;
      } else {
        callback = new ActionMenuPresenterCallback();
      } 
      actionMenuPresenter.setCallback(callback);
      this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return (Menu)this.mMenu;
  }
  
  @Nullable
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getWindowAnimations() {
    return 0;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    boolean bool;
    if (paramInt == 0)
      return false; 
    View view1 = getChildAt(paramInt - 1);
    View view2 = getChildAt(paramInt);
    int i = 0;
    int j = i;
    if (paramInt < getChildCount()) {
      j = i;
      if (view1 instanceof ActionMenuChildView)
        j = false | ((ActionMenuChildView)view1).needsDividerAfter(); 
    } 
    i = j;
    if (paramInt > 0) {
      i = j;
      if (view2 instanceof ActionMenuChildView)
        bool = j | ((ActionMenuChildView)view2).needsDividerBefore(); 
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    return (this.mPresenter != null && this.mPresenter.hideOverflowMenu());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
    return this.mMenu.performItemAction((MenuItem)paramMenuItemImpl, 0);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowMenuShowPending() {
    return (this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    if (this.mPresenter != null) {
      this.mPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int i = getChildCount();
    int j = (paramInt4 - paramInt2) / 2;
    int k = getDividerWidth();
    int m = 0;
    paramInt4 = 0;
    paramInt2 = paramInt3 - paramInt1 - getPaddingRight() - getPaddingLeft();
    int n = 0;
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    int i1;
    for (i1 = 0; i1 < i; i1++) {
      View view = getChildAt(i1);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i4;
          int i2 = view.getMeasuredWidth();
          n = i2;
          if (hasSupportDividerBeforeChildAt(i1))
            n = i2 + k; 
          int i3 = view.getMeasuredHeight();
          if (paramBoolean) {
            i2 = getPaddingLeft() + layoutParams.leftMargin;
            i4 = i2 + n;
          } else {
            i4 = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            i2 = i4 - n;
          } 
          int i5 = j - i3 / 2;
          view.layout(i2, i5, i4, i5 + i3);
          paramInt2 -= n;
          n = 1;
        } else {
          int i2 = view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          m += i2;
          i2 = paramInt2 - i2;
          paramInt2 = m;
          if (hasSupportDividerBeforeChildAt(i1))
            paramInt2 = m + k; 
          paramInt4++;
          m = paramInt2;
          paramInt2 = i2;
        } 
      } 
    } 
    if (i == 1 && n == 0) {
      View view = getChildAt(0);
      paramInt2 = view.getMeasuredWidth();
      paramInt4 = view.getMeasuredHeight();
      paramInt1 = (paramInt3 - paramInt1) / 2 - paramInt2 / 2;
      paramInt3 = j - paramInt4 / 2;
      view.layout(paramInt1, paramInt3, paramInt1 + paramInt2, paramInt3 + paramInt4);
      return;
    } 
    if (n != 0) {
      paramInt1 = 0;
    } else {
      paramInt1 = 1;
    } 
    paramInt1 = paramInt4 - paramInt1;
    if (paramInt1 > 0) {
      paramInt1 = paramInt2 / paramInt1;
    } else {
      paramInt1 = 0;
    } 
    paramInt4 = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt3 = getWidth() - getPaddingRight();
      paramInt1 = 0;
      while (true) {
        if (paramInt1 < i) {
          View view = getChildAt(paramInt1);
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt2 = paramInt3;
          if (view.getVisibility() != 8)
            if (layoutParams.isOverflowButton) {
              paramInt2 = paramInt3;
            } else {
              m = paramInt3 - layoutParams.rightMargin;
              i1 = view.getMeasuredWidth();
              paramInt3 = view.getMeasuredHeight();
              paramInt2 = j - paramInt3 / 2;
              view.layout(m - i1, paramInt2, m, paramInt2 + paramInt3);
              paramInt2 = m - layoutParams.leftMargin + i1 + paramInt4;
            }  
          paramInt1++;
          paramInt3 = paramInt2;
          continue;
        } 
        return;
      } 
    } 
    paramInt2 = getPaddingLeft();
    paramInt1 = 0;
    while (true) {
      if (paramInt1 < i) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            m = paramInt2 + layoutParams.leftMargin;
            paramInt2 = view.getMeasuredWidth();
            i1 = view.getMeasuredHeight();
            paramInt3 = j - i1 / 2;
            view.layout(m, paramInt3, m + paramInt2, paramInt3 + i1);
            paramInt3 = m + layoutParams.rightMargin + paramInt2 + paramInt4;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
        continue;
      } 
      return;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mFormatItems = bool1;
    if (bool != this.mFormatItems)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems && this.mMenu != null && i != this.mFormatItemsWidth) {
      this.mFormatItemsWidth = i;
      this.mMenu.onItemsChanged(true);
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
      return;
    } 
    for (i = 0; i < j; i++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
      layoutParams.rightMargin = 0;
      layoutParams.leftMargin = 0;
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public MenuBuilder peekMenu() {
    return this.mMenu;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setOverflowReserved(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(@StyleRes int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
        return;
      } 
    } else {
      return;
    } 
    this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    this.mPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    return (this.mPresenter != null && this.mPresenter.showOverflowMenu());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      return false;
    }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
      return (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem));
    }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ActionMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */