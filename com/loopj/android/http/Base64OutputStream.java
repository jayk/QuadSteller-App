package com.loopj.android.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream extends FilterOutputStream {
  private static byte[] EMPTY = new byte[0];
  
  private int bpos = 0;
  
  private byte[] buffer = null;
  
  private final Base64.Coder coder;
  
  private final int flags;
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt) {
    this(paramOutputStream, paramInt, true);
  }
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt, boolean paramBoolean) {
    super(paramOutputStream);
    this.flags = paramInt;
    if (paramBoolean) {
      this.coder = new Base64.Encoder(paramInt, null);
      return;
    } 
    this.coder = new Base64.Decoder(paramInt, null);
  }
  
  private byte[] embiggen(byte[] paramArrayOfbyte, int paramInt) {
    if (paramArrayOfbyte != null) {
      byte[] arrayOfByte = paramArrayOfbyte;
      return (paramArrayOfbyte.length < paramInt) ? new byte[paramInt] : arrayOfByte;
    } 
    return new byte[paramInt];
  }
  
  private void flushBuffer() throws IOException {
    if (this.bpos > 0) {
      internalWrite(this.buffer, 0, this.bpos, false);
      this.bpos = 0;
    } 
  }
  
  private void internalWrite(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean) throws IOException {
    this.coder.output = embiggen(this.coder.output, this.coder.maxOutputSize(paramInt2));
    if (!this.coder.process(paramArrayOfbyte, paramInt1, paramInt2, paramBoolean))
      throw new Base64DataException("bad base-64"); 
    this.out.write(this.coder.output, 0, this.coder.op);
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: invokespecial flushBuffer : ()V
    //   6: aload_0
    //   7: getstatic com/loopj/android/http/Base64OutputStream.EMPTY : [B
    //   10: iconst_0
    //   11: iconst_0
    //   12: iconst_1
    //   13: invokespecial internalWrite : ([BIIZ)V
    //   16: aload_0
    //   17: getfield flags : I
    //   20: bipush #16
    //   22: iand
    //   23: ifne -> 45
    //   26: aload_0
    //   27: getfield out : Ljava/io/OutputStream;
    //   30: invokevirtual close : ()V
    //   33: aload_1
    //   34: astore_2
    //   35: aload_2
    //   36: ifnull -> 69
    //   39: aload_2
    //   40: athrow
    //   41: astore_1
    //   42: goto -> 16
    //   45: aload_0
    //   46: getfield out : Ljava/io/OutputStream;
    //   49: invokevirtual flush : ()V
    //   52: aload_1
    //   53: astore_2
    //   54: goto -> 35
    //   57: astore_3
    //   58: aload_1
    //   59: astore_2
    //   60: aload_1
    //   61: ifnull -> 35
    //   64: aload_3
    //   65: astore_2
    //   66: goto -> 35
    //   69: return
    // Exception table:
    //   from	to	target	type
    //   2	16	41	java/io/IOException
    //   16	33	57	java/io/IOException
    //   45	52	57	java/io/IOException
  }
  
  public void write(int paramInt) throws IOException {
    if (this.buffer == null)
      this.buffer = new byte[1024]; 
    if (this.bpos >= this.buffer.length) {
      internalWrite(this.buffer, 0, this.bpos, false);
      this.bpos = 0;
    } 
    byte[] arrayOfByte = this.buffer;
    int i = this.bpos;
    this.bpos = i + 1;
    arrayOfByte[i] = (byte)(byte)paramInt;
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 > 0) {
      flushBuffer();
      internalWrite(paramArrayOfbyte, paramInt1, paramInt2, false);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/Base64OutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */