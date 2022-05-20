package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
        return (param1TypeToken.getRawType() == Time.class) ? new TimeTypeAdapter() : null;
      }
    };
  
  private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");
  
  public Time read(JsonReader paramJsonReader) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
    //   6: getstatic com/google/gson/stream/JsonToken.NULL : Lcom/google/gson/stream/JsonToken;
    //   9: if_acmpne -> 22
    //   12: aload_1
    //   13: invokevirtual nextNull : ()V
    //   16: aconst_null
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: areturn
    //   22: new java/sql/Time
    //   25: dup
    //   26: aload_0
    //   27: getfield format : Ljava/text/DateFormat;
    //   30: aload_1
    //   31: invokevirtual nextString : ()Ljava/lang/String;
    //   34: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   37: invokevirtual getTime : ()J
    //   40: invokespecial <init> : (J)V
    //   43: astore_1
    //   44: goto -> 18
    //   47: astore_2
    //   48: new com/google/gson/JsonSyntaxException
    //   51: astore_1
    //   52: aload_1
    //   53: aload_2
    //   54: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   57: aload_1
    //   58: athrow
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	59	finally
    //   22	44	47	java/text/ParseException
    //   22	44	59	finally
    //   48	59	59	finally
  }
  
  public void write(JsonWriter paramJsonWriter, Time paramTime) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnonnull -> 17
    //   6: aconst_null
    //   7: astore_2
    //   8: aload_1
    //   9: aload_2
    //   10: invokevirtual value : (Ljava/lang/String;)Lcom/google/gson/stream/JsonWriter;
    //   13: pop
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: getfield format : Ljava/text/DateFormat;
    //   21: aload_2
    //   22: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   25: astore_2
    //   26: goto -> 8
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   8	14	29	finally
    //   17	26	29	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/TimeTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */