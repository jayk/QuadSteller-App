package app.lib.pullToRefresh;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import java.util.HashMap;

public class SoundPullEventListener<V extends View> implements PullToRefreshBase.OnPullEventListener<V> {
  private final Context mContext;
  
  private MediaPlayer mCurrentMediaPlayer;
  
  private final HashMap<PullToRefreshBase.State, Integer> mSoundMap;
  
  public SoundPullEventListener(Context paramContext) {
    this.mContext = paramContext;
    this.mSoundMap = new HashMap<PullToRefreshBase.State, Integer>();
  }
  
  private void playSound(int paramInt) {
    if (this.mCurrentMediaPlayer != null) {
      this.mCurrentMediaPlayer.stop();
      this.mCurrentMediaPlayer.release();
    } 
    this.mCurrentMediaPlayer = MediaPlayer.create(this.mContext, paramInt);
    if (this.mCurrentMediaPlayer != null)
      this.mCurrentMediaPlayer.start(); 
  }
  
  public void addSoundEvent(PullToRefreshBase.State paramState, int paramInt) {
    this.mSoundMap.put(paramState, Integer.valueOf(paramInt));
  }
  
  public void clearSounds() {
    this.mSoundMap.clear();
  }
  
  public MediaPlayer getCurrentMediaPlayer() {
    return this.mCurrentMediaPlayer;
  }
  
  public final void onPullEvent(PullToRefreshBase<V> paramPullToRefreshBase, PullToRefreshBase.State paramState, PullToRefreshBase.Mode paramMode) {
    Integer integer = this.mSoundMap.get(paramState);
    if (integer != null)
      playSound(integer.intValue()); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/SoundPullEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */