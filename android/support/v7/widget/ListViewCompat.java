package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ListViewCompat extends ListView {
  public static final int INVALID_POSITION = -1;
  
  public static final int NO_POSITION = -1;
  
  private static final int[] STATE_SET_NOTHING = new int[] { 0 };
  
  private Field mIsChildViewEnabled;
  
  protected int mMotionPosition;
  
  int mSelectionBottomPadding = 0;
  
  int mSelectionLeftPadding = 0;
  
  int mSelectionRightPadding = 0;
  
  int mSelectionTopPadding = 0;
  
  private GateKeeperDrawable mSelector;
  
  final Rect mSelectorRect = new Rect();
  
  public ListViewCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ListViewCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ListViewCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    try {
      this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
      this.mIsChildViewEnabled.setAccessible(true);
    } catch (NoSuchFieldException noSuchFieldException) {
      noSuchFieldException.printStackTrace();
    } 
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    drawSelectorCompat(paramCanvas);
    super.dispatchDraw(paramCanvas);
  }
  
  protected void drawSelectorCompat(Canvas paramCanvas) {
    if (!this.mSelectorRect.isEmpty()) {
      Drawable drawable = getSelector();
      if (drawable != null) {
        drawable.setBounds(this.mSelectorRect);
        drawable.draw(paramCanvas);
      } 
    } 
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    setSelectorEnabled(true);
    updateSelectorStateCompat();
  }
  
  public int lookForSelectablePosition(int paramInt, boolean paramBoolean) {
    byte b = -1;
    ListAdapter listAdapter = getAdapter();
    int i = b;
    if (listAdapter != null) {
      if (isInTouchMode())
        return b; 
    } else {
      return i;
    } 
    int j = listAdapter.getCount();
    if (!getAdapter().areAllItemsEnabled()) {
      if (paramBoolean) {
        i = Math.max(0, paramInt);
        while (true) {
          paramInt = i;
          if (i < j) {
            paramInt = i;
            if (!listAdapter.isEnabled(i)) {
              i++;
              continue;
            } 
          } 
          break;
        } 
      } else {
        i = Math.min(paramInt, j - 1);
        while (true) {
          paramInt = i;
          if (i >= 0) {
            paramInt = i;
            if (!listAdapter.isEnabled(i)) {
              i--;
              continue;
            } 
          } 
          break;
        } 
      } 
      i = b;
      if (paramInt >= 0) {
        i = b;
        if (paramInt < j)
          i = paramInt; 
      } 
      return i;
    } 
    i = b;
    if (paramInt >= 0) {
      i = b;
      if (paramInt < j)
        i = paramInt; 
    } 
    return i;
  }
  
  public int measureHeightOfChildrenCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getListPaddingTop : ()I
    //   4: istore_3
    //   5: aload_0
    //   6: invokevirtual getListPaddingBottom : ()I
    //   9: istore_2
    //   10: aload_0
    //   11: invokevirtual getListPaddingLeft : ()I
    //   14: pop
    //   15: aload_0
    //   16: invokevirtual getListPaddingRight : ()I
    //   19: pop
    //   20: aload_0
    //   21: invokevirtual getDividerHeight : ()I
    //   24: istore #6
    //   26: aload_0
    //   27: invokevirtual getDivider : ()Landroid/graphics/drawable/Drawable;
    //   30: astore #7
    //   32: aload_0
    //   33: invokevirtual getAdapter : ()Landroid/widget/ListAdapter;
    //   36: astore #8
    //   38: aload #8
    //   40: ifnonnull -> 49
    //   43: iload_3
    //   44: iload_2
    //   45: iadd
    //   46: istore_1
    //   47: iload_1
    //   48: ireturn
    //   49: iload_3
    //   50: iload_2
    //   51: iadd
    //   52: istore_2
    //   53: iload #6
    //   55: ifle -> 254
    //   58: aload #7
    //   60: ifnull -> 254
    //   63: iconst_0
    //   64: istore_3
    //   65: aconst_null
    //   66: astore #7
    //   68: iconst_0
    //   69: istore #9
    //   71: aload #8
    //   73: invokeinterface getCount : ()I
    //   78: istore #10
    //   80: iconst_0
    //   81: istore #11
    //   83: iload #11
    //   85: iload #10
    //   87: if_icmpge -> 308
    //   90: aload #8
    //   92: iload #11
    //   94: invokeinterface getItemViewType : (I)I
    //   99: istore #12
    //   101: iload #9
    //   103: istore #13
    //   105: iload #12
    //   107: iload #9
    //   109: if_icmpeq -> 119
    //   112: aconst_null
    //   113: astore #7
    //   115: iload #12
    //   117: istore #13
    //   119: aload #8
    //   121: iload #11
    //   123: aload #7
    //   125: aload_0
    //   126: invokeinterface getView : (ILandroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   131: astore #14
    //   133: aload #14
    //   135: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   138: astore #15
    //   140: aload #15
    //   142: astore #7
    //   144: aload #15
    //   146: ifnonnull -> 162
    //   149: aload_0
    //   150: invokevirtual generateDefaultLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   153: astore #7
    //   155: aload #14
    //   157: aload #7
    //   159: invokevirtual setLayoutParams : (Landroid/view/ViewGroup$LayoutParams;)V
    //   162: aload #7
    //   164: getfield height : I
    //   167: ifle -> 260
    //   170: aload #7
    //   172: getfield height : I
    //   175: ldc 1073741824
    //   177: invokestatic makeMeasureSpec : (II)I
    //   180: istore #9
    //   182: aload #14
    //   184: iload_1
    //   185: iload #9
    //   187: invokevirtual measure : (II)V
    //   190: aload #14
    //   192: invokevirtual forceLayout : ()V
    //   195: iload_2
    //   196: istore #9
    //   198: iload #11
    //   200: ifle -> 209
    //   203: iload_2
    //   204: iload #6
    //   206: iadd
    //   207: istore #9
    //   209: iload #9
    //   211: aload #14
    //   213: invokevirtual getMeasuredHeight : ()I
    //   216: iadd
    //   217: istore_2
    //   218: iload_2
    //   219: iload #4
    //   221: if_icmplt -> 270
    //   224: iload #5
    //   226: iflt -> 248
    //   229: iload #11
    //   231: iload #5
    //   233: if_icmple -> 248
    //   236: iload_3
    //   237: ifle -> 248
    //   240: iload_3
    //   241: istore_1
    //   242: iload_2
    //   243: iload #4
    //   245: if_icmpne -> 47
    //   248: iload #4
    //   250: istore_1
    //   251: goto -> 47
    //   254: iconst_0
    //   255: istore #6
    //   257: goto -> 63
    //   260: iconst_0
    //   261: iconst_0
    //   262: invokestatic makeMeasureSpec : (II)I
    //   265: istore #9
    //   267: goto -> 182
    //   270: iload_3
    //   271: istore #9
    //   273: iload #5
    //   275: iflt -> 291
    //   278: iload_3
    //   279: istore #9
    //   281: iload #11
    //   283: iload #5
    //   285: if_icmplt -> 291
    //   288: iload_2
    //   289: istore #9
    //   291: iinc #11, 1
    //   294: aload #14
    //   296: astore #7
    //   298: iload #9
    //   300: istore_3
    //   301: iload #13
    //   303: istore #9
    //   305: goto -> 83
    //   308: iload_2
    //   309: istore_1
    //   310: goto -> 47
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    switch (paramMotionEvent.getAction()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 0:
        break;
    } 
    this.mMotionPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
  }
  
  protected void positionSelectorCompat(int paramInt, View paramView) {
    Rect rect = this.mSelectorRect;
    rect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    rect.left -= this.mSelectionLeftPadding;
    rect.top -= this.mSelectionTopPadding;
    rect.right += this.mSelectionRightPadding;
    rect.bottom += this.mSelectionBottomPadding;
    try {
      boolean bool = this.mIsChildViewEnabled.getBoolean(this);
      if (paramView.isEnabled() != bool) {
        Field field = this.mIsChildViewEnabled;
        if (!bool) {
          bool = true;
        } else {
          bool = false;
        } 
        field.set(this, Boolean.valueOf(bool));
        if (paramInt != -1)
          refreshDrawableState(); 
      } 
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } 
  }
  
  protected void positionSelectorLikeFocusCompat(int paramInt, View paramView) {
    boolean bool2;
    boolean bool1 = true;
    Drawable drawable = getSelector();
    if (drawable != null && paramInt != -1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool2)
      drawable.setVisible(false, false); 
    positionSelectorCompat(paramInt, paramView);
    if (bool2) {
      Rect rect = this.mSelectorRect;
      float f1 = rect.exactCenterX();
      float f2 = rect.exactCenterY();
      if (getVisibility() != 0)
        bool1 = false; 
      drawable.setVisible(bool1, false);
      DrawableCompat.setHotspot(drawable, f1, f2);
    } 
  }
  
  protected void positionSelectorLikeTouchCompat(int paramInt, View paramView, float paramFloat1, float paramFloat2) {
    positionSelectorLikeFocusCompat(paramInt, paramView);
    Drawable drawable = getSelector();
    if (drawable != null && paramInt != -1)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  public void setSelector(Drawable paramDrawable) {
    GateKeeperDrawable gateKeeperDrawable;
    if (paramDrawable != null) {
      gateKeeperDrawable = new GateKeeperDrawable(paramDrawable);
    } else {
      gateKeeperDrawable = null;
    } 
    this.mSelector = gateKeeperDrawable;
    super.setSelector((Drawable)this.mSelector);
    Rect rect = new Rect();
    if (paramDrawable != null)
      paramDrawable.getPadding(rect); 
    this.mSelectionLeftPadding = rect.left;
    this.mSelectionTopPadding = rect.top;
    this.mSelectionRightPadding = rect.right;
    this.mSelectionBottomPadding = rect.bottom;
  }
  
  protected void setSelectorEnabled(boolean paramBoolean) {
    if (this.mSelector != null)
      this.mSelector.setEnabled(paramBoolean); 
  }
  
  protected boolean shouldShowSelectorCompat() {
    return (touchModeDrawsInPressedStateCompat() && isPressed());
  }
  
  protected boolean touchModeDrawsInPressedStateCompat() {
    return false;
  }
  
  protected void updateSelectorStateCompat() {
    Drawable drawable = getSelector();
    if (drawable != null && shouldShowSelectorCompat())
      drawable.setState(getDrawableState()); 
  }
  
  private static class GateKeeperDrawable extends DrawableWrapper {
    private boolean mEnabled = true;
    
    public GateKeeperDrawable(Drawable param1Drawable) {
      super(param1Drawable);
    }
    
    public void draw(Canvas param1Canvas) {
      if (this.mEnabled)
        super.draw(param1Canvas); 
    }
    
    void setEnabled(boolean param1Boolean) {
      this.mEnabled = param1Boolean;
    }
    
    public void setHotspot(float param1Float1, float param1Float2) {
      if (this.mEnabled)
        super.setHotspot(param1Float1, param1Float2); 
    }
    
    public void setHotspotBounds(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mEnabled)
        super.setHotspotBounds(param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    public boolean setState(int[] param1ArrayOfint) {
      return this.mEnabled ? super.setState(param1ArrayOfint) : false;
    }
    
    public boolean setVisible(boolean param1Boolean1, boolean param1Boolean2) {
      return this.mEnabled ? super.setVisible(param1Boolean1, param1Boolean2) : false;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ListViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */