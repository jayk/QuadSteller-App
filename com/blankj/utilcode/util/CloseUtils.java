package com.blankj.utilcode.util;

import java.io.Closeable;
import java.io.IOException;

public final class CloseUtils {
  private CloseUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void closeIO(Closeable... paramVarArgs) {
    if (paramVarArgs != null) {
      int i = paramVarArgs.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          Closeable closeable = paramVarArgs[b];
          if (closeable != null)
            try {
              closeable.close();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            }  
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void closeIOQuietly(Closeable... paramVarArgs) {
    if (paramVarArgs != null) {
      int i = paramVarArgs.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          Closeable closeable = paramVarArgs[b];
          if (closeable != null)
            try {
              closeable.close();
            } catch (IOException iOException) {} 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/CloseUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */