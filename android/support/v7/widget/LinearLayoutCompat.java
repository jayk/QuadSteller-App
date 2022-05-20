package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        if (bool) {
          j = getPaddingLeft();
        } else {
          j = getWidth() - getPaddingRight() - this.mDividerWidth;
        } 
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          j = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } else {
          j = view.getRight() + layoutParams.rightMargin;
        } 
      } 
      drawVerticalDivider(paramCanvas, j);
    } 
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        j = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        j = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, j);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -2) : ((this.mOrientation == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    // Byte code:
    //   0: iconst_m1
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mBaselineAlignedChildIndex : I
    //   6: ifge -> 16
    //   9: aload_0
    //   10: invokespecial getBaseline : ()I
    //   13: istore_1
    //   14: iload_1
    //   15: ireturn
    //   16: aload_0
    //   17: invokevirtual getChildCount : ()I
    //   20: aload_0
    //   21: getfield mBaselineAlignedChildIndex : I
    //   24: if_icmpgt -> 38
    //   27: new java/lang/RuntimeException
    //   30: dup
    //   31: ldc_w 'mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.'
    //   34: invokespecial <init> : (Ljava/lang/String;)V
    //   37: athrow
    //   38: aload_0
    //   39: aload_0
    //   40: getfield mBaselineAlignedChildIndex : I
    //   43: invokevirtual getChildAt : (I)Landroid/view/View;
    //   46: astore_2
    //   47: aload_2
    //   48: invokevirtual getBaseline : ()I
    //   51: istore_3
    //   52: iload_3
    //   53: iconst_m1
    //   54: if_icmpne -> 75
    //   57: aload_0
    //   58: getfield mBaselineAlignedChildIndex : I
    //   61: ifeq -> 14
    //   64: new java/lang/RuntimeException
    //   67: dup
    //   68: ldc_w 'mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.'
    //   71: invokespecial <init> : (Ljava/lang/String;)V
    //   74: athrow
    //   75: aload_0
    //   76: getfield mBaselineChildTop : I
    //   79: istore #4
    //   81: iload #4
    //   83: istore_1
    //   84: aload_0
    //   85: getfield mOrientation : I
    //   88: iconst_1
    //   89: if_icmpne -> 143
    //   92: aload_0
    //   93: getfield mGravity : I
    //   96: bipush #112
    //   98: iand
    //   99: istore #5
    //   101: iload #4
    //   103: istore_1
    //   104: iload #5
    //   106: bipush #48
    //   108: if_icmpeq -> 143
    //   111: iload #5
    //   113: lookupswitch default -> 140, 16 -> 184, 80 -> 161
    //   140: iload #4
    //   142: istore_1
    //   143: aload_2
    //   144: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   147: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   150: getfield topMargin : I
    //   153: iload_1
    //   154: iadd
    //   155: iload_3
    //   156: iadd
    //   157: istore_1
    //   158: goto -> 14
    //   161: aload_0
    //   162: invokevirtual getBottom : ()I
    //   165: aload_0
    //   166: invokevirtual getTop : ()I
    //   169: isub
    //   170: aload_0
    //   171: invokevirtual getPaddingBottom : ()I
    //   174: isub
    //   175: aload_0
    //   176: getfield mTotalLength : I
    //   179: isub
    //   180: istore_1
    //   181: goto -> 143
    //   184: iload #4
    //   186: aload_0
    //   187: invokevirtual getBottom : ()I
    //   190: aload_0
    //   191: invokevirtual getTop : ()I
    //   194: isub
    //   195: aload_0
    //   196: invokevirtual getPaddingTop : ()I
    //   199: isub
    //   200: aload_0
    //   201: invokevirtual getPaddingBottom : ()I
    //   204: isub
    //   205: aload_0
    //   206: getfield mTotalLength : I
    //   209: isub
    //   210: iconst_2
    //   211: idiv
    //   212: iadd
    //   213: istore_1
    //   214: goto -> 143
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    null = true;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) == 0)
        null = false; 
      return null;
    } 
    if (paramInt == getChildCount()) {
      if ((this.mShowDividers & 0x4) == 0)
        null = false; 
      return null;
    } 
    if ((this.mShowDividers & 0x2) != 0) {
      boolean bool = false;
      paramInt--;
      while (true) {
        null = bool;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8)
            return true; 
          paramInt--;
          continue;
        } 
        return null;
      } 
    } 
    return false;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic isLayoutRtl : (Landroid/view/View;)Z
    //   4: istore #5
    //   6: aload_0
    //   7: invokevirtual getPaddingTop : ()I
    //   10: istore #6
    //   12: iload #4
    //   14: iload_2
    //   15: isub
    //   16: istore #7
    //   18: aload_0
    //   19: invokevirtual getPaddingBottom : ()I
    //   22: istore #8
    //   24: aload_0
    //   25: invokevirtual getPaddingBottom : ()I
    //   28: istore #9
    //   30: aload_0
    //   31: invokevirtual getVirtualChildCount : ()I
    //   34: istore #10
    //   36: aload_0
    //   37: getfield mGravity : I
    //   40: istore_2
    //   41: aload_0
    //   42: getfield mGravity : I
    //   45: istore #11
    //   47: aload_0
    //   48: getfield mBaselineAligned : Z
    //   51: istore #12
    //   53: aload_0
    //   54: getfield mMaxAscent : [I
    //   57: astore #13
    //   59: aload_0
    //   60: getfield mMaxDescent : [I
    //   63: astore #14
    //   65: iload_2
    //   66: ldc_w 8388615
    //   69: iand
    //   70: aload_0
    //   71: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   74: invokestatic getAbsoluteGravity : (II)I
    //   77: lookupswitch default -> 104, 1 -> 200, 5 -> 183
    //   104: aload_0
    //   105: invokevirtual getPaddingLeft : ()I
    //   108: istore_1
    //   109: iconst_0
    //   110: istore #4
    //   112: iconst_1
    //   113: istore #15
    //   115: iload #5
    //   117: ifeq -> 129
    //   120: iload #10
    //   122: iconst_1
    //   123: isub
    //   124: istore #4
    //   126: iconst_m1
    //   127: istore #15
    //   129: iconst_0
    //   130: istore_2
    //   131: iload_1
    //   132: istore_3
    //   133: iload_2
    //   134: iload #10
    //   136: if_icmpge -> 544
    //   139: iload #4
    //   141: iload #15
    //   143: iload_2
    //   144: imul
    //   145: iadd
    //   146: istore #16
    //   148: aload_0
    //   149: iload #16
    //   151: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   154: astore #17
    //   156: aload #17
    //   158: ifnonnull -> 219
    //   161: iload_3
    //   162: aload_0
    //   163: iload #16
    //   165: invokevirtual measureNullChild : (I)I
    //   168: iadd
    //   169: istore_1
    //   170: iload_2
    //   171: istore #18
    //   173: iload #18
    //   175: iconst_1
    //   176: iadd
    //   177: istore_2
    //   178: iload_1
    //   179: istore_3
    //   180: goto -> 133
    //   183: aload_0
    //   184: invokevirtual getPaddingLeft : ()I
    //   187: iload_3
    //   188: iadd
    //   189: iload_1
    //   190: isub
    //   191: aload_0
    //   192: getfield mTotalLength : I
    //   195: isub
    //   196: istore_1
    //   197: goto -> 109
    //   200: aload_0
    //   201: invokevirtual getPaddingLeft : ()I
    //   204: iload_3
    //   205: iload_1
    //   206: isub
    //   207: aload_0
    //   208: getfield mTotalLength : I
    //   211: isub
    //   212: iconst_2
    //   213: idiv
    //   214: iadd
    //   215: istore_1
    //   216: goto -> 109
    //   219: iload_3
    //   220: istore_1
    //   221: iload_2
    //   222: istore #18
    //   224: aload #17
    //   226: invokevirtual getVisibility : ()I
    //   229: bipush #8
    //   231: if_icmpeq -> 173
    //   234: aload #17
    //   236: invokevirtual getMeasuredWidth : ()I
    //   239: istore #19
    //   241: aload #17
    //   243: invokevirtual getMeasuredHeight : ()I
    //   246: istore #20
    //   248: iconst_m1
    //   249: istore_1
    //   250: aload #17
    //   252: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   255: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   258: astore #21
    //   260: iload_1
    //   261: istore #18
    //   263: iload #12
    //   265: ifeq -> 287
    //   268: iload_1
    //   269: istore #18
    //   271: aload #21
    //   273: getfield height : I
    //   276: iconst_m1
    //   277: if_icmpeq -> 287
    //   280: aload #17
    //   282: invokevirtual getBaseline : ()I
    //   285: istore #18
    //   287: aload #21
    //   289: getfield gravity : I
    //   292: istore #22
    //   294: iload #22
    //   296: istore_1
    //   297: iload #22
    //   299: ifge -> 308
    //   302: iload #11
    //   304: bipush #112
    //   306: iand
    //   307: istore_1
    //   308: iload_1
    //   309: bipush #112
    //   311: iand
    //   312: lookupswitch default -> 348, 16 -> 465, 48 -> 432, 80 -> 497
    //   348: iload #6
    //   350: istore_1
    //   351: iload_3
    //   352: istore #18
    //   354: aload_0
    //   355: iload #16
    //   357: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   360: ifeq -> 371
    //   363: iload_3
    //   364: aload_0
    //   365: getfield mDividerWidth : I
    //   368: iadd
    //   369: istore #18
    //   371: iload #18
    //   373: aload #21
    //   375: getfield leftMargin : I
    //   378: iadd
    //   379: istore_3
    //   380: aload_0
    //   381: aload #17
    //   383: iload_3
    //   384: aload_0
    //   385: aload #17
    //   387: invokevirtual getLocationOffset : (Landroid/view/View;)I
    //   390: iadd
    //   391: iload_1
    //   392: iload #19
    //   394: iload #20
    //   396: invokespecial setChildFrame : (Landroid/view/View;IIII)V
    //   399: iload_3
    //   400: aload #21
    //   402: getfield rightMargin : I
    //   405: iload #19
    //   407: iadd
    //   408: aload_0
    //   409: aload #17
    //   411: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   414: iadd
    //   415: iadd
    //   416: istore_1
    //   417: iload_2
    //   418: aload_0
    //   419: aload #17
    //   421: iload #16
    //   423: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   426: iadd
    //   427: istore #18
    //   429: goto -> 173
    //   432: iload #6
    //   434: aload #21
    //   436: getfield topMargin : I
    //   439: iadd
    //   440: istore #22
    //   442: iload #22
    //   444: istore_1
    //   445: iload #18
    //   447: iconst_m1
    //   448: if_icmpeq -> 351
    //   451: iload #22
    //   453: aload #13
    //   455: iconst_1
    //   456: iaload
    //   457: iload #18
    //   459: isub
    //   460: iadd
    //   461: istore_1
    //   462: goto -> 351
    //   465: iload #7
    //   467: iload #6
    //   469: isub
    //   470: iload #9
    //   472: isub
    //   473: iload #20
    //   475: isub
    //   476: iconst_2
    //   477: idiv
    //   478: iload #6
    //   480: iadd
    //   481: aload #21
    //   483: getfield topMargin : I
    //   486: iadd
    //   487: aload #21
    //   489: getfield bottomMargin : I
    //   492: isub
    //   493: istore_1
    //   494: goto -> 351
    //   497: iload #7
    //   499: iload #8
    //   501: isub
    //   502: iload #20
    //   504: isub
    //   505: aload #21
    //   507: getfield bottomMargin : I
    //   510: isub
    //   511: istore #22
    //   513: iload #22
    //   515: istore_1
    //   516: iload #18
    //   518: iconst_m1
    //   519: if_icmpeq -> 351
    //   522: aload #17
    //   524: invokevirtual getMeasuredHeight : ()I
    //   527: istore_1
    //   528: iload #22
    //   530: aload #14
    //   532: iconst_2
    //   533: iaload
    //   534: iload_1
    //   535: iload #18
    //   537: isub
    //   538: isub
    //   539: isub
    //   540: istore_1
    //   541: goto -> 351
    //   544: return
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getPaddingLeft : ()I
    //   4: istore #5
    //   6: iload_3
    //   7: iload_1
    //   8: isub
    //   9: istore #6
    //   11: aload_0
    //   12: invokevirtual getPaddingRight : ()I
    //   15: istore #7
    //   17: aload_0
    //   18: invokevirtual getPaddingRight : ()I
    //   21: istore #8
    //   23: aload_0
    //   24: invokevirtual getVirtualChildCount : ()I
    //   27: istore #9
    //   29: aload_0
    //   30: getfield mGravity : I
    //   33: istore_1
    //   34: aload_0
    //   35: getfield mGravity : I
    //   38: istore #10
    //   40: iload_1
    //   41: bipush #112
    //   43: iand
    //   44: lookupswitch default -> 72, 16 -> 136, 80 -> 118
    //   72: aload_0
    //   73: invokevirtual getPaddingTop : ()I
    //   76: istore_1
    //   77: iconst_0
    //   78: istore_2
    //   79: iload_2
    //   80: iload #9
    //   82: if_icmpge -> 394
    //   85: aload_0
    //   86: iload_2
    //   87: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   90: astore #11
    //   92: aload #11
    //   94: ifnonnull -> 156
    //   97: iload_1
    //   98: aload_0
    //   99: iload_2
    //   100: invokevirtual measureNullChild : (I)I
    //   103: iadd
    //   104: istore_3
    //   105: iload_2
    //   106: istore #4
    //   108: iload #4
    //   110: iconst_1
    //   111: iadd
    //   112: istore_2
    //   113: iload_3
    //   114: istore_1
    //   115: goto -> 79
    //   118: aload_0
    //   119: invokevirtual getPaddingTop : ()I
    //   122: iload #4
    //   124: iadd
    //   125: iload_2
    //   126: isub
    //   127: aload_0
    //   128: getfield mTotalLength : I
    //   131: isub
    //   132: istore_1
    //   133: goto -> 77
    //   136: aload_0
    //   137: invokevirtual getPaddingTop : ()I
    //   140: iload #4
    //   142: iload_2
    //   143: isub
    //   144: aload_0
    //   145: getfield mTotalLength : I
    //   148: isub
    //   149: iconst_2
    //   150: idiv
    //   151: iadd
    //   152: istore_1
    //   153: goto -> 77
    //   156: iload_1
    //   157: istore_3
    //   158: iload_2
    //   159: istore #4
    //   161: aload #11
    //   163: invokevirtual getVisibility : ()I
    //   166: bipush #8
    //   168: if_icmpeq -> 108
    //   171: aload #11
    //   173: invokevirtual getMeasuredWidth : ()I
    //   176: istore #12
    //   178: aload #11
    //   180: invokevirtual getMeasuredHeight : ()I
    //   183: istore #13
    //   185: aload #11
    //   187: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   190: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   193: astore #14
    //   195: aload #14
    //   197: getfield gravity : I
    //   200: istore #4
    //   202: iload #4
    //   204: istore_3
    //   205: iload #4
    //   207: ifge -> 217
    //   210: iload #10
    //   212: ldc_w 8388615
    //   215: iand
    //   216: istore_3
    //   217: iload_3
    //   218: aload_0
    //   219: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   222: invokestatic getAbsoluteGravity : (II)I
    //   225: bipush #7
    //   227: iand
    //   228: lookupswitch default -> 256, 1 -> 344, 5 -> 376
    //   256: iload #5
    //   258: aload #14
    //   260: getfield leftMargin : I
    //   263: iadd
    //   264: istore_3
    //   265: iload_1
    //   266: istore #4
    //   268: aload_0
    //   269: iload_2
    //   270: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   273: ifeq -> 284
    //   276: iload_1
    //   277: aload_0
    //   278: getfield mDividerHeight : I
    //   281: iadd
    //   282: istore #4
    //   284: iload #4
    //   286: aload #14
    //   288: getfield topMargin : I
    //   291: iadd
    //   292: istore_1
    //   293: aload_0
    //   294: aload #11
    //   296: iload_3
    //   297: iload_1
    //   298: aload_0
    //   299: aload #11
    //   301: invokevirtual getLocationOffset : (Landroid/view/View;)I
    //   304: iadd
    //   305: iload #12
    //   307: iload #13
    //   309: invokespecial setChildFrame : (Landroid/view/View;IIII)V
    //   312: iload_1
    //   313: aload #14
    //   315: getfield bottomMargin : I
    //   318: iload #13
    //   320: iadd
    //   321: aload_0
    //   322: aload #11
    //   324: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   327: iadd
    //   328: iadd
    //   329: istore_3
    //   330: iload_2
    //   331: aload_0
    //   332: aload #11
    //   334: iload_2
    //   335: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   338: iadd
    //   339: istore #4
    //   341: goto -> 108
    //   344: iload #6
    //   346: iload #5
    //   348: isub
    //   349: iload #8
    //   351: isub
    //   352: iload #12
    //   354: isub
    //   355: iconst_2
    //   356: idiv
    //   357: iload #5
    //   359: iadd
    //   360: aload #14
    //   362: getfield leftMargin : I
    //   365: iadd
    //   366: aload #14
    //   368: getfield rightMargin : I
    //   371: isub
    //   372: istore_3
    //   373: goto -> 265
    //   376: iload #6
    //   378: iload #7
    //   380: isub
    //   381: iload #12
    //   383: isub
    //   384: aload #14
    //   386: getfield rightMargin : I
    //   389: isub
    //   390: istore_3
    //   391: goto -> 265
    //   394: return
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: iconst_0
    //   6: istore_3
    //   7: iconst_0
    //   8: istore #4
    //   10: iconst_0
    //   11: istore #5
    //   13: iconst_0
    //   14: istore #6
    //   16: iconst_1
    //   17: istore #7
    //   19: fconst_0
    //   20: fstore #8
    //   22: aload_0
    //   23: invokevirtual getVirtualChildCount : ()I
    //   26: istore #9
    //   28: iload_1
    //   29: invokestatic getMode : (I)I
    //   32: istore #10
    //   34: iload_2
    //   35: invokestatic getMode : (I)I
    //   38: istore #11
    //   40: iconst_0
    //   41: istore #12
    //   43: iconst_0
    //   44: istore #13
    //   46: aload_0
    //   47: getfield mMaxAscent : [I
    //   50: ifnull -> 60
    //   53: aload_0
    //   54: getfield mMaxDescent : [I
    //   57: ifnonnull -> 74
    //   60: aload_0
    //   61: iconst_4
    //   62: newarray int
    //   64: putfield mMaxAscent : [I
    //   67: aload_0
    //   68: iconst_4
    //   69: newarray int
    //   71: putfield mMaxDescent : [I
    //   74: aload_0
    //   75: getfield mMaxAscent : [I
    //   78: astore #14
    //   80: aload_0
    //   81: getfield mMaxDescent : [I
    //   84: astore #15
    //   86: aload #14
    //   88: iconst_3
    //   89: iconst_m1
    //   90: iastore
    //   91: aload #14
    //   93: iconst_2
    //   94: iconst_m1
    //   95: iastore
    //   96: aload #14
    //   98: iconst_1
    //   99: iconst_m1
    //   100: iastore
    //   101: aload #14
    //   103: iconst_0
    //   104: iconst_m1
    //   105: iastore
    //   106: aload #15
    //   108: iconst_3
    //   109: iconst_m1
    //   110: iastore
    //   111: aload #15
    //   113: iconst_2
    //   114: iconst_m1
    //   115: iastore
    //   116: aload #15
    //   118: iconst_1
    //   119: iconst_m1
    //   120: iastore
    //   121: aload #15
    //   123: iconst_0
    //   124: iconst_m1
    //   125: iastore
    //   126: aload_0
    //   127: getfield mBaselineAligned : Z
    //   130: istore #16
    //   132: aload_0
    //   133: getfield mUseLargestChild : Z
    //   136: istore #17
    //   138: iload #10
    //   140: ldc 1073741824
    //   142: if_icmpne -> 205
    //   145: iconst_1
    //   146: istore #18
    //   148: ldc_w -2147483648
    //   151: istore #19
    //   153: iconst_0
    //   154: istore #20
    //   156: iload #20
    //   158: iload #9
    //   160: if_icmpge -> 883
    //   163: aload_0
    //   164: iload #20
    //   166: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   169: astore #21
    //   171: aload #21
    //   173: ifnonnull -> 211
    //   176: aload_0
    //   177: aload_0
    //   178: getfield mTotalLength : I
    //   181: aload_0
    //   182: iload #20
    //   184: invokevirtual measureNullChild : (I)I
    //   187: iadd
    //   188: putfield mTotalLength : I
    //   191: iload #19
    //   193: istore #22
    //   195: iinc #20, 1
    //   198: iload #22
    //   200: istore #19
    //   202: goto -> 156
    //   205: iconst_0
    //   206: istore #18
    //   208: goto -> 148
    //   211: aload #21
    //   213: invokevirtual getVisibility : ()I
    //   216: bipush #8
    //   218: if_icmpne -> 241
    //   221: iload #20
    //   223: aload_0
    //   224: aload #21
    //   226: iload #20
    //   228: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   231: iadd
    //   232: istore #20
    //   234: iload #19
    //   236: istore #22
    //   238: goto -> 195
    //   241: aload_0
    //   242: iload #20
    //   244: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   247: ifeq -> 263
    //   250: aload_0
    //   251: aload_0
    //   252: getfield mTotalLength : I
    //   255: aload_0
    //   256: getfield mDividerWidth : I
    //   259: iadd
    //   260: putfield mTotalLength : I
    //   263: aload #21
    //   265: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   268: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   271: astore #23
    //   273: fload #8
    //   275: aload #23
    //   277: getfield weight : F
    //   280: fadd
    //   281: fstore #8
    //   283: iload #10
    //   285: ldc 1073741824
    //   287: if_icmpne -> 635
    //   290: aload #23
    //   292: getfield width : I
    //   295: ifne -> 635
    //   298: aload #23
    //   300: getfield weight : F
    //   303: fconst_0
    //   304: fcmpl
    //   305: ifle -> 635
    //   308: iload #18
    //   310: ifeq -> 593
    //   313: aload_0
    //   314: aload_0
    //   315: getfield mTotalLength : I
    //   318: aload #23
    //   320: getfield leftMargin : I
    //   323: aload #23
    //   325: getfield rightMargin : I
    //   328: iadd
    //   329: iadd
    //   330: putfield mTotalLength : I
    //   333: iload #16
    //   335: ifeq -> 625
    //   338: iconst_0
    //   339: iconst_0
    //   340: invokestatic makeMeasureSpec : (II)I
    //   343: istore #24
    //   345: aload #21
    //   347: iload #24
    //   349: iload #24
    //   351: invokevirtual measure : (II)V
    //   354: iload #13
    //   356: istore #24
    //   358: iload #19
    //   360: istore #22
    //   362: iconst_0
    //   363: istore #25
    //   365: iload #12
    //   367: istore #19
    //   369: iload #25
    //   371: istore #13
    //   373: iload #11
    //   375: ldc 1073741824
    //   377: if_icmpeq -> 403
    //   380: iload #12
    //   382: istore #19
    //   384: iload #25
    //   386: istore #13
    //   388: aload #23
    //   390: getfield height : I
    //   393: iconst_m1
    //   394: if_icmpne -> 403
    //   397: iconst_1
    //   398: istore #19
    //   400: iconst_1
    //   401: istore #13
    //   403: aload #23
    //   405: getfield topMargin : I
    //   408: aload #23
    //   410: getfield bottomMargin : I
    //   413: iadd
    //   414: istore #25
    //   416: aload #21
    //   418: invokevirtual getMeasuredHeight : ()I
    //   421: iload #25
    //   423: iadd
    //   424: istore #12
    //   426: iload #4
    //   428: aload #21
    //   430: invokestatic getMeasuredState : (Landroid/view/View;)I
    //   433: invokestatic combineMeasuredStates : (II)I
    //   436: istore #26
    //   438: iload #16
    //   440: ifeq -> 517
    //   443: aload #21
    //   445: invokevirtual getBaseline : ()I
    //   448: istore #27
    //   450: iload #27
    //   452: iconst_m1
    //   453: if_icmpeq -> 517
    //   456: aload #23
    //   458: getfield gravity : I
    //   461: ifge -> 836
    //   464: aload_0
    //   465: getfield mGravity : I
    //   468: istore #4
    //   470: iload #4
    //   472: bipush #112
    //   474: iand
    //   475: iconst_4
    //   476: ishr
    //   477: bipush #-2
    //   479: iand
    //   480: iconst_1
    //   481: ishr
    //   482: istore #4
    //   484: aload #14
    //   486: iload #4
    //   488: aload #14
    //   490: iload #4
    //   492: iaload
    //   493: iload #27
    //   495: invokestatic max : (II)I
    //   498: iastore
    //   499: aload #15
    //   501: iload #4
    //   503: aload #15
    //   505: iload #4
    //   507: iaload
    //   508: iload #12
    //   510: iload #27
    //   512: isub
    //   513: invokestatic max : (II)I
    //   516: iastore
    //   517: iload_3
    //   518: iload #12
    //   520: invokestatic max : (II)I
    //   523: istore_3
    //   524: iload #7
    //   526: ifeq -> 846
    //   529: aload #23
    //   531: getfield height : I
    //   534: iconst_m1
    //   535: if_icmpne -> 846
    //   538: iconst_1
    //   539: istore #7
    //   541: aload #23
    //   543: getfield weight : F
    //   546: fconst_0
    //   547: fcmpl
    //   548: ifle -> 859
    //   551: iload #13
    //   553: ifeq -> 852
    //   556: iload #6
    //   558: iload #25
    //   560: invokestatic max : (II)I
    //   563: istore #6
    //   565: iload #20
    //   567: aload_0
    //   568: aload #21
    //   570: iload #20
    //   572: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   575: iadd
    //   576: istore #20
    //   578: iload #26
    //   580: istore #4
    //   582: iload #19
    //   584: istore #12
    //   586: iload #24
    //   588: istore #13
    //   590: goto -> 195
    //   593: aload_0
    //   594: getfield mTotalLength : I
    //   597: istore #24
    //   599: aload_0
    //   600: iload #24
    //   602: aload #23
    //   604: getfield leftMargin : I
    //   607: iload #24
    //   609: iadd
    //   610: aload #23
    //   612: getfield rightMargin : I
    //   615: iadd
    //   616: invokestatic max : (II)I
    //   619: putfield mTotalLength : I
    //   622: goto -> 333
    //   625: iconst_1
    //   626: istore #24
    //   628: iload #19
    //   630: istore #22
    //   632: goto -> 362
    //   635: ldc_w -2147483648
    //   638: istore #22
    //   640: iload #22
    //   642: istore #24
    //   644: aload #23
    //   646: getfield width : I
    //   649: ifne -> 676
    //   652: iload #22
    //   654: istore #24
    //   656: aload #23
    //   658: getfield weight : F
    //   661: fconst_0
    //   662: fcmpl
    //   663: ifle -> 676
    //   666: iconst_0
    //   667: istore #24
    //   669: aload #23
    //   671: bipush #-2
    //   673: putfield width : I
    //   676: fload #8
    //   678: fconst_0
    //   679: fcmpl
    //   680: ifne -> 788
    //   683: aload_0
    //   684: getfield mTotalLength : I
    //   687: istore #22
    //   689: aload_0
    //   690: aload #21
    //   692: iload #20
    //   694: iload_1
    //   695: iload #22
    //   697: iload_2
    //   698: iconst_0
    //   699: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   702: iload #24
    //   704: ldc_w -2147483648
    //   707: if_icmpeq -> 717
    //   710: aload #23
    //   712: iload #24
    //   714: putfield width : I
    //   717: aload #21
    //   719: invokevirtual getMeasuredWidth : ()I
    //   722: istore #25
    //   724: iload #18
    //   726: ifeq -> 794
    //   729: aload_0
    //   730: aload_0
    //   731: getfield mTotalLength : I
    //   734: aload #23
    //   736: getfield leftMargin : I
    //   739: iload #25
    //   741: iadd
    //   742: aload #23
    //   744: getfield rightMargin : I
    //   747: iadd
    //   748: aload_0
    //   749: aload #21
    //   751: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   754: iadd
    //   755: iadd
    //   756: putfield mTotalLength : I
    //   759: iload #19
    //   761: istore #22
    //   763: iload #13
    //   765: istore #24
    //   767: iload #17
    //   769: ifeq -> 362
    //   772: iload #25
    //   774: iload #19
    //   776: invokestatic max : (II)I
    //   779: istore #22
    //   781: iload #13
    //   783: istore #24
    //   785: goto -> 362
    //   788: iconst_0
    //   789: istore #22
    //   791: goto -> 689
    //   794: aload_0
    //   795: getfield mTotalLength : I
    //   798: istore #24
    //   800: aload_0
    //   801: iload #24
    //   803: iload #24
    //   805: iload #25
    //   807: iadd
    //   808: aload #23
    //   810: getfield leftMargin : I
    //   813: iadd
    //   814: aload #23
    //   816: getfield rightMargin : I
    //   819: iadd
    //   820: aload_0
    //   821: aload #21
    //   823: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   826: iadd
    //   827: invokestatic max : (II)I
    //   830: putfield mTotalLength : I
    //   833: goto -> 759
    //   836: aload #23
    //   838: getfield gravity : I
    //   841: istore #4
    //   843: goto -> 470
    //   846: iconst_0
    //   847: istore #7
    //   849: goto -> 541
    //   852: iload #12
    //   854: istore #25
    //   856: goto -> 556
    //   859: iload #13
    //   861: ifeq -> 876
    //   864: iload #5
    //   866: iload #25
    //   868: invokestatic max : (II)I
    //   871: istore #5
    //   873: goto -> 565
    //   876: iload #12
    //   878: istore #25
    //   880: goto -> 864
    //   883: aload_0
    //   884: getfield mTotalLength : I
    //   887: ifle -> 912
    //   890: aload_0
    //   891: iload #9
    //   893: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   896: ifeq -> 912
    //   899: aload_0
    //   900: aload_0
    //   901: getfield mTotalLength : I
    //   904: aload_0
    //   905: getfield mDividerWidth : I
    //   908: iadd
    //   909: putfield mTotalLength : I
    //   912: aload #14
    //   914: iconst_1
    //   915: iaload
    //   916: iconst_m1
    //   917: if_icmpne -> 947
    //   920: aload #14
    //   922: iconst_0
    //   923: iaload
    //   924: iconst_m1
    //   925: if_icmpne -> 947
    //   928: aload #14
    //   930: iconst_2
    //   931: iaload
    //   932: iconst_m1
    //   933: if_icmpne -> 947
    //   936: iload_3
    //   937: istore #20
    //   939: aload #14
    //   941: iconst_3
    //   942: iaload
    //   943: iconst_m1
    //   944: if_icmpeq -> 1004
    //   947: iload_3
    //   948: aload #14
    //   950: iconst_3
    //   951: iaload
    //   952: aload #14
    //   954: iconst_0
    //   955: iaload
    //   956: aload #14
    //   958: iconst_1
    //   959: iaload
    //   960: aload #14
    //   962: iconst_2
    //   963: iaload
    //   964: invokestatic max : (II)I
    //   967: invokestatic max : (II)I
    //   970: invokestatic max : (II)I
    //   973: aload #15
    //   975: iconst_3
    //   976: iaload
    //   977: aload #15
    //   979: iconst_0
    //   980: iaload
    //   981: aload #15
    //   983: iconst_1
    //   984: iaload
    //   985: aload #15
    //   987: iconst_2
    //   988: iaload
    //   989: invokestatic max : (II)I
    //   992: invokestatic max : (II)I
    //   995: invokestatic max : (II)I
    //   998: iadd
    //   999: invokestatic max : (II)I
    //   1002: istore #20
    //   1004: iload #17
    //   1006: ifeq -> 1180
    //   1009: iload #10
    //   1011: ldc_w -2147483648
    //   1014: if_icmpeq -> 1022
    //   1017: iload #10
    //   1019: ifne -> 1180
    //   1022: aload_0
    //   1023: iconst_0
    //   1024: putfield mTotalLength : I
    //   1027: iconst_0
    //   1028: istore_3
    //   1029: iload_3
    //   1030: iload #9
    //   1032: if_icmpge -> 1180
    //   1035: aload_0
    //   1036: iload_3
    //   1037: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1040: astore #23
    //   1042: aload #23
    //   1044: ifnonnull -> 1067
    //   1047: aload_0
    //   1048: aload_0
    //   1049: getfield mTotalLength : I
    //   1052: aload_0
    //   1053: iload_3
    //   1054: invokevirtual measureNullChild : (I)I
    //   1057: iadd
    //   1058: putfield mTotalLength : I
    //   1061: iinc #3, 1
    //   1064: goto -> 1029
    //   1067: aload #23
    //   1069: invokevirtual getVisibility : ()I
    //   1072: bipush #8
    //   1074: if_icmpne -> 1090
    //   1077: iload_3
    //   1078: aload_0
    //   1079: aload #23
    //   1081: iload_3
    //   1082: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1085: iadd
    //   1086: istore_3
    //   1087: goto -> 1061
    //   1090: aload #23
    //   1092: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1095: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1098: astore #21
    //   1100: iload #18
    //   1102: ifeq -> 1138
    //   1105: aload_0
    //   1106: aload_0
    //   1107: getfield mTotalLength : I
    //   1110: aload #21
    //   1112: getfield leftMargin : I
    //   1115: iload #19
    //   1117: iadd
    //   1118: aload #21
    //   1120: getfield rightMargin : I
    //   1123: iadd
    //   1124: aload_0
    //   1125: aload #23
    //   1127: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1130: iadd
    //   1131: iadd
    //   1132: putfield mTotalLength : I
    //   1135: goto -> 1061
    //   1138: aload_0
    //   1139: getfield mTotalLength : I
    //   1142: istore #24
    //   1144: aload_0
    //   1145: iload #24
    //   1147: iload #24
    //   1149: iload #19
    //   1151: iadd
    //   1152: aload #21
    //   1154: getfield leftMargin : I
    //   1157: iadd
    //   1158: aload #21
    //   1160: getfield rightMargin : I
    //   1163: iadd
    //   1164: aload_0
    //   1165: aload #23
    //   1167: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1170: iadd
    //   1171: invokestatic max : (II)I
    //   1174: putfield mTotalLength : I
    //   1177: goto -> 1061
    //   1180: aload_0
    //   1181: aload_0
    //   1182: getfield mTotalLength : I
    //   1185: aload_0
    //   1186: invokevirtual getPaddingLeft : ()I
    //   1189: aload_0
    //   1190: invokevirtual getPaddingRight : ()I
    //   1193: iadd
    //   1194: iadd
    //   1195: putfield mTotalLength : I
    //   1198: aload_0
    //   1199: getfield mTotalLength : I
    //   1202: aload_0
    //   1203: invokevirtual getSuggestedMinimumWidth : ()I
    //   1206: invokestatic max : (II)I
    //   1209: iload_1
    //   1210: iconst_0
    //   1211: invokestatic resolveSizeAndState : (III)I
    //   1214: istore #28
    //   1216: iload #28
    //   1218: ldc_w 16777215
    //   1221: iand
    //   1222: aload_0
    //   1223: getfield mTotalLength : I
    //   1226: isub
    //   1227: istore_3
    //   1228: iload #13
    //   1230: ifne -> 1244
    //   1233: iload_3
    //   1234: ifeq -> 2176
    //   1237: fload #8
    //   1239: fconst_0
    //   1240: fcmpl
    //   1241: ifle -> 2176
    //   1244: aload_0
    //   1245: getfield mWeightSum : F
    //   1248: fconst_0
    //   1249: fcmpl
    //   1250: ifle -> 1419
    //   1253: aload_0
    //   1254: getfield mWeightSum : F
    //   1257: fstore #29
    //   1259: aload #14
    //   1261: iconst_3
    //   1262: iconst_m1
    //   1263: iastore
    //   1264: aload #14
    //   1266: iconst_2
    //   1267: iconst_m1
    //   1268: iastore
    //   1269: aload #14
    //   1271: iconst_1
    //   1272: iconst_m1
    //   1273: iastore
    //   1274: aload #14
    //   1276: iconst_0
    //   1277: iconst_m1
    //   1278: iastore
    //   1279: aload #15
    //   1281: iconst_3
    //   1282: iconst_m1
    //   1283: iastore
    //   1284: aload #15
    //   1286: iconst_2
    //   1287: iconst_m1
    //   1288: iastore
    //   1289: aload #15
    //   1291: iconst_1
    //   1292: iconst_m1
    //   1293: iastore
    //   1294: aload #15
    //   1296: iconst_0
    //   1297: iconst_m1
    //   1298: iastore
    //   1299: iconst_m1
    //   1300: istore #20
    //   1302: aload_0
    //   1303: iconst_0
    //   1304: putfield mTotalLength : I
    //   1307: iconst_0
    //   1308: istore #13
    //   1310: iload #5
    //   1312: istore #6
    //   1314: iload #13
    //   1316: iload #9
    //   1318: if_icmpge -> 1965
    //   1321: aload_0
    //   1322: iload #13
    //   1324: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1327: astore #21
    //   1329: iload #7
    //   1331: istore #22
    //   1333: iload #6
    //   1335: istore #25
    //   1337: iload #4
    //   1339: istore #27
    //   1341: iload_3
    //   1342: istore #24
    //   1344: iload #20
    //   1346: istore #26
    //   1348: fload #29
    //   1350: fstore #30
    //   1352: aload #21
    //   1354: ifnull -> 1390
    //   1357: aload #21
    //   1359: invokevirtual getVisibility : ()I
    //   1362: bipush #8
    //   1364: if_icmpne -> 1426
    //   1367: fload #29
    //   1369: fstore #30
    //   1371: iload #20
    //   1373: istore #26
    //   1375: iload_3
    //   1376: istore #24
    //   1378: iload #4
    //   1380: istore #27
    //   1382: iload #6
    //   1384: istore #25
    //   1386: iload #7
    //   1388: istore #22
    //   1390: iinc #13, 1
    //   1393: iload #22
    //   1395: istore #7
    //   1397: iload #25
    //   1399: istore #6
    //   1401: iload #27
    //   1403: istore #4
    //   1405: iload #24
    //   1407: istore_3
    //   1408: iload #26
    //   1410: istore #20
    //   1412: fload #30
    //   1414: fstore #29
    //   1416: goto -> 1314
    //   1419: fload #8
    //   1421: fstore #29
    //   1423: goto -> 1259
    //   1426: aload #21
    //   1428: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1431: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1434: astore #23
    //   1436: aload #23
    //   1438: getfield weight : F
    //   1441: fstore #30
    //   1443: iload #4
    //   1445: istore #19
    //   1447: iload_3
    //   1448: istore #5
    //   1450: fload #29
    //   1452: fstore #8
    //   1454: fload #30
    //   1456: fconst_0
    //   1457: fcmpl
    //   1458: ifle -> 1589
    //   1461: iload_3
    //   1462: i2f
    //   1463: fload #30
    //   1465: fmul
    //   1466: fload #29
    //   1468: fdiv
    //   1469: f2i
    //   1470: istore #5
    //   1472: fload #29
    //   1474: fload #30
    //   1476: fsub
    //   1477: fstore #8
    //   1479: iload_3
    //   1480: iload #5
    //   1482: isub
    //   1483: istore #19
    //   1485: iload_2
    //   1486: aload_0
    //   1487: invokevirtual getPaddingTop : ()I
    //   1490: aload_0
    //   1491: invokevirtual getPaddingBottom : ()I
    //   1494: iadd
    //   1495: aload #23
    //   1497: getfield topMargin : I
    //   1500: iadd
    //   1501: aload #23
    //   1503: getfield bottomMargin : I
    //   1506: iadd
    //   1507: aload #23
    //   1509: getfield height : I
    //   1512: invokestatic getChildMeasureSpec : (III)I
    //   1515: istore #24
    //   1517: aload #23
    //   1519: getfield width : I
    //   1522: ifne -> 1532
    //   1525: iload #10
    //   1527: ldc 1073741824
    //   1529: if_icmpeq -> 1864
    //   1532: aload #21
    //   1534: invokevirtual getMeasuredWidth : ()I
    //   1537: iload #5
    //   1539: iadd
    //   1540: istore_3
    //   1541: iload_3
    //   1542: istore #5
    //   1544: iload_3
    //   1545: ifge -> 1551
    //   1548: iconst_0
    //   1549: istore #5
    //   1551: aload #21
    //   1553: iload #5
    //   1555: ldc 1073741824
    //   1557: invokestatic makeMeasureSpec : (II)I
    //   1560: iload #24
    //   1562: invokevirtual measure : (II)V
    //   1565: iload #4
    //   1567: aload #21
    //   1569: invokestatic getMeasuredState : (Landroid/view/View;)I
    //   1572: ldc_w -16777216
    //   1575: iand
    //   1576: invokestatic combineMeasuredStates : (II)I
    //   1579: istore #4
    //   1581: iload #19
    //   1583: istore #5
    //   1585: iload #4
    //   1587: istore #19
    //   1589: iload #18
    //   1591: ifeq -> 1892
    //   1594: aload_0
    //   1595: aload_0
    //   1596: getfield mTotalLength : I
    //   1599: aload #21
    //   1601: invokevirtual getMeasuredWidth : ()I
    //   1604: aload #23
    //   1606: getfield leftMargin : I
    //   1609: iadd
    //   1610: aload #23
    //   1612: getfield rightMargin : I
    //   1615: iadd
    //   1616: aload_0
    //   1617: aload #21
    //   1619: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1622: iadd
    //   1623: iadd
    //   1624: putfield mTotalLength : I
    //   1627: iload #11
    //   1629: ldc 1073741824
    //   1631: if_icmpeq -> 1937
    //   1634: aload #23
    //   1636: getfield height : I
    //   1639: iconst_m1
    //   1640: if_icmpne -> 1937
    //   1643: iconst_1
    //   1644: istore #4
    //   1646: aload #23
    //   1648: getfield topMargin : I
    //   1651: aload #23
    //   1653: getfield bottomMargin : I
    //   1656: iadd
    //   1657: istore #24
    //   1659: aload #21
    //   1661: invokevirtual getMeasuredHeight : ()I
    //   1664: iload #24
    //   1666: iadd
    //   1667: istore_3
    //   1668: iload #20
    //   1670: iload_3
    //   1671: invokestatic max : (II)I
    //   1674: istore #20
    //   1676: iload #4
    //   1678: ifeq -> 1943
    //   1681: iload #24
    //   1683: istore #4
    //   1685: iload #6
    //   1687: iload #4
    //   1689: invokestatic max : (II)I
    //   1692: istore #6
    //   1694: iload #7
    //   1696: ifeq -> 1949
    //   1699: aload #23
    //   1701: getfield height : I
    //   1704: iconst_m1
    //   1705: if_icmpne -> 1949
    //   1708: iconst_1
    //   1709: istore #7
    //   1711: iload #7
    //   1713: istore #22
    //   1715: iload #6
    //   1717: istore #25
    //   1719: iload #19
    //   1721: istore #27
    //   1723: iload #5
    //   1725: istore #24
    //   1727: iload #20
    //   1729: istore #26
    //   1731: fload #8
    //   1733: fstore #30
    //   1735: iload #16
    //   1737: ifeq -> 1390
    //   1740: aload #21
    //   1742: invokevirtual getBaseline : ()I
    //   1745: istore #31
    //   1747: iload #7
    //   1749: istore #22
    //   1751: iload #6
    //   1753: istore #25
    //   1755: iload #19
    //   1757: istore #27
    //   1759: iload #5
    //   1761: istore #24
    //   1763: iload #20
    //   1765: istore #26
    //   1767: fload #8
    //   1769: fstore #30
    //   1771: iload #31
    //   1773: iconst_m1
    //   1774: if_icmpeq -> 1390
    //   1777: aload #23
    //   1779: getfield gravity : I
    //   1782: ifge -> 1955
    //   1785: aload_0
    //   1786: getfield mGravity : I
    //   1789: istore #4
    //   1791: iload #4
    //   1793: bipush #112
    //   1795: iand
    //   1796: iconst_4
    //   1797: ishr
    //   1798: bipush #-2
    //   1800: iand
    //   1801: iconst_1
    //   1802: ishr
    //   1803: istore #4
    //   1805: aload #14
    //   1807: iload #4
    //   1809: aload #14
    //   1811: iload #4
    //   1813: iaload
    //   1814: iload #31
    //   1816: invokestatic max : (II)I
    //   1819: iastore
    //   1820: aload #15
    //   1822: iload #4
    //   1824: aload #15
    //   1826: iload #4
    //   1828: iaload
    //   1829: iload_3
    //   1830: iload #31
    //   1832: isub
    //   1833: invokestatic max : (II)I
    //   1836: iastore
    //   1837: iload #7
    //   1839: istore #22
    //   1841: iload #6
    //   1843: istore #25
    //   1845: iload #19
    //   1847: istore #27
    //   1849: iload #5
    //   1851: istore #24
    //   1853: iload #20
    //   1855: istore #26
    //   1857: fload #8
    //   1859: fstore #30
    //   1861: goto -> 1390
    //   1864: iload #5
    //   1866: ifle -> 1886
    //   1869: aload #21
    //   1871: iload #5
    //   1873: ldc 1073741824
    //   1875: invokestatic makeMeasureSpec : (II)I
    //   1878: iload #24
    //   1880: invokevirtual measure : (II)V
    //   1883: goto -> 1565
    //   1886: iconst_0
    //   1887: istore #5
    //   1889: goto -> 1869
    //   1892: aload_0
    //   1893: getfield mTotalLength : I
    //   1896: istore #4
    //   1898: aload_0
    //   1899: iload #4
    //   1901: aload #21
    //   1903: invokevirtual getMeasuredWidth : ()I
    //   1906: iload #4
    //   1908: iadd
    //   1909: aload #23
    //   1911: getfield leftMargin : I
    //   1914: iadd
    //   1915: aload #23
    //   1917: getfield rightMargin : I
    //   1920: iadd
    //   1921: aload_0
    //   1922: aload #21
    //   1924: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1927: iadd
    //   1928: invokestatic max : (II)I
    //   1931: putfield mTotalLength : I
    //   1934: goto -> 1627
    //   1937: iconst_0
    //   1938: istore #4
    //   1940: goto -> 1646
    //   1943: iload_3
    //   1944: istore #4
    //   1946: goto -> 1685
    //   1949: iconst_0
    //   1950: istore #7
    //   1952: goto -> 1711
    //   1955: aload #23
    //   1957: getfield gravity : I
    //   1960: istore #4
    //   1962: goto -> 1791
    //   1965: aload_0
    //   1966: aload_0
    //   1967: getfield mTotalLength : I
    //   1970: aload_0
    //   1971: invokevirtual getPaddingLeft : ()I
    //   1974: aload_0
    //   1975: invokevirtual getPaddingRight : ()I
    //   1978: iadd
    //   1979: iadd
    //   1980: putfield mTotalLength : I
    //   1983: aload #14
    //   1985: iconst_1
    //   1986: iaload
    //   1987: iconst_m1
    //   1988: if_icmpne -> 2030
    //   1991: aload #14
    //   1993: iconst_0
    //   1994: iaload
    //   1995: iconst_m1
    //   1996: if_icmpne -> 2030
    //   1999: aload #14
    //   2001: iconst_2
    //   2002: iaload
    //   2003: iconst_m1
    //   2004: if_icmpne -> 2030
    //   2007: iload #7
    //   2009: istore #24
    //   2011: iload #6
    //   2013: istore_3
    //   2014: iload #4
    //   2016: istore #13
    //   2018: iload #20
    //   2020: istore #5
    //   2022: aload #14
    //   2024: iconst_3
    //   2025: iaload
    //   2026: iconst_m1
    //   2027: if_icmpeq -> 2099
    //   2030: iload #20
    //   2032: aload #14
    //   2034: iconst_3
    //   2035: iaload
    //   2036: aload #14
    //   2038: iconst_0
    //   2039: iaload
    //   2040: aload #14
    //   2042: iconst_1
    //   2043: iaload
    //   2044: aload #14
    //   2046: iconst_2
    //   2047: iaload
    //   2048: invokestatic max : (II)I
    //   2051: invokestatic max : (II)I
    //   2054: invokestatic max : (II)I
    //   2057: aload #15
    //   2059: iconst_3
    //   2060: iaload
    //   2061: aload #15
    //   2063: iconst_0
    //   2064: iaload
    //   2065: aload #15
    //   2067: iconst_1
    //   2068: iaload
    //   2069: aload #15
    //   2071: iconst_2
    //   2072: iaload
    //   2073: invokestatic max : (II)I
    //   2076: invokestatic max : (II)I
    //   2079: invokestatic max : (II)I
    //   2082: iadd
    //   2083: invokestatic max : (II)I
    //   2086: istore #5
    //   2088: iload #4
    //   2090: istore #13
    //   2092: iload #6
    //   2094: istore_3
    //   2095: iload #7
    //   2097: istore #24
    //   2099: iload #5
    //   2101: istore #7
    //   2103: iload #24
    //   2105: ifne -> 2122
    //   2108: iload #5
    //   2110: istore #7
    //   2112: iload #11
    //   2114: ldc 1073741824
    //   2116: if_icmpeq -> 2122
    //   2119: iload_3
    //   2120: istore #7
    //   2122: aload_0
    //   2123: ldc_w -16777216
    //   2126: iload #13
    //   2128: iand
    //   2129: iload #28
    //   2131: ior
    //   2132: iload #7
    //   2134: aload_0
    //   2135: invokevirtual getPaddingTop : ()I
    //   2138: aload_0
    //   2139: invokevirtual getPaddingBottom : ()I
    //   2142: iadd
    //   2143: iadd
    //   2144: aload_0
    //   2145: invokevirtual getSuggestedMinimumHeight : ()I
    //   2148: invokestatic max : (II)I
    //   2151: iload_2
    //   2152: iload #13
    //   2154: bipush #16
    //   2156: ishl
    //   2157: invokestatic resolveSizeAndState : (III)I
    //   2160: invokevirtual setMeasuredDimension : (II)V
    //   2163: iload #12
    //   2165: ifeq -> 2175
    //   2168: aload_0
    //   2169: iload #9
    //   2171: iload_1
    //   2172: invokespecial forceUniformHeight : (II)V
    //   2175: return
    //   2176: iload #5
    //   2178: iload #6
    //   2180: invokestatic max : (II)I
    //   2183: istore #22
    //   2185: iload #7
    //   2187: istore #24
    //   2189: iload #22
    //   2191: istore_3
    //   2192: iload #4
    //   2194: istore #13
    //   2196: iload #20
    //   2198: istore #5
    //   2200: iload #17
    //   2202: ifeq -> 2099
    //   2205: iload #7
    //   2207: istore #24
    //   2209: iload #22
    //   2211: istore_3
    //   2212: iload #4
    //   2214: istore #13
    //   2216: iload #20
    //   2218: istore #5
    //   2220: iload #10
    //   2222: ldc 1073741824
    //   2224: if_icmpeq -> 2099
    //   2227: iconst_0
    //   2228: istore #6
    //   2230: iload #7
    //   2232: istore #24
    //   2234: iload #22
    //   2236: istore_3
    //   2237: iload #4
    //   2239: istore #13
    //   2241: iload #20
    //   2243: istore #5
    //   2245: iload #6
    //   2247: iload #9
    //   2249: if_icmpge -> 2099
    //   2252: aload_0
    //   2253: iload #6
    //   2255: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   2258: astore #14
    //   2260: aload #14
    //   2262: ifnull -> 2275
    //   2265: aload #14
    //   2267: invokevirtual getVisibility : ()I
    //   2270: bipush #8
    //   2272: if_icmpne -> 2281
    //   2275: iinc #6, 1
    //   2278: goto -> 2230
    //   2281: aload #14
    //   2283: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   2286: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   2289: getfield weight : F
    //   2292: fconst_0
    //   2293: fcmpl
    //   2294: ifle -> 2275
    //   2297: aload #14
    //   2299: iload #19
    //   2301: ldc 1073741824
    //   2303: invokestatic makeMeasureSpec : (II)I
    //   2306: aload #14
    //   2308: invokevirtual getMeasuredHeight : ()I
    //   2311: ldc 1073741824
    //   2313: invokestatic makeMeasureSpec : (II)I
    //   2316: invokevirtual measure : (II)V
    //   2319: goto -> 2275
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: iconst_0
    //   6: istore_3
    //   7: iconst_0
    //   8: istore #4
    //   10: iconst_0
    //   11: istore #5
    //   13: iconst_0
    //   14: istore #6
    //   16: iconst_1
    //   17: istore #7
    //   19: fconst_0
    //   20: fstore #8
    //   22: aload_0
    //   23: invokevirtual getVirtualChildCount : ()I
    //   26: istore #9
    //   28: iload_1
    //   29: invokestatic getMode : (I)I
    //   32: istore #10
    //   34: iload_2
    //   35: invokestatic getMode : (I)I
    //   38: istore #11
    //   40: iconst_0
    //   41: istore #12
    //   43: iconst_0
    //   44: istore #13
    //   46: aload_0
    //   47: getfield mBaselineAlignedChildIndex : I
    //   50: istore #14
    //   52: aload_0
    //   53: getfield mUseLargestChild : Z
    //   56: istore #15
    //   58: ldc_w -2147483648
    //   61: istore #16
    //   63: iconst_0
    //   64: istore #17
    //   66: iload #17
    //   68: iload #9
    //   70: if_icmpge -> 646
    //   73: aload_0
    //   74: iload #17
    //   76: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   79: astore #18
    //   81: aload #18
    //   83: ifnonnull -> 115
    //   86: aload_0
    //   87: aload_0
    //   88: getfield mTotalLength : I
    //   91: aload_0
    //   92: iload #17
    //   94: invokevirtual measureNullChild : (I)I
    //   97: iadd
    //   98: putfield mTotalLength : I
    //   101: iload #16
    //   103: istore #19
    //   105: iinc #17, 1
    //   108: iload #19
    //   110: istore #16
    //   112: goto -> 66
    //   115: aload #18
    //   117: invokevirtual getVisibility : ()I
    //   120: bipush #8
    //   122: if_icmpne -> 145
    //   125: iload #17
    //   127: aload_0
    //   128: aload #18
    //   130: iload #17
    //   132: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   135: iadd
    //   136: istore #17
    //   138: iload #16
    //   140: istore #19
    //   142: goto -> 105
    //   145: aload_0
    //   146: iload #17
    //   148: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   151: ifeq -> 167
    //   154: aload_0
    //   155: aload_0
    //   156: getfield mTotalLength : I
    //   159: aload_0
    //   160: getfield mDividerHeight : I
    //   163: iadd
    //   164: putfield mTotalLength : I
    //   167: aload #18
    //   169: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   172: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   175: astore #20
    //   177: fload #8
    //   179: aload #20
    //   181: getfield weight : F
    //   184: fadd
    //   185: fstore #8
    //   187: iload #11
    //   189: ldc 1073741824
    //   191: if_icmpne -> 298
    //   194: aload #20
    //   196: getfield height : I
    //   199: ifne -> 298
    //   202: aload #20
    //   204: getfield weight : F
    //   207: fconst_0
    //   208: fcmpl
    //   209: ifle -> 298
    //   212: aload_0
    //   213: getfield mTotalLength : I
    //   216: istore #13
    //   218: aload_0
    //   219: iload #13
    //   221: aload #20
    //   223: getfield topMargin : I
    //   226: iload #13
    //   228: iadd
    //   229: aload #20
    //   231: getfield bottomMargin : I
    //   234: iadd
    //   235: invokestatic max : (II)I
    //   238: putfield mTotalLength : I
    //   241: iconst_1
    //   242: istore #21
    //   244: iload #16
    //   246: istore #19
    //   248: iload #14
    //   250: iflt -> 270
    //   253: iload #14
    //   255: iload #17
    //   257: iconst_1
    //   258: iadd
    //   259: if_icmpne -> 270
    //   262: aload_0
    //   263: aload_0
    //   264: getfield mTotalLength : I
    //   267: putfield mBaselineChildTop : I
    //   270: iload #17
    //   272: iload #14
    //   274: if_icmpge -> 461
    //   277: aload #20
    //   279: getfield weight : F
    //   282: fconst_0
    //   283: fcmpl
    //   284: ifle -> 461
    //   287: new java/lang/RuntimeException
    //   290: dup
    //   291: ldc_w 'A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.'
    //   294: invokespecial <init> : (Ljava/lang/String;)V
    //   297: athrow
    //   298: ldc_w -2147483648
    //   301: istore #19
    //   303: iload #19
    //   305: istore #21
    //   307: aload #20
    //   309: getfield height : I
    //   312: ifne -> 339
    //   315: iload #19
    //   317: istore #21
    //   319: aload #20
    //   321: getfield weight : F
    //   324: fconst_0
    //   325: fcmpl
    //   326: ifle -> 339
    //   329: iconst_0
    //   330: istore #21
    //   332: aload #20
    //   334: bipush #-2
    //   336: putfield height : I
    //   339: fload #8
    //   341: fconst_0
    //   342: fcmpl
    //   343: ifne -> 455
    //   346: aload_0
    //   347: getfield mTotalLength : I
    //   350: istore #19
    //   352: aload_0
    //   353: aload #18
    //   355: iload #17
    //   357: iload_1
    //   358: iconst_0
    //   359: iload_2
    //   360: iload #19
    //   362: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   365: iload #21
    //   367: ldc_w -2147483648
    //   370: if_icmpeq -> 380
    //   373: aload #20
    //   375: iload #21
    //   377: putfield height : I
    //   380: aload #18
    //   382: invokevirtual getMeasuredHeight : ()I
    //   385: istore #22
    //   387: aload_0
    //   388: getfield mTotalLength : I
    //   391: istore #21
    //   393: aload_0
    //   394: iload #21
    //   396: iload #21
    //   398: iload #22
    //   400: iadd
    //   401: aload #20
    //   403: getfield topMargin : I
    //   406: iadd
    //   407: aload #20
    //   409: getfield bottomMargin : I
    //   412: iadd
    //   413: aload_0
    //   414: aload #18
    //   416: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   419: iadd
    //   420: invokestatic max : (II)I
    //   423: putfield mTotalLength : I
    //   426: iload #16
    //   428: istore #19
    //   430: iload #13
    //   432: istore #21
    //   434: iload #15
    //   436: ifeq -> 248
    //   439: iload #22
    //   441: iload #16
    //   443: invokestatic max : (II)I
    //   446: istore #19
    //   448: iload #13
    //   450: istore #21
    //   452: goto -> 248
    //   455: iconst_0
    //   456: istore #19
    //   458: goto -> 352
    //   461: iconst_0
    //   462: istore #22
    //   464: iload #12
    //   466: istore #16
    //   468: iload #22
    //   470: istore #13
    //   472: iload #10
    //   474: ldc 1073741824
    //   476: if_icmpeq -> 502
    //   479: iload #12
    //   481: istore #16
    //   483: iload #22
    //   485: istore #13
    //   487: aload #20
    //   489: getfield width : I
    //   492: iconst_m1
    //   493: if_icmpne -> 502
    //   496: iconst_1
    //   497: istore #16
    //   499: iconst_1
    //   500: istore #13
    //   502: aload #20
    //   504: getfield leftMargin : I
    //   507: aload #20
    //   509: getfield rightMargin : I
    //   512: iadd
    //   513: istore #12
    //   515: aload #18
    //   517: invokevirtual getMeasuredWidth : ()I
    //   520: iload #12
    //   522: iadd
    //   523: istore #22
    //   525: iload_3
    //   526: iload #22
    //   528: invokestatic max : (II)I
    //   531: istore_3
    //   532: iload #4
    //   534: aload #18
    //   536: invokestatic getMeasuredState : (Landroid/view/View;)I
    //   539: invokestatic combineMeasuredStates : (II)I
    //   542: istore #4
    //   544: iload #7
    //   546: ifeq -> 609
    //   549: aload #20
    //   551: getfield width : I
    //   554: iconst_m1
    //   555: if_icmpne -> 609
    //   558: iconst_1
    //   559: istore #7
    //   561: aload #20
    //   563: getfield weight : F
    //   566: fconst_0
    //   567: fcmpl
    //   568: ifle -> 622
    //   571: iload #13
    //   573: ifeq -> 615
    //   576: iload #6
    //   578: iload #12
    //   580: invokestatic max : (II)I
    //   583: istore #6
    //   585: iload #17
    //   587: aload_0
    //   588: aload #18
    //   590: iload #17
    //   592: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   595: iadd
    //   596: istore #17
    //   598: iload #16
    //   600: istore #12
    //   602: iload #21
    //   604: istore #13
    //   606: goto -> 105
    //   609: iconst_0
    //   610: istore #7
    //   612: goto -> 561
    //   615: iload #22
    //   617: istore #12
    //   619: goto -> 576
    //   622: iload #13
    //   624: ifeq -> 639
    //   627: iload #5
    //   629: iload #12
    //   631: invokestatic max : (II)I
    //   634: istore #5
    //   636: goto -> 585
    //   639: iload #22
    //   641: istore #12
    //   643: goto -> 627
    //   646: aload_0
    //   647: getfield mTotalLength : I
    //   650: ifle -> 675
    //   653: aload_0
    //   654: iload #9
    //   656: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   659: ifeq -> 675
    //   662: aload_0
    //   663: aload_0
    //   664: getfield mTotalLength : I
    //   667: aload_0
    //   668: getfield mDividerHeight : I
    //   671: iadd
    //   672: putfield mTotalLength : I
    //   675: iload #15
    //   677: ifeq -> 820
    //   680: iload #11
    //   682: ldc_w -2147483648
    //   685: if_icmpeq -> 693
    //   688: iload #11
    //   690: ifne -> 820
    //   693: aload_0
    //   694: iconst_0
    //   695: putfield mTotalLength : I
    //   698: iconst_0
    //   699: istore #17
    //   701: iload #17
    //   703: iload #9
    //   705: if_icmpge -> 820
    //   708: aload_0
    //   709: iload #17
    //   711: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   714: astore #20
    //   716: aload #20
    //   718: ifnonnull -> 742
    //   721: aload_0
    //   722: aload_0
    //   723: getfield mTotalLength : I
    //   726: aload_0
    //   727: iload #17
    //   729: invokevirtual measureNullChild : (I)I
    //   732: iadd
    //   733: putfield mTotalLength : I
    //   736: iinc #17, 1
    //   739: goto -> 701
    //   742: aload #20
    //   744: invokevirtual getVisibility : ()I
    //   747: bipush #8
    //   749: if_icmpne -> 768
    //   752: iload #17
    //   754: aload_0
    //   755: aload #20
    //   757: iload #17
    //   759: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   762: iadd
    //   763: istore #17
    //   765: goto -> 736
    //   768: aload #20
    //   770: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   773: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   776: astore #18
    //   778: aload_0
    //   779: getfield mTotalLength : I
    //   782: istore #21
    //   784: aload_0
    //   785: iload #21
    //   787: iload #21
    //   789: iload #16
    //   791: iadd
    //   792: aload #18
    //   794: getfield topMargin : I
    //   797: iadd
    //   798: aload #18
    //   800: getfield bottomMargin : I
    //   803: iadd
    //   804: aload_0
    //   805: aload #20
    //   807: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   810: iadd
    //   811: invokestatic max : (II)I
    //   814: putfield mTotalLength : I
    //   817: goto -> 736
    //   820: aload_0
    //   821: aload_0
    //   822: getfield mTotalLength : I
    //   825: aload_0
    //   826: invokevirtual getPaddingTop : ()I
    //   829: aload_0
    //   830: invokevirtual getPaddingBottom : ()I
    //   833: iadd
    //   834: iadd
    //   835: putfield mTotalLength : I
    //   838: aload_0
    //   839: getfield mTotalLength : I
    //   842: aload_0
    //   843: invokevirtual getSuggestedMinimumHeight : ()I
    //   846: invokestatic max : (II)I
    //   849: iload_2
    //   850: iconst_0
    //   851: invokestatic resolveSizeAndState : (III)I
    //   854: istore #22
    //   856: iload #22
    //   858: ldc_w 16777215
    //   861: iand
    //   862: aload_0
    //   863: getfield mTotalLength : I
    //   866: isub
    //   867: istore #17
    //   869: iload #13
    //   871: ifne -> 886
    //   874: iload #17
    //   876: ifeq -> 1422
    //   879: fload #8
    //   881: fconst_0
    //   882: fcmpl
    //   883: ifle -> 1422
    //   886: aload_0
    //   887: getfield mWeightSum : F
    //   890: fconst_0
    //   891: fcmpl
    //   892: ifle -> 985
    //   895: aload_0
    //   896: getfield mWeightSum : F
    //   899: fstore #23
    //   901: aload_0
    //   902: iconst_0
    //   903: putfield mTotalLength : I
    //   906: iconst_0
    //   907: istore #13
    //   909: iload_3
    //   910: istore #6
    //   912: iload #17
    //   914: istore #16
    //   916: iload #13
    //   918: iload #9
    //   920: if_icmpge -> 1328
    //   923: aload_0
    //   924: iload #13
    //   926: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   929: astore #20
    //   931: aload #20
    //   933: invokevirtual getVisibility : ()I
    //   936: bipush #8
    //   938: if_icmpne -> 992
    //   941: fload #23
    //   943: fstore #8
    //   945: iload #16
    //   947: istore_3
    //   948: iload #4
    //   950: istore #17
    //   952: iload #5
    //   954: istore #4
    //   956: iload #7
    //   958: istore #5
    //   960: iinc #13, 1
    //   963: iload #5
    //   965: istore #7
    //   967: iload #4
    //   969: istore #5
    //   971: iload #17
    //   973: istore #4
    //   975: iload_3
    //   976: istore #16
    //   978: fload #8
    //   980: fstore #23
    //   982: goto -> 916
    //   985: fload #8
    //   987: fstore #23
    //   989: goto -> 901
    //   992: aload #20
    //   994: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   997: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1000: astore #18
    //   1002: aload #18
    //   1004: getfield weight : F
    //   1007: fstore #24
    //   1009: iload #4
    //   1011: istore #17
    //   1013: iload #16
    //   1015: istore_3
    //   1016: fload #23
    //   1018: fstore #8
    //   1020: fload #24
    //   1022: fconst_0
    //   1023: fcmpl
    //   1024: ifle -> 1153
    //   1027: iload #16
    //   1029: i2f
    //   1030: fload #24
    //   1032: fmul
    //   1033: fload #23
    //   1035: fdiv
    //   1036: f2i
    //   1037: istore_3
    //   1038: fload #23
    //   1040: fload #24
    //   1042: fsub
    //   1043: fstore #8
    //   1045: iload #16
    //   1047: iload_3
    //   1048: isub
    //   1049: istore #17
    //   1051: iload_1
    //   1052: aload_0
    //   1053: invokevirtual getPaddingLeft : ()I
    //   1056: aload_0
    //   1057: invokevirtual getPaddingRight : ()I
    //   1060: iadd
    //   1061: aload #18
    //   1063: getfield leftMargin : I
    //   1066: iadd
    //   1067: aload #18
    //   1069: getfield rightMargin : I
    //   1072: iadd
    //   1073: aload #18
    //   1075: getfield width : I
    //   1078: invokestatic getChildMeasureSpec : (III)I
    //   1081: istore #21
    //   1083: aload #18
    //   1085: getfield height : I
    //   1088: ifne -> 1098
    //   1091: iload #11
    //   1093: ldc 1073741824
    //   1095: if_icmpeq -> 1284
    //   1098: aload #20
    //   1100: invokevirtual getMeasuredHeight : ()I
    //   1103: iload_3
    //   1104: iadd
    //   1105: istore #16
    //   1107: iload #16
    //   1109: istore_3
    //   1110: iload #16
    //   1112: ifge -> 1117
    //   1115: iconst_0
    //   1116: istore_3
    //   1117: aload #20
    //   1119: iload #21
    //   1121: iload_3
    //   1122: ldc 1073741824
    //   1124: invokestatic makeMeasureSpec : (II)I
    //   1127: invokevirtual measure : (II)V
    //   1130: iload #4
    //   1132: aload #20
    //   1134: invokestatic getMeasuredState : (Landroid/view/View;)I
    //   1137: sipush #-256
    //   1140: iand
    //   1141: invokestatic combineMeasuredStates : (II)I
    //   1144: istore #4
    //   1146: iload #17
    //   1148: istore_3
    //   1149: iload #4
    //   1151: istore #17
    //   1153: aload #18
    //   1155: getfield leftMargin : I
    //   1158: aload #18
    //   1160: getfield rightMargin : I
    //   1163: iadd
    //   1164: istore #16
    //   1166: aload #20
    //   1168: invokevirtual getMeasuredWidth : ()I
    //   1171: iload #16
    //   1173: iadd
    //   1174: istore #21
    //   1176: iload #6
    //   1178: iload #21
    //   1180: invokestatic max : (II)I
    //   1183: istore #6
    //   1185: iload #10
    //   1187: ldc 1073741824
    //   1189: if_icmpeq -> 1309
    //   1192: aload #18
    //   1194: getfield width : I
    //   1197: iconst_m1
    //   1198: if_icmpne -> 1309
    //   1201: iconst_1
    //   1202: istore #4
    //   1204: iload #4
    //   1206: ifeq -> 1315
    //   1209: iload #16
    //   1211: istore #4
    //   1213: iload #5
    //   1215: iload #4
    //   1217: invokestatic max : (II)I
    //   1220: istore #4
    //   1222: iload #7
    //   1224: ifeq -> 1322
    //   1227: aload #18
    //   1229: getfield width : I
    //   1232: iconst_m1
    //   1233: if_icmpne -> 1322
    //   1236: iconst_1
    //   1237: istore #5
    //   1239: aload_0
    //   1240: getfield mTotalLength : I
    //   1243: istore #7
    //   1245: aload_0
    //   1246: iload #7
    //   1248: aload #20
    //   1250: invokevirtual getMeasuredHeight : ()I
    //   1253: iload #7
    //   1255: iadd
    //   1256: aload #18
    //   1258: getfield topMargin : I
    //   1261: iadd
    //   1262: aload #18
    //   1264: getfield bottomMargin : I
    //   1267: iadd
    //   1268: aload_0
    //   1269: aload #20
    //   1271: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1274: iadd
    //   1275: invokestatic max : (II)I
    //   1278: putfield mTotalLength : I
    //   1281: goto -> 960
    //   1284: iload_3
    //   1285: ifle -> 1304
    //   1288: aload #20
    //   1290: iload #21
    //   1292: iload_3
    //   1293: ldc 1073741824
    //   1295: invokestatic makeMeasureSpec : (II)I
    //   1298: invokevirtual measure : (II)V
    //   1301: goto -> 1130
    //   1304: iconst_0
    //   1305: istore_3
    //   1306: goto -> 1288
    //   1309: iconst_0
    //   1310: istore #4
    //   1312: goto -> 1204
    //   1315: iload #21
    //   1317: istore #4
    //   1319: goto -> 1213
    //   1322: iconst_0
    //   1323: istore #5
    //   1325: goto -> 1239
    //   1328: aload_0
    //   1329: aload_0
    //   1330: getfield mTotalLength : I
    //   1333: aload_0
    //   1334: invokevirtual getPaddingTop : ()I
    //   1337: aload_0
    //   1338: invokevirtual getPaddingBottom : ()I
    //   1341: iadd
    //   1342: iadd
    //   1343: putfield mTotalLength : I
    //   1346: iload #4
    //   1348: istore #17
    //   1350: iload #7
    //   1352: istore #13
    //   1354: iload #6
    //   1356: istore #7
    //   1358: iload #13
    //   1360: ifne -> 1378
    //   1363: iload #6
    //   1365: istore #7
    //   1367: iload #10
    //   1369: ldc 1073741824
    //   1371: if_icmpeq -> 1378
    //   1374: iload #5
    //   1376: istore #7
    //   1378: aload_0
    //   1379: iload #7
    //   1381: aload_0
    //   1382: invokevirtual getPaddingLeft : ()I
    //   1385: aload_0
    //   1386: invokevirtual getPaddingRight : ()I
    //   1389: iadd
    //   1390: iadd
    //   1391: aload_0
    //   1392: invokevirtual getSuggestedMinimumWidth : ()I
    //   1395: invokestatic max : (II)I
    //   1398: iload_1
    //   1399: iload #17
    //   1401: invokestatic resolveSizeAndState : (III)I
    //   1404: iload #22
    //   1406: invokevirtual setMeasuredDimension : (II)V
    //   1409: iload #12
    //   1411: ifeq -> 1421
    //   1414: aload_0
    //   1415: iload #9
    //   1417: iload_2
    //   1418: invokespecial forceUniformWidth : (II)V
    //   1421: return
    //   1422: iload #5
    //   1424: iload #6
    //   1426: invokestatic max : (II)I
    //   1429: istore #19
    //   1431: iload #7
    //   1433: istore #13
    //   1435: iload #19
    //   1437: istore #5
    //   1439: iload #4
    //   1441: istore #17
    //   1443: iload_3
    //   1444: istore #6
    //   1446: iload #15
    //   1448: ifeq -> 1354
    //   1451: iload #7
    //   1453: istore #13
    //   1455: iload #19
    //   1457: istore #5
    //   1459: iload #4
    //   1461: istore #17
    //   1463: iload_3
    //   1464: istore #6
    //   1466: iload #11
    //   1468: ldc 1073741824
    //   1470: if_icmpeq -> 1354
    //   1473: iconst_0
    //   1474: istore #21
    //   1476: iload #7
    //   1478: istore #13
    //   1480: iload #19
    //   1482: istore #5
    //   1484: iload #4
    //   1486: istore #17
    //   1488: iload_3
    //   1489: istore #6
    //   1491: iload #21
    //   1493: iload #9
    //   1495: if_icmpge -> 1354
    //   1498: aload_0
    //   1499: iload #21
    //   1501: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1504: astore #18
    //   1506: aload #18
    //   1508: ifnull -> 1521
    //   1511: aload #18
    //   1513: invokevirtual getVisibility : ()I
    //   1516: bipush #8
    //   1518: if_icmpne -> 1527
    //   1521: iinc #21, 1
    //   1524: goto -> 1476
    //   1527: aload #18
    //   1529: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1532: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1535: getfield weight : F
    //   1538: fconst_0
    //   1539: fcmpl
    //   1540: ifle -> 1521
    //   1543: aload #18
    //   1545: aload #18
    //   1547: invokevirtual getMeasuredWidth : ()I
    //   1550: ldc 1073741824
    //   1552: invokestatic makeMeasureSpec : (II)I
    //   1555: iload #16
    //   1557: ldc 1073741824
    //   1559: invokestatic makeMeasureSpec : (II)I
    //   1562: invokevirtual measure : (II)V
    //   1565: goto -> 1521
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider != null) {
      if (this.mOrientation == 1) {
        drawDividersVertical(paramCanvas);
        return;
      } 
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (Build.VERSION.SDK_INT >= 14) {
      super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    } 
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    if (Build.VERSION.SDK_INT >= 14) {
      super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
      paramAccessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
      return;
    } 
    measureHorizontal(paramInt1, paramInt2);
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt < 0 || paramInt >= getChildCount())
      throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")"); 
    this.mBaselineAlignedChildIndex = paramInt;
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    boolean bool = false;
    if (paramDrawable != this.mDivider) {
      this.mDivider = paramDrawable;
      if (paramDrawable != null) {
        this.mDividerWidth = paramDrawable.getIntrinsicWidth();
        this.mDividerHeight = paramDrawable.getIntrinsicHeight();
      } else {
        this.mDividerWidth = 0;
        this.mDividerHeight = 0;
      } 
      if (paramDrawable == null)
        bool = true; 
      setWillNotDraw(bool);
      requestLayout();
    } 
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    if ((this.mGravity & 0x800007) != paramInt) {
      this.mGravity = this.mGravity & 0xFF7FFFF8 | paramInt;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    if ((this.mGravity & 0x70) != paramInt) {
      this.mGravity = this.mGravity & 0xFFFFFF8F | paramInt;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface OrientationMode {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */