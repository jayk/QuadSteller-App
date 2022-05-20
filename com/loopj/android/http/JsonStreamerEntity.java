package com.loopj.android.http;

import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonStreamerEntity implements HttpEntity {
  private static final int BUFFER_SIZE = 4096;
  
  private static final StringBuilder BUILDER;
  
  private static final UnsupportedOperationException ERR_UNSUPPORTED = new UnsupportedOperationException("Unsupported operation in this implementation.");
  
  private static final Header HEADER_GZIP_ENCODING;
  
  private static final Header HEADER_JSON_CONTENT;
  
  private static final byte[] JSON_FALSE;
  
  private static final byte[] JSON_NULL;
  
  private static final byte[] JSON_TRUE;
  
  private static final String LOG_TAG = "JsonStreamerEntity";
  
  private static final byte[] STREAM_CONTENTS;
  
  private static final byte[] STREAM_ELAPSED;
  
  private static final byte[] STREAM_NAME;
  
  private static final byte[] STREAM_TYPE;
  
  private final byte[] buffer = new byte[4096];
  
  private final Header contentEncoding;
  
  private final Map<String, Object> jsonParams = new HashMap<String, Object>();
  
  private final ResponseHandlerInterface progressHandler;
  
  static {
    BUILDER = new StringBuilder(128);
    JSON_TRUE = "true".getBytes();
    JSON_FALSE = "false".getBytes();
    JSON_NULL = "null".getBytes();
    STREAM_NAME = escape("name");
    STREAM_TYPE = escape("type");
    STREAM_CONTENTS = escape("contents");
    STREAM_ELAPSED = escape("_elapsed");
    HEADER_JSON_CONTENT = (Header)new BasicHeader("Content-Type", "application/json");
    HEADER_GZIP_ENCODING = (Header)new BasicHeader("Content-Encoding", "gzip");
  }
  
  public JsonStreamerEntity(ResponseHandlerInterface paramResponseHandlerInterface, boolean paramBoolean) {
    this.progressHandler = paramResponseHandlerInterface;
    if (paramBoolean) {
      Header header = HEADER_GZIP_ENCODING;
    } else {
      paramResponseHandlerInterface = null;
    } 
    this.contentEncoding = (Header)paramResponseHandlerInterface;
  }
  
  private void endMetaData(OutputStream paramOutputStream) throws IOException {
    paramOutputStream.write(34);
  }
  
  static byte[] escape(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 10
    //   4: getstatic com/loopj/android/http/JsonStreamerEntity.JSON_NULL : [B
    //   7: astore_0
    //   8: aload_0
    //   9: areturn
    //   10: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   13: bipush #34
    //   15: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload_0
    //   20: invokevirtual length : ()I
    //   23: istore_1
    //   24: iconst_m1
    //   25: istore_2
    //   26: iload_2
    //   27: iconst_1
    //   28: iadd
    //   29: istore_3
    //   30: iload_3
    //   31: iload_1
    //   32: if_icmpge -> 335
    //   35: aload_0
    //   36: iload_3
    //   37: invokevirtual charAt : (I)C
    //   40: istore #4
    //   42: iload #4
    //   44: lookupswitch default -> 112, 8 -> 231, 9 -> 287, 10 -> 259, 12 -> 245, 13 -> 273, 34 -> 203, 92 -> 217
    //   112: iload #4
    //   114: iflt -> 124
    //   117: iload #4
    //   119: bipush #31
    //   121: if_icmple -> 155
    //   124: iload #4
    //   126: bipush #127
    //   128: if_icmplt -> 139
    //   131: iload #4
    //   133: sipush #159
    //   136: if_icmple -> 155
    //   139: iload #4
    //   141: sipush #8192
    //   144: if_icmplt -> 321
    //   147: iload #4
    //   149: sipush #8447
    //   152: if_icmpgt -> 321
    //   155: iload #4
    //   157: invokestatic toHexString : (I)Ljava/lang/String;
    //   160: astore #5
    //   162: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   165: ldc '\u'
    //   167: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: aload #5
    //   173: invokevirtual length : ()I
    //   176: istore #6
    //   178: iconst_0
    //   179: istore_2
    //   180: iload_2
    //   181: iconst_4
    //   182: iload #6
    //   184: isub
    //   185: if_icmpge -> 301
    //   188: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   191: bipush #48
    //   193: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: iinc #2, 1
    //   200: goto -> 180
    //   203: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   206: ldc '\"'
    //   208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: pop
    //   212: iload_3
    //   213: istore_2
    //   214: goto -> 26
    //   217: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   220: ldc '\\'
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: iload_3
    //   227: istore_2
    //   228: goto -> 26
    //   231: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   234: ldc '\b'
    //   236: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   239: pop
    //   240: iload_3
    //   241: istore_2
    //   242: goto -> 26
    //   245: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   248: ldc '\f'
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: iload_3
    //   255: istore_2
    //   256: goto -> 26
    //   259: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   262: ldc '\n'
    //   264: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: pop
    //   268: iload_3
    //   269: istore_2
    //   270: goto -> 26
    //   273: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   276: ldc '\r'
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: iload_3
    //   283: istore_2
    //   284: goto -> 26
    //   287: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   290: ldc '\t'
    //   292: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: iload_3
    //   297: istore_2
    //   298: goto -> 26
    //   301: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   304: aload #5
    //   306: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   309: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   312: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: iload_3
    //   317: istore_2
    //   318: goto -> 26
    //   321: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   324: iload #4
    //   326: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   329: pop
    //   330: iload_3
    //   331: istore_2
    //   332: goto -> 26
    //   335: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   338: bipush #34
    //   340: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   343: pop
    //   344: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   347: invokevirtual toString : ()Ljava/lang/String;
    //   350: invokevirtual getBytes : ()[B
    //   353: astore_0
    //   354: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   357: iconst_0
    //   358: invokevirtual setLength : (I)V
    //   361: goto -> 8
    //   364: astore_0
    //   365: getstatic com/loopj/android/http/JsonStreamerEntity.BUILDER : Ljava/lang/StringBuilder;
    //   368: iconst_0
    //   369: invokevirtual setLength : (I)V
    //   372: aload_0
    //   373: athrow
    // Exception table:
    //   from	to	target	type
    //   344	354	364	finally
  }
  
  private void writeMetaData(OutputStream paramOutputStream, String paramString1, String paramString2) throws IOException {
    paramOutputStream.write(STREAM_NAME);
    paramOutputStream.write(58);
    paramOutputStream.write(escape(paramString1));
    paramOutputStream.write(44);
    paramOutputStream.write(STREAM_TYPE);
    paramOutputStream.write(58);
    paramOutputStream.write(escape(paramString2));
    paramOutputStream.write(44);
    paramOutputStream.write(STREAM_CONTENTS);
    paramOutputStream.write(58);
    paramOutputStream.write(34);
  }
  
  private void writeToFromFile(OutputStream paramOutputStream, RequestParams.FileWrapper paramFileWrapper) throws IOException {
    writeMetaData(paramOutputStream, paramFileWrapper.file.getName(), paramFileWrapper.contentType);
    int i = 0;
    int j = (int)paramFileWrapper.file.length();
    FileInputStream fileInputStream = new FileInputStream(paramFileWrapper.file);
    Base64OutputStream base64OutputStream = new Base64OutputStream(paramOutputStream, 18);
    while (true) {
      int k = fileInputStream.read(this.buffer);
      if (k != -1) {
        base64OutputStream.write(this.buffer, 0, k);
        i += k;
        this.progressHandler.sendProgressMessage(i, j);
        continue;
      } 
      AsyncHttpClient.silentCloseOutputStream(base64OutputStream);
      endMetaData(paramOutputStream);
      AsyncHttpClient.silentCloseInputStream(fileInputStream);
      return;
    } 
  }
  
  private void writeToFromStream(OutputStream paramOutputStream, RequestParams.StreamWrapper paramStreamWrapper) throws IOException {
    writeMetaData(paramOutputStream, paramStreamWrapper.name, paramStreamWrapper.contentType);
    Base64OutputStream base64OutputStream = new Base64OutputStream(paramOutputStream, 18);
    while (true) {
      int i = paramStreamWrapper.inputStream.read(this.buffer);
      if (i != -1) {
        base64OutputStream.write(this.buffer, 0, i);
        continue;
      } 
      AsyncHttpClient.silentCloseOutputStream(base64OutputStream);
      endMetaData(paramOutputStream);
      if (paramStreamWrapper.autoClose)
        AsyncHttpClient.silentCloseInputStream(paramStreamWrapper.inputStream); 
      return;
    } 
  }
  
  public void addPart(String paramString, Object paramObject) {
    this.jsonParams.put(paramString, paramObject);
  }
  
  public void consumeContent() throws IOException, UnsupportedOperationException {}
  
  public InputStream getContent() throws IOException, UnsupportedOperationException {
    throw ERR_UNSUPPORTED;
  }
  
  public Header getContentEncoding() {
    return this.contentEncoding;
  }
  
  public long getContentLength() {
    return -1L;
  }
  
  public Header getContentType() {
    return HEADER_JSON_CONTENT;
  }
  
  public boolean isChunked() {
    return false;
  }
  
  public boolean isRepeatable() {
    return false;
  }
  
  public boolean isStreaming() {
    return false;
  }
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream == null)
      throw new IllegalStateException("Output stream cannot be null."); 
    long l = System.currentTimeMillis();
    if (this.contentEncoding != null)
      paramOutputStream = new GZIPOutputStream(paramOutputStream, 4096); 
    paramOutputStream.write(123);
    for (String str : this.jsonParams.keySet()) {
      Object object = this.jsonParams.get(str);
      if (object != null) {
        paramOutputStream.write(escape(str));
        paramOutputStream.write(58);
        boolean bool = object instanceof RequestParams.FileWrapper;
        if (bool || object instanceof RequestParams.StreamWrapper) {
          paramOutputStream.write(123);
          if (bool) {
            writeToFromFile(paramOutputStream, (RequestParams.FileWrapper)object);
          } else {
            writeToFromStream(paramOutputStream, (RequestParams.StreamWrapper)object);
          } 
          paramOutputStream.write(125);
        } else if (object instanceof JsonValueInterface) {
          paramOutputStream.write(((JsonValueInterface)object).getEscapedJsonValue());
        } else if (object instanceof JSONObject) {
          paramOutputStream.write(((JSONObject)object).toString().getBytes());
        } else if (object instanceof JSONArray) {
          paramOutputStream.write(((JSONArray)object).toString().getBytes());
        } else if (object instanceof Boolean) {
          if (((Boolean)object).booleanValue()) {
            object = JSON_TRUE;
          } else {
            object = JSON_FALSE;
          } 
          paramOutputStream.write((byte[])object);
        } else if (object instanceof Long) {
          paramOutputStream.write((((Number)object).longValue() + "").getBytes());
        } else if (object instanceof Double) {
          paramOutputStream.write((((Number)object).doubleValue() + "").getBytes());
        } else if (object instanceof Float) {
          paramOutputStream.write((((Number)object).floatValue() + "").getBytes());
        } else if (object instanceof Integer) {
          paramOutputStream.write((((Number)object).intValue() + "").getBytes());
        } else {
          paramOutputStream.write(escape(object.toString()));
        } 
        paramOutputStream.write(44);
      } 
    } 
    paramOutputStream.write(STREAM_ELAPSED);
    paramOutputStream.write(58);
    l = System.currentTimeMillis() - l;
    paramOutputStream.write((l + "}").getBytes());
    Log.i("JsonStreamerEntity", "Uploaded JSON in " + Math.floor((l / 1000L)) + " seconds");
    paramOutputStream.flush();
    AsyncHttpClient.silentCloseOutputStream(paramOutputStream);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/JsonStreamerEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */