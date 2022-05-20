package okhttp3;

import java.net.Socket;
import javax.annotation.Nullable;

public interface Connection {
  @Nullable
  Handshake handshake();
  
  Protocol protocol();
  
  Route route();
  
  Socket socket();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */