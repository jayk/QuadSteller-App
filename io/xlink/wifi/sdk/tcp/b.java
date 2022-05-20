package io.xlink.wifi.sdk.tcp;

import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.util.MyLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class b {
  public static int a = 0;
  
  private Thread b;
  
  private XlinkTcpService c;
  
  private InputStream d;
  
  private boolean e;
  
  public b(XlinkTcpService paramXlinkTcpService, InputStream paramInputStream) {
    this.c = paramXlinkTcpService;
    a(paramInputStream);
  }
  
  private void a(String paramString) {
    MyLog.e("TCPreader", paramString);
  }
  
  private void a(Thread paramThread) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: astore_2
    //   4: aload_2
    //   5: invokespecial <init> : ()V
    //   8: aload_0
    //   9: aload_2
    //   10: ldc 'read thread  isdone: '
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: aload_0
    //   16: getfield e : Z
    //   19: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   22: invokevirtual toString : ()Ljava/lang/String;
    //   25: invokespecial a : (Ljava/lang/String;)V
    //   28: aload_0
    //   29: getfield e : Z
    //   32: ifne -> 94
    //   35: aload_1
    //   36: aload_0
    //   37: getfield b : Ljava/lang/Thread;
    //   40: if_acmpne -> 94
    //   43: aload_0
    //   44: getfield d : Ljava/io/InputStream;
    //   47: ifnull -> 94
    //   50: iconst_5
    //   51: newarray byte
    //   53: astore_3
    //   54: aload_0
    //   55: getfield d : Ljava/io/InputStream;
    //   58: aload_3
    //   59: iconst_0
    //   60: iconst_5
    //   61: invokevirtual read : ([BII)I
    //   64: ifge -> 172
    //   67: aload_0
    //   68: ldc 'tcp read ret <0  break'
    //   70: invokespecial a : (Ljava/lang/String;)V
    //   73: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
    //   76: getfield a : Z
    //   79: ifeq -> 83
    //   82: return
    //   83: aload_0
    //   84: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   87: iconst_1
    //   88: bipush #-4
    //   90: iconst_0
    //   91: invokevirtual a : (ZIZ)V
    //   94: aload_0
    //   95: ldc 'read Thread break logout...'
    //   97: invokespecial a : (Ljava/lang/String;)V
    //   100: aload_0
    //   101: getfield e : Z
    //   104: ifne -> 82
    //   107: aload_0
    //   108: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   111: iconst_1
    //   112: iconst_m1
    //   113: iconst_1
    //   114: invokevirtual a : (ZIZ)V
    //   117: goto -> 82
    //   120: astore_1
    //   121: aload_0
    //   122: new java/lang/StringBuilder
    //   125: dup
    //   126: invokespecial <init> : ()V
    //   129: ldc 'tcp socket read  error:'
    //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: aload_1
    //   135: invokevirtual getMessage : ()Ljava/lang/String;
    //   138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: invokevirtual toString : ()Ljava/lang/String;
    //   144: invokespecial a : (Ljava/lang/String;)V
    //   147: aload_0
    //   148: getfield e : Z
    //   151: ifne -> 82
    //   154: aload_0
    //   155: iconst_1
    //   156: putfield e : Z
    //   159: aload_0
    //   160: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   163: iconst_1
    //   164: iconst_m1
    //   165: iconst_1
    //   166: invokevirtual a : (ZIZ)V
    //   169: goto -> 82
    //   172: getstatic io/xlink/wifi/sdk/global/a.i : I
    //   175: iconst_3
    //   176: if_icmpeq -> 186
    //   179: getstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
    //   182: iconst_3
    //   183: if_icmpne -> 264
    //   186: aload_3
    //   187: invokestatic c : ([B)Ljava/lang/String;
    //   190: astore_2
    //   191: aload_2
    //   192: ldc 'HTTP'
    //   194: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   197: ifne -> 209
    //   200: aload_2
    //   201: ldc 'http'
    //   203: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   206: ifeq -> 264
    //   209: aload_0
    //   210: getfield d : Ljava/io/InputStream;
    //   213: invokevirtual available : ()I
    //   216: istore #4
    //   218: aload_0
    //   219: getfield d : Ljava/io/InputStream;
    //   222: iload #4
    //   224: i2l
    //   225: invokestatic a : (Ljava/io/InputStream;J)[B
    //   228: pop
    //   229: sipush #999
    //   232: invokestatic a : (I)Lio/xlink/wifi/sdk/encoder/g;
    //   235: astore_3
    //   236: aload_3
    //   237: ifnull -> 28
    //   240: aload_3
    //   241: invokevirtual c : ()V
    //   244: new io/xlink/wifi/sdk/encoder/e
    //   247: astore_2
    //   248: aload_2
    //   249: iconst_0
    //   250: invokespecial <init> : (I)V
    //   253: aload_3
    //   254: invokevirtual g : ()Lio/xlink/wifi/sdk/event/a;
    //   257: aload_2
    //   258: invokevirtual onResponse : (Lio/xlink/wifi/sdk/encoder/e;)V
    //   261: goto -> 28
    //   264: aload_3
    //   265: iconst_0
    //   266: baload
    //   267: invokestatic a : (B)I
    //   270: istore #4
    //   272: iload #4
    //   274: ifle -> 487
    //   277: iload #4
    //   279: bipush #16
    //   281: if_icmpge -> 487
    //   284: iconst_0
    //   285: putstatic io/xlink/wifi/sdk/tcp/b.a : I
    //   288: new io/xlink/wifi/sdk/decoder/b
    //   291: astore_2
    //   292: aload_2
    //   293: aload_3
    //   294: invokespecial <init> : ([B)V
    //   297: aload_2
    //   298: invokevirtual a : ()I
    //   301: sipush #10240
    //   304: if_icmpge -> 371
    //   307: aload_2
    //   308: invokevirtual a : ()I
    //   311: ifle -> 371
    //   314: aload_2
    //   315: invokevirtual a : ()I
    //   318: newarray byte
    //   320: astore #5
    //   322: aload_0
    //   323: getfield d : Ljava/io/InputStream;
    //   326: aload #5
    //   328: iconst_0
    //   329: aload_2
    //   330: invokevirtual a : ()I
    //   333: invokevirtual read : ([BII)I
    //   336: pop
    //   337: new io/xlink/wifi/sdk/buffer/a
    //   340: astore_3
    //   341: aload_3
    //   342: aload #5
    //   344: iconst_0
    //   345: invokespecial <init> : ([BI)V
    //   348: new io/xlink/wifi/sdk/decoder/a
    //   351: astore #5
    //   353: aload #5
    //   355: aload_3
    //   356: aload_2
    //   357: invokespecial <init> : (Lio/xlink/wifi/sdk/buffer/a;Lio/xlink/wifi/sdk/decoder/b;)V
    //   360: invokestatic a : ()Lio/xlink/wifi/sdk/decoder/c;
    //   363: aload #5
    //   365: invokevirtual a : (Lio/xlink/wifi/sdk/decoder/a;)V
    //   368: goto -> 28
    //   371: aload_2
    //   372: invokevirtual c : ()I
    //   375: bipush #13
    //   377: if_icmpne -> 439
    //   380: aload_2
    //   381: invokevirtual a : ()I
    //   384: ifne -> 439
    //   387: aload_0
    //   388: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   391: iconst_0
    //   392: putfield e : I
    //   395: aload_0
    //   396: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   399: invokestatic currentTimeMillis : ()J
    //   402: putfield f : J
    //   405: new java/lang/StringBuilder
    //   408: astore_2
    //   409: aload_2
    //   410: invokespecial <init> : ()V
    //   413: aload_0
    //   414: aload_2
    //   415: ldc 'Tcp refresh heartbeat time'
    //   417: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   420: aload_0
    //   421: getfield c : Lio/xlink/wifi/sdk/XlinkTcpService;
    //   424: getfield f : J
    //   427: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   430: invokevirtual toString : ()Ljava/lang/String;
    //   433: invokespecial a : (Ljava/lang/String;)V
    //   436: goto -> 28
    //   439: new java/lang/StringBuilder
    //   442: astore_3
    //   443: aload_3
    //   444: invokespecial <init> : ()V
    //   447: aload_0
    //   448: aload_3
    //   449: ldc 'read type ='
    //   451: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   454: aload_2
    //   455: invokevirtual c : ()I
    //   458: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   461: ldc ' But DataLength'
    //   463: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   466: aload_2
    //   467: invokevirtual a : ()I
    //   470: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   473: ldc ' error  !!untreated packet'
    //   475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   478: invokevirtual toString : ()Ljava/lang/String;
    //   481: invokespecial a : (Ljava/lang/String;)V
    //   484: goto -> 28
    //   487: iload #4
    //   489: ifne -> 28
    //   492: new java/lang/StringBuilder
    //   495: astore_2
    //   496: aload_2
    //   497: invokespecial <init> : ()V
    //   500: aload_0
    //   501: aload_2
    //   502: ldc 'read type Length is 0 count ='
    //   504: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   507: getstatic io/xlink/wifi/sdk/tcp/b.a : I
    //   510: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   513: invokevirtual toString : ()Ljava/lang/String;
    //   516: invokespecial a : (Ljava/lang/String;)V
    //   519: getstatic io/xlink/wifi/sdk/tcp/b.a : I
    //   522: iconst_1
    //   523: iadd
    //   524: putstatic io/xlink/wifi/sdk/tcp/b.a : I
    //   527: getstatic io/xlink/wifi/sdk/tcp/b.a : I
    //   530: istore #4
    //   532: iload #4
    //   534: bipush #100
    //   536: if_icmple -> 28
    //   539: goto -> 94
    // Exception table:
    //   from	to	target	type
    //   0	28	120	java/io/IOException
    //   28	82	120	java/io/IOException
    //   83	94	120	java/io/IOException
    //   94	117	120	java/io/IOException
    //   172	186	120	java/io/IOException
    //   186	209	120	java/io/IOException
    //   209	236	120	java/io/IOException
    //   240	261	120	java/io/IOException
    //   264	272	120	java/io/IOException
    //   284	368	120	java/io/IOException
    //   371	436	120	java/io/IOException
    //   439	484	120	java/io/IOException
    //   492	532	120	java/io/IOException
  }
  
  public static byte[] a(InputStream paramInputStream, long paramLong) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[1024];
    int i = 0;
    while (true) {
      if (i < paramLong) {
        int j = paramInputStream.read(arrayOfByte, 0, (int)Math.min(1024L, paramLong - i));
        if (j > 0) {
          i += j;
          byteArrayOutputStream.write(arrayOfByte, 0, j);
          continue;
        } 
      } 
      return byteArrayOutputStream.toByteArray();
    } 
  }
  
  public void a() {
    this.b.start();
  }
  
  public void a(InputStream paramInputStream) {
    this.d = paramInputStream;
    this.e = false;
    this.b = new Thread(this) {
        public void run() {
          b.a(this.a, this);
        }
      };
    this.b.setName("Tcp Packet Reader");
    this.b.setDaemon(true);
  }
  
  public void b() {
    this.e = true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/tcp/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */