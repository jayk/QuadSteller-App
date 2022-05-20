package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
abstract class VisibilityPort extends TransitionPort {
  private static final String PROPNAME_PARENT = "android:visibility:parent";
  
  private static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
  
  private static final String[] sTransitionProperties = new String[] { "android:visibility:visibility", "android:visibility:parent" };
  
  private void captureValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getVisibility();
    paramTransitionValues.values.put("android:visibility:visibility", Integer.valueOf(i));
    paramTransitionValues.values.put("android:visibility:parent", paramTransitionValues.view.getParent());
  }
  
  private VisibilityInfo getVisibilityChangeInfo(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = new VisibilityInfo();
    visibilityInfo.visibilityChange = false;
    visibilityInfo.fadeIn = false;
    if (paramTransitionValues1 != null) {
      visibilityInfo.startVisibility = ((Integer)paramTransitionValues1.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.startParent = (ViewGroup)paramTransitionValues1.values.get("android:visibility:parent");
    } else {
      visibilityInfo.startVisibility = -1;
      visibilityInfo.startParent = null;
    } 
    if (paramTransitionValues2 != null) {
      visibilityInfo.endVisibility = ((Integer)paramTransitionValues2.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.endParent = (ViewGroup)paramTransitionValues2.values.get("android:visibility:parent");
    } else {
      visibilityInfo.endVisibility = -1;
      visibilityInfo.endParent = null;
    } 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null) {
      if (visibilityInfo.startVisibility == visibilityInfo.endVisibility && visibilityInfo.startParent == visibilityInfo.endParent)
        return visibilityInfo; 
      if (visibilityInfo.startVisibility != visibilityInfo.endVisibility) {
        if (visibilityInfo.startVisibility == 0) {
          visibilityInfo.fadeIn = false;
          visibilityInfo.visibilityChange = true;
        } else if (visibilityInfo.endVisibility == 0) {
          visibilityInfo.fadeIn = true;
          visibilityInfo.visibilityChange = true;
        } 
      } else if (visibilityInfo.startParent != visibilityInfo.endParent) {
        if (visibilityInfo.endParent == null) {
          visibilityInfo.fadeIn = false;
          visibilityInfo.visibilityChange = true;
        } else if (visibilityInfo.startParent == null) {
          visibilityInfo.fadeIn = true;
          visibilityInfo.visibilityChange = true;
        } 
      } 
    } 
    if (paramTransitionValues1 == null) {
      visibilityInfo.fadeIn = true;
      visibilityInfo.visibilityChange = true;
      return visibilityInfo;
    } 
    if (paramTransitionValues2 == null) {
      visibilityInfo.fadeIn = false;
      visibilityInfo.visibilityChange = true;
    } 
    return visibilityInfo;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    int i = -1;
    View view1 = null;
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    View view2 = view1;
    if (visibilityInfo.visibilityChange) {
      int j = 0;
      if (this.mTargets.size() > 0 || this.mTargetIds.size() > 0) {
        View view;
        if (paramTransitionValues1 != null) {
          view2 = paramTransitionValues1.view;
        } else {
          view2 = null;
        } 
        if (paramTransitionValues2 != null) {
          view = paramTransitionValues2.view;
        } else {
          view = null;
        } 
        if (view2 != null) {
          j = view2.getId();
        } else {
          j = -1;
        } 
        if (view != null)
          i = view.getId(); 
        if (isValidTarget(view2, j) || isValidTarget(view, i)) {
          j = 1;
        } else {
          j = 0;
        } 
      } 
      if (j == 0 && visibilityInfo.startParent == null) {
        view2 = view1;
        return (Animator)((visibilityInfo.endParent != null) ? (visibilityInfo.fadeIn ? onAppear(paramViewGroup, paramTransitionValues1, visibilityInfo.startVisibility, paramTransitionValues2, visibilityInfo.endVisibility) : onDisappear(paramViewGroup, paramTransitionValues1, visibilityInfo.startVisibility, paramTransitionValues2, visibilityInfo.endVisibility)) : view2);
      } 
    } else {
      return (Animator)view2;
    } 
    return visibilityInfo.fadeIn ? onAppear(paramViewGroup, paramTransitionValues1, visibilityInfo.startVisibility, paramTransitionValues2, visibilityInfo.endVisibility) : onDisappear(paramViewGroup, paramTransitionValues1, visibilityInfo.startVisibility, paramTransitionValues2, visibilityInfo.endVisibility);
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    boolean bool = false;
    if (paramTransitionValues != null) {
      int i = ((Integer)paramTransitionValues.values.get("android:visibility:visibility")).intValue();
      View view = (View)paramTransitionValues.values.get("android:visibility:parent");
      if (i == 0 && view != null)
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return null;
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return null;
  }
  
  private static class VisibilityInfo {
    ViewGroup endParent;
    
    int endVisibility;
    
    boolean fadeIn;
    
    ViewGroup startParent;
    
    int startVisibility;
    
    boolean visibilityChange;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/VisibilityPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */