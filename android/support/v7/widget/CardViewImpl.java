package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@TargetApi(9)
@RequiresApi(9)
interface CardViewImpl {
  ColorStateList getBackgroundColor(CardViewDelegate paramCardViewDelegate);
  
  float getElevation(CardViewDelegate paramCardViewDelegate);
  
  float getMaxElevation(CardViewDelegate paramCardViewDelegate);
  
  float getMinHeight(CardViewDelegate paramCardViewDelegate);
  
  float getMinWidth(CardViewDelegate paramCardViewDelegate);
  
  float getRadius(CardViewDelegate paramCardViewDelegate);
  
  void initStatic();
  
  void initialize(CardViewDelegate paramCardViewDelegate, Context paramContext, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3);
  
  void onCompatPaddingChanged(CardViewDelegate paramCardViewDelegate);
  
  void onPreventCornerOverlapChanged(CardViewDelegate paramCardViewDelegate);
  
  void setBackgroundColor(CardViewDelegate paramCardViewDelegate, @Nullable ColorStateList paramColorStateList);
  
  void setElevation(CardViewDelegate paramCardViewDelegate, float paramFloat);
  
  void setMaxElevation(CardViewDelegate paramCardViewDelegate, float paramFloat);
  
  void setRadius(CardViewDelegate paramCardViewDelegate, float paramFloat);
  
  void updatePadding(CardViewDelegate paramCardViewDelegate);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/CardViewImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */