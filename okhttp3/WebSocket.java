package okhttp3;

import javax.annotation.Nullable;
import okio.ByteString;

public interface WebSocket {
  void cancel();
  
  boolean close(int paramInt, @Nullable String paramString);
  
  long queueSize();
  
  Request request();
  
  boolean send(String paramString);
  
  boolean send(ByteString paramByteString);
  
  public static interface Factory {
    WebSocket newWebSocket(Request param1Request, WebSocketListener param1WebSocketListener);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/WebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */