package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class VisibilityIcs extends TransitionIcs implements VisibilityImpl {
  public void init(TransitionInterface paramTransitionInterface, Object paramObject) {
    this.mExternalTransition = paramTransitionInterface;
    if (paramObject == null) {
      this.mTransition = new VisibilityWrapper((VisibilityInterface)paramTransitionInterface);
      return;
    } 
    this.mTransition = (VisibilityPort)paramObject;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    return ((VisibilityPort)this.mTransition).isVisible(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return ((VisibilityPort)this.mTransition).onAppear(paramViewGroup, paramTransitionValues1, paramInt1, paramTransitionValues2, paramInt2);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return ((VisibilityPort)this.mTransition).onDisappear(paramViewGroup, paramTransitionValues1, paramInt1, paramTransitionValues2, paramInt2);
  }
  
  private static class VisibilityWrapper extends VisibilityPort {
    private VisibilityInterface mVisibility;
    
    VisibilityWrapper(VisibilityInterface param1VisibilityInterface) {
      this.mVisibility = param1VisibilityInterface;
    }
    
    public void captureEndValues(TransitionValues param1TransitionValues) {
      this.mVisibility.captureEndValues(param1TransitionValues);
    }
    
    public void captureStartValues(TransitionValues param1TransitionValues) {
      this.mVisibility.captureStartValues(param1TransitionValues);
    }
    
    public Animator createAnimator(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, TransitionValues param1TransitionValues2) {
      return this.mVisibility.createAnimator(param1ViewGroup, param1TransitionValues1, param1TransitionValues2);
    }
    
    public boolean isVisible(TransitionValues param1TransitionValues) {
      return this.mVisibility.isVisible(param1TransitionValues);
    }
    
    public Animator onAppear(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, int param1Int1, TransitionValues param1TransitionValues2, int param1Int2) {
      return this.mVisibility.onAppear(param1ViewGroup, param1TransitionValues1, param1Int1, param1TransitionValues2, param1Int2);
    }
    
    public Animator onDisappear(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, int param1Int1, TransitionValues param1TransitionValues2, int param1Int2) {
      return this.mVisibility.onDisappear(param1ViewGroup, param1TransitionValues1, param1Int1, param1TransitionValues2, param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/VisibilityIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */