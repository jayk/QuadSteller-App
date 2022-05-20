package org.xutils.http.request;

import android.text.TextUtils;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.app.RequestTracker;

public final class UriRequestFactory {
  private static final HashMap<String, Class<? extends UriRequest>> SCHEME_CLS_MAP = new HashMap<String, Class<? extends UriRequest>>();
  
  private static Class<? extends RequestTracker> defaultTrackerCls;
  
  public static RequestTracker getDefaultTracker() {
    try {
      if (defaultTrackerCls == null)
        return null; 
      RequestTracker requestTracker = defaultTrackerCls.newInstance();
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
      throwable = null;
    } 
    return (RequestTracker)throwable;
  }
  
  public static UriRequest getUriRequest(RequestParams paramRequestParams, Type paramType) throws Throwable {
    String str1 = null;
    String str2 = paramRequestParams.getUri();
    int i = str2.indexOf(":");
    if (i > 0) {
      str1 = str2.substring(0, i);
    } else if (str2.startsWith("/")) {
      str1 = "file";
    } 
    if (!TextUtils.isEmpty(str1)) {
      Class<UriRequest> clazz = (Class)SCHEME_CLS_MAP.get(str1);
      if (clazz != null)
        return clazz.getConstructor(new Class[] { RequestParams.class, Class.class }).newInstance(new Object[] { paramRequestParams, paramType }); 
      if (str1.startsWith("http"))
        return new HttpRequest(paramRequestParams, paramType); 
      if (str1.equals("assets"))
        return new AssetsRequest(paramRequestParams, paramType); 
      if (str1.equals("file"))
        return new LocalFileRequest(paramRequestParams, paramType); 
      throw new IllegalArgumentException("The url not be support: " + str2);
    } 
    throw new IllegalArgumentException("The url not be support: " + str2);
  }
  
  public static void registerDefaultTrackerClass(Class<? extends RequestTracker> paramClass) {
    defaultTrackerCls = paramClass;
  }
  
  public static void registerRequestClass(String paramString, Class<? extends UriRequest> paramClass) {
    SCHEME_CLS_MAP.put(paramString, paramClass);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/request/UriRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */