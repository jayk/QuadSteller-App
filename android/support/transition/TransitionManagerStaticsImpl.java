package android.support.transition;

import android.view.ViewGroup;

abstract class TransitionManagerStaticsImpl {
  public abstract void beginDelayedTransition(ViewGroup paramViewGroup);
  
  public abstract void beginDelayedTransition(ViewGroup paramViewGroup, TransitionImpl paramTransitionImpl);
  
  public abstract void go(SceneImpl paramSceneImpl);
  
  public abstract void go(SceneImpl paramSceneImpl, TransitionImpl paramTransitionImpl);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerStaticsImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */