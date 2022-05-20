package org.xutils.http;

import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.http.body.BodyItemWrapper;
import org.xutils.http.body.FileBody;
import org.xutils.http.body.InputStreamBody;
import org.xutils.http.body.MultipartBody;
import org.xutils.http.body.RequestBody;
import org.xutils.http.body.StringBody;
import org.xutils.http.body.UrlEncodedParamsBody;

abstract class BaseParams {
  private boolean asJsonContent = false;
  
  private String bodyContent;
  
  private final List<KeyValue> bodyParams = new ArrayList<KeyValue>();
  
  private String charset = "UTF-8";
  
  private final List<KeyValue> fileParams = new ArrayList<KeyValue>();
  
  private final List<Header> headers = new ArrayList<Header>();
  
  private HttpMethod method;
  
  private boolean multipart = false;
  
  private final List<KeyValue> queryStringParams = new ArrayList<KeyValue>();
  
  private RequestBody requestBody;
  
  private void checkBodyParams() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield bodyParams : Ljava/util/List;
    //   6: invokeinterface isEmpty : ()Z
    //   11: istore_1
    //   12: iload_1
    //   13: ifeq -> 19
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: aload_0
    //   20: getfield method : Lorg/xutils/http/HttpMethod;
    //   23: invokestatic permitsRequestBody : (Lorg/xutils/http/HttpMethod;)Z
    //   26: ifeq -> 46
    //   29: aload_0
    //   30: getfield bodyContent : Ljava/lang/String;
    //   33: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   36: ifeq -> 46
    //   39: aload_0
    //   40: getfield requestBody : Lorg/xutils/http/body/RequestBody;
    //   43: ifnull -> 69
    //   46: aload_0
    //   47: getfield queryStringParams : Ljava/util/List;
    //   50: aload_0
    //   51: getfield bodyParams : Ljava/util/List;
    //   54: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   59: pop
    //   60: aload_0
    //   61: getfield bodyParams : Ljava/util/List;
    //   64: invokeinterface clear : ()V
    //   69: aload_0
    //   70: getfield bodyParams : Ljava/util/List;
    //   73: invokeinterface isEmpty : ()Z
    //   78: ifne -> 123
    //   81: aload_0
    //   82: getfield multipart : Z
    //   85: ifne -> 100
    //   88: aload_0
    //   89: getfield fileParams : Ljava/util/List;
    //   92: invokeinterface size : ()I
    //   97: ifle -> 123
    //   100: aload_0
    //   101: getfield fileParams : Ljava/util/List;
    //   104: aload_0
    //   105: getfield bodyParams : Ljava/util/List;
    //   108: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   113: pop
    //   114: aload_0
    //   115: getfield bodyParams : Ljava/util/List;
    //   118: invokeinterface clear : ()V
    //   123: aload_0
    //   124: getfield asJsonContent : Z
    //   127: ifeq -> 16
    //   130: aload_0
    //   131: getfield bodyParams : Ljava/util/List;
    //   134: invokeinterface isEmpty : ()Z
    //   139: istore_1
    //   140: iload_1
    //   141: ifne -> 16
    //   144: aload_0
    //   145: getfield bodyContent : Ljava/lang/String;
    //   148: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   151: ifne -> 212
    //   154: new org/json/JSONObject
    //   157: astore_2
    //   158: aload_2
    //   159: aload_0
    //   160: getfield bodyContent : Ljava/lang/String;
    //   163: invokespecial <init> : (Ljava/lang/String;)V
    //   166: aload_0
    //   167: aload_2
    //   168: aload_0
    //   169: getfield bodyParams : Ljava/util/List;
    //   172: invokespecial params2Json : (Lorg/json/JSONObject;Ljava/util/List;)V
    //   175: aload_0
    //   176: aload_2
    //   177: invokevirtual toString : ()Ljava/lang/String;
    //   180: putfield bodyContent : Ljava/lang/String;
    //   183: aload_0
    //   184: getfield bodyParams : Ljava/util/List;
    //   187: invokeinterface clear : ()V
    //   192: goto -> 16
    //   195: astore_3
    //   196: new java/lang/RuntimeException
    //   199: astore_2
    //   200: aload_2
    //   201: aload_3
    //   202: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   205: aload_2
    //   206: athrow
    //   207: astore_2
    //   208: aload_0
    //   209: monitorexit
    //   210: aload_2
    //   211: athrow
    //   212: new org/json/JSONObject
    //   215: dup
    //   216: invokespecial <init> : ()V
    //   219: astore_2
    //   220: goto -> 166
    // Exception table:
    //   from	to	target	type
    //   2	12	207	finally
    //   19	46	207	finally
    //   46	69	207	finally
    //   69	100	207	finally
    //   100	123	207	finally
    //   123	140	207	finally
    //   144	166	195	org/json/JSONException
    //   144	166	207	finally
    //   166	192	195	org/json/JSONException
    //   166	192	207	finally
    //   196	207	207	finally
    //   212	220	195	org/json/JSONException
    //   212	220	207	finally
  }
  
  private void params2Json(JSONObject paramJSONObject, List<KeyValue> paramList) throws JSONException {
    HashSet<String> hashSet = new HashSet(paramList.size());
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>(paramList.size());
    for (byte b = 0; b < paramList.size(); b++) {
      KeyValue keyValue = paramList.get(b);
      String str = keyValue.key;
      if (!TextUtils.isEmpty(str)) {
        JSONArray jSONArray;
        if (linkedHashMap.containsKey(str)) {
          jSONArray = (JSONArray)linkedHashMap.get(str);
        } else {
          jSONArray = new JSONArray();
          linkedHashMap.put(str, jSONArray);
        } 
        jSONArray.put(RequestParamsHelper.parseJSONObject(keyValue.value));
        if (keyValue instanceof ArrayItem)
          hashSet.add(str); 
      } 
    } 
    for (Map.Entry<Object, Object> entry : linkedHashMap.entrySet()) {
      String str = (String)entry.getKey();
      JSONArray jSONArray = (JSONArray)entry.getValue();
      if (jSONArray.length() > 1 || hashSet.contains(str)) {
        paramJSONObject.put(str, jSONArray);
        continue;
      } 
      paramJSONObject.put(str, jSONArray.get(0));
    } 
  }
  
  public void addBodyParameter(String paramString, File paramFile) {
    addBodyParameter(paramString, paramFile, null, null);
  }
  
  public void addBodyParameter(String paramString1, Object paramObject, String paramString2) {
    addBodyParameter(paramString1, paramObject, paramString2, null);
  }
  
  public void addBodyParameter(String paramString1, Object paramObject, String paramString2, String paramString3) {
    if (TextUtils.isEmpty(paramString2) && TextUtils.isEmpty(paramString3)) {
      this.fileParams.add(new KeyValue(paramString1, paramObject));
      return;
    } 
    this.fileParams.add(new KeyValue(paramString1, new BodyItemWrapper(paramObject, paramString2, paramString3)));
  }
  
  public void addBodyParameter(String paramString1, String paramString2) {
    if (!TextUtils.isEmpty(paramString1)) {
      this.bodyParams.add(new KeyValue(paramString1, paramString2));
      return;
    } 
    this.bodyContent = paramString2;
  }
  
  public void addHeader(String paramString1, String paramString2) {
    this.headers.add(new Header(paramString1, paramString2, false));
  }
  
  public void addParameter(String paramString, Object paramObject) {
    if (paramObject != null) {
      if (this.method == null || HttpMethod.permitsRequestBody(this.method)) {
        if (!TextUtils.isEmpty(paramString)) {
          if (paramObject instanceof File || paramObject instanceof InputStream || paramObject instanceof byte[]) {
            this.fileParams.add(new KeyValue(paramString, paramObject));
            return;
          } 
          if (paramObject instanceof Iterable) {
            Iterator iterator = ((Iterable)paramObject).iterator();
            while (true) {
              if (iterator.hasNext()) {
                paramObject = iterator.next();
                this.bodyParams.add(new ArrayItem(paramString, paramObject));
                continue;
              } 
              return;
            } 
          } 
          if (paramObject instanceof JSONArray) {
            paramObject = paramObject;
            int i = paramObject.length();
            byte b = 0;
            while (true) {
              if (b < i) {
                this.bodyParams.add(new ArrayItem(paramString, paramObject.opt(b)));
                b++;
                continue;
              } 
              return;
            } 
          } 
          if (paramObject.getClass().isArray()) {
            int i = Array.getLength(paramObject);
            byte b = 0;
            while (true) {
              if (b < i) {
                this.bodyParams.add(new ArrayItem(paramString, Array.get(paramObject, b)));
                b++;
                continue;
              } 
              return;
            } 
          } 
          this.bodyParams.add(new KeyValue(paramString, paramObject));
          return;
        } 
        this.bodyContent = paramObject.toString();
        return;
      } 
      if (!TextUtils.isEmpty(paramString)) {
        if (paramObject instanceof Iterable) {
          Iterator iterator = ((Iterable)paramObject).iterator();
          while (true) {
            if (iterator.hasNext()) {
              paramObject = iterator.next();
              this.queryStringParams.add(new ArrayItem(paramString, paramObject));
              continue;
            } 
            return;
          } 
        } 
        if (paramObject.getClass().isArray()) {
          int i = Array.getLength(paramObject);
          byte b = 0;
          while (true) {
            if (b < i) {
              this.queryStringParams.add(new ArrayItem(paramString, Array.get(paramObject, b)));
              b++;
              continue;
            } 
            return;
          } 
        } 
        this.queryStringParams.add(new KeyValue(paramString, paramObject));
      } 
    } 
  }
  
  public void addQueryStringParameter(String paramString1, String paramString2) {
    if (!TextUtils.isEmpty(paramString1))
      this.queryStringParams.add(new KeyValue(paramString1, paramString2)); 
  }
  
  public void clearParams() {
    this.queryStringParams.clear();
    this.bodyParams.clear();
    this.fileParams.clear();
    this.bodyContent = null;
    this.requestBody = null;
  }
  
  public String getBodyContent() {
    checkBodyParams();
    return this.bodyContent;
  }
  
  public List<KeyValue> getBodyParams() {
    checkBodyParams();
    return new ArrayList<KeyValue>(this.bodyParams);
  }
  
  public String getCharset() {
    return this.charset;
  }
  
  public List<KeyValue> getFileParams() {
    checkBodyParams();
    return new ArrayList<KeyValue>(this.fileParams);
  }
  
  public List<Header> getHeaders() {
    return new ArrayList<Header>(this.headers);
  }
  
  public HttpMethod getMethod() {
    return this.method;
  }
  
  public List<KeyValue> getParams(String paramString) {
    ArrayList<KeyValue> arrayList = new ArrayList();
    for (KeyValue keyValue : this.queryStringParams) {
      if (paramString == null && keyValue.key == null) {
        arrayList.add(keyValue);
        continue;
      } 
      if (paramString != null && paramString.equals(keyValue.key))
        arrayList.add(keyValue); 
    } 
    for (KeyValue keyValue : this.bodyParams) {
      if (paramString == null && keyValue.key == null) {
        arrayList.add(keyValue);
        continue;
      } 
      if (paramString != null && paramString.equals(keyValue.key))
        arrayList.add(keyValue); 
    } 
    for (KeyValue keyValue : this.fileParams) {
      if (paramString == null && keyValue.key == null) {
        arrayList.add(keyValue);
        continue;
      } 
      if (paramString != null && paramString.equals(keyValue.key))
        arrayList.add(keyValue); 
    } 
    return arrayList;
  }
  
  public List<KeyValue> getQueryStringParams() {
    checkBodyParams();
    return new ArrayList<KeyValue>(this.queryStringParams);
  }
  
  public RequestBody getRequestBody() throws IOException {
    Object object;
    checkBodyParams();
    if (this.requestBody != null)
      return this.requestBody; 
    KeyValue keyValue2 = null;
    if (!TextUtils.isEmpty(this.bodyContent))
      return (RequestBody)new StringBody(this.bodyContent, this.charset); 
    if (this.multipart || this.fileParams.size() > 0) {
      if (!this.multipart && this.fileParams.size() == 1) {
        Iterator<KeyValue> iterator = this.fileParams.iterator();
        object = keyValue2;
        if (iterator.hasNext()) {
          String str;
          object = iterator.next();
          iterator = null;
          Object object1 = ((KeyValue)object).value;
          object = object1;
          if (object1 instanceof BodyItemWrapper) {
            BodyItemWrapper bodyItemWrapper = (BodyItemWrapper)object1;
            object = bodyItemWrapper.getValue();
            str = bodyItemWrapper.getContentType();
          } 
          if (object instanceof File)
            return (RequestBody)new FileBody((File)object, str); 
          if (object instanceof InputStream)
            return (RequestBody)new InputStreamBody((InputStream)object, str); 
          if (object instanceof byte[])
            return (RequestBody)new InputStreamBody(new ByteArrayInputStream((byte[])object), str); 
          if (object instanceof String) {
            object = new StringBody((String)object, this.charset);
            object.setContentType(str);
            return (RequestBody)object;
          } 
          LogUtil.w("Some params will be ignored for: " + toString());
          object = keyValue2;
        } 
        return (RequestBody)object;
      } 
      this.multipart = true;
      return (RequestBody)new MultipartBody(this.fileParams, this.charset);
    } 
    KeyValue keyValue1 = keyValue2;
    if (this.bodyParams.size() > 0)
      object = new UrlEncodedParamsBody(this.bodyParams, this.charset); 
    return (RequestBody)object;
  }
  
  public String getStringParameter(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield queryStringParams : Ljava/util/List;
    //   4: invokeinterface iterator : ()Ljava/util/Iterator;
    //   9: astore_2
    //   10: aload_2
    //   11: invokeinterface hasNext : ()Z
    //   16: ifeq -> 70
    //   19: aload_2
    //   20: invokeinterface next : ()Ljava/lang/Object;
    //   25: checkcast org/xutils/common/util/KeyValue
    //   28: astore_3
    //   29: aload_1
    //   30: ifnonnull -> 47
    //   33: aload_3
    //   34: getfield key : Ljava/lang/String;
    //   37: ifnonnull -> 47
    //   40: aload_3
    //   41: invokevirtual getValueStr : ()Ljava/lang/String;
    //   44: astore_1
    //   45: aload_1
    //   46: areturn
    //   47: aload_1
    //   48: ifnull -> 10
    //   51: aload_1
    //   52: aload_3
    //   53: getfield key : Ljava/lang/String;
    //   56: invokevirtual equals : (Ljava/lang/Object;)Z
    //   59: ifeq -> 10
    //   62: aload_3
    //   63: invokevirtual getValueStr : ()Ljava/lang/String;
    //   66: astore_1
    //   67: goto -> 45
    //   70: aload_0
    //   71: getfield bodyParams : Ljava/util/List;
    //   74: invokeinterface iterator : ()Ljava/util/Iterator;
    //   79: astore_3
    //   80: aload_3
    //   81: invokeinterface hasNext : ()Z
    //   86: ifeq -> 141
    //   89: aload_3
    //   90: invokeinterface next : ()Ljava/lang/Object;
    //   95: checkcast org/xutils/common/util/KeyValue
    //   98: astore_2
    //   99: aload_1
    //   100: ifnonnull -> 118
    //   103: aload_2
    //   104: getfield key : Ljava/lang/String;
    //   107: ifnonnull -> 118
    //   110: aload_2
    //   111: invokevirtual getValueStr : ()Ljava/lang/String;
    //   114: astore_1
    //   115: goto -> 45
    //   118: aload_1
    //   119: ifnull -> 80
    //   122: aload_1
    //   123: aload_2
    //   124: getfield key : Ljava/lang/String;
    //   127: invokevirtual equals : (Ljava/lang/Object;)Z
    //   130: ifeq -> 80
    //   133: aload_2
    //   134: invokevirtual getValueStr : ()Ljava/lang/String;
    //   137: astore_1
    //   138: goto -> 45
    //   141: aconst_null
    //   142: astore_1
    //   143: goto -> 45
  }
  
  public List<KeyValue> getStringParams() {
    ArrayList<KeyValue> arrayList = new ArrayList(this.queryStringParams.size() + this.bodyParams.size());
    arrayList.addAll(this.queryStringParams);
    arrayList.addAll(this.bodyParams);
    return arrayList;
  }
  
  public boolean isAsJsonContent() {
    return this.asJsonContent;
  }
  
  public boolean isMultipart() {
    return this.multipart;
  }
  
  public void removeParameter(String paramString) {
    if (!TextUtils.isEmpty(paramString)) {
      Iterator<KeyValue> iterator = this.queryStringParams.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(((KeyValue)iterator.next()).key))
          iterator.remove(); 
      } 
      iterator = this.bodyParams.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(((KeyValue)iterator.next()).key))
          iterator.remove(); 
      } 
      iterator = this.fileParams.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(((KeyValue)iterator.next()).key))
          iterator.remove(); 
      } 
    } else {
      this.bodyContent = null;
    } 
  }
  
  public void setAsJsonContent(boolean paramBoolean) {
    this.asJsonContent = paramBoolean;
  }
  
  public void setBodyContent(String paramString) {
    this.bodyContent = paramString;
  }
  
  public void setCharset(String paramString) {
    if (!TextUtils.isEmpty(paramString))
      this.charset = paramString; 
  }
  
  public void setHeader(String paramString1, String paramString2) {
    Header header = new Header(paramString1, paramString2, true);
    Iterator<Header> iterator = this.headers.iterator();
    while (iterator.hasNext()) {
      if (paramString1.equals(((KeyValue)iterator.next()).key))
        iterator.remove(); 
    } 
    this.headers.add(header);
  }
  
  public void setMethod(HttpMethod paramHttpMethod) {
    this.method = paramHttpMethod;
  }
  
  public void setMultipart(boolean paramBoolean) {
    this.multipart = paramBoolean;
  }
  
  public void setRequestBody(RequestBody paramRequestBody) {
    this.requestBody = paramRequestBody;
  }
  
  public String toJSONString() {
    ArrayList<KeyValue> arrayList = new ArrayList(this.queryStringParams.size() + this.bodyParams.size());
    arrayList.addAll(this.queryStringParams);
    arrayList.addAll(this.bodyParams);
    try {
      if (!TextUtils.isEmpty(this.bodyContent)) {
        JSONObject jSONObject1 = new JSONObject();
        this(this.bodyContent);
        params2Json(jSONObject1, arrayList);
        return jSONObject1.toString();
      } 
      JSONObject jSONObject = new JSONObject();
      params2Json(jSONObject, arrayList);
      return jSONObject.toString();
    } catch (JSONException jSONException) {
      throw new RuntimeException(jSONException);
    } 
  }
  
  public String toString() {
    checkBodyParams();
    StringBuilder stringBuilder = new StringBuilder();
    if (!this.queryStringParams.isEmpty()) {
      for (KeyValue keyValue : this.queryStringParams)
        stringBuilder.append(keyValue.key).append("=").append(keyValue.value).append("&"); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    } 
    if (HttpMethod.permitsRequestBody(this.method)) {
      stringBuilder.append("<");
      if (!TextUtils.isEmpty(this.bodyContent)) {
        stringBuilder.append(this.bodyContent);
      } else if (!this.bodyParams.isEmpty()) {
        for (KeyValue keyValue : this.bodyParams)
          stringBuilder.append(keyValue.key).append("=").append(keyValue.value).append("&"); 
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      } 
      stringBuilder.append(">");
    } 
    return stringBuilder.toString();
  }
  
  public static final class ArrayItem extends KeyValue {
    public ArrayItem(String param1String, Object param1Object) {
      super(param1String, param1Object);
    }
  }
  
  public static final class Header extends KeyValue {
    public final boolean setHeader;
    
    public Header(String param1String1, String param1String2, boolean param1Boolean) {
      super(param1String1, param1String2);
      this.setHeader = param1Boolean;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/BaseParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */