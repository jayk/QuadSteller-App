package com.tencent.bugly.proguard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ad implements ab {
  public final byte[] a(byte[] paramArrayOfbyte) throws Exception {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
    ZipEntry zipEntry = new ZipEntry("zip");
    zipEntry.setSize(paramArrayOfbyte.length);
    zipOutputStream.putNextEntry(zipEntry);
    zipOutputStream.write(paramArrayOfbyte);
    zipOutputStream.closeEntry();
    zipOutputStream.close();
    paramArrayOfbyte = byteArrayOutputStream.toByteArray();
    byteArrayOutputStream.close();
    return paramArrayOfbyte;
  }
  
  public final byte[] b(byte[] paramArrayOfbyte) throws Exception {
    byte[] arrayOfByte = null;
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramArrayOfbyte);
    ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
    paramArrayOfbyte = arrayOfByte;
    while (zipInputStream.getNextEntry() != null) {
      paramArrayOfbyte = new byte[1024];
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      while (true) {
        int i = zipInputStream.read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
        if (i != -1) {
          byteArrayOutputStream.write(paramArrayOfbyte, 0, i);
          continue;
        } 
        paramArrayOfbyte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
      } 
    } 
    zipInputStream.close();
    byteArrayInputStream.close();
    return paramArrayOfbyte;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/ad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */