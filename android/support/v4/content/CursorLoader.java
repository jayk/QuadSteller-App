package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.os.CancellationSignal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor> {
  CancellationSignal mCancellationSignal;
  
  Cursor mCursor;
  
  final Loader<Cursor>.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
  
  String[] mProjection;
  
  String mSelection;
  
  String[] mSelectionArgs;
  
  String mSortOrder;
  
  Uri mUri;
  
  public CursorLoader(Context paramContext) {
    super(paramContext);
  }
  
  public CursorLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    super(paramContext);
    this.mUri = paramUri;
    this.mProjection = paramArrayOfString1;
    this.mSelection = paramString1;
    this.mSelectionArgs = paramArrayOfString2;
    this.mSortOrder = paramString2;
  }
  
  public void cancelLoadInBackground() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial cancelLoadInBackground : ()V
    //   4: aload_0
    //   5: monitorenter
    //   6: aload_0
    //   7: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   10: ifnull -> 20
    //   13: aload_0
    //   14: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   17: invokevirtual cancel : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   6	20	23	finally
    //   20	22	23	finally
    //   24	26	23	finally
  }
  
  public void deliverResult(Cursor paramCursor) {
    if (isReset()) {
      if (paramCursor != null)
        paramCursor.close(); 
      return;
    } 
    Cursor cursor = this.mCursor;
    this.mCursor = paramCursor;
    if (isStarted())
      super.deliverResult(paramCursor); 
    if (cursor != null && cursor != paramCursor && !cursor.isClosed())
      cursor.close(); 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    paramPrintWriter.println(this.mUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    paramPrintWriter.println(Arrays.toString((Object[])this.mProjection));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    paramPrintWriter.println(this.mSelection);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    paramPrintWriter.println(Arrays.toString((Object[])this.mSelectionArgs));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    paramPrintWriter.println(this.mSortOrder);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    paramPrintWriter.println(this.mCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    paramPrintWriter.println(this.mContentChanged);
  }
  
  public String[] getProjection() {
    return this.mProjection;
  }
  
  public String getSelection() {
    return this.mSelection;
  }
  
  public String[] getSelectionArgs() {
    return this.mSelectionArgs;
  }
  
  public String getSortOrder() {
    return this.mSortOrder;
  }
  
  public Uri getUri() {
    return this.mUri;
  }
  
  public Cursor loadInBackground() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isLoadInBackgroundCanceled : ()Z
    //   6: ifeq -> 24
    //   9: new android/support/v4/os/OperationCanceledException
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial <init> : ()V
    //   17: aload_1
    //   18: athrow
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    //   24: new android/support/v4/os/CancellationSignal
    //   27: astore_1
    //   28: aload_1
    //   29: invokespecial <init> : ()V
    //   32: aload_0
    //   33: aload_1
    //   34: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_0
    //   40: invokevirtual getContext : ()Landroid/content/Context;
    //   43: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   46: aload_0
    //   47: getfield mUri : Landroid/net/Uri;
    //   50: aload_0
    //   51: getfield mProjection : [Ljava/lang/String;
    //   54: aload_0
    //   55: getfield mSelection : Ljava/lang/String;
    //   58: aload_0
    //   59: getfield mSelectionArgs : [Ljava/lang/String;
    //   62: aload_0
    //   63: getfield mSortOrder : Ljava/lang/String;
    //   66: aload_0
    //   67: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   70: invokestatic query : (Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/support/v4/os/CancellationSignal;)Landroid/database/Cursor;
    //   73: astore_1
    //   74: aload_1
    //   75: ifnull -> 95
    //   78: aload_1
    //   79: invokeinterface getCount : ()I
    //   84: pop
    //   85: aload_1
    //   86: aload_0
    //   87: getfield mObserver : Landroid/support/v4/content/Loader$ForceLoadContentObserver;
    //   90: invokeinterface registerContentObserver : (Landroid/database/ContentObserver;)V
    //   95: aload_0
    //   96: monitorenter
    //   97: aload_0
    //   98: aconst_null
    //   99: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: areturn
    //   106: astore_2
    //   107: aload_1
    //   108: invokeinterface close : ()V
    //   113: aload_2
    //   114: athrow
    //   115: astore_1
    //   116: aload_0
    //   117: monitorenter
    //   118: aload_0
    //   119: aconst_null
    //   120: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_1
    //   126: athrow
    //   127: astore_1
    //   128: aload_0
    //   129: monitorexit
    //   130: aload_1
    //   131: athrow
    //   132: astore_1
    //   133: aload_0
    //   134: monitorexit
    //   135: aload_1
    //   136: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	19	finally
    //   20	22	19	finally
    //   24	39	19	finally
    //   39	74	115	finally
    //   78	95	106	java/lang/RuntimeException
    //   78	95	115	finally
    //   97	104	127	finally
    //   107	115	115	finally
    //   118	125	132	finally
    //   128	130	127	finally
    //   133	135	132	finally
  }
  
  public void onCanceled(Cursor paramCursor) {
    if (paramCursor != null && !paramCursor.isClosed())
      paramCursor.close(); 
  }
  
  protected void onReset() {
    super.onReset();
    onStopLoading();
    if (this.mCursor != null && !this.mCursor.isClosed())
      this.mCursor.close(); 
    this.mCursor = null;
  }
  
  protected void onStartLoading() {
    if (this.mCursor != null)
      deliverResult(this.mCursor); 
    if (takeContentChanged() || this.mCursor == null)
      forceLoad(); 
  }
  
  protected void onStopLoading() {
    cancelLoad();
  }
  
  public void setProjection(String[] paramArrayOfString) {
    this.mProjection = paramArrayOfString;
  }
  
  public void setSelection(String paramString) {
    this.mSelection = paramString;
  }
  
  public void setSelectionArgs(String[] paramArrayOfString) {
    this.mSelectionArgs = paramArrayOfString;
  }
  
  public void setSortOrder(String paramString) {
    this.mSortOrder = paramString;
  }
  
  public void setUri(Uri paramUri) {
    this.mUri = paramUri;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/CursorLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */