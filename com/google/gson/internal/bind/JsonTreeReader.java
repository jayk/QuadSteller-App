package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class JsonTreeReader extends JsonReader {
  private static final Object SENTINEL_CLOSED;
  
  private static final Reader UNREADABLE_READER = new Reader() {
      public void close() throws IOException {
        throw new AssertionError();
      }
      
      public int read(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws IOException {
        throw new AssertionError();
      }
    };
  
  private final List<Object> stack = new ArrayList();
  
  static {
    SENTINEL_CLOSED = new Object();
  }
  
  public JsonTreeReader(JsonElement paramJsonElement) {
    super(UNREADABLE_READER);
    this.stack.add(paramJsonElement);
  }
  
  private void expect(JsonToken paramJsonToken) throws IOException {
    if (peek() != paramJsonToken)
      throw new IllegalStateException("Expected " + paramJsonToken + " but was " + peek()); 
  }
  
  private Object peekStack() {
    return this.stack.get(this.stack.size() - 1);
  }
  
  private Object popStack() {
    return this.stack.remove(this.stack.size() - 1);
  }
  
  public void beginArray() throws IOException {
    expect(JsonToken.BEGIN_ARRAY);
    JsonArray jsonArray = (JsonArray)peekStack();
    this.stack.add(jsonArray.iterator());
  }
  
  public void beginObject() throws IOException {
    expect(JsonToken.BEGIN_OBJECT);
    JsonObject jsonObject = (JsonObject)peekStack();
    this.stack.add(jsonObject.entrySet().iterator());
  }
  
  public void close() throws IOException {
    this.stack.clear();
    this.stack.add(SENTINEL_CLOSED);
  }
  
  public void endArray() throws IOException {
    expect(JsonToken.END_ARRAY);
    popStack();
    popStack();
  }
  
  public void endObject() throws IOException {
    expect(JsonToken.END_OBJECT);
    popStack();
    popStack();
  }
  
  public boolean hasNext() throws IOException {
    JsonToken jsonToken = peek();
    return (jsonToken != JsonToken.END_OBJECT && jsonToken != JsonToken.END_ARRAY);
  }
  
  public boolean nextBoolean() throws IOException {
    expect(JsonToken.BOOLEAN);
    return ((JsonPrimitive)popStack()).getAsBoolean();
  }
  
  public double nextDouble() throws IOException {
    JsonToken jsonToken = peek();
    if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + jsonToken); 
    double d = ((JsonPrimitive)peekStack()).getAsDouble();
    if (!isLenient() && (Double.isNaN(d) || Double.isInfinite(d)))
      throw new NumberFormatException("JSON forbids NaN and infinities: " + d); 
    popStack();
    return d;
  }
  
  public int nextInt() throws IOException {
    JsonToken jsonToken = peek();
    if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + jsonToken); 
    int i = ((JsonPrimitive)peekStack()).getAsInt();
    popStack();
    return i;
  }
  
  public long nextLong() throws IOException {
    JsonToken jsonToken = peek();
    if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + jsonToken); 
    long l = ((JsonPrimitive)peekStack()).getAsLong();
    popStack();
    return l;
  }
  
  public String nextName() throws IOException {
    expect(JsonToken.NAME);
    Map.Entry entry = ((Iterator<Map.Entry>)peekStack()).next();
    this.stack.add(entry.getValue());
    return (String)entry.getKey();
  }
  
  public void nextNull() throws IOException {
    expect(JsonToken.NULL);
    popStack();
  }
  
  public String nextString() throws IOException {
    JsonToken jsonToken = peek();
    if (jsonToken != JsonToken.STRING && jsonToken != JsonToken.NUMBER)
      throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + jsonToken); 
    return ((JsonPrimitive)popStack()).getAsString();
  }
  
  public JsonToken peek() throws IOException {
    if (this.stack.isEmpty())
      return JsonToken.END_DOCUMENT; 
    Object object = peekStack();
    if (object instanceof Iterator) {
      boolean bool = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
      object = object;
      if (object.hasNext()) {
        if (bool)
          return JsonToken.NAME; 
        this.stack.add(object.next());
        return peek();
      } 
      return bool ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
    } 
    if (object instanceof JsonObject)
      return JsonToken.BEGIN_OBJECT; 
    if (object instanceof JsonArray)
      return JsonToken.BEGIN_ARRAY; 
    if (object instanceof JsonPrimitive) {
      object = object;
      if (object.isString())
        return JsonToken.STRING; 
      if (object.isBoolean())
        return JsonToken.BOOLEAN; 
      if (object.isNumber())
        return JsonToken.NUMBER; 
      throw new AssertionError();
    } 
    if (object instanceof com.google.gson.JsonNull)
      return JsonToken.NULL; 
    if (object == SENTINEL_CLOSED)
      throw new IllegalStateException("JsonReader is closed"); 
    throw new AssertionError();
  }
  
  public void promoteNameToValue() throws IOException {
    expect(JsonToken.NAME);
    Map.Entry entry = ((Iterator<Map.Entry>)peekStack()).next();
    this.stack.add(entry.getValue());
    this.stack.add(new JsonPrimitive((String)entry.getKey()));
  }
  
  public void skipValue() throws IOException {
    if (peek() == JsonToken.NAME) {
      nextName();
      return;
    } 
    popStack();
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/JsonTreeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */