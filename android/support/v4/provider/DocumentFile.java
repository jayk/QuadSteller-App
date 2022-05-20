package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import java.io.File;

public abstract class DocumentFile {
  static final String TAG = "DocumentFile";
  
  private final DocumentFile mParent;
  
  DocumentFile(DocumentFile paramDocumentFile) {
    this.mParent = paramDocumentFile;
  }
  
  public static DocumentFile fromFile(File paramFile) {
    return new RawDocumentFile(null, paramFile);
  }
  
  public static DocumentFile fromSingleUri(Context paramContext, Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? new SingleDocumentFile(null, paramContext, paramUri) : null;
  }
  
  public static DocumentFile fromTreeUri(Context paramContext, Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 21) ? new TreeDocumentFile(null, paramContext, DocumentsContractApi21.prepareTreeUri(paramUri)) : null;
  }
  
  public static boolean isDocumentUri(Context paramContext, Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? DocumentsContractApi19.isDocumentUri(paramContext, paramUri) : false;
  }
  
  public abstract boolean canRead();
  
  public abstract boolean canWrite();
  
  public abstract DocumentFile createDirectory(String paramString);
  
  public abstract DocumentFile createFile(String paramString1, String paramString2);
  
  public abstract boolean delete();
  
  public abstract boolean exists();
  
  public DocumentFile findFile(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual listFiles : ()[Landroid/support/v4/provider/DocumentFile;
    //   4: astore_2
    //   5: aload_2
    //   6: arraylength
    //   7: istore_3
    //   8: iconst_0
    //   9: istore #4
    //   11: iload #4
    //   13: iload_3
    //   14: if_icmpge -> 46
    //   17: aload_2
    //   18: iload #4
    //   20: aaload
    //   21: astore #5
    //   23: aload_1
    //   24: aload #5
    //   26: invokevirtual getName : ()Ljava/lang/String;
    //   29: invokevirtual equals : (Ljava/lang/Object;)Z
    //   32: ifeq -> 40
    //   35: aload #5
    //   37: astore_1
    //   38: aload_1
    //   39: areturn
    //   40: iinc #4, 1
    //   43: goto -> 11
    //   46: aconst_null
    //   47: astore_1
    //   48: goto -> 38
  }
  
  public abstract String getName();
  
  public DocumentFile getParentFile() {
    return this.mParent;
  }
  
  public abstract String getType();
  
  public abstract Uri getUri();
  
  public abstract boolean isDirectory();
  
  public abstract boolean isFile();
  
  public abstract boolean isVirtual();
  
  public abstract long lastModified();
  
  public abstract long length();
  
  public abstract DocumentFile[] listFiles();
  
  public abstract boolean renameTo(String paramString);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/provider/DocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */