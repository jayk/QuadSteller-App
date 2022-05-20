package org.xutils.http.app;

import java.lang.reflect.Type;
import org.xutils.http.request.UriRequest;

public interface ResponseParser {
  void checkResponse(UriRequest paramUriRequest) throws Throwable;
  
  Object parse(Type paramType, Class<?> paramClass, String paramString) throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/ResponseParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */