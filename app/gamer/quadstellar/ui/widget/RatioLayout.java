package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import app.gamer.quadstellar.R;

public class RatioLayout extends FrameLayout {
  public static final int RELATIVE_Height = 1;
  
  public static final int RELATIVE_WIDTH = 0;
  
  private static final String TAG = "RatioLayout";
  
  private float mRatio;
  
  private int mRelative;
  
  public RatioLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RatioLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RatioLayout);
    this.mRatio = typedArray.getFloat(0, 2.43F);
    this.mRelative = typedArray.getInt(1, 0);
    typedArray.recycle();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    if (i == 1073741824 && this.mRelative == 0) {
      Log.d("RatioLayout", "固定宽度");
      paramInt2 = View.MeasureSpec.getSize(paramInt1);
      i = paramInt2 - getPaddingLeft() - getPaddingRight();
      paramInt1 = (int)(i / this.mRatio + 0.5F);
      int k = getPaddingTop();
      j = getPaddingBottom();
      measureChildren(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824));
      setMeasuredDimension(paramInt2, k + paramInt1 + j);
      return;
    } 
    if (j == 1073741824 && this.mRelative == 1) {
      Log.d("RatioLayout", "固定高度");
      paramInt2 = View.MeasureSpec.getSize(paramInt2);
      paramInt1 = paramInt2 - getPaddingTop() - getPaddingBottom();
      i = (int)(paramInt1 * this.mRatio + 0.5F);
      int k = getPaddingLeft();
      j = getPaddingRight();
      measureChildren(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824));
      setMeasuredDimension(k + i + j, paramInt2);
      return;
    } 
    Log.d("RatioLayout", "执行默认测量");
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void setRatio(float paramFloat) {
    this.mRatio = paramFloat;
  }
  
  public void setRelative(int paramInt) {
    this.mRelative = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/RatioLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */