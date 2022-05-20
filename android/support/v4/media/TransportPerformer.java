package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;

public abstract class TransportPerformer {
  static final int AUDIOFOCUS_GAIN = 1;
  
  static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
  
  static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
  
  static final int AUDIOFOCUS_LOSS = -1;
  
  static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
  
  static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
  
  public void onAudioFocusChange(int paramInt) {
    boolean bool = false;
    switch (paramInt) {
      default:
        paramInt = bool;
        if (paramInt != 0) {
          long l = SystemClock.uptimeMillis();
          onMediaButtonDown(paramInt, new KeyEvent(l, l, 0, paramInt, 0));
          onMediaButtonUp(paramInt, new KeyEvent(l, l, 1, paramInt, 0));
        } 
        return;
      case -1:
        break;
    } 
    paramInt = 127;
    if (paramInt != 0) {
      long l = SystemClock.uptimeMillis();
      onMediaButtonDown(paramInt, new KeyEvent(l, l, 0, paramInt, 0));
      onMediaButtonUp(paramInt, new KeyEvent(l, l, 1, paramInt, 0));
    } 
  }
  
  public int onGetBufferPercentage() {
    return 100;
  }
  
  public abstract long onGetCurrentPosition();
  
  public abstract long onGetDuration();
  
  public int onGetTransportControlFlags() {
    return 60;
  }
  
  public abstract boolean onIsPlaying();
  
  public boolean onMediaButtonDown(int paramInt, KeyEvent paramKeyEvent) {
    switch (paramInt) {
      default:
        return true;
      case 126:
        onStart();
      case 127:
        onPause();
      case 86:
        onStop();
      case 79:
      case 85:
        break;
    } 
    if (onIsPlaying())
      onPause(); 
    onStart();
  }
  
  public boolean onMediaButtonUp(int paramInt, KeyEvent paramKeyEvent) {
    return true;
  }
  
  public abstract void onPause();
  
  public abstract void onSeekTo(long paramLong);
  
  public abstract void onStart();
  
  public abstract void onStop();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/TransportPerformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */