package org.xutils.http.app;

import java.io.InputStream;
import java.lang.reflect.Type;

public abstract class InputStreamResponseParser implements ResponseParser {
  public abstract Object parse(Type paramType, Class<?> paramClass, InputStream paramInputStream) throws Throwable;
  
  @Deprecated
  public final Object parse(Type paramType, Class<?> paramClass, String paramString) throws Throwable {
    return null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/InputStreamResponseParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */