package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

public final class TypeAdapters {
  public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
  
  public static final TypeAdapter<BigInteger> BIG_INTEGER;
  
  public static final TypeAdapter<BitSet> BIT_SET;
  
  public static final TypeAdapterFactory BIT_SET_FACTORY;
  
  public static final TypeAdapter<Boolean> BOOLEAN;
  
  public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
  
  public static final TypeAdapterFactory BOOLEAN_FACTORY;
  
  public static final TypeAdapter<Number> BYTE;
  
  public static final TypeAdapterFactory BYTE_FACTORY;
  
  public static final TypeAdapter<Calendar> CALENDAR;
  
  public static final TypeAdapterFactory CALENDAR_FACTORY;
  
  public static final TypeAdapter<Character> CHARACTER;
  
  public static final TypeAdapterFactory CHARACTER_FACTORY;
  
  public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>() {
      public Class read(JsonReader param1JsonReader) throws IOException {
        if (param1JsonReader.peek() == JsonToken.NULL) {
          param1JsonReader.nextNull();
          return null;
        } 
        throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
      }
      
      public void write(JsonWriter param1JsonWriter, Class param1Class) throws IOException {
        if (param1Class == null) {
          param1JsonWriter.nullValue();
          return;
        } 
        throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + param1Class.getName() + ". Forgot to register a type adapter?");
      }
    };
  
  public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
  
  public static final TypeAdapter<Number> DOUBLE;
  
  public static final TypeAdapterFactory ENUM_FACTORY;
  
  public static final TypeAdapter<Number> FLOAT;
  
  public static final TypeAdapter<InetAddress> INET_ADDRESS;
  
  public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
  
  public static final TypeAdapter<Number> INTEGER;
  
  public static final TypeAdapterFactory INTEGER_FACTORY;
  
  public static final TypeAdapter<JsonElement> JSON_ELEMENT;
  
  public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
  
  public static final TypeAdapter<Locale> LOCALE;
  
  public static final TypeAdapterFactory LOCALE_FACTORY;
  
  public static final TypeAdapter<Number> LONG;
  
  public static final TypeAdapter<Number> NUMBER;
  
  public static final TypeAdapterFactory NUMBER_FACTORY;
  
  public static final TypeAdapter<Number> SHORT;
  
  public static final TypeAdapterFactory SHORT_FACTORY;
  
  public static final TypeAdapter<String> STRING;
  
  public static final TypeAdapter<StringBuffer> STRING_BUFFER;
  
  public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
  
  public static final TypeAdapter<StringBuilder> STRING_BUILDER;
  
  public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
  
  public static final TypeAdapterFactory STRING_FACTORY;
  
  public static final TypeAdapterFactory TIMESTAMP_FACTORY;
  
  public static final TypeAdapter<URI> URI;
  
  public static final TypeAdapterFactory URI_FACTORY;
  
  public static final TypeAdapter<URL> URL;
  
  public static final TypeAdapterFactory URL_FACTORY;
  
  public static final TypeAdapter<UUID> UUID;
  
  public static final TypeAdapterFactory UUID_FACTORY;
  
  static {
    BIT_SET = new TypeAdapter<BitSet>() {
        public BitSet read(JsonReader param1JsonReader) throws IOException {
          // Byte code:
          //   0: aload_1
          //   1: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
          //   4: getstatic com/google/gson/stream/JsonToken.NULL : Lcom/google/gson/stream/JsonToken;
          //   7: if_acmpne -> 18
          //   10: aload_1
          //   11: invokevirtual nextNull : ()V
          //   14: aconst_null
          //   15: astore_1
          //   16: aload_1
          //   17: areturn
          //   18: new java/util/BitSet
          //   21: dup
          //   22: invokespecial <init> : ()V
          //   25: astore_2
          //   26: aload_1
          //   27: invokevirtual beginArray : ()V
          //   30: iconst_0
          //   31: istore_3
          //   32: aload_1
          //   33: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
          //   36: astore #4
          //   38: aload #4
          //   40: getstatic com/google/gson/stream/JsonToken.END_ARRAY : Lcom/google/gson/stream/JsonToken;
          //   43: if_acmpeq -> 214
          //   46: getstatic com/google/gson/internal/bind/TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken : [I
          //   49: aload #4
          //   51: invokevirtual ordinal : ()I
          //   54: iaload
          //   55: tableswitch default -> 80, 1 -> 108, 2 -> 146, 3 -> 155
          //   80: new com/google/gson/JsonSyntaxException
          //   83: dup
          //   84: new java/lang/StringBuilder
          //   87: dup
          //   88: invokespecial <init> : ()V
          //   91: ldc 'Invalid bitset value type: '
          //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   96: aload #4
          //   98: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   101: invokevirtual toString : ()Ljava/lang/String;
          //   104: invokespecial <init> : (Ljava/lang/String;)V
          //   107: athrow
          //   108: aload_1
          //   109: invokevirtual nextInt : ()I
          //   112: ifeq -> 140
          //   115: iconst_1
          //   116: istore #5
          //   118: iload #5
          //   120: ifeq -> 128
          //   123: aload_2
          //   124: iload_3
          //   125: invokevirtual set : (I)V
          //   128: iinc #3, 1
          //   131: aload_1
          //   132: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
          //   135: astore #4
          //   137: goto -> 38
          //   140: iconst_0
          //   141: istore #5
          //   143: goto -> 118
          //   146: aload_1
          //   147: invokevirtual nextBoolean : ()Z
          //   150: istore #5
          //   152: goto -> 118
          //   155: aload_1
          //   156: invokevirtual nextString : ()Ljava/lang/String;
          //   159: astore #4
          //   161: aload #4
          //   163: invokestatic parseInt : (Ljava/lang/String;)I
          //   166: istore #6
          //   168: iload #6
          //   170: ifeq -> 179
          //   173: iconst_1
          //   174: istore #5
          //   176: goto -> 118
          //   179: iconst_0
          //   180: istore #5
          //   182: goto -> 176
          //   185: astore_1
          //   186: new com/google/gson/JsonSyntaxException
          //   189: dup
          //   190: new java/lang/StringBuilder
          //   193: dup
          //   194: invokespecial <init> : ()V
          //   197: ldc 'Error: Expecting: bitset number value (1, 0), Found: '
          //   199: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   202: aload #4
          //   204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   207: invokevirtual toString : ()Ljava/lang/String;
          //   210: invokespecial <init> : (Ljava/lang/String;)V
          //   213: athrow
          //   214: aload_1
          //   215: invokevirtual endArray : ()V
          //   218: aload_2
          //   219: astore_1
          //   220: goto -> 16
          // Exception table:
          //   from	to	target	type
          //   161	168	185	java/lang/NumberFormatException
        }
        
        public void write(JsonWriter param1JsonWriter, BitSet param1BitSet) throws IOException {
          if (param1BitSet == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          param1JsonWriter.beginArray();
          for (byte b = 0; b < param1BitSet.length(); b++) {
            boolean bool;
            if (param1BitSet.get(b)) {
              bool = true;
            } else {
              bool = false;
            } 
            param1JsonWriter.value(bool);
          } 
          param1JsonWriter.endArray();
        }
      };
    BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
    BOOLEAN = new TypeAdapter<Boolean>() {
        public Boolean read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return (param1JsonReader.peek() == JsonToken.STRING) ? Boolean.valueOf(Boolean.parseBoolean(param1JsonReader.nextString())) : Boolean.valueOf(param1JsonReader.nextBoolean());
        }
        
        public void write(JsonWriter param1JsonWriter, Boolean param1Boolean) throws IOException {
          if (param1Boolean == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          param1JsonWriter.value(param1Boolean.booleanValue());
        }
      };
    BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
        public Boolean read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Boolean.valueOf(param1JsonReader.nextString());
        }
        
        public void write(JsonWriter param1JsonWriter, Boolean param1Boolean) throws IOException {
          String str;
          if (param1Boolean == null) {
            str = "null";
          } else {
            str = str.toString();
          } 
          param1JsonWriter.value(str);
        }
      };
    BOOLEAN_FACTORY = newFactory(boolean.class, (Class)Boolean.class, (TypeAdapter)BOOLEAN);
    BYTE = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            byte b = (byte)param1JsonReader.nextInt();
            return Byte.valueOf(b);
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    BYTE_FACTORY = newFactory(byte.class, (Class)Byte.class, (TypeAdapter)BYTE);
    SHORT = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            short s = (short)param1JsonReader.nextInt();
            return Short.valueOf(s);
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    SHORT_FACTORY = newFactory(short.class, (Class)Short.class, (TypeAdapter)SHORT);
    INTEGER = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            int i = param1JsonReader.nextInt();
            return Integer.valueOf(i);
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    INTEGER_FACTORY = newFactory(int.class, (Class)Integer.class, (TypeAdapter)INTEGER);
    LONG = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            long l = param1JsonReader.nextLong();
            return Long.valueOf(l);
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    FLOAT = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Float.valueOf((float)param1JsonReader.nextDouble());
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    DOUBLE = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return Double.valueOf(param1JsonReader.nextDouble());
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    NUMBER = new TypeAdapter<Number>() {
        public Number read(JsonReader param1JsonReader) throws IOException {
          // Byte code:
          //   0: aload_1
          //   1: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
          //   4: astore_2
          //   5: getstatic com/google/gson/internal/bind/TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken : [I
          //   8: aload_2
          //   9: invokevirtual ordinal : ()I
          //   12: iaload
          //   13: tableswitch default -> 44, 1 -> 79, 2 -> 44, 3 -> 44, 4 -> 71
          //   44: new com/google/gson/JsonSyntaxException
          //   47: dup
          //   48: new java/lang/StringBuilder
          //   51: dup
          //   52: invokespecial <init> : ()V
          //   55: ldc 'Expecting number, got: '
          //   57: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   60: aload_2
          //   61: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   64: invokevirtual toString : ()Ljava/lang/String;
          //   67: invokespecial <init> : (Ljava/lang/String;)V
          //   70: athrow
          //   71: aload_1
          //   72: invokevirtual nextNull : ()V
          //   75: aconst_null
          //   76: astore_1
          //   77: aload_1
          //   78: areturn
          //   79: new com/google/gson/internal/LazilyParsedNumber
          //   82: dup
          //   83: aload_1
          //   84: invokevirtual nextString : ()Ljava/lang/String;
          //   87: invokespecial <init> : (Ljava/lang/String;)V
          //   90: astore_1
          //   91: goto -> 77
        }
        
        public void write(JsonWriter param1JsonWriter, Number param1Number) throws IOException {
          param1JsonWriter.value(param1Number);
        }
      };
    NUMBER_FACTORY = newFactory(Number.class, NUMBER);
    CHARACTER = new TypeAdapter<Character>() {
        public Character read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          String str = param1JsonReader.nextString();
          if (str.length() != 1)
            throw new JsonSyntaxException("Expecting character, got: " + str); 
          return Character.valueOf(str.charAt(0));
        }
        
        public void write(JsonWriter param1JsonWriter, Character param1Character) throws IOException {
          String str;
          if (param1Character == null) {
            param1Character = null;
          } else {
            str = String.valueOf(param1Character);
          } 
          param1JsonWriter.value(str);
        }
      };
    CHARACTER_FACTORY = newFactory(char.class, (Class)Character.class, (TypeAdapter)CHARACTER);
    STRING = new TypeAdapter<String>() {
        public String read(JsonReader param1JsonReader) throws IOException {
          JsonToken jsonToken = param1JsonReader.peek();
          if (jsonToken == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return (jsonToken == JsonToken.BOOLEAN) ? Boolean.toString(param1JsonReader.nextBoolean()) : param1JsonReader.nextString();
        }
        
        public void write(JsonWriter param1JsonWriter, String param1String) throws IOException {
          param1JsonWriter.value(param1String);
        }
      };
    BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
        public BigDecimal read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            return new BigDecimal(param1JsonReader.nextString());
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, BigDecimal param1BigDecimal) throws IOException {
          param1JsonWriter.value(param1BigDecimal);
        }
      };
    BIG_INTEGER = new TypeAdapter<BigInteger>() {
        public BigInteger read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          try {
            return new BigInteger(param1JsonReader.nextString());
          } catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, BigInteger param1BigInteger) throws IOException {
          param1JsonWriter.value(param1BigInteger);
        }
      };
    STRING_FACTORY = newFactory(String.class, STRING);
    STRING_BUILDER = new TypeAdapter<StringBuilder>() {
        public StringBuilder read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return new StringBuilder(param1JsonReader.nextString());
        }
        
        public void write(JsonWriter param1JsonWriter, StringBuilder param1StringBuilder) throws IOException {
          String str;
          if (param1StringBuilder == null) {
            param1StringBuilder = null;
          } else {
            str = param1StringBuilder.toString();
          } 
          param1JsonWriter.value(str);
        }
      };
    STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
    STRING_BUFFER = new TypeAdapter<StringBuffer>() {
        public StringBuffer read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return new StringBuffer(param1JsonReader.nextString());
        }
        
        public void write(JsonWriter param1JsonWriter, StringBuffer param1StringBuffer) throws IOException {
          String str;
          if (param1StringBuffer == null) {
            param1StringBuffer = null;
          } else {
            str = param1StringBuffer.toString();
          } 
          param1JsonWriter.value(str);
        }
      };
    STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
    URL = new TypeAdapter<URL>() {
        public URL read(JsonReader param1JsonReader) throws IOException {
          URL uRL;
          JsonReader jsonReader = null;
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return (URL)jsonReader;
          } 
          String str = param1JsonReader.nextString();
          param1JsonReader = jsonReader;
          if (!"null".equals(str))
            uRL = new URL(str); 
          return uRL;
        }
        
        public void write(JsonWriter param1JsonWriter, URL param1URL) throws IOException {
          String str;
          if (param1URL == null) {
            param1URL = null;
          } else {
            str = param1URL.toExternalForm();
          } 
          param1JsonWriter.value(str);
        }
      };
    URL_FACTORY = newFactory(URL.class, URL);
    URI = new TypeAdapter<URI>() {
        public URI read(JsonReader param1JsonReader) throws IOException {
          JsonReader jsonReader = null;
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return (URI)jsonReader;
          } 
          try {
            URI uRI;
            String str = param1JsonReader.nextString();
            param1JsonReader = jsonReader;
            if (!"null".equals(str))
              uRI = new URI(str); 
            return uRI;
          } catch (URISyntaxException uRISyntaxException) {
            throw new JsonIOException(uRISyntaxException);
          } 
        }
        
        public void write(JsonWriter param1JsonWriter, URI param1URI) throws IOException {
          String str;
          if (param1URI == null) {
            param1URI = null;
          } else {
            str = param1URI.toASCIIString();
          } 
          param1JsonWriter.value(str);
        }
      };
    URI_FACTORY = newFactory(URI.class, URI);
    INET_ADDRESS = new TypeAdapter<InetAddress>() {
        public InetAddress read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return InetAddress.getByName(param1JsonReader.nextString());
        }
        
        public void write(JsonWriter param1JsonWriter, InetAddress param1InetAddress) throws IOException {
          String str;
          if (param1InetAddress == null) {
            param1InetAddress = null;
          } else {
            str = param1InetAddress.getHostAddress();
          } 
          param1JsonWriter.value(str);
        }
      };
    INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
    UUID = new TypeAdapter<UUID>() {
        public UUID read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          return UUID.fromString(param1JsonReader.nextString());
        }
        
        public void write(JsonWriter param1JsonWriter, UUID param1UUID) throws IOException {
          String str;
          if (param1UUID == null) {
            param1UUID = null;
          } else {
            str = param1UUID.toString();
          } 
          param1JsonWriter.value(str);
        }
      };
    UUID_FACTORY = newFactory(UUID.class, UUID);
    TIMESTAMP_FACTORY = new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          return (TypeAdapter)((param1TypeToken.getRawType() != Timestamp.class) ? null : new TypeAdapter<Timestamp>() {
              public Timestamp read(JsonReader param2JsonReader) throws IOException {
                null = (Date)dateTypeAdapter.read(param2JsonReader);
                return (null != null) ? new Timestamp(null.getTime()) : null;
              }
              
              public void write(JsonWriter param2JsonWriter, Timestamp param2Timestamp) throws IOException {
                dateTypeAdapter.write(param2JsonWriter, param2Timestamp);
              }
            });
        }
      };
    CALENDAR = new TypeAdapter<Calendar>() {
        private static final String DAY_OF_MONTH = "dayOfMonth";
        
        private static final String HOUR_OF_DAY = "hourOfDay";
        
        private static final String MINUTE = "minute";
        
        private static final String MONTH = "month";
        
        private static final String SECOND = "second";
        
        private static final String YEAR = "year";
        
        public Calendar read(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          param1JsonReader.beginObject();
          int i = 0;
          int j = 0;
          int k = 0;
          int m = 0;
          int n = 0;
          int i1 = 0;
          while (param1JsonReader.peek() != JsonToken.END_OBJECT) {
            String str = param1JsonReader.nextName();
            int i2 = param1JsonReader.nextInt();
            if ("year".equals(str)) {
              i = i2;
              continue;
            } 
            if ("month".equals(str)) {
              j = i2;
              continue;
            } 
            if ("dayOfMonth".equals(str)) {
              k = i2;
              continue;
            } 
            if ("hourOfDay".equals(str)) {
              m = i2;
              continue;
            } 
            if ("minute".equals(str)) {
              n = i2;
              continue;
            } 
            if ("second".equals(str))
              i1 = i2; 
          } 
          param1JsonReader.endObject();
          return new GregorianCalendar(i, j, k, m, n, i1);
        }
        
        public void write(JsonWriter param1JsonWriter, Calendar param1Calendar) throws IOException {
          if (param1Calendar == null) {
            param1JsonWriter.nullValue();
            return;
          } 
          param1JsonWriter.beginObject();
          param1JsonWriter.name("year");
          param1JsonWriter.value(param1Calendar.get(1));
          param1JsonWriter.name("month");
          param1JsonWriter.value(param1Calendar.get(2));
          param1JsonWriter.name("dayOfMonth");
          param1JsonWriter.value(param1Calendar.get(5));
          param1JsonWriter.name("hourOfDay");
          param1JsonWriter.value(param1Calendar.get(11));
          param1JsonWriter.name("minute");
          param1JsonWriter.value(param1Calendar.get(12));
          param1JsonWriter.name("second");
          param1JsonWriter.value(param1Calendar.get(13));
          param1JsonWriter.endObject();
        }
      };
    CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, (Class)GregorianCalendar.class, CALENDAR);
    LOCALE = new TypeAdapter<Locale>() {
        public Locale read(JsonReader param1JsonReader) throws IOException {
          String str1;
          if (param1JsonReader.peek() == JsonToken.NULL) {
            param1JsonReader.nextNull();
            return null;
          } 
          StringTokenizer stringTokenizer = new StringTokenizer(param1JsonReader.nextString(), "_");
          param1JsonReader = null;
          String str2 = null;
          String str3 = null;
          if (stringTokenizer.hasMoreElements())
            str1 = stringTokenizer.nextToken(); 
          if (stringTokenizer.hasMoreElements())
            str2 = stringTokenizer.nextToken(); 
          if (stringTokenizer.hasMoreElements())
            str3 = stringTokenizer.nextToken(); 
          return (str2 == null && str3 == null) ? new Locale(str1) : ((str3 == null) ? new Locale(str1, str2) : new Locale(str1, str2, str3));
        }
        
        public void write(JsonWriter param1JsonWriter, Locale param1Locale) throws IOException {
          String str;
          if (param1Locale == null) {
            param1Locale = null;
          } else {
            str = param1Locale.toString();
          } 
          param1JsonWriter.value(str);
        }
      };
    LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
    JSON_ELEMENT = new TypeAdapter<JsonElement>() {
        public JsonElement read(JsonReader param1JsonReader) throws IOException {
          // Byte code:
          //   0: getstatic com/google/gson/internal/bind/TypeAdapters$32.$SwitchMap$com$google$gson$stream$JsonToken : [I
          //   3: aload_1
          //   4: invokevirtual peek : ()Lcom/google/gson/stream/JsonToken;
          //   7: invokevirtual ordinal : ()I
          //   10: iaload
          //   11: tableswitch default -> 48, 1 -> 70, 2 -> 92, 3 -> 56, 4 -> 110, 5 -> 121, 6 -> 161
          //   48: new java/lang/IllegalArgumentException
          //   51: dup
          //   52: invokespecial <init> : ()V
          //   55: athrow
          //   56: new com/google/gson/JsonPrimitive
          //   59: dup
          //   60: aload_1
          //   61: invokevirtual nextString : ()Ljava/lang/String;
          //   64: invokespecial <init> : (Ljava/lang/String;)V
          //   67: astore_1
          //   68: aload_1
          //   69: areturn
          //   70: new com/google/gson/JsonPrimitive
          //   73: dup
          //   74: new com/google/gson/internal/LazilyParsedNumber
          //   77: dup
          //   78: aload_1
          //   79: invokevirtual nextString : ()Ljava/lang/String;
          //   82: invokespecial <init> : (Ljava/lang/String;)V
          //   85: invokespecial <init> : (Ljava/lang/Number;)V
          //   88: astore_1
          //   89: goto -> 68
          //   92: new com/google/gson/JsonPrimitive
          //   95: dup
          //   96: aload_1
          //   97: invokevirtual nextBoolean : ()Z
          //   100: invokestatic valueOf : (Z)Ljava/lang/Boolean;
          //   103: invokespecial <init> : (Ljava/lang/Boolean;)V
          //   106: astore_1
          //   107: goto -> 68
          //   110: aload_1
          //   111: invokevirtual nextNull : ()V
          //   114: getstatic com/google/gson/JsonNull.INSTANCE : Lcom/google/gson/JsonNull;
          //   117: astore_1
          //   118: goto -> 68
          //   121: new com/google/gson/JsonArray
          //   124: dup
          //   125: invokespecial <init> : ()V
          //   128: astore_2
          //   129: aload_1
          //   130: invokevirtual beginArray : ()V
          //   133: aload_1
          //   134: invokevirtual hasNext : ()Z
          //   137: ifeq -> 152
          //   140: aload_2
          //   141: aload_0
          //   142: aload_1
          //   143: invokevirtual read : (Lcom/google/gson/stream/JsonReader;)Lcom/google/gson/JsonElement;
          //   146: invokevirtual add : (Lcom/google/gson/JsonElement;)V
          //   149: goto -> 133
          //   152: aload_1
          //   153: invokevirtual endArray : ()V
          //   156: aload_2
          //   157: astore_1
          //   158: goto -> 68
          //   161: new com/google/gson/JsonObject
          //   164: dup
          //   165: invokespecial <init> : ()V
          //   168: astore_2
          //   169: aload_1
          //   170: invokevirtual beginObject : ()V
          //   173: aload_1
          //   174: invokevirtual hasNext : ()Z
          //   177: ifeq -> 196
          //   180: aload_2
          //   181: aload_1
          //   182: invokevirtual nextName : ()Ljava/lang/String;
          //   185: aload_0
          //   186: aload_1
          //   187: invokevirtual read : (Lcom/google/gson/stream/JsonReader;)Lcom/google/gson/JsonElement;
          //   190: invokevirtual add : (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
          //   193: goto -> 173
          //   196: aload_1
          //   197: invokevirtual endObject : ()V
          //   200: aload_2
          //   201: astore_1
          //   202: goto -> 68
        }
        
        public void write(JsonWriter param1JsonWriter, JsonElement param1JsonElement) throws IOException {
          JsonPrimitive jsonPrimitive;
          Iterator<JsonElement> iterator;
          if (param1JsonElement == null || param1JsonElement.isJsonNull()) {
            param1JsonWriter.nullValue();
            return;
          } 
          if (param1JsonElement.isJsonPrimitive()) {
            jsonPrimitive = param1JsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
              param1JsonWriter.value(jsonPrimitive.getAsNumber());
              return;
            } 
            if (jsonPrimitive.isBoolean()) {
              param1JsonWriter.value(jsonPrimitive.getAsBoolean());
              return;
            } 
            param1JsonWriter.value(jsonPrimitive.getAsString());
            return;
          } 
          if (jsonPrimitive.isJsonArray()) {
            param1JsonWriter.beginArray();
            iterator = jsonPrimitive.getAsJsonArray().iterator();
            while (iterator.hasNext())
              write(param1JsonWriter, iterator.next()); 
            param1JsonWriter.endArray();
            return;
          } 
          if (iterator.isJsonObject()) {
            param1JsonWriter.beginObject();
            for (Map.Entry entry : iterator.getAsJsonObject().entrySet()) {
              param1JsonWriter.name((String)entry.getKey());
              write(param1JsonWriter, (JsonElement)entry.getValue());
            } 
            param1JsonWriter.endObject();
            return;
          } 
          throw new IllegalArgumentException("Couldn't write " + entry.getClass());
        }
      };
    JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
    ENUM_FACTORY = newEnumTypeHierarchyFactory();
  }
  
  public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          Class<?> clazz2 = param1TypeToken.getRawType();
          if (!Enum.class.isAssignableFrom(clazz2) || clazz2 == Enum.class)
            return null; 
          Class<?> clazz1 = clazz2;
          if (!clazz2.isEnum())
            clazz1 = clazz2.getSuperclass(); 
          return new TypeAdapters.EnumTypeAdapter(clazz1);
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          return param1TypeToken.equals(type) ? typeAdapter : null;
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          return (param1TypeToken.getRawType() == type) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          null = param1TypeToken.getRawType();
          return (null == unboxed || null == boxed) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          null = param1TypeToken.getRawType();
          return (null == base || null == sub) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
          return clazz.isAssignableFrom(param1TypeToken.getRawType()) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<T, String> constantToName = new HashMap<T, String>();
    
    private final Map<String, T> nameToConstant = new HashMap<String, T>();
    
    public EnumTypeAdapter(Class<T> param1Class) {
      try {
        for (Enum enum_ : (Enum[])param1Class.getEnumConstants()) {
          String str = enum_.name();
          SerializedName serializedName = param1Class.getField(str).<SerializedName>getAnnotation(SerializedName.class);
          if (serializedName != null)
            str = serializedName.value(); 
          this.nameToConstant.put(str, (T)enum_);
          this.constantToName.put((T)enum_, str);
        } 
      } catch (NoSuchFieldException noSuchFieldException) {
        throw new AssertionError();
      } 
    }
    
    public T read(JsonReader param1JsonReader) throws IOException {
      if (param1JsonReader.peek() == JsonToken.NULL) {
        param1JsonReader.nextNull();
        return null;
      } 
      return this.nameToConstant.get(param1JsonReader.nextString());
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      String str;
      if (param1T == null) {
        param1T = null;
      } else {
        str = this.constantToName.get(param1T);
      } 
      param1JsonWriter.value(str);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/bind/TypeAdapters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */