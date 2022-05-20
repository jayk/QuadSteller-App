package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class TransitionIcs extends TransitionImpl {
  private CompatListener mCompatListener;
  
  TransitionInterface mExternalTransition;
  
  TransitionPort mTransition;
  
  public TransitionImpl addListener(TransitionInterfaceListener paramTransitionInterfaceListener) {
    if (this.mCompatListener == null) {
      this.mCompatListener = new CompatListener();
      this.mTransition.addListener(this.mCompatListener);
    } 
    this.mCompatListener.addListener(paramTransitionInterfaceListener);
    return this;
  }
  
  public TransitionImpl addTarget(int paramInt) {
    this.mTransition.addTarget(paramInt);
    return this;
  }
  
  public TransitionImpl addTarget(View paramView) {
    this.mTransition.addTarget(paramView);
    return this;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    this.mTransition.captureEndValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    this.mTransition.captureStartValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return this.mTransition.createAnimator(paramViewGroup, paramTransitionValues1, paramTransitionValues2);
  }
  
  public TransitionImpl excludeChildren(int paramInt, boolean paramBoolean) {
    this.mTransition.excludeChildren(paramInt, paramBoolean);
    return this;
  }
  
  public TransitionImpl excludeChildren(View paramView, boolean paramBoolean) {
    this.mTransition.excludeChildren(paramView, paramBoolean);
    return this;
  }
  
  public TransitionImpl excludeChildren(Class paramClass, boolean paramBoolean) {
    this.mTransition.excludeChildren(paramClass, paramBoolean);
    return this;
  }
  
  public TransitionImpl excludeTarget(int paramInt, boolean paramBoolean) {
    this.mTransition.excludeTarget(paramInt, paramBoolean);
    return this;
  }
  
  public TransitionImpl excludeTarget(View paramView, boolean paramBoolean) {
    this.mTransition.excludeTarget(paramView, paramBoolean);
    return this;
  }
  
  public TransitionImpl excludeTarget(Class paramClass, boolean paramBoolean) {
    this.mTransition.excludeTarget(paramClass, paramBoolean);
    return this;
  }
  
  public long getDuration() {
    return this.mTransition.getDuration();
  }
  
  public TimeInterpolator getInterpolator() {
    return this.mTransition.getInterpolator();
  }
  
  public String getName() {
    return this.mTransition.getName();
  }
  
  public long getStartDelay() {
    return this.mTransition.getStartDelay();
  }
  
  public List<Integer> getTargetIds() {
    return this.mTransition.getTargetIds();
  }
  
  public List<View> getTargets() {
    return this.mTransition.getTargets();
  }
  
  public String[] getTransitionProperties() {
    return this.mTransition.getTransitionProperties();
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean) {
    return this.mTransition.getTransitionValues(paramView, paramBoolean);
  }
  
  public void init(TransitionInterface paramTransitionInterface, Object paramObject) {
    this.mExternalTransition = paramTransitionInterface;
    if (paramObject == null) {
      this.mTransition = new TransitionWrapper(paramTransitionInterface);
      return;
    } 
    this.mTransition = (TransitionPort)paramObject;
  }
  
  public TransitionImpl removeListener(TransitionInterfaceListener paramTransitionInterfaceListener) {
    if (this.mCompatListener != null) {
      this.mCompatListener.removeListener(paramTransitionInterfaceListener);
      if (this.mCompatListener.isEmpty()) {
        this.mTransition.removeListener(this.mCompatListener);
        this.mCompatListener = null;
      } 
    } 
    return this;
  }
  
  public TransitionImpl removeTarget(int paramInt) {
    this.mTransition.removeTarget(paramInt);
    return this;
  }
  
  public TransitionImpl removeTarget(View paramView) {
    this.mTransition.removeTarget(paramView);
    return this;
  }
  
  public TransitionImpl setDuration(long paramLong) {
    this.mTransition.setDuration(paramLong);
    return this;
  }
  
  public TransitionImpl setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mTransition.setInterpolator(paramTimeInterpolator);
    return this;
  }
  
  public TransitionImpl setStartDelay(long paramLong) {
    this.mTransition.setStartDelay(paramLong);
    return this;
  }
  
  public String toString() {
    return this.mTransition.toString();
  }
  
  private class CompatListener implements TransitionPort.TransitionListener {
    private final ArrayList<TransitionInterfaceListener> mListeners = new ArrayList<TransitionInterfaceListener>();
    
    public void addListener(TransitionInterfaceListener param1TransitionInterfaceListener) {
      this.mListeners.add(param1TransitionInterfaceListener);
    }
    
    public boolean isEmpty() {
      return this.mListeners.isEmpty();
    }
    
    public void onTransitionCancel(TransitionPort param1TransitionPort) {
      Iterator<TransitionInterfaceListener> iterator = this.mListeners.iterator();
      while (iterator.hasNext())
        ((TransitionInterfaceListener<TransitionInterface>)iterator.next()).onTransitionCancel(TransitionIcs.this.mExternalTransition); 
    }
    
    public void onTransitionEnd(TransitionPort param1TransitionPort) {
      Iterator<TransitionInterfaceListener> iterator = this.mListeners.iterator();
      while (iterator.hasNext())
        ((TransitionInterfaceListener<TransitionInterface>)iterator.next()).onTransitionEnd(TransitionIcs.this.mExternalTransition); 
    }
    
    public void onTransitionPause(TransitionPort param1TransitionPort) {
      Iterator<TransitionInterfaceListener> iterator = this.mListeners.iterator();
      while (iterator.hasNext())
        ((TransitionInterfaceListener<TransitionInterface>)iterator.next()).onTransitionPause(TransitionIcs.this.mExternalTransition); 
    }
    
    public void onTransitionResume(TransitionPort param1TransitionPort) {
      Iterator<TransitionInterfaceListener> iterator = this.mListeners.iterator();
      while (iterator.hasNext())
        ((TransitionInterfaceListener<TransitionInterface>)iterator.next()).onTransitionResume(TransitionIcs.this.mExternalTransition); 
    }
    
    public void onTransitionStart(TransitionPort param1TransitionPort) {
      Iterator<TransitionInterfaceListener> iterator = this.mListeners.iterator();
      while (iterator.hasNext())
        ((TransitionInterfaceListener<TransitionInterface>)iterator.next()).onTransitionStart(TransitionIcs.this.mExternalTransition); 
    }
    
    public void removeListener(TransitionInterfaceListener param1TransitionInterfaceListener) {
      this.mListeners.remove(param1TransitionInterfaceListener);
    }
  }
  
  private static class TransitionWrapper extends TransitionPort {
    private TransitionInterface mTransition;
    
    public TransitionWrapper(TransitionInterface param1TransitionInterface) {
      this.mTransition = param1TransitionInterface;
    }
    
    public void captureEndValues(TransitionValues param1TransitionValues) {
      this.mTransition.captureEndValues(param1TransitionValues);
    }
    
    public void captureStartValues(TransitionValues param1TransitionValues) {
      this.mTransition.captureStartValues(param1TransitionValues);
    }
    
    public Animator createAnimator(ViewGroup param1ViewGroup, TransitionValues param1TransitionValues1, TransitionValues param1TransitionValues2) {
      return this.mTransition.createAnimator(param1ViewGroup, param1TransitionValues1, param1TransitionValues2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */