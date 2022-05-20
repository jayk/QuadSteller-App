package org.xutils.http.body;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

public class FileBody extends InputStreamBody {
  private String contentType;
  
  private File file;
  
  public FileBody(File paramFile) throws IOException {
    this(paramFile, null);
  }
  
  public FileBody(File paramFile, String paramString) throws IOException {
    super(new FileInputStream(paramFile));
    this.file = paramFile;
    this.contentType = paramString;
  }
  
  public static String getFileContentType(File paramFile) {
    null = HttpURLConnection.guessContentTypeFromName(paramFile.getName());
    return TextUtils.isEmpty(null) ? "application/octet-stream" : null.replaceFirst("\\/jpg$", "/jpeg");
  }
  
  public String getContentType() {
    if (TextUtils.isEmpty(this.contentType))
      this.contentType = getFileContentType(this.file); 
    return this.contentType;
  }
  
  public void setContentType(String paramString) {
    this.contentType = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/FileBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */