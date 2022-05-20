package android.support.transition;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
@RequiresApi(14)
class TransitionSetPort extends TransitionPort {
  public static final int ORDERING_SEQUENTIAL = 1;
  
  public static final int ORDERING_TOGETHER = 0;
  
  int mCurrentListeners;
  
  private boolean mPlayTogether = true;
  
  boolean mStarted = false;
  
  ArrayList<TransitionPort> mTransitions = new ArrayList<TransitionPort>();
  
  private void setupStartEndListeners() {
    TransitionSetListener transitionSetListener = new TransitionSetListener(this);
    Iterator<TransitionPort> iterator = this.mTransitions.iterator();
    while (iterator.hasNext())
      ((TransitionPort)iterator.next()).addListener(transitionSetListener); 
    this.mCurrentListeners = this.mTransitions.size();
  }
  
  public TransitionSetPort addListener(TransitionPort.TransitionListener paramTransitionListener) {
    return (TransitionSetPort)super.addListener(paramTransitionListener);
  }
  
  public TransitionSetPort addTarget(int paramInt) {
    return (TransitionSetPort)super.addTarget(paramInt);
  }
  
  public TransitionSetPort addTarget(View paramView) {
    return (TransitionSetPort)super.addTarget(paramView);
  }
  
  public TransitionSetPort addTransition(TransitionPort paramTransitionPort) {
    if (paramTransitionPort != null) {
      this.mTransitions.add(paramTransitionPort);
      paramTransitionPort.mParent = this;
      if (this.mDuration >= 0L)
        paramTransitionPort.setDuration(this.mDuration); 
    } 
    return this;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void cancel() {
    super.cancel();
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((TransitionPort)this.mTransitions.get(b)).cancel(); 
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getId();
    if (isValidTarget(paramTransitionValues.view, i))
      for (TransitionPort transitionPort : this.mTransitions) {
        if (transitionPort.isValidTarget(paramTransitionValues.view, i))
          transitionPort.captureEndValues(paramTransitionValues); 
      }  
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getId();
    if (isValidTarget(paramTransitionValues.view, i))
      for (TransitionPort transitionPort : this.mTransitions) {
        if (transitionPort.isValidTarget(paramTransitionValues.view, i))
          transitionPort.captureStartValues(paramTransitionValues); 
      }  
  }
  
  public TransitionSetPort clone() {
    TransitionSetPort transitionSetPort = (TransitionSetPort)super.clone();
    transitionSetPort.mTransitions = new ArrayList<TransitionPort>();
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      transitionSetPort.addTransition(((TransitionPort)this.mTransitions.get(b)).clone()); 
    return transitionSetPort;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2) {
    Iterator<TransitionPort> iterator = this.mTransitions.iterator();
    while (iterator.hasNext())
      ((TransitionPort)iterator.next()).createAnimators(paramViewGroup, paramTransitionValuesMaps1, paramTransitionValuesMaps2); 
  }
  
  public int getOrdering() {
    return this.mPlayTogether ? 0 : 1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void pause(View paramView) {
    super.pause(paramView);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((TransitionPort)this.mTransitions.get(b)).pause(paramView); 
  }
  
  public TransitionSetPort removeListener(TransitionPort.TransitionListener paramTransitionListener) {
    return (TransitionSetPort)super.removeListener(paramTransitionListener);
  }
  
  public TransitionSetPort removeTarget(int paramInt) {
    return (TransitionSetPort)super.removeTarget(paramInt);
  }
  
  public TransitionSetPort removeTarget(View paramView) {
    return (TransitionSetPort)super.removeTarget(paramView);
  }
  
  public TransitionSetPort removeTransition(TransitionPort paramTransitionPort) {
    this.mTransitions.remove(paramTransitionPort);
    paramTransitionPort.mParent = null;
    return this;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void resume(View paramView) {
    super.resume(paramView);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((TransitionPort)this.mTransitions.get(b)).resume(paramView); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void runAnimators() {
    if (this.mTransitions.isEmpty()) {
      start();
      end();
      return;
    } 
    setupStartEndListeners();
    if (!this.mPlayTogether) {
      for (byte b = 1; b < this.mTransitions.size(); b++) {
        ((TransitionPort)this.mTransitions.get(b - 1)).addListener(new TransitionPort.TransitionListenerAdapter() {
              public void onTransitionEnd(TransitionPort param1TransitionPort) {
                nextTransition.runAnimators();
                param1TransitionPort.removeListener(this);
              }
            });
      } 
      TransitionPort transitionPort = this.mTransitions.get(0);
      if (transitionPort != null)
        transitionPort.runAnimators(); 
      return;
    } 
    Iterator<TransitionPort> iterator = this.mTransitions.iterator();
    while (true) {
      if (iterator.hasNext()) {
        ((TransitionPort)iterator.next()).runAnimators();
        continue;
      } 
      return;
    } 
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    super.setCanRemoveViews(paramBoolean);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((TransitionPort)this.mTransitions.get(b)).setCanRemoveViews(paramBoolean); 
  }
  
  public TransitionSetPort setDuration(long paramLong) {
    super.setDuration(paramLong);
    if (this.mDuration >= 0L) {
      int i = this.mTransitions.size();
      for (byte b = 0; b < i; b++)
        ((TransitionPort)this.mTransitions.get(b)).setDuration(paramLong); 
    } 
    return this;
  }
  
  public TransitionSetPort setInterpolator(TimeInterpolator paramTimeInterpolator) {
    return (TransitionSetPort)super.setInterpolator(paramTimeInterpolator);
  }
  
  public TransitionSetPort setOrdering(int paramInt) {
    switch (paramInt) {
      default:
        throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + paramInt);
      case 1:
        this.mPlayTogether = false;
        return this;
      case 0:
        break;
    } 
    this.mPlayTogether = true;
    return this;
  }
  
  TransitionSetPort setSceneRoot(ViewGroup paramViewGroup) {
    super.setSceneRoot(paramViewGroup);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((TransitionPort)this.mTransitions.get(b)).setSceneRoot(paramViewGroup); 
    return this;
  }
  
  public TransitionSetPort setStartDelay(long paramLong) {
    return (TransitionSetPort)super.setStartDelay(paramLong);
  }
  
  String toString(String paramString) {
    String str = super.toString(paramString);
    for (byte b = 0; b < this.mTransitions.size(); b++)
      str = str + "\n" + ((TransitionPort)this.mTransitions.get(b)).toString(paramString + "  "); 
    return str;
  }
  
  static class TransitionSetListener extends TransitionPort.TransitionListenerAdapter {
    TransitionSetPort mTransitionSet;
    
    TransitionSetListener(TransitionSetPort param1TransitionSetPort) {
      this.mTransitionSet = param1TransitionSetPort;
    }
    
    public void onTransitionEnd(TransitionPort param1TransitionPort) {
      TransitionSetPort transitionSetPort = this.mTransitionSet;
      transitionSetPort.mCurrentListeners--;
      if (this.mTransitionSet.mCurrentListeners == 0) {
        this.mTransitionSet.mStarted = false;
        this.mTransitionSet.end();
      } 
      param1TransitionPort.removeListener(this);
    }
    
    public void onTransitionStart(TransitionPort param1TransitionPort) {
      if (!this.mTransitionSet.mStarted) {
        this.mTransitionSet.start();
        this.mTransitionSet.mStarted = true;
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionSetPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */