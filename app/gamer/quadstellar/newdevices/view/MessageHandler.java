package app.gamer.quadstellar.newdevices.view;

import android.os.Handler;
import android.os.Message;

final class MessageHandler extends Handler {
  public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
  
  public static final int WHAT_ITEM_SELECTED = 3000;
  
  public static final int WHAT_SMOOTH_SCROLL = 2000;
  
  final LoopView loopview;
  
  MessageHandler(LoopView paramLoopView) {
    this.loopview = paramLoopView;
  }
  
  public final void handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        return;
      case 1000:
        this.loopview.invalidate();
      case 2000:
        this.loopview.smoothScroll(LoopView.ACTION.FLING);
      case 3000:
        break;
    } 
    this.loopview.onItemSelected();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/MessageHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */