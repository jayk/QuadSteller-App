package com.tencent.bugly.crashreport.crash.h5;

import android.webkit.JavascriptInterface;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.inner.InnerApi;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class H5JavaScriptInterface {
  private static HashSet<Integer> a = new HashSet<Integer>();
  
  private String b = null;
  
  private Thread c = null;
  
  private String d = null;
  
  private Map<String, String> e = null;
  
  private static a a(String paramString) {
    a a1 = null;
    a a2 = a1;
    if (paramString != null) {
      if (paramString.length() <= 0)
        return a1; 
    } else {
      return a2;
    } 
    try {
      JSONObject jSONObject = new JSONObject();
      this(paramString);
      a a = new a();
      this();
      a.a = jSONObject.getString("projectRoot");
      a2 = a1;
      if (a.a != null) {
        a.b = jSONObject.getString("context");
        a2 = a1;
        if (a.b != null) {
          a.c = jSONObject.getString("url");
          a2 = a1;
          if (a.c != null) {
            a.d = jSONObject.getString("userAgent");
            a2 = a1;
            if (a.d != null) {
              a.e = jSONObject.getString("language");
              a2 = a1;
              if (a.e != null) {
                a.f = jSONObject.getString("name");
                a2 = a1;
                if (a.f != null) {
                  a2 = a1;
                  if (!a.f.equals("null")) {
                    String str = jSONObject.getString("stacktrace");
                    a2 = a1;
                    if (str != null) {
                      int i = str.indexOf("\n");
                      if (i < 0) {
                        x.d("H5 crash stack's format is wrong!", new Object[0]);
                        return a1;
                      } 
                      a.h = str.substring(i + 1);
                      a.g = str.substring(0, i);
                      i = a.g.indexOf(":");
                      if (i > 0)
                        a.g = a.g.substring(i + 1); 
                      a.i = jSONObject.getString("file");
                      a2 = a1;
                      if (a.f != null) {
                        a.j = jSONObject.getLong("lineNumber");
                        a2 = a1;
                        if (a.j >= 0L) {
                          a.k = jSONObject.getLong("columnNumber");
                          a2 = a1;
                          if (a.k >= 0L) {
                            x.a("H5 crash information is following: ", new Object[0]);
                            StringBuilder stringBuilder = new StringBuilder();
                            this("[projectRoot]: ");
                            x.a(stringBuilder.append(a.a).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[context]: ");
                            x.a(stringBuilder.append(a.b).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[url]: ");
                            x.a(stringBuilder.append(a.c).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[userAgent]: ");
                            x.a(stringBuilder.append(a.d).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[language]: ");
                            x.a(stringBuilder.append(a.e).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[name]: ");
                            x.a(stringBuilder.append(a.f).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[message]: ");
                            x.a(stringBuilder.append(a.g).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[stacktrace]: \n");
                            x.a(stringBuilder.append(a.h).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[file]: ");
                            x.a(stringBuilder.append(a.i).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[lineNumber]: ");
                            x.a(stringBuilder.append(a.j).toString(), new Object[0]);
                            stringBuilder = new StringBuilder();
                            this("[columnNumber]: ");
                            x.a(stringBuilder.append(a.k).toString(), new Object[0]);
                            a a3 = a;
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } catch (Throwable throwable) {
      a2 = a1;
    } 
    return a2;
  }
  
  public static H5JavaScriptInterface getInstance(CrashReport.WebViewInterface paramWebViewInterface) {
    String str;
    Thread thread1 = null;
    H5JavaScriptInterface h5JavaScriptInterface1 = null;
    H5JavaScriptInterface h5JavaScriptInterface2 = h5JavaScriptInterface1;
    if (paramWebViewInterface != null) {
      if (a.contains(Integer.valueOf(paramWebViewInterface.hashCode())))
        return h5JavaScriptInterface1; 
    } else {
      return h5JavaScriptInterface2;
    } 
    h5JavaScriptInterface1 = new H5JavaScriptInterface();
    a.add(Integer.valueOf(paramWebViewInterface.hashCode()));
    h5JavaScriptInterface1.c = Thread.currentThread();
    Thread thread2 = h5JavaScriptInterface1.c;
    if (thread2 == null) {
      thread2 = thread1;
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("\n");
      for (byte b = 2; b < (thread2.getStackTrace()).length; b++) {
        StackTraceElement stackTraceElement = thread2.getStackTrace()[b];
        if (!stackTraceElement.toString().contains("crashreport"))
          stringBuilder.append(stackTraceElement.toString()).append("\n"); 
      } 
      str = stringBuilder.toString();
    } 
    h5JavaScriptInterface1.d = str;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("[WebView] ContentDescription", paramWebViewInterface.getContentDescription());
    h5JavaScriptInterface1.e = (Map)hashMap;
    return h5JavaScriptInterface1;
  }
  
  @JavascriptInterface
  public void printLog(String paramString) {
    x.d("Log from js: %s", new Object[] { paramString });
  }
  
  @JavascriptInterface
  public void reportJSException(String paramString) {
    if (paramString == null) {
      x.d("Payload from JS is null.", new Object[0]);
      return;
    } 
    String str = z.b(paramString.getBytes());
    if (this.b != null && this.b.equals(str)) {
      x.d("Same payload from js. Please check whether you've injected bugly.js more than one times.", new Object[0]);
      return;
    } 
    this.b = str;
    x.d("Handling JS exception ...", new Object[0]);
    a a = a(paramString);
    if (a == null) {
      x.d("Failed to parse payload.", new Object[0]);
      return;
    } 
    LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap2 = new LinkedHashMap<Object, Object>();
    if (a.a != null)
      linkedHashMap2.put("[JS] projectRoot", a.a); 
    if (a.b != null)
      linkedHashMap2.put("[JS] context", a.b); 
    if (a.c != null)
      linkedHashMap2.put("[JS] url", a.c); 
    if (a.d != null)
      linkedHashMap2.put("[JS] userAgent", a.d); 
    if (a.i != null)
      linkedHashMap2.put("[JS] file", a.i); 
    if (a.j != 0L)
      linkedHashMap2.put("[JS] lineNumber", Long.toString(a.j)); 
    linkedHashMap1.putAll(linkedHashMap2);
    linkedHashMap1.putAll(this.e);
    linkedHashMap1.put("Java Stack", this.d);
    Thread thread = this.c;
    if (a != null)
      InnerApi.postH5CrashAsync(thread, a.f, a.g, a.h, linkedHashMap1); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/h5/H5JavaScriptInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */