package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.InputStreamResponseParser;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

class ObjectLoader extends Loader<Object> {
  private String charset = "UTF-8";
  
  private final Class<?> objectClass;
  
  private final Type objectType;
  
  private final ResponseParser parser;
  
  private String resultStr = null;
  
  public ObjectLoader(Type paramType) {
    this.objectType = paramType;
    if (paramType instanceof ParameterizedType) {
      this.objectClass = (Class)((ParameterizedType)paramType).getRawType();
    } else {
      if (paramType instanceof java.lang.reflect.TypeVariable)
        throw new IllegalArgumentException("not support callback type " + paramType.toString()); 
      this.objectClass = (Class)paramType;
    } 
    if (List.class.equals(this.objectClass)) {
      Type type = ParameterizedTypeUtil.getParameterizedType(this.objectType, List.class, 0);
      if (type instanceof ParameterizedType) {
        paramType = ((ParameterizedType)type).getRawType();
      } else {
        if (type instanceof java.lang.reflect.TypeVariable)
          throw new IllegalArgumentException("not support callback type " + type.toString()); 
        paramType = type;
      } 
      HttpResponse httpResponse1 = (HttpResponse)paramType.getAnnotation(HttpResponse.class);
      if (httpResponse1 != null)
        try {
          this.parser = httpResponse1.parser().newInstance();
          return;
        } catch (Throwable throwable) {
          throw new RuntimeException("create parser error", throwable);
        }  
      throw new IllegalArgumentException("not found @HttpResponse from " + type);
    } 
    HttpResponse httpResponse = this.objectClass.<HttpResponse>getAnnotation(HttpResponse.class);
    if (httpResponse != null)
      try {
        this.parser = httpResponse.parser().newInstance();
        return;
      } catch (Throwable throwable) {
        throw new RuntimeException("create parser error", throwable);
      }  
    throw new IllegalArgumentException("not found @HttpResponse from " + this.objectType);
  }
  
  public Object load(InputStream paramInputStream) throws Throwable {
    if (this.parser instanceof InputStreamResponseParser)
      return ((InputStreamResponseParser)this.parser).parse(this.objectType, this.objectClass, paramInputStream); 
    this.resultStr = IOUtil.readStr(paramInputStream, this.charset);
    return this.parser.parse(this.objectType, this.objectClass, this.resultStr);
  }
  
  public Object load(UriRequest paramUriRequest) throws Throwable {
    try {
      paramUriRequest.sendRequest();
      return load(paramUriRequest.getInputStream());
    } finally {
      this.parser.checkResponse(paramUriRequest);
    } 
  }
  
  public Object loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    if (paramDiskCacheEntity != null) {
      String str = paramDiskCacheEntity.getTextContent();
      if (!TextUtils.isEmpty(str))
        return this.parser.parse(this.objectType, this.objectClass, str); 
    } 
    return null;
  }
  
  public Loader<Object> newInstance() {
    throw new IllegalAccessError("use constructor create ObjectLoader.");
  }
  
  public void save2Cache(UriRequest paramUriRequest) {
    saveStringCache(paramUriRequest, this.resultStr);
  }
  
  public void setParams(RequestParams paramRequestParams) {
    if (paramRequestParams != null) {
      String str = paramRequestParams.getCharset();
      if (!TextUtils.isEmpty(str))
        this.charset = str; 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/ObjectLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */