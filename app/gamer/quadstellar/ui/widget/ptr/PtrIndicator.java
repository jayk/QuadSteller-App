package app.gamer.quadstellar.ui.widget.ptr;

import android.graphics.PointF;

public class PtrIndicator {
  public static final int POS_START = 0;
  
  private int mCurrentPos = 0;
  
  private int mHeaderHeight;
  
  private boolean mIsUnderTouch = false;
  
  private int mLastPos = 0;
  
  private int mOffsetToKeepHeaderWhileLoading = -1;
  
  protected int mOffsetToRefresh = 0;
  
  private float mOffsetX;
  
  private float mOffsetY;
  
  private int mPressedPos = 0;
  
  private PointF mPtLastMove = new PointF();
  
  private float mRatioOfHeaderHeightToRefresh = 1.2F;
  
  private int mRefreshCompleteY = 0;
  
  private float mResistance = 1.7F;
  
  public void convertFrom(PtrIndicator paramPtrIndicator) {
    this.mCurrentPos = paramPtrIndicator.mCurrentPos;
    this.mLastPos = paramPtrIndicator.mLastPos;
    this.mHeaderHeight = paramPtrIndicator.mHeaderHeight;
  }
  
  public boolean crossRefreshLineFromTopToBottom() {
    return (this.mLastPos < getOffsetToRefresh() && this.mCurrentPos >= getOffsetToRefresh());
  }
  
  public float getCurrentPercent() {
    return (this.mHeaderHeight == 0) ? 0.0F : (this.mCurrentPos * 1.0F / this.mHeaderHeight);
  }
  
  public int getCurrentPosY() {
    return this.mCurrentPos;
  }
  
  public int getHeaderHeight() {
    return this.mHeaderHeight;
  }
  
  public float getLastPercent() {
    return (this.mHeaderHeight == 0) ? 0.0F : (this.mLastPos * 1.0F / this.mHeaderHeight);
  }
  
  public int getLastPosY() {
    return this.mLastPos;
  }
  
  public int getOffsetToKeepHeaderWhileLoading() {
    return (this.mOffsetToKeepHeaderWhileLoading >= 0) ? this.mOffsetToKeepHeaderWhileLoading : this.mHeaderHeight;
  }
  
  public int getOffsetToRefresh() {
    return this.mOffsetToRefresh;
  }
  
  public float getOffsetX() {
    return this.mOffsetX;
  }
  
  public float getOffsetY() {
    return this.mOffsetY;
  }
  
  public float getRatioOfHeaderToHeightRefresh() {
    return this.mRatioOfHeaderHeightToRefresh;
  }
  
  public float getResistance() {
    return this.mResistance;
  }
  
  public boolean goDownCrossFinishPosition() {
    return (this.mCurrentPos >= this.mRefreshCompleteY);
  }
  
  public boolean hasJustBackToStartPosition() {
    return (this.mLastPos != 0 && isInStartPosition());
  }
  
  public boolean hasJustLeftStartPosition() {
    return (this.mLastPos == 0 && hasLeftStartPosition());
  }
  
  public boolean hasJustReachedHeaderHeightFromTopToBottom() {
    return (this.mLastPos < this.mHeaderHeight && this.mCurrentPos >= this.mHeaderHeight);
  }
  
  public boolean hasLeftStartPosition() {
    return (this.mCurrentPos > 0);
  }
  
  public boolean hasMovedAfterPressedDown() {
    return (this.mCurrentPos != this.mPressedPos);
  }
  
  public boolean isAlreadyHere(int paramInt) {
    return (this.mCurrentPos == paramInt);
  }
  
  public boolean isInStartPosition() {
    return (this.mCurrentPos == 0);
  }
  
  public boolean isOverOffsetToKeepHeaderWhileLoading() {
    return (this.mCurrentPos > getOffsetToKeepHeaderWhileLoading());
  }
  
  public boolean isOverOffsetToRefresh() {
    return (this.mCurrentPos >= getOffsetToRefresh());
  }
  
  public boolean isUnderTouch() {
    return this.mIsUnderTouch;
  }
  
  public final void onMove(float paramFloat1, float paramFloat2) {
    processOnMove(paramFloat1, paramFloat2, paramFloat1 - this.mPtLastMove.x, paramFloat2 - this.mPtLastMove.y);
    this.mPtLastMove.set(paramFloat1, paramFloat2);
  }
  
  public void onPressDown(float paramFloat1, float paramFloat2) {
    this.mIsUnderTouch = true;
    this.mPressedPos = this.mCurrentPos;
    this.mPtLastMove.set(paramFloat1, paramFloat2);
  }
  
  public void onRelease() {
    this.mIsUnderTouch = false;
  }
  
  public void onUIRefreshComplete() {
    this.mRefreshCompleteY = this.mCurrentPos;
  }
  
  protected void onUpdatePos(int paramInt1, int paramInt2) {}
  
  protected void processOnMove(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    setOffset(paramFloat3, paramFloat4 / this.mResistance);
  }
  
  public final void setCurrentPos(int paramInt) {
    this.mLastPos = this.mCurrentPos;
    this.mCurrentPos = paramInt;
    onUpdatePos(paramInt, this.mLastPos);
  }
  
  public void setHeaderHeight(int paramInt) {
    this.mHeaderHeight = paramInt;
    updateHeight();
  }
  
  protected void setOffset(float paramFloat1, float paramFloat2) {
    this.mOffsetX = paramFloat1;
    this.mOffsetY = paramFloat2;
  }
  
  public void setOffsetToKeepHeaderWhileLoading(int paramInt) {
    this.mOffsetToKeepHeaderWhileLoading = paramInt;
  }
  
  public void setOffsetToRefresh(int paramInt) {
    this.mRatioOfHeaderHeightToRefresh = this.mHeaderHeight * 1.0F / paramInt;
    this.mOffsetToRefresh = paramInt;
  }
  
  public void setRatioOfHeaderHeightToRefresh(float paramFloat) {
    this.mRatioOfHeaderHeightToRefresh = paramFloat;
    this.mOffsetToRefresh = (int)(this.mHeaderHeight * paramFloat);
  }
  
  public void setResistance(float paramFloat) {
    this.mResistance = paramFloat;
  }
  
  protected void updateHeight() {
    this.mOffsetToRefresh = (int)(this.mRatioOfHeaderHeightToRefresh * this.mHeaderHeight);
  }
  
  public boolean willOverTop(int paramInt) {
    return (paramInt < 0);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrIndicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */