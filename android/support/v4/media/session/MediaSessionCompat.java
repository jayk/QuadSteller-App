package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
  static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
  
  static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
  
  static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
  
  static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
  
  static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
  
  static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
  
  static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
  
  static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
  
  static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
  
  static final String EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
  
  public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
  
  public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
  
  private static final int MAX_BITMAP_SIZE_IN_DP = 320;
  
  static final String TAG = "MediaSessionCompat";
  
  static int sMaxBitmapSize;
  
  private final ArrayList<OnActiveChangeListener> mActiveListeners = new ArrayList<OnActiveChangeListener>();
  
  private final MediaControllerCompat mController;
  
  private final MediaSessionImpl mImpl;
  
  private MediaSessionCompat(Context paramContext, MediaSessionImpl paramMediaSessionImpl) {
    this.mImpl = paramMediaSessionImpl;
    if (Build.VERSION.SDK_INT >= 21)
      setCallback(new Callback() {
          
          }); 
    this.mController = new MediaControllerCompat(paramContext, this);
  }
  
  public MediaSessionCompat(Context paramContext, String paramString) {
    this(paramContext, paramString, null, null);
  }
  
  public MediaSessionCompat(Context paramContext, String paramString, ComponentName paramComponentName, PendingIntent paramPendingIntent) {
    if (paramContext == null)
      throw new IllegalArgumentException("context must not be null"); 
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("tag must not be null or empty"); 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaSessionImplApi21(paramContext, paramString);
    } else {
      this.mImpl = new MediaSessionImplBase(paramContext, paramString, paramComponentName, paramPendingIntent);
    } 
    this.mController = new MediaControllerCompat(paramContext, this);
    if (sMaxBitmapSize == 0)
      sMaxBitmapSize = (int)TypedValue.applyDimension(1, 320.0F, paramContext.getResources().getDisplayMetrics()); 
  }
  
  public static MediaSessionCompat fromMediaSession(Context paramContext, Object paramObject) {
    return (paramContext == null || paramObject == null || Build.VERSION.SDK_INT < 21) ? null : new MediaSessionCompat(paramContext, new MediaSessionImplApi21(paramObject));
  }
  
  @Deprecated
  public static MediaSessionCompat obtain(Context paramContext, Object paramObject) {
    return fromMediaSession(paramContext, paramObject);
  }
  
  public void addOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener) {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null"); 
    this.mActiveListeners.add(paramOnActiveChangeListener);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public String getCallingPackage() {
    return this.mImpl.getCallingPackage();
  }
  
  public MediaControllerCompat getController() {
    return this.mController;
  }
  
  public Object getMediaSession() {
    return this.mImpl.getMediaSession();
  }
  
  public Object getRemoteControlClient() {
    return this.mImpl.getRemoteControlClient();
  }
  
  public Token getSessionToken() {
    return this.mImpl.getSessionToken();
  }
  
  public boolean isActive() {
    return this.mImpl.isActive();
  }
  
  public void release() {
    this.mImpl.release();
  }
  
  public void removeOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener) {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null"); 
    this.mActiveListeners.remove(paramOnActiveChangeListener);
  }
  
  public void sendSessionEvent(String paramString, Bundle paramBundle) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("event cannot be null or empty"); 
    this.mImpl.sendSessionEvent(paramString, paramBundle);
  }
  
  public void setActive(boolean paramBoolean) {
    this.mImpl.setActive(paramBoolean);
    Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
    while (iterator.hasNext())
      ((OnActiveChangeListener)iterator.next()).onActiveChanged(); 
  }
  
  public void setCallback(Callback paramCallback) {
    setCallback(paramCallback, null);
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler) {
    MediaSessionImpl mediaSessionImpl = this.mImpl;
    if (paramHandler == null)
      paramHandler = new Handler(); 
    mediaSessionImpl.setCallback(paramCallback, paramHandler);
  }
  
  public void setExtras(Bundle paramBundle) {
    this.mImpl.setExtras(paramBundle);
  }
  
  public void setFlags(int paramInt) {
    this.mImpl.setFlags(paramInt);
  }
  
  public void setMediaButtonReceiver(PendingIntent paramPendingIntent) {
    this.mImpl.setMediaButtonReceiver(paramPendingIntent);
  }
  
  public void setMetadata(MediaMetadataCompat paramMediaMetadataCompat) {
    this.mImpl.setMetadata(paramMediaMetadataCompat);
  }
  
  public void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat) {
    this.mImpl.setPlaybackState(paramPlaybackStateCompat);
  }
  
  public void setPlaybackToLocal(int paramInt) {
    this.mImpl.setPlaybackToLocal(paramInt);
  }
  
  public void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat) {
    if (paramVolumeProviderCompat == null)
      throw new IllegalArgumentException("volumeProvider may not be null!"); 
    this.mImpl.setPlaybackToRemote(paramVolumeProviderCompat);
  }
  
  public void setQueue(List<QueueItem> paramList) {
    this.mImpl.setQueue(paramList);
  }
  
  public void setQueueTitle(CharSequence paramCharSequence) {
    this.mImpl.setQueueTitle(paramCharSequence);
  }
  
  public void setRatingType(int paramInt) {
    this.mImpl.setRatingType(paramInt);
  }
  
  public void setSessionActivity(PendingIntent paramPendingIntent) {
    this.mImpl.setSessionActivity(paramPendingIntent);
  }
  
  public static abstract class Callback {
    final Object mCallbackObj;
    
    WeakReference<MediaSessionCompat.MediaSessionImpl> mSessionImpl;
    
    public Callback() {
      if (Build.VERSION.SDK_INT >= 24) {
        this.mCallbackObj = MediaSessionCompatApi24.createCallback(new StubApi24());
        return;
      } 
      if (Build.VERSION.SDK_INT >= 23) {
        this.mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
        return;
      } 
      if (Build.VERSION.SDK_INT >= 21) {
        this.mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
        return;
      } 
      this.mCallbackObj = null;
    }
    
    public void onCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {}
    
    public void onCustomAction(String param1String, Bundle param1Bundle) {}
    
    public void onFastForward() {}
    
    public boolean onMediaButtonEvent(Intent param1Intent) {
      return false;
    }
    
    public void onPause() {}
    
    public void onPlay() {}
    
    public void onPlayFromMediaId(String param1String, Bundle param1Bundle) {}
    
    public void onPlayFromSearch(String param1String, Bundle param1Bundle) {}
    
    public void onPlayFromUri(Uri param1Uri, Bundle param1Bundle) {}
    
    public void onPrepare() {}
    
    public void onPrepareFromMediaId(String param1String, Bundle param1Bundle) {}
    
    public void onPrepareFromSearch(String param1String, Bundle param1Bundle) {}
    
    public void onPrepareFromUri(Uri param1Uri, Bundle param1Bundle) {}
    
    public void onRewind() {}
    
    public void onSeekTo(long param1Long) {}
    
    public void onSetRating(RatingCompat param1RatingCompat) {}
    
    public void onSkipToNext() {}
    
    public void onSkipToPrevious() {}
    
    public void onSkipToQueueItem(long param1Long) {}
    
    public void onStop() {}
    
    private class StubApi21 implements MediaSessionCompatApi21.Callback {
      public void onCommand(String param2String, Bundle param2Bundle, ResultReceiver param2ResultReceiver) {
        Bundle bundle;
        MediaSessionCompat.MediaSessionImplApi21 mediaSessionImplApi21;
        if (param2String.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER")) {
          mediaSessionImplApi21 = (MediaSessionCompat.MediaSessionImplApi21)MediaSessionCompat.Callback.this.mSessionImpl.get();
          if (mediaSessionImplApi21 != null) {
            bundle = new Bundle();
            BundleCompat.putBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER", (IBinder)mediaSessionImplApi21.getExtraSessionBinder());
            param2ResultReceiver.send(0, bundle);
          } 
          return;
        } 
        MediaSessionCompat.Callback.this.onCommand((String)bundle, (Bundle)mediaSessionImplApi21, param2ResultReceiver);
      }
      
      public void onCustomAction(String param2String, Bundle param2Bundle) {
        Uri uri2;
        String str;
        Uri uri1;
        if (param2String.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
          uri2 = (Uri)param2Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
          param2Bundle = (Bundle)param2Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPlayFromUri(uri2, param2Bundle);
          return;
        } 
        if (uri2.equals("android.support.v4.media.session.action.PREPARE")) {
          MediaSessionCompat.Callback.this.onPrepare();
          return;
        } 
        if (uri2.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID")) {
          str = param2Bundle.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
          param2Bundle = param2Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromMediaId(str, param2Bundle);
          return;
        } 
        if (str.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH")) {
          str = param2Bundle.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
          param2Bundle = param2Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromSearch(str, param2Bundle);
          return;
        } 
        if (str.equals("android.support.v4.media.session.action.PREPARE_FROM_URI")) {
          uri1 = (Uri)param2Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
          param2Bundle = param2Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromUri(uri1, param2Bundle);
          return;
        } 
        MediaSessionCompat.Callback.this.onCustomAction((String)uri1, param2Bundle);
      }
      
      public void onFastForward() {
        MediaSessionCompat.Callback.this.onFastForward();
      }
      
      public boolean onMediaButtonEvent(Intent param2Intent) {
        return MediaSessionCompat.Callback.this.onMediaButtonEvent(param2Intent);
      }
      
      public void onPause() {
        MediaSessionCompat.Callback.this.onPause();
      }
      
      public void onPlay() {
        MediaSessionCompat.Callback.this.onPlay();
      }
      
      public void onPlayFromMediaId(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromMediaId(param2String, param2Bundle);
      }
      
      public void onPlayFromSearch(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromSearch(param2String, param2Bundle);
      }
      
      public void onRewind() {
        MediaSessionCompat.Callback.this.onRewind();
      }
      
      public void onSeekTo(long param2Long) {
        MediaSessionCompat.Callback.this.onSeekTo(param2Long);
      }
      
      public void onSetRating(Object param2Object) {
        MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(param2Object));
      }
      
      public void onSkipToNext() {
        MediaSessionCompat.Callback.this.onSkipToNext();
      }
      
      public void onSkipToPrevious() {
        MediaSessionCompat.Callback.this.onSkipToPrevious();
      }
      
      public void onSkipToQueueItem(long param2Long) {
        MediaSessionCompat.Callback.this.onSkipToQueueItem(param2Long);
      }
      
      public void onStop() {
        MediaSessionCompat.Callback.this.onStop();
      }
    }
    
    private class StubApi23 extends StubApi21 implements MediaSessionCompatApi23.Callback {
      public void onPlayFromUri(Uri param2Uri, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromUri(param2Uri, param2Bundle);
      }
    }
    
    private class StubApi24 extends StubApi23 implements MediaSessionCompatApi24.Callback {
      public void onPrepare() {
        MediaSessionCompat.Callback.this.onPrepare();
      }
      
      public void onPrepareFromMediaId(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPrepareFromMediaId(param2String, param2Bundle);
      }
      
      public void onPrepareFromSearch(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPrepareFromSearch(param2String, param2Bundle);
      }
      
      public void onPrepareFromUri(Uri param2Uri, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPrepareFromUri(param2Uri, param2Bundle);
      }
    }
  }
  
  private class StubApi21 implements MediaSessionCompatApi21.Callback {
    public void onCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      Bundle bundle;
      MediaSessionCompat.MediaSessionImplApi21 mediaSessionImplApi21;
      if (param1String.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER")) {
        mediaSessionImplApi21 = (MediaSessionCompat.MediaSessionImplApi21)this.this$0.mSessionImpl.get();
        if (mediaSessionImplApi21 != null) {
          bundle = new Bundle();
          BundleCompat.putBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER", (IBinder)mediaSessionImplApi21.getExtraSessionBinder());
          param1ResultReceiver.send(0, bundle);
        } 
        return;
      } 
      this.this$0.onCommand((String)bundle, (Bundle)mediaSessionImplApi21, param1ResultReceiver);
    }
    
    public void onCustomAction(String param1String, Bundle param1Bundle) {
      Uri uri2;
      String str;
      Uri uri1;
      if (param1String.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
        uri2 = (Uri)param1Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
        param1Bundle = (Bundle)param1Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
        this.this$0.onPlayFromUri(uri2, param1Bundle);
        return;
      } 
      if (uri2.equals("android.support.v4.media.session.action.PREPARE")) {
        this.this$0.onPrepare();
        return;
      } 
      if (uri2.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID")) {
        str = param1Bundle.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
        param1Bundle = param1Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
        this.this$0.onPrepareFromMediaId(str, param1Bundle);
        return;
      } 
      if (str.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH")) {
        str = param1Bundle.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
        param1Bundle = param1Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
        this.this$0.onPrepareFromSearch(str, param1Bundle);
        return;
      } 
      if (str.equals("android.support.v4.media.session.action.PREPARE_FROM_URI")) {
        uri1 = (Uri)param1Bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
        param1Bundle = param1Bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
        this.this$0.onPrepareFromUri(uri1, param1Bundle);
        return;
      } 
      this.this$0.onCustomAction((String)uri1, param1Bundle);
    }
    
    public void onFastForward() {
      this.this$0.onFastForward();
    }
    
    public boolean onMediaButtonEvent(Intent param1Intent) {
      return this.this$0.onMediaButtonEvent(param1Intent);
    }
    
    public void onPause() {
      this.this$0.onPause();
    }
    
    public void onPlay() {
      this.this$0.onPlay();
    }
    
    public void onPlayFromMediaId(String param1String, Bundle param1Bundle) {
      this.this$0.onPlayFromMediaId(param1String, param1Bundle);
    }
    
    public void onPlayFromSearch(String param1String, Bundle param1Bundle) {
      this.this$0.onPlayFromSearch(param1String, param1Bundle);
    }
    
    public void onRewind() {
      this.this$0.onRewind();
    }
    
    public void onSeekTo(long param1Long) {
      this.this$0.onSeekTo(param1Long);
    }
    
    public void onSetRating(Object param1Object) {
      this.this$0.onSetRating(RatingCompat.fromRating(param1Object));
    }
    
    public void onSkipToNext() {
      this.this$0.onSkipToNext();
    }
    
    public void onSkipToPrevious() {
      this.this$0.onSkipToPrevious();
    }
    
    public void onSkipToQueueItem(long param1Long) {
      this.this$0.onSkipToQueueItem(param1Long);
    }
    
    public void onStop() {
      this.this$0.onStop();
    }
  }
  
  private class StubApi23 extends Callback.StubApi21 implements MediaSessionCompatApi23.Callback {
    StubApi23() {
      super((MediaSessionCompat.Callback)MediaSessionCompat.this);
    }
    
    public void onPlayFromUri(Uri param1Uri, Bundle param1Bundle) {
      this.this$0.onPlayFromUri(param1Uri, param1Bundle);
    }
  }
  
  private class StubApi24 extends Callback.StubApi23 implements MediaSessionCompatApi24.Callback {
    StubApi24() {
      super((MediaSessionCompat.Callback)MediaSessionCompat.this);
    }
    
    public void onPrepare() {
      this.this$0.onPrepare();
    }
    
    public void onPrepareFromMediaId(String param1String, Bundle param1Bundle) {
      this.this$0.onPrepareFromMediaId(param1String, param1Bundle);
    }
    
    public void onPrepareFromSearch(String param1String, Bundle param1Bundle) {
      this.this$0.onPrepareFromSearch(param1String, param1Bundle);
    }
    
    public void onPrepareFromUri(Uri param1Uri, Bundle param1Bundle) {
      this.this$0.onPrepareFromUri(param1Uri, param1Bundle);
    }
  }
  
  static interface MediaSessionImpl {
    String getCallingPackage();
    
    Object getMediaSession();
    
    Object getRemoteControlClient();
    
    MediaSessionCompat.Token getSessionToken();
    
    boolean isActive();
    
    void release();
    
    void sendSessionEvent(String param1String, Bundle param1Bundle);
    
    void setActive(boolean param1Boolean);
    
    void setCallback(MediaSessionCompat.Callback param1Callback, Handler param1Handler);
    
    void setExtras(Bundle param1Bundle);
    
    void setFlags(int param1Int);
    
    void setMediaButtonReceiver(PendingIntent param1PendingIntent);
    
    void setMetadata(MediaMetadataCompat param1MediaMetadataCompat);
    
    void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat);
    
    void setPlaybackToLocal(int param1Int);
    
    void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat);
    
    void setQueue(List<MediaSessionCompat.QueueItem> param1List);
    
    void setQueueTitle(CharSequence param1CharSequence);
    
    void setRatingType(int param1Int);
    
    void setSessionActivity(PendingIntent param1PendingIntent);
  }
  
  static class MediaSessionImplApi21 implements MediaSessionImpl {
    private boolean mDestroyed = false;
    
    private final RemoteCallbackList<IMediaControllerCallback> mExtraControllerCallbacks = new RemoteCallbackList();
    
    private ExtraSession mExtraSessionBinder;
    
    private PlaybackStateCompat mPlaybackState;
    
    int mRatingType;
    
    private final Object mSessionObj;
    
    private final MediaSessionCompat.Token mToken;
    
    public MediaSessionImplApi21(Context param1Context, String param1String) {
      this.mSessionObj = MediaSessionCompatApi21.createSession(param1Context, param1String);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }
    
    public MediaSessionImplApi21(Object param1Object) {
      this.mSessionObj = MediaSessionCompatApi21.verifySession(param1Object);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }
    
    public String getCallingPackage() {
      return (Build.VERSION.SDK_INT < 24) ? null : MediaSessionCompatApi24.getCallingPackage(this.mSessionObj);
    }
    
    ExtraSession getExtraSessionBinder() {
      if (this.mExtraSessionBinder == null)
        this.mExtraSessionBinder = new ExtraSession(); 
      return this.mExtraSessionBinder;
    }
    
    public Object getMediaSession() {
      return this.mSessionObj;
    }
    
    public Object getRemoteControlClient() {
      return null;
    }
    
    public MediaSessionCompat.Token getSessionToken() {
      return this.mToken;
    }
    
    public boolean isActive() {
      return MediaSessionCompatApi21.isActive(this.mSessionObj);
    }
    
    public void release() {
      this.mDestroyed = true;
      MediaSessionCompatApi21.release(this.mSessionObj);
    }
    
    public void sendSessionEvent(String param1String, Bundle param1Bundle) {
      if (Build.VERSION.SDK_INT < 23) {
        int i = this.mExtraControllerCallbacks.beginBroadcast() - 1;
        while (true) {
          if (i >= 0) {
            IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
            try {
              iMediaControllerCallback.onEvent(param1String, param1Bundle);
            } catch (RemoteException remoteException) {}
            i--;
            continue;
          } 
          this.mExtraControllerCallbacks.finishBroadcast();
          MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, param1String, param1Bundle);
          return;
        } 
      } 
      MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, param1String, param1Bundle);
    }
    
    public void setActive(boolean param1Boolean) {
      MediaSessionCompatApi21.setActive(this.mSessionObj, param1Boolean);
    }
    
    public void setCallback(MediaSessionCompat.Callback param1Callback, Handler param1Handler) {
      Object object2;
      Object object1 = this.mSessionObj;
      if (param1Callback == null) {
        object2 = null;
      } else {
        object2 = param1Callback.mCallbackObj;
      } 
      MediaSessionCompatApi21.setCallback(object1, object2, param1Handler);
      param1Callback.mSessionImpl = new WeakReference<MediaSessionCompat.MediaSessionImpl>(this);
    }
    
    public void setExtras(Bundle param1Bundle) {
      MediaSessionCompatApi21.setExtras(this.mSessionObj, param1Bundle);
    }
    
    public void setFlags(int param1Int) {
      MediaSessionCompatApi21.setFlags(this.mSessionObj, param1Int);
    }
    
    public void setMediaButtonReceiver(PendingIntent param1PendingIntent) {
      MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, param1PendingIntent);
    }
    
    public void setMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      Object object1;
      Object object2 = this.mSessionObj;
      if (param1MediaMetadataCompat == null) {
        param1MediaMetadataCompat = null;
      } else {
        object1 = param1MediaMetadataCompat.getMediaMetadata();
      } 
      MediaSessionCompatApi21.setMetadata(object2, object1);
    }
    
    public void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat) {
      // Byte code:
      //   0: getstatic android/os/Build$VERSION.SDK_INT : I
      //   3: bipush #22
      //   5: if_icmpge -> 59
      //   8: aload_0
      //   9: aload_1
      //   10: putfield mPlaybackState : Landroid/support/v4/media/session/PlaybackStateCompat;
      //   13: aload_0
      //   14: getfield mExtraControllerCallbacks : Landroid/os/RemoteCallbackList;
      //   17: invokevirtual beginBroadcast : ()I
      //   20: iconst_1
      //   21: isub
      //   22: istore_2
      //   23: iload_2
      //   24: iflt -> 52
      //   27: aload_0
      //   28: getfield mExtraControllerCallbacks : Landroid/os/RemoteCallbackList;
      //   31: iload_2
      //   32: invokevirtual getBroadcastItem : (I)Landroid/os/IInterface;
      //   35: checkcast android/support/v4/media/session/IMediaControllerCallback
      //   38: astore_3
      //   39: aload_3
      //   40: aload_1
      //   41: invokeinterface onPlaybackStateChanged : (Landroid/support/v4/media/session/PlaybackStateCompat;)V
      //   46: iinc #2, -1
      //   49: goto -> 23
      //   52: aload_0
      //   53: getfield mExtraControllerCallbacks : Landroid/os/RemoteCallbackList;
      //   56: invokevirtual finishBroadcast : ()V
      //   59: aload_0
      //   60: getfield mSessionObj : Ljava/lang/Object;
      //   63: astore_3
      //   64: aload_1
      //   65: ifnonnull -> 76
      //   68: aconst_null
      //   69: astore_1
      //   70: aload_3
      //   71: aload_1
      //   72: invokestatic setPlaybackState : (Ljava/lang/Object;Ljava/lang/Object;)V
      //   75: return
      //   76: aload_1
      //   77: invokevirtual getPlaybackState : ()Ljava/lang/Object;
      //   80: astore_1
      //   81: goto -> 70
      //   84: astore_3
      //   85: goto -> 46
      // Exception table:
      //   from	to	target	type
      //   39	46	84	android/os/RemoteException
    }
    
    public void setPlaybackToLocal(int param1Int) {
      MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, param1Int);
    }
    
    public void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat) {
      MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, param1VolumeProviderCompat.getVolumeProvider());
    }
    
    public void setQueue(List<MediaSessionCompat.QueueItem> param1List) {
      ArrayList<Object> arrayList = null;
      if (param1List != null) {
        ArrayList<Object> arrayList1 = new ArrayList();
        Iterator<MediaSessionCompat.QueueItem> iterator = param1List.iterator();
        while (true) {
          arrayList = arrayList1;
          if (iterator.hasNext()) {
            arrayList1.add(((MediaSessionCompat.QueueItem)iterator.next()).getQueueItem());
            continue;
          } 
          break;
        } 
      } 
      MediaSessionCompatApi21.setQueue(this.mSessionObj, arrayList);
    }
    
    public void setQueueTitle(CharSequence param1CharSequence) {
      MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, param1CharSequence);
    }
    
    public void setRatingType(int param1Int) {
      if (Build.VERSION.SDK_INT < 22) {
        this.mRatingType = param1Int;
        return;
      } 
      MediaSessionCompatApi22.setRatingType(this.mSessionObj, param1Int);
    }
    
    public void setSessionActivity(PendingIntent param1PendingIntent) {
      MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, param1PendingIntent);
    }
    
    class ExtraSession extends IMediaSession.Stub {
      public void adjustVolume(int param2Int1, int param2Int2, String param2String) {
        throw new AssertionError();
      }
      
      public void fastForward() throws RemoteException {
        throw new AssertionError();
      }
      
      public Bundle getExtras() {
        throw new AssertionError();
      }
      
      public long getFlags() {
        throw new AssertionError();
      }
      
      public PendingIntent getLaunchPendingIntent() {
        throw new AssertionError();
      }
      
      public MediaMetadataCompat getMetadata() {
        throw new AssertionError();
      }
      
      public String getPackageName() {
        throw new AssertionError();
      }
      
      public PlaybackStateCompat getPlaybackState() {
        return MediaSessionCompat.MediaSessionImplApi21.this.mPlaybackState;
      }
      
      public List<MediaSessionCompat.QueueItem> getQueue() {
        return null;
      }
      
      public CharSequence getQueueTitle() {
        throw new AssertionError();
      }
      
      public int getRatingType() {
        return MediaSessionCompat.MediaSessionImplApi21.this.mRatingType;
      }
      
      public String getTag() {
        throw new AssertionError();
      }
      
      public ParcelableVolumeInfo getVolumeAttributes() {
        throw new AssertionError();
      }
      
      public boolean isTransportControlEnabled() {
        throw new AssertionError();
      }
      
      public void next() throws RemoteException {
        throw new AssertionError();
      }
      
      public void pause() throws RemoteException {
        throw new AssertionError();
      }
      
      public void play() throws RemoteException {
        throw new AssertionError();
      }
      
      public void playFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void playFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void playFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void prepare() throws RemoteException {
        throw new AssertionError();
      }
      
      public void prepareFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void prepareFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void prepareFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public void previous() throws RemoteException {
        throw new AssertionError();
      }
      
      public void rate(RatingCompat param2RatingCompat) throws RemoteException {
        throw new AssertionError();
      }
      
      public void registerCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        if (!MediaSessionCompat.MediaSessionImplApi21.this.mDestroyed)
          MediaSessionCompat.MediaSessionImplApi21.this.mExtraControllerCallbacks.register(param2IMediaControllerCallback); 
      }
      
      public void rewind() throws RemoteException {
        throw new AssertionError();
      }
      
      public void seekTo(long param2Long) throws RemoteException {
        throw new AssertionError();
      }
      
      public void sendCommand(String param2String, Bundle param2Bundle, MediaSessionCompat.ResultReceiverWrapper param2ResultReceiverWrapper) {
        throw new AssertionError();
      }
      
      public void sendCustomAction(String param2String, Bundle param2Bundle) throws RemoteException {
        throw new AssertionError();
      }
      
      public boolean sendMediaButton(KeyEvent param2KeyEvent) {
        throw new AssertionError();
      }
      
      public void setVolumeTo(int param2Int1, int param2Int2, String param2String) {
        throw new AssertionError();
      }
      
      public void skipToQueueItem(long param2Long) {
        throw new AssertionError();
      }
      
      public void stop() throws RemoteException {
        throw new AssertionError();
      }
      
      public void unregisterCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        MediaSessionCompat.MediaSessionImplApi21.this.mExtraControllerCallbacks.unregister(param2IMediaControllerCallback);
      }
    }
  }
  
  class ExtraSession extends IMediaSession.Stub {
    public void adjustVolume(int param1Int1, int param1Int2, String param1String) {
      throw new AssertionError();
    }
    
    public void fastForward() throws RemoteException {
      throw new AssertionError();
    }
    
    public Bundle getExtras() {
      throw new AssertionError();
    }
    
    public long getFlags() {
      throw new AssertionError();
    }
    
    public PendingIntent getLaunchPendingIntent() {
      throw new AssertionError();
    }
    
    public MediaMetadataCompat getMetadata() {
      throw new AssertionError();
    }
    
    public String getPackageName() {
      throw new AssertionError();
    }
    
    public PlaybackStateCompat getPlaybackState() {
      return this.this$0.mPlaybackState;
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
      return null;
    }
    
    public CharSequence getQueueTitle() {
      throw new AssertionError();
    }
    
    public int getRatingType() {
      return this.this$0.mRatingType;
    }
    
    public String getTag() {
      throw new AssertionError();
    }
    
    public ParcelableVolumeInfo getVolumeAttributes() {
      throw new AssertionError();
    }
    
    public boolean isTransportControlEnabled() {
      throw new AssertionError();
    }
    
    public void next() throws RemoteException {
      throw new AssertionError();
    }
    
    public void pause() throws RemoteException {
      throw new AssertionError();
    }
    
    public void play() throws RemoteException {
      throw new AssertionError();
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void prepare() throws RemoteException {
      throw new AssertionError();
    }
    
    public void prepareFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void prepareFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void prepareFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public void previous() throws RemoteException {
      throw new AssertionError();
    }
    
    public void rate(RatingCompat param1RatingCompat) throws RemoteException {
      throw new AssertionError();
    }
    
    public void registerCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      if (!this.this$0.mDestroyed)
        this.this$0.mExtraControllerCallbacks.register(param1IMediaControllerCallback); 
    }
    
    public void rewind() throws RemoteException {
      throw new AssertionError();
    }
    
    public void seekTo(long param1Long) throws RemoteException {
      throw new AssertionError();
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, MediaSessionCompat.ResultReceiverWrapper param1ResultReceiverWrapper) {
      throw new AssertionError();
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) throws RemoteException {
      throw new AssertionError();
    }
    
    public boolean sendMediaButton(KeyEvent param1KeyEvent) {
      throw new AssertionError();
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2, String param1String) {
      throw new AssertionError();
    }
    
    public void skipToQueueItem(long param1Long) {
      throw new AssertionError();
    }
    
    public void stop() throws RemoteException {
      throw new AssertionError();
    }
    
    public void unregisterCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      this.this$0.mExtraControllerCallbacks.unregister(param1IMediaControllerCallback);
    }
  }
  
  static class MediaSessionImplBase implements MediaSessionImpl {
    final AudioManager mAudioManager;
    
    volatile MediaSessionCompat.Callback mCallback;
    
    private final Context mContext;
    
    final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks = new RemoteCallbackList();
    
    boolean mDestroyed = false;
    
    Bundle mExtras;
    
    int mFlags;
    
    private MessageHandler mHandler;
    
    private boolean mIsActive = false;
    
    private boolean mIsMbrRegistered = false;
    
    private boolean mIsRccRegistered = false;
    
    int mLocalStream;
    
    final Object mLock = new Object();
    
    private final ComponentName mMediaButtonReceiverComponentName;
    
    private final PendingIntent mMediaButtonReceiverIntent;
    
    MediaMetadataCompat mMetadata;
    
    final String mPackageName;
    
    List<MediaSessionCompat.QueueItem> mQueue;
    
    CharSequence mQueueTitle;
    
    int mRatingType;
    
    private final Object mRccObj;
    
    PendingIntent mSessionActivity;
    
    PlaybackStateCompat mState;
    
    private final MediaSessionStub mStub;
    
    final String mTag;
    
    private final MediaSessionCompat.Token mToken;
    
    private VolumeProviderCompat.Callback mVolumeCallback = new VolumeProviderCompat.Callback() {
        public void onVolumeChanged(VolumeProviderCompat param2VolumeProviderCompat) {
          if (MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider == param2VolumeProviderCompat) {
            ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(MediaSessionCompat.MediaSessionImplBase.this.mVolumeType, MediaSessionCompat.MediaSessionImplBase.this.mLocalStream, param2VolumeProviderCompat.getVolumeControl(), param2VolumeProviderCompat.getMaxVolume(), param2VolumeProviderCompat.getCurrentVolume());
            MediaSessionCompat.MediaSessionImplBase.this.sendVolumeInfoChanged(parcelableVolumeInfo);
          } 
        }
      };
    
    VolumeProviderCompat mVolumeProvider;
    
    int mVolumeType;
    
    public MediaSessionImplBase(Context param1Context, String param1String, ComponentName param1ComponentName, PendingIntent param1PendingIntent) {
      ComponentName componentName = param1ComponentName;
      if (param1ComponentName == null) {
        param1ComponentName = MediaButtonReceiver.getMediaButtonReceiverComponent(param1Context);
        componentName = param1ComponentName;
        if (param1ComponentName == null) {
          Log.w("MediaSessionCompat", "Couldn't find a unique registered media button receiver in the given context.");
          componentName = param1ComponentName;
        } 
      } 
      PendingIntent pendingIntent = param1PendingIntent;
      if (componentName != null) {
        pendingIntent = param1PendingIntent;
        if (param1PendingIntent == null) {
          Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
          intent.setComponent(componentName);
          pendingIntent = PendingIntent.getBroadcast(param1Context, 0, intent, 0);
        } 
      } 
      if (componentName == null)
        throw new IllegalArgumentException("MediaButtonReceiver component may not be null."); 
      this.mContext = param1Context;
      this.mPackageName = param1Context.getPackageName();
      this.mAudioManager = (AudioManager)param1Context.getSystemService("audio");
      this.mTag = param1String;
      this.mMediaButtonReceiverComponentName = componentName;
      this.mMediaButtonReceiverIntent = pendingIntent;
      this.mStub = new MediaSessionStub();
      this.mToken = new MediaSessionCompat.Token(this.mStub);
      this.mRatingType = 0;
      this.mVolumeType = 1;
      this.mLocalStream = 3;
      if (Build.VERSION.SDK_INT >= 14) {
        this.mRccObj = MediaSessionCompatApi14.createRemoteControlClient(pendingIntent);
        return;
      } 
      this.mRccObj = null;
    }
    
    private void sendEvent(String param1String, Bundle param1Bundle) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onEvent(param1String, param1Bundle);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendExtras(Bundle param1Bundle) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onExtrasChanged(param1Bundle);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onMetadataChanged(param1MediaMetadataCompat);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendQueue(List<MediaSessionCompat.QueueItem> param1List) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onQueueChanged(param1List);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendQueueTitle(CharSequence param1CharSequence) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onQueueTitleChanged(param1CharSequence);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendSessionDestroyed() {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onSessionDestroyed();
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        this.mControllerCallbacks.kill();
        return;
      } 
    }
    
    private void sendState(PlaybackStateCompat param1PlaybackStateCompat) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onPlaybackStateChanged(param1PlaybackStateCompat);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private boolean update() {
      boolean bool1 = false;
      if (this.mIsActive) {
        if (!this.mIsMbrRegistered && (this.mFlags & 0x1) != 0) {
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
          } else {
            ((AudioManager)this.mContext.getSystemService("audio")).registerMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
          } 
          this.mIsMbrRegistered = true;
        } else if (this.mIsMbrRegistered && (this.mFlags & 0x1) == 0) {
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
          } else {
            ((AudioManager)this.mContext.getSystemService("audio")).unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
          } 
          this.mIsMbrRegistered = false;
        } 
        boolean bool = bool1;
        if (Build.VERSION.SDK_INT >= 14) {
          if (!this.mIsRccRegistered && (this.mFlags & 0x2) != 0) {
            MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
            this.mIsRccRegistered = true;
            return true;
          } 
        } else {
          return bool;
        } 
        bool = bool1;
        if (this.mIsRccRegistered) {
          bool = bool1;
          if ((this.mFlags & 0x2) == 0) {
            MediaSessionCompatApi14.setState(this.mRccObj, 0);
            MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
            this.mIsRccRegistered = false;
            bool = bool1;
          } 
        } 
        return bool;
      } 
      if (this.mIsMbrRegistered) {
        if (Build.VERSION.SDK_INT >= 18) {
          MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
        } else {
          ((AudioManager)this.mContext.getSystemService("audio")).unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
        } 
        this.mIsMbrRegistered = false;
      } 
      boolean bool2 = bool1;
      if (this.mIsRccRegistered) {
        MediaSessionCompatApi14.setState(this.mRccObj, 0);
        MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
        this.mIsRccRegistered = false;
        bool2 = bool1;
      } 
      return bool2;
    }
    
    void adjustVolume(int param1Int1, int param1Int2) {
      if (this.mVolumeType == 2) {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onAdjustVolume(param1Int1); 
        return;
      } 
      this.mAudioManager.adjustStreamVolume(this.mLocalStream, param1Int1, param1Int2);
    }
    
    public String getCallingPackage() {
      return null;
    }
    
    public Object getMediaSession() {
      return null;
    }
    
    public Object getRemoteControlClient() {
      return this.mRccObj;
    }
    
    public MediaSessionCompat.Token getSessionToken() {
      return this.mToken;
    }
    
    PlaybackStateCompat getStateWithUpdatedPosition() {
      // Byte code:
      //   0: ldc2_w -1
      //   3: lstore_1
      //   4: aload_0
      //   5: getfield mLock : Ljava/lang/Object;
      //   8: astore_3
      //   9: aload_3
      //   10: monitorenter
      //   11: aload_0
      //   12: getfield mState : Landroid/support/v4/media/session/PlaybackStateCompat;
      //   15: astore #4
      //   17: lload_1
      //   18: lstore #5
      //   20: aload_0
      //   21: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   24: ifnull -> 55
      //   27: lload_1
      //   28: lstore #5
      //   30: aload_0
      //   31: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   34: ldc_w 'android.media.metadata.DURATION'
      //   37: invokevirtual containsKey : (Ljava/lang/String;)Z
      //   40: ifeq -> 55
      //   43: aload_0
      //   44: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   47: ldc_w 'android.media.metadata.DURATION'
      //   50: invokevirtual getLong : (Ljava/lang/String;)J
      //   53: lstore #5
      //   55: aload_3
      //   56: monitorexit
      //   57: aconst_null
      //   58: astore #7
      //   60: aload #7
      //   62: astore_3
      //   63: aload #4
      //   65: ifnull -> 185
      //   68: aload #4
      //   70: invokevirtual getState : ()I
      //   73: iconst_3
      //   74: if_icmpeq -> 98
      //   77: aload #4
      //   79: invokevirtual getState : ()I
      //   82: iconst_4
      //   83: if_icmpeq -> 98
      //   86: aload #7
      //   88: astore_3
      //   89: aload #4
      //   91: invokevirtual getState : ()I
      //   94: iconst_5
      //   95: if_icmpne -> 185
      //   98: aload #4
      //   100: invokevirtual getLastPositionUpdateTime : ()J
      //   103: lstore_1
      //   104: invokestatic elapsedRealtime : ()J
      //   107: lstore #8
      //   109: aload #7
      //   111: astore_3
      //   112: lload_1
      //   113: lconst_0
      //   114: lcmp
      //   115: ifle -> 185
      //   118: aload #4
      //   120: invokevirtual getPlaybackSpeed : ()F
      //   123: lload #8
      //   125: lload_1
      //   126: lsub
      //   127: l2f
      //   128: fmul
      //   129: f2l
      //   130: aload #4
      //   132: invokevirtual getPosition : ()J
      //   135: ladd
      //   136: lstore_1
      //   137: lload #5
      //   139: lconst_0
      //   140: lcmp
      //   141: iflt -> 201
      //   144: lload_1
      //   145: lload #5
      //   147: lcmp
      //   148: ifle -> 201
      //   151: new android/support/v4/media/session/PlaybackStateCompat$Builder
      //   154: dup
      //   155: aload #4
      //   157: invokespecial <init> : (Landroid/support/v4/media/session/PlaybackStateCompat;)V
      //   160: astore_3
      //   161: aload_3
      //   162: aload #4
      //   164: invokevirtual getState : ()I
      //   167: lload #5
      //   169: aload #4
      //   171: invokevirtual getPlaybackSpeed : ()F
      //   174: lload #8
      //   176: invokevirtual setState : (IJFJ)Landroid/support/v4/media/session/PlaybackStateCompat$Builder;
      //   179: pop
      //   180: aload_3
      //   181: invokevirtual build : ()Landroid/support/v4/media/session/PlaybackStateCompat;
      //   184: astore_3
      //   185: aload_3
      //   186: ifnonnull -> 216
      //   189: aload #4
      //   191: astore_3
      //   192: aload_3
      //   193: areturn
      //   194: astore #4
      //   196: aload_3
      //   197: monitorexit
      //   198: aload #4
      //   200: athrow
      //   201: lload_1
      //   202: lstore #5
      //   204: lload_1
      //   205: lconst_0
      //   206: lcmp
      //   207: ifge -> 151
      //   210: lconst_0
      //   211: lstore #5
      //   213: goto -> 151
      //   216: goto -> 192
      // Exception table:
      //   from	to	target	type
      //   11	17	194	finally
      //   20	27	194	finally
      //   30	55	194	finally
      //   55	57	194	finally
      //   196	198	194	finally
    }
    
    public boolean isActive() {
      return this.mIsActive;
    }
    
    void postToHandler(int param1Int) {
      postToHandler(param1Int, null);
    }
    
    void postToHandler(int param1Int, Object param1Object) {
      postToHandler(param1Int, param1Object, null);
    }
    
    void postToHandler(int param1Int, Object param1Object, Bundle param1Bundle) {
      synchronized (this.mLock) {
        if (this.mHandler != null)
          this.mHandler.post(param1Int, param1Object, param1Bundle); 
        return;
      } 
    }
    
    public void release() {
      this.mIsActive = false;
      this.mDestroyed = true;
      update();
      sendSessionDestroyed();
    }
    
    public void sendSessionEvent(String param1String, Bundle param1Bundle) {
      sendEvent(param1String, param1Bundle);
    }
    
    void sendVolumeInfoChanged(ParcelableVolumeInfo param1ParcelableVolumeInfo) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onVolumeInfoChanged(param1ParcelableVolumeInfo);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    public void setActive(boolean param1Boolean) {
      if (param1Boolean != this.mIsActive) {
        this.mIsActive = param1Boolean;
        if (update()) {
          setMetadata(this.mMetadata);
          setPlaybackState(this.mState);
        } 
      } 
    }
    
    public void setCallback(MediaSessionCompat.Callback param1Callback, Handler param1Handler) {
      this.mCallback = param1Callback;
      if (param1Callback == null) {
        if (Build.VERSION.SDK_INT >= 18)
          MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null); 
        if (Build.VERSION.SDK_INT >= 19)
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null); 
        return;
      } 
      null = param1Handler;
      if (param1Handler == null)
        null = new Handler(); 
      synchronized (this.mLock) {
        MessageHandler messageHandler = new MessageHandler();
        this(this, null.getLooper());
        this.mHandler = messageHandler;
        null = new MediaSessionCompatApi19.Callback() {
            public void onSeekTo(long param2Long) {
              MediaSessionCompat.MediaSessionImplBase.this.postToHandler(18, Long.valueOf(param2Long));
            }
            
            public void onSetRating(Object param2Object) {
              MediaSessionCompat.MediaSessionImplBase.this.postToHandler(19, RatingCompat.fromRating(param2Object));
            }
          };
        if (Build.VERSION.SDK_INT >= 18) {
          Object object = MediaSessionCompatApi18.createPlaybackPositionUpdateListener((MediaSessionCompatApi18.Callback)null);
          MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, object);
        } 
        if (Build.VERSION.SDK_INT >= 19) {
          Object object = MediaSessionCompatApi19.createMetadataUpdateListener((MediaSessionCompatApi19.Callback)null);
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, object);
        } 
        return;
      } 
    }
    
    public void setExtras(Bundle param1Bundle) {
      this.mExtras = param1Bundle;
      sendExtras(param1Bundle);
    }
    
    public void setFlags(int param1Int) {
      synchronized (this.mLock) {
        this.mFlags = param1Int;
        update();
        return;
      } 
    }
    
    public void setMediaButtonReceiver(PendingIntent param1PendingIntent) {}
    
    public void setMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      Object object1 = null;
      Object object2 = null;
      null = param1MediaMetadataCompat;
      if (param1MediaMetadataCompat != null)
        null = (new MediaMetadataCompat.Builder(param1MediaMetadataCompat, MediaSessionCompat.sMaxBitmapSize)).build(); 
      synchronized (this.mLock) {
        this.mMetadata = null;
        sendMetadata(null);
        if (this.mIsActive) {
          if (Build.VERSION.SDK_INT >= 19) {
            long l;
            object1 = this.mRccObj;
            if (null == null) {
              null = object2;
            } else {
              null = null.getBundle();
            } 
            if (this.mState == null) {
              l = 0L;
            } else {
              l = this.mState.getActions();
            } 
            MediaSessionCompatApi19.setMetadata(object1, (Bundle)null, l);
            return;
          } 
          if (Build.VERSION.SDK_INT >= 14) {
            object2 = this.mRccObj;
            if (null == null) {
              null = object1;
            } else {
              null = null.getBundle();
            } 
            MediaSessionCompatApi14.setMetadata(object2, (Bundle)null);
          } 
        } 
        return;
      } 
    }
    
    public void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat) {
      synchronized (this.mLock) {
        this.mState = param1PlaybackStateCompat;
        sendState(param1PlaybackStateCompat);
        if (this.mIsActive) {
          if (param1PlaybackStateCompat == null) {
            if (Build.VERSION.SDK_INT >= 14) {
              MediaSessionCompatApi14.setState(this.mRccObj, 0);
              MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0L);
            } 
            return;
          } 
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.setState(this.mRccObj, param1PlaybackStateCompat.getState(), param1PlaybackStateCompat.getPosition(), param1PlaybackStateCompat.getPlaybackSpeed(), param1PlaybackStateCompat.getLastPositionUpdateTime());
          } else if (Build.VERSION.SDK_INT >= 14) {
            MediaSessionCompatApi14.setState(this.mRccObj, param1PlaybackStateCompat.getState());
          } 
          if (Build.VERSION.SDK_INT >= 19) {
            MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions());
            return;
          } 
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions());
            return;
          } 
          if (Build.VERSION.SDK_INT >= 14)
            MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions()); 
        } 
        return;
      } 
    }
    
    public void setPlaybackToLocal(int param1Int) {
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null); 
      this.mVolumeType = 1;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
    }
    
    public void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat) {
      if (param1VolumeProviderCompat == null)
        throw new IllegalArgumentException("volumeProvider may not be null"); 
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null); 
      this.mVolumeType = 2;
      this.mVolumeProvider = param1VolumeProviderCompat;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
      param1VolumeProviderCompat.setCallback(this.mVolumeCallback);
    }
    
    public void setQueue(List<MediaSessionCompat.QueueItem> param1List) {
      this.mQueue = param1List;
      sendQueue(param1List);
    }
    
    public void setQueueTitle(CharSequence param1CharSequence) {
      this.mQueueTitle = param1CharSequence;
      sendQueueTitle(param1CharSequence);
    }
    
    public void setRatingType(int param1Int) {
      this.mRatingType = param1Int;
    }
    
    public void setSessionActivity(PendingIntent param1PendingIntent) {
      synchronized (this.mLock) {
        this.mSessionActivity = param1PendingIntent;
        return;
      } 
    }
    
    void setVolumeTo(int param1Int1, int param1Int2) {
      if (this.mVolumeType == 2) {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onSetVolumeTo(param1Int1); 
        return;
      } 
      this.mAudioManager.setStreamVolume(this.mLocalStream, param1Int1, param1Int2);
    }
    
    private static final class Command {
      public final String command;
      
      public final Bundle extras;
      
      public final ResultReceiver stub;
      
      public Command(String param2String, Bundle param2Bundle, ResultReceiver param2ResultReceiver) {
        this.command = param2String;
        this.extras = param2Bundle;
        this.stub = param2ResultReceiver;
      }
    }
    
    class MediaSessionStub extends IMediaSession.Stub {
      public void adjustVolume(int param2Int1, int param2Int2, String param2String) {
        MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(param2Int1, param2Int2);
      }
      
      public void fastForward() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(16);
      }
      
      public Bundle getExtras() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mExtras;
        } 
      }
      
      public long getFlags() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mFlags;
        } 
      }
      
      public PendingIntent getLaunchPendingIntent() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mSessionActivity;
        } 
      }
      
      public MediaMetadataCompat getMetadata() {
        return MediaSessionCompat.MediaSessionImplBase.this.mMetadata;
      }
      
      public String getPackageName() {
        return MediaSessionCompat.MediaSessionImplBase.this.mPackageName;
      }
      
      public PlaybackStateCompat getPlaybackState() {
        return MediaSessionCompat.MediaSessionImplBase.this.getStateWithUpdatedPosition();
      }
      
      public List<MediaSessionCompat.QueueItem> getQueue() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mQueue;
        } 
      }
      
      public CharSequence getQueueTitle() {
        return MediaSessionCompat.MediaSessionImplBase.this.mQueueTitle;
      }
      
      public int getRatingType() {
        return MediaSessionCompat.MediaSessionImplBase.this.mRatingType;
      }
      
      public String getTag() {
        return MediaSessionCompat.MediaSessionImplBase.this.mTag;
      }
      
      public ParcelableVolumeInfo getVolumeAttributes() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          int i = MediaSessionCompat.MediaSessionImplBase.this.mVolumeType;
          int j = MediaSessionCompat.MediaSessionImplBase.this.mLocalStream;
          VolumeProviderCompat volumeProviderCompat = MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider;
          if (i == 2) {
            int n = volumeProviderCompat.getVolumeControl();
            int i1 = volumeProviderCompat.getMaxVolume();
            int i2 = volumeProviderCompat.getCurrentVolume();
            return new ParcelableVolumeInfo(i, j, n, i1, i2);
          } 
          byte b = 2;
          int k = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(j);
          int m = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamVolume(j);
          return new ParcelableVolumeInfo(i, j, b, k, m);
        } 
      }
      
      public boolean isTransportControlEnabled() {
        return ((MediaSessionCompat.MediaSessionImplBase.this.mFlags & 0x2) != 0);
      }
      
      public void next() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(14);
      }
      
      public void pause() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(12);
      }
      
      public void play() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(7);
      }
      
      public void playFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(8, param2String, param2Bundle);
      }
      
      public void playFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(9, param2String, param2Bundle);
      }
      
      public void playFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(10, param2Uri, param2Bundle);
      }
      
      public void prepare() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(3);
      }
      
      public void prepareFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(4, param2String, param2Bundle);
      }
      
      public void prepareFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(5, param2String, param2Bundle);
      }
      
      public void prepareFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(6, param2Uri, param2Bundle);
      }
      
      public void previous() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(15);
      }
      
      public void rate(RatingCompat param2RatingCompat) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(19, param2RatingCompat);
      }
      
      public void registerCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        if (MediaSessionCompat.MediaSessionImplBase.this.mDestroyed) {
          try {
            param2IMediaControllerCallback.onSessionDestroyed();
          } catch (Exception exception) {}
          return;
        } 
        MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)exception);
      }
      
      public void rewind() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(17);
      }
      
      public void seekTo(long param2Long) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(18, Long.valueOf(param2Long));
      }
      
      public void sendCommand(String param2String, Bundle param2Bundle, MediaSessionCompat.ResultReceiverWrapper param2ResultReceiverWrapper) {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(1, new MediaSessionCompat.MediaSessionImplBase.Command(param2String, param2Bundle, param2ResultReceiverWrapper.mResultReceiver));
      }
      
      public void sendCustomAction(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(20, param2String, param2Bundle);
      }
      
      public boolean sendMediaButton(KeyEvent param2KeyEvent) {
        boolean bool;
        if ((MediaSessionCompat.MediaSessionImplBase.this.mFlags & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool)
          MediaSessionCompat.MediaSessionImplBase.this.postToHandler(21, param2KeyEvent); 
        return bool;
      }
      
      public void setVolumeTo(int param2Int1, int param2Int2, String param2String) {
        MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(param2Int1, param2Int2);
      }
      
      public void skipToQueueItem(long param2Long) {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(11, Long.valueOf(param2Long));
      }
      
      public void stop() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(13);
      }
      
      public void unregisterCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.unregister(param2IMediaControllerCallback);
      }
    }
    
    private class MessageHandler extends Handler {
      private static final int KEYCODE_MEDIA_PAUSE = 127;
      
      private static final int KEYCODE_MEDIA_PLAY = 126;
      
      private static final int MSG_ADJUST_VOLUME = 2;
      
      private static final int MSG_COMMAND = 1;
      
      private static final int MSG_CUSTOM_ACTION = 20;
      
      private static final int MSG_FAST_FORWARD = 16;
      
      private static final int MSG_MEDIA_BUTTON = 21;
      
      private static final int MSG_NEXT = 14;
      
      private static final int MSG_PAUSE = 12;
      
      private static final int MSG_PLAY = 7;
      
      private static final int MSG_PLAY_MEDIA_ID = 8;
      
      private static final int MSG_PLAY_SEARCH = 9;
      
      private static final int MSG_PLAY_URI = 10;
      
      private static final int MSG_PREPARE = 3;
      
      private static final int MSG_PREPARE_MEDIA_ID = 4;
      
      private static final int MSG_PREPARE_SEARCH = 5;
      
      private static final int MSG_PREPARE_URI = 6;
      
      private static final int MSG_PREVIOUS = 15;
      
      private static final int MSG_RATE = 19;
      
      private static final int MSG_REWIND = 17;
      
      private static final int MSG_SEEK_TO = 18;
      
      private static final int MSG_SET_VOLUME = 22;
      
      private static final int MSG_SKIP_TO_ITEM = 11;
      
      private static final int MSG_STOP = 13;
      
      public MessageHandler(Looper param2Looper) {
        super(param2Looper);
      }
      
      private void onMediaButtonEvent(KeyEvent param2KeyEvent, MediaSessionCompat.Callback param2Callback) {
        long l;
        boolean bool2;
        boolean bool3;
        boolean bool1 = true;
        if (param2KeyEvent == null || param2KeyEvent.getAction() != 0);
        if (MediaSessionCompat.MediaSessionImplBase.this.mState == null) {
          l = 0L;
        } else {
          l = MediaSessionCompat.MediaSessionImplBase.this.mState.getActions();
        } 
        switch (param2KeyEvent.getKeyCode()) {
          default:
            return;
          case 79:
          case 85:
            if (MediaSessionCompat.MediaSessionImplBase.this.mState != null && MediaSessionCompat.MediaSessionImplBase.this.mState.getState() == 3) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if ((0x204L & l) != 0L) {
              bool3 = true;
            } else {
              bool3 = false;
            } 
            if ((0x202L & l) == 0L)
              bool1 = false; 
            if (bool2 && bool1)
              param2Callback.onPause(); 
            break;
          case 126:
            if ((0x4L & l) != 0L)
              param2Callback.onPlay(); 
          case 127:
            if ((0x2L & l) != 0L)
              param2Callback.onPause(); 
          case 87:
            if ((0x20L & l) != 0L)
              param2Callback.onSkipToNext(); 
          case 88:
            if ((0x10L & l) != 0L)
              param2Callback.onSkipToPrevious(); 
          case 86:
            if ((0x1L & l) != 0L)
              param2Callback.onStop(); 
          case 90:
            if ((0x40L & l) != 0L)
              param2Callback.onFastForward(); 
          case 89:
            if ((0x8L & l) != 0L)
              param2Callback.onRewind(); 
        } 
        if (!bool2 && bool3)
          param2Callback.onPlay(); 
      }
      
      public void handleMessage(Message param2Message) {
        MediaSessionCompat.MediaSessionImplBase.Command command;
        KeyEvent keyEvent;
        Intent intent;
        MediaSessionCompat.Callback callback = MediaSessionCompat.MediaSessionImplBase.this.mCallback;
        if (callback == null);
        switch (param2Message.what) {
          default:
            return;
          case 1:
            command = (MediaSessionCompat.MediaSessionImplBase.Command)param2Message.obj;
            callback.onCommand(command.command, command.extras, command.stub);
          case 21:
            keyEvent = (KeyEvent)((Message)command).obj;
            intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
            if (!callback.onMediaButtonEvent(intent))
              onMediaButtonEvent(keyEvent, callback); 
          case 3:
            callback.onPrepare();
          case 4:
            callback.onPrepareFromMediaId((String)((Message)keyEvent).obj, keyEvent.getData());
          case 5:
            callback.onPrepareFromSearch((String)((Message)keyEvent).obj, keyEvent.getData());
          case 6:
            callback.onPrepareFromUri((Uri)((Message)keyEvent).obj, keyEvent.getData());
          case 7:
            callback.onPlay();
          case 8:
            callback.onPlayFromMediaId((String)((Message)keyEvent).obj, keyEvent.getData());
          case 9:
            callback.onPlayFromSearch((String)((Message)keyEvent).obj, keyEvent.getData());
          case 10:
            callback.onPlayFromUri((Uri)((Message)keyEvent).obj, keyEvent.getData());
          case 11:
            callback.onSkipToQueueItem(((Long)((Message)keyEvent).obj).longValue());
          case 12:
            callback.onPause();
          case 13:
            callback.onStop();
          case 14:
            callback.onSkipToNext();
          case 15:
            callback.onSkipToPrevious();
          case 16:
            callback.onFastForward();
          case 17:
            callback.onRewind();
          case 18:
            callback.onSeekTo(((Long)((Message)keyEvent).obj).longValue());
          case 19:
            callback.onSetRating((RatingCompat)((Message)keyEvent).obj);
          case 20:
            callback.onCustomAction((String)((Message)keyEvent).obj, keyEvent.getData());
          case 2:
            MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(((Integer)((Message)keyEvent).obj).intValue(), 0);
          case 22:
            break;
        } 
        MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(((Integer)((Message)keyEvent).obj).intValue(), 0);
      }
      
      public void post(int param2Int) {
        post(param2Int, (Object)null);
      }
      
      public void post(int param2Int, Object param2Object) {
        obtainMessage(param2Int, param2Object).sendToTarget();
      }
      
      public void post(int param2Int1, Object param2Object, int param2Int2) {
        obtainMessage(param2Int1, param2Int2, 0, param2Object).sendToTarget();
      }
      
      public void post(int param2Int, Object param2Object, Bundle param2Bundle) {
        param2Object = obtainMessage(param2Int, param2Object);
        param2Object.setData(param2Bundle);
        param2Object.sendToTarget();
      }
    }
  }
  
  class null extends VolumeProviderCompat.Callback {
    public void onVolumeChanged(VolumeProviderCompat param1VolumeProviderCompat) {
      if (this.this$0.mVolumeProvider == param1VolumeProviderCompat) {
        ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(this.this$0.mVolumeType, this.this$0.mLocalStream, param1VolumeProviderCompat.getVolumeControl(), param1VolumeProviderCompat.getMaxVolume(), param1VolumeProviderCompat.getCurrentVolume());
        this.this$0.sendVolumeInfoChanged(parcelableVolumeInfo);
      } 
    }
  }
  
  class null implements MediaSessionCompatApi19.Callback {
    public void onSeekTo(long param1Long) {
      this.this$0.postToHandler(18, Long.valueOf(param1Long));
    }
    
    public void onSetRating(Object param1Object) {
      this.this$0.postToHandler(19, RatingCompat.fromRating(param1Object));
    }
  }
  
  private static final class Command {
    public final String command;
    
    public final Bundle extras;
    
    public final ResultReceiver stub;
    
    public Command(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      this.command = param1String;
      this.extras = param1Bundle;
      this.stub = param1ResultReceiver;
    }
  }
  
  class MediaSessionStub extends IMediaSession.Stub {
    public void adjustVolume(int param1Int1, int param1Int2, String param1String) {
      this.this$0.adjustVolume(param1Int1, param1Int2);
    }
    
    public void fastForward() throws RemoteException {
      this.this$0.postToHandler(16);
    }
    
    public Bundle getExtras() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mExtras;
      } 
    }
    
    public long getFlags() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mFlags;
      } 
    }
    
    public PendingIntent getLaunchPendingIntent() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mSessionActivity;
      } 
    }
    
    public MediaMetadataCompat getMetadata() {
      return this.this$0.mMetadata;
    }
    
    public String getPackageName() {
      return this.this$0.mPackageName;
    }
    
    public PlaybackStateCompat getPlaybackState() {
      return this.this$0.getStateWithUpdatedPosition();
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mQueue;
      } 
    }
    
    public CharSequence getQueueTitle() {
      return this.this$0.mQueueTitle;
    }
    
    public int getRatingType() {
      return this.this$0.mRatingType;
    }
    
    public String getTag() {
      return this.this$0.mTag;
    }
    
    public ParcelableVolumeInfo getVolumeAttributes() {
      synchronized (this.this$0.mLock) {
        int i = this.this$0.mVolumeType;
        int j = this.this$0.mLocalStream;
        VolumeProviderCompat volumeProviderCompat = this.this$0.mVolumeProvider;
        if (i == 2) {
          int n = volumeProviderCompat.getVolumeControl();
          int i1 = volumeProviderCompat.getMaxVolume();
          int i2 = volumeProviderCompat.getCurrentVolume();
          return new ParcelableVolumeInfo(i, j, n, i1, i2);
        } 
        byte b = 2;
        int k = this.this$0.mAudioManager.getStreamMaxVolume(j);
        int m = this.this$0.mAudioManager.getStreamVolume(j);
        return new ParcelableVolumeInfo(i, j, b, k, m);
      } 
    }
    
    public boolean isTransportControlEnabled() {
      return ((this.this$0.mFlags & 0x2) != 0);
    }
    
    public void next() throws RemoteException {
      this.this$0.postToHandler(14);
    }
    
    public void pause() throws RemoteException {
      this.this$0.postToHandler(12);
    }
    
    public void play() throws RemoteException {
      this.this$0.postToHandler(7);
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(8, param1String, param1Bundle);
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(9, param1String, param1Bundle);
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(10, param1Uri, param1Bundle);
    }
    
    public void prepare() throws RemoteException {
      this.this$0.postToHandler(3);
    }
    
    public void prepareFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(4, param1String, param1Bundle);
    }
    
    public void prepareFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(5, param1String, param1Bundle);
    }
    
    public void prepareFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(6, param1Uri, param1Bundle);
    }
    
    public void previous() throws RemoteException {
      this.this$0.postToHandler(15);
    }
    
    public void rate(RatingCompat param1RatingCompat) throws RemoteException {
      this.this$0.postToHandler(19, param1RatingCompat);
    }
    
    public void registerCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      if (this.this$0.mDestroyed) {
        try {
          param1IMediaControllerCallback.onSessionDestroyed();
        } catch (Exception exception) {}
        return;
      } 
      this.this$0.mControllerCallbacks.register((IInterface)exception);
    }
    
    public void rewind() throws RemoteException {
      this.this$0.postToHandler(17);
    }
    
    public void seekTo(long param1Long) throws RemoteException {
      this.this$0.postToHandler(18, Long.valueOf(param1Long));
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, MediaSessionCompat.ResultReceiverWrapper param1ResultReceiverWrapper) {
      this.this$0.postToHandler(1, new MediaSessionCompat.MediaSessionImplBase.Command(param1String, param1Bundle, param1ResultReceiverWrapper.mResultReceiver));
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.postToHandler(20, param1String, param1Bundle);
    }
    
    public boolean sendMediaButton(KeyEvent param1KeyEvent) {
      boolean bool;
      if ((this.this$0.mFlags & 0x1) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        this.this$0.postToHandler(21, param1KeyEvent); 
      return bool;
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2, String param1String) {
      this.this$0.setVolumeTo(param1Int1, param1Int2);
    }
    
    public void skipToQueueItem(long param1Long) {
      this.this$0.postToHandler(11, Long.valueOf(param1Long));
    }
    
    public void stop() throws RemoteException {
      this.this$0.postToHandler(13);
    }
    
    public void unregisterCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      this.this$0.mControllerCallbacks.unregister(param1IMediaControllerCallback);
    }
  }
  
  private class MessageHandler extends Handler {
    private static final int KEYCODE_MEDIA_PAUSE = 127;
    
    private static final int KEYCODE_MEDIA_PLAY = 126;
    
    private static final int MSG_ADJUST_VOLUME = 2;
    
    private static final int MSG_COMMAND = 1;
    
    private static final int MSG_CUSTOM_ACTION = 20;
    
    private static final int MSG_FAST_FORWARD = 16;
    
    private static final int MSG_MEDIA_BUTTON = 21;
    
    private static final int MSG_NEXT = 14;
    
    private static final int MSG_PAUSE = 12;
    
    private static final int MSG_PLAY = 7;
    
    private static final int MSG_PLAY_MEDIA_ID = 8;
    
    private static final int MSG_PLAY_SEARCH = 9;
    
    private static final int MSG_PLAY_URI = 10;
    
    private static final int MSG_PREPARE = 3;
    
    private static final int MSG_PREPARE_MEDIA_ID = 4;
    
    private static final int MSG_PREPARE_SEARCH = 5;
    
    private static final int MSG_PREPARE_URI = 6;
    
    private static final int MSG_PREVIOUS = 15;
    
    private static final int MSG_RATE = 19;
    
    private static final int MSG_REWIND = 17;
    
    private static final int MSG_SEEK_TO = 18;
    
    private static final int MSG_SET_VOLUME = 22;
    
    private static final int MSG_SKIP_TO_ITEM = 11;
    
    private static final int MSG_STOP = 13;
    
    public MessageHandler(Looper param1Looper) {
      super(param1Looper);
    }
    
    private void onMediaButtonEvent(KeyEvent param1KeyEvent, MediaSessionCompat.Callback param1Callback) {
      long l;
      boolean bool2;
      boolean bool3;
      boolean bool1 = true;
      if (param1KeyEvent == null || param1KeyEvent.getAction() != 0);
      if (this.this$0.mState == null) {
        l = 0L;
      } else {
        l = this.this$0.mState.getActions();
      } 
      switch (param1KeyEvent.getKeyCode()) {
        default:
          return;
        case 79:
        case 85:
          if (this.this$0.mState != null && this.this$0.mState.getState() == 3) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          if ((0x204L & l) != 0L) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          if ((0x202L & l) == 0L)
            bool1 = false; 
          if (bool2 && bool1)
            param1Callback.onPause(); 
          break;
        case 126:
          if ((0x4L & l) != 0L)
            param1Callback.onPlay(); 
        case 127:
          if ((0x2L & l) != 0L)
            param1Callback.onPause(); 
        case 87:
          if ((0x20L & l) != 0L)
            param1Callback.onSkipToNext(); 
        case 88:
          if ((0x10L & l) != 0L)
            param1Callback.onSkipToPrevious(); 
        case 86:
          if ((0x1L & l) != 0L)
            param1Callback.onStop(); 
        case 90:
          if ((0x40L & l) != 0L)
            param1Callback.onFastForward(); 
        case 89:
          if ((0x8L & l) != 0L)
            param1Callback.onRewind(); 
      } 
      if (!bool2 && bool3)
        param1Callback.onPlay(); 
    }
    
    public void handleMessage(Message param1Message) {
      MediaSessionCompat.MediaSessionImplBase.Command command;
      KeyEvent keyEvent;
      Intent intent;
      MediaSessionCompat.Callback callback = this.this$0.mCallback;
      if (callback == null);
      switch (param1Message.what) {
        default:
          return;
        case 1:
          command = (MediaSessionCompat.MediaSessionImplBase.Command)param1Message.obj;
          callback.onCommand(command.command, command.extras, command.stub);
        case 21:
          keyEvent = (KeyEvent)((Message)command).obj;
          intent = new Intent("android.intent.action.MEDIA_BUTTON");
          intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
          if (!callback.onMediaButtonEvent(intent))
            onMediaButtonEvent(keyEvent, callback); 
        case 3:
          callback.onPrepare();
        case 4:
          callback.onPrepareFromMediaId((String)((Message)keyEvent).obj, keyEvent.getData());
        case 5:
          callback.onPrepareFromSearch((String)((Message)keyEvent).obj, keyEvent.getData());
        case 6:
          callback.onPrepareFromUri((Uri)((Message)keyEvent).obj, keyEvent.getData());
        case 7:
          callback.onPlay();
        case 8:
          callback.onPlayFromMediaId((String)((Message)keyEvent).obj, keyEvent.getData());
        case 9:
          callback.onPlayFromSearch((String)((Message)keyEvent).obj, keyEvent.getData());
        case 10:
          callback.onPlayFromUri((Uri)((Message)keyEvent).obj, keyEvent.getData());
        case 11:
          callback.onSkipToQueueItem(((Long)((Message)keyEvent).obj).longValue());
        case 12:
          callback.onPause();
        case 13:
          callback.onStop();
        case 14:
          callback.onSkipToNext();
        case 15:
          callback.onSkipToPrevious();
        case 16:
          callback.onFastForward();
        case 17:
          callback.onRewind();
        case 18:
          callback.onSeekTo(((Long)((Message)keyEvent).obj).longValue());
        case 19:
          callback.onSetRating((RatingCompat)((Message)keyEvent).obj);
        case 20:
          callback.onCustomAction((String)((Message)keyEvent).obj, keyEvent.getData());
        case 2:
          this.this$0.adjustVolume(((Integer)((Message)keyEvent).obj).intValue(), 0);
        case 22:
          break;
      } 
      this.this$0.setVolumeTo(((Integer)((Message)keyEvent).obj).intValue(), 0);
    }
    
    public void post(int param1Int) {
      post(param1Int, (Object)null);
    }
    
    public void post(int param1Int, Object param1Object) {
      obtainMessage(param1Int, param1Object).sendToTarget();
    }
    
    public void post(int param1Int1, Object param1Object, int param1Int2) {
      obtainMessage(param1Int1, param1Int2, 0, param1Object).sendToTarget();
    }
    
    public void post(int param1Int, Object param1Object, Bundle param1Bundle) {
      param1Object = obtainMessage(param1Int, param1Object);
      param1Object.setData(param1Bundle);
      param1Object.sendToTarget();
    }
  }
  
  public static interface OnActiveChangeListener {
    void onActiveChanged();
  }
  
  public static final class QueueItem implements Parcelable {
    public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>() {
        public MediaSessionCompat.QueueItem createFromParcel(Parcel param2Parcel) {
          return new MediaSessionCompat.QueueItem(param2Parcel);
        }
        
        public MediaSessionCompat.QueueItem[] newArray(int param2Int) {
          return new MediaSessionCompat.QueueItem[param2Int];
        }
      };
    
    public static final int UNKNOWN_ID = -1;
    
    private final MediaDescriptionCompat mDescription;
    
    private final long mId;
    
    private Object mItem;
    
    QueueItem(Parcel param1Parcel) {
      this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(param1Parcel);
      this.mId = param1Parcel.readLong();
    }
    
    public QueueItem(MediaDescriptionCompat param1MediaDescriptionCompat, long param1Long) {
      this(null, param1MediaDescriptionCompat, param1Long);
    }
    
    private QueueItem(Object param1Object, MediaDescriptionCompat param1MediaDescriptionCompat, long param1Long) {
      if (param1MediaDescriptionCompat == null)
        throw new IllegalArgumentException("Description cannot be null."); 
      if (param1Long == -1L)
        throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID"); 
      this.mDescription = param1MediaDescriptionCompat;
      this.mId = param1Long;
      this.mItem = param1Object;
    }
    
    public static QueueItem fromQueueItem(Object param1Object) {
      return (param1Object == null || Build.VERSION.SDK_INT < 21) ? null : new QueueItem(param1Object, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(param1Object)), MediaSessionCompatApi21.QueueItem.getQueueId(param1Object));
    }
    
    public static List<QueueItem> fromQueueItemList(List<?> param1List) {
      if (param1List == null || Build.VERSION.SDK_INT < 21)
        return null; 
      ArrayList<?> arrayList = new ArrayList();
      Iterator<?> iterator = param1List.iterator();
      while (true) {
        param1List = arrayList;
        if (iterator.hasNext()) {
          arrayList.add(fromQueueItem(iterator.next()));
          continue;
        } 
        return (List)param1List;
      } 
    }
    
    @Deprecated
    public static QueueItem obtain(Object param1Object) {
      return fromQueueItem(param1Object);
    }
    
    public int describeContents() {
      return 0;
    }
    
    public MediaDescriptionCompat getDescription() {
      return this.mDescription;
    }
    
    public long getQueueId() {
      return this.mId;
    }
    
    public Object getQueueItem() {
      if (this.mItem != null || Build.VERSION.SDK_INT < 21)
        return this.mItem; 
      this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
      return this.mItem;
    }
    
    public String toString() {
      return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      this.mDescription.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeLong(this.mId);
    }
  }
  
  static final class null implements Parcelable.Creator<QueueItem> {
    public MediaSessionCompat.QueueItem createFromParcel(Parcel param1Parcel) {
      return new MediaSessionCompat.QueueItem(param1Parcel);
    }
    
    public MediaSessionCompat.QueueItem[] newArray(int param1Int) {
      return new MediaSessionCompat.QueueItem[param1Int];
    }
  }
  
  static final class ResultReceiverWrapper implements Parcelable {
    public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator<ResultReceiverWrapper>() {
        public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel param2Parcel) {
          return new MediaSessionCompat.ResultReceiverWrapper(param2Parcel);
        }
        
        public MediaSessionCompat.ResultReceiverWrapper[] newArray(int param2Int) {
          return new MediaSessionCompat.ResultReceiverWrapper[param2Int];
        }
      };
    
    private ResultReceiver mResultReceiver;
    
    ResultReceiverWrapper(Parcel param1Parcel) {
      this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(param1Parcel);
    }
    
    public ResultReceiverWrapper(ResultReceiver param1ResultReceiver) {
      this.mResultReceiver = param1ResultReceiver;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      this.mResultReceiver.writeToParcel(param1Parcel, param1Int);
    }
  }
  
  static final class null implements Parcelable.Creator<ResultReceiverWrapper> {
    public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel param1Parcel) {
      return new MediaSessionCompat.ResultReceiverWrapper(param1Parcel);
    }
    
    public MediaSessionCompat.ResultReceiverWrapper[] newArray(int param1Int) {
      return new MediaSessionCompat.ResultReceiverWrapper[param1Int];
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface SessionFlags {}
  
  public static final class Token implements Parcelable {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public MediaSessionCompat.Token createFromParcel(Parcel param2Parcel) {
          Parcelable parcelable;
          if (Build.VERSION.SDK_INT >= 21) {
            parcelable = param2Parcel.readParcelable(null);
            return new MediaSessionCompat.Token(parcelable);
          } 
          IBinder iBinder = parcelable.readStrongBinder();
          return new MediaSessionCompat.Token(iBinder);
        }
        
        public MediaSessionCompat.Token[] newArray(int param2Int) {
          return new MediaSessionCompat.Token[param2Int];
        }
      };
    
    private final Object mInner;
    
    Token(Object param1Object) {
      this.mInner = param1Object;
    }
    
    public static Token fromToken(Object param1Object) {
      return (param1Object == null || Build.VERSION.SDK_INT < 21) ? null : new Token(MediaSessionCompatApi21.verifyToken(param1Object));
    }
    
    public int describeContents() {
      return 0;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this != param1Object) {
        if (!(param1Object instanceof Token))
          return false; 
        param1Object = param1Object;
        if (this.mInner == null) {
          if (((Token)param1Object).mInner != null)
            bool = false; 
          return bool;
        } 
        if (((Token)param1Object).mInner == null)
          return false; 
        bool = this.mInner.equals(((Token)param1Object).mInner);
      } 
      return bool;
    }
    
    public Object getToken() {
      return this.mInner;
    }
    
    public int hashCode() {
      return (this.mInner == null) ? 0 : this.mInner.hashCode();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      if (Build.VERSION.SDK_INT >= 21) {
        param1Parcel.writeParcelable((Parcelable)this.mInner, param1Int);
        return;
      } 
      param1Parcel.writeStrongBinder((IBinder)this.mInner);
    }
  }
  
  static final class null implements Parcelable.Creator<Token> {
    public MediaSessionCompat.Token createFromParcel(Parcel param1Parcel) {
      Parcelable parcelable;
      if (Build.VERSION.SDK_INT >= 21) {
        parcelable = param1Parcel.readParcelable(null);
        return new MediaSessionCompat.Token(parcelable);
      } 
      IBinder iBinder = parcelable.readStrongBinder();
      return new MediaSessionCompat.Token(iBinder);
    }
    
    public MediaSessionCompat.Token[] newArray(int param1Int) {
      return new MediaSessionCompat.Token[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/session/MediaSessionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */