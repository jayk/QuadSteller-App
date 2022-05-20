package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

@TargetApi(21)
@RequiresApi(21)
class ViewParentCompatLollipop {
  private static final String TAG = "ViewParentCompat";
  
  public static boolean onNestedFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    try {
      paramBoolean = paramViewParent.onNestedFling(paramView, paramFloat1, paramFloat2, paramBoolean);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onNestedFling", abstractMethodError);
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  public static boolean onNestedPreFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2) {
    boolean bool;
    try {
      bool = paramViewParent.onNestedPreFling(paramView, paramFloat1, paramFloat2);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onNestedPreFling", abstractMethodError);
      bool = false;
    } 
    return bool;
  }
  
  public static void onNestedPreScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    try {
      paramViewParent.onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfint);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onNestedPreScroll", abstractMethodError);
    } 
  }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    try {
      paramViewParent.onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onNestedScroll", abstractMethodError);
    } 
  }
  
  public static void onNestedScrollAccepted(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    try {
      paramViewParent.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onNestedScrollAccepted", abstractMethodError);
    } 
  }
  
  public static boolean onStartNestedScroll(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    boolean bool;
    try {
      bool = paramViewParent.onStartNestedScroll(paramView1, paramView2, paramInt);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onStartNestedScroll", abstractMethodError);
      bool = false;
    } 
    return bool;
  }
  
  public static void onStopNestedScroll(ViewParent paramViewParent, View paramView) {
    try {
      paramViewParent.onStopNestedScroll(paramView);
    } catch (AbstractMethodError abstractMethodError) {
      Log.e("ViewParentCompat", "ViewParent " + paramViewParent + " does not implement interface " + "method onStopNestedScroll", abstractMethodError);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/ViewParentCompatLollipop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */