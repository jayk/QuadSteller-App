package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader<D> {
  boolean mAbandoned = false;
  
  boolean mContentChanged = false;
  
  Context mContext;
  
  int mId;
  
  OnLoadCompleteListener<D> mListener;
  
  OnLoadCanceledListener<D> mOnLoadCanceledListener;
  
  boolean mProcessingChange = false;
  
  boolean mReset = true;
  
  boolean mStarted = false;
  
  public Loader(Context paramContext) {
    this.mContext = paramContext.getApplicationContext();
  }
  
  public void abandon() {
    this.mAbandoned = true;
    onAbandon();
  }
  
  public boolean cancelLoad() {
    return onCancelLoad();
  }
  
  public void commitContentChanged() {
    this.mProcessingChange = false;
  }
  
  public String dataToString(D paramD) {
    StringBuilder stringBuilder = new StringBuilder(64);
    DebugUtils.buildShortClassTag(paramD, stringBuilder);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public void deliverCancellation() {
    if (this.mOnLoadCanceledListener != null)
      this.mOnLoadCanceledListener.onLoadCanceled(this); 
  }
  
  public void deliverResult(D paramD) {
    if (this.mListener != null)
      this.mListener.onLoadComplete(this, paramD); 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mId=");
    paramPrintWriter.print(this.mId);
    paramPrintWriter.print(" mListener=");
    paramPrintWriter.println(this.mListener);
    if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStarted=");
      paramPrintWriter.print(this.mStarted);
      paramPrintWriter.print(" mContentChanged=");
      paramPrintWriter.print(this.mContentChanged);
      paramPrintWriter.print(" mProcessingChange=");
      paramPrintWriter.println(this.mProcessingChange);
    } 
    if (this.mAbandoned || this.mReset) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mAbandoned=");
      paramPrintWriter.print(this.mAbandoned);
      paramPrintWriter.print(" mReset=");
      paramPrintWriter.println(this.mReset);
    } 
  }
  
  public void forceLoad() {
    onForceLoad();
  }
  
  public Context getContext() {
    return this.mContext;
  }
  
  public int getId() {
    return this.mId;
  }
  
  public boolean isAbandoned() {
    return this.mAbandoned;
  }
  
  public boolean isReset() {
    return this.mReset;
  }
  
  public boolean isStarted() {
    return this.mStarted;
  }
  
  protected void onAbandon() {}
  
  protected boolean onCancelLoad() {
    return false;
  }
  
  public void onContentChanged() {
    if (this.mStarted) {
      forceLoad();
      return;
    } 
    this.mContentChanged = true;
  }
  
  protected void onForceLoad() {}
  
  protected void onReset() {}
  
  protected void onStartLoading() {}
  
  protected void onStopLoading() {}
  
  public void registerListener(int paramInt, OnLoadCompleteListener<D> paramOnLoadCompleteListener) {
    if (this.mListener != null)
      throw new IllegalStateException("There is already a listener registered"); 
    this.mListener = paramOnLoadCompleteListener;
    this.mId = paramInt;
  }
  
  public void registerOnLoadCanceledListener(OnLoadCanceledListener<D> paramOnLoadCanceledListener) {
    if (this.mOnLoadCanceledListener != null)
      throw new IllegalStateException("There is already a listener registered"); 
    this.mOnLoadCanceledListener = paramOnLoadCanceledListener;
  }
  
  public void reset() {
    onReset();
    this.mReset = true;
    this.mStarted = false;
    this.mAbandoned = false;
    this.mContentChanged = false;
    this.mProcessingChange = false;
  }
  
  public void rollbackContentChanged() {
    if (this.mProcessingChange)
      onContentChanged(); 
  }
  
  public final void startLoading() {
    this.mStarted = true;
    this.mReset = false;
    this.mAbandoned = false;
    onStartLoading();
  }
  
  public void stopLoading() {
    this.mStarted = false;
    onStopLoading();
  }
  
  public boolean takeContentChanged() {
    boolean bool = this.mContentChanged;
    this.mContentChanged = false;
    this.mProcessingChange |= bool;
    return bool;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(64);
    DebugUtils.buildShortClassTag(this, stringBuilder);
    stringBuilder.append(" id=");
    stringBuilder.append(this.mId);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public void unregisterListener(OnLoadCompleteListener<D> paramOnLoadCompleteListener) {
    if (this.mListener == null)
      throw new IllegalStateException("No listener register"); 
    if (this.mListener != paramOnLoadCompleteListener)
      throw new IllegalArgumentException("Attempting to unregister the wrong listener"); 
    this.mListener = null;
  }
  
  public void unregisterOnLoadCanceledListener(OnLoadCanceledListener<D> paramOnLoadCanceledListener) {
    if (this.mOnLoadCanceledListener == null)
      throw new IllegalStateException("No listener register"); 
    if (this.mOnLoadCanceledListener != paramOnLoadCanceledListener)
      throw new IllegalArgumentException("Attempting to unregister the wrong listener"); 
    this.mOnLoadCanceledListener = null;
  }
  
  public final class ForceLoadContentObserver extends ContentObserver {
    public ForceLoadContentObserver() {
      super(new Handler());
    }
    
    public boolean deliverSelfNotifications() {
      return true;
    }
    
    public void onChange(boolean param1Boolean) {
      Loader.this.onContentChanged();
    }
  }
  
  public static interface OnLoadCanceledListener<D> {
    void onLoadCanceled(Loader<D> param1Loader);
  }
  
  public static interface OnLoadCompleteListener<D> {
    void onLoadComplete(Loader<D> param1Loader, D param1D);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/Loader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */