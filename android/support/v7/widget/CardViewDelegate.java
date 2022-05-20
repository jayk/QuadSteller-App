package android.support.v7.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(9)
@RequiresApi(9)
interface CardViewDelegate {
  Drawable getCardBackground();
  
  View getCardView();
  
  boolean getPreventCornerOverlap();
  
  boolean getUseCompatPadding();
  
  void setCardBackground(Drawable paramDrawable);
  
  void setMinWidthHeightInternal(int paramInt1, int paramInt2);
  
  void setShadowPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/CardViewDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */