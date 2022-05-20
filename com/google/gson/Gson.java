package com.google.gson;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Gson {
  static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
  
  private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
  
  private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
  
  private final ConstructorConstructor constructorConstructor;
  
  final JsonDeserializationContext deserializationContext = new JsonDeserializationContext() {
      public <T> T deserialize(JsonElement param1JsonElement, Type param1Type) throws JsonParseException {
        return Gson.this.fromJson(param1JsonElement, param1Type);
      }
    };
  
  private final List<TypeAdapterFactory> factories;
  
  private final boolean generateNonExecutableJson;
  
  private final boolean htmlSafe;
  
  private final boolean prettyPrinting;
  
  final JsonSerializationContext serializationContext = new JsonSerializationContext() {
      public JsonElement serialize(Object param1Object) {
        return Gson.this.toJsonTree(param1Object);
      }
      
      public JsonElement serialize(Object param1Object, Type param1Type) {
        return Gson.this.toJsonTree(param1Object, param1Type);
      }
    };
  
  private final boolean serializeNulls;
  
  private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = Collections.synchronizedMap(new HashMap<TypeToken<?>, TypeAdapter<?>>());
  
  public Gson() {
    this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
  }
  
  Gson(Excluder paramExcluder, FieldNamingStrategy paramFieldNamingStrategy, Map<Type, InstanceCreator<?>> paramMap, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, LongSerializationPolicy paramLongSerializationPolicy, List<TypeAdapterFactory> paramList) {
    this.constructorConstructor = new ConstructorConstructor(paramMap);
    this.serializeNulls = paramBoolean1;
    this.generateNonExecutableJson = paramBoolean3;
    this.htmlSafe = paramBoolean4;
    this.prettyPrinting = paramBoolean5;
    ArrayList<TypeAdapterFactory> arrayList = new ArrayList();
    arrayList.add(TypeAdapters.JSON_ELEMENT_FACTORY);
    arrayList.add(ObjectTypeAdapter.FACTORY);
    arrayList.add(paramExcluder);
    arrayList.addAll(paramList);
    arrayList.add(TypeAdapters.STRING_FACTORY);
    arrayList.add(TypeAdapters.INTEGER_FACTORY);
    arrayList.add(TypeAdapters.BOOLEAN_FACTORY);
    arrayList.add(TypeAdapters.BYTE_FACTORY);
    arrayList.add(TypeAdapters.SHORT_FACTORY);
    arrayList.add(TypeAdapters.newFactory(long.class, Long.class, longAdapter(paramLongSerializationPolicy)));
    arrayList.add(TypeAdapters.newFactory(double.class, Double.class, doubleAdapter(paramBoolean6)));
    arrayList.add(TypeAdapters.newFactory(float.class, Float.class, floatAdapter(paramBoolean6)));
    arrayList.add(TypeAdapters.NUMBER_FACTORY);
    arrayList.add(TypeAdapters.CHARACTER_FACTORY);
    arrayList.add(TypeAdapters.STRING_BUILDER_FACTORY);
    arrayList.add(TypeAdapters.STRING_BUFFER_FACTORY);
    arrayList.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
    arrayList.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
    arrayList.add(TypeAdapters.URL_FACTORY);
    arrayList.add(TypeAdapters.URI_FACTORY);
    arrayList.add(TypeAdapters.UUID_FACTORY);
    arrayList.add(TypeAdapters.LOCALE_FACTORY);
    arrayList.add(TypeAdapters.INET_ADDRESS_FACTORY);
    arrayList.add(TypeAdapters.BIT_SET_FACTORY);
    arrayList.add(DateTypeAdapter.FACTORY);
    arrayList.add(TypeAdapters.CALENDAR_FACTORY);
    arrayList.add(TimeTypeAdapter.FACTORY);
    arrayList.add(SqlDateTypeAdapter.FACTORY);
    arrayList.add(TypeAdapters.TIMESTAMP_FACTORY);
    arrayList.add(ArrayTypeAdapter.FACTORY);
    arrayList.add(TypeAdapters.ENUM_FACTORY);
    arrayList.add(TypeAdapters.CLASS_FACTORY);
    arrayList.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
    arrayList.add(new MapTypeAdapterFactory(this.constructorConstructor, paramBoolean2));
    arrayList.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, paramFieldNamingStrategy, paramExcluder));
    this.factories = Collections.unmodifiableList(arrayList);
  }
  
  private static void assertFullConsumption(Object paramObject, JsonReader paramJsonReader) {
    if (paramObject != null)
      try {
        if (paramJsonReader.peek() != JsonToken.END_DOCUMENT) {
          paramObject = new JsonIOException();
          super("JSON document was not fully consumed.");
          throw paramObject;
        } 
      } catch (MalformedJsonException malformedJsonException) {
        throw new JsonSyntaxException(malformedJsonException);
      } catch (IOException iOException) {
        throw new JsonIOException(iOException);
      }  
  }
  
  private void checkValidFloatingPoint(double paramDouble) {
    if (Double.isNaN(paramDouble) || Double.isInfinite(paramDouble))
      throw new IllegalArgumentException(paramDouble + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method."); 
  }
  
  private TypeAdapter<Number> doubleAdapter(boolean paramBoolean) {
    return paramBoolean ? TypeAdapters.DOUBLE : new TypeAdapter<Number>() {
        public Double read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Double.valueOf(param1JsonReader.nextDouble());
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          if (param1Number == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          double d = param1Number.doubleValue();
          Gson.this.checkValidFloatingPoint(d);
          param1JsonWriter.value(param1Number);
        }
      };
  }
  
  private TypeAdapter<Number> floatAdapter(boolean paramBoolean) {
    return paramBoolean ? TypeAdapters.FLOAT : new TypeAdapter<Number>() {
        public Float read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Float.valueOf((float)param1JsonReader.nextDouble());
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          if (param1Number == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          float f = param1Number.floatValue();
          Gson.this.checkValidFloatingPoint(f);
          param1JsonWriter.value(param1Number);
        }
      };
  }
  
  private TypeAdapter<Number> longAdapter(LongSerializationPolicy paramLongSerializationPolicy) {
    return (paramLongSerializationPolicy == LongSerializationPolicy.DEFAULT) ? TypeAdapters.LONG : new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Long.valueOf(param1JsonReader.nextLong());
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          if (param1Number == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          param1JsonWriter.value(param1Number.toString());
        }
      };
  }
  
  private JsonWriter newJsonWriter(Writer paramWriter) throws IOException {
    if (this.generateNonExecutableJson)
      paramWriter.write(")]}'\n"); 
    JsonWriter jsonWriter = new JsonWriter(paramWriter);
    if (this.prettyPrinting)
      jsonWriter.setIndent("  "); 
    jsonWriter.setSerializeNulls(this.serializeNulls);
    return jsonWriter;
  }
  
  public <T> T fromJson(JsonElement paramJsonElement, Class<T> paramClass) throws JsonSyntaxException {
    paramJsonElement = fromJson(paramJsonElement, paramClass);
    return Primitives.wrap(paramClass).cast(paramJsonElement);
  }
  
  public <T> T fromJson(JsonElement paramJsonElement, Type paramType) throws JsonSyntaxException {
    return (T)((paramJsonElement == null) ? null : fromJson((JsonReader)new JsonTreeReader(paramJsonElement), paramType));
  }
  
  public <T> T fromJson(JsonReader paramJsonReader, Type paramType) throws JsonIOException, JsonSyntaxException {
    boolean bool = true;
    boolean bool1 = paramJsonReader.isLenient();
    paramJsonReader.setLenient(true);
    try {
      paramJsonReader.peek();
      bool = false;
      paramType = getAdapter(TypeToken.get(paramType)).read(paramJsonReader);
      return (T)paramType;
    } catch (EOFException eOFException) {
      if (bool) {
        paramType = null;
        return (T)paramType;
      } 
      JsonSyntaxException jsonSyntaxException = new JsonSyntaxException();
      this(eOFException);
      throw jsonSyntaxException;
    } catch (IllegalStateException illegalStateException) {
      JsonSyntaxException jsonSyntaxException = new JsonSyntaxException();
      this(illegalStateException);
      throw jsonSyntaxException;
    } catch (IOException iOException) {
      JsonSyntaxException jsonSyntaxException = new JsonSyntaxException();
      this(iOException);
      throw jsonSyntaxException;
    } finally {
      paramJsonReader.setLenient(bool1);
    } 
  }
  
  public <T> T fromJson(Reader paramReader, Class<T> paramClass) throws JsonSyntaxException, JsonIOException {
    JsonReader jsonReader = new JsonReader(paramReader);
    Object object = fromJson(jsonReader, paramClass);
    assertFullConsumption(object, jsonReader);
    return Primitives.wrap(paramClass).cast(object);
  }
  
  public <T> T fromJson(Reader paramReader, Type paramType) throws JsonIOException, JsonSyntaxException {
    JsonReader jsonReader = new JsonReader(paramReader);
    paramType = fromJson(jsonReader, paramType);
    assertFullConsumption(paramType, jsonReader);
    return (T)paramType;
  }
  
  public <T> T fromJson(String paramString, Class<T> paramClass) throws JsonSyntaxException {
    paramString = fromJson(paramString, paramClass);
    return Primitives.wrap(paramClass).cast(paramString);
  }
  
  public <T> T fromJson(String paramString, Type paramType) throws JsonSyntaxException {
    return (T)((paramString == null) ? null : fromJson(new StringReader(paramString), paramType));
  }
  
  public <T> TypeAdapter<T> getAdapter(TypeToken<T> paramTypeToken) {
    TypeAdapter<T> typeAdapter = (TypeAdapter)this.typeTokenCache.get(paramTypeToken);
    if (typeAdapter != null)
      return typeAdapter; 
    Map<Object, Object> map2 = (Map)this.calls.get();
    boolean bool = false;
    Map<Object, Object> map1 = map2;
    if (map2 == null) {
      map1 = new HashMap<Object, Object>();
      this.calls.set(map1);
      bool = true;
    } 
    null = (FutureTypeAdapter)map1.get(paramTypeToken);
    if (null != null)
      return null; 
    try {
      FutureTypeAdapter<T> futureTypeAdapter = new FutureTypeAdapter();
      this();
      map1.put(paramTypeToken, futureTypeAdapter);
      Iterator<TypeAdapterFactory> iterator = this.factories.iterator();
      while (iterator.hasNext()) {
        TypeAdapter<T> typeAdapter1 = ((TypeAdapterFactory)iterator.next()).create(this, paramTypeToken);
        if (typeAdapter1 != null) {
          futureTypeAdapter.setDelegate(typeAdapter1);
          this.typeTokenCache.put(paramTypeToken, typeAdapter1);
          return typeAdapter1;
        } 
      } 
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("GSON cannot handle ").append(paramTypeToken).toString());
      throw illegalArgumentException;
    } finally {
      map1.remove(paramTypeToken);
      if (bool)
        this.calls.remove(); 
    } 
  }
  
  public <T> TypeAdapter<T> getAdapter(Class<T> paramClass) {
    return getAdapter(TypeToken.get(paramClass));
  }
  
  public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory paramTypeAdapterFactory, TypeToken<T> paramTypeToken) {
    boolean bool = false;
    for (TypeAdapterFactory typeAdapterFactory : this.factories) {
      if (!bool) {
        if (typeAdapterFactory == paramTypeAdapterFactory)
          bool = true; 
        continue;
      } 
      TypeAdapter<T> typeAdapter = typeAdapterFactory.create(this, paramTypeToken);
      if (typeAdapter != null)
        return typeAdapter; 
    } 
    throw new IllegalArgumentException("GSON cannot serialize " + paramTypeToken);
  }
  
  public String toJson(JsonElement paramJsonElement) {
    StringWriter stringWriter = new StringWriter();
    toJson(paramJsonElement, stringWriter);
    return stringWriter.toString();
  }
  
  public String toJson(Object paramObject) {
    return (paramObject == null) ? toJson(JsonNull.INSTANCE) : toJson(paramObject, paramObject.getClass());
  }
  
  public String toJson(Object paramObject, Type paramType) {
    StringWriter stringWriter = new StringWriter();
    toJson(paramObject, paramType, stringWriter);
    return stringWriter.toString();
  }
  
  public void toJson(JsonElement paramJsonElement, JsonWriter paramJsonWriter) throws JsonIOException {
    boolean bool1 = paramJsonWriter.isLenient();
    paramJsonWriter.setLenient(true);
    boolean bool2 = paramJsonWriter.isHtmlSafe();
    paramJsonWriter.setHtmlSafe(this.htmlSafe);
    boolean bool3 = paramJsonWriter.getSerializeNulls();
    paramJsonWriter.setSerializeNulls(this.serializeNulls);
    try {
      Streams.write(paramJsonElement, paramJsonWriter);
      return;
    } catch (IOException iOException) {
      JsonIOException jsonIOException = new JsonIOException();
      this(iOException);
      throw jsonIOException;
    } finally {
      paramJsonWriter.setLenient(bool1);
      paramJsonWriter.setHtmlSafe(bool2);
      paramJsonWriter.setSerializeNulls(bool3);
    } 
  }
  
  public void toJson(JsonElement paramJsonElement, Appendable paramAppendable) throws JsonIOException {
    try {
      toJson(paramJsonElement, newJsonWriter(Streams.writerForAppendable(paramAppendable)));
      return;
    } catch (IOException iOException) {
      throw new RuntimeException(iOException);
    } 
  }
  
  public void toJson(Object paramObject, Appendable paramAppendable) throws JsonIOException {
    if (paramObject != null) {
      toJson(paramObject, paramObject.getClass(), paramAppendable);
      return;
    } 
    toJson(JsonNull.INSTANCE, paramAppendable);
  }
  
  public void toJson(Object paramObject, Type paramType, JsonWriter paramJsonWriter) throws JsonIOException {
    TypeAdapter<?> typeAdapter = getAdapter(TypeToken.get(paramType));
    boolean bool1 = paramJsonWriter.isLenient();
    paramJsonWriter.setLenient(true);
    boolean bool2 = paramJsonWriter.isHtmlSafe();
    paramJsonWriter.setHtmlSafe(this.htmlSafe);
    boolean bool3 = paramJsonWriter.getSerializeNulls();
    paramJsonWriter.setSerializeNulls(this.serializeNulls);
    try {
      typeAdapter.write(paramJsonWriter, paramObject);
      return;
    } catch (IOException iOException) {
      paramObject = new JsonIOException();
      super(iOException);
      throw paramObject;
    } finally {
      paramJsonWriter.setLenient(bool1);
      paramJsonWriter.setHtmlSafe(bool2);
      paramJsonWriter.setSerializeNulls(bool3);
    } 
  }
  
  public void toJson(Object paramObject, Type paramType, Appendable paramAppendable) throws JsonIOException {
    try {
      toJson(paramObject, paramType, newJsonWriter(Streams.writerForAppendable(paramAppendable)));
      return;
    } catch (IOException iOException) {
      throw new JsonIOException(iOException);
    } 
  }
  
  public JsonElement toJsonTree(Object paramObject) {
    return (paramObject == null) ? JsonNull.INSTANCE : toJsonTree(paramObject, paramObject.getClass());
  }
  
  public JsonElement toJsonTree(Object paramObject, Type paramType) {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    toJson(paramObject, paramType, (JsonWriter)jsonTreeWriter);
    return jsonTreeWriter.get();
  }
  
  public String toString() {
    return "{serializeNulls:" + this.serializeNulls + "factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
  }
  
  static class FutureTypeAdapter<T> extends TypeAdapter<T> {
    private TypeAdapter<T> delegate;
    
    public T read(JsonReader param1JsonReader) throws IOException {
      if (this.delegate == null)
        throw new IllegalStateException(); 
      return this.delegate.read(param1JsonReader);
    }
    
    public void setDelegate(TypeAdapter<T> param1TypeAdapter) {
      if (this.delegate != null)
        throw new AssertionError(); 
      this.delegate = param1TypeAdapter;
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      if (this.delegate == null)
        throw new IllegalStateException(); 
      this.delegate.write(param1JsonWriter, param1T);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/Gson.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */