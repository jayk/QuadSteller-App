package org.xutils.http.app;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.request.UriRequest;

public class HttpRetryHandler {
  protected static HashSet<Class<?>> blackList = new HashSet<Class<?>>();
  
  protected int maxRetryCount = 2;
  
  static {
    blackList.add(HttpException.class);
    blackList.add(Callback.CancelledException.class);
    blackList.add(MalformedURLException.class);
    blackList.add(URISyntaxException.class);
    blackList.add(NoRouteToHostException.class);
    blackList.add(PortUnreachableException.class);
    blackList.add(ProtocolException.class);
    blackList.add(NullPointerException.class);
    blackList.add(FileNotFoundException.class);
    blackList.add(JSONException.class);
    blackList.add(UnknownHostException.class);
    blackList.add(IllegalArgumentException.class);
  }
  
  public boolean canRetry(UriRequest paramUriRequest, Throwable paramThrowable, int paramInt) {
    null = false;
    LogUtil.w(paramThrowable.getMessage(), paramThrowable);
    if (paramInt > this.maxRetryCount) {
      LogUtil.w(paramUriRequest.toString());
      LogUtil.w("The Max Retry times has been reached!");
      return null;
    } 
    if (!HttpMethod.permitsRetry(paramUriRequest.getParams().getMethod())) {
      LogUtil.w(paramUriRequest.toString());
      LogUtil.w("The Request Method can not be retried.");
      return null;
    } 
    if (blackList.contains(paramThrowable.getClass())) {
      LogUtil.w(paramUriRequest.toString());
      LogUtil.w("The Exception can not be retried.");
      return null;
    } 
    return true;
  }
  
  public void setMaxRetryCount(int paramInt) {
    this.maxRetryCount = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/HttpRetryHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */