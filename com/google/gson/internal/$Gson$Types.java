package com.google.gson.internal;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public final class $Gson$Types {
  static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
  
  public static GenericArrayType arrayOf(Type paramType) {
    return new GenericArrayTypeImpl(paramType);
  }
  
  public static Type canonicalize(Type paramType) {
    if (paramType instanceof Class) {
      Class clazz = (Class)paramType;
      paramType = clazz;
      if (clazz.isArray())
        paramType = new GenericArrayTypeImpl(canonicalize(clazz.getComponentType())); 
      return paramType;
    } 
    if (paramType instanceof ParameterizedType) {
      paramType = paramType;
      return new ParameterizedTypeImpl(paramType.getOwnerType(), paramType.getRawType(), paramType.getActualTypeArguments());
    } 
    if (paramType instanceof GenericArrayType)
      return new GenericArrayTypeImpl(((GenericArrayType)paramType).getGenericComponentType()); 
    if (paramType instanceof WildcardType) {
      paramType = paramType;
      paramType = new WildcardTypeImpl(paramType.getUpperBounds(), paramType.getLowerBounds());
    } 
    return paramType;
  }
  
  private static void checkNotPrimitive(Type paramType) {
    boolean bool;
    if (!(paramType instanceof Class) || !((Class)paramType).isPrimitive()) {
      bool = true;
    } else {
      bool = false;
    } 
    $Gson$Preconditions.checkArgument(bool);
  }
  
  private static Class<?> declaringClassOf(TypeVariable<?> paramTypeVariable) {
    paramTypeVariable = (TypeVariable<?>)paramTypeVariable.getGenericDeclaration();
    return (paramTypeVariable instanceof Class) ? (Class)paramTypeVariable : null;
  }
  
  static boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static boolean equals(Type paramType1, Type paramType2) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool4 = false;
    if (paramType1 == paramType2)
      return true; 
    if (paramType1 instanceof Class)
      return paramType1.equals(paramType2); 
    if (paramType1 instanceof ParameterizedType) {
      boolean bool = bool4;
      if (paramType2 instanceof ParameterizedType) {
        paramType1 = paramType1;
        paramType2 = paramType2;
        if (equal(paramType1.getOwnerType(), paramType2.getOwnerType()) && paramType1.getRawType().equals(paramType2.getRawType()) && Arrays.equals((Object[])paramType1.getActualTypeArguments(), (Object[])paramType2.getActualTypeArguments()))
          return bool3; 
        bool = false;
      } 
      return bool;
    } 
    if (paramType1 instanceof GenericArrayType) {
      boolean bool = bool4;
      if (paramType2 instanceof GenericArrayType) {
        paramType1 = paramType1;
        paramType2 = paramType2;
        bool = equals(paramType1.getGenericComponentType(), paramType2.getGenericComponentType());
      } 
      return bool;
    } 
    if (paramType1 instanceof WildcardType) {
      boolean bool = bool4;
      if (paramType2 instanceof WildcardType) {
        paramType1 = paramType1;
        paramType2 = paramType2;
        if (Arrays.equals((Object[])paramType1.getUpperBounds(), (Object[])paramType2.getUpperBounds()) && Arrays.equals((Object[])paramType1.getLowerBounds(), (Object[])paramType2.getLowerBounds()))
          return bool1; 
        bool = false;
      } 
      return bool;
    } 
    boolean bool5 = bool4;
    if (paramType1 instanceof TypeVariable) {
      bool5 = bool4;
      if (paramType2 instanceof TypeVariable) {
        paramType1 = paramType1;
        paramType2 = paramType2;
        if (paramType1.getGenericDeclaration() == paramType2.getGenericDeclaration() && paramType1.getName().equals(paramType2.getName()))
          return bool2; 
        bool5 = false;
      } 
    } 
    return bool5;
  }
  
  public static Type getArrayComponentType(Type<?> paramType) {
    return (paramType instanceof GenericArrayType) ? ((GenericArrayType)paramType).getGenericComponentType() : ((Class)paramType).getComponentType();
  }
  
  public static Type getCollectionElementType(Type<Object> paramType, Class<?> paramClass) {
    Type<Object> type = getSupertype(paramType, paramClass, Collection.class);
    paramType = type;
    if (type instanceof WildcardType)
      paramType = ((WildcardType)type).getUpperBounds()[0]; 
    return (paramType instanceof ParameterizedType) ? ((ParameterizedType)paramType).getActualTypeArguments()[0] : Object.class;
  }
  
  static Type getGenericSupertype(Type<?> paramType, Class<?> paramClass1, Class<?> paramClass2) {
    // Byte code:
    //   0: aload_2
    //   1: aload_1
    //   2: if_acmpne -> 7
    //   5: aload_0
    //   6: areturn
    //   7: aload_2
    //   8: invokevirtual isInterface : ()Z
    //   11: ifeq -> 81
    //   14: aload_1
    //   15: invokevirtual getInterfaces : ()[Ljava/lang/Class;
    //   18: astore_0
    //   19: iconst_0
    //   20: istore_3
    //   21: aload_0
    //   22: arraylength
    //   23: istore #4
    //   25: iload_3
    //   26: iload #4
    //   28: if_icmpge -> 81
    //   31: aload_0
    //   32: iload_3
    //   33: aaload
    //   34: aload_2
    //   35: if_acmpne -> 48
    //   38: aload_1
    //   39: invokevirtual getGenericInterfaces : ()[Ljava/lang/reflect/Type;
    //   42: iload_3
    //   43: aaload
    //   44: astore_0
    //   45: goto -> 5
    //   48: aload_2
    //   49: aload_0
    //   50: iload_3
    //   51: aaload
    //   52: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   55: ifeq -> 75
    //   58: aload_1
    //   59: invokevirtual getGenericInterfaces : ()[Ljava/lang/reflect/Type;
    //   62: iload_3
    //   63: aaload
    //   64: aload_0
    //   65: iload_3
    //   66: aaload
    //   67: aload_2
    //   68: invokestatic getGenericSupertype : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/reflect/Type;
    //   71: astore_0
    //   72: goto -> 5
    //   75: iinc #3, 1
    //   78: goto -> 25
    //   81: aload_1
    //   82: invokevirtual isInterface : ()Z
    //   85: ifne -> 138
    //   88: aload_1
    //   89: ldc java/lang/Object
    //   91: if_acmpeq -> 138
    //   94: aload_1
    //   95: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   98: astore_0
    //   99: aload_0
    //   100: aload_2
    //   101: if_acmpne -> 112
    //   104: aload_1
    //   105: invokevirtual getGenericSuperclass : ()Ljava/lang/reflect/Type;
    //   108: astore_0
    //   109: goto -> 5
    //   112: aload_2
    //   113: aload_0
    //   114: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   117: ifeq -> 133
    //   120: aload_1
    //   121: invokevirtual getGenericSuperclass : ()Ljava/lang/reflect/Type;
    //   124: aload_0
    //   125: aload_2
    //   126: invokestatic getGenericSupertype : (Ljava/lang/reflect/Type;Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/reflect/Type;
    //   129: astore_0
    //   130: goto -> 5
    //   133: aload_0
    //   134: astore_1
    //   135: goto -> 88
    //   138: aload_2
    //   139: astore_0
    //   140: goto -> 5
  }
  
  public static Type[] getMapKeyAndValueTypes(Type paramType, Class<?> paramClass) {
    Type[] arrayOfType2;
    if (paramType == Properties.class) {
      arrayOfType2 = new Type[2];
      arrayOfType2[0] = String.class;
      arrayOfType2[1] = String.class;
      return arrayOfType2;
    } 
    Type type = getSupertype((Type)arrayOfType2, paramClass, Map.class);
    if (type instanceof ParameterizedType)
      return ((ParameterizedType)type).getActualTypeArguments(); 
    Type[] arrayOfType1 = new Type[2];
    arrayOfType1[0] = Object.class;
    arrayOfType1[1] = Object.class;
    return arrayOfType1;
  }
  
  public static Class<?> getRawType(Type<?> paramType) {
    if (paramType instanceof Class)
      return (Class)paramType; 
    if (paramType instanceof ParameterizedType) {
      paramType = ((ParameterizedType)paramType).getRawType();
      $Gson$Preconditions.checkArgument(paramType instanceof Class);
      return (Class)paramType;
    } 
    if (paramType instanceof GenericArrayType)
      return Array.newInstance(getRawType(((GenericArrayType)paramType).getGenericComponentType()), 0).getClass(); 
    if (paramType instanceof TypeVariable)
      return Object.class; 
    if (paramType instanceof WildcardType)
      return getRawType(((WildcardType)paramType).getUpperBounds()[0]); 
    if (paramType == null) {
      String str1 = "null";
      throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + paramType + "> is of type " + str1);
    } 
    String str = paramType.getClass().getName();
    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + paramType + "> is of type " + str);
  }
  
  static Type getSupertype(Type paramType, Class<?> paramClass1, Class<?> paramClass2) {
    $Gson$Preconditions.checkArgument(paramClass2.isAssignableFrom(paramClass1));
    return resolve(paramType, paramClass1, getGenericSupertype(paramType, paramClass1, paramClass2));
  }
  
  private static int hashCodeOrZero(Object paramObject) {
    return (paramObject != null) ? paramObject.hashCode() : 0;
  }
  
  private static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
    for (byte b = 0; b < paramArrayOfObject.length; b++) {
      if (paramObject.equals(paramArrayOfObject[b]))
        return b; 
    } 
    throw new NoSuchElementException();
  }
  
  public static ParameterizedType newParameterizedTypeWithOwner(Type paramType1, Type paramType2, Type... paramVarArgs) {
    return new ParameterizedTypeImpl(paramType1, paramType2, paramVarArgs);
  }
  
  public static Type resolve(Type paramType1, Class<?> paramClass, Type paramType2) {
    while (paramType2 instanceof TypeVariable) {
      TypeVariable<?> typeVariable = (TypeVariable)paramType2;
      Type type = resolveTypeVariable(paramType1, paramClass, typeVariable);
      paramType2 = type;
      if (type == typeVariable)
        return type; 
    } 
    if (paramType2 instanceof Class && ((Class)paramType2).isArray()) {
      paramType2 = paramType2;
      Class<?> clazz = paramType2.getComponentType();
      paramType1 = resolve(paramType1, paramClass, clazz);
      if (clazz == paramType1) {
        paramType1 = paramType2;
      } else {
        paramType1 = arrayOf(paramType1);
      } 
      return paramType1;
    } 
    if (paramType2 instanceof GenericArrayType) {
      paramType2 = paramType2;
      Type type = paramType2.getGenericComponentType();
      paramType1 = resolve(paramType1, paramClass, type);
      if (type != paramType1)
        paramType2 = arrayOf(paramType1); 
      return paramType2;
    } 
    if (paramType2 instanceof ParameterizedType) {
      boolean bool;
      ParameterizedType parameterizedType = (ParameterizedType)paramType2;
      paramType2 = parameterizedType.getOwnerType();
      Type type = resolve(paramType1, paramClass, paramType2);
      if (type != paramType2) {
        bool = true;
      } else {
        bool = false;
      } 
      Type[] arrayOfType = parameterizedType.getActualTypeArguments();
      byte b = 0;
      int i = arrayOfType.length;
      while (b < i) {
        Type type1 = resolve(paramType1, paramClass, arrayOfType[b]);
        Type[] arrayOfType1 = arrayOfType;
        boolean bool1 = bool;
        if (type1 != arrayOfType[b]) {
          arrayOfType1 = arrayOfType;
          bool1 = bool;
          if (!bool) {
            arrayOfType1 = (Type[])arrayOfType.clone();
            bool1 = true;
          } 
          arrayOfType1[b] = type1;
        } 
        b++;
        arrayOfType = arrayOfType1;
        bool = bool1;
      } 
      paramType2 = parameterizedType;
      if (bool)
        paramType2 = newParameterizedTypeWithOwner(type, parameterizedType.getRawType(), arrayOfType); 
      return paramType2;
    } 
    if (paramType2 instanceof WildcardType) {
      WildcardType wildcardType = (WildcardType)paramType2;
      Type[] arrayOfType2 = wildcardType.getLowerBounds();
      Type[] arrayOfType1 = wildcardType.getUpperBounds();
      if (arrayOfType2.length == 1) {
        paramType1 = resolve(paramType1, paramClass, arrayOfType2[0]);
        paramType2 = wildcardType;
        if (paramType1 != arrayOfType2[0])
          paramType2 = supertypeOf(paramType1); 
        return paramType2;
      } 
      paramType2 = wildcardType;
      if (arrayOfType1.length == 1) {
        paramType1 = resolve(paramType1, paramClass, arrayOfType1[0]);
        paramType2 = wildcardType;
        if (paramType1 != arrayOfType1[0])
          paramType2 = subtypeOf(paramType1); 
      } 
    } 
    return paramType2;
  }
  
  static Type resolveTypeVariable(Type<?> paramType, Class<?> paramClass, TypeVariable<?> paramTypeVariable) {
    Class<?> clazz = declaringClassOf(paramTypeVariable);
    if (clazz == null)
      return paramTypeVariable; 
    Type type = getGenericSupertype(paramType, paramClass, clazz);
    paramType = paramTypeVariable;
    if (type instanceof ParameterizedType) {
      int i = indexOf((Object[])clazz.getTypeParameters(), paramTypeVariable);
      paramType = ((ParameterizedType)type).getActualTypeArguments()[i];
    } 
    return paramType;
  }
  
  public static WildcardType subtypeOf(Type paramType) {
    Type[] arrayOfType = EMPTY_TYPE_ARRAY;
    return new WildcardTypeImpl(new Type[] { paramType }, arrayOfType);
  }
  
  public static WildcardType supertypeOf(Type paramType) {
    return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { paramType });
  }
  
  public static String typeToString(Type paramType) {
    return (paramType instanceof Class) ? ((Class)paramType).getName() : paramType.toString();
  }
  
  private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
    private static final long serialVersionUID = 0L;
    
    private final Type componentType;
    
    public GenericArrayTypeImpl(Type param1Type) {
      this.componentType = $Gson$Types.canonicalize(param1Type);
    }
    
    public boolean equals(Object param1Object) {
      return (param1Object instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)param1Object));
    }
    
    public Type getGenericComponentType() {
      return this.componentType;
    }
    
    public int hashCode() {
      return this.componentType.hashCode();
    }
    
    public String toString() {
      return $Gson$Types.typeToString(this.componentType) + "[]";
    }
  }
  
  private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
    private static final long serialVersionUID = 0L;
    
    private final Type ownerType;
    
    private final Type rawType;
    
    private final Type[] typeArguments;
    
    public ParameterizedTypeImpl(Type param1Type1, Type param1Type2, Type... param1VarArgs) {
      // Byte code:
      //   0: iconst_0
      //   1: istore #4
      //   3: aload_0
      //   4: invokespecial <init> : ()V
      //   7: aload_2
      //   8: instanceof java/lang/Class
      //   11: ifeq -> 64
      //   14: aload_2
      //   15: checkcast java/lang/Class
      //   18: astore #5
      //   20: aload_1
      //   21: ifnonnull -> 32
      //   24: aload #5
      //   26: invokevirtual getEnclosingClass : ()Ljava/lang/Class;
      //   29: ifnonnull -> 151
      //   32: iconst_1
      //   33: istore #6
      //   35: iload #6
      //   37: invokestatic checkArgument : (Z)V
      //   40: aload_1
      //   41: ifnull -> 56
      //   44: iload #4
      //   46: istore #6
      //   48: aload #5
      //   50: invokevirtual getEnclosingClass : ()Ljava/lang/Class;
      //   53: ifnull -> 59
      //   56: iconst_1
      //   57: istore #6
      //   59: iload #6
      //   61: invokestatic checkArgument : (Z)V
      //   64: aload_1
      //   65: ifnonnull -> 157
      //   68: aconst_null
      //   69: astore_1
      //   70: aload_0
      //   71: aload_1
      //   72: putfield ownerType : Ljava/lang/reflect/Type;
      //   75: aload_0
      //   76: aload_2
      //   77: invokestatic canonicalize : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
      //   80: putfield rawType : Ljava/lang/reflect/Type;
      //   83: aload_0
      //   84: aload_3
      //   85: invokevirtual clone : ()Ljava/lang/Object;
      //   88: checkcast [Ljava/lang/reflect/Type;
      //   91: putfield typeArguments : [Ljava/lang/reflect/Type;
      //   94: iconst_0
      //   95: istore #7
      //   97: iload #7
      //   99: aload_0
      //   100: getfield typeArguments : [Ljava/lang/reflect/Type;
      //   103: arraylength
      //   104: if_icmpge -> 165
      //   107: aload_0
      //   108: getfield typeArguments : [Ljava/lang/reflect/Type;
      //   111: iload #7
      //   113: aaload
      //   114: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
      //   117: pop
      //   118: aload_0
      //   119: getfield typeArguments : [Ljava/lang/reflect/Type;
      //   122: iload #7
      //   124: aaload
      //   125: invokestatic access$000 : (Ljava/lang/reflect/Type;)V
      //   128: aload_0
      //   129: getfield typeArguments : [Ljava/lang/reflect/Type;
      //   132: iload #7
      //   134: aload_0
      //   135: getfield typeArguments : [Ljava/lang/reflect/Type;
      //   138: iload #7
      //   140: aaload
      //   141: invokestatic canonicalize : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
      //   144: aastore
      //   145: iinc #7, 1
      //   148: goto -> 97
      //   151: iconst_0
      //   152: istore #6
      //   154: goto -> 35
      //   157: aload_1
      //   158: invokestatic canonicalize : (Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
      //   161: astore_1
      //   162: goto -> 70
      //   165: return
    }
    
    public boolean equals(Object param1Object) {
      return (param1Object instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)param1Object));
    }
    
    public Type[] getActualTypeArguments() {
      return (Type[])this.typeArguments.clone();
    }
    
    public Type getOwnerType() {
      return this.ownerType;
    }
    
    public Type getRawType() {
      return this.rawType;
    }
    
    public int hashCode() {
      return Arrays.hashCode((Object[])this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.hashCodeOrZero(this.ownerType);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
      stringBuilder.append($Gson$Types.typeToString(this.rawType));
      if (this.typeArguments.length == 0)
        return stringBuilder.toString(); 
      stringBuilder.append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
      for (byte b = 1; b < this.typeArguments.length; b++)
        stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[b])); 
      return stringBuilder.append(">").toString();
    }
  }
  
  private static final class WildcardTypeImpl implements WildcardType, Serializable {
    private static final long serialVersionUID = 0L;
    
    private final Type lowerBound;
    
    private final Type upperBound;
    
    public WildcardTypeImpl(Type[] param1ArrayOfType1, Type[] param1ArrayOfType2) {
      boolean bool2;
      if (param1ArrayOfType2.length <= 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      $Gson$Preconditions.checkArgument(bool2);
      if (param1ArrayOfType1.length == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      $Gson$Preconditions.checkArgument(bool2);
      if (param1ArrayOfType2.length == 1) {
        $Gson$Preconditions.checkNotNull(param1ArrayOfType2[0]);
        $Gson$Types.checkNotPrimitive(param1ArrayOfType2[0]);
        if (param1ArrayOfType1[0] == Object.class) {
          bool2 = bool1;
        } else {
          bool2 = false;
        } 
        $Gson$Preconditions.checkArgument(bool2);
        this.lowerBound = $Gson$Types.canonicalize(param1ArrayOfType2[0]);
        this.upperBound = Object.class;
        return;
      } 
      $Gson$Preconditions.checkNotNull(param1ArrayOfType1[0]);
      $Gson$Types.checkNotPrimitive(param1ArrayOfType1[0]);
      this.lowerBound = null;
      this.upperBound = $Gson$Types.canonicalize(param1ArrayOfType1[0]);
    }
    
    public boolean equals(Object param1Object) {
      return (param1Object instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)param1Object));
    }
    
    public Type[] getLowerBounds() {
      if (this.lowerBound != null) {
        Type[] arrayOfType = new Type[1];
        arrayOfType[0] = this.lowerBound;
        return arrayOfType;
      } 
      return $Gson$Types.EMPTY_TYPE_ARRAY;
    }
    
    public Type[] getUpperBounds() {
      return new Type[] { this.upperBound };
    }
    
    public int hashCode() {
      if (this.lowerBound != null) {
        int i = this.lowerBound.hashCode() + 31;
        return i ^ this.upperBound.hashCode() + 31;
      } 
      boolean bool = true;
      return bool ^ this.upperBound.hashCode() + 31;
    }
    
    public String toString() {
      return (this.lowerBound != null) ? ("? super " + $Gson$Types.typeToString(this.lowerBound)) : ((this.upperBound == Object.class) ? "?" : ("? extends " + $Gson$Types.typeToString(this.upperBound)));
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/$Gson$Types.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */