package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class JsonTreeWriter extends JsonWriter {
  private static final JsonPrimitive SENTINEL_CLOSED;
  
  private static final Writer UNWRITABLE_WRITER = new Writer() {
      public void close() throws IOException {
        throw new AssertionError();
      }
      
      public void flush() throws IOException {
        throw new AssertionError();
      }
      
      public void write(char[] param1ArrayOfchar, int param1Int1, int param1Int2) {
        throw new AssertionError();
      }
    };
  
  private String pendingName;
  
  private JsonElement product = (JsonElement)JsonNull.INSTANCE;
  
  private final List<JsonElement> stack = new ArrayList<JsonElement>();
  
  static {
    SENTINEL_CLOSED = new JsonPrimitive("closed");
  }
  
  public JsonTreeWriter() {
    super(UNWRITABLE_WRITER);
  }
  
  private JsonElement peek() {
    return this.stack.get(this.stack.size() - 1);
  }
  
  private void put(JsonElement paramJsonElement) {
    if (this.pendingName != null) {
      if (!paramJsonElement.isJsonNull() || getSerializeNulls())
        ((JsonObject)peek()).add(this.pendingName, paramJsonElement); 
      this.pendingName = null;
      return;
    } 
    if (this.stack.isEmpty()) {
      this.product = paramJsonElement;
      return;
    } 
    JsonElement jsonElement = peek();
    if (jsonElement instanceof JsonArray) {
      ((JsonArray)jsonElement).add(paramJsonElement);
      return;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter beginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    put((JsonElement)jsonArray);
    this.stack.add(jsonArray);
    return this;
  }
  
  public JsonWriter beginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    put((JsonElement)jsonObject);
    this.stack.add(jsonObject);
    return this;
  }
  
  public void close() throws IOException {
    if (!this.stack.isEmpty())
      throw new IOException("Incomplete document"); 
    this.stack.add(SENTINEL_CLOSED);
  }
  
  public JsonWriter endArray() throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    if (peek() instanceof JsonArray) {
      this.stack.remove(this.stack.size() - 1);
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter endObject() throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    if (peek() instanceof JsonObject) {
      this.stack.remove(this.stack.size() - 1);
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public void flush() throws IOException {}
  
  public JsonElement get() {
    if (!this.stack.isEmpty())
      throw new IllegalStateException("Expected one JSON element but was " + this.stack); 
    return this.product;
  }
  
  public JsonWriter name(String paramString) throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    if (peek() instanceof JsonObject) {
      this.pendingName = paramString;
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter nullValue() throws IOException {
    put((JsonElement)JsonNull.INSTANCE);
    return this;
  }
  
  public JsonWriter value(double paramDouble) throws IOException {
    if (!isLenient() && (Double.isNaN(paramDouble) || Double.isInfinite(paramDouble)))
      throw new IllegalArgumentException("JSON forbids NaN and infinities: " + paramDouble); 
    put((JsonElement)new JsonPrimitive(Double.valueOf(paramDouble)));
    return this;
  }
  
  public JsonWriter value(long paramLong) throws IOException {
    put((JsonElement)new JsonPrimitive(Long.valueOf(paramLong)));
    return this;
  }
  
  public JsonWriter value(Number paramNumber) throws IOException {
    if (paramNumber == null)
      return nullValue(); 
    if (!isLenient()) {
      double d = paramNumber.doubleValue();
      if (Double.isNaN(d) || Double.isInfinite(d))
        throw new IllegalArgumentException("JSON forbids NaN and infinities: " + paramNumber); 
    } 
    put((JsonElement)new JsonPrimitive(paramNumber));
    return this;
  }
  
  public JsonWriter value(String paramString) throws IOException {
    if (paramString == null)
      return nullValue(); 
    put((JsonElement)new JsonPrimitive(paramString));
    return this;
  }
  
  public JsonWriter value(boolean paramBoolean) throws IOException {
    put((JsonElement)new JsonPrimitive(Boolean.valueOf(paramBoolean)));
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/JsonTreeWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */