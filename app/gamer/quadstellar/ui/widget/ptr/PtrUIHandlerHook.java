package app.gamer.quadstellar.ui.widget.ptr;

public abstract class PtrUIHandlerHook implements Runnable {
  private static final byte STATUS_IN_HOOK = 1;
  
  private static final byte STATUS_PREPARE = 0;
  
  private static final byte STATUS_RESUMED = 2;
  
  private Runnable mResumeAction;
  
  private byte mStatus = (byte)0;
  
  public void reset() {
    this.mStatus = (byte)0;
  }
  
  public void resume() {
    if (this.mResumeAction != null)
      this.mResumeAction.run(); 
    this.mStatus = (byte)2;
  }
  
  public void setResumeAction(Runnable paramRunnable) {
    this.mResumeAction = paramRunnable;
  }
  
  public void takeOver() {
    takeOver(null);
  }
  
  public void takeOver(Runnable paramRunnable) {
    if (paramRunnable != null)
      this.mResumeAction = paramRunnable; 
    switch (this.mStatus) {
      default:
        return;
      case 0:
        this.mStatus = (byte)1;
        run();
      case 2:
        break;
    } 
    resume();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrUIHandlerHook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */