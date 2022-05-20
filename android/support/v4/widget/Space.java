package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Space extends View {
  public Space(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Space(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Space(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    if (getVisibility() == 0)
      setVisibility(4); 
  }
  
  private static int getDefaultSize2(int paramInt1, int paramInt2) {
    int i = paramInt1;
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    switch (j) {
      default:
        paramInt1 = i;
      case 0:
        return paramInt1;
      case -2147483648:
        paramInt1 = Math.min(paramInt1, paramInt2);
      case 1073741824:
        break;
    } 
    paramInt1 = paramInt2;
  }
  
  public void draw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize2(getSuggestedMinimumWidth(), paramInt1), getDefaultSize2(getSuggestedMinimumHeight(), paramInt2));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/Space.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */