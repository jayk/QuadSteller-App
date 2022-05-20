package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Excluder implements TypeAdapterFactory, Cloneable {
  public static final Excluder DEFAULT = new Excluder();
  
  private static final double IGNORE_VERSIONS = -1.0D;
  
  private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();
  
  private int modifiers = 136;
  
  private boolean requireExpose;
  
  private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
  
  private boolean serializeInnerClasses = true;
  
  private double version = -1.0D;
  
  private boolean isAnonymousOrLocal(Class<?> paramClass) {
    return (!Enum.class.isAssignableFrom(paramClass) && (paramClass.isAnonymousClass() || paramClass.isLocalClass()));
  }
  
  private boolean isInnerClass(Class<?> paramClass) {
    return (paramClass.isMemberClass() && !isStatic(paramClass));
  }
  
  private boolean isStatic(Class<?> paramClass) {
    return ((paramClass.getModifiers() & 0x8) != 0);
  }
  
  private boolean isValidSince(Since paramSince) {
    return !(paramSince != null && paramSince.value() > this.version);
  }
  
  private boolean isValidUntil(Until paramUntil) {
    return !(paramUntil != null && paramUntil.value() <= this.version);
  }
  
  private boolean isValidVersion(Since paramSince, Until paramUntil) {
    return (isValidSince(paramSince) && isValidUntil(paramUntil));
  }
  
  protected Excluder clone() {
    try {
      return (Excluder)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new AssertionError();
    } 
  }
  
  public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
    Class<?> clazz = type.getRawType();
    final boolean skipSerialize = excludeClass(clazz, true);
    final boolean skipDeserialize = excludeClass(clazz, false);
    return (!bool1 && !bool2) ? null : new TypeAdapter<T>() {
        private TypeAdapter<T> delegate;
        
        private TypeAdapter<T> delegate() {
          TypeAdapter<T> typeAdapter = this.delegate;
          if (typeAdapter == null) {
            typeAdapter = gson.getDelegateAdapter(Excluder.this, type);
            this.delegate = typeAdapter;
          } 
          return typeAdapter;
        }
        
        public T read(JsonReader param1JsonReader) throws IOException {
          if (skipDeserialize) {
            param1JsonReader.skipValue();
            return null;
          } 
          return (T)delegate().read(param1JsonReader);
        }
        
        public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
          if (skipSerialize) {
            param1JsonWriter.nullValue();
            return;
          } 
          delegate().write(param1JsonWriter, param1T);
        }
      };
  }
  
  public Excluder disableInnerClassSerialization() {
    Excluder excluder = clone();
    excluder.serializeInnerClasses = false;
    return excluder;
  }
  
  public boolean excludeClass(Class<?> paramClass, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield version : D
    //   4: ldc2_w -1.0
    //   7: dcmpl
    //   8: ifeq -> 40
    //   11: aload_0
    //   12: aload_1
    //   13: ldc com/google/gson/annotations/Since
    //   15: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   18: checkcast com/google/gson/annotations/Since
    //   21: aload_1
    //   22: ldc com/google/gson/annotations/Until
    //   24: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   27: checkcast com/google/gson/annotations/Until
    //   30: invokespecial isValidVersion : (Lcom/google/gson/annotations/Since;Lcom/google/gson/annotations/Until;)Z
    //   33: ifne -> 40
    //   36: iconst_1
    //   37: istore_2
    //   38: iload_2
    //   39: ireturn
    //   40: aload_0
    //   41: getfield serializeInnerClasses : Z
    //   44: ifne -> 60
    //   47: aload_0
    //   48: aload_1
    //   49: invokespecial isInnerClass : (Ljava/lang/Class;)Z
    //   52: ifeq -> 60
    //   55: iconst_1
    //   56: istore_2
    //   57: goto -> 38
    //   60: aload_0
    //   61: aload_1
    //   62: invokespecial isAnonymousOrLocal : (Ljava/lang/Class;)Z
    //   65: ifeq -> 73
    //   68: iconst_1
    //   69: istore_2
    //   70: goto -> 38
    //   73: iload_2
    //   74: ifeq -> 121
    //   77: aload_0
    //   78: getfield serializationStrategies : Ljava/util/List;
    //   81: astore_3
    //   82: aload_3
    //   83: invokeinterface iterator : ()Ljava/util/Iterator;
    //   88: astore_3
    //   89: aload_3
    //   90: invokeinterface hasNext : ()Z
    //   95: ifeq -> 129
    //   98: aload_3
    //   99: invokeinterface next : ()Ljava/lang/Object;
    //   104: checkcast com/google/gson/ExclusionStrategy
    //   107: aload_1
    //   108: invokeinterface shouldSkipClass : (Ljava/lang/Class;)Z
    //   113: ifeq -> 89
    //   116: iconst_1
    //   117: istore_2
    //   118: goto -> 38
    //   121: aload_0
    //   122: getfield deserializationStrategies : Ljava/util/List;
    //   125: astore_3
    //   126: goto -> 82
    //   129: iconst_0
    //   130: istore_2
    //   131: goto -> 38
  }
  
  public boolean excludeField(Field paramField, boolean paramBoolean) {
    FieldAttributes fieldAttributes;
    List<ExclusionStrategy> list;
    Iterator<ExclusionStrategy> iterator;
    if ((this.modifiers & paramField.getModifiers()) != 0)
      return true; 
    if (this.version != -1.0D && !isValidVersion(paramField.<Since>getAnnotation(Since.class), paramField.<Until>getAnnotation(Until.class)))
      return true; 
    if (paramField.isSynthetic())
      return true; 
    if (this.requireExpose) {
      Expose expose = paramField.<Expose>getAnnotation(Expose.class);
      if (expose == null || (paramBoolean ? !expose.serialize() : !expose.deserialize()))
        return true; 
    } 
    if (!this.serializeInnerClasses && isInnerClass(paramField.getType()))
      return true; 
    if (isAnonymousOrLocal(paramField.getType()))
      return true; 
    if (paramBoolean) {
      list = this.serializationStrategies;
    } else {
      list = this.deserializationStrategies;
    } 
    if (!list.isEmpty()) {
      fieldAttributes = new FieldAttributes(paramField);
      iterator = list.iterator();
      while (iterator.hasNext()) {
        if (((ExclusionStrategy)iterator.next()).shouldSkipField(fieldAttributes))
          return true; 
      } 
    } 
    paramBoolean = false;
    while (iterator.hasNext()) {
      if (((ExclusionStrategy)iterator.next()).shouldSkipField(fieldAttributes))
        return true; 
    } 
  }
  
  public Excluder excludeFieldsWithoutExposeAnnotation() {
    Excluder excluder = clone();
    excluder.requireExpose = true;
    return excluder;
  }
  
  public Excluder withExclusionStrategy(ExclusionStrategy paramExclusionStrategy, boolean paramBoolean1, boolean paramBoolean2) {
    Excluder excluder = clone();
    if (paramBoolean1) {
      excluder.serializationStrategies = new ArrayList<ExclusionStrategy>(this.serializationStrategies);
      excluder.serializationStrategies.add(paramExclusionStrategy);
    } 
    if (paramBoolean2) {
      excluder.deserializationStrategies = new ArrayList<ExclusionStrategy>(this.deserializationStrategies);
      excluder.deserializationStrategies.add(paramExclusionStrategy);
    } 
    return excluder;
  }
  
  public Excluder withModifiers(int... paramVarArgs) {
    Excluder excluder = clone();
    excluder.modifiers = 0;
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      int j = paramVarArgs[b];
      excluder.modifiers |= j;
    } 
    return excluder;
  }
  
  public Excluder withVersion(double paramDouble) {
    Excluder excluder = clone();
    excluder.version = paramDouble;
    return excluder;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/Excluder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */