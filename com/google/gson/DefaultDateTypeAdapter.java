package com.google.gson;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
  private final DateFormat enUsFormat;
  
  private final DateFormat iso8601Format;
  
  private final DateFormat localFormat;
  
  DefaultDateTypeAdapter() {
    this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
  }
  
  DefaultDateTypeAdapter(int paramInt) {
    this(DateFormat.getDateInstance(paramInt, Locale.US), DateFormat.getDateInstance(paramInt));
  }
  
  public DefaultDateTypeAdapter(int paramInt1, int paramInt2) {
    this(DateFormat.getDateTimeInstance(paramInt1, paramInt2, Locale.US), DateFormat.getDateTimeInstance(paramInt1, paramInt2));
  }
  
  DefaultDateTypeAdapter(String paramString) {
    this(new SimpleDateFormat(paramString, Locale.US), new SimpleDateFormat(paramString));
  }
  
  DefaultDateTypeAdapter(DateFormat paramDateFormat1, DateFormat paramDateFormat2) {
    this.enUsFormat = paramDateFormat1;
    this.localFormat = paramDateFormat2;
    this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  private Date deserializeToDate(JsonElement paramJsonElement) {
    synchronized (this.localFormat) {
      Date date = this.localFormat.parse(paramJsonElement.getAsString());
      null = date;
      return null;
    } 
  }
  
  public Date deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
    if (!(paramJsonElement instanceof JsonPrimitive))
      throw new JsonParseException("The date should be a string value"); 
    Date date = deserializeToDate(paramJsonElement);
    if (paramType != Date.class) {
      if (paramType == Timestamp.class)
        return new Timestamp(date.getTime()); 
      if (paramType == Date.class)
        return new Date(date.getTime()); 
      throw new IllegalArgumentException(getClass() + " cannot deserialize to " + paramType);
    } 
    return date;
  }
  
  public JsonElement serialize(Date paramDate, Type paramType, JsonSerializationContext paramJsonSerializationContext) {
    synchronized (this.localFormat) {
      String str = this.enUsFormat.format(paramDate);
      JsonPrimitive jsonPrimitive = new JsonPrimitive();
      this(str);
      return jsonPrimitive;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(DefaultDateTypeAdapter.class.getSimpleName());
    stringBuilder.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/DefaultDateTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */