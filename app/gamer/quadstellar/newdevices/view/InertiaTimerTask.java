package app.gamer.quadstellar.newdevices.view;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {
  float a;
  
  final LoopView loopView;
  
  final float velocityY;
  
  InertiaTimerTask(LoopView paramLoopView, float paramFloat) {
    this.loopView = paramLoopView;
    this.velocityY = paramFloat;
    this.a = 2.14748365E9F;
  }
  
  public final void run() {
    if (this.a == 2.14748365E9F)
      if (Math.abs(this.velocityY) > 2000.0F) {
        if (this.velocityY > 0.0F) {
          this.a = 2000.0F;
        } else {
          this.a = -2000.0F;
        } 
      } else {
        this.a = this.velocityY;
      }  
    if (Math.abs(this.a) >= 0.0F && Math.abs(this.a) <= 20.0F) {
      this.loopView.cancelFuture();
      this.loopView.handler.sendEmptyMessage(2000);
      return;
    } 
    int i = (int)(this.a * 10.0F / 1000.0F);
    LoopView loopView = this.loopView;
    loopView.totalScrollY -= i;
    if (!this.loopView.isLoop) {
      float f = this.loopView.lineSpacingMultiplier * this.loopView.maxTextHeight;
      if (this.loopView.totalScrollY <= (int)(-this.loopView.initPosition * f)) {
        this.a = 40.0F;
        this.loopView.totalScrollY = (int)(-this.loopView.initPosition * f);
      } else if (this.loopView.totalScrollY >= (int)((this.loopView.items.size() - 1 - this.loopView.initPosition) * f)) {
        this.loopView.totalScrollY = (int)((this.loopView.items.size() - 1 - this.loopView.initPosition) * f);
        this.a = -40.0F;
      } 
    } 
    if (this.a < 0.0F) {
      this.a += 20.0F;
    } else {
      this.a -= 20.0F;
    } 
    this.loopView.handler.sendEmptyMessage(1000);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/InertiaTimerTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */