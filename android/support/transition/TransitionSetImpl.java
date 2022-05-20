package android.support.transition;

interface TransitionSetImpl {
  TransitionSetImpl addTransition(TransitionImpl paramTransitionImpl);
  
  int getOrdering();
  
  TransitionSetImpl removeTransition(TransitionImpl paramTransitionImpl);
  
  TransitionSetImpl setOrdering(int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */