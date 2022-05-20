package org.xutils.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ParameterizedTypeUtil {
  public static Type getParameterizedType(Type<Object> paramType, Class<?> paramClass, int paramInt) {
    TypeVariable[] arrayOfTypeVariable;
    Class<?> clazz1;
    Type[] arrayOfType1 = null;
    ParameterizedType parameterizedType = null;
    if (paramType instanceof ParameterizedType) {
      parameterizedType = (ParameterizedType)paramType;
      clazz1 = (Class)parameterizedType.getRawType();
      arrayOfType1 = parameterizedType.getActualTypeArguments();
      arrayOfTypeVariable = clazz1.getTypeParameters();
    } else {
      clazz1 = (Class)paramType;
    } 
    if (paramClass == clazz1)
      return (arrayOfType1 != null) ? arrayOfType1[paramInt] : Object.class; 
    Type[] arrayOfType2 = clazz1.getGenericInterfaces();
    if (arrayOfType2 != null)
      for (byte b = 0; b < arrayOfType2.length; b++) {
        Type type = arrayOfType2[b];
        if (type instanceof ParameterizedType && paramClass.isAssignableFrom((Class)((ParameterizedType)type).getRawType())) {
          try {
            type = getTrueType(getParameterizedType(type, paramClass, paramInt), (TypeVariable<?>[])arrayOfTypeVariable, arrayOfType1);
            paramType = type;
          } catch (Throwable throwable) {}
          return paramType;
        } 
      }  
    Class<?> clazz2 = clazz1.getSuperclass();
    if (clazz2 != null && paramClass.isAssignableFrom(clazz2))
      return getTrueType(getParameterizedType(clazz1.getGenericSuperclass(), paramClass, paramInt), (TypeVariable<?>[])arrayOfTypeVariable, arrayOfType1); 
    throw new IllegalArgumentException("FindGenericType:" + paramType + ", declaredClass: " + paramClass + ", index: " + paramInt);
  }
  
  private static Type getTrueType(Type<?> paramType, TypeVariable<?>[] paramArrayOfTypeVariable, Type[] paramArrayOfType) {
    if (paramType instanceof TypeVariable) {
      TypeVariable<?> typeVariable = (TypeVariable)paramType;
      String str = typeVariable.getName();
      paramType = typeVariable;
      if (paramArrayOfType != null)
        for (byte b = 0;; b++) {
          paramType = typeVariable;
          if (b < paramArrayOfTypeVariable.length) {
            if (str.equals(paramArrayOfTypeVariable[b].getName()))
              return paramArrayOfType[b]; 
          } else {
            return paramType;
          } 
        }  
      return paramType;
    } 
    if (paramType instanceof GenericArrayType) {
      Type type = ((GenericArrayType)paramType).getGenericComponentType();
      if (type instanceof Class)
        paramType = Array.newInstance((Class)type, 0).getClass(); 
    } 
    return paramType;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/ParameterizedTypeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */