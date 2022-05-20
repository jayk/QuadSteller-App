package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout extends LinearLayoutCompat {
  public AlertDialogLayout(@Nullable Context paramContext) {
    super(paramContext);
  }
  
  public AlertDialogLayout(@Nullable Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private static int resolveMinimumHeight(View paramView) {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i <= 0) {
      if (paramView instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)paramView;
        if (viewGroup.getChildCount() == 1)
          return resolveMinimumHeight(viewGroup.getChildAt(0)); 
      } 
      i = 0;
    } 
    return i;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2) {
    View view1 = null;
    View view2 = null;
    View view3 = null;
    int i = getChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getChildAt(j);
      if (view.getVisibility() != 8) {
        int i8 = view.getId();
        if (i8 == R.id.topPanel) {
          view1 = view;
        } else if (i8 == R.id.buttonPanel) {
          view2 = view;
        } else if (i8 == R.id.contentPanel || i8 == R.id.customPanel) {
          if (view3 != null)
            return false; 
          view3 = view;
        } else {
          return false;
        } 
      } 
    } 
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int i2 = 0;
    j = getPaddingTop() + getPaddingBottom();
    int k = j;
    if (view1 != null) {
      view1.measure(paramInt1, 0);
      k = j + view1.getMeasuredHeight();
      i2 = ViewCompat.combineMeasuredStates(0, ViewCompat.getMeasuredState(view1));
    } 
    j = 0;
    int i3 = 0;
    int i4 = i2;
    int i5 = k;
    if (view2 != null) {
      view2.measure(paramInt1, 0);
      j = resolveMinimumHeight(view2);
      i3 = view2.getMeasuredHeight() - j;
      i5 = k + j;
      i4 = ViewCompat.combineMeasuredStates(i2, ViewCompat.getMeasuredState(view2));
    } 
    int i6 = 0;
    k = i4;
    i2 = i5;
    if (view3 != null) {
      if (m == 0) {
        k = 0;
      } else {
        k = View.MeasureSpec.makeMeasureSpec(Math.max(0, n - i5), m);
      } 
      view3.measure(paramInt1, k);
      i6 = view3.getMeasuredHeight();
      i2 = i5 + i6;
      k = ViewCompat.combineMeasuredStates(i4, ViewCompat.getMeasuredState(view3));
    } 
    int i7 = n - i2;
    i5 = k;
    n = i7;
    i4 = i2;
    if (view2 != null) {
      i3 = Math.min(i7, i3);
      i4 = j;
      i5 = i7;
      if (i3 > 0) {
        i5 = i7 - i3;
        i4 = j + i3;
      } 
      view2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
      i4 = i2 - j + view2.getMeasuredHeight();
      j = ViewCompat.combineMeasuredStates(k, ViewCompat.getMeasuredState(view2));
      n = i5;
      i5 = j;
    } 
    k = i5;
    j = i4;
    if (view3 != null) {
      k = i5;
      j = i4;
      if (n > 0) {
        view3.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i6 + n, m));
        j = i4 - i6 + view3.getMeasuredHeight();
        k = ViewCompat.combineMeasuredStates(i5, ViewCompat.getMeasuredState(view3));
      } 
    } 
    i5 = 0;
    i4 = 0;
    while (i4 < i) {
      View view = getChildAt(i4);
      i2 = i5;
      if (view.getVisibility() != 8)
        i2 = Math.max(i5, view.getMeasuredWidth()); 
      i4++;
      i5 = i2;
    } 
    setMeasuredDimension(ViewCompat.resolveSizeAndState(i5 + getPaddingLeft() + getPaddingRight(), paramInt1, k), ViewCompat.resolveSizeAndState(j, paramInt2, 0));
    if (i1 != 1073741824)
      forceUniformWidth(i, paramInt2); 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getPaddingLeft : ()I
    //   4: istore #6
    //   6: iload #4
    //   8: iload_2
    //   9: isub
    //   10: istore #7
    //   12: aload_0
    //   13: invokevirtual getPaddingRight : ()I
    //   16: istore #8
    //   18: aload_0
    //   19: invokevirtual getPaddingRight : ()I
    //   22: istore #9
    //   24: aload_0
    //   25: invokevirtual getMeasuredHeight : ()I
    //   28: istore_2
    //   29: aload_0
    //   30: invokevirtual getChildCount : ()I
    //   33: istore #10
    //   35: aload_0
    //   36: invokevirtual getGravity : ()I
    //   39: istore #11
    //   41: iload #11
    //   43: bipush #112
    //   45: iand
    //   46: lookupswitch default -> 72, 16 -> 294, 80 -> 279
    //   72: aload_0
    //   73: invokevirtual getPaddingTop : ()I
    //   76: istore_2
    //   77: aload_0
    //   78: invokevirtual getDividerDrawable : ()Landroid/graphics/drawable/Drawable;
    //   81: astore #12
    //   83: aload #12
    //   85: ifnonnull -> 311
    //   88: iconst_0
    //   89: istore #4
    //   91: iconst_0
    //   92: istore #5
    //   94: iload #5
    //   96: iload #10
    //   98: if_icmpge -> 371
    //   101: aload_0
    //   102: iload #5
    //   104: invokevirtual getChildAt : (I)Landroid/view/View;
    //   107: astore #13
    //   109: iload_2
    //   110: istore_3
    //   111: aload #13
    //   113: ifnull -> 271
    //   116: iload_2
    //   117: istore_3
    //   118: aload #13
    //   120: invokevirtual getVisibility : ()I
    //   123: bipush #8
    //   125: if_icmpeq -> 271
    //   128: aload #13
    //   130: invokevirtual getMeasuredWidth : ()I
    //   133: istore #14
    //   135: aload #13
    //   137: invokevirtual getMeasuredHeight : ()I
    //   140: istore #15
    //   142: aload #13
    //   144: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   147: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   150: astore #12
    //   152: aload #12
    //   154: getfield gravity : I
    //   157: istore #16
    //   159: iload #16
    //   161: istore_3
    //   162: iload #16
    //   164: ifge -> 173
    //   167: iload #11
    //   169: ldc 8388615
    //   171: iand
    //   172: istore_3
    //   173: iload_3
    //   174: aload_0
    //   175: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   178: invokestatic getAbsoluteGravity : (II)I
    //   181: bipush #7
    //   183: iand
    //   184: lookupswitch default -> 212, 1 -> 321, 5 -> 353
    //   212: iload #6
    //   214: aload #12
    //   216: getfield leftMargin : I
    //   219: iadd
    //   220: istore_3
    //   221: iload_2
    //   222: istore #16
    //   224: aload_0
    //   225: iload #5
    //   227: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   230: ifeq -> 239
    //   233: iload_2
    //   234: iload #4
    //   236: iadd
    //   237: istore #16
    //   239: iload #16
    //   241: aload #12
    //   243: getfield topMargin : I
    //   246: iadd
    //   247: istore_2
    //   248: aload_0
    //   249: aload #13
    //   251: iload_3
    //   252: iload_2
    //   253: iload #14
    //   255: iload #15
    //   257: invokespecial setChildFrame : (Landroid/view/View;IIII)V
    //   260: iload_2
    //   261: aload #12
    //   263: getfield bottomMargin : I
    //   266: iload #15
    //   268: iadd
    //   269: iadd
    //   270: istore_3
    //   271: iinc #5, 1
    //   274: iload_3
    //   275: istore_2
    //   276: goto -> 94
    //   279: aload_0
    //   280: invokevirtual getPaddingTop : ()I
    //   283: iload #5
    //   285: iadd
    //   286: iload_3
    //   287: isub
    //   288: iload_2
    //   289: isub
    //   290: istore_2
    //   291: goto -> 77
    //   294: aload_0
    //   295: invokevirtual getPaddingTop : ()I
    //   298: iload #5
    //   300: iload_3
    //   301: isub
    //   302: iload_2
    //   303: isub
    //   304: iconst_2
    //   305: idiv
    //   306: iadd
    //   307: istore_2
    //   308: goto -> 77
    //   311: aload #12
    //   313: invokevirtual getIntrinsicHeight : ()I
    //   316: istore #4
    //   318: goto -> 91
    //   321: iload #7
    //   323: iload #6
    //   325: isub
    //   326: iload #9
    //   328: isub
    //   329: iload #14
    //   331: isub
    //   332: iconst_2
    //   333: idiv
    //   334: iload #6
    //   336: iadd
    //   337: aload #12
    //   339: getfield leftMargin : I
    //   342: iadd
    //   343: aload #12
    //   345: getfield rightMargin : I
    //   348: isub
    //   349: istore_3
    //   350: goto -> 221
    //   353: iload #7
    //   355: iload #8
    //   357: isub
    //   358: iload #14
    //   360: isub
    //   361: aload #12
    //   363: getfield rightMargin : I
    //   366: isub
    //   367: istore_3
    //   368: goto -> 221
    //   371: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (!tryOnMeasure(paramInt1, paramInt2))
      super.onMeasure(paramInt1, paramInt2); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/AlertDialogLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */