package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
  public static final Authenticator NONE = new Authenticator() {
      public Request authenticate(Route param1Route, Response param1Response) {
        return null;
      }
    };
  
  @Nullable
  Request authenticate(Route paramRoute, Response paramResponse) throws IOException;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Authenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */