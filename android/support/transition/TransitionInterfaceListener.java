package android.support.transition;

interface TransitionInterfaceListener<TransitionT extends TransitionInterface> {
  void onTransitionCancel(TransitionT paramTransitionT);
  
  void onTransitionEnd(TransitionT paramTransitionT);
  
  void onTransitionPause(TransitionT paramTransitionT);
  
  void onTransitionResume(TransitionT paramTransitionT);
  
  void onTransitionStart(TransitionT paramTransitionT);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionInterfaceListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */