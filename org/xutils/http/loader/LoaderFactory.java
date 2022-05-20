package org.xutils.http.loader;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

public final class LoaderFactory {
  private static final HashMap<Type, Loader> converterHashMap = new HashMap<Type, Loader>();
  
  static {
    converterHashMap.put(JSONObject.class, new JSONObjectLoader());
    converterHashMap.put(JSONArray.class, new JSONArrayLoader());
    converterHashMap.put(String.class, new StringLoader());
    converterHashMap.put(File.class, new FileLoader());
    converterHashMap.put(byte[].class, new ByteArrayLoader());
    BooleanLoader booleanLoader = new BooleanLoader();
    converterHashMap.put(boolean.class, booleanLoader);
    converterHashMap.put(Boolean.class, booleanLoader);
    IntegerLoader integerLoader = new IntegerLoader();
    converterHashMap.put(int.class, integerLoader);
    converterHashMap.put(Integer.class, integerLoader);
  }
  
  public static Loader<?> getLoader(Type paramType, RequestParams paramRequestParams) {
    Loader loader1 = converterHashMap.get(paramType);
    if (loader1 == null) {
      ObjectLoader objectLoader = new ObjectLoader(paramType);
      objectLoader.setParams(paramRequestParams);
      return objectLoader;
    } 
    Loader<?> loader = loader1.newInstance();
    loader.setParams(paramRequestParams);
    return loader;
  }
  
  public static <T> void registerLoader(Type paramType, Loader<T> paramLoader) {
    converterHashMap.put(paramType, paramLoader);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/LoaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */