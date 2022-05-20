package app.gamer.quadstellar.ui.widget.ptr;

public class PtrTensionIndicator extends PtrIndicator {
  private float DRAG_RATE = 0.5F;
  
  private float mCurrentDragPercent;
  
  private float mDownPos;
  
  private float mDownY;
  
  private float mOneHeight = 0.0F;
  
  private float mReleasePercent = -1.0F;
  
  private int mReleasePos;
  
  private float offsetToTarget(float paramFloat) {
    float f = paramFloat / this.mOneHeight;
    this.mCurrentDragPercent = f;
    f = Math.min(1.0F, Math.abs(f));
    paramFloat = Math.max(0.0F, Math.min(paramFloat - this.mOneHeight, this.mOneHeight * 2.0F) / this.mOneHeight);
    paramFloat = (float)((paramFloat / 4.0F) - Math.pow((paramFloat / 4.0F), 2.0D));
    paramFloat = this.mOneHeight * paramFloat * 2.0F / 2.0F;
    int i = (int)(this.mOneHeight * f + paramFloat);
    return 0.0F;
  }
  
  public int getOffsetToKeepHeaderWhileLoading() {
    return getOffsetToRefresh();
  }
  
  public int getOffsetToRefresh() {
    return (int)this.mOneHeight;
  }
  
  public float getOverDragPercent() {
    return isUnderTouch() ? this.mCurrentDragPercent : ((this.mReleasePercent <= 0.0F) ? (1.0F * getCurrentPosY() / getOffsetToKeepHeaderWhileLoading()) : (this.mReleasePercent * getCurrentPosY() / this.mReleasePos));
  }
  
  public void onPressDown(float paramFloat1, float paramFloat2) {
    super.onPressDown(paramFloat1, paramFloat2);
    this.mDownY = paramFloat2;
    this.mDownPos = getCurrentPosY();
  }
  
  public void onRelease() {
    super.onRelease();
    this.mReleasePos = getCurrentPosY();
    this.mReleasePercent = this.mCurrentDragPercent;
  }
  
  public void onUIRefreshComplete() {
    this.mReleasePos = getCurrentPosY();
    this.mReleasePercent = getOverDragPercent();
  }
  
  protected void processOnMove(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (paramFloat2 < this.mDownY) {
      super.processOnMove(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      return;
    } 
    paramFloat4 = (paramFloat2 - this.mDownY) * this.DRAG_RATE + this.mDownPos;
    paramFloat2 = paramFloat4 / this.mOneHeight;
    if (paramFloat2 < 0.0F) {
      setOffset(paramFloat3, 0.0F);
      return;
    } 
    this.mCurrentDragPercent = paramFloat2;
    paramFloat2 = Math.min(1.0F, Math.abs(paramFloat2));
    paramFloat3 = Math.max(0.0F, Math.min(paramFloat4 - this.mOneHeight, this.mOneHeight * 2.0F) / this.mOneHeight);
    paramFloat3 = (float)((paramFloat3 / 4.0F) - Math.pow((paramFloat3 / 4.0F), 2.0D));
    paramFloat3 = this.mOneHeight * paramFloat3 * 2.0F / 2.0F;
    setOffset(paramFloat1, ((int)(this.mOneHeight * paramFloat2 + paramFloat3) - getCurrentPosY()));
  }
  
  public void setHeaderHeight(int paramInt) {
    super.setHeaderHeight(paramInt);
    this.mOneHeight = paramInt * 4.0F / 5.0F;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrTensionIndicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */