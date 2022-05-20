package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class TransitionApi23 extends TransitionKitKat {
  public TransitionImpl removeTarget(int paramInt) {
    this.mTransition.removeTarget(paramInt);
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionApi23.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */