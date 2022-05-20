package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class VisibilityKitKat extends TransitionKitKat implements VisibilityImpl {
  public void init(TransitionInterface paramTransitionInterface, Object paramObject) {
    this.mExternalTransition = paramTransitionInterface;
    if (paramObject == null) {
      this.mTransition = (Transition)new VisibilityWrapper((VisibilityInterface)paramTransitionInterface);
      return;
    } 
    this.mTransition = (Transition)paramObject;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    return ((Visibility)this.mTransition).isVisible(convertToPlatform(paramTransitionValues));
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return ((Visibility)this.mTransition).onAppear(paramViewGroup, convertToPlatform(paramTransitionValues1), paramInt1, convertToPlatform(paramTransitionValues2), paramInt2);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    return ((Visibility)this.mTransition).onDisappear(paramViewGroup, convertToPlatform(paramTransitionValues1), paramInt1, convertToPlatform(paramTransitionValues2), paramInt2);
  }
  
  private static class VisibilityWrapper extends Visibility {
    private final VisibilityInterface mVisibility;
    
    VisibilityWrapper(VisibilityInterface param1VisibilityInterface) {
      this.mVisibility = param1VisibilityInterface;
    }
    
    public void captureEndValues(TransitionValues param1TransitionValues) {
      TransitionKitKat.wrapCaptureEndValues(this.mVisibility, param1TransitionValues);
    }
    
    public void captureStartValues(TransitionValues param1TransitionValues) {
      TransitionKitKat.wrapCaptureStartValues(this.mVisibility, param1TransitionValues);
    }
    
    public Animator createAnimator(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, TransitionValues param1TransitionValues2) {
      return this.mVisibility.createAnimator(param1ViewGroup, TransitionKitKat.convertToSupport(param1TransitionValues1), TransitionKitKat.convertToSupport(param1TransitionValues2));
    }
    
    public boolean isVisible(TransitionValues param1TransitionValues) {
      if (param1TransitionValues == null)
        return false; 
      TransitionValues transitionValues = new TransitionValues();
      TransitionKitKat.copyValues(param1TransitionValues, transitionValues);
      return this.mVisibility.isVisible(transitionValues);
    }
    
    public Animator onAppear(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, int param1Int1, TransitionValues param1TransitionValues2, int param1Int2) {
      return this.mVisibility.onAppear(param1ViewGroup, TransitionKitKat.convertToSupport(param1TransitionValues1), param1Int1, TransitionKitKat.convertToSupport(param1TransitionValues2), param1Int2);
    }
    
    public Animator onDisappear(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, int param1Int1, TransitionValues param1TransitionValues2, int param1Int2) {
      return this.mVisibility.onDisappear(param1ViewGroup, TransitionKitKat.convertToSupport(param1TransitionValues1), param1Int1, TransitionKitKat.convertToSupport(param1TransitionValues2), param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/VisibilityKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */