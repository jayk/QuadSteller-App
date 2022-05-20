package com.blankj.utilcode.util;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {
  private static final int BUFFER_LEN = 8192;
  
  private ZipUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  private static boolean createOrExistsFile(File paramFile) {
    boolean bool1 = false;
    if (paramFile == null)
      return bool1; 
    if (paramFile.exists())
      return paramFile.isFile(); 
    boolean bool2 = bool1;
    if (createOrExistsDir(paramFile.getParentFile()))
      try {
        bool2 = paramFile.createNewFile();
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bool2 = bool1;
      }  
    return bool2;
  }
  
  public static List<String> getComments(File paramFile) throws IOException {
    if (paramFile == null)
      return null; 
    ArrayList<String> arrayList = new ArrayList();
    Enumeration<? extends ZipEntry> enumeration = (new ZipFile(paramFile)).entries();
    while (true) {
      ArrayList<String> arrayList1 = arrayList;
      if (enumeration.hasMoreElements()) {
        arrayList.add(((ZipEntry)enumeration.nextElement()).getComment());
        continue;
      } 
      return arrayList1;
    } 
  }
  
  public static List<String> getComments(String paramString) throws IOException {
    return getComments(getFileByPath(paramString));
  }
  
  private static File getFileByPath(String paramString) {
    return isSpace(paramString) ? null : new File(paramString);
  }
  
  public static List<String> getFilesPath(File paramFile) throws IOException {
    if (paramFile == null)
      return null; 
    ArrayList<String> arrayList = new ArrayList();
    Enumeration<? extends ZipEntry> enumeration = (new ZipFile(paramFile)).entries();
    while (true) {
      ArrayList<String> arrayList1 = arrayList;
      if (enumeration.hasMoreElements()) {
        arrayList.add(((ZipEntry)enumeration.nextElement()).getName());
        continue;
      } 
      return arrayList1;
    } 
  }
  
  public static List<String> getFilesPath(String paramString) throws IOException {
    return getFilesPath(getFileByPath(paramString));
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  private static boolean unzipChildFile(File paramFile, List<File> paramList, ZipFile paramZipFile, ZipEntry paramZipEntry, String paramString) throws IOException {
    File file = new File(paramFile + File.separator + paramString);
    paramList.add(file);
    if (paramZipEntry.isDirectory()) {
      if (!createOrExistsDir(file))
        return false; 
    } else {
      String str;
      if (!createOrExistsFile(file))
        return false; 
      File file1 = null;
      paramString = null;
      try {
        BufferedInputStream bufferedInputStream = new BufferedInputStream();
      } finally {
        paramList = null;
        paramFile = file1;
      } 
      CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile, (Closeable)str });
      throw paramList;
    } 
    return true;
  }
  
  public static List<File> unzipFile(File paramFile1, File paramFile2) throws IOException {
    return unzipFileByKeyword(paramFile1, paramFile2, (String)null);
  }
  
  public static List<File> unzipFile(String paramString1, String paramString2) throws IOException {
    return unzipFileByKeyword(paramString1, paramString2, (String)null);
  }
  
  public static List<File> unzipFileByKeyword(File paramFile1, File paramFile2, String paramString) throws IOException {
    if (paramFile1 == null || paramFile2 == null)
      return null; 
    ArrayList<File> arrayList = new ArrayList();
    ZipFile zipFile = new ZipFile(paramFile1);
    Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
    if (isSpace(paramString))
      while (true) {
        ArrayList<File> arrayList1 = arrayList;
        if (enumeration.hasMoreElements()) {
          ZipEntry zipEntry = enumeration.nextElement();
          if (!unzipChildFile(paramFile2, arrayList, zipFile, zipEntry, zipEntry.getName()))
            return arrayList; 
          continue;
        } 
        return arrayList1;
      }  
    while (true) {
      ArrayList<File> arrayList1 = arrayList;
      if (enumeration.hasMoreElements()) {
        ZipEntry zipEntry = enumeration.nextElement();
        String str = zipEntry.getName();
        if (str.contains(paramString) && !unzipChildFile(paramFile2, arrayList, zipFile, zipEntry, str))
          return arrayList; 
        continue;
      } 
      return arrayList1;
    } 
  }
  
  public static List<File> unzipFileByKeyword(String paramString1, String paramString2, String paramString3) throws IOException {
    return unzipFileByKeyword(getFileByPath(paramString1), getFileByPath(paramString2), paramString3);
  }
  
  public static boolean zipFile(File paramFile1, File paramFile2) throws IOException {
    return zipFile(paramFile1, paramFile2, (String)null);
  }
  
  public static boolean zipFile(File paramFile1, File paramFile2, String paramString) throws IOException {
    if (paramFile1 == null || paramFile2 == null)
      return false; 
    File file = null;
    try {
      ZipOutputStream zipOutputStream = new ZipOutputStream();
      FileOutputStream fileOutputStream = new FileOutputStream();
      this(paramFile2);
    } finally {
      paramFile2 = file;
      if (paramFile2 != null)
        CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile2 }); 
    } 
  }
  
  private static boolean zipFile(File paramFile, String paramString1, ZipOutputStream paramZipOutputStream, String paramString2) throws IOException {
    ZipEntry zipEntry;
    StringBuilder stringBuilder = (new StringBuilder()).append(paramString1);
    if (isSpace(paramString1)) {
      paramString1 = "";
    } else {
      paramString1 = File.separator;
    } 
    String str = stringBuilder.append(paramString1).append(paramFile.getName()).toString();
    if (paramFile.isDirectory()) {
      File[] arrayOfFile = paramFile.listFiles();
      if (arrayOfFile == null || arrayOfFile.length <= 0) {
        zipEntry = new ZipEntry(str + '/');
        if (!isSpace(paramString2))
          zipEntry.setComment(paramString2); 
        paramZipOutputStream.putNextEntry(zipEntry);
        paramZipOutputStream.closeEntry();
      } else {
        int i = zipEntry.length;
        byte b = 0;
        while (true) {
          if (b < i) {
            if (!zipFile((File)zipEntry[b], str, paramZipOutputStream, paramString2))
              return false; 
            b++;
            continue;
          } 
          return true;
        } 
      } 
    } else {
      StringBuilder stringBuilder1;
      stringBuilder = null;
      try {
        BufferedInputStream bufferedInputStream = new BufferedInputStream();
        FileInputStream fileInputStream = new FileInputStream();
        this((File)zipEntry);
        this(fileInputStream);
      } finally {
        zipEntry = null;
      } 
      CloseUtils.closeIO(new Closeable[] { (Closeable)stringBuilder1 });
      throw zipEntry;
    } 
    return true;
  }
  
  public static boolean zipFile(String paramString1, String paramString2) throws IOException {
    return zipFile(paramString1, paramString2, (String)null);
  }
  
  public static boolean zipFile(String paramString1, String paramString2, String paramString3) throws IOException {
    return zipFile(getFileByPath(paramString1), getFileByPath(paramString2), paramString3);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ZipUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */