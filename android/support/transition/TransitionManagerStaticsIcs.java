package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerStaticsIcs extends TransitionManagerStaticsImpl {
  public void beginDelayedTransition(ViewGroup paramViewGroup) {
    TransitionManagerPort.beginDelayedTransition(paramViewGroup);
  }
  
  public void beginDelayedTransition(ViewGroup paramViewGroup, TransitionImpl paramTransitionImpl) {
    TransitionPort transitionPort;
    if (paramTransitionImpl == null) {
      paramTransitionImpl = null;
    } else {
      transitionPort = ((TransitionIcs)paramTransitionImpl).mTransition;
    } 
    TransitionManagerPort.beginDelayedTransition(paramViewGroup, transitionPort);
  }
  
  public void go(SceneImpl paramSceneImpl) {
    TransitionManagerPort.go(((SceneIcs)paramSceneImpl).mScene);
  }
  
  public void go(SceneImpl paramSceneImpl, TransitionImpl paramTransitionImpl) {
    TransitionPort transitionPort;
    ScenePort scenePort = ((SceneIcs)paramSceneImpl).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl = null;
    } else {
      transitionPort = ((TransitionIcs)paramTransitionImpl).mTransition;
    } 
    TransitionManagerPort.go(scenePort, transitionPort);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerStaticsIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */