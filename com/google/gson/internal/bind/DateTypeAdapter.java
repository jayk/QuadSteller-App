package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTypeAdapter extends TypeAdapter<Date> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
        return (param1TypeToken.getRawType() == Date.class) ? new DateTypeAdapter() : null;
      }
    };
  
  private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
  
  private final DateFormat iso8601Format = buildIso8601Format();
  
  private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);
  
  private static DateFormat buildIso8601Format() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return simpleDateFormat;
  }
  
  private Date deserializeToDate(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield localFormat : Ljava/text/DateFormat;
    //   6: aload_1
    //   7: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   10: astore_2
    //   11: aload_2
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: astore_2
    //   18: aload_0
    //   19: getfield enUsFormat : Ljava/text/DateFormat;
    //   22: aload_1
    //   23: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   26: astore_2
    //   27: aload_2
    //   28: astore_1
    //   29: goto -> 13
    //   32: astore_2
    //   33: aload_0
    //   34: getfield iso8601Format : Ljava/text/DateFormat;
    //   37: aload_1
    //   38: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   41: astore_2
    //   42: aload_2
    //   43: astore_1
    //   44: goto -> 13
    //   47: astore_3
    //   48: new com/google/gson/JsonSyntaxException
    //   51: astore_2
    //   52: aload_2
    //   53: aload_1
    //   54: aload_3
    //   55: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   58: aload_2
    //   59: athrow
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	17	java/text/ParseException
    //   2	11	60	finally
    //   18	27	32	java/text/ParseException
    //   18	27	60	finally
    //   33	42	47	java/text/ParseException
    //   33	42	60	finally
    //   48	60	60	finally
  }
  
  public Date read(JsonReader paramJsonReader) throws IOException {
    if (paramJsonReader.peek() == JsonToken.NULL) {
      paramJsonReader.nextNull();
      return null;
    } 
    return deserializeToDate(paramJsonReader.nextString());
  }
  
  public void write(JsonWriter paramJsonWriter, Date paramDate) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnonnull -> 14
    //   6: aload_1
    //   7: invokevirtual nullValue : ()Lcom/google/gson/stream/JsonWriter;
    //   10: pop
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_1
    //   15: aload_0
    //   16: getfield enUsFormat : Ljava/text/DateFormat;
    //   19: aload_2
    //   20: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   23: invokevirtual value : (Ljava/lang/String;)Lcom/google/gson/stream/JsonWriter;
    //   26: pop
    //   27: goto -> 11
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   6	11	30	finally
    //   14	27	30	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/DateTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */