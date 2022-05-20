package app.gamer.quadstellar.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XTGlobals {
  private static XTProperties properties = null;
  
  public static void deleteProperty(String paramString) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    properties.remove(paramString);
  }
  
  public static Map<String, String> getAllProperty() {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    return properties.getProperties();
  }
  
  public static boolean getBooleanProperty(String paramString) {
    return Boolean.valueOf(getProperty(paramString)).booleanValue();
  }
  
  public static boolean getBooleanProperty(String paramString, boolean paramBoolean) {
    paramString = getProperty(paramString);
    if (paramString != null)
      paramBoolean = Boolean.valueOf(paramString).booleanValue(); 
    return paramBoolean;
  }
  
  public static int getIntProperty(String paramString, int paramInt) {
    paramString = getProperty(paramString);
    int i = paramInt;
    if (paramString != null)
      try {
        i = Integer.parseInt(paramString);
      } catch (NumberFormatException numberFormatException) {
        i = paramInt;
      }  
    return i;
  }
  
  public static long getLongProperty(String paramString, long paramLong) {
    paramString = getProperty(paramString);
    long l = paramLong;
    if (paramString != null)
      try {
        l = Long.parseLong(paramString);
      } catch (NumberFormatException numberFormatException) {
        l = paramLong;
      }  
    return l;
  }
  
  public static List<String> getProperties(String paramString) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    Collection<String> collection = properties.getChildrenNames(paramString);
    ArrayList<String> arrayList = new ArrayList();
    Iterator<String> iterator = collection.iterator();
    while (iterator.hasNext()) {
      String str = getProperty(iterator.next());
      if (str != null)
        arrayList.add(str); 
    } 
    return arrayList;
  }
  
  public static String getProperty(String paramString) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    return properties.get(paramString);
  }
  
  public static String getProperty(String paramString1, String paramString2) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    paramString1 = properties.get(paramString1);
    if (paramString1 != null)
      paramString2 = paramString1; 
    return paramString2;
  }
  
  public static List<String> getPropertyNames() {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    return new ArrayList<String>(properties.getPropertyNames());
  }
  
  public static List<String> getPropertyNames(String paramString) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    return new ArrayList<String>(properties.getChildrenNames(paramString));
  }
  
  public static void setProperties(Map<String, String> paramMap) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    properties.putAll(paramMap);
  }
  
  public static void setProperty(String paramString1, String paramString2) {
    if (properties == null)
      properties = XTProperties.getInstance(); 
    properties.put(paramString1, paramString2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/XTGlobals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */