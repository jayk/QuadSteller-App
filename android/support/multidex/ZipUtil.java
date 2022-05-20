package android.support.multidex;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.ZipException;

final class ZipUtil {
  private static final int BUFFER_SIZE = 16384;
  
  private static final int ENDHDR = 22;
  
  private static final int ENDSIG = 101010256;
  
  static long computeCrcOfCentralDir(RandomAccessFile paramRandomAccessFile, CentralDirectory paramCentralDirectory) throws IOException {
    CRC32 cRC32 = new CRC32();
    long l = paramCentralDirectory.size;
    paramRandomAccessFile.seek(paramCentralDirectory.offset);
    int i = (int)Math.min(16384L, l);
    byte[] arrayOfByte = new byte[16384];
    i = paramRandomAccessFile.read(arrayOfByte, 0, i);
    while (true) {
      if (i != -1) {
        cRC32.update(arrayOfByte, 0, i);
        l -= i;
        if (l != 0L) {
          i = paramRandomAccessFile.read(arrayOfByte, 0, (int)Math.min(16384L, l));
          continue;
        } 
      } 
      return cRC32.getValue();
    } 
  }
  
  static CentralDirectory findCentralDirectory(RandomAccessFile paramRandomAccessFile) throws IOException, ZipException {
    long l1 = paramRandomAccessFile.length() - 22L;
    if (l1 < 0L)
      throw new ZipException("File too short to be a zip file: " + paramRandomAccessFile.length()); 
    long l2 = l1 - 65536L;
    long l3 = l2;
    if (l2 < 0L)
      l3 = 0L; 
    int i = Integer.reverseBytes(101010256);
    while (true) {
      paramRandomAccessFile.seek(l1);
      if (paramRandomAccessFile.readInt() == i) {
        paramRandomAccessFile.skipBytes(2);
        paramRandomAccessFile.skipBytes(2);
        paramRandomAccessFile.skipBytes(2);
        paramRandomAccessFile.skipBytes(2);
        CentralDirectory centralDirectory = new CentralDirectory();
        centralDirectory.size = Integer.reverseBytes(paramRandomAccessFile.readInt()) & 0xFFFFFFFFL;
        centralDirectory.offset = Integer.reverseBytes(paramRandomAccessFile.readInt()) & 0xFFFFFFFFL;
        return centralDirectory;
      } 
      l2 = l1 - 1L;
      l1 = l2;
      if (l2 < l3)
        throw new ZipException("End Of Central Directory signature not found"); 
    } 
  }
  
  static long getZipCrc(File paramFile) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "r");
    try {
      return computeCrcOfCentralDir(randomAccessFile, findCentralDirectory(randomAccessFile));
    } finally {
      randomAccessFile.close();
    } 
  }
  
  static class CentralDirectory {
    long offset;
    
    long size;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/multidex/ZipUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */