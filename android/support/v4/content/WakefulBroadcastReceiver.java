package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
  private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
  
  private static final SparseArray<PowerManager.WakeLock> mActiveWakeLocks = new SparseArray();
  
  private static int mNextId = 1;
  
  public static boolean completeWakefulIntent(Intent paramIntent) {
    boolean bool = false;
    int i = paramIntent.getIntExtra("android.support.content.wakelockid", 0);
    if (i != 0) {
      SparseArray<PowerManager.WakeLock> sparseArray = mActiveWakeLocks;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{android/util/SparseArray<InnerObjectType{ObjectType{android/os/PowerManager}.Landroid/os/PowerManager$WakeLock;}>}, name=null} */
      try {
        PowerManager.WakeLock wakeLock = (PowerManager.WakeLock)mActiveWakeLocks.get(i);
        if (wakeLock != null) {
          wakeLock.release();
          mActiveWakeLocks.remove(i);
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{android/util/SparseArray<InnerObjectType{ObjectType{android/os/PowerManager}.Landroid/os/PowerManager$WakeLock;}>}, name=null} */
          return true;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.w("WakefulBroadcastReceiver", stringBuilder.append("No active wake lock id #").append(i).toString());
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{android/util/SparseArray<InnerObjectType{ObjectType{android/os/PowerManager}.Landroid/os/PowerManager$WakeLock;}>}, name=null} */
        bool = true;
      } finally {
        Exception exception;
      } 
    } 
    return bool;
  }
  
  public static ComponentName startWakefulService(Context paramContext, Intent paramIntent) {
    synchronized (mActiveWakeLocks) {
      int i = mNextId;
      mNextId++;
      if (mNextId <= 0)
        mNextId = 1; 
      paramIntent.putExtra("android.support.content.wakelockid", i);
      ComponentName componentName = paramContext.startService(paramIntent);
      if (componentName == null) {
        paramContext = null;
        return (ComponentName)paramContext;
      } 
      PowerManager powerManager = (PowerManager)paramContext.getSystemService("power");
      StringBuilder stringBuilder = new StringBuilder();
      this();
      PowerManager.WakeLock wakeLock = powerManager.newWakeLock(1, stringBuilder.append("wake:").append(componentName.flattenToShortString()).toString());
      wakeLock.setReferenceCounted(false);
      wakeLock.acquire(60000L);
      mActiveWakeLocks.put(i, wakeLock);
      return componentName;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/WakefulBroadcastReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */