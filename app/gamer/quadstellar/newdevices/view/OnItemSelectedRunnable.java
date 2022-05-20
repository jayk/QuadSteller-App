package app.gamer.quadstellar.newdevices.view;

final class OnItemSelectedRunnable implements Runnable {
  final LoopView loopView;
  
  OnItemSelectedRunnable(LoopView paramLoopView) {
    this.loopView = paramLoopView;
  }
  
  public final void run() {
    this.loopView.onItemSelectedListener.onItemSelected(this.loopView.getSelectedItem());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/OnItemSelectedRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */