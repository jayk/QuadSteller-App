package okio;

import java.io.IOException;

public final class Pipe {
  final Buffer buffer = new Buffer();
  
  final long maxBufferSize;
  
  private final Sink sink = new PipeSink();
  
  boolean sinkClosed;
  
  private final Source source = new PipeSource();
  
  boolean sourceClosed;
  
  public Pipe(long paramLong) {
    if (paramLong < 1L)
      throw new IllegalArgumentException("maxBufferSize < 1: " + paramLong); 
    this.maxBufferSize = paramLong;
  }
  
  public Sink sink() {
    return this.sink;
  }
  
  public Source source() {
    return this.source;
  }
  
  final class PipeSink implements Sink {
    final Timeout timeout = new Timeout();
    
    public void close() throws IOException {
      synchronized (Pipe.this.buffer) {
        if (Pipe.this.sinkClosed)
          return; 
        if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
          IOException iOException = new IOException();
          this("source is closed");
          throw iOException;
        } 
      } 
      Pipe.this.sinkClosed = true;
      Pipe.this.buffer.notifyAll();
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_1} */
    }
    
    public void flush() throws IOException {
      synchronized (Pipe.this.buffer) {
        if (Pipe.this.sinkClosed) {
          IllegalStateException illegalStateException = new IllegalStateException();
          this("closed");
          throw illegalStateException;
        } 
      } 
      if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0L) {
        IOException iOException = new IOException();
        this("source is closed");
        throw iOException;
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_1} */
    }
    
    public Timeout timeout() {
      return this.timeout;
    }
    
    public void write(Buffer param1Buffer, long param1Long) throws IOException {
      synchronized (Pipe.this.buffer) {
        if (Pipe.this.sinkClosed) {
          IllegalStateException illegalStateException = new IllegalStateException();
          this("closed");
          throw illegalStateException;
        } 
      } 
      while (param1Long > 0L) {
        IOException iOException;
        if (Pipe.this.sourceClosed) {
          iOException = new IOException();
          this("source is closed");
          throw iOException;
        } 
        long l = Pipe.this.maxBufferSize - Pipe.this.buffer.size();
        if (l == 0L) {
          this.timeout.waitUntilNotified(Pipe.this.buffer);
          continue;
        } 
        l = Math.min(l, param1Long);
        Pipe.this.buffer.write((Buffer)iOException, l);
        param1Long -= l;
        Pipe.this.buffer.notifyAll();
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_4} */
    }
  }
  
  final class PipeSource implements Source {
    final Timeout timeout = new Timeout();
    
    public void close() throws IOException {
      synchronized (Pipe.this.buffer) {
        Pipe.this.sourceClosed = true;
        Pipe.this.buffer.notifyAll();
        return;
      } 
    }
    
    public long read(Buffer param1Buffer, long param1Long) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lokio/Pipe;
      //   4: getfield buffer : Lokio/Buffer;
      //   7: astore #4
      //   9: aload #4
      //   11: monitorenter
      //   12: aload_0
      //   13: getfield this$0 : Lokio/Pipe;
      //   16: getfield sourceClosed : Z
      //   19: ifeq -> 54
      //   22: new java/lang/IllegalStateException
      //   25: astore_1
      //   26: aload_1
      //   27: ldc 'closed'
      //   29: invokespecial <init> : (Ljava/lang/String;)V
      //   32: aload_1
      //   33: athrow
      //   34: astore_1
      //   35: aload #4
      //   37: monitorexit
      //   38: aload_1
      //   39: athrow
      //   40: aload_0
      //   41: getfield timeout : Lokio/Timeout;
      //   44: aload_0
      //   45: getfield this$0 : Lokio/Pipe;
      //   48: getfield buffer : Lokio/Buffer;
      //   51: invokevirtual waitUntilNotified : (Ljava/lang/Object;)V
      //   54: aload_0
      //   55: getfield this$0 : Lokio/Pipe;
      //   58: getfield buffer : Lokio/Buffer;
      //   61: invokevirtual size : ()J
      //   64: lconst_0
      //   65: lcmp
      //   66: ifne -> 88
      //   69: aload_0
      //   70: getfield this$0 : Lokio/Pipe;
      //   73: getfield sinkClosed : Z
      //   76: ifeq -> 40
      //   79: ldc2_w -1
      //   82: lstore_2
      //   83: aload #4
      //   85: monitorexit
      //   86: lload_2
      //   87: lreturn
      //   88: aload_0
      //   89: getfield this$0 : Lokio/Pipe;
      //   92: getfield buffer : Lokio/Buffer;
      //   95: aload_1
      //   96: lload_2
      //   97: invokevirtual read : (Lokio/Buffer;J)J
      //   100: lstore_2
      //   101: aload_0
      //   102: getfield this$0 : Lokio/Pipe;
      //   105: getfield buffer : Lokio/Buffer;
      //   108: invokevirtual notifyAll : ()V
      //   111: aload #4
      //   113: monitorexit
      //   114: goto -> 86
      // Exception table:
      //   from	to	target	type
      //   12	34	34	finally
      //   35	38	34	finally
      //   40	54	34	finally
      //   54	79	34	finally
      //   83	86	34	finally
      //   88	114	34	finally
    }
    
    public Timeout timeout() {
      return this.timeout;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Pipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */