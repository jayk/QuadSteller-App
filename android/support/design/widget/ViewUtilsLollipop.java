package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RequiresApi;
import android.support.design.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

@TargetApi(21)
@RequiresApi(21)
class ViewUtilsLollipop {
  private static final int[] STATE_LIST_ANIM_ATTRS = new int[] { 16843848 };
  
  static void setBoundsViewOutlineProvider(View paramView) {
    paramView.setOutlineProvider(ViewOutlineProvider.BOUNDS);
  }
  
  static void setDefaultAppBarLayoutStateListAnimator(View paramView, float paramFloat) {
    int i = paramView.getResources().getInteger(R.integer.app_bar_elevation_anim_duration);
    StateListAnimator stateListAnimator = new StateListAnimator();
    int j = R.attr.state_collapsible;
    int k = -R.attr.state_collapsed;
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { 0.0F }).setDuration(i);
    stateListAnimator.addState(new int[] { 16842766, j, k }, (Animator)objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { paramFloat }).setDuration(i);
    stateListAnimator.addState(new int[] { 16842766 }, (Animator)objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(paramView, "elevation", new float[] { 0.0F }).setDuration(0L);
    stateListAnimator.addState(new int[0], (Animator)objectAnimator);
    paramView.setStateListAnimator(stateListAnimator);
  }
  
  static void setStateListAnimatorFromAttrs(View paramView, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    Context context = paramView.getContext();
    TypedArray typedArray = context.obtainStyledAttributes(paramAttributeSet, STATE_LIST_ANIM_ATTRS, paramInt1, paramInt2);
    try {
      if (typedArray.hasValue(0))
        paramView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, typedArray.getResourceId(0, 0))); 
      return;
    } finally {
      typedArray.recycle();
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ViewUtilsLollipop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */