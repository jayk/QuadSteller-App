package org.xutils.http;

public enum HttpMethod {
  CONNECT,
  COPY,
  DELETE,
  GET("GET"),
  HEAD("GET"),
  MOVE("GET"),
  OPTIONS("GET"),
  PATCH("GET"),
  POST("POST"),
  PUT("PUT"),
  TRACE("PUT");
  
  private final String value;
  
  static {
    PATCH = new HttpMethod("PATCH", 3, "PATCH");
    HEAD = new HttpMethod("HEAD", 4, "HEAD");
    MOVE = new HttpMethod("MOVE", 5, "MOVE");
    COPY = new HttpMethod("COPY", 6, "COPY");
    DELETE = new HttpMethod("DELETE", 7, "DELETE");
    OPTIONS = new HttpMethod("OPTIONS", 8, "OPTIONS");
    TRACE = new HttpMethod("TRACE", 9, "TRACE");
    CONNECT = new HttpMethod("CONNECT", 10, "CONNECT");
    $VALUES = new HttpMethod[] { 
        GET, POST, PUT, PATCH, HEAD, MOVE, COPY, DELETE, OPTIONS, TRACE, 
        CONNECT };
  }
  
  HttpMethod(String paramString1) {
    this.value = paramString1;
  }
  
  public static boolean permitsCache(HttpMethod paramHttpMethod) {
    return (paramHttpMethod == GET || paramHttpMethod == POST);
  }
  
  public static boolean permitsRequestBody(HttpMethod paramHttpMethod) {
    return (paramHttpMethod == POST || paramHttpMethod == PUT || paramHttpMethod == PATCH || paramHttpMethod == DELETE);
  }
  
  public static boolean permitsRetry(HttpMethod paramHttpMethod) {
    return (paramHttpMethod == GET);
  }
  
  public String toString() {
    return this.value;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/HttpMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */