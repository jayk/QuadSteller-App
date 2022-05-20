package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Interceptor {
  Response intercept(Chain paramChain) throws IOException;
  
  public static interface Chain {
    @Nullable
    Connection connection();
    
    Response proceed(Request param1Request) throws IOException;
    
    Request request();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Interceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */