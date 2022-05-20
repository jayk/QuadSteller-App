package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;

@TargetApi(19)
@RequiresApi(19)
class TransitionManagerKitKat extends TransitionManagerImpl {
  private final TransitionManager mTransitionManager = new TransitionManager();
  
  public void setTransition(SceneImpl paramSceneImpl1, SceneImpl paramSceneImpl2, TransitionImpl paramTransitionImpl) {
    Transition transition;
    TransitionManager transitionManager = this.mTransitionManager;
    Scene scene2 = ((SceneWrapper)paramSceneImpl1).mScene;
    Scene scene1 = ((SceneWrapper)paramSceneImpl2).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl1 = null;
    } else {
      transition = ((TransitionKitKat)paramTransitionImpl).mTransition;
    } 
    transitionManager.setTransition(scene2, scene1, transition);
  }
  
  public void setTransition(SceneImpl paramSceneImpl, TransitionImpl paramTransitionImpl) {
    Transition transition;
    TransitionManager transitionManager = this.mTransitionManager;
    Scene scene = ((SceneWrapper)paramSceneImpl).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl = null;
    } else {
      transition = ((TransitionKitKat)paramTransitionImpl).mTransition;
    } 
    transitionManager.setTransition(scene, transition);
  }
  
  public void transitionTo(SceneImpl paramSceneImpl) {
    this.mTransitionManager.transitionTo(((SceneWrapper)paramSceneImpl).mScene);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */