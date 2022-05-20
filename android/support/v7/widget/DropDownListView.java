package android.support.v7.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

class DropDownListView extends ListViewCompat {
  private ViewPropertyAnimatorCompat mClickAnimation;
  
  private boolean mDrawsInPressedState;
  
  private boolean mHijackFocus;
  
  private boolean mListSelectionHidden;
  
  private ListViewAutoScrollHelper mScrollHelper;
  
  public DropDownListView(Context paramContext, boolean paramBoolean) {
    super(paramContext, (AttributeSet)null, R.attr.dropDownListViewStyle);
    this.mHijackFocus = paramBoolean;
    setCacheColorHint(0);
  }
  
  private void clearPressedItem() {
    this.mDrawsInPressedState = false;
    setPressed(false);
    drawableStateChanged();
    View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
    if (view != null)
      view.setPressed(false); 
    if (this.mClickAnimation != null) {
      this.mClickAnimation.cancel();
      this.mClickAnimation = null;
    } 
  }
  
  private void clickPressedItem(View paramView, int paramInt) {
    performItemClick(paramView, paramInt, getItemIdAtPosition(paramInt));
  }
  
  private void setPressedItem(View paramView, int paramInt, float paramFloat1, float paramFloat2) {
    this.mDrawsInPressedState = true;
    if (Build.VERSION.SDK_INT >= 21)
      drawableHotspotChanged(paramFloat1, paramFloat2); 
    if (!isPressed())
      setPressed(true); 
    layoutChildren();
    if (this.mMotionPosition != -1) {
      View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
      if (view != null && view != paramView && view.isPressed())
        view.setPressed(false); 
    } 
    this.mMotionPosition = paramInt;
    float f1 = paramView.getLeft();
    float f2 = paramView.getTop();
    if (Build.VERSION.SDK_INT >= 21)
      paramView.drawableHotspotChanged(paramFloat1 - f1, paramFloat2 - f2); 
    if (!paramView.isPressed())
      paramView.setPressed(true); 
    positionSelectorLikeTouchCompat(paramInt, paramView, paramFloat1, paramFloat2);
    setSelectorEnabled(false);
    refreshDrawableState();
  }
  
  public boolean hasFocus() {
    return (this.mHijackFocus || super.hasFocus());
  }
  
  public boolean hasWindowFocus() {
    return (this.mHijackFocus || super.hasWindowFocus());
  }
  
  public boolean isFocused() {
    return (this.mHijackFocus || super.isFocused());
  }
  
  public boolean isInTouchMode() {
    return ((this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode());
  }
  
  public boolean onForwardedEvent(MotionEvent paramMotionEvent, int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_1
    //   3: istore #4
    //   5: iconst_0
    //   6: istore #5
    //   8: aload_1
    //   9: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   12: istore #6
    //   14: iload #6
    //   16: tableswitch default -> 44, 1 -> 114, 2 -> 116, 3 -> 106
    //   44: iload #4
    //   46: istore_3
    //   47: iload #5
    //   49: istore_2
    //   50: iload_3
    //   51: ifeq -> 58
    //   54: iload_2
    //   55: ifeq -> 62
    //   58: aload_0
    //   59: invokespecial clearPressedItem : ()V
    //   62: iload_3
    //   63: ifeq -> 231
    //   66: aload_0
    //   67: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   70: ifnonnull -> 85
    //   73: aload_0
    //   74: new android/support/v4/widget/ListViewAutoScrollHelper
    //   77: dup
    //   78: aload_0
    //   79: invokespecial <init> : (Landroid/widget/ListView;)V
    //   82: putfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   85: aload_0
    //   86: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   89: iconst_1
    //   90: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
    //   93: pop
    //   94: aload_0
    //   95: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   98: aload_0
    //   99: aload_1
    //   100: invokevirtual onTouch : (Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   103: pop
    //   104: iload_3
    //   105: ireturn
    //   106: iconst_0
    //   107: istore_3
    //   108: iload #5
    //   110: istore_2
    //   111: goto -> 50
    //   114: iconst_0
    //   115: istore_3
    //   116: aload_1
    //   117: iload_2
    //   118: invokevirtual findPointerIndex : (I)I
    //   121: istore #7
    //   123: iload #7
    //   125: ifge -> 136
    //   128: iconst_0
    //   129: istore_3
    //   130: iload #5
    //   132: istore_2
    //   133: goto -> 50
    //   136: aload_1
    //   137: iload #7
    //   139: invokevirtual getX : (I)F
    //   142: f2i
    //   143: istore_2
    //   144: aload_1
    //   145: iload #7
    //   147: invokevirtual getY : (I)F
    //   150: f2i
    //   151: istore #8
    //   153: aload_0
    //   154: iload_2
    //   155: iload #8
    //   157: invokevirtual pointToPosition : (II)I
    //   160: istore #7
    //   162: iload #7
    //   164: iconst_m1
    //   165: if_icmpne -> 173
    //   168: iconst_1
    //   169: istore_2
    //   170: goto -> 50
    //   173: aload_0
    //   174: iload #7
    //   176: aload_0
    //   177: invokevirtual getFirstVisiblePosition : ()I
    //   180: isub
    //   181: invokevirtual getChildAt : (I)Landroid/view/View;
    //   184: astore #9
    //   186: aload_0
    //   187: aload #9
    //   189: iload #7
    //   191: iload_2
    //   192: i2f
    //   193: iload #8
    //   195: i2f
    //   196: invokespecial setPressedItem : (Landroid/view/View;IFF)V
    //   199: iconst_1
    //   200: istore #4
    //   202: iload #5
    //   204: istore_2
    //   205: iload #4
    //   207: istore_3
    //   208: iload #6
    //   210: iconst_1
    //   211: if_icmpne -> 50
    //   214: aload_0
    //   215: aload #9
    //   217: iload #7
    //   219: invokespecial clickPressedItem : (Landroid/view/View;I)V
    //   222: iload #5
    //   224: istore_2
    //   225: iload #4
    //   227: istore_3
    //   228: goto -> 50
    //   231: aload_0
    //   232: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   235: ifnull -> 104
    //   238: aload_0
    //   239: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   242: iconst_0
    //   243: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
    //   246: pop
    //   247: goto -> 104
  }
  
  void setListSelectionHidden(boolean paramBoolean) {
    this.mListSelectionHidden = paramBoolean;
  }
  
  protected boolean touchModeDrawsInPressedStateCompat() {
    return (this.mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/DropDownListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */