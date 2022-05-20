package okhttp3.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import okio.Okio;
import okio.Sink;
import okio.Source;

public interface FileSystem {
  public static final FileSystem SYSTEM = new FileSystem() {
      public Sink appendingSink(File param1File) throws FileNotFoundException {
        Sink sink;
        try {
          Sink sink1 = Okio.appendingSink(param1File);
          sink = sink1;
        } catch (FileNotFoundException fileNotFoundException) {
          sink.getParentFile().mkdirs();
          sink = Okio.appendingSink((File)sink);
        } 
        return sink;
      }
      
      public void delete(File param1File) throws IOException {
        if (!param1File.delete() && param1File.exists())
          throw new IOException("failed to delete " + param1File); 
      }
      
      public void deleteContents(File param1File) throws IOException {
        File[] arrayOfFile = param1File.listFiles();
        if (arrayOfFile == null)
          throw new IOException("not a readable directory: " + param1File); 
        int i = arrayOfFile.length;
        for (byte b = 0; b < i; b++) {
          param1File = arrayOfFile[b];
          if (param1File.isDirectory())
            deleteContents(param1File); 
          if (!param1File.delete())
            throw new IOException("failed to delete " + param1File); 
        } 
      }
      
      public boolean exists(File param1File) {
        return param1File.exists();
      }
      
      public void rename(File param1File1, File param1File2) throws IOException {
        delete(param1File2);
        if (!param1File1.renameTo(param1File2))
          throw new IOException("failed to rename " + param1File1 + " to " + param1File2); 
      }
      
      public Sink sink(File param1File) throws FileNotFoundException {
        Sink sink;
        try {
          Sink sink1 = Okio.sink(param1File);
          sink = sink1;
        } catch (FileNotFoundException fileNotFoundException) {
          sink.getParentFile().mkdirs();
          sink = Okio.sink((File)sink);
        } 
        return sink;
      }
      
      public long size(File param1File) {
        return param1File.length();
      }
      
      public Source source(File param1File) throws FileNotFoundException {
        return Okio.source(param1File);
      }
    };
  
  Sink appendingSink(File paramFile) throws FileNotFoundException;
  
  void delete(File paramFile) throws IOException;
  
  void deleteContents(File paramFile) throws IOException;
  
  boolean exists(File paramFile);
  
  void rename(File paramFile1, File paramFile2) throws IOException;
  
  Sink sink(File paramFile) throws FileNotFoundException;
  
  long size(File paramFile);
  
  Source source(File paramFile) throws FileNotFoundException;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/io/FileSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */