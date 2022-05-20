package org.xutils.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import org.xutils.common.util.LogUtil;
import org.xutils.db.converter.ColumnConverterFactory;

public final class ColumnUtils {
  private static final HashSet<Class<?>> AUTO_INCREMENT_TYPES;
  
  private static final HashSet<Class<?>> BOOLEAN_TYPES = new HashSet<Class<?>>(2);
  
  private static final HashSet<Class<?>> INTEGER_TYPES = new HashSet<Class<?>>(2);
  
  static {
    AUTO_INCREMENT_TYPES = new HashSet<Class<?>>(4);
    BOOLEAN_TYPES.add(boolean.class);
    BOOLEAN_TYPES.add(Boolean.class);
    INTEGER_TYPES.add(int.class);
    INTEGER_TYPES.add(Integer.class);
    AUTO_INCREMENT_TYPES.addAll(INTEGER_TYPES);
    AUTO_INCREMENT_TYPES.add(long.class);
    AUTO_INCREMENT_TYPES.add(Long.class);
  }
  
  public static Object convert2DbValueIfNeeded(Object paramObject) {
    Object object = paramObject;
    if (paramObject != null)
      object = ColumnConverterFactory.getColumnConverter(paramObject.getClass()).fieldValue2DbValue(paramObject); 
    return object;
  }
  
  private static Method findBooleanGetMethod(Class<?> paramClass, String paramString) {
    Method method;
    if (!paramString.startsWith("is"))
      paramString = "is" + paramString.substring(0, 1).toUpperCase() + paramString.substring(1); 
    try {
      Method method1 = paramClass.getDeclaredMethod(paramString, new Class[0]);
      method = method1;
    } catch (NoSuchMethodException noSuchMethodException) {
      LogUtil.d(method.getName() + "#" + paramString + " not exist");
      method = null;
    } 
    return method;
  }
  
  private static Method findBooleanSetMethod(Class<?> paramClass1, String paramString, Class<?> paramClass2) {
    Method method;
    if (paramString.startsWith("is")) {
      paramString = "set" + paramString.substring(2, 3).toUpperCase() + paramString.substring(3);
    } else {
      paramString = "set" + paramString.substring(0, 1).toUpperCase() + paramString.substring(1);
    } 
    try {
      Method method1 = paramClass1.getDeclaredMethod(paramString, new Class[] { paramClass2 });
      method = method1;
    } catch (NoSuchMethodException noSuchMethodException) {
      LogUtil.d(method.getName() + "#" + paramString + " not exist");
      method = null;
    } 
    return method;
  }
  
  static Method findGetMethod(Class<?> paramClass, Field paramField) {
    // Byte code:
    //   0: ldc java/lang/Object
    //   2: aload_0
    //   3: invokevirtual equals : (Ljava/lang/Object;)Z
    //   6: ifeq -> 13
    //   9: aconst_null
    //   10: astore_2
    //   11: aload_2
    //   12: areturn
    //   13: aload_1
    //   14: invokevirtual getName : ()Ljava/lang/String;
    //   17: astore_3
    //   18: aconst_null
    //   19: astore_2
    //   20: aload_1
    //   21: invokevirtual getType : ()Ljava/lang/Class;
    //   24: invokestatic isBoolean : (Ljava/lang/Class;)Z
    //   27: ifeq -> 36
    //   30: aload_0
    //   31: aload_3
    //   32: invokestatic findBooleanGetMethod : (Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Method;
    //   35: astore_2
    //   36: aload_2
    //   37: astore #4
    //   39: aload_2
    //   40: ifnonnull -> 90
    //   43: new java/lang/StringBuilder
    //   46: dup
    //   47: invokespecial <init> : ()V
    //   50: ldc 'get'
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: aload_3
    //   56: iconst_0
    //   57: iconst_1
    //   58: invokevirtual substring : (II)Ljava/lang/String;
    //   61: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: aload_3
    //   68: iconst_1
    //   69: invokevirtual substring : (I)Ljava/lang/String;
    //   72: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: invokevirtual toString : ()Ljava/lang/String;
    //   78: astore_3
    //   79: aload_0
    //   80: aload_3
    //   81: iconst_0
    //   82: anewarray java/lang/Class
    //   85: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   88: astore #4
    //   90: aload #4
    //   92: astore_2
    //   93: aload #4
    //   95: ifnonnull -> 11
    //   98: aload_0
    //   99: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   102: aload_1
    //   103: invokestatic findGetMethod : (Ljava/lang/Class;Ljava/lang/reflect/Field;)Ljava/lang/reflect/Method;
    //   106: astore_2
    //   107: goto -> 11
    //   110: astore #4
    //   112: new java/lang/StringBuilder
    //   115: dup
    //   116: invokespecial <init> : ()V
    //   119: aload_0
    //   120: invokevirtual getName : ()Ljava/lang/String;
    //   123: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   126: ldc '#'
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: aload_3
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: ldc ' not exist'
    //   137: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: invokevirtual toString : ()Ljava/lang/String;
    //   143: invokestatic d : (Ljava/lang/String;)V
    //   146: aload_2
    //   147: astore #4
    //   149: goto -> 90
    // Exception table:
    //   from	to	target	type
    //   79	90	110	java/lang/NoSuchMethodException
  }
  
  static Method findSetMethod(Class<?> paramClass, Field paramField) {
    // Byte code:
    //   0: ldc java/lang/Object
    //   2: aload_0
    //   3: invokevirtual equals : (Ljava/lang/Object;)Z
    //   6: ifeq -> 13
    //   9: aconst_null
    //   10: astore_2
    //   11: aload_2
    //   12: areturn
    //   13: aload_1
    //   14: invokevirtual getName : ()Ljava/lang/String;
    //   17: astore_3
    //   18: aload_1
    //   19: invokevirtual getType : ()Ljava/lang/Class;
    //   22: astore #4
    //   24: aconst_null
    //   25: astore_2
    //   26: aload #4
    //   28: invokestatic isBoolean : (Ljava/lang/Class;)Z
    //   31: ifeq -> 42
    //   34: aload_0
    //   35: aload_3
    //   36: aload #4
    //   38: invokestatic findBooleanSetMethod : (Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   41: astore_2
    //   42: aload_2
    //   43: astore #5
    //   45: aload_2
    //   46: ifnonnull -> 101
    //   49: new java/lang/StringBuilder
    //   52: dup
    //   53: invokespecial <init> : ()V
    //   56: ldc 'set'
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: aload_3
    //   62: iconst_0
    //   63: iconst_1
    //   64: invokevirtual substring : (II)Ljava/lang/String;
    //   67: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: aload_3
    //   74: iconst_1
    //   75: invokevirtual substring : (I)Ljava/lang/String;
    //   78: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: invokevirtual toString : ()Ljava/lang/String;
    //   84: astore_3
    //   85: aload_0
    //   86: aload_3
    //   87: iconst_1
    //   88: anewarray java/lang/Class
    //   91: dup
    //   92: iconst_0
    //   93: aload #4
    //   95: aastore
    //   96: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   99: astore #5
    //   101: aload #5
    //   103: astore_2
    //   104: aload #5
    //   106: ifnonnull -> 11
    //   109: aload_0
    //   110: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   113: aload_1
    //   114: invokestatic findSetMethod : (Ljava/lang/Class;Ljava/lang/reflect/Field;)Ljava/lang/reflect/Method;
    //   117: astore_2
    //   118: goto -> 11
    //   121: astore #5
    //   123: new java/lang/StringBuilder
    //   126: dup
    //   127: invokespecial <init> : ()V
    //   130: aload_0
    //   131: invokevirtual getName : ()Ljava/lang/String;
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: ldc '#'
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: aload_3
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: ldc ' not exist'
    //   148: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: invokevirtual toString : ()Ljava/lang/String;
    //   154: invokestatic d : (Ljava/lang/String;)V
    //   157: aload_2
    //   158: astore #5
    //   160: goto -> 101
    // Exception table:
    //   from	to	target	type
    //   85	101	121	java/lang/NoSuchMethodException
  }
  
  public static boolean isAutoIdType(Class<?> paramClass) {
    return AUTO_INCREMENT_TYPES.contains(paramClass);
  }
  
  public static boolean isBoolean(Class<?> paramClass) {
    return BOOLEAN_TYPES.contains(paramClass);
  }
  
  public static boolean isInteger(Class<?> paramClass) {
    return INTEGER_TYPES.contains(paramClass);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/ColumnUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */