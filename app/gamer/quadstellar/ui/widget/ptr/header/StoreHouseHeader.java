package app.gamer.quadstellar.ui.widget.ptr.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import app.gamer.quadstellar.ui.widget.ptr.PtrFrameLayout;
import app.gamer.quadstellar.ui.widget.ptr.PtrIndicator;
import app.gamer.quadstellar.ui.widget.ptr.PtrLocalDisplay;
import app.gamer.quadstellar.ui.widget.ptr.PtrUIHandler;
import java.util.ArrayList;

public class StoreHouseHeader extends View implements PtrUIHandler {
  private AniController mAniController = new AniController();
  
  private float mBarDarkAlpha = 0.4F;
  
  private int mDrawZoneHeight = 0;
  
  private int mDrawZoneWidth = 0;
  
  private int mDropHeight = -1;
  
  private float mFromAlpha = 1.0F;
  
  private int mHorizontalRandomness = -1;
  
  private float mInternalAnimationFactor = 0.7F;
  
  private boolean mIsInLoading = false;
  
  public ArrayList<StoreHouseBarItem> mItemList = new ArrayList<StoreHouseBarItem>();
  
  private int mLineWidth = -1;
  
  private int mLoadingAniDuration = 1000;
  
  private int mLoadingAniItemDuration = 400;
  
  private int mLoadingAniSegDuration = 1000;
  
  private int mOffsetX = 0;
  
  private int mOffsetY = 0;
  
  private float mProgress = 0.0F;
  
  private float mScale = 1.0F;
  
  private int mTextColor = -1;
  
  private float mToAlpha = 0.4F;
  
  private Transformation mTransformation = new Transformation();
  
  public StoreHouseHeader(Context paramContext) {
    super(paramContext);
    initView();
  }
  
  public StoreHouseHeader(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public StoreHouseHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  private void beginLoading() {
    this.mIsInLoading = true;
    this.mAniController.start();
    invalidate();
  }
  
  private int getBottomOffset() {
    return getPaddingBottom() + PtrLocalDisplay.dp2px(10.0F);
  }
  
  private int getTopOffset() {
    return getPaddingTop() + PtrLocalDisplay.dp2px(10.0F);
  }
  
  private void initView() {
    PtrLocalDisplay.init(getContext());
    this.mLineWidth = PtrLocalDisplay.dp2px(1.0F);
    this.mDropHeight = PtrLocalDisplay.dp2px(40.0F);
    this.mHorizontalRandomness = PtrLocalDisplay.SCREEN_WIDTH_PIXELS / 2;
  }
  
  private void loadFinish() {
    this.mIsInLoading = false;
    this.mAniController.stop();
  }
  
  private void setProgress(float paramFloat) {
    this.mProgress = paramFloat;
  }
  
  public int getLoadingAniDuration() {
    return this.mLoadingAniDuration;
  }
  
  public float getScale() {
    return this.mScale;
  }
  
  public void initWithPointList(ArrayList<float[]> paramArrayList) {
    boolean bool;
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (this.mItemList.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mItemList.clear();
    for (byte b = 0; b < paramArrayList.size(); b++) {
      float[] arrayOfFloat = paramArrayList.get(b);
      PointF pointF2 = new PointF(PtrLocalDisplay.dp2px(arrayOfFloat[0]) * this.mScale, PtrLocalDisplay.dp2px(arrayOfFloat[1]) * this.mScale);
      PointF pointF1 = new PointF(PtrLocalDisplay.dp2px(arrayOfFloat[2]) * this.mScale, PtrLocalDisplay.dp2px(arrayOfFloat[3]) * this.mScale);
      f1 = Math.max(Math.max(f1, pointF2.x), pointF1.x);
      f2 = Math.max(Math.max(f2, pointF2.y), pointF1.y);
      StoreHouseBarItem storeHouseBarItem = new StoreHouseBarItem(b, pointF2, pointF1, this.mTextColor, this.mLineWidth);
      storeHouseBarItem.resetPosition(this.mHorizontalRandomness);
      this.mItemList.add(storeHouseBarItem);
    } 
    this.mDrawZoneWidth = (int)Math.ceil(f1);
    this.mDrawZoneHeight = (int)Math.ceil(f2);
    if (bool)
      requestLayout(); 
  }
  
  public void initWithString(String paramString) {
    initWithString(paramString, 25);
  }
  
  public void initWithString(String paramString, int paramInt) {
    initWithPointList(StoreHousePath.getPath(paramString, paramInt * 0.01F, 14));
  }
  
  public void initWithStringArray(int paramInt) {
    String[] arrayOfString = getResources().getStringArray(paramInt);
    ArrayList<float[]> arrayList = new ArrayList();
    for (paramInt = 0; paramInt < arrayOfString.length; paramInt++) {
      String[] arrayOfString1 = arrayOfString[paramInt].split(",");
      float[] arrayOfFloat = new float[4];
      for (byte b = 0; b < 4; b++)
        arrayOfFloat[b] = Float.parseFloat(arrayOfString1[b]); 
      arrayList.add(arrayOfFloat);
    } 
    initWithPointList(arrayList);
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    float f = this.mProgress;
    int i = paramCanvas.save();
    int j = this.mItemList.size();
    byte b = 0;
    while (true) {
      StoreHouseBarItem storeHouseBarItem;
      if (b < j) {
        paramCanvas.save();
        storeHouseBarItem = this.mItemList.get(b);
        float f1 = this.mOffsetX + storeHouseBarItem.midPoint.x;
        float f2 = this.mOffsetY + storeHouseBarItem.midPoint.y;
        if (this.mIsInLoading) {
          storeHouseBarItem.getTransformation(getDrawingTime(), this.mTransformation);
          paramCanvas.translate(f1, f2);
        } else {
          if (f == 0.0F) {
            storeHouseBarItem.resetPosition(this.mHorizontalRandomness);
          } else {
            float f3 = (1.0F - this.mInternalAnimationFactor) * b / j;
            float f4 = this.mInternalAnimationFactor;
            if (f == 1.0F || f >= 1.0F - 1.0F - f4 - f3) {
              paramCanvas.translate(f1, f2);
              storeHouseBarItem.setAlpha(this.mBarDarkAlpha);
            } else {
              if (f <= f3) {
                f3 = 0.0F;
              } else {
                f3 = Math.min(1.0F, (f - f3) / this.mInternalAnimationFactor);
              } 
              f4 = storeHouseBarItem.translationX;
              float f5 = -this.mDropHeight;
              Matrix matrix = new Matrix();
              matrix.postRotate(360.0F * f3);
              matrix.postScale(f3, f3);
              matrix.postTranslate(f1 + f4 * (1.0F - f3), f2 + f5 * (1.0F - f3));
              storeHouseBarItem.setAlpha(this.mBarDarkAlpha * f3);
              paramCanvas.concat(matrix);
            } 
            storeHouseBarItem.draw(paramCanvas);
            paramCanvas.restore();
          } 
          b++;
          continue;
        } 
      } else {
        break;
      } 
      storeHouseBarItem.draw(paramCanvas);
      paramCanvas.restore();
    } 
    if (this.mIsInLoading)
      invalidate(); 
    paramCanvas.restoreToCount(i);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(getTopOffset() + this.mDrawZoneHeight + getBottomOffset(), 1073741824));
    this.mOffsetX = (getMeasuredWidth() - this.mDrawZoneWidth) / 2;
    this.mOffsetY = getTopOffset();
    this.mDropHeight = getTopOffset();
  }
  
  public void onUIPositionChange(PtrFrameLayout paramPtrFrameLayout, boolean paramBoolean, byte paramByte, PtrIndicator paramPtrIndicator) {
    setProgress(Math.min(1.0F, paramPtrIndicator.getCurrentPercent()));
    invalidate();
  }
  
  public void onUIRefreshBegin(PtrFrameLayout paramPtrFrameLayout) {
    beginLoading();
  }
  
  public void onUIRefreshComplete(PtrFrameLayout paramPtrFrameLayout) {
    loadFinish();
  }
  
  public void onUIRefreshPrepare(PtrFrameLayout paramPtrFrameLayout) {}
  
  public void onUIReset(PtrFrameLayout paramPtrFrameLayout) {
    loadFinish();
    for (byte b = 0; b < this.mItemList.size(); b++)
      ((StoreHouseBarItem)this.mItemList.get(b)).resetPosition(this.mHorizontalRandomness); 
  }
  
  public StoreHouseHeader setDropHeight(int paramInt) {
    this.mDropHeight = paramInt;
    return this;
  }
  
  public StoreHouseHeader setLineWidth(int paramInt) {
    this.mLineWidth = paramInt;
    for (byte b = 0; b < this.mItemList.size(); b++)
      ((StoreHouseBarItem)this.mItemList.get(b)).setLineWidth(paramInt); 
    return this;
  }
  
  public void setLoadingAniDuration(int paramInt) {
    this.mLoadingAniDuration = paramInt;
    this.mLoadingAniSegDuration = paramInt;
  }
  
  public void setScale(float paramFloat) {
    this.mScale = paramFloat;
  }
  
  public StoreHouseHeader setTextColor(int paramInt) {
    this.mTextColor = paramInt;
    for (byte b = 0; b < this.mItemList.size(); b++)
      ((StoreHouseBarItem)this.mItemList.get(b)).setColor(paramInt); 
    return this;
  }
  
  private class AniController implements Runnable {
    private int mCountPerSeg = 0;
    
    private int mInterval = 0;
    
    private boolean mRunning = true;
    
    private int mSegCount = 0;
    
    private int mTick = 0;
    
    private AniController() {}
    
    private void start() {
      this.mRunning = true;
      this.mTick = 0;
      this.mInterval = StoreHouseHeader.this.mLoadingAniDuration / StoreHouseHeader.this.mItemList.size();
      this.mCountPerSeg = StoreHouseHeader.this.mLoadingAniSegDuration / this.mInterval;
      this.mSegCount = StoreHouseHeader.this.mItemList.size() / this.mCountPerSeg + 1;
      run();
    }
    
    private void stop() {
      this.mRunning = false;
      StoreHouseHeader.this.removeCallbacks(this);
    }
    
    public void run() {
      int i = this.mTick;
      int j = this.mCountPerSeg;
      for (byte b = 0; b < this.mSegCount; b++) {
        int k = this.mCountPerSeg * b + i % j;
        if (k <= this.mTick) {
          int m = StoreHouseHeader.this.mItemList.size();
          StoreHouseBarItem storeHouseBarItem = StoreHouseHeader.this.mItemList.get(k % m);
          storeHouseBarItem.setFillAfter(false);
          storeHouseBarItem.setFillEnabled(true);
          storeHouseBarItem.setFillBefore(false);
          storeHouseBarItem.setDuration(StoreHouseHeader.this.mLoadingAniItemDuration);
          storeHouseBarItem.start(StoreHouseHeader.this.mFromAlpha, StoreHouseHeader.this.mToAlpha);
        } 
      } 
      this.mTick++;
      if (this.mRunning)
        StoreHouseHeader.this.postDelayed(this, this.mInterval); 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/header/StoreHouseHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */