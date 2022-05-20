package android.support.v4.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class MediaBrowserServiceCompat extends Service {
  static final boolean DEBUG = Log.isLoggable("MBServiceCompat", 3);
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final String KEY_MEDIA_ITEM = "media_item";
  
  static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
  
  static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
  
  public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
  
  static final String TAG = "MBServiceCompat";
  
  final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
  
  ConnectionRecord mCurConnection;
  
  final ServiceHandler mHandler = new ServiceHandler();
  
  private MediaBrowserServiceImpl mImpl;
  
  MediaSessionCompat.Token mSession;
  
  void addSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder, Bundle paramBundle) {
    // Byte code:
    //   0: aload_2
    //   1: getfield subscriptions : Ljava/util/HashMap;
    //   4: aload_1
    //   5: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   8: checkcast java/util/List
    //   11: astore #5
    //   13: aload #5
    //   15: astore #6
    //   17: aload #5
    //   19: ifnonnull -> 31
    //   22: new java/util/ArrayList
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: astore #6
    //   31: aload #6
    //   33: invokeinterface iterator : ()Ljava/util/Iterator;
    //   38: astore #5
    //   40: aload #5
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 88
    //   50: aload #5
    //   52: invokeinterface next : ()Ljava/lang/Object;
    //   57: checkcast android/support/v4/util/Pair
    //   60: astore #7
    //   62: aload_3
    //   63: aload #7
    //   65: getfield first : Ljava/lang/Object;
    //   68: if_acmpne -> 40
    //   71: aload #4
    //   73: aload #7
    //   75: getfield second : Ljava/lang/Object;
    //   78: checkcast android/os/Bundle
    //   81: invokestatic areSameOptions : (Landroid/os/Bundle;Landroid/os/Bundle;)Z
    //   84: ifeq -> 40
    //   87: return
    //   88: aload #6
    //   90: new android/support/v4/util/Pair
    //   93: dup
    //   94: aload_3
    //   95: aload #4
    //   97: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   100: invokeinterface add : (Ljava/lang/Object;)Z
    //   105: pop
    //   106: aload_2
    //   107: getfield subscriptions : Ljava/util/HashMap;
    //   110: aload_1
    //   111: aload #6
    //   113: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   116: pop
    //   117: aload_0
    //   118: aload_1
    //   119: aload_2
    //   120: aload #4
    //   122: invokevirtual performLoadChildren : (Ljava/lang/String;Landroid/support/v4/media/MediaBrowserServiceCompat$ConnectionRecord;Landroid/os/Bundle;)V
    //   125: goto -> 87
  }
  
  List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> paramList, Bundle paramBundle) {
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
    //   81: istore #4
    //   83: iload #6
    //   85: aload_1
    //   86: invokeinterface size : ()I
    //   91: if_icmple -> 102
    //   94: aload_1
    //   95: invokeinterface size : ()I
    //   100: istore #4
    //   102: aload_1
    //   103: iload #5
    //   105: iload #4
    //   107: invokeinterface subList : (II)Ljava/util/List;
    //   112: astore_2
    //   113: goto -> 6
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  public final Bundle getBrowserRootHints() {
    return this.mImpl.getBrowserRootHints();
  }
  
  @Nullable
  public MediaSessionCompat.Token getSessionToken() {
    return this.mSession;
  }
  
  boolean isValidPackage(String paramString, int paramInt) {
    boolean bool = false;
    if (paramString == null)
      return bool; 
    String[] arrayOfString = getPackageManager().getPackagesForUid(paramInt);
    int i = arrayOfString.length;
    paramInt = 0;
    while (true) {
      boolean bool1 = bool;
      if (paramInt < i) {
        if (arrayOfString[paramInt].equals(paramString))
          return true; 
        paramInt++;
        continue;
      } 
      return bool1;
    } 
  }
  
  public void notifyChildrenChanged(@NonNull String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged"); 
    this.mImpl.notifyChildrenChanged(paramString, null);
  }
  
  public void notifyChildrenChanged(@NonNull String paramString, @NonNull Bundle paramBundle) {
    if (paramString == null)
      throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged"); 
    if (paramBundle == null)
      throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged"); 
    this.mImpl.notifyChildrenChanged(paramString, paramBundle);
  }
  
  public IBinder onBind(Intent paramIntent) {
    return this.mImpl.onBind(paramIntent);
  }
  
  public void onCreate() {
    super.onCreate();
    if (Build.VERSION.SDK_INT >= 24 || BuildCompat.isAtLeastN()) {
      this.mImpl = new MediaBrowserServiceImplApi24();
    } else if (Build.VERSION.SDK_INT >= 23) {
      this.mImpl = new MediaBrowserServiceImplApi23();
    } else if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaBrowserServiceImplApi21();
    } else {
      this.mImpl = new MediaBrowserServiceImplBase();
    } 
    this.mImpl.onCreate();
  }
  
  @Nullable
  public abstract BrowserRoot onGetRoot(@NonNull String paramString, int paramInt, @Nullable Bundle paramBundle);
  
  public abstract void onLoadChildren(@NonNull String paramString, @NonNull Result<List<MediaBrowserCompat.MediaItem>> paramResult);
  
  public void onLoadChildren(@NonNull String paramString, @NonNull Result<List<MediaBrowserCompat.MediaItem>> paramResult, @NonNull Bundle paramBundle) {
    paramResult.setFlags(1);
    onLoadChildren(paramString, paramResult);
  }
  
  public void onLoadItem(String paramString, Result<MediaBrowserCompat.MediaItem> paramResult) {
    paramResult.setFlags(2);
    paramResult.sendResult(null);
  }
  
  void performLoadChildren(final String parentId, final ConnectionRecord connection, final Bundle options) {
    Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(parentId) {
        void onResultSent(List<MediaBrowserCompat.MediaItem> param1List, int param1Int) {
          if (MediaBrowserServiceCompat.this.mConnections.get(connection.callbacks.asBinder()) != connection) {
            if (MediaBrowserServiceCompat.DEBUG)
              Log.d("MBServiceCompat", "Not sending onLoadChildren result for connection that has been disconnected. pkg=" + connection.pkg + " id=" + parentId); 
            return;
          } 
          if ((param1Int & 0x1) != 0)
            param1List = MediaBrowserServiceCompat.this.applyOptions(param1List, options); 
          try {
            connection.callbacks.onLoadChildren(parentId, param1List, options);
          } catch (RemoteException remoteException) {
            Log.w("MBServiceCompat", "Calling onLoadChildren() failed for id=" + parentId + " package=" + connection.pkg);
          } 
        }
      };
    this.mCurConnection = connection;
    if (options == null) {
      onLoadChildren(parentId, result);
    } else {
      onLoadChildren(parentId, result, options);
    } 
    this.mCurConnection = null;
    if (!result.isDone())
      throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + connection.pkg + " id=" + parentId); 
  }
  
  void performLoadItem(String paramString, ConnectionRecord paramConnectionRecord, final ResultReceiver receiver) {
    Result<MediaBrowserCompat.MediaItem> result = new Result<MediaBrowserCompat.MediaItem>(paramString) {
        void onResultSent(MediaBrowserCompat.MediaItem param1MediaItem, int param1Int) {
          if ((param1Int & 0x2) != 0) {
            receiver.send(-1, null);
            return;
          } 
          Bundle bundle = new Bundle();
          bundle.putParcelable("media_item", param1MediaItem);
          receiver.send(0, bundle);
        }
      };
    this.mCurConnection = paramConnectionRecord;
    onLoadItem(paramString, result);
    this.mCurConnection = null;
    if (!result.isDone())
      throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + paramString); 
  }
  
  boolean removeSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder) {
    if (paramIBinder == null)
      return (paramConnectionRecord.subscriptions.remove(paramString) != null); 
    boolean bool1 = false;
    boolean bool2 = false;
    List list = paramConnectionRecord.subscriptions.get(paramString);
    if (list != null) {
      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        if (paramIBinder == ((Pair)iterator.next()).first) {
          bool2 = true;
          iterator.remove();
        } 
      } 
      bool1 = bool2;
      if (list.size() == 0) {
        paramConnectionRecord.subscriptions.remove(paramString);
        bool1 = bool2;
      } 
    } 
    return bool1;
  }
  
  public void setSessionToken(MediaSessionCompat.Token paramToken) {
    if (paramToken == null)
      throw new IllegalArgumentException("Session token may not be null."); 
    if (this.mSession != null)
      throw new IllegalStateException("The session token has already been set."); 
    this.mSession = paramToken;
    this.mImpl.setSessionToken(paramToken);
  }
  
  public static final class BrowserRoot {
    public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
    
    public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
    
    public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
    
    public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
    
    private final Bundle mExtras;
    
    private final String mRootId;
    
    public BrowserRoot(@NonNull String param1String, @Nullable Bundle param1Bundle) {
      if (param1String == null)
        throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead."); 
      this.mRootId = param1String;
      this.mExtras = param1Bundle;
    }
    
    public Bundle getExtras() {
      return this.mExtras;
    }
    
    public String getRootId() {
      return this.mRootId;
    }
  }
  
  private class ConnectionRecord {
    MediaBrowserServiceCompat.ServiceCallbacks callbacks;
    
    String pkg;
    
    MediaBrowserServiceCompat.BrowserRoot root;
    
    Bundle rootHints;
    
    HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions = new HashMap<String, List<Pair<IBinder, Bundle>>>();
  }
  
  static interface MediaBrowserServiceImpl {
    Bundle getBrowserRootHints();
    
    void notifyChildrenChanged(String param1String, Bundle param1Bundle);
    
    IBinder onBind(Intent param1Intent);
    
    void onCreate();
    
    void setSessionToken(MediaSessionCompat.Token param1Token);
  }
  
  class MediaBrowserServiceImplApi21 implements MediaBrowserServiceImpl, MediaBrowserServiceCompatApi21.ServiceCompatProxy {
    Messenger mMessenger;
    
    Object mServiceObj;
    
    public Bundle getBrowserRootHints() {
      Bundle bundle = null;
      if (this.mMessenger != null) {
        if (MediaBrowserServiceCompat.this.mCurConnection == null)
          throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods"); 
        if (MediaBrowserServiceCompat.this.mCurConnection.rootHints != null)
          bundle = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints); 
      } 
      return bundle;
    }
    
    public void notifyChildrenChanged(final String parentId, final Bundle options) {
      if (this.mMessenger == null) {
        MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, parentId);
        return;
      } 
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
                MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
                List list = connectionRecord.subscriptions.get(parentId);
                if (list != null)
                  for (Pair pair : list) {
                    if (MediaBrowserCompatUtils.hasDuplicatedItems(options, (Bundle)pair.second))
                      MediaBrowserServiceCompat.this.performLoadChildren(parentId, connectionRecord, (Bundle)pair.second); 
                  }  
              } 
            }
          });
    }
    
    public IBinder onBind(Intent param1Intent) {
      return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, param1Intent);
    }
    
    public void onCreate() {
      this.mServiceObj = MediaBrowserServiceCompatApi21.createService((Context)MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }
    
    public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String param1String, int param1Int, Bundle param1Bundle) {
      Bundle bundle1;
      Bundle bundle2 = null;
      Bundle bundle3 = bundle2;
      if (param1Bundle != null) {
        bundle3 = bundle2;
        if (param1Bundle.getInt("extra_client_version", 0) != 0) {
          param1Bundle.remove("extra_client_version");
          this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
          bundle3 = new Bundle();
          bundle3.putInt("extra_service_version", 1);
          BundleCompat.putBinder(bundle3, "extra_messenger", this.mMessenger.getBinder());
        } 
      } 
      MediaBrowserServiceCompat.BrowserRoot browserRoot = MediaBrowserServiceCompat.this.onGetRoot(param1String, param1Int, param1Bundle);
      if (browserRoot == null)
        return null; 
      if (bundle3 == null) {
        bundle1 = browserRoot.getExtras();
      } else {
        bundle1 = bundle3;
        if (browserRoot.getExtras() != null) {
          bundle3.putAll(browserRoot.getExtras());
          bundle1 = bundle3;
        } 
      } 
      return new MediaBrowserServiceCompatApi21.BrowserRoot(browserRoot.getRootId(), bundle1);
    }
    
    public void onLoadChildren(String param1String, final MediaBrowserServiceCompatApi21.ResultWrapper<List<Parcel>> resultWrapper) {
      MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = new MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>(param1String) {
          public void detach() {
            resultWrapper.detach();
          }
          
          void onResultSent(List<MediaBrowserCompat.MediaItem> param2List, int param2Int) {
            Parcel parcel;
            ArrayList<Parcel> arrayList = null;
            if (param2List != null) {
              ArrayList<Parcel> arrayList1 = new ArrayList();
              Iterator<MediaBrowserCompat.MediaItem> iterator = param2List.iterator();
              while (true) {
                arrayList = arrayList1;
                if (iterator.hasNext()) {
                  MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                  parcel = Parcel.obtain();
                  mediaItem.writeToParcel(parcel, 0);
                  arrayList1.add(parcel);
                  continue;
                } 
                break;
              } 
            } 
            resultWrapper.sendResult(parcel);
          }
        };
      MediaBrowserServiceCompat.this.onLoadChildren(param1String, result);
    }
    
    public void setSessionToken(MediaSessionCompat.Token param1Token) {
      MediaBrowserServiceCompatApi21.setSessionToken(this.mServiceObj, param1Token.getToken());
    }
  }
  
  class null implements Runnable {
    public void run() {
      for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
        MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
        List list = connectionRecord.subscriptions.get(parentId);
        if (list != null)
          for (Pair pair : list) {
            if (MediaBrowserCompatUtils.hasDuplicatedItems(options, (Bundle)pair.second))
              MediaBrowserServiceCompat.this.performLoadChildren(parentId, connectionRecord, (Bundle)pair.second); 
          }  
      } 
    }
  }
  
  class null extends Result<List<MediaBrowserCompat.MediaItem>> {
    null(Object param1Object) {
      super(param1Object);
    }
    
    public void detach() {
      resultWrapper.detach();
    }
    
    void onResultSent(List<MediaBrowserCompat.MediaItem> param1List, int param1Int) {
      Parcel parcel;
      ArrayList<Parcel> arrayList = null;
      if (param1List != null) {
        ArrayList<Parcel> arrayList1 = new ArrayList();
        Iterator<MediaBrowserCompat.MediaItem> iterator = param1List.iterator();
        while (true) {
          arrayList = arrayList1;
          if (iterator.hasNext()) {
            MediaBrowserCompat.MediaItem mediaItem = iterator.next();
            parcel = Parcel.obtain();
            mediaItem.writeToParcel(parcel, 0);
            arrayList1.add(parcel);
            continue;
          } 
          break;
        } 
      } 
      resultWrapper.sendResult(parcel);
    }
  }
  
  class MediaBrowserServiceImplApi23 extends MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompatApi23.ServiceCompatProxy {
    public void onCreate() {
      this.mServiceObj = MediaBrowserServiceCompatApi23.createService((Context)MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }
    
    public void onLoadItem(String param1String, final MediaBrowserServiceCompatApi21.ResultWrapper<Parcel> resultWrapper) {
      MediaBrowserServiceCompat.Result<MediaBrowserCompat.MediaItem> result = new MediaBrowserServiceCompat.Result<MediaBrowserCompat.MediaItem>(param1String) {
          public void detach() {
            resultWrapper.detach();
          }
          
          void onResultSent(MediaBrowserCompat.MediaItem param2MediaItem, int param2Int) {
            if (param2MediaItem == null) {
              resultWrapper.sendResult(null);
              return;
            } 
            Parcel parcel = Parcel.obtain();
            param2MediaItem.writeToParcel(parcel, 0);
            resultWrapper.sendResult(parcel);
          }
        };
      MediaBrowserServiceCompat.this.onLoadItem(param1String, result);
    }
  }
  
  class null extends Result<MediaBrowserCompat.MediaItem> {
    null(Object param1Object) {
      super(param1Object);
    }
    
    public void detach() {
      resultWrapper.detach();
    }
    
    void onResultSent(MediaBrowserCompat.MediaItem param1MediaItem, int param1Int) {
      if (param1MediaItem == null) {
        resultWrapper.sendResult(null);
        return;
      } 
      Parcel parcel = Parcel.obtain();
      param1MediaItem.writeToParcel(parcel, 0);
      resultWrapper.sendResult(parcel);
    }
  }
  
  class MediaBrowserServiceImplApi24 extends MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi24.ServiceCompatProxy {
    public Bundle getBrowserRootHints() {
      return MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
    }
    
    public void notifyChildrenChanged(String param1String, Bundle param1Bundle) {
      if (param1Bundle == null) {
        MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, param1String);
        return;
      } 
      MediaBrowserServiceCompatApi24.notifyChildrenChanged(this.mServiceObj, param1String, param1Bundle);
    }
    
    public void onCreate() {
      this.mServiceObj = MediaBrowserServiceCompatApi24.createService((Context)MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }
    
    public void onLoadChildren(String param1String, final MediaBrowserServiceCompatApi24.ResultWrapper resultWrapper, Bundle param1Bundle) {
      MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = new MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>(param1String) {
          public void detach() {
            resultWrapper.detach();
          }
          
          void onResultSent(List<MediaBrowserCompat.MediaItem> param2List, int param2Int) {
            Parcel parcel;
            ArrayList<Parcel> arrayList = null;
            if (param2List != null) {
              ArrayList<Parcel> arrayList1 = new ArrayList();
              Iterator<MediaBrowserCompat.MediaItem> iterator = param2List.iterator();
              while (true) {
                arrayList = arrayList1;
                if (iterator.hasNext()) {
                  MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                  parcel = Parcel.obtain();
                  mediaItem.writeToParcel(parcel, 0);
                  arrayList1.add(parcel);
                  continue;
                } 
                break;
              } 
            } 
            resultWrapper.sendResult((List<Parcel>)parcel, param2Int);
          }
        };
      MediaBrowserServiceCompat.this.onLoadChildren(param1String, result, param1Bundle);
    }
  }
  
  class null extends Result<List<MediaBrowserCompat.MediaItem>> {
    null(Object param1Object) {
      super(param1Object);
    }
    
    public void detach() {
      resultWrapper.detach();
    }
    
    void onResultSent(List<MediaBrowserCompat.MediaItem> param1List, int param1Int) {
      Parcel parcel;
      ArrayList<Parcel> arrayList = null;
      if (param1List != null) {
        ArrayList<Parcel> arrayList1 = new ArrayList();
        Iterator<MediaBrowserCompat.MediaItem> iterator = param1List.iterator();
        while (true) {
          arrayList = arrayList1;
          if (iterator.hasNext()) {
            MediaBrowserCompat.MediaItem mediaItem = iterator.next();
            parcel = Parcel.obtain();
            mediaItem.writeToParcel(parcel, 0);
            arrayList1.add(parcel);
            continue;
          } 
          break;
        } 
      } 
      resultWrapper.sendResult((List<Parcel>)parcel, param1Int);
    }
  }
  
  class MediaBrowserServiceImplBase implements MediaBrowserServiceImpl {
    private Messenger mMessenger;
    
    public Bundle getBrowserRootHints() {
      if (MediaBrowserServiceCompat.this.mCurConnection == null)
        throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods"); 
      return (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) ? null : new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
    }
    
    public void notifyChildrenChanged(@NonNull final String parentId, final Bundle options) {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
                MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
                List list = connectionRecord.subscriptions.get(parentId);
                if (list != null)
                  for (Pair pair : list) {
                    if (MediaBrowserCompatUtils.hasDuplicatedItems(options, (Bundle)pair.second))
                      MediaBrowserServiceCompat.this.performLoadChildren(parentId, connectionRecord, (Bundle)pair.second); 
                  }  
              } 
            }
          });
    }
    
    public IBinder onBind(Intent param1Intent) {
      return "android.media.browse.MediaBrowserService".equals(param1Intent.getAction()) ? this.mMessenger.getBinder() : null;
    }
    
    public void onCreate() {
      this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
    }
    
    public void setSessionToken(final MediaSessionCompat.Token token) {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              Iterator<MediaBrowserServiceCompat.ConnectionRecord> iterator = MediaBrowserServiceCompat.this.mConnections.values().iterator();
              while (iterator.hasNext()) {
                MediaBrowserServiceCompat.ConnectionRecord connectionRecord = iterator.next();
                try {
                  connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
                } catch (RemoteException remoteException) {
                  Log.w("MBServiceCompat", "Connection for " + connectionRecord.pkg + " is no longer valid.");
                  iterator.remove();
                } 
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      Iterator<MediaBrowserServiceCompat.ConnectionRecord> iterator = MediaBrowserServiceCompat.this.mConnections.values().iterator();
      while (iterator.hasNext()) {
        MediaBrowserServiceCompat.ConnectionRecord connectionRecord = iterator.next();
        try {
          connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
        } catch (RemoteException remoteException) {
          Log.w("MBServiceCompat", "Connection for " + connectionRecord.pkg + " is no longer valid.");
          iterator.remove();
        } 
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
        MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
        List list = connectionRecord.subscriptions.get(parentId);
        if (list != null)
          for (Pair pair : list) {
            if (MediaBrowserCompatUtils.hasDuplicatedItems(options, (Bundle)pair.second))
              MediaBrowserServiceCompat.this.performLoadChildren(parentId, connectionRecord, (Bundle)pair.second); 
          }  
      } 
    }
  }
  
  public static class Result<T> {
    private Object mDebug;
    
    private boolean mDetachCalled;
    
    private int mFlags;
    
    private boolean mSendResultCalled;
    
    Result(Object param1Object) {
      this.mDebug = param1Object;
    }
    
    public void detach() {
      if (this.mDetachCalled)
        throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug); 
      if (this.mSendResultCalled)
        throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug); 
      this.mDetachCalled = true;
    }
    
    boolean isDone() {
      return (this.mDetachCalled || this.mSendResultCalled);
    }
    
    void onResultSent(T param1T, int param1Int) {}
    
    public void sendResult(T param1T) {
      if (this.mSendResultCalled)
        throw new IllegalStateException("sendResult() called twice for: " + this.mDebug); 
      this.mSendResultCalled = true;
      onResultSent(param1T, this.mFlags);
    }
    
    void setFlags(int param1Int) {
      this.mFlags = param1Int;
    }
  }
  
  private class ServiceBinderImpl {
    public void addSubscription(final String id, final IBinder token, final Bundle options, final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              if (connectionRecord == null) {
                Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + id);
                return;
              } 
              MediaBrowserServiceCompat.this.addSubscription(id, connectionRecord, token, options);
            }
          });
    }
    
    public void connect(final String pkg, final int uid, final Bundle rootHints, final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      if (!MediaBrowserServiceCompat.this.isValidPackage(pkg, uid))
        throw new IllegalArgumentException("Package/uid mismatch: uid=" + uid + " package=" + pkg); 
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
              connectionRecord.pkg = pkg;
              connectionRecord.rootHints = rootHints;
              connectionRecord.callbacks = callbacks;
              connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(pkg, uid, rootHints);
              if (connectionRecord.root == null) {
                Log.i("MBServiceCompat", "No root for client " + pkg + " from service " + getClass().getName());
                try {
                  callbacks.onConnectFailed();
                } catch (RemoteException remoteException) {
                  Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + pkg);
                } 
                return;
              } 
              try {
                MediaBrowserServiceCompat.this.mConnections.put(remoteException, connectionRecord);
                if (MediaBrowserServiceCompat.this.mSession != null)
                  callbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras()); 
              } catch (RemoteException remoteException1) {
                Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + pkg);
                MediaBrowserServiceCompat.this.mConnections.remove(remoteException);
              } 
            }
          });
    }
    
    public void disconnect(final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(iBinder) != null);
            }
          });
    }
    
    public void getMediaItem(final String mediaId, final ResultReceiver receiver, final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      if (!TextUtils.isEmpty(mediaId) && receiver != null)
        MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
              public void run() {
                IBinder iBinder = callbacks.asBinder();
                MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
                if (connectionRecord == null) {
                  Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + mediaId);
                  return;
                } 
                MediaBrowserServiceCompat.this.performLoadItem(mediaId, connectionRecord, receiver);
              }
            }); 
    }
    
    public void registerCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks callbacks, final Bundle rootHints) {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
              connectionRecord.callbacks = callbacks;
              connectionRecord.rootHints = rootHints;
              MediaBrowserServiceCompat.this.mConnections.put(iBinder, connectionRecord);
            }
          });
    }
    
    public void removeSubscription(final String id, final IBinder token, final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              if (connectionRecord == null) {
                Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + id);
                return;
              } 
              if (!MediaBrowserServiceCompat.this.removeSubscription(id, connectionRecord, token))
                Log.w("MBServiceCompat", "removeSubscription called for " + id + " which is not subscribed"); 
            }
          });
    }
    
    public void unregisterCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
      connectionRecord.pkg = pkg;
      connectionRecord.rootHints = rootHints;
      connectionRecord.callbacks = callbacks;
      connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(pkg, uid, rootHints);
      if (connectionRecord.root == null) {
        Log.i("MBServiceCompat", "No root for client " + pkg + " from service " + getClass().getName());
        try {
          callbacks.onConnectFailed();
        } catch (RemoteException remoteException) {
          Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + pkg);
        } 
        return;
      } 
      try {
        MediaBrowserServiceCompat.this.mConnections.put(remoteException, connectionRecord);
        if (MediaBrowserServiceCompat.this.mSession != null)
          callbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras()); 
      } catch (RemoteException remoteException1) {
        Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + pkg);
        MediaBrowserServiceCompat.this.mConnections.remove(remoteException);
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(iBinder) != null);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
      if (connectionRecord == null) {
        Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + id);
        return;
      } 
      MediaBrowserServiceCompat.this.addSubscription(id, connectionRecord, token, options);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
      if (connectionRecord == null) {
        Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + id);
        return;
      } 
      if (!MediaBrowserServiceCompat.this.removeSubscription(id, connectionRecord, token))
        Log.w("MBServiceCompat", "removeSubscription called for " + id + " which is not subscribed"); 
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
      if (connectionRecord == null) {
        Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + mediaId);
        return;
      } 
      MediaBrowserServiceCompat.this.performLoadItem(mediaId, connectionRecord, receiver);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
      connectionRecord.callbacks = callbacks;
      connectionRecord.rootHints = rootHints;
      MediaBrowserServiceCompat.this.mConnections.put(iBinder, connectionRecord);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
    }
  }
  
  private static interface ServiceCallbacks {
    IBinder asBinder();
    
    void onConnect(String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) throws RemoteException;
    
    void onConnectFailed() throws RemoteException;
    
    void onLoadChildren(String param1String, List<MediaBrowserCompat.MediaItem> param1List, Bundle param1Bundle) throws RemoteException;
  }
  
  private class ServiceCallbacksCompat implements ServiceCallbacks {
    final Messenger mCallbacks;
    
    ServiceCallbacksCompat(Messenger param1Messenger) {
      this.mCallbacks = param1Messenger;
    }
    
    private void sendRequest(int param1Int, Bundle param1Bundle) throws RemoteException {
      Message message = Message.obtain();
      message.what = param1Int;
      message.arg1 = 1;
      message.setData(param1Bundle);
      this.mCallbacks.send(message);
    }
    
    public IBinder asBinder() {
      return this.mCallbacks.getBinder();
    }
    
    public void onConnect(String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) throws RemoteException {
      Bundle bundle = param1Bundle;
      if (param1Bundle == null)
        bundle = new Bundle(); 
      bundle.putInt("extra_service_version", 1);
      param1Bundle = new Bundle();
      param1Bundle.putString("data_media_item_id", param1String);
      param1Bundle.putParcelable("data_media_session_token", (Parcelable)param1Token);
      param1Bundle.putBundle("data_root_hints", bundle);
      sendRequest(1, param1Bundle);
    }
    
    public void onConnectFailed() throws RemoteException {
      sendRequest(2, null);
    }
    
    public void onLoadChildren(String param1String, List<MediaBrowserCompat.MediaItem> param1List, Bundle param1Bundle) throws RemoteException {
      Bundle bundle = new Bundle();
      bundle.putString("data_media_item_id", param1String);
      bundle.putBundle("data_options", param1Bundle);
      if (param1List != null) {
        ArrayList<MediaBrowserCompat.MediaItem> arrayList;
        if (param1List instanceof ArrayList) {
          arrayList = (ArrayList)param1List;
        } else {
          arrayList = new ArrayList<MediaBrowserCompat.MediaItem>(param1List);
        } 
        bundle.putParcelableArrayList("data_media_item_list", arrayList);
      } 
      sendRequest(3, bundle);
    }
  }
  
  private final class ServiceHandler extends Handler {
    private final MediaBrowserServiceCompat.ServiceBinderImpl mServiceBinderImpl = new MediaBrowserServiceCompat.ServiceBinderImpl();
    
    public void handleMessage(Message param1Message) {
      Bundle bundle = param1Message.getData();
      switch (param1Message.what) {
        default:
          Log.w("MBServiceCompat", "Unhandled message: " + param1Message + "\n  Service version: " + '\001' + "\n  Client version: " + param1Message.arg1);
          return;
        case 1:
          this.mServiceBinderImpl.connect(bundle.getString("data_package_name"), bundle.getInt("data_calling_uid"), bundle.getBundle("data_root_hints"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
          return;
        case 2:
          this.mServiceBinderImpl.disconnect(new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
          return;
        case 3:
          this.mServiceBinderImpl.addSubscription(bundle.getString("data_media_item_id"), BundleCompat.getBinder(bundle, "data_callback_token"), bundle.getBundle("data_options"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
          return;
        case 4:
          this.mServiceBinderImpl.removeSubscription(bundle.getString("data_media_item_id"), BundleCompat.getBinder(bundle, "data_callback_token"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
          return;
        case 5:
          this.mServiceBinderImpl.getMediaItem(bundle.getString("data_media_item_id"), (ResultReceiver)bundle.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
          return;
        case 6:
          this.mServiceBinderImpl.registerCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo), bundle.getBundle("data_root_hints"));
          return;
        case 7:
          break;
      } 
      this.mServiceBinderImpl.unregisterCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(param1Message.replyTo));
    }
    
    public void postOrRun(Runnable param1Runnable) {
      if (Thread.currentThread() == getLooper().getThread()) {
        param1Runnable.run();
        return;
      } 
      post(param1Runnable);
    }
    
    public boolean sendMessageAtTime(Message param1Message, long param1Long) {
      Bundle bundle = param1Message.getData();
      bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
      bundle.putInt("data_calling_uid", Binder.getCallingUid());
      return super.sendMessageAtTime(param1Message, param1Long);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/MediaBrowserServiceCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */