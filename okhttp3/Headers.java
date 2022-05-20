package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Headers {
  private final String[] namesAndValues;
  
  Headers(Builder paramBuilder) {
    this.namesAndValues = paramBuilder.namesAndValues.<String>toArray(new String[paramBuilder.namesAndValues.size()]);
  }
  
  private Headers(String[] paramArrayOfString) {
    this.namesAndValues = paramArrayOfString;
  }
  
  private static String get(String[] paramArrayOfString, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: iconst_2
    //   3: isub
    //   4: istore_2
    //   5: iload_2
    //   6: iflt -> 33
    //   9: aload_1
    //   10: aload_0
    //   11: iload_2
    //   12: aaload
    //   13: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   16: ifeq -> 27
    //   19: aload_0
    //   20: iload_2
    //   21: iconst_1
    //   22: iadd
    //   23: aaload
    //   24: astore_0
    //   25: aload_0
    //   26: areturn
    //   27: iinc #2, -2
    //   30: goto -> 5
    //   33: aconst_null
    //   34: astore_0
    //   35: goto -> 25
  }
  
  public static Headers of(Map<String, String> paramMap) {
    if (paramMap == null)
      throw new NullPointerException("headers == null"); 
    String[] arrayOfString = new String[paramMap.size() * 2];
    byte b = 0;
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      if (entry.getKey() == null || entry.getValue() == null)
        throw new IllegalArgumentException("Headers cannot be null"); 
      String str1 = ((String)entry.getKey()).trim();
      String str2 = ((String)entry.getValue()).trim();
      if (str1.length() == 0 || str1.indexOf(false) != -1 || str2.indexOf(false) != -1)
        throw new IllegalArgumentException("Unexpected header: " + str1 + ": " + str2); 
      arrayOfString[b] = str1;
      arrayOfString[b + 1] = str2;
      b += 2;
    } 
    return new Headers(arrayOfString);
  }
  
  public static Headers of(String... paramVarArgs) {
    if (paramVarArgs == null)
      throw new NullPointerException("namesAndValues == null"); 
    if (paramVarArgs.length % 2 != 0)
      throw new IllegalArgumentException("Expected alternating header names and values"); 
    String[] arrayOfString = (String[])paramVarArgs.clone();
    byte b;
    for (b = 0; b < arrayOfString.length; b++) {
      if (arrayOfString[b] == null)
        throw new IllegalArgumentException("Headers cannot be null"); 
      arrayOfString[b] = arrayOfString[b].trim();
    } 
    for (b = 0; b < arrayOfString.length; b += 2) {
      String str1 = arrayOfString[b];
      String str2 = arrayOfString[b + 1];
      if (str1.length() == 0 || str1.indexOf(false) != -1 || str2.indexOf(false) != -1)
        throw new IllegalArgumentException("Unexpected header: " + str1 + ": " + str2); 
    } 
    return new Headers(arrayOfString);
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof Headers && Arrays.equals((Object[])((Headers)paramObject).namesAndValues, (Object[])this.namesAndValues));
  }
  
  @Nullable
  public String get(String paramString) {
    return get(this.namesAndValues, paramString);
  }
  
  @Nullable
  public Date getDate(String paramString) {
    paramString = get(paramString);
    return (paramString != null) ? HttpDate.parse(paramString) : null;
  }
  
  public int hashCode() {
    return Arrays.hashCode((Object[])this.namesAndValues);
  }
  
  public String name(int paramInt) {
    return this.namesAndValues[paramInt * 2];
  }
  
  public Set<String> names() {
    TreeSet<String> treeSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    byte b = 0;
    int i = size();
    while (b < i) {
      treeSet.add(name(b));
      b++;
    } 
    return Collections.unmodifiableSet(treeSet);
  }
  
  public Builder newBuilder() {
    Builder builder = new Builder();
    Collections.addAll(builder.namesAndValues, this.namesAndValues);
    return builder;
  }
  
  public int size() {
    return this.namesAndValues.length / 2;
  }
  
  public Map<String, List<String>> toMultimap() {
    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
    byte b = 0;
    int i = size();
    while (b < i) {
      String str = name(b).toLowerCase(Locale.US);
      List<String> list1 = (List)treeMap.get(str);
      List<String> list2 = list1;
      if (list1 == null) {
        list2 = new ArrayList(2);
        treeMap.put(str, list2);
      } 
      list2.add(value(b));
      b++;
    } 
    return (Map)treeMap;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    byte b = 0;
    int i = size();
    while (b < i) {
      stringBuilder.append(name(b)).append(": ").append(value(b)).append("\n");
      b++;
    } 
    return stringBuilder.toString();
  }
  
  public String value(int paramInt) {
    return this.namesAndValues[paramInt * 2 + 1];
  }
  
  public List<String> values(String paramString) {
    ArrayList<String> arrayList = null;
    byte b = 0;
    int i = size();
    while (b < i) {
      ArrayList<String> arrayList1 = arrayList;
      if (paramString.equalsIgnoreCase(name(b))) {
        arrayList1 = arrayList;
        if (arrayList == null)
          arrayList1 = new ArrayList(2); 
        arrayList1.add(value(b));
      } 
      b++;
      arrayList = arrayList1;
    } 
    return (List)((arrayList != null) ? Collections.unmodifiableList(arrayList) : Collections.emptyList());
  }
  
  public static final class Builder {
    final List<String> namesAndValues = new ArrayList<String>(20);
    
    private void checkNameAndValue(String param1String1, String param1String2) {
      if (param1String1 == null)
        throw new NullPointerException("name == null"); 
      if (param1String1.isEmpty())
        throw new IllegalArgumentException("name is empty"); 
      byte b = 0;
      int i = param1String1.length();
      while (b < i) {
        char c = param1String1.charAt(b);
        if (c <= ' ' || c >= '')
          throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", new Object[] { Integer.valueOf(c), Integer.valueOf(b), param1String1 })); 
        b++;
      } 
      if (param1String2 == null)
        throw new NullPointerException("value for name " + param1String1 + " == null"); 
      b = 0;
      i = param1String2.length();
      while (b < i) {
        char c = param1String2.charAt(b);
        if ((c <= '\037' && c != '\t') || c >= '')
          throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", new Object[] { Integer.valueOf(c), Integer.valueOf(b), param1String1, param1String2 })); 
        b++;
      } 
    }
    
    public Builder add(String param1String) {
      int i = param1String.indexOf(":");
      if (i == -1)
        throw new IllegalArgumentException("Unexpected header: " + param1String); 
      return add(param1String.substring(0, i).trim(), param1String.substring(i + 1));
    }
    
    public Builder add(String param1String1, String param1String2) {
      checkNameAndValue(param1String1, param1String2);
      return addLenient(param1String1, param1String2);
    }
    
    Builder addLenient(String param1String) {
      int i = param1String.indexOf(":", 1);
      return (i != -1) ? addLenient(param1String.substring(0, i), param1String.substring(i + 1)) : (param1String.startsWith(":") ? addLenient("", param1String.substring(1)) : addLenient("", param1String));
    }
    
    Builder addLenient(String param1String1, String param1String2) {
      this.namesAndValues.add(param1String1);
      this.namesAndValues.add(param1String2.trim());
      return this;
    }
    
    public Headers build() {
      return new Headers(this);
    }
    
    public String get(String param1String) {
      // Byte code:
      //   0: aload_0
      //   1: getfield namesAndValues : Ljava/util/List;
      //   4: invokeinterface size : ()I
      //   9: iconst_2
      //   10: isub
      //   11: istore_2
      //   12: iload_2
      //   13: iflt -> 60
      //   16: aload_1
      //   17: aload_0
      //   18: getfield namesAndValues : Ljava/util/List;
      //   21: iload_2
      //   22: invokeinterface get : (I)Ljava/lang/Object;
      //   27: checkcast java/lang/String
      //   30: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   33: ifeq -> 54
      //   36: aload_0
      //   37: getfield namesAndValues : Ljava/util/List;
      //   40: iload_2
      //   41: iconst_1
      //   42: iadd
      //   43: invokeinterface get : (I)Ljava/lang/Object;
      //   48: checkcast java/lang/String
      //   51: astore_1
      //   52: aload_1
      //   53: areturn
      //   54: iinc #2, -2
      //   57: goto -> 12
      //   60: aconst_null
      //   61: astore_1
      //   62: goto -> 52
    }
    
    public Builder removeAll(String param1String) {
      for (int i = 0; i < this.namesAndValues.size(); i = j + 2) {
        int j = i;
        if (param1String.equalsIgnoreCase(this.namesAndValues.get(i))) {
          this.namesAndValues.remove(i);
          this.namesAndValues.remove(i);
          j = i - 2;
        } 
      } 
      return this;
    }
    
    public Builder set(String param1String1, String param1String2) {
      checkNameAndValue(param1String1, param1String2);
      removeAll(param1String1);
      addLenient(param1String1, param1String2);
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */