package android.support.design.widget;

import android.util.StateSet;
import java.util.ArrayList;

final class StateListAnimator {
  private final ValueAnimatorCompat.AnimatorListener mAnimationListener = new ValueAnimatorCompat.AnimatorListenerAdapter() {
      public void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat) {
        if (StateListAnimator.this.mRunningAnimator == param1ValueAnimatorCompat)
          StateListAnimator.this.mRunningAnimator = null; 
      }
    };
  
  private Tuple mLastMatch = null;
  
  ValueAnimatorCompat mRunningAnimator = null;
  
  private final ArrayList<Tuple> mTuples = new ArrayList<Tuple>();
  
  private void cancel() {
    if (this.mRunningAnimator != null) {
      this.mRunningAnimator.cancel();
      this.mRunningAnimator = null;
    } 
  }
  
  private void start(Tuple paramTuple) {
    this.mRunningAnimator = paramTuple.mAnimator;
    this.mRunningAnimator.start();
  }
  
  public void addState(int[] paramArrayOfint, ValueAnimatorCompat paramValueAnimatorCompat) {
    Tuple tuple = new Tuple(paramArrayOfint, paramValueAnimatorCompat);
    paramValueAnimatorCompat.addListener(this.mAnimationListener);
    this.mTuples.add(tuple);
  }
  
  public void jumpToCurrentState() {
    if (this.mRunningAnimator != null) {
      this.mRunningAnimator.end();
      this.mRunningAnimator = null;
    } 
  }
  
  void setState(int[] paramArrayOfint) {
    Tuple tuple = null;
    int i = this.mTuples.size();
    byte b = 0;
    while (true) {
      Tuple tuple1 = tuple;
      if (b < i) {
        tuple1 = this.mTuples.get(b);
        if (!StateSet.stateSetMatches(tuple1.mSpecs, paramArrayOfint)) {
          b++;
          continue;
        } 
      } 
      if (tuple1 != this.mLastMatch) {
        if (this.mLastMatch != null)
          cancel(); 
        this.mLastMatch = tuple1;
        if (tuple1 != null)
          start(tuple1); 
      } 
      return;
    } 
  }
  
  static class Tuple {
    final ValueAnimatorCompat mAnimator;
    
    final int[] mSpecs;
    
    Tuple(int[] param1ArrayOfint, ValueAnimatorCompat param1ValueAnimatorCompat) {
      this.mSpecs = param1ArrayOfint;
      this.mAnimator = param1ValueAnimatorCompat;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/StateListAnimator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */