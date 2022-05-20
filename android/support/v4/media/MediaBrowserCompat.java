package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class MediaBrowserCompat {
  static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
  
  public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
  
  public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
  
  static final String TAG = "MediaBrowserCompat";
  
  private final MediaBrowserImpl mImpl;
  
  public MediaBrowserCompat(Context paramContext, ComponentName paramComponentName, ConnectionCallback paramConnectionCallback, Bundle paramBundle) {
    if (Build.VERSION.SDK_INT >= 24 || BuildCompat.isAtLeastN()) {
      this.mImpl = new MediaBrowserImplApi24(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 23) {
      this.mImpl = new MediaBrowserImplApi23(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaBrowserImplApi21(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    } 
    this.mImpl = new MediaBrowserImplBase(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
  }
  
  public void connect() {
    this.mImpl.connect();
  }
  
  public void disconnect() {
    this.mImpl.disconnect();
  }
  
  @Nullable
  public Bundle getExtras() {
    return this.mImpl.getExtras();
  }
  
  public void getItem(@NonNull String paramString, @NonNull ItemCallback paramItemCallback) {
    this.mImpl.getItem(paramString, paramItemCallback);
  }
  
  @NonNull
  public String getRoot() {
    return this.mImpl.getRoot();
  }
  
  @NonNull
  public ComponentName getServiceComponent() {
    return this.mImpl.getServiceComponent();
  }
  
  @NonNull
  public MediaSessionCompat.Token getSessionToken() {
    return this.mImpl.getSessionToken();
  }
  
  public boolean isConnected() {
    return this.mImpl.isConnected();
  }
  
  public void subscribe(@NonNull String paramString, @NonNull Bundle paramBundle, @NonNull SubscriptionCallback paramSubscriptionCallback) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty"); 
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null"); 
    if (paramBundle == null)
      throw new IllegalArgumentException("options are null"); 
    this.mImpl.subscribe(paramString, paramBundle, paramSubscriptionCallback);
  }
  
  public void subscribe(@NonNull String paramString, @NonNull SubscriptionCallback paramSubscriptionCallback) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty"); 
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null"); 
    this.mImpl.subscribe(paramString, null, paramSubscriptionCallback);
  }
  
  public void unsubscribe(@NonNull String paramString) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty"); 
    this.mImpl.unsubscribe(paramString, null);
  }
  
  public void unsubscribe(@NonNull String paramString, @NonNull SubscriptionCallback paramSubscriptionCallback) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty"); 
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null"); 
    this.mImpl.unsubscribe(paramString, paramSubscriptionCallback);
  }
  
  private static class CallbackHandler extends Handler {
    private final WeakReference<MediaBrowserCompat.MediaBrowserServiceCallbackImpl> mCallbackImplRef;
    
    private WeakReference<Messenger> mCallbacksMessengerRef;
    
    CallbackHandler(MediaBrowserCompat.MediaBrowserServiceCallbackImpl param1MediaBrowserServiceCallbackImpl) {
      this.mCallbackImplRef = new WeakReference<MediaBrowserCompat.MediaBrowserServiceCallbackImpl>(param1MediaBrowserServiceCallbackImpl);
    }
    
    public void handleMessage(Message param1Message) {
      if (this.mCallbacksMessengerRef != null && this.mCallbacksMessengerRef.get() != null && this.mCallbackImplRef.get() != null) {
        Bundle bundle = param1Message.getData();
        bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        switch (param1Message.what) {
          default:
            Log.w("MediaBrowserCompat", "Unhandled message: " + param1Message + "\n  Client version: " + '\001' + "\n  Service version: " + param1Message.arg1);
            return;
          case 1:
            ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onServiceConnected(this.mCallbacksMessengerRef.get(), bundle.getString("data_media_item_id"), (MediaSessionCompat.Token)bundle.getParcelable("data_media_session_token"), bundle.getBundle("data_root_hints"));
            return;
          case 2:
            ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onConnectionFailed(this.mCallbacksMessengerRef.get());
            return;
          case 3:
            break;
        } 
        ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onLoadChildren(this.mCallbacksMessengerRef.get(), bundle.getString("data_media_item_id"), bundle.getParcelableArrayList("data_media_item_list"), bundle.getBundle("data_options"));
      } 
    }
    
    void setCallbacksMessenger(Messenger param1Messenger) {
      this.mCallbacksMessengerRef = new WeakReference<Messenger>(param1Messenger);
    }
  }
  
  public static class ConnectionCallback {
    ConnectionCallbackInternal mConnectionCallbackInternal;
    
    final Object mConnectionCallbackObj;
    
    public ConnectionCallback() {
      if (Build.VERSION.SDK_INT >= 21) {
        this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new StubApi21());
        return;
      } 
      this.mConnectionCallbackObj = null;
    }
    
    public void onConnected() {}
    
    public void onConnectionFailed() {}
    
    public void onConnectionSuspended() {}
    
    void setInternalConnectionCallback(ConnectionCallbackInternal param1ConnectionCallbackInternal) {
      this.mConnectionCallbackInternal = param1ConnectionCallbackInternal;
    }
    
    static interface ConnectionCallbackInternal {
      void onConnected();
      
      void onConnectionFailed();
      
      void onConnectionSuspended();
    }
    
    private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback {
      public void onConnected() {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnected(); 
        MediaBrowserCompat.ConnectionCallback.this.onConnected();
      }
      
      public void onConnectionFailed() {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed(); 
        MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
      }
      
      public void onConnectionSuspended() {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended(); 
        MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
      }
    }
  }
  
  static interface ConnectionCallbackInternal {
    void onConnected();
    
    void onConnectionFailed();
    
    void onConnectionSuspended();
  }
  
  private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback {
    public void onConnected() {
      if (this.this$0.mConnectionCallbackInternal != null)
        this.this$0.mConnectionCallbackInternal.onConnected(); 
      this.this$0.onConnected();
    }
    
    public void onConnectionFailed() {
      if (this.this$0.mConnectionCallbackInternal != null)
        this.this$0.mConnectionCallbackInternal.onConnectionFailed(); 
      this.this$0.onConnectionFailed();
    }
    
    public void onConnectionSuspended() {
      if (this.this$0.mConnectionCallbackInternal != null)
        this.this$0.mConnectionCallbackInternal.onConnectionSuspended(); 
      this.this$0.onConnectionSuspended();
    }
  }
  
  public static abstract class ItemCallback {
    final Object mItemCallbackObj;
    
    public ItemCallback() {
      if (Build.VERSION.SDK_INT >= 23) {
        this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new StubApi23());
        return;
      } 
      this.mItemCallbackObj = null;
    }
    
    public void onError(@NonNull String param1String) {}
    
    public void onItemLoaded(MediaBrowserCompat.MediaItem param1MediaItem) {}
    
    private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback {
      public void onError(@NonNull String param2String) {
        MediaBrowserCompat.ItemCallback.this.onError(param2String);
      }
      
      public void onItemLoaded(Parcel param2Parcel) {
        param2Parcel.setDataPosition(0);
        MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem)MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(param2Parcel);
        param2Parcel.recycle();
        MediaBrowserCompat.ItemCallback.this.onItemLoaded(mediaItem);
      }
    }
  }
  
  private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback {
    public void onError(@NonNull String param1String) {
      this.this$0.onError(param1String);
    }
    
    public void onItemLoaded(Parcel param1Parcel) {
      param1Parcel.setDataPosition(0);
      MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem)MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(param1Parcel);
      param1Parcel.recycle();
      this.this$0.onItemLoaded(mediaItem);
    }
  }
  
  private static class ItemReceiver extends ResultReceiver {
    private final MediaBrowserCompat.ItemCallback mCallback;
    
    private final String mMediaId;
    
    ItemReceiver(String param1String, MediaBrowserCompat.ItemCallback param1ItemCallback, Handler param1Handler) {
      super(param1Handler);
      this.mMediaId = param1String;
      this.mCallback = param1ItemCallback;
    }
    
    protected void onReceiveResult(int param1Int, Bundle param1Bundle) {
      if (param1Bundle != null)
        param1Bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader()); 
      if (param1Int != 0 || param1Bundle == null || !param1Bundle.containsKey("media_item")) {
        this.mCallback.onError(this.mMediaId);
        return;
      } 
      Parcelable parcelable = param1Bundle.getParcelable("media_item");
      if (parcelable == null || parcelable instanceof MediaBrowserCompat.MediaItem) {
        this.mCallback.onItemLoaded((MediaBrowserCompat.MediaItem)parcelable);
        return;
      } 
      this.mCallback.onError(this.mMediaId);
    }
  }
  
  static interface MediaBrowserImpl {
    void connect();
    
    void disconnect();
    
    @Nullable
    Bundle getExtras();
    
    void getItem(@NonNull String param1String, @NonNull MediaBrowserCompat.ItemCallback param1ItemCallback);
    
    @NonNull
    String getRoot();
    
    ComponentName getServiceComponent();
    
    @NonNull
    MediaSessionCompat.Token getSessionToken();
    
    boolean isConnected();
    
    void subscribe(@NonNull String param1String, Bundle param1Bundle, @NonNull MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback);
    
    void unsubscribe(@NonNull String param1String, MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback);
  }
  
  static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallback.ConnectionCallbackInternal {
    protected final Object mBrowserObj;
    
    protected Messenger mCallbacksMessenger;
    
    protected final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
    
    protected final Bundle mRootHints;
    
    protected MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
    
    private final ArrayMap<String, MediaBrowserCompat.Subscription> mSubscriptions = new ArrayMap();
    
    public MediaBrowserImplApi21(Context param1Context, ComponentName param1ComponentName, MediaBrowserCompat.ConnectionCallback param1ConnectionCallback, Bundle param1Bundle) {
      if (Build.VERSION.SDK_INT < 25) {
        Bundle bundle = param1Bundle;
        if (param1Bundle == null)
          bundle = new Bundle(); 
        bundle.putInt("extra_client_version", 1);
        this.mRootHints = new Bundle(bundle);
      } else {
        if (param1Bundle == null) {
          param1Bundle = null;
        } else {
          param1Bundle = new Bundle(param1Bundle);
        } 
        this.mRootHints = param1Bundle;
      } 
      param1ConnectionCallback.setInternalConnectionCallback(this);
      this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(param1Context, param1ComponentName, param1ConnectionCallback.mConnectionCallbackObj, this.mRootHints);
    }
    
    public void connect() {
      MediaBrowserCompatApi21.connect(this.mBrowserObj);
    }
    
    public void disconnect() {
      if (this.mServiceBinderWrapper != null && this.mCallbacksMessenger != null)
        try {
          this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
        } catch (RemoteException remoteException) {
          Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
        }  
      MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
    }
    
    @Nullable
    public Bundle getExtras() {
      return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
    }
    
    public void getItem(@NonNull final String mediaId, @NonNull final MediaBrowserCompat.ItemCallback cb) {
      if (TextUtils.isEmpty(mediaId))
        throw new IllegalArgumentException("mediaId is empty"); 
      if (cb == null)
        throw new IllegalArgumentException("cb is null"); 
      if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
        return;
      } 
      if (this.mServiceBinderWrapper == null) {
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
        return;
      } 
      MediaBrowserCompat.ItemReceiver itemReceiver = new MediaBrowserCompat.ItemReceiver(mediaId, cb, this.mHandler);
      try {
        this.mServiceBinderWrapper.getMediaItem(mediaId, itemReceiver, this.mCallbacksMessenger);
      } catch (RemoteException remoteException) {
        Log.i("MediaBrowserCompat", "Remote error getting media item: " + mediaId);
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
      } 
    }
    
    @NonNull
    public String getRoot() {
      return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
    }
    
    public ComponentName getServiceComponent() {
      return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
    }
    
    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
      return MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
    }
    
    public boolean isConnected() {
      return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
    }
    
    public void onConnected() {
      Bundle bundle = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
      if (bundle != null) {
        IBinder iBinder = BundleCompat.getBinder(bundle, "extra_messenger");
        if (iBinder != null) {
          this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(iBinder, this.mRootHints);
          this.mCallbacksMessenger = new Messenger(this.mHandler);
          this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
          try {
            this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
          } catch (RemoteException remoteException) {
            Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
          } 
        } 
      } 
    }
    
    public void onConnectionFailed() {}
    
    public void onConnectionFailed(Messenger param1Messenger) {}
    
    public void onConnectionSuspended() {
      this.mServiceBinderWrapper = null;
      this.mCallbacksMessenger = null;
      this.mHandler.setCallbacksMessenger((Messenger)null);
    }
    
    public void onLoadChildren(Messenger param1Messenger, String param1String, List<MediaBrowserCompat.MediaItem> param1List, Bundle param1Bundle) {
      if (this.mCallbacksMessenger == param1Messenger) {
        MediaBrowserCompat.Subscription subscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(param1String);
        if (subscription == null) {
          if (MediaBrowserCompat.DEBUG)
            Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + param1String); 
          return;
        } 
        MediaBrowserCompat.SubscriptionCallback subscriptionCallback = subscription.getCallback(param1Bundle);
        if (subscriptionCallback != null) {
          if (param1Bundle == null) {
            subscriptionCallback.onChildrenLoaded(param1String, param1List);
            return;
          } 
          subscriptionCallback.onChildrenLoaded(param1String, param1List, param1Bundle);
        } 
      } 
    }
    
    public void onServiceConnected(Messenger param1Messenger, String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) {}
    
    public void subscribe(@NonNull String param1String, Bundle param1Bundle, @NonNull MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      MediaBrowserCompat.Subscription subscription1 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(param1String);
      MediaBrowserCompat.Subscription subscription2 = subscription1;
      if (subscription1 == null) {
        subscription2 = new MediaBrowserCompat.Subscription();
        this.mSubscriptions.put(param1String, subscription2);
      } 
      param1SubscriptionCallback.setSubscription(subscription2);
      subscription2.putCallback(param1Bundle, param1SubscriptionCallback);
      if (this.mServiceBinderWrapper == null) {
        MediaBrowserCompatApi21.subscribe(this.mBrowserObj, param1String, param1SubscriptionCallback.mSubscriptionCallbackObj);
        return;
      } 
      try {
        this.mServiceBinderWrapper.addSubscription(param1String, param1SubscriptionCallback.mToken, param1Bundle, this.mCallbacksMessenger);
      } catch (RemoteException remoteException) {
        Log.i("MediaBrowserCompat", "Remote error subscribing media item: " + param1String);
      } 
    }
    
    public void unsubscribe(@NonNull String param1String, MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mSubscriptions : Landroid/support/v4/util/ArrayMap;
      //   4: aload_1
      //   5: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   8: checkcast android/support/v4/media/MediaBrowserCompat$Subscription
      //   11: astore_3
      //   12: aload_3
      //   13: ifnonnull -> 17
      //   16: return
      //   17: aload_0
      //   18: getfield mServiceBinderWrapper : Landroid/support/v4/media/MediaBrowserCompat$ServiceBinderWrapper;
      //   21: ifnonnull -> 147
      //   24: aload_2
      //   25: ifnonnull -> 59
      //   28: aload_0
      //   29: getfield mBrowserObj : Ljava/lang/Object;
      //   32: aload_1
      //   33: invokestatic unsubscribe : (Ljava/lang/Object;Ljava/lang/String;)V
      //   36: aload_3
      //   37: invokevirtual isEmpty : ()Z
      //   40: ifne -> 47
      //   43: aload_2
      //   44: ifnonnull -> 16
      //   47: aload_0
      //   48: getfield mSubscriptions : Landroid/support/v4/util/ArrayMap;
      //   51: aload_1
      //   52: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
      //   55: pop
      //   56: goto -> 16
      //   59: aload_3
      //   60: invokevirtual getCallbacks : ()Ljava/util/List;
      //   63: astore #4
      //   65: aload_3
      //   66: invokevirtual getOptionsList : ()Ljava/util/List;
      //   69: astore #5
      //   71: aload #4
      //   73: invokeinterface size : ()I
      //   78: iconst_1
      //   79: isub
      //   80: istore #6
      //   82: iload #6
      //   84: iflt -> 126
      //   87: aload #4
      //   89: iload #6
      //   91: invokeinterface get : (I)Ljava/lang/Object;
      //   96: aload_2
      //   97: if_acmpne -> 120
      //   100: aload #4
      //   102: iload #6
      //   104: invokeinterface remove : (I)Ljava/lang/Object;
      //   109: pop
      //   110: aload #5
      //   112: iload #6
      //   114: invokeinterface remove : (I)Ljava/lang/Object;
      //   119: pop
      //   120: iinc #6, -1
      //   123: goto -> 82
      //   126: aload #4
      //   128: invokeinterface size : ()I
      //   133: ifne -> 36
      //   136: aload_0
      //   137: getfield mBrowserObj : Ljava/lang/Object;
      //   140: aload_1
      //   141: invokestatic unsubscribe : (Ljava/lang/Object;Ljava/lang/String;)V
      //   144: goto -> 36
      //   147: aload_2
      //   148: ifnonnull -> 198
      //   151: aload_0
      //   152: getfield mServiceBinderWrapper : Landroid/support/v4/media/MediaBrowserCompat$ServiceBinderWrapper;
      //   155: aload_1
      //   156: aconst_null
      //   157: aload_0
      //   158: getfield mCallbacksMessenger : Landroid/os/Messenger;
      //   161: invokevirtual removeSubscription : (Ljava/lang/String;Landroid/os/IBinder;Landroid/os/Messenger;)V
      //   164: goto -> 36
      //   167: astore #4
      //   169: ldc 'MediaBrowserCompat'
      //   171: new java/lang/StringBuilder
      //   174: dup
      //   175: invokespecial <init> : ()V
      //   178: ldc_w 'removeSubscription failed with RemoteException parentId='
      //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   184: aload_1
      //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   188: invokevirtual toString : ()Ljava/lang/String;
      //   191: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   194: pop
      //   195: goto -> 36
      //   198: aload_3
      //   199: invokevirtual getCallbacks : ()Ljava/util/List;
      //   202: astore #5
      //   204: aload_3
      //   205: invokevirtual getOptionsList : ()Ljava/util/List;
      //   208: astore #4
      //   210: aload #5
      //   212: invokeinterface size : ()I
      //   217: iconst_1
      //   218: isub
      //   219: istore #6
      //   221: iload #6
      //   223: iflt -> 36
      //   226: aload #5
      //   228: iload #6
      //   230: invokeinterface get : (I)Ljava/lang/Object;
      //   235: aload_2
      //   236: if_acmpne -> 275
      //   239: aload_0
      //   240: getfield mServiceBinderWrapper : Landroid/support/v4/media/MediaBrowserCompat$ServiceBinderWrapper;
      //   243: aload_1
      //   244: aload_2
      //   245: invokestatic access$000 : (Landroid/support/v4/media/MediaBrowserCompat$SubscriptionCallback;)Landroid/os/IBinder;
      //   248: aload_0
      //   249: getfield mCallbacksMessenger : Landroid/os/Messenger;
      //   252: invokevirtual removeSubscription : (Ljava/lang/String;Landroid/os/IBinder;Landroid/os/Messenger;)V
      //   255: aload #5
      //   257: iload #6
      //   259: invokeinterface remove : (I)Ljava/lang/Object;
      //   264: pop
      //   265: aload #4
      //   267: iload #6
      //   269: invokeinterface remove : (I)Ljava/lang/Object;
      //   274: pop
      //   275: iinc #6, -1
      //   278: goto -> 221
      // Exception table:
      //   from	to	target	type
      //   151	164	167	android/os/RemoteException
      //   198	221	167	android/os/RemoteException
      //   226	275	167	android/os/RemoteException
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  static class MediaBrowserImplApi23 extends MediaBrowserImplApi21 {
    public MediaBrowserImplApi23(Context param1Context, ComponentName param1ComponentName, MediaBrowserCompat.ConnectionCallback param1ConnectionCallback, Bundle param1Bundle) {
      super(param1Context, param1ComponentName, param1ConnectionCallback, param1Bundle);
    }
    
    public void getItem(@NonNull String param1String, @NonNull MediaBrowserCompat.ItemCallback param1ItemCallback) {
      if (this.mServiceBinderWrapper == null) {
        MediaBrowserCompatApi23.getItem(this.mBrowserObj, param1String, param1ItemCallback.mItemCallbackObj);
        return;
      } 
      super.getItem(param1String, param1ItemCallback);
    }
  }
  
  static class MediaBrowserImplApi24 extends MediaBrowserImplApi23 {
    public MediaBrowserImplApi24(Context param1Context, ComponentName param1ComponentName, MediaBrowserCompat.ConnectionCallback param1ConnectionCallback, Bundle param1Bundle) {
      super(param1Context, param1ComponentName, param1ConnectionCallback, param1Bundle);
    }
    
    public void subscribe(@NonNull String param1String, @NonNull Bundle param1Bundle, @NonNull MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      if (param1Bundle == null) {
        MediaBrowserCompatApi21.subscribe(this.mBrowserObj, param1String, param1SubscriptionCallback.mSubscriptionCallbackObj);
        return;
      } 
      MediaBrowserCompatApi24.subscribe(this.mBrowserObj, param1String, param1Bundle, param1SubscriptionCallback.mSubscriptionCallbackObj);
    }
    
    public void unsubscribe(@NonNull String param1String, MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      if (param1SubscriptionCallback == null) {
        MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, param1String);
        return;
      } 
      MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, param1String, param1SubscriptionCallback.mSubscriptionCallbackObj);
    }
  }
  
  static class MediaBrowserImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl {
    private static final int CONNECT_STATE_CONNECTED = 2;
    
    static final int CONNECT_STATE_CONNECTING = 1;
    
    static final int CONNECT_STATE_DISCONNECTED = 0;
    
    static final int CONNECT_STATE_SUSPENDED = 3;
    
    final MediaBrowserCompat.ConnectionCallback mCallback;
    
    Messenger mCallbacksMessenger;
    
    final Context mContext;
    
    private Bundle mExtras;
    
    final MediaBrowserCompat.CallbackHandler mHandler;
    
    private MediaSessionCompat.Token mMediaSessionToken;
    
    final Bundle mRootHints;
    
    private String mRootId;
    
    MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
    
    final ComponentName mServiceComponent;
    
    MediaServiceConnection mServiceConnection;
    
    int mState;
    
    private final ArrayMap<String, MediaBrowserCompat.Subscription> mSubscriptions;
    
    public MediaBrowserImplBase(Context param1Context, ComponentName param1ComponentName, MediaBrowserCompat.ConnectionCallback param1ConnectionCallback, Bundle param1Bundle) {
      Bundle bundle;
      this.mHandler = new MediaBrowserCompat.CallbackHandler(this);
      this.mSubscriptions = new ArrayMap();
      this.mState = 0;
      if (param1Context == null)
        throw new IllegalArgumentException("context must not be null"); 
      if (param1ComponentName == null)
        throw new IllegalArgumentException("service component must not be null"); 
      if (param1ConnectionCallback == null)
        throw new IllegalArgumentException("connection callback must not be null"); 
      this.mContext = param1Context;
      this.mServiceComponent = param1ComponentName;
      this.mCallback = param1ConnectionCallback;
      if (param1Bundle == null) {
        param1Context = null;
      } else {
        bundle = new Bundle(param1Bundle);
      } 
      this.mRootHints = bundle;
    }
    
    private static String getStateLabel(int param1Int) {
      switch (param1Int) {
        default:
          return "UNKNOWN/" + param1Int;
        case 0:
          return "CONNECT_STATE_DISCONNECTED";
        case 1:
          return "CONNECT_STATE_CONNECTING";
        case 2:
          return "CONNECT_STATE_CONNECTED";
        case 3:
          break;
      } 
      return "CONNECT_STATE_SUSPENDED";
    }
    
    private boolean isCurrent(Messenger param1Messenger, String param1String) {
      if (this.mCallbacksMessenger != param1Messenger) {
        if (this.mState != 0)
          Log.i("MediaBrowserCompat", param1String + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this); 
        return false;
      } 
      return true;
    }
    
    public void connect() {
      if (this.mState != 0)
        throw new IllegalStateException("connect() called while not disconnected (state=" + getStateLabel(this.mState) + ")"); 
      if (MediaBrowserCompat.DEBUG && this.mServiceConnection != null)
        throw new RuntimeException("mServiceConnection should be null. Instead it is " + this.mServiceConnection); 
      if (this.mServiceBinderWrapper != null)
        throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + this.mServiceBinderWrapper); 
      if (this.mCallbacksMessenger != null)
        throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + this.mCallbacksMessenger); 
      this.mState = 1;
      Intent intent = new Intent("android.media.browse.MediaBrowserService");
      intent.setComponent(this.mServiceComponent);
      final MediaServiceConnection thisConnection = new MediaServiceConnection();
      this.mServiceConnection = mediaServiceConnection;
      boolean bool = false;
      try {
        boolean bool1 = this.mContext.bindService(intent, this.mServiceConnection, 1);
        bool = bool1;
      } catch (Exception exception) {
        Log.e("MediaBrowserCompat", "Failed binding to service " + this.mServiceComponent);
      } 
      if (!bool)
        this.mHandler.post(new Runnable() {
              public void run() {
                if (thisConnection == MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection) {
                  MediaBrowserCompat.MediaBrowserImplBase.this.forceCloseConnection();
                  MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                } 
              }
            }); 
      if (MediaBrowserCompat.DEBUG) {
        Log.d("MediaBrowserCompat", "connect...");
        dump();
      } 
    }
    
    public void disconnect() {
      if (this.mCallbacksMessenger != null)
        try {
          this.mServiceBinderWrapper.disconnect(this.mCallbacksMessenger);
        } catch (RemoteException remoteException) {
          Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.mServiceComponent);
        }  
      forceCloseConnection();
      if (MediaBrowserCompat.DEBUG) {
        Log.d("MediaBrowserCompat", "disconnect...");
        dump();
      } 
    }
    
    void dump() {
      Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
      Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.mServiceComponent);
      Log.d("MediaBrowserCompat", "  mCallback=" + this.mCallback);
      Log.d("MediaBrowserCompat", "  mRootHints=" + this.mRootHints);
      Log.d("MediaBrowserCompat", "  mState=" + getStateLabel(this.mState));
      Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.mServiceConnection);
      Log.d("MediaBrowserCompat", "  mServiceBinderWrapper=" + this.mServiceBinderWrapper);
      Log.d("MediaBrowserCompat", "  mCallbacksMessenger=" + this.mCallbacksMessenger);
      Log.d("MediaBrowserCompat", "  mRootId=" + this.mRootId);
      Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.mMediaSessionToken);
    }
    
    void forceCloseConnection() {
      if (this.mServiceConnection != null)
        this.mContext.unbindService(this.mServiceConnection); 
      this.mState = 0;
      this.mServiceConnection = null;
      this.mServiceBinderWrapper = null;
      this.mCallbacksMessenger = null;
      this.mHandler.setCallbacksMessenger((Messenger)null);
      this.mRootId = null;
      this.mMediaSessionToken = null;
    }
    
    @Nullable
    public Bundle getExtras() {
      if (!isConnected())
        throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")"); 
      return this.mExtras;
    }
    
    public void getItem(@NonNull final String mediaId, @NonNull final MediaBrowserCompat.ItemCallback cb) {
      if (TextUtils.isEmpty(mediaId))
        throw new IllegalArgumentException("mediaId is empty"); 
      if (cb == null)
        throw new IllegalArgumentException("cb is null"); 
      if (this.mState != 2) {
        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
        return;
      } 
      MediaBrowserCompat.ItemReceiver itemReceiver = new MediaBrowserCompat.ItemReceiver(mediaId, cb, this.mHandler);
      try {
        this.mServiceBinderWrapper.getMediaItem(mediaId, itemReceiver, this.mCallbacksMessenger);
      } catch (RemoteException remoteException) {
        Log.i("MediaBrowserCompat", "Remote error getting media item.");
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
      } 
    }
    
    @NonNull
    public String getRoot() {
      if (!isConnected())
        throw new IllegalStateException("getRoot() called while not connected(state=" + getStateLabel(this.mState) + ")"); 
      return this.mRootId;
    }
    
    @NonNull
    public ComponentName getServiceComponent() {
      if (!isConnected())
        throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")"); 
      return this.mServiceComponent;
    }
    
    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
      if (!isConnected())
        throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")"); 
      return this.mMediaSessionToken;
    }
    
    public boolean isConnected() {
      return (this.mState == 2);
    }
    
    public void onConnectionFailed(Messenger param1Messenger) {
      Log.e("MediaBrowserCompat", "onConnectFailed for " + this.mServiceComponent);
      if (isCurrent(param1Messenger, "onConnectFailed")) {
        if (this.mState != 1) {
          Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
          return;
        } 
        forceCloseConnection();
        this.mCallback.onConnectionFailed();
      } 
    }
    
    public void onLoadChildren(Messenger param1Messenger, String param1String, List<MediaBrowserCompat.MediaItem> param1List, Bundle param1Bundle) {
      if (isCurrent(param1Messenger, "onLoadChildren")) {
        if (MediaBrowserCompat.DEBUG)
          Log.d("MediaBrowserCompat", "onLoadChildren for " + this.mServiceComponent + " id=" + param1String); 
        MediaBrowserCompat.Subscription subscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(param1String);
        if (subscription == null) {
          if (MediaBrowserCompat.DEBUG)
            Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + param1String); 
          return;
        } 
        MediaBrowserCompat.SubscriptionCallback subscriptionCallback = subscription.getCallback(param1Bundle);
        if (subscriptionCallback != null) {
          if (param1Bundle == null) {
            subscriptionCallback.onChildrenLoaded(param1String, param1List);
            return;
          } 
          subscriptionCallback.onChildrenLoaded(param1String, param1List, param1Bundle);
        } 
      } 
    }
    
    public void onServiceConnected(Messenger param1Messenger, String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) {
      if (isCurrent(param1Messenger, "onConnect")) {
        if (this.mState != 1) {
          Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
          return;
        } 
        this.mRootId = param1String;
        this.mMediaSessionToken = param1Token;
        this.mExtras = param1Bundle;
        this.mState = 2;
        if (MediaBrowserCompat.DEBUG) {
          Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
          dump();
        } 
        this.mCallback.onConnected();
        try {
          Iterator<Map.Entry> iterator = this.mSubscriptions.entrySet().iterator();
          while (true) {
            if (iterator.hasNext()) {
              Map.Entry entry = iterator.next();
              param1String = (String)entry.getKey();
              MediaBrowserCompat.Subscription subscription = (MediaBrowserCompat.Subscription)entry.getValue();
              List<MediaBrowserCompat.SubscriptionCallback> list = subscription.getCallbacks();
              List<Bundle> list1 = subscription.getOptionsList();
              for (byte b = 0; b < list.size(); b++)
                this.mServiceBinderWrapper.addSubscription(param1String, (list.get(b)).mToken, list1.get(b), this.mCallbacksMessenger); 
              continue;
            } 
            return;
          } 
        } catch (RemoteException remoteException) {
          Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
        } 
      } 
    }
    
    public void subscribe(@NonNull String param1String, Bundle param1Bundle, @NonNull MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      MediaBrowserCompat.Subscription subscription1 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(param1String);
      MediaBrowserCompat.Subscription subscription2 = subscription1;
      if (subscription1 == null) {
        subscription2 = new MediaBrowserCompat.Subscription();
        this.mSubscriptions.put(param1String, subscription2);
      } 
      subscription2.putCallback(param1Bundle, param1SubscriptionCallback);
      if (this.mState == 2)
        try {
          this.mServiceBinderWrapper.addSubscription(param1String, param1SubscriptionCallback.mToken, param1Bundle, this.mCallbacksMessenger);
        } catch (RemoteException remoteException) {
          Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + param1String);
        }  
    }
    
    public void unsubscribe(@NonNull String param1String, MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mSubscriptions : Landroid/support/v4/util/ArrayMap;
      //   4: aload_1
      //   5: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   8: checkcast android/support/v4/media/MediaBrowserCompat$Subscription
      //   11: astore_3
      //   12: aload_3
      //   13: ifnonnull -> 17
      //   16: return
      //   17: aload_2
      //   18: ifnonnull -> 65
      //   21: aload_0
      //   22: getfield mState : I
      //   25: iconst_2
      //   26: if_icmpne -> 42
      //   29: aload_0
      //   30: getfield mServiceBinderWrapper : Landroid/support/v4/media/MediaBrowserCompat$ServiceBinderWrapper;
      //   33: aload_1
      //   34: aconst_null
      //   35: aload_0
      //   36: getfield mCallbacksMessenger : Landroid/os/Messenger;
      //   39: invokevirtual removeSubscription : (Ljava/lang/String;Landroid/os/IBinder;Landroid/os/Messenger;)V
      //   42: aload_3
      //   43: invokevirtual isEmpty : ()Z
      //   46: ifne -> 53
      //   49: aload_2
      //   50: ifnonnull -> 16
      //   53: aload_0
      //   54: getfield mSubscriptions : Landroid/support/v4/util/ArrayMap;
      //   57: aload_1
      //   58: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
      //   61: pop
      //   62: goto -> 16
      //   65: aload_3
      //   66: invokevirtual getCallbacks : ()Ljava/util/List;
      //   69: astore #4
      //   71: aload_3
      //   72: invokevirtual getOptionsList : ()Ljava/util/List;
      //   75: astore #5
      //   77: aload #4
      //   79: invokeinterface size : ()I
      //   84: iconst_1
      //   85: isub
      //   86: istore #6
      //   88: iload #6
      //   90: iflt -> 42
      //   93: aload #4
      //   95: iload #6
      //   97: invokeinterface get : (I)Ljava/lang/Object;
      //   102: aload_2
      //   103: if_acmpne -> 150
      //   106: aload_0
      //   107: getfield mState : I
      //   110: iconst_2
      //   111: if_icmpne -> 130
      //   114: aload_0
      //   115: getfield mServiceBinderWrapper : Landroid/support/v4/media/MediaBrowserCompat$ServiceBinderWrapper;
      //   118: aload_1
      //   119: aload_2
      //   120: invokestatic access$000 : (Landroid/support/v4/media/MediaBrowserCompat$SubscriptionCallback;)Landroid/os/IBinder;
      //   123: aload_0
      //   124: getfield mCallbacksMessenger : Landroid/os/Messenger;
      //   127: invokevirtual removeSubscription : (Ljava/lang/String;Landroid/os/IBinder;Landroid/os/Messenger;)V
      //   130: aload #4
      //   132: iload #6
      //   134: invokeinterface remove : (I)Ljava/lang/Object;
      //   139: pop
      //   140: aload #5
      //   142: iload #6
      //   144: invokeinterface remove : (I)Ljava/lang/Object;
      //   149: pop
      //   150: iinc #6, -1
      //   153: goto -> 88
      //   156: astore #4
      //   158: ldc 'MediaBrowserCompat'
      //   160: new java/lang/StringBuilder
      //   163: dup
      //   164: invokespecial <init> : ()V
      //   167: ldc_w 'removeSubscription failed with RemoteException parentId='
      //   170: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   173: aload_1
      //   174: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   177: invokevirtual toString : ()Ljava/lang/String;
      //   180: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   183: pop
      //   184: goto -> 42
      // Exception table:
      //   from	to	target	type
      //   21	42	156	android/os/RemoteException
      //   65	88	156	android/os/RemoteException
      //   93	130	156	android/os/RemoteException
      //   130	150	156	android/os/RemoteException
    }
    
    private class MediaServiceConnection implements ServiceConnection {
      private void postOrRun(Runnable param2Runnable) {
        if (Thread.currentThread() == MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
          param2Runnable.run();
          return;
        } 
        MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.post(param2Runnable);
      }
      
      boolean isCurrent(String param2String) {
        if (MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection != this) {
          if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 0)
            Log.i("MediaBrowserCompat", param2String + " for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection + " this=" + this); 
          return false;
        } 
        return true;
      }
      
      public void onServiceConnected(final ComponentName name, final IBinder binder) {
        postOrRun(new Runnable() {
              public void run() {
                if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + name + " binder=" + binder);
                  MediaBrowserCompat.MediaBrowserImplBase.this.dump();
                } 
                if (MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                  MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(binder, MediaBrowserCompat.MediaBrowserImplBase.this.mRootHints);
                  MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserCompat.MediaBrowserImplBase.this.mHandler);
                  MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
                  MediaBrowserCompat.MediaBrowserImplBase.this.mState = 1;
                  try {
                    if (MediaBrowserCompat.DEBUG) {
                      Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                      MediaBrowserCompat.MediaBrowserImplBase.this.dump();
                    } 
                    MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserCompat.MediaBrowserImplBase.this.mContext, MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
                  } catch (RemoteException remoteException) {
                    Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
                  } 
                } 
              }
            });
      }
      
      public void onServiceDisconnected(final ComponentName name) {
        postOrRun(new Runnable() {
              public void run() {
                if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + name + " this=" + this + " mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection);
                  MediaBrowserCompat.MediaBrowserImplBase.this.dump();
                } 
                if (MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                  MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                  MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = null;
                  MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger((Messenger)null);
                  MediaBrowserCompat.MediaBrowserImplBase.this.mState = 3;
                  MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                } 
              }
            });
      }
    }
    
    class null implements Runnable {
      public void run() {
        if (MediaBrowserCompat.DEBUG) {
          Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + name + " binder=" + binder);
          MediaBrowserCompat.MediaBrowserImplBase.this.dump();
        } 
        if (this.this$1.isCurrent("onServiceConnected")) {
          MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(binder, MediaBrowserCompat.MediaBrowserImplBase.this.mRootHints);
          MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserCompat.MediaBrowserImplBase.this.mHandler);
          MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
          MediaBrowserCompat.MediaBrowserImplBase.this.mState = 1;
          try {
            if (MediaBrowserCompat.DEBUG) {
              Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
              MediaBrowserCompat.MediaBrowserImplBase.this.dump();
            } 
            MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserCompat.MediaBrowserImplBase.this.mContext, MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
          } catch (RemoteException remoteException) {
            Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
          } 
        } 
      }
    }
    
    class null implements Runnable {
      public void run() {
        if (MediaBrowserCompat.DEBUG) {
          Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + name + " this=" + this + " mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection);
          MediaBrowserCompat.MediaBrowserImplBase.this.dump();
        } 
        if (this.this$1.isCurrent("onServiceDisconnected")) {
          MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = null;
          MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = null;
          MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger((Messenger)null);
          MediaBrowserCompat.MediaBrowserImplBase.this.mState = 3;
          MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
        } 
      }
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (thisConnection == this.this$0.mServiceConnection) {
        this.this$0.forceCloseConnection();
        this.this$0.mCallback.onConnectionFailed();
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  private class MediaServiceConnection implements ServiceConnection {
    private void postOrRun(Runnable param1Runnable) {
      if (Thread.currentThread() == this.this$0.mHandler.getLooper().getThread()) {
        param1Runnable.run();
        return;
      } 
      this.this$0.mHandler.post(param1Runnable);
    }
    
    boolean isCurrent(String param1String) {
      if (this.this$0.mServiceConnection != this) {
        if (this.this$0.mState != 0)
          Log.i("MediaBrowserCompat", param1String + " for " + this.this$0.mServiceComponent + " with mServiceConnection=" + this.this$0.mServiceConnection + " this=" + this); 
        return false;
      } 
      return true;
    }
    
    public void onServiceConnected(final ComponentName name, final IBinder binder) {
      postOrRun(new Runnable() {
            public void run() {
              if (MediaBrowserCompat.DEBUG) {
                Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + name + " binder=" + binder);
                this.this$1.this$0.dump();
              } 
              if (MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                this.this$1.this$0.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(binder, this.this$1.this$0.mRootHints);
                this.this$1.this$0.mCallbacksMessenger = new Messenger(this.this$1.this$0.mHandler);
                this.this$1.this$0.mHandler.setCallbacksMessenger(this.this$1.this$0.mCallbacksMessenger);
                this.this$1.this$0.mState = 1;
                try {
                  if (MediaBrowserCompat.DEBUG) {
                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                    this.this$1.this$0.dump();
                  } 
                  this.this$1.this$0.mServiceBinderWrapper.connect(this.this$1.this$0.mContext, this.this$1.this$0.mCallbacksMessenger);
                } catch (RemoteException remoteException) {
                  Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.this$1.this$0.mServiceComponent);
                } 
              } 
            }
          });
    }
    
    public void onServiceDisconnected(final ComponentName name) {
      postOrRun(new Runnable() {
            public void run() {
              if (MediaBrowserCompat.DEBUG) {
                Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + name + " this=" + this + " mServiceConnection=" + this.this$1.this$0.mServiceConnection);
                this.this$1.this$0.dump();
              } 
              if (MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                this.this$1.this$0.mServiceBinderWrapper = null;
                this.this$1.this$0.mCallbacksMessenger = null;
                this.this$1.this$0.mHandler.setCallbacksMessenger((Messenger)null);
                this.this$1.this$0.mState = 3;
                this.this$1.this$0.mCallback.onConnectionSuspended();
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (MediaBrowserCompat.DEBUG) {
        Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + name + " binder=" + binder);
        this.this$1.this$0.dump();
      } 
      if (this.this$1.isCurrent("onServiceConnected")) {
        this.this$1.this$0.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(binder, this.this$1.this$0.mRootHints);
        this.this$1.this$0.mCallbacksMessenger = new Messenger(this.this$1.this$0.mHandler);
        this.this$1.this$0.mHandler.setCallbacksMessenger(this.this$1.this$0.mCallbacksMessenger);
        this.this$1.this$0.mState = 1;
        try {
          if (MediaBrowserCompat.DEBUG) {
            Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
            this.this$1.this$0.dump();
          } 
          this.this$1.this$0.mServiceBinderWrapper.connect(this.this$1.this$0.mContext, this.this$1.this$0.mCallbacksMessenger);
        } catch (RemoteException remoteException) {
          Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.this$1.this$0.mServiceComponent);
        } 
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (MediaBrowserCompat.DEBUG) {
        Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + name + " this=" + this + " mServiceConnection=" + this.this$1.this$0.mServiceConnection);
        this.this$1.this$0.dump();
      } 
      if (this.this$1.isCurrent("onServiceDisconnected")) {
        this.this$1.this$0.mServiceBinderWrapper = null;
        this.this$1.this$0.mCallbacksMessenger = null;
        this.this$1.this$0.mHandler.setCallbacksMessenger((Messenger)null);
        this.this$1.this$0.mState = 3;
        this.this$1.this$0.mCallback.onConnectionSuspended();
      } 
    }
  }
  
  static interface MediaBrowserServiceCallbackImpl {
    void onConnectionFailed(Messenger param1Messenger);
    
    void onLoadChildren(Messenger param1Messenger, String param1String, List param1List, Bundle param1Bundle);
    
    void onServiceConnected(Messenger param1Messenger, String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle);
  }
  
  public static class MediaItem implements Parcelable {
    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>() {
        public MediaBrowserCompat.MediaItem createFromParcel(Parcel param2Parcel) {
          return new MediaBrowserCompat.MediaItem(param2Parcel);
        }
        
        public MediaBrowserCompat.MediaItem[] newArray(int param2Int) {
          return new MediaBrowserCompat.MediaItem[param2Int];
        }
      };
    
    public static final int FLAG_BROWSABLE = 1;
    
    public static final int FLAG_PLAYABLE = 2;
    
    private final MediaDescriptionCompat mDescription;
    
    private final int mFlags;
    
    MediaItem(Parcel param1Parcel) {
      this.mFlags = param1Parcel.readInt();
      this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(param1Parcel);
    }
    
    public MediaItem(@NonNull MediaDescriptionCompat param1MediaDescriptionCompat, int param1Int) {
      if (param1MediaDescriptionCompat == null)
        throw new IllegalArgumentException("description cannot be null"); 
      if (TextUtils.isEmpty(param1MediaDescriptionCompat.getMediaId()))
        throw new IllegalArgumentException("description must have a non-empty media id"); 
      this.mFlags = param1Int;
      this.mDescription = param1MediaDescriptionCompat;
    }
    
    public static MediaItem fromMediaItem(Object param1Object) {
      if (param1Object == null || Build.VERSION.SDK_INT < 21)
        return null; 
      int i = MediaBrowserCompatApi21.MediaItem.getFlags(param1Object);
      return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(param1Object)), i);
    }
    
    public static List<MediaItem> fromMediaItemList(List<?> param1List) {
      if (param1List == null || Build.VERSION.SDK_INT < 21)
        return null; 
      ArrayList<?> arrayList = new ArrayList(param1List.size());
      Iterator<?> iterator = param1List.iterator();
      while (true) {
        param1List = arrayList;
        if (iterator.hasNext()) {
          arrayList.add(fromMediaItem(iterator.next()));
          continue;
        } 
        return (List)param1List;
      } 
    }
    
    public int describeContents() {
      return 0;
    }
    
    @NonNull
    public MediaDescriptionCompat getDescription() {
      return this.mDescription;
    }
    
    public int getFlags() {
      return this.mFlags;
    }
    
    @NonNull
    public String getMediaId() {
      return this.mDescription.getMediaId();
    }
    
    public boolean isBrowsable() {
      return ((this.mFlags & 0x1) != 0);
    }
    
    public boolean isPlayable() {
      return ((this.mFlags & 0x2) != 0);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder("MediaItem{");
      stringBuilder.append("mFlags=").append(this.mFlags);
      stringBuilder.append(", mDescription=").append(this.mDescription);
      stringBuilder.append('}');
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mFlags);
      this.mDescription.writeToParcel(param1Parcel, param1Int);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface Flags {}
  }
  
  static final class null implements Parcelable.Creator<MediaItem> {
    public MediaBrowserCompat.MediaItem createFromParcel(Parcel param1Parcel) {
      return new MediaBrowserCompat.MediaItem(param1Parcel);
    }
    
    public MediaBrowserCompat.MediaItem[] newArray(int param1Int) {
      return new MediaBrowserCompat.MediaItem[param1Int];
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface Flags {}
  
  private static class ServiceBinderWrapper {
    private Messenger mMessenger;
    
    private Bundle mRootHints;
    
    public ServiceBinderWrapper(IBinder param1IBinder, Bundle param1Bundle) {
      this.mMessenger = new Messenger(param1IBinder);
      this.mRootHints = param1Bundle;
    }
    
    private void sendRequest(int param1Int, Bundle param1Bundle, Messenger param1Messenger) throws RemoteException {
      Message message = Message.obtain();
      message.what = param1Int;
      message.arg1 = 1;
      message.setData(param1Bundle);
      message.replyTo = param1Messenger;
      this.mMessenger.send(message);
    }
    
    void addSubscription(String param1String, IBinder param1IBinder, Bundle param1Bundle, Messenger param1Messenger) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putString("data_media_item_id", param1String);
      BundleCompat.putBinder(bundle, "data_callback_token", param1IBinder);
      bundle.putBundle("data_options", param1Bundle);
      sendRequest(3, bundle, param1Messenger);
    }
    
    void connect(Context param1Context, Messenger param1Messenger) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putString("data_package_name", param1Context.getPackageName());
      bundle.putBundle("data_root_hints", this.mRootHints);
      sendRequest(1, bundle, param1Messenger);
    }
    
    void disconnect(Messenger param1Messenger) throws RemoteException {
      sendRequest(2, null, param1Messenger);
    }
    
    void getMediaItem(String param1String, ResultReceiver param1ResultReceiver, Messenger param1Messenger) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putString("data_media_item_id", param1String);
      bundle.putParcelable("data_result_receiver", (Parcelable)param1ResultReceiver);
      sendRequest(5, bundle, param1Messenger);
    }
    
    void registerCallbackMessenger(Messenger param1Messenger) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putBundle("data_root_hints", this.mRootHints);
      sendRequest(6, bundle, param1Messenger);
    }
    
    void removeSubscription(String param1String, IBinder param1IBinder, Messenger param1Messenger) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putString("data_media_item_id", param1String);
      BundleCompat.putBinder(bundle, "data_callback_token", param1IBinder);
      sendRequest(4, bundle, param1Messenger);
    }
    
    void unregisterCallbackMessenger(Messenger param1Messenger) throws RemoteException {
      sendRequest(7, null, param1Messenger);
    }
  }
  
  private static class Subscription {
    private final List<MediaBrowserCompat.SubscriptionCallback> mCallbacks = new ArrayList<MediaBrowserCompat.SubscriptionCallback>();
    
    private final List<Bundle> mOptionsList = new ArrayList<Bundle>();
    
    public MediaBrowserCompat.SubscriptionCallback getCallback(Bundle param1Bundle) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: iload_2
      //   3: aload_0
      //   4: getfield mOptionsList : Ljava/util/List;
      //   7: invokeinterface size : ()I
      //   12: if_icmpge -> 57
      //   15: aload_0
      //   16: getfield mOptionsList : Ljava/util/List;
      //   19: iload_2
      //   20: invokeinterface get : (I)Ljava/lang/Object;
      //   25: checkcast android/os/Bundle
      //   28: aload_1
      //   29: invokestatic areSameOptions : (Landroid/os/Bundle;Landroid/os/Bundle;)Z
      //   32: ifeq -> 51
      //   35: aload_0
      //   36: getfield mCallbacks : Ljava/util/List;
      //   39: iload_2
      //   40: invokeinterface get : (I)Ljava/lang/Object;
      //   45: checkcast android/support/v4/media/MediaBrowserCompat$SubscriptionCallback
      //   48: astore_1
      //   49: aload_1
      //   50: areturn
      //   51: iinc #2, 1
      //   54: goto -> 2
      //   57: aconst_null
      //   58: astore_1
      //   59: goto -> 49
    }
    
    public List<MediaBrowserCompat.SubscriptionCallback> getCallbacks() {
      return this.mCallbacks;
    }
    
    public List<Bundle> getOptionsList() {
      return this.mOptionsList;
    }
    
    public boolean isEmpty() {
      return this.mCallbacks.isEmpty();
    }
    
    public void putCallback(Bundle param1Bundle, MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_3
      //   2: iload_3
      //   3: aload_0
      //   4: getfield mOptionsList : Ljava/util/List;
      //   7: invokeinterface size : ()I
      //   12: if_icmpge -> 54
      //   15: aload_0
      //   16: getfield mOptionsList : Ljava/util/List;
      //   19: iload_3
      //   20: invokeinterface get : (I)Ljava/lang/Object;
      //   25: checkcast android/os/Bundle
      //   28: aload_1
      //   29: invokestatic areSameOptions : (Landroid/os/Bundle;Landroid/os/Bundle;)Z
      //   32: ifeq -> 48
      //   35: aload_0
      //   36: getfield mCallbacks : Ljava/util/List;
      //   39: iload_3
      //   40: aload_2
      //   41: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
      //   46: pop
      //   47: return
      //   48: iinc #3, 1
      //   51: goto -> 2
      //   54: aload_0
      //   55: getfield mCallbacks : Ljava/util/List;
      //   58: aload_2
      //   59: invokeinterface add : (Ljava/lang/Object;)Z
      //   64: pop
      //   65: aload_0
      //   66: getfield mOptionsList : Ljava/util/List;
      //   69: aload_1
      //   70: invokeinterface add : (Ljava/lang/Object;)Z
      //   75: pop
      //   76: goto -> 47
    }
  }
  
  public static abstract class SubscriptionCallback {
    private final Object mSubscriptionCallbackObj;
    
    WeakReference<MediaBrowserCompat.Subscription> mSubscriptionRef;
    
    private final IBinder mToken;
    
    public SubscriptionCallback() {
      if (Build.VERSION.SDK_INT >= 24 || BuildCompat.isAtLeastN()) {
        this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new StubApi24());
        this.mToken = null;
        return;
      } 
      if (Build.VERSION.SDK_INT >= 21) {
        this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new StubApi21());
        this.mToken = (IBinder)new Binder();
        return;
      } 
      this.mSubscriptionCallbackObj = null;
      this.mToken = (IBinder)new Binder();
    }
    
    private void setSubscription(MediaBrowserCompat.Subscription param1Subscription) {
      this.mSubscriptionRef = new WeakReference<MediaBrowserCompat.Subscription>(param1Subscription);
    }
    
    public void onChildrenLoaded(@NonNull String param1String, List<MediaBrowserCompat.MediaItem> param1List) {}
    
    public void onChildrenLoaded(@NonNull String param1String, List<MediaBrowserCompat.MediaItem> param1List, @NonNull Bundle param1Bundle) {}
    
    public void onError(@NonNull String param1String) {}
    
    public void onError(@NonNull String param1String, @NonNull Bundle param1Bundle) {}
    
    private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback {
      List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> param2List, Bundle param2Bundle) {
        // Byte code:
        //   0: aload_1
        //   1: ifnonnull -> 8
        //   4: aconst_null
        //   5: astore_2
        //   6: aload_2
        //   7: areturn
        //   8: aload_2
        //   9: ldc 'android.media.browse.extra.PAGE'
        //   11: iconst_m1
        //   12: invokevirtual getInt : (Ljava/lang/String;I)I
        //   15: istore_3
        //   16: aload_2
        //   17: ldc 'android.media.browse.extra.PAGE_SIZE'
        //   19: iconst_m1
        //   20: invokevirtual getInt : (Ljava/lang/String;I)I
        //   23: istore #4
        //   25: iload_3
        //   26: iconst_m1
        //   27: if_icmpne -> 38
        //   30: aload_1
        //   31: astore_2
        //   32: iload #4
        //   34: iconst_m1
        //   35: if_icmpeq -> 6
        //   38: iload #4
        //   40: iload_3
        //   41: imul
        //   42: istore #5
        //   44: iload #5
        //   46: iload #4
        //   48: iadd
        //   49: istore #6
        //   51: iload_3
        //   52: iflt -> 72
        //   55: iload #4
        //   57: iconst_1
        //   58: if_icmplt -> 72
        //   61: iload #5
        //   63: aload_1
        //   64: invokeinterface size : ()I
        //   69: if_icmplt -> 79
        //   72: getstatic java/util/Collections.EMPTY_LIST : Ljava/util/List;
        //   75: astore_2
        //   76: goto -> 6
        //   79: iload #6
        //   81: istore_3
        //   82: iload #6
        //   84: aload_1
        //   85: invokeinterface size : ()I
        //   90: if_icmple -> 100
        //   93: aload_1
        //   94: invokeinterface size : ()I
        //   99: istore_3
        //   100: aload_1
        //   101: iload #5
        //   103: iload_3
        //   104: invokeinterface subList : (II)Ljava/util/List;
        //   109: astore_2
        //   110: goto -> 6
      }
      
      public void onChildrenLoaded(@NonNull String param2String, List<?> param2List) {
        MediaBrowserCompat.Subscription subscription;
        if (MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef == null) {
          subscription = null;
        } else {
          subscription = MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef.get();
        } 
        if (subscription == null) {
          MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(param2String, MediaBrowserCompat.MediaItem.fromMediaItemList(param2List));
          return;
        } 
        param2List = MediaBrowserCompat.MediaItem.fromMediaItemList(param2List);
        List<MediaBrowserCompat.SubscriptionCallback> list1 = subscription.getCallbacks();
        List<Bundle> list = subscription.getOptionsList();
        byte b = 0;
        while (true) {
          if (b < list1.size()) {
            Bundle bundle = list.get(b);
            if (bundle == null) {
              MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(param2String, (List)param2List);
            } else {
              MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(param2String, applyOptions((List)param2List, bundle), bundle);
            } 
            b++;
            continue;
          } 
          return;
        } 
      }
      
      public void onError(@NonNull String param2String) {
        MediaBrowserCompat.SubscriptionCallback.this.onError(param2String);
      }
    }
    
    private class StubApi24 extends StubApi21 implements MediaBrowserCompatApi24.SubscriptionCallback {
      public void onChildrenLoaded(@NonNull String param2String, List<?> param2List, @NonNull Bundle param2Bundle) {
        MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(param2String, MediaBrowserCompat.MediaItem.fromMediaItemList(param2List), param2Bundle);
      }
      
      public void onError(@NonNull String param2String, @NonNull Bundle param2Bundle) {
        MediaBrowserCompat.SubscriptionCallback.this.onError(param2String, param2Bundle);
      }
    }
  }
  
  private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback {
    List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> param1List, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_1
      //   1: ifnonnull -> 8
      //   4: aconst_null
      //   5: astore_2
      //   6: aload_2
      //   7: areturn
      //   8: aload_2
      //   9: ldc 'android.media.browse.extra.PAGE'
      //   11: iconst_m1
      //   12: invokevirtual getInt : (Ljava/lang/String;I)I
      //   15: istore_3
      //   16: aload_2
      //   17: ldc 'android.media.browse.extra.PAGE_SIZE'
      //   19: iconst_m1
      //   20: invokevirtual getInt : (Ljava/lang/String;I)I
      //   23: istore #4
      //   25: iload_3
      //   26: iconst_m1
      //   27: if_icmpne -> 38
      //   30: aload_1
      //   31: astore_2
      //   32: iload #4
      //   34: iconst_m1
      //   35: if_icmpeq -> 6
      //   38: iload #4
      //   40: iload_3
      //   41: imul
      //   42: istore #5
      //   44: iload #5
      //   46: iload #4
      //   48: iadd
      //   49: istore #6
      //   51: iload_3
      //   52: iflt -> 72
      //   55: iload #4
      //   57: iconst_1
      //   58: if_icmplt -> 72
      //   61: iload #5
      //   63: aload_1
      //   64: invokeinterface size : ()I
      //   69: if_icmplt -> 79
      //   72: getstatic java/util/Collections.EMPTY_LIST : Ljava/util/List;
      //   75: astore_2
      //   76: goto -> 6
      //   79: iload #6
      //   81: istore_3
      //   82: iload #6
      //   84: aload_1
      //   85: invokeinterface size : ()I
      //   90: if_icmple -> 100
      //   93: aload_1
      //   94: invokeinterface size : ()I
      //   99: istore_3
      //   100: aload_1
      //   101: iload #5
      //   103: iload_3
      //   104: invokeinterface subList : (II)Ljava/util/List;
      //   109: astore_2
      //   110: goto -> 6
    }
    
    public void onChildrenLoaded(@NonNull String param1String, List<?> param1List) {
      MediaBrowserCompat.Subscription subscription;
      if (this.this$0.mSubscriptionRef == null) {
        subscription = null;
      } else {
        subscription = this.this$0.mSubscriptionRef.get();
      } 
      if (subscription == null) {
        this.this$0.onChildrenLoaded(param1String, MediaBrowserCompat.MediaItem.fromMediaItemList(param1List));
        return;
      } 
      param1List = MediaBrowserCompat.MediaItem.fromMediaItemList(param1List);
      List<MediaBrowserCompat.SubscriptionCallback> list1 = subscription.getCallbacks();
      List<Bundle> list = subscription.getOptionsList();
      byte b = 0;
      while (true) {
        if (b < list1.size()) {
          Bundle bundle = list.get(b);
          if (bundle == null) {
            this.this$0.onChildrenLoaded(param1String, (List)param1List);
          } else {
            this.this$0.onChildrenLoaded(param1String, applyOptions((List)param1List, bundle), bundle);
          } 
          b++;
          continue;
        } 
        return;
      } 
    }
    
    public void onError(@NonNull String param1String) {
      this.this$0.onError(param1String);
    }
  }
  
  private class StubApi24 extends SubscriptionCallback.StubApi21 implements MediaBrowserCompatApi24.SubscriptionCallback {
    StubApi24() {
      super((MediaBrowserCompat.SubscriptionCallback)MediaBrowserCompat.this);
    }
    
    public void onChildrenLoaded(@NonNull String param1String, List<?> param1List, @NonNull Bundle param1Bundle) {
      this.this$0.onChildrenLoaded(param1String, MediaBrowserCompat.MediaItem.fromMediaItemList(param1List), param1Bundle);
    }
    
    public void onError(@NonNull String param1String, @NonNull Bundle param1Bundle) {
      this.this$0.onError(param1String, param1Bundle);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/MediaBrowserCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */