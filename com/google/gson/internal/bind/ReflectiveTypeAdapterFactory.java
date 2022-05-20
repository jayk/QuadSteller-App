package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
  private final ConstructorConstructor constructorConstructor;
  
  private final Excluder excluder;
  
  private final FieldNamingStrategy fieldNamingPolicy;
  
  public ReflectiveTypeAdapterFactory(ConstructorConstructor paramConstructorConstructor, FieldNamingStrategy paramFieldNamingStrategy, Excluder paramExcluder) {
    this.constructorConstructor = paramConstructorConstructor;
    this.fieldNamingPolicy = paramFieldNamingStrategy;
    this.excluder = paramExcluder;
  }
  
  private BoundField createBoundField(final Gson context, final Field field, String paramString, final TypeToken<?> fieldType, boolean paramBoolean1, boolean paramBoolean2) {
    return new BoundField(paramString, paramBoolean1, paramBoolean2) {
        final TypeAdapter<?> typeAdapter = context.getAdapter(fieldType);
        
        void read(JsonReader param1JsonReader, Object param1Object) throws IOException, IllegalAccessException {
          Object object = this.typeAdapter.read(param1JsonReader);
          if (object != null || !isPrimitive)
            field.set(param1Object, object); 
        }
        
        void write(JsonWriter param1JsonWriter, Object param1Object) throws IOException, IllegalAccessException {
          param1Object = field.get(param1Object);
          (new TypeAdapterRuntimeTypeWrapper(context, this.typeAdapter, fieldType.getType())).write(param1JsonWriter, param1Object);
        }
      };
  }
  
  private Map<String, BoundField> getBoundFields(Gson paramGson, TypeToken<?> paramTypeToken, Class<?> paramClass) {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    if (!paramClass.isInterface()) {
      Type type = paramTypeToken.getType();
      while (true) {
        if (paramClass != Object.class) {
          for (Field field : paramClass.getDeclaredFields()) {
            boolean bool1 = excludeField(field, true);
            boolean bool2 = excludeField(field, false);
            if (bool1 || bool2) {
              field.setAccessible(true);
              Type type1 = .Gson.Types.resolve(paramTypeToken.getType(), paramClass, field.getGenericType());
              BoundField boundField = createBoundField(paramGson, field, getFieldName(field), TypeToken.get(type1), bool1, bool2);
              boundField = (BoundField)linkedHashMap.put(boundField.name, boundField);
              if (boundField != null)
                throw new IllegalArgumentException(type + " declares multiple JSON fields named " + boundField.name); 
            } 
          } 
          paramTypeToken = TypeToken.get(.Gson.Types.resolve(paramTypeToken.getType(), paramClass, paramClass.getGenericSuperclass()));
          paramClass = paramTypeToken.getRawType();
          continue;
        } 
        return (Map)linkedHashMap;
      } 
    } 
    return (Map)linkedHashMap;
  }
  
  private String getFieldName(Field paramField) {
    SerializedName serializedName = paramField.<SerializedName>getAnnotation(SerializedName.class);
    return (serializedName == null) ? this.fieldNamingPolicy.translateName(paramField) : serializedName.value();
  }
  
  public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
    Gson gson = null;
    Class<?> clazz = paramTypeToken.getRawType();
    return (TypeAdapter<T>)(!Object.class.isAssignableFrom(clazz) ? gson : new Adapter(this.constructorConstructor.get(paramTypeToken), getBoundFields(paramGson, paramTypeToken, clazz)));
  }
  
  public boolean excludeField(Field paramField, boolean paramBoolean) {
    return (!this.excluder.excludeClass(paramField.getType(), paramBoolean) && !this.excluder.excludeField(paramField, paramBoolean));
  }
  
  public static final class Adapter<T> extends TypeAdapter<T> {
    private final Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;
    
    private final ObjectConstructor<T> constructor;
    
    private Adapter(ObjectConstructor<T> param1ObjectConstructor, Map<String, ReflectiveTypeAdapterFactory.BoundField> param1Map) {
      this.constructor = param1ObjectConstructor;
      this.boundFields = param1Map;
    }
    
    public T read(JsonReader param1JsonReader) throws IOException {
      if (param1JsonReader.peek() == JsonToken.NULL) {
        param1JsonReader.nextNull();
        return null;
      } 
      Object object = this.constructor.construct();
      try {
        param1JsonReader.beginObject();
        while (param1JsonReader.hasNext()) {
          String str = param1JsonReader.nextName();
          ReflectiveTypeAdapterFactory.BoundField boundField = this.boundFields.get(str);
          if (boundField == null || !boundField.deserialized) {
            param1JsonReader.skipValue();
            continue;
          } 
          boundField.read(param1JsonReader, object);
        } 
      } catch (IllegalStateException illegalStateException) {
        throw new JsonSyntaxException(illegalStateException);
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError(illegalAccessException);
      } 
      illegalAccessException.endObject();
      return (T)object;
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      if (param1T == null) {
        param1JsonWriter.nullValue();
        return;
      } 
      param1JsonWriter.beginObject();
      try {
        for (ReflectiveTypeAdapterFactory.BoundField boundField : this.boundFields.values()) {
          if (boundField.serialized) {
            param1JsonWriter.name(boundField.name);
            boundField.write(param1JsonWriter, param1T);
          } 
        } 
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError();
      } 
      illegalAccessException.endObject();
    }
  }
  
  static abstract class BoundField {
    final boolean deserialized;
    
    final String name;
    
    final boolean serialized;
    
    protected BoundField(String param1String, boolean param1Boolean1, boolean param1Boolean2) {
      this.name = param1String;
      this.serialized = param1Boolean1;
      this.deserialized = param1Boolean2;
    }
    
    abstract void read(JsonReader param1JsonReader, Object param1Object) throws IOException, IllegalAccessException;
    
    abstract void write(JsonWriter param1JsonWriter, Object param1Object) throws IOException, IllegalAccessException;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/ReflectiveTypeAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */