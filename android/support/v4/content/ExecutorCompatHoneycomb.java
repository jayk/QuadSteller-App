package android.support.v4.content;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.support.annotation.RequiresApi;
import java.util.concurrent.Executor;

@TargetApi(11)
@RequiresApi(11)
class ExecutorCompatHoneycomb {
  public static Executor getParallelExecutor() {
    return AsyncTask.THREAD_POOL_EXECUTOR;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/ExecutorCompatHoneycomb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */