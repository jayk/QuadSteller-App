package android.support.v4.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RawDocumentFile extends DocumentFile {
  private File mFile;
  
  RawDocumentFile(DocumentFile paramDocumentFile, File paramFile) {
    super(paramDocumentFile);
    this.mFile = paramFile;
  }
  
  private static boolean deleteContents(File paramFile) {
    File[] arrayOfFile = paramFile.listFiles();
    boolean bool1 = true;
    boolean bool2 = true;
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      byte b = 0;
      while (true) {
        bool1 = bool2;
        if (b < i) {
          paramFile = arrayOfFile[b];
          bool1 = bool2;
          if (paramFile.isDirectory())
            bool1 = bool2 & deleteContents(paramFile); 
          bool2 = bool1;
          if (!paramFile.delete()) {
            Log.w("DocumentFile", "Failed to delete " + paramFile);
            bool2 = false;
          } 
          b++;
          continue;
        } 
        break;
      } 
    } 
    return bool1;
  }
  
  private static String getTypeForName(String paramString) {
    int i = paramString.lastIndexOf('.');
    if (i >= 0) {
      paramString = paramString.substring(i + 1).toLowerCase();
      paramString = MimeTypeMap.getSingleton().getMimeTypeFromExtension(paramString);
      if (paramString != null)
        return paramString; 
    } 
    return "application/octet-stream";
  }
  
  public boolean canRead() {
    return this.mFile.canRead();
  }
  
  public boolean canWrite() {
    return this.mFile.canWrite();
  }
  
  public DocumentFile createDirectory(String paramString) {
    null = new File(this.mFile, paramString);
    return (null.isDirectory() || null.mkdir()) ? new RawDocumentFile(this, null) : null;
  }
  
  public DocumentFile createFile(String paramString1, String paramString2) {
    String str = MimeTypeMap.getSingleton().getExtensionFromMimeType(paramString1);
    paramString1 = paramString2;
    if (str != null)
      paramString1 = paramString2 + "." + str; 
    File file = new File(this.mFile, paramString1);
    try {
      file.createNewFile();
      RawDocumentFile rawDocumentFile = new RawDocumentFile();
      this(this, file);
    } catch (IOException iOException) {
      Log.w("DocumentFile", "Failed to createFile: " + iOException);
      iOException = null;
    } 
    return (DocumentFile)iOException;
  }
  
  public boolean delete() {
    deleteContents(this.mFile);
    return this.mFile.delete();
  }
  
  public boolean exists() {
    return this.mFile.exists();
  }
  
  public String getName() {
    return this.mFile.getName();
  }
  
  public String getType() {
    return this.mFile.isDirectory() ? null : getTypeForName(this.mFile.getName());
  }
  
  public Uri getUri() {
    return Uri.fromFile(this.mFile);
  }
  
  public boolean isDirectory() {
    return this.mFile.isDirectory();
  }
  
  public boolean isFile() {
    return this.mFile.isFile();
  }
  
  public boolean isVirtual() {
    return false;
  }
  
  public long lastModified() {
    return this.mFile.lastModified();
  }
  
  public long length() {
    return this.mFile.length();
  }
  
  public DocumentFile[] listFiles() {
    ArrayList<RawDocumentFile> arrayList = new ArrayList();
    File[] arrayOfFile = this.mFile.listFiles();
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      for (byte b = 0; b < i; b++)
        arrayList.add(new RawDocumentFile(this, arrayOfFile[b])); 
    } 
    return arrayList.<DocumentFile>toArray(new DocumentFile[arrayList.size()]);
  }
  
  public boolean renameTo(String paramString) {
    File file = new File(this.mFile.getParentFile(), paramString);
    if (this.mFile.renameTo(file)) {
      this.mFile = file;
      return true;
    } 
    return false;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/provider/RawDocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */