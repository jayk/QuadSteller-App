package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.transition.Scene;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class SceneStaticsKitKat extends SceneStaticsImpl {
  public SceneImpl getSceneForLayout(ViewGroup paramViewGroup, int paramInt, Context paramContext) {
    SceneKitKat sceneKitKat = new SceneKitKat();
    sceneKitKat.mScene = Scene.getSceneForLayout(paramViewGroup, paramInt, paramContext);
    return sceneKitKat;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/SceneStaticsKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */