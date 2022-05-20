package org.xutils.common.util;

import android.database.Cursor;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class IOUtil {
  public static void closeQuietly(Cursor paramCursor) {
    if (paramCursor != null)
      try {
        paramCursor.close();
      } catch (Throwable throwable) {
        LogUtil.d(throwable.getMessage(), throwable);
      }  
  }
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (Throwable throwable) {
        LogUtil.d(throwable.getMessage(), throwable);
      }  
  }
  
  public static void copy(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    InputStream inputStream = paramInputStream;
    if (!(paramInputStream instanceof BufferedInputStream))
      inputStream = new BufferedInputStream(paramInputStream); 
    OutputStream outputStream = paramOutputStream;
    if (!(paramOutputStream instanceof BufferedOutputStream))
      outputStream = new BufferedOutputStream(paramOutputStream); 
    byte[] arrayOfByte = new byte[1024];
    while (true) {
      int i = inputStream.read(arrayOfByte);
      if (i != -1) {
        outputStream.write(arrayOfByte, 0, i);
        continue;
      } 
      outputStream.flush();
      return;
    } 
  }
  
  public static boolean deleteFileOrDir(File paramFile) {
    if (paramFile == null || !paramFile.exists())
      return true; 
    if (paramFile.isFile())
      return paramFile.delete(); 
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      for (byte b = 0; b < i; b++)
        deleteFileOrDir(arrayOfFile[b]); 
    } 
    return paramFile.delete();
  }
  
  public static byte[] readBytes(InputStream paramInputStream) throws IOException {
    InputStream inputStream1 = paramInputStream;
    if (!(paramInputStream instanceof BufferedInputStream))
      inputStream1 = new BufferedInputStream(paramInputStream); 
    InputStream inputStream2 = null;
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
    } finally {
      paramInputStream = null;
    } 
    closeQuietly(inputStream1);
    throw paramInputStream;
  }
  
  public static byte[] readBytes(InputStream paramInputStream, long paramLong, int paramInt) throws IOException {
    if (paramLong > 0L)
      while (paramLong > 0L) {
        long l = paramInputStream.skip(paramLong);
        if (l > 0L)
          paramLong -= l; 
      }  
    byte[] arrayOfByte = new byte[paramInt];
    for (byte b = 0; b < paramInt; b++)
      arrayOfByte[b] = (byte)(byte)paramInputStream.read(); 
    return arrayOfByte;
  }
  
  public static String readStr(InputStream paramInputStream) throws IOException {
    return readStr(paramInputStream, "UTF-8");
  }
  
  public static String readStr(InputStream paramInputStream, String paramString) throws IOException {
    String str = paramString;
    if (TextUtils.isEmpty(paramString))
      str = "UTF-8"; 
    InputStream inputStream = paramInputStream;
    if (!(paramInputStream instanceof BufferedInputStream))
      inputStream = new BufferedInputStream(paramInputStream); 
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, str);
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = new char[1024];
    while (true) {
      int i = inputStreamReader.read(arrayOfChar);
      if (i >= 0) {
        stringBuilder.append(arrayOfChar, 0, i);
        continue;
      } 
      return stringBuilder.toString();
    } 
  }
  
  public static void writeStr(OutputStream paramOutputStream, String paramString) throws IOException {
    writeStr(paramOutputStream, paramString, "UTF-8");
  }
  
  public static void writeStr(OutputStream paramOutputStream, String paramString1, String paramString2) throws IOException {
    String str = paramString2;
    if (TextUtils.isEmpty(paramString2))
      str = "UTF-8"; 
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(paramOutputStream, str);
    outputStreamWriter.write(paramString1);
    outputStreamWriter.flush();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/IOUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */