package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerIcs extends TransitionManagerImpl {
  private final TransitionManagerPort mTransitionManager = new TransitionManagerPort();
  
  public void setTransition(SceneImpl paramSceneImpl1, SceneImpl paramSceneImpl2, TransitionImpl paramTransitionImpl) {
    TransitionPort transitionPort;
    TransitionManagerPort transitionManagerPort = this.mTransitionManager;
    ScenePort scenePort2 = ((SceneIcs)paramSceneImpl1).mScene;
    ScenePort scenePort1 = ((SceneIcs)paramSceneImpl2).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl1 = null;
    } else {
      transitionPort = ((TransitionIcs)paramTransitionImpl).mTransition;
    } 
    transitionManagerPort.setTransition(scenePort2, scenePort1, transitionPort);
  }
  
  public void setTransition(SceneImpl paramSceneImpl, TransitionImpl paramTransitionImpl) {
    TransitionPort transitionPort;
    TransitionManagerPort transitionManagerPort = this.mTransitionManager;
    ScenePort scenePort = ((SceneIcs)paramSceneImpl).mScene;
    if (paramTransitionImpl == null) {
      paramSceneImpl = null;
    } else {
      transitionPort = ((TransitionIcs)paramTransitionImpl).mTransition;
    } 
    transitionManagerPort.setTransition(scenePort, transitionPort);
  }
  
  public void transitionTo(SceneImpl paramSceneImpl) {
    this.mTransitionManager.transitionTo(((SceneIcs)paramSceneImpl).mScene);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */