package okhttp3.internal.cache;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class FaultHidingSink extends ForwardingSink {
  private boolean hasErrors;
  
  FaultHidingSink(Sink paramSink) {
    super(paramSink);
  }
  
  public void close() throws IOException {
    if (!this.hasErrors)
      try {
        super.close();
      } catch (IOException iOException) {
        this.hasErrors = true;
        onException(iOException);
      }  
  }
  
  public void flush() throws IOException {
    if (!this.hasErrors)
      try {
        super.flush();
      } catch (IOException iOException) {
        this.hasErrors = true;
        onException(iOException);
      }  
  }
  
  protected void onException(IOException paramIOException) {}
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    if (this.hasErrors) {
      paramBuffer.skip(paramLong);
      return;
    } 
    try {
      super.write(paramBuffer, paramLong);
    } catch (IOException iOException) {
      this.hasErrors = true;
      onException(iOException);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache/FaultHidingSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */