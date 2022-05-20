package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
  public JsonElement parse(JsonReader paramJsonReader) throws JsonIOException, JsonSyntaxException {
    boolean bool = paramJsonReader.isLenient();
    paramJsonReader.setLenient(true);
    try {
      return Streams.parse(paramJsonReader);
    } catch (StackOverflowError stackOverflowError) {
      JsonParseException jsonParseException = new JsonParseException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("Failed parsing JSON source: ").append(paramJsonReader).append(" to Json").toString(), stackOverflowError);
      throw jsonParseException;
    } catch (OutOfMemoryError outOfMemoryError) {
      JsonParseException jsonParseException = new JsonParseException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("Failed parsing JSON source: ").append(paramJsonReader).append(" to Json").toString(), outOfMemoryError);
      throw jsonParseException;
    } finally {
      paramJsonReader.setLenient(bool);
    } 
  }
  
  public JsonElement parse(Reader paramReader) throws JsonIOException, JsonSyntaxException {
    try {
      JsonReader jsonReader = new JsonReader();
      this(paramReader);
      JsonElement jsonElement = parse(jsonReader);
      if (!jsonElement.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
        JsonSyntaxException jsonSyntaxException = new JsonSyntaxException();
        this("Did not consume the entire document.");
        throw jsonSyntaxException;
      } 
    } catch (MalformedJsonException malformedJsonException) {
      throw new JsonSyntaxException(malformedJsonException);
    } catch (IOException iOException) {
      throw new JsonIOException(iOException);
    } catch (NumberFormatException numberFormatException) {
      throw new JsonSyntaxException(numberFormatException);
    } 
    return (JsonElement)numberFormatException;
  }
  
  public JsonElement parse(String paramString) throws JsonSyntaxException {
    return parse(new StringReader(paramString));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/JsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */