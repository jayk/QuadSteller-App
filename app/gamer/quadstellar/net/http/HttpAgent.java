package app.gamer.quadstellar.net.http;

import android.content.Context;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.utils.XlinkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class HttpAgent {
  public static String ACCESS_ID;
  
  private static final String AccessID = "X-AccessId";
  
  public static String SECRET_KEY = "4b5d7675883440fa9c1fdb8264659ad2";
  
  private static final String X_ContentMD5 = "X-ContentMD5";
  
  private static final String X_Sign = "X-Sign";
  
  private static AsyncHttpClient client;
  
  private static HttpAgent instance;
  
  public final String loginUrl = "http://app.xlink.cn/v1/user/login";
  
  public final String registerUrl = "http://app.xlink.cn/v1/user/register";
  
  public final String resetUrl = "http://app.xlink.cn/v1/user/reset";
  
  private final String url = "http://app.xlink.cn";
  
  static {
    ACCESS_ID = "46a174b0fbe240e9b86d197c9a48fd34";
  }
  
  private HttpAgent() {
    client = new AsyncHttpClient();
    client.setTimeout(5000);
  }
  
  public static final String MD5(String paramString) {
    char[] arrayOfChar = new char[16];
    arrayOfChar[0] = '0';
    arrayOfChar[1] = '1';
    arrayOfChar[2] = '2';
    arrayOfChar[3] = '3';
    arrayOfChar[4] = '4';
    arrayOfChar[5] = '5';
    arrayOfChar[6] = '6';
    arrayOfChar[7] = '7';
    arrayOfChar[8] = '8';
    arrayOfChar[9] = '9';
    arrayOfChar[10] = 'A';
    arrayOfChar[11] = 'B';
    arrayOfChar[12] = 'C';
    arrayOfChar[13] = 'D';
    arrayOfChar[14] = 'E';
    arrayOfChar[15] = 'F';
    try {
      byte[] arrayOfByte = paramString.getBytes();
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(arrayOfByte);
      arrayOfByte = messageDigest.digest();
      int i = arrayOfByte.length;
      char[] arrayOfChar1 = new char[i * 2];
      byte b = 0;
      int j = 0;
      while (b < i) {
        byte b1 = arrayOfByte[b];
        int k = j + 1;
        arrayOfChar1[j] = (char)arrayOfChar[b1 >>> 4 & 0xF];
        j = k + 1;
        arrayOfChar1[k] = (char)arrayOfChar[b1 & 0xF];
        b++;
      } 
      String str = new String();
      this(arrayOfChar1);
    } catch (Exception exception) {
      exception.printStackTrace();
      exception = null;
    } 
    return (String)exception;
  }
  
  public static HttpAgent getInstance() {
    if (instance == null)
      instance = new HttpAgent(); 
    return instance;
  }
  
  private XHeader getSign(String paramString) {
    return new XHeader("X-Sign", MD5(SECRET_KEY + paramString), null);
  }
  
  public void getAppId(String paramString1, String paramString2, TextHttpResponseHandler paramTextHttpResponseHandler) {
    StringEntity stringEntity;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("uid", paramString1);
    hashMap.put("pwd", paramString2);
    JSONObject jSONObject = XlinkUtils.getJsonObject(hashMap);
    paramString1 = null;
    try {
      StringEntity stringEntity1 = new StringEntity();
      this(jSONObject.toString(), "UTF-8");
      stringEntity = stringEntity1;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    XHeader xHeader = new XHeader("X-AccessId", ACCESS_ID, null);
    String str = MD5(jSONObject.toString());
    post("http://app.xlink.cn/v1/user/login", new Header[] { xHeader, new XHeader("X-ContentMD5", str, null), getSign(str) }(HttpEntity)stringEntity, (AsyncHttpResponseHandler)paramTextHttpResponseHandler);
  }
  
  public void getResetPw(String paramString1, String paramString2, TextHttpResponseHandler paramTextHttpResponseHandler) {
    StringEntity stringEntity;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("uid", paramString1);
    hashMap.put("pwd", paramString2);
    JSONObject jSONObject = XlinkUtils.getJsonObject(hashMap);
    paramString1 = null;
    try {
      StringEntity stringEntity1 = new StringEntity();
      this(jSONObject.toString(), "UTF-8");
      stringEntity = stringEntity1;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    XHeader xHeader = new XHeader("X-AccessId", ACCESS_ID, null);
    String str = MD5(jSONObject.toString());
    post("http://app.xlink.cn/v1/user/reset", new Header[] { xHeader, new XHeader("X-ContentMD5", str, null), getSign(str) }(HttpEntity)stringEntity, (AsyncHttpResponseHandler)paramTextHttpResponseHandler);
  }
  
  public void onRegister(String paramString1, String paramString2, String paramString3, TextHttpResponseHandler paramTextHttpResponseHandler) {
    StringEntity stringEntity;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("uid", paramString1);
    hashMap.put("name", paramString1);
    hashMap.put("pwd", paramString3);
    JSONObject jSONObject = XlinkUtils.getJsonObject(hashMap);
    paramString1 = null;
    try {
      StringEntity stringEntity1 = new StringEntity();
      this(jSONObject.toString(), "UTF-8");
      stringEntity = stringEntity1;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    XHeader xHeader = new XHeader("X-AccessId", ACCESS_ID, null);
    String str = MD5(jSONObject.toString());
    post("http://app.xlink.cn/v1/user/register", new Header[] { xHeader, new XHeader("X-ContentMD5", str, null), getSign(str) }(HttpEntity)stringEntity, (AsyncHttpResponseHandler)paramTextHttpResponseHandler);
  }
  
  public void post(String paramString, Header[] paramArrayOfHeader, HttpEntity paramHttpEntity, AsyncHttpResponseHandler paramAsyncHttpResponseHandler) {
    client.post((Context)App.getInstance(), paramString, paramArrayOfHeader, paramHttpEntity, "text/html", (ResponseHandlerInterface)paramAsyncHttpResponseHandler);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/http/HttpAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */