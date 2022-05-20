package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.SupportActivity;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class MediaControllerCompat {
  static final String COMMAND_GET_EXTRA_BINDER = "android.support.v4.media.session.command.GET_EXTRA_BINDER";
  
  static final String TAG = "MediaControllerCompat";
  
  private final MediaControllerImpl mImpl;
  
  private final MediaSessionCompat.Token mToken;
  
  public MediaControllerCompat(Context paramContext, MediaSessionCompat.Token paramToken) throws RemoteException {
    if (paramToken == null)
      throw new IllegalArgumentException("sessionToken must not be null"); 
    this.mToken = paramToken;
    if (Build.VERSION.SDK_INT >= 24) {
      this.mImpl = new MediaControllerImplApi24(paramContext, paramToken);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 23) {
      this.mImpl = new MediaControllerImplApi23(paramContext, paramToken);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaControllerImplApi21(paramContext, paramToken);
      return;
    } 
    this.mImpl = new MediaControllerImplBase(this.mToken);
  }
  
  public MediaControllerCompat(Context paramContext, MediaSessionCompat paramMediaSessionCompat) {
    if (paramMediaSessionCompat == null)
      throw new IllegalArgumentException("session must not be null"); 
    this.mToken = paramMediaSessionCompat.getSessionToken();
    if (Build.VERSION.SDK_INT >= 24) {
      this.mImpl = new MediaControllerImplApi24(paramContext, paramMediaSessionCompat);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 23) {
      this.mImpl = new MediaControllerImplApi23(paramContext, paramMediaSessionCompat);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaControllerImplApi21(paramContext, paramMediaSessionCompat);
      return;
    } 
    this.mImpl = new MediaControllerImplBase(this.mToken);
  }
  
  public static MediaControllerCompat getMediaController(Activity paramActivity) {
    MediaControllerExtraData mediaControllerExtraData;
    MediaControllerCompat mediaControllerCompat1 = null;
    if (paramActivity instanceof SupportActivity) {
      mediaControllerExtraData = (MediaControllerExtraData)((SupportActivity)paramActivity).getExtraData(MediaControllerExtraData.class);
      MediaControllerCompat mediaControllerCompat = mediaControllerCompat1;
      if (mediaControllerExtraData != null)
        mediaControllerCompat = mediaControllerExtraData.getMediaController(); 
      return mediaControllerCompat;
    } 
    MediaControllerCompat mediaControllerCompat2 = mediaControllerCompat1;
    if (Build.VERSION.SDK_INT >= 21) {
      Object object = MediaControllerCompatApi21.getMediaController((Activity)mediaControllerExtraData);
      mediaControllerCompat2 = mediaControllerCompat1;
      if (object != null) {
        object = MediaControllerCompatApi21.getSessionToken(object);
        try {
          mediaControllerCompat2 = new MediaControllerCompat();
          this((Context)mediaControllerExtraData, MediaSessionCompat.Token.fromToken(object));
        } catch (RemoteException remoteException) {
          Log.e("MediaControllerCompat", "Dead object in getMediaController. " + remoteException);
          mediaControllerCompat2 = mediaControllerCompat1;
        } 
      } 
    } 
    return mediaControllerCompat2;
  }
  
  public static void setMediaController(Activity paramActivity, MediaControllerCompat paramMediaControllerCompat) {
    if (paramActivity instanceof SupportActivity)
      ((SupportActivity)paramActivity).putExtraData(new MediaControllerExtraData(paramMediaControllerCompat)); 
    if (Build.VERSION.SDK_INT >= 21) {
      Object object = null;
      if (paramMediaControllerCompat != null)
        object = MediaControllerCompatApi21.fromToken((Context)paramActivity, paramMediaControllerCompat.getSessionToken().getToken()); 
      MediaControllerCompatApi21.setMediaController(paramActivity, object);
    } 
  }
  
  public void adjustVolume(int paramInt1, int paramInt2) {
    this.mImpl.adjustVolume(paramInt1, paramInt2);
  }
  
  public boolean dispatchMediaButtonEvent(KeyEvent paramKeyEvent) {
    if (paramKeyEvent == null)
      throw new IllegalArgumentException("KeyEvent may not be null"); 
    return this.mImpl.dispatchMediaButtonEvent(paramKeyEvent);
  }
  
  public Bundle getExtras() {
    return this.mImpl.getExtras();
  }
  
  public long getFlags() {
    return this.mImpl.getFlags();
  }
  
  public Object getMediaController() {
    return this.mImpl.getMediaController();
  }
  
  public MediaMetadataCompat getMetadata() {
    return this.mImpl.getMetadata();
  }
  
  public String getPackageName() {
    return this.mImpl.getPackageName();
  }
  
  public PlaybackInfo getPlaybackInfo() {
    return this.mImpl.getPlaybackInfo();
  }
  
  public PlaybackStateCompat getPlaybackState() {
    return this.mImpl.getPlaybackState();
  }
  
  public List<MediaSessionCompat.QueueItem> getQueue() {
    return this.mImpl.getQueue();
  }
  
  public CharSequence getQueueTitle() {
    return this.mImpl.getQueueTitle();
  }
  
  public int getRatingType() {
    return this.mImpl.getRatingType();
  }
  
  public PendingIntent getSessionActivity() {
    return this.mImpl.getSessionActivity();
  }
  
  public MediaSessionCompat.Token getSessionToken() {
    return this.mToken;
  }
  
  public TransportControls getTransportControls() {
    return this.mImpl.getTransportControls();
  }
  
  public void registerCallback(Callback paramCallback) {
    registerCallback(paramCallback, null);
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler) {
    if (paramCallback == null)
      throw new IllegalArgumentException("callback cannot be null"); 
    Handler handler = paramHandler;
    if (paramHandler == null)
      handler = new Handler(); 
    this.mImpl.registerCallback(paramCallback, handler);
  }
  
  public void sendCommand(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("command cannot be null or empty"); 
    this.mImpl.sendCommand(paramString, paramBundle, paramResultReceiver);
  }
  
  public void setVolumeTo(int paramInt1, int paramInt2) {
    this.mImpl.setVolumeTo(paramInt1, paramInt2);
  }
  
  public void unregisterCallback(Callback paramCallback) {
    if (paramCallback == null)
      throw new IllegalArgumentException("callback cannot be null"); 
    this.mImpl.unregisterCallback(paramCallback);
  }
  
  public static abstract class Callback implements IBinder.DeathRecipient {
    private final Object mCallbackObj;
    
    MessageHandler mHandler;
    
    boolean mHasExtraCallback;
    
    boolean mRegistered = false;
    
    public Callback() {
      if (Build.VERSION.SDK_INT >= 21) {
        this.mCallbackObj = MediaControllerCompatApi21.createCallback(new StubApi21());
        return;
      } 
      this.mCallbackObj = new StubCompat();
    }
    
    private void setHandler(Handler param1Handler) {
      this.mHandler = new MessageHandler(param1Handler.getLooper());
    }
    
    public void binderDied() {
      onSessionDestroyed();
    }
    
    public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo param1PlaybackInfo) {}
    
    public void onExtrasChanged(Bundle param1Bundle) {}
    
    public void onMetadataChanged(MediaMetadataCompat param1MediaMetadataCompat) {}
    
    public void onPlaybackStateChanged(PlaybackStateCompat param1PlaybackStateCompat) {}
    
    public void onQueueChanged(List<MediaSessionCompat.QueueItem> param1List) {}
    
    public void onQueueTitleChanged(CharSequence param1CharSequence) {}
    
    public void onSessionDestroyed() {}
    
    public void onSessionEvent(String param1String, Bundle param1Bundle) {}
    
    private class MessageHandler extends Handler {
      private static final int MSG_DESTROYED = 8;
      
      private static final int MSG_EVENT = 1;
      
      private static final int MSG_UPDATE_EXTRAS = 7;
      
      private static final int MSG_UPDATE_METADATA = 3;
      
      private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
      
      private static final int MSG_UPDATE_QUEUE = 5;
      
      private static final int MSG_UPDATE_QUEUE_TITLE = 6;
      
      private static final int MSG_UPDATE_VOLUME = 4;
      
      public MessageHandler(Looper param2Looper) {
        super(param2Looper);
      }
      
      public void handleMessage(Message param2Message) {
        if (!MediaControllerCompat.Callback.this.mRegistered);
        switch (param2Message.what) {
          default:
            return;
          case 1:
            MediaControllerCompat.Callback.this.onSessionEvent((String)param2Message.obj, param2Message.getData());
          case 2:
            MediaControllerCompat.Callback.this.onPlaybackStateChanged((PlaybackStateCompat)param2Message.obj);
          case 3:
            MediaControllerCompat.Callback.this.onMetadataChanged((MediaMetadataCompat)param2Message.obj);
          case 5:
            MediaControllerCompat.Callback.this.onQueueChanged((List<MediaSessionCompat.QueueItem>)param2Message.obj);
          case 6:
            MediaControllerCompat.Callback.this.onQueueTitleChanged((CharSequence)param2Message.obj);
          case 7:
            MediaControllerCompat.Callback.this.onExtrasChanged((Bundle)param2Message.obj);
          case 4:
            MediaControllerCompat.Callback.this.onAudioInfoChanged((MediaControllerCompat.PlaybackInfo)param2Message.obj);
          case 8:
            break;
        } 
        MediaControllerCompat.Callback.this.onSessionDestroyed();
      }
      
      public void post(int param2Int, Object param2Object, Bundle param2Bundle) {
        param2Object = obtainMessage(param2Int, param2Object);
        param2Object.setData(param2Bundle);
        param2Object.sendToTarget();
      }
    }
    
    private class StubApi21 implements MediaControllerCompatApi21.Callback {
      public void onAudioInfoChanged(int param2Int1, int param2Int2, int param2Int3, int param2Int4, int param2Int5) {
        MediaControllerCompat.Callback.this.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(param2Int1, param2Int2, param2Int3, param2Int4, param2Int5));
      }
      
      public void onExtrasChanged(Bundle param2Bundle) {
        MediaControllerCompat.Callback.this.onExtrasChanged(param2Bundle);
      }
      
      public void onMetadataChanged(Object param2Object) {
        MediaControllerCompat.Callback.this.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(param2Object));
      }
      
      public void onPlaybackStateChanged(Object param2Object) {
        if (!MediaControllerCompat.Callback.this.mHasExtraCallback || Build.VERSION.SDK_INT >= 22)
          MediaControllerCompat.Callback.this.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(param2Object)); 
      }
      
      public void onQueueChanged(List<?> param2List) {
        MediaControllerCompat.Callback.this.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(param2List));
      }
      
      public void onQueueTitleChanged(CharSequence param2CharSequence) {
        MediaControllerCompat.Callback.this.onQueueTitleChanged(param2CharSequence);
      }
      
      public void onSessionDestroyed() {
        MediaControllerCompat.Callback.this.onSessionDestroyed();
      }
      
      public void onSessionEvent(String param2String, Bundle param2Bundle) {
        if (!MediaControllerCompat.Callback.this.mHasExtraCallback || Build.VERSION.SDK_INT >= 23)
          MediaControllerCompat.Callback.this.onSessionEvent(param2String, param2Bundle); 
      }
    }
    
    private class StubCompat extends IMediaControllerCallback.Stub {
      public void onEvent(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(1, param2String, param2Bundle);
      }
      
      public void onExtrasChanged(Bundle param2Bundle) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(7, param2Bundle, (Bundle)null);
      }
      
      public void onMetadataChanged(MediaMetadataCompat param2MediaMetadataCompat) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(3, param2MediaMetadataCompat, (Bundle)null);
      }
      
      public void onPlaybackStateChanged(PlaybackStateCompat param2PlaybackStateCompat) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(2, param2PlaybackStateCompat, (Bundle)null);
      }
      
      public void onQueueChanged(List<MediaSessionCompat.QueueItem> param2List) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(5, param2List, (Bundle)null);
      }
      
      public void onQueueTitleChanged(CharSequence param2CharSequence) throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(6, param2CharSequence, (Bundle)null);
      }
      
      public void onSessionDestroyed() throws RemoteException {
        MediaControllerCompat.Callback.this.mHandler.post(8, (Object)null, (Bundle)null);
      }
      
      public void onVolumeInfoChanged(ParcelableVolumeInfo param2ParcelableVolumeInfo) throws RemoteException {
        MediaControllerCompat.PlaybackInfo playbackInfo = null;
        if (param2ParcelableVolumeInfo != null)
          playbackInfo = new MediaControllerCompat.PlaybackInfo(param2ParcelableVolumeInfo.volumeType, param2ParcelableVolumeInfo.audioStream, param2ParcelableVolumeInfo.controlType, param2ParcelableVolumeInfo.maxVolume, param2ParcelableVolumeInfo.currentVolume); 
        MediaControllerCompat.Callback.this.mHandler.post(4, playbackInfo, (Bundle)null);
      }
    }
  }
  
  private class MessageHandler extends Handler {
    private static final int MSG_DESTROYED = 8;
    
    private static final int MSG_EVENT = 1;
    
    private static final int MSG_UPDATE_EXTRAS = 7;
    
    private static final int MSG_UPDATE_METADATA = 3;
    
    private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
    
    private static final int MSG_UPDATE_QUEUE = 5;
    
    private static final int MSG_UPDATE_QUEUE_TITLE = 6;
    
    private static final int MSG_UPDATE_VOLUME = 4;
    
    public MessageHandler(Looper param1Looper) {
      super(param1Looper);
    }
    
    public void handleMessage(Message param1Message) {
      if (!this.this$0.mRegistered);
      switch (param1Message.what) {
        default:
          return;
        case 1:
          this.this$0.onSessionEvent((String)param1Message.obj, param1Message.getData());
        case 2:
          this.this$0.onPlaybackStateChanged((PlaybackStateCompat)param1Message.obj);
        case 3:
          this.this$0.onMetadataChanged((MediaMetadataCompat)param1Message.obj);
        case 5:
          this.this$0.onQueueChanged((List<MediaSessionCompat.QueueItem>)param1Message.obj);
        case 6:
          this.this$0.onQueueTitleChanged((CharSequence)param1Message.obj);
        case 7:
          this.this$0.onExtrasChanged((Bundle)param1Message.obj);
        case 4:
          this.this$0.onAudioInfoChanged((MediaControllerCompat.PlaybackInfo)param1Message.obj);
        case 8:
          break;
      } 
      this.this$0.onSessionDestroyed();
    }
    
    public void post(int param1Int, Object param1Object, Bundle param1Bundle) {
      param1Object = obtainMessage(param1Int, param1Object);
      param1Object.setData(param1Bundle);
      param1Object.sendToTarget();
    }
  }
  
  private class StubApi21 implements MediaControllerCompatApi21.Callback {
    public void onAudioInfoChanged(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      this.this$0.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5));
    }
    
    public void onExtrasChanged(Bundle param1Bundle) {
      this.this$0.onExtrasChanged(param1Bundle);
    }
    
    public void onMetadataChanged(Object param1Object) {
      this.this$0.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(param1Object));
    }
    
    public void onPlaybackStateChanged(Object param1Object) {
      if (!this.this$0.mHasExtraCallback || Build.VERSION.SDK_INT >= 22)
        this.this$0.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(param1Object)); 
    }
    
    public void onQueueChanged(List<?> param1List) {
      this.this$0.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(param1List));
    }
    
    public void onQueueTitleChanged(CharSequence param1CharSequence) {
      this.this$0.onQueueTitleChanged(param1CharSequence);
    }
    
    public void onSessionDestroyed() {
      this.this$0.onSessionDestroyed();
    }
    
    public void onSessionEvent(String param1String, Bundle param1Bundle) {
      if (!this.this$0.mHasExtraCallback || Build.VERSION.SDK_INT >= 23)
        this.this$0.onSessionEvent(param1String, param1Bundle); 
    }
  }
  
  private class StubCompat extends IMediaControllerCallback.Stub {
    public void onEvent(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(1, param1String, param1Bundle);
    }
    
    public void onExtrasChanged(Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(7, param1Bundle, (Bundle)null);
    }
    
    public void onMetadataChanged(MediaMetadataCompat param1MediaMetadataCompat) throws RemoteException {
      this.this$0.mHandler.post(3, param1MediaMetadataCompat, (Bundle)null);
    }
    
    public void onPlaybackStateChanged(PlaybackStateCompat param1PlaybackStateCompat) throws RemoteException {
      this.this$0.mHandler.post(2, param1PlaybackStateCompat, (Bundle)null);
    }
    
    public void onQueueChanged(List<MediaSessionCompat.QueueItem> param1List) throws RemoteException {
      this.this$0.mHandler.post(5, param1List, (Bundle)null);
    }
    
    public void onQueueTitleChanged(CharSequence param1CharSequence) throws RemoteException {
      this.this$0.mHandler.post(6, param1CharSequence, (Bundle)null);
    }
    
    public void onSessionDestroyed() throws RemoteException {
      this.this$0.mHandler.post(8, (Object)null, (Bundle)null);
    }
    
    public void onVolumeInfoChanged(ParcelableVolumeInfo param1ParcelableVolumeInfo) throws RemoteException {
      MediaControllerCompat.PlaybackInfo playbackInfo = null;
      if (param1ParcelableVolumeInfo != null)
        playbackInfo = new MediaControllerCompat.PlaybackInfo(param1ParcelableVolumeInfo.volumeType, param1ParcelableVolumeInfo.audioStream, param1ParcelableVolumeInfo.controlType, param1ParcelableVolumeInfo.maxVolume, param1ParcelableVolumeInfo.currentVolume); 
      this.this$0.mHandler.post(4, playbackInfo, (Bundle)null);
    }
  }
  
  private static class MediaControllerExtraData extends SupportActivity.ExtraData {
    private final MediaControllerCompat mMediaController;
    
    MediaControllerExtraData(MediaControllerCompat param1MediaControllerCompat) {
      this.mMediaController = param1MediaControllerCompat;
    }
    
    MediaControllerCompat getMediaController() {
      return this.mMediaController;
    }
  }
  
  static interface MediaControllerImpl {
    void adjustVolume(int param1Int1, int param1Int2);
    
    boolean dispatchMediaButtonEvent(KeyEvent param1KeyEvent);
    
    Bundle getExtras();
    
    long getFlags();
    
    Object getMediaController();
    
    MediaMetadataCompat getMetadata();
    
    String getPackageName();
    
    MediaControllerCompat.PlaybackInfo getPlaybackInfo();
    
    PlaybackStateCompat getPlaybackState();
    
    List<MediaSessionCompat.QueueItem> getQueue();
    
    CharSequence getQueueTitle();
    
    int getRatingType();
    
    PendingIntent getSessionActivity();
    
    MediaControllerCompat.TransportControls getTransportControls();
    
    void registerCallback(MediaControllerCompat.Callback param1Callback, Handler param1Handler);
    
    void sendCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver);
    
    void setVolumeTo(int param1Int1, int param1Int2);
    
    void unregisterCallback(MediaControllerCompat.Callback param1Callback);
  }
  
  static class MediaControllerImplApi21 implements MediaControllerImpl {
    private HashMap<MediaControllerCompat.Callback, ExtraCallback> mCallbackMap = new HashMap<MediaControllerCompat.Callback, ExtraCallback>();
    
    protected final Object mControllerObj;
    
    private IMediaSession mExtraBinder;
    
    private List<MediaControllerCompat.Callback> mPendingCallbacks;
    
    public MediaControllerImplApi21(Context param1Context, MediaSessionCompat.Token param1Token) throws RemoteException {
      this.mControllerObj = MediaControllerCompatApi21.fromToken(param1Context, param1Token.getToken());
      if (this.mControllerObj == null)
        throw new RemoteException(); 
      requestExtraBinder();
    }
    
    public MediaControllerImplApi21(Context param1Context, MediaSessionCompat param1MediaSessionCompat) {
      this.mControllerObj = MediaControllerCompatApi21.fromToken(param1Context, param1MediaSessionCompat.getSessionToken().getToken());
      requestExtraBinder();
    }
    
    private void requestExtraBinder() {
      sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", null, new ResultReceiver(new Handler()) {
            protected void onReceiveResult(int param2Int, Bundle param2Bundle) {
              if (param2Bundle != null) {
                MediaControllerCompat.MediaControllerImplApi21.access$202(MediaControllerCompat.MediaControllerImplApi21.this, IMediaSession.Stub.asInterface(BundleCompat.getBinder(param2Bundle, "android.support.v4.media.session.EXTRA_BINDER")));
                if (MediaControllerCompat.MediaControllerImplApi21.this.mPendingCallbacks != null) {
                  Iterator<MediaControllerCompat.Callback> iterator = MediaControllerCompat.MediaControllerImplApi21.this.mPendingCallbacks.iterator();
                  while (iterator.hasNext()) {
                    MediaControllerCompat.Callback callback = iterator.next();
                    MediaControllerCompat.MediaControllerImplApi21.ExtraCallback extraCallback = new MediaControllerCompat.MediaControllerImplApi21.ExtraCallback(callback);
                    MediaControllerCompat.MediaControllerImplApi21.this.mCallbackMap.put(callback, extraCallback);
                    callback.mHasExtraCallback = true;
                    try {
                      MediaControllerCompat.MediaControllerImplApi21.this.mExtraBinder.registerCallbackListener(extraCallback);
                    } catch (RemoteException remoteException) {
                      Log.e("MediaControllerCompat", "Dead object in registerCallback. " + remoteException);
                      break;
                    } 
                  } 
                  MediaControllerCompat.MediaControllerImplApi21.access$302(MediaControllerCompat.MediaControllerImplApi21.this, null);
                } 
              } 
            }
          });
    }
    
    public void adjustVolume(int param1Int1, int param1Int2) {
      MediaControllerCompatApi21.adjustVolume(this.mControllerObj, param1Int1, param1Int2);
    }
    
    public boolean dispatchMediaButtonEvent(KeyEvent param1KeyEvent) {
      return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, param1KeyEvent);
    }
    
    public Bundle getExtras() {
      return MediaControllerCompatApi21.getExtras(this.mControllerObj);
    }
    
    public long getFlags() {
      return MediaControllerCompatApi21.getFlags(this.mControllerObj);
    }
    
    public Object getMediaController() {
      return this.mControllerObj;
    }
    
    public MediaMetadataCompat getMetadata() {
      null = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
      return (null != null) ? MediaMetadataCompat.fromMediaMetadata(null) : null;
    }
    
    public String getPackageName() {
      return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
    }
    
    public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
      null = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
      return (null != null) ? new MediaControllerCompat.PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(null), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(null), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(null), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(null), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(null)) : null;
    }
    
    public PlaybackStateCompat getPlaybackState() {
      if (Build.VERSION.SDK_INT < 22 && this.mExtraBinder != null)
        try {
          return this.mExtraBinder.getPlaybackState();
        } catch (RemoteException remoteException) {
          Log.e("MediaControllerCompat", "Dead object in getPlaybackState. " + remoteException);
        }  
      null = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj);
      return (null != null) ? PlaybackStateCompat.fromPlaybackState(null) : null;
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
      null = MediaControllerCompatApi21.getQueue(this.mControllerObj);
      return (null != null) ? MediaSessionCompat.QueueItem.fromQueueItemList(null) : null;
    }
    
    public CharSequence getQueueTitle() {
      return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
    }
    
    public int getRatingType() {
      if (Build.VERSION.SDK_INT < 22 && this.mExtraBinder != null)
        try {
          return this.mExtraBinder.getRatingType();
        } catch (RemoteException remoteException) {
          Log.e("MediaControllerCompat", "Dead object in getRatingType. " + remoteException);
        }  
      return MediaControllerCompatApi21.getRatingType(this.mControllerObj);
    }
    
    public PendingIntent getSessionActivity() {
      return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
    }
    
    public MediaControllerCompat.TransportControls getTransportControls() {
      null = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
      return (null != null) ? new MediaControllerCompat.TransportControlsApi21(null) : null;
    }
    
    public final void registerCallback(MediaControllerCompat.Callback param1Callback, Handler param1Handler) {
      ExtraCallback extraCallback;
      MediaControllerCompatApi21.registerCallback(this.mControllerObj, param1Callback.mCallbackObj, param1Handler);
      if (this.mExtraBinder != null) {
        param1Callback.setHandler(param1Handler);
        extraCallback = new ExtraCallback(param1Callback);
        this.mCallbackMap.put(param1Callback, extraCallback);
        param1Callback.mHasExtraCallback = true;
        try {
          this.mExtraBinder.registerCallbackListener(extraCallback);
        } catch (RemoteException remoteException) {
          Log.e("MediaControllerCompat", "Dead object in registerCallback. " + remoteException);
        } 
        return;
      } 
      if (this.mPendingCallbacks == null)
        this.mPendingCallbacks = new ArrayList<MediaControllerCompat.Callback>(); 
      remoteException.setHandler((Handler)extraCallback);
      this.mPendingCallbacks.add(remoteException);
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      MediaControllerCompatApi21.sendCommand(this.mControllerObj, param1String, param1Bundle, param1ResultReceiver);
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2) {
      MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, param1Int1, param1Int2);
    }
    
    public final void unregisterCallback(MediaControllerCompat.Callback param1Callback) {
      MediaControllerCompatApi21.unregisterCallback(this.mControllerObj, param1Callback.mCallbackObj);
      if (this.mExtraBinder != null) {
        try {
          ExtraCallback extraCallback = this.mCallbackMap.remove(param1Callback);
          if (extraCallback != null)
            this.mExtraBinder.unregisterCallbackListener(extraCallback); 
        } catch (RemoteException remoteException) {
          Log.e("MediaControllerCompat", "Dead object in unregisterCallback. " + remoteException);
        } 
        return;
      } 
      if (this.mPendingCallbacks == null)
        this.mPendingCallbacks = new ArrayList<MediaControllerCompat.Callback>(); 
      this.mPendingCallbacks.remove(remoteException);
    }
    
    private class ExtraCallback extends IMediaControllerCallback.Stub {
      private MediaControllerCompat.Callback mCallback;
      
      ExtraCallback(MediaControllerCompat.Callback param2Callback) {
        this.mCallback = param2Callback;
      }
      
      public void onEvent(final String event, final Bundle extras) throws RemoteException {
        this.mCallback.mHandler.post(new Runnable() {
              public void run() {
                MediaControllerCompat.MediaControllerImplApi21.ExtraCallback.this.mCallback.onSessionEvent(event, extras);
              }
            });
      }
      
      public void onExtrasChanged(Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void onMetadataChanged(MediaMetadataCompat param2MediaMetadataCompat) throws RemoteException {
        throw new AssertionError();
      }
      
      public void onPlaybackStateChanged(final PlaybackStateCompat state) throws RemoteException {
        this.mCallback.mHandler.post(new Runnable() {
              public void run() {
                MediaControllerCompat.MediaControllerImplApi21.ExtraCallback.this.mCallback.onPlaybackStateChanged(state);
              }
            });
      }
      
      public void onQueueChanged(List<MediaSessionCompat.QueueItem> param2List) throws RemoteException {
        throw new AssertionError();
      }
      
      public void onQueueTitleChanged(CharSequence param2CharSequence) throws RemoteException {
        throw new AssertionError();
      }
      
      public void onSessionDestroyed() throws RemoteException {
        throw new AssertionError();
      }
      
      public void onVolumeInfoChanged(ParcelableVolumeInfo param2ParcelableVolumeInfo) throws RemoteException {
        throw new AssertionError();
      }
    }
    
    class null implements Runnable {
      public void run() {
        this.this$1.mCallback.onSessionEvent(event, extras);
      }
    }
    
    class null implements Runnable {
      public void run() {
        this.this$1.mCallback.onPlaybackStateChanged(state);
      }
    }
  }
  
  class null extends ResultReceiver {
    null(Handler param1Handler) {
      super(param1Handler);
    }
    
    protected void onReceiveResult(int param1Int, Bundle param1Bundle) {
      if (param1Bundle != null) {
        MediaControllerCompat.MediaControllerImplApi21.access$202(this.this$0, IMediaSession.Stub.asInterface(BundleCompat.getBinder(param1Bundle, "android.support.v4.media.session.EXTRA_BINDER")));
        if (this.this$0.mPendingCallbacks != null) {
          Iterator<MediaControllerCompat.Callback> iterator = this.this$0.mPendingCallbacks.iterator();
          while (iterator.hasNext()) {
            MediaControllerCompat.Callback callback = iterator.next();
            MediaControllerCompat.MediaControllerImplApi21.ExtraCallback extraCallback = new MediaControllerCompat.MediaControllerImplApi21.ExtraCallback(callback);
            this.this$0.mCallbackMap.put(callback, extraCallback);
            callback.mHasExtraCallback = true;
            try {
              this.this$0.mExtraBinder.registerCallbackListener(extraCallback);
            } catch (RemoteException remoteException) {
              Log.e("MediaControllerCompat", "Dead object in registerCallback. " + remoteException);
              break;
            } 
          } 
          MediaControllerCompat.MediaControllerImplApi21.access$302(this.this$0, null);
        } 
      } 
    }
  }
  
  private class ExtraCallback extends IMediaControllerCallback.Stub {
    private MediaControllerCompat.Callback mCallback;
    
    ExtraCallback(MediaControllerCompat.Callback param1Callback) {
      this.mCallback = param1Callback;
    }
    
    public void onEvent(final String event, final Bundle extras) throws RemoteException {
      this.mCallback.mHandler.post(new Runnable() {
            public void run() {
              MediaControllerCompat.MediaControllerImplApi21.ExtraCallback.this.mCallback.onSessionEvent(event, extras);
            }
          });
    }
    
    public void onExtrasChanged(Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void onMetadataChanged(MediaMetadataCompat param1MediaMetadataCompat) throws RemoteException {
      throw new AssertionError();
    }
    
    public void onPlaybackStateChanged(final PlaybackStateCompat state) throws RemoteException {
      this.mCallback.mHandler.post(new Runnable() {
            public void run() {
              MediaControllerCompat.MediaControllerImplApi21.ExtraCallback.this.mCallback.onPlaybackStateChanged(state);
            }
          });
    }
    
    public void onQueueChanged(List<MediaSessionCompat.QueueItem> param1List) throws RemoteException {
      throw new AssertionError();
    }
    
    public void onQueueTitleChanged(CharSequence param1CharSequence) throws RemoteException {
      throw new AssertionError();
    }
    
    public void onSessionDestroyed() throws RemoteException {
      throw new AssertionError();
    }
    
    public void onVolumeInfoChanged(ParcelableVolumeInfo param1ParcelableVolumeInfo) throws RemoteException {
      throw new AssertionError();
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.mCallback.onSessionEvent(event, extras);
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.mCallback.onPlaybackStateChanged(state);
    }
  }
  
  static class MediaControllerImplApi23 extends MediaControllerImplApi21 {
    public MediaControllerImplApi23(Context param1Context, MediaSessionCompat.Token param1Token) throws RemoteException {
      super(param1Context, param1Token);
    }
    
    public MediaControllerImplApi23(Context param1Context, MediaSessionCompat param1MediaSessionCompat) {
      super(param1Context, param1MediaSessionCompat);
    }
    
    public MediaControllerCompat.TransportControls getTransportControls() {
      null = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
      return (null != null) ? new MediaControllerCompat.TransportControlsApi23(null) : null;
    }
  }
  
  static class MediaControllerImplApi24 extends MediaControllerImplApi23 {
    public MediaControllerImplApi24(Context param1Context, MediaSessionCompat.Token param1Token) throws RemoteException {
      super(param1Context, param1Token);
    }
    
    public MediaControllerImplApi24(Context param1Context, MediaSessionCompat param1MediaSessionCompat) {
      super(param1Context, param1MediaSessionCompat);
    }
    
    public MediaControllerCompat.TransportControls getTransportControls() {
      null = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
      return (null != null) ? new MediaControllerCompat.TransportControlsApi24(null) : null;
    }
  }
  
  static class MediaControllerImplBase implements MediaControllerImpl {
    private IMediaSession mBinder;
    
    private MediaSessionCompat.Token mToken;
    
    private MediaControllerCompat.TransportControls mTransportControls;
    
    public MediaControllerImplBase(MediaSessionCompat.Token param1Token) {
      this.mToken = param1Token;
      this.mBinder = IMediaSession.Stub.asInterface((IBinder)param1Token.getToken());
    }
    
    public void adjustVolume(int param1Int1, int param1Int2) {
      try {
        this.mBinder.adjustVolume(param1Int1, param1Int2, (String)null);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in adjustVolume. " + remoteException);
      } 
    }
    
    public boolean dispatchMediaButtonEvent(KeyEvent param1KeyEvent) {
      if (param1KeyEvent == null)
        throw new IllegalArgumentException("event may not be null."); 
      try {
        this.mBinder.sendMediaButton(param1KeyEvent);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in dispatchMediaButtonEvent. " + remoteException);
      } 
      return false;
    }
    
    public Bundle getExtras() {
      try {
        Bundle bundle = this.mBinder.getExtras();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getExtras. " + remoteException);
        remoteException = null;
      } 
      return (Bundle)remoteException;
    }
    
    public long getFlags() {
      long l;
      try {
        l = this.mBinder.getFlags();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getFlags. " + remoteException);
        l = 0L;
      } 
      return l;
    }
    
    public Object getMediaController() {
      return null;
    }
    
    public MediaMetadataCompat getMetadata() {
      try {
        MediaMetadataCompat mediaMetadataCompat = this.mBinder.getMetadata();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getMetadata. " + remoteException);
        remoteException = null;
      } 
      return (MediaMetadataCompat)remoteException;
    }
    
    public String getPackageName() {
      try {
        String str = this.mBinder.getPackageName();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getPackageName. " + remoteException);
        remoteException = null;
      } 
      return (String)remoteException;
    }
    
    public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
      try {
        ParcelableVolumeInfo parcelableVolumeInfo = this.mBinder.getVolumeAttributes();
        MediaControllerCompat.PlaybackInfo playbackInfo = new MediaControllerCompat.PlaybackInfo();
        this(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getPlaybackInfo. " + remoteException);
        remoteException = null;
      } 
      return (MediaControllerCompat.PlaybackInfo)remoteException;
    }
    
    public PlaybackStateCompat getPlaybackState() {
      try {
        PlaybackStateCompat playbackStateCompat = this.mBinder.getPlaybackState();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getPlaybackState. " + remoteException);
        remoteException = null;
      } 
      return (PlaybackStateCompat)remoteException;
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
      try {
        List<MediaSessionCompat.QueueItem> list = this.mBinder.getQueue();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getQueue. " + remoteException);
        remoteException = null;
      } 
      return (List<MediaSessionCompat.QueueItem>)remoteException;
    }
    
    public CharSequence getQueueTitle() {
      try {
        CharSequence charSequence = this.mBinder.getQueueTitle();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getQueueTitle. " + remoteException);
        remoteException = null;
      } 
      return (CharSequence)remoteException;
    }
    
    public int getRatingType() {
      boolean bool;
      try {
        bool = this.mBinder.getRatingType();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getRatingType. " + remoteException);
        bool = false;
      } 
      return bool;
    }
    
    public PendingIntent getSessionActivity() {
      try {
        PendingIntent pendingIntent = this.mBinder.getLaunchPendingIntent();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in getSessionActivity. " + remoteException);
        remoteException = null;
      } 
      return (PendingIntent)remoteException;
    }
    
    public MediaControllerCompat.TransportControls getTransportControls() {
      if (this.mTransportControls == null)
        this.mTransportControls = new MediaControllerCompat.TransportControlsBase(this.mBinder); 
      return this.mTransportControls;
    }
    
    public void registerCallback(MediaControllerCompat.Callback param1Callback, Handler param1Handler) {
      if (param1Callback == null)
        throw new IllegalArgumentException("callback may not be null."); 
      try {
        this.mBinder.asBinder().linkToDeath(param1Callback, 0);
        this.mBinder.registerCallbackListener((IMediaControllerCallback)param1Callback.mCallbackObj);
        param1Callback.setHandler(param1Handler);
        param1Callback.mRegistered = true;
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in registerCallback. " + remoteException);
        param1Callback.onSessionDestroyed();
      } 
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      try {
        IMediaSession iMediaSession = this.mBinder;
        MediaSessionCompat.ResultReceiverWrapper resultReceiverWrapper = new MediaSessionCompat.ResultReceiverWrapper();
        this(param1ResultReceiver);
        iMediaSession.sendCommand(param1String, param1Bundle, resultReceiverWrapper);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in sendCommand. " + remoteException);
      } 
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2) {
      try {
        this.mBinder.setVolumeTo(param1Int1, param1Int2, (String)null);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in setVolumeTo. " + remoteException);
      } 
    }
    
    public void unregisterCallback(MediaControllerCompat.Callback param1Callback) {
      if (param1Callback == null)
        throw new IllegalArgumentException("callback may not be null."); 
      try {
        this.mBinder.unregisterCallbackListener((IMediaControllerCallback)param1Callback.mCallbackObj);
        this.mBinder.asBinder().unlinkToDeath(param1Callback, 0);
        param1Callback.mRegistered = false;
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in unregisterCallback. " + remoteException);
      } 
    }
  }
  
  public static final class PlaybackInfo {
    public static final int PLAYBACK_TYPE_LOCAL = 1;
    
    public static final int PLAYBACK_TYPE_REMOTE = 2;
    
    private final int mAudioStream;
    
    private final int mCurrentVolume;
    
    private final int mMaxVolume;
    
    private final int mPlaybackType;
    
    private final int mVolumeControl;
    
    PlaybackInfo(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      this.mPlaybackType = param1Int1;
      this.mAudioStream = param1Int2;
      this.mVolumeControl = param1Int3;
      this.mMaxVolume = param1Int4;
      this.mCurrentVolume = param1Int5;
    }
    
    public int getAudioStream() {
      return this.mAudioStream;
    }
    
    public int getCurrentVolume() {
      return this.mCurrentVolume;
    }
    
    public int getMaxVolume() {
      return this.mMaxVolume;
    }
    
    public int getPlaybackType() {
      return this.mPlaybackType;
    }
    
    public int getVolumeControl() {
      return this.mVolumeControl;
    }
  }
  
  public static abstract class TransportControls {
    public abstract void fastForward();
    
    public abstract void pause();
    
    public abstract void play();
    
    public abstract void playFromMediaId(String param1String, Bundle param1Bundle);
    
    public abstract void playFromSearch(String param1String, Bundle param1Bundle);
    
    public abstract void playFromUri(Uri param1Uri, Bundle param1Bundle);
    
    public abstract void prepare();
    
    public abstract void prepareFromMediaId(String param1String, Bundle param1Bundle);
    
    public abstract void prepareFromSearch(String param1String, Bundle param1Bundle);
    
    public abstract void prepareFromUri(Uri param1Uri, Bundle param1Bundle);
    
    public abstract void rewind();
    
    public abstract void seekTo(long param1Long);
    
    public abstract void sendCustomAction(PlaybackStateCompat.CustomAction param1CustomAction, Bundle param1Bundle);
    
    public abstract void sendCustomAction(String param1String, Bundle param1Bundle);
    
    public abstract void setRating(RatingCompat param1RatingCompat);
    
    public abstract void skipToNext();
    
    public abstract void skipToPrevious();
    
    public abstract void skipToQueueItem(long param1Long);
    
    public abstract void stop();
  }
  
  static class TransportControlsApi21 extends TransportControls {
    protected final Object mControlsObj;
    
    public TransportControlsApi21(Object param1Object) {
      this.mControlsObj = param1Object;
    }
    
    public void fastForward() {
      MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
    }
    
    public void pause() {
      MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
    }
    
    public void play() {
      MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) {
      MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, param1String, param1Bundle);
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) {
      MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, param1String, param1Bundle);
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) {
      if (param1Uri == null || Uri.EMPTY.equals(param1Uri))
        throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri."); 
      Bundle bundle = new Bundle();
      bundle.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)param1Uri);
      bundle.putParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS", (Parcelable)param1Bundle);
      sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", bundle);
    }
    
    public void prepare() {
      sendCustomAction("android.support.v4.media.session.action.PREPARE", (Bundle)null);
    }
    
    public void prepareFromMediaId(String param1String, Bundle param1Bundle) {
      Bundle bundle = new Bundle();
      bundle.putString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID", param1String);
      bundle.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", param1Bundle);
      sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID", bundle);
    }
    
    public void prepareFromSearch(String param1String, Bundle param1Bundle) {
      Bundle bundle = new Bundle();
      bundle.putString("android.support.v4.media.session.action.ARGUMENT_QUERY", param1String);
      bundle.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", param1Bundle);
      sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_SEARCH", bundle);
    }
    
    public void prepareFromUri(Uri param1Uri, Bundle param1Bundle) {
      Bundle bundle = new Bundle();
      bundle.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)param1Uri);
      bundle.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", param1Bundle);
      sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_URI", bundle);
    }
    
    public void rewind() {
      MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
    }
    
    public void seekTo(long param1Long) {
      MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, param1Long);
    }
    
    public void sendCustomAction(PlaybackStateCompat.CustomAction param1CustomAction, Bundle param1Bundle) {
      MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, param1CustomAction.getAction(), param1Bundle);
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) {
      MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, param1String, param1Bundle);
    }
    
    public void setRating(RatingCompat param1RatingCompat) {
      Object object = this.mControlsObj;
      if (param1RatingCompat != null) {
        Object object1 = param1RatingCompat.getRating();
      } else {
        param1RatingCompat = null;
      } 
      MediaControllerCompatApi21.TransportControls.setRating(object, param1RatingCompat);
    }
    
    public void skipToNext() {
      MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
    }
    
    public void skipToPrevious() {
      MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
    }
    
    public void skipToQueueItem(long param1Long) {
      MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, param1Long);
    }
    
    public void stop() {
      MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
    }
  }
  
  static class TransportControlsApi23 extends TransportControlsApi21 {
    public TransportControlsApi23(Object param1Object) {
      super(param1Object);
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) {
      MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, param1Uri, param1Bundle);
    }
  }
  
  static class TransportControlsApi24 extends TransportControlsApi23 {
    public TransportControlsApi24(Object param1Object) {
      super(param1Object);
    }
    
    public void prepare() {
      MediaControllerCompatApi24.TransportControls.prepare(this.mControlsObj);
    }
    
    public void prepareFromMediaId(String param1String, Bundle param1Bundle) {
      MediaControllerCompatApi24.TransportControls.prepareFromMediaId(this.mControlsObj, param1String, param1Bundle);
    }
    
    public void prepareFromSearch(String param1String, Bundle param1Bundle) {
      MediaControllerCompatApi24.TransportControls.prepareFromSearch(this.mControlsObj, param1String, param1Bundle);
    }
    
    public void prepareFromUri(Uri param1Uri, Bundle param1Bundle) {
      MediaControllerCompatApi24.TransportControls.prepareFromUri(this.mControlsObj, param1Uri, param1Bundle);
    }
  }
  
  static class TransportControlsBase extends TransportControls {
    private IMediaSession mBinder;
    
    public TransportControlsBase(IMediaSession param1IMediaSession) {
      this.mBinder = param1IMediaSession;
    }
    
    public void fastForward() {
      try {
        this.mBinder.fastForward();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in fastForward. " + remoteException);
      } 
    }
    
    public void pause() {
      try {
        this.mBinder.pause();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in pause. " + remoteException);
      } 
    }
    
    public void play() {
      try {
        this.mBinder.play();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in play. " + remoteException);
      } 
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) {
      try {
        this.mBinder.playFromMediaId(param1String, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in playFromMediaId. " + remoteException);
      } 
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) {
      try {
        this.mBinder.playFromSearch(param1String, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in playFromSearch. " + remoteException);
      } 
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) {
      try {
        this.mBinder.playFromUri(param1Uri, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in playFromUri. " + remoteException);
      } 
    }
    
    public void prepare() {
      try {
        this.mBinder.prepare();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in prepare. " + remoteException);
      } 
    }
    
    public void prepareFromMediaId(String param1String, Bundle param1Bundle) {
      try {
        this.mBinder.prepareFromMediaId(param1String, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in prepareFromMediaId. " + remoteException);
      } 
    }
    
    public void prepareFromSearch(String param1String, Bundle param1Bundle) {
      try {
        this.mBinder.prepareFromSearch(param1String, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in prepareFromSearch. " + remoteException);
      } 
    }
    
    public void prepareFromUri(Uri param1Uri, Bundle param1Bundle) {
      try {
        this.mBinder.prepareFromUri(param1Uri, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in prepareFromUri. " + remoteException);
      } 
    }
    
    public void rewind() {
      try {
        this.mBinder.rewind();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in rewind. " + remoteException);
      } 
    }
    
    public void seekTo(long param1Long) {
      try {
        this.mBinder.seekTo(param1Long);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in seekTo. " + remoteException);
      } 
    }
    
    public void sendCustomAction(PlaybackStateCompat.CustomAction param1CustomAction, Bundle param1Bundle) {
      sendCustomAction(param1CustomAction.getAction(), param1Bundle);
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) {
      try {
        this.mBinder.sendCustomAction(param1String, param1Bundle);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in sendCustomAction. " + remoteException);
      } 
    }
    
    public void setRating(RatingCompat param1RatingCompat) {
      try {
        this.mBinder.rate(param1RatingCompat);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in setRating. " + remoteException);
      } 
    }
    
    public void skipToNext() {
      try {
        this.mBinder.next();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in skipToNext. " + remoteException);
      } 
    }
    
    public void skipToPrevious() {
      try {
        this.mBinder.previous();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in skipToPrevious. " + remoteException);
      } 
    }
    
    public void skipToQueueItem(long param1Long) {
      try {
        this.mBinder.skipToQueueItem(param1Long);
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in skipToQueueItem. " + remoteException);
      } 
    }
    
    public void stop() {
      try {
        this.mBinder.stop();
      } catch (RemoteException remoteException) {
        Log.e("MediaControllerCompat", "Dead object in stop. " + remoteException);
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/session/MediaControllerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */