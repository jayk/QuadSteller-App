package com.loopj.android.http;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SaxAsyncHttpResponseHandler<T extends DefaultHandler> extends AsyncHttpResponseHandler {
  private static final String LOG_TAG = "SaxAsyncHttpResponseHandler";
  
  private T handler = null;
  
  public SaxAsyncHttpResponseHandler(T paramT) {
    if (paramT == null)
      throw new Error("null instance of <T extends DefaultHandler> passed to constructor"); 
    this.handler = paramT;
  }
  
  protected byte[] getResponseData(HttpEntity paramHttpEntity) throws IOException {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 102
    //   4: aload_1
    //   5: invokeinterface getContent : ()Ljava/io/InputStream;
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_3
    //   13: aconst_null
    //   14: astore #4
    //   16: aconst_null
    //   17: astore #5
    //   19: aload_2
    //   20: ifnull -> 102
    //   23: aload #4
    //   25: astore_1
    //   26: invokestatic newInstance : ()Ljavax/xml/parsers/SAXParserFactory;
    //   29: invokevirtual newSAXParser : ()Ljavax/xml/parsers/SAXParser;
    //   32: invokevirtual getXMLReader : ()Lorg/xml/sax/XMLReader;
    //   35: astore #6
    //   37: aload #4
    //   39: astore_1
    //   40: aload #6
    //   42: aload_0
    //   43: getfield handler : Lorg/xml/sax/helpers/DefaultHandler;
    //   46: invokeinterface setContentHandler : (Lorg/xml/sax/ContentHandler;)V
    //   51: aload #4
    //   53: astore_1
    //   54: new java/io/InputStreamReader
    //   57: astore #7
    //   59: aload #4
    //   61: astore_1
    //   62: aload #7
    //   64: aload_2
    //   65: ldc 'UTF-8'
    //   67: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   70: new org/xml/sax/InputSource
    //   73: astore_1
    //   74: aload_1
    //   75: aload #7
    //   77: invokespecial <init> : (Ljava/io/Reader;)V
    //   80: aload #6
    //   82: aload_1
    //   83: invokeinterface parse : (Lorg/xml/sax/InputSource;)V
    //   88: aload_2
    //   89: invokestatic silentCloseInputStream : (Ljava/io/InputStream;)V
    //   92: aload #7
    //   94: ifnull -> 102
    //   97: aload #7
    //   99: invokevirtual close : ()V
    //   102: aconst_null
    //   103: areturn
    //   104: astore #4
    //   106: aload #5
    //   108: astore #7
    //   110: aload #7
    //   112: astore_1
    //   113: ldc 'SaxAsyncHttpResponseHandler'
    //   115: ldc 'getResponseData exception'
    //   117: aload #4
    //   119: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   122: pop
    //   123: aload_2
    //   124: invokestatic silentCloseInputStream : (Ljava/io/InputStream;)V
    //   127: aload #7
    //   129: ifnull -> 102
    //   132: aload #7
    //   134: invokevirtual close : ()V
    //   137: goto -> 102
    //   140: astore_1
    //   141: goto -> 102
    //   144: astore #4
    //   146: aload_3
    //   147: astore #7
    //   149: aload #7
    //   151: astore_1
    //   152: ldc 'SaxAsyncHttpResponseHandler'
    //   154: ldc 'getResponseData exception'
    //   156: aload #4
    //   158: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   161: pop
    //   162: aload_2
    //   163: invokestatic silentCloseInputStream : (Ljava/io/InputStream;)V
    //   166: aload #7
    //   168: ifnull -> 102
    //   171: aload #7
    //   173: invokevirtual close : ()V
    //   176: goto -> 102
    //   179: astore_1
    //   180: goto -> 102
    //   183: astore #7
    //   185: aload_1
    //   186: astore #4
    //   188: aload_2
    //   189: invokestatic silentCloseInputStream : (Ljava/io/InputStream;)V
    //   192: aload #4
    //   194: ifnull -> 202
    //   197: aload #4
    //   199: invokevirtual close : ()V
    //   202: aload #7
    //   204: athrow
    //   205: astore_1
    //   206: goto -> 102
    //   209: astore_1
    //   210: goto -> 202
    //   213: astore_1
    //   214: aload #7
    //   216: astore #4
    //   218: aload_1
    //   219: astore #7
    //   221: goto -> 188
    //   224: astore #4
    //   226: goto -> 149
    //   229: astore #4
    //   231: goto -> 110
    // Exception table:
    //   from	to	target	type
    //   26	37	104	org/xml/sax/SAXException
    //   26	37	144	javax/xml/parsers/ParserConfigurationException
    //   26	37	183	finally
    //   40	51	104	org/xml/sax/SAXException
    //   40	51	144	javax/xml/parsers/ParserConfigurationException
    //   40	51	183	finally
    //   54	59	104	org/xml/sax/SAXException
    //   54	59	144	javax/xml/parsers/ParserConfigurationException
    //   54	59	183	finally
    //   62	70	104	org/xml/sax/SAXException
    //   62	70	144	javax/xml/parsers/ParserConfigurationException
    //   62	70	183	finally
    //   70	88	229	org/xml/sax/SAXException
    //   70	88	224	javax/xml/parsers/ParserConfigurationException
    //   70	88	213	finally
    //   97	102	205	java/io/IOException
    //   113	123	183	finally
    //   132	137	140	java/io/IOException
    //   152	162	183	finally
    //   171	176	179	java/io/IOException
    //   197	202	209	java/io/IOException
  }
  
  public abstract void onFailure(int paramInt, Header[] paramArrayOfHeader, T paramT);
  
  public void onFailure(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte, Throwable paramThrowable) {
    onSuccess(paramInt, paramArrayOfHeader, this.handler);
  }
  
  public abstract void onSuccess(int paramInt, Header[] paramArrayOfHeader, T paramT);
  
  public void onSuccess(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte) {
    onSuccess(paramInt, paramArrayOfHeader, this.handler);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/SaxAsyncHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */