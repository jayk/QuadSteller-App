package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class TransitionManagerStaticsKitKat extends TransitionManagerStaticsImpl {
  public void beginDelayedTransition(ViewGroup paramViewGroup) {
    TransitionManager.beginDelayedTransition(paramViewGroup);
  }
  
  public void beginDelayedTransition(ViewGroup paramViewGroup, TransitionImpl paramTransitionImpl) {
    Transition transition;
    if (paramTransitionImpl == null) {
      paramTransitionImpl = null;
    } else {
      transition = ((TransitionKitKat)paramTransitionImpl).mTransition;
    } 
    TransitionManager.beginDelayedTransition(paramViewGroup, transition);
  }
  
  public void go(SceneImpl paramSceneImpl) {
    TransitionManager.go(((SceneWrapper)paramSceneImpl).mScene);
  }
  
  public void go(SceneImpl paramSceneImpl, TransitionImpl paramTransitionImpl) {
    Transition transition;
    Scene scene = ((SceneWrapper)paramSceneImpl).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl = null;
    } else {
      transition = ((TransitionKitKat)paramTransitionImpl).mTransition;
    } 
    TransitionManager.go(scene, transition);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerStaticsKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */