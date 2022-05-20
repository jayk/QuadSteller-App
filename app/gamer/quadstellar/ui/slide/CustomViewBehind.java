package app.gamer.quadstellar.ui.slide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import app.gamer.quadstellar.utils.LogUtil;

public class CustomViewBehind extends ViewGroup {
  private static final int MARGIN_THRESHOLD = 48;
  
  private static final String TAG = "CustomViewBehind";
  
  private boolean mChildrenEnabled;
  
  private View mContent;
  
  private float mFadeDegree;
  
  private boolean mFadeEnabled;
  
  private final Paint mFadePaint = new Paint();
  
  private int mMarginThreshold = (int)TypedValue.applyDimension(1, 48.0F, getResources().getDisplayMetrics());
  
  private int mMode;
  
  private float mScrollScale;
  
  private View mSecondaryContent;
  
  private Drawable mSecondaryShadowDrawable;
  
  private View mSelectedView;
  
  private Bitmap mSelectorDrawable;
  
  private boolean mSelectorEnabled = true;
  
  private Drawable mShadowDrawable;
  
  private int mShadowWidth;
  
  private int mTouchMode = 0;
  
  private SlidingMenu.CanvasTransformer mTransformer;
  
  private CustomViewAbove mViewAbove;
  
  private int mWidthOffset;
  
  public CustomViewBehind(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CustomViewBehind(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private int getSelectorTop() {
    return this.mSelectedView.getTop() + (this.mSelectedView.getHeight() - this.mSelectorDrawable.getHeight()) / 2;
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    if (this.mTransformer != null) {
      paramCanvas.save();
      this.mTransformer.transformCanvas(paramCanvas, this.mViewAbove.getPercentOpen());
      super.dispatchDraw(paramCanvas);
      paramCanvas.restore();
      return;
    } 
    super.dispatchDraw(paramCanvas);
  }
  
  public void drawFade(View paramView, Canvas paramCanvas, float paramFloat) {
    if (this.mFadeEnabled) {
      int i = (int)(this.mFadeDegree * 255.0F * Math.abs(1.0F - paramFloat));
      this.mFadePaint.setColor(Color.argb(i, 0, 0, 0));
      i = 0;
      if (this.mMode == 0) {
        i = paramView.getLeft() - getBehindWidth();
        paramView.getLeft();
      } else if (this.mMode == 1) {
        i = paramView.getRight();
        paramView.getRight();
        getBehindWidth();
      } else if (this.mMode == 2) {
        int j = paramView.getLeft();
        int k = getBehindWidth();
        i = paramView.getLeft();
        paramCanvas.drawRect((j - k), 0.0F, i, getHeight(), this.mFadePaint);
        i = paramView.getRight();
        paramView.getRight();
        getBehindWidth();
      } 
      paramCanvas.drawRect(i, 0.0F, getWidth(), getHeight(), this.mFadePaint);
    } 
  }
  
  public void drawSelector(View paramView, Canvas paramCanvas, float paramFloat) {
    if (this.mSelectorEnabled && this.mSelectorDrawable != null && this.mSelectedView != null && ((String)this.mSelectedView.getTag(2131755033)).equals("CustomViewBehindSelectedView")) {
      paramCanvas.save();
      int i = (int)(this.mSelectorDrawable.getWidth() * paramFloat);
      if (this.mMode == 0) {
        int j = paramView.getLeft();
        i = j - i;
        paramCanvas.clipRect(i, 0, j, getHeight());
        paramCanvas.drawBitmap(this.mSelectorDrawable, i, getSelectorTop(), null);
      } else if (this.mMode == 1) {
        int j = paramView.getRight();
        i = j + i;
        paramCanvas.clipRect(j, 0, i, getHeight());
        paramCanvas.drawBitmap(this.mSelectorDrawable, (i - this.mSelectorDrawable.getWidth()), getSelectorTop(), null);
      } 
      paramCanvas.restore();
    } 
  }
  
  public void drawShadow(View paramView, Canvas paramCanvas) {
    if (this.mShadowDrawable != null && this.mShadowWidth > 0) {
      int i = 0;
      if (this.mMode == 0) {
        i = paramView.getLeft() - this.mShadowWidth;
      } else if (this.mMode == 1) {
        i = paramView.getRight();
      } else if (this.mMode == 2) {
        if (this.mSecondaryShadowDrawable != null) {
          i = paramView.getRight();
          this.mSecondaryShadowDrawable.setBounds(i, 0, this.mShadowWidth + i, getHeight());
          this.mSecondaryShadowDrawable.draw(paramCanvas);
        } 
        i = paramView.getLeft() - this.mShadowWidth;
      } 
      this.mShadowDrawable.setBounds(i, 0, this.mShadowWidth + i, getHeight());
      this.mShadowDrawable.draw(paramCanvas);
    } 
  }
  
  public int getAbsLeftBound(View paramView) {
    return (this.mMode == 0 || this.mMode == 2) ? (paramView.getLeft() - getBehindWidth()) : ((this.mMode == 1) ? paramView.getLeft() : 0);
  }
  
  public int getAbsRightBound(View paramView) {
    return (this.mMode == 0) ? paramView.getLeft() : ((this.mMode == 1 || this.mMode == 2) ? (paramView.getLeft() + getBehindWidth()) : 0);
  }
  
  public int getBehindWidth() {
    return this.mContent.getWidth();
  }
  
  public View getContent() {
    return this.mContent;
  }
  
  public int getMarginThreshold() {
    return this.mMarginThreshold;
  }
  
  public int getMenuLeft(View paramView, int paramInt) {
    if (this.mMode == 0) {
      switch (paramInt) {
        default:
          return paramView.getLeft();
        case 0:
          return paramView.getLeft() - getBehindWidth();
        case 2:
          break;
      } 
      return paramView.getLeft();
    } 
    if (this.mMode == 1) {
      switch (paramInt) {
        default:
        
        case 0:
          return paramView.getLeft();
        case 2:
          break;
      } 
      return paramView.getLeft() + getBehindWidth();
    } 
    if (this.mMode == 2) {
      switch (paramInt) {
        default:
        
        case 0:
          return paramView.getLeft() - getBehindWidth();
        case 2:
          break;
      } 
      return paramView.getLeft() + getBehindWidth();
    } 
  }
  
  public int getMenuPage(int paramInt) {
    int i;
    boolean bool = false;
    if (paramInt > 1) {
      i = 2;
    } else {
      i = paramInt;
      if (paramInt < 1)
        i = 0; 
    } 
    return (this.mMode == 0 && i > 1) ? bool : ((this.mMode == 1 && i < 1) ? 2 : i);
  }
  
  public int getMode() {
    return this.mMode;
  }
  
  public float getScrollScale() {
    return this.mScrollScale;
  }
  
  public View getSecondaryContent() {
    return this.mSecondaryContent;
  }
  
  public boolean marginTouchAllowed(View paramView, int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_1
    //   3: invokevirtual getLeft : ()I
    //   6: istore #4
    //   8: aload_1
    //   9: invokevirtual getRight : ()I
    //   12: istore #5
    //   14: aload_0
    //   15: getfield mMode : I
    //   18: ifne -> 50
    //   21: iload_2
    //   22: iload #4
    //   24: if_icmplt -> 44
    //   27: iload_2
    //   28: aload_0
    //   29: getfield mMarginThreshold : I
    //   32: iload #4
    //   34: iadd
    //   35: if_icmpgt -> 44
    //   38: iload_3
    //   39: istore #6
    //   41: iload #6
    //   43: ireturn
    //   44: iconst_0
    //   45: istore #6
    //   47: goto -> 41
    //   50: aload_0
    //   51: getfield mMode : I
    //   54: iconst_1
    //   55: if_icmpne -> 84
    //   58: iload_2
    //   59: iload #5
    //   61: if_icmpgt -> 78
    //   64: iload_3
    //   65: istore #6
    //   67: iload_2
    //   68: iload #5
    //   70: aload_0
    //   71: getfield mMarginThreshold : I
    //   74: isub
    //   75: if_icmpge -> 41
    //   78: iconst_0
    //   79: istore #6
    //   81: goto -> 41
    //   84: aload_0
    //   85: getfield mMode : I
    //   88: iconst_2
    //   89: if_icmpne -> 138
    //   92: iload_2
    //   93: iload #4
    //   95: if_icmplt -> 112
    //   98: iload_3
    //   99: istore #6
    //   101: iload_2
    //   102: aload_0
    //   103: getfield mMarginThreshold : I
    //   106: iload #4
    //   108: iadd
    //   109: if_icmple -> 41
    //   112: iload_2
    //   113: iload #5
    //   115: if_icmpgt -> 132
    //   118: iload_3
    //   119: istore #6
    //   121: iload_2
    //   122: iload #5
    //   124: aload_0
    //   125: getfield mMarginThreshold : I
    //   128: isub
    //   129: if_icmpge -> 41
    //   132: iconst_0
    //   133: istore #6
    //   135: goto -> 41
    //   138: iconst_0
    //   139: istore #6
    //   141: goto -> 41
  }
  
  public boolean menuClosedSlideAllowed(float paramFloat) {
    boolean bool = true;
    if (this.mMode == 0) {
      if (paramFloat <= 0.0F)
        bool = false; 
      return bool;
    } 
    if (this.mMode == 1) {
      if (paramFloat >= 0.0F)
        bool = false; 
      return bool;
    } 
    if (this.mMode != 2)
      bool = false; 
    return bool;
  }
  
  public boolean menuOpenSlideAllowed(float paramFloat) {
    boolean bool = true;
    if (this.mMode == 0) {
      if (paramFloat >= 0.0F)
        bool = false; 
      return bool;
    } 
    if (this.mMode == 1) {
      if (paramFloat <= 0.0F)
        bool = false; 
      return bool;
    } 
    if (this.mMode != 2)
      bool = false; 
    return bool;
  }
  
  public boolean menuOpenTouchAllowed(View paramView, int paramInt, float paramFloat) {
    switch (this.mTouchMode) {
      default:
        return false;
      case 1:
        return true;
      case 0:
        break;
    } 
    return menuTouchInQuickReturn(paramView, paramInt, paramFloat);
  }
  
  public boolean menuTouchInQuickReturn(View paramView, int paramInt, float paramFloat) {
    null = true;
    if (this.mMode == 0 || (this.mMode == 2 && paramInt == 0)) {
      if (paramFloat < paramView.getLeft())
        null = false; 
      return null;
    } 
    if (this.mMode == 1 || (this.mMode == 2 && paramInt == 2)) {
      if (paramFloat > paramView.getRight())
        null = false; 
      return null;
    } 
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    return !this.mChildrenEnabled;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 = paramInt3 - paramInt1;
    paramInt2 = paramInt4 - paramInt2;
    this.mContent.layout(0, 0, paramInt1 - this.mWidthOffset, paramInt2);
    if (this.mSecondaryContent != null)
      this.mSecondaryContent.layout(0, 0, paramInt1 - this.mWidthOffset, paramInt2); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getDefaultSize(0, paramInt1);
    int j = getDefaultSize(0, paramInt2);
    setMeasuredDimension(i, j);
    paramInt1 = getChildMeasureSpec(paramInt1, 0, i - this.mWidthOffset);
    paramInt2 = getChildMeasureSpec(paramInt2, 0, j);
    this.mContent.measure(paramInt1, paramInt2);
    if (this.mSecondaryContent != null)
      this.mSecondaryContent.measure(paramInt1, paramInt2); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return !this.mChildrenEnabled;
  }
  
  public void scrollBehindTo(View paramView, int paramInt1, int paramInt2) {
    boolean bool1 = false;
    boolean bool2 = false;
    byte b = 0;
    if (this.mMode == 0) {
      if (paramInt1 >= paramView.getLeft())
        b = 4; 
      scrollTo((int)((getBehindWidth() + paramInt1) * this.mScrollScale), paramInt2);
    } else if (this.mMode == 1) {
      b = bool2;
      if (paramInt1 <= paramView.getLeft())
        b = 4; 
      scrollTo((int)((getBehindWidth() - getWidth()) + (paramInt1 - getBehindWidth()) * this.mScrollScale), paramInt2);
    } else {
      b = bool1;
      if (this.mMode == 2) {
        View view = this.mContent;
        if (paramInt1 >= paramView.getLeft()) {
          b = 4;
        } else {
          b = 0;
        } 
        view.setVisibility(b);
        view = this.mSecondaryContent;
        if (paramInt1 <= paramView.getLeft()) {
          b = 4;
        } else {
          b = 0;
        } 
        view.setVisibility(b);
        if (paramInt1 == 0) {
          b = 4;
        } else {
          b = 0;
        } 
        if (paramInt1 <= paramView.getLeft()) {
          scrollTo((int)((getBehindWidth() + paramInt1) * this.mScrollScale), paramInt2);
        } else {
          scrollTo((int)((getBehindWidth() - getWidth()) + (paramInt1 - getBehindWidth()) * this.mScrollScale), paramInt2);
        } 
      } 
    } 
    if (b == 4)
      LogUtil.v("CustomViewBehind", "behind INVISIBLE"); 
    setVisibility(b);
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    super.scrollTo(paramInt1, paramInt2);
    if (this.mTransformer != null)
      invalidate(); 
  }
  
  public void setCanvasTransformer(SlidingMenu.CanvasTransformer paramCanvasTransformer) {
    this.mTransformer = paramCanvasTransformer;
  }
  
  public void setChildrenEnabled(boolean paramBoolean) {
    this.mChildrenEnabled = paramBoolean;
  }
  
  public void setContent(View paramView) {
    if (this.mContent != null)
      removeView(this.mContent); 
    this.mContent = paramView;
    addView(this.mContent);
  }
  
  public void setCustomViewAbove(CustomViewAbove paramCustomViewAbove) {
    this.mViewAbove = paramCustomViewAbove;
  }
  
  public void setFadeDegree(float paramFloat) {
    if (paramFloat > 1.0F || paramFloat < 0.0F)
      throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f"); 
    this.mFadeDegree = paramFloat;
  }
  
  public void setFadeEnabled(boolean paramBoolean) {
    this.mFadeEnabled = paramBoolean;
  }
  
  public void setMarginThreshold(int paramInt) {
    this.mMarginThreshold = paramInt;
  }
  
  public void setMode(int paramInt) {
    if (paramInt == 0 || paramInt == 1) {
      if (this.mContent != null)
        this.mContent.setVisibility(0); 
      if (this.mSecondaryContent != null)
        this.mSecondaryContent.setVisibility(4); 
    } 
    this.mMode = paramInt;
  }
  
  public void setScrollScale(float paramFloat) {
    this.mScrollScale = paramFloat;
  }
  
  public void setSecondaryContent(View paramView) {
    if (this.mSecondaryContent != null)
      removeView(this.mSecondaryContent); 
    this.mSecondaryContent = paramView;
    addView(this.mSecondaryContent);
  }
  
  public void setSecondaryShadowDrawable(Drawable paramDrawable) {
    this.mSecondaryShadowDrawable = paramDrawable;
    invalidate();
  }
  
  public void setSelectedView(View paramView) {
    if (this.mSelectedView != null) {
      this.mSelectedView.setTag(2131755033, null);
      this.mSelectedView = null;
    } 
    if (paramView != null && paramView.getParent() != null) {
      this.mSelectedView = paramView;
      this.mSelectedView.setTag(2131755033, "CustomViewBehindSelectedView");
      invalidate();
    } 
  }
  
  public void setSelectorBitmap(Bitmap paramBitmap) {
    this.mSelectorDrawable = paramBitmap;
    refreshDrawableState();
  }
  
  public void setSelectorEnabled(boolean paramBoolean) {
    this.mSelectorEnabled = paramBoolean;
  }
  
  public void setShadowDrawable(Drawable paramDrawable) {
    this.mShadowDrawable = paramDrawable;
    invalidate();
  }
  
  public void setShadowWidth(int paramInt) {
    this.mShadowWidth = paramInt;
    invalidate();
  }
  
  public void setTouchMode(int paramInt) {
    this.mTouchMode = paramInt;
  }
  
  public void setWidthOffset(int paramInt) {
    this.mWidthOffset = paramInt;
    requestLayout();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/CustomViewBehind.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */