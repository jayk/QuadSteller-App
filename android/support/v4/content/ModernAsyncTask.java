package android.support.v4.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
  private static final int CORE_POOL_SIZE = 5;
  
  private static final int KEEP_ALIVE = 1;
  
  private static final String LOG_TAG = "AsyncTask";
  
  private static final int MAXIMUM_POOL_SIZE = 128;
  
  private static final int MESSAGE_POST_PROGRESS = 2;
  
  private static final int MESSAGE_POST_RESULT = 1;
  
  public static final Executor THREAD_POOL_EXECUTOR;
  
  private static volatile Executor sDefaultExecutor;
  
  private static InternalHandler sHandler;
  
  private static final BlockingQueue<Runnable> sPoolWorkQueue;
  
  private static final ThreadFactory sThreadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger(1);
      
      public Thread newThread(Runnable param1Runnable) {
        return new Thread(param1Runnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
      }
    };
  
  private final AtomicBoolean mCancelled = new AtomicBoolean();
  
  private final FutureTask<Result> mFuture = new FutureTask<Result>(this.mWorker) {
      protected void done() {
        try {
          Result result = get();
          ModernAsyncTask.this.postResultIfNotInvoked(result);
          return;
        } catch (InterruptedException interruptedException) {
          Log.w("AsyncTask", interruptedException);
          return;
        } catch (ExecutionException executionException) {
          throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
        } catch (CancellationException cancellationException) {
          ModernAsyncTask.this.postResultIfNotInvoked(null);
          return;
        } catch (Throwable throwable) {
          throw new RuntimeException("An error occurred while executing doInBackground()", throwable);
        } 
      }
    };
  
  private volatile Status mStatus = Status.PENDING;
  
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  
  private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>() {
      public Result call() throws Exception {
        ModernAsyncTask.this.mTaskInvoked.set(true);
        Object object = null;
        Object object1 = null;
        null = object1;
        Object object2 = object;
        try {
          Process.setThreadPriority(10);
          null = object1;
          object2 = object;
          object1 = ModernAsyncTask.this.doInBackground((Object[])this.mParams);
          null = object1;
          object2 = object1;
          Binder.flushPendingCommands();
          return (Result)object1;
        } catch (Throwable throwable) {
          object2 = null;
          ModernAsyncTask.this.mCancelled.set(true);
          object2 = null;
          throw throwable;
        } finally {
          ModernAsyncTask.this.postResult(object2);
        } 
      }
    };
  
  static {
    sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
    THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    sDefaultExecutor = THREAD_POOL_EXECUTOR;
  }
  
  public static void execute(Runnable paramRunnable) {
    sDefaultExecutor.execute(paramRunnable);
  }
  
  private static Handler getHandler() {
    // Byte code:
    //   0: ldc android/support/v4/content/ModernAsyncTask
    //   2: monitorenter
    //   3: getstatic android/support/v4/content/ModernAsyncTask.sHandler : Landroid/support/v4/content/ModernAsyncTask$InternalHandler;
    //   6: ifnonnull -> 21
    //   9: new android/support/v4/content/ModernAsyncTask$InternalHandler
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic android/support/v4/content/ModernAsyncTask.sHandler : Landroid/support/v4/content/ModernAsyncTask$InternalHandler;
    //   21: getstatic android/support/v4/content/ModernAsyncTask.sHandler : Landroid/support/v4/content/ModernAsyncTask$InternalHandler;
    //   24: astore_0
    //   25: ldc android/support/v4/content/ModernAsyncTask
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc android/support/v4/content/ModernAsyncTask
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	28	30	finally
    //   31	34	30	finally
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void setDefaultExecutor(Executor paramExecutor) {
    sDefaultExecutor = paramExecutor;
  }
  
  public final boolean cancel(boolean paramBoolean) {
    this.mCancelled.set(true);
    return this.mFuture.cancel(paramBoolean);
  }
  
  protected abstract Result doInBackground(Params... paramVarArgs);
  
  public final ModernAsyncTask<Params, Progress, Result> execute(Params... paramVarArgs) {
    return executeOnExecutor(sDefaultExecutor, paramVarArgs);
  }
  
  public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params... paramVarArgs) {
    if (this.mStatus != Status.PENDING) {
      switch (this.mStatus) {
        default:
          this.mStatus = Status.RUNNING;
          onPreExecute();
          this.mWorker.mParams = paramVarArgs;
          paramExecutor.execute(this.mFuture);
          return this;
        case RUNNING:
          throw new IllegalStateException("Cannot execute task: the task is already running.");
        case FINISHED:
          break;
      } 
      throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    } 
  }
  
  void finish(Result paramResult) {
    if (isCancelled()) {
      onCancelled(paramResult);
    } else {
      onPostExecute(paramResult);
    } 
    this.mStatus = Status.FINISHED;
  }
  
  public final Result get() throws InterruptedException, ExecutionException {
    return this.mFuture.get();
  }
  
  public final Result get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }
  
  public final Status getStatus() {
    return this.mStatus;
  }
  
  public final boolean isCancelled() {
    return this.mCancelled.get();
  }
  
  protected void onCancelled() {}
  
  protected void onCancelled(Result paramResult) {
    onCancelled();
  }
  
  protected void onPostExecute(Result paramResult) {}
  
  protected void onPreExecute() {}
  
  protected void onProgressUpdate(Progress... paramVarArgs) {}
  
  Result postResult(Result paramResult) {
    getHandler().obtainMessage(1, new AsyncTaskResult(this, new Object[] { paramResult })).sendToTarget();
    return paramResult;
  }
  
  void postResultIfNotInvoked(Result paramResult) {
    if (!this.mTaskInvoked.get())
      postResult(paramResult); 
  }
  
  protected final void publishProgress(Progress... paramVarArgs) {
    if (!isCancelled())
      getHandler().obtainMessage(2, new AsyncTaskResult<Progress>(this, paramVarArgs)).sendToTarget(); 
  }
  
  private static class AsyncTaskResult<Data> {
    final Data[] mData;
    
    final ModernAsyncTask mTask;
    
    AsyncTaskResult(ModernAsyncTask param1ModernAsyncTask, Data... param1VarArgs) {
      this.mTask = param1ModernAsyncTask;
      this.mData = param1VarArgs;
    }
  }
  
  private static class InternalHandler extends Handler {
    public InternalHandler() {
      super(Looper.getMainLooper());
    }
    
    public void handleMessage(Message param1Message) {
      ModernAsyncTask.AsyncTaskResult asyncTaskResult = (ModernAsyncTask.AsyncTaskResult)param1Message.obj;
      switch (param1Message.what) {
        default:
          return;
        case 1:
          asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
        case 2:
          break;
      } 
      asyncTaskResult.mTask.onProgressUpdate((Object[])asyncTaskResult.mData);
    }
  }
  
  public enum Status {
    FINISHED, PENDING, RUNNING;
    
    static {
      $VALUES = new Status[] { PENDING, RUNNING, FINISHED };
    }
  }
  
  private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
    Params[] mParams;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/ModernAsyncTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */