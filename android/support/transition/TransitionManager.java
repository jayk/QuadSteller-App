package android.support.transition;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

public class TransitionManager {
  private static TransitionManagerStaticsImpl sImpl = new TransitionManagerStaticsKitKat();
  
  private TransitionManagerImpl mImpl;
  
  public TransitionManager() {
    if (Build.VERSION.SDK_INT < 19) {
      this.mImpl = new TransitionManagerIcs();
      return;
    } 
    this.mImpl = new TransitionManagerKitKat();
  }
  
  public static void beginDelayedTransition(@NonNull ViewGroup paramViewGroup) {
    sImpl.beginDelayedTransition(paramViewGroup);
  }
  
  public static void beginDelayedTransition(@NonNull ViewGroup paramViewGroup, @Nullable Transition paramTransition) {
    TransitionImpl transitionImpl;
    TransitionManagerStaticsImpl transitionManagerStaticsImpl = sImpl;
    if (paramTransition == null) {
      paramTransition = null;
    } else {
      transitionImpl = paramTransition.mImpl;
    } 
    transitionManagerStaticsImpl.beginDelayedTransition(paramViewGroup, transitionImpl);
  }
  
  public static void go(@NonNull Scene paramScene) {
    sImpl.go(paramScene.mImpl);
  }
  
  public static void go(@NonNull Scene paramScene, @Nullable Transition paramTransition) {
    TransitionImpl transitionImpl;
    TransitionManagerStaticsImpl transitionManagerStaticsImpl = sImpl;
    SceneImpl sceneImpl = paramScene.mImpl;
    if (paramTransition == null) {
      paramScene = null;
    } else {
      transitionImpl = paramTransition.mImpl;
    } 
    transitionManagerStaticsImpl.go(sceneImpl, transitionImpl);
  }
  
  public void setTransition(@NonNull Scene paramScene1, @NonNull Scene paramScene2, @Nullable Transition paramTransition) {
    TransitionImpl transitionImpl;
    TransitionManagerImpl transitionManagerImpl = this.mImpl;
    SceneImpl sceneImpl2 = paramScene1.mImpl;
    SceneImpl sceneImpl1 = paramScene2.mImpl;
    if (paramTransition == null) {
      paramScene1 = null;
    } else {
      transitionImpl = paramTransition.mImpl;
    } 
    transitionManagerImpl.setTransition(sceneImpl2, sceneImpl1, transitionImpl);
  }
  
  public void setTransition(@NonNull Scene paramScene, @Nullable Transition paramTransition) {
    TransitionImpl transitionImpl;
    TransitionManagerImpl transitionManagerImpl = this.mImpl;
    SceneImpl sceneImpl = paramScene.mImpl;
    if (paramTransition == null) {
      paramScene = null;
    } else {
      transitionImpl = paramTransition.mImpl;
    } 
    transitionManagerImpl.setTransition(sceneImpl, transitionImpl);
  }
  
  public void transitionTo(@NonNull Scene paramScene) {
    this.mImpl.transitionTo(paramScene.mImpl);
  }
  
  static {
    if (Build.VERSION.SDK_INT < 19) {
      sImpl = new TransitionManagerStaticsIcs();
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */