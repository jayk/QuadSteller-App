package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class AutoTransitionPort extends TransitionSetPort {
  public AutoTransitionPort() {
    setOrdering(1);
    addTransition(new FadePort(2)).addTransition(new ChangeBoundsPort()).addTransition(new FadePort(1));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/AutoTransitionPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */