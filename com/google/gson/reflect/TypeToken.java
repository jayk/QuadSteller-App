package com.google.gson.reflect;

import com.google.gson.internal.;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
  final int hashCode = this.type.hashCode();
  
  final Class<? super T> rawType = .Gson.Types.getRawType(this.type);
  
  final Type type = getSuperclassTypeParameter(getClass());
  
  protected TypeToken() {}
  
  TypeToken(Type paramType) {}
  
  private static AssertionError buildUnexpectedTypeError(Type paramType, Class<?>... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder("Unexpected type. Expected one of: ");
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      stringBuilder.append(paramVarArgs[b].getName()).append(", "); 
    stringBuilder.append("but got: ").append(paramType.getClass().getName()).append(", for type token: ").append(paramType.toString()).append('.');
    return new AssertionError(stringBuilder.toString());
  }
  
  public static <T> TypeToken<T> get(Class<T> paramClass) {
    return new TypeToken<T>(paramClass);
  }
  
  public static TypeToken<?> get(Type paramType) {
    return new TypeToken(paramType);
  }
  
  static Type getSuperclassTypeParameter(Class<?> paramClass) {
    Type type = paramClass.getGenericSuperclass();
    if (type instanceof Class)
      throw new RuntimeException("Missing type parameter."); 
    return .Gson.Types.canonicalize(((ParameterizedType)type).getActualTypeArguments()[0]);
  }
  
  private static boolean isAssignableFrom(Type<?> paramType, GenericArrayType paramGenericArrayType) {
    Type type = paramGenericArrayType.getGenericComponentType();
    if (type instanceof ParameterizedType) {
      Type<?> type1 = paramType;
      if (paramType instanceof GenericArrayType) {
        type1 = ((GenericArrayType)paramType).getGenericComponentType();
      } else if (paramType instanceof Class) {
        for (paramType = paramType; paramType.isArray(); paramType = paramType.getComponentType());
        type1 = paramType;
      } 
      return isAssignableFrom(type1, (ParameterizedType)type, new HashMap<String, Type>());
    } 
    return true;
  }
  
  private static boolean isAssignableFrom(Type paramType, ParameterizedType paramParameterizedType, Map<String, Type> paramMap) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 8
    //   4: iconst_0
    //   5: istore_3
    //   6: iload_3
    //   7: ireturn
    //   8: aload_1
    //   9: aload_0
    //   10: invokevirtual equals : (Ljava/lang/Object;)Z
    //   13: ifeq -> 21
    //   16: iconst_1
    //   17: istore_3
    //   18: goto -> 6
    //   21: aload_0
    //   22: invokestatic getRawType : (Ljava/lang/reflect/Type;)Ljava/lang/Class;
    //   25: astore #4
    //   27: aconst_null
    //   28: astore #5
    //   30: aload_0
    //   31: instanceof java/lang/reflect/ParameterizedType
    //   34: ifeq -> 43
    //   37: aload_0
    //   38: checkcast java/lang/reflect/ParameterizedType
    //   41: astore #5
    //   43: aload #5
    //   45: ifnull -> 153
    //   48: aload #5
    //   50: invokeinterface getActualTypeArguments : ()[Ljava/lang/reflect/Type;
    //   55: astore #6
    //   57: aload #4
    //   59: invokevirtual getTypeParameters : ()[Ljava/lang/reflect/TypeVariable;
    //   62: astore #7
    //   64: iconst_0
    //   65: istore #8
    //   67: iload #8
    //   69: aload #6
    //   71: arraylength
    //   72: if_icmpge -> 138
    //   75: aload #6
    //   77: iload #8
    //   79: aaload
    //   80: astore_0
    //   81: aload #7
    //   83: iload #8
    //   85: aaload
    //   86: astore #9
    //   88: aload_0
    //   89: instanceof java/lang/reflect/TypeVariable
    //   92: ifeq -> 117
    //   95: aload_2
    //   96: aload_0
    //   97: checkcast java/lang/reflect/TypeVariable
    //   100: invokeinterface getName : ()Ljava/lang/String;
    //   105: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   110: checkcast java/lang/reflect/Type
    //   113: astore_0
    //   114: goto -> 88
    //   117: aload_2
    //   118: aload #9
    //   120: invokeinterface getName : ()Ljava/lang/String;
    //   125: aload_0
    //   126: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   131: pop
    //   132: iinc #8, 1
    //   135: goto -> 67
    //   138: aload #5
    //   140: aload_1
    //   141: aload_2
    //   142: invokestatic typeEquals : (Ljava/lang/reflect/ParameterizedType;Ljava/lang/reflect/ParameterizedType;Ljava/util/Map;)Z
    //   145: ifeq -> 153
    //   148: iconst_1
    //   149: istore_3
    //   150: goto -> 6
    //   153: aload #4
    //   155: invokevirtual getGenericInterfaces : ()[Ljava/lang/reflect/Type;
    //   158: astore_0
    //   159: aload_0
    //   160: arraylength
    //   161: istore #10
    //   163: iconst_0
    //   164: istore #8
    //   166: iload #8
    //   168: iload #10
    //   170: if_icmpge -> 203
    //   173: aload_0
    //   174: iload #8
    //   176: aaload
    //   177: aload_1
    //   178: new java/util/HashMap
    //   181: dup
    //   182: aload_2
    //   183: invokespecial <init> : (Ljava/util/Map;)V
    //   186: invokestatic isAssignableFrom : (Ljava/lang/reflect/Type;Ljava/lang/reflect/ParameterizedType;Ljava/util/Map;)Z
    //   189: ifeq -> 197
    //   192: iconst_1
    //   193: istore_3
    //   194: goto -> 6
    //   197: iinc #8, 1
    //   200: goto -> 166
    //   203: aload #4
    //   205: invokevirtual getGenericSuperclass : ()Ljava/lang/reflect/Type;
    //   208: aload_1
    //   209: new java/util/HashMap
    //   212: dup
    //   213: aload_2
    //   214: invokespecial <init> : (Ljava/util/Map;)V
    //   217: invokestatic isAssignableFrom : (Ljava/lang/reflect/Type;Ljava/lang/reflect/ParameterizedType;Ljava/util/Map;)Z
    //   220: istore_3
    //   221: goto -> 6
  }
  
  private static boolean matches(Type paramType1, Type paramType2, Map<String, Type> paramMap) {
    return (paramType2.equals(paramType1) || (paramType1 instanceof TypeVariable && paramType2.equals(paramMap.get(((TypeVariable)paramType1).getName()))));
  }
  
  private static boolean typeEquals(ParameterizedType paramParameterizedType1, ParameterizedType paramParameterizedType2, Map<String, Type> paramMap) {
    Type[] arrayOfType1;
    Type[] arrayOfType2;
    byte b;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramParameterizedType1.getRawType().equals(paramParameterizedType2.getRawType())) {
      arrayOfType1 = paramParameterizedType1.getActualTypeArguments();
      arrayOfType2 = paramParameterizedType2.getActualTypeArguments();
      for (b = 0; b < arrayOfType1.length; b++) {
        if (!matches(arrayOfType1[b], arrayOfType2[b], paramMap))
          return bool1; 
      } 
    } else {
      return bool2;
    } 
    bool2 = true;
    while (b < arrayOfType1.length) {
      if (!matches(arrayOfType1[b], arrayOfType2[b], paramMap))
        return bool1; 
      b++;
    } 
  }
  
  public final boolean equals(Object paramObject) {
    return (paramObject instanceof TypeToken && .Gson.Types.equals(this.type, ((TypeToken)paramObject).type));
  }
  
  public final Class<? super T> getRawType() {
    return this.rawType;
  }
  
  public final Type getType() {
    return this.type;
  }
  
  public final int hashCode() {
    return this.hashCode;
  }
  
  @Deprecated
  public boolean isAssignableFrom(TypeToken<?> paramTypeToken) {
    return isAssignableFrom(paramTypeToken.getType());
  }
  
  @Deprecated
  public boolean isAssignableFrom(Class<?> paramClass) {
    return isAssignableFrom(paramClass);
  }
  
  @Deprecated
  public boolean isAssignableFrom(Type paramType) {
    boolean bool = false;
    if (paramType != null) {
      if (this.type.equals(paramType))
        return true; 
      if (this.type instanceof Class)
        return this.rawType.isAssignableFrom(.Gson.Types.getRawType(paramType)); 
      if (this.type instanceof ParameterizedType)
        return isAssignableFrom(paramType, (ParameterizedType)this.type, new HashMap<String, Type>()); 
      if (this.type instanceof GenericArrayType)
        return (this.rawType.isAssignableFrom(.Gson.Types.getRawType(paramType)) && isAssignableFrom(paramType, (GenericArrayType)this.type)); 
      throw buildUnexpectedTypeError(this.type, new Class[] { Class.class, ParameterizedType.class, GenericArrayType.class });
    } 
    return bool;
  }
  
  public final String toString() {
    return .Gson.Types.typeToString(this.type);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/reflect/TypeToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */