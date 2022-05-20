package com.google.gson;

import com.google.gson.internal.;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class TreeTypeAdapter<T> extends TypeAdapter<T> {
  private TypeAdapter<T> delegate;
  
  private final JsonDeserializer<T> deserializer;
  
  private final Gson gson;
  
  private final JsonSerializer<T> serializer;
  
  private final TypeAdapterFactory skipPast;
  
  private final TypeToken<T> typeToken;
  
  private TreeTypeAdapter(JsonSerializer<T> paramJsonSerializer, JsonDeserializer<T> paramJsonDeserializer, Gson paramGson, TypeToken<T> paramTypeToken, TypeAdapterFactory paramTypeAdapterFactory) {
    this.serializer = paramJsonSerializer;
    this.deserializer = paramJsonDeserializer;
    this.gson = paramGson;
    this.typeToken = paramTypeToken;
    this.skipPast = paramTypeAdapterFactory;
  }
  
  private TypeAdapter<T> delegate() {
    TypeAdapter<T> typeAdapter = this.delegate;
    if (typeAdapter == null) {
      typeAdapter = this.gson.getDelegateAdapter(this.skipPast, this.typeToken);
      this.delegate = typeAdapter;
    } 
    return typeAdapter;
  }
  
  public static TypeAdapterFactory newFactory(TypeToken<?> paramTypeToken, Object paramObject) {
    return new SingleTypeFactory(paramObject, paramTypeToken, false, null);
  }
  
  public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken<?> paramTypeToken, Object paramObject) {
    if (paramTypeToken.getType() == paramTypeToken.getRawType()) {
      boolean bool1 = true;
      return new SingleTypeFactory(paramObject, paramTypeToken, bool1, null);
    } 
    boolean bool = false;
    return new SingleTypeFactory(paramObject, paramTypeToken, bool, null);
  }
  
  public static TypeAdapterFactory newTypeHierarchyFactory(Class<?> paramClass, Object paramObject) {
    return new SingleTypeFactory(paramObject, null, false, paramClass);
  }
  
  public T read(JsonReader paramJsonReader) throws IOException {
    if (this.deserializer == null)
      return delegate().read(paramJsonReader); 
    null = Streams.parse(paramJsonReader);
    return null.isJsonNull() ? null : this.deserializer.deserialize(null, this.typeToken.getType(), this.gson.deserializationContext);
  }
  
  public void write(JsonWriter paramJsonWriter, T paramT) throws IOException {
    if (this.serializer == null) {
      delegate().write(paramJsonWriter, paramT);
      return;
    } 
    if (paramT == null) {
      paramJsonWriter.nullValue();
      return;
    } 
    Streams.write(this.serializer.serialize(paramT, this.typeToken.getType(), this.gson.serializationContext), paramJsonWriter);
  }
  
  private static class SingleTypeFactory implements TypeAdapterFactory {
    private final JsonDeserializer<?> deserializer;
    
    private final TypeToken<?> exactType;
    
    private final Class<?> hierarchyType;
    
    private final boolean matchRawType;
    
    private final JsonSerializer<?> serializer;
    
    private SingleTypeFactory(Object param1Object, TypeToken<?> param1TypeToken, boolean param1Boolean, Class<?> param1Class) {
      JsonSerializer jsonSerializer;
      boolean bool;
      if (param1Object instanceof JsonSerializer) {
        jsonSerializer = (JsonSerializer)param1Object;
      } else {
        jsonSerializer = null;
      } 
      this.serializer = jsonSerializer;
      if (param1Object instanceof JsonDeserializer) {
        param1Object = param1Object;
      } else {
        param1Object = null;
      } 
      this.deserializer = (JsonDeserializer<?>)param1Object;
      if (this.serializer != null || this.deserializer != null) {
        bool = true;
      } else {
        bool = false;
      } 
      .Gson.Preconditions.checkArgument(bool);
      this.exactType = param1TypeToken;
      this.matchRawType = param1Boolean;
      this.hierarchyType = param1Class;
    }
    
    public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
      boolean bool;
      if (this.exactType != null) {
        if (this.exactType.equals(param1TypeToken) || (this.matchRawType && this.exactType.getType() == param1TypeToken.getRawType())) {
          bool = true;
        } else {
          bool = false;
        } 
      } else {
        bool = this.hierarchyType.isAssignableFrom(param1TypeToken.getRawType());
      } 
      return bool ? new TreeTypeAdapter(this.serializer, this.deserializer, param1Gson, param1TypeToken, this) : null;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/TreeTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */