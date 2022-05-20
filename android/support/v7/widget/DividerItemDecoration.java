package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
  private static final int[] ATTRS = new int[] { 16843284 };
  
  public static final int HORIZONTAL = 0;
  
  public static final int VERTICAL = 1;
  
  private final Rect mBounds = new Rect();
  
  private Drawable mDivider;
  
  private int mOrientation;
  
  public DividerItemDecoration(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(ATTRS);
    this.mDivider = typedArray.getDrawable(0);
    typedArray.recycle();
    setOrientation(paramInt);
  }
  
  @SuppressLint({"NewApi"})
  private void drawHorizontal(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool;
    int i;
    paramCanvas.save();
    if (paramRecyclerView.getClipToPadding()) {
      bool = paramRecyclerView.getPaddingTop();
      i = paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom();
      paramCanvas.clipRect(paramRecyclerView.getPaddingLeft(), bool, paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight(), i);
    } else {
      bool = false;
      i = paramRecyclerView.getHeight();
    } 
    int j = paramRecyclerView.getChildCount();
    for (byte b = 0; b < j; b++) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getLayoutManager().getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.right + Math.round(ViewCompat.getTranslationX(view));
      int m = this.mDivider.getIntrinsicWidth();
      this.mDivider.setBounds(k - m, bool, k, i);
      this.mDivider.draw(paramCanvas);
    } 
    paramCanvas.restore();
  }
  
  @SuppressLint({"NewApi"})
  private void drawVertical(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool;
    int i;
    paramCanvas.save();
    if (paramRecyclerView.getClipToPadding()) {
      bool = paramRecyclerView.getPaddingLeft();
      i = paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight();
      paramCanvas.clipRect(bool, paramRecyclerView.getPaddingTop(), i, paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom());
    } else {
      bool = false;
      i = paramRecyclerView.getWidth();
    } 
    int j = paramRecyclerView.getChildCount();
    for (byte b = 0; b < j; b++) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.bottom + Math.round(ViewCompat.getTranslationY(view));
      int m = this.mDivider.getIntrinsicHeight();
      this.mDivider.setBounds(bool, k - m, i, k);
      this.mDivider.draw(paramCanvas);
    } 
    paramCanvas.restore();
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    if (this.mOrientation == 1) {
      paramRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
      return;
    } 
    paramRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    if (paramRecyclerView.getLayoutManager() != null) {
      if (this.mOrientation == 1) {
        drawVertical(paramCanvas, paramRecyclerView);
        return;
      } 
      drawHorizontal(paramCanvas, paramRecyclerView);
    } 
  }
  
  public void setDrawable(@NonNull Drawable paramDrawable) {
    if (paramDrawable == null)
      throw new IllegalArgumentException("Drawable cannot be null."); 
    this.mDivider = paramDrawable;
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt != 0 && paramInt != 1)
      throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL"); 
    this.mOrientation = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/DividerItemDecoration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */