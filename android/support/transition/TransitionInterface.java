package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;

interface TransitionInterface {
  void captureEndValues(TransitionValues paramTransitionValues);
  
  void captureStartValues(TransitionValues paramTransitionValues);
  
  Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */