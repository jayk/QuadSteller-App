package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext {
  <T> T deserialize(JsonElement paramJsonElement, Type paramType) throws JsonParseException;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/JsonDeserializationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */