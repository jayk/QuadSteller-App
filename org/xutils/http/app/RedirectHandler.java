package org.xutils.http.app;

import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public interface RedirectHandler {
  RequestParams getRedirectParams(UriRequest paramUriRequest) throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/RedirectHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */