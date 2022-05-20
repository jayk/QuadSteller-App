package okhttp3.internal.cache2;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import okio.Buffer;

final class FileOperator {
  private static final int BUFFER_SIZE = 8192;
  
  private final byte[] byteArray = new byte[8192];
  
  private final ByteBuffer byteBuffer = ByteBuffer.wrap(this.byteArray);
  
  private final FileChannel fileChannel;
  
  FileOperator(FileChannel paramFileChannel) {
    this.fileChannel = paramFileChannel;
  }
  
  public void read(long paramLong1, Buffer paramBuffer, long paramLong2) throws IOException {
    long l = paramLong1;
    paramLong1 = paramLong2;
    if (paramLong2 < 0L)
      throw new IndexOutOfBoundsException(); 
    while (paramLong1 > 0L) {
      this.byteBuffer.limit((int)Math.min(8192L, paramLong1));
      if (this.fileChannel.read(this.byteBuffer, l) == -1) {
        null = new EOFException();
        this();
        throw null;
      } 
      try {
        int i = this.byteBuffer.position();
        null.write(this.byteArray, 0, i);
        l += i;
        paramLong1 -= i;
      } finally {
        this.byteBuffer.clear();
      } 
    } 
  }
  
  public void write(long paramLong1, Buffer paramBuffer, long paramLong2) throws IOException {
    if (paramLong2 >= 0L) {
      long l = paramLong2;
      if (paramLong2 <= paramBuffer.size()) {
        while (l > 0L) {
          try {
            int i = (int)Math.min(8192L, l);
            paramBuffer.read(this.byteArray, 0, i);
            this.byteBuffer.limit(i);
            paramLong2 = paramLong1;
            while (true) {
              paramLong1 = paramLong2 + this.fileChannel.write(this.byteBuffer, paramLong2);
              boolean bool = this.byteBuffer.hasRemaining();
              paramLong2 = paramLong1;
              if (!bool) {
                l -= i;
                this.byteBuffer.clear();
              } 
            } 
          } finally {
            this.byteBuffer.clear();
          } 
        } 
        return;
      } 
    } 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache2/FileOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */