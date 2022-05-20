package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.util.Pools;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.concurrent.ArrayBlockingQueue;

public final class AsyncLayoutInflater {
  private static final String TAG = "AsyncLayoutInflater";
  
  Handler mHandler;
  
  private Handler.Callback mHandlerCallback = new Handler.Callback() {
      public boolean handleMessage(Message param1Message) {
        AsyncLayoutInflater.InflateRequest inflateRequest = (AsyncLayoutInflater.InflateRequest)param1Message.obj;
        if (inflateRequest.view == null)
          inflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false); 
        inflateRequest.callback.onInflateFinished(inflateRequest.view, inflateRequest.resid, inflateRequest.parent);
        AsyncLayoutInflater.this.mInflateThread.releaseRequest(inflateRequest);
        return true;
      }
    };
  
  InflateThread mInflateThread;
  
  LayoutInflater mInflater;
  
  public AsyncLayoutInflater(@NonNull Context paramContext) {
    this.mInflater = new BasicInflater(paramContext);
    this.mHandler = new Handler(this.mHandlerCallback);
    this.mInflateThread = InflateThread.getInstance();
  }
  
  @UiThread
  public void inflate(@LayoutRes int paramInt, @Nullable ViewGroup paramViewGroup, @NonNull OnInflateFinishedListener paramOnInflateFinishedListener) {
    if (paramOnInflateFinishedListener == null)
      throw new NullPointerException("callback argument may not be null!"); 
    InflateRequest inflateRequest = this.mInflateThread.obtainRequest();
    inflateRequest.inflater = this;
    inflateRequest.resid = paramInt;
    inflateRequest.parent = paramViewGroup;
    inflateRequest.callback = paramOnInflateFinishedListener;
    this.mInflateThread.enqueue(inflateRequest);
  }
  
  private static class BasicInflater extends LayoutInflater {
    private static final String[] sClassPrefixList = new String[] { "android.widget.", "android.webkit.", "android.app." };
    
    BasicInflater(Context param1Context) {
      super(param1Context);
    }
    
    public LayoutInflater cloneInContext(Context param1Context) {
      return new BasicInflater(param1Context);
    }
    
    protected View onCreateView(String param1String, AttributeSet param1AttributeSet) throws ClassNotFoundException {
      // Byte code:
      //   0: getstatic android/support/v4/view/AsyncLayoutInflater$BasicInflater.sClassPrefixList : [Ljava/lang/String;
      //   3: astore_3
      //   4: aload_3
      //   5: arraylength
      //   6: istore #4
      //   8: iconst_0
      //   9: istore #5
      //   11: iload #5
      //   13: iload #4
      //   15: if_icmpge -> 50
      //   18: aload_3
      //   19: iload #5
      //   21: aaload
      //   22: astore #6
      //   24: aload_0
      //   25: aload_1
      //   26: aload #6
      //   28: aload_2
      //   29: invokevirtual createView : (Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;
      //   32: astore #6
      //   34: aload #6
      //   36: ifnull -> 44
      //   39: aload #6
      //   41: areturn
      //   42: astore #6
      //   44: iinc #5, 1
      //   47: goto -> 11
      //   50: aload_0
      //   51: aload_1
      //   52: aload_2
      //   53: invokespecial onCreateView : (Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;
      //   56: astore #6
      //   58: goto -> 39
      // Exception table:
      //   from	to	target	type
      //   24	34	42	java/lang/ClassNotFoundException
    }
  }
  
  private static class InflateRequest {
    AsyncLayoutInflater.OnInflateFinishedListener callback;
    
    AsyncLayoutInflater inflater;
    
    ViewGroup parent;
    
    int resid;
    
    View view;
  }
  
  private static class InflateThread extends Thread {
    private static final InflateThread sInstance = new InflateThread();
    
    private ArrayBlockingQueue<AsyncLayoutInflater.InflateRequest> mQueue = new ArrayBlockingQueue<AsyncLayoutInflater.InflateRequest>(10);
    
    private Pools.SynchronizedPool<AsyncLayoutInflater.InflateRequest> mRequestPool = new Pools.SynchronizedPool(10);
    
    static {
      sInstance.start();
    }
    
    public static InflateThread getInstance() {
      return sInstance;
    }
    
    public void enqueue(AsyncLayoutInflater.InflateRequest param1InflateRequest) {
      try {
        this.mQueue.put(param1InflateRequest);
        return;
      } catch (InterruptedException interruptedException) {
        throw new RuntimeException("Failed to enqueue async inflate request", interruptedException);
      } 
    }
    
    public AsyncLayoutInflater.InflateRequest obtainRequest() {
      AsyncLayoutInflater.InflateRequest inflateRequest1 = (AsyncLayoutInflater.InflateRequest)this.mRequestPool.acquire();
      AsyncLayoutInflater.InflateRequest inflateRequest2 = inflateRequest1;
      if (inflateRequest1 == null)
        inflateRequest2 = new AsyncLayoutInflater.InflateRequest(); 
      return inflateRequest2;
    }
    
    public void releaseRequest(AsyncLayoutInflater.InflateRequest param1InflateRequest) {
      param1InflateRequest.callback = null;
      param1InflateRequest.inflater = null;
      param1InflateRequest.parent = null;
      param1InflateRequest.resid = 0;
      param1InflateRequest.view = null;
      this.mRequestPool.release(param1InflateRequest);
    }
    
    public void run() {
      while (true) {
        try {
          AsyncLayoutInflater.InflateRequest inflateRequest = this.mQueue.take();
          try {
            inflateRequest.view = inflateRequest.inflater.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
          } catch (RuntimeException runtimeException) {
            Log.w("AsyncLayoutInflater", "Failed to inflate resource in the background! Retrying on the UI thread", runtimeException);
          } 
          Message.obtain(inflateRequest.inflater.mHandler, 0, inflateRequest).sendToTarget();
        } catch (InterruptedException interruptedException) {
          Log.w("AsyncLayoutInflater", interruptedException);
        } 
      } 
    }
  }
  
  public static interface OnInflateFinishedListener {
    void onInflateFinished(View param1View, int param1Int, ViewGroup param1ViewGroup);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/AsyncLayoutInflater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */