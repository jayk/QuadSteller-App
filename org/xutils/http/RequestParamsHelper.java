package org.xutils.http;

import android.os.Parcelable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

final class RequestParamsHelper {
  private static final ClassLoader BOOT_CL = String.class.getClassLoader();
  
  static Object parseJSONObject(Object paramObject) throws JSONException {
    Map.Entry entry;
    if (paramObject == null)
      return null; 
    Object object2 = paramObject;
    Class<?> clazz = paramObject.getClass();
    if (clazz.isArray()) {
      JSONArray jSONArray = new JSONArray();
      int i = Array.getLength(paramObject);
      byte b = 0;
      while (true) {
        if (b < i) {
          jSONArray.put(parseJSONObject(Array.get(paramObject, b)));
          b++;
          continue;
        } 
        return jSONArray;
      } 
    } 
    if (paramObject instanceof Iterable) {
      JSONArray jSONArray = new JSONArray();
      paramObject = ((Iterable)paramObject).iterator();
      while (true) {
        if (paramObject.hasNext()) {
          jSONArray.put(parseJSONObject(paramObject.next()));
          continue;
        } 
        return jSONArray;
      } 
    } 
    if (paramObject instanceof Map) {
      JSONObject jSONObject = new JSONObject();
      paramObject = ((Map)paramObject).entrySet().iterator();
      while (true) {
        if (paramObject.hasNext()) {
          entry = paramObject.next();
          object2 = entry.getKey();
          entry = (Map.Entry)entry.getValue();
          if (object2 != null && entry != null)
            jSONObject.put(String.valueOf(object2), parseJSONObject(entry)); 
          continue;
        } 
        return jSONObject;
      } 
    } 
    ClassLoader classLoader = entry.getClassLoader();
    final Object jo = object2;
    if (classLoader != null) {
      object1 = object2;
      if (classLoader != BOOT_CL) {
        object1 = new JSONObject();
        parseKV(paramObject, (Class<?>)entry, new ParseKVListener() {
              public void onParseKV(String param1String, Object param1Object) {
                try {
                  param1Object = RequestParamsHelper.parseJSONObject(param1Object);
                  jo.put(param1String, param1Object);
                  return;
                } catch (JSONException jSONException) {
                  throw new IllegalArgumentException("parse RequestParams to json failed", jSONException);
                } 
              }
            });
      } 
    } 
    return object1;
  }
  
  static void parseKV(Object paramObject, Class<?> paramClass, ParseKVListener paramParseKVListener) {
    if (paramObject != null && paramClass != null && paramClass != RequestParams.class && paramClass != Object.class) {
      ClassLoader classLoader = paramClass.getClassLoader();
      if (classLoader != null && classLoader != BOOT_CL) {
        Field[] arrayOfField = paramClass.getDeclaredFields();
        if (arrayOfField != null && arrayOfField.length > 0) {
          int i = arrayOfField.length;
          for (byte b = 0; b < i; b++) {
            Field field = arrayOfField[b];
            if (!Modifier.isTransient(field.getModifiers()) && field.getType() != Parcelable.Creator.class) {
              field.setAccessible(true);
              try {
                String str = field.getName();
                Object object = field.get(paramObject);
                if (object != null)
                  paramParseKVListener.onParseKV(str, object); 
              } catch (IllegalAccessException illegalAccessException) {
                LogUtil.e(illegalAccessException.getMessage(), illegalAccessException);
              } 
            } 
          } 
        } 
        parseKV(paramObject, paramClass.getSuperclass(), paramParseKVListener);
      } 
    } 
  }
  
  static interface ParseKVListener {
    void onParseKV(String param1String, Object param1Object);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/RequestParamsHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */