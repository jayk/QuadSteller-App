package app.gamer.quadstellar.newdevices.view;

import android.view.GestureDetector;
import android.view.MotionEvent;

final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
  final LoopView loopView;
  
  LoopViewGestureListener(LoopView paramLoopView) {
    this.loopView = paramLoopView;
  }
  
  public final boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
    this.loopView.scrollBy(paramFloat2);
    return true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/LoopViewGestureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */